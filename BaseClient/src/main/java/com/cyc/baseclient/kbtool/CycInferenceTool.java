package com.cyc.baseclient.kbtool;

/*
 * #%L
 * File: CycInferenceTool.java
 * Project: Base Client
 * %%
 * Copyright (C) 2013 - 2014 Cycorp, Inc.
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

import com.cyc.base.CycAccess;
import com.cyc.base.CycApiException;
import com.cyc.base.CycConnectionException;
import com.cyc.base.CycTimeOutException;
import com.cyc.base.cycobject.FormulaSentence;
import com.cyc.base.cycobject.CycList;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.CycSymbol;
import com.cyc.base.cycobject.CycVariable;
import com.cyc.base.cycobject.ELMt;
import com.cyc.base.inference.InferenceParameters;
import com.cyc.base.inference.InferenceResultSet;
import com.cyc.base.kbtool.InferenceTool;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.cyc.baseclient.AbstractKBTool;
import com.cyc.baseclient.api.CycApiServerSideException;
import com.cyc.baseclient.CycObjectFactory;
import static com.cyc.baseclient.CycObjectFactory.makeCycSymbol;
import com.cyc.baseclient.api.DefaultSubLWorkerSynch;
import static com.cyc.baseclient.api.SubLAPIHelper.makeNestedSubLStmt;
import static com.cyc.baseclient.api.SubLAPIHelper.makeSubLStmt;
import com.cyc.baseclient.api.SubLWorkerSynch;
import com.cyc.baseclient.cycobject.CycArrayList;
import com.cyc.baseclient.cycobject.CycFormulaSentence;
import com.cyc.baseclient.cycobject.CycSymbolImpl;
import com.cyc.baseclient.inference.DefaultInferenceWorkerSynch;
import com.cyc.baseclient.inference.DefaultResultSet;
import com.cyc.baseclient.inference.DefaultInferenceSuspendReason;
import com.cyc.base.inference.InferenceWorkerSynch;
import com.cyc.baseclient.inference.params.DefaultInferenceParameterDescriptions;
import com.cyc.baseclient.inference.params.DefaultInferenceParameters;
import com.cyc.baseclient.inference.params.InferenceParameterDescriptions;
import com.cyc.baseclient.util.LRUCache;

/**
 * Tools for performing inferences over the Cyc KB. To lookup
 * facts in the Cyc KB, use the {@link com.cyc.baseclient.kbtool.CycLookupTool}.
 * 
 * @see com.cyc.baseclient.kbtool.CycLookupTool
 * @author nwinant
 */
public class CycInferenceTool extends AbstractKBTool implements InferenceTool {
  // @todo: more specific return types?
  
  public CycInferenceTool(CycAccess client) {
    super(client);
  }
  
  
  // Public
  
  /** 
   * Returns a query properties string for the given query properties if present, otherwise
   * returns a query properties string for the default query properties.
   *
   * @param queryProperties the given query properties or null if the defaults are to be used
   *
   * @return a query properties string for the given query properties if present, otherwise
   * returns a query properties string for the default query properties
   */
  @Override
  public String queryPropertiesToString(
          final InferenceParameters queryProperties) {
    final InferenceParameters tempQueryProperties = (queryProperties == null) ? getHLQueryProperties() : queryProperties;
    final CycArrayList parameterList = new CycArrayList();
    for (final Iterator<Map.Entry<CycSymbol, Object>> iter = tempQueryProperties.entrySet().iterator(); iter.hasNext();) {
      Map.Entry<CycSymbol, Object> mapEntry = iter.next();
      CycSymbol queryParameterKeyword = mapEntry.getKey();
      parameterList.add(queryParameterKeyword);
      final Object rawValue = mapEntry.getValue();
      parameterList.add(tempQueryProperties.parameterValueCycListApiValue(
              queryParameterKeyword, rawValue));
    }
    return parameterList.stringApiValue();
  }
  
  /**
   * Asks a Cyc query (new inference parameters) and returns the binding list.
   *
   * @param query the query expression
   * @param mt the inference microtheory
   * @param queryProperties queryProperties the list of query property keywords and values
   *
   * @return the binding list resulting from the given query
   * @throws com.cyc.base.CycConnectionException
   *
   * @throws IOException if a communications error occurs
   * @throws UnknownHostException if the Cyc server cannot be found
   * @throws CycApiException if the Cyc server returns an error
   * @deprecated use
   * <code>executeQuery</code>
   */
  public CycList askNewCycQuery(final CycList query,
          final CycObject mt,
          final InferenceParameters queryProperties)
          throws CycConnectionException, CycApiException {
    final String script =
            "(new-cyc-query " + query.stringApiValue() + " " + makeELMt_inner(mt).stringApiValue() + " " + queryPropertiesToString(
            queryProperties) + ")";
    return getConverse().converseList(script);
  }

  /**
   * Run a synchronous query against Cyc.
   *
   * </p><strong>Note: </strong> One should put this call in a try/finally statement
   * and the finally statement should explicitly close the result set. Failure to do so
   * could cause garbage to accumulate on the server.
   *
   * @param query the query in CycArrayList form to ask
   * @param mt the microtheory in which the query should be asked
   * @param queryProperties the query properties to use when asking the query
   * @param timeoutMsecs the amount of time in milliseconds to wait before
   * giving up. A zero for this value means to wait forever.
   * @return the
   * <code>DefaultResultSet</code> which provides a convenient
   * mechanism for walking over results similar to java.sql.ResultSet
   * @throws com.cyc.base.CycConnectionException
   * @throws IOException if a communication error occurs
   * @throws CycApiException if an internal error occurs
   * @throws CycTimeOutException if the query times out
   */
  @Override
  public InferenceResultSet executeQuery(final FormulaSentence query,
          final ELMt mt, final InferenceParameters queryProperties,
          final long timeoutMsecs)
          throws CycConnectionException, com.cyc.base.CycApiException, CycTimeOutException {
    CycFormulaSentence s = (CycFormulaSentence)query;
    CycList sList = (CycList)(s.getArgs());
    return executeQuery(sList, mt, queryProperties,
            timeoutMsecs);
  }

