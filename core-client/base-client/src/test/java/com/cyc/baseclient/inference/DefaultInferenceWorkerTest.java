package com.cyc.baseclient.inference;

/*
 * #%L
 * File: DefaultInferenceWorkerTest.java
 * Project: Base Client
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
import com.cyc.base.cycobject.FormulaSentence;
import com.cyc.baseclient.inference.params.DefaultInferenceParameters;
import com.cyc.base.inference.InferenceWorkerListener;
import com.cyc.base.inference.InferenceWorker;
import com.cyc.baseclient.cycobject.CycVariableImpl;
import com.cyc.baseclient.cycobject.CycFormulaSentence;
import com.cyc.base.cycobject.ElMt;
import com.cyc.query.InferenceParameters;
import com.cyc.query.InferenceStatus;
import com.cyc.query.InferenceSuspendReason;
import com.cyc.baseclient.testing.TestUtils;
import static com.cyc.baseclient.CommonConstants.*;
import static com.cyc.baseclient.testing.TestConstants.*;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;


/**
 * 
 * @author nwinant
 */
public class DefaultInferenceWorkerTest  {

  @BeforeClass
  public static void setUpClass() throws MalformedURLException, Exception {
    TestUtils.ensureTestEnvironmentInitialized();
  }
  
  @Test
  public void testInferenceWorkerListener() {
    boolean errorFree = false;
    try {
      final List answers = new ArrayList();
      final CycAccess access = CycAccessManager.getCurrentAccess();
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
      final ElMt queryMt = access.getObjectTool().makeElMt("InferencePSC");
      
      final InferenceWorker worker = new DefaultInferenceWorker(query, queryMt, params, access, 30000);
      worker.addInferenceListener(new InferenceWorkerListener() {
        @Override
        public void notifyInferenceCreated(InferenceWorker inferenceWorker) {
          //System.out.println("Inference created");
        }
        
        @Override
        public void notifyInferenceStatusChanged(InferenceStatus oldStatus, InferenceStatus newStatus, InferenceSuspendReason suspendReason, InferenceWorker inferenceWorker) {
          //System.out.println("Inference status changed: " + newStatus);
        }
        
        @Override
        public void notifyInferenceAnswersAvailable(InferenceWorker inferenceWorker, List newAnswers) {
          
          // THIS IS WHERE YOU COLLECT ANY INCREMENTAL ANSWERS...
          
          for (Object o : newAnswers) {
            answers.add(o);
            //System.out.println("> " + o);
          }
        }
        
        @Override
        public void notifyInferenceTerminated(InferenceWorker inferenceWorker, Exception e) {
          //System.out.println("Inference terminated: " + e);
        }
      });
      try {
        //System.out.println("Beginning...");
        worker.start();
        while (!worker.isDone()) {
          try {
            Thread.sleep(100);
          } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
          }
        }
        //System.out.println(" ... done!");
      } finally {
        if (!worker.isDone()) {
          worker.abort();
        }
        worker.releaseInferenceResources(30000);
      }
      errorFree = true;
      assertFalse(answers.isEmpty());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    assertTrue(errorFree);
  }
}
