package com.cyc.baseclient.connection;

/*
 * #%L
 * File: SublApiHelper.java
 * Project: Base Client
 * %%
 * Copyright (C) 2013 - 2016 Cycorp, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

//// Internal Imports
//// External Imports
import com.cyc.baseclient.exception.CycApiServerSideException;
import com.cyc.baseclient.exception.CycApiClosedConnectionException;
import com.cyc.base.exception.BaseClientRuntimeException;
import com.cyc.baseclient.CycObjectFactory;
import com.cyc.baseclient.DefaultSublWorker;
import com.cyc.baseclient.CycClient;
import com.cyc.base.exception.CycConnectionException;
import com.cyc.base.conn.Worker;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.cyc.baseclient.cycobject.CycConstantImpl;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.CycSymbol;
import com.cyc.baseclient.CommonConstants;
import com.cyc.baseclient.CycClientManager;
//import com.cyc.baseclient.cycobject.CycSymbolImpl;
import com.cyc.baseclient.cycobject.DefaultCycObject;
import com.cyc.session.CycServerAddress;

/** 
 * <P>SubLAPIHelper is designed to...
 *
 * @author tbrussea, Nov 4, 2010, 11:33:24 AM
 * @version $Id: SublApiHelper.java 163356 2016-01-04 20:55:47Z nwinant $
 */
public class SublApiHelper {

  private static String getAPIString(Object param) {
    return (param instanceof AsIsTerm) ? ((AsIsTerm) param).toString() : DefaultCycObject.stringApiValue(param);
  }

  //// Constructors
  
  public SublApiHelper(CycClient access, CycObject user) {
    if (access == null) {
      throw new BaseClientRuntimeException("Got invalid access: " + access);
    }
    this.access = access;
    this.user = user;
  }

  //// Public Area
  public synchronized void close() {
    if (madeCycAccess) {
      try {
        getCycAccess().close();
      } catch (Exception e) {
      } // ignore
    }
    setCycAccess(null);
  }

  public CycClient getCycAccess() {
    return access;
  }

  private synchronized void setCycAccess(CycClient access) {
    this.access = access;
    madeCycAccess = false;
  }
  
  private synchronized void recreateCycAccess() throws CycConnectionException {
    final CycServerAddress server = getCycAccess().getCycServer();
    setCycAccess(new CycClient(server));
    madeCycAccess = true;
  }
  
  private CycObject getUser() {
    return user;
  }

  public String getHost() {
    return getCycAccess().getHostName();
  }

  public int getPort() {
    return getCycAccess().getBasePort();
  }
  static public final CycSymbol THE_CYCLIST = CycObjectFactory.makeCycSymbol("*the-cyclist*");

  public String wrapCommandWithUser(String command) {
    return wrapVariableBinding(command, THE_CYCLIST, getUser());
  }

  public static String wrapVariableBinding(String command, CycSymbol variable, Object value) {
    try {
      // @todo consider setting *ke-purpose*
      return "(clet ((" + DefaultCycObject.cyclifyWithEscapeChars(variable, true) + " " + getAPIString(value) + ")) " + command + ")";
    } catch (Exception e) {
      return command;
    }
  }

  public Worker makeAsynchSubLWorker(String command) {
    return makeAsynchSubLWorker(command, getMaxTimeoutMSecs());
  }

  public Worker makeAsynchSubLWorker(String command, long timeoutMsecs) {
    command = wrapCommandWithUser(command);
    Worker worker = new DefaultSublWorker(command, getCycAccess(), timeoutMsecs);
    return worker;
  }

  public Object executeCommandSynchronously(String command) throws CycConnectionException {
    return executeCommandSynchronously(command, getMaxTimeoutMSecs());
  }

  public Object executeCommandSynchronously(String command, long timeoutMsecs) throws CycConnectionException {
    return executeCommandSynchronouslyInt(command, timeoutMsecs, 0);
  }

  public Object executeCommandSynchronouslyInt(String command, long timeoutMsecs, int currentTry)
          throws CycConnectionException {
    command = wrapCommandWithUser(command);
    synchronized (System.out) { // @hack for tomcat in netbeans issue
      logger.log(Level.INFO, "About to execute command: {0}\nto server: {1}:{2}",
              new Object[]{command, getCycAccess().getHostName(), getCycAccess().getBasePort()});
    }
    Object result = null;
    try {
      final SublWorkerSynch worker = new DefaultSublWorkerSynch(command, getCycAccess(), timeoutMsecs);
      result = worker.getWork();
    } catch (CycApiServerSideException csse) {
      throw new CycApiServerSideException(csse.getMessage());
    } catch (CycApiClosedConnectionException expt) {
      if (currentTry < 2) {
        recreateCycAccess();
        result = executeCommandSynchronouslyInt(command, timeoutMsecs, currentTry + 1);
      } else {
        throw expt;
      }
    }
    synchronized (System.out) { // @hack for tomcat in netbeans issue
      logger.log(Level.FINE, "Got result: {0}", DefaultCycObject.cyclify(result));
    }
    return result;
  }

