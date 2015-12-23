package com.cyc.query;

/*
 * #%L
 * File: QueryAnswer.java
 * Project: Core API Object Specification
 * %%
 * Copyright (C) 2013 - 2015 Cycorp, Inc
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
import com.cyc.kb.KbObject;
import com.cyc.kb.KbTerm;
import com.cyc.kb.Variable;

import java.util.Map;
import java.util.Set;

/**
 * Encodes one answer to a {@link Query}.
 * <p/>
 * Unlike {@link InferenceAnswer}, this class is designed to use KB API classes, so variables are
 * {@link Variable}s, and bindings are returned as {@link KbObject}s where possible.
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
   * @return A mapping from variables to the values to which they are bound in this answer.
   */
  Map<Variable, Object> getBindings();

  /**
   * Get the identifier for this answer. This can be used to unambiguously identify this answer to
   * the Cyc that produced it, so long as the inference has not been destroyed.
   *
   * @return the identifier.
   */
  InferenceAnswerIdentifier getId();

  /**
   * Get the attributed sources used in inferring this answer. Generally, results will include SKSI
   * sources, or terms used in meta-assertions using sourceOfTerm-NonTrivial or its specializations.
   * Note that this method works by introspecting on the inference object, and will not work if the
   * inference has already been destroyed when this is called. The canonical way of ensuring that
   * the inference is not destroyed immediately is to call 
   * {@link com.cyc.query.Query#retainInference()}, though there are other ways of ensuring the 
   * inference is not immediately destroyed, such as 
   * {@link com.cyc.query.Query#setBrowsable(boolean)} and 
   * {@link com.cyc.query.Query#setContinuable(boolean)}.
   *
   * @return A set of KbTerms that are the attributed sources for this answer.
   */
  Set<KbTerm> getSources();

}
