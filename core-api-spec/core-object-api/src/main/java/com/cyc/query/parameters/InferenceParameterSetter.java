package com.cyc.query.parameters;

import com.cyc.query.metrics.InferenceMetrics;

/*
 * #%L
 * File: InferenceParameterSetter.java
 * Project: Core API Object Specification
 * %%
 * Copyright (C) 2013 - 2017 Cycorp, Inc
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
 * An interface defining the setters of Cyc inference parameters. Implementing
 * classes allow the various parameters that govern how Cyc performs inference
 * to be specified.
 * <p>
 * By convention, the setter methods return the object itself, to allow method
 * calls to be chained.
 *
 * @author baxter
 * @param <T> this implementation of InferenceParameterSetter
 */
public interface InferenceParameterSetter<T extends InferenceParameterSetter> {

  /**
   * Set the flavor of CycL to use for answers.
   *
   * @param language
   * @return this object.
   */
  T setAnswerLanguage(InferenceAnswerLanguage language);

  /**
   * Specify whether inferences should be browsable. Browsable inferences can be
   * inspected using the Cyc Browser, and may require more resources during
   * inference.
   *
   * @param browsable
   * @return this object
   */
  T setBrowsable(boolean browsable);

  /**
   * Specify whether inferences should be continuable. Continuable inferences
   * are used e.g. when a maximum number of answers has been specified. The
   * inference will be suspended after an initial batch of answers has been
   * found, and continuing it may then yield the next batch.
   *
   * @see #setMaxAnswerCount(java.lang.Integer)
   *
   * @param continuable
   * @return this object
   */
  T setContinuable(boolean continuable);

  /**
   * Set the criteria for what to do when disjuncts have difference free
   * variables.
   *
   * @param policy
   * @return this object.
   */
  T setDisjunctionFreeElVarsPolicy(DisjunctionFreeElVarsPolicy policy);

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
  T setInferenceMode(InferenceMode mode);
  
  /**
   * Set the maximum number of answers (or sets of answers) that Cyc will
   * attempt to find for the Query. In some cases (such as when a set of
   * answers is retrieved in a batch), more answers than this may actually be
   * returned. Once this number of answers has been reached, Cyc will not
   * actively look for additional answers.
   *
   * <p>
   * A value of <code>null</code> means the inference will continue until it
   * exhausts or some other limit is reached.
   * 
   * @param maxAnswers number of answers
   * @return this object.
   */
  T setMaxAnswerCount(Integer maxAnswers);

  /**
   * Set the max time value (in seconds). Setting this parameter to some number
   * licenses Cyc to stop work on an inference once it has been working on it
   * for at least that many seconds.
   * <p>
   * A value of <code>null</code> means the inference will continue until it
   * exhausts or some other limit is reached.
   *
   * @param maxSeconds timeout value in seconds
   * @return this object.
   */
  T setMaxTime(Integer maxSeconds);

  /**
   * Set the max transformation depth value. Setting this parameter to some
   * number prevents Cyc from reasoning using chains of rules longer than that
   * number.
   *
   * @param depth
   * @return this object.
   */
  T setMaxTransformationDepth(Integer depth);

  /**
   * Specify the set of inference metrics to gather.
   * @see StandardInferenceMetric 
   * @see InferenceMetricsHashSet
   *
   * @param metrics
   * @return this object
   */
  T setMetrics(InferenceMetrics metrics);

  /**
   * Set the criteria for when to try to reuse problems.
   *
   * @param policy
   * @return this object.
   */
  T setProblemReusePolicy(ProblemReusePolicy policy);

  /**
   * Set the criteria for how carefully to check the intermediate proofs that
   * are used in inference for semantic well-formedness.
   *
   * @param mode
   * @return this object.
   */
  T setProofValidationMode(ProofValidationMode mode);

  /**
   * Set the criteria for determining when two answers are the same.
   *
   * @param mode
   * @return this object.
   */
  T setResultUniqueness(ResultUniqueness mode);

  /**
   * Set the extent to which transitive closures are computed.
   *
   * @param mode
   * @return this object.
   */
  T setTransitiveClosureMode(TransitiveClosureMode mode);

}
