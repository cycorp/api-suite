package com.cyc.baseclient.api;

/*
 * #%L
 * File: SubLCommandProfiler.java
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

//// External Imports
import com.cyc.base.CycAccess;
import com.cyc.base.CycAccessManager;
import com.cyc.session.CycServer;
import com.cyc.baseclient.DefaultSubLWorker;
import com.cyc.baseclient.CycClient;
import com.cyc.base.conn.WorkerListener;
import com.cyc.base.conn.WorkerStatus;
import com.cyc.base.conn.Worker;
import com.cyc.base.conn.WorkerEvent;
import com.cyc.base.cycobject.CycList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.logging.Logger;
import java.io.*;
import com.cyc.baseclient.cycobject.CycArrayList;

/**
 * <P>SubLCommandProfiler profiles SubL commands.
 *
 * <P>Copyright (c) 2003 - 2006 Cycorp, Inc.  All rights reserved.
 * <BR>This software is the proprietary information of Cycorp, Inc.
 * <P>Use is subject to license terms.
 * @author reed
  date September 2, 2005, 7:37 AM
 * @version $Id: SubLCommandProfiler.java 155703 2015-01-05 23:15:30Z nwinant $
 */
public class SubLCommandProfiler implements WorkerListener {
  
  //// Constructors
  
  /** Creates a new instance of SubLCommandProfiler. */
  public SubLCommandProfiler() {
    logger = Logger.getLogger(com.cyc.baseclient.api.SubLCommandProfiler.class.getName());
  }
  
  //// Public Area
  
  /** Creates the SubL command profile report. 
   *
   * @param reportPath the profiling report path
   */
  public void report(final String reportPath) throws IOException {
    //// Preconditions 
    if (reportPath == null)
      throw new NullPointerException("reportPath must not be null");
    if (reportPath.length() == 0)
      throw new IllegalArgumentException("reportPath must not be an empty string");  
    assert mostTimeConsumingSubLCommandInfos != null : "mostTimeConsumingSubLCommandInfos must not be null";

    fileWriter = new FileWriter(reportPath);
    fileWriter.write("Longest Duration Cyc API Requests");
    fileWriter.write("\n\n");
    for (int i = mostTimeConsumingSubLCommandInfos.size() - 1; i >= 0; i--) {
      final SubLCommandInfo subLCommandInfo = (SubLCommandInfo) mostTimeConsumingSubLCommandInfos.get(i);
      fileWriter.write("----------------------------------------------------------------------\n");
      final String subLCommandInfoString = subLCommandInfo.toString();
      fileWriter.write(subLCommandInfo.toString());  
    }
    fileWriter.close();
  }

  /** This event is fired when a Worker is starting the 
 processing of a SubL task.
   * @param event the event object with details about this event
   */    
  public void notifySubLWorkerStarted(WorkerEvent event) {
    //// Preconditions 
    assert event != null : "event must not be null";
    assert subLWorkerStartTimeDictionary != null : "subLWorkerStartTimeDictionary must not be null";
    
    subLWorkerStartTimeDictionary.put(event.getWorker(), new Long(System.currentTimeMillis()));
    logger.fine(event.getWorker().getSubLCommand().toPrettyCyclifiedString(""));
  }
  
  /** This event is fired when new data becomes available in 
   * the processing of a SubL task.
   * @param event the event object with details about this event
   */
  public void notifySubLWorkerDataAvailable(WorkerEvent event) {
  }
  
  /** This event is fired when a SubL task has been terminated
   * for any reason.
   * @param event the event object with details about this event
   */
  public void notifySubLWorkerTerminated(WorkerEvent event) {
    //// Preconditions 
    assert event != null : "event must not be null";
    assert subLWorkerStartTimeDictionary != null : "subLWorkerStartTimeDictionary must not be null";
    
    final Worker subLWorker = event.getWorker();
    final WorkerStatus status = event.getStatus();
    if (status.equals(WorkerStatus.FINISHED_STATUS)) {
      final Long startTime = (Long) subLWorkerStartTimeDictionary.get(subLWorker);
      if (startTime != null) {
        final long durationMillis = System.currentTimeMillis() - startTime.longValue();
        final SubLCommandInfo subLCommandInfo = new SubLCommandInfo(subLWorker.getSubLCommand(), durationMillis);
        insertSubLCommandInfo(subLCommandInfo);
        logger.fine(subLCommandInfo.durationMillisToString() + "\n\n");
      }
    }
    subLWorkerStartTimeDictionary.remove(subLWorker);
  }
  
