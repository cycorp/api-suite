package com.cyc.session;

/*
 * #%L
 * File: SessionOptions.java
 * Project: Session API
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

/**
 * A set of modifiable options, including the name of the cyclist
 * making assertions, and the project's KE purpose.
 * 
 * The session's configuration may provide defaults for these options, or 
 * possibly prevent certain options from being changed.
 * 
 * @author nwinant
 */
public interface SessionOptions {
  
  /**
   * Returns the value of the Cyclist. If it has not been set, return null.
   *
   * @return the value of the Cyclist
   */
  String getCyclistName();

  /**
   * Returns the value of the project (KE purpose).
   *
   * @return the value of the project (KE purpose)
   */
  String getKePurposeName();
  
  /**
   * Sets the value of the default Cyclist for this CycSession, whose identity 
   * will be attached via #$myCreator bookkeeping assertions to new KB entities
   * created in this session.
   *
   * @param cyclistName the name of the cyclist term
   *
   * @throws SessionCommunicationException if a communications error occurs or the Cyc server cannot be found.
   * @throws SessionConfigurationException if the cyclist cannot be set.
   */
  void setCyclistName(String cyclistName) throws SessionCommunicationException, SessionConfigurationException;
  
  /**
   * Clear the current cyclist value.  If the current CycSessionConfiguration has a default Cyclist, that cyclist will now become the current cyclist.
   */
  void clearCyclist();
  
  /**
   * Sets the value of the KE purpose, whose project name will be attached via #$myCreationPurpose
   * bookkeeping assertions to new KB entities created in this session.
   *
   * @param project the KE Purpose term
   * 
   * @throws SessionCommunicationException if a communications error occurs or the Cyc server cannot be found.
   * @throws SessionConfigurationException if the project cannot be set.
   */
  void setKePurposeName(String project) throws SessionCommunicationException, SessionConfigurationException;
  
  
  /**
   * Clears all values, reverting to any defaults specified in CycSessionConfiguration.
   */
  void clear();
}
