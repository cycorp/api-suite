package com.cyc.query;

import com.cyc.base.CycAccess;
import com.cyc.base.CycAccessManager;
import com.cyc.base.exception.CycConnectionException;
import com.cyc.base.annotation.CycObjectLibrary;
import com.cyc.base.annotation.CycTerm;
import com.cyc.base.cycobject.Naut;
import com.cyc.kb.Context;
import com.cyc.kb.KbCollection;
import com.cyc.kb.KbIndividual;
import com.cyc.kb.KbPredicate;
import com.cyc.kb.client.ContextImpl;
import com.cyc.kb.client.KbCollectionImpl;
import com.cyc.kb.client.KbIndividualImpl;
import com.cyc.kb.client.KbPredicateImpl;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbTypeException;
import com.cyc.session.CycSessionManager;
import com.cyc.session.exception.SessionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * #%L
 * File: QueryConstants.java
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
/**
 *
 * @author baxter
 */
@CycObjectLibrary(accessor="getInstance")
public class QueryConstants {
  
  public static synchronized QueryConstants getInstance() throws KbTypeException, CreateException {
    if (instance == null) {
      instance = new QueryConstants();
    }
    return instance;
  }
  
  @CycTerm(cycl="#$specificationDescription")
  public final KbPredicate SPECIFICATION_DESCRIPTION = KbPredicateImpl.get("specificationDescription");
  
  @CycTerm(cycl="#$nameString")
  public final KbPredicate NAMESTRING = KbPredicateImpl.get("nameString");
  
  @CycTerm(cycl="#$allottedAgents")
  public final KbPredicate ALLOTTED_AGENTS = KbPredicateImpl.get("allottedAgents");
  
  @CycTerm(cycl="#$topicOfIndividual")
  public final KbPredicate TOPIC_OF_INDIVIDUAL = KbPredicateImpl.get("topicOfIndividual");
  
  @CycTerm(cycl="#$CAEAnalysisTask")
  public final KbCollection CAE_ANALYSIS_TASK = KbCollectionImpl.get("CAEAnalysisTask");
  
  @CycTerm(cycl="#$CurrentWorldDataCollectorMt-NonHomocentric")
  public final Context CURRENT_WORLD_DATA_COLLECTOR_MT_NON_HOMOCENTRIC = ContextImpl.get("CurrentWorldDataCollectorMt-NonHomocentric");
  
  @CycTerm(cycl="(#$IndexicalReferentFn #$CAETask-Indexical)")
  @Deprecated
  public final Naut taskIndexical = getCyc().getObjectTool().makeCycNaut("(#$IndexicalReferentFn #$CAETask-Indexical)");
  
  @CycTerm(cycl="#$CycLQuerySpecification")
  public final KbCollection CYCL_QUERY_SPECIFICATION = KbCollectionImpl.get("CycLQuerySpecification");
  
  @CycTerm(cycl="#$CAEGuidanceMtQuery", includedInOpenCycKB=false)
  public final KbIndividual CAE_GUIDANCE_MT_QUERY;
  
  private static final Logger LOG = LoggerFactory.getLogger(QueryConstants.class);
  private static QueryConstants instance = null;

  private QueryConstants() throws KbTypeException, CreateException {
    super();
    try {
      if (!getCyc().isOpenCyc()) {
        CAE_GUIDANCE_MT_QUERY = KbIndividualImpl.get("CAEGuidanceMtQuery");
      } else {
        LOG.warn("#$CAEGuidanceMtQuery is not included in OpenCyc KB, skipping...");
        CAE_GUIDANCE_MT_QUERY = null;
      }
    } catch (CycConnectionException ex) {
      throw new RuntimeException("Couldn't get Cyc access to initialize constants.", ex);
    }
  }
  
  static private CycAccess getCyc() {
    try {
    return CycAccessManager.getCurrentAccess();
    } catch (SessionException ex) {
      throw new RuntimeException("Couldn't get Cyc access to initialize constants.", ex);
    }
  }
}