  /**
   * Run a synchronous query against Cyc.
   *
   * </p><strong>Note: </strong> One should put this call in a try/finally statement
   * and the finally statement should explicitly close the result set. Failure to do so
   * could cause garbage to accumulate on the server.
   *
   * @param query the query in CycFormulaSentence form to ask
   * @param mt the microtheory in which the query should be asked
   * @param queryProperties the query properties to use when asking the query
   * @return the
   * <code>DefaultResultSet</code> which provides a convenient
   * mechanism for walking over results similar to java.sql.ResultSet
   * @throws IOException if a communication error occurs
   * @throws CycApiException if an internal error occurs
   * @throws CycTimeOutException if the query times out
   */
  @Override
  public InferenceResultSet executeQuery(final FormulaSentence query,
          final ELMt mt, final InferenceParameters queryProperties)
          throws CycConnectionException, com.cyc.base.CycApiException, CycTimeOutException {
    return executeQuery(query, mt, queryProperties, 0);
  }

  /**
   * Run a synchronous query against Cyc.
   *
   * </p><strong>Note: </strong> One should put this call in a try/finally statement
   * and the finally statement should explicitly close the result set. Failure to do so
   * could cause garbage to accumulate on the server.
   *
   * @param query the query in CycFormulaSentence form to ask
   * @param mt the microtheory in which the query should be asked
   * @param queryProperties the query properties to use when asking the query
   * @param timeoutMsecs the amount of time in milliseconds to wait before
   * giving up. A zero for this value means to wait forever.
   * @return the
   * <code>DefaultResultSet</code> which provides a convenient
   * mechanism for walking over results similar to java.sql.ResultSet
   * @throws IOException if a communication error occurs
   * @throws CycApiException if an internal error occurs
   * @throws CycTimeOutException if the query times out
   */
  @Override
  public InferenceResultSet executeQuery(final CycList query,
          final CycObject mt, final InferenceParameters queryProperties,
          final long timeoutMsecs)
          throws CycConnectionException, com.cyc.base.CycApiException, CycTimeOutException {
    // Use new-cyc-query if args are okay for it:
    if (isOkForNewCycQuery(queryProperties, timeoutMsecs)) {
        final String command = makeSubLStmt("multiple-value-list",
                               makeNestedSubLStmt("new-cyc-query", query, mt,
                               (queryProperties != null) ? queryProperties : new DefaultInferenceParameters(
                               getCyc())));
      final CycList results = getConverse().converseList(command);
      final CycObject bindings = (CycObject) results.get(0);
      final DefaultInferenceSuspendReason haltReason = DefaultInferenceSuspendReason.fromCycSuspendReason(
              results.get(1));
      if (haltReason.isError()) {
        throw new CycApiServerSideException(
                "Inference halted due to error:\n" + haltReason);
      }
      return new DefaultResultSet(CycObjectFactory.nil.equals(bindings)
              ? Collections.emptyList() : (CycArrayList) bindings);
    } else {
      InferenceWorkerSynch worker =
              new DefaultInferenceWorkerSynch(query, (ELMt) mt, queryProperties,
              getCyc(), timeoutMsecs);
      return worker.executeQuery();
    }
  }
  
  /**
   * Run a synchronous query against Cyc.
   *
   * </p><strong>Note: </strong> One should put this call in a try/finally statement
   * and the finally statement should explicitly close the result set. Failure to do so
   * could cause garbage to accumulate on the server.
   *
   * @param query the query in String form to ask
   * @param mt the microtheory in which the query should be asked (as a String, CycObject or ELMt)
   * @param queryProperties the query properties to use when asking the query
   * @param timeoutMsecs the amount of time in milliseconds to wait before
   * giving up. A zero for this value means to wait forever.
   * @return the
   * <code>DefaultResultSet</code> which provides a convenient
   * mechanism for walking over results similar to java.sql.ResultSet
   * @throws IOException if a communication error occurs
   * @throws CycApiException if an internal error occurs
   * @throws CycTimeOutException if the query times out
   */
  @Override
  public InferenceResultSet executeQuery(final String query,
          final Object mt, final InferenceParameters queryProperties,
          final long timeoutMsecs)
          throws CycConnectionException, com.cyc.base.CycApiException, CycTimeOutException {
    InferenceWorkerSynch worker =
            new DefaultInferenceWorkerSynch(query, getCyc().getObjectTool().makeELMt(mt), queryProperties,
            getCyc(), timeoutMsecs);
    InferenceResultSet rs = worker.executeQuery();
    return rs;
  }

  /**
   * Asks a Cyc query (new inference parameters) and returns an XML stream according
 to the specifications in the CycArrayList xmlSpec.
   *
   * @param query the query expression
   * @param mt the inference microtheory
   * @param queryProperties queryProperties the list of query property keywords and values
   * @param xmlSpec the specification of elements, attributes, sort order and bindings for the XML that the method returns
   *
   * @return the binding list from the query in the XML format specified by xmlSpec
   *
   * @throws IOException if a communications error occurs
   * @throws UnknownHostException if the Cyc server cannot be found
   * @throws CycApiException if the Cyc server returns an error
   */
  public String queryResultsToXMLString(CycList query,
          CycObject mt,
          InferenceParameters queryProperties,
          CycList xmlSpec)
          throws CycConnectionException, com.cyc.base.CycApiException {
    return queryResultsToXMLStringInternal(query, mt, queryProperties, xmlSpec);
  }

  /**
   * Asks a Cyc query (new inference parameters) and returns an XML stream according
 to the specifications in the CycArrayList xmlSpec.
   *
   * @param query the query expression
   * @param mt the inference microtheory
   * @param queryProperties queryProperties the list of query property keywords and values
   * @param xmlSpec the specification of elements, attributes, sort order and bindings for the XML that the method returns
   *
   * @return the binding list from the query in the XML format specified by xmlSpec
   *
   * @throws IOException if a communications error occurs
   * @throws UnknownHostException if the Cyc server cannot be found
   * @throws CycApiException if the Cyc server returns an error
   */
  public String queryResultsToXMLString(FormulaSentence query,
          CycObject mt,
          InferenceParameters queryProperties,
          CycList xmlSpec)
          throws CycConnectionException, com.cyc.base.CycApiException {
    return queryResultsToXMLStringInternal(query, mt, queryProperties, xmlSpec);

  }

