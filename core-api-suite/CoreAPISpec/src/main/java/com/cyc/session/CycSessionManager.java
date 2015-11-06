package com.cyc.session;

/*
 * #%L
 * File: CycSessionManager.java
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The CycSessionManager provides singleton access to a SessionManager instance.
 * Additionally, it provides several convenience methods to access it. 
 * 
 * <p>The CycSessionManager searches for SessionManager services during class
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
    
    public CycSessionManagerInitializationError(String msg, Throwable cause) {
      super(cause);
      this.msg = msg;
    }
    
    @Override
    public String getMessage() {
      return this.msg;
    }
  }
  
  
  // Static Fields
  
  private static final Logger LOGGER;
  private static SessionManager MANAGER;
  
  static {
    try {
      LOGGER = LoggerFactory.getLogger(CycSessionManager.class);
      loadSessionManager();
    } catch (SessionApiException ex) {
      throw new CycSessionManagerInitializationError(CycSessionManager.class.getName() + " could not be initialized.", ex);
    } catch (ServiceConfigurationError ex) {
      throw new CycSessionManagerInitializationError(CycSessionManager.class.getName() + " could not be initialized.", ex);
    }
  }
  
  
  // Constructors
  
  private CycSessionManager() {}
  
  
  // Public static methods
  
  /**
   * Returns the current instance of {@link SessionManager}.
   * 
   * @return The current SessionManager instance
   */
  public static SessionManager getInstance() {
    return MANAGER;
  }
  
  /**
   * Loads a new SessionManager. The previous SessionManager will be closed if it has not been closed already.
   * 
   * @throws SessionServiceException
   * @throws SessionConfigurationException 
   */
  static public synchronized void reloadInstance() throws SessionServiceException, SessionConfigurationException {
    final SessionManager currMgr = getInstance();
    if ((currMgr != null) && !currMgr.isClosed()) {
      try {
        currMgr.close();
      } catch (IOException ex) {
        throw new SessionServiceException(CycSessionManager.class, "Error closing current SessionManager", ex);
      }
    }
    loadSessionManager();
  }
  
  /**
   * Returns the CycSession currently assigned to this thread. If no CycSession
   * object currently exists, one will be acquired via {@link #getSession()} and assigned to the
   * local thread.
   * 
   * <p>Note that this is a convenience method which simply trampolines to
   * {@link CycSessionManager#getInstance()#getCurrentSession() }.
   * 
   * @return CycSession
   * @throws SessionConfigurationException if the application is not sufficiently configured for the CycConfigurationManager to connect to a Cyc server.
   * @throws SessionCommunicationException if the application encounters problems communicating with a Cyc server.
   * @throws SessionInitializationException if the application encounters problems initializing the CycSession.
   */
  static public CycSession getCurrentSession() throws SessionConfigurationException, SessionCommunicationException, SessionInitializationException {
    return CycSessionManager.getInstance().getCurrentSession();
  }
  
  /* *
   * Returns a CycSessionConfiguration, suitable for creating a new CycSession. The SessionManager 
   * will first retrieve an {@link EnvironmentConfiguration} based on the System properties, and 
   * then expanding any configuration directives it might contain; the result is returned by this 
   * method. The SessionManager <em>may</em> cache CycSessionConfigurations if it believes there is
   * no possibility that they would have changed: for example, if the EnvironmentConfiguration 
   * directly specifies a CycServer address, and the relevant System properties have not changed.
   * 
   * @return a new CycSessionConfiguration
   * @throws SessionConfigurationException 
   * /
  static public CycSessionConfiguration getConfiguration() throws SessionConfigurationException {
    return CycSessionManager.get().getConfiguration();
  }
  */
  
  /* *
   * Returns an EnvironmentConfiguration, drawn from the System properties.
   * @return
   * @throws SessionConfigurationException 
   * /
  static public CycSessionConfiguration getEnvironmentConfiguration() throws SessionConfigurationException {
    return CycSessionManager.get().getEnvironmentConfiguration();
  }
  */
  
  
  // Protected static methods
  
  /**
   * Returns a list of all available SessionManager implementations, with the default
   * {@link com.cyc.session.internal.SessionManagerImpl} implementation as the last element.
   * @return all session managers
   * @throws com.cyc.session.SessionServiceException
   * @throws SessionConfigurationException 
   */
  static protected List<SessionManager> loadAllSessionManagers() throws SessionServiceException, SessionConfigurationException, ServiceConfigurationError {
    // Note: The relevant service provider file in META-INF/services
    //       is generated by the serviceloader-maven-plugin, specified
    //       in the pom.xml file.
    
    final List<SessionManager> sessionMgrs = new ArrayList();
    final ServiceLoader<SessionManager> loader =
            ServiceLoader.load(SessionManager.class);
    for (SessionManager sessionMgr : loader) {
      if (!sessionMgr.isClosed()) {
        sessionMgrs.add(sessionMgr);
      }
    }
    Collections.sort(sessionMgrs);
    return sessionMgrs;
  }
  
  
  // Private static methods
  
  private static SessionManager selectSessionManager(List<SessionManager> sessionMgrs) {
    return sessionMgrs.get(0);
  }
  
  private static synchronized void loadSessionManager() throws SessionServiceException, SessionConfigurationException {
    final List<SessionManager> sessionMgrs = loadAllSessionManagers();
    if (sessionMgrs.isEmpty()) {
      throw new SessionServiceException(SessionManager.class, "Could not find any " + SessionManager.class.getSimpleName() + " implementations to load. This should never happen.");
    }
    for (SessionManager sessionMgr : sessionMgrs) {
      LOGGER.info("Found {}: {}", SessionManager.class.getSimpleName(), sessionMgr);
    }
    final SessionManager mgr = selectSessionManager(sessionMgrs);
    MANAGER = mgr;
    LOGGER.warn("Loaded {}: {}", SessionManager.class.getSimpleName(), MANAGER);
  }

}
