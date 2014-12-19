/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cyc.kb.client;

/*
 * #%L
 * File: SentenceTest.java
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
import com.cyc.base.CycApiException;
import com.cyc.base.CycConnectionException;
import com.cyc.baseclient.cycobject.CycFormulaSentence;
import com.cyc.kb.KBCollection;
import com.cyc.kb.KBIndividual;
import com.cyc.kb.KBPredicate;
import com.cyc.kb.Sentence;
import com.cyc.kb.Symbol;
import com.cyc.kb.Variable;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KBApiException;
import com.cyc.kb.exception.KBTypeException;
import com.cyc.kb.quant.InstanceRestrictedVariable;
import com.cyc.kb.quant.RestrictedVariable;
import com.cyc.kb.quant.SpecializationRestrictedVariable;
import java.io.IOException;
import java.util.ArrayList;
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

  private static org.slf4j.Logger log = LoggerFactory.getLogger(SentenceTest.class.getCanonicalName());

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
  public void testSentenceArgs() throws KBApiException {
    System.out.println("Sentence (Object[] args)");

    KBCollection c = KBCollectionImpl.get("#$Dog");
    RestrictedVariable rv = c.toInstanceRestrictedVariable();

    KBPredicate p = KBPredicateImpl.get("#$owns");
    //Variable v = new Variable ("?PER");
    KBIndividual d = KBIndividualImpl.get("TestIndividual001");

    List<Object> ol = new ArrayList();
    ol.add(p);
    //ol.add(v);
    ol.add(d);
    ol.add(rv);

    Sentence instance = new SentenceImpl(ol.toArray());
    System.out.println("Sentence: " + instance);
  }

  @Test
  public void testSentenceArgs2() throws KBApiException {
    System.out.println("Sentence (Object[] args)");

    KBCollection c = KBCollectionImpl.get("#$Dog");
    RestrictedVariable rv1 = c.toInstanceRestrictedVariable();
    KBPredicate p = KBPredicateImpl.get("#$owns");
    KBCollection c2 = KBCollectionImpl.get("#$Person");
    RestrictedVariable rv2 = c2.toInstanceRestrictedVariable();

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
  public void testSentenceArgs3() throws KBApiException {
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
    ol3.add(KBFunctionImpl.get("Quote"));
    ol3.add(nestedS);
    SentenceImpl q = new SentenceImpl(ol3.toArray());
    q.addFact(ContextImpl.get("UniversalVocabularyMt"), KBPredicateImpl.get("sentenceParameterValueInSpecification"), 1, KBIndividualImpl.findOrCreate("SomeQuerySpecification"));

  }

  @Test
  public void testNegatedSentenceFromString() throws KBApiException, CycApiException, CycConnectionException {
    System.out.println("testNegatedSentenceFromString");
    Sentence instance = new SentenceImpl(CycFormulaSentence.makeCycSentence(TestConstants.cyc, "(#$not (#$isa #$Thing #$Predicate))"));
    System.out.println("Sentence: " + instance);
    assertTrue(instance instanceof Sentence);

    Sentence expSent = new SentenceImpl(CycFormulaSentence.makeCycSentence(TestConstants.cyc, "(#$isa #$Thing #$Predicate)"));
    Sentence subSent = instance.getArgument(1);
    assertEquals(expSent, subSent);

    assertEquals(KBCollectionImpl.get("Predicate"), subSent.getArgument(2));
  }

  @Test
  public void testNegatedSentence() throws KBApiException, CycApiException, IOException, CycConnectionException {
    System.out.println("testNegatedSentence");
    System.out.println("testNegatedSentenceFromString");
    Sentence base = new SentenceImpl(CycFormulaSentence.makeCycSentence(TestConstants.cyc, "(#$isa #$Thing #$Predicate)"));
    Sentence instance = new SentenceImpl(KBTermImpl.get("not"), base);
    System.out.println("Sentence: " + instance);
    assertTrue(instance instanceof Sentence);
  }

  @Test
  public void testSentenceArgs4() throws KBApiException {
    System.out.println("Sentence (Object[] args)");

    List<Object> ol = new ArrayList();
    ol.add(KBFunctionImpl.get("TheList"));

    Sentence instance = new SentenceImpl(ol.toArray());
    System.out.println("Sentence: " + instance);
  }

  @Test
  public void testSentenceArgs5() throws KBApiException {
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
    ol.add(KBPredicateImpl.get("relationAllExists"));
    ol.add(new InstanceRestrictedVariable(null, KBCollectionImpl.get("QuantificationalRuleMacroPredicate-Canonical")));
    ol.add(new SpecializationRestrictedVariable(null, KBCollectionImpl.get("Event")));
    ol.add(new SpecializationRestrictedVariable(null, KBCollectionImpl.get("Dog")));

    Sentence instance = new SentenceImpl(ol.toArray());
    System.out.println("Sentence: " + instance);
  }

  @Test
  public void testSentenceArgs6() throws KBTypeException, CycApiException, CycConnectionException, CreateException {
    System.out.println("Testing args from CycSentence");
    assertNotNull(new SentenceImpl(CycFormulaSentence.makeCycSentence(TestConstants.cyc, "(#$isa #$Thing #$Predicate)")).getArgument(2));
  }

  /**
   * Test of getTypeString method, of class Sentence.
   */
  //@Test
  public void testGetTypeString() throws KBApiException {
    System.out.println("getTypeString");
    SentenceImpl instance = new SentenceImpl("");
    String expResult = "";
    KBCollection result = (KBCollection) instance.getType();
    //assertEquals(expResult, result);
    // @TODO review the generated test code and remove the default call to fail.
    //fail("The test case is a prototype.");
  }

  public void testIsWff() throws KBTypeException, CreateException {
    KBIndividual attack = KBIndividualImpl.findOrCreate("AttackByComputerOperation-WnErDFED8C");
    attack.instantiates(KBCollectionImpl.get("AttackByComputerOperation"));
    SentenceImpl s1 = new SentenceImpl(KBPredicateImpl.get("doneBy"), attack, KBIndividualImpl.get("Plane-APITest"));
    assertTrue(!s1.isAssertible(ContextImpl.get("SomeAirlineEquipmentMt")));
    SentenceImpl s2 = new SentenceImpl(KBPredicateImpl.get("doneBy"), attack, KBIndividualImpl.get("TestIndividual001"));
    assertTrue(s2.isAssertible(Constants.uvMt()));
  }

  @Test
  public void testReplaceTerms() throws KBApiException {
    KBCollection cPlane = KBCollectionImpl.get("CommercialAircraft");
    BinaryPredicateImpl flying2Pred1 = BinaryPredicateImpl.findOrCreate("flyingDoneBySomething-Move");
    Variable varp = new VariableImpl("?PLANE");
    Variable varf = new VariableImpl("?FLIGHT");
    Variable varend = new VariableImpl("?END-DATE");
    Variable vart = new VariableImpl("?TO");
    SentenceImpl s1 = new SentenceImpl(KBPredicateImpl.get("isa"), varp, cPlane);
    SentenceImpl s2 = new SentenceImpl(flying2Pred1, varf, varp);
    SentenceImpl s3 = new SentenceImpl(KBPredicateImpl.get("endingDate"), varf, varend);
    SentenceImpl s4 = new SentenceImpl(KBPredicateImpl.get("toLocation"), varf, vart);
    Sentence s5 = new SentenceImpl(KBPredicateImpl.get("artifactFoundInLocation"), varp, vart);
    Sentence s6 = new SentenceImpl(KBPredicateImpl.get("holdsIn"), varend, s5);
    List<Sentence> sandlist = new ArrayList<Sentence>();
    sandlist.add(s1);
    sandlist.add(s2);
    sandlist.add(s3);
    sandlist.add(s4);
    Sentence sand = SentenceImpl.and(sandlist);

    Sentence rule = new SentenceImpl(KBIndividualImpl.get("implies"), sand, s6);

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
  public void testSentenceOperator() throws KBApiException {
    KBCollection cPlane = KBCollectionImpl.get("CommercialAircraft");
    Variable varp = new VariableImpl("?PLANE");
    SentenceImpl s1 = new SentenceImpl(KBPredicateImpl.get("isa"), varp, cPlane);

    Sentence notS1 = SentenceImpl.SentenceOperatorImpl.NOT.wrap(s1);
    assertEquals(new SentenceImpl(LogicalConnectiveImpl.get("#$not"), s1), notS1);
  }
}
