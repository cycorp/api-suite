package com.cyc.baseclient.connection;

/*
 * #%L
 * File: SublWorkerSynch.java
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
import com.cyc.base.exception.CycApiException;
import com.cyc.baseclient.exception.CycTaskInterruptedException;
import com.cyc.base.exception.CycConnectionException;
import com.cyc.base.exception.CycTimeOutException;
import com.cyc.base.conn.Worker;

//// External Imports
import java.io.*;

/**
 * <P>WorkerSynch is designed to provide a handle for a particular 
 communication event with a Cyc server in a synchronous manner. 
 DefaultWorkerSynch provides the default
 implementation while Worker and DefaultWorker provide
 asynchronous communications capabilities. Currently, WorkerSynchs are one-shot,
 i.e., a new WorkerSynch needs to be created for every new communication.
 WorkerSynchs are cancelable, time-outable and provide means for incremental
 return results.
  
 <P>Example usage: <pre>
 try {
    CycAccess access = CycAccessManager.getAccess();
    WorkerSynch worker = new DefaultWorkerSynch("(+ 1 1)", access);
    Object work = worker.getWork();
    System.out.println("Got worker: " + worker + "\nGot result: " + work + ".");
  } catch (Exception e) {
    e.printStackTrace();
  }
 </pre>
 *
 * @author tbrussea
 * @date March 17, 2004, 11:26 AM
 * @version $Id: SublWorkerSynch.java 162904 2015-12-02 18:35:34Z nwinant $
 */
public interface SublWorkerSynch extends Worker {
  
  /** This method starts communications with the Cyc server, waits for the work
   * to be performed, then returns the resultant work.
   * @throws CycConnectionException thown when there is a problem with the communications
   * protocol with the CycServer
   * @throws CycTimeOutException thrown if the worker takes to long to return results
   * @throws CycApiException thrown if any other error occurs
   * @return The work produced by this WorkerSynch
   */  
  Object getWork() throws CycConnectionException, CycTimeOutException, CycApiException, CycTaskInterruptedException;
  
}
