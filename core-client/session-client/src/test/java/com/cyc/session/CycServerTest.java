/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cyc.session;

/*
 * #%L
 * File: CycServerTest.java
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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author nwinant
 */
public class CycServerTest {
  
  public CycServerTest() {
  }
  
  @BeforeClass
  public static void setUpClass() {
  }
  
  @AfterClass
  public static void tearDownClass() {
  }
  
  @Before
  public void setUp() {
  }
  
  @After
  public void tearDown() {
  }

  @Test
  public void testResolveHostName() {
    final String localhost = "localhost";
    final String localIP = "127.0.01";
    
    final CycServer server1 = new CycServer(localhost, 3600);
    System.out.println("Resolved " + server1.getHostName() + " to " + server1.getResolvedHostName());
    assertNotEquals(localhost, server1.getResolvedHostName());
    assertNotEquals(localIP, server1.getResolvedHostName());

    final CycServer server2 = new CycServer(localIP, 3600);
    System.out.println("Resolved " + server2.getHostName() + " to " + server2.getResolvedHostName());
    assertNotEquals(localhost, server2.getResolvedHostName());
    assertNotEquals(localIP, server2.getResolvedHostName());
    
    assertNotEquals(server1, server2);
    assertNotEquals(server1.getHostName(), server2.getHostName());
    assertEquals(server1.getResolvedHostName(), server2.getResolvedHostName());
  }
  
  @Test
  public void testIsDefined() {
    assertFalse(new CycServer(null, 3600).isDefined());
    assertFalse(new CycServer(" ", 3600).isDefined());
    assertTrue(new CycServer("somehost", 3600).isDefined());
  }
}
