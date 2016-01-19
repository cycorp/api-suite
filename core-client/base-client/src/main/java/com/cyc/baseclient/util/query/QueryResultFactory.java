package com.cyc.baseclient.util.query;

/*
 * #%L
 * File: QueryResultFactory.java
 * Project: Base Client
 * %%
 * Copyright (C) 2013 - 2016 Cycorp, Inc.
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
import com.cyc.baseclient.cycobject.CycArrayList;
import com.cyc.baseclient.cycobject.CycVariableImpl;
import com.cyc.baseclient.util.query.DefaultQueryResult.DefaultBinding;
import com.cyc.baseclient.util.query.QueryResult.Binding;

/**
 *
 * @author baxter
 */
public class QueryResultFactory {

  public static Binding parseBinding(final CycArrayList rawBinding) {
    if (rawBinding.size() == 2) {
      final CycVariableImpl variable = (CycVariableImpl) rawBinding.get(0);
      final Object term = rawBinding.getDottedElement();
      return new DefaultBinding(variable, term);
    }
    return null;
  }

  public static QueryResult constructResult(List<Binding> bindings) {
    return new DefaultQueryResult(bindings, null);
  }
}
