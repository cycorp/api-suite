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
 * File: ConvenienceServiceImplTest.java
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

import com.cyc.baseclient.testing.TestIterator.IteratedTest;
import com.cyc.kb.client.AssertionImpl;
import com.cyc.kb.client.KbIndividualImpl;
import com.cyc.kb.client.KbTermImpl;
import com.cyc.kb.client.SentenceImpl;
import com.cyc.kb.client.SymbolImpl;
import static com.cyc.kb.client.TestUtils.NOT_RCYC_4_0Q;
import static com.cyc.kb.client.TestUtils.assumeCycSessionRequirement;
import com.cyc.kb.client.VariableImpl;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbRuntimeException;
import com.cyc.kb.exception.KbTypeException;
import com.cyc.kb.service.examples.ServiceTestExamplesInKb;
import com.cyc.kb.service.examples.ServiceTestExamplesNotInKb;
import com.cyc.kb.service.examples.ServiceTestUtils;
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
public class ConvenienceServiceImplTest {
  
  public ConvenienceServiceImplTest() {
  }
  
  @BeforeClass
  public static void setUpClass() throws KbTypeException, CreateException {
  }
  
  @AfterClass
  public static void tearDownClass() {
  }
  
  @Before
  public void setUp() {
    KbFactoryServicesImpl services = new KbFactoryServicesImpl();
    instance = services.getConvenienceService();
  }
  
  @After
  public void tearDown() {
  }
  
  
  // Fields
  
  private ConvenienceServiceImpl instance;

  
  // Tests
  
  @Test(expected = KbRuntimeException.class)
  public void testExistsInKB_Null() throws CreateException, KbTypeException {
    System.out.println("testExistsInKB_Null");
    instance.existsInKb(null);
  }
  
  @Test(expected = KbRuntimeException.class)
  public void testExistsInKB_Empty() {
    System.out.println("testExistsInKB_Empty");
    instance.existsInKb("");
  }
  
  @Test(expected = KbRuntimeException.class)
  public void testExistsInKB_Whitespace() {
    System.out.println("testExistsInKB_Whitespace");
    instance.existsInKb("         ");
  }
  
  @Test
  public void testExistsInKB() throws CreateException, KbTypeException {
    System.out.println("existsInKB");
    final List<String> failures = TEST_ITERATOR.testValidAndInvalidObjects(
            ServiceTestExamplesInKb.ALL_EXAMPLES_IN_KB,
            ServiceTestExamplesNotInKb.ALL_EXAMPLES_NOT_IN_KB,
            new IteratedTest<String>() {
              @Override
              public boolean isValidObject(String string) throws Exception {
                return instance.existsInKb(string);
              }
            });
    assertEquals(0, failures.size());
  }
  
  @Test(expected = KbRuntimeException.class)
  public void testGetKbObject_Null() throws CreateException, KbTypeException {
    System.out.println("getKbObject_Null");
    instance.getKbObject(null);
  }
  
  @Test(expected = KbRuntimeException.class)
  public void testGetKbObject_Empty() throws CreateException, KbTypeException {
    System.out.println("getKbObject_Empty");
    instance.getKbObject("");
  }
  
  @Test(expected = KbRuntimeException.class)
  public void testGetKbObject_Whitespace() throws CreateException, KbTypeException {
    System.out.println("getKbObject_Whitespace");
    instance.getKbObject("         ");
  }
  
  @Test
  public void testGetKbObject_Assertion_1() throws Exception {
    System.out.println("getKbObject_Assertion");
    final List<String> failures = TEST_ITERATOR.testValidObjects(
            ServiceTestExamplesInKb.ALL_ASSERTION_HLIDS_IN_KB,
            new IteratedTest<String>() {
              @Override
              public boolean isValidObject(String string) throws Exception {
                return AssertionImpl.get(trimString(string)).equals(
                        instance.getKbObject(string));
              }
            });
    assertEquals(0, failures.size());

    // Note that Assertions can only be retrieved by the HLID for the assertion:
    assertEquals(ServiceTestExamplesInKb.FLYING_DONE_BY_PILOT_FACT,
            instance.getKbObject(ServiceTestExamplesInKb.FLYING_DONE_BY_PILOT_HLID));
    assertEquals(
            new SentenceImpl(ServiceTestExamplesNotInKb.FLYING_DONE_BY_PILOT_SENTENCE_STRING),
            instance.getKbObject(ServiceTestExamplesNotInKb.FLYING_DONE_BY_PILOT_SENTENCE_STRING));
    assertEquals(
            new SentenceImpl(ServiceTestExamplesNotInKb.IST_FLYING_DONE_BY_PILOT_SENTENCE_STRING),
            instance.getKbObject(ServiceTestExamplesNotInKb.IST_FLYING_DONE_BY_PILOT_SENTENCE_STRING));
  }

