package com.cyc.km.modeling.task;

/*
 * #%L
 * File: CycBasedTask.java
 * Project: Query API Implementation
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
import com.cyc.base.CycConnectionException;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.ELMt;
import com.cyc.base.cycobject.FormulaSentence;
import com.cyc.base.cycobject.Fort;
import com.cyc.base.kbtool.ObjectTool;
import com.cyc.kb.ArgPosition;
import com.cyc.kb.client.Constants;
import com.cyc.kb.Fact;
import com.cyc.kb.KBObject;
import com.cyc.kb.KBIndividual;
import com.cyc.kb.Sentence;
import com.cyc.kb.client.KBObjectImpl;
import com.cyc.query.Query;
import com.cyc.kb.exception.KBApiException;
import com.cyc.kb.exception.KBObjectNotFoundException;
import com.cyc.query.QueryApiConstants;
import com.cyc.query.exception.QueryApiRuntimeException;
import com.cyc.query.exception.QueryConstructionException;
import com.cyc.session.SessionApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * A class to represent tasks that involve interaction with Cyc.
 *
 * @author baxter
 * @todo consider adding a way to make up a new task
 * @todo explain what pieces of information need to be asserted on a task for it
 * to be useful (e.g. via sharedNotes in KB).
 * @todo consider adding assignCyclist to add a cyclist to a task (and reflect
 * immediately into the KB).
 * @todo Make a static method to find out what tasks a user is assigned to.
 */
public class CycBasedTask {

  /**
   * Returns a collection of all known Cyc-based tasks.
   *
   * @return the collection of tasks.
   * @throws Exception if something goes wrong.
   */
  static public Collection<CycBasedTask> getAll() throws Exception {
    final Collection<CycBasedTask> tasks = new HashSet<CycBasedTask>();
    for (final KBIndividual task : QueryApiConstants.getInstance().CAE_ANALYSIS_TASK.
            <KBIndividual>getInstances(Constants.everythingPSCMt())) {
      tasks.add(new CycBasedTask(task));
    }
    return tasks;
  }

  /**
   * Constructs a new task, backed by the specified term. Note that this does
   * not create a new task in the KB, but just creates a new CycBasedTask object
   * based on an existing <code>KBIndividual</code> in the Knowledge Base.
   *
   * @param taskTerm the KBIndividual representing this task.
   */
  public CycBasedTask(KBIndividual taskTerm) {
    this.taskTerm = taskTerm;
  }

  /**
   * Returns the KBIndividual representing this task.
   *
   * @return the KBIndividual representing this task.
   */
  public KBIndividual getTaskTerm() {
    return taskTerm;
  }

  @Override
  public String toString() {
    return "TASK " + getTaskTerm();
  }

  /**
   * Returns a text description of the task. This description, derived from the
   * KBIndividual underlying the task, is typically about one sentence, but
   * may be arbitrarily long.
   *
   * @return the description, or null if there is no description available from
   * Cyc.
   * @throws KBApiException
   */
  public String getDescription() throws KBApiException {
    final Collection<Fact> facts = taskTerm.getFacts(QueryApiConstants.getInstance().SPECIFICATION_DESCRIPTION,
            1, Constants.everythingPSCMt());
    if (facts != null && !facts.isEmpty()) {
      return facts.iterator().next().<String>getArgument(2);
    } else {
      return null;
    }
  }

  /**
   * Returns a brief (typically less than one sentence) description of the task.
   *
   * @return the summary
   * @throws Exception
   */
  public String getSummary() throws Exception {
    final ArrayList names = new ArrayList(taskTerm.getValues(
            QueryApiConstants.getInstance().NAMESTRING, 1, 2, Constants.inferencePSCMt()));
    return (names.isEmpty()) ? null : (String) names.get(0);
  }

  /**
   * Returns a collection of Cyclists assigned to this task
   *
   * @return the collection of Cyclists
   * @throws KBApiException
   */
  public Collection<KBIndividual> getAssignedCyclists() throws KBApiException {
    synchronized (cyclists) {
      if (cyclists.contains(null)) {
        cyclists.clear();
        for (final Fact fact : taskTerm.getFacts(
                QueryApiConstants.getInstance().ALLOTTED_AGENTS, 1, Constants.everythingPSCMt())) {
          cyclists.add(fact.<KBIndividual>getArgument(2));
        }
      }
    }
    return cyclists;
  }

