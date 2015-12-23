/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cyc.query;

/*
 * #%L
 * File: AsynchronousQueryTest.java
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
import com.cyc.query.metrics.InferenceMetric;
import com.cyc.query.exception.QueryConstructionException;
import static com.cyc.query.TestUtils.*;

import com.cyc.base.CycAccessManager;
import com.cyc.base.exception.CycConnectionException;
import com.cyc.base.exception.CycTimeOutException;
import com.cyc.kb.ArgPosition;
import com.cyc.base.cycobject.FormulaSentence;
import com.cyc.kb.exception.KbException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.*;

import static org.junit.Assert.*;

import com.cyc.baseclient.CycObjectFactory;
import com.cyc.baseclient.cycobject.ArgPositionImpl;
import com.cyc.baseclient.cycobject.CycFormulaSentence;
import com.cyc.baseclient.inference.params.DefaultInferenceParameters;
import com.cyc.baseclient.inference.params.OpenCycInferenceParameterEnum.OpenCycInferenceMode;
import com.cyc.kb.Sentence;
import com.cyc.kb.Variable;
import com.cyc.kb.client.Constants;
import com.cyc.kb.client.SentenceImpl;
import com.cyc.query.metrics.InferenceMetricsValues;
import com.cyc.session.exception.SessionException;
import com.cyc.session.exception.SessionCommunicationException;
import com.cyc.session.exception.OpenCycUnsupportedFeatureException;

/**
 *
 * @author baxter
 */
public class AsynchronousQueryTest {

  private InferenceParameters defaultParams;

  public AsynchronousQueryTest() {
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
    TestUtils.ensureConstantsInitialized();
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }

  @Before
  public void setUp() {
    try {
      defaultParams = new DefaultInferenceParameters(CycAccessManager.getCurrentAccess());
    } catch (SessionException ex) {
      throw new RuntimeException(ex);
    } 
  }

  @After
  public void tearDown() {
    closeTestQuery();
  }

  /**
   * Test of start method, of class Query.
   *
   * @throws CycConnectionException
   */
  @Test
  public void testStart() throws QueryConstructionException, InterruptedException, SessionCommunicationException {
    System.out.println("start");
    q = constructXIsaBirdQuery();
    final InferenceStatus status = q.getStatus();
    q.start();
    waitForQueryToFinish();
    System.out.println(q.getStatus());
    assertFalse("Wrong query status after starting and waiting.", status.equals(q.getStatus()));
  }

  /**
   * Test of stop method, of class Query.
   *
   * @throws java.lang.InterruptedException
   * @throws com.cyc.query.exception.QueryConstructionException
   * @throws com.cyc.session.exception.SessionCommunicationException
   */
  @Test
  public void testStop() throws InterruptedException, QueryConstructionException, SessionCommunicationException {
    System.out.println("stop");
    q = constructXIsaBirdQuery();
    q.start();
    q.stop(1);
    waitForQueryToFinish();
    final InferenceStatus status = q.getStatus();
    System.out.println(q.getStatus());
    assertFalse("Wrong query status after starting, stopping and waiting.", status.equals(InferenceStatus.RUNNING));
  }

  /**
   * Test of continueQuery method, of class Query.
   *
   * @throws java.lang.InterruptedException
   * @throws com.cyc.query.exception.QueryConstructionException
   * @throws com.cyc.session.exception.SessionCommunicationException
   * @throws CycConnectionException
   */
  @Test
  public void testContinueQuery() throws InterruptedException, QueryConstructionException, SessionCommunicationException {
    System.out.println("continueQuery");
    q = constructXIsaBirdQuery();
    q.setInferenceMode(OpenCycInferenceMode.MAXIMAL_MODE);
    System.out.println(q.getStatus());
    q.start();
    System.out.println(q.getStatus());
    q.stop(1);
    waitForQueryToFinish();
    System.out.println(q.getStatus());
    q.continueQuery();
    System.out.println(q.getStatus());
  }

