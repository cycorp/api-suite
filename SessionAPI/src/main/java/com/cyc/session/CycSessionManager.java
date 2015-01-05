package com.cyc.session;

/*
 * #%L
 * File: CycSessionManager.java
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

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The CycSessionManager provides a singleton implementation of a SessionManager.
 * Additionally, it provides several convenience methods to access it. 
 * 
 * The CycSessionManager searches for SessionManager services during class
 * initialization. If it cannot find any, it will throw a 
 * {@link CycSessionManagerInitializationError}.
 * 
 * @author nwinant
 */
public class CycSessionManager {

  /**
   * Exception thrown if the CycSessionManager cannot find any SessionManager services during class
   * initialization. The CycSessionManager cannot recover from this exception.
   */
  static public class CycSessionManagerInitializationError extends ExceptionInInitializerError {
    private final String msg;
    
    public CycSessionManagerInitializationError(String msg, Exception ex) {
      super(ex);
      this.msg = msg;
    }
    
    @Override
    public String getMessage() {
      return this.msg;
    }
  }
  
  
  // Static Fields
  
  private static final Logger LOGGER;
  private static final SessionManager MANAGER;
  
  static {
    try {
      LOGGER = LoggerFactory.getLogger(CycSessionManager.class);
      MANAGER = loadManager();
    } catch (SessionApiException ex) {
      throw new CycSessionManagerInitializationError(CycSessionManager.class.getName() + " could not be initialized.", ex);
    }
  }
  
  
  // Constructors
  
  private CycSessionManager() {}
  
  
  // Static
  
  /**
   * Returns a singleton implementation of {@link SessionManager}.
   * 
   * @return A singleton SessionManager instance
   */
  public static SessionManager get() {
    return MANAGER;
  }
  
  
  // Public
  
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
  static public CycSession getCurrentSession() throws SessionConfigurationException, SessionCommunicationException, SessionInitializationException {
    return CycSessionManager.get().getCurrentSession();
  }
  
  /**
   * Returns a CycSession object, creating one if necessary. If no CycSession object currently 
   * exists, one will be created from a configuration drawn from {@link #getConfiguration()}.
   * 
   * @return a new CycSession object
   * @throws SessionConfigurationException if the application is not sufficiently configured for the CycSession to identify a Cyc server.
   * @throws SessionCommunicationException if the application encounters problems communicating with a Cyc server.
   * @throws SessionInitializationException if the application encounters problems initializing the CycSession.
   */
  static public CycSession getSession() throws SessionConfigurationException, SessionCommunicationException, SessionInitializationException {
    return CycSessionManager.get().getSession();
  }
  
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
  static public CycSessionConfiguration getConfiguration() throws SessionConfigurationException {
    return CycSessionManager.get().getConfiguration();
  }
  
  /**
   * Returns an EnvironmentConfiguration, drawn from the System properties.
   * @return
   * @throws SessionConfigurationException 
   */
  static public CycSessionConfiguration getEnvironmentConfiguration() throws SessionConfigurationException {
    return CycSessionManager.get().getEnvironmentConfiguration();
  }
  
  
  // Protected
          
  /**
   * Returns a list of all available SessionManager implementations, with the default
   * {@link com.cyc.session.internal.SessionManagerImpl} implementation as the last element.
   * @return all session managers
   * @throws com.cyc.session.SessionServiceException
   * @throws SessionConfigurationException 
   */
  static protected List<SessionManager> loadAllSessionManagers() throws SessionServiceException, SessionConfigurationException {
    // Note: The relevant service provider file in META-INF/services
    //       is generated by the serviceloader-maven-plugin, specified
    //       in the pom.xml file.
    
    final List<SessionManager> sessionMgrs = new ArrayList();
    final ServiceLoader<SessionManager> loader =
            ServiceLoader.load(SessionManager.class);
    for (SessionManager sessionMgr : loader) {
      if (!com.cyc.session.internal.SessionManagerImpl.class.equals(sessionMgr.getClass())) {
        sessionMgrs.add(sessionMgr);
      }
    }
    sessionMgrs.add(new com.cyc.session.internal.SessionManagerImpl());
    return sessionMgrs;
  }
  
  
  // Private
  
  private static SessionManager selectSessionManager(List<SessionManager> sessionMgrs) {
    return sessionMgrs.get(0);
  }
  
  private static synchronized SessionManager loadManager() throws SessionServiceException, SessionConfigurationException {
    final List<SessionManager> sessionMgrs = loadAllSessionManagers();
    if (sessionMgrs.isEmpty()) {
      throw new SessionServiceException(SessionManager.class, "Could not find any " + SessionManager.class.getSimpleName() + " implementations to load. This should never happen.");
    }
    for (SessionManager sessionMgr : sessionMgrs) {
      LOGGER.info("Found {}: {}", SessionManager.class.getSimpleName(), sessionMgr.getClass().getName());
    }
    final SessionManager mgr = selectSessionManager(sessionMgrs);
    LOGGER.info("Selected {}: {}", SessionManager.class.getSimpleName(), mgr.getClass().getName());
    return mgr;
  }
  
}
