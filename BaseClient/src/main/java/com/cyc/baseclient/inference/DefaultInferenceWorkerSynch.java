package com.cyc.baseclient.inference;

/*
 * #%L
 * File: DefaultInferenceWorkerSynch.java
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

//// Internal Imports
import com.cyc.base.BaseClientRuntimeException;
import com.cyc.base.CycAccess;
import com.cyc.base.CycAccessManager;
import com.cyc.base.inference.InferenceWorkerSynch;
import com.cyc.base.inference.InferenceWorkerListener;
import com.cyc.base.inference.InferenceWorker;
import com.cyc.baseclient.api.SubLWorkerEvent;
import com.cyc.base.CycApiException;
import com.cyc.baseclient.api.CycConnection;
import com.cyc.baseclient.CommonConstants;
import com.cyc.base.CycConnectionException;
import com.cyc.session.CycServer;
import com.cyc.base.cycobject.ELMt;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.CycTimeOutException;
import com.cyc.base.conn.WorkerStatus;
import com.cyc.base.cycobject.FormulaSentence;
import com.cyc.base.cycobject.CycList;
import com.cyc.base.cycobject.CycSymbol;
import com.cyc.query.InferenceParameters;
import com.cyc.base.inference.InferenceResultSet;
import com.cyc.query.InferenceStatus;
import com.cyc.query.InferenceSuspendReason;

//// External Imports
import java.util.*;
import java.io.*;
import com.cyc.baseclient.parser.CycLParserUtil;

/**
 * <P>DefaultInferenceWorkerSynch provides a synchronous version of the DefaultInferenceWorker.

 <p>
 * @author tbrussea, zelal
 * @date July 27, 2005, 11:55 AM
 * @version $Id: DefaultInferenceWorkerSynch.java 161606 2015-10-16 21:52:46Z nwinant $
 */
public class DefaultInferenceWorkerSynch extends DefaultInferenceWorker implements InferenceWorkerSynch {
  
  //// Constructors
  
  /**
   * Creates a new instance of DefaultInferenceWorker.
   * @param query the query sentence
   * @param mt the inference microtheory
   * @param queryProperties the query properties
   * @param access the Cyc communications object
   * @param timeoutMsecs the timeout duration in milliseconds
   */
  public DefaultInferenceWorkerSynch(String query, ELMt mt, 
      InferenceParameters queryProperties, CycAccess access, long timeoutMsecs) {
    this(makeCycLSentence(query, access), mt, queryProperties,
      null, null, false, access, timeoutMsecs);
  }
  
  /**
   * Creates a new instance of DefaultInferenceWorker.
   * @param query the query sentence
   * @param mt the inference microtheory
   * @param queryProperties the query properties
   * @param access the Cyc communications object
   * @param timeoutMsecs the timeout duration in milliseconds
   */
  public DefaultInferenceWorkerSynch(CycList<Object> query, ELMt mt, 
      InferenceParameters queryProperties, CycAccess access, long timeoutMsecs) {
    super(query, mt, queryProperties, access, timeoutMsecs);
    init();
  }

  /**
   * Creates a new instance of DefaultInferenceWorker.
   * @param query the query sentence
   * @param mt the inference microtheory
   * @param queryProperties the query properties
   * @param access the Cyc communications object
   * @param timeoutMsecs the timeout duration in milliseconds
   */
  public DefaultInferenceWorkerSynch(FormulaSentence query, ELMt mt,
      InferenceParameters queryProperties, CycAccess access, long timeoutMsecs) {
    super(query.getArgs(), mt, queryProperties, access, timeoutMsecs);
    init();
  }
  
  /**
   * Creates a new instance of DefaultInferenceWorker.
   * @param query the query sentence
   * @param mt the inference microtheory
   * @param queryProperties the query properties
   * @param nlGenerationProperties the natural language generation properties
   * @param answerProcessingFunction the answer processing function
   * @param optimizeVariables the indicatior for whether variables are optimized
   * @param access the Cyc communications object
   * @param timeoutMsecs the timeout duration in milliseconds
   */
  public DefaultInferenceWorkerSynch(String query, ELMt mt, 
      InferenceParameters queryProperties, Map nlGenerationProperties, 
      CycSymbol answerProcessingFunction, boolean optimizeVariables, 
      CycAccess access, long timeoutMsecs) {
    this(makeCycLSentence(query, access), mt, queryProperties, nlGenerationProperties,
      answerProcessingFunction, optimizeVariables, access, timeoutMsecs);
  }
  
