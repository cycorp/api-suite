package com.cyc.session.internal;

/*
 * #%L
 * File: SessionManagerImpl.java
 * Project: Session API Implementation
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

import com.cyc.session.CycServer;
import com.cyc.session.SessionConfigurationException;
import com.cyc.session.CycSession;
import com.cyc.session.CycSession.SessionStatus;
import com.cyc.session.CycSessionConfiguration;
import com.cyc.session.EnvironmentConfiguration;
import com.cyc.session.SessionCommunicationException;
import com.cyc.session.SessionInitializationException;
import com.cyc.session.SessionServiceException;
import com.cyc.session.SessionManager;
import com.cyc.session.connection.SessionFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ServiceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reference implementation of SessionManager.
 * @author nwinant
 * @param <T>
 */
public class SessionManagerImpl<T extends CycSession> implements SessionManager<T> {
  
  // Fields
  
  // TODO: add currentSessions map!
  static final private Logger LOGGER = LoggerFactory.getLogger(SessionManagerImpl.class);
  static final private ThreadLocal<CycSession> currentSession = new ThreadLocal<CycSession>() {
    @Override
    protected CycSession initialValue() {
      return null;
    }
  };
  
  final private ConfigurationLoaderManager loaderMgr;
  final private SessionFactory<T> sessionFactory;
  final private ConfigurationCache configCache;
  final private CycSessionCache<T> sessionCache;
  final private EnvironmentConfigurationLoader environmentLoader;
  
  
  // Constructors
  
  public SessionManagerImpl() throws SessionServiceException, SessionConfigurationException {
    this.loaderMgr = new ConfigurationLoaderManager();
    this.sessionFactory = loadSessionFactory();
    this.configCache = new ConfigurationCache();
    this.sessionCache = new CycSessionCache();
    this.environmentLoader = new EnvironmentConfigurationLoader();
    LOGGER.debug("{} instantiated.", SessionManagerImpl.class.getSimpleName());
  }
  
  
  // Public
  
  @Override
  public EnvironmentConfiguration getEnvironmentConfiguration() throws SessionConfigurationException {
    return environmentLoader.getConfiguration();
  }
  
  @Override
  public CycSessionConfiguration getConfiguration() throws SessionConfigurationException {
    final EnvironmentConfiguration environment = getEnvironmentConfiguration();
    final CycSessionConfiguration cachedConfig = configCache.get(environment);
    if (cachedConfig != null) {
      LOGGER.info("Retrieving config from cache: {}", cachedConfig);
      return cachedConfig;
    }
    LOGGER.info("#getConfiguration() Loading new configuration");
    CycSessionConfiguration config = loaderMgr.getConfiguration(environment);
    if (!(config instanceof EnvironmentConfiguration)) {
      configCache.put(environment, config);
    }
    return config;
  }
  
  @Override
  public T getSession() throws SessionConfigurationException, SessionCommunicationException, SessionInitializationException {
    final CycSessionConfiguration config = getConfiguration();
    final ConfigurationValidator configUtils = new ConfigurationValidator(config);
    if (!configUtils.isSufficient()) {
      throw new SessionConfigurationException("Configuration " + config + " is not sufficient to create a valid session");
    }
    
    final T cachedSession = retrieveCachedSession(config);
    if (cachedSession != null) {
      return cachedSession;
    }
    
    return sessionCache.put(createSession(config), 
            getEnvironmentConfiguration());
  }
  
  @Override
  public T getCurrentSession() throws SessionConfigurationException, SessionInitializationException, SessionCommunicationException {
    reapDeadSessions();
    if (!this.hasCurrentSession()) {
      LOGGER.debug("#getCurrentSession: There is no current session. Retrieving...");
      initCurrentSession();
    }
    return (T) currentSession.get();
  }
  
  
  // Protected
  
  protected boolean hasCurrentSession() {
    return currentSession.get() instanceof CycSession;
  }
  
  /**
   * @param server
   * @return is the server managed by this session manager?
   * @deprecated May be removed or changed in 1.0.0-rc2
   */
  @Deprecated
  protected boolean hasSession(CycServer server) {
    return this.sessionCache.hasSession(server);
  }
  
  /**
   * @param session
   * @throws SessionConfigurationException
   * @deprecated May be removed or changed in 1.0.0-rc2
   */
  @Deprecated
  protected void putSession(T session) throws SessionConfigurationException {
    this.sessionCache.put(session, this.getEnvironmentConfiguration());
  }
  
  /**
   * @param server
   * @return the session associated with this server.
   * @deprecated May be removed or changed in 1.0.0-rc2
   */
  @Deprecated
  protected T getSession(CycServer server) {
    if (!hasSession(server)) {
      return null;
    }
    return this.sessionCache.get(server);
  }
  
