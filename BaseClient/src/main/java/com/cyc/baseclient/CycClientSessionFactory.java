package com.cyc.baseclient;

/*
 * #%L
 * File: CycClientSessionFactory.java
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

import com.cyc.base.CycApiException;
import com.cyc.base.CycConnectionException;
import com.cyc.session.connection.SessionFactory;
import com.cyc.session.CycServerAddress;
import com.cyc.session.CycSessionConfiguration;
import com.cyc.session.SessionCommunicationException;
import com.cyc.session.SessionConfigurationException;
import com.cyc.session.SessionInitializationException;
import com.cyc.session.internal.ConfigurationValidator;

/**
 *
 * @author nwinant
 */
public class CycClientSessionFactory implements SessionFactory<CycClientSession> {

  @Override
  public CycClientSession createSession(CycSessionConfiguration config) throws SessionConfigurationException {
    final ConfigurationValidator validator = new ConfigurationValidator(config);
    if (!validator.isSufficient()) {
      throw new SessionConfigurationException("Configuration is not sufficient to create a " + CycClientSession.class.getSimpleName());
    }
    return new CycClientSession(config);
  }
  
  @Override
  public CycClientSession initializeSession(CycClientSession session) throws SessionCommunicationException, SessionInitializationException {
    final CycServerAddress server = session.getConfiguration().getCycServer();
    final CycClient client;
    try {
      client = new CycClient(session);
      session.setAccess(client);
    } catch (CycConnectionException ex) {
      ex.throwAsSessionException("Error communicating with " + server);
    } catch (CycApiException ex) {
      throw new SessionInitializationException("Error initializing CycSession for " + server, ex);
    }
    return session;
  }
}
