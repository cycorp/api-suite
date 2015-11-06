package com.cyc.query;

/*
 * #%L
 * File: Query.java
 * Project: Core API Specification
 * %%
 * Copyright (C) 2013 - 2015 Cycorp, Inc
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
import com.cyc.query.metrics.InferenceMetricsValues;
import com.cyc.query.metrics.InferenceMetrics;
import com.cyc.query.exception.QueryConstructionException;
import com.cyc.kb.ArgPosition;
import com.cyc.kb.Context;
import com.cyc.kb.KBIndividual;
import com.cyc.kb.KBObject;
import com.cyc.kb.Sentence;
import com.cyc.kb.Variable;
import com.cyc.kb.exception.KBApiException;
import com.cyc.query.exception.QueryApiRuntimeException;
import com.cyc.session.CycSession;
import com.cyc.session.SessionCommunicationException;
import com.cyc.session.exception.OpenCycUnsupportedFeatureException;
import java.io.Closeable;

import java.util.*;

/**
 * <code>QueryImpl</code> is designed to represent queries posed to Cyc and provide
 * access to their results.
 * <p>
 * In general, the process of getting an answer from Cyc is:
 * <ol>
 * <li> Create a <code>QueryImpl</code> object and set relevant fields on it.
 * <li> Access the answers via methods like
 * {@link #getAnswersCyc()}, {@link #getResultSet()}, or by adding a listener
 * via {@link #addListener(com.cyc.query.QueryListener)} and starting the query
 * via {@link #performInference()}.
 * <li> To avoid filling up memory on the Cyc server, {@link #close()} the QueryImpl
 when you are done with it, which will free up any lingering associated
 inference resources on the Cyc image. Queries are also closed when their
 {@link #finalize()} method is invoked, notably when they are garbage
 * collected.
 * </ol>
 *
 * @author Vijay Raj
 * @author David Baxter
 *
 */
public interface Query extends Closeable, InferenceParameterSetter, InferenceParameterGetter {


  /**
   * Run this query and return the results.
   *
   * @return the results of running the query.
   * @throws QueryApiRuntimeException if an exception is thrown during
   * inference.
   *
   */
  public QueryResultSet performInference() throws QueryApiRuntimeException;
  
  /**
   * Get the Cyc term that defines this query. To change the Id of an existing
   * query, see {@link #saveAs(String)}
   *
   * @return the id term.
   */
  public KBIndividual getId();

  /**
   *
   * @param indexicals
   */
  public void substituteTerms(Map<KBObject, Object> indexicals);


  /**
   * Ensure that any required Semantic Knowledge Source removal modules for this
   * query have been registered on the Cyc Server and made available for
   * inference.
   * <p/>
   * This should be done prior to running a query or set of queries that relies
   * on real-time access to external knowledge sources.
   * <p/>
   * Required knowledge sources are noted in the KB using the predicate
   * sksiModulesNeeded.
   * 
   * @todo can we figure out how to not require that this be done?  It seems silly to not auto-register.
   */
  public void registerRequiredSKSModules();
  
  /**
   * Saves this Query as the term which is its current ID.
   *
   * @see Query#saveAs(String)
   * @see Query#getId()
   */
  public void save();

  /**
   * Saves this Query as a new query term with the specified name.
   *
   * @param name The name by which to save the query.
   *
   * @return the new term
   * @throws com.cyc.query.exception.QueryConstructionException
   * @see Query#save()
   */
  public KBIndividual saveAs(String name) throws KBApiException, SessionCommunicationException, QueryConstructionException;
          
  /**
   * Returns the categories to which this query belongs. Categories are
   * associated with queries via
   * {@link #addCategory()}.
   *
   * @return the categories to which this query belongs.
   * @todo move all this category stuff into another class??
   */
  public Collection<String> getCategories();

  /**
   * Add a new category to which this query belongs.
   *
   * @param category
   */
  public void addCategory(String category);
  
  /**
   * Get the inference identifier for this query.
   *
   * @return the identifier, or null if inference has not been started.
   * @throws SessionCommunicationException if there is a problem communicating with
   * Cyc.
   */
  public InferenceIdentifier getInferenceIdentifier() throws SessionCommunicationException; 

