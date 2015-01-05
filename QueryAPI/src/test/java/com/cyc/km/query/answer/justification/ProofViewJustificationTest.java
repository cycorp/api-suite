/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cyc.km.query.answer.justification;

/*
 * #%L
 * File: ProofViewJustificationTest.java
 * Project: Query API
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


import org.junit.*;

import static org.junit.Assert.*;

import com.cyc.base.CycConnectionException;
import com.cyc.base.cycobject.ELMt;
import com.cyc.query.QueryAnswer;
import com.cyc.xml.query.Utils;

import static com.cyc.xml.query.Utils.*;

/**
 *
 * @author baxter
 */
public class ProofViewJustificationTest {

  public ProofViewJustificationTest() {
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
    Utils.setup();
  }
  static ProofViewJustification instance;

  @AfterClass
  public static void tearDownClass() throws Exception {
    Utils.teardown();
  }

  @Before
  public void setUp() throws Exception {
    instance = new ProofViewJustification(answer);
  }

  @After
  public void tearDown() {
  }

  /**
   * Test of getAnswer method, of class ProofViewJustification.
   */
  @Test
  public void testGetAnswer() {
    System.out.println("getAnswer");
    QueryAnswer expResult = answer;
    QueryAnswer result = instance.getQueryAnswer();
    System.out.println("Expected " + expResult + "\nGot " + result);
    assertEquals(expResult, result);
  }

