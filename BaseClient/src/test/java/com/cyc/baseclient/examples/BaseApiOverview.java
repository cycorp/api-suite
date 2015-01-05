package com.cyc.baseclient.examples;

/*
 * #%L
 * File: BaseApiOverview.java
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
import com.cyc.base.CycConnectionException;
import com.cyc.session.SessionApiException;
import com.cyc.base.cycobject.CycConstant;
import com.cyc.base.cycobject.FormulaSentence;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.CycVariable;
import com.cyc.base.cycobject.ELMt;
import com.cyc.base.inference.InferenceParameters;
import com.cyc.base.inference.InferenceResultSet;
import com.cyc.base.inference.InferenceStatus;
import com.cyc.base.inference.InferenceSuspendReason;
import com.cyc.base.inference.InferenceWorker;
import com.cyc.base.inference.InferenceWorkerListener;
import com.cyc.base.inference.InferenceWorkerSynch;

import static com.cyc.baseclient.CommonConstants.*;
import static com.cyc.baseclient.testing.TestConstants.*;

import com.cyc.baseclient.CycObjectFactory;
import com.cyc.baseclient.cycobject.CycFormulaSentence;
import com.cyc.baseclient.cycobject.CycVariableImpl;
import com.cyc.baseclient.inference.DefaultInferenceWorker;
import com.cyc.baseclient.inference.KBQueryFactory;
import com.cyc.baseclient.inference.params.DefaultInferenceParameters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nwinant
 */
public class BaseApiOverview {
  
  protected CycAccess getCyc() throws SessionApiException {
    return CycAccessManager.getAccess();
  }
  
  
  
