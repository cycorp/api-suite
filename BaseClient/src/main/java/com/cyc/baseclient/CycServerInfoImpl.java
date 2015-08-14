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
import static com.cyc.baseclient.subl.functions.SubLFunctions.CYC_REVISION_NUMBERS;
import static com.cyc.baseclient.subl.functions.SubLFunctions.CYC_REVISION_STRING;
import static com.cyc.baseclient.subl.functions.SubLFunctions.CYC_SYSTEM_CODE_STRING;
import static com.cyc.baseclient.subl.functions.SubLFunctions.KB_VERSION_STRING;
import com.cyc.session.CycServerReleaseType;
import com.cyc.session.CycServer;
import com.cyc.session.CycServerInfo;
import com.cyc.session.SessionCommandException;
import com.cyc.session.SessionCommunicationException;
import com.cyc.session.compatibility.AbstractCycClientRequirement;
import com.cyc.session.compatibility.CycClientRequirementList;
import com.cyc.session.compatibility.MinimumPatchRequirement;
import com.cyc.session.compatibility.ServerFunctionRequirement;
import com.cyc.session.exception.SessionApiRuntimeException;
import com.cyc.session.exception.UnsupportedCycOperationException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides basic information about the state and location of the current
 * Cyc server.
 * 
 * @author nwinant
 */
public class CycServerInfoImpl implements CycServerInfo {

  // Fields
  
  /**
   * Functions which are so critical that API compatibility isn't even a possibility without them.
   * 
   * TODO: should these just be the set of required functions in SubLFunctions?
   */
  /*
  private static final List<SubLFunction> CRITICAL_FUNCTIONS = Arrays.<SubLFunction>asList(
          CYC_REVISION_NUMBERS,
          CYC_SYSTEM_CODE_STRING,
          CYC_REVISION_STRING,
          KB_VERSION_STRING
  );
  */
  private static final int REQUIRED_MAJOR_REVISION = 10;
  private static final int MINIMUM_MINOR_REVISION_MAINT = 152303;
  private static final int MINIMUM_MINOR_REVISION_RCYC = 152303;
  private static final int MINIMUM_MINOR_REVISION_ECYC = 157082;
  private static final int MINIMUM_MINOR_REVISION_OCYC = 158630;
  private static final String MINIMUM_RCYC_RELEASE = "4.0q";
  private static final String MINIMUM_ECYC_RELEASE = "1.7-preview";
  private static final String MINIMUM_OCYC_RELEASE = "5.0-preview";
  private static final String CYC_DOWNLOAD_URL = "http://dev.cyc.com/downloads/";
  
  private static final Logger LOGGER = LoggerFactory.getLogger(CycServerInfoImpl.class);
  
  /**
   * We currently have no way of knowing if this implementation will be compatible with systems
   * *later* than Cyc 10, but it doesn't seem likely. - nwinant, 2015-05-29
   */
  public static final AbstractCycClientRequirement MAJOR_REVISION_REQUIREMENT = new AbstractCycClientRequirement() {
    @Override
    public boolean isCompatible(CycClient client) throws CycApiException, CycConnectionException {
      final CycServerInfoImpl info = client.getServerInfo();
      return info.getCycMajorRevisionNumber() == REQUIRED_MAJOR_REVISION;
    }
    @Override
    public String getErrorMessage(CycClient client) throws UnsupportedCycOperationException, CycApiException, CycConnectionException {
      final int serverMajorRevisionNumber = client.getServerInfo().getCycMajorRevisionNumber();
      final CycServer server = client.getCycServer();
      return "This API implementation requires servers at System " + REQUIRED_MAJOR_REVISION + ", but " + server + " is at System " + serverMajorRevisionNumber + ".";
    }
    
  };
  
  public static final CycClientRequirementList BASELINE_API_REQUIREMENTS = CycClientRequirementList.fromList(
          new ServerFunctionRequirement(CYC_REVISION_NUMBERS),
          MAJOR_REVISION_REQUIREMENT,
          new ServerFunctionRequirement(CYC_SYSTEM_CODE_STRING),
          new ServerFunctionRequirement(CYC_REVISION_STRING),
          new ServerFunctionRequirement(KB_VERSION_STRING),
          new MinimumPatchRequirement(CycServerReleaseType.MAINT, MINIMUM_MINOR_REVISION_MAINT),
          new MinimumPatchRequirement(CycServerReleaseType.RESEARCHCYC, MINIMUM_MINOR_REVISION_RCYC),
          new MinimumPatchRequirement(CycServerReleaseType.ENTERPRISECYC, MINIMUM_MINOR_REVISION_ECYC),
          new MinimumPatchRequirement(CycServerReleaseType.OPENCYC, MINIMUM_MINOR_REVISION_OCYC)
  );
    
  final private CycAccess cyc;
  final private CycServer server;
  private boolean openCyc;
  private List<Integer> cycRevisionNumbers;
  
  
  // Constructors
  
  public CycServerInfoImpl(CycAccess cyc) {
    this.cyc = cyc;
    if ((cyc != null) && !cyc.isClosed()) {
      this.server = cyc.getCycServer();
    } else {
      server = null;
    }
    try {
      reloadCache();
    } catch (SessionCommunicationException ex) {
      throw new SessionApiRuntimeException(ex);
    }
  }
  

  // Public
  
