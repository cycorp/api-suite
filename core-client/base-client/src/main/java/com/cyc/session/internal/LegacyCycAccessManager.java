package com.cyc.session.internal;

/*
 * #%L
 * File: LegacyCycAccessManager.java
 * Project: Base Client
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

import com.cyc.baseclient.CycClientSession;
import com.cyc.session.CycServerAddress;
import com.cyc.session.CycSessionManager;
import com.cyc.session.exception.SessionCommunicationException;
import com.cyc.session.exception.SessionConfigurationException;
import com.cyc.session.exception.SessionInitializationException;

/**
 * Provides backwards-compatible hooks for older code not fully migrated to the Session API.
 * 
 * @param <T>
 * @deprecated This class will be removed before final 1.0.0 release
 * @author nwinant
 */
@Deprecated
abstract public class LegacyCycAccessManager<T extends CycClientSession> {
  
  
  public LegacyCycAccessManager() { }
  
  
  // Protected
  
  protected T createSession(CycServerAddress server) throws SessionConfigurationException, SessionCommunicationException, SessionInitializationException {
    ImmutableConfiguration config = new ImmutableConfiguration(server, this.getClass());
    return this.getSessionMgr().createSession(config);
  }
  
  protected T setCurrentSession(T session) throws SessionConfigurationException {
    return this.getSessionMgr().setCurrentSession(session);
  }
  
  protected boolean hasCurrentSession() {
    return this.getSessionMgr().hasCurrentSession();
  }
  
  protected T getCurrentSession() throws SessionConfigurationException, SessionInitializationException, SessionCommunicationException {
    return this.getSessionMgr().getCurrentSession();
  }
  
  // Private
  
  private SessionManagerImpl<T> getSessionMgr() {
    return (SessionManagerImpl) CycSessionManager.getInstance();
  }
}
