/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cyc.kb.client;

/*
 * #%L
 * File: FirstOrderCollectionTest.java
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

import com.cyc.kb.FirstOrderCollection;
import com.cyc.kb.client.LookupType;
import com.cyc.kb.SecondOrderCollection;
import com.cyc.kb.client.Constants;
import com.cyc.kb.client.ContextImpl;
import com.cyc.kb.client.FirstOrderCollectionImpl;
import com.cyc.kb.client.KbPredicateImpl;
import com.cyc.kb.client.SecondOrderCollectionImpl;
import com.cyc.kb.exception.KbException;

import java.util.Set;
import java.util.logging.Logger;
import org.junit.AfterClass;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author vijay
 */
public class FirstOrderCollectionTest {

  public FirstOrderCollectionTest() {
  }
  private static Logger log = null;

  @BeforeClass
  public static void setUp() throws Exception {
    log = Logger.getLogger(KbCollectionTest.class.getName());
    TestConstants.ensureInitialized();
  }

  @AfterClass
  public static void tearDown() throws Exception {
  }

  @Test
  public void testFirstOrderCollection_Constructors() throws Exception {
    System.out.println("Constructors");

    try {
      SecondOrderCollection soc = SecondOrderCollectionImpl.findOrCreate("NewSecondOrderCollectionByUser");
      FirstOrderCollection foc_bad = FirstOrderCollectionImpl.get("NewSecondOrderCollectionByUser");
      fail("Allowed a second order collection to be created using FirstOrderCollection");
    } catch (KbException sae) {
      assertTrue(true);
    }
    
    FirstOrderCollectionImpl foc2 = FirstOrderCollectionImpl.findOrCreate("NewFirstOrderCollectionByUser");
    System.out.println("Facts: " + foc2.getFacts(Constants.isa(), 1, Constants.uvMt()));
    foc2.delete();
    
    try {
      FirstOrderCollection foc3 = FirstOrderCollectionImpl.get("NewFirstOrderCollectionByUser");
      fail("Lookup mode FIND should not have found \"NewFirstOrderCollectionByUser\"");
    } catch (KbException sae) {
      assertTrue(true);
    }
  }
  
  /**
   * Test of addGeneralization method, of class FirstOrderCollection.
   */
  /*
  @Test
  public void testGeneralizations_0args() throws Exception {
    System.out.println("addGeneralization");
    FirstOrderCollection instance = new FirstOrderCollection();
    List expResult = null;
    List result = instance.addGeneralization();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }*/

  /**
   * Test of addGeneralization method, of class FirstOrderCollection.
   */
  @Test
  public void testGeneralizations_String() throws Exception {
    System.out.println("generalizations");
    
    FirstOrderCollection foc1 = FirstOrderCollectionImpl.findOrCreate("NewFirstOrderCollectionByUser");
    FirstOrderCollection foc2 = FirstOrderCollectionImpl.findOrCreate("AnotherNewFirstOrderCollectionByUser");
    foc1.addGeneralization("AnotherNewFirstOrderCollectionByUser");
    
    java.util.Collection<FirstOrderCollection> genls = foc1.getGeneralizations();
    System.out.println("Generalizations: " + genls);
    
    if (!genls.contains(FirstOrderCollectionImpl.findOrCreate("AnotherNewFirstOrderCollectionByUser"))) {
      fail("Dog genls does not contain CanisGenus");
    }
    
    foc1.delete();
    foc2.delete();
  }

  /**
   * Test of addGeneralization method, of class FirstOrderCollection.
   */
  @Test
  public void testGeneralizations_String_String() throws Exception {
    System.out.println("generalizations");
      
    FirstOrderCollectionImpl foc1 = FirstOrderCollectionImpl.findOrCreate("NewFirstOrderCollectionByUser");
    System.out.println("Facts: " + foc1.getFacts(Constants.isa(), 1, Constants.uvMt()));
    
    String ctxStr = "UniversalVocabularyMt";
    String collectionStr = "AnotherNewFirstOrderCollectionByUser";
    FirstOrderCollectionImpl foc2 = FirstOrderCollectionImpl.findOrCreate(collectionStr);
    
    FirstOrderCollection result = foc1.addGeneralization(collectionStr, ctxStr);
    assertEquals(foc1, result);
    
    java.util.Collection<FirstOrderCollection> genls = foc1.getGeneralizations();
    System.out.println("Generalizations: " + genls);
    
    if (!genls.contains(foc2)) {
      fail("\"NewFirstOrderCollectionByUser\" genls does not contain \"AnotherNewFirstOrderCollectionByUser\"");
    }
    foc1.delete();
    foc2.delete();   
  }

  /**
   * Test of addGeneralization method, of class FirstOrderCollection.
   */
  /*
  @Test
  public void testGeneralizations_Context_Collection() throws Exception {
    System.out.println("addGeneralization");
    Context ctx = null;
    Collection c = null;
    FirstOrderCollection instance = new FirstOrderCollection();
    FirstOrderCollection expResult = null;
    FirstOrderCollection result = instance.addGeneralization(ctx, c);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  */

  /**
   * Test of isInstanceOf method, of class FirstOrderCollection.
   */
  @Test
  public void testCheckSuperType() throws Exception {
    System.out.println("checkSuperType");
    
    FirstOrderCollectionImpl foc1 = FirstOrderCollectionImpl.findOrCreate("NewFirstOrderCollectionByUser");

    if (!foc1.isInstanceOf(SecondOrderCollectionImpl.get("FirstOrderCollection"), ContextImpl.get("UniversalVocabularyMt"))){     
      fail("\"NewFirstOrderCollectionByUser\" is not an isa of \"FirstOrderCollection\"");
    }
    
    foc1.delete();
  }

}