  /**
   * Test of addListener method, of class Query.
   *
   * @throws java.lang.InterruptedException
   * @throws com.cyc.query.exception.QueryConstructionException
   * @throws com.cyc.session.exception.SessionCommunicationException
   */
  @Test
  public void testAddListener() throws InterruptedException, QueryConstructionException, SessionCommunicationException {
    System.out.println("addListener");
    final TestQueryListener testQueryListener = new TestQueryListener();
    {
      q = constructXIsaBirdQuery();
      q.setMaxTime(1);
      q.addListener(testQueryListener).start();
      final long startMillis = System.currentTimeMillis();
      while (testQueryListener.terminated == false
              && System.currentTimeMillis() - startMillis < 1000) {
        Thread.sleep(10);
      }
      assertTrue(testQueryListener.terminated);
    }
    {//Try with performInference()
      q.close();
      q = constructXIsaBirdQuery();
      q.setMaxTime(1);
      q.addListener(testQueryListener).performInference();
      final long startMillis = System.currentTimeMillis();
      while (testQueryListener.terminated == false
              && System.currentTimeMillis() - startMillis < 1000) {
        Thread.sleep(10);
      }
      assertTrue(testQueryListener.terminated);
    }
  }

  /**
   * Test of removeQueryVariable and addQueryVariable methods, of class Query.
   * @throws com.cyc.query.exception.QueryConstructionException
   * @throws com.cyc.kb.exception.KbException
   */
  @Test
  public void testAddAndRemoveQueryVariable() throws QueryConstructionException, KbException {
    System.out.println("removeQueryVariable");
    q = constructXIsaBirdQuery();
    assertTrue(q.getQueryVariables().contains(X));
    q.removeQueryVariable(X);
    assertFalse(q.getQueryVariables().contains(X));
    q.addQueryVariable(X);
    assertTrue(q.getQueryVariables().contains(X));
  }

  /**
   * Test of setQueryVariables method, of class Query.
   * @throws com.cyc.query.exception.QueryConstructionException
   * @throws com.cyc.kb.exception.KbException
   */
  @Test
  public void testSetQueryVariables() throws QueryConstructionException, KbException {
    System.out.println("setQueryVariables");
    q = constructXIsaBirdQuery();
    assertTrue(q + " does not contain " + X, q.getQueryVariables().contains(X));
    final List<Variable> vars = new ArrayList<Variable>();
    q.setQueryVariables(vars);
    assertFalse(q.getQueryVariables().contains(X));
    vars.add(X);
    q.setQueryVariables(vars);
    assertTrue(q.getQueryVariables().contains(X));
  }

  /**
   * Test of setQuerySentenceMainClause method, of class Query.
   * @throws com.cyc.query.exception.QueryConstructionException
   * @throws com.cyc.kb.exception.KbException
   */
  @Test
  public void testSetQuerySentenceMainClause() throws QueryConstructionException, KbException {
    System.out.println("setQuerySentenceMainClause");
    q = constructXIsaBirdQuery().setQuerySentenceMainClause(xIsaEmu);
    assertEquals(xIsaEmu, q.getQuerySentenceMainClause());
  }

  /**
   * Test of setQuerySentenceHypothesizedClause method, of class Query.
   * @throws com.cyc.query.exception.QueryConstructionException
   * @throws com.cyc.kb.exception.KbException
   */
  @Test
  public void testSetQuerySentenceHypothesizedClause() throws QueryConstructionException, KbException {
    System.out.println("setQuerySentenceHypothesizedClause");
    q = constructXIsaBirdQuery().
            setQuerySentenceHypothesizedClause(xIsaEmu);
    assertEquals(xIsaEmu, q.getQuerySentenceHypothesizedClause());
    q.setQuerySentenceHypothesizedClause(xIsaBird());
    assertEquals(xIsaBird(), q.getQuerySentenceHypothesizedClause()
    );
  }

  /**
   * Test of findRedundantClauses method, of class Query.
   *
   * @throws com.cyc.query.exception.QueryConstructionException
   * @throws com.cyc.kb.exception.KbException
   * @throws com.cyc.session.exception.SessionCommunicationException
   */
  @Test
  public void testFindRedundantClauses() throws QueryConstructionException, KbException, SessionCommunicationException, OpenCycUnsupportedFeatureException {
    assumeNotOpenCyc();
    q = QueryFactory.getQuery(SentenceImpl.and(xIsaBird(), xIsaEmu), Constants.inferencePSCMt());
    final Collection<Collection<Sentence>> redundantClauses = q.findRedundantClauses();
    assertFalse(redundantClauses.isEmpty());
    final Collection<Sentence> oneSet = redundantClauses.iterator().next();
    assertTrue(oneSet.contains(xIsaBird()) && oneSet.contains(xIsaEmu));
  }