  /**
   * Returns true if the Cyc query (with inference parameters) is proven true.
   *
   * @param query the query expression
   * @param mt the inference microtheory
   * @param queryProperties queryProperties the list of query property keywords
   * and values, or null if the defaults are to used
   *
   * @return true if the query is proven true.
   *
   * @throws IOException if a communications error occurs
   * @throws UnknownHostException if the Cyc server cannot be found
   * @throws CycApiException if the Cyc server returns an error or if there are open variables in the query
   */
  @Override
  public boolean isQueryTrue(CycList query, CycObject mt,
          InferenceParameters queryProperties)
          throws CycConnectionException, com.cyc.base.CycApiException {
    return isQueryTrue(query, mt, queryProperties, 0);
  }

  /**
   * Returns true if the Cyc query (with inference parameters) is proven true.
   *
   * @param query the query expression
   * @param mt the inference microtheory
   * @param queryProperties queryProperties the list of query property keywords
   * and values, or null if the defaults are to used
   *
   * @return true if the query is proven true.
   *
   * @throws IOException if a communications error occurs
   * @throws UnknownHostException if the Cyc server cannot be found
   * @throws CycApiException if the Cyc server returns an error or if there are open variables in the query
   */
  @Override
  public boolean isQueryTrue(FormulaSentence query, CycObject mt,
          InferenceParameters queryProperties)
          throws CycConnectionException, com.cyc.base.CycApiException {
    return isQueryTrue(query, mt, queryProperties, 0);
  }

  /**
   * Returns true if the Cyc query (with inference parameters) is proven true.
   *
   * @param query the query expression
   * @param mt the inference microtheory
   * @param queryProperties queryProperties the list of query property keywords
   * and values, or null if the defaults are to used
   * @param timeoutMsecs the time in milliseconds to wait before giving up,
   * set to zero in order to wait forever
   *
   * @return true if the query is proven true.
   *
   * @throws IOException if a communications error occurs
   * @throws UnknownHostException if the Cyc server cannot be found
   * @throws CycApiException if the Cyc server returns an error or if there are open variables in the query
   * @throws CycTimeOutException if the calculation takes too long
   */
  @Override
  public boolean isQueryTrue(CycList query, CycObject mt,
          InferenceParameters queryProperties, long timeoutMsecs)
          throws CycConnectionException, com.cyc.base.CycApiException {
    InferenceResultSet rs = executeQuery(query, makeELMt_inner(mt), queryProperties,
            timeoutMsecs);
    try {
      return rs.getTruthValue();
    } finally {
      rs.close();
    }
  }

  /**
   * Returns true if the Cyc query (with inference parameters) is proven true.
   *
   * @param query the query expression
   * @param mt the inference microtheory
   * @param queryProperties queryProperties the list of query property keywords
   * and values, or null if the defaults are to used
   * @param timeoutMsecs the time in milliseconds to wait before giving up,
   * set to zero in order to wait forever
   *
   * @return true if the query is proven true.
   *
   * @throws IOException if a communications error occurs
   * @throws UnknownHostException if the Cyc server cannot be found
   * @throws CycApiException if the Cyc server returns an error or if there are open variables in the query
   * @throws CycTimeOutException if the calculation takes too long
   */
  @Override
  public boolean isQueryTrue(FormulaSentence query, CycObject mt,
          InferenceParameters queryProperties, long timeoutMsecs)
          throws CycConnectionException, com.cyc.base.CycApiException {
    InferenceResultSet rs = executeQuery(query, makeELMt_inner(mt), queryProperties,
            timeoutMsecs);
    try {
      return rs.getTruthValue();
    } finally {
      rs.close();
    }
  }

  /**
   * Asks a Cyc query (new inference parameters) and returns the binding list for the given variable.
   *
   * @param variable the unbound variable for which bindings are sought
   * @param query the query expression
   * @param mt the inference microtheory
   * @param queryProperties queryProperties the list of query property keywords and values, or null if the defaults are to used
   *
   * @return the binding list resulting from the given query
   *
   * @throws IOException if a communications error occurs
   * @throws UnknownHostException if the Cyc server cannot be found
   * @throws CycApiException if the Cyc server returns an error
   */
  @Override
  public CycArrayList<Object> queryVariable(final CycVariable variable,
          final CycList query, final CycObject mt,
          final InferenceParameters queryProperties)
          throws CycConnectionException, com.cyc.base.CycApiException {
    return queryVariable(variable, query, mt, queryProperties, 0);
  }

  /**
   * Asks a Cyc query (new inference parameters) and returns the binding list for the given variable.
   *
   * @param queryVariable the unbound variable for which bindings are sought
   * @param query the query expression
   * @param mt the inference microtheory
   *
   * @return the binding list resulting from the given query
   *
   * @throws IOException if a communications error occurs
   * @throws UnknownHostException if the Cyc server cannot be found
   * @throws CycApiException if the Cyc server returns an error
   */
  @Override
  public CycArrayList<Object> queryVariable(final CycVariable queryVariable,
          final FormulaSentence query, final CycObject mt)
          throws CycConnectionException, com.cyc.base.CycApiException {
    return queryVariable(queryVariable, query, mt,
            new DefaultInferenceParameters(getCyc()));
  }

  /**
   * Asks a Cyc query (new inference parameters) and returns the binding list for the given variable.
   *
   * @param queryVariable the unbound variable for which bindings are sought
   * @param query the query expression
   * @param mt the inference microtheory
   * @param queryProperties queryProperties the list of query property keywords and values, or null if the defaults are to used
   *
   * @return the binding list resulting from the given query
   *
   * @throws IOException if a communications error occurs
   * @throws UnknownHostException if the Cyc server cannot be found
   * @throws CycApiException if the Cyc server returns an error
   */
  @Override
  public CycArrayList<Object> queryVariable(final CycVariable queryVariable,
          final FormulaSentence query, final CycObject mt,
          final InferenceParameters queryProperties)
          throws CycConnectionException, com.cyc.base.CycApiException {
    final InferenceResultSet rs = executeQuery(query, makeELMt_inner(mt),
            queryProperties);
    return queryVariableLow(queryVariable, rs);
  }

