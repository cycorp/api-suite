package com.cyc.baseclient;

/*
 * #%L
 * File: CycClientOptions.java
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

import com.cyc.base.CycAccessOptions;
import com.cyc.base.CycApiException;
import com.cyc.base.CycConnectionException;
import com.cyc.base.cycobject.Fort;
import com.cyc.kb.config.DefaultContext;
import com.cyc.session.CycSessionConfiguration.DefaultSessionOptions;
import com.cyc.session.SessionApiException;
import com.cyc.session.SessionCommunicationException;
import com.cyc.session.SessionConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nwinant
 */
public class CycClientOptions implements CycAccessOptions {

  static final private Logger LOGGER = LoggerFactory.getLogger(CycClientOptions.class);
  final private CycClientSession session;
  private Fort cyclist;
  private Fort project;
  private DefaultContext defaultContext;
  private Boolean shouldTranscriptOperations;
  
  protected CycClientOptions(CycClientSession session) {
    this.session = session;
    LOGGER.debug("Created new CycClientOptions");
    this.reset();
  }
  
  /**
   * Sets the value of the default Cyclist, whose identity will be attached via #$myCreator bookkeeping
   * assertions to new KB entities created in this session.  Setting the current Cyclist (via {@link #setCurrentCyclist})
   * will override the default cyclist within that thread.
   *
   * @param cyclist the cyclist term
   */
  @Override
  public void setCyclist(Fort cyclist) {
    LOGGER.debug("Setting cyclist: ", cyclist);
    this.cyclist = cyclist;
  }
  
  /**
   * Sets the value of the default Cyclist for this CycClient, whose identity will be attached via #$myCreator bookkeeping
 assertions to new KB entities created in this session.  Setting the current Cyclist (via {@link #setCurrentCyclist} will
   * override this default Cyclist within that thread.
   *
   * @param cyclistName the name of the default cyclist term
   * 
   * @throws SessionCommunicationException if a communications error occurs or the Cyc server cannot be found.
   * @throws SessionConfigurationException if the cyclist cannot be set.
   */
  @Override
  public void setCyclistName(String cyclistName)
          throws SessionCommunicationException, SessionConfigurationException {
    setCyclist(convertStringToFort(cyclistName, "cyclist"));
  }
  
  /**
   * Resets the current cyclist value.  If the current CycSessionConfiguration has a default Cyclist, that cyclist will now become the current cyclist.
   */
  @Override
  public void resetCyclist() {
    LOGGER.debug("Resetting cyclist. Previous value: {}", this.cyclist);
    this.cyclist = null;
    final DefaultSessionOptions defaults = this.session.getConfiguration().getDefaultSessionOptions();
    if (defaults.getCyclistName() != null) {
      try {
        setCyclistName(defaults.getCyclistName());
      } catch (SessionApiException ex) {
        // TODO: should this be a checked exception, or a higher-level runtime exception?
        throw new RuntimeException(ex);
      }
    }
  }
  
  /**
   * Returns the value of the Cyclist.  Returns the value from {@link #setCurrentCyclist(com.cyc.base.cycobject.Fort) } if one has been set.
   * Otherwise, returns the default as set with {@link #setCyclist(com.cyc.base.cycobject.Fort)}.  If that also has not been set,
 return null.
   *
   * @return the value of the default Cyclist
   */
  @Override
  public Fort getCyclist() {
    //return (currentCyclist.get() != null) ? currentCyclist.get() : cyclist;
    return this.cyclist;
  }

  @Override
  public String getCyclistName() {
    return convertFort(getCyclist());
  }
  
  @Override
  public void setKePurpose(Fort project) {
    LOGGER.debug("Setting KE purpose: ", project);
    this.project = project;
  }
  
  /**
   * Sets the value of the KE purpose, whose project name will be attached via #$myCreationPurpose
   * bookkeeping assertions to new KB entities created in this session.
   *
   * @param projectName the string name of the KE Purpose term
   *
   * @throws SessionCommunicationException if a communications error occurs or the Cyc server cannot be found.
   * @throws SessionConfigurationException if the cyclist cannot be set.
   */
  @Override
  public void setKePurposeName(String projectName)
          throws SessionCommunicationException, SessionConfigurationException {
    setKePurpose(convertStringToFort(projectName, "project"));
  }
  