  /**
   * Get the metrics to collect for this QueryImpl.
   *
   * @return the metrics
   *
   * @see com.cyc.base.inference.InferenceParameters#getMetrics()
   */
  public InferenceMetrics getMetrics();

  /**
   * Get the metrics values for this QueryImpl.
   *
   * @throws SessionCommunicationException if there is a problem communicating with
   * Cyc.
   * @return the metrics values.
   */
  public InferenceMetricsValues getMetricsValues() throws SessionCommunicationException;

  /**
   * Return the inference parameters for this query.
   *
   * @return the inference parameters.
   */
  public InferenceParameters getInferenceParameters();

  /**
   * Adds a listener to this query.
   *
   * @param listener
   * @return this query.
   */
  public Query addListener(QueryListener listener);

  /**
   * Designates var as a variable to return bindings for.
   *
   * @param var
   * @return this query.
   * @throws IllegalArgumentException if var is not found in this query.
   * @throws IllegalStateException if query has already been started.
   */
  public Query addQueryVariable(Variable var);


  /**
   * Bind a query variable to a specified value. All occurrences of the variable
   * in this query's sentence will be replaced with the specified value.
   *
   * @param var must be a query variable in this query.
   * @param replacement the value to substitute for var.
   */
  public void bindVariable(Variable var, Object replacement);

  /**
   * Bind a query variable to a specified value.
   *
   * @param varName The name of the variable, with or without the '?' prefix.
   * @param replacement
   */
  public void bindVariable(String varName, Object replacement);

  /**
   * Designates var as a variable to <i>not</i> return bindings for.
   *
   * @param var
   * @return this QueryImpl
   * @throws IllegalArgumentException if var is not found in this query.
   * @throws IllegalStateException if query has already been started.
   */
  public Query removeQueryVariable(Variable var);

  /**
   * Return a Collection of the variables in this query for which bindings are
   * sought. Note that this is a copy of the variables, and modification of the
 returned value will not result in modifications of the underlying QueryImpl.
   *
   * @return a Collection of the variables in this query for which bindings are
   * to be sought.
   * @throws KBApiException
   */
  public java.util.Collection<Variable> getQueryVariables() throws KBApiException;

  /**
   * Continues the query. Can be used if a query has not been started yet, has
   * stopped due to reaching the specified number of answers, or has used its
   * alloted time or other resources and is continuable.
   * <p>
   * Any resource constraints, e.g. time or number, get to "start over," so if
   * the inference has already used its alloted 5 seconds, or found the
   * specified three answers, continuing it will allow it to run for up to
   * <i>another</i>
   * 5 seconds, or until it finds up to <i>another</i> three answers.
   * <p>
   * Returns when the inference has stopped running.
   *
   * @see #setMaxNumber(Integer)
   * @see #setMaxTime(Integer)
   * @see #isContinuable()
   * @see #setContinuable(boolean)
   *
   * @see
   * InferenceWorker#continueInference(com.cyc.base.inference.InferenceParameters)
   */
  public void continueQuery();

  /**
   * Identifies redundant clauses in this query.
   *
   * For instance, if one clause isa (isa ?X Dog) and another is (isa ?X
   * GreatDane), that pair is considered redundant. This method provides no
   * guidance as to what can or should be done to resolve the redundancy, and in
   * fact it may be virtually harmless, as Cyc can often solve such a query
   * almost as efficiently as it can solve the more specific clause of the pair.
   *
   * @return a collection of pairs of any such clauses
   * @throws KBApiException
   * @throws com.cyc.session.SessionCommunicationException
   * @throws com.cyc.session.exception.OpenCycUnsupportedFeatureException when run against an OpenCyc server.
   */
  public Collection<Collection<Sentence>> findRedundantClauses() throws KBApiException, SessionCommunicationException, OpenCycUnsupportedFeatureException;

  /**
   * Identifies unconnected clauses in this query. Generally, all clauses of a
   * query will be connected by a chain of variables that connect them together.
   * Queries with unconnected clauses are effectively separate queries, and
   * running queries with disconnected clauses generally results in a cartesian
   * product of the answer sets of the two separate queries.
   *
   * @return a collection of the arg positions of any such clauses
   * @throws com.cyc.session.SessionCommunicationException if there is a problem communicating with
   * Cyc.
   * @throws com.cyc.session.exception.OpenCycUnsupportedFeatureException when run against an OpenCyc server.
   */
  public Collection<ArgPosition> findUnconnectedClauses() throws SessionCommunicationException, OpenCycUnsupportedFeatureException;

