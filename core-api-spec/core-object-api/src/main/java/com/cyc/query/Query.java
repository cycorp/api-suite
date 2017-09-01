package com.cyc.query;

/*
 * #%L
 * File: Query.java
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
import com.cyc.kb.ArgPosition;
import com.cyc.kb.Sentence;
import com.cyc.kb.exception.KbException;
import com.cyc.nl.Paraphraser;
import com.cyc.query.QuerySpecification.MutableQuerySpecification;
import com.cyc.query.exception.QueryConstructionException;
import com.cyc.query.exception.QueryRuntimeException;
import com.cyc.query.metrics.InferenceMetricsValues;
import com.cyc.query.parameters.InferenceMode;
import com.cyc.query.parameters.InferenceParameters;
import com.cyc.session.CycSession;
import com.cyc.session.exception.OpenCycUnsupportedFeatureException;
import com.cyc.session.exception.SessionCommunicationException;
import java.io.Closeable;
import java.util.Collection;

/**
 * <code>Query</code> extends {@link com.cyc.query.QuerySpecification} to represent queries posed to
 * Cyc and provide access to their results.
 * <p>
 * In general, the process of getting an answer from Cyc is:
 * <ol>
 * <li> Create a <code>Query</code> object and set relevant fields on it.
 * <li> Access the answers via methods like {@link #getAnswersCyc()}, {@link #getResultSet()}, or by
 * adding a listener via {@link #addListener(com.cyc.query.QueryListener)} and starting the query
 * via {@link #performInference()}.
 * <li> To avoid filling up memory on the Cyc server, {@link #close()} the Query when you are done
 * with it, which will free up any lingering associated inference resources on the Cyc image.
 * Queries are also closed when their {@link #finalize()} method is invoked, notably when they are
 * garbage collected.
 * </ol>
 *
 * @author  Vijay Raj
 * @author  David Baxter
 * @author  Nathan Winant
 */
public interface Query extends MutableQuerySpecification<Query>, Closeable {

  /**
   * Run this query and return the results.
   *
   * @return  the results of running the query
   * @throws  QueryRuntimeException  if an exception is thrown during inference
   */
  QueryResultSet performInference() throws QueryRuntimeException;
  
  /**
   * Ensure that any required Semantic Knowledge Source removal modules for this query have been
   * registered on the Cyc Server and made available for inference.
   * <p>
   * This should be done prior to running a query or set of queries that relies on real-time access
   * to external knowledge sources.
   * <p>
   * Required knowledge sources are noted in the KB using the predicate sksiModulesNeeded.
   *
   * @todo    can we figure out how to not require that this be done? It seems silly to not 
   *          auto-register.
   */
  void registerRequiredSksModules();
  
  /**
   * Get the metrics values for this Query.
   *
   * @return  the metrics values
   * @throws  SessionCommunicationException  if there is a problem communicating with Cyc
   */
  InferenceMetricsValues getMetricsValues() throws SessionCommunicationException;
  
  /**
   * Get the inference mode to use. Inference modes are meant to be intuitive
   * measures of how hard Cyc should work to answer a query. Setting the
   * inference mode sets various other low-level parameters to appropriate
   * values for that mode, but explictly setting values for such parameters
   * overrides values set by the mode.
   *
   * <p>This method is a trampoline to 
   * {@link QuerySpecification#getInferenceParameters()#getInferenceMode()}.
   * 
   * @return  the <code>InferenceMode</code>
   * @see     com.cyc.query.parameters.InferenceParameters for more parameters
   */
  InferenceMode getInferenceMode();

  /**
   * Returns the maximum number of answers (or sets of answers) that Cyc will
   * attempt to find for the Query. In some cases (such as when a set of
   * answers is retrieved in a batch), more answers than this may actually be
   * returned. Once this number of answers has been reached, Cyc will not
   * actively look for additional answers.
   *
   * <p>This method is a trampoline to 
   * {@link QuerySpecification#getInferenceParameters()#getMaxAnswerCount()}.
   * 
   * @return  the number cutoff for the Query
   * @see     com.cyc.query.parameters.InferenceParameters for more parameters
   */
  Integer getMaxAnswerCount();

  /**
   * Returns the soft timeout for the Query in seconds.
   *
   * <p>This method is a trampoline to 
   * {@link QuerySpecification#getInferenceParameters()#getMaxTime()}.
   * 
   * @return  the soft timeout for the Query in seconds
   * @see     com.cyc.query.parameters.InferenceParameters for more parameters
   */
  Integer getMaxTime();
  
