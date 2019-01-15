package com.cyc.query.parameters;

/*
 * #%L
 * File: Browsable.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2019 Cycorp, Inc
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
public enum Browsable implements InferenceParameterValue {

  /**
   * @deprecated.  Equivalent to NEVER.
   */
  NIL, /**
  /**
   * @deprecated.  Equivalent to ALWAYS.
   */
  T, /**
  /**
   * The inference will always be browsable.
   */
  ALWAYS, /**
   * If the inference engine chooses to create a browsable object, it will be browsable.  But it might choose to not create one,
   * and it will therefore not be browsable.  Use of this value via the Query API is not advised, as it may leave inference objects
   * on the Cyc server that are not cleaned up with the close() method is called on the inference.  @todo Ensure that
   * maybe-browsable inferences do get cleaned up when close() is called.
   */
  MAYBE, /**
   * This inference will not be browsable.
   */
  NEVER;

}
