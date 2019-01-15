package com.cyc.query.parameters;

/*
 * #%L
 * File: ResultUniqueness.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2018 Cycorp, Inc
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
 *
 * @author baxter
 */
public enum ResultUniqueness implements InferenceParameterValue {

  /**
   * Two answers are considered the same if they assign the same bindings to all
   * open variables.
   */
  BINDINGS, /**
   * Results can have the same answers, as long as they have distinct proofs.
   */
  PROOF;

}