  /**
   * Returns the max transformation depth value. Cyc will not reason using chains of rules longer
   * than this number.
   *
   * <p>This method is a trampoline to 
   * {@link QuerySpecification#getInferenceParameters()#getMaxTransformationDepth()}.
   * 
   * @return  the max transformation depth.
   * @see     com.cyc.query.parameters.InferenceParameters for more parameters
   */
  Integer getMaxTransformationDepth();
  
  /**
   * Check whether this Query is continuable. Queries that have not yet been run are considered
   * continuable, as well as ones whose parameters have the continuable flag set to 
   * <code>true</code>.
   *
   * @return  true iff it can be continued
   * @see     InferenceParameters#setContinuable(boolean)
   * @see     #continueQuery()
   */
  boolean isContinuable();
  
  /**
   * Adds a listener to this query.
   *
   * @param   listener
   * @return  this query
   */
  Query addListener(QueryListener listener);
  
  /**
   * Continues the query. Can be used if a query has not been started yet, has stopped due to
   * reaching the specified number of answers, or has used its alloted time or other resources and
   * is continuable.
   * <p>
   * Any resource constraints, e.g. time or number, get to "start over," so if the inference has
   * already used its alloted 5 seconds, or found the specified three answers, continuing it will
   * allow it to run for up to
   * <i>another</i>
   * 5 seconds, or until it finds up to <i>another</i> three answers.
   * <p>
   * Returns when the inference has stopped running.
   *
   * @see     #setMaxNumber(Integer)
   * @see     #setMaxTime(Integer)
   * @see     #isContinuable()
   * @see     #setContinuable(boolean)
   */
  void continueQuery();

  /**
   * Identifies redundant clauses in this query.
   *
   * For instance, if one clause isa (isa ?X Dog) and another is (isa ?X GreatDane), that pair is
   * considered redundant. This method provides no guidance as to what can or should be done to
   * resolve the redundancy, and in fact it may be virtually harmless, as Cyc can often solve such a
   * query almost as efficiently as it can solve the more specific clause of the pair.
   *
   * @return  a collection of pairs of any such clauses
   * @throws  KbException                         
   * @throws  SessionCommunicationException       
   * @throws  OpenCycUnsupportedFeatureException  when run against an OpenCyc server
   */
  Collection<Collection<Sentence>> findRedundantClauses()
          throws KbException, SessionCommunicationException, OpenCycUnsupportedFeatureException;

  /**
   * Identifies unconnected clauses in this query. Generally, all clauses of a query will be
   * connected by a chain of variables that connect them together. Queries with unconnected clauses
   * are effectively separate queries, and running queries with disconnected clauses generally
   * results in a cartesian product of the answer sets of the two separate queries.
   *
   * @return  a collection of the arg positions of any such clauses
   * @throws  SessionCommunicationException       if there is a problem communicating with Cyc
   * @throws  OpenCycUnsupportedFeatureException  when run against an OpenCyc server
   */
  Collection<ArgPosition> findUnconnectedClauses()
          throws SessionCommunicationException, OpenCycUnsupportedFeatureException;

  /**
   * Conjoin this sentence with otherQuery, attempting to unify and rename variables. Typically, two
   * different variables will unify into a single variable, causing all the uses of one of the
   * variables to be renamed with the name of the other. In some cases, additional renaming may
   * happen (e.g. if the queries contain mnemonic variables that become more tightly constrained as
   * a result of the unification, a new mnemonic variable may be used in place of both of the
   * original variables).
   *
   * @param   otherQuery  
   * @return  the new query
   * @throws  QueryConstructionException          if there was a problem constructing the new query
   * @throws  SessionCommunicationException       if there is a problem communicating with Cyc
   * @throws  OpenCycUnsupportedFeatureException  when run against an OpenCyc server
   */
  Query merge(Query otherQuery) throws
          QueryConstructionException,
          SessionCommunicationException,
          OpenCycUnsupportedFeatureException;
  
  /**
   * Starts the query.
   *
   * @throws  SessionCommunicationException  if there is a problem communicating with Cyc
   */
  void start() throws SessionCommunicationException;

  /**
   * Issues a request that the query stop immediately.
   *
   * @param   patience  if non-null, the query will be forcibly aborted if it does not stop before 
   *                    this many seconds have elapsed
   */
  void stop(final Integer patience);

