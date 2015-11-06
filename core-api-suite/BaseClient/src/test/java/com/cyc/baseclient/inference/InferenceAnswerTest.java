package com.cyc.baseclient.inference;

/*
 * #%L
 * File: InferenceAnswerTest.java
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

import com.cyc.base.CycConnectionException;

import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import static com.cyc.baseclient.testing.TestUtils.getCyc;

import com.cyc.base.CycApiException;
import com.cyc.baseclient.CycObjectFactory;
import com.cyc.base.cycobject.ELMt;
import com.cyc.base.inference.InferenceAnswer;
import com.cyc.base.CycTimeOutException;
import com.cyc.baseclient.CommonConstants;
import static com.cyc.baseclient.testing.TestSentences.*;


/**
 *
 * @author baxter
 */
public abstract class InferenceAnswerTest {


  @BeforeClass
  public static void setUpClass() throws Exception {
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
    if (worker != null) {
      try {
        worker.cancel();
      } catch (CycConnectionException ex) {
        ex.printStackTrace();
      }
    }
  }

  /**
   * Test of getAnswerID method, of class CycBackedInferenceAnswer.
   */
  @Test
  public void testGetAnswerID() throws CycConnectionException {
    System.out.println("getAnswerID");
    InferenceAnswer instance = getFirstInferenceAnswer(WHAT_IS_ONE_PLUS_ONE_STRING, CommonConstants.BASE_KB);
    int identifier = instance.getAnswerID();
    assertNotNull(identifier);
    assertEquals(0, identifier);
  }

  /**
   * Test of getBinding method, of class CycBackedInferenceAnswer.
   */
  @Test
  public void testGetBinding() throws Exception {
    System.out.println("getBinding");
    InferenceAnswer inferenceAnswer = getFirstInferenceAnswer(WHAT_IS_ONE_PLUS_ONE_STRING, CommonConstants.BASE_KB);
    assertEquals(2, inferenceAnswer.getBinding(CycObjectFactory.makeCycVariable("X")));
  }

  /**
   * Test of getBindings method, of class CycBackedInferenceAnswer.
   */
  @Test
  public void testGetBindings() throws Exception {
    System.out.println("getBindings");
    InferenceAnswer instance = getFirstInferenceAnswer(WHAT_IS_ONE_PLUS_ONE_STRING, CommonConstants.BASE_KB);
    Map bindings = instance.getBindings();
    assertEquals(1, bindings.size());
    assertEquals(2, bindings.get(CycObjectFactory.makeCycVariable("X")));
  }
  private DefaultInferenceWorker worker = null;

  protected InferenceAnswer getFirstInferenceAnswer(
          final String querySentence,
          final ELMt mt) throws CycConnectionException, CycTimeOutException, CycApiException {
    worker = new DefaultInferenceWorkerSynch(
            getCyc().getObjectTool().makeCycSentence(querySentence), mt, null, getCyc(), 10000);
    ((DefaultInferenceWorkerSynch) worker).performSynchronousInference();
    DefaultInferenceSuspendReason suspendReason = worker.getSuspendReason();
    assertFalse("Got error running query " + querySentence + ": "
            + suspendReason, suspendReason.isError());
    assertTrue(worker.getAnswersCount() > 0);
    final InferenceAnswer inferenceAnswer =
            constructFirstInferenceAnswer(worker);
    return inferenceAnswer;
  }

  protected abstract InferenceAnswer constructFirstInferenceAnswer(DefaultInferenceWorker worker);
}