  @Test
  public void testGetKbObject_Assertion_2() throws Exception {
    assumeCycSessionRequirement(NOT_RCYC_4_0Q); // TODO: why isn't this working with RCyc 4.0q?
    assertNotEquals(KbIndividualImpl.get(ServiceTestExamplesNotInKb.FLYING_DONE_BY_PILOT_SENTENCE_STRING),
            KbIndividualImpl.get(ServiceTestExamplesNotInKb.IST_FLYING_DONE_BY_PILOT_SENTENCE_STRING)
    );
  }
  
  @Test
  public void testGetKbObject_AtomicTerm() throws Exception {
    System.out.println("testGetKbObject_AtomicTerm");
    final List<String> failures = TEST_ITERATOR.testValidObjects(ServiceTestUtils.ALL_TERMS,
            new IteratedTest<String>() {
              @Override
              public boolean isValidObject(String string) throws Exception {
                return KbTermImpl.get(trimString(string)).equals(
                        instance.getKbObject(string));
              }
            });
    assertEquals(0, failures.size());
  }
  
  @Test
  public void testGetKbObject_Terms_In_Kb() throws Exception {
    System.out.println("testGetKbObject_Terms_In_Kb");
    final List<String> failures = TEST_ITERATOR.testValidObjects(ServiceTestExamplesInKb.ALL_TERMS_IN_KB,
            new IteratedTest<String>() {
              @Override
              public boolean isValidObject(String string) throws Exception {
                return KbTermImpl.get(trimString(string)).equals(
                        instance.getKbObject(string));
              }
            });
    assertEquals(0, failures.size());
  }
  
  @Test
  public void testGetKbObject_Terms_Not_In_Kb() throws Exception {
    System.out.println("testGetKbObject_Terms_Not_In_Kb");
    final List<String> failures = TEST_ITERATOR.testValidObjects(ServiceTestExamplesNotInKb.ALL_TERMS_NOT_IN_KB,
            new IteratedTest<String>() {
              @Override
              public boolean isValidObject(String string) throws Exception {
                return KbTermImpl.get(trimString(string)).equals(
                        instance.getKbObject(string));
              }
            });
    assertEquals(0, failures.size());
  }
  
  @Test
  public void testGetKbObject_Sentence() throws Exception {
    System.out.println("getKbObject_Sentence");
    final List<String> failures = TEST_ITERATOR.testValidObjects(
            ServiceTestExamplesNotInKb.SENTENCES,
            new IteratedTest<String>() {
              @Override
              public boolean isValidObject(String string) throws Exception {
                return new SentenceImpl(trimString(string)).equals(
                        instance.getKbObject(string));
              }
            });
    assertEquals(0, failures.size());
  }
  
  @Test
  public void testGetKbObject_Symbol() throws Exception {
    System.out.println("testGetKbObject_Symbol");
    final List<String> failures = TEST_ITERATOR.testValidObjects(
            ServiceTestExamplesNotInKb.SYMBOLS,
            new IteratedTest<String>() {
              @Override
              public boolean isValidObject(String string) throws Exception {
                return new SymbolImpl(trimString(string)).equals(
                        instance.getKbObject(string));
              }
            });
    assertEquals(0, failures.size());
  }

  @Test
  public void testGetKbObject_Variable() throws Exception {
    System.out.println("testGetKbObject_Variable");
    List<String> failures = TEST_ITERATOR.testValidObjects(
            ServiceTestExamplesNotInKb.VARIABLES,
            new IteratedTest<String>() {
              @Override
              public boolean isValidObject(String string) throws Exception {
                return new VariableImpl(trimString(string)).equals(
                        instance.getKbObject(string));
              }
            });
    assertEquals(0, failures.size());
  }
  
}