  public void setMaxTimeoutMSecs(long maxTimeoutMSecs) {
    this.maxTimeoutMSecs = maxTimeoutMSecs;
  }

  public long getMaxTimeoutMSecs() {
    return maxTimeoutMSecs;
  }

  /** @return an executable SubL statement string applying function to params. */
  public static String makeSubLStmt(CycSymbol function, Object... params) {
    return makeSubLStmt(function.getSymbolName(), params);
  }

  /** @return an executable SubL statement string applying function to params. */
  public static String makeSubLStmt(String functionName, Object... params) {
    StringBuilder buf = new StringBuilder(2048);
    buf.append("(").append(functionName);
    for (Object param : params) {
      buf.append(" ").append(getAPIString(param));
    }
    buf.append(")");
    return buf.toString();
  }
  
  /** @return an executable SubL statement string applying function to params. */
  public static String makeSubLStmtWNartSubstitute(String functionName, Object... params) {
    StringBuilder buf = new StringBuilder(2048);
    buf.append("(").append(functionName);
    for (Object param : params) {
      buf.append(" (nart-substitute ").append(getAPIString(param)).append(")");
    }
    buf.append(")");
    return buf.toString();
  }

  /** @return a SubL statement applying function to params, suitable for using as an argument to {@link #makeSubLStmt(String, Object[])}. */
  public static AsIsTerm makeNestedSubLStmt(CycSymbol function, Object... params) {
    return makeNestedSubLStmt(function.getSymbolName(), params);
  }

  /** @return a SubL statement applying function to params, suitable for using as an argument to {@link #makeSubLStmt(String, Object[])}. */
  public static AsIsTerm makeNestedSubLStmt(String functionName, Object... params) {
    return new AsIsTerm(makeSubLStmt(functionName, params));
  }

  public static class AsIsTerm {

    public AsIsTerm(Object obj) {
      this.obj = obj;
    }

    @Override
    public String toString() {
      return "" + obj;
    }

    public Object getObj() {
      return obj;
    }
    protected Object obj;
  }

  public static final class QuotedAsIsTerm extends AsIsTerm {

    public QuotedAsIsTerm(Object obj) {
      super(obj);
    }

    @Override
    public String toString() {
      return "(quote " + obj + ")";
    }
  }

  public static final class QuotedTerm extends AsIsTerm {

    public QuotedTerm(Object obj) {
      super(obj);
    }

    @Override
    public String toString() {
      return "(quote " + DefaultCycObject.stringApiValue(obj) + ")";
    }
  }

  public static void setLoggingLevel(Level newLevel) {
    logger.setLevel(newLevel);
  }
  //// Protected Area
  //// Private Area
  //// Internal Rep
  private volatile CycClient access;
  private final CycObject user;
  private volatile boolean madeCycAccess = false;
  private volatile long maxTimeoutMSecs = DEFAULT_MAX_TIMEOUT_MSECS;
  public static long DEFAULT_MAX_TIMEOUT_MSECS = 10000;
  private static final Logger logger = Logger.getLogger(SublApiHelper.class.toString());
  public static final Level DEFAULT_LOGGING_LEVEL = Level.WARNING;

  static {
    logger.addHandler(new ConsoleHandler());
    logger.setUseParentHandlers(false);
    setLoggingLevel(DEFAULT_LOGGING_LEVEL);
  }

  //// Main
  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    logger.info("Starting");
    SublApiHelper thisObj = null;
    try {
      CycConstantImpl admin = CommonConstants.ADMINISTRATOR;
      thisObj = new SublApiHelper(CycClientManager.getCurrentClient(), admin);
      setLoggingLevel(Level.FINE);
      String command = thisObj.makeSubLStmt("asdlfksjd", 1, 2, 3, 4);
      Object result = thisObj.executeCommandSynchronously(command);
      command = thisObj.makeSubLStmt("list", new QuotedAsIsTerm("a"),
              new QuotedAsIsTerm("b"), new QuotedAsIsTerm("c"), admin);
      result = thisObj.executeCommandSynchronously(command);
    } catch (Exception e) {
      logger.log(Level.SEVERE, e.getMessage(), e);
    } finally {
      if (thisObj != null) {
        thisObj.close();
      }
      logger.info("Finished.");
      System.exit(0);
    }
  }
}
