package com.cyc.baseclient.util.query;

/*
 * #%L
 * File: DefaultQueryResult.java
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

//// External Imports
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//// Internal Imports
import com.cyc.baseclient.cycobject.CycVariableImpl;
import com.cyc.baseclient.inference.SpecifiedInferenceAnswerIdentifier;

/**
 *
 * @author baxter
 */
public class DefaultQueryResult implements QueryResult {

  public DefaultQueryResult(final List<? extends Binding> bindings, final SpecifiedInferenceAnswerIdentifier inferenceAnswerIdentifier) {
    this.answerID = (inferenceAnswerIdentifier == null) ? null : inferenceAnswerIdentifier.getAnswerId();
    if (bindings != null) {
      this.bindings.addAll(bindings);
    }
  }

  protected DefaultQueryResult() {
    this(null, null);
  }

  public List<Binding> getBindings() {
    return Collections.unmodifiableList(bindings);
  }

  public Integer getAnswerID() {
    return answerID;
  }

  public Object getBindingForVar(final CycVariableImpl var) {
    for (final Binding binding : getBindings()) {
      if (binding.getVariable().equals(var)) {
        return binding.getValue();
      }
    }
    return null;
  }

  public int compareTo(final QueryResult o) {
    if (o == null) {
      return -1;
    } else {
      return answerID.compareTo(o.getAnswerID());
    }
  }

  public static class DefaultBinding implements Binding {

    private final Object term;
    private CycVariableImpl variable;

    public DefaultBinding(final CycVariableImpl variable, final Object term) {
      this.variable = variable;
      this.term = term;
    }

    @Override
    public String toString() {
      return variable + " -> " + term;
    }

    @Override
    public String getVariableName() {
      return variable.getName();
    }

    @Override
    public CycVariableImpl getVariable() {
      return variable;
    }

    @Override
    public Object getValue() {
      return term;
    }

    @Override
    public void setVariable(CycVariableImpl variable) {
      this.variable = variable;
    }
  }
  private final List<Binding> bindings = new ArrayList<Binding>();
  private final Integer answerID;
}
