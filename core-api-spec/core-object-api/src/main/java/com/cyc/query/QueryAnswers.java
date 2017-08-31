/*
 * Copyright 2017 Cycorp, Inc.
 *
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
 */
package com.cyc.query;

/*
 * #%L
 * File: QueryAnswers.java
 * Project: Core API Object Specification
 * %%
 * Copyright (C) 2013 - 2017 Cycorp, Inc
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

import com.cyc.kb.Variable;
import java.io.PrintStream;
import java.util.List;

/**
 * A list of answers for a Query. A QueryAnswers collection represents a snapshot of the answers 
 * found by the query <em>at the time that {@link com.cyc.query.Query#getAnswers()} was called;</em>
 * for running queries, subsequent calls to {@link com.cyc.query.Query#getAnswers()} may return 
 * different instances of QueryAnswers (with different numbers of answers) as additional answers are
 * found.
 * 
 * @author nwinant
 * @param <E>
 */
public interface QueryAnswers<E extends QueryAnswer> extends List<E> {
  
  /**
   * Returns a list of all the values for one variable from all the answers to a query.
   * 
   * @param <T> The expected class of the value.
   * @param var The variable for which the binding is sought.
   * @return a list of all of values for one variable from all answers to a query.
   */
  <T> List<T> getBindingsForVariable(Variable var);
  
  /**
   * Returns a list of all the values from all the answers to a single-variable query. This method 
   * specifically expects the query to return bindings for <em>exactly one variable;</em> the
   * existence of more than variable will be interpreted as an error which will result in a 
   * {@link com.cyc.query.exception.QueryRuntimeException}.
   * 
   * @param <T> The expected class of the value.
   * @return a list of all the values from all the answers to a single-variable query.
   */
  <T> List<T> getBindingsForSingleVariableQuery();
  
  /**
   * Returns a single value for a single variable from the sole answer to a query. This method 
   * specifically expects the query to return <em>no more than one answer,</em> and will interpret
   * the existence of more than one answer as an error which will result in a 
   * {@link com.cyc.query.exception.QueryRuntimeException}. If no answers have been found, this 
   * method will return <code>null</code>.
   * 
   * <p>For a query which might reasonably return multiple answers, similar behavior can be 
   * accomplished with something like the following:
   * <code>
   * QueryAnswers answers = query.getAnswers();
   * T value = (!answers.isEmpty()) ? answers.get(0).<T>getBinding(var) : null;
   * </code>
   * 
   * @param <T> The expected class of the value.
   * @param var The variable for which the binding is sought.
   * @return a single value for a single variable from the sole answer to a query, or <code>null</code> if no answer has been found.
   */
  <T> T getBindingForSingleAnswerQuery(Variable var);
  
  /**
   * Returns a single value for the sole answer to a single-variable query. This method 
   * specifically expects the query to return bindings for <em>exactly one variable,</em> and for 
   * the query to return <em>no more than one answer;</em> the existence of more than variable or 
   * more than one answer will be interpreted as an error which will result in a 
   * {@link com.cyc.query.exception.QueryRuntimeException}. If no answers have been found, this 
   * method will return <code>null</code>.
   * 
   * <p>For a single-variable query which might reasonably return multiple answers, similar behavior
   * can be accomplished with something like the following:
   * <code>
   * QueryAnswers answers = query.getAnswers();
   * T value = (!answers.isEmpty()) ? answers.<T>getBindingsForSingleVariableQuery().get(0) : null;
   * </code>
   * 
   * @param <T> The expected class of the value.
   * @return a single value for a single variable from the sole answer to a query, or <code>null</code> if no answer has been found.
   */
  <T> T getBindingForSingleVariableSingleAnswerQuery();
  
  List<String> toAnswersTableStrings(boolean includeOuterBorder, String colBorder, String colPadding);
  
  List<String> toAnswersTableStrings(boolean includeOuterBorder);
  
  void printAnswersTable(PrintStream out, boolean includeOuterBorder, String colBorder, String colPadding);
  
  void printAnswersTable(PrintStream out, boolean includeOuterBorder);
  
}