  //// Protected Area
  
  //// Private Area
  
  /** Inserts the given SubLCommandInfo into the ranked list.
   *
   * @param SubLCommandInfo the info object containing the SubL command and its duration
   */
  private void insertSubLCommandInfo(final SubLCommandInfo subLCommandInfo) {
    //// Preconditions 
    assert subLCommandInfo != null : "subLCommandInfo must not be null";
    assert mostTimeConsumingSubLCommandInfos != null : "mostTimeConsumingSubLCommandInfos must not be null";
    assert MOST_TIME_CONSUMING_API_REQUEST_INFOS_LIST_MAX_SIZE > 0 : "MOST_TIME_CONSUMING_API_REQUEST_INFOS_LIST_MAX_SIZE must be positive";
    
    synchronized (mostTimeConsumingSubLCommandInfos_lock) {
      if (mostTimeConsumingSubLCommandInfos.size() == MOST_TIME_CONSUMING_API_REQUEST_INFOS_LIST_MAX_SIZE) {
        final SubLCommandInfo firstSubLCommandInfo = (SubLCommandInfo) mostTimeConsumingSubLCommandInfos.getFirst();
        if (subLCommandInfo.compareTo(firstSubLCommandInfo) == -1)
          // do not insert if the list is maxed out and this info is less than the first one
          return;
      }
      final int searchIndex = Collections.binarySearch(mostTimeConsumingSubLCommandInfos, subLCommandInfo);
      final int insertIndex = (searchIndex >= 0) ? searchIndex : - searchIndex - 1;
      mostTimeConsumingSubLCommandInfos.add(insertIndex, subLCommandInfo);
      if (mostTimeConsumingSubLCommandInfos.size() > MOST_TIME_CONSUMING_API_REQUEST_INFOS_LIST_MAX_SIZE)
        mostTimeConsumingSubLCommandInfos.removeFirst();
    }
  }
  
  /** Instances of this class contain SubL command info that is used to maintain a list of the
   * SubL commands with the longest durations. */
  private class SubLCommandInfo implements Comparable {
    
    /** the SubL command */
    private final CycList subLCommand;
    
    /** the processing duration of the API request */
    private final long durationMillis;
    
    SubLCommandInfo(final CycList subLCommand, final long durationMillis) {
      //// Preconditions
      assert subLCommand != null : "subLCommand must not be null";
      assert durationMillis >= 0 : "durationMillis must not be negative " + durationMillis;
      
      this.subLCommand = subLCommand;
      this.durationMillis = durationMillis;
    }
    
    /** Compares this object with the specified object for order. Returns a negative integer, zero, 
     * or a positive integer as this object is less than, equal to, or greater than the specified object. 
     * Note that for this class x.compareTo(y) == 0 does not mean x.equals(y).
     *
     * @param obj the object for comparision with this object
     */
    public int compareTo(Object obj) {
      final SubLCommandInfo that = (SubLCommandInfo) obj;
      if (this.durationMillis < that.durationMillis)
        return -1;
      else if (this.durationMillis == that.durationMillis)
        return 0;
      else
        return 1;
    }
    
    /** Returns a string representation of this object. 
     *
     * @return 
     */
    public String toString() {
      final StringBuffer stringBuffer = new StringBuffer(1000);
      stringBuffer.append(durationMillisToString());
      stringBuffer.append('\n');
      stringBuffer.append(subLCommandToString());
      stringBuffer.append("\n\n");  
      return stringBuffer.toString();
    }
    
    /** Returns a string representation of the command.
     *
     * @return a string representation of the command
     */
    public String subLCommandToString() {
      return subLCommand.toPrettyCyclifiedString("");
    }
    
    /** Returns a string representation of the duration.
     *
     * @return a string representation of the duration
     */
    public String durationMillisToString() {
      final StringBuffer stringBuffer = new StringBuffer(50);
      stringBuffer.append("milliseconds duration: ");
      stringBuffer.append(durationMillis);
      return stringBuffer.toString();
    }
  }
  
