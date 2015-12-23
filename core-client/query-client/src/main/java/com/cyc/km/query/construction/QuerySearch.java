package com.cyc.km.query.construction;

/*
 * #%L
 * File: QuerySearch.java
 * Project: Query Client
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
import com.cyc.base.CycAccess;
import com.cyc.base.CycAccessManager;
import com.cyc.base.exception.CycApiException;
import com.cyc.base.exception.CycConnectionException;
import com.cyc.base.cycobject.CycList;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.CycSymbol;
import com.cyc.baseclient.CycObjectFactory;
import com.cyc.baseclient.connection.SublApiHelper;
import com.cyc.baseclient.connection.SublApiHelper.AsIsTerm;
import com.cyc.baseclient.cycobject.NautImpl;
import com.cyc.km.modeling.task.CycBasedTask;
import com.cyc.kb.KbObject;
import com.cyc.kb.client.KbCollectionImpl;
import com.cyc.kb.client.KbIndividualImpl;
import com.cyc.kb.client.KbObjectImpl;
import com.cyc.nl.Span;
import com.cyc.kb.exception.KbException;
import com.cyc.query.Query;
import com.cyc.query.QueryReader;
import com.cyc.query.exception.QueryRuntimeException;
import com.cyc.query.exception.QueryConstructionException;
import com.cyc.session.exception.SessionException;
import com.cyc.session.compatibility.CycSessionRequirementList;
import com.cyc.session.compatibility.NotOpenCycRequirement;
import com.cyc.session.exception.OpenCycUnsupportedFeatureException;
import com.cyc.xml.query.CyclQuery;
import com.cyc.xml.query.FormulaTemplate;
import com.cyc.xml.query.FormulaTemplateUnmarshaller;

import java.io.ByteArrayInputStream;
import java.util.*;

import javax.xml.bind.JAXBException;

/**
 * QuerySearch is a class encapsulating the parameters and results of the
 * process of searching for <code>Queries</code> based on an NL string. Terms
 * that may be mentioned in the string are identified, and generally small
 * "fragment" queries using them are constructed. The results can be combined
 * interactively to construct a query corresponding to the overall input string.
 * <p>
 QueryImpl searches are performed within the context of a {@link CycBasedTask},
 * which defines filters, etc., to guide the search results to task-relevant
 * terms.
 *
 * @see com.cyc.query.QueryImpl
 * @author David Baxter
 *
 */
public class QuerySearch {
  
  public static final CycSessionRequirementList<OpenCycUnsupportedFeatureException> QUERY_SEARCH_REQUIREMENTS = CycSessionRequirementList.fromList(
          NotOpenCycRequirement.NOT_OPENCYC
  );
  
  /**
   * Creates a new query search with the specified search string and task.
   *
   * @param searchString A natural language string (usually a question or other
   * sort of request for information)
   * @param task Provides the context for interpreting searchString as a set of
   * queries.
   * @throws com.cyc.session.exception.OpenCycUnsupportedFeatureException when run against an OpenCyc server.
   */
  public QuerySearch(String searchString, CycBasedTask task) throws OpenCycUnsupportedFeatureException {
    this(searchString, task, VariableFormat.DEFAULT);
  }

  /**
   * Creates a new query search with the specified search string, task, and
   * variable format.
   *
   * @param searchString A natural language string (usually a question or other
   * sort of request for information)
   * @param task Provides the context for interpreting searchString as a set of
   * queries.
   * @param format The {@link VariableFormat} used for queries constructed by
   * the search.
   * @throws com.cyc.session.exception.OpenCycUnsupportedFeatureException when run against an OpenCyc server.
   */
  public QuerySearch(String searchString, CycBasedTask task, VariableFormat format) throws OpenCycUnsupportedFeatureException {
    QUERY_SEARCH_REQUIREMENTS.throwRuntimeExceptionIfIncompatible();
    this.searchString = searchString;
    this.task = task;
    this.variableFormat = format;
  }

  /**
   * Returns the task associated with this search.
   *
   * @return the task associated with this search.
   */
  public CycBasedTask getTask() {
    return task;
  }

  /**
   * Returns the search string associated with this search.
   *
   * @return the string for which matching queries are sought.
   */
  public String getSearchString() {
    return searchString;
  }

  /**
   * Returns the terms identified in the search string. The results are filtered
   * and focused based on the {@link CycBasedTask} associated with this search.
   * <p>
   * Unlike {@link #getQueries()}, the results of this search need to be
   * incorporated into queries, e.g. by constructing {@link com.cyc.kb.Sentence}
   * objects.
   *
   * @see #getTask()
   * @return the terms identified in the search string.
   */
  public Collection<KbObject> getTerms() {
    return termLocations.getKeys();
  }

  /**
   * Returns a {@link Map} mapping terms identified in the search string to the
   * {@link Span}s where they were found. Each {@link java.util.Map.Entry} can
   * be considered a situated term, combining the {@link KbObject} with its
   * location information.
   *
   * @see #getTerms()
   * @return the map
   */
  public Map<KbObject, Collection<Span>> getSituatedTerms() {
    termLocations.maybeInitialize();
    return Collections.unmodifiableMap(termLocations);
  }

