package com.cyc.session;

/*
 * #%L
 * File: CycSessionManager.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2017 Cycorp, Inc
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
import com.cyc.CoreServicesLoader;
import com.cyc.session.exception.SessionCommunicationException;
import com.cyc.session.exception.SessionConfigurationException;
import com.cyc.session.exception.SessionException;
import com.cyc.session.exception.SessionInitializationException;
import com.cyc.session.exception.SessionServiceException;
import com.cyc.session.spi.SessionManager;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.ServiceConfigurationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The CycSessionManager provides singleton access to a SessionManager instance. Additionally, it
 * provides several convenience methods to access it.
 *
 * <p>
 * The CycSessionManager searches for SessionManager services during class initialization. If it
 * cannot find any, it will throw a {@link CycSessionManagerInitializationError}.
 *
 * @author nwinant
 */
public class CycSessionManager {

  /**
   * Exception thrown if the CycSessionManager cannot find any SessionManager services during class
   * initialization. The CycSessionManager cannot recover from this exception.
   */
  public static class CycSessionManagerInitializationError extends ExceptionInInitializerError {

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
    } catch (SessionException ex) {
      throw new CycSessionManagerInitializationError(
              CycSessionManager.class.getName() + " could not be initialized.", ex);
    } catch (ServiceConfigurationError ex) {
      throw new CycSessionManagerInitializationError(
              CycSessionManager.class.getName() + " could not be initialized.", ex);
    }
  }

  // Constructors
  private CycSessionManager() {
  }

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
   * Loads a new SessionManager. The previous SessionManager will be closed if it has not been
   * closed already.
   *
   * @throws SessionServiceException
   * @throws SessionConfigurationException
   */
  public static synchronized void reloadInstance()
          throws SessionServiceException, SessionConfigurationException {
    final SessionManager currMgr = getInstance();
    if ((currMgr != null) && !currMgr.isClosed()) {
      try {
        currMgr.close();
      } catch (IOException ex) {
        throw new SessionServiceException(CycSessionManager.class,
                "Error closing current SessionManager", ex);
      }
    }
    loadSessionManager();
  }

  /**
   * Returns the CycSession currently assigned to this thread. If no CycSession object currently
   * exists, one will be created and assigned to the local thread.
   *
   * <p>
   * Note that this is a convenience method which simply trampolines to
   * {@link CycSessionManager#getInstance()#getCurrentSession() }.
   *
   * @return CycSession
   *
   * @throws SessionConfigurationException  if the application is not sufficiently configured for
   *                                        the CycConfigurationManager to connect to a Cyc server.
   * @throws SessionCommunicationException  if the application encounters problems communicating
   *                                        with a Cyc server.
   * @throws SessionInitializationException if the application encounters problems initializing the
   *                                        CycSession.
   */
  public static CycSession getCurrentSession()
          throws SessionConfigurationException, SessionCommunicationException,
                 SessionInitializationException {
    return CycSessionManager.getInstance().getCurrentSession();
  }

  /* *
   * Returns a CycSessionConfiguration, suitable for creating a new CycSession. The SessionManager 
   * will first retrieve an {@link EnvironmentConfiguration} based on the System properties, and 
   * then expanding any configuration directives it might contain; the result is returned by this 
   * method. The SessionManager <em>may</em> cache CycSessionConfigurations if it believes there is
   * no possibility that they would have changed: for example, if the EnvironmentConfiguration 
   * directly specifies a CycAddress address, and the relevant System properties have not changed.
   * 
   * @return a new CycSessionConfiguration
   * @throws SessionConfigurationException 
   * /
  public static CycSessionConfiguration getConfiguration() throws SessionConfigurationException {
    return CycSessionManager.get().getConfiguration();
  }
   */
 /* *
   * Returns an EnvironmentConfiguration, drawn from the System properties.
   * @return
   * @throws SessionConfigurationException 
   * /
  public static CycSessionConfiguration getEnvironmentConfiguration() 
         throws SessionConfigurationException {
    return CycSessionManager.get().getEnvironmentConfiguration();
  }
   */
  // Private static methods
  private static SessionManager selectSessionManager(List<SessionManager> sessionMgrs) {
    return sessionMgrs.get(0);
  }

  /**
   * Selects a SessionManager instance from all available implementations. The default
   * {@link com.cyc.session.internal.SessionManagerImpl} implementation, if present, should be
   * sorted as the last element.
   *
   * @return the selected SessionManager
   *
   * @throws com.cyc.session.SessionServiceException
   * @throws SessionConfigurationException
   */
  private static synchronized void loadSessionManager()
          throws SessionServiceException, SessionConfigurationException {
    final List<SessionManager> sessionMgrs = CoreServicesLoader.loadAllSessionManagers();

    Collections.sort(sessionMgrs);

    if (sessionMgrs.isEmpty()) {
      throw new SessionServiceException(
              SessionManager.class,
              "Could not find any " + SessionManager.class.getSimpleName()
                      + " implementations to load. This should never happen.");
    }
    for (SessionManager sessionMgr : sessionMgrs) {
      LOGGER.debug("Found {}: {}", SessionManager.class.getSimpleName(), sessionMgr);
    }
    MANAGER = selectSessionManager(sessionMgrs);
    LOGGER.debug("Loaded {}: {}", SessionManager.class.getSimpleName(), MANAGER);
  }

}
