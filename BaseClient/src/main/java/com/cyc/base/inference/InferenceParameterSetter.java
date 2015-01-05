package com.cyc.base.inference;

/*
 * #%L
 * File: InferenceParameterSetter.java
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
import com.cyc.baseclient.inference.metrics.InferenceMetricsHashSet;
import com.cyc.baseclient.inference.metrics.StandardInferenceMetric;

/**
 * An interface defining the setters of Cyc inference parameters. Implementing
 * classes allow the various parameters that govern how Cyc performs inference
 * to be specified.
 * <p>
 * By convention, the setter methods return the object itself, to allow method
 * calls to be chained.
 *
 * @author baxter
 */
public interface InferenceParameterSetter {

  /**
   * Set the flavor of CycL to use for answers.
   *
   * @param language
   * @return this object.
   */
  InferenceParameterSetter setAnswerLanguage(InferenceAnswerLanguage language);

  /**
   * Specify whether inferences should be browsable. Browsable inferences can be
   * inspected using the Cyc Browser, and may require more resources during
   * inference.
   *
   * @param b
   * @return this object
   */
  InferenceParameterSetter setBrowsable(boolean b);

  /**
   * Specify whether inferences should be continuable. Continuable inferences
   * are used e.g. when a maximum number of answers has been specified. The
   * inference will be suspended after an initial batch of answers has been
   * found, and continuing it may then yield the next batch.
   *
   * @see #setMaxNumber(java.lang.Integer)
   *
   * @param b
   * @return this object
   */
  InferenceParameterSetter setContinuable(boolean b);

  /**
   * Set the criteria for what to do when disjuncts have difference free
   * variables.
   *
   * @param policy
   * @return this object.
   */
  InferenceParameterSetter setDisjunctionFreeELVarsPolicy(DisjunctionFreeELVarsPolicy policy);

  /**
   * Set the inference mode. Inference modes are meant to be intuitive measures
   * of how hard Cyc should work to answer a query. Setting the inference mode
   * sets various other low-level parameters to appropriate values for that
   * mode, but explictly setting values for such parameters overrides values set
   * by the mode.
   *
   * @param mode
   * @return this object.
   */
  InferenceParameterSetter setInferenceMode(InferenceMode mode);

  /**
   * Set the max number value. Setting this parameter to some number licenses
   * Cyc to stop work on an inference once it has found at least that many
   * answers.
   * <p>
   * A value of <code>null</code> means the inference will continue until it
   * exhausts or some other limit is reached.
   *
   * @param max
   * @return this object.
   */
  InferenceParameterSetter setMaxNumber(Integer max);

  /**
   * Set the max time value (in seconds). Setting this parameter to some number
   * licenses Cyc to stop work on an inference once it has been working on it
   * for at least that many seconds.
   * <p>
   * A value of <code>null</code> means the inference will continue until it
   * exhausts or some other limit is reached.
   *
   * @param max
   * @return this object.
   */
  InferenceParameterSetter setMaxTime(Integer max);

  /**
   * Set the max transformation depth value. Setting this parameter to some
   * number prevents Cyc from reasoning using chains of rules longer than that
   * number.
   *
   * @param i
   * @return this object.
   */
  InferenceParameterSetter setMaxTransformationDepth(Integer i);

  /**
   * Specify the set of inference metrics to gather.
   * @see StandardInferenceMetric 
   * @see InferenceMetricsHashSet
   *
   * @param metrics
   * @return this object
   */
  InferenceParameterSetter setMetrics(InferenceMetrics metrics);

  /**
   * Set the criteria for when to try to reuse problems.
   *
   * @param policy
   * @return this object.
   */
  InferenceParameterSetter setProblemReusePolicy(ProblemReusePolicy policy);

  /**
   * Set the criteria for how carefully to check the intermediate proofs that
   * are used in inference for semantic well-formedness.
   *
   * @param mode
   * @return this object.
   */
  InferenceParameterSetter setProofValidationMode(ProofValidationMode mode);

  /**
   * Set the criteria for determining when two answers are the same.
   *
   * @param mode
   * @return this object.
   */
  InferenceParameterSetter setResultUniqueness(ResultUniqueness mode);

  /**
   * Set the extent to which transitive closures are computed.
   *
   * @param mode
   * @return this object.
   */
  InferenceParameterSetter setTransitiveClosureMode(TransitiveClosureMode mode);

}
