package com.cyc.session;

import java.io.Closeable;

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
public interface SessionManager<T extends CycSession> extends Closeable, Comparable<SessionManager<T>> {
  
  /**
   * Returns the CycSession currently assigned to this thread. If no CycSession object currently 
   * exists, one will be created from a configuration drawn from {@link #getConfiguration()} and
   * assigned to the local thread.
   * 
   * @return a (possibly new) CycSession object for the current thread.
   * @throws SessionConfigurationException if the application is not sufficiently configured for the CycConfigurationManager to connect to a Cyc server.
   * @throws SessionCommunicationException if the application encounters problems communicating with a Cyc server.
   * @throws SessionInitializationException if the application encounters problems initializing the CycSession.
   */
  T getCurrentSession() throws SessionConfigurationException, SessionCommunicationException, SessionInitializationException;
  
  /* *
   * Returns the CycSession currently assigned to this thread. If no CycSession
   * object currently exists, one will be acquired via {@link #getSession(SessionCriteria)} and 
   * assigned to the local thread.
   * 
   * @param criteria The criteria for selecting the session.
   * @return a (possibly) new CycSession object for the current thread.
   * @throws SessionConfigurationException if the application is not sufficiently configured for the CycConfigurationManager to connect to a Cyc server.
   * @throws SessionCommunicationException if the application encounters problems communicating with a Cyc server.
   * @throws SessionInitializationException if the application encounters problems initializing the CycSession.
   */
  //T getCurrentSession(SessionCriteria criteria) throws SessionConfigurationException, SessionCommunicationException, SessionInitializationException;

  /* *
   * Returns a new CycSession object, creating one if necessary. If no CycSession object currently 
   * exists, one will be created from a configuration drawn from {@link #getConfiguration()}.
   *
   * @return a new CycSession object.
   * @throws SessionConfigurationException if the application is not sufficiently configured for the CycSession to identify a Cyc server.
   * @throws SessionCommunicationException if the application encounters problems communicating with a Cyc server.
   * @throws SessionInitializationException if the application encounters problems initializing the CycSession.
   */
  //T createSession() throws SessionConfigurationException, SessionCommunicationException, SessionInitializationException;
  
  /* *
   * Returns a CycSession object, creating one if necessary. If no CycSession object currently 
   * exists, one will be created from a configuration drawn from {@link #getConfiguration()}.
   *
   * @param criteria The criteria for selecting the session.
   * @return a (possibly) new CycSession object.
   * @throws SessionConfigurationException if the application is not sufficiently configured for the CycSession to identify a Cyc server.
   * @throws SessionCommunicationException if the application encounters problems communicating with a Cyc server.
   * @throws SessionInitializationException if the application encounters problems initializing the CycSession.
   */
  //T createSession(SessionCriteria criteria) throws SessionConfigurationException, SessionCommunicationException, SessionInitializationException;
  
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
  
  /* *
   * Retrieves all sessions matching a SessionCriteria object.
   * 
   * @param criteria
   * @return 
   */
  //Collection<T> getSessions(SessionCriteria criteria);
  
  /* *
   * Releases all sessions matching a SessionCriteria object.
   * @param criteria
   * @return 
   */
  //Collection<T> releaseSessions(SessionCriteria criteria);
  
  /**
   * Returns whether the SessionManager is closed.
   * 
   * @return whether SessionManager is closed
   */
  public boolean isClosed();
  
}
