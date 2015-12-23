package com.cyc.km.modeling.task;

/*
 * #%L
 * File: CycBasedTask.java
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
import com.cyc.base.exception.CycConnectionException;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.ElMt;
import com.cyc.base.cycobject.FormulaSentence;
import com.cyc.base.cycobject.Fort;
import com.cyc.base.kbtool.ObjectTool;
import com.cyc.kb.ArgPosition;
import com.cyc.kb.client.Constants;
import com.cyc.kb.Fact;
import com.cyc.kb.KbObject;
import com.cyc.kb.KbIndividual;
import com.cyc.kb.Sentence;
import com.cyc.kb.client.KbIndividualImpl;
import com.cyc.kb.client.KbObjectImpl;
import com.cyc.kb.exception.CreateException;
import com.cyc.query.Query;
import com.cyc.kb.exception.KbException;
import com.cyc.kb.exception.KbObjectNotFoundException;
import com.cyc.kb.exception.KbTypeException;
import com.cyc.query.QueryConstants;
import com.cyc.query.QueryFactory;
import com.cyc.query.exception.QueryRuntimeException;
import com.cyc.query.exception.QueryConstructionException;
import com.cyc.session.exception.SessionException;
import com.cyc.session.compatibility.CycSessionRequirementList;
import com.cyc.session.compatibility.NotOpenCycRequirement;
import com.cyc.session.exception.OpenCycUnsupportedFeatureException;

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

  public static final CycSessionRequirementList<OpenCycUnsupportedFeatureException> CYC_BASED_TASK_REQUIREMENTS = CycSessionRequirementList.fromList(
          NotOpenCycRequirement.NOT_OPENCYC
  );
  
  /**
   * Returns a collection of all known Cyc-based tasks.
   *
   * @return the collection of tasks.
   * @throws com.cyc.kb.exception.KbTypeException
   * @throws com.cyc.kb.exception.CreateException
   * @throws com.cyc.session.exception.OpenCycUnsupportedFeatureException
   */
  static public Collection<CycBasedTask> getAll() throws KbTypeException, CreateException, OpenCycUnsupportedFeatureException {
    final Collection<CycBasedTask> tasks = new HashSet<CycBasedTask>();
    for (final KbIndividual task : QueryConstants.getInstance().CAE_ANALYSIS_TASK.
            <KbIndividual>getInstances(Constants.everythingPSCMt())) {
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
   * @throws com.cyc.session.exception.OpenCycUnsupportedFeatureException when run against an OpenCyc server.
   */
  public CycBasedTask(KbIndividual taskTerm) throws OpenCycUnsupportedFeatureException {
    CYC_BASED_TASK_REQUIREMENTS.throwRuntimeExceptionIfIncompatible();
    this.taskTerm = taskTerm;
  }

  /**
   * Returns the KBIndividual representing this task.
   *
   * @return the KBIndividual representing this task.
   */
  public KbIndividual getTaskTerm() {
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
   * @throws KbException
   */
  public String getDescription() throws KbException {
    final Collection<Fact> facts = taskTerm.getFacts(QueryConstants.getInstance().SPECIFICATION_DESCRIPTION,
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
  public String getSummary() throws KbTypeException, CreateException {
    final ArrayList names = new ArrayList(taskTerm.getValues(QueryConstants.getInstance().NAMESTRING, 1, 2, Constants.inferencePSCMt()));
    return (names.isEmpty()) ? null : (String) names.get(0);
  }

  /**
   * Returns a collection of Cyclists assigned to this task
   *
   * @return the collection of Cyclists
   * @throws KbException
   */
  public Collection<KbIndividual> getAssignedCyclists() throws KbException {
    synchronized (cyclists) {
      if (cyclists.contains(null)) {
        cyclists.clear();
        for (final Fact fact : taskTerm.getFacts(QueryConstants.getInstance().ALLOTTED_AGENTS, 1, Constants.everythingPSCMt())) {
          cyclists.add(fact.<KbIndividual>getArgument(2));
        }
      }
    }
    return cyclists;
  }

  /**
   * Returns a collection of concepts particularly relevant to this task
   *
   * @return the collection of key concepts.
   * @throws com.cyc.kb.exception.KbTypeException
   * @throws com.cyc.kb.exception.CreateException
   */
  public Collection<KbObject> getKeyConcepts() throws KbTypeException, CreateException, KbException {
    synchronized (concepts) {
      if (concepts.contains(null)) {
        for (final Fact fact : taskTerm.getFacts(QueryConstants.getInstance().TOPIC_OF_INDIVIDUAL, 1, Constants.everythingPSCMt())) {
          final KbObject concept = fact.getArgument(2);
          if (!(concept instanceof KbIndividual && getAssignedCyclists().contains((KbIndividual) concept))) {
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
          ArgPosition argPosition) throws RuntimeException, OpenCycUnsupportedFeatureException {
    try {
      final List<Object> bapiAnswer = ((FormulaSentence) querySentence.getCore()).getCandidateReplacements(argPosition, getGuidanceMt(),
              getCyc());
      final List<Object> answer = new ArrayList<Object>(bapiAnswer.size());
      for (final Object cycObject : bapiAnswer) {
        if (cycObject instanceof CycObject) {
          answer.add(KbObjectImpl.get((CycObject) cycObject));
        } else {
          answer.add(cycObject);
        }
      }
      return answer;
    } catch (KbException ex) {
      throw new QueryRuntimeException(ex);
    } catch (SessionException ex) {
      throw new QueryRuntimeException(ex);
    } catch (QueryConstructionException ex) {
      throw new QueryRuntimeException(ex);
    } catch (CycConnectionException ex) {
      throw new QueryRuntimeException(ex);
    }
  }

  static private CycAccess getCyc() throws SessionException {
    return CycAccessManager.getCurrentAccess();
  }

  private Fort getFort() throws CycConnectionException, SessionException {
    return getCyc().getLookupTool().getKnownFortByName(
            getTaskTerm().toString());
  }

  private Object getSingleAnswerQueryValue(final KbIndividual kbQuery,
          final Object defaultAnswer) throws KbException,
          SessionException, QueryConstructionException {
    try {
      Map<KbObject, Object> substitutions = new HashMap<KbObject, Object>();
      final Fort taskTermFort = getFort();
      substitutions.put(KbObjectImpl.get(QueryConstants.getInstance().taskIndexical), KbIndividualImpl.get(taskTermFort));
      final Query query = QueryFactory.getQuery(kbQuery, substitutions);
      query.setMaxAnswerCount(1);
      return query.getAnswerCount() >= 1
              ? query.getAnswer(0).getBindings().values().iterator().next()
              : defaultAnswer;
    } catch (CycConnectionException ex) {
      throw new QueryConstructionException(ex);
    } catch (Exception ex) {
      throw new QueryConstructionException(ex);
    }
  }

  private synchronized ElMt getGuidanceMt() throws KbException, CycConnectionException,
          SessionException, QueryConstructionException {
    if (guidanceMt == null) {
      try {
        guidanceMt = getObjectTool().makeELMt(QueryConstants.getInstance().CURRENT_WORLD_DATA_COLLECTOR_MT_NON_HOMOCENTRIC.getCore());
        final KbIndividual guidanceMtQuery = QueryConstants.getInstance().CAE_GUIDANCE_MT_QUERY;
        guidanceMt = getObjectTool().makeELMt(getSingleAnswerQueryValue(guidanceMtQuery, guidanceMt));
      } catch (KbObjectNotFoundException ex) {
        //Just use default.
      } 
    }
    return guidanceMt;
  }

  private ObjectTool getObjectTool() throws SessionException {
    return getCyc().getObjectTool();
  }
  private final KbIndividual taskTerm;
  private final Collection<KbIndividual> cyclists = new HashSet<KbIndividual>(Arrays.asList((KbIndividual) null));
  private final Collection<KbObject> concepts = new HashSet<KbObject>(Arrays.asList((KbObject) null));
  private ElMt guidanceMt = null;
}
