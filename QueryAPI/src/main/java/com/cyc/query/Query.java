package com.cyc.query;

/*
 * #%L
 * File: Query.java
 * Project: Query API
 * %%
 * Copyright (C) 2013 - 2015 Cycorp, Inc.
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
import com.cyc.query.exception.QueryConstructionException;
import com.cyc.base.CycAccess;
import com.cyc.base.CycAccessManager;
import com.cyc.base.CycConnectionException;
import com.cyc.base.cycobject.ArgPosition;
import com.cyc.base.cycobject.CycList;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.CycSymbol;
import com.cyc.base.cycobject.CycVariable;
import com.cyc.base.cycobject.DenotationalTerm;
import com.cyc.base.cycobject.ELMt;
import com.cyc.base.cycobject.FormulaSentence;
import com.cyc.base.inference.DisjunctionFreeELVarsPolicy;
import com.cyc.base.inference.InferenceAnswer;
import com.cyc.base.inference.InferenceAnswerLanguage;
import com.cyc.base.inference.InferenceIdentifier;
import com.cyc.base.inference.InferenceMode;
import com.cyc.base.inference.InferenceParameterGetter;
import com.cyc.base.inference.InferenceParameterSetter;
import com.cyc.base.inference.InferenceParameters;
import com.cyc.base.inference.InferenceResultSet;
import com.cyc.base.inference.InferenceStatus;
import com.cyc.base.inference.InferenceSuspendReason;
import com.cyc.base.inference.InferenceWorker;
import com.cyc.base.inference.InferenceWorkerListener;
import com.cyc.base.inference.ProblemReusePolicy;
import com.cyc.base.inference.ProofValidationMode;
import com.cyc.base.inference.ResultUniqueness;
import com.cyc.base.inference.TransitiveClosureMode;
import com.cyc.base.inference.metrics.InferenceMetrics;
import com.cyc.base.inference.metrics.InferenceMetricsValues;
import com.cyc.baseclient.CycObjectFactory;
import com.cyc.baseclient.api.SubLAPIHelper;
import com.cyc.baseclient.cycobject.ArgPositionImpl;
import com.cyc.baseclient.cycobject.CycFormulaSentence;
import com.cyc.baseclient.inference.CycBackedInferenceAnswer;
import com.cyc.baseclient.inference.DefaultInferenceStatus;
import com.cyc.baseclient.inference.DefaultInferenceSuspendReason;
import com.cyc.baseclient.inference.DefaultInferenceWorkerSynch;
import com.cyc.baseclient.inference.DefaultResultSet;
import com.cyc.baseclient.inference.SpecifiedInferenceAnswerIdentifier;
import com.cyc.baseclient.inference.ResultSetInferenceAnswer;
import com.cyc.baseclient.inference.metrics.InferenceMetricsValuesImpl;
import com.cyc.baseclient.inference.params.DefaultInferenceParameters;
import com.cyc.baseclient.inference.params.SpecifiedInferenceParameters;
import com.cyc.baseclient.parser.CycLParserUtil;
import com.cyc.kb.Context;
import com.cyc.kb.KBIndividual;
import com.cyc.kb.KBObject;
import com.cyc.kb.Sentence;
import com.cyc.kb.Variable;
import com.cyc.kb.client.Constants;
import com.cyc.kb.client.ContextImpl;
import com.cyc.kb.client.KBIndividualImpl;
import com.cyc.kb.client.KBTermImpl;
import com.cyc.kb.client.KBUtils;
import com.cyc.kb.client.SentenceImpl;
import com.cyc.kb.client.VariableImpl;
import com.cyc.kb.config.KBAPIConfiguration;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KBApiException;
import com.cyc.kb.exception.KBTypeException;
import com.cyc.query.exception.QueryApiRuntimeException;
import com.cyc.session.CycSession;
import com.cyc.session.CycSessionManager;
import com.cyc.session.SessionApiException;
import com.cyc.session.SessionCommunicationException;
import com.cyc.session.SessionConfigurationException;
import com.cyc.session.SessionInitializationException;
import java.io.Closeable;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

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
 * when you are done with it, which will free up any lingering associated
 * inference resources on the Cyc image. Queries are also closed when their
 * {@link #finalize()} method is invoked, notably when they are garbage
 * collected.
 * </ol>
 *
 * @author Vijay Raj
 * @author David Baxter
 *
 */
public class Query implements Closeable, InferenceParameterSetter, InferenceParameterGetter {

  private static final int TIMEOUT = 0;

  private static final CycSymbol RETURN
          = CycObjectFactory.makeCycSymbol(":RETURN");
  private static final CycSymbol COMPUTE_ANSWER_JUSTIFICATIONS
          = CycObjectFactory.makeCycSymbol(":COMPUTE-ANSWER-JUSTIFICATIONS?");
  private Context ctx;
  private InferenceParameters params;
  private FormulaSentence querySentence = null;
  private final Set<QueryListener> listeners = new HashSet<QueryListener>();
  final private CycSession session;
  final private CycAccess cyc;
  private KBIndividual id;
  private final Set<Category> categories = new HashSet<Category>();
  private boolean retainInference = false;
  private final QueryInference inference = new QueryInference();

  /**
   * constructs a Query working with the string queryStr.
   * <p>
   * The query is executed in InferencePSC with a default timeout and default
   * inference parameters.
   *
   * @param queryStr	the string representing the CycL query
   * @see com.cyc.query.Query#TIMEOUT
   *
   * @throws QueryConstructionException
   */
  public Query(String queryStr) throws QueryConstructionException {
    this(constructSentence(queryStr), Constants.inferencePSCMt());
  }

  /**
   * Returns a query object defined by queryStr asked in Microtheory ctxStr,
   * with default inference parameters.
   *
   * @param queryStr The query string.
   * @param ctxStr The Microtheory where the query is asked.
   *
   * @throws QueryConstructionException
   *
   */
  public Query(String queryStr, String ctxStr) throws QueryConstructionException {
    this(queryStr, ctxStr, "");
  }

  /**
   * Returns a query object defined by queryStr asked in Microtheory ctxStr,
   * with inference parameters, queryParams.
   *
   * @param queryStr The query string.
   * @param ctxStr The Microtheory where the query is asked.
   * @param queryParams The inference parameters to use for the query. This
   * string should consist of a series of keywords followed by the values for
   * those keywords. The keywords can be found by looking for the
   * #$sublIdentifier for the desired instance of InferenceParameter in the Cyc
   * KB. For example, to limit a query to single-depth transformation and to
   * allow at most 5 seconds per query, use the string
   * ":max-transformation-depth 1 :max-time 5".
   *
   * @throws QueryConstructionException
   *
   */
  public Query(String queryStr, String ctxStr, String queryParams) throws QueryConstructionException {
    this(constructSentence(queryStr), constructContext(ctxStr), constructParams(queryParams));
  }

  private static ContextImpl constructContext(String ctxStr) throws QueryConstructionException {
    try {
      return ContextImpl.get(ctxStr);
    } catch (Exception ex) {
      throw new QueryConstructionException(ex);
    }
  }

  private static DefaultInferenceParameters constructParams(String queryParams) throws QueryConstructionException {
    try {
      return queryParams.isEmpty() ? null : new DefaultInferenceParameters(
              getAccess(), queryParams);
    } catch (Exception ex) {
      throw new QueryConstructionException(ex);
    }
  }

  private static CycFormulaSentence constructSentence(String queryStr) throws QueryConstructionException {
    try {
      return CycLParserUtil.parseCycLSentence(queryStr, true,
              getAccess());
    } catch (Exception ex) {
      throw new QueryConstructionException(ex);
    }
  }

  protected static CycAccess getAccess() throws SessionConfigurationException, SessionCommunicationException, SessionInitializationException {
    return CycAccessManager.getCurrentAccess();
  }

