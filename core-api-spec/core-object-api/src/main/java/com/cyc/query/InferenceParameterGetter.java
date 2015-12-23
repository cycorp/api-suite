package com.cyc.query;

import com.cyc.query.metrics.InferenceMetrics;

/*
 * #%L
 * File: InferenceParameterGetter.java
 * Project: Core API Object Specification
 * %%
 * Copyright (C) 2013 - 2015 Cycorp, Inc
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


/**
 * An interface defining the getters of Cyc inference parameters. Implementing
 * classes allow the various parameters that govern how Cyc performs inference
 * to be retrieved.
 *
 * @author baxter
 */
public interface InferenceParameterGetter {

  /**
   * Check whether the Query allows abduction.
   *
   * @return true if it does, false otherwise.
   */
  boolean getAbductionAllowed();

  /**
   * Get the flavor of CycL to use for answers, or null if unspecified.
   *
   * @return the <code>InferenceAnswerLanguage</code>
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
  DisjunctionFreeElVarsPolicy getDisjunctionFreeElVarsPolicy();

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
   * Returns the maximum number of answers (or sets of answers) that Cyc will
   * attempt to find for the Query. In some cases (such as when a set of
   * answers is retrieved in a batch), more answers than this may actually be
   * returned. Once this number of answers has been reached, Cyc will not
   * actively look for additional answers.
   *
   * @return the number cutoff for the Query.
   */
  Integer getMaxAnswerCount();

  /**
   * Returns the soft timeout for the Query in seconds.
   *
   * @return the soft timeout for the Query in seconds.
   */
  Integer getMaxTime();
  
  /**
   * Returns the max transformation depth value. Cyc will not reason using chains of rules longer
   * than this number.
   *
   * @return the max transformation depth.
   */
  Integer getMaxTransformationDepth();

  /**
   * Get the set of inference metrics to gather for the Query.
   *
   * @return the inference metrics.
   */
  InferenceMetrics getMetrics();

  /**
   * Get the criteria for when to try to reuse problems, or null if not
   * specified.
   *
   * @return the <code>ProblemReusePolicy</code>, or null if unspecified.
   */
  ProblemReusePolicy getProblemReusePolicy();

  /**
   * Get the criteria for how carefully to check the intermediate proofs that
   * are used in inference for semantic well-formedness, or null if unspecified.
   *
   * @return the <code>ResultUniqueness</code>
   */
  ProofValidationMode getProofValidationMode();

  /**
   * Get the criteria for determining when two answers are the same, or null if
   * unspecified.
   *
   * @return the <code>ResultUniqueness</code>
   */
  ResultUniqueness getResultUniqueness();

  /**
   * Get the criteria for determining when two answers are the same, or null if
   * unspecified.
   *
   * @return the <code>TransitiveClosureMode</code>
   */
  TransitiveClosureMode getTransitiveClosureMode();

  /**
   * Check whether the Query (once run) is browsable.
   *
   * @return true iff it is so specified.
   */
  Boolean isBrowsable();
  
  /**
   * Check whether the Query is continuable. Queries that have not yet been run
   * are considered continuable, as well as ones whose parameters have the
   * continuable flag set.
   *
   * @see DefaultInferenceParameters#setContinuable(boolean)
   * @see #continueQuery()
   *
   * @return true iff it can be continued.
   */
  Boolean isContinuable();

}
