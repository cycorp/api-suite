package com.cyc.query;

/*
 * #%L
 * File: TestSetTest.java
 * Project: Query Client
 * %%
 * Copyright (C) 2013 - 2016 Cycorp, Inc.
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
import com.cyc.base.exception.CycConnectionException;
import com.cyc.kb.Context;
import com.cyc.kb.KbCollection;
import com.cyc.kb.KbIndividual;
import com.cyc.kb.client.Constants;
import com.cyc.kb.client.KbCollectionImpl;
import com.cyc.kb.client.config.KbConfiguration;
import com.cyc.kb.exception.KbException;
import com.cyc.session.exception.UnsupportedCycOperationException;

import java.io.IOException;
import java.util.Collection;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Runs the tests defined in {@link #testSet}.
 *
 * @author baxter
 */
public class TestSetTest {

  private static KbCollection testSet = null;
  private KbCollection multipleChoiceTest = null;
  private boolean mctChecked = false;
  private final Context inferencePSC;
  private static final int MAX_TESTS_TO_RUN = 4;

  @BeforeClass
  public static void setupClass() throws IOException, KbException, CycConnectionException {
    KbConfiguration.getOptions().setShouldTranscriptOperations(false);
    TestUtils.ensureConstantsInitialized();
  }

  public TestSetTest() {
    inferencePSC = Constants.inferencePSCMt();
    try {
      testSet = KbCollectionImpl.get("QueryAPIKBQTest");
    } catch (KbException ex) {
      System.out.println("No QueryAPIKBQTest collection defined in this KB.");
    }
  }

  @Test
  public void testTestSet() throws QueryConstructionException, KbException, UnsupportedCycOperationException, CycConnectionException {
    if (testSet == null) {
      System.out.println("No tests to run.");
      return;
    }
    final Collection<KbIndividual> tests = testSet.<KbIndividual>getInstances(inferencePSC);
    if (tests.isEmpty()) {
      System.out.println("No tests to run.");
      return;
    }
    System.out.println("Testing instances of " + testSet);
    int runCount = 0;
    for (final KbIndividual test : tests) {
      if (runCount < MAX_TESTS_TO_RUN && !isMultipleChoice(test)) {
        new KbContentTestTester(test).test();
        runCount++;
      }
    }
  }

  private boolean isMultipleChoice(final KbIndividual test)
          throws CycConnectionException, QueryConstructionException {
    synchronized (this) {
      if (!mctChecked) {
        try {
          multipleChoiceTest = KbCollectionImpl.get("MultipleChoiceKBContentTest");
        } catch (KbException ex) {
        } finally {
          mctChecked = true;
        }
      }
    }
    if (multipleChoiceTest == null) {
      return false;
    }
    return test.isInstanceOf(multipleChoiceTest, inferencePSC);
  }
}