  /**
   * Returns a new query with the specified context, sentence, and parameters.
   *
   * @param sent The CycL sentence to be queried
   * @param ctx the Context in which to run the query
   * @param params
   *
   */
  @Deprecated
  public Query(FormulaSentence sent, Context ctx, InferenceParameters params) throws QueryConstructionException {
    this();
    this.ctx = ctx;
    this.querySentence = sent;
    if (params == null) {
      initializeParams();
    } else {
      this.params = params;
    }
    unclosedQueries.add(this);
  }

  /**
   *
   * @param sent
   * @param ctx
   * @param params
   */
  public Query(Sentence sent, Context ctx, InferenceParameters params) throws QueryConstructionException {
    this((FormulaSentence) sent.getCore(), ctx, params);
  }

  /**
   * Returns a new query with the specified context and sentence, and default
   * parameters.
   *
   * @param sent The CycL sentence to be queried
   * @param ctx the Context in which to run the query
   *
   * @throws QueryConstructionException
   *
   */
  @Deprecated
  public Query(FormulaSentence sent, Context ctx) throws QueryConstructionException {
    this(sent, ctx, null);
  }

  /**
   *
   * @param sent
   * @param ctx
   * @throws QueryConstructionException
   */
  public Query(Sentence sent, Context ctx) throws QueryConstructionException {
    this((FormulaSentence) sent.getCore(), ctx);
  }

  /**
   * Returns a new query with the specified context, sentence, and parameters.
   *
   * @param sent The CycL sentence to be queried
   * @param ctx the Context in which to run the query
   * @param params
   * @throws QueryConstructionException
   *
   * @deprecated Use {@link #Query(FormulaSentence,Context,InferenceParameters)}
   *
   */
  @Deprecated
  public Query(FormulaSentence sent, ELMt ctx, InferenceParameters params) throws QueryConstructionException {
    this(sent, constructContext(ctx), params);
  }

  private static ContextImpl constructContext(CycObject ctx) throws QueryConstructionException {
    try {
      return ContextImpl.get(ctx);
    } catch (Exception ex) {
      throw new QueryConstructionException(ex);
    }
  }

  /**
   * Construct a new Query with the specified sentence, context, and inference
   * parameters.
   *
   * @param sent
   * @param ctx
   * @param params
   * @throws QueryConstructionException
   */
  public Query(Sentence sent, ELMt ctx, InferenceParameters params) throws QueryConstructionException {
    this((FormulaSentence) sent.getCore(), ctx, params);
  }

  /**
   * Returns a new query with the specified context and sentence, and default
   * parameters.
   *
   * @param sent The CycL sentence to be queried
   * @param ctx the Context in which to run the query
   *
   * @throws QueryConstructionException
   * @deprecated Use {@link #Query(FormulaSentence,Context)}
   *
   */
  @Deprecated
  public Query(FormulaSentence sent, ELMt ctx) throws QueryConstructionException {
    this(sent, constructContext(ctx));
  }

  /**
   * Construct a new Query with the specified sentence and context.
   *
   * @param sent
   * @param ctx
   * @throws QueryConstructionException
   */
  public Query(Sentence sent, ELMt ctx) throws QueryConstructionException {
    this((FormulaSentence) sent.getCore(), ctx);
  }

  /**
   * Constructs a Query from a CycLQuerySpecification term.
   *
   * @param id
   * @throws QueryConstructionException
   * @throws KBApiException
   * <p/>
   * <b>Note:</b> {@link QueryConstructionException} is thrown if the specified
   * query term has a sentence whose outermost operator is #$ist and the query
   * is loaded from a Cyc server with a system level under 10.154917 (Nov.
   * 2014). A workaround is to edit the query in the KB, removing the #$ist from
   * the query's sentence, and specifying it as the query mt using
   * #$microtheoryParameterValueInSpecification.
   * @deprecated New code should use {@link Query#Query(com.cyc.kb.KBIndividual)
   * }.
   *
   */
  public Query(final DenotationalTerm id) throws QueryConstructionException,
          KBApiException {
    this(constructQueryIdTerm(id));
  }

  private static KBIndividualImpl constructQueryIdTerm(final DenotationalTerm id)
          throws QueryConstructionException {
    try {
      return KBIndividualImpl.get(id.cyclify());
    } catch (Exception ex) {
      throw new QueryConstructionException(ex);
    }
  }

  /**
   * Constructs a Query from a CycLQuerySpecification KBIndividualImpl. Use of
   * this constructor is equivalent to calling
   * {@link Query#load(com.cyc.kb.KBIndividual)}.
   *
   * @param id
   * @throws QueryConstructionException
   * @throws KBApiException
   * <p/>
   * <b>Note:</b> {@link QueryConstructionException} is thrown if the specified
   * query term has a sentence whose outermost operator is #$ist and the query
   * is loaded from a Cyc server with a system level under 10.154917 (Nov.
   * 2014). A workaround is to edit the query in the KB, removing the #$ist from
   * the query's sentence, and specifying it as the query mt using
   * #$microtheoryParameterValueInSpecification.
   */
  public Query(final KBIndividual id) throws QueryConstructionException, KBApiException {
    this();
    final Query tempQuery = load(id);
    this.setQuerySentence(tempQuery.getQuerySentenceCyc());
    this.setContext(tempQuery.getContext());
    this.setInferenceParameters(tempQuery.getInferenceParameters());
    unclosedQueries.add(this);
  }

  /**
   * Run this query and return the results.
   *
   * @return the results of running the query.
   * @throws QueryApiRuntimeException if an exception is thrown during
   * inference.
   *
   */
  public QueryResultSet performInference() throws QueryApiRuntimeException {
    try {
      return inference.performInference();
    } catch (Exception e) {
      throw new QueryApiRuntimeException("Exception thrown during inference.", e);
    }
  }

  /**
   * Get the Cyc term that defines this query. To change the Id of an existing
   * query, see {@link #saveAs(String)}
   *
   * @return the id term.
   */
  public KBIndividual getId() {
    return id;
  }

  /**
   * Set the Cyc term that underlies this query in the KB. Note that setting a
   * new ID will not be reflected immediately in the KB. Instead, the change
   * will only be reflected in the KB when the query is saved. Setting a new Id
   * on a query is similar to calling {@link #saveAs(String)}, in that neither
   * method will change the original Query specification in the KB.
   *
   * In general, this method should be avoided. <code>Query</code> is saved.
   *
   * @param id
   */
  void setId(KBIndividual id) {
    this.id = id;
  }

  /**
   * Returns a Query object defined by a query term (i.e. an instance of
   * #$CycLQuerySpecification). Use of this method is equivalent to calling
   * {@link Query#Query(com.cyc.kb.KBIndividual)}.
   *
   *
   * @param id an instance of #$CycLQuerySpecification
   * @return a Query object defined by queryTerm
   * @throws KBApiException if there is a problem creating the Query object
   * @throws QueryConstructionException if there is some other kind of problem
   * <p/>
   * <b>Note:</b> {@link QueryConstructionException} is thrown if the specified
   * query term has a sentence whose outermost operator is #$ist and the query
   * is loaded from a Cyc server with a system level under 10.154917 (Nov.
   * 2014). A workaround is to edit the query in the KB, removing the #$ist from
   * the query's sentence, and specifying it as the query mt using
   * #$microtheoryParameterValueInSpecification.
   */
  public static Query load(KBIndividual id) throws QueryConstructionException,
          KBApiException {
    try {
      return new QueryReader().queryFromTerm(id);
    } catch (JAXBException ex) {
      throw new RuntimeException(ex);
    } catch (KBApiException ex) {
      throw ex;
    } catch (NullPointerException ex) {
      throw new QueryConstructionException("Could not load a query for " + id, ex);
    } catch (Exception ex) {
      throw new QueryConstructionException(ex);
    }
  }

