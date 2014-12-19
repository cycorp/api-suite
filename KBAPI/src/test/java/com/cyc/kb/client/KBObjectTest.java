package com.cyc.kb.client;

/*
 * #%L
 * File: KBObjectTest.java
 * Project: KB API
 * %%
 * Copyright (C) 2013 - 2014 Cycorp, Inc
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

import com.cyc.base.CycAccess;
import com.cyc.base.CycAccessManager;
import com.cyc.base.CycConnectionException;
import com.cyc.base.cycobject.CycConstant;
import com.cyc.base.cycobject.CycList;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.Nart;
import com.cyc.base.cycobject.Naut;
import com.cyc.baseclient.cycobject.CycArrayList;
import com.cyc.baseclient.cycobject.NartImpl;
import com.cyc.baseclient.cycobject.NautImpl;
import com.cyc.baseclient.datatype.DateConverter;
import com.cyc.kb.*;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KBApiException;
import com.cyc.kb.exception.KBObjectNotFoundException;
import com.cyc.kb.exception.KBTypeException;
import com.cyc.kb.quant.ForAllQuantifiedInstanceRestrictedVariable;
import com.cyc.kb.quant.QuantifiedRestrictedVariable;
import com.cyc.kb.quant.ThereExistsQuantifiedInstanceRestrictedVariable;
import com.cyc.session.SessionApiException;

import java.util.*;
import java.util.logging.Logger;
import org.junit.AfterClass;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;


public class KBObjectTest {

  private static Logger log = null;

  @BeforeClass
  public static void setUp() throws Exception {
    log = Logger.getLogger(KBObjectTest.class.getName());
    TestConstants.ensureInitialized();
  }

  @AfterClass
  public static void tearDown() throws Exception {
  }

  @Test
  public void testGetAssertions() {
    //fail("Not yet implemented");
  }

  @Test
  public void testGetValuesString() {
    //fail("Not yet implemented");
  }

  @Test
  public void testGetValuesSPredicate() {
    //fail("Not yet implemented");
  }

  @Test
  public void testGetValuesSPredicateKBObject() {
    //fail("Not yet implemented");
  }

  @Test
  public void testSetAssertion() throws KBApiException {
    //(genls Dog CanisGenus)
    KBPredicate p = KBPredicateImpl.get("genls");
    KBCollectionImpl c = KBCollectionImpl.get("Dog");
    KBCollection genl = KBCollectionImpl.get("CanisGenus");
    Fact a = c.addFact(ContextImpl.get("UniversalVocabularyMt"), p, 1,
            (Object) genl);

    //String factString = a.toString();
    //assertEquals(factString, "( (canonicalize-term #$genls)  (canonicalize-term #$Dog)  (canonicalize-term #$CanisGenus) )");
    //log.fine("Fact string: " + factString);

  }

  @Test
  public void testSetAssertion2() throws KBApiException {
    BinaryPredicateImpl p = BinaryPredicateImpl.get("physicalParts");
    KBCollection c1 = KBCollectionImpl.get("Heart");
    KBCollection c2 = KBCollectionImpl.get("Supracommissure");

    QuantifiedRestrictedVariable qc1 = new ForAllQuantifiedInstanceRestrictedVariable(c1);
    QuantifiedRestrictedVariable qc2 = new ThereExistsQuantifiedInstanceRestrictedVariable(c1);

    //forAll.quantified(c2);
    //c1.setObject(new Context("UniversalVocabularyMt"), p, c2);

    qc1.addArg2(p, qc2, ContextImpl.get("UniversalVocabularyMt"));
    /*
     qc1.set(new Context("UniversalVocabularyMt"), p, qc1, qc2);
     // new Rule(ctx, p, qc1, qc2)
     f1 = Fact(p, uqc1, uqc2)
     f2 = f1.forAll(uqc1);
     f3 = f2.thereExists(uqc2);
      
     UnquantifiedLiteral ucq1 = new UnquantifiedLiteral(c1);
      
     f1 = new Fact( Formula(p, uqc1, uqc2).thereExists(uqc2).forAll(uqc1))
              
     f1 = Fact(p, forAll(uqc1), thereExists(uqc2))
              
     forAll(uqc1, thereExists(uqc2, f1))
     */
  }

    @Test
    public void testTypedVariables() throws Exception {
        // Declaring a typed variable
        KBIndividual indVar = new KBIndividualImpl("?DOG");
        indVar.getRestriction();
        Sentence indVarRestriction = new SentenceImpl(Constants.isa(), indVar, KBIndividualImpl.getClassType());
        assertEquals(indVarRestriction, indVar.getRestriction());

        // The problem with above code is that there is no way to set some other
        // collection as the type of KBIndividual. This is useful both in KB API and
        // MAPI, where we might want to say, "DrinkingEvent" is the restriction of an
        // #$Event instance, but we don't really want to generate DrinkingEvent MAPI class.

        // There are cases we want to build a KBObject or MAPI object in memory and
        // reify later. This brings us back to "Detached objects". The detach-ability
        // is required both for KBObjects and MAPI objects. Since MAPI extend KBObjects,
        // we need to implement detachability in KB API.

        // At a very high level and as initial implementation, detachment just requires
        // a collection, to hold KB sentences or non-reified Assertion objects. To provide
        // more flexibility in storing various kinds of temporary knowledge, the initial
        // implementation is just a map, Map<String, Object>. (We can introduce more complicated
        // object/mechanism later.) This map is not an identity property of the KBObject.

        // So, if we want to define a different "typeCore" we need to set that in the map
        // and build a KB Object.
        KBCollection dog = KBCollectionImpl.get("Dog");
        Map<String, Object> kboData = new HashMap<String, Object>();
        kboData.put("typeCore", dog);
        KBIndividual dogVar = new KBIndividualImpl(indVar, kboData);

        Sentence dogVarRestriction = new SentenceImpl(Constants.isa(), dogVar, dog);
        assertEquals(dogVarRestriction, dogVar.getRestriction());

        // Now even though we have dogVar as KBIndividual, we have the right restriction.
        // The collection #$Dog, has to be a spec of #$Individual, to make it a typeCore
        // of KBIndividual. As discussed earlier, this works similarly for EventImpl, MAPI object.

        // The restriction is computed by typeCore as a "isa" restriction. Any arbitrary
        // restriction can be passed in using kboData, which will override "isa" restriction.

        // In some cases, as in NLM paper extraction, we might want to assign a name to
        // a KBIndividual variable, but reify it later.

        kboData.put("constantName", "Fido-APITest");
        KBIndividual dogVarWithName = new KBIndividualImpl(indVar, kboData);

        // When the time comes in the code execution, we can actually reify the variable,
        // which will create a new KBIndividual object, because the "core", the identity
        // property of KBObject has changed.
        // The new KBIndividual will also be made an instance of typeCore, here, #$Dog
        KBIndividual fido = ((KBIndividualImpl)dogVarWithName).reifyTypedVariable();
        assertTrue(fido.isInstanceOf(dog));
        fido.delete();

        // Quantification:
        // This was a point of debate earlier. We wanted Quantifier not be associated with
        // the variable. But it is actually convenient to do so. I will attempt to balance
        // the concerns here.

        Quantifier thereExists = QuantifierImpl.get("thereExists");
        kboData.put("quantifier", thereExists);
        KBIndividual someDog = new KBIndividualImpl(indVar, kboData);

        // We will follow this model:
        // f1 = new Fact( Formula(p, uqc1, uqc2).thereExists(uqc2).forAll(uqc1))

        KBPredicate owns = KBPredicateImpl.get("owns");
        KBIndividual dave = KBIndividualImpl.get("DaveStieb-BaseballPlayer");
        Sentence unQuantifiedSent = new SentenceImpl(owns, dave, someDog);
        Sentence quantifiedSent = unQuantifiedSent.quantify(someDog);

        // Internally, the quantifiedSent can actually build a sentence with
        // #$thereExists or just note the nested position of someDog's quantifier.
        // This will allow the user to specify arbitrary nesting of quantifiers.

        assertEquals(quantifiedSent, new SentenceImpl(thereExists, someDog, unQuantifiedSent));

        // Rule Macro Sentences. As Keith mentioned, after ordinary GAFs, RMP GAFs
        // are the most important kind.
        // The above quantifiedSent passed into a TypeFact should be converted to
        // (relationInstanceExists owns DaveS Dog)
        // Currently it uses *.quant package, but will be moved to this framework
        // if approved, in future.

    }
  
  /*
   @Test
   public void testSetAssertion2() throws KBApiException {
   BinaryPredicate p = new BinaryPredicate("physicalParts");
   KBCollection c1 = new KBCollection("Heart");
   KBCollection c2 = new KBCollection("Supracommissure");
       
   RestrictedVariable uqc1 = new UnquantifiedInstanceVariable(c1);
   RestrictedVariable uqc2 = new UnquantifiedInstanceVariable(c2);
      
   f1 = new Fact( Formula(p, uqc1, uqc2).thereExists(uqc2).forAll(uqc1))
 
   }
   
   @Test
   public void testSetAssertion2() throws KBApiException {
   BinaryPredicate p = new BinaryPredicate("likes");
   KBCollection c = new KBCollection("Person");
       
   RestrictedVariable uqc1 = new UnquantifiedInstanceVariable(c, "var1");
   RestrictedVariable uqc2 = new UnquantifiedInstanceVariable(c, "var2");
      
   f1 = new Fact( Formula(p, uqc1, uqc2).thereExists(uqc2).forAll(uqc1))
 
   }
   */