  /**
   * Creates a new instance of DefaultInferenceWorker.
   * @param query the query sentence
   * @param mt the inference microtheory
   * @param queryProperties the query properties
   * @param nlGenerationProperties the natural language generation properties
   * @param answerProcessingFunction the answer processing function
   * @param optimizeVariables the indicatior for whether variables are optimized
   * @param access the Cyc communications object
   * @param timeoutMsecs the timeout duration in milliseconds
   */
  public DefaultInferenceWorkerSynch(CycList<Object> query, ELMt mt, 
      InferenceParameters queryProperties, Map nlGenerationProperties, 
      CycSymbol answerProcessingFunction, boolean optimizeVariables, 
      CycAccess access, long timeoutMsecs) {
    this(query, mt, queryProperties, nlGenerationProperties, answerProcessingFunction,
      optimizeVariables, access, timeoutMsecs, CycConnection.NORMAL_PRIORITY);
  }

  /**
   * Creates a new instance of DefaultInferenceWorker.
   * @param query the query sentence
   * @param mt the inference microtheory
   * @param queryProperties the query properties
   * @param nlGenerationProperties the natural language generation properties
   * @param answerProcessingFunction the answer processing function
   * @param optimizeVariables the indicatior for whether variables are optimized
   * @param access the Cyc communications object
   * @param timeoutMsecs the timeout duration in milliseconds
   */
  public DefaultInferenceWorkerSynch(FormulaSentence query, ELMt mt,
      InferenceParameters queryProperties, Map nlGenerationProperties,
      CycSymbol answerProcessingFunction, boolean optimizeVariables,
      CycAccess access, long timeoutMsecs) {
    this(query, mt, queryProperties, nlGenerationProperties, answerProcessingFunction,
      optimizeVariables, access, timeoutMsecs, CycConnection.NORMAL_PRIORITY);
  }

  /**
   * Creates a new instance of DefaultInferenceWorker.
   * @param query the query sentence
   * @param mt the inference microtheory
   * @param queryProperties the query properties
   * @param nlGenerationProperties the natural language generation properties
   * @param answerProcessingFunction the answer processing function
   * @param optimizeVariables the indicatior for whether variables are optimized
   * @param access the Cyc communications object
   * @param timeoutMsecs the timeout duration in milliseconds
   * @param priority the SubL task priority
   */
  public DefaultInferenceWorkerSynch(CycList query, ELMt mt, 
      InferenceParameters queryProperties, Map nlGenerationProperties, 
      CycSymbol answerProcessingFunction, boolean optimizeVariables, 
      CycAccess access, long timeoutMsecs, Integer priority) {
    super(query, mt, queryProperties, nlGenerationProperties, answerProcessingFunction,
      optimizeVariables, access, timeoutMsecs, priority);
    init();
  }

  /**
   * Creates a new instance of DefaultInferenceWorker.
   * @param query the query sentence
   * @param mt the inference microtheory
   * @param queryProperties the query properties
   * @param nlGenerationProperties the natural language generation properties
   * @param answerProcessingFunction the answer processing function
   * @param optimizeVariables the indicatior for whether variables are optimized
   * @param access the Cyc communications object
   * @param timeoutMsecs the timeout duration in milliseconds
   * @param priority the SubL task priority
   */
  public DefaultInferenceWorkerSynch(FormulaSentence query, ELMt mt,
      InferenceParameters queryProperties, Map nlGenerationProperties,
      CycSymbol answerProcessingFunction, boolean optimizeVariables,
      CycAccess access, long timeoutMsecs, Integer priority) {
    super(query, mt, queryProperties, nlGenerationProperties, answerProcessingFunction,
      optimizeVariables, access, timeoutMsecs, priority);
    init();
  }
  
  //// Public Area

