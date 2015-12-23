package com.cyc.baseclient.ui;

/*
 * #%L
 * File: CycWorker.java
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

import com.cyc.baseclient.ui.SwingWorker;
import java.util.*;
import javax.swing.event.EventListenerList;
import com.cyc.baseclient.util.CycWorkerEvent;
import com.cyc.baseclient.util.CycWorkerListener;

/**
 *
 * This is a convenient event callback wrapper around the
 * the SwingWorker class. It's inteded to allow easily
 * running tasks in the background. Users of this class
 * will be notified when the worker is started, interrupted
 * or finished with its work. If finished, the rusults of the
 * work will be made available as well.
 * <P>
 * Here is some example code:<P>
 * <CODE><PLAINTEXT>
 *   public static CycWorker evalSubLInBackground(CycAccess conn,
 *                                                String subl,
 *                                                CycWorkerListener cwl) {
 *       CycWorker worker = new CycWorker() {
 *           //The return val of construct() method is considered to be
 *           //the output for this worker and retrieved with
 *           //worker.getWork().
 *           public Object construct() { 
 *               return evalSubL(conn, subl); //Do work here.
 *           }
 *       };
 *       if(cwl != null) { worker.addListener(cwl); }
 *       worker.start();
 *       return worker;
 *   }
 *
 *   public class GKETermDialog implements CycWorkerListener {
 *
 *      private CycWorker specWorker = null;
 *
 *	public void notifyWorkerStarted(CycWorkerEvent evt) {}
 *    
 *	public void notifyWorkerInterrupted(CycWorkerEvent evt) {}
 *    
 *	public void notifyWorkerFinished(CycWorkerEvent evt) {
 *	    CycWorker worker = (CycWorker)evt.getSource();
 *	    System.out.println("WORKER FINISHED: " + worker);
 *	    System.out.println("WORKER OUTPUT: " + worker.getWork());
 *	}
 *   }
 * </PLAINTEXT></CODE>
 */
public abstract class CycWorker extends SwingWorker {

  /** This CycWorkers listeners (instances of CycWorkerListener only).**/
  private EventListenerList listeners = new EventListenerList();
    
  /** Creates a new instance of CycWorker */
  public CycWorker() { }
    
  /**
   * Adds a new worker listener to this worker. Listeners will
   * be notified when the worker is started, interrupted or 
   * finished.
   * @param cwl The CycWorkerListener that wishes to listen to
   * this worker
   **/
  public void addListener(CycWorkerListener cwl) {
    listeners.add(CycWorkerListener.class, cwl);
  }
  
  /**
   * Removes a worker listener from this worker.
   * @param cwl The CycWorkerListener that no longer wishes to
   * be notified of this workers events.
   */
  public void removeListener(CycWorkerListener cwl) {
    listeners.remove(CycWorkerListener.class, cwl);
  }
  
  /**
   * Return a list of all CycWorkerListeners listening to
   * this CycWorker.
   * @return A non-null array of CycWorkerListener objects.
   */
  public Object[] getListeners() {
    return listeners.getListeners(CycWorkerListener.class);
  }
    
  final private static int CYC_WORKER_START=0;
  final private static int CYC_WORKER_INTERRUPT=1;
  final private static int CYC_WORKER_FINISHED=2;
    
  private void notifyStatChange(int eventType) {
    try {
      Object[] curListeners = listeners.getListenerList();
      CycWorkerEvent cwe = null;
      for (int i = curListeners.length-2; i>=0; i-=2) {
	if (curListeners[i]==CycWorkerListener.class) {
	  if (cwe == null) { cwe = new CycWorkerEvent(this); }
	  switch(eventType) {
	  case CYC_WORKER_START:
	    ((CycWorkerListener)curListeners[i+1]).
	      notifyWorkerStarted(cwe);
	    break;
	  case CYC_WORKER_INTERRUPT:
	    ((CycWorkerListener)curListeners[i+1]).
	      notifyWorkerInterrupted(cwe);
	    break;
	  case CYC_WORKER_FINISHED:
	    ((CycWorkerListener)curListeners[i+1]).
	      notifyWorkerFinished(cwe);
	    break;
	  }
	}
      }
    } catch (Exception e) { e.printStackTrace(); }
  }
  
  /** 
   * This method starts the backround processing of a particular
   * CycWorker. Appropriate listeners are notified when this
   * is called.
   */
  public void start() {
    notifyStatChange(CYC_WORKER_START);
    super.start();
  }
      
  /** 
   * This method interrupts a currently running CycWorker.
   * Appropriate listeners are notified when this is called.
   */       
  public void interrupt() {
    notifyStatChange(CYC_WORKER_INTERRUPT);
    super.interrupt();
  }
  
  /** 
   * This method should be called internally only...public for
   * Java related reasons only. It is automatically called
   * then the construct() method finishes.
   * Appropriate listeners are notified when this is called.
   */
  public void finished() {
    notifyStatChange(CYC_WORKER_FINISHED);
    super.finished();
  }
  
  /**
   * Exposed the get() method from SwingWorker class.
   * Returns the result of this CycWorker. Returns
   * null if worker not started or worker interrupted before 
   * processing could be completed. If processing is ongoing,
   * will block till finished.
   * @return Returns the result of this CycWorker or null if
   * CycWorker interrupted or not started. 
   **/
  public Object getWork() throws Exception { 
    if (getException() != null) {
      throw getException();
    }
    return super.get(); 
  }

  /**
   * @return the <tt>Thread</tt> doing the work of this worker.
   **/
  public Thread getThread() {
    return threadVar.get();
  }

}