//	@Test
//	public void testGetFact() {
//		//(genls Dog CanisGenus)
//		try {
//			KBCollection c = new KBCollection ("Dog");
//			Fact a = c.getFact("'(#$genls #$Dog #$CanisGenus)", "#$UniversalVocabularyMt");
//
//			LOG.fine("Fact string:" + a.toString());
//		} catch (Exception e) {
//			e.printStackTrace();
//			fail("Exception when running the test case");
//		}
//	}
//	
  @Test
  public void testKBObjectHashSet() throws KBApiException {
    Set<KBObjectImpl> sso = new HashSet<KBObjectImpl>();

    sso.add(KBCollectionImpl.get("Dog"));
    sso.add(KBCollectionImpl.get("Dog"));
    assertEquals(1, sso.size());

  }

  @Test
  public void testComment() throws KBApiException {
    
    KBIndividualImpl pluto = null;
    try {
      pluto = KBIndividualImpl.get("Pluto");
    } catch (KBObjectNotFoundException e) {
      pluto = KBIndividualImpl.findOrCreate("Pluto");
      pluto.addComment("API added this comment.", ContextImpl.findOrCreate("UniverseDataMt"));
    }
    
    Collection<String> comments = pluto.getComments(ContextImpl.findOrCreate("UniverseDataMt"));
    assertFalse("Found no comments for " + pluto, comments.isEmpty());
  }

  @Test
  public void testGetArguments() throws KBApiException {

    KBCollectionImpl c = KBCollectionImpl.get(
            "(SubcollectionOfWithRelationToTypeFn PettingAnAnimal doneBy Person)");
    assertEquals(c.<KBCollection>getArgument(3).toString(), "Person");
    assertEquals(c.<KBPredicate>getArgument(2).toString(), "doneBy");
    assertEquals(c.<FirstOrderCollection>getArgument(1).toString(), "PettingAnAnimal");
    assertEquals(c.<Relation>getArgument(0).toString(),
            "SubcollectionOfWithRelationToTypeFn");

    KBCollectionImpl pa = KBCollectionImpl.get("PettingAnAnimal");
    try {
      pa.<KBCollection>getArgument(0);
    } catch (Exception e) {
      assertEquals(e.getMessage(),
              "The object: PettingAnAnimal is an Atomic term. It does not have arguments.");
    }
    try {
      pa.<KBCollection>getArgument(1);
    } catch (Exception e) {
      assertEquals(e.getMessage(),
              "The object: PettingAnAnimal is an Atomic term. It does not have arguments.");
    }
  }

  @Test
  public void testPredValues() throws Exception {
    ContextImpl c = ContextImpl.findOrCreate("ExampleContext-APITest");
    KBIndividualImpl i = KBIndividualImpl.findOrCreate("AConceptualWork-APITest");
    i.instantiates(KBCollectionImpl.get("ConceptualWork"));
    KBPredicateImpl p = KBPredicateImpl.get("authorList");
    List<KBIndividual> inputList = new ArrayList<KBIndividual>();
    inputList.add(KBIndividualImpl.findOrCreate("TestAuthor1-APITest"));
    inputList.add(KBIndividualImpl.findOrCreate("TestAuthor2-APITest"));
    i.addFact(c, p, 1, inputList);
    Collection<List> list = i.getValues(p, 1, 2, c);
    System.out.println("TheList: " + list);
    assertEquals(inputList, list.iterator().next());
  }
  
  @Test
  public void testQuotedIsa() throws KBApiException {
    FirstOrderCollectionImpl c = FirstOrderCollectionImpl.get("TheTerm");
    KBIndividualImpl t = KBIndividualImpl.findOrCreate("Trixie-TheDog");
    t.addQuotedIsa(c, ContextImpl.get("BaseKB"));
    assertEquals(t.getQuotedIsa().iterator().next(), c);
  }
  
  @Test
  public void testQuote() throws Exception {
    KBFunctionImpl f = KBFunctionImpl.get("Quote");
    KBCollectionImpl d = KBCollectionImpl.get("Dog");
    KBIndividual kbo1 = f.findOrCreateFunctionalTerm(KBIndividualImpl.class, d);
    System.out.println("Quote 1: " + kbo1);
    
    KBIndividual kbo1_api = d.quote();
    assertEquals(kbo1, kbo1_api);

    List<Object> l = new ArrayList<Object>();
    l.add(KBPredicateImpl.get("isa"));
    final KBIndividual testDog = KBIndividualImpl.findOrCreate("Dog-APITest");
    l.add(testDog);
    l.add(KBCollectionImpl.get("Dog"));
    SentenceImpl s = new SentenceImpl(l.toArray());
    KBIndividual kbo2 = f.findOrCreateFunctionalTerm(KBIndividualImpl.class, s);
    System.out.println("Quote 2: " + kbo2);
    
    KBIndividual kbo2_api = s.quote();
    assertEquals(kbo2, kbo2_api);

    testDog.addQuotedIsa(KBCollectionImpl.get("ProprietaryConstant"), Constants.uvMt());
    assertTrue(testDog.isQuotedInstanceOf(KBCollectionImpl.get("ProprietaryConstant"), Constants.uvMt()));
    
    Fact b = s.addFact(ContextImpl.get("UniversalVocabularyMt"), KBPredicateImpl.get("beliefs"), 2, KBIndividualImpl.get("TestIndividual001"));
    Fact b_expt = new FactImpl("UniversalVocabularyMt", "(beliefs TestIndividual001 (isa Dog-APITest Dog))");
    assertEquals(b_expt, b);
    
  }
  
  @Test
  public void testCheckAndCastObject() throws Exception {

    Calendar c = Calendar.getInstance();
    c.set(2014, 0, 10, 16, 10, 10);
    Date d = c.getTime();
    
    CycAccess ca = CycAccessManager.getCurrentAccess();
    CycConstant f1 = ca.getLookupTool().getConstantByName("TheList");
    CycConstant c1 = ca.getLookupTool().getConstantByName("RonaldReagan");
    CycConstant c2 = ca.getLookupTool().getConstantByName("GeorgeHWBush");
    CycConstant c3 = ca.getLookupTool().getConstantByName("UnitedStatesPresident");
    CycObject cd = DateConverter.toCycDate(d);
    
    Naut cn1 = new NautImpl(f1, c1, c2, c3, "President", 1, cd);
    List<Object> o1 = KBObjectImpl.checkAndCastObject(cn1);
    
    KBIndividualImpl k1 = KBIndividualImpl.class.cast(o1.get(0));
    assertEquals(c1, k1.getCore());

    KBIndividualImpl k2 = KBIndividualImpl.class.cast(o1.get(1));
    assertEquals(c2, k2.getCore());
    
    KBCollectionImpl k3 = KBCollectionImpl.class.cast(o1.get(2));
    assertEquals(c3, k3.getCore());
    
    assertEquals("President", String.class.cast(o1.get(3)));
    
    assertEquals(new Integer(1), Integer.class.cast(o1.get(4)));
    
    assertEquals(d, Date.class.cast(o1.get(5)));

    System.out.println("TheList Example: " + o1);
    
    CycConstant f2 = ca.getLookupTool().getConstantByName("TheSet");
    NautImpl cn2 = new NautImpl(f2, c1, c2, c3, "President", 1, cd);
    
    HashSet<Object> o2 = KBObjectImpl.checkAndCastObject(cn2);
    assertEquals(6, o2.size());
    
    assertTrue(o2.contains(k1));
    assertTrue(o2.contains(k2));
    assertTrue(o2.contains(k3));
    assertTrue(o2.contains("President"));
    assertTrue(o2.contains(new Integer(1)));
    assertTrue(o2.contains(d));
    
    System.out.println("TheSet Example: " + o2);
    
    NautImpl cn3 = new NautImpl(f1, cn2, c1, c3, "President", 1, cd);
    List<Object> o3 = KBObjectImpl.checkAndCastObject(cn3);
    
    assertEquals(o2, HashSet.class.cast(o3.get(0)));
    System.out.println("The Nested List-Set: " + o3);
  }
  
  //@Test(expected = ClassCastException.class)
  public void testClassCast () throws Exception {
    System.out.println("testClassCast");
    Naut n = TestConstants.cyc.getObjectTool().makeCycNaut("(#$MinuteFn 20 \n" +
      "  (#$HourFn 10 \n" +
      "    (#$DayFn 15 \n" +
      "      (#$MonthFn #$March \n" +
      "        (#$YearFn 2014))))) ");
    // TODO: This should throw a ClassCastException, because we have a date, not a predicate:
    KBObjectImpl.<KBPredicate>checkAndCastObject(n);
  }
  
  @Test
  public void testCycArrayListInKBOCast () throws SessionApiException, KBTypeException, CreateException, CycConnectionException {
    CycAccess ca = CycAccessManager.getCurrentAccess();
    CycConstant f1 = ca.getLookupTool().getConstantByName("TheList");
    CycConstant f2 = ca.getLookupTool().getConstantByName("FlyingATypeOfObject-Operate-Fn");
    CycConstant c1 = ca.getLookupTool().getConstantByName("CommercialAircraft");
    Nart n = new NartImpl(f2, c1);
    CycList cl = new CycArrayList(f1, n);
    
    Object o = KBObjectImpl.checkAndCastObject(cl);
    System.out.println("Object: " + o);
    System.out.println("Object class: " + o.getClass());
    assertTrue(((List)o).contains(KBCollectionImpl.get("(FlyingATypeOfObject-Operate-Fn CommercialAircraft)")));
  }

  @Test
  public void testTheEmptyListinKBOCast () throws SessionApiException, CycConnectionException, KBTypeException, CreateException {
    CycAccess ca = CycAccessManager.getCurrentAccess();
    CycConstant f1 = ca.getLookupTool().getConstantByName("TheEmptyList");
    
    Object o = KBObjectImpl.checkAndCastObject(f1);
    System.out.println("Object: " + o);
    System.out.println("Object class: " + o.getClass());
    assertTrue(o instanceof List);
    assertTrue(((List)o).isEmpty());
  }
}
