package com.cyc.base.inference;

/*
 * #%L
 * File: InferenceAnswer.java
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

import com.cyc.query.InferenceAnswerIdentifier;
import com.cyc.base.exception.CycConnectionException;
import com.cyc.base.cycobject.CycVariable;

import java.util.Collection;
import java.util.Map;

import com.cyc.base.cycobject.InformationSource;

/**
 * An interface for representing one answer to a Cyc query.
 * @author baxter
 */
public interface InferenceAnswer {

  /**
   * Returns the value to which var is bound in this answer
   *
   * @param var a variable for which this answer provides a binding.
   * @return the value to which var is bound.
   */
  Object getBinding(CycVariable var) throws CycConnectionException;
  
  /**
   * Returns the set of bindings for this answer for all query variables.
   * @return the set of bindings.
   */
  Map<CycVariable, Object> getBindings() throws CycConnectionException;

  /**
   * Returns the identifier for this answer.
   *
   * @return the identifier for this answer.
   */
  InferenceAnswerIdentifier getId();

  /**
   * Return the sources to which this answer is attributed.
   *
   * @return the sources to which this answer is attributed.
   */
  Collection<InformationSource> getSources(InformationSource.CitationGenerator citationGenerator) throws CycConnectionException;

  /**
   * Returns the identifier for this answer within its inference.
   * The first answer has the identifier 0.
   * @return the identifier for this answer within its inference
   */
  public int getAnswerID();
  
}
