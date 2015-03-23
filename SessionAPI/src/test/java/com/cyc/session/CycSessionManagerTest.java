package com.cyc.session;

/*
 * #%L
 * File: CycSessionManagerTest.java
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

import com.cyc.session.CycSessionManager.CycSessionManagerInitializationError;
import com.cyc.session.internal.TestEnvironmentProperties;
import junit.framework.TestCase;
import org.junit.Test;

/**
 *
 * @author nwinant
 */
public class CycSessionManagerTest extends TestCase {
  
  public CycSessionManagerTest(String testName) {
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
   * project, so the CycSessionManager static initialization should throw a
   * CycSessionManagerInitializationError.
   */
  @Test
  public void testInstantiateCycSessionManager() throws SessionServiceException, SessionConfigurationException {
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
   * @throws com.cyc.session.CycSessionManagerInitializationError
   * @throws com.cyc.session.SessionConfigurationException
   */
  protected void runCanInstantiateTest() throws CycSessionManagerInitializationError, SessionConfigurationException {
    SessionManager sessionMgr = CycSessionManager.get();
    System.out.println("Found SessionManager: " + sessionMgr);
    assertNotNull(sessionMgr);
  }
  
  /**
   * No SessionConnectionFactory implementation should be available within this
   * project, so the SessionManagerImpl constructor should throw a
   * SessionServiceException.
   * @throws com.cyc.session.SessionConfigurationException
   */
  protected void runCannotInstantiateTest() throws SessionConfigurationException {
    SessionManager sessionMgr = null;
    try {
      sessionMgr = CycSessionManager.get();
      fail("Should have thrown an exception, but did not.");
    } catch (CycSessionManagerInitializationError ex) {
      System.out.println("Good news! Test captured an expected exception: " + ex.getMessage());
      ex.printStackTrace(System.err);
      assertEquals(SessionServiceException.class, ex.getException().getClass());
    }
    assertNull(sessionMgr);
  }
  
}
