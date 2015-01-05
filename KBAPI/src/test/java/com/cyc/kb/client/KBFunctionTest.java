package com.cyc.kb.client;

/*
 * #%L
 * File: KBFunctionTest.java
 * Project: KB API
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

import com.cyc.base.cycobject.Naut;
import com.cyc.kb.Context;
import com.cyc.kb.Fact;
import com.cyc.kb.KBCollection;
import com.cyc.kb.KBFunction;
import com.cyc.kb.KBIndividual;
import com.cyc.kb.KBPredicate;
import com.cyc.kb.KBTerm;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KBApiException;
import com.cyc.kb.exception.KBApiRuntimeException;
import com.cyc.kb.exception.KBTypeException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.AfterClass;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;


public class KBFunctionTest {

  private static final Logger log = Logger.getLogger(KBFunctionTest.class.getName());
  private static KBFunctionImpl appfn;
  private static KBCollection iPad;

  @BeforeClass
  public static void setUp() throws Exception {
    log.setLevel(Level.FINE);
    TestConstants.ensureInitialized();
    appfn = KBFunctionImpl.findOrCreate("AppFn");
    appfn.addFact(ContextImpl.findOrCreate("AppleProductMt"), KBPredicateImpl.findOrCreate("arity"), 1,
            (Object) 1);
    appfn.addResultIsa("FirstOrderCollection", "UniversalVocabularyMt");
    appfn.instantiates(KBCollectionImpl.get("ReifiableFunction"));
  }

  @AfterClass
  public static void tearDown() throws Exception {
  }

  @Test
  public void testFunction() throws KBApiException, UnknownHostException, IOException {
    String str = "AgeFn";
    KBCollection constrainingCol = KBCollectionImpl.get("#$UnaryFunction");
    Context ctx = ContextImpl.get("#$UniversalVocabularyMt");

    KBFunctionImpl f = KBFunctionImpl.findOrCreate(str, constrainingCol, ctx);
    assertEquals(f.getCore().cyclify(), "#$AgeFn");
  }

  @Test
  public void testFunctionString() throws KBTypeException, CreateException {
    KBFunctionImpl f = KBFunctionImpl.findOrCreate("AppFn");
    f.addFact(ContextImpl.findOrCreate("AppleProductMt"), KBPredicateImpl.get("arity"), 1,
            (Object) 1);
  }

  @Test
  public void testArgIsa() throws KBApiException {
    KBFunctionImpl f = KBFunctionImpl.findOrCreate("AppFn");
    KBCollection c = KBCollectionImpl.findOrCreate("iProduct");
    c.addGeneralization("Product", "AppleProductMt");

    f.addArgIsa(1, "iProduct", "AppleProductMt");
    assertEquals(f.getArgIsa(1, "AppleProductMt").iterator().next().toString(), "iProduct");
    new FactImpl("AppleProductMt", "(argIsa AppFn 1 iProduct)").delete();
    try {
      FactImpl factImpl = new FactImpl(true, "AppleProductMt", "(arg1Isa AppFn iProduct)");
      fail("This shouldn't be true any more.");
    } catch (KBApiException ex) {
    }
  }

  @Test
  public void testArgGenl() throws KBApiException {
    KBFunctionImpl f = KBFunctionImpl.findOrCreate("AppFn");
    f.addArgGenl(1, "Product", "AppleProductMt");
    assertTrue(f.getArgGenl(1, "AppleProductMt").contains(KBCollectionImpl.get("Product")));
  }

  @Test
  public void testResultIsa() throws KBApiException {
    KBFunction f = KBFunctionImpl.findOrCreate("AppFn");
    final String computerProgramTypeByPlatform = "ComputerProgramTypeByPlatform";
    f.addResultIsa(computerProgramTypeByPlatform, "AppleProductMt");
    final Collection<KBCollection> resultIsas = f.getResultIsa("AppleProductMt");
    assertTrue("resultIsas for " + f + " were " + resultIsas
            + ". Couldn't find " + computerProgramTypeByPlatform,
            resultIsas.contains(SecondOrderCollectionImpl.get(computerProgramTypeByPlatform)));
  }

  @Test
  public void testGenObject() throws KBApiException, Exception {
    KBFunctionImpl f = KBFunctionImpl.get("FruitFn");
    KBCollection c = f.<KBCollection>findOrCreateFunctionalTerm(KBCollection.class,
            KBCollectionImpl.get("AppleTree"));
    log.log(Level.FINE, "Returned function: {0}", c.toString());

    /*
    KBFunctionImpl f2 = KBFunctionImpl.get("AVWorkWithIDFn");
    KBIndividual ct = f2.<KBIndividual>findOrCreateFunctionalTerm(KBIndividual.class,
            507995);
    log.fine("Returned func extent ct: " + ct.toString());
    assertTrue(ct + " should be an instance of CycNart, but isn't.", ct.getCore() instanceof Nart);
    */
    
    KBFunctionImpl f3 = KBFunctionImpl.get("SomeAirlineEquipmentLogFn");    
    Context ctx2 = f3.<Context>findOrCreateFunctionalTerm(Context.class, KBIndividualImpl.get("Plane-APITest"));
    final Collection<Context> supers = ctx2.getInheritsFrom();
    log.log(Level.FINE, "Returned func extent mt: {0}", supers);
    assertTrue(supers.contains(ContextImpl.findOrCreate("BaseKB")));

    KBIndividual i = KBIndividualImpl.findOrCreate("TestIndividual001");
    i.instantiates(KBCollectionImpl.get("Person"), ctx2);

    i.instantiates("MaleHuman", 
            "(SomeAirlineEquipmentLogFn Plane-APITest)");
    Fact a = new FactImpl("(SomeAirlineEquipmentLogFn Plane-APITest)",
            "(#$isa #$TestIndividual001 (#$CitizenFn #$UnitedStatesOfAmerica))");

    // Apple Ontology related
    KBCollection ipad = KBCollectionImpl.findOrCreate("iPad-Test");
    ipad.addGeneralization(KBCollectionImpl.findOrCreate("iProduct"), ContextImpl.findOrCreate("AppleProductMt"));

    appfn.addArgGenl(1, "iProduct", "AppleProductMt");
    KBTerm ipadapp = appfn.<KBTerm>findOrCreateFunctionalTerm(KBTerm.class, ipad);
    ipadapp.instantiates((KBCollection)KBCollectionImpl.getClassType());
    ipadapp.instantiates("ComputerProgramTypeByPlatform", "AppleProductMt");

    KBFunctionImpl f4 = KBFunctionImpl.get("MtSpace");
    f4.<Context>findOrCreateFunctionalTerm(Context.class, new Date());
    
    try {
      String s = f3.<String>findOrCreateFunctionalTerm(String.class, KBIndividualImpl.get("Plane-APITest"));
    } catch (Exception e) {
      // assertEquals(e.getMessage(), "Casting of type class java.lang.String not supported");
      assertEquals(e.getMessage(),
          "com.cyc.kb.client.ContextImpl cannot be cast to java.lang.String");
    }

  }

  @Test
  public void testUnreifiableFunctionWithDateInput() throws KBApiException {
    KBFunctionImpl f4 = KBFunctionImpl.get("MtDim");
    KBPredicate p = KBPredicateImpl.get("mtTimeIndex");
    Context c = f4.<Context>findOrCreateFunctionalTerm(Context.class, p, new Date());
    System.out.println("Context : " + c);

    KBFunctionImpl dollars = KBFunctionImpl.findOrCreate("(USDollarFn 2012)");
    KBIndividual m1 = dollars.findOrCreateFunctionalTerm(KBIndividualImpl.class, 5);
    System.out.println("Ind 1: " + m1);

    KBIndividualImpl m2 = KBIndividualImpl.findOrCreate("((USDollarFn 2012) 5)");
    System.out.println("Ind 2: " + m2);
    assertTrue(m2 + " should be a NAUT, but is not.", m2.getCore() instanceof Naut);
  }

  @Test
  public void testUnreifiableFunctionWithDateInput2() throws KBApiException {
  
    KBFunctionImpl f1 = KBFunctionImpl.get ("USDollarFn");
    KBFunctionImpl f2 = f1.findOrCreateFunctionalTerm(KBFunctionImpl.class, 2012);
    f2.addResultIsa(KBCollectionImpl.get("MonetaryValue"), ContextImpl.get("UniversalVocabularyMt"));
    FactImpl.findOrCreate(new SentenceImpl(KBPredicateImpl.get("argsIsa"), f2, KBCollectionImpl.get("NumericInterval")));
    KBIndividual i1 = f2.findOrCreateFunctionalTerm(KBIndividualImpl.class, 10000000);
    
    KBPredicate p = KBPredicateImpl.get("revenueForPeriodByAccountingCOC");
    KBIndividualImpl w = KBIndividualImpl.get("Walmart-CommercialOrganization");
    
    KBFunctionImpl fy = KBFunctionImpl.get("FiscalYearFn");
    KBIndividual i2 = fy.findOrCreateFunctionalTerm(KBIndividualImpl.class, w, 2012);
    KBIndividual coc = KBIndividualImpl.findOrCreate("(#$AccountingCodeOfTypeTypicallyUsedByAgentFn #$GenerallyAcceptedAccountingPrinciples #$Walmart-CommercialOrganization)");
    w.addFact(Constants.uvMt(), p, 1, i1, i2, coc);
  }
  
  @Test
  public void testDelete() {
    try {
      
      KBFunctionImpl f = KBFunctionImpl.findOrCreate("SomeRandomConstant");
      f.delete();

      assertEquals(f.getComments().size(), 0);
    } catch (KBApiRuntimeException ex) {
      assertEquals(ex.getMessage(), "The reference to SomeRandomConstant object is stale. Possibly because it was delete using x.delete() method.");
    } catch (Exception e) {
      fail("Failed to delete something.");
    }
  }
  
  @Test 
  public void testFunctionCreateKBTerm() throws Exception {
  
  //KBFunctionImpl fin = KBFunctionImpl.get("FindObjectByCompactHLExternalIDStringFn");
  KBFunctionImpl phys = KBFunctionImpl.get("ThePhysicalFieldValueFn");
  
  KBIndividualImpl ps = KBIndividualImpl.findOrCreate("DreamStore-EVIDENCE-PS");
  ps.isInstanceOf(KBCollectionImpl.get("PhysicalSchema"), ContextImpl.get("UniversalVocabularyMt"));
  phys.findOrCreateFunctionalTerm(KBIndividualImpl.class, ps, "SOMETHING");
          
  //fin.findOrCreateFunctionalTerm(KBTermImpl.class, 
    //      phys.findOrCreateFunctionalTerm(KBIndividualImpl.class, ps, "SOMETHING"));
  
  KBFunctionImpl lsf = KBFunctionImpl.get("TheLogicalFieldValueFn");
  
  
  KBIndividualImpl ls = KBIndividualImpl.findOrCreate("DreamStore-EVIDENCE-LS");
  ls.isInstanceOf(KBCollectionImpl.get("LogicalSchema"), ContextImpl.get("UniversalVocabularyMt"));
  
  //(TheLogicalFieldValueFn DreamStore-EVIDENCE-LS Set-Mathematical 1)
  lsf.findOrCreateFunctionalTerm(KBTermImpl.class, ls, KBCollectionImpl.get("Set-Mathematical"), 1);
  }
}
