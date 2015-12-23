/* $Id: Class.java 138466 2012-02-07 13:39:51Z tbrussea $
 *
 * Copyright (c) 2012 Cycorp, Inc.  All rights reserved.
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 */
package com.cyc.baseclient.api;

/*
 * #%L
 * File: SingleThreadedQueryStressTest.java
 * Project: Base Client
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

//// Internal Imports
import com.cyc.baseclient.testing.TestUtils;
import com.cyc.base.CycAccess;
import com.cyc.baseclient.cycobject.CycFormulaSentence;
import com.cyc.baseclient.cycobject.CycArrayList;
import com.cyc.baseclient.CycObjectFactory;
import com.cyc.baseclient.CommonConstants;
import com.cyc.base.exception.CycConnectionException;
import com.cyc.base.cycobject.CycConstant;
import com.cyc.base.cycobject.FormulaSentence;
import com.cyc.base.cycobject.CycList;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.CycVariable;
import com.cyc.query.InferenceParameters;
import java.util.*;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import static com.cyc.baseclient.testing.TestUtils.*;
import com.cyc.baseclient.inference.params.DefaultInferenceParameters;

/**
 * <P>StressTest is designed to...
 *
 * <P>Copyright (c) 2012 Cycorp, Inc. All rights reserved.
 <BR>This software is the proprietary information of Cycorp, Inc.
 <P>Use is subject to license terms.

 Created on : Jun 26, 2012, 11:40:47 AM
 Author : tbrussea
 *
 * @version $Id: Class.java 138466 2012-02-07 13:39:51Z tbrussea $
 */
public class SingleThreadedQueryStressTest {

  //// Constructors
  /**
   * Creates a new instance of StressTest.
   */
  public SingleThreadedQueryStressTest() {
  }

  //// Public Area
  @BeforeClass
  public static void initializeCyc() throws CycConnectionException {
    CycAccess cyc = TestUtils.getCyc();

    // Create PREDICATE
    testPredicate = cyc.getAssertTool().findOrCreateBinaryPredicate("testPredicate", null,
            "Test predicate.", CommonConstants.THING, CommonConstants.THING, null, null,
            null);

    // Create constant
    testConstant = cyc.getAssertTool().findOrCreateNewPermanent("TestConstant");
    cyc.getAssertTool().assertIsa(testConstant, CommonConstants.THING);

    // Create mt
    testMt = cyc.getAssertTool().createMicrotheory("TestMt", "Test microtheory",
            cyc.getLookupTool().getKnownConstantByName("Microtheory"), new ArrayList());

    // Assert key assertion
    cyc.getAssertTool().assertGaf(testMt, testPredicate, testConstant, testString);

    // Create parameters
    DefaultInferenceParameters params = new DefaultInferenceParameters(cyc);
    params.setAllowIndeterminateResults(false);
    params.setContinuable(false);
    params.setMaxAnswerCount(10);
    // @TODO -- Reinstate these once they work on an OpenCyc image:
    //params.setMaxNumber(null);
    //params.setMaxTime(600);
    params.setMaxTransformationDepth(0);

    if (!cyc.isOpenCyc()) {
      params.setMaxTime(600);
      params.getAnswersInHL();
      params.setIntermediateStepValidationLevel(CycObjectFactory.makeCycSymbol(
              ":none"));
      params.setBrowsable(false);
      params.put(":add-restriction-layer-of-indirection?", CycObjectFactory.nil);
      params.put(":allow-abnormality-checking?", CycObjectFactory.nil);
      params.put(":compute-answer-justifications?", CycObjectFactory.nil);
      params.put(":max-problem-count", CycObjectFactory.makeCycSymbol(":positive-infinity"));
      params.put(":new-terms-allowed?", CycObjectFactory.nil);
      params.put(":productivity-limit", CycObjectFactory.makeCycSymbol(":positive-infinity"));
      params.put(":transformation-allowed?", CycObjectFactory.nil);
    }
    parameters = params;
  }

  @AfterClass
  public static void closeCyc() throws CycConnectionException {
    CycAccess cyc = getCyc();
    if (cyc != null) {
      cyc.close();
    }
  }

  @Test
  public void testIndexedByConstant() throws CycConnectionException {
    runQueryRepeatedly(CycFormulaSentence.makeCycFormulaSentence(testPredicate,
            testConstant, var), testMt, CycArrayList.makeCycList(testString));
  }

  @Test
  public void testIndexedByString() throws CycConnectionException {
    runQueryRepeatedly(CycFormulaSentence.makeCycFormulaSentence(testPredicate,
            var, testString), testMt, CycArrayList.makeCycList(testConstant));
  }

  public void runQueryRepeatedly(FormulaSentence query, CycObject mt,
          Collection<?> expectedAnswers) throws CycConnectionException {
    Set<Object> expectedAnswerSet = new HashSet<Object>(expectedAnswers);
    List<Integer> failures = new ArrayList<Integer>();
    boolean abort = false;
    for (int i = 0; (i < TEST_LENGTH) && (abort == false); i++) {
      if ((i % 100) == 0) {
        System.out.println("");
        System.out.print(i);
        System.out.flush();
      }
      CycList<?> results = TestUtils.getCyc().getInferenceTool().queryVariable(var, query, mt, parameters);
      if (results.size() != expectedAnswerSet.size()) {
        noteFailure(failures, i,
                "Expected " + expectedAnswerSet.size() + " answers, got " + results.size());
        System.out.print("!");
        System.out.flush();
      } else {
        Set<Object> resultSet = new HashSet<Object>(results);
        if (!resultSet.equals(expectedAnswerSet)) {
          noteFailure(failures, i,
                  "Expected " + expectedAnswerSet + ", got " + results);
          System.out.print("!");
          System.out.flush();
        } else {
          System.out.print(".");
          System.out.flush();
        }
      }
      if (STOP_ON_FAILURE && !failures.isEmpty()) {
        abort = true;
      }
    }
    System.out.println();
    System.out.flush();
    if (!failures.isEmpty()) {
      Assert.fail("The following failed: " + failures);
    }
  }

  private void noteFailure(List<Integer> failures, int i, String msg) {
    failures.add(i);
    if (STOP_ON_FAILURE) {
      Assert.fail(msg);
    }
  }
  //// Protected Area
  //// Private Area
  //// Internal Rep
  public static final int TEST_LENGTH = 100;
  public static final boolean STOP_ON_FAILURE = true;
  public static CycConstant testPredicate;
  public static CycConstant testConstant;
  public static CycConstant testMt;
  public static String testString = "Test string";
  public static CycVariable var = CycObjectFactory.makeCycVariable("X");
  public static InferenceParameters parameters;
}
