package com.cyc.query;

/*
 * #%L
 * File: QueryFactory.java
 * Project: Core API Object Factories
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
import com.cyc.core.service.CoreServicesLoader;
import com.cyc.kb.Context;
import com.cyc.kb.KbIndividual;
import com.cyc.kb.KbObject;
import com.cyc.kb.Sentence;
import com.cyc.kb.exception.KbException;
import com.cyc.kb.exception.KbTypeException;
import com.cyc.query.exception.QueryConstructionException;
import com.cyc.query.spi.QueryFactoryService;
import com.cyc.session.exception.UnsupportedCycOperationException;

import java.util.Map;

/**
 *
 * @author daves
 */
public class QueryFactory {

  // Initialization
  
  private static final QueryFactoryService SERVICE;
  
  static {
    SERVICE = CoreServicesLoader.getQueryFactoryService();
  }
  
  private QueryFactory() {}

  private static QueryFactoryService getInstance() {
    return SERVICE;
  }
  
  
  // Public static methods

  /**
   * constructs a Query working with the string queryStr.
   * <p>
   * The query is executed in InferencePSC with a default timeout and default inference parameters.
   *
   * @param queryStr the string representing the CycL query
   * @return 
   * @see com.cyc.query.Query#TIMEOUT
   *
   * @throws QueryConstructionException
   */
  public static Query getQuery(String queryStr) throws QueryConstructionException {
    return getInstance().getQuery(queryStr);
  }

  /**
   * Returns a query object defined by queryStr asked in Microtheory ctxStr, with default inference
   * parameters.
   *
   * @param queryStr The query string.
   * @param ctxStr The Microtheory where the query is asked.
   * @return 
   *
   * @throws QueryConstructionException
   *
   */
  public static Query getQuery(String queryStr, String ctxStr) throws QueryConstructionException {
    return getInstance().getQuery(queryStr, ctxStr);
  }

  /**
   * Returns a query object defined by queryStr asked in Microtheory ctxStr, with inference
   * parameters, queryParams.
   *
   * @param queryStr The query string.
   * @param ctxStr The Microtheory where the query is asked.
   * @param queryParams The inference parameters to use for the query. This string should consist of
   * a series of keywords followed by the values for those keywords. The keywords can be found by
   * looking for the #$sublIdentifier for the desired instance of InferenceParameter in the Cyc KB.
   * For example, to limit a query to single-depth transformation and to allow at most 5 seconds per
   * query, use the string ":max-transformation-depth 1 :max-time 5".
   * @return 
   *
   * @throws QueryConstructionException
   *
   */
  public static Query getQuery(String queryStr, String ctxStr, String queryParams)
          throws QueryConstructionException {
    return getInstance().getQuery(queryStr, ctxStr, queryParams);
  }

  /**
   *
   * @param sent
   * @param ctx
   * @param params
   * @return 
   * @throws com.cyc.query.exception.QueryConstructionException
   */
  public static Query getQuery(Sentence sent, Context ctx, InferenceParameters params)
          throws QueryConstructionException {
    return getInstance().getQuery(sent, ctx, params);
  }

  /**
   *
   * @param sent
   * @param ctx
   * @return 
   * @throws QueryConstructionException
   */
  public static Query getQuery(Sentence sent, Context ctx) throws QueryConstructionException {
    return getInstance().getQuery(sent, ctx);
  }

  /**
   * Constructs a Query from a KbIndividual corresponding to #$CycLQuerySpecification.
   *
   * @param id
   * @return 
   * 
   * @throws QueryConstructionException
   * <p/>
   * <b>Note:</b> {@link QueryConstructionException} is thrown if the specified query term has a
   * sentence whose outermost operator is #$ist and the query is loaded from a Cyc server with a
   * system level under 10.154917 (Nov. 2014). A workaround is to edit the query in the KB, removing
   * the #$ist from the query's sentence, and specifying it as the query mt using
   * #$microtheoryParameterValueInSpecification.
   * 
   * @throws KbException if <code>idStr</code> does not identify a KbIndividual.
   *
   * @throws UnsupportedCycOperationException when run against ResearchCyc 4.0q and earlier.
   */
  public static Query getQuery(final KbIndividual id) 
          throws QueryConstructionException, KbException, UnsupportedCycOperationException {
    return getInstance().getQuery(id);
  }

  /**
   * Returns a new Query loaded from a term in Cyc specifying its properties. Terms in the specified
   * query can be replaced with others by providing a non-empty <code>indexicals</code> map.
   *
   * @param id the Cyc term
   * @param indexicals A map of substitutions to be made.
   * 
   * @throws QueryConstructionException
   * <p/>
   * <b>Note:</b> {@link QueryConstructionException} is thrown if the specified query term has a
   * sentence whose outermost operator is #$ist and the query is loaded from a Cyc server with a
   * system level under 10.154917 (Nov. 2014). A workaround is to edit the query in the KB, removing
   * the #$ist from the query's sentence, and specifying it as the query mt using
   * #$microtheoryParameterValueInSpecification.
   * 
   * @throws KbException if <code>idStr</code> does not identify a KbIndividual.
   *
   * @throws UnsupportedCycOperationException when run against ResearchCyc 4.0q and earlier.
   * 
   * @return the Query specified by <code>id</code>
   */
  public static Query getQuery(KbIndividual id, Map<KbObject, Object> indexicals) 
          throws QueryConstructionException, KbException, UnsupportedCycOperationException {
    return getInstance().getQuery(id, indexicals);
  }

  /**
   * Returns a Query object defined by a CycLQuerySpecification term, and substitutes in relevant
   * values from the indexicals Map.
   *
   * @param idStr The instance of CycLQuerySpecification
   * @param indexicals A map from terms in the query (as loaded from the KB) to the actual values
   * that should be queried with.
   * @throws QueryConstructionException
   * <p/>
   * <b>Note:</b> {@link QueryConstructionException} is thrown if the specified query term has a
   * sentence whose outermost operator is #$ist and the query is loaded from a Cyc server with a
   * system level under 10.154917 (Nov. 2014). A workaround is to edit the query in the KB, removing
   * the #$ist from the query's sentence, and specifying it as the query mt using
   * #$microtheoryParameterValueInSpecification.
   *
   * @throws KbTypeException if <code>idStr</code> does not identify a KbIndividual.
   *
   * @throws UnsupportedCycOperationException when run against ResearchCyc 4.0q and earlier.
   * 
   * @return a Query object defined by idStr
   */
  public static Query getQuery(String idStr, Map<String, String> indexicals)
          throws QueryConstructionException, KbTypeException, UnsupportedCycOperationException {
    return getInstance().getQuery(idStr, indexicals);
  }

  public static int closeAllUnclosedQueries() {
    return getInstance().closeAllUnclosedQueries();
  }
}
