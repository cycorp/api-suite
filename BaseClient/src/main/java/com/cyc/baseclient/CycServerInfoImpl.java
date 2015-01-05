 package com.cyc.baseclient;

/*
 * #%L
 * File: CycServerInfoImpl.java
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

import com.cyc.base.CycAccess;
import com.cyc.base.CycApiException;
import com.cyc.base.CycConnectionException;
import static com.cyc.baseclient.api.SubLAPIHelper.makeSubLStmt;
import com.cyc.session.CycServer;
import com.cyc.session.CycServerInfo;
import com.cyc.session.SessionCommandException;
import com.cyc.session.SessionCommunicationException;
import java.util.List;

/**
 * This class provides basic information about the state and location of the current
 * Cyc server.
 * 
 * @author nwinant
 */
public class CycServerInfoImpl implements CycServerInfo {

  // Fields
  
  public static final int MINIMUM_MAJOR_REVISION = 10;
  public static final int MINIMUM_MINOR_REVISION = 152303;
  final private CycAccess cyc;
  final private CycServer server;
  
  
  // Constructors
  
  public CycServerInfoImpl(CycAccess cyc) {
    this.cyc = cyc;
    if ((cyc != null) && !cyc.isClosed()) {
      this.server = cyc.getCycServer();
    } else {
      server = null;
    }
  }

  // Public
  
  @Override
  public CycServer getCycServer() {
    return this.server;
  }
  
  /**
   * Returns the browser URL for the Cyc image that this CycAccess is connected
   * to, as the URL would be seen from the machine where the CycAccess is
   * running. Note that if there is a firewall, port-forwarding or other
   * indirection between the browser and the machine where this CycSession is
   * situated, the URL returned may not be functional.
   */
  @Override
  public String getBrowserUrl() {
    final CycServer server = this.getCycServer();
    return "http://" + server.getResolvedHostName() + ":" + server.getHttpPort() + "/cgi-bin/cg?cb-start";
  }

  /**
   * Returns the Cyc revision string (akin to the build number).
   *
   * @return the Cyc revision string for the Cyc server.
   *
   * @throws SessionCommunicationException if a communications error occurs or the Cyc server cannot be found.
   * @throws SessionCommandException if an error occurs in issuing a command to the Cyc server.
   */
  @Override
  public String getCycRevisionString() throws SessionCommunicationException, SessionCommandException {
    try {
      return getCyc().converse().converseString(makeSubLStmt("cyc-revision-string"));
    } catch (CycConnectionException ex) {
      ex.throwAsSessionException();
    } catch (CycApiException ex) {
      ex.throwAsSessionException();
    }
    return null;
  }

  /**
   * Returns the KB version string (KBNum.OperationCount) for the Cyc server.
   *
   * @return the Cyc KB version string.
   *
   * @throws SessionCommunicationException if a communications error occurs or the Cyc server cannot be found.
   * @throws SessionCommandException if an error occurs in issuing a command to the Cyc server.
   */
  @Override
  public String getCycKBVersionString() throws SessionCommunicationException, SessionCommandException {
    try {
      return getCyc().converse().converseString(makeSubLStmt("kb-version-string"));
    } catch (CycConnectionException ex) {
      ex.throwAsSessionException();
    } catch (CycApiException ex) {
      ex.throwAsSessionException();
    }
    return null;
  }

  @Override
  public boolean isAPICompatible() throws SessionCommunicationException, SessionCommandException {
    try {
      //    return isExecutableAPICompatible() && isKBAPICompatible();
      return isExecutableAPICompatible();
    } catch (CycConnectionException ex) {
      ex.throwAsSessionException();
    } catch (CycApiException ex) {
      ex.throwAsSessionException();
    }
    return false;
  }
  
  @Override
  public boolean isOpenCyc() throws SessionCommunicationException {
    try {
      return this.getCyc().isOpenCyc();
    } catch (CycConnectionException ex) {
      ex.throwAsSessionException();
    }
    return false;
  }
  

  // Protected
  
  protected CycAccess getCyc() {
    return this.cyc;
  }

  
  // Internal
  
  private boolean isExecutableAPICompatible() throws CycConnectionException, CycApiException {
    final int cycMajorRevisionNumber = getCycMajorRevisionNumber();
    return cycMajorRevisionNumber > MINIMUM_MAJOR_REVISION
            || (cycMajorRevisionNumber == MINIMUM_MAJOR_REVISION
            && getCycMinorRevisionNumber() >= MINIMUM_MINOR_REVISION);
  }
  /*
  private boolean isKBAPICompatible() throws CycApiException, CycConnectionException {
    try {
      return Double.parseDouble(getCycKBVersionString()) >= MINIMUM_KB_VERSION;
    } catch (NumberFormatException nfe) {
      nfe.printStackTrace();
      return false;
    }
  }
  */
  
  private List<Integer> getCycRevisionNumbers()
          throws CycConnectionException, CycApiException {
    return getCyc().converse().converseList(makeSubLStmt("cyc-revision-numbers"));
  }

  private int getCycMajorRevisionNumber()
          throws CycConnectionException, CycApiException {
    return getCycRevisionNumbers().get(0);
  }

  private int getCycMinorRevisionNumber()
          throws CycConnectionException, CycApiException {
    return getCycRevisionNumbers().get(1);
  }
}
