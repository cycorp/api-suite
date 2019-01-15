package com.cyc.session;

/*
 * #%L
 * File: SessionOptions.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2019 Cycorp, Inc
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
import com.cyc.kb.DefaultContext;
import com.cyc.session.exception.SessionCommunicationException;
import com.cyc.session.exception.SessionConfigurationException;

/**
 * A set of modifiable options, such as the name of the cyclist making assertions and the project's
 * KE purpose.
 * <p>
 * The session's configuration may provide defaults for these options (typically via  
 * {@link CycSessionConfiguration#getDefaultSessionOptions()) or possibly prevent certain options
 * from being changed.
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
   * Sets the value of the default Cyclist for this CycSession, whose identity will be attached via
   * #$myCreator bookkeeping assertions to new KB entities created in this session.
   *
   * @param cyclistName the name of the cyclist term
   *
   * @throws SessionCommunicationException if a communications error occurs or the Cyc server cannot
   *                                       be found.
   * @throws SessionConfigurationException if the cyclist cannot be set.
   */
  void setCyclistName(String cyclistName)
          throws SessionCommunicationException, SessionConfigurationException;

  /**
   * Sets the value of the KE purpose, whose project name will be attached via #$myCreationPurpose
   * bookkeeping assertions to new KB entities created in this session.
   *
   * @param project the KE Purpose term
   *
   * @throws SessionCommunicationException if a communications error occurs or the Cyc server cannot
   *                                       be found.
   * @throws SessionConfigurationException if the project cannot be set.
   */
  void setKePurposeName(String project)
          throws SessionCommunicationException, SessionConfigurationException;

  /**
   * Declare that KB operations performed in this thread should or shouldn't be transcripted by the
   * Cyc server.
   *
   * @param shouldTranscriptOperations flag to control the transcription of the operations
   */
  void setShouldTranscriptOperations(boolean shouldTranscriptOperations);

  /**
   * Will actions in the current thread that modify the KB be transcripted by the Cyc server?
   *
   * @return will KB operations from the current thread be transcripted?
   */
  boolean getShouldTranscriptOperations();

  /**
   * Sets the default context ThreadLocal
   *
   * @param defaultContext
   */
  void setDefaultContext(DefaultContext defaultContext);

  /**
   * Returns the current default contexts
   *
   * @return the contents of the DefaultContest ThreadLocal
   */
  DefaultContext getDefaultContext();

  /**
   * Sets the ThreadLocal that controls whether or not to convert CycL dates to Java dates
   *
   * @param performConversion
   */
  void setShouldConvertToJavaDates(boolean performConversion);

  /**
   * Returns the ThreadLocal that controls whether or not to convert CycL dates to Java dates
   *
   * @return the contents of the ThreadLocal that controls whether or not to convert CycL dates to
   *         Java dates
   */
  boolean getShouldConvertToJavaDates();

  /**
   * Resets the current cyclist value to the default specified in the current
   * CycSessionConfiguration.
   */
  void resetCyclist();

  /**
   * Resets the current KB purpose value to the default specified in the current
   * CycSessionConfiguration.
   */
  void resetKePurpose();

  /**
   * Resets the current DefaultContext value to the default specified in the current
   * CycSessionConfiguration.
   */
  void resetDefaultContext();

  /**
   * Resets the current shouldTranscriptOperations value to the default specified in the current
   * CycSessionConfiguration.
   */
  void resetShouldTranscriptOperations();

  /**
   * Resets the current shouldConvertToJavaDates value to the default specified in the current
   * CycSessionConfiguration.
   */
  void resetShouldConvertToJavaDates();

  /**
   * Clears all values, reverting to the defaults specified in CycSessionConfiguration.
   */
  void reset();
  
  //====|    DefaultSessionOptions    |===========================================================//
  
  /**
   * An <b>immutable</b> set of options, including the name of the cyclist making assertions, and
   * the project's KE purpose. This provides the defaults for the mutable SessionOptions interface.
   *
   * @author nwinant
   */
  public interface DefaultSessionOptions {

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
     * Will actions in the current thread that modify the KB be transcripted by the Cyc server?
     *
     * @return will KB operations from the current thread be transcripted?
     */
    boolean getShouldTranscriptOperations();

    /**
     * Returns the current default contexts
     *
     * @return the contents of the DefaultContest ThreadLocal
     */
    DefaultContext getDefaultContext();

  }

}