  public void simpleSynchronousQuery() throws SessionApiException, CycConnectionException {
    final CycAccess access = getCyc();
    
    final CycVariableImpl var1 = new CycVariableImpl("?var");
    final FormulaSentence query = CycFormulaSentence.makeCycFormulaSentence(
            ISA, var1, DOG);
    
    final DefaultInferenceParameters params = new DefaultInferenceParameters(access);
    // You can now override params like so:
    params.setMaxTransformationDepth(1);
    params.setBrowsable(true);
    params.setMaxTime(30000);
    params.put(CycObjectFactory.makeCycSymbol(":INFERENCE-MODE"), 
            CycObjectFactory.makeCycSymbol(":MINIMAL"));
    // etc...
    
    final ELMt queryMt = access.getObjectTool().makeELMt("InferencePSC");
    
    InferenceResultSet rs = null;
    try {
      rs = access.getInferenceTool().executeQuery(query, queryMt, params);
      while (rs.next()){
        rs.getCycObject(var1);
      }
    } finally {
      if (rs != null) { rs.close(); }
    }
  }
  
  
  
  
  public void indexicalQuery() throws SessionApiException, CycConnectionException {
    final CycAccess access = getCyc();
    
    final int timeoutMS = 30000;
    final boolean sync = true;    
    final CycConstant kbq = access.getLookupTool().getConstantByName("#$SampleIndexicalQuery");
    final InferenceWorkerSynch worker = (InferenceWorkerSynch) KBQueryFactory.prepareKBQuery(access, kbq, timeoutMS, sync);
    InferenceResultSet rs = null;
    try {
      rs = worker.executeQuery();
      
//      ... iterate over RS...
      
    } finally {
      if (rs != null) { rs.close(); }
    }
  }
  
  
  
  
  public void treeTemplatedQuery() throws SessionApiException, CycConnectionException {
    final CycAccess access = getCyc();
    
    final int timeoutMS = 30000;
    final boolean sync = true;
    final CycVariableImpl eventVar = new CycVariableImpl(":EVENT");
    final CycVariableImpl locVar = new CycVariableImpl(":LOCATION");
//    final CycObject event = ...;
//    final CycObject location = ...;
    
    final CycConstant kbq = access.getLookupTool().getConstantByName("#$SampleIndexicalQuery");
    final Map<CycObject,Object> substitutions = new HashMap<CycObject,Object>();
//    substitutions.put(eventVar, event);
//    substitutions.put(locVar, location);
    final InferenceWorkerSynch worker = (InferenceWorkerSynch) KBQueryFactory.prepareKBQueryTreeTemplate(access, kbq, substitutions, timeoutMS, sync);
    InferenceResultSet rs = null;
    try {
      rs = worker.executeQuery();
      
//      ??... iterate over RS...
    } finally {
      if (rs != null) { rs.close(); }
    }
  }
  
  
  
  
  public void asynchronousQuery() throws SessionApiException, CycConnectionException {
    final CycAccess access = getCyc();
    
    final CycVariableImpl var1 = new CycVariableImpl("?var");
    final FormulaSentence query = CycFormulaSentence.makeCycFormulaSentence(
            ISA, var1, DOG);
    
    final InferenceParameters params = new DefaultInferenceParameters(access);
// You can now override params like so:
// params.setMaxTransformationDepth(1);
// params.setBrowsable(true);
// params.setMaxTime(30000);
// params.put(CycObjectFactory.makeCycSymbol(":INFERENCE-MODE"), CycObjectFactory.makeCycSymbol(":MINIMAL"));
// etc...
    
    final ELMt queryMt = access.getObjectTool().makeELMt("InferencePSC");
    
    final InferenceWorker worker = new DefaultInferenceWorker(query, queryMt, params, access, 30000);
    worker.addInferenceListener(new InferenceWorkerListener() {
      @Override
      public void notifyInferenceCreated(InferenceWorker inferenceWorker) {
        System.out.println("Inference created");
      }
      
      @Override
      public void notifyInferenceStatusChanged(InferenceStatus oldStatus, InferenceStatus newStatus, InferenceSuspendReason suspendReason, InferenceWorker inferenceWorker) {
        System.out.println("Inference status changed: " + newStatus);
      }
      
      @Override
      public void notifyInferenceAnswersAvailable(InferenceWorker inferenceWorker, List newAnswers) {
        
        // THIS IS WHERE YOU COLLECT ANY INCREMENTAL ANSWERS...
        
        for (Object o : newAnswers) {
          System.out.println("> " + o);
        }
      }
      
      @Override
      public void notifyInferenceTerminated(InferenceWorker inferenceWorker, Exception e) {
        System.out.println("Inference terminated: " + e);
      }
    });
    
// And now we can actually run the worker...
    
    try {
      System.out.println("Beginning...");
      worker.start();
      while (!worker.isDone()) {
        try {
          Thread.sleep(100);
        } catch (InterruptedException ex) {
          Thread.currentThread().interrupt();
        }
      }
      System.out.println(" ... done!");
    } finally {
      if (!worker.isDone()) {
        worker.abort();
      }
      worker.releaseInferenceResources(30000);
    }
  }
  
  
  public void simpleAssertion() throws SessionApiException, CycConnectionException {
    final CycAccess access = getCyc();
    
    // @todo implement
    
//    access.getAssertTool().
  }
  
  
  
  
  public void assertTemplatedSentences() throws SessionApiException, CycConnectionException {
    final CycAccess access = getCyc();
    
    final CycVariable eventVar = new CycVariableImpl(":EVENT");
    final CycVariable locVar = new CycVariableImpl(":LOCATION");
    final FormulaSentence sentenceTemplate = CycFormulaSentence.makeCycFormulaSentence(
            access.getLookupTool().getConstantByName("eventOccuredAtPlace"),
            eventVar,
            locVar
    );
    
//    final CycObject event = ...;
//    final CycObject location = ...;
    final CycConstant sentenceMt = access.getLookupTool().getConstantByName("#$AssertionMt");
    final Map<CycObject,Object> substitutions = new HashMap<CycObject,Object>();
//    substitutions.put(eventVar, event);
//    substitutions.put(locVar, location);
    final FormulaSentence newSentence = sentenceTemplate.treeSubstitute(access, substitutions);
    access.getAssertTool().assertWithTranscriptAndBookkeeping(newSentence, sentenceMt);
  }
  
  
  
  
  public void simpleKill() throws SessionApiException, CycConnectionException {
    final CycAccess access = getCyc();
    
    // @todo implement
    
//    access.getUnassertTool().kill(...);
  }
  
  
  
    
  public void justificationExample() throws SessionApiException, CycConnectionException {
    final CycAccess access = getCyc();
    
    // @todo implement
    
//   ??????
  }
  
  
  
}