  /**
   * Test of populate method, of class ProofViewJustification.
   */
  @Test
  public void testPopulate() {
    System.out.println("populate");
    instance.populate();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testPopulateTwice() {
    System.out.println("populate");
    instance.populate();
    instance.populate();
  }

  /**
   * Test of getDomainMt method, of class ProofViewJustification.
   */
  @Test
  public void testGetDomainMt() throws Exception {
    System.out.println("getDomainMt");
    ELMt result = instance.getDomainMt();
    assertNotNull("Domain mt was null", result);
    instance.setDomainMt(domainMt);
    assertEquals(domainMt, instance.getDomainMt());
    instance.populate();
    assertEquals(domainMt, instance.getDomainMt());
  }

  /**
   * Test of setDomainMt method, of class ProofViewJustification.
   */
  @Test
  public void testSetDomainMt() throws Exception {
    System.out.println("setDomainMt");
    instance.setDomainMt(domainMt);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testSetDomainMtException() throws CycConnectionException {
    instance.populate();
    instance.setDomainMt(domainMt);
  }

  /**
   * Test of isIncludeDetails method, of class ProofViewJustification.
   */
  @Test
  public void testIsIncludeDetails() throws Exception {
    System.out.println("isIncludeDetails");
    instance.isIncludeDetails();
    instance.setIncludeDetails(true);
    assertEquals(true, instance.isIncludeDetails());
    instance.setIncludeDetails(false);
    assertEquals(false, instance.isIncludeDetails());
    instance.populate();
    assertEquals(false, instance.isIncludeDetails());
  }

  /**
   * Test of setIncludeDetails method, of class ProofViewJustification.
   */
  @Test
  public void testSetIncludeDetails() throws Exception {
    System.out.println("setIncludeDetails");
    instance.setIncludeDetails(false);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testSetIncludeDetailsException() throws CycConnectionException {
    instance.populate();
    instance.setIncludeDetails(false);
  }

  /**
   * Test of isIncludeLinear method, of class ProofViewJustification.
   */
  @Test
  public void testIsIncludeLinear() throws Exception {
    System.out.println("isIncludeLinear");
    instance.isIncludeLinear();
    instance.setIncludeLinear(true);
    assertEquals(true, instance.isIncludeLinear());
    instance.setIncludeLinear(false);
    assertEquals(false, instance.isIncludeLinear());
    instance.populate();
    assertEquals(false, instance.isIncludeLinear());
  }

  /**
   * Test of setIncludeLinear method, of class ProofViewJustification.
   */
  @Test
  public void testSetIncludeLinear() throws Exception {
    System.out.println("setIncludeLinear");
    instance.setIncludeLinear(false);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testSetIncludeLinearException() throws CycConnectionException {
    instance.populate();
    instance.setIncludeLinear(false);
  }

  /**
   * Test of isIncludeSummary method, of class ProofViewJustification.
   */
  @Test
  public void testIsIncludeSummary() throws Exception {
    System.out.println("isIncludeSummary");
    instance.isIncludeSummary();
    instance.setIncludeSummary(true);
    assertEquals(true, instance.isIncludeSummary());
    instance.setIncludeSummary(false);
    assertEquals(false, instance.isIncludeSummary());
    instance.populate();
    assertEquals(false, instance.isIncludeSummary());
  }

  /**
   * Test of setIncludeSummary method, of class ProofViewJustification.
   */
  @Test
  public void testSetIncludeSummary() throws Exception {
    System.out.println("setIncludeSummary");
    instance.setIncludeSummary(false);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testSetIncludeSummaryException() throws CycConnectionException {
    instance.populate();
    instance.setIncludeSummary(false);
  }

  /**
   * Test of getLanguageMt method, of class ProofViewJustification.
   */
  @Test
  public void testGetLanguageMt() throws Exception {
    System.out.println("getLanguageMt");
    ELMt result = instance.getLanguageMt();
    assertNotNull("Language mt was null", result);
    instance.setLanguageMt(languageMt);
    assertEquals(languageMt, instance.getLanguageMt());
    instance.populate();
    assertEquals(languageMt, instance.getLanguageMt());
  }

  /**
   * Test of setLanguageMt method, of class ProofViewJustification.
   */
  @Test
  public void testSetLanguageMt() throws Exception {
    System.out.println("setLanguageMt");
    instance.setLanguageMt(languageMt);
  }

  /**
   * Test of setRichCycLContent method, of class ProofViewJustification.
   */
  @Test
  public void testSetRichCycLContent() throws Exception {
    System.out.println("setRichCycLContent");
    instance.setRichCycLContent(false);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testSetRichCycLContentException() throws CycConnectionException {
    instance.populate();
    instance.setRichCycLContent(false);
  }

  /**
   * Test of getRoot method, of class ProofViewJustification.
   */
  @Test
  public void testGetRoot() {
    System.out.println("getRoot");
    instance.populate();
    assertNotNull(instance.getRoot());
  }

  /**
   * Test of isSuppressAssertionBookkeeping method, of class ProofViewJustification.
   */
  @Test
  public void testIsSuppressAssertionBookkeeping() throws Exception {
    System.out.println("isSuppressAssertionBookkeeping");
    instance.isSuppressAssertionBookkeeping();
    instance.setSuppressAssertionBookkeeping(true);
    assertEquals(true, instance.isSuppressAssertionBookkeeping());
    instance.setSuppressAssertionBookkeeping(false);
    assertEquals(false, instance.isSuppressAssertionBookkeeping());
    instance.populate();
    assertEquals(false, instance.isSuppressAssertionBookkeeping());
  }

  /**
   * Test of setSuppressAssertionBookkeeping method, of class ProofViewJustification.
   */
  @Test
  public void testSetSuppressAssertionBookkeeping() throws Exception {
    System.out.println("setSuppressAssertionBookkeeping");
    instance.setSuppressAssertionBookkeeping(false);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testSetSuppressAssertionBookkeepingException() throws CycConnectionException {
    instance.populate();
    instance.setSuppressAssertionBookkeeping(false);
  }

  /**
   * Test of isSuppressAssertionCyclists method, of class ProofViewJustification.
   */
  @Test
  public void testIsSuppressAssertionCyclists() throws Exception {
    System.out.println("isSuppressAssertionCyclists");
    instance.isSuppressAssertionCyclists();
    instance.setSuppressAssertionCyclists(true);
    assertEquals(true, instance.isSuppressAssertionCyclists());
    instance.setSuppressAssertionCyclists(false);
    assertEquals(false, instance.isSuppressAssertionCyclists());
    instance.populate();
    assertEquals(false, instance.isSuppressAssertionCyclists());
  }

  /**
   * Test of setSuppressAssertionCyclists method, of class ProofViewJustification.
   */
  @Test
  public void testSetSuppressAssertionCyclists() throws Exception {
    System.out.println("setSuppressAssertionCyclists");
    instance.setSuppressAssertionCyclists(false);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testSetSuppressAssertionCyclistsException() throws CycConnectionException {
    instance.populate();
    instance.setSuppressAssertionCyclists(false);
  }
}
