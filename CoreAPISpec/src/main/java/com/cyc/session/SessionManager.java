package com.cyc.session;

/*
 * #%L
 * File: SessionManager.java
 * Project: Core API Specification
 * %%
 * Copyright (C) 2013 - 2015 Cycorp, Inc
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
 * The SessionManager is responsible for managing the lifecycle of {@link CycSession}s, from 
 * configuration, to creation, to caching, to cleanup, and providing appropriate CycSessions to
 * API and application code that requires them.
 * 
 * @author nwinant
 * @param <T>
 */
public interface SessionManager<T extends CycSession> {
  
  /**
   * Returns the CycSession currently assigned to this thread. If no CycSession
   * object currently exists, one will be acquired via {@link #getSession()} and assigned to the
   * local thread.
   * 
   * @return CycSession
   * @throws SessionConfigurationException if the application is not sufficiently configured for the CycConfigurationManager to connect to a Cyc server.
   * @throws SessionCommunicationException if the application encounters problems communicating with a Cyc server.
   * @throws SessionInitializationException if the application encounters problems initializing the CycSession.
   */
  T getCurrentSession() throws SessionConfigurationException, SessionCommunicationException, SessionInitializationException;

  /**
   * Returns a CycSession object, creating one if necessary. If no CycSession object currently 
   * exists, one will be created from a configuration drawn from {@link #getConfiguration()}.
   *
   * @return a new CycSession object
   * @throws SessionConfigurationException if the application is not sufficiently configured for the CycSession to identify a Cyc server.
   * @throws SessionCommunicationException if the application encounters problems communicating with a Cyc server.
   * @throws SessionInitializationException if the application encounters problems initializing the CycSession.
   */
  T getSession() throws SessionConfigurationException, SessionCommunicationException, SessionInitializationException;
  
  /**
   * Returns a CycSessionConfiguration, suitable for creating a new CycSession. The SessionManager 
   * will first retrieve an {@link EnvironmentConfiguration} based on the System properties, and 
   * then expanding any configuration directives it might contain; the result is returned by this 
   * method. The SessionManager <em>may</em> cache CycSessionConfigurations if it believes there is
   * no possibility that they would have changed: for example, if the EnvironmentConfiguration 
   * directly specifies a CycServer address, and the relevant System properties have not changed.
   * 
   * @return a new CycSessionConfiguration
   * @throws SessionConfigurationException 
   */
  CycSessionConfiguration getConfiguration() throws SessionConfigurationException;
  
  /**
   * Returns an EnvironmentConfiguration, drawn from the System properties. The resulting 
   * EnvironmentConfiguration instance is a snapshot of the relevant System properties from the time
   * at which it was created.
   * @return
   * @throws SessionConfigurationException 
   */
  EnvironmentConfiguration getEnvironmentConfiguration() throws SessionConfigurationException;
}