  /**
   * Returns a Query object defined by a CycLQuerySpecification term, and
   * substitutes in relevant values from the indexicals Map. This is the
   * equivalent of calling {@link #load(com.cyc.kb.KBIndividual)} and then
   * calling {@link #substituteTermsWithCycObject(java.util.Map)} on it.
   *
   *
   * @param id The instance of CycLQuerySpecification
   * @param indexicals A map from terms in the query (as loaded from the KB) to
   * the actual values that should be queried with.
   *
   * @return a Query object defined by id
   * @throws Exception
   * <p/>
   * <b>Note:</b> {@link QueryConstructionException} is thrown if the specified
   * query term has a sentence whose outermost operator is #$ist and the query
   * is loaded from a Cyc server with a system level under 10.154917 (Nov.
   * 2014). A workaround is to edit the query in the KB, removing the #$ist from
   * the query's sentence, and specifying it as the query mt using
   * #$microtheoryParameterValueInSpecification.
   *
   * @deprecated Use {@link #load(KBIndividual,Map)}
   */
  @Deprecated
  public static Query loadCycObjectMap(KBIndividual id, Map<CycObject, Object> indexicals) throws Exception {

    Query q = load(id);
    replaceIndexicalsWithCycObject(q, indexicals);
    return q;
  }

  /**
   * Returns a new Query loaded from a term in Cyc specifying its properties.
   * Terms in the specified query can be replaced with others by providing a
   * non-empty <code>indexicals</code> map.
   *
   * @param id the Cyc term
   * @param indexicals A map of substitutions to be made.
   * @return
   * @throws Exception
   * <p/>
   * <b>Note:</b> {@link QueryConstructionException} is thrown if the specified
   * query term has a sentence whose outermost operator is #$ist and the query
   * is loaded from a Cyc server with a system level under 10.154917 (Nov.
   * 2014). A workaround is to edit the query in the KB, removing the #$ist from
   * the query's sentence, and specifying it as the query mt using
   * #$microtheoryParameterValueInSpecification.
   */
  public static Query load(KBIndividual id, Map<KBObject, Object> indexicals) throws Exception {
    Map<CycObject, Object> kboToCoMap = KBUtils.convertKBObjectMapToCoObjectMap(indexicals);

    Query q = loadCycObjectMap(id, kboToCoMap);
    return q;
  }

  /**
   * Returns a Query object defined by a CycLQuerySpecification term, and
   * substitutes in relevant values from the indexicals Map.
   *
   * @param idStr The instance of CycLQuerySpecification
   * @param indexicals A map from terms in the query (as loaded from the KB) to
   * the actual values that should be queried with.
   * @throws QueryConstructionException
   * <p/>
   * <b>Note:</b> {@link QueryConstructionException} is thrown if the specified
   * query term has a sentence whose outermost operator is #$ist and the query
   * is loaded from a Cyc server with a system level under 10.154917 (Nov.
   * 2014). A workaround is to edit the query in the KB, removing the #$ist from
   * the query's sentence, and specifying it as the query mt using
   * #$microtheoryParameterValueInSpecification.
   *
   * @throws KBTypeException if <code>idStr</code> does not identify a
   * KBIndividual.
   *
   * @return a Query object defined by idStr
   */
  public static Query load(String idStr, Map<String, String> indexicals)
          throws QueryConstructionException, KBTypeException {
    try {
      Query q = load(KBIndividualImpl.get(idStr));
      replaceIndexicals(indexicals, q);
      return q;
    } catch (KBApiException e) {
      throw new QueryApiRuntimeException("Exception thrown while trying to load " + idStr, e);
    }
  }

  /**
   * Substitute specified terms for specified indexicals in the query sentence.
   *
   * @param indexicals - a map from indexicals to their replacements.
   */
  @Deprecated
  public void substituteTermsWithCycObject(Map<CycObject, Object> indexicals) {
    querySentence.applySubstitutionsDestructive(indexicals);
  }

  /**
   *
   * @param indexicals
   */
  public void substituteTerms(Map<KBObject, Object> indexicals) {
    Map<CycObject, Object> kboToCoMap = KBUtils.convertKBObjectMapToCoObjectMap(indexicals);
    querySentence.applySubstitutionsDestructive(kboToCoMap);
  }

  @Deprecated
  private static void replaceIndexicalsWithCycObject(Query q,
          Map<CycObject, Object> indexicals) {
    try {
      FormulaSentence cfs = q.getQuerySentenceCyc().treeSubstitute(
              getAccess(), indexicals);
      q.setQuerySentence(cfs);
      q.substituteTermsWithCycObject(indexicals);
    } catch (Exception e) {
      throw new QueryApiRuntimeException("Exception thrown during indexical replacement.", e);
    }
  }

  private static void replaceIndexicals(Query q, Map<KBObject, Object> indexicals) {
    Map<CycObject, Object> kboToCoMap = KBUtils.convertKBObjectMapToCoObjectMap(indexicals);
    replaceIndexicalsWithCycObject(q, kboToCoMap);
  }

  private static void replaceIndexicals(Map<String, String> indexicals, Query q) {
    Map<CycObject, Object> idx = new HashMap<CycObject, Object>();
    for (Map.Entry<String, String> kv : indexicals.entrySet()) {
      //this.indexicals.put(cyc.getKnownFortByName(kv.getKey()), CycObjectFactory.makeCycVariable(kv.getValue()));
      idx.put((CycObject) getCycObject(kv.getKey()), getCycObject(kv.getValue()));
    }
    replaceIndexicalsWithCycObject(q, idx);
  }

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
   */
  public void registerRequiredSKSModules() {
    try {
      getCycAccess().converse().converseVoid(
              "(ensure-sksi-modules-needed " + getId().stringApiValue() + ")");
    } catch (Exception e) {
      throw new QueryApiRuntimeException("Exception thrown during SKS module registration.", e);
    }
  }

  private static Object getCycObject(String str) {
    if (str.startsWith("?")) {
      return CycObjectFactory.makeCycVariable(str);
    } else if (str.startsWith(":")) {
      return CycObjectFactory.makeCycSymbol(str);
    } else {
      try {
        return getAccess().getLookupTool().getKnownFortByName(getAccess().cyclifyString(
                str));
      } catch (Exception e) {
        return str;
      }
    }
  }

  /**
   * Saves this Query as the term which is its current ID.
   *
   * @see Query#saveAs(String)
   * @see Query#getId()
   */
  public void save() {
    final String fn = KBAPIConfiguration.getShouldTranscriptOperations()
            ? "update-kbq-definition" : "update-kbq-definition-silent";
    try {
      final DenotationalTerm idCycTerm = (DenotationalTerm) getCycAccess().getLookupTool().getKnownFortByName(
              getId().stringApiValue());
      final String command = SubLAPIHelper.makeSubLStmt(fn, idCycTerm,
              getQuerySentenceCyc(), getContext().asELMt(), params);
      getCycAccess().converse().converseVoid(command);
    } catch (Exception e) {
      throw new QueryApiRuntimeException("Exception thrown while saving query.", e);
    }
  }

  /**
   * Saves this Query as a new query term with the specified name.
   *
   * @param name The name by which to save the query.
   *
   * @return the new term
   * @throws com.cyc.base.CycConnectionException
   * @throws com.cyc.query.exception.QueryConstructionException
   * @see Query#save()
   */
  public KBIndividual saveAs(String name) throws KBApiException, CycConnectionException,
          QueryConstructionException {
    if (KBTermImpl.existsAsType(name)) {
      throw new CreateException("The name " + name + " is already used.");
    }
    KBIndividual newID = KBIndividualImpl.findOrCreate(name);
    newID.instantiates(QueryApiConstants.getInstance().CYCL_QUERY_SPECIFICATION, Constants.uvMt());
    setId(newID);
    save();
    return newID;
  }

