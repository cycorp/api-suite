package com.cyc.query;

/*
 * #%L
 * File: QuerySpecification.java
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
import com.cyc.query.parameters.InferenceParameterGetter;
import com.cyc.session.exception.SessionCommunicationException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * <code>QuerySpecification</code> is designed to represent queries posed to Cyc. Information about
 * the <em>processing</em> of a query is represented by {@link com.cyc.query.Query}, which extends
 * this interface.
 *
 * @author  Vijay Raj
 * @author  David Baxter
 * @author  Nathan Winant
 * @param   <T>
 * 
 * @see ModifiableQuerySpecification
 */
public interface QuerySpecification<T extends QuerySpecification> {
  
  /**
   * For saved queries (aka "KBQs") this returns the Cyc term that identifies the query. To change 
   * the id of an existing query, see {@link ModifiableQuerySpecification#saveAs(String)}
   *
   * @return  the id term
   */
  KbIndividual getId();
  
  /**
   * Returns the Sentence used to create this Query. Unless the sentence is changed via
   * {@link ModifiableQuerySpecification#setQuerySentence(com.cyc.kb.Sentence) }, this method will 
   * return exactly the same Sentence every time it is called. Any substitutions made on the query
   * sentence (see {@link #getSubstitutions() }) will be reflected in a copy of this sentence, 
   * accessible via {@link #getQuerySentence() }.
   * 
   * @return  
   * @throws  KbException
   */
  Sentence getOriginalQuerySentence() throws KbException;
  
  /**
   * Returns the Sentence which will be asked of Cyc, after all substitutions have been applied. In
   * other words, this method essentially returns the value of {@link #getOriginalQuerySentence() }
   * after {@link ModifiableQuerySpecification#setSubstitutions(java.util.Map) } has been invoked.
   * 
   * @return  
   * @throws  KbException
   */
  Sentence getQuerySentence() throws KbException;
  
  /**
   * Returns the Context of this Query.
   *
   * @return  the Context of this Query
   */
  Context getContext();
  
  /**
   * Returns the inference parameters for this query. 
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
  InferenceParameterGetter getInferenceParameters();
  
  /**
   * Returns a map of the substitutions which have already been applied to the current query. 
   * 
   * @return  the substitutions which have been applied to the current query
   * @see     com.cyc.query.QuerySpecification#setSubstitutions(java.util.Map) 
   * @see     com.cyc.query.QuerySpecification#addSubstitutions(java.util.Map) 
   * @see     com.cyc.query.QuerySpecification#getUnresolvedIndexicals() 
   * @see     com.cyc.query.Indexicals
   */
  Map<KbObject, Object> getSubstitutions();
  
  /**
   * Returns the categories to which this query belongs. Categories are associated with queries via
   * {@link ModifiableQuerySpecification#addCategory()}.
   *
   * @todo    move all this category stuff into another class?
   * @return  the categories to which this query belongs.
   */
  Collection<String> getCategories();
  
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
