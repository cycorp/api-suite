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
 * File: KbTermServiceImplTest.java
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
import com.cyc.baseclient.testing.TestIterator.IteratedTest;
import com.cyc.kb.Context;
import com.cyc.kb.KbCollection;
import com.cyc.kb.KbStatus;
import com.cyc.kb.client.ContextImpl;
import com.cyc.kb.client.KbCollectionImpl;
import com.cyc.kb.client.KbObjectImpl;
import com.cyc.kb.client.KbTermImpl;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.DeleteException;
import com.cyc.kb.exception.KbTypeException;
import com.cyc.kb.service.examples.ServiceTestExamplesInKb;
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
public class KbTermServiceImplTest {
  
  public KbTermServiceImplTest() {
  }
  
  @BeforeClass
  public static void setUpClass() {
  }
  
  @AfterClass
  public static void tearDownClass() {
  }
  
  @Before
  public void setUp() throws KbTypeException, CreateException {
    instance = new KbTermServiceImpl();
    nameOrId = "TestConst-" + System.currentTimeMillis();
    constraintCol = KbCollectionImpl.get(constraintColStr);
    ctx = ContextImpl.get(ctxStr);
  }
  
  @After
  public void tearDown() throws KbTypeException, CreateException, DeleteException {
    if (KbTermImpl.existsAsType(nameOrId)) {
      KbTermImpl.get(nameOrId).delete();
    }
  }
  
  
  // Fields
  
  KbTermServiceImpl instance;
  String nameOrId;
  String constraintColStr = "CommercialAircraft";
  String ctxStr = "SomeAirlineLogMt";
  KbCollection constraintCol;
  Context ctx;
  
  
  // Tests
  
  /**
   * Test of existsAsType method, of class KbTermServiceImplTest.
   */
  @Test
  public void testExistsAsType_String() {
    System.out.println("existsAsType");
    assertTrue(instance.existsAsType("Predicate"));
    assertFalse(instance.existsAsType("BlarghArg"));
  }
  
  /**
   * Test of findOrCreate method, of class KbTermServiceImplTest.
   */
  @Test
  public void testFindOrCreate_String() throws Exception {
    System.out.println("findOrCreate");
    KbObjectImpl result = instance.findOrCreate(nameOrId);
    KbObjectImpl expResult = KbTermImpl.get(nameOrId);
    assertEquals(expResult, result);
  }

  /**
   * Test of findOrCreate method, of class KbTermServiceImplTest.
   */
  @Test
  public void testFindOrCreate_String_String() throws Exception {
    System.out.println("findOrCreate");
    KbObjectImpl result = instance.findOrCreate(nameOrId, constraintColStr);
    KbObjectImpl expResult = KbTermImpl.get(nameOrId);
    assertEquals(expResult, result);
  }

  /**
   * Test of findOrCreate method, of class KbTermServiceImplTest.
   */
  @Test
  public void testFindOrCreate_String_KBCollection() throws Exception {
    System.out.println("findOrCreate");
    KbObjectImpl result = instance.findOrCreate(nameOrId, constraintCol);
    KbObjectImpl expResult = KbTermImpl.get(nameOrId);
    assertEquals(expResult, result);
  }

  /**
   * Test of findOrCreate method, of class KbTermServiceImplTest.
   */
  @Test
  public void testFindOrCreate_3args_1() throws Exception {
    System.out.println("findOrCreate");
    KbObjectImpl result = instance.findOrCreate(nameOrId, constraintColStr, ctxStr);
    KbObjectImpl expResult = KbTermImpl.get(nameOrId);
    assertEquals(expResult, result);
  }

  /**
   * Test of findOrCreate method, of class KbTermServiceImplTest.
   */
  @Test
  public void testFindOrCreate_3args_2() throws Exception {
    System.out.println("findOrCreate");
    KbObjectImpl result = instance.findOrCreate(nameOrId, constraintCol, ctx);
    KbObjectImpl expResult = KbTermImpl.get(nameOrId);
    assertEquals(expResult, result);
  }

  /**
   * Test of get method, of class KbTermServiceImplTest.
   */
  @Test
  public void testGet_String() throws Exception {
    System.out.println("get");
    String name = "SomeAirlineLogMt";
    KbObjectImpl result = instance.get(name);
    KbObjectImpl expResult = KbTermImpl.get(name);
    assertEquals(expResult, result);
  }

  /**
   * Test of getStatus method, of class KbTermServiceImplTest.
   */
  @Test
  public void testGetStatus_String() {
    System.out.println("getStatus");
    KbStatus result = instance.getStatus(nameOrId);
    KbStatus expResult = KbTermImpl.getStatus(nameOrId);
    assertEquals(expResult, result);
  }
  
  /**
   * Test of getObjectType method, of class KbTermServiceImplTest.
   */
  @Test
  public void testGetObjectType() {
    System.out.println("getObjectType");
    Class result = instance.getObjectType();
    assertEquals(KbTermImpl.class, result);
  }
  
  @Test
  public void testGet_Terms_In_Kb() throws Exception {
    System.out.println("testGet_Terms_In_Kb");
    final List<String> failures = TEST_ITERATOR.testValidObjects(ServiceTestExamplesInKb.ALL_TERMS_IN_KB,
            new IteratedTest<String>() {
              @Override
              public boolean isValidObject(String string) throws Exception {
                return KbTermImpl.get(trimString(string)).equals(
                        instance.get(string));
              }
            });
    assertEquals(0, failures.size());
  }
  
  /* *
   * Test of existsAsType method, of class KbTermServiceImplTest.
   * /
  @Test
  public void testExistsAsType_CycObject() {
  
    //TODO: flesh this out - nwinant, 2015-11-09
  
    System.out.println("existsAsType");
    boolean expResult = false;
    boolean result = instance.existsAsType(cycObject);
    assertEquals(expResult, result);
  }
  */
  
  /* *
   * Test of findOrCreate method, of class KbTermServiceImplTest.
   * /
  @Test
  public void testFindOrCreate_CycObject() throws Exception {
    
    //TODO: flesh this out - nwinant, 2015-11-09
    
    System.out.println("findOrCreate");
    KBObjectImpl expResult = null;
    KBObjectImpl result = instance.findOrCreate(cycObject);
    assertEquals(expResult, result);
  }
  */

  /* *
   * Test of get method, of class KbTermServiceImplTest.
   * /
  @Test
  public void testGet_CycObject() throws Exception {
    
    //TODO: flesh this out - nwinant, 2015-11-09
    
    System.out.println("get");
    KBObjectImpl expResult = null;
    KBObjectImpl result = instance.get(cycObject);
    assertEquals(expResult, result);
  }
  */

  /* *
   * Test of getStatus method, of class KbTermServiceImplTest.
   * /
  @Test
  public void testGetStatus_CycObject() {
    
    //TODO: flesh this out - nwinant, 2015-11-09
    
    System.out.println("getStatus");
    CycObject cycObject = null;
    KBStatus expResult = null;
    KBStatus result = instance.getStatus(cycObject);
    assertEquals(expResult, result);
  }
  */
  
}