  /**
   * Returns the categories to which this query belongs. Categories are
   * associated with queries via
   * {@link #addCategory(com.cyc.query.Query.Category)}.
   *
   * @return the categories to which this query belongs.
   */
  public Collection<? extends Category> getCategories() {
    return Collections.unmodifiableCollection(categories);
  }

  /**
   * Add a new category to which this query belongs.
   *
   * @param category
   */
  public void addCategory(Category category) {
    categories.add(category);
  }

  /**
   * Get the inference identifier for this query.
   *
   * @return the identifier, or null if inference has not been started.
   * @throws CycConnectionException if there is a problem communicating with
   * Cyc.
   */
  public InferenceIdentifier getInferenceIdentifier() throws CycConnectionException {
    return inference.getInferenceIdentifier();
  }

  /**
   * Get the metrics to collect for this Query.
   *
   * @return the metrics
   *
   * @see com.cyc.base.inference.InferenceParameters#getMetrics()
   */
  @Override
  public InferenceMetrics getMetrics() {
    return getInferenceParameters().getMetrics();
  }

  /**
   * Get the metrics values for this Query.
   *
   * @throws CycConnectionException if there is a problem communicating with
   * Cyc.
   * @return the metrics values.
   */
  public InferenceMetricsValues getMetricsValues() throws CycConnectionException {
    return InferenceMetricsValuesImpl.fromInference(getInferenceIdentifier());
  }

  /**
   * Return the inference parameters for this query.
   *
   * @return the inference parameters.
   */
  public synchronized InferenceParameters getInferenceParameters() {
    if (params == null) {
      initializeParams();
    }
    return params;
  }

  /**
   * Adds a listener to this query.
   *
   * @param listener
   * @return this query.
   */
  public Query addListener(QueryListener listener) {
    listeners.add(listener);
    return this;
  }

  /**
   * Designates var as a variable to return bindings for.
   *
   * @param var
   * @return this query.
   * @throws IllegalArgumentException if var is not found in this query.
   * @throws IllegalStateException if query has already been started.
   */
  public Query addQueryVariable(Variable var) {
    Query q = addQueryVariable((CycVariable) var.getCore());
    return q;
  }

  private Query addQueryVariable(CycVariable var) {
    if (!querySentence.treeContains(var)) {
      throw new IllegalArgumentException(
              var + " is not contained in " + querySentence);
    }
    if (inference.hasBeenStarted) {
      throw new IllegalStateException("Query has already been started.");
    }
    if (!getQueryVariablesCyc().contains(var)) {
      querySentence.existentiallyUnbind(var);
    }
    return this;
  }

  /**
   * Bind a query variable to a specified value. All occurrences of the variable
   * in this query's sentence will be replaced with the specified value.
   *
   * @param var must be a query variable in this query.
   * @param replacement the value to substitute for var.
   */
  public void bindVariable(Variable var, Object replacement) {
    bindVariable((CycVariable) var.getCore(), replacement);
  }

  private void bindVariable(CycVariable var, Object replacement) {
    if (!getQueryVariablesCyc().contains(var)) {
      throw new IllegalArgumentException(
              var + " is not a query variable in " + getQuerySentenceCyc());
    }
    if (replacement instanceof KBObject) {
      replacement = ((KBObject) replacement).getCore();
    }
    getQuerySentenceCyc().substituteDestructive(var, replacement);
  }

  /**
   * Bind a query variable to a specified value.
   *
   * @param varName The name of the variable, with or without the '?' prefix.
   * @param replacement
   */
  public void bindVariable(String varName, Object replacement) {
    bindVariable(CycObjectFactory.makeCycVariable(varName), replacement);
  }

  /**
   * Designates var as a variable to <i>not</i> return bindings for.
   *
   * @param var
   * @return this Query
   * @throws IllegalArgumentException if var is not found in this query.
   * @throws IllegalStateException if query has already been started.
   */
  public Query removeQueryVariable(Variable var) {
    Query q = removeQueryVariable((CycVariable) var.getCore());
    return q;
  }

  private Query removeQueryVariable(CycVariable var) {
    if (!querySentence.treeContains(var)) {
      throw new IllegalArgumentException(
              var + " is not contained in " + querySentence);
    }
    if (inference.hasBeenStarted) {
      throw new IllegalStateException("Query has already been started.");
    }
    if (getQueryVariablesCyc().contains(var)) {
      querySentence.existentiallyBind(var);
    }
    return this;
  }

  /**
   * Return a Collection of the variables in this query for which bindings are
   * sought. Note that this is a copy of the variables, and modification of the
   * returned value will not result in modifications of the underlying Query.
   *
   * @return a Collection of the variables in this query for which bindings are
   * to be sought.
   * @throws KBApiException
   */
  public java.util.Collection<Variable> getQueryVariables() throws KBApiException {
    java.util.Collection<CycVariable> cycvars = getQueryVariablesCyc();
    java.util.Collection<Variable> vars = new HashSet<Variable>();
    for (CycVariable v : cycvars) {
      vars.add(new VariableImpl(v));
    }
    return vars;
  }

  private Collection<CycVariable> getQueryVariablesCyc() {
    return querySentence.findQueryableVariables();
  }

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
  public void continueQuery() {
    try {
      inference.continueInference();
    } catch (Exception e) {
      throw new QueryApiRuntimeException("Exception thrown while continuing query.", e);
    }
  }

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
   * @throws com.cyc.base.CycConnectionException
   */
  @Deprecated
  public Collection<Collection<FormulaSentence>> findRedundantClausesCFS()
          throws CycConnectionException {
    final Set<Collection<FormulaSentence>> clausePairs = new HashSet<Collection<FormulaSentence>>();
    for (final Object obj : getCycAccess().converse().converseList(SubLAPIHelper.makeSubLStmt(
            "find-redundant-literals",
            querySentence, ctx.asELMt()))) {
      final CycList dottedPair = (CycList) obj;
      final FormulaSentence sentence1 = new CycFormulaSentence(
              (CycList) dottedPair.first());
      final FormulaSentence sentence2 = new CycFormulaSentence(
              (CycList) dottedPair.rest());
      clausePairs.add(Arrays.asList(sentence1, sentence2));
    }
    return clausePairs;
  }

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
   * @throws CycConnectionException if there is a problem communicating with
   * Cyc.
   */
  public Collection<Collection<Sentence>> findRedundantClauses() throws KBApiException,
          CycConnectionException {
    Collection<Collection<FormulaSentence>> cycClauseCollections = findRedundantClausesCFS();
    Collection<Collection<Sentence>> clauses = new HashSet<Collection<Sentence>>();
    Collection<Sentence> clauseCollections = new HashSet<Sentence>();
    for (Collection<FormulaSentence> c : cycClauseCollections) {
      for (FormulaSentence s : c) {
        clauseCollections.add(new SentenceImpl(s));
      }
      clauses.add(clauseCollections);
    }
    return clauses;
  }

  /**
   * Identifies unconnected clauses in this query. Generally, all clauses of a
   * query will be connected by a chain of variables that connect them together.
   * Queries with unconnected clauses are effectively separate queries, and
   * running queries with disconnected clauses generally results in a cartesian
   * product of the answer sets of the two separate queries.
   *
   * @return a collection of the arg positions of any such clauses
   * @throws CycConnectionException if there is a problem communicating with
   * Cyc.
   */
  public Collection<ArgPosition> findUnconnectedClauses() throws CycConnectionException {
    final Set<ArgPosition> argPositions = new HashSet<ArgPosition>();
    for (final Object obj : getCycAccess().converse().converseList(SubLAPIHelper.makeSubLStmt(
            "find-unconnected-literals", querySentence))) {
      argPositions.add(new ArgPositionImpl((List<Integer>) obj));
    }
    return argPositions;
  }

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
   * @throws CycConnectionException if there was a problem connecting to Cyc.
   * @throws QueryConstructionException if there was a problem constructing the
   * new query.
   */
  public Query merge(Query otherQuery) throws QueryConstructionException,
          CycConnectionException {
    final String command = SubLAPIHelper.makeSubLStmt("combine-queries",
            querySentence, ctx.asELMt(), params, otherQuery.querySentence,
            otherQuery.ctx.asELMt(), otherQuery.params);
    final CycList newStuff = getCycAccess().converse().converseList(command);
    final Object paramsObj = newStuff.third();
    final List paramsList = CycObjectFactory.nil.equals(paramsObj) ? Collections.emptyList() : (List) paramsObj;
    final List sentenceAsList = (List) newStuff.first();
    return new Query(
            new CycFormulaSentence(sentenceAsList),
            constructContext((CycObject) newStuff.second()),
            DefaultInferenceParameters.fromPlist(getCycAccess(), paramsList));
  }

