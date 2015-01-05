package com.cyc.baseclient.util.query;

/*
 * #%L
 * File: QueryResult.java
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
import java.util.List;

//// Internal Imports
import com.cyc.baseclient.cycobject.CycVariableImpl;

/**
 *
 * @author baxter
 */
public interface QueryResult extends Comparable<QueryResult> {

  List<Binding> getBindings();

  Integer getAnswerID();

  Object getBindingForVar(CycVariableImpl var);

  public static interface Binding {

    String getVariableName();

    CycVariableImpl getVariable();

    void setVariable(CycVariableImpl variable);

    Object getValue();
  }
}
