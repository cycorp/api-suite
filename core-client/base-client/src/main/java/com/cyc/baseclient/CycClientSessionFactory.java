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

import com.cyc.base.exception.CycApiException;
import com.cyc.base.exception.CycConnectionException;
import com.cyc.session.spi.SessionFactory;
import com.cyc.session.CycServerAddress;
import com.cyc.session.CycSessionConfiguration;
import com.cyc.session.exception.SessionCommunicationException;
import com.cyc.session.exception.SessionConfigurationException;
import com.cyc.session.exception.SessionInitializationException;
import com.cyc.session.exception.SessionRuntimeException;
import com.cyc.session.internal.ConfigurationValidator;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nwinant
 */
public class CycClientSessionFactory implements SessionFactory<CycClientSession> {
    
  private static final Logger LOGGER = LoggerFactory.getLogger(CycClientSessionFactory.class);
  private final Map<CycServerAddress, CycClient> clients = new ConcurrentHashMap();
  private boolean closed = false;
  
  
  // Public
  
  @Override
  public CycClientSession createSession(CycSessionConfiguration config) throws SessionConfigurationException {
    errorIfClosed("Cannot create new session.");
    LOGGER.debug("Creating new session for {}", config);
    final ConfigurationValidator validator = new ConfigurationValidator(config);
    if (!validator.isSufficient()) {
      throw new SessionConfigurationException("Configuration is not sufficient to create a " + CycClientSession.class.getSimpleName());
    }
    return new CycClientSession(config);
  }
  
  @Override
  public CycClientSession initializeSession(CycClientSession session) throws SessionCommunicationException, SessionInitializationException {
    errorIfClosed("Cannot initialize session.");
    final CycServerAddress server = session.getConfiguration().getCycServer();
    LOGGER.debug("Initializing session for {}", server);
    try {
      session.setAccess(getClient(server));
    } catch (CycConnectionException ex) {
      ex.throwAsSessionException("Error communicating with " + server);
    } catch (CycApiException ex) {
      throw new SessionInitializationException("Error initializing CycSession for " + server, ex);
    }
    // NOTE: I briefly thought of attaching SessionListeners here, but anything interesting that 
    //       could go in a SessionFactory would probably go better in the SessionManager instead,
    //       as the SessionManager should have configurable, executive control over cleanup, etc. 
    //       - nwinant, 2015-10-16
    return session;
  }
  
  @Override
  public void releaseResourcesForServer(CycServerAddress server) {
    final CycClient client = clients.get(server);
    LOGGER.debug("Releasing client for {}: {}", server, client);
    if ((client != null) && !client.isClosed()) {
      client.close();
    }
    clients.remove(server);
  }
  
  @Override
  public void close() throws IOException {
    this.closed = true;
    final Set<CycServerAddress> servers = getServers();
    for (CycServerAddress server : servers) {
      releaseResourcesForServer(server);
    }
  }
  
  @Override
  public boolean isClosed() {
    return this.closed;
  }
  
  public Set<CycServerAddress> getServers() {
    return Collections.unmodifiableSet(clients.keySet());
  }
  
  synchronized public CycClient getClient(CycServerAddress server) throws CycConnectionException {
    reapOldClients();
    if (!clients.containsKey(server)) {
      final long startMillis = System.currentTimeMillis();
      final CycClient newClient = new CycClient(server);
      LOGGER.debug("Created new client for {}: {} in {}ms", server, newClient, (System.currentTimeMillis() - startMillis));
      clients.put(server, newClient);
    }
    final CycClient client = clients.get(server);
    LOGGER.debug("Retrieving client for {}: {}", server, client);
    return client;
  }
  
  
  // Private
  
  private void reapOldClients() {
    final long startMillis = System.currentTimeMillis();
    int numClientsReaped = 0;
    final Set<CycServerAddress> servers = clients.keySet();
    for (CycServerAddress server : servers) {
      final CycClient client = clients.get(server);
      if ((client == null) || client.isClosed()) {
        LOGGER.debug("Found expired client for {}: {}", server, client);
        releaseResourcesForServer(server);
        numClientsReaped++;
      }
    }
    final long duration = System.currentTimeMillis() - startMillis;
    if ((numClientsReaped > 0) || (duration > 1)) {
      LOGGER.info("Reaped {} clients in {}ms", numClientsReaped, duration);
    }
  }
  
  private void errorIfClosed(String msg) {
    if (isClosed()) {
      throw new SessionRuntimeException(getClass().getSimpleName() + " has been closed. " + msg + " " + this);
    }
  }
  
}
