package com.cyc.session.internal;

/*
 * #%L
 * File: CycSessionCacheTest.java
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

import com.cyc.session.CycServer;
import com.cyc.session.CycServerAddress;
import com.cyc.session.CycSession;
import com.cyc.session.SessionApiException;
import com.cyc.session.SessionConfigurationException;
import com.cyc.session.selection.CycServerSelector;
import com.cyc.session.mock.MockSession;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import junit.framework.TestCase;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;

/**
 *
 * @author nwinant
 */
public class CycSessionCacheTest extends TestCase {
  
  public CycSessionCacheTest(String testName) {
    super(testName);
  }
  
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    this.cache = new CycSessionCache();
    this.environmentLoader = new EnvironmentConfigurationLoader();
  }
  
  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
  }
  
  
  // Fields
  
  private CycSessionCache cache;
  private EnvironmentConfigurationLoader environmentLoader;
  
  
  // Tests
  
  @Test
  public void testEmptyCache() throws SessionConfigurationException {
    assertEquals(0, cache.size());
  }
  
  @Test
  public void testSingleSessionAdd() throws SessionConfigurationException {
    assertEquals(0, cache.size());
    final CycSession session = new MockSession(new CycServer("localhost", 3600));
    cache.add(session, environmentLoader.getConfiguration());
    assertEquals(1, cache.size());
    final CycSession result = (CycSession) cache.getAll().iterator().next();
    assertEquals(session, result);
  }
  
  @Test
  public void testMultipleSessionsAdd() throws SessionConfigurationException {
    final CycSession session1 = new MockSession(new CycServer("localhost", 3600));
    final CycSession session2 = new MockSession(new CycServer("localhost", 3620));
    final CycSession session3 = new MockSession(new CycServer("localhost", 3600));
    assertNotEquals(session1, session2);
    assertNotEquals(session2, session3);
    assertNotEquals(session1, session3);
    
    cache.add(session1, environmentLoader.getConfiguration());
    assertEquals(1, cache.size());
    cache.add(session2, environmentLoader.getConfiguration());
    assertEquals(2, cache.size());
    cache.add(session3, environmentLoader.getConfiguration());
    assertEquals(3, cache.size());
  }
  
  @Test
  public void testSessionUniqueness() throws SessionConfigurationException {
    final CycSession session1 = new MockSession(new CycServer("localhost", 3600));
    //final CycSession session2 = new MockSession(new CycServer("localhost", 3620));
    //final CycSession session3 = new MockSession(new CycServer("localhost", 3600));
    
    assertEquals(0, cache.size());
    cache.add(session1, environmentLoader.getConfiguration());
    assertEquals(1, cache.size());
    cache.add(session1, environmentLoader.getConfiguration());
    assertEquals(1, cache.size());
    final CycSession result1 = (CycSession) cache.getAll().iterator().next();
    assertEquals(session1, result1);
    cache.add(result1, environmentLoader.getConfiguration());
    assertEquals(1, cache.size());
  }
  
  @Test
  public void testContains() throws SessionConfigurationException {
    final CycSession session1 = new MockSession(new CycServer("localhost", 3600));
    final CycSession session2 = new MockSession(new CycServer("localhost", 3620));
    final CycSession session3 = new MockSession(new CycServer("localhost", 3600));
    assertFalse(cache.contains(session1));
    assertFalse(cache.contains(session2));
    assertFalse(cache.contains(session3));
    
    cache.add(session1, environmentLoader.getConfiguration());
    assertEquals(1, cache.size());
    assertTrue(cache.contains(session1));
    assertFalse(cache.contains(session2));
    assertFalse(cache.contains(session3));
    
    cache.add(session2, environmentLoader.getConfiguration());
    assertEquals(2, cache.size());
    assertTrue(cache.contains(session1));
    assertTrue(cache.contains(session2));
    assertFalse(cache.contains(session3));
    
    cache.add(session3, environmentLoader.getConfiguration());
    assertEquals(3, cache.size());
    assertTrue(cache.contains(session1));
    assertTrue(cache.contains(session2));
    assertTrue(cache.contains(session3));
  }
  
  @Test
  public void testRemove() throws SessionConfigurationException {
    final CycSession session1 = new MockSession(new CycServer("localhost", 3600));
    final CycSession session2 = new MockSession(new CycServer("localhost", 3620));
    final CycSession session3 = new MockSession(new CycServer("localhost", 3600));
    
    assertEquals(0, cache.size());
    cache.add(session1, environmentLoader.getConfiguration());
    assertEquals(1, cache.size());
    assertTrue(cache.contains(session1));
    
    cache.remove(session1);
    assertEquals(0, cache.size());
    assertFalse(cache.contains(session1));
    
    cache.add(session1, environmentLoader.getConfiguration());
    assertEquals(1, cache.size());
    assertTrue(cache.contains(session1));
    
    cache.remove(session1);
    assertEquals(0, cache.size());
    assertFalse(cache.contains(session1));
    
    cache.add(session1, environmentLoader.getConfiguration());
    cache.add(session2, environmentLoader.getConfiguration());
    cache.add(session3, environmentLoader.getConfiguration());
    assertEquals(3, cache.size());
    assertTrue(cache.contains(session1));
    assertTrue(cache.contains(session2));
    assertTrue(cache.contains(session3));
    
    cache.remove(session2);
    assertEquals(2, cache.size());
    assertTrue(cache.contains(session1));
    assertFalse(cache.contains(session2));
    assertTrue(cache.contains(session3));
    
    cache.remove(session1);
    assertEquals(1, cache.size());
    assertFalse(cache.contains(session1));
    assertFalse(cache.contains(session2));
    assertTrue(cache.contains(session3));
    
    cache.remove(session3);
    assertEquals(0, cache.size());
    assertFalse(cache.contains(session1));
    assertFalse(cache.contains(session2));
    assertFalse(cache.contains(session3));
  }
  
  @Test
  public void testGetAllByCycServer() throws SessionConfigurationException, SessionApiException {
    final CycServerAddress server = new CycServer("localhost", 3600);
    final CycSession session1 = new MockSession(new CycServer("localhost", 3600));
    final CycSession session2 = new MockSession(new CycServer("localhost", 3620));
    final CycSession session3 = new MockSession(new CycServer("localhost", 3600));
    assertEquals(0, cache.size());
    cache.add(session1, environmentLoader.getConfiguration());
    cache.add(session2, environmentLoader.getConfiguration());
    cache.add(session3, environmentLoader.getConfiguration());
    assertEquals(3, cache.size());
    
    final Set<CycSession> results = cache.getAll(new CycServerSelector(server));
    assertEquals(2, results.size());
    final Iterator i = results.iterator();
    assertNotEquals(i.next(), i.next());
    for (CycSession session : results) {
      assertEquals(server, session.getServerInfo().getCycServer());
    }
  }
  
}
