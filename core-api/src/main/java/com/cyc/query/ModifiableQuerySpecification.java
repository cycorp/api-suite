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
 * File: ModifiableQuerySpecification.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2017 Cycorp, Inc
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

import com.cyc.kb.Context;
import com.cyc.kb.KbIndividual;
import com.cyc.kb.KbObject;
import com.cyc.kb.Sentence;
import com.cyc.kb.Variable;
import com.cyc.kb.exception.KbException;
import com.cyc.query.exception.QueryConstructionException;
import com.cyc.query.parameters.InferenceMode;
import com.cyc.query.parameters.InferenceParameters;
import com.cyc.session.exception.SessionCommunicationException;
import java.util.Collection;
import java.util.Map;

/**
 * A modifiable QuerySpecification.
 *
 * @param <T>
 */
public interface ModifiableQuerySpecification<T extends QuerySpecification>
        extends QuerySpecification<T> {

  /**
   * Set the Sentence for this Query.
   *
   * @param querySentence
   * @return this object
   */
  T setQuerySentence(Sentence querySentence);

  /**
   * Set the Context of this Query.
   *
   * @param ctx
   * @return this object
   */
  T setContext(final Context ctx);

  /**
   * Returns the inference parameters for this query. Individual inference parameters may be modified
   * on the InferenceParameters object.
   *
   * <p>For convenience, several of the most commonly used inference parameters may be also accessed
   * from getters and setters which are directly on the Query object. Those parameters are:
   * <ul>
   *   <li>inferenceMode (<code>:INFERENCE-MODE</code>)</li>
   *   <li>maxAnswerCount (<code>:MAX-NUMBER</code>)</li>
   *   <li>maxTime (<code>:MAX-TIME</code>)</li>
   *   <li>maxTransformationDepth (<code>:MAX-TRANSFORMATION-DEPTH</code>)</li>
   * </ul>
   *
   * @return  the inference parameters
   */
  @Override
  InferenceParameters getInferenceParameters();

  /**
   * Set the inference parameters for this query. Individual inference parameters may be modified
   * by accessing the Query's InferenceParameters object via
   * {@link ModifiableQuerySpecification#getInferenceParameters()}.
   *
   * @param params the inference parameters
   * @return this Query object
   */
  T setInferenceParameters(final InferenceParameters params);

  /**
   * Applies substitutions to the original query. This method will <em>undo</em> any substitutions
   * which had previously been applied. To apply additional substitutions, use
   * {@link ModifiableQuerySpecification#addSubstitutions(java.util.Map)}.
   *
   * Indexical terms are frequently replaced in Sentences, particularly in Query sentences, so you
   * may want to see {@link com.cyc.kb.KbObject#isIndexical() } for more details about them.
   * However, this method can be used to replace <em>any</em> KbObjects within a Sentence, not
   * just indexicals.
   *
   * @param substitutions
   * @return this query
   * @see com.cyc.kb.KbObject#isIndexical()
   * @see com.cyc.kb.Sentence#getIndexicals(boolean)
   * @see com.cyc.kb.Sentence#replaceTerms(java.util.Map)
   * @see com.cyc.query.QuerySpecification#addSubstitutions(java.util.Map)
   * @see com.cyc.query.QuerySpecification#getSubstitutions()
   * @see com.cyc.query.QuerySpecification#getUnresolvedIndexicals()
   * @see com.cyc.query.Indexicals
   */
  T setSubstitutions(Map<KbObject, Object> substitutions);

  /**
   * Applies substitutions to the current query, even if it has already had some substitutions
   * applied. If substitutions have not already been set, this method effectively acts as a call
   * to {@link ModifiableQuerySpecification#setSubstitutions(java.util.Map)}.
   *
   * @param substitutions
   * @return this query
   * @see #setSubstitutions(java.util.Map) 
   */
  T addSubstitutions(Map<KbObject, Object> substitutions);

  /**
   * Saves this Query as the term which is its current ID.
   *
   * @see     #saveAs(String)
   * @see     QuerySpecification#getId()
   */
  void save();

  /**
   * Saves this Query as a new query term with the specified name.
   *
   * @param name The name by which to save the query.
   *
   * @return  the new term
   * @throws  KbException
   * @throws  SessionCommunicationException  if there is a problem communicating with Cyc
   * @throws  QueryConstructionException     if there was a problem constructing the new query
   * @see     Query#save()
   */
  KbIndividual saveAs(String name) 
          throws KbException, SessionCommunicationException, QueryConstructionException;

  /**
   * Add a new category to which this query belongs.
   *
   * @param   category
   */
  void addCategory(String category);

  /**
   * Set the inference mode. Inference modes are meant to be intuitive measures
   * of how hard Cyc should work to answer a query. Setting the inference mode
   * sets various other low-level parameters to appropriate values for that
   * mode, but explictly setting values for such parameters overrides values set
   * by the mode.
   *
   * <p>This method is a trampoline to
   * {@link QuerySpecification#getInferenceParameters()#setInferenceMode(com.cyc.query.parameters.InferenceMode)}.
   *
   * @param   mode
   * @return  this object
   * @see     com.cyc.query.parameters.InferenceParameters for more parameters
   */
  T setInferenceMode(InferenceMode mode);

  /**
   * Set the maximum number of answers (or sets of answers) that Cyc will
   * attempt to find for the Query. In some cases (such as when a set of
   * answers is retrieved in a batch), more answers than this may actually be
   * returned. Once this number of answers has been reached, Cyc will not
   * actively look for additional answers.
   *
   * <p> A value of <code>null</code> means the inference will continue until it
   * exhausts or some other limit is reached.
   *
   * <p>This method is a trampoline to
   * {@link QuerySpecification#getInferenceParameters()#setMaxAnswerCount(java.lang.Integer)}.
   *
   * @param   maxAnswers  number of answers
   * @return  this object
   * @see     com.cyc.query.parameters.InferenceParameters for more parameters
   */
  T setMaxAnswerCount(Integer maxAnswers);

  /**
   * Set the max time value (in seconds). Setting this parameter to some number
   * licenses Cyc to stop work on an inference once it has been working on it
   * for at least that many seconds.
   * <p>
   * A value of <code>null</code> means the inference will continue until it
   * exhausts or some other limit is reached.
   *
   * <p>This method is a trampoline to
   * {@link QuerySpecification#getInferenceParameters()#setMaxTime(java.lang.Integer)}.
   *
   * @param   maxSeconds timeout value in seconds
   * @return  this object
   * @see     com.cyc.query.parameters.InferenceParameters for more parameters
   */
  T setMaxTime(Integer maxSeconds);

  /**
   * Returns the timeout for the {@link Query#close()} method.
   *
   * @return  timeout in milliseconds
   */
  long getCloseTimeout();

  /**
   * Sets the timeout for the {@link Query#close()} method.
   *
   * @param   timeoutMs  timeout in milliseconds
   */
  T setCloseTimeout(long timeoutMs);

  /**
   * Set the max transformation depth value. Setting this parameter to some
   * number prevents Cyc from reasoning using chains of rules longer than that
   * number.
   *
   * <p>This method is a trampoline to
   * {@link QuerySpecification#getInferenceParameters()#setMaxTransformationDepth(java.lang.Integer)}.
   *
   * @param   depth
   * @return  this object
   * @see     com.cyc.query.parameters.InferenceParameters for more parameters
   */
  T setMaxTransformationDepth(Integer depth);

  /**
   * Sets the hypothesized clause of this Query. When the query is run, Cyc will assume that this
   * clause is true. If the clause is independently known to be false in the query context, the
   * query will be considered tautologous, and will fail.
   *
   * @param   sentence
   * @return  this Query
   * @throws  IllegalStateException  if query has already been started
   * @see     QuerySpecification#getQuerySentenceHypothesizedClause()
   */
  T setQuerySentenceHypothesizedClause(Sentence sentence);

  /**
   * Sets the main (i.e. non-hypothesized) clause of this Query
   *
   * @param   sentence
   * @return  this Query
   * @throws  IllegalStateException  if query has already been started
   * @see     QuerySpecification#getQuerySentenceMainClause()
   */
  T setQuerySentenceMainClause(Sentence sentence);

  /**
   * Bind a query variable to a specified value. All occurrences of the variable in this query's
   * sentence will be replaced with the specified value.
   *
   * @param  var          must be a query variable in this query
   * @param  replacement  the value to substitute for var
   */
  void bindVariable(Variable var, Object replacement);

  /* *
   * Bind a query variable to a specified value.
   *
   * @param  varName      The name of the variable, with or without the '?' prefix
   * @param  replacement
   * /
  void bindVariable(String varName, Object replacement);
   */
  /**
   * Designates var as a variable to return bindings for.
   *
   * @param   var
   * @return  this query
   * @throws  IllegalArgumentException  if var is not found in this query
   * @throws  IllegalStateException     if query has already been started
   */
  Query addQueryVariable(Variable var) throws IllegalArgumentException, IllegalStateException;

  /**
   * Designates var as a variable to <i>not</i> return bindings for.
   *
   * @param   var
   * @return  this Query
   * @throws  IllegalArgumentException  if var is not found in this query
   * @throws  IllegalStateException     if query has already been started
   */
  Query removeQueryVariable(Variable var) throws IllegalArgumentException, IllegalStateException;

  /**
   * Designates vars as the variables to return bindings for.
   *
   * @param   vars
   * @return  this query
   * @throws  IllegalArgumentException  if any of vars is not found in this query
   * @throws  IllegalStateException     if query has already been started
   */
  Query setQueryVariables(Collection<Variable> vars) 
          throws IllegalArgumentException, IllegalStateException;
  
}
