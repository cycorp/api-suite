package com.cyc.query.spi;

/*
 * #%L
 * File: QueryService.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2019 Cycorp, Inc
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
import com.cyc.kb.exception.KbException;
import com.cyc.kb.exception.KbTypeException;
import com.cyc.query.Query;
import com.cyc.query.exception.QueryConstructionException;
import com.cyc.query.parameters.InferenceParameters;
import com.cyc.session.exception.UnsupportedCycOperationException;
import java.util.Map;

/**
 *
 * @author daves
 */
public interface QueryService {

  /**
   * constructs a Query working with the string queryStr.
   * <p>
   * The query is executed in InferencePSC with a default timeout and default inference parameters.
   *
   * @param queryStr the string representing the CycL query
   *
   * @return
   *
   * @throws QueryConstructionException
   * @see com.cyc.query.Query#TIMEOUT
   *
   */
  Query getQuery(String queryStr) throws QueryConstructionException;

  /**
   * Returns a query object defined by queryStr asked in Microtheory ctxStr, with default inference
   * parameters.
   *
   * @param queryStr The query string
   * @param ctxStr   The Microtheory where the query is asked
   *
   * @return
   *
   * @throws QueryConstructionException
   *
   */
  Query getQuery(String queryStr, String ctxStr) throws QueryConstructionException;

  /**
   * Returns a query object defined by queryStr asked in Microtheory ctxStr, with inference
   * parameters, queryParams.
   *
   * @param queryStr    the query string.
   * @param ctxStr      the Microtheory where the query is asked
   * @param queryParams the inference parameters to use for the query. This string should consist of
   *                    a series of keywords followed by the values for those keywords. The keywords
   *                    can be found by looking for the #$sublIdentifier for the desired instance of
   *                    InferenceParameter in the Cyc KB. For example, to limit a query to
   *                    single-depth transformation and to allow at most 5 seconds per query, use
   *                    the string ":max-transformation-depth 1 :max-time 5".
   *
   * @return
   *
   * @throws QueryConstructionException
   */
  Query getQuery(String queryStr, String ctxStr, String queryParams)
          throws QueryConstructionException;

  /**
   *
   * @param sent
   * @param ctx
   * @param params
   *
   * @return
   *
   * @throws QueryConstructionException
   */
  Query getQuery(Sentence sent, Context ctx, InferenceParameters params)
          throws QueryConstructionException;

  /**
   *
   * @param sent
   * @param ctx
   *
   * @return
   *
   * @throws QueryConstructionException
   */
  Query getQuery(Sentence sent, Context ctx) throws QueryConstructionException;

  /**
   * Constructs a Query from a KbIndividual corresponding to a <tt>#$CycLQuerySpecification</tt>.
   *
   * @param id
   *
   * @return the Query specified by <code>id</code>
   *
   * @throws QueryConstructionException       if the specified query term has a sentence whose
   *                                          outermost operator is #$ist and the query is loaded
   *                                          from a Cyc server with a system level under 10.154917
   *                                          (Nov. 2014). A workaround is to edit the query in the
   *                                          KB, removing the #$ist from the query's sentence, and
   *                                          specifying it as the query mt using
   * <tt>#$microtheoryParameterValueInSpecification</tt>.
   * @throws KbException                      if <code>idStr</code> does not identify a KbIndividual
   * @throws UnsupportedCycOperationException when run against ResearchCyc 4.0q and earlier
   *
   * @see com.cyc.Cyc.Constants#CYCL_QUERY_SPECIFICATION
   */
  Query getQuery(final KbIndividual id)
          throws QueryConstructionException, KbException, UnsupportedCycOperationException;

  /**
   * Returns a new Query loaded from a term in Cyc specifying its properties. Terms in the specified
   * query can be replaced with others by providing a non-empty <code>indexicals</code> map.
   *
   * @param id         the Cyc term
   * @param indexicals a map of substitutions to be made
   *
   * @return the Query specified by <code>id</code>
   *
   * @throws QueryConstructionException       if the specified query term has a sentence whose
   *                                          outermost operator is #$ist and the query is loaded
   *                                          from a Cyc server with a system level under 10.154917
   *                                          (Nov. 2014). A workaround is to edit the query in the
   *                                          KB, removing the #$ist from the query's sentence, and
   *                                          specifying it as the query mt using
   *                                          #$microtheoryParameterValueInSpecification.
   * @throws KbException                      if <code>idStr</code> does not identify a KbIndividual
   * @throws UnsupportedCycOperationException when run against ResearchCyc 4.0q and earlier
   */
  Query getQuery(KbIndividual id, Map<KbObject, Object> indexicals)
          throws QueryConstructionException, KbException, UnsupportedCycOperationException;

  /**
   * Returns a Query object defined by a CycLQuerySpecification term, and substitutes in relevant
   * values from the indexicals Map.
   *
   * @param idStr      The instance of CycLQuerySpecification
   * @param indexicals A map from terms in the query (as loaded from the KB) to the actual values
   *                   that should be queried with
   *
   * @return a Query object defined by idStr
   *
   * @throws QueryConstructionException       if the specified query term has a sentence whose
   *                                          outermost operator is #$ist and the query is loaded
   *                                          from a Cyc server with a system level under 10.154917
   *                                          (Nov. 2014). A workaround is to edit the query in the
   *                                          KB, removing the #$ist from the query's sentence, and
   *                                          specifying it as the query mt using
   *                                          #$microtheoryParameterValueInSpecification.
   * @throws KbTypeException                  if <code>idStr</code> does not identify a KbIndividual
   * @throws UnsupportedCycOperationException when run against ResearchCyc 4.0q and earlier
   */
  Query getQuery(String idStr, Map<String, String> indexicals)
          throws QueryConstructionException, KbTypeException, UnsupportedCycOperationException;

  /**
   * Closes all unclosed queries.
   *
   * @return the number of queries that were closed
   */
  int closeAllUnclosedQueries();

}