  /**
   * Returns the locations of the specified term in the search string.
   *
   * @param term A term identified in the search string.
   * @see com.cyc.km.query.construction.QuerySearch#getTerms().
   * @return the locations
   */
  public Collection<Span> getTermLocations(KbObject term) {
    return termLocations.getLocations(term);
  }

  /**
   * Returns the <code>Queries</code> identified with the search string. The
   * results are filtered and focused based on the {@link CycBasedTask}
   * associated with this search.
   * <p>
   * Unlike {@link #getTerms()}, the results of this search are full-fledged
   * queries that can be run, although they typically must be combined via
   * {@link Query#merge(com.cyc.query.Query)} to cover the full meaning of the
   * input string.
   *
   * @return the queries found in the search string.
   */
  public Collection<Query> getQueries() {
    return queryLocations.getKeys();
  }

  /**
   * Returns the locations of a <code>QueryImpl</code> identified with the search
   * string.
   *
   * @param query A particular <code>QueryImpl</code> that was found somewhere in
   * this <code>QuerySearch</code>
   * @return start, end indices for locations of query in search string.
   */
  public Collection<Span> getQueryLocations(Query query) {
    return queryLocations.getLocations(query);
  }

  /**
   * Returns a {@link Map} mapping queries identified in the search string to
   * the {@link Span}s where they were found. Each {@link java.util.Map.Entry}
   * can be considered a situated query, combining the {@link KbObject} with its
   * location information.
   *
   * @see #getQueries()
   * @return the map
   */
  public Map<Query, Collection<Span>> getSituatedQueries() {
    queryLocations.maybeInitialize();
    return Collections.unmodifiableMap(queryLocations);
  }

  // Private Area
  private CycList getCycTermSearchResults(final CycAccess cyc) throws CycConnectionException {
    final AsIsTerm coreCommand = SublApiHelper.makeNestedSubLStmt(
            "get-terms-from-cae-query-search", searchString);
    final String command = wrapTask(coreCommand);
    final CycList response = cyc.converse().converseList(command);
    return response;
  }

  private void processOneFolder(final CycList folderData) throws KbException, QueryConstructionException {
    final String folderName = (String) folderData.getf(FOLDER);
    for (int i = 2; i < folderData.size(); i++) {
      processQueryDataForFolder((CycList) folderData.get(i), folderName);
    }
  }

  private void processQueryDataForFolder(final CycList queryData,
          final String category) throws KbException, QueryConstructionException {
    final Integer offset = (Integer) queryData.getf(OFFSET);
    final Integer end = (Integer) queryData.getf(END);
    for (final Object cyclQuery : extractQueriesAndFormulas(queryData)) {
      if (cyclQuery instanceof CyclQuery) {
        processOneQuery((CyclQuery) cyclQuery, category, offset, end);
      }
    }
  }

  private List<Object> extractQueriesAndFormulas(final CycList queryData) {
    try {
      final String xml = (String) queryData.getf(XML);
      final FormulaTemplate formulaTemplate = (FormulaTemplate) formulaTemplateUnmarshaller.get().unmarshal(
              new ByteArrayInputStream(xml.getBytes()));
      final List<Object> queriesOrFormulas = formulaTemplate.getCyclQueryOrFormula();
      return queriesOrFormulas;
    } catch (JAXBException ex) {
      throw new QueryRuntimeException(ex);
    }
  }

  private void processOneQuery(final CyclQuery cyclQuery,
          final String category,
          final Integer offset, final Integer end) throws KbException,
          QueryConstructionException {
    final Query query = queryReader.get().convertQuery(cyclQuery);
    query.addCategory(category);
    queryLocations.get(query).add(new Span(offset, end));
  }

  private CycList getCycQuerySearchResults() throws SessionException {
    final CycAccess cyc = getCyc();
    final AsIsTerm coreCommand = SublApiHelper.makeNestedSubLStmt(
            "indexed-queries-from-string-with-confidences", searchString,
            CycObjectFactory.nil, variableFormat.getSymbol());
    final String command = requireNamespace(wrapTask(coreCommand));
    try {
      return cyc.converse().converseList(command);
    } catch (CycApiException e) {
      throw new QueryRuntimeException(e);
    } catch (CycConnectionException e) {
      throw new QueryRuntimeException(e);
    }
  }

  private void processOneTermSearchResult(final Object obj, final CycAccess cyc) {
    final CycList substringData = (CycList) obj;
    final Integer offset = (Integer) substringData.getf(OFFSET);
    final Integer end = (Integer) substringData.getf(END);
    for (final CycObject denot : (List<CycObject>) substringData.getf(
            DENOTATIONS)) {
      processOneTermSearchResultDenot(cyc, NautImpl.convertIfPromising(denot),
              offset, end);
    }
  }

