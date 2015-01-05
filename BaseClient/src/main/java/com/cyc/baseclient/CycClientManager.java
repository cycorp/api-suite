package com.cyc.baseclient;

/*
 * #%L
 * File: CycClientManager.java
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

import com.cyc.base.CycAccess;
import com.cyc.base.CycAccessManager;
import com.cyc.session.CycSession;
import com.cyc.session.SessionConfigurationException;
import com.cyc.session.CycSessionConfiguration;
import com.cyc.session.EnvironmentConfiguration;
import com.cyc.session.SessionCommunicationException;
import com.cyc.session.SessionInitializationException;
import com.cyc.session.SessionManager;

/**
 *
 * @author nwinant
 * @param <T>
 */
public class CycClientManager<T extends CycClientSession> extends CycAccessManager<T> {

  // Fields
  
  static private CycClientManager ME;
  
  public CycClientManager(SessionManager<T> sessionMgr) {
    super(sessionMgr);
  }
  
  public CycClientManager() {
    super();
  }
  
  public synchronized static <T extends CycClientSession> CycClientManager<T> getClientManager() {
    if (ME == null) {
      ME = new CycClientManager();
    }
    return ME;
  }
  
  
  // Public

  static public CycClient getClient() throws SessionConfigurationException, SessionCommunicationException, SessionInitializationException {
    return CycClientManager.getClientManager().getSession().getAccess();
  }

  static public CycClient getCurrentClient() throws SessionConfigurationException, SessionCommunicationException, SessionInitializationException {
    return CycClientManager.getClientManager().getCurrentSession().getAccess();
  }
  
  @Override
  public T getSession() throws SessionConfigurationException, SessionCommunicationException, SessionInitializationException {
    return this.getSessionMgr().getSession();
  }

  @Override
  public T getCurrentSession() throws SessionConfigurationException, SessionCommunicationException, SessionInitializationException {
    return this.getSessionMgr().getCurrentSession();
  }

  @Override
  public CycSessionConfiguration getConfiguration() throws SessionConfigurationException {
    return this.getSessionMgr().getConfiguration();
  }

  @Override
  public EnvironmentConfiguration getEnvironmentConfiguration() throws SessionConfigurationException {
    return this.getSessionMgr().getEnvironmentConfiguration();
  }
  
  @Override
  public CycClient fromSession(CycSession session) {
    return ((CycClientSession) session).getAccess();
  }
  
  public CycClient fromCycAccess(CycAccess access) {
    return (CycClient) access;
  }
}
