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
 * File: QueryListenerAdaptor.java
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

import com.cyc.base.cycobject.CycList;
import com.cyc.base.cycobject.CycVariable;
import com.cyc.base.inference.InferenceWorker;
import com.cyc.base.inference.InferenceWorkerListener;
import com.cyc.kb.Variable;
import com.cyc.kb.client.KbObjectImpl;
import com.cyc.kb.client.VariableImpl;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbTypeException;
import com.cyc.query.QueryImpl.QueryWorker;
import com.cyc.query.exception.QueryRuntimeException;
import com.cyc.session.exception.SessionCommunicationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wraps a QueryListener in an InferenceWorkerListener.
 * 
 * @author nwinant
 */
public class QueryListenerAdaptor implements InferenceWorkerListener {

  static private QueryImpl getQuery(InferenceWorker inferenceWorker) {
    return ((QueryWorker) inferenceWorker).getQuery();
  }

  
  // Fields
  
  static final private Logger LOGGER = LoggerFactory.getLogger(QueryListenerAdaptor.class.getName());
  final private QueryListener queryListener;
  private int nextAnswerId = 0;

  
  // Constructor
  
  public QueryListenerAdaptor(QueryListener listener) {
    this.queryListener = listener;
  }
  
  
  // Public
  
  public QueryListener getQueryListener() {
    return this.queryListener;
  }
  
  @Override
  public void notifyInferenceCreated(InferenceWorker inferenceWorker) {
    final QueryImpl query = getQuery(inferenceWorker);
    queryListener.notifyInferenceCreated(query);
  }
  
  @Override
  public void notifyInferenceStatusChanged(InferenceStatus oldStatus, InferenceStatus newStatus,
          InferenceSuspendReason suspendReason, InferenceWorker inferenceWorker) {
    final QueryImpl query = getQuery(inferenceWorker);
    queryListener.notifyInferenceStatusChanged(oldStatus, newStatus, suspendReason,
            query);
  }
  
  @Override
  public void notifyInferenceAnswersAvailable(InferenceWorker inferenceWorker, List newAnswers) {
    final List<QueryAnswer> newQueryAnswers = new ArrayList<QueryAnswer>(newAnswers.size());
    final QueryImpl query = getQuery(inferenceWorker);
    try {
      for (final Object newAnswer : newAnswers) {
        newQueryAnswers.add(getQueryAnswerFromCycBindings(query, (CycList<CycList>) newAnswer));
      }
    } catch (Exception ex) {

    }
    queryListener.notifyInferenceAnswersAvailable(query, newQueryAnswers);
  }
  
    
  @Override
  public void notifyInferenceTerminated(InferenceWorker inferenceWorker, Exception e) {
    queryListener.notifyInferenceTerminated(getQuery(inferenceWorker), e);
  }
  
  
  // Private
  
  private QueryAnswer getQueryAnswerFromCycBindings(final QueryImpl query, final CycList<CycList> cycBindings)
          throws SessionCommunicationException {
    final QueryAnswer qa = query.getAnswer(nextAnswerId++);
    final Map<Variable, Object> bindings = gatherBindings(cycBindings, qa);
    if (bindings == null) {
      return qa;
    } else {
      return new BindingsBackedQueryAnswer(bindings);
    }
  }
  
  /**
   * We prefer to use <code>qa</code> as long as its bindings match <code>cycBindings</code>. If
   * they don't match, we return a map of bindings to use, but the resulting QueryAnswer won't know
   * its ID.
   *
   * @param cycBindings
   * @param qa
   * @return
   */
  private Map<Variable, Object> gatherBindings(CycList<CycList> cycBindings,
          final QueryAnswer qa) {
    final Map<Variable, Object> bindings = new HashMap<Variable, Object>(cycBindings.size());
    boolean qaGood = true;
    for (final CycList cycBinding : cycBindings) {
      try {
        final VariableImpl var = new VariableImpl((CycVariable) cycBinding.get(0));
        final Object val = KbObjectImpl.checkAndCastObject(cycBinding.getDottedElement());
        if (!(val.equals(qa.getBinding(var)))) {
          System.err.println("For " + var + ", expected " + qa.getBinding(var) + ", got " + val);
          qaGood = false;
        }
        bindings.put(var, val);
      } catch (KbTypeException ex) {
        LOGGER.error("Problem getting binding.", ex);
        throw new QueryRuntimeException(ex);
      } catch (CreateException ex) {
        throw new QueryRuntimeException(ex);
      }
    }
    return (qaGood ? null : bindings);
  }
  
}
