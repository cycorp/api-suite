/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cyc.baseclient.inference.params;

/*
 * #%L
 * File: SpecifiedInferenceParametersTest.java
 * Project: Base Client
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

import com.cyc.base.CycConnectionException;
import com.cyc.base.inference.DisjunctionFreeELVarsPolicy;
import com.cyc.base.inference.InferenceAnswerLanguage;
import com.cyc.base.inference.InferenceParameters;
import com.cyc.base.inference.ProblemReusePolicy;
import com.cyc.base.inference.ProofValidationMode;
import com.cyc.base.inference.ResultUniqueness;
import com.cyc.base.inference.TransitiveClosureMode;
import com.cyc.baseclient.testing.TestUtils;
import com.cyc.session.SessionApiException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author baxter
 */
public class SpecifiedInferenceParametersTest {

  public SpecifiedInferenceParametersTest() {
  }

  private InferenceParameters paramValues;

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() throws CycConnectionException, SessionApiException {
    TestUtils.ensureTestEnvironmentInitialized();
    paramValues = DefaultInferenceParameterDescriptions.
            getDefaultInferenceParameterDescriptions(TestUtils.getCyc()).getDefaultInferenceParameters();
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testClone() {
  }

  @Test
  public void testToDefaultInferenceParameters() {
  }

  @Test
  public void testClear() {
  }

  @Test
  public void testParameterValueCycListApiValue() {
  }

  @Test
  public void testEntrySet() {
  }

  @Test
  public void testKeySet() {
  }

  @Test
  public void test() {
  }

  @Test
  public void testPutAll() {
  }

  @Test
  public void testRemove() {
  }

  @Test
  public void testPut() {
  }

  @Test
  public void testContainsKey() {
  }

  @Test
  public void testMaxNumber() {
    paramValues.setMaxNumber(24);
    assertEquals(Integer.valueOf(24), paramValues.getMaxNumber());
  }

  @Test
  public void testMaxTime() {
    paramValues.setMaxTime(24);
    assertEquals(Integer.valueOf(24), paramValues.getMaxTime());
  }

  @Test
  public void testMetrics() {
  }

  @Test
  public void testSetMetrics() {
  }

  @Test
  public void testSetInferenceMode() {
  }

  @Test
  public void testInferenceMode() {
  }

  @Test
  public void testStringApiValue() {
  }

  @Test
  public void testCycListApiValue() {
  }

  @Test
  public void testCycAccess() {
  }

  @Test
  public void testUpdateFromPlist() {
  }

  @Test
  public void testAbductionAllowed() {
  }

  @Test
  public void testMaxTransformationDepth() {
    paramValues.setMaxTransformationDepth(24);
    assertEquals(Integer.valueOf(24), paramValues.getMaxTransformationDepth());
  }

  @Test
  public void testSetProblemStorePath() {
  }

  @Test
  public void testSetProblemStoreId() {
  }

  @Test
  public void testUsesLoadedProblemStore() {
  }

  @Test
  public void testMakeAtLeastAsLooseAs() {
  }

  @Test
  public void testIsBrowsable() {
    paramValues.setBrowsable(true);
    assertEquals(true, paramValues.isBrowsable());
  }

  @Test
  public void testEquals() {
  }

  @Test
  public void testHashCode() {
  }

  @Test
  public void testToString() {
  }

  @Test
  public void testIsContinuable() {
    paramValues.setContinuable(true);
    assertEquals(true, paramValues.isContinuable());
  }

  @Test
  public void testResultUniqueness() {
    paramValues.setResultUniqueness(ResultUniqueness.PROOF);
    assertEquals(ResultUniqueness.PROOF, paramValues.getResultUniqueness());
  }

  @Test
  public void testAnswerLanguage() {
    paramValues.setAnswerLanguage(InferenceAnswerLanguage.EL);
    assertEquals(InferenceAnswerLanguage.EL, paramValues.getAnswerLanguage());
  }

  @Test
  public void testTransitiveClosureMode() {
    paramValues.setTransitiveClosureMode(TransitiveClosureMode.NONE);
    assertEquals(TransitiveClosureMode.NONE, paramValues.getTransitiveClosureMode());
  }

  @Test
  public void testProofValidationMode() {
    paramValues.setProofValidationMode(ProofValidationMode.ARG_TYPE);
    assertEquals(ProofValidationMode.ARG_TYPE, paramValues.getProofValidationMode());
  }

  @Test
  public void testDisjunctionFreeELVarsPolicy() {
    paramValues.setDisjunctionFreeELVarsPolicy(DisjunctionFreeELVarsPolicy.COMPUTE_INTERSECTION);
    assertEquals(DisjunctionFreeELVarsPolicy.COMPUTE_INTERSECTION, paramValues.getDisjunctionFreeELVarsPolicy());
  }

  @Test
  public void testProblemReusePolicy() {
    paramValues.setProblemReusePolicy(ProblemReusePolicy.ALL);
    assertEquals(ProblemReusePolicy.ALL, paramValues.getProblemReusePolicy());
  }

}
