/*
 */
package com.cyc.query;

/*
 * #%L
 * File: TestUtils.java
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
import com.cyc.query.exception.QueryConstructionException;
import com.cyc.base.CycAccess;
import com.cyc.base.CycAccessManager;
import com.cyc.kb.Context;
import com.cyc.kb.KbStatus;
import com.cyc.kb.Sentence;
import com.cyc.kb.Variable;
import com.cyc.kb.client.Constants;
import com.cyc.kb.client.KbObjectFactory;
import com.cyc.kb.client.KbObjectImpl;
import com.cyc.kb.client.SentenceImpl;
import com.cyc.kb.client.VariableImpl;
import com.cyc.kb.client.config.KbConfiguration;
import com.cyc.kb.exception.KbException;
import com.cyc.kb.exception.KbRuntimeException;
import com.cyc.session.CycSession;
import com.cyc.session.CycSessionManager;
import com.cyc.session.exception.SessionException;
import com.cyc.session.exception.SessionCommunicationException;
import com.cyc.session.exception.SessionConfigurationException;
import com.cyc.session.exception.SessionInitializationException;
import com.cyc.session.compatibility.CycSessionRequirement;
import com.cyc.session.compatibility.CycSessionRequirementList;
import com.cyc.session.compatibility.NotOpenCycRequirement;
import com.cyc.session.internal.TestEnvironmentProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author baxter
 */
public class TestUtils {

  public static Query q;

  public static Sentence xIsaBird() throws KbException {
    return new SentenceImpl(Constants.isa(), X, QueryTestConstants.bird());
  }

  private static QueryTestConstants testConstants() throws KbRuntimeException {
    return QueryTestConstants.getInstance();
  }
  public static Variable X;
  public static Sentence xIsaEmu;
  public static Context inferencePSC;
  private static final Logger LOG = LoggerFactory.getLogger("Query API test suite");

  private static boolean initialized = false;

  public static synchronized void ensureConstantsInitialized() {
    if (initialized == false) {
      try {
        KbConfiguration.getOptions().setShouldTranscriptOperations(false);
        System.out.println("Current Cyc: * * *  " + getCyc().getServerInfo().getCycServer() + "  * * *");
        X = new VariableImpl("?X");
        xIsaEmu = new SentenceImpl(Constants.isa(), X, testConstants().emu());
        inferencePSC = Constants.inferencePSCMt();
        initialized = true;
      } catch (Exception e) {
        throw new RuntimeException("Failed to initialize test constants.", e);
      }
    }
  }

  static Query constructXIsaBirdQuery() throws QueryConstructionException {
    try {
      return QueryFactory.getQuery(xIsaBird(), inferencePSC);
    } catch (KbException ex) {
      throw new QueryConstructionException(ex);
    }
  }

  static protected void closeTestQuery() {
    if (q != null) {
      q.close();
    }
  }
  
  public static void assumeKBObject(String nameOrId, Class<? extends KbObjectImpl> clazz) {
    // TODO: this should be moved into a CycSessionRequirement.
    org.junit.Assume.assumeTrue(KbObjectFactory.getStatus(nameOrId, clazz).equals(KbStatus.EXISTS_AS_TYPE));
  }
  
  public static void assumeCycSessionRequirement(CycSessionRequirement requirement) {
    // TODO: move this into some central test library
    try {
      org.junit.Assume.assumeTrue(requirement.checkCompatibility(getSession()).isCompatible());
    } catch (SessionException ex) {
      ex.printStackTrace(System.err);
      throw new RuntimeException(ex);
    }
  }
  
  public static void assumeCycSessionRequirements(CycSessionRequirementList requirements) {
    // TODO: move this into some central test library
    try {
      org.junit.Assume.assumeTrue(requirements.checkCompatibility().isCompatible());
    } catch (SessionException ex) {
      ex.printStackTrace(System.err);
      throw new RuntimeException(ex);
    }
  }
  
  public static void assumeNotOpenCyc() {
    // TODO: move this into some central test library
    
    // To toggle #areOpenCycTestsForcedToRun, edit "cyc.test.forceOpenCycTestsToRun" in the pom.xml
    if (!TestEnvironmentProperties.get().areOpenCycTestsForcedToRun()) {
      assumeCycSessionRequirement(NotOpenCycRequirement.NOT_OPENCYC);
    }
  }
  
  private static CycSession getSession() throws SessionConfigurationException, SessionInitializationException, SessionCommunicationException {
    return CycSessionManager.getCurrentSession();
  }

  public static CycAccess getCyc() {
    try {
      return CycAccessManager.getCurrentAccess();
    } catch (Exception e) {
      throw new RuntimeException("Failed retrieve current CycAccess.", e);
    }
  }

}
