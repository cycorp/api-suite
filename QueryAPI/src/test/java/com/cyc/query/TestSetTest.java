package com.cyc.query;

/*
 * #%L
 * File: TestSetTest.java
 * Project: Query API
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
import com.cyc.query.exception.QueryConstructionException;
import com.cyc.base.CycApiException;
import com.cyc.base.CycConnectionException;
import com.cyc.base.CycTimeOutException;
import com.cyc.kb.Context;
import com.cyc.kb.KBCollection;
import com.cyc.kb.KBIndividual;
import com.cyc.kb.client.Constants;
import com.cyc.kb.client.KBCollectionImpl;
import com.cyc.kb.config.KBAPIConfiguration;
import com.cyc.kb.exception.KBApiException;

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

  private static KBCollection testSet = null;
  private KBCollection multipleChoiceTest = null;
  private boolean mctChecked = false;
  private final Context inferencePSC;
  private static final int MAX_TESTS_TO_RUN = 4;

  @BeforeClass
  public static void setupClass() throws IOException, KBApiException, CycConnectionException {
    KBAPIConfiguration.setShouldTranscriptOperations(false);
    TestUtils.ensureConstantsInitialized();
  }

  public TestSetTest() throws Exception {
    inferencePSC = Constants.inferencePSCMt();
    try {
      testSet = KBCollectionImpl.get("QueryAPIKBQTest");
    } catch (KBApiException ex) {
      System.out.println("No QueryAPIKBQTest collection defined in this KB.");
    }
  }

  @Test
  public void testTestSet() throws Exception {
    if (testSet == null) {
      System.out.println("No tests to run.");
      return;
    }
    final Collection<KBIndividual> tests = testSet.<KBIndividual>getInstances(inferencePSC);
    if (tests.isEmpty()) {
      System.out.println("No tests to run.");
      return;
    }
    System.out.println("Testing instances of " + testSet);
    int runCount = 0;
    for (final KBIndividual test : tests) {
      if (runCount < MAX_TESTS_TO_RUN && !isMultipleChoice(test)) {
        new KBContentTestTester(test).test();
        runCount++;
      }
    }
  }

  private boolean isMultipleChoice(final KBIndividual test)
          throws CycConnectionException, QueryConstructionException {
    synchronized (this) {
      if (!mctChecked) {
        try {
          multipleChoiceTest = KBCollectionImpl.get("MultipleChoiceKBContentTest");
        } catch (KBApiException ex) {
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