  /**
   * Set the inference mode for this Query.
   *
   * @param mode
   * @return this Query
   *
   * @see
   * com.cyc.base.inference.InferenceParameters#setInferenceMode(com.cyc.base.inference.InferenceMode)
   */
  public InferenceParameterSetter setInferenceMode(InferenceMode mode) {
    getInferenceParameters().setInferenceMode(mode);
    return this;
  }

  /**
   * Check whether this Query (once run) is browsable.
   *
   * @return true iff it is so specified.
   */
  public Boolean isBrowsable() {
    return getInferenceParameters().isBrowsable();
  }

  public InferenceParameterSetter setBrowsable(boolean value) {
    ((SpecifiedInferenceParameters) getInferenceParameters()).setBrowsable(value);
    return this;
  }

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
  public Boolean isContinuable() {
    return !inference.hasBeenStarted || getInferenceParameters().isContinuable();
  }

  /**
   * {@inheritDoc}
   *
   * @return this Query
   * @see #continueQuery()
   */
  @Override
  public InferenceParameterSetter setContinuable(boolean b) {
    ((SpecifiedInferenceParameters) getInferenceParameters()).setContinuable(b);
    return this;
  }

  /**
   * Set the maximum number of answers (or sets of answers) that Cyc will
   * attempt to find for this Query. In some cases (such as when a set of
   * answers is retrieved in a batch), more answers than this may actually be
   * returned. Once this number of answers has been reached, Cyc will not
   * actively look for additional answers.
   *
   * @param max
   * @return this Query
   *
   * @see com.cyc.base.inference.InferenceParameters#setMaxNumber(Integer)
   */
  public InferenceParameterSetter setMaxNumber(Integer max) {
    getInferenceParameters().setMaxNumber(max);
    return this;
  }

  /**
   * Set the Context of this Query.
   *
   * @param ctx
   * @return this object.
   */
  public Query setContext(final Context ctx) {
    this.ctx = ctx;
    return this;
  }

  /**
   * Set the soft timeout for this Query in seconds.
   *
   * @param max
   * @return this Query
   *
   * @see com.cyc.base.inference.InferenceParameters#setMaxTime(Integer)
   */
  public InferenceParameterSetter setMaxTime(Integer max) {
    getInferenceParameters().setMaxTime(max);
    return this;
  }

  /**
   * Set the metrics to collect for this Query.
   *
   * @param metrics
   * @return this Query
   *
   * @see
   * com.cyc.base.inference.InferenceParameters#setMetrics(com.cyc.base.inference.metrics.InferenceMetrics)
   * @throws IllegalStateException if query has already been started.
   */
  public InferenceParameterSetter setMetrics(InferenceMetrics metrics) {
    getInferenceParameters().setMetrics(metrics);
    return this;
  }

  /**
   * Check whether this Query allows abduction.
   *
   * @return true if it does, false otherwise.
   */
  @Override
  public boolean getAbductionAllowed() {
    return getInferenceParameters().getAbductionAllowed();
  }

  /**
   * Get the flavor of CycL to use for answers, or null if unspecified.
   *
   * @return the <code>InferenceAnswerLanguage</code>
   * @see InferenceParameterGetter#getAnswerLanguage().
   *
   */
  @Override
  public InferenceAnswerLanguage getAnswerLanguage() {
    return getInferenceParameters().getAnswerLanguage();
  }

  /**
   * Get the criteria for what to do when disjuncts have difference free
   * variables.
   *
   * @return the <code>DisjunctionFreeELVarsPolicy</code>, or null if
   * unspecified.
   * @see InferenceParameterGetter#getDisjunctionFreeELVarsPolicy().
   *
   */
  @Override
  public DisjunctionFreeELVarsPolicy getDisjunctionFreeELVarsPolicy() {
    return getInferenceParameters().getDisjunctionFreeELVarsPolicy();
  }

  @Override
  public Integer getMaxTransformationDepth() {
    return getInferenceParameters().getMaxTransformationDepth();
  }

  @Override
  public ProblemReusePolicy getProblemReusePolicy() {
    return getInferenceParameters().getProblemReusePolicy();
  }

  @Override
  public ProofValidationMode getProofValidationMode() {
    return getInferenceParameters().getProofValidationMode();
  }

  @Override
  public ResultUniqueness getResultUniqueness() {
    return getInferenceParameters().getResultUniqueness();
  }

  @Override
  public TransitiveClosureMode getTransitiveClosureMode() {
    return getInferenceParameters().getTransitiveClosureMode();
  }

  @Override
  public InferenceParameterSetter setAnswerLanguage(InferenceAnswerLanguage language) {
    getInferenceParameters().setAnswerLanguage(language);
    return this;
  }

  @Override
  public InferenceParameterSetter setDisjunctionFreeELVarsPolicy(DisjunctionFreeELVarsPolicy policy) {
    getInferenceParameters().setDisjunctionFreeELVarsPolicy(policy);
    return this;
  }

  @Override
  public InferenceParameterSetter setMaxTransformationDepth(Integer i) {
    getInferenceParameters().setMaxTransformationDepth(i);
    return this;
  }

  @Override
  public InferenceParameterSetter setProblemReusePolicy(ProblemReusePolicy policy) {
    getInferenceParameters().setProblemReusePolicy(policy);
    return this;
  }

  @Override
  public InferenceParameterSetter setProofValidationMode(ProofValidationMode mode) {
    getInferenceParameters().setProofValidationMode(mode);
    return this;
  }

  @Override
  public InferenceParameterSetter setResultUniqueness(ResultUniqueness mode) {
    getInferenceParameters().setResultUniqueness(mode);
    return this;
  }

  @Override
  public InferenceParameterSetter setTransitiveClosureMode(TransitiveClosureMode mode) {
    getInferenceParameters().setTransitiveClosureMode(mode);
    return this;
  }

  /**
   * Set the inference parameters for this query.
   *
   * @param params the inference parameters
   * @return this Query object.
   */
  public Query setInferenceParameters(final InferenceParameters params) {
    this.params = params;
    return this;
  }

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
  @Deprecated
  public Query setQuerySentenceHypothesizedClause(
          CycFormulaSentence sentence) {
    if (querySentence.isConditionalSentence()) {
      querySentence.setSpecifiedObject(ArgPositionImpl.ARG1, sentence);
    } else {
      querySentence = CycFormulaSentence.makeConditional(sentence, querySentence);
    }
    return this;
  }

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
  public Query setQuerySentenceHypothesizedClause(Sentence sentence) {
    Query q = setQuerySentenceHypothesizedClause((CycFormulaSentence) sentence.getCore());
    return q;
  }

  /**
   * Sets the main (i.e. non-hypothesized) clause of this Query
   *
   * @param sentence
   * @return this Query.
   * @see Query#getQuerySentenceMainClause()
   * @throws IllegalStateException if query has already been started.
   */
  @Deprecated
  public Query setQuerySentenceMainClause(
          CycFormulaSentence sentence) {
    if (querySentence.isConditionalSentence()) {
      querySentence.setSpecifiedObject(ArgPositionImpl.ARG2, sentence);
    } else {
      querySentence = sentence;
    }
    return this;
  }

