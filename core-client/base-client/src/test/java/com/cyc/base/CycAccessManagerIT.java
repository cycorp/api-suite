package com.cyc.base;

/*
 * #%L
 * File: CycAccessManagerIT.java
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

import com.cyc.baseclient.CycClientManager;
import org.junit.Test;
import com.cyc.session.CycServer;
import com.cyc.session.CycServerAddress;
import com.cyc.session.CycSession;
import com.cyc.session.CycSession.ConnectionStatus;
import com.cyc.session.CycSessionConfiguration;
import com.cyc.session.CycSessionManager;
import com.cyc.session.exception.SessionException;
import com.cyc.session.exception.SessionCommandException;
import com.cyc.session.exception.SessionCommunicationException;
import com.cyc.session.exception.SessionConfigurationException;
import com.cyc.session.exception.SessionInitializationException;
import com.cyc.session.spi.SessionManager;
import com.cyc.session.internal.ConfigurationValidator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CycAccessManagerIT  {

  @Test
  public void testCycAccessManagerAccessor() {
    assertNotNull(CycAccessManager.getAccessManager());
    assertTrue(CycClientManager.class.isInstance(CycAccessManager.getAccessManager()));
  }
  
  @Test
  public void testGetSessionManager() throws SessionConfigurationException, SessionCommunicationException, SessionInitializationException {
    final SessionManager sessionMgr = CycAccessManager.getAccessManager().getSessionMgr();
    assertNotNull(sessionMgr);
  }
  
  @Test
  public void testGetSessionManagerConfig() throws SessionConfigurationException, SessionCommunicationException, SessionInitializationException {
    final SessionManager sessionMgr = CycAccessManager.getAccessManager().getSessionMgr();
    final CycSessionConfiguration config = sessionMgr.getConfiguration();
    assertNotNull(config);
    final ConfigurationValidator validator = new ConfigurationValidator(config);
    validator.print();
    assertTrue(validator.isSufficient());
  }
  
  @Test
  public void testGetAccess() throws SessionConfigurationException, SessionCommunicationException, SessionInitializationException, SessionCommandException {
    final CycAccess access = CycAccessManager.getCurrentAccess();
    assertNotNull(access);
    final String kbVersion = access.getServerInfo().getCycKbVersionString();
    System.out.println("KB version:  " + kbVersion);
    assertNotNull(kbVersion);
    assertFalse(kbVersion.trim().isEmpty());
    final String kbRevision = access.getServerInfo().getCycRevisionString();
    System.out.println("KB revision: " + kbRevision);
    assertNotNull(kbRevision);
    assertFalse(kbRevision.trim().isEmpty());
  }
  
  @Test
  public void testAccessGetCycSession() throws SessionConfigurationException, SessionCommunicationException, SessionInitializationException, SessionCommandException {
    final CycAccess access = CycAccessManager.getCurrentAccess();
    final CycSession session = access.getCycSession();
    assertNotNull(session);
    assertEquals(ConnectionStatus.CONNECTED, session.getConnectionStatus());
    assertEquals(access.getCycServer(), session.getServerInfo().getCycServer());
    
    final String kbVersion = session.getServerInfo().getCycKbVersionString();
    System.out.println("KB version:  " + kbVersion);
    assertNotNull(kbVersion);
    assertFalse(kbVersion.trim().isEmpty());
    assertEquals(access.getServerInfo().getCycKbVersionString(), kbVersion);
    
    final String kbRevision = session.getServerInfo().getCycRevisionString();
    System.out.println("KB revision: " + kbRevision);
    assertNotNull(kbRevision);
    assertFalse(kbRevision.trim().isEmpty());
    assertEquals(access.getServerInfo().getCycRevisionString(), kbRevision);
  }
  
  /**
   * This test assume two images, the secondary one running on localhost:3660.
   * Test test is disabled by default.
   * 
   * @throws SessionException 
   */
  //@Test
  public void testLegacyCycAccessManager() throws SessionException {
    final CycSession session1 = CycSessionManager.getCurrentSession();
    final CycAccess cyc1 = CycAccessManager.getCurrentAccess();
    final CycServerAddress server1 = cyc1.getServerInfo().getCycServer();
    assertEquals(session1, cyc1.getCycSession());
    assertEquals(server1, session1.getServerInfo().getCycServer());
     
    final CycServer server2 = new CycServer("localhost", 3660);
    assertNotEquals(server1, server2);
    
    final CycAccess cyc2 = CycAccessManager.get().getAccess(server2);
    final CycSession session2 = CycAccessManager.get().getSession(server2);
    assertEquals(session2, cyc2.getCycSession());
    assertEquals(server2, session2.getServerInfo().getCycServer());
    assertNotEquals(session1, session2);
    assertNotEquals(cyc1, cyc2);
    
    assertEquals(session1, CycSessionManager.getCurrentSession());
    assertEquals(cyc1, CycAccessManager.getCurrentAccess());
    assertNotEquals(session2, CycSessionManager.getCurrentSession());
    assertNotEquals(cyc2, CycAccessManager.getCurrentAccess());
    
    final CycSession currSession = CycAccessManager.get().setCurrentSession(server2);
    assertNotEquals(session1, CycSessionManager.getCurrentSession());
    assertNotEquals(session1, currSession);
    assertNotEquals(cyc1, CycAccessManager.getCurrentAccess());
    assertEquals(session2, CycSessionManager.getCurrentSession());
    assertEquals(session2, currSession);
    assertEquals(cyc2, CycAccessManager.getCurrentAccess());
  }
  
  //@Test
  public void testCloseCycAccessOnFail() throws SessionConfigurationException, SessionInitializationException, InterruptedException{
    try {
      final CycServer server = new CycServer("nonexisting-host", 3600);
      CycAccessManager.get().setCurrentSession(new CycServer("nonexisting-host", 3600));
      CycAccessManager.getCurrentAccess();
    } catch (SessionCommunicationException ex) {
      ex.printStackTrace(System.out);
    }
    for (int i=0; i<=70; i++) {
      System.out.print(".");
      if ((i + 1) % 10 == 0) {
        System.out.println("  " + (i + 1));
      }
      Thread.sleep(1000);
    }
    System.out.println("... Should have thrown a CycTimeOutException by now.");
  }
  
  //@Test
  public void testSetCurrentSession() throws SessionException {
    CycServer server = new CycServer("localhost", 3600);
    System.out.println(server);
    CycAccessManager.get().setCurrentSession(server);
    CycSession session = CycSessionManager.getCurrentSession();
    assertEquals(server, session.getConfiguration().getCycServer());
  }
}