  //// Internal Rep
 
  
  /** the logger */
  private final Logger logger;
  
  /** the thread-safe dictionary of Worker --> start time millis */
  private final Hashtable subLWorkerStartTimeDictionary = new Hashtable();

  /** the maximum length of API request info objects list */
  private int MOST_TIME_CONSUMING_API_REQUEST_INFOS_LIST_MAX_SIZE = 1000;
  
  /** the lock for the linked list of most time consuming API request info objects */
  final Object mostTimeConsumingSubLCommandInfos_lock = new Object();
  
  /** the linked list of most time consuming API request info objects which is maintained in ascending order */
  private final LinkedList mostTimeConsumingSubLCommandInfos = new LinkedList();
  
  /** the report file writer */
  public FileWriter fileWriter = null;
  
  //// Main
  
  /** For testing purposes only. */
  static SubLWorkerSynch testWorker; 
  
  //// Main
  
  /** Test main method and example usage.
   * @param args the command line arguments (not used)
   */
  public static void main(final String[] args) {
    DefaultSubLWorker.startProfiling();
    try {
      final CycAccess access = CycAccessManager.getAccess();
      SubLWorkerSynch worker = new DefaultSubLWorkerSynch("(+ 1 1)", access);
      Object work = worker.getWork();
      System.out.println("Got worker: " + worker + "\nGot result: " + work + ".");
      worker = new DefaultSubLWorkerSynch("(genls #$TransportationDevice)", access);
      work = worker.getWork();
      System.out.println("Got worker: " + worker + "\nGot result: " + work + ".");
      worker = new DefaultSubLWorkerSynch("(generate-phrase '(#$isa #$Brazil #$IndependentCountry))", access);
      work = worker.getWork();
      System.out.println("Got worker: " + worker + "\nGot result: " + work + ".");
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      final CycAccess access = CycAccessManager.getAccess();
      Thread workerThread = new Thread() {
        public void run() {
          try {
            System.out.println("Starting work.");
            testWorker = new DefaultSubLWorkerSynch("(do-assertions (a))", access);
            final Object obj = testWorker.getWork();
            System.out.println("Finished work with " + testWorker.getStatus().getName()
              + ", received: " + obj);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      };
      workerThread.start();
      Thread.currentThread().sleep(10000);
      System.out.println("About to cancel work.");
      testWorker.cancel();
      System.out.println("Canceled work.");
      
      System.out.println("\nGiving chance to get ready ....\n");
      Thread.currentThread().sleep(1000);

      System.out.println( "\nOk, second round ....\n\n");      
      workerThread = new Thread() {
        public void run() {
          try {
            System.out.println("Starting work.");
            testWorker = new DefaultSubLWorkerSynch("(do-assertions (a))", access);
            Object obj = testWorker.getWork();
            System.out.println("Finished work with " + testWorker.getStatus().getName()
              + ", received: " + obj);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      };
      workerThread.start();
      Thread.currentThread().sleep(10000);
      System.out.println("About to abort work.");
      testWorker.abort();
      System.out.println("Aborted work.");

      System.out.println("\nGiving chance to get ready ....\n");
      Thread.currentThread().sleep(1000);

      System.out.println( "\nOk, third round ....\n\n");
      workerThread = new Thread() {
        public void run() {
          long timeBefore = 0, timeAfter = 0;
          try {
            System.out.println("Starting work.");
            timeBefore = System.currentTimeMillis();
            testWorker = new DefaultSubLWorkerSynch("(do-assertions (a))", access, 500);
            Object obj = testWorker.getWork();
            timeAfter = System.currentTimeMillis();
            System.out.println("Finished work with " + testWorker.getStatus().getName()
              + " after " + (timeAfter - timeBefore) 
              + " millisecs (should be about 500), received: " + obj);
          } catch (Exception e) {
            timeAfter = System.currentTimeMillis();
            System.out.println( "The current time is: " + (timeAfter - timeBefore) 
              + " millisecs (should be about 500)");
            e.printStackTrace(); 
          }
        }
      };
      workerThread.start();
      Thread.currentThread().sleep(10000);
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      DefaultSubLWorker.endProfiling("/home/reed/subl-command-profile-report.txt");
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.exit(0);
  }  

}
