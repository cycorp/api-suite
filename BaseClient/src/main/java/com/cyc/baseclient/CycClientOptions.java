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
import com.cyc.session.CycSessionConfiguration;
import com.cyc.session.SessionCommunicationException;
import com.cyc.session.SessionConfigurationException;

/**
 *
 * @author nwinant
 */
public class CycClientOptions implements CycAccessOptions {

  final private CycClient cyc;
  private Fort cyclist;
  private Fort project;
  
  protected CycClientOptions(CycClient cyc) {
    this.cyc = cyc;
    this.clear();
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
    try {
      if (cyclistName == null || "".equals(cyclistName.trim())) {
        throw new SessionConfigurationException("Invalid cyclist name specified.");
      }
      Object term = cyc.getObjectTool().getHLCycTerm(cyclistName);
      Fort newCyclist;
      if (term instanceof Fort) {
        newCyclist = (Fort) term;
      } else {
        // see if it is a blank name
        newCyclist = cyc.getLookupTool().find(cyclistName);
      }
      if (newCyclist == null) {
        throw new SessionConfigurationException(
                "Cannot interpret " + cyclistName + " as a cyclist.");
      }
      setCyclist(newCyclist);
    } catch (CycConnectionException cce) {
      throw new SessionCommunicationException(cce);
    } catch (CycApiException cae) {
      throw new SessionConfigurationException(cae);
    }
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
    this.cyclist = cyclist;
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
    return cyclist;
  }

  @Override
  public String getCyclistName() {
    return convertFort(getCyclist());
  }
  
  /**
   * Clear the current cyclist value.  If the current CycSessionConfiguration has a default Cyclist, that cyclist will now become the current cyclist.
   */
  @Override
  public void clearCyclist() {
    this.cyclist = null;
  }
  
  /**
   * Returns the value of the project (KE purpose).
   *
   * @return he value of the project (KE purpose)
   */
  @Override
  public Fort getKePurpose() {
    return project;
  }
  
  @Override
  public String getKePurposeName() {
    return convertFort(getKePurpose());
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
    try {
      setKePurpose((Fort) cyc.getObjectTool().getHLCycTerm(projectName));
    } catch (CycConnectionException cce) {
      throw new SessionCommunicationException(cce);
    } catch (CycApiException cae) {
      throw new SessionConfigurationException(cae);
    }
  }

  @Override
  public void setKePurpose(Fort project) {
    this.project = project;
  }
  
  @Override
  public void clear() {
    this.cyclist = null;
    this.project = null;
    if (getConfig() != null) {
      // TODO: Populate defaults...
    }
  }
  

  // Private
  
  private CycSessionConfiguration getConfig() {
    return (this.cyc != null) ? this.cyc.getCycSession().getConfiguration() : null;
  }
  
  private String convertFort(Fort fort) {
    return (fort != null) ? fort.toString() : null;
  }

}
