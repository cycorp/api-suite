package com.cyc.kb.client;

/*
 * #%L
 * File: KBObjectFactoryTest.java
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

import com.cyc.base.CycAccess;
import com.cyc.base.CycAccessManager;
import com.cyc.base.cycobject.CycAssertion;
import com.cyc.base.cycobject.ELMt;
import com.cyc.base.cycobject.FormulaSentence;
import com.cyc.base.cycobject.Nart;
import com.cyc.base.cycobject.Naut;
import com.cyc.baseclient.cycobject.CycAssertionImpl;
import com.cyc.baseclient.cycobject.CycFormulaSentence;
import com.cyc.kb.Assertion;
import com.cyc.kb.Context;
import com.cyc.kb.KBIndividual;
import com.cyc.kb.KBObject;
import com.cyc.kb.exception.KBApiException;
import com.cyc.session.SessionApiException;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import org.junit.AfterClass;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;


public class KBObjectFactoryTest {

  private static Logger log = null;

  @BeforeClass
  public static void setUp() throws Exception {
    log = Logger.getLogger(KBObjectFactoryTest.class.getName());
    TestConstants.ensureInitialized();
  }

  @AfterClass
  public static void tearDown() throws Exception {
  }
  
  @Test
  public void testAssertionsAreNotSentences() throws SessionApiException, KBApiException, UnknownHostException, IOException, Exception {
//    FormulaSentence istSentence = CycFormulaSentence.makeCycSentence("(ist LogicalTruthMt (isa Collection Collection))");
    CycAccess cyc = CycAccessManager.getCurrentAccess();
    FormulaSentence cycSentence = CycFormulaSentence.makeCycSentence(cyc, "(isa Collection Collection)");
    ELMt ltMt = CycAccessManager.getCurrentAccess().getObjectTool().makeELMt("LogicalTruthMt");
    CycAssertion cycAssert = new CycAssertionImpl(cycSentence, ltMt);
    Assertion a = AssertionImpl.get(cycAssert); // TODO: since this is failing, it looks like the getter doesn't properly accept ist sentences
    KBObject s = new SentenceImpl(a.getCore());
    assertNotSame("Got the same object for " + cycSentence + " as both an Assertion and a Sentence.", s, a);
    assertTrue ("Got a " + s.getClass() + " when expecting a SentenceImpl", s instanceof SentenceImpl);
    assertTrue ("Got a " + a.getClass() + " when expecting an Assertion", a instanceof Assertion);
  }

  @Test
  public void testRuleCanonicalizationOKForCaches() throws SessionApiException, KBApiException, UnknownHostException, IOException, Exception {
    CycAccess cyc = CycAccessManager.getCurrentAccess();
    ELMt coreCyclMt = cyc.getObjectTool().makeELMt("CoreCycLMt");
    Context coreCyclMtContext = ContextImpl.get(coreCyclMt);
    FormulaSentence cycSentence = CycFormulaSentence.makeCycSentence(cyc, "(implies (and "
            + "(natFunction ?NAT ?FUNCTION) "
            + "(resultIsa ?FUNCTION ?COL)) "
            + "(isa ?NAT ?COL))");
    Assertion a = AssertionImpl.get(new SentenceImpl(cycSentence), coreCyclMtContext); 
    FormulaSentence cycSentence2 = CycFormulaSentence.makeCycSentence(cyc, "(implies (and "
            + "(natFunction ?NAT2 ?FUNCTION) "
            + "(resultIsa ?FUNCTION ?COL)) "
            + "(isa ?NAT2 ?COL))");
    Assertion a2 = AssertionImpl.get(new SentenceImpl(cycSentence2), coreCyclMtContext);
    assertSame("Got different rules back for what should be the same assertion.", a, a2);
  }
  
  @Test
  public void testNartAndNautWithSameFormula() throws KBApiException, UnknownHostException, IOException, Exception {
    Naut naut = CycAccessManager.getCurrentAccess().getObjectTool().makeCycNaut("(#$AirForceFn #$France)");
    Nart nart = CycAccessManager.getCurrentAccess().getLookupTool().getCycNartFromCons(naut.toCycList());
    KBIndividual nartInd = KBIndividualImpl.get(nart);
    KBIndividual nautInd = KBIndividualImpl.get(naut);
    assertSame("Got the different KBIndividuals back for what should be the same NAT.", nartInd, nautInd);
  }

  
  @Test
  public void testAlwaysGetTightestType() throws KBApiException {
    KBCollectionImpl c1 = KBCollectionImpl.get("Dog");
    KBCollectionImpl c2 = FirstOrderCollectionImpl.get("Dog");
    assertSame("Didn't get the same when looking for Dog as a KBCollection and a FirstOrderCollection", c1, c2);

  }


}
