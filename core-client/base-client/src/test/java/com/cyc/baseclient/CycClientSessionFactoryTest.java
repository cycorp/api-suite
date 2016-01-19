package com.cyc.baseclient;

/*
 * #%L
 * File: CycClientSessionFactoryTest.java
 * Project: Base Client
 * %%
 * Copyright (C) 2013 - 2016 Cycorp, Inc.
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

import com.cyc.session.exception.SessionInitializationException;
import com.cyc.session.exception.SessionConfigurationException;
import com.cyc.session.exception.SessionCommunicationException;
import com.cyc.session.exception.SessionCommandException;
import com.cyc.session.*;
import com.cyc.base.exception.CycConnectionException;
import com.cyc.session.CycSession.ConnectionStatus;
import com.cyc.session.exception.SessionRuntimeException;
import com.cyc.session.internal.ImmutableConfiguration;
import java.io.IOException;
import junit.framework.TestCase;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;

/**
 *
 * @author nwinant
 */
public class CycClientSessionFactoryTest extends TestCase {
  
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    this.factory = new CycClientSessionFactory();
    this.config = CycSessionManager.getInstance().getConfiguration();
    assertTrue(factory.getServers().isEmpty());
    
  }
  
  @Override
  protected void tearDown() throws Exception {
    try {
      super.tearDown();
    } finally {
      if (!factory.isClosed()) {
        factory.close();
      }
    }
  }
  
  
  // Fields
  
  private CycClientSessionFactory factory;
  private CycSessionConfiguration config;
  
  
  // Tests
  
  @Test
  public void testCreateSession() throws SessionConfigurationException {
    final CycClientSession session1 = factory.createSession(config);
    assertNotNull(session1);
    assertEquals(ConnectionStatus.UNINITIALIZED, session1.getConnectionStatus());
    assertNull(session1.getAccess());
    assertNotNull(session1.getConfiguration().getCycServer());
    assertEquals(config.getCycServer(), session1.getConfiguration().getCycServer());
    
    final CycClientSession session2 = factory.createSession(config);
    assertNotNull(session2);
    assertEquals(ConnectionStatus.UNINITIALIZED, session1.getConnectionStatus());
    assertNull(session2.getAccess());
    assertNotNull(session2.getConfiguration().getCycServer());
    assertEquals(config.getCycServer(), session2.getConfiguration().getCycServer());
    
    assertNotEquals(session1, session2);
    
    Exception ex = null;
    try {
      session1.getServerInfo().getCycServer();
    } catch (NullPointerException npe) {
      ex = npe;
    }
    assertNotNull(ex);
  }
  
  @Test
  public void testInitializeSession() throws SessionConfigurationException, SessionCommunicationException, SessionInitializationException, SessionCommandException {
    final CycClientSession session = factory.createSession(config);
    assertNotNull(session);
    assertEquals(ConnectionStatus.UNINITIALIZED, session.getConnectionStatus());
    assertNull(session.getAccess());
    
    factory.initializeSession(session);
    assertEquals(ConnectionStatus.CONNECTED, session.getConnectionStatus());
    assertNotNull(session.getAccess());
    assertFalse(session.getAccess().isClosed());
    
    assertNotNull(session.getServerInfo().getCycServer());
    assertEquals(session.getConfiguration().getCycServer(), session.getServerInfo().getCycServer());
    assertFalse(session.getServerInfo().getCycKbVersionString().trim().isEmpty());
  }
  
  @Test
  public void testCycClientCaching() throws SessionConfigurationException, SessionCommunicationException, SessionInitializationException, CycConnectionException {
    assertTrue(factory.getServers().isEmpty());
    
    final CycClientSession session1 = factory.createSession(config);
    assertEquals(0, factory.getServers().size());
    
    final CycClientSession session2 = factory.createSession(config);
    assertEquals(0, factory.getServers().size());
    
    factory.initializeSession(session1);
    assertEquals(1, factory.getServers().size());
    
    factory.initializeSession(session2);
    assertEquals(1, factory.getServers().size());
    assertEquals(session1.getAccess(), session2.getAccess());
    
    final CycServerAddress server = factory.getServers().iterator().next();
    final ImmutableConfiguration serverConfig = new ImmutableConfiguration(server, this.getClass());
    final CycClient client = factory.getClient(serverConfig);
    assertEquals(client, session1.getAccess());
    assertEquals(client, session2.getAccess());
    assertEquals(server, session1.getServerInfo().getCycServer());
    assertEquals(server, session2.getServerInfo().getCycServer());
  }
  
  @Test
  public void testReleaseResourcesForServer() throws SessionConfigurationException, SessionCommunicationException, SessionInitializationException, InterruptedException {
    assertTrue(factory.getServers().isEmpty());
    
    final CycClientSession session = factory.createSession(config);
    assertEquals(0, factory.getServers().size());
    factory.initializeSession(session);
    assertEquals(1, factory.getServers().size());
    assertEquals(ConnectionStatus.CONNECTED, session.getConnectionStatus());
    
    final CycServerAddress server = session.getServerInfo().getCycServer();
    factory.releaseResourcesForServer(server);
    assertEquals(0, factory.getServers().size());
    assertEquals(ConnectionStatus.DISCONNECTED, session.getConnectionStatus());
  }
  
  @Test
  public void testClose() throws IOException, SessionCommunicationException, SessionInitializationException, SessionConfigurationException {
    assertFalse(factory.isClosed());
    
    final CycClientSession initializedSession = factory.createSession(config);
    
    factory.initializeSession(initializedSession);
    assertNotNull(initializedSession.getAccess());
    assertFalse(factory.getServers().isEmpty());
    assertEquals(ConnectionStatus.CONNECTED, initializedSession.getConnectionStatus());
    
    final CycClientSession uninitializedSession = factory.createSession(config);
    
    factory.close();
    assertEquals(ConnectionStatus.DISCONNECTED, initializedSession.getConnectionStatus());
    assertTrue(factory.isClosed());
    assertTrue(factory.getServers().isEmpty());
    
    assertEquals(ConnectionStatus.UNINITIALIZED, uninitializedSession.getConnectionStatus());
    Exception ex1 = null;
    try {
      factory.initializeSession(uninitializedSession);
    } catch (SessionRuntimeException sare) {
      ex1 = sare;
    }
    assertNotNull(ex1);
    
    Exception ex2 = null;
    try {
      factory.createSession(config);
    } catch (SessionRuntimeException sare) {
      ex2 = sare;
    }
    assertNotNull(ex2);
  }
  
}