  /**
   * Asks a Cyc query (new inference parameters) and returns the binding list for the given variable.
   *
   * @param queryVariable the unbound variable for which bindings are sought
   * @param query the query expression
   * @param mt the inference microtheory (given in String, CycObject or ELMt form)
   * @param queryProperties queryProperties the list of query property keywords and values, or null if the defaults are to used
   *
   * @return the binding list resulting from the given query
   *
   * @throws IOException if a communications error occurs
   * @throws UnknownHostException if the Cyc server cannot be found
   * @throws CycApiException if the Cyc server returns an error
   */
  @Override
  public CycArrayList<Object> queryVariable(final CycVariable queryVariable,
          final String query, final Object mt,
          final InferenceParameters queryProperties,
          final long timeoutMsecs)
          throws CycConnectionException, com.cyc.base.CycApiException {
    final InferenceResultSet rs = executeQuery(query, mt, queryProperties,
            timeoutMsecs);
    return queryVariableLow(queryVariable, rs);
  }

  /**
   * Asks a Cyc query (new inference parameters) and returns the binding list for the given variable.
   *
   * @param variable the unbound variable for which bindings are sought
   * @param query the query expression
   * @param mt the inference microtheory
   * @param queryProperties queryProperties the list of query property keywords and values, or null if the defaults are to used
   * @param timeoutMsecs the amount of time in milliseconds to wait before giving up, set to
   * zero in order to wait forever.
   * @return the binding list resulting from the given query
   *
   * @throws IOException if a communications error occurs
   * @throws UnknownHostException if the Cyc server cannot be found
   * @throws CycApiException if the Cyc server returns an error
   * @throws CycTimeOutException if the calculation takes too long
   */
  @Override
  public CycArrayList<Object> queryVariable(final CycVariable variable,
          final CycList query, final CycObject mt,
          final InferenceParameters queryProperties, long timeoutMsecs)
          throws CycConnectionException, com.cyc.base.CycApiException, CycTimeOutException {
    InferenceResultSet rs = null;
    try {
      rs = executeQuery(query, makeELMt_inner(mt), queryProperties, timeoutMsecs);
      return queryVariableLow(variable, rs);
    } finally {
      return new CycArrayList<Object>();
    }
  }

  @Override
  public CycArrayList<Object> queryVariableLow(final CycVariable queryVariable,
          final InferenceResultSet rs)
          throws CycConnectionException, com.cyc.base.CycApiException, CycTimeOutException {
    CycArrayList result = new CycArrayList();
    try {
      if (rs.getCurrentRowCount() == 0) {
        return result;
      }
      int colIndex = rs.findColumn(queryVariable);
      if (colIndex < 0) {
        throw new com.cyc.base.CycApiException("Unable to find variable: " + queryVariable);
      }
      while (rs.next()) {
        result.add(rs.getObject(colIndex));
      }
      return result;
    } finally {
      if (rs != null) {
        rs.close();
      }
    }
  }

  /**
   * Asks a Cyc query (new inference parameters) and returns the binding list for the given variable.
   *
   * @param variable the unbound variable for which bindings are sought
   * @param query the query expression
   * @param mt the inference microtheory
   * @param queryProperties queryProperties the list of query property keywords and values, or null if the defaults are to used
   * @param timeoutMsecs the amount of time in milliseconds to wait before giving up, set to
   * zero in order to wait forever.
   * @return the binding list resulting from the given query
   *
   * @throws IOException if a communications error occurs
   * @throws UnknownHostException if the Cyc server cannot be found
   * @throws CycApiException if the Cyc server returns an error
   * @throws CycTimeOutException if the calculation takes too long
   */
  @Override
  public CycArrayList<Object> queryVariable(final CycVariable variable,
          final FormulaSentence query, final CycObject mt,
          final InferenceParameters queryProperties, long timeoutMsecs)
          throws CycConnectionException, com.cyc.base.CycApiException, CycTimeOutException {
    InferenceResultSet rs = executeQuery(query, makeELMt_inner(mt), queryProperties,
            timeoutMsecs);
    return queryVariableLow(variable, rs);
  }

  /**
   * Asks a Cyc query (new inference parameters) and returns the binding list for the given variable.
   *
   * @param variable the unbound variable for which bindings are sought
   * @param query the query expression
   * @param mt the inference microtheory
   * @param queryProperties queryProperties the list of query property keywords and values, or null if the defaults are to used
   * @param inferenceProblemStoreName the problem store name
   *
   * @return the binding list resulting from the given query
   *
   * @throws IOException if a communications error occurs
   * @throws UnknownHostException if the Cyc server cannot be found
   * @throws CycApiException if the Cyc server returns an error
   */
  @Override
  public CycList queryVariable(final CycVariable variable,
          final CycList query,
          final CycObject mt,
          final InferenceParameters queryProperties,
          final String inferenceProblemStoreName)
          throws CycConnectionException, com.cyc.base.CycApiException {
    if (variable == null) {
      throw new NullPointerException("queryVariables must not be null");
    }
    if (query == null) {
      throw new NullPointerException("query must not be null");
    }
    if (query.isEmpty()) {
      throw new IllegalArgumentException("query must not be empty");
    }
    if (mt == null) {
      throw new NullPointerException("mt must not be null");
    }
    if (inferenceProblemStoreName == null) {
      throw new NullPointerException(
              "inferenceProblemStoreName must not be null");
    }
    if (inferenceProblemStoreName.length() == 0) {
      throw new IllegalArgumentException(
              "inferenceProblemStoreName must not be an empty list");
    }
    final InferenceParameters tempQueryProperties = (queryProperties == null) ? getHLQueryProperties() : queryProperties;
    tempQueryProperties.put(makeCycSymbol(":problem-store"), makeCycSymbol(
            "problem-store", false));
    final String script =
            "(clet ((problem-store (find-problem-store-by-name \"" + inferenceProblemStoreName + "\")))"
            + "  (query-variable " + variable.stringApiValue() + " "
            + query.stringApiValue() + " " + makeELMt_inner(mt).stringApiValue() + " " + queryPropertiesToString(
            tempQueryProperties) + "))";
    return getConverse().converseList(script);
  }

  /**
   * Asks a Cyc query (new inference parameters) and returns the binding list for the given variable list.
   *
   * @param variables the list of unbound variables for which bindings are sought
   * @param query the query expression
   * @param mt the inference microtheory
   * @param queryProperties queryProperties the list of query property keywords and values, or null if the defaults are to be used
   *
   * @return the binding list resulting from the given query
   *
   * @throws IOException if a communications error occurs
   * @throws UnknownHostException if the Cyc server cannot be found
   * @throws CycApiException if the Cyc server returns an error
   */
  @Override
  public CycList<Object> queryVariables(final CycList<CycVariable> variables,
          final CycList<Object> query,
          final CycObject mt,
          final InferenceParameters queryProperties)
          throws CycConnectionException, com.cyc.base.CycApiException {
    // @todo use inference property timeout rather than 0 if given
    return queryVariablesLow(variables, query, mt, queryProperties, 0);
  }

