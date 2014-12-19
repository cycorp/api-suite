package com.cyc.query;

/*
 * #%L
 * File: QueryTest.java
 * Project: Query API
 * %%
 * Copyright (C) 2013 - 2014 Cycorp, Inc.
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
import com.cyc.query.exception.QueryConstructionException;
import com.cyc.base.CycAccessManager;
import com.cyc.base.CycConnectionException;
import com.cyc.base.CycTimeOutException;
import com.cyc.base.cycobject.CycAssertion;
import com.cyc.base.cycobject.CycConstant;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.inference.InferenceStatus;
import com.cyc.base.inference.InferenceSuspendReason;
import com.cyc.baseclient.CycObjectFactory;
import com.cyc.baseclient.inference.DefaultInferenceStatus;
import com.cyc.baseclient.inference.params.OpenCycInferenceParameterEnum;
import com.cyc.kb.Fact;
import com.cyc.kb.KBCollection;
import com.cyc.kb.KBIndividual;
import com.cyc.kb.KBObject;
import com.cyc.kb.Variable;
import com.cyc.kb.client.Constants;
import com.cyc.kb.client.KBIndividualImpl;
import com.cyc.kb.client.SentenceImpl;
import com.cyc.kb.client.VariableImpl;
import com.cyc.kb.exception.KBApiException;
import com.cyc.kb.exception.KBApiRuntimeException;
import com.cyc.query.Query.Category;
import static com.cyc.query.QueryApiTestConstants.emu;
import static com.cyc.query.QueryApiTestConstants.zebra;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class QueryTest {

  private static final String queryStringAssembling = "(#$disjointWith #$Assembling ?COLL)";
  private static final String queryStringAbesAPresident = "(#$isa #$AbrahamLincoln #$UnitedStatesPresident)";
  private static final String queryStringConditional = "(implies " + queryStringAssembling + " " + queryStringAbesAPresident + ")";
  private Query q = null;
  private KBInferenceResultSet r = null;

  @BeforeClass
  public static void setUpClass() throws Exception {
    TestUtils.ensureConstantsInitialized();
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
    if (q != null) {
      q.close();
    }
  }

  @Test
  public void testGetInferenceIdentifier() throws IOException, QueryConstructionException {
    System.out.println("testGetInferenceIdentifier");
    q = new Query(testConstants().queryAnimals, Constants.inferencePSCMt());
    try {
      q.retainInference();
      assertNull(q.getInferenceIdentifier());
      q.addListener(new QueryListener() {
        int problemStoreID;

        @Override
        public void notifyInferenceCreated(Query query) {
          try {
            assertNotNull(q.getInferenceIdentifier());
            problemStoreID = q.getInferenceIdentifier().getProblemStoreID();
            System.out.println("Problem store ID: " + problemStoreID);
            assertTrue("Got problem store ID " + problemStoreID, problemStoreID > 1);
          } catch (CycConnectionException ex) {
            ex.printStackTrace();
            fail("Caught exception.");
          }
        }

        @Override
        public void notifyInferenceStatusChanged(InferenceStatus oldStatus, InferenceStatus newStatus, InferenceSuspendReason suspendReason, Query query) {
          System.out.println(oldStatus + " -> " + newStatus);
        }

        @Override
        public void notifyInferenceAnswersAvailable(Query query, List<QueryAnswer> newAnswers) {
          try {
            final int problemStoreID1 = q.getInferenceIdentifier().getProblemStoreID();
            assertEquals("Inference answers available; problem store ID now " + problemStoreID1,
                    problemStoreID, problemStoreID1);
          } catch (CycConnectionException ex) {
            ex.printStackTrace();
            fail("Caught exception.");
          }
        }

        @Override
        public void notifyInferenceTerminated(Query query, Exception e) {
          try {
            final int problemStoreID1 = q.getInferenceIdentifier().getProblemStoreID();
            assertEquals("Inference terminated; problem store ID now " + problemStoreID1,
                    problemStoreID, problemStoreID1);
          } catch (CycConnectionException ex) {
            ex.printStackTrace();
            fail("Caught exception.");
          }
        }
      });
      q.performInference();
      assertNotNull(q.getInferenceIdentifier());
    } catch (Exception ex) {
      ex.printStackTrace();
      fail("Caught exception running testGetInferenceIdentifier");
    }
  }

  @Test
  public void testQueryString() {

    try {
      String queryStr = queryStringAssembling;
      q = new Query(queryStr);
      KBInferenceResultSet results = q.getResultSet();
      while (results.next()) {
        results.getObject("?COLL");
      }

    } catch (Exception e) {
      e.printStackTrace();
      fail("Failed to create a Query from a query string.");
    }
  }

  @Test
  public void testBooleanQueryString() {

    try {
      String queryStr = queryStringAbesAPresident;
      q = new Query(queryStr);
      assertTrue(q.isTrue());

    } catch (Exception e) {
      e.printStackTrace();
      fail("Failed to create a Query from a query string.");
    }
  }

  @Test
  public void testCycAssertionAsBinding() throws Exception {
    final VariableImpl var = new VariableImpl("AS");
    q = new Query(new SentenceImpl(testConstants().assertionSentence, var,
            testConstants().genlsAnimalX), Constants.inferencePSCMt());
    q.setMaxNumber(1);
    final Object binding = q.getAnswerCyc(0).getBinding(CycObjectFactory.makeCycVariable(var.getName()));
    assertTrue("Wanted a CycAssertion, got " + binding.getClass().getSimpleName(),
            binding instanceof CycAssertion);
  }

  @Test
  public void testFactAsBinding() throws Exception {
    final VariableImpl var = new VariableImpl("AS");
    q = new Query(new SentenceImpl(testConstants().assertionSentence, var,
            testConstants().genlsAnimalX), Constants.inferencePSCMt());
    q.setMaxNumber(1);
    final Object binding = q.getAnswer(0).getBinding(var);
    assertTrue("Wanted a Fact, got " + binding.getClass().getSimpleName(),
            binding instanceof Fact);
  }

  @Test
  public void testQueryStringString() {

    try {
      r = new Query(testConstants().whatIsAbe.toString(), testConstants().peopleDataMt.toString()).getResultSet();
      while (r.next()) {
        System.out.println("All types: " + r.getObject("?TYPE", KBCollection.class));
      }

    } catch (Exception e) {
      e.printStackTrace();
      fail("Failed to create a Query from a query string.");
    }
  }

  private QueryApiTestConstants testConstants() throws KBApiRuntimeException {
    return QueryApiTestConstants.getInstance();
  }

  @Test
  public void testContinuableQuery() {
    System.out.println("testContinuableQuery");
    try {
      q = new Query(testConstants().whatIsAbe, testConstants().peopleDataMt);
      q.setInferenceMode(OpenCycInferenceParameterEnum.OpenCycInferenceMode.MINIMAL_MODE);
      q.setMaxNumber(1);
      q.setContinuable(true);
      assertTrue("Query not continuable.", q.isContinuable());
      int answerCount = q.getAnswerCount();
      assertEquals("Expected one answer, got " + answerCount, 1, answerCount);
      q.continueQuery();
      int updatedAnswerCount = q.getAnswerCount();
      while (updatedAnswerCount > answerCount) {
        answerCount = updatedAnswerCount;
        q.continueQuery();
        updatedAnswerCount = q.getAnswerCount();
      }
      assertTrue("Found only " + answerCount + " answers.", answerCount > 1);
    } catch (Exception e) {
      e.printStackTrace();
      fail("Failed to create a Query from a query string.");
    }
  }
  
  @Test
  public void testNonContinuableQuery() {
    System.out.println("test setContinuable(false)");
    try {
      q = new Query(testConstants().whatIsAbe, testConstants().peopleDataMt);
      q.setMaxNumber(1);
      q.setInferenceMode(OpenCycInferenceParameterEnum.OpenCycInferenceMode.SHALLOW_MODE);
      q.setContinuable(false);
      assertFalse("Query parameters are continuable.", q.getInferenceParameters().isContinuable());
      q.continueQuery();
      assertFalse("Query is continuable.", q.isContinuable());
    } catch (Exception e) {
      e.printStackTrace();
      fail("Failed to create a Query from a query string.");
    }
  }

  @Test
  public void testQueryStringStringString() {
    try {

      String queryStr = testConstants().whatIsAbe.toString();

      r = new Query(queryStr, testConstants().peopleDataMt.toString(),
              ":INFERENCE-MODE :MINIMAL :MAX-TIME 1 :MAX-NUMBER 12").getResultSet();
      while (r.next()) {
        System.out.println("TYPE: " + r.getObject("?TYPE", KBObject.class));
      }

    } catch (Exception e) {
      e.printStackTrace();
      fail("Failed to create a Query from a query string.");
    }
  }

  /**
   * Test of getId method, of class Query.
   */
  @Test
  public void testGetId() throws Exception {
    System.out.println("getId and saveAs");
    q = new Query(queryStringAssembling);
    final KBIndividual id = q.saveAs("TestQuery-AssemblingSlots");
    try {
      assertEquals(id, q.getId());
    } finally {
      id.delete();
    }
  }

  /**
   * Test of load method, of class Query.
   */
  @Test
  public void testLoad() throws Exception {
    System.out.println("load");
    //Query conceptFinder = Query.load(new KBIndividual("AURORAQuery-PredictAllFeaturesFromReifiedVideosUsingThisFeatureSet"));
    q = new Query(queryStringAssembling);
    final KBIndividual id = q.saveAs("TestQuery-AssemblingSlots");
    try {
      final Query loadedQuery = Query.load(id);
      assertEquals("Query contexts are different.", q.getContext(),
              loadedQuery.getContext());
      assertEquals("Query sentences are different.", q.getQuerySentenceCyc(),
              loadedQuery.getQuerySentenceCyc());
    } finally {
      id.delete();
    }
  }

  //@Test Depends on #$TestKBQuery
  public void testKBQ() throws Exception {
    r = Query.load(KBIndividualImpl.get("TestKBQuery")).getResultSet();
    while (r.next()) {
      System.out.println("Dog: " + r.getObject(TestUtils.X.toString(),
              KBIndividual.class));
    }
  }

  @Test
  public void testKBQIndexical() throws Exception {
    System.out.println("testKBQIndexical");

    q = new Query(testConstants().theAnimalIsAnAnimal, Constants.inferencePSCMt());
    q.setMaxNumber(10);
    q.saveAs("TestKBQueryIndexical-2012-01-16");

    try {
      Map<CycObject, Object> indexicals = new HashMap<CycObject, Object>();
      indexicals.put(testConstants().theAnimal.getCore(),
              CycObjectFactory.makeCycVariable("X"));

      Query query = Query.loadCycObjectMap(KBIndividualImpl.get("TestKBQueryIndexical-2012-01-16"),
              indexicals);
      r = query.getResultSet();
      while (r.next()) {
        System.out.println("Animal: " + r.getObject("?X", KBIndividual.class));
      }
    } finally {
      q.getId().delete();
    }
  }

  @Test
  public void testKBQIndexicalKBObject() throws Exception {
    System.out.println("testKBQIndexicalKBObject");

    q = new Query(testConstants().theAnimalIsAnAnimal, Constants.inferencePSCMt());
    q.setMaxNumber(10);
    q.saveAs("TestKBQueryKBObject-2014-04-1");

    try {
      Map<KBObject, Object> indexicals = new HashMap<KBObject, Object>();
      indexicals.put(testConstants().theAnimal, new VariableImpl("X"));

      r = Query.load(KBIndividualImpl.get("TestKBQueryKBObject-2014-04-1"),
              indexicals).getResultSet();
      while (r.next()) {
        System.out.println("Animal: " + r.getObject("?X", KBIndividual.class));
      }
    } finally {
      q.getId().delete();
    }
  }

  @Test
  public void testKBQIndexicalString() throws Exception {
    System.out.println("testKBQIndexicalString");

    q = new Query(testConstants().theAnimalIsAnAnimal, Constants.inferencePSCMt());
    q.setMaxNumber(10);
    q.saveAs("TestKBQuery-Vijay-2012-01-16");
    try {
      // The bound query is converted to an unbound query.
      // The "(#$TheFn #$Dog)" in the query is replaced with "?X".
      Map<String, String> indexicals = new HashMap<String, String>();
      indexicals.put(testConstants().theAnimal.toString(), "?X");

      r = Query.load("TestKBQuery-Vijay-2012-01-16", indexicals).getResultSet();

      while (r.next()) {
        System.out.println("Animal: " + r.getObject("?X", KBIndividual.class));
      }
    } finally {
      q.getId().delete();
    }
  }

  @Test
  public void testKBQIndexicalVarToConst() throws Exception {
    System.out.println("testKBQIndexicalVarToConst");

    q = new Query(
            SentenceImpl.and(testConstants().xIsAnAnimal, testConstants().yOwnsX),
            Constants.everythingPSCMt());
    q.saveAs("TestKBQueryAnimalOwners-Vijay-2012-01-16");

    try {
      Map<KBObject, Object> indexicals = new HashMap<KBObject, Object>();
      indexicals.put(new VariableImpl("Y"), testConstants().abrahamLincoln);
      final Query loadedQuery = Query.load(KBIndividualImpl.get("TestKBQueryAnimalOwners-Vijay-2012-01-16"), indexicals);
      System.out.println("Result of replacing variable: " + loadedQuery);
      r = loadedQuery.getResultSet();
      System.out.println("Result set: " + r);
      while (r.next()) {
        System.out.println("Answer " + r.getObject("?X", KBIndividual.class));
      }
      loadedQuery.close();
    } finally {
      q.getId().delete();
    }
  }

  @Test
  //@TODO Replace this with a test using vocabulary in OpenCyc (or at least RCyc).
  public void testKBQIndexicalAurora() throws Exception {
    if (!CycAccessManager.getCurrentAccess().isOpenCyc()
            && CycAccessManager.getCurrentAccess().getLookupTool().find("AuroraConceptIDSourceStore") instanceof CycConstant) {
      System.out.println("testKBQIndexicalAurora");
      Map<CycObject, Object> binding = new HashMap<CycObject, Object>();
      binding.put(CycObjectFactory.makeCycVariable("?VIDEO-ID"), 27850);
      //Idiotically, need #$ here
      binding.put(CycAccessManager.getCurrentAccess().getLookupTool().getKnownFortByName(
              "(#$TheFn #$AuroraConceptIDSourceStore)"),
              CycAccessManager.getCurrentAccess().getLookupTool().getKnownFortByName("MED12-SIN-Concept-List"));

      q = Query.loadCycObjectMap(
              KBIndividualImpl.get(
                      "AURORAQuery-PredictAllFeaturesFromReifiedVideosUsingThisFeatureSet"),
              binding);
      q.setMaxTime(30).setMaxNumber(10);
      r = q.getResultSet();
      while (r.next()) {
        System.out.println("Answer " + r.getObject("?READABLE",
                String.class));
      }
    }
  }

  /**
   * Test of getCategories method, of class Query.
   */
  @Test
  public void testGetCategories() throws IOException, QueryConstructionException {
    System.out.println("getCategories");
    q = new Query(queryStringAssembling);
    assertTrue(q.getCategories().isEmpty());
  }

  /**
   * Test of addCategory method, of class Query.
   */
  @Test
  public void testAddCategory() throws IOException, QueryConstructionException {
    System.out.println("addCategory");
    q = new Query(queryStringAssembling);
    Category cat = Category.get("Test Queries");
    q.addCategory(cat);
    assertTrue(q.getCategories().contains(cat));
  }

  /**
   * Test of getAnswerCount method, of class Query.
   *
   */
  @Test
  public void testGetAnswerCount() throws CycConnectionException, QueryConstructionException {
    System.out.println("getAnswerCount");
    q = new Query(testConstants().queryAnimals, Constants.inferencePSCMt());
    assertEquals(2, q.getAnswerCount());
  }

  /**
   * Test of getContext method, of class Query.
   */
  @Test
  public void testGetContext() throws Exception {
    System.out.println("getContext");
    q = new Query(queryStringAssembling);
    assertEquals(Constants.inferencePSCMt(), q.getContext());
  }

  /**
   * Test of getQuerySentence method, of class Query.
   */
  @Test
  public void testgetQuerySentenceCyc() throws Exception {
    System.out.println("getQuerySentence");
    q = new Query(queryStringAssembling);
    assertEquals(queryStringAssembling, q.getQuerySentenceCyc().cyclify());
  }

  /**
   * Test of getQuerySentenceMainClauseCyc method, of class Query.
   */
  @Test
  public void testgetQuerySentenceMainClauseCyc() throws IOException, QueryConstructionException {
    System.out.println("getQuerySentenceMainClauseCyc");
    q = new Query(queryStringAssembling);
    assertEquals(queryStringAssembling,
            q.getQuerySentenceMainClauseCyc().cyclify());
  }

  /**
   * Test of getQuerySentenceHypothesizedClause method, of class Query.
   */
  @Test
  public void testGetQuerySentenceHypothesizedClause() throws IOException, QueryConstructionException {
    System.out.println("getQuerySentenceHypothesizedClause");
    q = new Query(queryStringConditional);
    assertEquals(queryStringAssembling,
            q.getQuerySentenceHypothesizedClauseCyc().cyclify());
    assertEquals(queryStringAbesAPresident,
            q.getQuerySentenceMainClauseCyc().cyclify());
  }

  /**
   * Test of getMaxTime method, of class Query.
   */
  @Test
  public void testGetMaxTime() throws IOException, QueryConstructionException {
    System.out.println("getMaxTime");
    q = new Query(queryStringAssembling);
    assertEquals(null, q.getMaxTime());
  }

  /**
   * Test of getMaxNumber method, of class Query.
   */
  @Test
  public void testGetMaxNumber() throws IOException, QueryConstructionException {
    System.out.println("getMaxNumber");
    q = new Query(queryStringAssembling);
    assertEquals(null, q.getMaxNumber());
  }

  /**
   * Test of getInferenceMode method, of class Query.
   */
  @Test
  public void testGetInferenceMode() throws IOException, QueryConstructionException {
    System.out.println("getInferenceMode");
    q = new Query(queryStringAssembling);
    assertEquals(null, q.getInferenceMode());
  }

  /**
   * Test of getStatus method, of class Query.
   *
   * @throws CycConnectionException
   * @throws CycTimeOutException
   */
  @Test
  public void testGetStatus() throws CycConnectionException, QueryConstructionException {
    System.out.println("getStatus");
    q = new Query(queryStringAssembling);
    q.performInference();
    assertEquals(DefaultInferenceStatus.SUSPENDED, q.getStatus());
  }

  /**
   * Test of get method, of class Query.
   */
  @Test
  public void testGet() throws Exception {
    System.out.println("get");
    r = new Query(testConstants().queryAnimals, Constants.inferencePSCMt()).getResultSet();
    r.next();
    final KBCollection animal = (KBCollection) r.getKBObject("?N");
    final List<KBCollection> emuAndZebra = Arrays.asList(emu(), zebra());
    assertTrue("Couldn't find " + animal + " (" + animal.getClass().getSimpleName()
            + ") in " + emuAndZebra, emuAndZebra.contains(animal));
  }

  /**
   * Test of isTrue method, of class Query.
   */
  @Test
  public void testIsTrue() throws Exception {
    System.out.println("isTrue");
    q = new Query(queryStringAbesAPresident);
    assertTrue(q.isTrue());
  }

  /**
   * Test of isProvable method, of class Query.
   */
  @Test
  public void testIsProvable() throws Exception {
    System.out.println("isProvable");
    q = new Query(queryStringAbesAPresident);
    assertTrue(q.isProvable());
    q.close();
    q = new Query(testConstants().queryAnimals, Constants.inferencePSCMt());
    assertTrue(q.isProvable());
    q.getResultSet().afterLast();
    assertTrue(q.isProvable());
  }

  @Test
  public void testClearResults() throws Exception {
    System.out.println("clearResults");
    q = new Query(testConstants().whatTimeIsIt, Constants.inferencePSCMt());
    final Variable var = q.getQuerySentence().getArgument(2);
    final Object firstTime = q.getAnswer(0).getBinding(var);
    assertEquals(firstTime, q.getAnswer(0).getBinding(var));
    q.clearResults();
    Thread.sleep(1500);
    assertFalse(firstTime.equals(q.getAnswer(0).getBinding(var)));
  }

  /**
   * Test of next method, of class Query.
   */
  @Test
  public void testNext() throws Exception {
    System.out.println("next");
    r = new Query(testConstants().queryAnimals, Constants.inferencePSCMt()).getResultSet();
    assertTrue(r.next());
  }

  /**
   * Test of close method, of class Query.
   */
  @Test
  public void testClose() throws IOException, QueryConstructionException {
    System.out.println("close");
    q = new Query(testConstants().queryAnimals, Constants.inferencePSCMt());
    q.close();
  }

  /**
   * Test of getResultSet method, of class Query.
   */
  @Test
  public void testGetResultSet() throws Exception {
    System.out.println("getResultSet");
    q = new Query(testConstants().queryAnimals, Constants.inferencePSCMt());
    q.getResultSet();
  }

  /**
   * Test of getQueryVariablesCyc method, of class Query.
   */
  @Test
  public void testGetQueryVariables() throws IOException, QueryConstructionException, KBApiException {
    System.out.println("getQueryVariablesCyc");
    q = new Query(testConstants().queryAnimals, Constants.inferencePSCMt());
    assertTrue(q.getQueryVariables().contains(new VariableImpl("N")));
    q.close();
    q = new Query(queryStringConditional);
    assertTrue(q.getQueryVariables().isEmpty());
  }
}