  @Override
  public void resetKePurpose() {
    LOGGER.debug("Resetting KE purpose. Previous value: {}", this.project);
    this.project = null;
    final DefaultSessionOptions defaults = this.session.getConfiguration().getDefaultSessionOptions();
    if (defaults.getKePurposeName() != null) {
      try {
        setKePurposeName(defaults.getKePurposeName());
      } catch (SessionApiException ex) {
        // TODO: should this be a checked exception, or a higher-level runtime exception?
        throw new RuntimeException(ex);
      }
    }
  }
  
  /**
   * Returns the value of the project (KE purpose).
   *
   * @return he value of the project (KE purpose)
   */
  @Override
  public Fort getKePurpose() {
    return this.project;
  }
  
  @Override
  public String getKePurposeName() {
    return convertFort(getKePurpose());
  }
  
  /**
   * Sets the default context ThreadLocal
   * @param defaultContext
   */
  @Override
  public void setDefaultContext(DefaultContext defaultContext) {
    LOGGER.debug("Setting DefaultContext: ", defaultContext);
    this.defaultContext = defaultContext;
  }
  
  @Override
  public void resetDefaultContext() {
    LOGGER.debug("Resetting defaultContext. Previous value: {}", this.defaultContext);
    final DefaultSessionOptions defaults = this.session.getConfiguration().getDefaultSessionOptions();
    setDefaultContext(defaults.getDefaultContext());
  }
  
  /**
   * Returns the current default contexts
   * @return the contents of the DefaultContest ThreadLocal
    */
  @Override
  public DefaultContext getDefaultContext() {
    return this.defaultContext;
  }
  
  /**
   * Declare that KB operations performed in this thread should or shouldn't be
   * transcripted by the Cyc server.
   * 
   * @param shouldTranscriptOperations flag to control the transcription of the operations
   */
  @Override
  public void setShouldTranscriptOperations(boolean shouldTranscriptOperations) {
    LOGGER.debug("Setting shouldTranscriptOperations: ", shouldTranscriptOperations);
    this.shouldTranscriptOperations = shouldTranscriptOperations;
  }
  
  @Override
  public void resetShouldTranscriptOperations() {
    LOGGER.debug("Resetting shouldTranscriptOperations. Previous value: {}", this.shouldTranscriptOperations);
    final DefaultSessionOptions defaults = this.session.getConfiguration().getDefaultSessionOptions();
    setShouldTranscriptOperations(defaults.getShouldTranscriptOperations());
  }
  
  /**
   * Will actions in the current thread that modify the KB be transcripted by
   * the Cyc server?
   *
   * @return will KB operations from the current thread be transcripted?
   */
  @Override
  public boolean getShouldTranscriptOperations() {
    return this.shouldTranscriptOperations;
  }
  
  /**
   * Clears all values, reverting to any defaults specified in 
   * {@link com.cyc.session.CycSessionConfiguration#getDefaultSessionOptions() }.
   */
  @Override
  public void reset() {
    final DefaultSessionOptions opts = this.session.getConfiguration().getDefaultSessionOptions();
    resetCyclist();
    resetKePurpose();
    resetDefaultContext();
    resetShouldTranscriptOperations();
  }
  
  
  // Private
  
  private Fort convertStringToFort(String fortName, String descriptionString) throws SessionCommunicationException, SessionConfigurationException {
    try {
      if (fortName == null || "".equals(fortName.trim())) {
        throw new SessionConfigurationException("Invalid " + descriptionString + " name specified.");
      }
      Object term = session.getAccess().getObjectTool().getHLCycTerm(fortName);
      Fort newFort;
      if (term instanceof Fort) {
        newFort = (Fort) term;
      } else {
        // see if it is a blank name
        newFort = session.getAccess().getLookupTool().find(fortName);
      }
      if (newFort == null) {
        throw new SessionConfigurationException(
                "Cannot interpret " + fortName + " as a " + descriptionString + ".");
      }
      return newFort;
    } catch (CycConnectionException cce) {
      throw new SessionCommunicationException(cce);
    } catch (CycApiException cae) {
      throw new SessionConfigurationException(cae);
    }
  }
  
  private String convertFort(Fort fort) {
    return (fort != null) ? fort.toString() : null;
  }
  
  // TODO: override equals & hashcode - nwinant, 2015-10-16
  
}
