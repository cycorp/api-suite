/*
 * Copyright 2015 Cycorp, Inc..
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
package com.cyc.query;

/*
 * #%L
 * File: InferenceAnswerBackedQueryAnswerIT.java
 * Project: Query Client
 * %%
 * Copyright (C) 2013 - 2015 Cycorp, Inc.
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
import com.cyc.base.exception.CycConnectionException;
import com.cyc.base.inference.InferenceAnswer;
import com.cyc.kb.Context;
import com.cyc.kb.KbTerm;
import com.cyc.kb.Sentence;
import com.cyc.kb.Variable;
import com.cyc.kb.client.ContextImpl;
import com.cyc.kb.client.SentenceImpl;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbException;
import com.cyc.kb.exception.KbTypeException;
import static com.cyc.query.TestUtils.assumeNotOpenCyc;
import com.cyc.query.exception.QueryConstructionException;
import com.cyc.session.exception.SessionCommunicationException;
import com.cyc.session.exception.SessionConfigurationException;
import com.cyc.session.exception.SessionInitializationException;
import java.util.Map;
import java.util.Set;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author daves
 */
public class InferenceAnswerBackedQueryAnswerIT {

  public InferenceAnswerBackedQueryAnswerIT() {
  }

  @BeforeClass
  public static void setUpClass() throws SessionConfigurationException, SessionCommunicationException, SessionInitializationException, CycConnectionException {
    com.cyc.baseclient.testing.KbPopulator.ensureKBPopulated(CycAccessManager.getCurrentAccess());
  }

  @AfterClass
  public static void tearDownClass() {
  }

  /**
   * Test of getId method, of class InferenceAnswerBackedQueryAnswer.
   */
  //@Test
  public void testGetId() {
    System.out.println("getId");
    InferenceAnswerBackedQueryAnswer instance = null;
    InferenceAnswerIdentifier expResult = null;
    InferenceAnswerIdentifier result = instance.getId();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getAnswerCyc method, of class InferenceAnswerBackedQueryAnswer.
   */
  //@Test
  public void testGetAnswerCyc() {
    System.out.println("getAnswerCyc");
    InferenceAnswerBackedQueryAnswer instance = null;
    InferenceAnswer expResult = null;
    InferenceAnswer result = instance.getAnswerCyc();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getBinding method, of class InferenceAnswerBackedQueryAnswer.
   */
  //@Test
  public void testGetBinding() {
    System.out.println("getBinding");
    Variable var = null;
    InferenceAnswerBackedQueryAnswer instance = null;
    Object expResult = null;
    Object result = instance.getBinding(var);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getProofIdentifier method, of class InferenceAnswerBackedQueryAnswer.
   */
  //@Test
  public void testGetProofIdentifier() {
    System.out.println("getProofIdentifier");
    InferenceAnswerBackedQueryAnswer instance = null;
    ProofIdentifier expResult = null;
    ProofIdentifier result = instance.getProofIdentifier();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getProofIdentifiers method, of class InferenceAnswerBackedQueryAnswer.
   */
  //@Test
  public void testGetProofIdentifiers() throws Exception {
    System.out.println("getProofIdentifiers");
    InferenceAnswerBackedQueryAnswer instance = null;
    Set<ProofIdentifier> expResult = null;
    Set<ProofIdentifier> result = instance.getProofIdentifiers();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getBindings method, of class InferenceAnswerBackedQueryAnswer.
   */
  //@Test
  public void testGetBindings() {
    System.out.println("getBindings");
    InferenceAnswerBackedQueryAnswer instance = null;
    Map<Variable, Object> expResult = null;
    Map<Variable, Object> result = instance.getBindings();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of toString method, of class InferenceAnswerBackedQueryAnswer.
   */
  //@Test
  public void testToString() {
    System.out.println("toString");
    InferenceAnswerBackedQueryAnswer instance = null;
    String expResult = "";
    String result = instance.toString();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getSources method, of class InferenceAnswerBackedQueryAnswer.
   */
  @Test
  public void testGetSources() throws KbTypeException, CreateException, SessionCommunicationException, QueryConstructionException, KbException {
    System.out.println("getSources");
    assumeNotOpenCyc();
    final Context WHH_WP_CTX = ContextImpl.findOrCreate("(#$ContextOfPCWFn #$TestFactEntrySource-WikipediaArticle-WilliamHenryHarrison)");
    Sentence querySentence = new SentenceImpl("(#$isa #$WilliamHenryHarrison (#$FormerFn #$UnitedStatesPresident))");
    Query q = QueryFactory.getQuery(querySentence, WHH_WP_CTX);
    q.retainInference();
    QueryAnswer answer = q.getAnswer(0);
    Set<KbTerm> sources = answer.getSources();
    assertFalse(sources.isEmpty());
  }

  /**
   * Test of getSources method, of class InferenceAnswerBackedQueryAnswer.  Should throw an exception because
   * the inference has not been retained.
   */
  @Test(expected=UnsupportedOperationException.class)
  public void testGetSourcesFromClosedInference() throws KbTypeException, CreateException, SessionCommunicationException, QueryConstructionException, KbException {
    System.out.println("getSourcesFromClosed");
    assumeNotOpenCyc();
    final Context WHH_WP_CTX = ContextImpl.findOrCreate("(#$ContextOfPCWFn #$TestFactEntrySource-WikipediaArticle-WilliamHenryHarrison)");
    Sentence querySentence = new SentenceImpl("(#$isa #$WilliamHenryHarrison (#$FormerFn #$UnitedStatesPresident))");
    Query q = QueryFactory.getQuery(querySentence, WHH_WP_CTX);
    QueryAnswer answer = q.getAnswer(0);
    q.close();
    Set<KbTerm> sources = answer.getSources();
    assertFalse("This code should never be run, due to the exception that gets thrown.", true);
  }

}