  protected void clearCurrentSession() {
    LOGGER.debug("Clearing current session");
    currentSession.set(null);
  }
  
  /**
   * Sets the current CycSession. It will not allow null values. If you need to
   * clear the current CycSession, call {@link #clearCurrentSession}.
   * 
   * @param session
   * @return the session
   * @throws SessionConfigurationException 
   */
  protected T setCurrentSession(T session) throws SessionConfigurationException {
    if (!isValidSession(session)) {
      throw new SessionConfigurationException("Session " + session + " is not valid. Cannot set it to the current session.");
    }
    currentSession.set(session);
    if (!hasCurrentSession()) {
      throw new SessionConfigurationException("Session " + session + " seems valid, but could not set it to the current session.");
    }
    LOGGER.debug("Current session set: {}", session);
    return session;
  }
  
  protected void initCurrentSession() throws SessionConfigurationException, SessionInitializationException, SessionCommunicationException {
    LOGGER.debug("Initializing current session");
    setCurrentSession(getSession());
  }
  
  protected SessionFactory<T> getSessionFactory() {
    return this.sessionFactory;
  }
  
  protected boolean isValidSession(CycSession session) {
    // TODO: this could be more fleshed out
    return session != null;
  }
  
  protected T retrieveCachedSession(CycSessionConfiguration config) {
    if (config == null) {
      return null; 
    }
    final T cachedSession = sessionCache.get(config.getCycServer());
    if (cachedSession != null) {
      if (!isSessionDead(cachedSession)) {
        LOGGER.info("Retrieved session from cache: {}", cachedSession);
        return cachedSession;
      }
      reapDeadSessions();
    }
    return null;
  }
  
  protected T createSession(CycSessionConfiguration config) throws SessionConfigurationException, SessionCommunicationException, SessionInitializationException {
    final SessionFactory<T> factory = getSessionFactory();
    final T session = factory.createSession(config);
    if (!isValidSession(session)) {
      throw new SessionConfigurationException("Session " + session + " is not valid; could not retrieve a valid session from " + factory.getClass());
    }
    getSessionFactory().initializeSession(session);
    if (SessionStatus.UNINITIALIZED.equals(session.getStatus())) {
      throw new SessionInitializationException("Session " + session + " could not be initialized by " + factory);
    }
    LOGGER.info("Created new session: {}", session);
    return sessionCache.put(session, getEnvironmentConfiguration());
  }
  
  synchronized protected void reapDeadSessions() {
    // Should we consider a way to allow closed sessions to remain in the cache for diagnostics / debugging / least surprise?
    if (hasCurrentSession()) {
      T currSession = (T) currentSession.get();
      if (isSessionDead(currSession)) {
        final CycSession current = currentSession.get();
        LOGGER.warn("Reaping current session with status {}: {}", current.getStatus(), current);
        this.clearCurrentSession();
        sessionCache.remove(currSession);
      }
    }
    final Collection<T> cachedSessions = sessionCache.getAll();
    for (T cachedSession : cachedSessions) {
      if ((cachedSession != null) && isSessionDead(cachedSession)) {
        LOGGER.warn("Reaping session with status {}: {}", cachedSession.getStatus(), cachedSession);
        sessionCache.remove(cachedSession);
      }
    }
  }
  
  protected boolean isSessionDead(CycSession session) {
    return (session == null) 
            || !SessionStatus.CONNECTED.equals(session.getStatus());
  }
   
  
  // Private
  
  /**
   * Returns an implementation of {@link SessionFactory}. It will return the first
   * implementation it finds which has been registered per {@link java.util.ServiceLoader}.
   * 
   * @return an object which implements SessionFactory.
   * @throws com.cyc.session.SessionServiceException
   */
  final protected SessionFactory<T> loadSessionFactory() throws SessionServiceException {
    final List<SessionFactory> factories = loadAllSessionFactories();
    if (factories.isEmpty()) {
      throw new SessionServiceException(SessionFactory.class, "Could not find a service implementation!");
    }
    for (SessionFactory factory : factories) {
      LOGGER.info("Found {}: {}", SessionFactory.class.getSimpleName(), factory.getClass().getName());
    }
    return factories.get(0);
  }
  
  /**
   * Returns a list of all implementations of SessionFactory which can be located
   * via the {@link java.util.ServiceLoader}.
   * @return the available session factories
   */
  static protected List<SessionFactory> loadAllSessionFactories() {
    // Note: The relevant service provider file in META-INF/services
    //       is generated by the serviceloader-maven-plugin, specified
    //       in the pom.xml file.
    final List<SessionFactory> factories = new ArrayList();
    final ServiceLoader<SessionFactory> loader =
            ServiceLoader.load(SessionFactory.class);
    for (SessionFactory factory : loader) {
      factories.add(factory);
    }
    return factories;
  }
}
