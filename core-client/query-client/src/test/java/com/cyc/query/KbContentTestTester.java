package com.cyc.query;

/*
 * #%L
 * File: KbContentTestTester.java
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
import java.io.IOException;
import java.util.List;

import com.cyc.baseclient.export.PrintStreamExporter;
import com.cyc.baseclient.inference.DefaultInferenceSuspendReason;
import com.cyc.kb.Fact;
import com.cyc.kb.KbIndividual;
import com.cyc.kb.KbObject;
import com.cyc.kb.Variable;
import com.cyc.kb.client.Constants;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbException;
import com.cyc.kb.exception.KbTypeException;
import com.cyc.km.query.answer.justification.ProofViewJustification;
import static com.cyc.query.TestUtils.assumeCycSessionRequirements;
import com.cyc.query.exception.QueryConstructionException;
import com.cyc.session.exception.SessionCommunicationException;
import com.cyc.session.exception.OpenCycUnsupportedFeatureException;
import com.cyc.session.exception.UnsupportedCycOperationException;
import com.cyc.xml.query.ProofViewMarshaller;
import java.util.Set;
import javax.xml.bind.JAXBException;

import static org.junit.Assert.*;

/**
 * Test one KB Content Test.
 *
 * @author baxter
 */
public class KbContentTestTester implements QueryListener {

  private final QueryTestConstants testConstants;
  private final KbIndividual test;
  private Query query;
  long startTimeMillis;

  public KbContentTestTester(KbIndividual test) {
    testConstants = QueryTestConstants.getInstance();
    this.test = test;
  }

  protected int getAnswerCount() {
    return query.getAnswerCount();
  }

  public void test() throws QueryConstructionException, KbException, UnsupportedCycOperationException {
    System.out.println("\nRunning " + test);
    assumeCycSessionRequirements(QueryImpl.QUERY_LOADER_REQUIREMENTS);
    Exception exception = null;
    final Fact qSpecFact = test.getFacts(testConstants.testQuerySpecification,
            1, Constants.inferencePSCMt()).iterator().next();
    query = QueryFactory.getQuery(qSpecFact.<KbIndividual>getArgument(2));
    try {
      query.registerRequiredSksModules();
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
        protected void doExport() throws JAXBException, IOException, OpenCycUnsupportedFeatureException {
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
      exception = ex;
      ex.printStackTrace();
    } finally {
      System.out.println("Closing " + query);
      query.close();
      if (exception != null) {
        throw new RuntimeException(exception);
      }
    }
  }
  private final int maxTime = 10;

  private void verifyDesiredAnswerCountReturned() throws KbTypeException, CreateException {
    for (final Fact fact : test.getFacts(testConstants.testAnswersCardinalityExact, 1, Constants.inferencePSCMt())) {
      final int desiredAnswerCount = fact.<Integer>getArgument(2);
      System.out.println(
              "Verifying that " + test + " has exactly " + desiredAnswerCount + " answer(s).");
      assertEquals("Wrong number of answers for " + test, desiredAnswerCount,
              getAnswerCount());
    }
  }

  private void verifyDesiredMinAnswerCountReturned() throws KbTypeException, CreateException {
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

  private void verifyExactBindingsReturned() throws KbTypeException, CreateException, IOException, SessionCommunicationException {
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

  private void verifyDesiredBindingsReturned() throws KbTypeException, CreateException, IOException, SessionCommunicationException {
    for (final Fact fact : test.getFacts(testConstants.testAnswersCycLWanted, 1, Constants.inferencePSCMt())) {
      final Set desiredBindings = fact.getArgument(2);
      assertTrue(test + " failed to find " + desiredBindings, queryHasBindings(desiredBindings));
    }
  }

  protected boolean queryHasBindings(final Set desiredBindings) throws
          IOException, SessionCommunicationException {
    for (final QueryAnswer answer : query.getAnswers()) {
      if (answerHasBindings(answer, desiredBindings)) {
        return true;
      }
    }
    return false;
  }

  private boolean answerHasBindings(QueryAnswer answer, Set<KbObject> desiredBindings)
          throws IOException {
    System.out.println("Checking " + answer + "\n for desired bindings.");
    for (final KbObject binding : desiredBindings) {
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
      } catch (KbTypeException ex) {
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
  public void notifyInferenceTerminated(Query query, Exception e) {
    System.out.println(
            "Inference terminated after " + (System.currentTimeMillis() - startTimeMillis) + "ms");
  }
}