  /**
   * Sets the main (i.e. non-hypothesized) clause of this Query
   *
   * @param sentence
   * @return this Query.
   * @see Query#getQuerySentenceMainClause()
   * @throws IllegalStateException if query has already been started.
   */
  public Query setQuerySentenceMainClause(Sentence sentence) {
    Query q = setQuerySentenceMainClause((CycFormulaSentence) sentence.getCore());
    return q;
  }

  /**
   * Designates vars as the variables to return bindings for.
   *
   * @param vars
   * @return this query.
   * @throws IllegalArgumentException if any of vars is not found in this query.
   * @throws IllegalStateException if query has already been started.
   */
  private Query setQueryVariablesCyc(Collection<CycVariable> vars) {
    for (final CycVariable var : getQueryVariablesCyc()) {
      removeQueryVariable(var);
    }
    for (final CycVariable var : vars) {
      addQueryVariable(var);
    }
    return this;
  }

  /**
   * Designates vars as the variables to return bindings for.
   *
   * @param vars
   * @return this query.
   * @throws IllegalArgumentException if any of vars is not found in this query.
   * @throws IllegalStateException if query has already been started.
   */
  public Query setQueryVariables(Collection<Variable> vars) {
    Collection<CycVariable> cycvars = new HashSet<CycVariable>();
    for (Variable v : vars) {
      cycvars.add((CycVariable) v.getCore());
    }
    Query q = setQueryVariablesCyc(cycvars);
    return q;
  }

  /**
   * Starts the query.
   *
   *
   * @throws CycConnectionException if there is a problem communicating with
   * Cyc.
   */
  public void start() throws CycConnectionException {
    try {
      inference.start();
    } catch (IOException e) {
      throw new CycConnectionException(e);
    }
  }

  /**
   * Issues a request that the query stop immediately.
   *
   * @param patience If non-null, the query will be forcibly aborted if it does
   * not stop before this many seconds have elapsed.
   */
  public void stop(final Integer patience) {
    inference.stop(patience);
  }

  /**
   * Get the Cyc image to be used for this query.
   *
   * @return a CycAccess for the Cyc image.
   */
  protected final CycAccess getCycAccess() {
    return this.cyc;
  }

  /**
   * Get the Cyc session to be used for this query.
   *
   * @return a CycSession for this query.
   */
  public final CycSession getCycSession() {
    return this.session;
  }

  /**
   * Specify that this inference should be retained by Cyc until the Query is
   * closed. This can be called before the query has been started, and must be
   * called before the query has finished running.
   *
   * @see Query#close()
   */
  public void retainInference() {
    this.retainInference = true;
  }

  private DefaultInferenceParameters getDefaultInferenceParameters() {
    return new DefaultInferenceParameters(getCycAccess(), true);
  }

  private void initializeParams() {
    this.params = getDefaultInferenceParameters();
  }

  /**
   * A class for categorizing queries. There are no predefined categories, and
   * these categories need not correspond to any categorization of reified
   * queries in Cyc. Adding a category does not result in any assertions being
   * made.
   *
   * @see #addCategory(com.cyc.query.Query.Category)
   */
  public static class Category {

    /**
     * Find or create the Category with the specified name.
     *
     * @param name
     * @return this category
     */
    static public synchronized Category get(final String name) {
      final Category existing = instances.get(name);
      if (existing != null) {
        return existing;
      }
      final Category category = new Category(name);
      instances.put(name, category);
      return category;
    }

    /**
     *
     * @return
     */
    public String getName() {
      return name;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final Category other = (Category) obj;
      if ((this.name == null) ? (other.name != null) : !this.name.equals(
              other.name)) {
        return false;
      }
      return true;
    }

    @Override
    public int hashCode() {
      int hash = 5;
      hash = 43 * hash + (this.name != null ? this.name.hashCode() : 0);
      return hash;
    }
    private final String name;

    /**
     * Create a new category with the specified name. Does not cause a new term
     * to be reified on the Cyc server.
     *
     * @param name
     */
    private Category(final String name) {
      this.name = name;
    }

    private static final Map<String, Category> instances = new HashMap<String, Category>();
  }

  /**
   * Returns the number of answers found for this query. For running queries,
   * the value returned by this method may change as additional answers are
   * found.
   *
   * @return the number of answers found for this query.
   */
  public int getAnswerCount() {
    if (!getQueryVariablesCyc().isEmpty()) {
      return getResultSet().getCurrentRowCount();
    } else if (isTrue()) {
      return 1;
    } else {
      return 0;
    }
  }

  /**
   * Returns the list of answers for this query. For running queries, the value
   * returned by this method may change as additional answers are found.
   *
   * @return the list of answers
   * @throws CycConnectionException if there is a problem communicating with
   * Cyc.
   */
  @Deprecated
  public List<InferenceAnswer> getAnswersCyc() throws CycConnectionException {
    final List<InferenceAnswer> answers = new ArrayList<InferenceAnswer>(
            getAnswerCount());
    for (int i = 0; i < getAnswerCount(); i++) {
      answers.add(getAnswerCyc(i));
    }
    return answers;
  }

  /**
   * Returns the list of answers for this query. For running queries, the value
   * returned by this method may change as additional answers are found.
   *
   * @return the list of answers
   * @throws CycConnectionException if there is a problem communicating with
   * Cyc.
   */
  public List<QueryAnswer> getAnswers() throws CycConnectionException {
    final List<QueryAnswer> answers = new ArrayList<QueryAnswer>(
            getAnswerCount());
    for (int i = 0; i < getAnswerCount(); i++) {
      answers.add(getAnswer(i));
    }
    return answers;
  }

  /**
   * Returns the nth answer for this query. For the first answer, n == 0.
   *
   * @param n
   * @return the answer.
   * @throws CycConnectionException if there is a problem communicating with
   * Cyc.
   */
  public QueryAnswer getAnswer(final int n) throws CycConnectionException {
    final InferenceAnswer answerCyc = getAnswerCyc(n);
    return (answerCyc == null) ? null : new InferenceAnswerBackedQueryAnswer(answerCyc) {
      //Inner class is used so pointer to parent Query is maintained.
      // That prevents the Query from being closed and the inference from being destroyed
      // so long as this answer stays around.
    };
  }

  /**
   * Returns the nth answer for this query. For the first answer, n == 0.
   *
   * @param n
   * @return the answer.
   * @throws CycConnectionException if there is a problem communicating with
   * Cyc.
   */
  @Deprecated
  public InferenceAnswer getAnswerCyc(final int n) throws CycConnectionException {
    if (n >= getAnswerCount()) {
      throw new IllegalArgumentException("Can't get answer " + n
              + ". Query has only " + getAnswerCount() + " answers.");
    }
    final InferenceIdentifier inferenceIdentifier = getInferenceIdentifier();
    if (inferenceIdentifier != null) {
      return new CycBackedInferenceAnswer(new SpecifiedInferenceAnswerIdentifier(
              inferenceIdentifier, n));
    } else if (getResultSet() != null) {
      final KBInferenceResultSet resultSet = getResultSet();
      return new ResultSetInferenceAnswer((DefaultResultSet) resultSet.getInferenceResultSet(), n);
    } else {
      return null;
    }
  }

  /**
   * Returns the Context of this Query.
   *
   * @return the Context of this Query
   */
  public Context getContext() {
    return ctx;
  }

  /**
   * Get the CycL sentence of this query.
   *
   * @return the query sentence
   */
  @Deprecated
  public FormulaSentence getQuerySentenceCyc() {
    return querySentence;
  }

  /**
   *
   * @return @throws KBApiException
   */
  public Sentence getQuerySentence() throws KBApiException {
    Sentence s = new SentenceImpl(getQuerySentenceCyc());
    return s;
  }