  /** Performs the synchronous inference operation. */
  public DefaultResultSet executeQuery()
  throws CycConnectionException, CycTimeOutException, CycApiException {
    List results = performSynchronousInference();
    return new DefaultResultSet(results, this);
  }
  
  /** Performs the synchronous inference operation. */
  public List performSynchronousInference()
  throws CycConnectionException, CycTimeOutException, CycApiException {
    if (getStatus() == WorkerStatus.NOT_STARTED_STATUS) {
      start();
    }
    if (getStatus() == WorkerStatus.WORKING_STATUS) {
      try {
        synchronized (lock) {
          lock.wait(getTimeoutMsecs());
          if (getStatus() == WorkerStatus.WORKING_STATUS) {
            try {
              this.abort();
            } finally {
              this.fireSubLWorkerTerminatedEvent(new SubLWorkerEvent(this,
                WorkerStatus.EXCEPTION_STATUS, 
                new CycTimeOutException("Communications took more than: " 
                + getTimeoutMsecs() + " msecs.\nWhile trying to execute inference: \n" 
                + getSubLCommand().toPrettyCyclifiedString(""))));
            }
          }
        }
      } catch (Exception xcpt) {
        throw new BaseClientRuntimeException(xcpt);
      }
    }
    if (getException() != null) { 
      try {
        throw getException(); 
      } catch (IOException ioe) {
        throw new CycConnectionException(ioe); 
      } catch (Exception xcpt) {
        if (xcpt instanceof BaseClientRuntimeException) {
          throw (RuntimeException)xcpt;
        } else {
          throw new BaseClientRuntimeException(xcpt);
        }
      }
    }
    return getAnswers();
  }
  
  /** Returns the exception thrown in the process of doing the work.
   * The value will be null if now exception has been thrown.
   * @return the exception thrown in the process of doing the work
   */  
  public Exception getException() { return e; }
  
  //// Protected Area
  
  /** Sets the exception.
   * @param e The exception that was thrown while processing this worker
   */  
  protected void setException(Exception e) {
    this.e = e;
  }
  
  //// Private Area
  
  /** Initializes this object by registering an inference event listener. */
  private void init() {
    addInferenceListener(new InferenceWorkerListener () {
      public void notifyInferenceAnswersAvailable(InferenceWorker inferenceWorker, List newAnswers) {
      }

      public void notifyInferenceCreated(InferenceWorker inferenceWorker) {
      }

      public void notifyInferenceStatusChanged(InferenceStatus oldStatus, InferenceStatus newStatus, InferenceSuspendReason suspendReason, InferenceWorker inferenceWorker) {
      }

      public void notifyInferenceTerminated(InferenceWorker inferenceWorker, Exception e) {
        synchronized(lock) {
          lock.notify();
        }
      }
    });
    
  }
  
  private static FormulaSentence makeCycLSentence(String query, CycAccess access) {
    try {
      return CycLParserUtil.parseCycLSentence(query, true, access);
    } catch (Exception e) {
      throw new BaseClientRuntimeException(e);
    }
  }

  
  //// Internal Rep
  
  /** the lock for the inference timeout */
  private final Object lock = new Object();
  
  /** the exception that was thrown while processing this worker */
  private Exception e = null;

  
  //// Main
  
  /** Provides a demonstration main method.
   *
   * @param args the command line arguments (ignored)
   */
  public static void main(String[] args) {
    System.out.println("Starting");
    CycAccess access = null;
    try {
      access = CycAccessManager.getCurrentAccess();
      String query = "(#$isa ?X #$Dog)";
      InferenceWorkerSynch worker = new DefaultInferenceWorkerSynch(query, 
        CommonConstants.INFERENCE_PSC, null, access, 500000);
      InferenceResultSet rs = worker.executeQuery();
      try {
        int indexOfX = rs.findColumn("?X");
        while (rs.next()) {
          CycObject curDog = rs.getCycObject(indexOfX);
          System.out.println("Got dog: " + curDog.cyclify());
        }
        System.out.flush();
      } finally {
        rs.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (access != null) {
        access.close();
      }
    }
    System.out.println("Finished");
    System.exit(0);
  }

  @Override
  public InferenceStatus getInferenceStatus() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
}