  /**
   * Asks a Cyc query (new inference parameters) and returns the binding list for the given variable list.
   *
   * @param variables the list of unbound variables for which bindings are sought
   * @param query the query expression
   * @param mt the inference microtheory
   * @param queryProperties queryProperties the list of query property keywords and values, or null if the defaults are to be used
   *
   * @return the binding list resulting from the given query
   *
   * @throws IOException if a communications error occurs
   * @throws UnknownHostException if the Cyc server cannot be found
   * @throws CycApiException if the Cyc server returns an error
   */
  @Override
  public CycArrayList<Object> queryVariables(final CycList<CycVariable> variables,
          final FormulaSentence query,
          final CycObject mt,
          final InferenceParameters queryProperties)
          throws CycConnectionException, com.cyc.base.CycApiException {
    return queryVariablesLow(variables, query, mt, queryProperties, 0);
  }

  /**
   * Asks a Cyc query (new inference parameters) and returns the binding list for the given variable list.
   *
   * @param variables the list of unbound variables for which bindings are sought
   * @param query the query expression
   * @param mt the inference microtheory
   * @param queryProperties queryProperties the list of query property keywords and values, or null if the defaults are to be used
   *
   * @return the binding list resulting from the given query
   *
   * @throws IOException if a communications error occurs
   * @throws UnknownHostException if the Cyc server cannot be found
   * @throws CycApiException if the Cyc server returns an error
   */
  @SuppressWarnings("unchecked")
  @Override
  public CycArrayList<Object> queryVariables(final CycList<CycVariable> variables,
          final String query,
          final Object mt,
          final InferenceParameters queryProperties,
          final long timeoutMsecs)
          throws CycConnectionException, com.cyc.base.CycApiException {
    final String command = makeSubLStmt("query-template", variables, query,
            getCyc().getObjectTool().makeELMt(mt), queryProperties);
    SubLWorkerSynch worker = new DefaultSubLWorkerSynch(command, getCyc(),
            timeoutMsecs);
    if (CycObjectFactory.nil.equals(worker.getWork())) {
      return new CycArrayList<Object>();
    }
    return (CycArrayList<Object>) worker.getWork();
  }

  /**
   * Asks a Cyc query (new inference parameters) and returns the binding list for the given variable list.
   *
   * @param queryVariables the list of unbound variables for which bindings are sought
   * @param query the query expression
   * @param mt the inference microtheory
   * @param queryProperties queryProperties the list of query property keywords and values, or null if the defaults are to be used
   * @param inferenceProblemStoreName the problem store name
   *
   * @return the binding list resulting from the given query
   *
   * @throws IOException if a communications error occurs
   * @throws UnknownHostException if the Cyc server cannot be found
   * @throws CycApiException if the Cyc server returns an error
   */
  @Override
  public CycArrayList queryVariables(final CycList queryVariables,
          final CycList query,
          final CycObject mt,
          final InferenceParameters queryProperties,
          final String inferenceProblemStoreName)
          throws CycConnectionException, com.cyc.base.CycApiException {
    if (query.isEmpty()) {
      throw new IllegalArgumentException("query must not be empty");
    }
    return queryVariablesInternal(queryVariables, query, mt, queryProperties,
            inferenceProblemStoreName, 0);
  }

  /**
   * Asks a Cyc query (new inference parameters) and returns the binding list for the given variable list.
   *
   * @param queryVariables the list of unbound variables for which bindings are sought
   * @param query the query expression
   * @param mt the inference microtheory
   * @param queryProperties queryProperties the list of query property keywords and values, or null if the defaults are to be used
   * @param inferenceProblemStoreName the problem store name
   *
   * @return the binding list resulting from the given query
   *
   * @throws IOException if a communications error occurs
   * @throws UnknownHostException if the Cyc server cannot be found
   * @throws CycApiException if the Cyc server returns an error
   */
  @Override
  public CycArrayList queryVariables(final CycList<CycVariable> queryVariables,
          final FormulaSentence query,
          final CycObject mt,
          final InferenceParameters queryProperties,
          final String inferenceProblemStoreName)
          throws CycConnectionException, com.cyc.base.CycApiException {
    return queryVariablesInternal(queryVariables, query, mt, queryProperties,
            inferenceProblemStoreName, 0);
  }

  /**
   * Asks a Cyc query and returns the binding list. Properties:
   *
   * @param query the query expression
   * @param mt the inference microtheory
   * @param maxTransformationDepth the Integer maximum transformation depth or nil for no limit
   * @param maxNumber the Integer maximum number of returned bindings or nil for no limit
   * @param maxTimeSeconds the Integer maximum number of seconds inference duration or nil for no
   * limit
   * @param maxProofDepth the Integer maximum number of levels in the proof tree or nil for no
   * limit
   *
   * @return the binding list of answers for the given query and inference property settings
   *
   * @throws IOException if a communication error occurs
   * @throws UnknownHostException if the Cyc server cannot be found
   * @throws CycApiException if the Cyc server returns an error
   */
  @Override
  public CycList askCycQuery(CycList query,
          CycObject mt,
          Object maxTransformationDepth,
          Object maxNumber,
          Object maxTimeSeconds,
          Object maxProofDepth)
          throws CycConnectionException, com.cyc.base.CycApiException {
    HashMap queryProperties = new HashMap();
    queryProperties.put(makeCycSymbol(
            ":max-transformation-depth"),
            maxTransformationDepth);
    queryProperties.put(makeCycSymbol(
            ":max-number"),
            maxNumber);
    queryProperties.put(makeCycSymbol(
            ":max-time"),
            maxTimeSeconds);
    queryProperties.put(makeCycSymbol(
            ":max-proof-depth"),
            maxProofDepth);
    queryProperties.put(makeCycSymbol(
            ":forget-extra-results?"),
            CycObjectFactory.t);

    return askCycQuery(query,
            mt,
            queryProperties);
  }

