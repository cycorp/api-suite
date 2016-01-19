/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cyc.query;

/*
 * #%L
 * File: InferenceAnswerBackedQueryAnswer.java
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
import com.cyc.base.CycAccess;
import com.cyc.base.CycAccessManager;
import com.cyc.base.exception.CycApiException;
import com.cyc.base.exception.CycConnectionException;
import com.cyc.base.exception.CycTimeOutException;
import com.cyc.base.cycobject.CycVariable;
import com.cyc.base.cycobject.InformationSource;
import com.cyc.base.inference.InferenceAnswer;
import com.cyc.baseclient.connection.DefaultSublWorkerSynch;
import com.cyc.baseclient.connection.SublWorkerSynch;
import com.cyc.baseclient.cycobject.InformationSourceImpl;
import com.cyc.baseclient.inference.CycBackedInferenceAnswer;
import com.cyc.baseclient.inference.DefaultProofIdentifier;
import com.cyc.baseclient.exception.CycTaskInterruptedException;
import com.cyc.kb.KbTerm;
import com.cyc.kb.Variable;
import com.cyc.kb.client.KbObjectFactory;
import com.cyc.kb.client.KbObjectImpl;
import com.cyc.kb.client.KbTermImpl;
import com.cyc.kb.client.VariableImpl;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbTypeException;
import com.cyc.query.exception.QueryRuntimeException;
import com.cyc.session.exception.SessionCommunicationException;
import com.cyc.session.exception.SessionConfigurationException;
import com.cyc.session.exception.SessionInitializationException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author baxter
 */
public class InferenceAnswerBackedQueryAnswer implements QueryAnswer {

  private final InferenceAnswer answerCyc;

  InferenceAnswerBackedQueryAnswer(InferenceAnswerIdentifier id) {
    this(new CycBackedInferenceAnswer(id));
  }

  InferenceAnswerBackedQueryAnswer(InferenceAnswer answerCyc) {
    if (answerCyc == null) {
      throw new IllegalArgumentException();
    }
    this.answerCyc = answerCyc;
  }

  @Override
  public InferenceAnswerIdentifier getId() {
    return answerCyc.getId();
  }

  @Deprecated
  public InferenceAnswer getAnswerCyc() {
    return answerCyc;
  }

