package com.cyc.query;

/*
 * #%L
 * File: QueryReader.java
 * Project: Query API
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
import com.cyc.base.CycAccessManager;
import com.cyc.base.CycApiException;
import com.cyc.base.CycConnectionException;
import com.cyc.base.cycobject.CycConstant;
import com.cyc.base.cycobject.CycSymbol;
import com.cyc.base.cycobject.DenotationalTerm;
import com.cyc.base.cycobject.ELMt;
import com.cyc.base.cycobject.FormulaSentence;
import com.cyc.base.cycobject.Nart;
import com.cyc.base.cycobject.Naut;
import com.cyc.base.cycobject.NonAtomicTerm;
import com.cyc.base.inference.InferenceParameters;
import com.cyc.baseclient.CycObjectFactory;
import com.cyc.baseclient.api.SubLAPIHelper;
import com.cyc.baseclient.cycobject.ELMtCycNaut;
import com.cyc.baseclient.cycobject.ELMtNart;
import com.cyc.baseclient.inference.params.DefaultInferenceParameters;
import com.cyc.kb.Context;
import com.cyc.kb.KBIndividual;
import com.cyc.kb.exception.KBApiException;
import com.cyc.xml.query.CyclQueryUnmarshaller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.bind.JAXBException;

import static com.cyc.baseclient.xml.cycml.CycMLDecoder.translateObject;
import com.cyc.kb.client.ContextImpl;
import com.cyc.kb.client.KBIndividualImpl;
import com.cyc.query.exception.QueryApiRuntimeException;
import com.cyc.query.exception.QueryConstructionException;
import com.cyc.session.SessionApiException;
import com.cyc.xml.query.CyclQuery;
import com.cyc.xml.query.PropertyValue;
import com.cyc.xml.query.QueryFormula;
import com.cyc.xml.query.QueryID;
import com.cyc.xml.query.QueryInferenceProperties;
import com.cyc.xml.query.QueryInferenceProperty;
import com.cyc.xml.query.QueryMt;

/**
 * Support for reading in a Query from XML.
 *
 * @see com.cyc.query.Query
 * @author baxter
 */
public class QueryReader {

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
   * @throws KBApiException if any needed KBObjects cannot be found or created.
   * @throws QueryConstructionException if any other problem is encountered
   */
  protected Query queryFromXML(final InputStream stream) throws KBApiException,
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
   * @throws KBApiException if any needed KBObjects cannot be found or created.
   * @throws QueryApiRuntimeException if there is some other kind of problem
   */
  protected Query queryFromTerm(KBIndividual queryTerm) throws KBApiException,
          QueryConstructionException, QueryApiRuntimeException {
    final String coreCommand = "(get-cycl-query-in-xml " + queryTerm.stringApiValue() + ")";
    final String command
            = SubLAPIHelper.wrapVariableBinding(
                    coreCommand, CycObjectFactory.makeCycSymbol(
                            "*cycl-query-include-namespace?*"),
                    CycObjectFactory.makeCycSymbol("T"));
    try {
      final String xmlString = CycAccessManager.getCurrentAccess().converse().converseString(command);
      return queryFromXML(new ByteArrayInputStream(xmlString.getBytes()));
    } catch (KBApiException e) {
      throw e;
    } catch (CycApiException e) {
      throw new QueryApiRuntimeException("Exception communicating with Cyc.", e);
    } catch (CycConnectionException ex) {
      throw new QueryApiRuntimeException("Exception communicating with Cyc.", ex);
    } catch (SessionApiException ex) {
      throw new QueryApiRuntimeException("Exception communicating with Cyc.", ex);
    }
  }

  /**
   * Convert a CyclQuery to a Query.
   *
   * @param cyclQuery
   * @return the new Query object.
   * @throws KBApiException if any needed KBObjects cannot be found or created.
   * @throws QueryConstructionException if any other problem is encountered
   * constructing the Query object.
   * @see CyclQuery
   */
  public Query convertQuery(CyclQuery cyclQuery) throws KBApiException, QueryConstructionException {
    final DenotationalTerm queryID = convertID(cyclQuery.getQueryID());
    final FormulaSentence querySentence = convertFormula(
            cyclQuery.getQueryFormula());
    final Context queryContext = convertMt(cyclQuery.getQueryMt());
    InferenceParameters queryParams = null;
    for (final Object obj : cyclQuery.getQueryCommentOrQueryInferencePropertiesOrQueryIndexicals()) {
      if (obj instanceof QueryInferenceProperties) {
        try {
          queryParams = convertParams((QueryInferenceProperties) obj);
        } catch (SessionApiException ex) {
          throw new QueryConstructionException("Couldn't convert parameters.", ex);
        }
      }
    }
    final Query query = new Query(querySentence, queryContext, queryParams);
    query.setId(KBIndividualImpl.findOrCreate(queryID));
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

  private Context convertMt(QueryMt queryMt) throws KBApiException {
    if (queryMt.getConstant() != null) {
      return ContextImpl.get(((CycConstant) translateObject(
              queryMt.getConstant())).stringApiValue());
    } else {
      final NonAtomicTerm mtNAT = (NonAtomicTerm) translateObject(
              queryMt.getFunction());
      final ELMt elmt = (mtNAT instanceof Nart) //@todo mtNat can probably be sent directly to Context.get(), without trying to convert to either an ELMt or a CycNaut
              ? ELMtNart.makeELMtNart((Nart) mtNAT)
              : ELMtCycNaut.makeELMtCycNaut(
                      ((Naut) mtNAT).getArgs());
      return ContextImpl.get(elmt);

    }
  }

  private InferenceParameters convertParams (
          QueryInferenceProperties queryInferenceProperties)  throws SessionApiException {
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
