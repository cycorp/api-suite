package com.cyc.base;

/*
 * #%L
 * File: CycAccessManagerIT.java
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

import com.cyc.baseclient.testing.TestUtils;
import com.cyc.base.CycAccess;
import com.cyc.base.CycAccessManager;
import com.cyc.base.cycobject.FormulaSentence;
import com.cyc.baseclient.inference.params.DefaultInferenceParameters;
import com.cyc.baseclient.cycobject.CycVariableImpl;
import com.cyc.baseclient.cycobject.CycFormulaSentence;
import com.cyc.base.cycobject.ELMt;
import com.cyc.base.inference.InferenceParameters;
import com.cyc.base.inference.InferenceResultSet;
import com.cyc.baseclient.CycClientManager;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static com.cyc.baseclient.CommonConstants.*;
import static com.cyc.baseclient.testing.TestConstants.*;
import com.cyc.session.CycServer;
import com.cyc.session.CycSession;
import com.cyc.session.CycSession.SessionStatus;
import com.cyc.session.CycSessionConfiguration;
import com.cyc.session.CycSessionManager;
import com.cyc.session.SessionApiException;
import com.cyc.session.SessionCommandException;
import com.cyc.session.SessionCommunicationException;
import com.cyc.session.SessionConfigurationException;
import com.cyc.session.SessionInitializationException;
import com.cyc.session.SessionManager;
import com.cyc.session.internal.ConfigurationValidator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;

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
    final CycAccess access = CycAccessManager.getAccess();
    assertNotNull(access);
    final String kbVersion = access.getServerInfo().getCycKBVersionString();
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
    final CycAccess access = CycAccessManager.getAccess();
    final CycSession session = access.getCycSession();
    assertNotNull(session);
    assertEquals(SessionStatus.CONNECTED, session.getStatus());
    assertEquals(access.getCycServer(), session.getServerInfo().getCycServer());
    
    final String kbVersion = session.getServerInfo().getCycKBVersionString();
    System.out.println("KB version:  " + kbVersion);
    assertNotNull(kbVersion);
    assertFalse(kbVersion.trim().isEmpty());
    assertEquals(access.getServerInfo().getCycKBVersionString(), kbVersion);
    
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
   * @throws SessionApiException 
   */
  //@Test
  public void testLegacyCycAccessManager() throws SessionApiException {
    final CycSession session1 = CycSessionManager.getCurrentSession();
    final CycAccess cyc1 = CycAccessManager.getCurrentAccess();
    final CycServer server1 = cyc1.getServerInfo().getCycServer();
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
  
}
