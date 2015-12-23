package com.cyc.query;

/*
 * #%L
 * File: Query.java
 * Project: Core API Object Specification
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
import com.cyc.kb.ArgPosition;
import com.cyc.kb.Context;
import com.cyc.kb.KbIndividual;
import com.cyc.kb.KbObject;
import com.cyc.kb.Sentence;
import com.cyc.kb.Variable;
import com.cyc.kb.exception.KbException;
import com.cyc.query.exception.QueryConstructionException;
import com.cyc.query.exception.QueryRuntimeException;
import com.cyc.query.metrics.InferenceMetricsValues;
import com.cyc.session.CycSession;
import com.cyc.session.exception.OpenCycUnsupportedFeatureException;
import com.cyc.session.exception.SessionCommunicationException;

import java.io.Closeable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <code>Query</code> is designed to represent queries posed to Cyc and provide
 * access to their results.
 * <p>
 * In general, the process of getting an answer from Cyc is:
 * <ol>
 * <li> Create a <code>Query</code> object and set relevant fields on it.
 * <li> Access the answers via methods like
 * {@link #getAnswersCyc()}, {@link #getResultSet()}, or by adding a listener
 * via {@link #addListener(com.cyc.query.QueryListener)} and starting the query
 * via {@link #performInference()}.
 * <li> To avoid filling up memory on the Cyc server, {@link #close()} the Query
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
   * @throws QueryRuntimeException if an exception is thrown during
   * inference.
   *
   */
  public QueryResultSet performInference() throws QueryRuntimeException;
  
  /**
   * Get the Cyc term that defines this query. To change the Id of an existing
   * query, see {@link #saveAs(String)}
   *
   * @return the id term.
   */
  public KbIndividual getId();

  /**
   *
   * @param indexicals
   */
  public void substituteTerms(Map<KbObject, Object> indexicals);


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
   * @todo can we figure out how to not require that this be done?  It seems silly to not 
   * auto-register.
   */
  public void registerRequiredSksModules();
  
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
   * @throws com.cyc.kb.exception.KbException
   * @throws com.cyc.session.exception.SessionCommunicationException if there is a problem 
   * communicating with Cyc.
   * @throws com.cyc.query.exception.QueryConstructionException if there was a problem constructing 
   * the new query.
   * @see Query#save()
   */
  public KbIndividual saveAs(String name) 
          throws KbException, SessionCommunicationException, QueryConstructionException;
          
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
   * Get the metrics values for this Query.
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
   * @return this Query
   * @throws IllegalArgumentException if var is not found in this query.
   * @throws IllegalStateException if query has already been started.
   */
  public Query removeQueryVariable(Variable var);

  /**
   * Return a Collection of the variables in this query for which bindings are
   * sought. Note that this is a copy of the variables, and modification of the
   * returned value will not result in modifications of the underlying Query.
   *
   * @return a Collection of the variables in this query for which bindings are
   * to be sought.
   * @throws KbException
   */
  public java.util.Collection<Variable> getQueryVariables() throws KbException;

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
   * @throws KbException
   * @throws com.cyc.session.exception.SessionCommunicationException
   * @throws com.cyc.session.exception.OpenCycUnsupportedFeatureException when run against an 
   * OpenCyc server.
   */
  public Collection<Collection<Sentence>> findRedundantClauses() 
          throws KbException, SessionCommunicationException, OpenCycUnsupportedFeatureException;

  /**
   * Identifies unconnected clauses in this query. Generally, all clauses of a
   * query will be connected by a chain of variables that connect them together.
   * Queries with unconnected clauses are effectively separate queries, and
   * running queries with disconnected clauses generally results in a cartesian
   * product of the answer sets of the two separate queries.
   *
   * @return a collection of the arg positions of any such clauses
   * @throws com.cyc.session.exception.SessionCommunicationException if there is a problem
   * communicating with Cyc.
   * @throws com.cyc.session.exception.OpenCycUnsupportedFeatureException when run against an 
   * OpenCyc server.
   */
  public Collection<ArgPosition> findUnconnectedClauses() 
          throws SessionCommunicationException, OpenCycUnsupportedFeatureException;

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
   * @throws com.cyc.session.exception.SessionCommunicationException
   * @throws com.cyc.session.exception.OpenCycUnsupportedFeatureException when run against an 
   * OpenCyc server.
   */
  public Query merge(Query otherQuery) throws 
          QueryConstructionException,
          SessionCommunicationException,
          OpenCycUnsupportedFeatureException;
  
  /**
   * Set the Context of this Query.
   *
   * @param ctx
   * @return this object.
   */
  public Query setContext(final Context ctx);
  
  /**
   * Set the inference parameters for this query.
   *
   * @param params the inference parameters
   * @return this Query object.
   */
  public Query setInferenceParameters(final InferenceParameters params);


  /**
   * Sets the hypothesized clause of this Query. When the query is run, Cyc will
   * assume that this clause is true. If the clause is independently known to be
   * false in the query context, the query will be considered tautologous, and
   * will fail.
   *
   * @param sentence
   * @return this Query.
   * @see Query#getQuerySentenceHypothesizedClause()
   * @throws IllegalStateException if query has already been started.
   */
  public Query setQuerySentenceHypothesizedClause(Sentence sentence);

  /**
   * Sets the main (i.e. non-hypothesized) clause of this Query
   *
   * @param sentence
   * @return this Query.
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
   * @throws com.cyc.session.exception.SessionCommunicationException if there is a problem 
   * communicating with Cyc.
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
   * @throws com.cyc.session.exception.SessionCommunicationException if there is a problem 
   * communicating with Cyc.
   */
  public List<QueryAnswer> getAnswers() throws SessionCommunicationException;

  /**
   * Returns the nth answer for this query. For the first answer, n == 0.
   *
   * @param answerIndex
   * @return the answer.
   * @throws com.cyc.session.exception.SessionCommunicationException if there is a problem 
   * communicating with Cyc.
   */
  public QueryAnswer getAnswer(final int answerIndex) throws SessionCommunicationException;

  /**
   * Returns the Context of this Query.
   *
   * @return the Context of this Query
   */
  public Context getContext();

  /**
   *
   * @return @throws KbException
   */
  public Sentence getQuerySentence() throws KbException;

  
  /**
   *
   * @param querySentence
   */
  public void setQuerySentence(Sentence querySentence);


  /**
   *
   * @return @throws KbException
   */
  public Sentence getQuerySentenceMainClause() throws KbException;


  /**
   *
   * @return @throws KbException
   */
  public Sentence getQuerySentenceHypothesizedClause() throws KbException;


  /**
   * Get the CycL sentence from the specified answer to this query. Substitutes
   * the set of bindings from answer into the query sentence.
   *
   * @param answer
   * @return the answer sentence
   * @throws KbException
   */
  public Sentence getAnswerSentence(QueryAnswer answer) throws KbException;

  /**
   * Forget all results for this query. All settings on the Query are retained, 
   * including the query sentence, context, and inference parameters. After a
   * Query has been cleared, it can be re-run, with possibly different results.
   *
   * @return this Query
   */
  public Query clearResults();

  /**
   * Return the InferenceStatus for this Query.
   *
   * @return the InferenceStatus for this Query.
   */
  public InferenceStatus getStatus();

  /**
   * Return the reason why this Query was suspended (if it was).
   *
   * @return the reason, or null if this Query was not suspended.
   * @see DefaultInferenceSuspendReason for examples.
   */
  public InferenceSuspendReason getSuspendReason();

  /**
   *
   * @return true iff this query has been proven true.
   * @throws RuntimeException if the query has open variables.
   * @see com.cyc.query.QueryResultSet#getTruthValue()
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
   * See {@link com.cyc.query.QueryResultSet#close()} for more details on
   * what happens when a query is closed.
   * 
   * <p>It is good practice to always invoke this method explicitly as soon as a Query is no longer
   * needed, as they can take up significant amounts of memory on the Cyc server.
   *
   * @see com.cyc.query.QueryResultSet#close()
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
   * @param timeoutMs timeout in milliseconds
   */
  public void setCloseTimeout(long timeoutMs);

  /**
   *
   * @return this query's result set. The QueryResultSet is an object that
   * may be updated dynamically for running queries. This contrasts with
   * {@link #getAnswersCyc()} which returns a static list of the answers at the
   * time is was called.
   *
   * @see com.cyc.query.QueryResultSet
   */
  public QueryResultSet getResultSet();


}
