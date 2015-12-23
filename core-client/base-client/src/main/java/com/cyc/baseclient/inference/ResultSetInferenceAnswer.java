package com.cyc.baseclient.inference;

/*
 * #%L
 * File: ResultSetInferenceAnswer.java
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

import com.cyc.base.exception.CycConnectionException;
import com.cyc.base.cycobject.CycVariable;
import com.cyc.base.cycobject.InformationSource;
import com.cyc.base.inference.InferenceAnswer;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.cyc.baseclient.CycObjectFactory;
import com.cyc.base.cycobject.InformationSource.CitationGenerator;

/**
 * An InferenceAnswer backed by an DefaultResultSet.
 *
 * @author baxter
 */
public class ResultSetInferenceAnswer implements InferenceAnswer {

  private final DefaultResultSet resultSet;
  private final int answerId;
  private Bindings bindings = null;

  /**
   * Create a new inference answer for the answer currently at the cursor in the
   * specified result set.
   *
   * @param resultSet
   */
  public ResultSetInferenceAnswer(DefaultResultSet resultSet) {
    this(resultSet, resultSet.getRow() - 1);
  }

  /**
   * Create a new inference answer for the nth answer in the specified result
   * set.
   *
   * @param resultSet
   * @param answerId the ID of the answer. The first answer's ID is 0.
   */
  public ResultSetInferenceAnswer(DefaultResultSet resultSet, int answerId) {
    this.resultSet = resultSet;
    this.answerId = answerId;
  }

  @Override
  public Object getBinding(CycVariable var) throws CycConnectionException {
    resultSet.absolute(answerId + 1);
    return resultSet.getObject(var);
  }

  @Override
  public synchronized Map<CycVariable, Object> getBindings() throws CycConnectionException {
    if (bindings == null) {
      bindings = new Bindings();
      for (final String varName : resultSet.getColumnNames()) {
        final CycVariable var = CycObjectFactory.makeCycVariable(varName);
        bindings.put(var, getBinding(var));
      }
    }
    return Collections.unmodifiableMap(bindings);
  }

  @Override
  public SpecifiedInferenceAnswerIdentifier getId() {
    return null;
  }

  @Override
  public Collection<InformationSource> getSources(CitationGenerator citationGenerator) throws CycConnectionException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public int getAnswerID() {
    return answerId;
  }
}