  /**
   * Asks a Cyc query and returns the binding list.
   *
   * @deprecated use
   * <code>executeQuery</code>
   * @param query the query expression
   * @param mt the inference microtheory
   * @param queryProperties queryProperties the list of query property keywords and values
   *
   * @return the binding list resulting from the given query
   *
   * @throws IOException if a communications error occurs
   * @throws UnknownHostException if the Cyc server cannot be found
   * @throws CycApiException if the Cyc server returns an error
   */
  public CycList askCycQuery(CycList query, CycObject mt,
          HashMap queryProperties)
          throws CycConnectionException, com.cyc.base.CycApiException {
    CycArrayList parameterList = new CycArrayList();
    Iterator iter = queryProperties.entrySet().iterator();

    if (iter.hasNext()) {
      while (iter.hasNext()) {
        Map.Entry mapEntry = (Map.Entry) iter.next();
        CycSymbolImpl queryParameterKeyword = (CycSymbolImpl) mapEntry.getKey();
        parameterList.add(queryParameterKeyword);

        Object queryParameterValue = mapEntry.getValue();
        parameterList.add(queryParameterValue);
      }
    }
    String command = makeSubLStmt(CYC_QUERY, query, makeELMt_inner(mt), parameterList);

    return getConverse().converseList(command);
  }

  /**
   * Returns a list of bindings for a query with a single unbound variable.
   *
   * @param query the query to be asked in the knowledge base
   * @param variable the single unbound variable in the query for which bindings are sought
   * @param mt the microtheory in which the query is asked
   *
   * @return a list of bindings for the query
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public CycList queryVariable(final CycList query,
          final CycVariable variable,
          final CycObject mt)
          throws CycConnectionException, com.cyc.base.CycApiException {
    StringBuilder queryBuffer = new StringBuilder();
    queryBuffer.append("(clet ((*cache-inference-results* nil) ");
    queryBuffer.append("       (*compute-inference-results* nil) ");
    queryBuffer.append("       (*unique-inference-result-bindings* t) ");
    queryBuffer.append("       (*generate-readable-fi-results* nil)) ");
    queryBuffer.append("  (without-wff-semantics ");
    queryBuffer.append("    (ask-template ").append(variable.stringApiValue()).append(
            " ");
    queryBuffer.append("                  ").append(query.stringApiValue()).append(
            " ");
    queryBuffer.append("                  ").append(makeELMt_inner(
            mt).stringApiValue()).append(" ");
    queryBuffer.append("                  0 nil nil nil)))");

    CycList answer = getConverse().converseList(queryBuffer.toString());

    return getCyc().getObjectTool().canonicalizeList(answer);
  }

  /**
   * Returns a list of bindings for a query with a single unbound variable.
   *
   * @deprecated use queryVariable
   * @param query the query to be asked in the knowledge base
   * @param variable the single unbound variable in the query for which bindings are sought
   * @param mt the microtheory in which the query is asked
   *
   * @return a list of bindings for the query
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  public CycList askWithVariable(CycList query,
          CycVariable variable,
          CycObject mt)
          throws CycConnectionException, com.cyc.base.CycApiException {
    StringBuilder queryBuffer = new StringBuilder();
    queryBuffer.append("(clet ((*cache-inference-results* nil) ");
    queryBuffer.append("       (*compute-inference-results* nil) ");
    queryBuffer.append("       (*unique-inference-result-bindings* t) ");
    queryBuffer.append("       (*generate-readable-fi-results* nil)) ");
    queryBuffer.append("  (without-wff-semantics ");
    queryBuffer.append("    (ask-template ").append(variable.stringApiValue()).append(
            " ");
    queryBuffer.append("                  ").append(query.stringApiValue()).append(
            " ");
    queryBuffer.append("                  ").append(makeELMt_inner(
            mt).stringApiValue()).append(" ");
    queryBuffer.append("                  0 nil nil nil)))");

    CycList answer = getConverse().converseList(queryBuffer.toString());

    return getCyc().getObjectTool().canonicalizeList(answer);
  }

  /**
   * Returns a list of bindings for a query with unbound variables. The bindings each consist of a
   * list in the order of the unbound variables list parameter, in which each bound term is the
   * binding for the corresponding variable.
   *
   * @deprecated use queryVariables
   * @param query the query to be asked in the knowledge base
   * @param variables the list of unbound variables in the query for which bindings are sought
   * @param mt the microtheory in which the query is asked
   *
   * @return a list of bindings for the query
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  public CycList askWithVariables(CycList query,
          List variables,
          CycObject mt)
          throws CycConnectionException, com.cyc.base.CycApiException {
    StringBuilder queryBuffer = new StringBuilder();
    queryBuffer.append("(clet ((*cache-inference-results* nil) ");
    queryBuffer.append("       (*compute-inference-results* nil) ");
    queryBuffer.append("       (*unique-inference-result-bindings* t) ");
    queryBuffer.append("       (*generate-readable-fi-results* nil)) ");
    queryBuffer.append("  (without-wff-semantics ");
    queryBuffer.append("    (ask-template ").append(
            (new CycArrayList(variables)).stringApiValue()).append(" ");
    queryBuffer.append("                  ").append(query.stringApiValue()).append(
            " ");
    queryBuffer.append("                  ").append(mt.stringApiValue()).append(
            " ");
    queryBuffer.append("                  0 nil nil nil)))");

    CycList bindings = getConverse().converseList(queryBuffer.toString());
    CycArrayList canonicalBindings = new CycArrayList();
    Iterator iter = bindings.iterator();

    while (iter.hasNext()) {
      canonicalBindings.add(getCyc().getObjectTool().canonicalizeList((CycArrayList) iter.next()));
    }
    return canonicalBindings;
  }

  /**
   * Returns <tt>true</tt> iff the query is true in the knowledge base.
   *
   * @deprecated
   * @param query the query to be asked in the knowledge base
   * @param mt the microtheory in which the query is asked
   *
   * @return <tt>true</tt> iff the query is true in the knowledge base
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isQueryTrue(CycList query, CycObject mt)
          throws CycConnectionException, com.cyc.base.CycApiException {
    String command = makeSubLStmt(CYC_QUERY, getCyc().getObjectTool().canonicalizeList(query), makeELMt_inner(
            mt));
    CycList response = getConverse().converseList(command);

    return response.size() > 0;
  }

  /**
   * Returns <tt>true</tt> iff the query is true in the knowledge base, implements a cache to avoid
   * asking the same question twice from the KB.
   *
   * @deprecated
   * @param query the query to be asked in the knowledge base
   * @param mt the microtheory in which the query is asked
   *
   * @return <tt>true</tt> iff the query is true in the knowledge base
   *
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isQueryTrue_Cached(CycList query,
          CycObject mt)
          throws CycConnectionException, com.cyc.base.CycApiException {
    Boolean isQueryTrue = askCache.get(query);

    if (isQueryTrue != null) {
      return isQueryTrue.booleanValue();
    }

    final boolean answer = isQueryTrue(query, makeELMt_inner(mt));
    askCache.put(query, answer);

    return answer;
  }
  
  /**
   * Initializes a named inference problem store.
   *
   * @param name the unique problem store name
   * @param queryProperties the given query properties or null if the defaults are to be used
   */
  @Override
  public void initializeNamedInferenceProblemStore(final String name,
          final InferenceParameters queryProperties) throws CycConnectionException, com.cyc.base.CycApiException {
    //// Preconditions
    if (name == null) {
      throw new NullPointerException("name must not be null");
    }
    if (name.length() == 0) {
      throw new IllegalArgumentException("name must not be an empty string");
    }
    final InferenceParameters tempQueryProperties = (queryProperties == null) ? getHLQueryProperties() : queryProperties;
    final String command =
            "(progn "
            + "  (find-or-create-problem-store-by-name \"" + name + "\" (filter-plist " + queryPropertiesToString(
            tempQueryProperties) + "'problem-store-property-p)) "
            + "  nil)";
    getConverse().converseVoid(command);
  }

