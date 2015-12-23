/*
 * Copyright 2015 Cycorp, Inc.
 *
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
 */
package com.cyc.kb.service;

/*
 * #%L
 * File: AssertionServiceImplTest.java
 * Project: KB Client
 * %%
 * Copyright (C) 2013 - 2015 Cycorp, Inc
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

import static com.cyc.baseclient.CommonConstants.ISA;
import com.cyc.baseclient.testing.TestIterator.IteratedTest;
import com.cyc.kb.Assertion;
import com.cyc.kb.Context;
import com.cyc.kb.Assertion.Direction;
import com.cyc.kb.Assertion.Strength;
import com.cyc.kb.KbTerm;
import com.cyc.kb.client.AssertionImpl;
import com.cyc.kb.client.ContextImpl;
import com.cyc.kb.client.KbIndividualImpl;
import com.cyc.kb.client.KbPredicateImpl;
import com.cyc.kb.client.KbTermImpl;
import com.cyc.kb.client.SentenceImpl;
import com.cyc.kb.client.TestConstants;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.DeleteException;
import com.cyc.kb.exception.KbTypeException;
import com.cyc.kb.service.examples.ServiceTestExamplesInKb;
import static com.cyc.kb.service.examples.ServiceTestExamplesInKb.CITY_NAMED_FN_AUSTIN_HLID;
import static com.cyc.kb.service.examples.ServiceTestExamplesInKb.CITY_NAMED_FN_AUSTIN_SENTENCE_STRING;
import static com.cyc.kb.service.examples.ServiceTestExamplesInKb.CTX;
import static com.cyc.kb.service.examples.ServiceTestExamplesInKb.CTX_STR;
import static com.cyc.kb.service.examples.ServiceTestExamplesInKb.FLYING_DONE_BY_PILOT_FACT;
import static com.cyc.kb.service.examples.ServiceTestExamplesInKb.FLYING_DONE_BY_PILOT_SENTENCE;
import static com.cyc.kb.service.examples.ServiceTestExamplesNotInKb.FLYING_DONE_BY_PILOT_SENTENCE_STRING;
import static com.cyc.kb.service.examples.ServiceTestUtils.TEST_ITERATOR;
import static com.cyc.kb.service.examples.ServiceTestUtils.trimString;
import java.util.List;
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
public class AssertionServiceImplTest {
  
  public AssertionServiceImplTest() {
  }
  
  @BeforeClass
  public static void setUpClass() throws Exception {
    TestConstants.ensureInitialized();
  }
  
  @AfterClass
  public static void tearDownClass() {
  }
  
  @Before
  public void setUp() throws KbTypeException, CreateException {
    instance = new AssertionServiceImpl();
    nameOrId = "TestConst-" + System.currentTimeMillis();
    commercialAircraft = KbTermImpl.get("CommercialAircraft");
  }
  
  @After
  public void tearDown() throws KbTypeException, CreateException, DeleteException {
    if (KbTermImpl.existsAsType(nameOrId)) {
      KbTermImpl.get(nameOrId).delete();
    }
  }
  
  
  // Fields
  
  AssertionServiceImpl instance;
  String nameOrId;
  KbTerm commercialAircraft;
  
  private KbTerm createTestConstant() throws CreateException, KbTypeException {
    return KbTermImpl.findOrCreate(nameOrId);
  }
  
  
  // Tests

  /* *
   * Test of get method, of class AssertionServiceImpl.
   * /
  @Test
  public void testGet_String() throws Exception {
    System.out.println("get");
    SentenceImpl formula = new SentenceImpl(
            KBPredicateImpl.get("flyingDoneBySomething-Operate"),
            KBIndividualImpl.get("FlyingAPlane-APITest"),
            KBIndividualImpl.get("Pilot-APITest"));
    Assertion expResult = AssertionImpl.findOrCreate(formula, CTX);
    String hlid = expResult.getId();
    Object result = instance.get(hlid);
    assertEquals(expResult, result);
  }
  */
  
  /**
   * Test of get method, of class AssertionServiceImpl.
   */
  @Test
  public void testGet_String_HLID() throws Exception {
    System.out.println("testGet_String_HLID");
    /*
    Object result = instance.get(CITY_NAMED_FN_AUSTIN_HLID);
    System.out.println(result);
    System.out.println(result.getClass().getName());
    //assertEquals(expResult, result);
    */
    final List<String> failures = TEST_ITERATOR.testValidObjects(
            ServiceTestExamplesInKb.ALL_ASSERTION_HLIDS_IN_KB,
            new IteratedTest<String>() {
              @Override
              public boolean isValidObject(String string) throws Exception {
                return AssertionImpl.get(trimString(string)).equals(
                        instance.get(string));
              }
            });
    assertEquals(0, failures.size());
  }
  
  /* *
   * Test of get method, of class AssertionServiceImpl.
   * /
  @Test
  public void testGet_String_Sentence() throws Exception {
    System.out.println("testGet_String_Sentence");
    Object result = instance.get(CITY_NAMED_FN_AUSTIN_SENTENCE_STRING);
    System.out.println(result);
    System.out.println(result.getClass().getName());
    //assertEquals(expResult, result);
  }
  */
  
  /**
   * Test of get method, of class AssertionServiceImpl.
   */
  @Test
  public void testGet_String_String() throws Exception {
    System.out.println("get");
    Object result = instance.get(FLYING_DONE_BY_PILOT_SENTENCE_STRING, CTX_STR);
    assertEquals(FLYING_DONE_BY_PILOT_FACT, result);
  }

  /**
   * Test of get method, of class AssertionServiceImpl.
   */
  @Test
  public void testGet_Sentence_Context() throws Exception {
    System.out.println("get");
    Object result = instance.get(FLYING_DONE_BY_PILOT_SENTENCE, CTX);
    assertEquals(FLYING_DONE_BY_PILOT_FACT, result);
  }

  /**
   * Test of findOrCreate method, of class AssertionServiceImpl.
   */
  @Test
  public void testFindOrCreate_String() throws Exception {
    System.out.println("findOrCreate");
    createTestConstant();
    String formulaStr = "(isa " + nameOrId + " CommercialAircraft)";
    AssertionImpl result = instance.findOrCreate(formulaStr);
    Assertion expResult = AssertionImpl.get(result.getId());
    assertEquals(expResult, result);
  }

  /**
   * Test of findOrCreate method, of class AssertionServiceImpl.
   */
  @Test
  public void testFindOrCreate_String_String() throws Exception {
    System.out.println("findOrCreate");
    createTestConstant();
    String formulaStr = "(isa " + nameOrId + " CommercialAircraft)";
    AssertionImpl result = instance.findOrCreate(formulaStr, CTX_STR);
    Assertion expResult = AssertionImpl.get(result.getId());
    assertEquals(expResult, result);
  }

  /**
   * Test of findOrCreate method, of class AssertionServiceImpl.
   */
  @Test
  public void testFindOrCreate_4args_1() throws Exception {
    System.out.println("findOrCreate");
    createTestConstant();
    String formulaStr = "(isa " + nameOrId + " CommercialAircraft)";
    // TODO: flesh out testing of Strength and Direction - nwinant, 2015-11-09
    AssertionImpl result = instance.findOrCreate(formulaStr, CTX_STR, Strength.AUTO, Direction.AUTO);
    Assertion expResult = AssertionImpl.get(result.getId());
    assertEquals(expResult, result);
  }

  /**
   * Test of findOrCreate method, of class AssertionServiceImpl.
   */
  @Test
  public void testFindOrCreate_Sentence() throws Exception {
    System.out.println("findOrCreate");
    KbTerm term = createTestConstant();
    SentenceImpl formula = new SentenceImpl(
            ISA,
            term,
            commercialAircraft);
    AssertionImpl result = instance.findOrCreate(formula);
    Assertion expResult = AssertionImpl.get(result.getId());
    assertEquals(expResult, result);
  }

  /**
   * Test of findOrCreate method, of class AssertionServiceImpl.
   */
  @Test
  public void testFindOrCreate_Sentence_Context() throws Exception {
    System.out.println("findOrCreate");
    KbTerm term = createTestConstant();
    SentenceImpl formula = new SentenceImpl(
            ISA,
            term,
            commercialAircraft);
    AssertionImpl result = instance.findOrCreate(formula, CTX);
    Assertion expResult = AssertionImpl.get(result.getId());
    assertEquals(expResult, result);
  }

  /**
   * Test of findOrCreate method, of class AssertionServiceImpl.
   */
  @Test
  public void testFindOrCreate_4args_2() throws Exception {
    System.out.println("findOrCreate");
    KbTerm term = createTestConstant();
    SentenceImpl formula = new SentenceImpl(
            ISA,
            term,
            commercialAircraft);
    // TODO: flesh out testing of Strength and Direction - nwinant, 2015-11-09
    AssertionImpl result = instance.findOrCreate(formula, CTX, Strength.AUTO, Direction.AUTO);
    Assertion expResult = AssertionImpl.get(result.getId());
    assertEquals(expResult, result);
  }
  
}