  @Override
  public <T> T getBinding(Variable var) {
    try {
      return KbObjectImpl.<T>checkAndCastObject(answerCyc.getBinding((CycVariable) var.getCore()));
    } catch (CreateException ex) {
      throw new IllegalStateException(ex);
    } catch (CycConnectionException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Gets the id of an arbitrary proof used to support this InferenceAnswer.
   *
   * @return
   */
  public ProofIdentifier getProofIdentifier() throws QueryRuntimeException {
    try {
      CycAccess cyc;
      cyc = CycAccessManager.getCurrentAccess();

      int timeoutMsecs = 1000;
      InferenceIdentifier inferenceIdentifier = getId().getInferenceIdentifier();
      SublWorkerSynch subLWorker = new DefaultSublWorkerSynch("(proof-suid (first (inference-answer-proofs (find-inference-answer-by-ids  "
              + inferenceIdentifier.getProblemStoreId()
              + " "
              + inferenceIdentifier.getInferenceId()
              + " "
              + getId().getAnswerId()
              + "))))",
              cyc, timeoutMsecs);
      Integer id = (Integer) subLWorker.getWork();
      return new DefaultProofIdentifier(this.getId().getInferenceIdentifier().getProblemStoreId(), id);
    } catch (CycConnectionException ex) {
      throw new QueryRuntimeException(ex);
    } catch (CycTimeOutException ex) {
      throw new QueryRuntimeException(ex);
    } catch (CycApiException ex) {
      throw new QueryRuntimeException(ex);
    } catch (CycTaskInterruptedException ex) {
      throw new QueryRuntimeException(ex);
    } catch (SessionConfigurationException ex) {
      throw new QueryRuntimeException(ex);
    } catch (SessionCommunicationException ex) {
      throw new QueryRuntimeException(ex);
    } catch (SessionInitializationException ex) {
      throw new QueryRuntimeException(ex);
    }
  }

  public Set<ProofIdentifier> getProofIdentifiers() throws SessionCommunicationException, SessionConfigurationException, SessionInitializationException, CycApiException, CycTimeOutException, CycConnectionException {
    Set<ProofIdentifier> ids = new HashSet<ProofIdentifier>();
    CycAccess cyc = CycAccessManager.getCurrentAccess();
    int timeoutMsecs = 1000;
    InferenceIdentifier inferenceIdentifier = getId().getInferenceIdentifier();
    SublWorkerSynch subLWorker = new DefaultSublWorkerSynch("(find-proof-ids-for-inference-answer "
            + inferenceIdentifier.getProblemStoreId()
            + " "
            + inferenceIdentifier.getInferenceId()
            + " "
            + getId().getAnswerId()
            + ")",
            cyc, timeoutMsecs);
    List<Integer> result = (List<Integer>) subLWorker.getWork();
    for (Integer proofId : result) {
      ids.add(new DefaultProofIdentifier(this.getId().getInferenceIdentifier().getProblemStoreId(), proofId));
    }
    return ids;
  }

  @Override
  public Map<Variable, Object> getBindings() {
    try {
      final Map<CycVariable, Object> cycBindings = answerCyc.getBindings();
      return new Map<Variable, Object>() {

        @Override
        public int size() {
          return cycBindings.size();
        }

        @Override
        public boolean isEmpty() {
          return cycBindings.isEmpty();
        }

        @Override
        public boolean containsKey(Object key) {
          return key instanceof Variable && cycBindings.containsKey(((Variable) key).getCore());
        }

        @Override
        public boolean containsValue(Object value) {
          return cycBindings.containsValue(value);
        }

        @Override
        public Object get(Object key) {
          if (key instanceof Variable) {
            return cycBindings.get(((Variable) key).getCore());
          } else {
            return null;
          }
        }

        @Override
        public Object put(Variable key, Object value) {
          return cycBindings.put((CycVariable) key.getCore(), value);
        }

        @Override
        public Object remove(Object key) {
          throw new UnsupportedOperationException();
        }

        @Override
        public void putAll(Map<? extends Variable, ? extends Object> m) {
          throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
          throw new UnsupportedOperationException();
        }

        @Override
        public Set<Variable> keySet() {
          Set<Variable> vars = new HashSet<Variable>();
          for (CycVariable var : cycBindings.keySet()) {
            try {
              vars.add(new VariableImpl(var));
            } catch (KbTypeException ex) {
              ex.printStackTrace();
            }
          }
          return vars;
        }

        @Override
        public Collection<Object> values() {
          return Collections.unmodifiableCollection(cycBindings.values());
        }

        @Override
        public Set<Map.Entry<Variable, Object>> entrySet() {
          throw new UnsupportedOperationException("Not supported yet.");
        }

      };
    } catch (CycConnectionException ex) {
      throw new IllegalStateException(ex);
    }

  }

  @Override
  public String toString() {
    return answerCyc.toString();
  }

  @Override
  public Set<KbTerm> getSources() {
    Set<KbTerm> sources = new HashSet<KbTerm>();
    if (answerCyc.getId() == null) {
      throw new UnsupportedOperationException("Sources can not be retrieved from an inference that has been destroyed.  "
              + "The inference must be retained until after getSources() has been called.");
    }
    try {
      for (InformationSource source : answerCyc.getSources(InformationSourceImpl.CycCitationGenerator.DEFAULT)) {
        try {
          sources.add(KbObjectFactory.get(source.getCycL(), KbTermImpl.class));
        } catch (KbTypeException ex) {
          throw new QueryRuntimeException("Unable to turn source " + source.getCycL() + " into a KB Object");
        } catch (CreateException ex) {
          throw new QueryRuntimeException("Unable to turn source " + source.getCycL() + " into a KB Object");
        }        
      }      
    } catch (CycConnectionException ex) {
      throw new QueryRuntimeException("Unable to get source for inference answer " + answerCyc.toString());
    }
    return sources;
  }

}