  /** Destroys the named problem store.
   *
   * @param name the unique problem store name
   * @throws com.cyc.base.CycConnectionException
   */
  @Override
  public void destroyInferenceProblemStoreByName(final String name) throws CycConnectionException, com.cyc.base.CycApiException {
    //// Preconditions
    if (name == null) {
      throw new NullPointerException("name must not be null");
    }
    if (name.length() == 0) {
      throw new IllegalArgumentException("name must not be an empty string");
    }
    final String command =
            "(destroy-problem-store-by-name \"" + name + "\")";
    getConverse().converseVoid(command);
  }
  
  /** 
   * Returns a clone of the default HL query propoerties.
   *
   * @return the default HL query propoerties
   */
  @Override
  public InferenceParameters getHLQueryProperties() {
    synchronized (defaultQueryProperties) {
      if (!queryPropertiesInitialized) {
        initializeQueryProperties();
      }
      return (InferenceParameters) defaultQueryProperties.clone();
    }
  }
  
  /** 
   * Determines whether the two input queries are equal EL expressions.
   * @throws com.cyc.base.CycConnectionException
   */
  @Override
  public boolean areQueriesEqualAtEL(Object obj1, Object obj2) throws CycConnectionException {
    String command = makeSubLStmt("queries-equal-at-el?", obj1, obj2);
    // execute the SubL function-call and access the response
    Object response = getConverse().converseObject(command);
    return !response.equals(CycObjectFactory.nil);
  }
  
  
  
  // Private
  
  private CycArrayList queryVariablesInternal(final CycList queryVariables,
          final CycObject query,
          final CycObject mt,
          final InferenceParameters queryProperties,
          final String inferenceProblemStoreName,
          final long timeoutMsecs)
          throws CycConnectionException, com.cyc.base.CycApiException {
    //// Preconditions
    if (queryVariables == null) {
      throw new NullPointerException("queryVariables must not be null");
    }
    if (queryVariables.isEmpty()) {
      throw new IllegalArgumentException("queryVariables must not be empty");
    }
    if (query == null) {
      throw new NullPointerException("query must not be null");
    }
    if (mt == null) {
      throw new NullPointerException("mt must not be null");
    }
    if (inferenceProblemStoreName == null) {
      throw new NullPointerException(
              "inferenceProblemStoreName must not be null");
    }
    if (inferenceProblemStoreName.length() == 0) {
      throw new IllegalArgumentException(
              "inferenceProblemStoreName must not be an empty list");
    }
    final InferenceParameters tempQueryProperties = (queryProperties == null) ? getHLQueryProperties() : queryProperties;
    tempQueryProperties.put(makeCycSymbol(":problem-store"), makeCycSymbol(
            "problem-store", false));
    final String script =
            "(clet ((problem-store (find-problem-store-by-name \"" + inferenceProblemStoreName + "\")))"
            + "  (query-template " + queryVariables.stringApiValue() + " "
            + query.stringApiValue() + " " + makeELMt_inner(mt).stringApiValue() + " " + queryPropertiesToString(
            tempQueryProperties) + "))";
    SubLWorkerSynch worker = new DefaultSubLWorkerSynch(script, getCyc(),
            timeoutMsecs);
    if (CycObjectFactory.nil.equals(worker.getWork())) {
      return new CycArrayList<Object>();
    }
    return (CycArrayList) worker.getWork();
  }
  
  private CycArrayList<Object> queryVariablesLow(
          final CycList<CycVariable> queryVariables,
          final CycObject query,
          final CycObject mt,
          final InferenceParameters queryProperties,
          final long timeoutMsecs)
          throws CycConnectionException, com.cyc.base.CycApiException {
    final String command = makeSubLStmt("query-template", queryVariables, query,
            makeELMt_inner(mt), queryProperties);
    SubLWorkerSynch worker = new DefaultSubLWorkerSynch(command, getCyc(),
            timeoutMsecs);
    if (CycObjectFactory.nil.equals(worker.getWork())) {
      return new CycArrayList<Object>();
    }
    return (CycArrayList<Object>) worker.getWork();
  }

  
  private String queryResultsToXMLStringInternal(CycObject query,
          CycObject mt,
          InferenceParameters queryProperties,
          CycList xmlSpec)
          throws CycConnectionException, com.cyc.base.CycApiException {
    String xmlSpecString = (xmlSpec == null) ? ":default" : xmlSpec.stringApiValue();
    final String script =
            "(query-results-to-xml-string " + query.stringApiValue() + " " + makeELMt_inner(
            mt).stringApiValue() + " " + queryPropertiesToString(queryProperties) + " " + xmlSpecString + ")";
    return getConverse().converseString(script);
  }
  
