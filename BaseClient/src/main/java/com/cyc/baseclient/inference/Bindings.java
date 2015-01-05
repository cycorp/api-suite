package com.cyc.baseclient.inference;

/*
 * #%L
 * File: Bindings.java
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
import com.cyc.base.cycobject.Naut;
import com.cyc.base.cycobject.CycVariable;
import java.util.HashMap;
import com.cyc.base.CycApiException;
import com.cyc.baseclient.CycObjectFactory;
import com.cyc.baseclient.cycobject.CycArrayList;

/**
 * A map from variables to values. A Bindings object covering the query
 * variables in a query constitutes a common type of "answer" for that query.
 */
public class Bindings extends HashMap<CycVariable, Object> {

  public Bindings() {
    super();
  }

  /**
   * Create a Bindings object from a result set and row.
   *
   * @param resultSet
   * @param row -- The first set of bindings is in row 1.
   */
  public Bindings(DefaultResultSet resultSet, int row) {
    final int oldRow = resultSet.getRow();
    resultSet.absolute(row);
    populateFromResultSet(resultSet);
    resultSet.absolute(oldRow);
  }

  /**
   * Create a Bindings object from the current row of a result set.
   *
   * @param resultSet
   */
  public Bindings(DefaultResultSet resultSet) {
    populateFromResultSet(resultSet);
  }

  /**
   * Construct a Bindings object from its standard CycL representation. Example:
   * <pre>(TheSet
   *  (ELInferenceBindingFn ?X Gallium)
   *  (ELInferenceBindingFn ?Y 31))</pre>
   *
   * @param bindingsNaut
   */
  public Bindings(Naut bindingsNaut) {
    super();
    for (int argNum = 1; argNum <= bindingsNaut.getArity(); argNum++) {
      Naut binding = (Naut) bindingsNaut.getArg(argNum);
      final CycVariable var = (CycVariable) binding.getArg(1);
      final Object desiredValue = binding.getArg(2);
      put(var, desiredValue);
    }
  }

  /**
   * Populate this Bindings object from its standard SubL representation.
   * Example:
   * <pre>((?X . Gallium) (?Y . 31))</pre>
   *
   * @param cycList
   */
  void populateFromCycList(CycArrayList cycList) {
    clear();
    putAll(cycList.toMap());
  }

  private void populateFromResultSet(DefaultResultSet resultSet) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, CycApiException {
    for (final String varName : resultSet.getColumnNames()) {
      final CycVariable var = CycObjectFactory.makeCycVariable(varName);
      put(var, resultSet.getObject(var));
    }
  }
}
