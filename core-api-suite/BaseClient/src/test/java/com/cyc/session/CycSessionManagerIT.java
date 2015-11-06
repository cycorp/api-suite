package com.cyc.session;

/*
 * #%L
 * File: CycSessionManagerIT.java
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

import com.cyc.baseclient.CycClient;
import com.cyc.baseclient.CycClientSession;
import static com.cyc.baseclient.testing.TestUtils.skipTest;
import com.cyc.session.internal.SessionManagerImpl;
import junit.framework.TestCase;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;

/**
 *
 * @author nwinant
 */
public class CycSessionManagerIT extends TestCase {
  
  public CycSessionManagerIT(String testName) {
    super(testName);
  }
  
  @Override
  protected void setUp() throws Exception {
    super.setUp();
  }
  
  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
  }

  @Test
  public void testSingletonAccessor() throws Exception {
    SessionManager result = CycSessionManager.getInstance();
    assertNotNull(result);
    System.out.println("SessionManager implementation: " + result.getClass().getName());
    assertTrue(SessionManagerImpl.class.isInstance(result));
  }
  
  @Test
  public void testReloadSessionManager() throws SessionServiceException, SessionConfigurationException {
    final SessionManager mgr1 = CycSessionManager.getInstance();
    assertNotNull(mgr1);
    assertFalse(mgr1.isClosed());
    
    CycSessionManager.reloadInstance();
    
    final SessionManager mgr2 = CycSessionManager.getInstance();
    assertNotNull(mgr2);
    assertNotEquals(mgr1, mgr2);
    assertTrue(mgr1.isClosed());
    assertFalse(mgr2.isClosed());
  }
  
  /*
  @Test
  public void testSessionClosingViaTryWithResources() throws SessionConfigurationException, SessionCommunicationException, SessionInitializationException {
    // Sources are still expected to build to Java 6, so this test is currently disabled. However,
    // it does pass when sources are built as Java 7.
    // TODO: re-enable once APIs have moved to Java 7.
    final CycSession session1;
    try (CycSession session = CycSessionManager.getCurrentSession()) {
      session1 = session;
      assertFalse(session1.isClosed());
    }
    assertTrue(session1.isClosed());
    CycSession session2 = CycSessionManager.getCurrentSession();
    assertFalse(session2.isClosed());
  }
  */
  
  @Test
  public void testClientClosing() throws SessionConfigurationException, SessionCommunicationException, SessionInitializationException {
    final CycClientSession session1 = (CycClientSession) CycSessionManager.getCurrentSession();
    final CycClient client1 = session1.getAccess();
    assertFalse(client1.isClosed());
    assertFalse(session1.isClosed());
    client1.close();
    assertTrue(client1.isClosed());
    assertFalse(session1.isClosed()); // CycSession does not yet know that its CycAccess is closed.
    
    // TODO: CycSessions really ought to have a listener to self-update when their CycAccess is closed. - nwinant, 2015-10-27
    
    // Retrieving a new session forces the SessionManager to recognize that the underlying CycAccess
    // is closed, and to close & replace the session:
    final CycClientSession session2 = (CycClientSession) CycSessionManager.getCurrentSession();
    final CycClient client2 = session2.getAccess();
    assertFalse(client2.isClosed());
    assertFalse(session2.isClosed());
    
    assertTrue(client1.isClosed());
    assertTrue(session1.isClosed());
    
    assertNotEquals(session1, session2);
    assertNotEquals(client1, client2);
    
    assertNotEquals(session1, CycSessionManager.getCurrentSession());
    assertEquals(session2, CycSessionManager.getCurrentSession());
  }
  
  @Test 
  public void testSessionReaping() throws SessionConfigurationException, SessionCommunicationException, SessionInitializationException {
    // TODO: Could be fleshed out a bit more, with some multithreaded tests, 
    //       possibly broken out into a separate test class. - nwinant, 2015-10-16
    
    final CycSession session1 = CycSessionManager.getCurrentSession();
    final int h1 = session1.hashCode();
    System.out.println("Hash for session 1: " + h1);
    session1.close();
    
    final CycSession session2 = CycSessionManager.getCurrentSession();
    final int h2 = session2.hashCode();
    System.out.println("Hash for session 2: " + h2);
    session2.close();
    assertNotEquals(h1, h2);
    
    final CycSession session3 = CycSessionManager.getCurrentSession();
    final int h3 = session3.hashCode();
    System.out.println("Hash for session 3: " + h3);
    session3.close();
    assertNotEquals(h2, h3);
  }
  
  /*
  public void testGetEnvironmentProperties() throws SessionConfigurationException {
    // TODO: nwinant, 2015-07-06
    skipTest(this, "", 
            "This test needs to be moved somewhere where it won't pick up the actual Session Manager");
    
    CycSessionConfiguration result = CycSessionManager.get().getEnvironmentConfiguration();
    assertNotNull(result);
    assertNull(result.getCycServer());
    assertNull(result.getConfigurationFileName());
    assertNull(result.getConfigurationLoaderName());
  }
  
  public void testSystemProperties() throws Exception {
    // TODO: nwinant, 2015-07-06
    skipTest(this, "", 
            "This test needs to be moved somewhere where it won't pick up the actual Session Manager");
    
    final CycServer expectedServer = new CycServer("localhost", 3620);
    System.setProperty(SessionConfigurationProperties.SERVER_KEY, "localhost:3620");
    assertEquals(expectedServer, CycSessionManager.get().getSession().getServerInfo().getCycServer());
  }
  
  public void testChangingSystemProperties() throws Exception {
    // TODO: nwinant, 2015-07-06
    skipTest(this, "", 
            "This test needs to be moved somewhere where it won't pick up the actual Session Manager");
    
    final CycServer server1 = new CycServer("localhost", 3620);
    final CycServer server2 =  new CycServer("localhost", 3660);
    
    System.setProperty(SessionConfigurationProperties.SERVER_KEY, "localhost:3620");
    System.out.println(CycSessionManager.get().getSession().getServerInfo().getCycServer());
    assertEquals(server1, CycSessionManager.get().getSession().getServerInfo().getCycServer());
    
    System.setProperty(SessionConfigurationProperties.SERVER_KEY, "localhost:3660");
    System.out.println(CycSessionManager.get().getSession().getServerInfo().getCycServer());
    assertEquals(server2, CycSessionManager.get().getSession().getServerInfo().getCycServer());
    
    System.setProperty(SessionConfigurationProperties.SERVER_KEY, "localhost:3620");
    System.out.println(CycSessionManager.get().getSession().getServerInfo().getCycServer());
    assertEquals(server1, CycSessionManager.get().getSession().getServerInfo().getCycServer());
  }

  public void testInteractive() throws Exception {
    // TODO: nwinant, 2015-07-06
    skipTest(this, "", 
            "This test needs to be moved somewhere where it won't pick up the actual Session Manager");
    
    final CycServer expectedServer = new CycServer("localhost", 3620);
    System.setProperty(SessionConfigurationProperties.CONFIGURATION_LOADER_KEY, SimpleInteractiveLoader.NAME);
    assertEquals(expectedServer, CycSessionManager.get().getSession().getServerInfo().getCycServer());
  }
  */
}