  private void processOneTermSearchResultDenot(final CycAccess cyc,
          final Object denot, final Integer offset, final Integer end) {
    try {
      final String denotString = denot.toString();
      final KbObject term = (cyc.getInspectorTool().isCollection(denot))
              ? KbCollectionImpl.get(denotString)
              : (denot instanceof CycObject && cyc.getInspectorTool().isIndividual(
                      (CycObject) denot))
                      ? KbIndividualImpl.get(denotString)
                      //: KBObjectFactory.getKBObject((CycObject) denot, KBObject.class);
                      : KbObjectImpl.get((CycObject) denot);
      termLocations.get(term).add(new Span(offset, end));
    } catch (Exception e) {
      System.err.println("Couldn't find or create KBObject for " + denot);
      e.printStackTrace(System.err);
    }
  }

  private String requireNamespace(String command) {
    return SublApiHelper.wrapVariableBinding(
            command, CycObjectFactory.makeCycSymbol("*formula-template-include-namespace?*"),
            CycObjectFactory.makeCycSymbol("T"));
  }

  private String wrapTask(final AsIsTerm coreCommand) {
    return "(with-current-cae-task " + task.getTaskTerm().stringApiValue()
            + "\n "
            + coreCommand + ")";
  }

  private CycAccess getCyc() throws SessionException {
    return CycAccessManager.getCurrentAccess();
  }
  private final String searchString;
  private final CycBasedTask task;
  private final VariableFormat variableFormat;

  public enum VariableFormat {

    /**
     * Whatever the default for this Cyc is.
     */
    DEFAULT,
    
    /**
     * Variable names are based on variable types, e.g. <nobr>?DOG</nobr>,
     * <nobr>?CAT</nobr>.
     */
    MNEMONIC,
    
    /**
     * Variable names are single letters, e.g. ?X, ?Y, ?Z.
     */
    XYZ;

    private CycSymbol getSymbol() {
      return CycObjectFactory.makeCycSymbol(":" + name());
    }
  }

  private abstract class LocationIndex<K> extends HashMap<K, Collection<Span>> {

    private LocationIndex() {
      super();
      put(null, null);
    }

    private boolean isVirgin() {
      return containsKey(null);
    }

    private void maybeInitialize() {
      synchronized (this) {
        if (isVirgin()) {
          initialize();
        }
      }
    }

    private Collection<K> getKeys() {
      maybeInitialize();
      return Collections.unmodifiableCollection(keySet());
    }

    private Collection<Span> getLocations(final K key) {
      maybeInitialize();
      return Collections.unmodifiableCollection(get(key));
    }

    private void initialize() {
      clear();
      populate();
    }

    protected abstract void populate();

    @Override
    public Collection<Span> get(Object key) {
      synchronized (QuerySearch.this) {
        Collection<Span> spans = super.get(key);
        if (spans == null) {
          spans = new HashSet<Span>();
          put((K) key, spans);
        }
        return spans;
      }
    }
  }
  private final LocationIndex<Query> queryLocations = new LocationIndex<Query>() {
    @Override
    protected void populate() {
      try {
        for (final Object obj : getCycQuerySearchResults()) {
          processOneFolder((CycList) obj);
        }
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    }
  };
  private final LocationIndex<KbObject> termLocations = new LocationIndex<KbObject>() {
    @Override
    protected void populate() {
      try {
        final CycAccess cyc = getCyc();
        for (final Object obj : getCycTermSearchResults(cyc)) {
          processOneTermSearchResult(obj, cyc);
        }
      } catch (CycApiException e) {
        // @TODO Handle exceptions better.
        e.printStackTrace(System.err);
      } catch (SessionException e) {
        e.printStackTrace(System.err);
      } catch (CycConnectionException e) {
        e.printStackTrace(System.err);
      }
    }
  };
  private static final CycSymbol END = CycObjectFactory.makeCycSymbol(":end");
  private static final CycSymbol FOLDER = CycObjectFactory.makeCycSymbol(
          ":folder");
  private static final CycSymbol OFFSET = CycObjectFactory.makeCycSymbol(
          ":offset");
  private static final CycSymbol XML = CycObjectFactory.makeCycSymbol(":xml");
  private static final CycSymbol DENOTATIONS = CycObjectFactory.makeCycSymbol(":denotations");
  private static ThreadLocal<FormulaTemplateUnmarshaller> formulaTemplateUnmarshaller
          = new ThreadLocal<FormulaTemplateUnmarshaller>() {
            @Override
            protected FormulaTemplateUnmarshaller initialValue() {
              try {
                return new FormulaTemplateUnmarshaller();
              } catch (JAXBException ex) {
                throw new RuntimeException(ex);
              }
            }
          };
  private static ThreadLocal<QueryReader> queryReader
          = new ThreadLocal<QueryReader>() {
            @Override
            protected QueryReader initialValue() {
              try {
                return new QueryReader();
              } catch (JAXBException ex) {
                throw new RuntimeException(ex);
              }
            }
          };
}