  final public void reloadCache() throws SessionCommunicationException {
    try {
      
      // TODO: add more caching of server info values - nwinant, 2015-06-25
      
      this.openCyc = this.getCyc().isOpenCyc(); // TODO: this is already cached inside CycClient.
      this.cycRevisionNumbers = CYC_REVISION_NUMBERS.eval(getCyc());
    } catch (CycConnectionException ex) {
      ex.throwAsSessionException();
    }
  }
  
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
    return getBaseBrowserUrl() + "/cgi-bin/cg?cb-start";
  }
  
  public String getBaseBrowserUrl() {
    return "http://" + getCycServer().getResolvedHostName() + ":" + getCycServer().getHttpPort();
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
      return CYC_REVISION_STRING.eval(getCyc());
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
      return KB_VERSION_STRING.eval(getCyc());
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
    return this.openCyc;
  }
  
  @Override
  public CycServerReleaseType getSystemReleaseType() throws SessionCommunicationException, SessionCommandException {
    return CycServerReleaseType.fromString(getSystemReleaseTypeString());
  }
  
  public int getCycMajorRevisionNumber()
          throws CycConnectionException, CycApiException {
    return getCycRevisionNumbers().get(0);
  }
  
  @Override
  public int getCycMinorRevisionNumber() throws SessionCommunicationException, SessionCommandException {
    return getCycRevisionNumbers().get(1);
  }
  
  
  // Protected
  
  protected String getSystemReleaseTypeString() throws SessionCommunicationException, SessionCommandException {
    try {
      return CYC_SYSTEM_CODE_STRING.eval(getCyc());
    } catch (CycConnectionException ex) {
      ex.throwAsSessionException();
    } catch (CycApiException ex) {
      ex.throwAsSessionException();
    }
    return null;
  }
  
  protected CycAccess getCyc() {
    return this.cyc;
  }

  
  // Internal
  /*
  private int checkForMissingCriticalFunctions() throws CycApiException, CycConnectionException {
    int i = 0;
    for (SubLFunction function : CRITICAL_FUNCTIONS) {
      if (!function.isBound(getCyc())) {
        LOGGER.error("{} is missing critical function: {}", server, function);
        i++;
      }
    }
    return i;
  }
  */
    
  private boolean isExecutableAPICompatible() throws CycConnectionException, CycApiException, SessionCommunicationException, SessionCommandException {
    

    /*
    if (checkForMissingCriticalFunctions() > 0) {
      return false;
    }
    */
    /*
    // We currently have no way of knowing if this implementation will be compatible with systems
    // *later* than Cyc 10, but it doesn't seem likely. - nwinant, 2015-05-29
    final int serverMajorRevisionNumber = getCycMajorRevisionNumber();
    if (getCycMajorRevisionNumber() != REQUIRED_MAJOR_REVISION) {
      LOGGER.error("This API implementation requires servers at System {}, but {} is at System {}.", REQUIRED_MAJOR_REVISION, server, serverMajorRevisionNumber);
      return false;
    }
    */
    return BASELINE_API_REQUIREMENTS.isCompatible((CycClient) getCyc());
    
    
    // TODO: clean up this cruft - nwinant, 2015-06-25
    
    
    /*
    if (!BASELINE_API_REQUIREMENTS.isCompatible(getCyc().getCycSession())) {
      return false;
    }*/
    
    /*
    final int serverMinorRevisionNumber = getCycMinorRevisionNumber();
    final CycServerReleaseType type = getSystemReleaseType();
    final Integer minimumMinorRevisionNumber;
    final String minimumReleaseNumber;
    final String downloadUrl;
    switch (type) {
      case MAINT:
        minimumMinorRevisionNumber = MINIMUM_MINOR_REVISION_MAINT;
        minimumReleaseNumber = null;
        downloadUrl = null;
        break;
        
      case RESEARCHCYC:
        minimumMinorRevisionNumber = MINIMUM_MINOR_REVISION_RCYC;
        minimumReleaseNumber = MINIMUM_RCYC_RELEASE;
        downloadUrl = CYC_DOWNLOAD_URL;
        break;
        
      case ENTERPRISECYC:
        minimumMinorRevisionNumber = MINIMUM_MINOR_REVISION_ECYC;
        minimumReleaseNumber = MINIMUM_ECYC_RELEASE;
        downloadUrl = CYC_DOWNLOAD_URL;
        break;
        
      case OPENCYC:
        minimumMinorRevisionNumber = MINIMUM_MINOR_REVISION_OCYC;
        minimumReleaseNumber = MINIMUM_OCYC_RELEASE;
        downloadUrl = CYC_DOWNLOAD_URL;
        break;
        
      default:
        LOGGER.error("Unknown system type '{}' for {}, cannot determine compatibility.", getSystemReleaseTypeString(), server);
        return false;
    }
    
    if (serverMinorRevisionNumber < minimumMinorRevisionNumber) {
      LOGGER.error("{} servers must be at least System {}.{} for API compatibility, but {} is only at System {}.{}.",
              type.getName(),
              serverMajorRevisionNumber, minimumMinorRevisionNumber, 
              server, 
              serverMajorRevisionNumber, serverMinorRevisionNumber);
      if (minimumReleaseNumber != null) {
        LOGGER.error("The earliest release of {} supported by this API implementation is {} {}.", 
                type.getName(), 
                type.getName(), 
                minimumReleaseNumber);
      }
      if (downloadUrl != null) {
        LOGGER.error("To upgrade your {} server, visit {}.", type.getName(), downloadUrl);
      }
      return false;
    }
    return true;
    */
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
  
  private List<Integer> getCycRevisionNumbers() {
    return this.cycRevisionNumbers;
  }

}
