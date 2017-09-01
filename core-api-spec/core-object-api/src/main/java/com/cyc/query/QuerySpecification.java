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
import com.cyc.query.parameters.InferenceMode;
import com.cyc.query.parameters.InferenceParameterGetter;
import com.cyc.query.parameters.InferenceParameters;
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
 */
public interface QuerySpecification<T extends QuerySpecification> {
  
  /**
   * For saved queries (aka "KBQs") this returns the Cyc term that identifies the query. To change 
   * the id of an existing query, see {@link MutableQuerySpecification#saveAs(String)}
   *
   * @return  the id term
   */
  KbIndividual getId();
  
  /**
   * Returns the Sentence used to create this Query. Unless the sentence is changed via
   * {@link MutableQuerySpecification#setQuerySentence(com.cyc.kb.Sentence) }, this method will 
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
   * after {@link MutableQuerySpecification#setSubstitutions(java.util.Map) } has been invoked.
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
   * {@link MutableQuerySpecification#addCategory()}.
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
  
  //====|    MutableQuerySpecification    |=======================================================//
  
  /**
   * A mutable QuerySpecification.
   * 
   * @param <T> 
   */
  public interface MutableQuerySpecification<T extends QuerySpecification> 
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
     * {@link MutableQuerySpecification#getInferenceParameters()}.
     *
     * @param params the inference parameters
     * @return this Query object
     */
    T setInferenceParameters(final InferenceParameters params);
    
    /**
     * Applies substitutions to the original query. This method will <em>undo</em> any substitutions
     * which had previously been applied. To apply additional substitutions, use
     * {@link MutableQuerySpecification#addSubstitutions(java.util.Map)}.
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
     * to {@link MutableQuerySpecification#setSubstitutions(java.util.Map)}.
     *
     * @param substitutions
     * @return this query
     * @see com.cyc.query.MutableQuerySpecification#setSubstitutions(java.util.Map)
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
    
    /**
     * Bind a query variable to a specified value.
     *
     * @param  varName      The name of the variable, with or without the '?' prefix
     * @param  replacement  
     */
    void bindVariable(String varName, Object replacement);
    
    /**
     * Designates var as a variable to return bindings for.
     *
     * @param   var
     * @return  this query
     * @throws  IllegalArgumentException  if var is not found in this query
     * @throws  IllegalStateException     if query has already been started
     */
    Query addQueryVariable(Variable var)
            throws IllegalArgumentException, IllegalStateException;

    /**
     * Designates var as a variable to <i>not</i> return bindings for.
     *
     * @param   var  
     * @return  this Query
     * @throws  IllegalArgumentException  if var is not found in this query
     * @throws  IllegalStateException     if query has already been started
     */
    Query removeQueryVariable(Variable var)
            throws IllegalArgumentException, IllegalStateException;

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
  
}
