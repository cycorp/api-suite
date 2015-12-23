/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cyc.kb.client;

/*
 * #%L
 * File: SentenceTest.java
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
import com.cyc.base.exception.CycApiException;
import com.cyc.base.exception.CycConnectionException;
import com.cyc.baseclient.cycobject.CycFormulaSentence;
import com.cyc.kb.KbCollection;
import com.cyc.kb.KbIndividual;
import com.cyc.kb.KbPredicate;
import com.cyc.kb.Sentence;
import com.cyc.kb.Symbol;
import com.cyc.kb.Variable;
import static com.cyc.kb.client.TestUtils.skipTest;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbException;
import com.cyc.kb.exception.KbTypeException;
import com.cyc.kb.client.quant.InstanceRestrictedVariable;
import com.cyc.kb.client.quant.RestrictedVariable;
import com.cyc.kb.client.quant.SpecializationRestrictedVariable;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.junit.After;
import org.junit.AfterClass;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.LoggerFactory;

/**
 *
 * @author vijay
 */
public class SentenceTest {

  final private static org.slf4j.Logger log = LoggerFactory.getLogger(SentenceTest.class.getCanonicalName());

  public SentenceTest() {
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
    TestConstants.ensureInitialized();

  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() throws IOException {
    Logger rootLogger = Logger.getLogger("com.cyc");
    rootLogger.setLevel(Level.ALL);
    PatternLayout layout = new PatternLayout("%d{HH:mm:ss} %-5p %c{1}:%L - %m%n");
    rootLogger.removeAllAppenders();
    rootLogger.addAppender(new FileAppender(layout, "/tmp/kbapi.log", false));

    log.debug("do something");
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testSentenceArgs() throws KbException {
    System.out.println("Sentence (Object[] args)");

    KbCollection c = KbCollectionImpl.get("#$Dog");
    RestrictedVariable rv = KbCollectionImpl.from(c).toInstanceRestrictedVariable();

    KbPredicate p = KbPredicateImpl.get("#$owns");
    //Variable v = new Variable ("?PER");
    KbIndividual d = KbIndividualImpl.get("TestIndividual001");

    List<Object> ol = new ArrayList();
    ol.add(p);
    //ol.add(v);
    ol.add(d);
    ol.add(rv);

    Sentence instance = new SentenceImpl(ol.toArray());
    System.out.println("Sentence: " + instance);
  }

  @Test
  public void testSentenceArgs2() throws KbException {
    System.out.println("Sentence (Object[] args)");

    KbCollection c = KbCollectionImpl.get("#$Dog");
    RestrictedVariable rv1 = KbCollectionImpl.from(c).toInstanceRestrictedVariable();
    KbPredicate p = KbPredicateImpl.get("#$owns");
    KbCollection c2 = KbCollectionImpl.get("#$Person");
    RestrictedVariable rv2 = KbCollectionImpl.from(c2).toInstanceRestrictedVariable();

    List<Object> ol = new ArrayList();
    ol.add(p);
    ol.add(rv2);
    ol.add(rv1);

    Sentence instance = new SentenceImpl(ol.toArray());
    System.out.println("Sentence: " + instance);

    RestrictedVariable rv3 = new SpecializationRestrictedVariable(null, c);

    List<Object> ol2 = new ArrayList();
    ol2.add(p);
    ol2.add(rv2);
    ol2.add(rv3);

    Sentence instance2 = new SentenceImpl(ol2.toArray());
    System.out.println("Sentence: " + instance2);
  }

  @Test
  public void testSentenceArgs3() throws KbException {
    System.out.println("Sentence (Object[] args)");

    Symbol pred = new SymbolImpl(":PRED");
    Variable well = new VariableImpl("WELL");
    Variable date = new VariableImpl("DATE");
    Variable event = new VariableImpl("EVENT");

    List<Object> ol = new ArrayList();
    ol.add(pred);
    ol.add(well);
    ol.add(date);
    ol.add(event);

    Sentence instance = new SentenceImpl(ol.toArray());
    System.out.println("Sentence: " + instance);

    List<Object> ol2 = new ArrayList();
    ol2.add(new QuantifierImpl("thereExists"));
    ol2.add(event);
    ol2.add(instance);
    Sentence nestedS = new SentenceImpl(ol2.toArray());
    System.out.println("Nested Sentence: " + nestedS);

    List<Object> ol3 = new ArrayList();
    ol3.add(KbFunctionImpl.get("Quote"));
    ol3.add(nestedS);
    SentenceImpl q = new SentenceImpl(ol3.toArray());
    q.addFact(ContextImpl.get("UniversalVocabularyMt"), KbPredicateImpl.get("sentenceParameterValueInSpecification"), 1, KbIndividualImpl.findOrCreate("SomeQuerySpecification"));

  }

  @Test
  public void testNegatedSentenceFromString() throws KbException, CycApiException, CycConnectionException {
    System.out.println("testNegatedSentenceFromString");
    Sentence instance = new SentenceImpl(CycFormulaSentence.makeCycSentence(TestConstants.getCyc(), "(#$not (#$isa #$Thing #$Predicate))"));
    System.out.println("Sentence: " + instance);
    assertTrue(instance instanceof Sentence);

    Sentence expSent = new SentenceImpl(CycFormulaSentence.makeCycSentence(TestConstants.getCyc(), "(#$isa #$Thing #$Predicate)"));
    Sentence subSent = instance.getArgument(1);
    assertEquals(expSent, subSent);

    assertEquals(KbCollectionImpl.get("Predicate"), subSent.getArgument(2));
  }

  @Test
  public void testNegatedSentence() throws KbException, CycApiException, IOException, CycConnectionException {
    System.out.println("testNegatedSentence");
    System.out.println("testNegatedSentenceFromString");
    Sentence base = new SentenceImpl(CycFormulaSentence.makeCycSentence(TestConstants.getCyc(), "(#$isa #$Thing #$Predicate)"));
    Sentence instance = new SentenceImpl(KbTermImpl.get("not"), base);
    System.out.println("Sentence: " + instance);
    assertTrue(instance instanceof Sentence);
  }

  @Test
  public void testSentenceArgs4() throws KbException {
    System.out.println("Sentence (Object[] args)");

    List<Object> ol = new ArrayList();
    ol.add(KbFunctionImpl.get("TheList"));

    Sentence instance = new SentenceImpl(ol.toArray());
    System.out.println("Sentence: " + instance);
  }

  @Test
  public void testSentenceArgs5() throws KbException {
    System.out.println("Sentence (Object[] args)");

    String col = "Someevent type";

    //TODO: add sentence support to get to this target Sentence, instead of using a String. We are not
    // capable of doing that yet. 
    String qStr = "(#$thereExists ?PRED\n"
            + " (#$thereExists ?BPRED\n"
            + "   (#$thereExists ?COL1\n"
            + "     (#$thereExists ?COL2\n"
            + "       (#$and\n"
            + "         (#$isa ?PRED #$QuantificationalRuleMacroPredicate-Canonical)"
            + "         (#$genls ?COL1 #$Event)"
            + "         (#$assertedSentence\n"
            + "           (#$genls " + col + " ?COL2))\n"
            + "         (#$assertionSentence ?ASSERTION (#$relationAllExists ?BPRED ?COL1 ?COL2)))))))";

    List<Object> ol = new ArrayList();
    ol.add(KbPredicateImpl.get("relationAllExists"));
    ol.add(new InstanceRestrictedVariable(null, KbCollectionImpl.get("QuantificationalRuleMacroPredicate-Canonical")));
    ol.add(new SpecializationRestrictedVariable(null, KbCollectionImpl.get("Event")));
    ol.add(new SpecializationRestrictedVariable(null, KbCollectionImpl.get("Dog")));

    Sentence instance = new SentenceImpl(ol.toArray());
    System.out.println("Sentence: " + instance);
  }

  @Test
  public void testSentenceArgs6() throws KbTypeException, CycApiException, CycConnectionException, CreateException {
    System.out.println("Testing args from CycSentence");
    assertNotNull(new SentenceImpl(CycFormulaSentence.makeCycSentence(TestConstants.getCyc(), "(#$isa #$Thing #$Predicate)")).getArgument(2));
  }

  /**
   * Test of getTypeString method, of class Sentence.
   * @throws com.cyc.kb.exception.KbException
   */
  @Test
  public void testGetTypeString() throws KbException {
    System.out.println("getTypeString");
    skipTest(this, "testGetTypeString", "This test is not yet implemented.");
    SentenceImpl instance = new SentenceImpl("");
    String expResult = "";
    KbCollection result = (KbCollection) instance.getType();
    //assertEquals(expResult, result);
    // @TODO review the generated test code and remove the default call to fail.
    //fail("The test case is a prototype.");
  }

  public void testIsWff() throws KbTypeException, CreateException {
    KbIndividual attack = KbIndividualImpl.findOrCreate("AttackByComputerOperation-WnErDFED8C");
    attack.instantiates(KbCollectionImpl.get("AttackByComputerOperation"));
    SentenceImpl s1 = new SentenceImpl(KbPredicateImpl.get("doneBy"), attack, KbIndividualImpl.get("Plane-APITest"));
    assertTrue(!s1.isAssertible(ContextImpl.get("SomeAirlineEquipmentMt")));
    SentenceImpl s2 = new SentenceImpl(KbPredicateImpl.get("doneBy"), attack, KbIndividualImpl.get("TestIndividual001"));
    assertTrue(s2.isAssertible(Constants.uvMt()));
  }

  @Test
  public void testReplaceTerms() throws KbException {
    KbCollection cPlane = KbCollectionImpl.get("CommercialAircraft");
    BinaryPredicateImpl flying2Pred1 = BinaryPredicateImpl.findOrCreate("flyingDoneBySomething-Move");
    Variable varp = new VariableImpl("?PLANE");
    Variable varf = new VariableImpl("?FLIGHT");
    Variable varend = new VariableImpl("?END-DATE");
    Variable vart = new VariableImpl("?TO");
    SentenceImpl s1 = new SentenceImpl(KbPredicateImpl.get("isa"), varp, cPlane);
    SentenceImpl s2 = new SentenceImpl(flying2Pred1, varf, varp);
    SentenceImpl s3 = new SentenceImpl(KbPredicateImpl.get("endingDate"), varf, varend);
    SentenceImpl s4 = new SentenceImpl(KbPredicateImpl.get("toLocation"), varf, vart);
    Sentence s5 = new SentenceImpl(KbPredicateImpl.get("artifactFoundInLocation"), varp, vart);
    Sentence s6 = new SentenceImpl(KbPredicateImpl.get("holdsIn"), varend, s5);
    List<Sentence> sandlist = new ArrayList<Sentence>();
    sandlist.add(s1);
    sandlist.add(s2);
    sandlist.add(s3);
    sandlist.add(s4);
    Sentence sand = SentenceImpl.and(sandlist);

    Sentence rule = new SentenceImpl(KbIndividualImpl.get("implies"), sand, s6);

    // Build a new sentence to check equality
    Sentence replaceSent = new SentenceImpl(Constants.isa(), varp, cPlane);

    List<Object> from = new ArrayList<Object>();
    from.add(varf);
    from.add(replaceSent);
    List<Object> to = new ArrayList<Object>();
    Symbol fSym = new SymbolImpl(":FLIGHT_MARKER");
    to.add(fSym);
    Symbol sSym = new SymbolImpl(":SENTENCE_MARKER");
    to.add(sSym);
    Sentence replacedRule = rule.replaceTerms(from, to);

    log.debug("Replaced sentence: " + replacedRule);
    //  [implies, 
    //  (and 
    //    :SENTENCE_MARKER 
    //    (flyingDoneBySomething-Move :FLIGHT_MARKER ?PLANE) 
    //    (endingDate :FLIGHT_MARKER ?END-DATE) 
    //    (toLocation :FLIGHT_MARKER ?TO)), 
    //  (holdsIn ?END-DATE (artifactFoundInLocation ?PLANE ?TO))] 

    assertEquals(sSym, ((Sentence) replacedRule.getArgument(1)).getArgument(1));
    assertEquals(fSym, ((Sentence) ((Sentence) replacedRule.getArgument(1)).getArgument(3)).getArgument(1));
  }

  @Test
  public void testSentenceOperator() throws KbException {
    KbCollection cPlane = KbCollectionImpl.get("CommercialAircraft");
    Variable varp = new VariableImpl("?PLANE");
    SentenceImpl s1 = new SentenceImpl(KbPredicateImpl.get("isa"), varp, cPlane);

    Sentence notS1 = SentenceImpl.SentenceOperatorImpl.NOT.wrap(s1);
    assertEquals(new SentenceImpl(LogicalConnectiveImpl.get("#$not"), s1), notS1);
  }
  
  @Test
  public void testJavaDateWrapping() throws ParseException, KbTypeException, CreateException {
    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd HH:mm");
    final Date date = sdf.parse("2014 03 15 10:20:23");
    
    final Sentence dateWrappingSentence = new SentenceImpl(date);
    assertEquals("((MinuteFn 20 (HourFn 10 (DayFn 15 (MonthFn March (YearFn 2014))))))", dateWrappingSentence.toString());
    
    final Date innerDate = new SentenceImpl(date).getArgument(0);
    assertEquals(date, innerDate);
  }
  
  @Test
  public void testToString() throws KbException {
    System.out.println("toString");
    final String expected = "(isa Thing Predicate)";
    final Sentence sentence = new SentenceImpl(expected);
    final String result = sentence.toString();
    System.out.println("Sentence: " + result);
    assertEquals(expected, result);
  }
  
}