  /**
   * Conjoin this sentence with otherQuery, attempting to unify and rename
   * variables. Typically, two different variables will unify into a single
   * variable, causing all the uses of one of the variables to be renamed with
   * the name of the other. In some cases, additional renaming may happen (e.g.
   * if the queries contain mnemonic variables that become more tightly
   * constrained as a result of the unification, a new mnemonic variable may be
   * used in place of both of the original variables).
   *
   * @param otherQuery
   * @return the new query
   * @throws QueryConstructionException if there was a problem constructing the new query
   * @throws com.cyc.session.SessionCommunicationException
   * @throws com.cyc.session.exception.OpenCycUnsupportedFeatureException when run against an OpenCyc server.
   */
  public Query merge(Query otherQuery) throws 
          QueryConstructionException,
          SessionCommunicationException,
          OpenCycUnsupportedFeatureException;
  
  /**
   * Set the inference mode for this QueryImpl.
   *
   * @param mode
   * @return this QueryImpl
   *
   * @todo get a new version of this...
   * @see
   * com.cyc.base.inference.InferenceParameters#setInferenceMode(com.cyc.base.inference.InferenceMode)
   */
  public InferenceParameterSetter setInferenceMode(InferenceMode mode);

  /**
   * Check whether this Query (once run) is browsable.
   *
   * @return true iff it is so specified.
   */
  public Boolean isBrowsable();

  public InferenceParameterSetter setBrowsable(boolean value);

  /**
   * Check whether this Query is continuable. Queries that have not yet been run
   * are considered continuable, as well as ones whose parameters have the
   * continuable flag set.
   *
   * @see DefaultInferenceParameters#setContinuable(boolean)
   * @see #continueQuery()
   *
   * @return true iff it can be continued.
   */
  public Boolean isContinuable();

  /**
   * {@inheritDoc}
   *
   * @return this QueryImpl
   * @see #continueQuery()
   */
  public InferenceParameterSetter setContinuable(boolean b);

  /**
   * Set the maximum number of answers (or sets of answers) that Cyc will
 attempt to find for this Query. In some cases (such as when a set of
   * answers is retrieved in a batch), more answers than this may actually be
   * returned. Once this number of answers has been reached, Cyc will not
   * actively look for additional answers.
   *
   * @param max
   * @return this Query
   *
   * @see com.cyc.base.inference.InferenceParameters#setMaxNumber(Integer)
   */
  public InferenceParameterSetter setMaxNumber(Integer max);

  /**
   * Set the Context of this Query.
   *
   * @param ctx
   * @return this object.
   */
  public Query setContext(final Context ctx);

  /**
   * Set the soft timeout for this Query in seconds.
   *
   * @param max
   * @return this Query
   *
   * @todo fix this so it either takes milliseconds or a Float.  Second granularity is too big.
   * @see com.cyc.base.inference.InferenceParameters#setMaxTime(Integer)
   */
  public InferenceParameterSetter setMaxTime(Integer max);

  /**
   * Set the metrics to collect for this QueryImpl.
   *
   * @param metrics
   * @return this QueryImpl
   *
   * @see
   * com.cyc.base.inference.InferenceParameters#setMetrics(com.cyc.base.inference.metrics.InferenceMetrics)
   * @throws IllegalStateException if query has already been started.
   */
  public InferenceParameterSetter setMetrics(InferenceMetrics metrics);

  /**
   * Check whether this Query allows abduction.
   *
   * @return true if it does, false otherwise.
   */
  public boolean getAbductionAllowed();

  /**
   * Get the flavor of CycL to use for answers, or null if unspecified.
   *
   * @return the <code>InferenceAnswerLanguage</code>
   * @see InferenceParameterGetter#getAnswerLanguage().
   *
   */
  public InferenceAnswerLanguage getAnswerLanguage();

