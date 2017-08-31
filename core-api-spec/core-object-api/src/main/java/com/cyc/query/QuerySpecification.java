package com.cyc.query;

/*
 * #%L
 * File: QuerySpecification.java
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
import com.cyc.kb.Context;
import com.cyc.kb.KbIndividual;
import com.cyc.kb.KbObject;
import com.cyc.kb.Sentence;
import com.cyc.kb.Variable;
import com.cyc.kb.exception.KbException;
import com.cyc.query.exception.QueryConstructionException;
import com.cyc.query.parameters.InferenceParameters;
import com.cyc.session.exception.SessionCommunicationException;
import java.util.Map;
import java.util.Set;

/**
 * <code>QuerySpecification</code> is designed to represent queries posed to Cyc. Information about
 * the <em>processing</em> of a query is represented by {@link com.cyc.query.Query}, which extends
 * this interface.
 *
 * @author  Vijay Raj
 * @author  David Baxter
 * @param   <T>
 *
 */
public interface QuerySpecification<T extends QuerySpecification> {

  /**
   * For saved queries (aka "KBQs") this returns the Cyc term that identifies the query. To change 
   * the id of an existing query, see {@link #saveAs(String)}
   *
   * @return  the id term
   */
  KbIndividual getId();
  
  /**
   * Returns the Sentence which will be asked of Cyc, after all substitutions have been applied. In
   * other words, this method essentially returns the value of {@link #getOriginalQuerySentence() }
   * after {@link #setSubstitutions(java.util.Map) } has been invoked.
   * 
   * @return  
   * @throws  KbException
   */
  Sentence getQuerySentence() throws KbException;
  
  /**
   * Set the Sentence for this Query.
   * 
   * @param   querySentence
   * @return  this object
   */
  T setQuerySentence(Sentence querySentence);
  
  /**
   * Returns the original Sentence used to create this Query. Unless the sentence is changed via
   * {@link #setQuerySentence(com.cyc.kb.Sentence) }, this method will return exactly the same 
   * Sentence every time it is called.
   * 
   * @return  
   * @throws  KbException
   */
  Sentence getOriginalQuerySentence() throws KbException;
  
  /**
   * Returns the Context of this Query.
   *
   * @return  the Context of this Query
   */
  Context getContext();
  
  /**
   * Set the Context of this Query.
   *
   * @param   ctx
   * @return  this object
   */
  T setContext(final Context ctx);
  
  /**
   * Returns the inference parameters for this query. Individual inference parameters may be modified
   * on the InferenceParameters object.
   * 
   * <p>For convenience, several of the most commonly used inference parameters may be also accessed
   * from getters and setters which are directly on the Query object. Those parameters are:
   * <ul>
   *   <li>inferenceMode (<code>:INFERENCE-MODE</code)</li>
   *   <li>maxAnswerCount (<code>:MAX-NUMBER</code)</li>
   *   <li>maxTime (<code>:MAX-TIME</code)</li>
   *   <li>maxTransformationDepth (<code>:MAX-TRANSFORMATION-DEPTH</code)</li>
   * </ul>
   * 
   * @return  the inference parameters
   */
  InferenceParameters getInferenceParameters();
  
  /**
   * Set the inference parameters for this query. Individual inference parameters may be modified
   * by accessing the Query's InferenceParameters object via 
   * {@link QuerySpecification#getInferenceParameters()}.
   * 
   * @param   params  the inference parameters
   * @return  this Query object
   */
  T setInferenceParameters(final InferenceParameters params);
  
  /**
   * Returns a map of the substitutions which have already been applied to the current query. 
   * 
   * @return  the substitutions which have been applied to the current query
   * @see     com.cyc.query.QuerySpecification#setSubstitutions(java.util.Map) 
   * @see     com.cyc.query.QuerySpecification#addSubstitutions(java.util.Map) 
   * @see     com.cyc.query.QuerySpecification#getUnresolvedIndexicals() 
   * @see     com.cyc.query.Indexicals
   * 
   */
  Map<KbObject, Object> getSubstitutions();
  
