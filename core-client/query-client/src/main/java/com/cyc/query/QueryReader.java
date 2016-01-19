package com.cyc.query;

/*
 * #%L
 * File: QueryReader.java
 * Project: Query Client
 * %%
 * Copyright (C) 2013 - 2016 Cycorp, Inc.
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
import com.cyc.base.CycAccessManager;
import com.cyc.base.exception.CycApiException;
import com.cyc.base.exception.CycConnectionException;
import com.cyc.base.cycobject.CycConstant;
import com.cyc.base.cycobject.CycSymbol;
import com.cyc.base.cycobject.DenotationalTerm;
import com.cyc.base.cycobject.ElMt;
import com.cyc.base.cycobject.FormulaSentence;
import com.cyc.base.cycobject.Nart;
import com.cyc.base.cycobject.Naut;
import com.cyc.base.cycobject.NonAtomicTerm;
import com.cyc.baseclient.CycObjectFactory;
import com.cyc.baseclient.connection.SublApiHelper;
import com.cyc.baseclient.cycobject.ElMtCycNaut;
import com.cyc.baseclient.cycobject.ElMtNart;
import com.cyc.baseclient.inference.params.DefaultInferenceParameters;
import com.cyc.kb.Context;
import com.cyc.kb.KbIndividual;
import com.cyc.kb.exception.KbException;
import com.cyc.xml.query.CyclQueryUnmarshaller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.bind.JAXBException;

import static com.cyc.baseclient.xml.cycml.CycmlDecoder.translateObject;
import com.cyc.kb.Sentence;
import com.cyc.kb.client.ContextImpl;
import com.cyc.kb.client.KbIndividualImpl;
import com.cyc.kb.client.SentenceImpl;
import com.cyc.query.exception.QueryRuntimeException;
import com.cyc.query.exception.QueryConstructionException;
import com.cyc.session.exception.SessionException;
import com.cyc.xml.query.CyclQuery;
import com.cyc.xml.query.PropertyValue;
import com.cyc.xml.query.QueryFormula;
import com.cyc.xml.query.QueryID;
import com.cyc.xml.query.QueryInferenceProperties;
import com.cyc.xml.query.QueryInferenceProperty;
import com.cyc.xml.query.QueryMt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Support for reading in a QueryImpl from XML.
 *
 * @see com.cyc.query.QueryImpl
 * @author baxter
 */
public class QueryReader {
  
  static private final Logger LOGGER = LoggerFactory.getLogger(QueryReader.class);
  final CyclQueryUnmarshaller unmarshaller;

  /**
   * Create a new QueryReader.
   *
   * @throws JAXBException
   */
  public QueryReader() throws JAXBException {
    unmarshaller = new CyclQueryUnmarshaller();
  }

  QueryReader(CyclQueryUnmarshaller unmarshaller) throws JAXBException {
    this.unmarshaller = unmarshaller;
  }