  /**
   * Set the CycL sentence of this query.
   *
   * @param querySentence
   */
  @Deprecated
  public void setQuerySentence(FormulaSentence querySentence) {
    this.querySentence = querySentence;
  }

  /**
   *
   * @param querySentence
   */
  public void setQuerySentence(Sentence querySentence) {
    setQuerySentence((FormulaSentence) querySentence.getCore());
  }

  /**
   * Returns the main (that is, non-hypothesized) clause of this Query. All
   * valid queries have a main sentence clause.
   *
   * @return the main (i.e. non-hypothesized) clause of this query.
   */
  @Deprecated
  public FormulaSentence getQuerySentenceMainClauseCyc() {
    if (querySentence.isConditionalSentence()) {
      return (FormulaSentence) querySentence.getArg2();
    } else {
      return querySentence;
    }
  }

  /**
   *
   * @return @throws KBApiException
   */
  public Sentence getQuerySentenceMainClause() throws KBApiException {
    Sentence s = new SentenceImpl(getQuerySentenceMainClauseCyc());
    return s;
  }

  /**
   * Returns the hypothesized clause of this Query, if any. Most queries have no
   * hypothesized clause, in which case this method will return null.
   *
   * @return the hypothesized clause of this query, if any, or null if there is
   * none.
   */
  @Deprecated
  public FormulaSentence getQuerySentenceHypothesizedClauseCyc() {
    if (querySentence.isConditionalSentence()) {
      return (FormulaSentence) querySentence.getArg1();
    } else {
      return null;
    }
  }

  /**
   *
   * @return @throws KBApiException
   */
  public Sentence getQuerySentenceHypothesizedClause() throws KBApiException {
    Sentence s = new SentenceImpl(getQuerySentenceHypothesizedClauseCyc());
    return s;
  }

  /**
   * Get the CycL sentence from the specified answer to this query. Substitutes
   * the set of bindings from answer into the query sentence.
   *
   * @param answer
   * @throws CycConnectionException if there is a problem communicating with
   * Cyc.
   * @return the answer sentence
   */
  @Deprecated
  public FormulaSentence getAnswerSentenceCyc(InferenceAnswer answer)
          throws CycConnectionException {
    final FormulaSentence sentence = getQuerySentenceMainClauseCyc().deepCopy();
    for (final CycVariable var : getQueryVariablesCyc()) {
      sentence.substituteDestructive(var, answer.getBinding(var));
    }
    return sentence;
  }

  /**
   * Get the CycL sentence from the specified answer to this query. Substitutes
   * the set of bindings from answer into the query sentence.
   *
   * @param answer
   * @return the answer sentence
   * @throws KBApiException
   */
  public Sentence getAnswerSentence(QueryAnswer answer) throws KBApiException {
    final FormulaSentence sentence = getQuerySentenceMainClauseCyc().deepCopy();
    for (final Variable var : getQueryVariables()) {
      sentence.substituteDestructive(var, answer.getBinding(var));
    }
    return new SentenceImpl(sentence);
  }

  /**
   * Forget all results for this query. All settings on the Query are retained,
   * including the query sentence, context, and inference parameters. After a
   * Query has been cleared, it can be re-run, with possibly different results.
   *
   * @return this Query
   */
  public Query clearResults() {
    inference.clear();
    return this;
  }

  /**
   * Returns the soft timeout for this Query in seconds.
   *
   * @return the soft timeout for this Query in seconds.
   *
   * @see com.cyc.base.inference.InferenceParameters#getMaxTime()
   */
  @Override
  public Integer getMaxTime() {
    if (params == null) {
      return null;
    } else {
      return params.getMaxTime();
    }
  }

  /**
   * Returns the maximum number of answers (or sets of answers) that Cyc will
   * attempt to find for this Query. In some cases (such as when a set of
   * answers is retrieved in a batch), more answers than this may actually be
   * returned. Once this number of answers has been reached, Cyc will not
   * actively look for additional answers.
   *
   * @return the number cutoff for this Query.
   *
   * @see com.cyc.base.inference.InferenceParameters#getMaxNumber()
   */
  public Integer getMaxNumber() {
    if (params == null) {
      return null;
    } else {
      return params.getMaxNumber();
    }
  }

  /**
   * Returns the inference mode for this Query.
   *
   * @return the inference mode for this Query.
   *
   * @see com.cyc.base.inference.InferenceParameters#getInferenceMode()
   */
  public InferenceMode getInferenceMode() {
    if (params == null) {
      return null;
    } else {
      return params.getInferenceMode();
    }
  }

  /**
   * Return the InferenceStatus for this Query.
   *
   * @return the InferenceStatus for this Query.
   */
  public InferenceStatus getStatus() {
    return this.inference.inferenceStatus;
  }

  /**
   * Return the reason why this Query was suspended (if it was).
   *
   * @return the reason, or null if this Query was not suspended.
   * @see DefaultInferenceSuspendReason for examples.
   */
  public InferenceSuspendReason getSuspendReason() {
    return this.inference.getSuspendReason();
  }

  /**
   *
   * @return true iff this query has been proven true.
   * @throws RuntimeException if the query has open variables.
   * @see com.cyc.base.inference.InferenceResultSet#getTruthValue()
   */
  public boolean isTrue() {
    return getResultSet().getTruthValue();
  }

  /**
   * Is this query either True (if a boolean query) or does it have bindings (if
   * non-boolean)
   *
   * @return True if there are bindings (or it's a true boolean query), false if
   * there are no bindings (or it's a false boolean query).
   *
   */
  public boolean isProvable() {
    return getAnswerCount() > 0;
  }

  /**
   * Closes this query's result set, and releases resources on the Cyc server.
   * See {@link com.cyc.query.KBInferenceResultSet#close()} for more details on
   * what happens when a query is closed.
   * <p>
   * It is good practice to always invoke this method explicitly as soon as a
   * Query is no longer needed, as they can take up significant amounts of
   * memory on the Cyc server.
   *
   * @see com.cyc.query.KBInferenceResultSet#close()
   */
  @Override
  public void close() {
    inference.close();
    unclosedQueries.remove(this);
  }

  /**
   * Returns the timeout for the {@link #close()} method.
   *
   * @return timeout in milliseconds
   */
  public long getCloseTimeout() {
    return inference.closeTimeoutMS;
  }

  /**
   * Sets the timeout for the {@link #close()} method.
   *
   * @param timeoutMS timeout in milliseconds
   */
  public void setCloseTimeout(long timeoutMS) {
    inference.closeTimeoutMS = timeoutMS;
  }

  /**
   *
   * @return
   */
  public static synchronized int closeAllUnclosedQueries() {
    int count = 0;
    for (final Query q : unclosedQueries) {
      q.close();
      count++;
    }
    if (count > 0) {
      System.out.println("Closed " + count + " queries.");
    }
    return count;
  }

  @Override
  protected void finalize() throws Throwable {
    close();
    super.finalize();
  }

  @Override
  public String toString() {
    return "Query: " + querySentence;
  }

  /**
   *
   * @return this query's result set. The KBInferenceResultSet is an object that
   * may be updated dynamically for running queries. This contrasts with
   * {@link #getAnswersCyc()} which returns a static list of the answers at the
   * time is was called.
   *
   * @see com.cyc.query.KBInferenceResultSet
   */
  public synchronized QueryResultSet getResultSet() {
    try {
      return inference.getResultSet();
    } catch (Exception e) {
      throw new QueryApiRuntimeException("Exception thrown while getting result set.", e);
    }
  }

  @Override
  public int hashCode() {
    int hash = 1;
    hash = hash * 17 + (ctx == null ? 0 : ctx.hashCode());
    hash = hash * 31 + (params == null ? 0 : params.hashCode());
    hash = hash * 13 + (querySentence == null ? 0 : querySentence.hashCode());
    return hash;
  }

