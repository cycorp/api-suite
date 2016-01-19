/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cyc.baseclient.inference.params;

/*
 * #%L
 * File: InferenceParameterOCycRCycComparisonTest.java
 * Project: Base Client
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

import com.cyc.base.exception.CycApiException;
import com.cyc.base.exception.CycConnectionException;
import com.cyc.base.exception.CycTimeOutException;
import com.cyc.session.exception.SessionException;
import com.cyc.baseclient.inference.params.InferenceParameterComparatron.ParamMap;
import com.cyc.session.CycServer;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author baxter
 */
public class InferenceParameterOCycRCycComparisonTest {

  public InferenceParameterOCycRCycComparisonTest() {
  }

  @BeforeClass
  public static void setUpClass() {
  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  /**
   * This test loads the inference parameters from both an OpenCyc server and a Research Cyc server
   * and compares them to make sure they're the same.
   * 
   * Because this test is unique in that it requires two independent Cyc servers, it is skipped by
   * default. To enable it, modify or override #isComparatronEnabled(), below.
   * 
   * @throws SessionException
   * @throws CycConnectionException
   * @throws CycTimeOutException
   * @throws CycApiException 
   */
  @Test
  public void testInferenceParameterComparatron() throws SessionException, CycConnectionException, CycTimeOutException, CycApiException {
    org.junit.Assume.assumeTrue(isComparatronEnabled());
    // TODO: find a better way of ensuring that an OpenCyc & ResearchCyc server are available.
    final InferenceParameterComparatron comparatron = 
            new InferenceParameterComparatron(getResearchCycServer(), getOpenCycServer(), 10000);
    
    comparatron.load();
    //comparatron.print();
    comparatron.compare();
    
    final ParamMap researchCycParams = comparatron.getResearchCycParams();
    final ParamMap openCycParams = comparatron.getOpenCycParams();
    assertTrue(comparatron.getErrors().isEmpty());
    assertFalse(comparatron.getSuccesses().isEmpty());
    assertFalse(researchCycParams.getParamIds().isEmpty());
    assertFalse(openCycParams.getParamIds().isEmpty());
    assertNotEquals(researchCycParams, openCycParams);
    assertEquals(
            researchCycParams.getParamIds().size(),
            openCycParams.getParamIds().size());
  }
  
  
  // Private
  
  private CycServer getResearchCycServer() {
    return CycServer.fromString("localhost:3620");
  }
  
  private CycServer getOpenCycServer() {
    return CycServer.fromString("localhost:3600");
  }
  
  private boolean isComparatronEnabled() {
    return false;
  }
}
