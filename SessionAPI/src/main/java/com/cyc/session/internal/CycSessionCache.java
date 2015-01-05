package com.cyc.session.internal;

/*
 * #%L
 * File: CycSessionCache.java
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

import com.cyc.session.CycServer;
import com.cyc.session.CycSession;
import com.cyc.session.CycSessionConfiguration;
import com.cyc.session.EnvironmentConfiguration;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A cache for CycSessions. Current implementation is extremely simple.
 * 
 * @author nwinant
 * @param <T>
 */
public class CycSessionCache<T extends CycSession> {
  
  // Fields
  
  private static final Logger LOGGER = LoggerFactory.getLogger(CycSessionCache.class);
  private final ConcurrentHashMap<CycServer, T> cachedSessions = new ConcurrentHashMap();
  
  
  // Public
  
  public Collection<T> getAll() {
    return this.cachedSessions.values();
  }
  
  public T get(CycServer server) {
    if (server == null) {
      return null;
    }
    return this.cachedSessions.get(server);
  }
  
  public boolean hasSession(CycServer server) {
    return get(server) != null;
  }
  
  public boolean hasSession(CycSession session) {
    if ((session == null)
            || (session.getServerInfo() == null)) {
      return false;
    }
    return hasSession(session.getServerInfo().getCycServer());
  }
  
  synchronized public T remove(T session) {
    CycServer server = this.lookupKey(session);
    return remove(server);
  }
  
  synchronized public T remove(CycServer server) {
    if (server == null) {
      return null;
    }
    T session = this.cachedSessions.remove(server);
    LOGGER.debug("Removed session {}", session);
    return session;
  }
  
  /**
   * Caches a CycSession, if the session's configuration and the environment
   * both specify that caching is allowed.
   * 
   * Cached sessions are indexed by their CycSessionConfiguration,
   * and the session will not be cached if another session is already indexed
   * under an equivalent CycSessionConfiguration.
   * @param session
   * @param environment
   * @return the session
   */
  synchronized public T put(T session, EnvironmentConfiguration environment) {
    if ((session != null)
            && !hasSession(session)
            && (session.getServerInfo() != null)
            && (session.getServerInfo().getCycServer() != null)) {
      LOGGER.debug("Caching session {}", session);
      final CycServer server = session.getServerInfo().getCycServer();
      this.cachedSessions.put(server, session);
    }
    return session;
  }
  
  public boolean isCachingAllowed(CycSessionConfiguration config, EnvironmentConfiguration environment) {
    return environment.isSessionCachingAllowed()
            && config.isSessionCachingAllowed();
  }
  
  public boolean isCacheable(T session, EnvironmentConfiguration environment) {
    return isCachingAllowed(session.getConfiguration(), environment);
  }
  
  public int size() {
    return this.cachedSessions.size();
  }
  
  
  // Private
  
  private CycServer lookupKey(T session) {
    if (session == null) {
      return null;
    }
    final Set<CycServer> keys = this.cachedSessions.keySet();
    for (CycServer key : keys) {
      final T cachedSession = this.get(key);
      if ((session.equals(cachedSession))) {
        return key;
      }
    }
    return null;
  }
}