  /**
   * Returns a collection of concepts particularly relevant to this task
   *
   * @return the collection of key concepts.
   * @throws Exception if something goes wrong.
   */
  public Collection<KBObject> getKeyConcepts() throws Exception {
    synchronized (concepts) {
      if (concepts.contains(null)) {
        for (final Fact fact : taskTerm.getFacts(
                QueryApiConstants.getInstance().TOPIC_OF_INDIVIDUAL, 1, Constants.everythingPSCMt())) {
          final KBObject concept = fact.getArgument(2);
          if (!(concept instanceof KBIndividual && getAssignedCyclists().contains(
                  (KBIndividual) concept))) {
            concepts.add(concept);
          }
        }
      }
    }
    return concepts;
  }

  /**
   * Returns a list of task-relevant candidate replacement terms.
   *
   * @param querySentence - The sentence in which the focal term appears.
   * @param argPosition - The arg position of the focal term in the sentence.
   * @return List of candidate replacement terms.
   * @throws RuntimeException if there is some other kind of problem
   * @throws CycConnectionException
   * @todo consider moving this to the Query class, since the Query is the more
   * focal
   */
  public List<Object> getCandidateReplacements(Sentence querySentence,
          ArgPosition argPosition) throws RuntimeException, CycConnectionException {
    try {
      final List<Object> bapiAnswer = ((FormulaSentence) querySentence.getCore()).getCandidateReplacements(argPosition, getGuidanceMt(),
              getCyc());
      final List<Object> answer = new ArrayList<Object>(bapiAnswer.size());
      for (final Object cycObject : bapiAnswer) {
        if (cycObject instanceof CycObject) {
          answer.add(KBObjectImpl.get((CycObject) cycObject));
        } else {
          answer.add(cycObject);
        }
      }
      return answer;
    } catch (KBApiException ex) {
      throw new QueryApiRuntimeException(ex);
    } catch (SessionApiException ex) {
      throw new QueryApiRuntimeException(ex);
    } catch (QueryConstructionException ex) {
      throw new QueryApiRuntimeException(ex);
    }
  }

  static private CycAccess getCyc() throws SessionApiException {
    return CycAccessManager.getCurrentAccess();
  }

  private Fort getFort() throws CycConnectionException, SessionApiException {
    return getCyc().getLookupTool().getKnownFortByName(
            getTaskTerm().toString());
  }

  private Object getSingleAnswerQueryValue(final KBIndividual kbQuery,
          final Object defaultAnswer) throws KBApiException, CycConnectionException,
          SessionApiException, QueryConstructionException {
    final Query query = Query.load(kbQuery);
    Map<CycObject, Object> substitutions = new HashMap<CycObject, Object>();
    final Fort taskTermFort = getFort();
    // @TODO Use KBObjects:
    substitutions.put(QueryApiConstants.getInstance().taskIndexical, taskTermFort);
    query.substituteTermsWithCycObject(substitutions);
    query.setMaxNumber(1);
    return query.getAnswerCount() >= 1
            ? query.getAnswer(0).getBindings().values().iterator().next()
            : defaultAnswer;
  }

  private synchronized ELMt getGuidanceMt() throws KBApiException, CycConnectionException,
          SessionApiException, QueryConstructionException {
    if (guidanceMt == null) {
      try {
        guidanceMt = getObjectTool().makeELMt(QueryApiConstants.getInstance().CURRENT_WORLD_DATA_COLLECTOR_MT_NON_HOMOCENTRIC.getCore());
        final KBIndividual guidanceMtQuery = QueryApiConstants.getInstance().CAE_GUIDANCE_MT_QUERY;
        guidanceMt = getObjectTool().makeELMt(getSingleAnswerQueryValue(guidanceMtQuery, guidanceMt));
      } catch (KBObjectNotFoundException ex) {
        //Just use default.
      } 
    }
    return guidanceMt;
  }

  private ObjectTool getObjectTool() throws SessionApiException {
    return getCyc().getObjectTool();
  }
  private final KBIndividual taskTerm;
  private final Collection<KBIndividual> cyclists = new HashSet<KBIndividual>(Arrays.asList(
          (KBIndividual) null));
  private final Collection<KBObject> concepts = new HashSet<KBObject>(Arrays.asList(
          (KBObject) null));
  private ELMt guidanceMt = null;
}
