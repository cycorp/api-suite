package com.cyc.query;

import com.cyc.base.CycAccess;
import com.cyc.base.CycAccessManager;
import com.cyc.base.annotation.CycObjectLibrary;
import com.cyc.base.annotation.CycTerm;
import com.cyc.base.cycobject.Naut;
import com.cyc.kb.Context;
import com.cyc.kb.KBCollection;
import com.cyc.kb.KBIndividual;
import com.cyc.kb.KBPredicate;
import com.cyc.kb.client.ContextImpl;
import com.cyc.kb.client.KBCollectionImpl;
import com.cyc.kb.client.KBIndividualImpl;
import com.cyc.kb.client.KBPredicateImpl;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KBTypeException;
import com.cyc.session.SessionApiException;

/*
 * #%L
 * File: QueryApiConstants.java
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
/**
 *
 * @author baxter
 */
@CycObjectLibrary(accessor="getInstance")
public class QueryApiConstants {

  public static synchronized QueryApiConstants getInstance() throws KBTypeException, CreateException {
    if (instance == null) {
      instance = new QueryApiConstants();
    }
    return instance;
  }
  
  @CycTerm(cycl="#$specificationDescription")
  public final KBPredicate SPECIFICATION_DESCRIPTION = KBPredicateImpl.get("specificationDescription");
  
  @CycTerm(cycl="#$nameString")
  public final KBPredicate NAMESTRING = KBPredicateImpl.get("nameString");
  
  @CycTerm(cycl="#$allottedAgents")
  public final KBPredicate ALLOTTED_AGENTS = KBPredicateImpl.get("allottedAgents");
  
  @CycTerm(cycl="#$topicOfIndividual")
  public final KBPredicate TOPIC_OF_INDIVIDUAL = KBPredicateImpl.get("topicOfIndividual");
  
  @CycTerm(cycl="#$CAEAnalysisTask")
  public final KBCollection CAE_ANALYSIS_TASK = KBCollectionImpl.get("CAEAnalysisTask");
  
  @CycTerm(cycl="#$CurrentWorldDataCollectorMt-NonHomocentric")
  public final Context CURRENT_WORLD_DATA_COLLECTOR_MT_NON_HOMOCENTRIC = ContextImpl.get("CurrentWorldDataCollectorMt-NonHomocentric");
  
  @CycTerm(cycl="(#$IndexicalReferentFn #$CAETask-Indexical)")
  @Deprecated
  public final Naut taskIndexical = getCyc().getObjectTool().makeCycNaut("(#$IndexicalReferentFn #$CAETask-Indexical)");
  
  @CycTerm(cycl="#$CycLQuerySpecification")
  public final KBCollection CYCL_QUERY_SPECIFICATION = KBCollectionImpl.get("CycLQuerySpecification");
  
  @CycTerm(cycl="#$CAEGuidanceMtQuery")
  public final KBIndividual CAE_GUIDANCE_MT_QUERY = KBIndividualImpl.get("CAEGuidanceMtQuery");
  
  private static QueryApiConstants instance = null;

  private QueryApiConstants() throws KBTypeException, CreateException {
    super();
  }

  static private CycAccess getCyc() {
    try {
    return CycAccessManager.getCurrentAccess();
    } catch (SessionApiException ex) {
      throw new RuntimeException("Couldn't get Cyc access to initialize constants.", ex);
    }
  }
}
