package com.cyc.session.internal;

/*
 * #%L
 * File: SessionManagerImplTest.java
 * Project: Session API Implementation
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

import com.cyc.session.SessionServiceException;
import com.cyc.session.SessionManager;
import com.cyc.session.SessionConfigurationException;
import com.cyc.session.connection.SessionFactory;
import java.util.List;
import junit.framework.TestCase;
import org.junit.Test;

/**
 *
 * @author nwinant
 */
public class SessionManagerImplTest extends TestCase {
  
  public SessionManagerImplTest(String testName) {
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
  
  /**
   * No SessionConnectionFactory implementation should be available within this
   * project, so this should return nothing.
   * @throws SessionConfigurationException 
   */
  @Test
  public void testLoadAllSessionManagers() throws SessionConfigurationException {
    final List<SessionFactory> factories = SessionManagerImpl.loadAllSessionFactories();
    System.out.println("SessionFactories found: " + factories.size());
    for (SessionFactory factory : factories) {
      System.out.println(" - " + factory);
    }
    if (TestEnvironmentProperties.get().isConnectionFactoryExpectedOnClassPath()) {
      assertFalse(factories.isEmpty());
    } else {
      assertTrue(factories.isEmpty());
    }
  }
  
  /**
   * Test whether the SessionManagerImpl can successfully instantiate.
   * 
   * @throws com.cyc.session.SessionConfigurationException
   */
  @Test
  public void testInstantiationBehavior() throws SessionServiceException, SessionConfigurationException {
    System.out.println("TestEnvProperties.get().isConnectionFactoryExpectedOnClassPath()=" 
            + TestEnvironmentProperties.get().isConnectionFactoryExpectedOnClassPath());
    if (TestEnvironmentProperties.get().isConnectionFactoryExpectedOnClassPath()) {
      runCanInstantiateTest();
    } else {
      runCannotInstantiateTest();
    }
  }
  
  /**
   * A SessionConnectionFactory implementation is expected on the
   * classpath, so the SessionManagerImpl constructor should be able
   * to find one.
   */
  protected void runCanInstantiateTest() throws SessionServiceException, SessionConfigurationException {
    SessionManager sessionMgr = new SessionManagerImpl();
    System.out.println("Found SessionManager: " + sessionMgr);
    assertNotNull(sessionMgr);
  }
  
  /**
   * No SessionConnectionFactory implementation should be available within this
   * project, so the SessionManagerImpl constructor should throw a
   * SessionServiceException.
   */
  protected void runCannotInstantiateTest() throws SessionConfigurationException {
    SessionManager sessionMgr = null;
    try {
      sessionMgr = new SessionManagerImpl();
      fail("Should have thrown an exception, but did not.");
    } catch (SessionServiceException ex) {
      System.out.println("Good news! Test captured an expected exception: " + ex.getMessage());
      assertNotNull(ex);
      assertEquals(SessionFactory.class, ex.getInterfaceClass());
    }
    assertNull(sessionMgr);
  }
}
