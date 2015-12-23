package com.cyc.session.mock.tests;

/*
 * #%L
 * File: MockSessionTest.java
 * Project: Session Client
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

import com.cyc.session.internal.*;
import com.cyc.session.CycServer;
import com.cyc.session.CycSession;
import com.cyc.session.exception.SessionServiceException;
import com.cyc.session.spi.SessionManager;
import com.cyc.session.exception.SessionConfigurationException;
import com.cyc.session.spi.SessionFactory;
import com.cyc.session.mock.MockSession;
import java.util.List;
import junit.framework.TestCase;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;

/**
 *
 * @author nwinant
 */
public class MockSessionTest extends TestCase {
  
  public MockSessionTest(String testName) {
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
  
  
  // Tests
  
  @Test
  public void testEquality() throws SessionConfigurationException {
    final CycSession session1 = new MockSession(new CycServer("localhost", 3600));
    final CycSession session2 = new MockSession(new CycServer("localhost", 3620));
    final CycSession session3 = new MockSession(new CycServer("localhost", 3600));
    assertEquals(session1, session1);
    assertEquals(session2, session2);
    assertEquals(session3, session3);
    assertNotEquals(session1, session2);
    assertNotEquals(session2, session3);
    assertNotEquals(session1, session3);
  }
  
  @Test
  public void testGetServerInfo() throws SessionConfigurationException {
    final CycSession session = new MockSession(new CycServer("localhost", 3600));
    assertNotNull(session.getServerInfo());
    assertEquals(new CycServer("localhost", 3600), session.getServerInfo().getCycServer());
  }
  
  @Test
  public void testGetConfiguration() throws SessionConfigurationException {
    final CycSession session = new MockSession(new CycServer("localhost", 3600));
    assertNotNull(session.getConfiguration());
    assertEquals(new CycServer("localhost", 3600), session.getConfiguration().getCycServer());
  }
  
}
