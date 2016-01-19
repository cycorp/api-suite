package com.cyc.base.conn;

/*
 * #%L
 * File: Worker.java
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


//// External Imports
import com.cyc.base.Cancelable;
import com.cyc.base.exception.CycApiException;
import com.cyc.base.exception.CycConnectionException;
import com.cyc.base.exception.CycTimeOutException;
import com.cyc.base.cycobject.CycList;
import java.util.concurrent.BlockingQueue;

/**
 * <P>Worker is designed to provide a handle for a particular 
 communication event with a Cyc server. Workers by default handle
 communications asynchronously with callbacks through the 
 WorkerListener interface. DefaultWorker provides the default
 implementation while WorkerSynch and DefaultWorkerSynch provide
 synchronous communications capabilities. Currently, Workers are one-shot,
 i.e., a new Worker needs to be created for every new communication.
 Workers are cancelable, time-outable and provide means for incremental
 return results. Note, all callbacks happen in a single communications thread --
 if you need to do any significant work based on the results of a callback
 you *must* do it in a separate thread or risk delaying or breaking other
 communications with Cyc.
  
 <P>Example usage: <pre>
 try {
   CycAccess access = CycAccessManager.getAccess();
   Worker worker = new DefaultWorker("(+ 1 1)", access);
   worker.addListener(new WorkerListener() {
     public void notifyWorkerStarted(WorkerEvent event) {
       System.out.println("Received SubL Worker Event: \n" + event.toString(2) + "\n");
     }
     public void notifyWorkerDataAvailable(WorkerEvent event) {
       System.out.println("Received SubL Worker Event: \n" + event.toString(2) + "\n");
     }
     public void notifyWorkerTerminated(WorkerEvent event) {
       System.out.println("Received SubL Worker Event: \n" + event.toString(2) + "\n");
       System.exit(0);
     }
   });
   worker.start();
 } catch (Exception e) {
   e.printStackTrace();
 }
 </pre>
 * 
 * @see com.cyc.baseclient.DefaultSublWorker
 * @see com.cyc.baseclient.api.SublWorkerSynch
 * @see com.cyc.baseclient.api.DefaultSublWorkerSynch
 * @see com.cyc.base.conn.WorkerListener
 * @author tbrussea
 * @date March 17, 2004, 11:26 AM
 * @version $Id: Worker.java 163356 2016-01-04 20:55:47Z nwinant $
 */
public interface Worker extends Cancelable {
  
  /**
   * Returns the SubL command that will be evaluated to execute the
 work requested by this Worker.
   * @return the SubL command that will be evaluated to execute the
 work requested by this Worker
   */  
  CycList getSubLCommand();

  /**
   * The Cyc server that should do the work.
   * @return the Cyc server that should do the work
   */
//  CycAccess getCycServer();
  
  /**
   * Return's the unique id for this communication. It typically won't
   * be valid until the start event has been sent out.
   * @return the unique id for this communication. It typically won't
   * be valid until the start event has been sent out.
   */  
  Integer getId();
  
  /**
   * Return the task's priority. This is a value that meets the
   * constraints of SL:SET-PROCESS-PRIORITY.
   * @see com.cyc.baseclient.api.CycConnection#MAX_PRIORITY
   * @see com.cyc.baseclient.api.CycConnection#CRITICAL_PRIORITY
   * @see com.cyc.baseclient.api.CycConnection#NORMAL_PRIORITY
   * @see com.cyc.baseclient.api.CycConnection#BACKGROUND_PRIORITY
   * @see com.cyc.baseclient.api.CycConnection#MIN_PRIORITY
   * @return the priority of the process
   */
  Integer getPriority();
  
  /**
   * This call will start the Cyc server processing the worker's SubL command.
   * @throws CycConnectionException if communications with Cyc server fail
   * @throws CycTimeOutException if the communication with Cyc takes too long
   * @throws CycApiException all other errors
   */  
  void start() throws CycConnectionException, CycTimeOutException, CycApiException;
  
  /**
   * Attempts to terminate the work being processed by the Cyc server.
   * This method should be preferred to abort() in that it tries to use
   * the natural termination event messaging infracture.
   * @throws CycConnectionException if communications with the Cyc server fails
   */  
  void cancel() throws CycConnectionException;
  
  /**
   * Attempts to terminate the work being processed by the Cyc server.
   * This call bypasses the communications infrasture and results in
   * no new messages being sent back to the Java client.
   * @throws CycConnectionException
   */  
  void abort() throws CycConnectionException;
  
  /**
   * Returns the current status of this Worker.
   * @return the current status of this Worker
   */  
  WorkerStatus getStatus();
  
  /**
   * Returns A boolean indicating whether communications with the Cyc server
 on behalf of this Worker have terminated
   * @return a boolean indicating whether communications with the Cyc server
 on behalf of this Worker have terminated
   */
  boolean isDone();
  
  /** 
   * Returns the number of msecs that this communication will wait, once
 started, before throwing a CycTimeOutException. 0 msecs means to wait forever.
   * @return the number of msecs that this communication will wait before 
 throwing a CycTimeOutException
   */  
  long getTimeoutMsecs();
  
  /** Returns wether this communication should expect incremental results.
   * @return wether this communication should expect incremental results
   */  
  boolean expectIncrementalResults();
  
  /**
   * Returns all the WorkerListeners listening in on this
 Worker's events
   * @return all the WorkerListeners listening in on this
 Worker's events
   */  
  Object[] getListeners();

  /**
   * Adds the given listener to this Worker.
   * @param listener the listener that wishes to listen
 for events sent out by this Worker
   */  
  void addListener(WorkerListener listener);
  
  /** 
   * Removes the given listener from this Worker.
   * @param listener the listener that no longer wishes
 to receive events from this Worker
   */  
  void removeListener(WorkerListener listener);
  
  /** Removes all listeners from this Worker. */  
  void removeAllListeners();
  
  /**
   * Returns a string representation of the Worker.
   * @return a string representation of the Worker
   */  
  String toString();
  
  /**
   * Returns a string representation of the Worker.
   * @param indentLength the number of spaces to preceed each line of 
   * output String
   * @return a string representation of the Worker
   */  
  String toString(int indentLength);
  
  /**
   * Indicates whether this communication should be attempted even if
   * the current lease to the Cyc image has expired.
   */  
  boolean shouldIgnoreInvalidLeases();
  
  /** 
   * Public for implementation reasons only, this method should
   * only ever be called by subclasses of CycConnection.java.
   * @param event The start event that should be transmitted to
 listeners of this Worker
   */  
  void fireSubLWorkerStartedEvent(WorkerEvent event);
  
  /**
   * Public for implementation reasons only, this method should
   * only ever be called by subclasses of CycConnection.java.
   * @param event The data available event that should be transmitted to
 listeners of this Worker
   */  
  void fireSubLWorkerDataAvailableEvent(WorkerEvent event);
  
  /**
   * Public for implementation reasons only, this method should
   * only ever be called by subclasses of CycConnection.java.
   * @param event The termination event that should be transmitted to
 listeners of this Worker
   */  
  void fireSubLWorkerTerminatedEvent(WorkerEvent event);
  
  public BlockingQueue<NotificationTaskI> getNotificationQueue();
  
  public interface NotificationTaskI extends Runnable {}
}
