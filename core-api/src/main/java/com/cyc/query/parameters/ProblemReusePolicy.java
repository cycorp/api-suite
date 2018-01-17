package com.cyc.query.parameters;

/*
 * #%L
 * File: ProblemReusePolicy.java
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

/**
 * Which problems are considered for reuse by the Worker (aka Problem Store).
 * Problems deemed "equal" are reused, but since equality reasoning can be
 * non-trivial, under some policies not all problems are checked.
 *
 * @author baxter
 */
public enum ProblemReusePolicy implements InferenceParameterValue {

  /**
   * The worker tries to do equality reasoning on all problems.
   */
  ALL,
  /**
   * The worker tries to do equality reasoning only on single-literal problems.
   */
  SINGLE_LITERAL,
  /**
   * The worker doesn't try to do equality reasoning on any kind of problem.
   */
  NONE;

}
