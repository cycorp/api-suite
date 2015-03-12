/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cyc.query;

/*
 * #%L
 * File: QueryAnswer.java
 * Project: Query API Implementation
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

import com.cyc.base.inference.InferenceAnswer;
import com.cyc.base.inference.InferenceAnswerIdentifier;
import com.cyc.kb.KBObject;
import com.cyc.kb.Variable;
import java.util.Map;

/**
 * Encodes one answer to a {@link Query}.
 * <p/>Unlike {@link InferenceAnswer}, this class is designed to use
 * KB API classes, so variables are {@link Variable}s, and bindings are
 * returned as {@link KBObject}s where possible.
 *
 * @author baxter
 */
public interface QueryAnswer {

  /**
   * Get the value to which the specified variable is bound.
   *
   * @param <T> The expected class of the value.
   * @param var The variable for which the binding is sought.
   * @return The value to which <code>var</code> is bound in this answer.
   */
  <T> T getBinding(Variable var);

  /**
   * Get the values to which the all variables are bound.
   *
   * @return A mapping from variables to the values to which they are bound in
   * this answer.
   */
  Map<Variable, Object> getBindings();

  /**
   * Get the identifier for this answer. This can be used to unambiguously
   * identify this answer to the Cyc that produced it, so long as the inference
   * has not been destroyed.
   *
   * @return the identifier.
   */
  InferenceAnswerIdentifier getId();

}