  /**
   * Get the criteria for what to do when disjuncts have difference free
   * variables.
   *
   * @return the <code>DisjunctionFreeELVarsPolicy</code>, or null if
   * unspecified.
   * @see InferenceParameterGetter#getDisjunctionFreeELVarsPolicy().
   *
   */
  public DisjunctionFreeELVarsPolicy getDisjunctionFreeELVarsPolicy();

  public Integer getMaxTransformationDepth();

  public ProblemReusePolicy getProblemReusePolicy();

  public ProofValidationMode getProofValidationMode();

  public ResultUniqueness getResultUniqueness();

  public TransitiveClosureMode getTransitiveClosureMode();

  public InferenceParameterSetter setAnswerLanguage(InferenceAnswerLanguage language);

  public InferenceParameterSetter setDisjunctionFreeELVarsPolicy(DisjunctionFreeELVarsPolicy policy);

  public InferenceParameterSetter setMaxTransformationDepth(Integer i);

  public InferenceParameterSetter setProblemReusePolicy(ProblemReusePolicy policy);

  public InferenceParameterSetter setProofValidationMode(ProofValidationMode mode);

  public InferenceParameterSetter setResultUniqueness(ResultUniqueness mode);

  public InferenceParameterSetter setTransitiveClosureMode(TransitiveClosureMode mode);

  /**
   * Set the inference parameters for this query.
   *
   * @param params the inference parameters
   * @return this QueryImpl object.
   */
  public Query setInferenceParameters(final InferenceParameters params);


  /**
   * Sets the hypothesized clause of this QueryImpl. When the query is run, Cyc will
   * assume that this clause is true. If the clause is independently known to be
   * false in the query context, the query will be considered tautologous, and
   * will fail.
   *
   * @param sentence
   * @return this QueryImpl.
   * @see Query#getQuerySentenceHypothesizedClause()
   * @throws IllegalStateException if query has already been started.
   */
  public Query setQuerySentenceHypothesizedClause(Sentence sentence);

  /**
   * Sets the main (i.e. non-hypothesized) clause of this QueryImpl
   *
   * @param sentence
   * @return this QueryImpl.
   * @see Query#getQuerySentenceMainClause()
   * @throws IllegalStateException if query has already been started.
   */
  public Query setQuerySentenceMainClause(Sentence sentence);

  /**
   * Designates vars as the variables to return bindings for.
   *
   * @param vars
   * @return this query.
   * @throws IllegalArgumentException if any of vars is not found in this query.
   * @throws IllegalStateException if query has already been started.
   */
  public Query setQueryVariables(Collection<Variable> vars);

  /**
   * Starts the query.
   *
   *
   * @throws com.cyc.session.SessionCommunicationException if there is a problem communicating with
   * Cyc.
   */
  public void start() throws SessionCommunicationException;

  /**
   * Issues a request that the query stop immediately.
   *
   * @param patience If non-null, the query will be forcibly aborted if it does
   * not stop before this many seconds have elapsed.
   */
  public void stop(final Integer patience);

  /**
   * Get the Cyc session to be used for this query.
   *
   * @return a CycSession for this query.
   */
  public CycSession getCycSession();

  /**
   * Specify that this inference should be retained by Cyc until the Query is
 closed. This can be called before the query has been started, and must be
   * called before the query has finished running.
   *
   * @see Query#close()
   */
  public void retainInference();

 

  /**
   * Returns the number of answers found for this query. For running queries,
   * the value returned by this method may change as additional answers are
   * found.
   *
   * @return the number of answers found for this query.
   */
  public int getAnswerCount();

  /**
   * Returns the list of answers for this query. For running queries, the value
   * returned by this method may change as additional answers are found.
   *
   * @return the list of answers
   * @throws com.cyc.session.SessionCommunicationException if there is a problem communicating with
   * Cyc.
   */
  public List<QueryAnswer> getAnswers() throws SessionCommunicationException;

  /**
   * Returns the nth answer for this query. For the first answer, n == 0.
   *
   * @param n
   * @return the answer.
   * @throws com.cyc.session.SessionCommunicationException if there is a problem communicating with
   * Cyc.
   */
  public QueryAnswer getAnswer(final int n) throws SessionCommunicationException;

