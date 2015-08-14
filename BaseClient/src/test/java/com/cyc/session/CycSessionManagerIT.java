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

import com.cyc.base.CycAccess;
import com.cyc.base.CycAccessManager;
import static com.cyc.baseclient.testing.TestConstants.CYC_ADMINISTRATOR;
import static com.cyc.baseclient.testing.TestConstants.LENAT;
import static com.cyc.baseclient.testing.TestUtils.skipTest;
import com.cyc.session.internal.SessionManagerImpl;
import junit.framework.TestCase;
import static org.junit.Assert.assertTrue;
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

  public void testSingletonAccessor() throws Exception {
    SessionManager result = CycSessionManager.get();
    assertNotNull(result);
    assertTrue(SessionManagerImpl.class.isInstance(result));
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
  
  @Test
  public void testSetCyclist() throws Exception {
    System.out.println("\n**** testSetCyclist ****");
    
    final CycSession session = CycSessionManager.getCurrentSession();
    final CycAccess cyc = CycAccessManager.getCurrentAccess();
    
    session.getOptions().reset();
    assertNotNull(session.getOptions());
    assertNull(session.getOptions().getCyclistName());
    assertNotNull(cyc.getOptions());
    assertNull(cyc.getOptions().getCyclistName());
    
    // Set via CycSession w/ String toString
    session.getOptions().setCyclistName(CYC_ADMINISTRATOR.toString());
    assertEquals("CycAdministrator", session.getOptions().getCyclistName());
    assertEquals(session.getOptions().getCyclistName(),
            cyc.getOptions().getCyclistName());
    assertEquals("CycAdministrator", cyc.getOptions().getCyclistName());
    assertEquals(CYC_ADMINISTRATOR, cyc.getOptions().getCyclist());

    // Set via CycAccess w/ String toString
    cyc.getOptions().setCyclistName(LENAT.toString());
    assertEquals("Lenat", cyc.getOptions().getCyclistName());
    assertEquals(LENAT, cyc.getOptions().getCyclist());
    assertEquals(session.getOptions().getCyclistName(),
            cyc.getOptions().getCyclistName());
    assertEquals("Lenat", session.getOptions().getCyclistName());
    
    // Clear cyclist via CycSession
    session.getOptions().clearCyclist();
    assertNull(session.getOptions().getCyclistName());
    assertNull(cyc.getOptions().getCyclistName());
    
    // Set via CycAccess w/ Fort
    cyc.getOptions().setCyclist(CYC_ADMINISTRATOR);
    assertEquals("CycAdministrator", session.getOptions().getCyclistName());
    assertEquals(session.getOptions().getCyclistName(),
            cyc.getOptions().getCyclistName());
    assertEquals("CycAdministrator", cyc.getOptions().getCyclistName());
    assertEquals(CYC_ADMINISTRATOR, cyc.getOptions().getCyclist());
    
    // Clear cyclist via CycAccess
    cyc.getOptions().clearCyclist();
    assertNull(cyc.getOptions().getCyclistName());
    assertNull(session.getOptions().getCyclistName());
    
    // Set via CycSession w/ String stringApiValue
    session.getOptions().setCyclistName(LENAT.stringApiValue());
    assertEquals("Lenat", session.getOptions().getCyclistName());
    assertEquals(session.getOptions().getCyclistName(),
            cyc.getOptions().getCyclistName());
    assertEquals("Lenat", cyc.getOptions().getCyclistName());
    assertEquals(LENAT, cyc.getOptions().getCyclist());

    session.getOptions().reset();
    assertNull(session.getOptions().getCyclistName());
    assertNull(cyc.getOptions().getCyclistName());
    
    System.out.println("**** testSetCyclist OK ****");
  }
}