  /**
   * Applies substitutions to the original query. This method will <em>undo</em> any substitutions
   * which had previously been applied. To apply additional substitutions, use
   * {@link QuerySpecification#addSubstitutions(java.util.Map)}.
   * 
   * Indexical terms are frequently replaced in Sentences, particularly in Query sentences, so 
   * you may want to see {@link com.cyc.kb.KbObject#isIndexical() } for more details about them. 
   * However, this method can be used to replace <em>any</em> KbObjects within a Sentence, not just
   * indexicals. 
   * 
   * @param   substitutions
   * @return  this query
   * @see     com.cyc.kb.KbObject#isIndexical()
   * @see     com.cyc.kb.Sentence#getIndexicals(boolean) 
   * @see     com.cyc.kb.Sentence#replaceTerms(java.util.Map) 
   * @see     com.cyc.query.QuerySpecification#addSubstitutions(java.util.Map) 
   * @see     com.cyc.query.QuerySpecification#getSubstitutions()
   * @see     com.cyc.query.QuerySpecification#getUnresolvedIndexicals() 
   * @see     com.cyc.query.Indexicals
   */
  T setSubstitutions(Map<KbObject, Object> substitutions);
  
  /**
   * Applies substitutions to the current query, even if it has already had some substitutions 
   * applied. If substitutions have not already been set, this method effectively acts as a call to
   * {@link QuerySpecification#setSubstitutions(java.util.Map)}.
   * 
   * @param   substitutions
   * @return  this query
   * @see     com.cyc.query.QuerySpecification#setSubstitutions(java.util.Map) 
   */
  T addSubstitutions(Map<KbObject, Object> substitutions);
  
  /**
   * Returns a {@link QueryRules} object which provides access to the rules for the query.
   * 
   * @return  the QueryRules for the query
   * @throws  com.cyc.query.exception.QueryConstructionException
   * @throws  com.cyc.kb.exception.KbException
   */
  QueryRules getRules() throws QueryConstructionException, KbException;
  
  /**
   *
   * @return  
   * @throws  KbException
   */
  Sentence getQuerySentenceMainClause() throws KbException;
  
  /**
   *
   * @return  
   * @throws  KbException
   */
  Sentence getQuerySentenceHypothesizedClause() throws KbException;
  
  /**
   * Returns a Collection of the variables in this query for which bindings are sought. Note that
   * this is a copy of the variables, and modification of the returned value will not result in
   * modifications of the underlying Query.
   *
   * <p>This method is essentially a trampoline to {@link #getQuerySentence()#getVariables() }.
   * 
   * @return  a Set of the variables in this query for which bindings are to be sought
   * @throws  KbException
   */
  Set<Variable> getQueryVariables() throws KbException;
  
  /**
   * Returns a set of all remaining <code>#$IndexicalConcept</code>s which the application must 
   * resolve before the query can be run. As such, this set does <em>not</em> include 
   * automatically-resolvable indexicals, or indexicals which have already been resolved by 
   * substitutions previously added to the Query.
   * 
   * <p>This method is essentially a trampoline to {@link #getQuerySentence()#getIndexicals() }. To
   * get <em>all</em> of the indexicals for a Query (unresolved or not) see 
   * {@link #getOriginalQuerySentence()#getIndexicals(boolean) }.
   * 
   * <p>For a brief overview of indexicals, see {@link com.cyc.kb.KbObject#isIndexical() }.
   * 
   * @return  the indexicals for a query
   * @throws  KbException
   * @throws  SessionCommunicationException 
   * @see     com.cyc.kb.KbObject#isIndexical()
   * @see     com.cyc.kb.Sentence#getIndexicals(boolean) 
   * @see     com.cyc.kb.Sentence#replaceTerms(java.util.Map) 
   * @see     com.cyc.query.QuerySpecification#addSubstitutions(java.util.Map) 
   * @see     com.cyc.query.QuerySpecification#getSubstitutions()
   */
  Set<KbObject> getUnresolvedIndexicals() throws KbException, SessionCommunicationException;
  
}