  /**
   * Returns the Context of this Query.
   *
   * @return the Context of this Query
   */
  public Context getContext();

  /**
   *
   * @return @throws KBApiException
   */
  public Sentence getQuerySentence() throws KBApiException;

  
  /**
   *
   * @param querySentence
   */
  public void setQuerySentence(Sentence querySentence);


  /**
   *
   * @return @throws KBApiException
   */
  public Sentence getQuerySentenceMainClause() throws KBApiException;


  /**
   *
   * @return @throws KBApiException
   */
  public Sentence getQuerySentenceHypothesizedClause() throws KBApiException;


  /**
   * Get the CycL sentence from the specified answer to this query. Substitutes
   * the set of bindings from answer into the query sentence.
   *
   * @param answer
   * @return the answer sentence
   * @throws KBApiException
   */
  public Sentence getAnswerSentence(QueryAnswer answer) throws KBApiException;

  /**
   * Forget all results for this query. All settings on the Query are retained,
 including the query sentence, context, and inference parameters. After a
 QueryImpl has been cleared, it can be re-run, with possibly different results.
   *
   * @return this Query
   */
  public Query clearResults();

  /**
   * Returns the soft timeout for this QueryImpl in seconds.
   *
   * @return the soft timeout for this QueryImpl in seconds.
   *
   * @see com.cyc.base.inference.InferenceParameters#getMaxTime()
   * @todo use milliseconds, or non-integer
   */
  public Integer getMaxTime();

  /**
   * Returns the maximum number of answers (or sets of answers) that Cyc will
 attempt to find for this Query. In some cases (such as when a set of
   * answers is retrieved in a batch), more answers than this may actually be
   * returned. Once this number of answers has been reached, Cyc will not
   * actively look for additional answers.
   *
   * @return the number cutoff for this Query.
   *
   * @see com.cyc.base.inference.InferenceParameters#getMaxNumber()
   * @todo rename to getMaxAnserCount ??
   */
  public Integer getMaxNumber();

  /**
   * Returns the inference mode for this Query.
   *
   * @return the inference mode for this Query.
   *
   * @see com.cyc.base.inference.InferenceParameters#getInferenceMode()
   */
  public InferenceMode getInferenceMode();

  /**
   * Return the InferenceStatus for this Query.
   *
   * @return the InferenceStatus for this Query.
   */
  public InferenceStatus getStatus();

  /**
   * Return the reason why this QueryImpl was suspended (if it was).
   *
   * @return the reason, or null if this QueryImpl was not suspended.
   * @see DefaultInferenceSuspendReason for examples.
   */
  public InferenceSuspendReason getSuspendReason();

  /**
   *
   * @return true iff this query has been proven true.
   * @throws RuntimeException if the query has open variables.
   * @see com.cyc.base.inference.InferenceResultSet#getTruthValue()
   */
  public boolean isTrue();

  /**
   * Is this query either True (if a boolean query) or does it have bindings (if
   * non-boolean)
   *
   * @return True if there are bindings (or it's a true boolean query), false if
   * there are no bindings (or it's a false boolean query).
   *
   */
  public boolean isProvable();

  /**
   * Closes this query's result set, and releases resources on the Cyc server.
   * See {@link com.cyc.query.KBInferenceResultSet#close()} for more details on
   * what happens when a query is closed.
   * <p>
 It is good practice to always invoke this method explicitly as soon as a
 QueryImpl is no longer needed, as they can take up significant amounts of
 memory on the Cyc server.
   *
   * @see com.cyc.query.KBInferenceResultSet#close()
   */
  @Override
  public void close();

  /**
   * Returns the timeout for the {@link #close()} method.
   *
   * @return timeout in milliseconds
   */
  public long getCloseTimeout();

  /**
   * Sets the timeout for the {@link #close()} method.
   *
   * @param timeoutMS timeout in milliseconds
   */
  public void setCloseTimeout(long timeoutMS);

  /**
   *
   * @return this query's result set. The KBInferenceResultSet is an object that
   * may be updated dynamically for running queries. This contrasts with
   * {@link #getAnswersCyc()} which returns a static list of the answers at the
   * time is was called.
   *
   * @see com.cyc.query.KBInferenceResultSet
   */
  public QueryResultSet getResultSet();


}
