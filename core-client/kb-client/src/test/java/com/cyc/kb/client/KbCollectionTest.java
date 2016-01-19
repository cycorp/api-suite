package com.cyc.kb.client;

/*
 * #%L
 * File: KbCollectionTest.java
 * Project: KB Client
 * %%
 * Copyright (C) 2013 - 2016 Cycorp, Inc
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
import com.cyc.kb.KbCollection;
import com.cyc.kb.KbCollectionFactory;
import com.cyc.kb.KbIndividual;
import com.cyc.kb.KbStatus;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.InvalidNameException;
import com.cyc.kb.exception.KbException;
import com.cyc.kb.exception.KbTypeConflictException;
import com.cyc.kb.exception.KbTypeException;
import com.cyc.session.exception.SessionException;
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


public class KbCollectionTest {

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
  public void testCollection() throws KbException, UnknownHostException, IOException, Exception {
    String str1 = "dog";
    KbCollection constrainingCol1 = KbCollectionImpl.get("#$BiologicalSpecies");
    Context ctx1 = ContextImpl.get("#$UniversalVocabularyMt");

    String str2 = "dog123";
    KbCollection constrainingCol2 = constrainingCol1;
    Context ctx2 = ctx1;
    KbCollection c2 = null;
  }

  @Test
  public void testCollectionString() throws KbException {
    KbCollectionImpl c = KbCollectionImpl.findOrCreate("iProduct");
    assertEquals(c.toString(), "iProduct");
    c.addComment("All Apple products start with i", "AppleProductMt");
    assertTrue(c.getComments("AppleProductMt").contains("All Apple products start with i"));

  }
  
  
  @Test
  public void testToNlString() throws KbTypeException, CreateException, SessionException {
    System.out.println("testToNlString");
    KbCollection temporalThing = KbCollectionFactory.get("TemporalThing");
    String paraphrase = temporalThing.toNlString();
    assertNotEquals("Got CycL instead of NL", "TemporalThing", paraphrase);
    assertEquals("temporal thing", paraphrase);
  }


  @Test
  public void testGetMinCol() throws KbException {
    KbCollectionImpl expected_min = KbCollectionImpl.get("Volume");
    KbCollectionImpl somegenls = KbCollectionImpl.get("Individual");
    
    List<KbCollection> l = new ArrayList<KbCollection>();
    l.add(expected_min);
    l.add(somegenls);
    KbCollection min = KbCollectionImpl.getMinCol(l);
    
    System.out.println("List of Cols: " + l + ", Expected min: " + min);
    assertEquals(expected_min, min);
  }

  @Test
  public void testSpecs() throws KbException {
    final String iProductString = "iProduct";
    KbCollectionImpl c = KbCollectionImpl.findOrCreate(iProductString);
    assertEquals(c.toString(), iProductString);
    final String IPadString = "IPad-APITest";
    final KbCollectionImpl ipadColl = KbCollectionImpl.findOrCreate(IPadString);
    assertTrue(KbCollectionImpl.existsAsType(ipadColl.getCore()));
    final ContextImpl ctx = ContextImpl.findOrCreate("AppleProductMt");
    c.addSpecialization(ipadColl, ctx);
    c.addSpecialization(KbCollectionImpl.findOrCreate("IPhone-APITest"), ctx);
    final Collection<KbCollection> productTypes = c.getSpecializations("AppleProductMt");

    assertTrue("Couldn't find " + IPadString + " in " + productTypes,
            productTypes.contains(ipadColl));
    //assertEquals(c.specs("").get(1).toString(), "IPhone-APITest");
    // AppleProductMt is not visible by default

  }

  @Test
  public void testGenls() throws KbException {

    KbCollectionImpl c = KbCollectionImpl.findOrCreate("iProduct");
    assertEquals(c.toString(), "iProduct");
    final String productString = "Product";

    // Internally genls(mt, c) will be run, so not additional test needed.
    c.addGeneralization(productString, "AppleProductMt");
    final Collection<? extends KbCollection> generalizations = c.getGeneralizations("AppleProductMt");
    final FirstOrderCollection productQuaFOC = FirstOrderCollectionImpl.findOrCreate(productString);
    assertTrue(generalizations + " does not contain " + productString + " qua "
            + FirstOrderCollectionImpl.class.getSimpleName(),
            generalizations.contains(productQuaFOC));
    final KbCollection productQuaKBC = KbCollectionImpl.findOrCreate(productString);
    assertTrue(generalizations + " does not contain " + productString + " qua "
            + KbCollectionImpl.class.getSimpleName(),
            generalizations.contains(productQuaKBC));
  }
  
  @Test
  public void testGenlsWithDefaultContext() throws KbException {
	  KbCollectionImpl c = KbCollectionImpl.findOrCreate("iProduct");
	    assertEquals(c.toString(), "iProduct");
	    final String productString = "Product";

	    // Internally genls(mt, c) will be run, so not additional test needed.
	    c.addGeneralization(productString);
	    final Collection<? extends KbCollection> generalizations = c.getGeneralizations("AppleProductMt");
	    final FirstOrderCollection productQuaFOC = FirstOrderCollectionImpl.findOrCreate(productString);
	    assertTrue(generalizations + " does not contain " + productString + " qua "
	            + FirstOrderCollectionImpl.class.getSimpleName(),
	            generalizations.contains(productQuaFOC));
	    final KbCollection productQuaKBC = KbCollectionImpl.findOrCreate(productString);
	    assertTrue(generalizations + " does not contain " + productString + " qua "
	            + KbCollectionImpl.class.getSimpleName(),
	            generalizations.contains(productQuaKBC));
  }

  @Test
  public void testAllGeneralizations() throws KbException {

    KbCollectionImpl c = KbCollectionImpl.findOrCreate("iProduct");
    assertEquals(c.toString(), "iProduct");

    java.util.Collection<KbCollection> ags = c.allGeneralizations();
    log.fine("All Generalizations: " + ags);
    KbCollectionImpl[] expectedArray = {
      KbCollectionImpl.get("TemporallyExistingThing"),
      KbCollectionImpl.get("Location-Underspecified"),
      KbCollectionImpl.get("Trajector-Underspecified"),
      KbCollectionImpl.get("Individual"),
      KbCollectionImpl.get("Thing"),
      KbCollectionImpl.get("TemporalThing"),
      KbCollectionImpl.get("Product"),
      KbCollectionImpl.findOrCreate("iProduct")};
    Set expected = new HashSet(Arrays.asList(expectedArray));
    assertTrue(ags.containsAll(expected));
  }

  @Test
  public void testSubTypes() throws KbException, Exception {
    KbCollection c = KbCollectionImpl.findOrCreate("IPad-APITest");
    KbIndividualImpl a = KbIndividualImpl.findOrCreate("anIPad");
    ContextImpl con = ContextImpl.findOrCreate("AppleProductMt");
    a.instantiates(c, con);
    assertTrue(c.<KbIndividual>getInstances("AppleProductMt").contains(KbIndividualImpl.get("anIPad")));

    KbCollection cet = KbCollectionImpl.findOrCreate("ConsumerElectronicsType");
    c.instantiates(cet, con);
    assertTrue(cet.<KbCollection>getInstances("AppleProductMt").contains(
            KbCollectionImpl.get("IPad-APITest")));

  }

  @Test
  public void testSuperTypes() throws KbException {
    KbCollection cet = KbCollectionImpl.findOrCreate("ConsumerElectronicsType");
    KbCollection c = KbCollectionImpl.findOrCreate("IPhone-APITest");
    c.instantiates("ConsumerElectronicsType", "AppleProductMt");

    assertTrue(c.instancesOf("AppleProductMt").contains(cet));
  }

  @Test
  public void testGetValues() throws Exception {
    //TODO: Find better example. Here iGuy is not a collection.
    //TODO: Test the getValues method.

    KbIndividualImpl ig = KbIndividualImpl.findOrCreate("iGuy");
    ig.instantiates("Person", "AppleProductMt");
    ig.addFact(ContextImpl.get("AppleProductMt"), KbPredicateImpl.get("owns"), 1, KbIndividualImpl.findOrCreate("anIPad"));
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
  public void testCheckSuperType() throws KbException {

    KbCollectionImpl hc = KbCollectionImpl.get("HumanCyclist");
    assertFalse(hc.isInstanceOf(KbCollectionImpl.findOrCreate("PersonTypeByRegionalAffiliation"), ContextImpl.get("UniversalVocabularyMt")));
    System.out.print(hc.isInstanceOf(KbCollectionImpl.findOrCreate("PersonTypeByRegionalAffiliation"), ContextImpl.get("UniversalVocabularyMt")));
  }

  @Test
  public void testCheckGeneralizations() throws KbException {
    KbCollection hc = KbCollectionImpl.get("HumanCyclist");
    assertTrue(KbCollectionImpl.get("Person").isGeneralizationOf(hc, ContextImpl.get("UniversalVocabularyMt")));
    System.out.print(KbCollectionImpl.get("Person").isGeneralizationOf(hc, ContextImpl.get("UniversalVocabularyMt")));
  }

  /**
   * CollectionFactory get tests
   */
  @Test
  public void testCollectionFactoryGet() throws KbException {
    System.out.println("collectionFactoryGet");
    final String collName = "Emu";
    KbCollection c1 = new KbCollectionImpl(collName);
    KbCollectionImpl c2 = new KbCollectionImpl(collName);
    assertFalse("Two different " + collName + " are the same object!", c1 == c2);
    assertTrue("Two different " + collName + " are not equals()!", c2.equals(c1));


    c1 = KbCollectionImpl.get(collName);
    c2 = KbCollectionImpl.get(new CycConstantImpl(collName, new Guid("c01a4ba0-9c29-11b1-9dad-c379636f7270")));
    assertTrue("CycObject-based " + collName + " and string-based version differ!", c1 == c2);

    c2 = KbCollectionImpl.get("Mx4rwBpLoJwpEbGdrcN5Y29ycA");
    assertTrue("HLID-based " + collName + " and string-based version differ!", c1 == c2);

  }

  @Test(expected = KbTypeException.class)
  public void testGetTypeConflictException() throws KbException {
    KbCollection c3 = KbCollectionImpl.get("isa");
  }

  @Test(expected = KbTypeException.class)
  public void testGetTypeConflictException2() throws KbException {
    //even though i1 could be coerced into a Context, get() isn't supposed to do so.
    KbIndividualImpl i1 = new KbIndividualImpl("TestIndividualForTypeConflict");
    try {
      KbCollection c3 = KbCollectionImpl.get("TestIndividualForTypeConflict");
      assertTrue("TestIndividualForTypeConflict shouldn't be a collection!", c3 == null);
    } finally {
      i1.delete();
    }
  }

  @Test
  public void testCollectionFactoryFindOrCreateWorksLikeGet() throws KbException {

    final KbCollection c1 = KbCollectionImpl.findOrCreate("Emu");
    KbCollectionImpl c2 = KbCollectionImpl.findOrCreate("Emu");
    assertTrue("Two different BaseKBs are actually different objects!", c2 == c1);
    assertTrue("Two different BaseKBs are not equals()!", c2.equals(c1));

    c2 = KbCollectionImpl.findOrCreate(new CycConstantImpl("Emu", new Guid("c01a4ba0-9c29-11b1-9dad-c379636f7270")));
    assertTrue("CycObject-based BaseKB and string-based version differ!", c1 == c2);

    c2 = KbCollectionImpl.findOrCreate("Mx4rwBpLoJwpEbGdrcN5Y29ycA");
    assertTrue("HLID-based BaseKB and string-based version differ!", c1 == c2);

  }

  @Test
  public void testContextFactoryFindOrCreateCreatesOnlyOneObject() throws KbException {

    final KbCollectionImpl c1 = KbCollectionImpl.findOrCreate("EmuTheTestCollection");
    KbCollectionImpl c2 = KbCollectionImpl.findOrCreate("EmuTheTestCollection");
    assertTrue("Two different EmuTheTestCollections are actually different objects!", c2 == c1);
    assertTrue("Two different EmuTheTestCollections are not equals()!", c2.equals(c1));
    
    c2 = KbCollectionImpl.findOrCreate(c1.getCore());
    assertTrue("CycObject-based EmuTheTestCollection and string-based version differ!", c1 == c2);
    
    c2 = KbCollectionImpl.findOrCreate(c1.getId());
    
    assertTrue("HLID-based EmuTheTestCollection and string-based version differ!", c1 == c2);
    
    c1.delete();
  }

  @Test(expected = KbTypeConflictException.class)
  public void testFindOrCreateTypeConflictException() throws KbException {
    KbCollection c3 = KbCollectionImpl.findOrCreate("isa");
    System.out.println("Done.");
  }

  @Test(expected = InvalidNameException.class)
  public void testFindOrCreateInvalidNameException() throws KbException {
    KbCollection c3 = KbCollectionImpl.findOrCreate("Emu@#$SVA!@#R");
  }

  @Test
  public void testCollectionFactoryGetStatus() throws KbException {
    KbIndividual i1 = null;
    String testIndName = "TestIndividualForTypeConflict1";
    try {
      assertTrue("Emu isn't a collection!", KbCollectionImpl.getStatus("Emu").equals(KbStatus.EXISTS_AS_TYPE));
      assertTrue("isa isn't in type-conflict with collection!", KbCollectionImpl.getStatus("isa").equals(KbStatus.EXISTS_WITH_TYPE_CONFLICT));
      assertTrue(testIndName + " shouldn't exist at all, but has status " + KbCollectionImpl.getStatus(testIndName) + ".",
              KbCollectionImpl.getStatus(testIndName).equals(KbStatus.DOES_NOT_EXIST));
      System.out.println("Status = " + KbCollectionImpl.getStatus(testIndName));
    } finally {
      if (KbIndividualImpl.getStatus(testIndName) != KbStatus.DOES_NOT_EXIST) {
        KbIndividualImpl.get(testIndName).delete();
      }
    }
  }

  @Test
  public void testCollectionFactoryExistsAsType() throws KbException {
    KbIndividualImpl i1 = null;
    try {
      String testIndName = "TestIndividualForTypeConflict";

      assertTrue("Emu isn't a collection!", KbCollectionImpl.existsAsType("Emu"));
      assertFalse("isa is a collection!", KbCollectionImpl.existsAsType("isa"));
      assertFalse(testIndName + " is a collection.", KbCollectionImpl.existsAsType(testIndName));
      i1 = new KbIndividualImpl(testIndName);
      assertFalse(testIndName + " is a collection!", KbCollectionImpl.existsAsType(testIndName));
    } finally {
      if (i1 != null) {
        i1.delete();
      }
    }
  }
  
  @Test
  public void testGetVariable() throws KbException {
    // These are all find/create and the test doesn't depend on the semantics of the collections,
    // so these will not be added to the KBAPITestConstants list
    assertEquals("?HUM", KbCollectionImpl.findOrCreate("HumanInfant").getVariable().toString());
    assertEquals("?SUB-VEH", KbCollectionImpl.findOrCreate("(SubcollectionOfWithRelationToTypeFn VehicleAccident damages Airplane)").getVariable().toString());
    assertEquals("?ABCO", KbCollectionImpl.findOrCreate("AttackByComputerOperation").getVariable().toString());
    assertEquals("?ABCOC", KbCollectionImpl.findOrCreate("AttackByComputerOperation-CorruptionOfInformation").getVariable().toString());
  }

}
