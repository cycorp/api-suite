package com.cyc.baseclient.inference;

/*
 * #%L
 * File: CycBackedInferenceAnswerTest.java
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
import com.cyc.base.cycobject.CycConstant;
import com.cyc.base.cycobject.ELMt;
import com.cyc.base.cycobject.InformationSource;
import com.cyc.base.inference.InferenceAnswer;
import com.cyc.base.inference.InferenceAnswerIdentifier;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.cyc.baseclient.CommonConstants;
import com.cyc.baseclient.CycClient;
import com.cyc.baseclient.CycClientManager;
import com.cyc.baseclient.cycobject.CycArrayList;
import com.cyc.baseclient.cycobject.ELMtCycNaut;

import static com.cyc.baseclient.testing.TestUtils.getCyc;

import com.cyc.baseclient.cycobject.InformationSourceImpl;
import static com.cyc.baseclient.testing.TestConstants.*;
import static com.cyc.baseclient.testing.TestSentences.*;

/**
 *
 * @author baxter
 */
public class CycBackedInferenceAnswerTest extends InferenceAnswerTest {

  public CycBackedInferenceAnswerTest() {
  }

  /**
   * Test of getId method, of class CycBackedInferenceAnswer.
   */
  @Test
  public void testGetId() throws CycConnectionException {
    System.out.println("getId");
    InferenceAnswer instance = getFirstInferenceAnswer(WHAT_IS_ONE_PLUS_ONE_STRING, CommonConstants.BASE_KB);
    InferenceAnswerIdentifier identifier = instance.getId();
    assertNotNull(identifier);
    assertEquals(0, identifier.getAnswerID());
  }

  /**
   * Test of getSources method, of class CycBackedInferenceAnswer.
   */
  @Test
  public void testGetSources() throws Exception {
    System.out.println("getSources");
    // TODO -- Find or create a sourced query available in OpenCyc KB.
    if (!getCyc().isOpenCyc()) {
      final CycConstant WHH_WP = getCyc().getLookupTool().find("#$TestFactEntrySource-WikipediaArticle-WilliamHenryHarrison");
      // (#$ContextOfPCWFn #$TestFactEntrySource-WikipediaArticle-WilliamHenryHarrison)
      final ELMt WHH_WP_MT = ELMtCycNaut.makeELMtCycNaut(
          CycArrayList.makeCycList(CONTEXT_OF_PCW_FN, WHH_WP));
      
      InferenceAnswer inferenceAnswer = getFirstInferenceAnswer(ISA_WILLIAM_HENRY_HARRISON_US_PRESIDENT_STRING, WHH_WP_MT);
      final Collection<InformationSource> sources = 
              inferenceAnswer.getSources(
                      new InformationSourceImpl.CycCitationGenerator(
                              CHICAGO_MANUAL_OF_STYLE_STANDARD));
      assertFalse(sources.isEmpty());
      for (final InformationSource source : sources) {
        assertNotNull("No citation string for " + source.getCycL(), source.getCitationString());
        if (CycClientManager.getClientManager().fromCycAccess(getCyc()).isFullKB()) {
          // TODO: should we add an assertion for the icon?
          assertNotNull("No icon for " + source.getCycL(), source.getIconURL());
        }
      }
    }
  }

  @Override
  protected InferenceAnswer constructFirstInferenceAnswer(DefaultInferenceWorker worker) {
    return new CycBackedInferenceAnswer(new SpecifiedInferenceAnswerIdentifier(
           worker.getInferenceIdentifier(), 0));
  }
}
