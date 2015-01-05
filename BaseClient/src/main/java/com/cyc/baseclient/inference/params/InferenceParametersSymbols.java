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
import com.cyc.base.cycobject.CycSymbol;
import com.cyc.baseclient.CycObjectFactory;

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
 * @version $Id: InferenceParametersSymbols.java 155483 2014-12-10 21:56:51Z nwinant $
 */
public class InferenceParametersSymbols  {

    public final static CycSymbol MAX_NUMBER = CycObjectFactory.makeCycSymbol(":MAX-NUMBER");
    public static final CycSymbol METRICS = CycObjectFactory.makeCycSymbol(":METRICS");
    public static final CycSymbol ALLOW_INDETERMINATE_RESULTS = CycObjectFactory.makeCycSymbol(":allow-indeterminate-results?");
    public static final CycSymbol ANSWER_LANGUAGE = CycObjectFactory.makeCycSymbol(":ANSWER-LANGUAGE");
    public static final CycSymbol CONDITIONAL_SENTENCE = CycObjectFactory.makeCycSymbol(":CONDITIONAL-SENTENCE?");
    public static final CycSymbol CONTINUABLE = CycObjectFactory.makeCycSymbol(":continuable?");
    public static final CycSymbol RESULT_UNIQUENESS = CycObjectFactory.makeCycSymbol(":result-uniqueness");
    public static final CycSymbol TRANSITIVE_CLOSURE_MODE = CycObjectFactory.makeCycSymbol(":transitive-closure-mode");
    public static final CycSymbol DISJUNCTION_FREE_EL_VARS_POLICY = CycObjectFactory.makeCycSymbol(":disjunction-free-el-vars-policy");
    public static final CycSymbol EQUALITY_REASONING_DOMAIN = CycObjectFactory.makeCycSymbol(":equality-reasoning-domain");
    public static final CycSymbol BROWSABLE = CycObjectFactory.makeCycSymbol(":browsable?");
    public static final CycSymbol INTERMEDIATE_STEP_VALIDATION_LEVEL = CycObjectFactory.makeCycSymbol(":INTERMEDIATE-STEP-VALIDATION-LEVEL");
    public static final CycSymbol INFERENCE_MODE = CycObjectFactory.makeCycSymbol(":inference-mode");

    public static final CycSymbol MAX_TIME = CycObjectFactory.makeCycSymbol(":MAX-TIME");

    public static final CycSymbol ABDUCTION_ALLOWED = CycObjectFactory.makeCycSymbol(":ABDUCTION-ALLOWED?");
    public static final CycSymbol MAX_TRANSFORMATION_DEPTH = CycObjectFactory.makeCycSymbol(":MAX-TRANSFORMATION-DEPTH");

    public static final CycSymbol LOAD_PROBLEM_STORE = CycObjectFactory.makeCycSymbol("LOAD-PROBLEM-STORE");
    public static final CycSymbol FIND_PROBLEM_STORE_BY_ID = CycObjectFactory.makeCycSymbol("FIND-PROBLEM-STORE-BY-ID");
    public static final CycSymbol PROBLEM_STORE = CycObjectFactory.makeCycSymbol(":PROBLEM-STORE");
}