  /**
   * Read a query from an XML stream.
   *
   * @param stream
   * @return the query
   * @throws KbException if any needed KBObjects cannot be found or created.
   * @throws QueryConstructionException if any other problem is encountered
   */
  protected Query queryFromXML(final InputStream stream) throws KbException,
          QueryConstructionException {
    try {
      final Object contentTree = unmarshaller.unmarshal(stream);
      if (contentTree instanceof CyclQuery) {
        return convertQuery((CyclQuery) contentTree);
      } else {
        throw new IllegalArgumentException("Can't convert " + contentTree);
      }
    } catch (JAXBException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Returns a Query object defined by a query term.
   *
   * @param queryTerm
   * @return a Query object defined by queryTerm
   * @throws KbException if any needed KBObjects cannot be found or created.
   * @throws com.cyc.query.exception.QueryConstructionException
   * @throws QueryRuntimeException if there is some other kind of problem
   */
  protected Query queryFromTerm(KbIndividual queryTerm) throws KbException,
          QueryConstructionException, QueryRuntimeException {
    final String coreCommand = "(get-cycl-query-in-xml " + queryTerm.stringApiValue() + ")";
    final String command
            = SublApiHelper.wrapVariableBinding(
                    coreCommand, CycObjectFactory.makeCycSymbol(
                            "*cycl-query-include-namespace?*"),
                    CycObjectFactory.makeCycSymbol("T"));
    try {
      final String xmlString = CycAccessManager.getCurrentAccess().converse().converseString(command);
      LOGGER.debug("Query XML:\n{}", xmlString);
      return queryFromXML(new ByteArrayInputStream(xmlString.getBytes()));
    } catch (KbException e) {
      throw e;
    } catch (CycApiException e) {
      throw new QueryRuntimeException("Exception communicating with Cyc.", e);
    } catch (CycConnectionException ex) {
      throw new QueryRuntimeException("Exception communicating with Cyc.", ex);
    } catch (SessionException ex) {
      throw new QueryRuntimeException("Exception communicating with Cyc.", ex);
    }
  }

  /**
   * Convert a CyclQuery to a QueryImpl.
   *
   * @param cyclQuery
   * @return the new QueryImpl object.
   * @throws KbException if any needed KBObjects cannot be found or created.
   * @throws QueryConstructionException if any other problem is encountered
 constructing the QueryImpl object.
   * @see CyclQuery
   */
  public Query convertQuery(CyclQuery cyclQuery) throws KbException, QueryConstructionException {
    final DenotationalTerm queryID = convertID(cyclQuery.getQueryID());
    final Sentence querySentence = new SentenceImpl(convertFormula(
            cyclQuery.getQueryFormula()));
    final Context queryContext = convertMt(cyclQuery.getQueryMt());
    InferenceParameters queryParams = null;
    for (final Object obj : cyclQuery.getQueryCommentOrQueryInferencePropertiesOrQueryIndexicals()) {
      if (obj instanceof QueryInferenceProperties) {
        try {
          queryParams = convertParams((QueryInferenceProperties) obj);
        } catch (SessionException ex) {
          throw new QueryConstructionException("Couldn't convert parameters.", ex);
        }
      }
    }
    final Query query = QueryFactory.getQuery(querySentence, queryContext, queryParams);
    ((QueryImpl)query).setId(KbIndividualImpl.findOrCreate(queryID));
    return query;
  }

  private DenotationalTerm convertID(QueryID queryID) {
    if (queryID.getConstant() != null) {
      return (CycConstant) translateObject(queryID.getConstant());
    } else {
      return (NonAtomicTerm) translateObject(queryID.getFunction());
    }
  }

  private FormulaSentence convertFormula(QueryFormula queryFormula) {
    return (FormulaSentence) translateObject(
            queryFormula.getSentence());
  }

  private Context convertMt(QueryMt queryMt) throws KbException {
    if (queryMt.getConstant() != null) {
      return ContextImpl.get(((CycConstant) translateObject(
              queryMt.getConstant())).stringApiValue());
    } else {
      final NonAtomicTerm mtNAT = (NonAtomicTerm) translateObject(
              queryMt.getFunction());
      final ElMt elmt = (mtNAT instanceof Nart) //@todo mtNat can probably be sent directly to Context.get(), without trying to convert to either an ELMt or a CycNaut
              ? ElMtNart.makeElMtNart((Nart) mtNAT)
              : ElMtCycNaut.makeElMtCycNaut(
                      ((Naut) mtNAT).getArgs());
      return ContextImpl.get(elmt);

    }
  }

  private InferenceParameters convertParams (
          QueryInferenceProperties queryInferenceProperties)  throws SessionException {
    final StringBuilder sb = new StringBuilder();
    for (final QueryInferenceProperty qip : queryInferenceProperties.getQueryInferenceProperty()) {
      if (sb.length() > 0) {
        sb.append(" ");
      }
      final String symbolName = qip.getPropertySymbol().getContent().trim();
      sb.append(":").append(symbolName).append(" ");
      appendPropertyValue(qip.getPropertyValue(), sb);
    }
    final String paramString = sb.toString();
    return new DefaultInferenceParameters(CycAccessManager.getCurrentAccess(), paramString);
  }

  private void appendPropertyValue(final PropertyValue val,
          final StringBuilder sb) {
    if (val.getConstant() != null) {
      sb.append(((CycConstant) translateObject(
              val.getConstant())).stringApiValue());
    } else if (val.getFunction() != null) {
      sb.append(((NonAtomicTerm) translateObject(
              val.getFunction())).stringApiValue());
    } else if (val.getNumber() != null) {
      sb.append((val.getNumber()));
    } else if (val.getSentence() != null) {
      sb.append(((FormulaSentence) translateObject(
              val.getSentence())).stringApiValue());
    } else if (val.getSymbol() != null) {
      sb.append(((CycSymbol) translateObject(
              val.getSymbol())).stringApiValue());
    }
  }
}
