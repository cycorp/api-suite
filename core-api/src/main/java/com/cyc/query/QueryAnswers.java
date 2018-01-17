package com.cyc.query;

/*
 * #%L
 * File: QueryAnswers.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2018 Cycorp, Inc
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
import com.cyc.query.exception.QueryRuntimeException;
import java.io.PrintStream;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
   * Returns a List of all the values for one variable from all the answers to a query. If the query
   * has only one variable, it may be more convenient to call {@link #getBindingsForOnlyVariable() }.
   *
   * @param <O> The expected class of the value
   * @param var The variable for which the bindings are sought
   *
   * @return a list of all values for one variable from all answers to a query
   *
   * @see #getBindingsForOnlyVariable()
   * @see #getUniqueBindingsForVariable(com.cyc.kb.Variable)
   * @see #getUniqueBindingsForOnlyVariable()
   * @see #getOnlyBindingForVariable(com.cyc.kb.Variable)
   * @see #getOnlyBindingForOnlyVariable()
   */
  <O> List<O> getBindingsForVariable(Variable var);

  /**
   * Returns a List of all the values from all the answers to a single-variable query.
   * <p>
   * This method specifically expects the query to return bindings for <em>exactly one
   * variable;</em> the existence of more than variable will be interpreted as an error which will
   * result in a {@link com.cyc.query.exception.QueryRuntimeException}. If the query has more than
   * one variable, use {@link #getBindingsForVariable(com.cyc.kb.Variable) } instead.
   *
   * @param <O> The expected class of the value
   *
   * @return a list of all the values from all the answers to a single-variable query
   *
   * @throws QueryRuntimeException if the query contains more than one variable
   *
   * @see #getBindingsForVariable(com.cyc.kb.Variable)
   * @see #getUniqueBindingsForVariable(com.cyc.kb.Variable)
   * @see #getUniqueBindingsForOnlyVariable()
   * @see #getOnlyBindingForVariable(com.cyc.kb.Variable)
   * @see #getOnlyBindingForOnlyVariable()
   */
  <O> List<O> getBindingsForOnlyVariable() throws QueryRuntimeException;

  /**
   * Returns a Set of all unique values for one variable from all the answers to a query. If the
   * query has only one variable, it may be more convenient to call
   * {@link #getUniqueBindingsForOnlyVariable() }.
   *
   * @param <O> The expected class of the value
   * @param var The variable for which the bindings are sought
   *
   * @return a set of all unique values for one variable from all answers to a query
   *
   * @see #getBindingsForVariable(com.cyc.kb.Variable)
   *
   * @see #getBindingsForVariable(com.cyc.kb.Variable)
   * @see #getBindingsForOnlyVariable()
   * @see #getUniqueBindingsForOnlyVariable()
   * @see #getOnlyBindingForVariable(com.cyc.kb.Variable)
   * @see #getOnlyBindingForOnlyVariable()
   */
  <O> Set<O> getUniqueBindingsForVariable(Variable var);
  
  /**
   * Returns a Set of all unique values from all the answers to a single-variable query.
   * <p>
   * This method specifically expects the query to return bindings for <em>exactly one
   * variable;</em> the existence of more than variable will be interpreted as an error which will
   * result in a {@link com.cyc.query.exception.QueryRuntimeException}. If the query has more than
   * one variable, use {@link #getUniqueBindingsForVariable(com.cyc.kb.Variable) } instead.
   *
   * @param <O> The expected class of the value
   *
   * @return a list of all the values from all the answers to a single-variable query
   *
   * @throws QueryRuntimeException if the query contains more than one variable
   *
   * @see #getBindingsForVariable(com.cyc.kb.Variable)
   * @see #getBindingsForOnlyVariable()
   * @see #getUniqueBindingsForVariable(com.cyc.kb.Variable)
   * @see #getOnlyBindingForVariable(com.cyc.kb.Variable)
   * @see #getOnlyBindingForOnlyVariable()
   */
  <O> Set<O> getUniqueBindingsForOnlyVariable() throws QueryRuntimeException;

  /* 
  <O> Optional<O> getFirstBindingForVariable(Variable var);
  
  <O> Optional<O> getFirstBindingForOnlyVariable();
   */

  /**
   * Returns an Optional containing the sole answer (if present) to a query.
   * <p>
   * This method specifically expects the query to return <em>no more than one answer,</em> and will
   * interpret the existence of more than one answer as an error which will result in a
   * {@link QueryRuntimeException}. If no answers have been found, the Optional will be empty.
   *
   * @return an Optional containing the sole answer to a query, or empty if no answer has been found
   *
   * @see #getBindingsForVariable(com.cyc.kb.Variable)
   * @see #getBindingsForOnlyVariable()
   * @see #getUniqueBindingsForVariable(com.cyc.kb.Variable)
   * @see #getUniqueBindingsForOnlyVariable()
   * @see #getOnlyBindingForVariable(com.cyc.kb.Variable)
   */
  Optional<E> getOnlyAnswer() throws QueryRuntimeException;
  
  /**
   * Returns an Optional containing the single value for a single variable from the sole answer (if
   * present) to a query.
   *
   * <p>
   * This method specifically expects the query to return <em>no more than one answer,</em> and will
   * interpret the existence of more than one answer as an error which will result in a
   * {@link QueryRuntimeException}. If no answers have been found, the Optional will be empty.
   *
   * @param <O> The expected class of the value.
   * @param var The variable for which the binding is sought.
   *
   * @return an Optional containing the single value for a single variable from the sole answer to a
   *         query, or empty if no answer has been found.
   *
   * @see #getBindingsForVariable(com.cyc.kb.Variable)
   * @see #getBindingsForOnlyVariable()
   * @see #getUniqueBindingsForVariable(com.cyc.kb.Variable)
   * @see #getUniqueBindingsForOnlyVariable()
   * @see #getOnlyBindingForOnlyVariable()
   */
  <O> Optional<O> getOnlyBindingForVariable(Variable var) throws QueryRuntimeException;
  
  /**
   * Returns an Optional containing the single value from the sole answer (if present) to a
   * single-variable query.
   * <p>
   * Like {@link #getOnlyBindingForVariable(com.cyc.kb.Variable) }, this method specifically expects
   * the query to return <em>no more than one answer,</em> and will interpret the existence of more
   * than one answer as an error which will result in a {@link QueryRuntimeException}. If no answers
   * have been found, the Optional will be empty.
   * <p>
   * Furthermore, this method specifically expects the query to return bindings for <em>exactly one
   * variable;</em> the existence of more than variable will be interpreted as an error which will
   * result in a {@link QueryRuntimeException}. If the query has more than one variable, use
   * {@link #getOnlyBindingForVariable(com.cyc.kb.Variable)} instead.
   *
   * @param <O> The expected class of the value.
   *
   * @return an Optional containing the single value for a single variable from the sole answer to a
   *         query, or empty if no answer has been found.
   *
   * @see #getBindingsForVariable(com.cyc.kb.Variable)
   * @see #getBindingsForOnlyVariable()
   * @see #getUniqueBindingsForVariable(com.cyc.kb.Variable)
   * @see #getUniqueBindingsForOnlyVariable()
   * @see #getOnlyBindingForVariable(com.cyc.kb.Variable)
   */
  <O> Optional<O> getOnlyBindingForOnlyVariable() throws QueryRuntimeException;
  
  List<String> toAnswersTableStrings(boolean includeOuterBorder,
                                     String colBorder,
                                     String colPadding);

  List<String> toAnswersTableStrings(boolean includeOuterBorder);

  void printAnswersTable(PrintStream out,
                         boolean includeOuterBorder, 
                         String colBorder, 
                         String colPadding);

  void printAnswersTable(PrintStream out, boolean includeOuterBorder);

  List<String> toBindingsStringsForVariable(Variable var);

}
