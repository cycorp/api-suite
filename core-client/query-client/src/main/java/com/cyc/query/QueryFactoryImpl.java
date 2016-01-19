/*
 * Copyright 2015 Cycorp, Inc..
 *
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
 */
package com.cyc.query;

/*
 * #%L
 * File: QueryFactoryImpl.java
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

import com.cyc.query.spi.QueryFactoryService;
import com.cyc.base.cycobject.CycObject;
import com.cyc.kb.Context;
import com.cyc.kb.KbIndividual;
import com.cyc.kb.KbObject;
import com.cyc.kb.Sentence;
import com.cyc.kb.exception.KbException;
import com.cyc.kb.exception.KbTypeException;
import com.cyc.query.exception.QueryConstructionException;
import com.cyc.session.exception.UnsupportedCycOperationException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author daves
 */
public class QueryFactoryImpl implements QueryFactoryService {

    
  @Override
  public Query getQuery(String queryStr) throws QueryConstructionException {
    return new QueryImpl(queryStr);
  }

  @Override
  public Query getQuery(String queryStr, String ctxStr) throws QueryConstructionException {
    return new QueryImpl(queryStr, ctxStr);
  }

  @Override
  public Query getQuery(String queryStr, String ctxStr, String queryParams) throws QueryConstructionException {
    return new QueryImpl(queryStr, ctxStr, queryParams);
  }

  @Override
  public Query getQuery(Sentence sent, Context ctx, InferenceParameters params) throws QueryConstructionException {
    return new QueryImpl(sent, ctx, params);
  }

  @Override
  public Query getQuery(Sentence sent, Context ctx) throws QueryConstructionException {
    return new QueryImpl(sent, ctx);
  }

  @Override
  public Query getQuery(KbIndividual id) throws QueryConstructionException, KbException, UnsupportedCycOperationException {
    return new QueryImpl(id);
  }

  @Override
  public Query getQuery(KbIndividual id, Map<KbObject, Object> indexicals) throws UnsupportedCycOperationException, QueryConstructionException, KbException {
    Map<CycObject, Object> cycIndexicals = new HashMap<CycObject, Object>();
    for (Map.Entry<KbObject, Object> e : indexicals.entrySet()) {
      CycObject cycIndexical =(CycObject)((KbObject)e.getKey()).getCore(); 
      Object cycValue = e.getValue();
      if (cycValue instanceof KbObject) {
        cycValue = ((KbObject)cycValue).getCore();
      }
      cycIndexicals.put(cycIndexical, cycValue);
    }
    return QueryImpl.loadCycObjectMap(id, cycIndexicals);
  }

  @Override
  public Query getQuery(String idStr, Map<String, String> indexicals) throws QueryConstructionException, KbTypeException, UnsupportedCycOperationException {
    return QueryImpl.load(idStr, indexicals);
  }

  @Override
  public int closeAllUnclosedQueries() {
    return QueryImpl.closeAllUnclosedQueries();
  }
  
}
