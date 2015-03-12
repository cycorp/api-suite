package com.cyc.baseclient.util;

/*
 * #%L
 * File: CycUtils.java
 * Project: Base Client
 * %%
 * Copyright (C) 2013 - 2015 Cycorp, Inc.
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

import com.cyc.base.BaseClientRuntimeException;
import com.cyc.base.CycAccess;
import com.cyc.base.CycConnectionException;
import java.util.Calendar;
import java.util.Date;
import com.cyc.base.CycApiException;
import com.cyc.baseclient.api.CycConnection;
import com.cyc.baseclient.api.CycIOException;
import com.cyc.baseclient.api.DefaultSubLWorkerSynch;
import com.cyc.baseclient.api.SubLWorkerSynch;
import com.cyc.base.cycobject.CycObject;
import com.cyc.baseclient.ui.CycWorker;
import com.cyc.base.CycTimeOutException;

/**
 * This is a placeholder class for general cyc utilities.
 * All methods in this class are static.
 * <p>
 * @deprecated There should be no such class as this. It should be broken up.
 * @author  tbrussea
 * @version $Id: CycUtils.java 155703 2015-01-05 23:15:30Z nwinant $
 */
@Deprecated
public class CycUtils {
    
  
  private static boolean useTiming = false;
  /*
   * Creates a new instance of CycUtils and hides it since no instances 
   * of this class need ever be made. All methods here are static. 
   */
  private CycUtils() {}
    
  /** 
   * Evaluates the given SubL expression given on the cyc image
 provided by the CycAccess object given. Really just a thin wrapper
 around "CycClient.converseObject()" because I found that
 to be a very non-intuitive method name.  Currently all
 exceptions are caught and stack traces printed to standard
 err. I expect that the API on this method may change in the near future
 to throw appropriate exceptions.
   * @param connection The CycAccess object to use for communications
 with the appropriate Cyc image.
   * @param subl The string that represents the SubL expression that
   * needs to be evaluated.
   * @return The value of evaluating the passed in subl expression or
   * null if an error occurred.
   * @deprecated use SubLWorker instead
   **/
  private static synchronized Object evalSubL(CycAccess connection, String subl) {
    Object result = null;
    try {
      if (CycConnection.inAWTEventThread()) {
        throw new BaseClientRuntimeException("Unable to communicate with Cyc from the AWT event dispatch thread.");
      }
      long resultMsecs = 0;
      if(useTiming) {
        resultMsecs = System.currentTimeMillis();
      }
      result = connection.converse().converseObject(subl);
      if(useTiming) {
        System.out.println("Finished call: " + subl);
        resultMsecs = System.currentTimeMillis() - resultMsecs;
        System.out.println("Call took: " + resultMsecs + " msecs.");
      }
    } catch (CycConnectionException e) {
      throw new CycIOException(e);
    }
    return result;
  }

  /** 
   * Evaluates the given SubL expression given on the cyc image
 provided by the CycAccess object given. 
   * @param connection The CycAccess object to use for communications
 with the appropriate Cyc image.
   * @param subl The string that represents the SubL expression that
   * needs to be evaluated.
   * @return The value of evaluating the passed in subl expression or
   * null if an error occurred.
   **/
  public static synchronized Object evalSubLWithWorker(final CycAccess connection, final String subl) 
    throws CycConnectionException, CycTimeOutException, CycApiException {
    final SubLWorkerSynch worker = new DefaultSubLWorkerSynch(subl, connection);
    return worker.getWork();
  }

  /** 
   * Resolve the value of the Symbol whose name is in the string
   * symbol.
   * @param connection The CycAccess object to use for communications
 with the appropriate Cyc image.
   * @param symbol The string that represents the Symbol that
   * whose value is requested
   * @return The value of the symbol or null if an error occurred.
   **/
  private static Object getSymbolValue(CycAccess connection, 
                                      String symbol) {
    Object result = null;
    result = evalSubL( connection, "(SYMBOL-VALUE (QUOTE " + symbol + "))");
    return result;
  }
  
  /**
   * Evalutes the given subl expression on the given Cyc image in the 
   * background. When the evaluation is complete the CycWorkerListener
   * passed to this method is notified via an event callback.
   * @param conn The CycAccess object to use for communications
 with the appropriate Cyc image.
   * @param subl The string that represents the SubL expression that
   * needs to be evaluated.
   * @param cwl The CycWorkerListener that should be notified of
   * the background tasks progress.
   * @return The CycWorker object that is doing the work. It will be
   * either already be started.
   * @see CycWorker
   * @see CycWorkerListener
   * @deprecated use SubLWorker instead
   */
  private static CycWorker evalSubLInBackground(final CycAccess conn,
					       final String subl,
					       final CycWorkerListener cwl) {
    CycWorker worker = new CycWorker() {
      public Object construct() throws Exception {
        return evalSubL(conn, subl); 
      }
    };
    if(cwl != null) { worker.addListener(cwl); }
    worker.start();
    return worker;
  }
  
  private static long SUBL_TIME_OFFSET;
  
  static {
    Calendar cal = Calendar.getInstance();
    cal.set(1900, Calendar.JANUARY, 1);
    long time = cal.getTime().getTime();
    cal.set(1970, Calendar.JANUARY, 1);
    SUBL_TIME_OFFSET = (cal.getTime().getTime() - time);
  }
  
  private static Date convertSubLTimeStampToDate(long timeStamp) {
    //@hack the (60*60*1000) is a complete hack and should be remved once
    //we can determine why out timestamps are off by 1 hour
    return new Date((long)(timeStamp * 1000) - SUBL_TIME_OFFSET + (60 * 60 * 1000));
  }
  
  
  /*
   * @param elem: A cyc Fort to get url for
   * @param cyc: CycAccess Object to talk to
   * 
   * @returns: the url that opens the page for elem in the cyc browser. 
   */
  @Deprecated  
  private static String getCBFormString(CycObject elem, CycAccess cyc) throws CycConnectionException {
      String command = "(cb-form-string '" + elem.cyclify() + " :cb)";
      return cyc.converse().converseString(command);
  }
}