  /**
   * Get the Cyc session to be used for this query.
   *
   * @return  a CycSession for this query
   */
  CycSession getCycSession();

  /**
   * Specify that this inference should be retained by Cyc until the Query is closed. This can be
   * called before the query has been started, and must be called before the query has finished
   * running.
   *
   * @see     Query#close()
   */
  void retainInference();

  /**
   * Get the inference identifier for this query.
   *
   * @return  the identifier, or null if inference has not been started
   * @throws  SessionCommunicationException  if there is a problem communicating with Cyc
   */
  InferenceIdentifier getInferenceIdentifier() throws SessionCommunicationException;
  
  /**
   * Returns the number of answers found for this query. For running queries, the value returned by
   * this method may change as additional answers are found.
   *
   * @return  the number of answers found for this query
   */
  int getAnswerCount();

  /**
   * Returns the list of answers for this query. For running queries, the value returned may change
   * on subsequent calls to this method as additional answers are found.
   *
   * @return  the list of answers
   * @throws  SessionCommunicationException  if there is a problem communicating with Cyc
   */
  QueryAnswers<QueryAnswer> getAnswers() throws SessionCommunicationException;

  /**
   * Returns the list of answers for this query. For running queries, the value returned by this
   * method may change as additional answers are found.
   *
   * @param   paraphraser
   * @return  the list of answers
   * @throws  SessionCommunicationException  if there is a problem communicating with Cyc.
   */
  QueryAnswers<ParaphrasedQueryAnswer> getAnswers(Paraphraser paraphraser)
          throws SessionCommunicationException;

  /**
   * Returns the nth answer for this query. For the first answer, n == 0.
   *
   * @param   answerIndex
   * @return  the answer
   * @throws  SessionCommunicationException  if there is a problem communicating with Cyc
   */
  QueryAnswer getAnswer(final int answerIndex) throws SessionCommunicationException;

  /**
   * Returns the nth answer for this query. For the first answer, n == 0.
   *
   * @param   answerIndex
   * @param   paraphraser
   * @return  the answer
   * @throws  SessionCommunicationException if there is a problem communicating with Cyc
   */
  ParaphrasedQueryAnswer getAnswer(final int answerIndex, final Paraphraser paraphraser)
          throws SessionCommunicationException;
  
  /**
   * Get the CycL sentence from the specified answer to this query. Substitutes the set of bindings
   * from answer into the query sentence.
   *
   * @param   answer
   * @return  the answer sentence
   * @throws  KbException
   */
  Sentence getAnswerSentence(QueryAnswer answer) throws KbException;

  /**
   * Forget all results for this query. All settings on the Query are retained, including the query
   * sentence, context, and inference parameters. After a Query has been cleared, it can be re-run,
   * with possibly different results.
   *
   * @return  this Query
   */
  Query clearResults();

  /**
   * Return the InferenceStatus for this Query.
   *
   * @return  the InferenceStatus for this Query.
   */
  InferenceStatus getStatus();

  /**
   * Return the reason why this Query was suspended (if it was).
   *
   * @return  the reason, or null if this Query was not suspended
   * @see     DefaultInferenceSuspendReason for examples
   */
  InferenceSuspendReason getSuspendReason();

  /**
   *
   * @return  true iff this query has been proven true
   * @throws  RuntimeException if the query has open variables
   * @see     com.cyc.query.QueryResultSet#getTruthValue()
   */
  boolean isTrue();

  /**
   * Is this query either True (if a boolean query) or does it have bindings (if non-boolean)
   *
   * @return  True if there are bindings (or it's a true boolean query), false if there are no
   *          bindings (or it's a false boolean query).
   */
  boolean isProvable();

  /**
   * Closes this query's result set, and releases resources on the Cyc server. See
   * {@link com.cyc.query.QueryResultSet#close()} for more details on what happens when a query is
   * closed.
   *
   * <p>It is good practice to always invoke this method explicitly as soon as a Query is no longer
   * needed, as they can take up significant amounts of memory on the Cyc server.
   *
   * @see     com.cyc.query.QueryResultSet#close()
   */
  @Override
  void close();

  /**
   *
   * @return  this query's result set. The QueryResultSet is an object that may be updated 
   *          dynamically for running queries. This contrasts with {@link #getAnswersCyc()} which
   *          returns a static list of the answers at the time is was called.
   * @see     com.cyc.query.QueryResultSet
   */
  QueryResultSet getResultSet();
  
}
