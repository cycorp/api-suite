/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cyc.query;

/*
 * #%L
 * File: BindingsBackedQueryAnswer.java
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
import com.cyc.kb.KBTerm;
import com.cyc.kb.Variable;
import com.cyc.kb.client.KBObjectImpl;
import com.cyc.kb.exception.CreateException;
import com.cyc.query.exception.QueryApiException;
import com.cyc.query.exception.QueryApiRuntimeException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author baxter
 */
class BindingsBackedQueryAnswer implements QueryAnswer {

  private final Map<Variable, Object> bindings;
  private final InferenceAnswerIdentifier id;

  public BindingsBackedQueryAnswer(Map<Variable, Object> bindings) {
    this(bindings, null);
  }

  public BindingsBackedQueryAnswer(Map<Variable, Object> bindings,
          InferenceAnswerIdentifier id) {
    this.bindings = bindings;
    this.id = id;
  }

  @Override
  public <T> T getBinding(Variable var) {
    try {
      return KBObjectImpl.<T>checkAndCastObject(bindings.get(var));
    } catch (CreateException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public Map<Variable, Object> getBindings() {
    return Collections.unmodifiableMap(bindings);
  }

  @Override
  public InferenceAnswerIdentifier getId() {
    if (id == null) {
      throw new UnsupportedOperationException();
    } else {
      return id;
    }
  }

  @Override
  public String toString() {
    if (id == null) {
      return "Query Answer with " + bindings.size() + " Binding"
              + ((bindings.size() == 1) ? "" : "s");
    } else {
      return id.toString();
    }
  }

  @Override
  public Set<KBTerm> getSources() {
    if (getId() == null) {
      throw new QueryApiRuntimeException("Unable to get sources for BindingsBackedQueryAnswer without an inference answer id");
    }
    return new InferenceAnswerBackedQueryAnswer(getId()).getSources();
  }
}
