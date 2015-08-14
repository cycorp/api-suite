package com.cyc.baseclient;

/*
 * #%L
 * File: CycAccessTest.java
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

import com.cyc.baseclient.testing.TestUtils;
import com.cyc.base.CycAccess;
import com.cyc.base.CycAccessManager;
import com.cyc.base.cycobject.FormulaSentence;
import com.cyc.baseclient.inference.params.DefaultInferenceParameters;
import com.cyc.baseclient.cycobject.CycVariableImpl;
import com.cyc.baseclient.cycobject.CycFormulaSentence;
import com.cyc.base.cycobject.ELMt;
import com.cyc.query.InferenceParameters;
import com.cyc.base.inference.InferenceResultSet;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static com.cyc.baseclient.CommonConstants.*;
import static com.cyc.baseclient.testing.TestConstants.*;
import com.cyc.session.SessionApiException;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;


/**
 * Most of the test GeneralUnitTest.java is really testing CycClient, so CycAccess should not be considered
 tested unless GeneralUnitTest also passes.
 * 
 * @author daves
 */
public class CycAccessTest  {

  @BeforeClass
  public static void setUpClass() throws MalformedURLException, Exception {
    TestUtils.ensureTestEnvironmentInitialized();
  }
  /*
  
  TODO: reimplement, to take CycSessionManager into account? - nwinant
  
  @Test
  public void testHasCurrent() {
    boolean errorFree = false;
    try {
      boolean hasCurrent = CycClientManager.hasCurrentAccess();
      errorFree = true;
      assertTrue(hasCurrent);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    assertTrue(errorFree);
  }
  */
  
  @Test 
  public void testGetOptions() throws SessionApiException {
    final CycAccess access = CycAccessManager.getAccess();
    assertNotNull(access.getOptions());
    assertNull(access.getOptions().getCyclistName());
    assertNull(access.getOptions().getKePurposeName());
    
    // TODO: expand to include get/setShouldTranscriptOperations and get/setDefaultContext
    
    access.getOptions().reset();
    assertNull(access.getOptions().getCyclistName());
    assertNull(access.getOptions().getKePurposeName());
  }
  
  
  @Test
  public void testExecuteQuery() {
    boolean errorFree = false;
    try {
      final List answers = new ArrayList();
      final CycAccess access = CycAccessManager.getAccess();
      
      final CycVariableImpl var1 = new CycVariableImpl("?var");
      final FormulaSentence query = CycFormulaSentence.makeCycFormulaSentence(
              access.getLookupTool().getConstantByName(ISA.stringApiValue()),
              var1,
              access.getLookupTool().getConstantByName(DOG.stringApiValue()));
      
      final InferenceParameters params = new DefaultInferenceParameters(access);
      /*
      You can now override params like so:
      params.setMaxTransformationDepth(1);
      params.setBrowsable(true);
      params.setMaxTime(30000);
      params.put(CycObjectFactory.makeCycSymbol(":INFERENCE-MODE"), CycObjectFactory.makeCycSymbol(":MINIMAL"));
      etc...
      */
      final ELMt queryMt = access.getObjectTool().makeELMt(INFERENCE_PSC.toString());
      
      InferenceResultSet rs = null;
      try {
        rs = access.getInferenceTool().executeQuery(query, queryMt, params);
        while (rs.next()){
          answers.add(rs.getCycObject(var1));
          //System.out.println("> " + rs.getCycObject(var1));
        }
      } finally {
        if (rs != null) { rs.close(); }
      }

      errorFree = true;
      assertFalse(answers.isEmpty());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    assertTrue(errorFree);
  }
  
  @Test
  public void testSetCyclist() throws Exception {
    System.out.println("\n**** testSetCyclist ****");
    CycAccess cyc = CycAccessManager.getAccess();
    assertTrue(cyc.getOptions().getCyclistName() == null);
    
    cyc.getOptions().setCyclistName(CYC_ADMINISTRATOR.toString());
    assertTrue(cyc.getOptions().getCyclistName().toString().equals("CycAdministrator"));

    cyc.getOptions().setCyclistName(LENAT.stringApiValue());
    assertTrue(cyc.getOptions().getCyclistName().toString().equals("Lenat"));

    cyc.getOptions().setCyclistName(LENAT.toString());
    assertTrue(cyc.getOptions().getCyclistName().toString().equals("Lenat"));
    
    cyc.getOptions().clearCyclist();
    assertNull(cyc.getOptions().getCyclistName());
    
    System.out.println("**** testSetCyclist OK ****");
  }
}
