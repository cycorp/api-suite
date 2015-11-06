package com.cyc.session;

/*
 * #%L
 * File: CycSessionOptionsIT.java
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
import com.cyc.base.CycAccessOptions;
import com.cyc.baseclient.CycClientSession;
import static com.cyc.baseclient.testing.TestConstants.CYC_ADMINISTRATOR;
import static com.cyc.baseclient.testing.TestConstants.GENERAL_CYC_KE;
import static com.cyc.baseclient.testing.TestConstants.LENAT;
import com.cyc.kb.config.DefaultContext;
import junit.framework.TestCase;
import org.junit.Test;

/**
 *
 * @author nwinant
 */
public class CycSessionOptionsIT extends TestCase {
  
  public CycSessionOptionsIT(String testName) {
    super(testName);
  }
  
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    session = CycSessionManager.getCurrentSession();
    cyc = CycAccessManager.getCurrentAccess();
    sessionOptions = ((CycClientSession) session).getOptions();
    cycOptions = cyc.getOptions();
    sessionOptions.reset();
  }
  
  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
  }
  
  
  // Fields
  
  CycSession session;
  CycAccess cyc;
  SessionOptions sessionOptions;
  CycAccessOptions cycOptions;
    
  
  // Tests
  
  @Test
  public void testEquality() {
    assertNotNull(sessionOptions);
    assertNotNull(cycOptions);
    assertEquals(sessionOptions, session.getOptions());
    assertEquals(cycOptions, cyc.getOptions());
    assertEquals(sessionOptions, cycOptions);
  }
  
  @Test
  public void testCyclist() throws Exception {
    assertNull(sessionOptions.getCyclistName());
    assertNull(cycOptions.getCyclistName());
    
    // Set via CycSession w/ String toString
    sessionOptions.setCyclistName(CYC_ADMINISTRATOR.toString());
    assertEquals("CycAdministrator", sessionOptions.getCyclistName());
    assertEquals(sessionOptions.getCyclistName(), cycOptions.getCyclistName());
    assertEquals("CycAdministrator", cycOptions.getCyclistName());
    assertEquals(CYC_ADMINISTRATOR, cycOptions.getCyclist());

    // Set via CycAccess w/ String toString
    cycOptions.setCyclistName(LENAT.toString());
    assertEquals("Lenat", cycOptions.getCyclistName());
    assertEquals(LENAT, cycOptions.getCyclist());
    assertEquals(sessionOptions.getCyclistName(), cycOptions.getCyclistName());
    assertEquals("Lenat", sessionOptions.getCyclistName());
    
    // Reset cyclist via CycSession
    sessionOptions.resetCyclist();
    assertNull(sessionOptions.getCyclistName());
    assertNull(cycOptions.getCyclistName());
    
    // Set via CycAccess w/ Fort
    cycOptions.setCyclist(CYC_ADMINISTRATOR);
    assertEquals("CycAdministrator", sessionOptions.getCyclistName());
    assertEquals(sessionOptions.getCyclistName(), cycOptions.getCyclistName());
    assertEquals("CycAdministrator", cycOptions.getCyclistName());
    assertEquals(CYC_ADMINISTRATOR, cycOptions.getCyclist());
    
    // Reset cyclist via CycAccess
    cycOptions.resetCyclist();
    assertNull(cycOptions.getCyclistName());
    assertNull(sessionOptions.getCyclistName());
    
    // Set via CycSession w/ String stringApiValue
    sessionOptions.setCyclistName(LENAT.stringApiValue());
    assertEquals("Lenat", sessionOptions.getCyclistName());
    assertEquals(sessionOptions.getCyclistName(), cycOptions.getCyclistName());
    assertEquals("Lenat", cycOptions.getCyclistName());
    assertEquals(LENAT, cycOptions.getCyclist());

    sessionOptions.reset();
    assertNull(sessionOptions.getCyclistName());
    assertNull(cycOptions.getCyclistName());
  }
  
  @Test
  public void testKePurpose() throws Exception {
    // TODO: flesh this test out - nwinant, 2015-10-23
    assertNull(sessionOptions.getKePurposeName());
    assertNull(cycOptions.getKePurpose());
    
    sessionOptions.setKePurposeName("GeneralCycKE");
    assertEquals("GeneralCycKE", sessionOptions.getKePurposeName());
    assertEquals(GENERAL_CYC_KE, cycOptions.getKePurpose());
    
    sessionOptions.resetKePurpose();
    assertNull(sessionOptions.getKePurposeName());
    assertNull(cycOptions.getKePurpose());
    
    cycOptions.setKePurpose(GENERAL_CYC_KE);
    assertEquals(GENERAL_CYC_KE, cycOptions.getKePurpose());
    assertEquals("GeneralCycKE", sessionOptions.getKePurposeName());
    
    cycOptions.resetKePurpose();
    assertNull(sessionOptions.getKePurposeName());
    assertNull(cycOptions.getKePurpose());
  }
  
  @Test
  public void testDefaultContext() throws Exception {
    // TODO: flesh this test out by creating corresponding test for KBAPIDefaultContext. - nwinant, 2015-10-23
    DefaultContext ctx = sessionOptions.getDefaultContext();
    assertNotNull(ctx);
    
    Exception ex1 = null;
    try {
      assertNull(ctx.forAssertion());
    } catch (NullPointerException npe) {
      ex1 = npe;
    }
    assertNotNull(ex1);
    
    Exception ex2 = null;
    try {
      assertNull(ctx.forQuery());
    } catch (NullPointerException npe) {
      ex2 = npe;
    }
    assertNotNull(ex2);
    
    sessionOptions.resetDefaultContext();
    cycOptions.resetDefaultContext();
  }
  
  @Test
  public void testShouldTranscriptOperations() throws Exception {
    assertTrue(sessionOptions.getShouldTranscriptOperations());
    assertTrue(cycOptions.getShouldTranscriptOperations());
    
    sessionOptions.setShouldTranscriptOperations(false);
    assertFalse(sessionOptions.getShouldTranscriptOperations());
    assertFalse(cycOptions.getShouldTranscriptOperations());
    
    sessionOptions.setShouldTranscriptOperations(true);
    assertTrue(sessionOptions.getShouldTranscriptOperations());
    assertTrue(cycOptions.getShouldTranscriptOperations());
    sessionOptions.setShouldTranscriptOperations(true);
    assertTrue(sessionOptions.getShouldTranscriptOperations());
    assertTrue(cycOptions.getShouldTranscriptOperations());
    
    sessionOptions.setShouldTranscriptOperations(false);
    assertFalse(sessionOptions.getShouldTranscriptOperations());
    assertFalse(cycOptions.getShouldTranscriptOperations());
    sessionOptions.setShouldTranscriptOperations(false);
    assertFalse(sessionOptions.getShouldTranscriptOperations());
    assertFalse(cycOptions.getShouldTranscriptOperations());
    
    sessionOptions.resetShouldTranscriptOperations();
    assertTrue(sessionOptions.getShouldTranscriptOperations());
    assertTrue(cycOptions.getShouldTranscriptOperations());
    
    sessionOptions.setShouldTranscriptOperations(false);
    assertFalse(sessionOptions.getShouldTranscriptOperations());
    assertFalse(cycOptions.getShouldTranscriptOperations());
    
    cycOptions.resetShouldTranscriptOperations();
    assertTrue(sessionOptions.getShouldTranscriptOperations());
    assertTrue(cycOptions.getShouldTranscriptOperations());
  }
  
}
