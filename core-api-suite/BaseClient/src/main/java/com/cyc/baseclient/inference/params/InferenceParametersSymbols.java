package com.cyc.baseclient.inference.params;

/*
 * #%L
 * File: InferenceParametersSymbols.java
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

/**
 * <P>InferenceParameters is designed to encode the parameters that can be
 * specified for running a Cyc query.
 *
 * <P>Copyright (c) 2004 - 2006 Cycorp, Inc. All rights reserved.
 * <BR>This software is the proprietary information of Cycorp, Inc.
 * <P>Use is subject to license terms.
 *
 * @author zelal
 * @date August 14, 2005, 2:41 PM
 * @version $Id: InferenceParametersSymbols.java 158569 2015-05-19 21:51:08Z daves $
 */
public class InferenceParametersSymbols  {

    public final static String MAX_NUMBER = ":MAX-NUMBER";
    public static final String METRICS = ":METRICS";
    public static final String ALLOW_INDETERMINATE_RESULTS = ":allow-indeterminate-results?";
    public static final String ANSWER_LANGUAGE = ":ANSWER-LANGUAGE";
    public static final String CONDITIONAL_SENTENCE = ":CONDITIONAL-SENTENCE?";
    public static final String CONTINUABLE = ":continuable?";
    public static final String RESULT_UNIQUENESS = ":result-uniqueness";
    public static final String TRANSITIVE_CLOSURE_MODE = ":transitive-closure-mode";
    public static final String DISJUNCTION_FREE_EL_VARS_POLICY = ":disjunction-free-el-vars-policy";
    public static final String EQUALITY_REASONING_DOMAIN = ":equality-reasoning-domain";
    public static final String BROWSABLE = ":browsable?";
    public static final String INTERMEDIATE_STEP_VALIDATION_LEVEL = ":INTERMEDIATE-STEP-VALIDATION-LEVEL";
    public static final String INFERENCE_MODE = ":inference-mode";

    public static final String MAX_TIME = ":MAX-TIME";

    public static final String ABDUCTION_ALLOWED = ":ABDUCTION-ALLOWED?";
    public static final String MAX_TRANSFORMATION_DEPTH = ":MAX-TRANSFORMATION-DEPTH";

    public static final String LOAD_PROBLEM_STORE = "LOAD-PROBLEM-STORE";
    public static final String FIND_PROBLEM_STORE_BY_ID = "FIND-PROBLEM-STORE-BY-ID";
    public static final String PROBLEM_STORE = ":PROBLEM-STORE";
}