  /**
   * Test of findUnconnectedClauses method, of class QueryImpl.
   *
   * @throws com.cyc.query.exception.QueryConstructionException
   * @throws com.cyc.kb.exception.KbException
   * @throws com.cyc.session.exception.SessionCommunicationException
   */
  @Test
  public void testFindUnconnectedClauses() throws QueryConstructionException, KbException, SessionCommunicationException, OpenCycUnsupportedFeatureException {
    System.out.println("findUnconnectedClauses");
    assumeNotOpenCyc();
    q = QueryFactory.getQuery(SentenceImpl.and(xIsaBird(), xIsaEmu), Constants.inferencePSCMt());
    final Collection<ArgPosition> unconnectedClauses = q.findUnconnectedClauses();
    assertTrue(unconnectedClauses.isEmpty());
    FormulaSentence newSentence = ((QueryImpl)q).getQuerySentenceMainClauseCyc().deepCopy();
    newSentence.setSpecifiedObject(ArgPositionImpl.ARG1.deepCopy().extend(
            ArgPositionImpl.ARG1),
            CycObjectFactory.makeCycVariable("Y"));
    Sentence s = new SentenceImpl(newSentence);
    q.setQuerySentenceMainClause(s);
    unconnectedClauses.addAll(q.findUnconnectedClauses());
    assertFalse(unconnectedClauses.isEmpty());
  }

  /**
   * Test of merge method, of class QueryImpl.
   *
   * @throws com.cyc.kb.exception.KbException
   * @throws com.cyc.session.exception.SessionCommunicationException
   * @throws com.cyc.query.exception.QueryConstructionException
   */
  @Test
  public void testMerge() throws KbException, QueryConstructionException, SessionCommunicationException, OpenCycUnsupportedFeatureException {
    System.out.println("merge");
    assumeNotOpenCyc();
    q = QueryFactory.getQuery(xIsaBird(), Constants.inferencePSCMt(), defaultParams);
    Query otherQuery = QueryFactory.getQuery(xIsaEmu, Constants.inferencePSCMt(), defaultParams);
    q = q.merge(otherQuery);
    assertTrue(((CycFormulaSentence)q.getQuerySentence().getCore()).treeContains(QueryTestConstants.bird().getCore()));
  }

  /**
   * Test of setMaxTime method, of class QueryImpl.
   * @throws com.cyc.query.exception.QueryConstructionException
   */
  @Test
  public void testSetMaxTime() throws QueryConstructionException {
    System.out.println("setMaxTime");
    q = constructXIsaBirdQuery();
    q.setMaxTime(12);
    assertTrue(12 == q.getMaxTime());
  }

  /**
   * Test of setMaxNumber method, of class Query.
   * @throws com.cyc.query.exception.QueryConstructionException
   */
  @Test
  public void testSetMaxNumber() throws QueryConstructionException {
    System.out.println("setMaxNumber");
    q = constructXIsaBirdQuery();
    q.setMaxAnswerCount(12);
    assertTrue(12 == q.getMaxAnswerCount());
  }

  /**
   * Test of setInferenceMode method, of class Query.
   * @throws com.cyc.query.exception.QueryConstructionException
   */
  @Test
  public void testSetInferenceMode() throws QueryConstructionException {
    System.out.println("setInferenceMode");
    q = constructXIsaBirdQuery();
    q.setInferenceMode(OpenCycInferenceMode.MINIMAL_MODE); //@#$@%$@#$ OpenCycInferenceMode @##$&#%^@#$%
    assertEquals(OpenCycInferenceMode.MINIMAL_MODE, q.getInferenceMode());
  }

  private static void waitForQueryToFinish() throws InterruptedException {
    Thread.sleep(1000);
  }
  
  private static class TestQueryListener implements QueryListener {
    // TODO: use some of these fields for verifying test results.
    private boolean created = false;
    private boolean terminated = false;
    private final List<InferenceStatus> statuses = new ArrayList<InferenceStatus>();
    private final List answers = new ArrayList();

    @Override
    public void notifyInferenceCreated(Query query) {
      System.out.println("Inference created.");
      created = true;
    }

