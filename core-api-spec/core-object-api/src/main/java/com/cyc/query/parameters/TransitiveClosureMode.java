package com.cyc.query.parameters;

/*
 * #%L
 * File: TransitiveClosureMode.java
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
 * The degree to which modules (such as transitiveViaArg) should generate
 * transitive closures.
 *
 * @author baxter
 */
public enum TransitiveClosureMode implements InferenceParameterValue {

  /**
   * Full closures (very expensive). Transitivity modules will provide the
   * minimal answers and full transitive closures on the minimal answers.
   */
  ALL, /**
   * Focused closures (expensive). Transitivity modules will provide the minimal
   * answers and focused transitive closures on the minimal answers.
   */
  FOCUSED,
  /**
   * No closures. Transitivity modules will only provide the minimal answers
   * (and no transitive closures on the minimal answers).
   *
   */
  NONE;

}
