package com.cyc.baseclient.testing;

/*
 * #%L
 * File: TestUtils.java
 * Project: Base Client
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
import com.cyc.base.BaseClientRuntimeException;
import com.cyc.base.CycConnectionException;
import com.cyc.baseclient.CycClient;
import com.cyc.session.CycServerReleaseType;
import static com.cyc.session.CycSessionManager.getSession;
import com.cyc.session.SessionApiException;
import com.cyc.session.compatibility.CycClientRequirement;
import com.cyc.session.compatibility.CycClientRequirementList;
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
  
  private static final Logger LOG = LoggerFactory.getLogger(TestUtils.class);

  private static final KBPopulator populator = new KBPopulator();
  
  synchronized public static void ensureKBPopulated(CycAccess access) throws CycConnectionException {
    if (!populator.isAlreadyCalledForKB(access)){
      populator.populate(access);
    }
  }
  
  public static boolean isCurrentKBAlreadyPopulated() throws CycConnectionException {
    return populator.isAlreadyCalledForKB(getCyc());
  }
  
  public static void ensureTestEnvironmentInitialized() throws SessionApiException, CycConnectionException {
      CycAccess access = CycAccessManager.getCurrentAccess();
      ensureKBPopulated(access);
  }
  
  public static CycAccess getCyc() throws CycConnectionException {
    CycAccess cyc = null;
    try {
      cyc = CycAccessManager.getCurrentAccess();
      ensureKBPopulated(cyc);
    } catch (SessionApiException ex) {
      throw new BaseClientRuntimeException(ex);
    } 
//    throw new UnsupportedOperationException("FIXME NATHAN SERIOUSLY.");0
    return cyc;
  }
  
  public static void assumeCycSessionRequirement(CycSessionRequirement requirement) {
    // TODO: move this into some central test library
    try {
      org.junit.Assume.assumeTrue(requirement.isCompatible(getSession()));
    } catch (SessionApiException ex) {
      ex.printStackTrace(System.err);
      throw new RuntimeException(ex);
    }
  }
  
  public static void assumeCycSessionRequirements(CycSessionRequirementList requirements) {
    // TODO: move this into some central test library
    try {
      org.junit.Assume.assumeTrue(requirements.isCompatible());
    } catch (SessionApiException ex) {
      ex.printStackTrace(System.err);
      throw new RuntimeException(ex);
    }
  }
  
  public static void assumeCycClientRequirement(CycClientRequirement requirement) {
    // TODO: move this into some central test library
    try {
      org.junit.Assume.assumeTrue(requirement.isCompatible((CycClient) getCyc()));
    } catch (CycConnectionException ex) {
      ex.printStackTrace(System.err);
      throw new RuntimeException(ex);
    }
  }
  
  public static void assumeCycClientRequirements(CycClientRequirementList requirements) {
    // TODO: move this into some central test library
    try {
      org.junit.Assume.assumeTrue(requirements.isCompatible((CycClient) getCyc()));
    } catch (CycConnectionException ex) {
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
  
  public static boolean isEnterpriseCyc() {
    try {
      return CycServerReleaseType.ENTERPRISECYC.equals(getCyc().getServerInfo().getSystemReleaseType());
    } catch (CycConnectionException ex) {
      ex.printStackTrace(System.err);
      throw new RuntimeException(ex);
    } catch (SessionApiException ex) {
      ex.printStackTrace(System.err);
      throw new RuntimeException(ex);
    }
  }
  
  public static void skipTest(Object testCase, String testName, String msg) {
    LOG.warn("Skipping {}#{}: {}", testCase.getClass().getSimpleName(), testName, msg);
    org.junit.Assume.assumeTrue(msg, false);
  }
}
