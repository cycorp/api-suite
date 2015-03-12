/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cyc.query;

/*
 * #%L
 * File: InferenceAnswerBackedQueryAnswer.java
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
import com.cyc.base.CycConnectionException;
import com.cyc.base.cycobject.CycVariable;
import com.cyc.base.inference.InferenceAnswer;
import com.cyc.base.inference.InferenceAnswerIdentifier;
import com.cyc.baseclient.inference.CycBackedInferenceAnswer;
import com.cyc.kb.Variable;
import com.cyc.kb.client.KBObjectImpl;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KBTypeException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author baxter
 */
class InferenceAnswerBackedQueryAnswer implements QueryAnswer {

  private final InferenceAnswer answerCyc;

  InferenceAnswerBackedQueryAnswer(InferenceAnswerIdentifier id) {
    this(new CycBackedInferenceAnswer(id));
  }

  InferenceAnswerBackedQueryAnswer(InferenceAnswer answerCyc) {
    if (answerCyc == null) {
      throw new IllegalArgumentException();
    }
    this.answerCyc = answerCyc;
  }

  @Override
  public InferenceAnswerIdentifier getId() {
    return answerCyc.getId();
  }

  @Deprecated
  public InferenceAnswer getAnswerCyc() {
    return answerCyc;
  }

  @Override
  public <T> T getBinding(Variable var) {
    try {
      return KBObjectImpl.<T>checkAndCastObject(answerCyc.getBinding((CycVariable) var.getCore()));
    } catch (CreateException ex) {
      throw new IllegalStateException(ex);
    } catch (CycConnectionException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public Map<Variable, Object> getBindings() {
    try {
      final Map<CycVariable, Object> cycBindings = answerCyc.getBindings();
      return new Map<Variable, Object>() {

        @Override
        public int size() {
          return cycBindings.size();
        }

        @Override
        public boolean isEmpty() {
          return cycBindings.isEmpty();
        }

        @Override
        public boolean containsKey(Object key) {
          return key instanceof Variable && cycBindings.containsKey(((Variable) key).getCore());
        }

        @Override
        public boolean containsValue(Object value) {
          return cycBindings.containsValue(value);
        }

        @Override
        public Object get(Object key) {
          if (key instanceof Variable) {
            return cycBindings.get(((Variable) key).getCore());
          } else {
            return null;
          }
        }

        @Override
        public Object put(Variable key, Object value) {
          return cycBindings.put((CycVariable) key.getCore(), value);
        }

        @Override
        public Object remove(Object key) {
          throw new UnsupportedOperationException();
        }

        @Override
        public void putAll(Map<? extends Variable, ? extends Object> m) {
          throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
          throw new UnsupportedOperationException();
        }

        @Override
        public Set<Variable> keySet() {
          throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Collection<Object> values() {
          return Collections.unmodifiableCollection(cycBindings.values());
        }

        @Override
        public Set<Map.Entry<Variable, Object>> entrySet() {
          throw new UnsupportedOperationException("Not supported yet.");
        }

      };
    } catch (CycConnectionException ex) {
      throw new IllegalStateException(ex);
    }

  }

  @Override
  public String toString() {
    return answerCyc.toString();
  }

}
