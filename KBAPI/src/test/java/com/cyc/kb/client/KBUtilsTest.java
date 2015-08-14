/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cyc.kb.client;

/*
 * #%L
 * File: KBUtilsTest.java
 * Project: KB API Implementation
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

import com.cyc.base.cycobject.CycObject;
import com.cyc.kb.Context;
import com.cyc.kb.KBCollection;
import com.cyc.kb.KBObject;
import static com.cyc.kb.client.TestUtils.skipTest;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KBTypeException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author vijay
 */
public class KBUtilsTest {
  
  public KBUtilsTest() {
  }
  
  @BeforeClass
  public static void setUpClass() throws Exception {
    TestConstants.ensureInitialized();
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

  /**
   * Test of convertKBObjectMapToCoObjectMap method, of class KBUtils.
   */
  @Test
  public void testConvertKBObjectMapToCoObjectMap() {
    // @TODO -- Make a real test.
    System.out.println("convertKBObjectMapToCoObjectMap");
    skipTest(this, "testConvertKBObjectMapToCoObjectMap", "This test is not yet implemented.");
    Map<KBObject, Object> mapToConvert = null;
    Map<CycObject, Object> expResult = null;
    Map<CycObject, Object> result = KBUtils.convertKBObjectMapToCoObjectMap(mapToConvert);
    assertEquals(expResult, result);
    fail("The test case is a prototype.");
  }
  
  @Test 
  public void testGetKBObjectForArgument () throws KBTypeException, CreateException {
    System.out.println("getKBObjectForArgument");
    Context c = ContextImpl.get("UniversalVocabularyMt");
    Context rc = KBUtils.getKBObjectForArgument("UniversalVocabularyMt", ContextImpl.class);
    
    System.out.println("Expected: " + rc);
    System.out.println("Expected Class: " + rc.getClass());
       
    assertEquals(c, rc);
    
    try {
      Context rc2 = KBUtils.getKBObjectForArgument("Collection", ContextImpl.class);
    } catch (Exception e) {
      System.out.println("Exception: " + e.getMessage());
      System.out.println("Exception Type: " + e.getClass());
      assertEquals(IllegalArgumentException.class, e.getClass());
    }
  }

  /**
   * Test of minCol method, of class KBUtils.
   */
  @Test
  public void testMinCol() throws KBTypeException, CreateException {
    System.out.println("minCol");
    Collection<KBCollection> cols = new ArrayList<KBCollection> ();
    cols.add(KBCollectionImpl.get("Person"));
    KBCollection dog = KBCollectionImpl.get("Dog");
    cols.add(dog);
    cols.add(KBCollectionImpl.get("CanisGenus"));
    KBCollection result = KBUtils.minCol(cols, Constants.uvMt());
    assertEquals(dog, result);
  }

  /**
   * Test of minCols method, of class KBUtils.
   */
  @Test
  public void testMinCols() throws KBTypeException, CreateException {
    System.out.println("minCols");
    Collection<KBCollection> cols = new ArrayList<KBCollection> ();
    KBCollection person = KBCollectionImpl.get("Person");
    cols.add(person);
    KBCollection dog = KBCollectionImpl.get("Dog");
    cols.add(dog);
    cols.add(KBCollectionImpl.get("CanisGenus"));
    Collection<KBCollection> result = KBUtils.minCols(cols);
    assertTrue(result.contains(dog));
    assertTrue(result.contains(person));
  }

  /**
   * Test of maxCols method, of class KBUtils.
   */
  @Test
  public void testMaxCols() throws KBTypeException, CreateException {
    System.out.println("maxCols");
    Collection<KBCollection> cols = new ArrayList<KBCollection> ();
    KBCollection person = KBCollectionImpl.get("Person");
    cols.add(person);
    KBCollection dog = KBCollectionImpl.get("Dog");
    cols.add(dog);
    KBCollection canis = KBCollectionImpl.get("CanisGenus");
    cols.add(canis);
    Collection<KBCollection> result = KBUtils.maxCols(cols);
    assertTrue(result.contains(canis));
    assertTrue(result.contains(person));
  }
  
}