  @Override
  public boolean equals(Object o) {
    if ((o == null) || !(o instanceof Query)) {
      return false;
    }
    return this.hashCode() == o.hashCode();
  }

  /**
   * Inner class to hold the aspects of a Query that it acquires when run.
   */
  private class QueryInference implements InferenceWorkerListener {

    protected long closeTimeoutMS = 5000;
    private QueryResultSet rs = null;
    private boolean hasBeenStarted = false;
    private InferenceWorker worker;
    private InferenceStatus inferenceStatus = DefaultInferenceStatus.NOT_STARTED;
    private InferenceIdentifier inferenceIdentifier = null;
    private final List<Object> cycAnswers = new ArrayList<Object>();

    private QueryInference() {

    }

    /**
     *
     * @param inferenceWorker
     */
    @Override
    public void notifyInferenceCreated(InferenceWorker inferenceWorker) {
      this.hasBeenStarted = true;
    }

    /**
     *
     * @param oldStatus
     * @param newStatus
     * @param suspendReason
     * @param inferenceWorker
     */
    @Override
    public void notifyInferenceStatusChanged(InferenceStatus oldStatus,
            InferenceStatus newStatus, InferenceSuspendReason suspendReason,
            InferenceWorker inferenceWorker) {
      this.inferenceStatus = newStatus;
    }

    /**
     *
     * @param inferenceWorker
     * @param newAnswers
     */
    @Override
    public void notifyInferenceAnswersAvailable(InferenceWorker inferenceWorker,
            List newAnswers) {
      cycAnswers.addAll(newAnswers);
    }

    /**
     *
     * @param inferenceWorker
     * @param e
     */
    @Override
    public void notifyInferenceTerminated(InferenceWorker inferenceWorker,
            Exception e) {
      if (isContinuingInference) {
        isContinuingInference = false;
        synchronized (continueInferenceLock) {
          continueInferenceLock.notify();
        }
      }
    }

    private InferenceIdentifier getInferenceIdentifier() throws CycConnectionException {
      if (inferenceIdentifier == null && hasBeenStarted) {
        if (worker != null) {
          inferenceIdentifier = worker.getInferenceIdentifier();
        }
        if (getResultSet() != null && inferenceIdentifier == null) {
          inferenceIdentifier = rs.getInferenceIdentifier();
        }
      }
      return inferenceIdentifier;
    }

    private InferenceSuspendReason getSuspendReason() {
      if (worker != null) {
        return worker.getSuspendReason();
      } else {
        return null;
      }
    }

    private void start() throws IOException, CycConnectionException {
      worker = createInferenceWorker();
      worker.start();
      hasBeenStarted = true;
    }

    private void stop(final Integer patience) {
      if (patience == null) {
        worker.interruptInference();
      } else {
        worker.interruptInference(patience.intValue());
      }
    }

    private void close() {
      if (worker != null) {
        try {
          worker.releaseInferenceResources(closeTimeoutMS);
          return;
        } catch (Exception ex) {
          System.err.println(
                  "Got exception try to free inference resources for " + this);
          ex.printStackTrace(System.err);
        }
      }
      if (rs != null) {
        rs.close();
      }
    }

    private QueryResultSet getResultSet() throws CycConnectionException {
      if (rs == null) {
        if (!hasBeenStarted) {
          performInference();
        }
        if (rs == null) {
          setResultSet(new QueryResultSet(cycAnswers));
        }
      }
      if (rs.getCurrentRowCount() < cycAnswers.size()) {
        setResultSet(new QueryResultSet(cycAnswers));
      }
      return rs;
    }

    private QueryResultSet performInference() throws CycConnectionException {

      if (requiresInferenceWorker()) {
        createInferenceWorker().executeQuery();
      } else {
        final InferenceResultSet inferenceResultSet = getCycAccess().getInferenceTool().executeQuery(getQuerySentenceCyc(),
                getContext().asELMt(), getInferenceParameters());
        this.setResultSet(new QueryResultSet(inferenceResultSet));
        hasBeenStarted = true;
        this.inferenceStatus = DefaultInferenceStatus.SUSPENDED;
      }
      return rs;
    }
    private final Object continueInferenceLock = new Object();
    private boolean isContinuingInference = false;

    private void continueInference() throws CycConnectionException {
      if (!isContinuable()) {
        throw new UnsupportedOperationException("This query is not continuable.");
      } else if (!hasBeenStarted) {
        performInference();
      } else if (worker != null) {
        try {
          isContinuingInference = true;
          worker.continueInference(params);
          synchronized (continueInferenceLock) {
            while (isContinuingInference) {
              continueInferenceLock.wait();
            }
          }
        } catch (InterruptedException ex) {

        } finally {
          isContinuingInference = false;
        }
      } else {
        throw new UnsupportedOperationException("This query cannot be continued.");
      }
    }

    private QueryWorker createInferenceWorker() {

      final ELMt elmt = ctx.asELMt();
      worker = new QueryWorker(elmt, getCycAccess());
      // We get to be the first listener, so we can be sure we're up to date
      // when other listeners are called.
      worker.addInferenceListener(this);
      for (final QueryListener listener : listeners) {
        worker.addInferenceListener(listener.getInferenceListener());
      }
      return (QueryWorker) worker;

    }

    private void setResultSet(QueryResultSet rs) {
      this.rs = rs;
    }

    private void clear() {
      close();
      rs = null;
      hasBeenStarted = false;
      worker = null;
      inferenceStatus = DefaultInferenceStatus.NOT_STARTED;
      inferenceIdentifier = null;
      cycAnswers.clear();
    }
  }

  class QueryWorker extends DefaultInferenceWorkerSynch {

    public QueryWorker(ELMt mt, CycAccess access) {
      super(querySentence, mt, params, access, TIMEOUT);
    }

    Query getQuery() {
      return Query.this;
    }
  }

  /**
   * An extension of {@link KBInferenceResultSet} that also provides an
   * {@link Iterable#iterator} method for {@link QueryAnswer} objects.
   */
  public class QueryResultSet extends KBInferenceResultSet implements Iterable<QueryAnswer> {

    /**
     *
     * @param rs
     */
    public QueryResultSet(InferenceResultSet rs) {
      super(rs);
    }

    /**
     *
     * @param results
     */
    public QueryResultSet(List<Object> results) {
      super(results);
    }

    @Override
    public Iterator<QueryAnswer> iterator() {
      try {
        return Query.this.getAnswers().iterator();
      } catch (Exception ex) {
        throw new IllegalStateException(ex);
      }
    }
  }

  /**
   *
   * @throws IOException
   */
  private Query() throws QueryConstructionException {
    try {
      this.session = CycSessionManager.getCurrentSession();
      this.cyc = CycAccessManager.getAccessManager().fromSession(session);
    } catch (SessionConfigurationException ex) {
      throw new QueryConstructionException(ex);
    } catch (SessionCommunicationException ex) {
      throw new QueryConstructionException(ex);
    } catch (SessionInitializationException ex) {
      throw new QueryConstructionException(ex);
    }
  }

  private boolean requiresInferenceWorker() {
    final InferenceParameters inferenceParameters = getInferenceParameters();
    if (inferenceParameters.containsKey(RETURN)) {
      return true;
    } else if (Boolean.TRUE.equals(inferenceParameters.isContinuable())) {
      return true;
    } else if (Boolean.TRUE.equals(inferenceParameters.isBrowsable())) {
      return true;
    } else if (!listeners.isEmpty()) {
      return true;
    } else if (retainInference == true) {
      return true;
    } else if (CycObjectFactory.t.equals(
            inferenceParameters.get(COMPUTE_ANSWER_JUSTIFICATIONS))) {
      return true;
    } else {
      return false;
    }
  }

  private static final Set<Query> unclosedQueries = Collections.newSetFromMap(new ConcurrentHashMap<Query, Boolean>());
}
