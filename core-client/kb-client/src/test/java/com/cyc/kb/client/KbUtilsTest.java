/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cyc.kb.client;

/*
 * #%L
 * File: KbUtilsTest.java
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

import com.cyc.base.cycobject.CycObject;
import com.cyc.kb.Context;
import com.cyc.kb.KbCollection;
import com.cyc.kb.KbObject;
import static com.cyc.kb.client.TestUtils.skipTest;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbTypeException;
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
public class KbUtilsTest {
  
  public KbUtilsTest() {
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
    Map<KbObject, Object> mapToConvert = null;
    Map<CycObject, Object> expResult = null;
    Map<CycObject, Object> result = KbUtils.convertKBObjectMapToCoObjectMap(mapToConvert);
    assertEquals(expResult, result);
    fail("The test case is a prototype.");
  }
  
  @Test 
  public void testGetKBObjectForArgument () throws KbTypeException, CreateException {
    System.out.println("getKBObjectForArgument");
    Context c = ContextImpl.get("UniversalVocabularyMt");
    Context rc = KbUtils.getKBObjectForArgument("UniversalVocabularyMt", ContextImpl.class);
    
    System.out.println("Expected: " + rc);
    System.out.println("Expected Class: " + rc.getClass());
       
    assertEquals(c, rc);
    
    try {
      Context rc2 = KbUtils.getKBObjectForArgument("Collection", ContextImpl.class);
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
  public void testMinCol() throws KbTypeException, CreateException {
    System.out.println("minCol");
    Collection<KbCollection> cols = new ArrayList<KbCollection> ();
    cols.add(KbCollectionImpl.get("Person"));
    KbCollection dog = KbCollectionImpl.get("Dog");
    cols.add(dog);
    cols.add(KbCollectionImpl.get("CanisGenus"));
    KbCollection result = KbUtils.minCol(cols, Constants.uvMt());
    assertEquals(dog, result);
  }

  /**
   * Test of minCols method, of class KBUtils.
   */
  @Test
  public void testMinCols() throws KbTypeException, CreateException {
    System.out.println("minCols");
    Collection<KbCollection> cols = new ArrayList<KbCollection> ();
    KbCollection person = KbCollectionImpl.get("Person");
    cols.add(person);
    KbCollection dog = KbCollectionImpl.get("Dog");
    cols.add(dog);
    cols.add(KbCollectionImpl.get("CanisGenus"));
    Collection<KbCollection> result = KbUtils.minCols(cols);
    assertTrue(result.contains(dog));
    assertTrue(result.contains(person));
  }

  /**
   * Test of maxCols method, of class KBUtils.
   */
  @Test
  public void testMaxCols() throws KbTypeException, CreateException {
    System.out.println("maxCols");
    Collection<KbCollection> cols = new ArrayList<KbCollection> ();
    KbCollection person = KbCollectionImpl.get("Person");
    cols.add(person);
    KbCollection dog = KbCollectionImpl.get("Dog");
    cols.add(dog);
    KbCollection canis = KbCollectionImpl.get("CanisGenus");
    cols.add(canis);
    Collection<KbCollection> result = KbUtils.maxCols(cols);
    assertTrue(result.contains(canis));
    assertTrue(result.contains(person));
  }
  
}
