/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cyc.km.query.answer.justification;

/*
 * #%L
 * File: ProofViewJustificationTest.java
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


import org.junit.*;

import static org.junit.Assert.*;

import com.cyc.base.exception.CycConnectionException;
import com.cyc.base.cycobject.ElMt;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbException;
import com.cyc.query.QueryAnswer;
import static com.cyc.query.TestUtils.assumeNotOpenCyc;
import com.cyc.query.exception.QueryConstructionException;
import com.cyc.session.exception.SessionCommunicationException;
import com.cyc.session.exception.OpenCycUnsupportedFeatureException;
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
    assumeNotOpenCyc();
    Utils.setup();
  }
  static ProofViewJustification instance;

  @AfterClass
  public static void tearDownClass() {
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
  public void testPopulate() throws OpenCycUnsupportedFeatureException {
    System.out.println("populate");
    assumeNotOpenCyc();
    instance.populate();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testPopulateTwice() throws OpenCycUnsupportedFeatureException {
    System.out.println("populate");
    assumeNotOpenCyc();
    instance.populate();
    instance.populate();
  }

  /**
   * Test of getDomainMt method, of class ProofViewJustification.
   */
  @Test
  public void testGetDomainMt() throws SessionCommunicationException, CycConnectionException, OpenCycUnsupportedFeatureException {
    System.out.println("getDomainMt");
    assumeNotOpenCyc();
    ElMt result = instance.getDomainMt();
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
  public void testSetDomainMt() throws CycConnectionException {
    System.out.println("setDomainMt");
    instance.setDomainMt(domainMt);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testSetDomainMtException() throws CycConnectionException, OpenCycUnsupportedFeatureException {
    assumeNotOpenCyc();
    instance.populate();
    instance.setDomainMt(domainMt);
  }

  /**
   * Test of isIncludeDetails method, of class ProofViewJustification.
   */
  @Test
  public void testIsIncludeDetails() throws SessionCommunicationException, OpenCycUnsupportedFeatureException {
    System.out.println("isIncludeDetails");
    assumeNotOpenCyc();
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
  public void testSetIncludeDetails() throws SessionCommunicationException {
    System.out.println("setIncludeDetails");
    instance.setIncludeDetails(false);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testSetIncludeDetailsException() throws OpenCycUnsupportedFeatureException, SessionCommunicationException {
    assumeNotOpenCyc();
    instance.populate();
    instance.setIncludeDetails(false);
  }

  /**
   * Test of isIncludeLinear method, of class ProofViewJustification.
   */
  @Test
  public void testIsIncludeLinear() throws SessionCommunicationException, OpenCycUnsupportedFeatureException {
    System.out.println("isIncludeLinear");
    assumeNotOpenCyc();
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
  public void testSetIncludeLinear() throws SessionCommunicationException {
    System.out.println("setIncludeLinear");
    instance.setIncludeLinear(false);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testSetIncludeLinearException() throws OpenCycUnsupportedFeatureException, SessionCommunicationException {
    assumeNotOpenCyc();
    instance.populate();
    instance.setIncludeLinear(false);
  }

  /**
   * Test of isIncludeSummary method, of class ProofViewJustification.
   */
  @Test
  public void testIsIncludeSummary() throws SessionCommunicationException, OpenCycUnsupportedFeatureException {
    System.out.println("isIncludeSummary");
    assumeNotOpenCyc();
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
  public void testSetIncludeSummary() throws SessionCommunicationException {
    System.out.println("setIncludeSummary");
    instance.setIncludeSummary(false);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testSetIncludeSummaryException() throws OpenCycUnsupportedFeatureException, SessionCommunicationException {
    assumeNotOpenCyc();
    instance.populate();
    instance.setIncludeSummary(false);
  }

  /**
   * Test of getLanguageMt method, of class ProofViewJustification.
   */
  @Test
  public void testGetLanguageMt() throws SessionCommunicationException, OpenCycUnsupportedFeatureException {
    System.out.println("getLanguageMt");
    assumeNotOpenCyc();
    ElMt result = instance.getLanguageMt();
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
  public void testSetLanguageMt() throws SessionCommunicationException {
    System.out.println("setLanguageMt");
    instance.setLanguageMt(languageMt);
  }

  /**
   * Test of setRichCycLContent method, of class ProofViewJustification.
   */
  @Test
  public void testSetRichCycLContent() throws SessionCommunicationException {
    System.out.println("setRichCycLContent");
    instance.setRichCycLContent(false);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testSetRichCycLContentException() throws OpenCycUnsupportedFeatureException, SessionCommunicationException {
    assumeNotOpenCyc();
    instance.populate();
    instance.setRichCycLContent(false);
  }

  /**
   * Test of getRoot method, of class ProofViewJustification.
   */
  @Test
  public void testGetRoot() throws OpenCycUnsupportedFeatureException {
    System.out.println("getRoot");
    assumeNotOpenCyc();
    instance.populate();
    assertNotNull(instance.getRoot());
  }

  /**
   * Test of isSuppressAssertionBookkeeping method, of class ProofViewJustification.
   */
  @Test
  public void testIsSuppressAssertionBookkeeping() throws SessionCommunicationException, OpenCycUnsupportedFeatureException {
    System.out.println("isSuppressAssertionBookkeeping");
    assumeNotOpenCyc();
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
  public void testSetSuppressAssertionBookkeeping() throws SessionCommunicationException {
    System.out.println("setSuppressAssertionBookkeeping");
    instance.setSuppressAssertionBookkeeping(false);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testSetSuppressAssertionBookkeepingException() throws OpenCycUnsupportedFeatureException, SessionCommunicationException {
    assumeNotOpenCyc();
    instance.populate();
    instance.setSuppressAssertionBookkeeping(false);
  }

  /**
   * Test of isSuppressAssertionCyclists method, of class ProofViewJustification.
   */
  @Test
  public void testIsSuppressAssertionCyclists() throws SessionCommunicationException, OpenCycUnsupportedFeatureException {
    System.out.println("isSuppressAssertionCyclists");
    assumeNotOpenCyc();
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
  public void testSetSuppressAssertionCyclists() throws SessionCommunicationException {
    System.out.println("setSuppressAssertionCyclists");
    instance.setSuppressAssertionCyclists(false);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testSetSuppressAssertionCyclistsException() throws OpenCycUnsupportedFeatureException, SessionCommunicationException {
    assumeNotOpenCyc();
    instance.populate();
    instance.setSuppressAssertionCyclists(false);
  }
}
