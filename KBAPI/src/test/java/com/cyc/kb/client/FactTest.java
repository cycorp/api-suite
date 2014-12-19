package com.cyc.kb.client;

/*
 * #%L
 * File: FactTest.java
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
import com.cyc.base.cycobject.CycAssertion;
import com.cyc.base.cycobject.CycConstant;
import com.cyc.base.cycobject.FormulaSentence;
import com.cyc.baseclient.cycobject.CycAssertionImpl;
import com.cyc.baseclient.cycobject.CycFormulaSentence;
import com.cyc.kb.Assertion;
import com.cyc.kb.Context;
import com.cyc.kb.Fact;
import com.cyc.kb.KBCollection;
import com.cyc.kb.KBIndividual;
import com.cyc.kb.KBPredicate;
import com.cyc.kb.Sentence;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.DeleteException;
import com.cyc.kb.exception.KBApiException;
import com.cyc.kb.exception.KBObjectNotFoundException;
import com.cyc.kb.exception.KBTypeException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import org.junit.AfterClass;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class FactTest {

  private static Logger log = null;

  @BeforeClass
  public static void setUp() throws Exception {
    log = Logger.getLogger(FactTest.class.getName());
    TestConstants.ensureInitialized();
  }

  @AfterClass
  public static void tearDown() throws Exception {
  }

  @Test
  public void testFactCycObject() throws Exception {    
    CycAccess cyc = CycAccessManager.getCurrentAccess();
    FormulaSentence cfs = CycFormulaSentence.makeCycSentence(cyc, "(isa Plane-APITest CommercialAircraft)");
    CycConstant cc = cyc.getLookupTool().find("SomeAirlineEquipmentMt");
    CycAssertion ca = new CycAssertionImpl(cfs, cc);
    System.out.println("Assertion OC API: " + ca);

    FactImpl f = new FactImpl(ca);
    assertEquals(ca, f.getCore());
  }
  
  @Test
  public void testFactStringString() throws KBApiException {
    //Fact a = new Fact("'( #$genls  #$Dog  #$CanisGenus )", "#$UniversalVocabularyMt" );
    Fact a = new FactImpl("#$SomeAirlineEquipmentMt", "(#$isa #$Plane-APITest #$CommercialAircraft)");
    assertEquals("(ist SomeAirlineEquipmentMt (isa Plane-APITest CommercialAircraft))", a.toString());

    assertEquals(a.<KBPredicate>getArgument(0).toString(), "isa");
    assertEquals(a.<KBIndividual>getArgument(1).toString(), "Plane-APITest");
    assertEquals(a.<KBCollection>getArgument(2).toString(), "CommercialAircraft");

  }

  @Test
  public void testFactContextObject() throws KBApiException {

    Context uctx = Constants.uvMt();
    Fact anA = new FactImpl("(SomeAirlineEquipmentLogFn Plane-APITest)", "(flyingDoneBySomething-Move FlightXYZ-APITest Plane-APITest)");

    Fact anAonA = new FactImpl("(SomeAirlineEquipmentLogFn Plane-APITest)", "(#$assertionUtility  (#$ist (SomeAirlineEquipmentLogFn Plane-APITest) (flyingDoneBySomething-Move FlightXYZ-APITest Plane-APITest))  0.5 )");

    KBPredicate p = KBPredicateImpl.get("assertionUtility");
    FactImpl a = new FactImpl(uctx, p, anA, 0.89);
    assertEquals("(ist UniversalVocabularyMt (assertionUtility (() ((#$flyingDoneBySomething-Move #$FlightXYZ-APITest #$Plane-APITest))) 0.89))", a.toString());

    assertEquals(a.getContext().toString(), "UniversalVocabularyMt");

  }

  @Test
  public void testConstructAssertion() throws KBApiException {
    // "(SomeAirlineEquipmentLogFn Plane-APITest)", "(flyingDoneBySomething-Move FlightXYZ-APITest Plane-APITest)"
    KBPredicate p = KBPredicateImpl.get("flyingDoneBySomething-Move");
    KBIndividual flight = KBIndividualImpl.get("FlightXYZ-APITest");
    KBIndividual plane = KBIndividualImpl.get("Plane-APITest");
    List<Object> arguments = new ArrayList<Object>();
    arguments.add(flight);
    arguments.add(plane);

    Context ctx = ContextImpl.get("(SomeAirlineEquipmentLogFn Plane-APITest)");
    Fact a = new FactImpl(ctx, p, arguments.toArray());
    String factString = a.toString();

    System.out.println("Fact string: " + factString);

    // With date

    ContextImpl airlineLog = ContextImpl.get("SomeAirlineLogMt");
    KBPredicate start = TestConstants.kbapitc.startingDate;
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd HH:mm");
    try {
		Date d = sdf.parse("2014 03 15 8:05");
        Fact wellA = new FactImpl(airlineLog, start, flight, d);
        Date checkd = wellA.<Date>getArgument(2);
        assertEquals(checkd, d);
    } catch (ParseException pe) {
      //ignore
    }

  }
  
  @Test
  public void testFactFactories() throws KBTypeException, CreateException, ParseException, DeleteException {
    
    // Only GAFs can be Fact.class
    System.out.println("Testing that only GAFs can be Fact");
    try {
      @SuppressWarnings("deprecation")
      Fact f = FactImpl.get(TestConstants.flyingRule.getCore());
    } catch (KBObjectNotFoundException kboe){
      System.out.println("Got Exception: " + kboe.toString());
    }
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd HH:mm");
    Context airlineLogMt = ContextImpl.findOrCreate("SomeAirlineLogMt");
    KBIndividual flight = KBIndividualImpl.get("FlightXYZ-APITest");

    Date d = sdf.parse("2014 03 15 10:20");
    SentenceImpl s = new SentenceImpl(TestConstants.kbapitc.endingDate, flight, d);
    Assertion a = AssertionImpl.get(s, airlineLogMt);
    @SuppressWarnings("deprecation")
    Fact flDate = FactImpl.get(a.getCore());

    System.out.println("Testing HLID Factory");
    String hlid = a.getId();
    Fact flDate1 = FactImpl.get(hlid);
    assertTrue("Fact not equal to one from HLID", flDate1 == flDate);
    
    // Get based on formula and context strings
    System.out.println("Testing factory get(formulaStr, ctxStr)");
    Fact flDate2 = FactImpl.get(s.toString(), airlineLogMt.toString());
    assertTrue("Fact not equal to one from formulaStr, ctxStr", flDate2 == flDate);
    
    System.out.println("Testing factory get(formula, ctx)");
    Fact flDate3 = FactImpl.get(s, airlineLogMt);
    assertTrue("Fact not equal to one from formula, ctx", flDate3 == flDate);
    
    FirstOrderCollectionImpl flying2Col = FirstOrderCollectionImpl.get("Flying-Move");
    KBIndividual flight2 = KBIndividualImpl.findOrCreate("FlightABC-APITest", flying2Col, airlineLogMt);
    
    SentenceImpl s2 = new SentenceImpl(TestConstants.kbapitc.endingDate, flight2, d);
    FactImpl fl2Date = FactImpl.findOrCreate(s2, airlineLogMt);
    fl2Date.delete();
    
    Fact fl3Date = FactImpl.findOrCreate(s2.toString(), airlineLogMt.toString());
    assertFalse("Probably failed to assert, HLID is empty string", fl2Date.getId().equals(""));
  }

  @Test
  public void testDelete() throws KBApiException {
    
    KBIndividual i = KBIndividualImpl.findOrCreate("PlaneTwo-APITest");
    Fact a = new FactImpl("#$SomeAirlineEquipmentMt", "(#$isa #$PlaneTwo-APITest #$CommercialAircraft)");
    assertEquals("(ist SomeAirlineEquipmentMt (isa PlaneTwo-APITest CommercialAircraft))", a.toString());
    a.delete();
    
    try {
      Fact a2 = FactImpl.get("(#$isa #$PlaneTwo-APITest #$CommercialAircraft)", "#$SomeAirlineEquipmentMt");
    } catch (CreateException ce) {
      System.out.println("Exception: " + ce.getClass().getSimpleName() + " : " + ce.getMessage());
    }
  }

  @Test
  public void testAddList() throws KBApiException {
    // Replace this test case,
    // create and use a new predicate based on Flight event
    KBIndividualImpl report = new KBIndividualImpl ("TestReport");
    report.instantiates(TestConstants.kbapitc.databaseTable);
    
    List<String> ls = new ArrayList<String>();
    ls.add("INDEX1");
    ls.add("INDEX2");
    ls.add("CUSTOM_NAME");
    
    KBIndividual sksDb = KBIndividualImpl.findOrCreate("SimpleSKSIDB-APITest", "SimpleDatabase");

    SentenceImpl s = new SentenceImpl(KBPredicateImpl.get("tableFieldNameList"), report, ls);
    Fact f = new FactImpl(ContextImpl.get("(MappingMtFn SimpleSKSIDB-APITest)"), s);
    System.out.println("Fact F: " + f);
  }

  @Test
  public void testAddSet() throws KBApiException {
    KBIndividual i = Constants.getInstance().THESET_FUNC;
    Set<KBObjectImpl> ls = new HashSet<KBObjectImpl>();
    ls.add(KBIndividualImpl.findOrCreate("SomeIndividual001"));
    ls.add(KBIndividualImpl.findOrCreate("SomeIndividual002"));
    ls.add(KBIndividualImpl.findOrCreate("SomeIndividual003"));
    ls.add(KBIndividualImpl.findOrCreate("SomeIndividual004"));
    //Fact f = i.addFact(Context.get("UniversalVocabularyMt"), Predicate.get("exampleNATs"), 1, ls);
    SentenceImpl s = new SentenceImpl(TestConstants.kbapitc.exampleNATS, i, ls);
      Fact f = new FactImpl(Constants.uvMt(), s);
    System.out.println("Fact F: " + f);
    
    
  }

  @Test
  public void testAddAndGetSentence() throws KBApiException {

    KBIndividualImpl i = KBIndividualImpl.findOrCreate("SomeRandom-LS");
    i.isInstanceOf(KBCollectionImpl.get("LogicalSchema"));
    KBPredicateImpl p = KBPredicateImpl.get("meaningSentenceOfSchema");
    List<Object> l = new ArrayList<Object>();
    l.add(KBPredicateImpl.get("isa"));
    l.add(i);
    l.add(p);
    Sentence s = new SentenceImpl(l.toArray());

    i.addFact(ContextImpl.get("UniversalVocabularyMt"), p, 1, s);

    Collection<Fact> lfs = i.getFacts(p, 1, ContextImpl.get("UniversalVocabularyMt"));
    KBIndividual iback = lfs.iterator().next().<KBIndividual>getArgument(1);
    Sentence a = lfs.iterator().next().<Sentence>getArgument(2);
    System.out.println("Got a sentence: " + a);
  }
}
