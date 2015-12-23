/*
 * Copyright 2015 Cycorp, Inc..
 *
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
 */
package com.cyc.session.internal;

/*
 * #%L
 * File: CurrentObjectCache.java
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
import com.cyc.session.exception.SessionConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cache responsible for storing (and cleaning up after) all "current" objects. Right now 
 * that only means the current CycSession, but could be expanded to manage other resources if 
 * necessary.
 * 
 * <p>Because thread-local resources can be a source of memory leaks (especially in, e.g., servlets)
 * the goal of this class is to encapsulate all code for managing them in one place; there should be
 * no references to ThreadLocals anywhere else in the Session API. The underlying implementation is
 * a ThreadLocal map, but that could change in the future. 
 * 
 * @author nwinant
 */
public class CurrentObjectCache<T extends CycSession> {
  
  // Fields
  
  static final private Logger LOGGER = LoggerFactory.getLogger(CurrentObjectCache.class);
  static final private ThreadLocal<CycSession> CURRENT_SESSION = new ThreadLocal<CycSession>() {
      @Override
      protected CycSession initialValue() {
        return null;
      }
    };
  
  
  // Constructors
  
  public CurrentObjectCache() {}
  
  
  // Public
  
  public T getCurrentSession() {
    return (T) CURRENT_SESSION.get();
  }
  
  public T setCurrentSession(T session) throws SessionConfigurationException {
    if (session == null) {
      throw new NullPointerException("CycSession is not allowed to be null");
    }
    CURRENT_SESSION.set(session);
    if (!hasCurrentSession()) {
      throw new SessionConfigurationException("Session " + session + " seems valid, but could not set it to the current session.");
    }
    return session;
  }
  
  public boolean hasCurrentSession() {
    return CURRENT_SESSION.get() instanceof CycSession;
  }
  
  public void clearCurrentSession() {
    CURRENT_SESSION.remove();
  }
  
}
