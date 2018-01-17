package com.cyc.query.parameters;

/*
 * #%L
 * File: InferenceParameters.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2017 Cycorp, Inc
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

import java.util.List;

/**
 * <P>
 * InferenceParameters is designed to encode the parameters that can be
 * specified for running a Cyc query.
 *
 * @author zelal
 * @date August 14, 2005, 2:41 PM
 * @version $Id: InferenceParameters.java 175435 2017-10-20 23:37:33Z nwinant $
 */
public interface InferenceParameters 
        extends Cloneable, InferenceParameterGetter, InferenceParameterSetter<InferenceParameters> {

  
  // Inference parameter names
  
  /**
   * Inference parameter name for <strong>abduction allowed</strong>.
   */
  public static final String ABDUCTION_ALLOWED = ":ABDUCTION-ALLOWED?";
  
  /**
   * Inference parameter name for <strong>answer language</strong>.
   */
  public static final String ANSWER_LANGUAGE = ":ANSWER-LANGUAGE";
  
  /**
   * Inference parameter name for <strong>browsable</strong>.
   */
  public static final String BROWSABLE = ":BROWSABLE?";
  
  /**
   * Inference parameter name for <strong>continuable</strong>.
   */
  public static final String CONTINUABLE = ":CONTINUABLE?";
  
  /**
   * Inference parameter name for <strong>disjunction free EL vars policy</strong>.
   */
  public static final String DISJUNCTION_FREE_EL_VARS_POLICY = ":DISJUNCTION-FREE-EL-VARS-POLICY";
  
  /**
   * Inference parameter name for <strong>problem reuse policy</strong>.
   */
  public static final String EQUALITY_REASONING_DOMAIN = ":EQUALITY-REASONING-DOMAIN";
  
  /**
   * Inference parameter name for <strong>inference mode</strong>.
   */
  public static final String INFERENCE_MODE = ":INFERENCE-MODE";
  
  /**
   * Inference parameter name for <strong>proof validation mode</strong>.
   */
  public static final String INTERMEDIATE_STEP_VALIDATION_LEVEL = ":INTERMEDIATE-STEP-VALIDATION-LEVEL";
  
  /**
   * Inference parameter name for <strong>max answer count</strong>.
   */
  public static final String MAX_NUMBER = ":MAX-NUMBER";
  
  /**
   * Inference parameter name for <strong>max time</strong>.
   */
  public static final String MAX_TIME = ":MAX-TIME";
  
  /**
   * Inference parameter name for <strong>max transformation depth</strong>.
   */
  public static final String MAX_TRANSFORMATION_DEPTH = ":MAX-TRANSFORMATION-DEPTH";
  
  /**
   * Inference parameter name for <strong>metrics</strong>.
   */
  public static final String METRICS = ":METRICS";
  
  /**
   * Inference parameter name for <strong>problem store ID</strong>.
   */
  public static final String PROBLEM_STORE = ":PROBLEM-STORE";
  
  /**
   * Inference parameter name for <strong>result uniqueness</strong>.
   */
  public static final String RESULT_UNIQUENESS = ":RESULT-UNIQUENESS";
  
  /**
   * Inference parameter name for <strong>transitive closure mode</strong>.
   */
  public static final String TRANSITIVE_CLOSURE_MODE = ":TRANSITIVE-CLOSURE-MODE";
  
  
  // Methods

  Object parameterValueCycListApiValue(final String key, final Object val);
  
  String stringApiValue();

  Object cycListApiValue();
  
  /**
   * Update from a plist of the type used by Cyc's inference engine.
   *
   * @param plist
   */
  public void updateFromPlist(List plist);

  public void setProblemStorePath(String path);

  /**
   * Specify the problem store in which to run the query. *
   * @param id
   */
  public void setProblemStoreId(int id);

  public boolean usesLoadedProblemStore();

  /**
   * Adjust these parameters to give the inference engine at least as extensive
   * resources as newParams.
   *
   * @param newParams
   */
  public void makeAtLeastAsLooseAs(InferenceParameters newParams);
  
}
