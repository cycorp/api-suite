package com.cyc.kb.client;

/*
 * #%L
 * File: KbIndividualTest.java
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

import com.cyc.base.CycAccessManager;
import com.cyc.base.cycobject.CycConstant;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.DenotationalTerm;
import com.cyc.base.cycobject.Fort;
import com.cyc.baseclient.cycobject.NartImpl;
import com.cyc.kb.KbCollection;
import com.cyc.kb.KbIndividual;
import com.cyc.kb.KbStatus;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbException;
import com.cyc.kb.exception.KbRuntimeException;
import com.cyc.kb.exception.KbTypeException;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.AfterClass;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class KbIndividualTest {

  private static Logger log = null;

  @BeforeClass
  public static void setUpClass() throws Exception {
    log = Logger.getLogger(KbIndividualTest.class.getName());
    TestConstants.ensureInitialized();
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
    if(TestConstants.getCyc().getLookupTool().find("NewConstantNotInKB") != null) {
      TestConstants.getCyc().getUnassertTool().kill(TestConstants.getCyc().getLookupTool().getKnownConstantByName("NewConstantNotInKB"));
    }
  }
  
  @Before
  public void setUp() {
    
  }
  
  @After
  public void tearDown() {
    // Do not do this. We setup a detailed script based on flying event.
    // KBObjectFactory.clearKBObjectCache();
  }
  

  /**
   * Test of get method, of class KBIndividual.
   * @throws java.lang.Exception
   */
  @Test
  public void testGet_String() throws Exception {
    System.out.println("get");
    String nameOrId1 = "AlbertEinstein";
    KbIndividualImpl kbi1 = KbIndividualImpl.get(nameOrId1);
    assertEquals(KbIndividualImpl.class, kbi1.getClass());
    String expResult1 = TestConstants.getCyc().cyclifyString(nameOrId1);
    String result1 = kbi1.getCore().cyclify();
    assertEquals(expResult1, result1);
    
    String nameOrId2 = "(GenericInstanceFn Dog)";
    KbIndividualImpl kbi2 = KbIndividualImpl.get(nameOrId2);
    String expResult2 = TestConstants.getCyc().cyclifyString(nameOrId2);
    String result2 = kbi2.getCore().cyclify();
    assertEquals(expResult2, result2);
  }

  /**
   * Test of get method, of class KBIndividual.
   * @throws java.lang.Exception
   */
  @Test
  public void testGet_CycObject() throws Exception {
    System.out.println("get");
    String nameOrId1 = "AlbertEinstein";
    CycObject object1 = TestConstants.getCyc().getLookupTool().getKnownConstantByName(nameOrId1);
    KbIndividualImpl kbi1 = KbIndividualImpl.get(object1);
    assertEquals(KbIndividualImpl.class, kbi1.getClass());
    String expResult1 = TestConstants.getCyc().cyclifyString(nameOrId1);
    String result1 = kbi1.getCore().cyclify();
    assertEquals(expResult1, result1);
    
    Fort object2_functor = TestConstants.getCyc().getLookupTool().getKnownFortByName("GenericInstanceFn");
    CycConstant object2_arg = TestConstants.getCyc().getLookupTool().getKnownConstantByName("Dog");
    CycObject object2 = new NartImpl(object2_functor, object2_arg);
    KbIndividualImpl kbi2 = KbIndividualImpl.get(object2);
    String expResult2 = TestConstants.getCyc().cyclifyString("(GenericInstanceFn Dog)");
    String result2 = kbi2.getCore().cyclify();
    assertEquals(expResult2, result2);
  }

  /**
   * Test of findOrCreate method, of class KBIndividual.
   * @throws java.lang.Exception
   */
    @Test
    public void testFindOrCreate_String() throws Exception {
    System.out.println("findOrCreate");
    // Test for find
    String nameOrId1 = "AlbertEinstein";
    KbIndividualImpl kbi1 = KbIndividualImpl.findOrCreate(nameOrId1);
    assertEquals(KbIndividualImpl.class, kbi1.getClass());
    String expResult1 = TestConstants.getCyc().cyclifyString(nameOrId1);
    String result1 = kbi1.getCore().cyclify();
    assertEquals(expResult1, result1);

    // Test for create
    String nameOrId2 = "NewConstantNotInKB";
    KbIndividualImpl kbi2 = KbIndividualImpl.findOrCreate(nameOrId2);
    String expResult2 = TestConstants.getCyc().cyclifyString(nameOrId2);
    String result2 = KbIndividualImpl.get(nameOrId2).getCore().cyclify();
    assertEquals(expResult2, result2);
    
    kbi2.delete();
  }

  /**
   * Test of findOrCreate method, of class KBIndividual.
   * @throws java.lang.Exception
   */
  @Test
  public void testFindOrCreate_CycObject() throws Exception {
    System.out.println("findOrCreate");
    // Test for find
    String nameOrId1 = "AlbertEinstein";
    CycObject object1 = TestConstants.getCyc().getLookupTool().getKnownConstantByName(nameOrId1);
    KbIndividualImpl kbi1 = KbIndividualImpl.findOrCreate(object1);
    assertEquals(KbIndividualImpl.class, kbi1.getClass());
    String expResult1 = TestConstants.getCyc().cyclifyString(nameOrId1);
    String result1 = kbi1.getCore().cyclify();
    assertEquals(expResult1, result1);
    
    /* Test for create
    Question - why do we need this? If the method takes a CycObject object as input, then 
               the corresponding term already exists in the KB. 
    CycObject object2 = "ThisIsANewKBIndividualForTestingCreation";
    KBIndividual kbi2 = KBIndividual.findOrCreate(nameOrId2);
    String expResult2 = TestConstants.getCyc().cyclifyString("ThisIsANewKBIndividualForTestingCreation");
    String result2 = kbi2.getCore().cyclify();
    assertEquals(expResult2, result2);
    */
  }

  /**
   * Test of findOrCreate method, of class KBIndividual.
   * @throws java.lang.Exception
   */
  @Test
  public void testFindOrCreate_String_KBCollection() throws Exception {
    System.out.println("findOrCreate");
    // Test for find
    String nameOrId1 = "AlbertEinstein";
    KbCollection col1 = KbCollectionImpl.get("Scientist");
    KbIndividualImpl kbi1 = KbIndividualImpl.findOrCreate(nameOrId1, col1);
    String expResult1 = TestConstants.getCyc().cyclifyString(nameOrId1);
    String result1 = kbi1.getCore().cyclify();
    assertEquals(expResult1, result1);

    // Test for create
    String nameOrId2 = "NewConstantNotInKB";
    KbCollection col2 = KbCollectionImpl.get("Nonsense");
    KbIndividualImpl kbi2 = KbIndividualImpl.findOrCreate(nameOrId2, col2);
    String expResult2 = TestConstants.getCyc().cyclifyString(nameOrId2);
    String result2 = KbIndividualImpl.get(nameOrId2).getCore().cyclify();
    assertEquals(expResult2, result2);
    
   kbi2.delete();
  }

  /**
   * Test of findOrCreate method, of class KBIndividual.
   * @throws java.lang.Exception
   */
  @Test
  public void testFindOrCreate_String_String() throws Exception {
    System.out.println("findOrCreate");
    // Test for find
    String nameOrId1 = "AlbertEinstein";
    KbIndividualImpl kbi1 = KbIndividualImpl.findOrCreate(nameOrId1, "Scientist");
    String expResult1 = TestConstants.getCyc().cyclifyString(nameOrId1);
    String result1 = kbi1.getCore().cyclify();
    assertEquals(expResult1, result1);

    // Test for create
    String nameOrId2 = "NewConstantNotInKB";
    KbIndividualImpl kbi2 = KbIndividualImpl.findOrCreate(nameOrId2, "Nonsense");
    String expResult2 = TestConstants.getCyc().cyclifyString(nameOrId2);
    String result2 = KbIndividualImpl.get(nameOrId2).getCore().cyclify();
    assertEquals(expResult2, result2);
    
    kbi2.delete();
  }

  /**
   * Test of findOrCreate method, of class KBIndividual.
   * @throws java.lang.Exception
   */
  @Test
  public void testFindOrCreate_String_KBCollection_Context() throws Exception {
    System.out.println("findOrCreate");
    // Test for find
    String nameOrId1 = "AlbertEinstein";
    KbCollection col1 = KbCollectionImpl.get("Scientist");
    ContextImpl ctx1 = ContextImpl.findOrCreate("WebSearchDataMt");
    KbIndividualImpl.findOrCreate(nameOrId1, col1, ctx1);
    String expResult1 = TestConstants.getCyc().cyclifyString(nameOrId1);
    String result1 = KbIndividualImpl.get(nameOrId1).getCore().cyclify();
    assertEquals(expResult1, result1);

    // Test for create
    String nameOrId2 = "NewConstantNotInKB";
    KbCollection col2 = KbCollectionImpl.get("Nonsense");
    KbIndividualImpl kbi2 = KbIndividualImpl.findOrCreate(nameOrId2, col2, TestConstants.baseKB);
    String expResult2 = TestConstants.getCyc().cyclifyString(nameOrId2);
    String result2 = KbIndividualImpl.get(nameOrId2).getCore().cyclify();
    assertEquals(expResult2, result2);
    
    kbi2.delete();
  }

  /**
   * Test of findOrCreate method, of class KBIndividual.
   * @throws java.lang.Exception
   */
  @Test
  public void testFindOrCreate_String_String_String() throws Exception {
    System.out.println("findOrCreate");
        // Test for find
    String nameOrId1 = "AlbertEinstein";
    ContextImpl.findOrCreate("WebSearchDataMt"); // We are using this context as a string
    // This will fail if WebSearchDataMt or Scientist is not found
    KbIndividualImpl.findOrCreate(nameOrId1, "Scientist", "WebSearchDataMt");
    String expResult1 = TestConstants.getCyc().cyclifyString(nameOrId1);
    String result1 = KbIndividualImpl.get(nameOrId1).getCore().cyclify();
    assertEquals(expResult1, result1);

    // Test for create
    String nameOrId2 = "NewConstantNotInKB";
    KbIndividualImpl.findOrCreate(nameOrId2, "Nonsense", "BaseKB");
    String expResult2 = TestConstants.getCyc().cyclifyString(nameOrId2);
    KbIndividualImpl kbi2 = KbIndividualImpl.get(nameOrId2);
    String result2 = kbi2.getCore().cyclify();
    assertEquals(expResult2, result2);
    kbi2.delete();
  }

  
  
  
    /**
   * Test of existsAsType method, of class KBIndividual.
   * @throws java.lang.Exception
   */
  @Test
  public void testExistsAsType_String() throws Exception {
    System.out.println("existsAsType");
    String nameOrId1 = "AlbertEinstein";
    boolean expResult1 = true;
    boolean result1 = KbIndividualImpl.existsAsType(nameOrId1);
    assertEquals(expResult1, result1);
    
    String nameOrId2 = "Scientist";
    boolean expResult2 = false;
    boolean result2 = KbIndividualImpl.existsAsType(nameOrId2);
    assertEquals(expResult2, result2);
  }

  /**
   * Test of existsAsType method, of class KBIndividual.
   * @throws java.lang.Exception
   */
  @Test
  public void testExistsAsType_CycObject() throws Exception {
    System.out.println("existsAsType");
    String nameOrId1 = "AlbertEinstein";
    CycObject object1 = TestConstants.getCyc().getLookupTool().getKnownConstantByName(nameOrId1);
    boolean expResult1 = true;
    boolean result1 = KbIndividualImpl.existsAsType(object1);
    assertEquals(expResult1, result1);

    String nameOrId2 = "Scientist";
    CycObject object2 = TestConstants.getCyc().getLookupTool().getKnownConstantByName(nameOrId2);
    boolean expResult2 = false;
    boolean result2 = KbIndividualImpl.existsAsType(object2);
    assertEquals(expResult2, result2);
  }

    /**
   * Test of getStatus method, of class KBIndividual.
   * @throws java.lang.Exception
   */
  @Test
  public void testGetStatus() throws Exception {
    System.out.println("getStatus");
    String nameOrId = "Pilot-APITest";
    KbStatus expResult = KbStatus.EXISTS_AS_TYPE;
    KbStatus result = KbIndividualImpl.getStatus(nameOrId);
    assertEquals(expResult, result);
    
    CycConstant cc = CycAccessManager.getCurrentAccess().getLookupTool().getKnownConstantByName(nameOrId);
    KbStatus result1 = KbIndividualImpl.getStatus(cc);
    assertEquals(expResult, result1);
  }

    
  @Test
  public void testIndividualString() throws CreateException, KbTypeException {
    KbIndividualImpl i = KbIndividualImpl.findOrCreate("TestIndividual001");
    assertEquals(i.toString(), "TestIndividual001");
  }

  @Test
  public void testIndividualStringLookupType() throws KbException {
    assertFalse(KbIndividualImpl.existsAsType("ThisConstantDoesNotExist"));
  }

  @Test
  public void testIndividualStringFort() throws KbException, Exception {
    //Individual i = new KBIndividual("(InstanceOfCollectionFromVideoFn WeddingCeremony (AVWorkWithIDFn 20834) 1)");
    //assertEquals(i.toString(), "(InstanceOfCollectionFromVideoFn WeddingCeremony (AVWorkWithIDFn 20834) 1)");
    //System.out.println("Isas: " + i.instantiates("BaseKB").toString());

    KbIndividualImpl i = KbIndividualImpl.findOrCreate("(SomeAirlineEquipmentLogFn Plane-APITest)");
    assertEquals(i.toString(), "(SomeAirlineEquipmentLogFn Plane-APITest)");
    System.out.println("Isas: " + i.instanceOf("UniversalVocabularyMt").toString());

  }

  @Test
  public void testSuperTypes() throws Exception {
    final String name = "TestSuperTypesTestIndividual";
    KbIndividualImpl i = KbIndividualImpl.findOrCreate(name);
    assertEquals(i.toString(), name);
    final KbCollection person = KbCollectionImpl.get("Person");


    i.instantiates(person, ContextImpl.get("PeopleDataMt")).instantiates(
            "MaleHuman", "PeopleDataMt");
    System.out.println("Just checking: " + i.isInstanceOf("MaleHuman", "EverythingPSC"));
    assertTrue(i.instanceOf("PeopleDataMt").contains(person));
  }

  @Test
  public void testComment() throws KbException {
    KbIndividualImpl i = KbIndividualImpl.findOrCreate("TestCommentTestIndividual");
    i.addComment("Adding a comment", "PeopleDataMt");
    assertTrue(i.getComments("PeopleDataMt").contains("Adding a comment"));
  }

  @Test(expected = KbRuntimeException.class)
  public void testDelete() throws Exception {
    KbIndividualImpl i = KbIndividualImpl.findOrCreate("TestDeleteTestIndividual");
    i.delete();
    assertEquals(i.getComments().size(), 0);
  }

  @Test(expected = KbException.class)
  public void testDelete2() throws Exception {
    final String name = "TestDelete2TestIndividual";
    KbIndividualImpl i = KbIndividualImpl.findOrCreate(name);
    i.delete();
    i = KbIndividualImpl.get(name);
    assertTrue(i != null);
  }

  @Test
  public void testDelete3() throws Exception {
    final String name = "TestDelete3TestIndividual";
    KbIndividualImpl i = KbIndividualImpl.findOrCreate(name);
    String id = i.getId();
    i.delete();
    i = KbIndividualImpl.findOrCreate(name);
    assertFalse(i.getId().equals(id));
  }

  /**
   * Test of getClassTypeCore method, of class KBIndividual.
 * @throws IOException 
 * @throws Exception 
   */
  @Test
  public void testGetClassTypeCore() throws Exception {
    System.out.println("getClassTypeCore");
    DenotationalTerm expResult = CycAccessManager.getCurrentAccess().getLookupTool().getKnownConstantByName("Individual");
    DenotationalTerm result = KbIndividualImpl.getClassTypeCore();
    assertEquals(expResult, result);
  }


  


  /**
   * Test of instanceOf method, of class KBIndividual.
   * @throws java.lang.Exception
   */
  @Test
  public void testInstanceOf() throws Exception {
    System.out.println("instanceOf");
    KbIndividual instance = KbIndividualImpl.get("FlightXYZ-APITest");
    KbCollectionImpl col = KbCollectionImpl.get("Flying-Move");
    Collection<KbCollectionImpl> expResult = new HashSet<KbCollectionImpl>();
    expResult.add(col);
    expResult.add(KbCollectionImpl.get("Individual"));
    Collection<KbCollection> result = instance.instanceOf("SomeAirlineLogMt");
    assertEquals(expResult, result);
    
    expResult = new HashSet<KbCollectionImpl>();
    expResult.add(KbCollectionImpl.get("Individual"));
    expResult.add(col);
    result = instance.instanceOf();
    assertTrue(result.containsAll(expResult));
  }


  /**
   * Test of getTypeString method, of class KBIndividual.
   * @throws com.cyc.kb.exception.KbException
   */
  @Test
  public void testGetTypeString() throws KbException {
    System.out.println("getTypeString");
    KbIndividualImpl instance = new KbIndividualImpl();
    KbCollection expResult = KbCollectionImpl.get("#$Individual");
    KbCollection result = (KbCollection)instance.getType();
    assertEquals(expResult, result);
  }
  
  @Test
  public void testKBObjectVar() throws KbTypeException, CreateException {
    System.out.println("KBObjectVar");
    KbIndividual i = KbIndividualImpl.get("?IND");
    System.out.println("Object: " + i + " Class: " + i.getClass());
  }
}