    @Override
    public void notifyInferenceStatusChanged(InferenceStatus oldStatus,
            InferenceStatus newStatus,
            InferenceSuspendReason suspendReason,
            Query query) {
      System.out.println(
              "Inference status changed from " + oldStatus + " to " + newStatus);
      statuses.add(oldStatus);
      statuses.add(newStatus);
    }

    @Override
    public void notifyInferenceAnswersAvailable(Query query, List<QueryAnswer> newAnswers) {
      System.out.println("New answers: " + newAnswers);
      answers.addAll(newAnswers);
    }

    @Override
    public void notifyInferenceTerminated(Query query, Exception e) {
      System.out.println("Inference terminated.");
      terminated = true;
    }
  }
  
  /**
   * Test of bindVariable method, of class Query.
   * @throws java.io.IOException
   * @throws com.cyc.query.exception.QueryConstructionException
   * @throws com.cyc.kb.exception.KbException
   */
  @Test
  public void testBindVariable_String_Object() throws IOException, QueryConstructionException, KbException {
    q = constructXIsaBirdQuery();
    assertTrue(q + " does not contain " + X, q.getQueryVariables().contains(X));
    q.bindVariable("?X", QueryTestConstants.bird());
    assertFalse(q + " contains " + X, q.getQueryVariables().contains(X));
    q.setQuerySentenceMainClause(xIsaBird());
    assertTrue(q + " does not contain " + X, q.getQueryVariables().contains(X));
    q.bindVariable("X", QueryTestConstants.bird());
  }

  /**
   * Test of bindVariable method, of class Query.
   * @throws java.io.IOException
   * @throws com.cyc.query.exception.QueryConstructionException
   * @throws com.cyc.kb.exception.KbException
   */
  @Test
  public void testBindVariable_CycVariable_Object() throws IOException, QueryConstructionException, KbException {
    q = constructXIsaBirdQuery();
    assertTrue(q + " does not contain " + X, q.getQueryVariables().contains(X));
    q.bindVariable(X, QueryTestConstants.bird());
    assertFalse(q + " contains " + X, q.getQueryVariables().contains(X));
  }

  /**
   * Test of metrics accessors, of class Query.
   *
   * @throws java.lang.InterruptedException
   * @throws com.cyc.query.exception.QueryConstructionException
   * @throws com.cyc.session.exception.SessionCommunicationException
   * @throws com.cyc.base.exception.CycConnectionException
   * @throws CycTimeOutException
   */
  @Test
  public void testMetrics() throws InterruptedException, QueryConstructionException, SessionCommunicationException, CycConnectionException {
    System.out.println("testMetrics");
    q = constructXIsaBirdQuery();
    // Gather up all the metrics we have java constants for:
    final List<? extends InferenceMetric> metricsList = Arrays.asList(
            StandardInferenceMetric.ANSWER_COUNT, StandardInferenceMetric.HYPOTHESIZATION_TIME, StandardInferenceMetric.LINK_COUNT,
            StandardInferenceMetric.PROBLEM_COUNT, StandardInferenceMetric.PROBLEM_STORE_PROBLEM_COUNT,
            StandardInferenceMetric.PROBLEM_STORE_PROOF_COUNT, StandardInferenceMetric.PROOF_COUNT,
            StandardInferenceMetric.TACTIC_COUNT, StandardInferenceMetric.TIME_PER_ANSWER, StandardInferenceMetric.TIME_TO_FIRST_ANSWER,
            StandardInferenceMetric.TIME_TO_LAST_ANSWER, StandardInferenceMetric.TOTAL_TIME, StandardInferenceMetric.WASTED_TIME_AFTER_LAST_ANSWER);
    // Add them all to our query:
    q.getMetrics().addAll(metricsList);
    if (!TestUtils.getCyc().isOpenCyc()) {
      q.getMetrics().add(StandardInferenceMetric.SKSI_QUERY_START_TIMES);
      q.getMetrics().add(StandardInferenceMetric.SKSI_QUERY_TOTAL_TIME);
    }
    q.start();
    waitForQueryToFinish();
    final InferenceMetricsValues metricsValues =  q.getMetricsValues();
    for (final InferenceMetric metric : metricsList) {
      final Object value = metricsValues.getValue(metric);
      System.out.println(metric + ": " + value);
      assertNotNull("Got null for " + metric, value);
    }
  }

}
