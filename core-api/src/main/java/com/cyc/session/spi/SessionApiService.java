package com.cyc.session.spi;

/*
 * #%L
 * File: SessionApiService.java
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
import com.cyc.CycApiEntryPoint;
import com.cyc.session.SessionManager;
import com.cyc.session.SessionManagerConfiguration;
import com.cyc.session.exception.SessionManagerConfigurationException;
import com.cyc.session.exception.SessionManagerException;
import com.cyc.session.exception.SessionServiceException;

/**
 * The primary entrypoint for a Session API implementation.
 * 
 * @author nwinant
 */
public interface SessionApiService extends CycApiEntryPoint {
  
  /**
   * Returns the current SessionManager instance.
   * 
   * @return 
   */
  SessionManager getSessionManager();
  
  /**
   * Loads a new SessionManager, configured via the supplied SessionManagerConfiguration. The
   * previous SessionManager will be closed if it has not been closed already.
   *
   * @param configuration
   * 
   * @return the new SessionManager
   * 
   * @throws SessionServiceException
   * @throws SessionManagerException
   * @throws SessionManagerConfigurationException
   * 
   * @see #reloadSessionManager() 
   */
  SessionManager reloadSessionManager(SessionManagerConfiguration configuration)
          throws SessionServiceException, SessionManagerException, SessionManagerConfigurationException;
  
  /**
   * Loads a new SessionManager with the default SessionManagerConfiguration. The previous
   * SessionManager will be closed if it has not been closed already.
   *
   * @return the new SessionManager
   * 
   * @throws SessionServiceException
   * @throws SessionManagerException
   * 
   * @see #reloadSessionManager(com.cyc.session.manager.SessionManagerConfiguration) 
   */
  SessionManager reloadSessionManager() 
          throws SessionServiceException, SessionManagerException, SessionManagerConfigurationException;
  
  /* *
   * Returns the configuration for the SessionManager.
   * 
   * @return 
   * /
  SessionManagerConfiguration getConfiguration();
  */
}
