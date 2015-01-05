package com.cyc.base.inference;

/*
 * #%L
 * File: InferenceParameterGetter.java
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

import com.cyc.base.inference.metrics.InferenceMetrics;

/**
 * An interface defining the getters of Cyc inference parameters. Implementing
 * classes allow the various parameters that govern how Cyc performs inference
 * to be retrieved.
 *
 * @author baxter
 */
public interface InferenceParameterGetter {

  boolean getAbductionAllowed();

  /**
   * Get the flavor of CycL to use for answers, or null if unspecified.
   *
   * @return the <code>InferenceAnswerLanguage</code>
   *
   */
  InferenceAnswerLanguage getAnswerLanguage();

  /**
   * Get the criteria for what to do when disjuncts have difference free
   * variables.
   *
   * @return the <code>DisjunctionFreeELVarsPolicy</code>, or null if
   * unspecified.
   *
   */
  DisjunctionFreeELVarsPolicy getDisjunctionFreeELVarsPolicy();

  /**
   * Get the inference mode to use. Inference modes are meant to be intuitive
   * measures of how hard Cyc should work to answer a query. Setting the
   * inference mode sets various other low-level parameters to appropriate
   * values for that mode, but explictly setting values for such parameters
   * overrides values set by the mode.
   *
   * @return the <code>InferenceMode</code>
   */
  InferenceMode getInferenceMode();

  /**
   * Get the number cutoff. If this parameter is set to some number Cyc is
   * licensed to stop work on an inference once it has found at least that many
   * answers.
   *
   * @return the max number, or null if unlimited.
   */
  Integer getMaxNumber();

  Integer getMaxTime();

  Integer getMaxTransformationDepth();

  /**
   * Get the set of inference metrics to gather.
   *
   * @return the inference metrics.
   */
  InferenceMetrics getMetrics();

  /**
   * Get the criteria for when to try to reuse problems, or null if not
   * specified.
   *
   * @return the <code>ProblemReusePolicy</code>, or null if unspecified.
   *
   */
  ProblemReusePolicy getProblemReusePolicy();

  /**
   * Get the criteria for how carefully to check the intermediate proofs that
   * are used in inference for semantic well-formedness, or null if unspecified.
   *
   * @return the <code>ResultUniqueness</code>
   *
   */
  ProofValidationMode getProofValidationMode();

  /**
   * Get the criteria for determining when two answers are the same, or null if
   * unspecified.
   *
   * @return the <code>ResultUniqueness</code>
   *
   */
  ResultUniqueness getResultUniqueness();

  /**
   * Get the criteria for determining when two answers are the same, or null if
   * unspecified.
   *
   * @return the <code>TransitiveClosureMode</code>
   *
   */
  TransitiveClosureMode getTransitiveClosureMode();

  Boolean isBrowsable();

  Boolean isContinuable();

}
