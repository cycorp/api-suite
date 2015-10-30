package com.cyc.kb.client;

/*
 * #%L
 * File: KBCollectionTest.java
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

import com.cyc.base.cycobject.Guid;
import com.cyc.baseclient.cycobject.CycConstantImpl;
import com.cyc.kb.Context;
import com.cyc.kb.FirstOrderCollection;
import com.cyc.kb.KBCollection;
import com.cyc.kb.KBIndividual;
import com.cyc.kb.KBStatus;
import com.cyc.kb.exception.InvalidNameException;
import com.cyc.kb.exception.KBApiException;
import com.cyc.kb.exception.KBTypeConflictException;
import com.cyc.kb.exception.KBTypeException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import org.junit.AfterClass;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;


public class KBCollectionTest {

  private static Logger log = null;

  @BeforeClass
  public static void setUp() throws Exception {
    log = Logger.getLogger(KBCollectionTest.class.getName());
    TestConstants.ensureInitialized();
  }

  @AfterClass
  public static void tearDown() throws Exception {
  }

  @Test
  public void testCollection() throws KBApiException, UnknownHostException, IOException, Exception {
    String str1 = "dog";
    KBCollection constrainingCol1 = KBCollectionImpl.get("#$BiologicalSpecies");
    Context ctx1 = ContextImpl.get("#$UniversalVocabularyMt");

    String str2 = "dog123";
    KBCollection constrainingCol2 = constrainingCol1;
    Context ctx2 = ctx1;
    KBCollection c2 = null;
  }

  @Test
  public void testCollectionString() throws KBApiException {
    KBCollectionImpl c = KBCollectionImpl.findOrCreate("iProduct");
    assertEquals(c.toString(), "iProduct");
    c.addComment("All Apple products start with i", "AppleProductMt");
    assertTrue(c.getComments("AppleProductMt").contains("All Apple products start with i"));

  }

  @Test
  public void testGetMinCol() throws KBApiException {
    KBCollectionImpl expected_min = KBCollectionImpl.get("Volume");
    KBCollectionImpl somegenls = KBCollectionImpl.get("Individual");
    
    List<KBCollection> l = new ArrayList<KBCollection>();
    l.add(expected_min);
    l.add(somegenls);
    KBCollection min = KBCollectionImpl.getMinCol(l);
    
    System.out.println("List of Cols: " + l + ", Expected min: " + min);
    assertEquals(expected_min, min);
  }

  @Test
  public void testSpecs() throws KBApiException {
    final String iProductString = "iProduct";
    KBCollectionImpl c = KBCollectionImpl.findOrCreate(iProductString);
    assertEquals(c.toString(), iProductString);
    final String IPadString = "IPad-APITest";
    final KBCollectionImpl ipadColl = KBCollectionImpl.findOrCreate(IPadString);
    assertTrue(KBCollectionImpl.existsAsType(ipadColl.getCore()));
    final ContextImpl ctx = ContextImpl.findOrCreate("AppleProductMt");
    c.addSpecialization(ipadColl, ctx);
    c.addSpecialization(KBCollectionImpl.findOrCreate("IPhone-APITest"), ctx);
    final Collection<KBCollection> productTypes = c.getSpecializations("AppleProductMt");

    assertTrue("Couldn't find " + IPadString + " in " + productTypes,
            productTypes.contains(ipadColl));
    //assertEquals(c.specs("").get(1).toString(), "IPhone-APITest");
    // AppleProductMt is not visible by default

  }

  @Test
  public void testGenls() throws KBApiException {

    KBCollectionImpl c = KBCollectionImpl.findOrCreate("iProduct");
    assertEquals(c.toString(), "iProduct");
    final String productString = "Product";

    // Internally genls(mt, c) will be run, so not additional test needed.
    c.addGeneralization(productString, "AppleProductMt");
    final Collection<? extends KBCollection> generalizations = c.getGeneralizations("AppleProductMt");
    final FirstOrderCollection productQuaFOC = FirstOrderCollectionImpl.findOrCreate(productString);
    assertTrue(generalizations + " does not contain " + productString + " qua "
            + FirstOrderCollectionImpl.class.getSimpleName(),
            generalizations.contains(productQuaFOC));
    final KBCollection productQuaKBC = KBCollectionImpl.findOrCreate(productString);
    assertTrue(generalizations + " does not contain " + productString + " qua "
            + KBCollectionImpl.class.getSimpleName(),
            generalizations.contains(productQuaKBC));
  }
  
  @Test
  public void testGenlsWithDefaultContext() throws KBApiException {
	  KBCollectionImpl c = KBCollectionImpl.findOrCreate("iProduct");
	    assertEquals(c.toString(), "iProduct");
	    final String productString = "Product";

	    // Internally genls(mt, c) will be run, so not additional test needed.
	    c.addGeneralization(productString);
	    final Collection<? extends KBCollection> generalizations = c.getGeneralizations("AppleProductMt");
	    final FirstOrderCollection productQuaFOC = FirstOrderCollectionImpl.findOrCreate(productString);
	    assertTrue(generalizations + " does not contain " + productString + " qua "
	            + FirstOrderCollectionImpl.class.getSimpleName(),
	            generalizations.contains(productQuaFOC));
	    final KBCollection productQuaKBC = KBCollectionImpl.findOrCreate(productString);
	    assertTrue(generalizations + " does not contain " + productString + " qua "
	            + KBCollectionImpl.class.getSimpleName(),
	            generalizations.contains(productQuaKBC));
  }

  @Test
  public void testAllGeneralizations() throws KBApiException {

    KBCollectionImpl c = KBCollectionImpl.findOrCreate("iProduct");
    assertEquals(c.toString(), "iProduct");

    java.util.Collection<KBCollection> ags = c.allGeneralizations();
    log.fine("All Generalizations: " + ags);
    KBCollectionImpl[] expectedArray = {
      KBCollectionImpl.get("TemporallyExistingThing"),
      KBCollectionImpl.get("Location-Underspecified"),
      KBCollectionImpl.get("Trajector-Underspecified"),
      KBCollectionImpl.get("Individual"),
      KBCollectionImpl.get("Thing"),
      KBCollectionImpl.get("TemporalThing"),
      KBCollectionImpl.get("Product"),
      KBCollectionImpl.findOrCreate("iProduct")};
    Set expected = new HashSet(Arrays.asList(expectedArray));
    assertTrue(ags.containsAll(expected));
  }

  @Test
  public void testSubTypes() throws KBApiException, Exception {
    KBCollection c = KBCollectionImpl.findOrCreate("IPad-APITest");
    KBIndividualImpl a = KBIndividualImpl.findOrCreate("anIPad");
    ContextImpl con = ContextImpl.findOrCreate("AppleProductMt");
    a.instantiates(c, con);
    assertTrue(c.<KBIndividual>getInstances("AppleProductMt").contains(KBIndividualImpl.get("anIPad")));

    KBCollection cet = KBCollectionImpl.findOrCreate("ConsumerElectronicsType");
    c.instantiates(cet, con);
    assertTrue(cet.<KBCollection>getInstances("AppleProductMt").contains(
            KBCollectionImpl.get("IPad-APITest")));

  }

  @Test
  public void testSuperTypes() throws KBApiException {
    KBCollection cet = KBCollectionImpl.findOrCreate("ConsumerElectronicsType");
    KBCollection c = KBCollectionImpl.findOrCreate("IPhone-APITest");
    c.instantiates("ConsumerElectronicsType", "AppleProductMt");

    assertTrue(c.instancesOf("AppleProductMt").contains(cet));
  }

  @Test
  public void testGetValues() throws Exception {
    //TODO: Find better example. Here iGuy is not a collection.
    //TODO: Test the getValues method.

    KBIndividualImpl ig = KBIndividualImpl.findOrCreate("iGuy");
    ig.instantiates("Person", "AppleProductMt");
    ig.addFact(ContextImpl.get("AppleProductMt"), KBPredicateImpl.get("owns"), 1, KBIndividualImpl.findOrCreate("anIPad"));
  }

  /*
     @Test public void testDelete() { try { KBCollection c = new
   * KBCollection("iProduct"); c.delete();
   *
   * new KBCollection("IPhone-APITest").delete(); new KBCollection("IPad-APITest").delete(); new
   * KBIndividual("iGuy").delete(); new KBIndividual("anIPad").delete();
   *
   * new Context("AppleProductMt").delete();
   *
   * assertEquals(c.getComments("").size(), 0);
   *
   * } catch(CycApiException cae) { assertEquals(cae.getMessage(), "Expected
   * constant not found #$iProduct"); } catch (Exception e) {
   * e.printStackTrace(); fail("Failed to create or retrieve comments."); }
   }
   */
  
  @Test
  public void testCheckSuperType() throws KBApiException {

    KBCollectionImpl hc = KBCollectionImpl.get("HumanCyclist");
    assertFalse(hc.isInstanceOf(KBCollectionImpl.findOrCreate("PersonTypeByRegionalAffiliation"), ContextImpl.get("UniversalVocabularyMt")));
    System.out.print(hc.isInstanceOf(KBCollectionImpl.findOrCreate("PersonTypeByRegionalAffiliation"), ContextImpl.get("UniversalVocabularyMt")));
  }

  @Test
  public void testCheckGeneralizations() throws KBApiException {
    KBCollection hc = KBCollectionImpl.get("HumanCyclist");
    assertTrue(KBCollectionImpl.get("Person").isGeneralizationOf(hc, ContextImpl.get("UniversalVocabularyMt")));
    System.out.print(KBCollectionImpl.get("Person").isGeneralizationOf(hc, ContextImpl.get("UniversalVocabularyMt")));
  }

  /**
   * CollectionFactory get tests
   */
  @Test
  public void testCollectionFactoryGet() throws KBApiException {
    System.out.println("collectionFactoryGet");
    final String collName = "Emu";
    KBCollection c1 = new KBCollectionImpl(collName);
    KBCollectionImpl c2 = new KBCollectionImpl(collName);
    assertFalse("Two different " + collName + " are the same object!", c1 == c2);
    assertTrue("Two different " + collName + " are not equals()!", c2.equals(c1));


    c1 = KBCollectionImpl.get(collName);
    c2 = KBCollectionImpl.get(new CycConstantImpl(collName, new Guid("c01a4ba0-9c29-11b1-9dad-c379636f7270")));
    assertTrue("CycObject-based " + collName + " and string-based version differ!", c1 == c2);

    c2 = KBCollectionImpl.get("Mx4rwBpLoJwpEbGdrcN5Y29ycA");
    assertTrue("HLID-based " + collName + " and string-based version differ!", c1 == c2);

  }

  @Test(expected = KBTypeException.class)
  public void testGetTypeConflictException() throws KBApiException {
    KBCollection c3 = KBCollectionImpl.get("isa");
  }

  @Test(expected = KBTypeException.class)
  public void testGetTypeConflictException2() throws KBApiException {
    //even though i1 could be coerced into a Context, get() isn't supposed to do so.
    KBIndividualImpl i1 = new KBIndividualImpl("TestIndividualForTypeConflict");
    try {
      KBCollection c3 = KBCollectionImpl.get("TestIndividualForTypeConflict");
      assertTrue("TestIndividualForTypeConflict shouldn't be a collection!", c3 == null);
    } finally {
      i1.delete();
    }
  }

  @Test
  public void testCollectionFactoryFindOrCreateWorksLikeGet() throws KBApiException {

    final KBCollection c1 = KBCollectionImpl.findOrCreate("Emu");
    KBCollectionImpl c2 = KBCollectionImpl.findOrCreate("Emu");
    assertTrue("Two different BaseKBs are actually different objects!", c2 == c1);
    assertTrue("Two different BaseKBs are not equals()!", c2.equals(c1));

    c2 = KBCollectionImpl.findOrCreate(new CycConstantImpl("Emu", new Guid("c01a4ba0-9c29-11b1-9dad-c379636f7270")));
    assertTrue("CycObject-based BaseKB and string-based version differ!", c1 == c2);

    c2 = KBCollectionImpl.findOrCreate("Mx4rwBpLoJwpEbGdrcN5Y29ycA");
    assertTrue("HLID-based BaseKB and string-based version differ!", c1 == c2);

  }

  @Test
  public void testContextFactoryFindOrCreateCreatesOnlyOneObject() throws KBApiException {

    final KBCollectionImpl c1 = KBCollectionImpl.findOrCreate("EmuTheTestCollection");
    KBCollectionImpl c2 = KBCollectionImpl.findOrCreate("EmuTheTestCollection");
    assertTrue("Two different EmuTheTestCollections are actually different objects!", c2 == c1);
    assertTrue("Two different EmuTheTestCollections are not equals()!", c2.equals(c1));
    
    c2 = KBCollectionImpl.findOrCreate(c1.getCore());
    assertTrue("CycObject-based EmuTheTestCollection and string-based version differ!", c1 == c2);
    
    c2 = KBCollectionImpl.findOrCreate(c1.getId());
    
    assertTrue("HLID-based EmuTheTestCollection and string-based version differ!", c1 == c2);
    
    c1.delete();
  }

  @Test(expected = KBTypeConflictException.class)
  public void testFindOrCreateTypeConflictException() throws KBApiException {
    KBCollection c3 = KBCollectionImpl.findOrCreate("isa");
    System.out.println("Done.");
  }

  @Test(expected = InvalidNameException.class)
  public void testFindOrCreateInvalidNameException() throws KBApiException {
    KBCollection c3 = KBCollectionImpl.findOrCreate("Emu@#$SVA!@#R");
  }

  @Test
  public void testCollectionFactoryGetStatus() throws KBApiException {
    KBIndividual i1 = null;
    String testIndName = "TestIndividualForTypeConflict1";
    try {
      assertTrue("Emu isn't a collection!", KBCollectionImpl.getStatus("Emu").equals(KBStatus.EXISTS_AS_TYPE));
      assertTrue("isa isn't in type-conflict with collection!", KBCollectionImpl.getStatus("isa").equals(KBStatus.EXISTS_WITH_TYPE_CONFLICT));
      assertTrue(testIndName + " shouldn't exist at all, but has status " + KBCollectionImpl.getStatus(testIndName) + ".",
              KBCollectionImpl.getStatus(testIndName).equals(KBStatus.DOES_NOT_EXIST));
      System.out.println("Status = " + KBCollectionImpl.getStatus(testIndName));
    } finally {
      if (KBIndividualImpl.getStatus(testIndName) != KBStatus.DOES_NOT_EXIST) {
        KBIndividualImpl.get(testIndName).delete();
      }
    }
  }

  @Test
  public void testCollectionFactoryExistsAsType() throws KBApiException {
    KBIndividualImpl i1 = null;
    try {
      String testIndName = "TestIndividualForTypeConflict";

      assertTrue("Emu isn't a collection!", KBCollectionImpl.existsAsType("Emu"));
      assertFalse("isa is a collection!", KBCollectionImpl.existsAsType("isa"));
      assertFalse(testIndName + " is a collection.", KBCollectionImpl.existsAsType(testIndName));
      i1 = new KBIndividualImpl(testIndName);
      assertFalse(testIndName + " is a collection!", KBCollectionImpl.existsAsType(testIndName));
    } finally {
      if (i1 != null) {
        i1.delete();
      }
    }
  }
  
  @Test
  public void testGetVariable() throws KBApiException {
    // These are all find/create and the test doesn't depend on the semantics of the collections,
    // so these will not be added to the KBAPITestConstants list
    assertEquals("?HUM", KBCollectionImpl.findOrCreate("HumanInfant").getVariable().toString());
    assertEquals("?SUB-VEH", KBCollectionImpl.findOrCreate("(SubcollectionOfWithRelationToTypeFn VehicleAccident damages Airplane)").getVariable().toString());
    assertEquals("?ABCO", KBCollectionImpl.findOrCreate("AttackByComputerOperation").getVariable().toString());
    assertEquals("?ABCOC", KBCollectionImpl.findOrCreate("AttackByComputerOperation-CorruptionOfInformation").getVariable().toString());
  }

}
