package com.cyc.session.internal;

/*
 * #%L
 * File: CycSessionCache.java
 * Project: Session Client
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

import com.cyc.session.CycSession;
import com.cyc.session.CycSessionConfiguration;
import com.cyc.session.EnvironmentConfiguration;
import com.cyc.session.exception.SessionException;
import com.cyc.session.selection.SessionSelector;
import java.util.Collections;
import java.util.HashSet;
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
  private final Set<T> cachedSessions;
  
  
  // Constructors
  
  public CycSessionCache() {
    this.cachedSessions = Collections.newSetFromMap(new ConcurrentHashMap());
    // TODO: Consider using a ConcurrentSkipListSet instead. - nwinant, 2015-10-20
    //       http://stackoverflow.com/questions/6720396/different-types-of-thread-safe-sets-in-java
  }
  
  
  // Public
  
  public Set<T> getAll() {
    return Collections.unmodifiableSet(this.cachedSessions);
  }
  
  public Set<T> getAll(SessionSelector criteria) throws SessionException {
    final Set<T> results = new HashSet();
    for (T session : getAll()) {
      if (criteria.matchesSession(session)) {
        results.add(session);
      }
    }
    return results;
  }
  
  public boolean contains(T session) {
    return cachedSessions.contains(session);
  }
  
  public boolean remove(T session) {
    if (cachedSessions.remove(session)) {
      LOGGER.debug("Removed session {}", session);
      return true;
    }
    return false;
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
  public boolean add(T session, EnvironmentConfiguration environment) {
    if (session == null) {
      throw new NullPointerException("Cannot cache null session");
    } else if (contains(session)) {
      LOGGER.warn("Being asked to re-cache session: {}", session);
    } else if (!isCacheable(session, environment)) {
      LOGGER.warn("Caching of session {} not permitted by EnvironmentConfiguration {}", session, environment);
    } else {
      LOGGER.debug("Caching session {}", session);
      return this.cachedSessions.add(session);
    }
    return false;
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
  
}