  private boolean isOkForNewCycQuery(final InferenceParameters queryProperties,
          final long timeoutMsecs) {
    if (timeoutMsecs != 0) {
      return false; //timeouts require subl workers.
    } else if (queryProperties == null) {
      return true; //default properties are okay.
    } else if (queryProperties.containsKey(CycObjectFactory.makeCycSymbol(
            ":RETURN"))) {
      return false; //we rely on the standard return format.
    } else {
      return true;
    }
  }
  
  /** 
   * Initializes the query properties. 
   */
  private void initializeQueryProperties() {
    defaultQueryProperties.put(makeCycSymbol(":allowed-rules"), makeCycSymbol(
            ":all"));
    defaultQueryProperties.put(makeCycSymbol(":result-uniqueness"),
            makeCycSymbol(":bindings"));
    defaultQueryProperties.put(makeCycSymbol(
            ":allow-hl-predicate-transformation?"), false);
    defaultQueryProperties.put(makeCycSymbol(
            ":allow-unbound-predicate-transformation?"), false);
    defaultQueryProperties.put(makeCycSymbol(
            ":allow-evaluatable-predicate-transformation?"), false);
    defaultQueryProperties.put(makeCycSymbol(
            ":intermediate-step-validation-level"), makeCycSymbol(":all"));
    defaultQueryProperties.put(makeCycSymbol(":negation-by-failure?"), false);
    defaultQueryProperties.put(makeCycSymbol(":allow-indeterminate-results?"),
            true);
    defaultQueryProperties.put(makeCycSymbol(":allow-abnormality-checking?"),
            true);
    defaultQueryProperties.put(makeCycSymbol(":disjunction-free-el-vars-policy"),
            makeCycSymbol(":compute-intersection"));
    defaultQueryProperties.put(makeCycSymbol(":allowed-modules"), makeCycSymbol(
            ":all"));
    defaultQueryProperties.put(makeCycSymbol(
            ":completeness-minimization-allowed?"), true);
    defaultQueryProperties.put(makeCycSymbol(":direction"), makeCycSymbol(
            ":backward"));
    defaultQueryProperties.put(makeCycSymbol(":equality-reasoning-method"),
            makeCycSymbol(":czer-equal"));
    defaultQueryProperties.put(makeCycSymbol(":equality-reasoning-domain"),
            makeCycSymbol(":all"));
    defaultQueryProperties.put(makeCycSymbol(":max-problem-count"),
            Long.valueOf(100000));
    defaultQueryProperties.put(makeCycSymbol(":transformation-allowed?"), false);
    defaultQueryProperties.put(makeCycSymbol(
            ":add-restriction-layer-of-indirection?"), true);
    defaultQueryProperties.put(makeCycSymbol(":evaluate-subl-allowed?"), true);
    defaultQueryProperties.put(makeCycSymbol(":rewrite-allowed?"), false);
    defaultQueryProperties.put(makeCycSymbol(":abduction-allowed?"), false);
    defaultQueryProperties.put(makeCycSymbol(
            ":removal-backtracking-productivity-limit"), Long.valueOf(2000000));
    // dynamic query properties
    defaultQueryProperties.put(makeCycSymbol(":max-number"), null);
    defaultQueryProperties.put(makeCycSymbol(":max-time"), Integer.valueOf(120));
    defaultQueryProperties.put(makeCycSymbol(":max-transformation-depth"),
            Integer.valueOf(0));
    defaultQueryProperties.put(makeCycSymbol(":block?"), false);
    defaultQueryProperties.put(makeCycSymbol(":max-proof-depth"), null);
    defaultQueryProperties.put(makeCycSymbol(":cache-inference-results?"), false);
    defaultQueryProperties.put(makeCycSymbol(":answer-language"), makeCycSymbol(
            ":el"));
    defaultQueryProperties.put(makeCycSymbol(":continuable?"), false);
    defaultQueryProperties.put(makeCycSymbol(":browsable?"), false);
    defaultQueryProperties.put(makeCycSymbol(":productivity-limit"),
            Long.valueOf(2000000));

    final CycArrayList<CycSymbolImpl> queryPropertiesList = new CycArrayList(
            defaultQueryProperties.keySet());
    final String command = makeSubLStmt("mapcar", makeCycSymbol(
            "query-property-p"), queryPropertiesList);
    try {
      CycList results = getConverse().converseList(command);
      for (int i = 0, size = results.size(); i < size; i++) {
        if (results.get(i).equals(CycObjectFactory.nil)) {
          final CycSymbolImpl badProperty = queryPropertiesList.get(i);
          System.err.println(badProperty + " is not a query-property-p");
          defaultQueryProperties.remove(badProperty);
        }
      }
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
    queryPropertiesInitialized = true;
  }

  /** Migrate to this once inference parameter definitions are included in OpenCyc KB. */
  private void initializeQueryPropertiesNew() {
    synchronized (defaultQueryProperties) {
      defaultQueryProperties.clear();
      try {
        final InferenceParameterDescriptions desc = DefaultInferenceParameterDescriptions.loadInferenceParameterDescriptions(
                getCyc(), 10000);
        final InferenceParameters defaults = desc.getDefaultInferenceParameters();
        final CycList allQueryProperties = getConverse().converseList(makeSubLStmt(
                "ALL-QUERY-PROPERTIES"));
        for (final Object property : allQueryProperties) {
          if (property instanceof CycSymbolImpl && defaults.containsKey(
                  (CycSymbolImpl) property)) {
            final Object value = defaults.get((CycSymbolImpl) property);
            defaultQueryProperties.put((CycSymbolImpl) property, value);
          }
        }
      } catch (CycConnectionException ex) {
        Logger.getLogger(CycInferenceTool.class.getName()).log(Level.SEVERE, null, ex);
      } catch (com.cyc.base.CycApiException ex) {
        Logger.getLogger(CycInferenceTool.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    queryPropertiesInitialized = true;
  }
  
  
  // Internal
  
  private static final CycSymbolImpl CYC_QUERY = makeCycSymbol("cyc-query");

  /** 
   * default query properties, initialized by initializeQueryProperties().
   */
  private final InferenceParameters defaultQueryProperties = new DefaultInferenceParameters(
          getCyc());
  
  private boolean queryPropertiesInitialized = false;
  
  /** 
   * Least Recently Used Cache of ask results.
   */
  private final Map<CycList, Boolean> askCache = new LRUCache<CycList, Boolean>(
          500, 5000, true);
}
