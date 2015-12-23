/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cyc.km.modeling.task;

/*
 * #%L
 * File: CycBasedTaskTest.java
 * Project: Query Client
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

import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbException;
import com.cyc.kb.exception.KbTypeException;
import static org.junit.Assert.*;
import org.junit.*;
import static com.cyc.query.TestUtils.*;
import com.cyc.session.exception.OpenCycUnsupportedFeatureException;

/**
 *
 * @author baxter
 */
public class CycBasedTaskTest {

  public CycBasedTaskTest() {
  }
  static private CycBasedTask t;

  @BeforeClass
  public static void setUpClass() throws Exception {
    assumeNotOpenCyc();
    ensureConstantsInitialized();
    t = CycBasedTask.getAll().iterator().next();
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  /**
   * Test of getAll method, of class CycBasedTask.
   */
  @Test
  public void testGetAll() throws KbTypeException, CreateException, OpenCycUnsupportedFeatureException {
    System.out.println("getAll");
    assertFalse(CycBasedTask.getAll().isEmpty());
  }

  /**
   * Test of getTaskTerm method, of class CycBasedTask.
   */
  @Test
  public void testGetTaskTerm() {
    System.out.println("getTaskTerm");
    assertNotNull(t.getTaskTerm());
  }

  /**
   * Test of getDescription method, of class CycBasedTask.
   */
  @Test
  public void testGetDescription() throws KbException {
    System.out.println("getDescription");
    t.getDescription();
  }

  /**
   * Test of getSummary method, of class CycBasedTask.
   */
  @Test
  public void testGetSummary() throws KbTypeException, CreateException {
    System.out.println("getSummary");
    t.getSummary();
  }

  /**
   * Test of getAssignedCyclists method, of class CycBasedTask.
   */
  @Test
  public void testGetAssignedCyclists() throws KbException {
    System.out.println("getAssignedCyclists");
    t.getAssignedCyclists();
  }

  /**
   * Test of getKeyConcepts method, of class CycBasedTask.
   */
  @Test
  public void testGetKeyConcepts() throws CreateException, KbException {
    System.out.println("getKeyConcepts");
    t.getKeyConcepts();
  }
}
