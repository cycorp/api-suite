package com.cyc.query;

/*
 * #%L
 * File: KBContentTestTester.java
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
import java.io.IOException;
import java.util.List;

import com.cyc.base.CycApiException;
import com.cyc.base.CycConnectionException;
import com.cyc.base.CycTimeOutException;
import com.cyc.base.inference.InferenceStatus;
import com.cyc.base.inference.InferenceSuspendReason;
import com.cyc.baseclient.export.PrintStreamExporter;
import com.cyc.baseclient.inference.DefaultInferenceSuspendReason;
import com.cyc.kb.Fact;
import com.cyc.kb.KBIndividual;
import com.cyc.kb.KBObject;
import com.cyc.kb.Variable;
import com.cyc.kb.client.Constants;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KBTypeException;
import com.cyc.km.query.answer.justification.ProofViewJustification;
import com.cyc.xml.query.ProofViewMarshaller;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Test one KB Content Test.
 *
 * @author baxter
 */
public class KBContentTestTester extends QueryListener {

  private final QueryApiTestConstants testConstants;
  private final KBIndividual test;
  private Query query;
  long startTimeMillis;

  public KBContentTestTester(KBIndividual test) throws Exception {
    testConstants = QueryApiTestConstants.getInstance();
    this.test = test;
  }

  protected int getAnswerCount() throws CycConnectionException {
    return query.getAnswerCount();
  }

  public void test() throws Exception {
    System.out.println("\nRunning " + test);
    final Fact qSpecFact = test.getFacts(testConstants.testQuerySpecification,
            1, Constants.inferencePSCMt()).iterator().next();
    query = Query.load(qSpecFact.<KBIndividual>getArgument(2));
    try {
      query.registerRequiredSKSModules();
      query.addListener(this);
      query.setMaxTime(maxTime);
      query.performInference();
      if (DefaultInferenceSuspendReason.MAX_TIME.equals(query.getSuspendReason())) {
        System.out.println("Query timed out after (at least) " + maxTime + " seconds.");
      } else {
        verifyDesiredMinAnswerCountReturned();
        verifyDesiredAnswerCountReturned();
        verifyDesiredBindingsReturned();
        verifyExactBindingsReturned();
      }
      final PrintStreamExporter<ProofViewJustification> exporter = new PrintStreamExporter<ProofViewJustification>() {
        final ProofViewMarshaller marshaller = new ProofViewMarshaller();

        @Override
        protected void doExport() throws Exception {
          marshaller.marshal(object.getProofView(), getPrintStream());
        }
      };
      for (final QueryAnswer answer : query.getAnswers()) {
        System.out.println("Checking justification for " + answer);
        final ProofViewJustification justification = new ProofViewJustification(
                answer);
        final String xml = exporter.exportToString(justification);
        assertFalse("Justification for " + answer + " is empty!", xml.isEmpty());
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      System.out.println("Closing " + query);
      query.close();
    }
  }
  private final int maxTime = 10;

  private void verifyDesiredAnswerCountReturned() throws Exception {
    for (final Fact fact : test.getFacts(testConstants.testAnswersCardinalityExact, 1, Constants.inferencePSCMt())) {
      final int desiredAnswerCount = fact.<Integer>getArgument(2);
      System.out.println(
              "Verifying that " + test + " has exactly " + desiredAnswerCount + " answer(s).");
      assertEquals("Wrong number of answers for " + test, desiredAnswerCount,
              getAnswerCount());
    }
  }

  private void verifyDesiredMinAnswerCountReturned() throws Exception {
    if (!DefaultInferenceSuspendReason.MAX_TIME.equals(query.getSuspendReason())) {
      for (final Fact fact : test.getFacts(testConstants.testAnswersCardinalityMin, 1, Constants.inferencePSCMt())) {
        final int desiredAnswerCount = fact.<Integer>getArgument(2);
        System.out.println(
                "Verifying that " + test + " has at least " + desiredAnswerCount + " answer(s).");
        assertTrue("Not enough answers for " + test,
                getAnswerCount() >= desiredAnswerCount);
      }
    }
  }

  private void verifyExactBindingsReturned() throws Exception {
    for (final Fact fact : test.getFacts(testConstants.testAnswersCycLExact, 1, Constants.inferencePSCMt())) {
      final Set<Set> desiredBindingSets = fact.getArgument(2);
      assertEquals("Wrong number of answers for " + test,
              desiredBindingSets.size(), getAnswerCount());
      for (final Set desiredBindings : desiredBindingSets) {
        assertTrue(test + " failed to find " + desiredBindings,
                queryHasBindings(desiredBindings));
      }
    }
  }

  private void verifyDesiredBindingsReturned() throws Exception {
    for (final Fact fact : test.getFacts(testConstants.testAnswersCycLWanted, 1, Constants.inferencePSCMt())) {
      final Set desiredBindings = fact.getArgument(2);
      assertTrue(test + " failed to find " + desiredBindings, queryHasBindings(desiredBindings));
    }
  }

  protected boolean queryHasBindings(final Set desiredBindings) throws
          IOException, CycApiException, CycTimeOutException, CycConnectionException {
    for (final QueryAnswer answer : query.getAnswers()) {
      if (answerHasBindings(answer, desiredBindings)) {
        return true;
      }
    }
    return false;
  }

  private boolean answerHasBindings(QueryAnswer answer, Set<KBObject> desiredBindings)
          throws IOException, CycConnectionException {
    System.out.println("Checking " + answer + "\n for desired bindings.");
    for (final KBObject binding : desiredBindings) {
      try {
        final Variable var = binding.<Variable>getArgument(1);
        final Object desiredValue = binding.getArgument(2);
        System.out.println("Checking for " + var + " -> " + desiredValue);
        final Object answerValue = answer.getBinding(var);
        if (desiredValue.equals(answerValue)) {
          System.out.println("Found it.");
          continue;
        } else {
          return false;
        }
      } catch (KBTypeException ex) {
      } catch (CreateException ex) {
      }
    }
    return true;
  }

  @Override
  public void notifyInferenceCreated(Query query) {
    startTimeMillis = System.currentTimeMillis();
  }

  @Override
  public void notifyInferenceStatusChanged(InferenceStatus oldStatus,
          InferenceStatus newStatus, InferenceSuspendReason suspendReason,
          Query query) {
  }

  @Override
  public void notifyInferenceAnswersAvailable(Query query,
          List<QueryAnswer> newAnswers) {
    final long elapsedTime = System.currentTimeMillis() - startTimeMillis;
    System.out.println(
            "Found " + newAnswers.size() + " answer(s) after " + elapsedTime + "ms.");
  }

  @Override
  public void notifyInferenceTerminated(Query query,
          Exception e) {
    System.out.println(
            "Inference terminated after " + (System.currentTimeMillis() - startTimeMillis) + "ms");
  }
}
