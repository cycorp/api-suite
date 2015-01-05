package com.cyc.baseclient.api;

/*
 * #%L
 * File: TaskProcessorBinaryResponseHandler.java
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

import com.cyc.base.BaseClientException;
import com.cyc.baseclient.CycObjectFactory;
import com.cyc.base.CycApiException;
import com.cyc.base.conn.Worker;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import com.cyc.baseclient.cycobject.CycArrayList;
import com.cyc.baseclient.cycobject.CycSymbolImpl;
import com.cyc.baseclient.util.Log;
import com.cyc.baseclient.datatype.StringUtils;

/**
 * Class TaskProcessorBinaryResponseHandler handles responses from
 * task-processor requests in binary communication mode.
 */
class TaskProcessorBinaryResponseHandler extends Thread {

  /**
   * Maximum number of local cyc clients supported by this listener.
   */
  public static final int MAX_LOCAL_CLIENT_CLIENTS = 50;
  /**
   * The socket which listens for new connections.
   */
  protected ServerSocket listenerSocket = null;
  /**
   * The socket which receives asychronous inbound messages from the Cyc server.
   */
  protected Socket inboundSocket = null;
  /**
   * The binary interface input stream which receives asychronous messages from
   * the Cyc server
   */
  public CfaslInputStream inboundStream;
  /**
   * The binary interface output stream, which is the output side of the
   * bidirectional socket, is used only to start up and close down the socket.
   */
  protected CfaslOutputStream inboundOutputStream;
  /**
   * Reference to the parent thread which will sleep until this handler is
   * initialized.
   */
  protected Thread parentThread;
  /**
   * The (ignore) message from the Cyc server to test if the connection is
   * alive.
   */
  protected CycArrayList ignoreMessage;
  /**
   * the parent CycConnection
   */
  private volatile boolean isClosed = false;
  volatile boolean isClosing = false;
  /**
   * the synchronization object to ensure that the streams are ready
   */
  private Semaphore initializedSemaphore;
  private volatile boolean initialized;
  private volatile Exception initializationError = null;
  /**
   * the indices into the task processor response object, which is a list
   */
  static final int TASK_PROCESSOR_RESPONSE_ID = 2;
  static final int TASK_PROCESSOR_RESPONSE_RESPONSE = 5;
  static final int TASK_PROCESSOR_RESPONSE_STATUS = 6;
  static final int TASK_PROCESSOR_RESPONSE_FINISHED_FLAG = 7;
  private final CycConnection cycConnection;

  private String hostName;
  private int port;
  /**
   * Constructs a TaskProcessorBinaryResponseHandler object.
   *
   * @param parentThread the parent thread of this thread
   * @param cycConnection the parent CycConnection
   */
  /**
   * Constructs a TaskProcessorBinaryResponseHandler object.
   *
   * @param parentThread the parent thread of this thread
   * @param cycConnection the parent CycConnection
   */
  public TaskProcessorBinaryResponseHandler(Thread parentThread, final CycConnection cycConnection, String hostName, int port) {
    this.cycConnection = cycConnection;
    this.parentThread = parentThread;
    ignoreMessage = new CycArrayList();
    ignoreMessage.add(new CycSymbolImpl("IGNORE"));
    
    this.hostName = hostName;
    this.port = port;
    initializeSynchronization();
  }
  
  public TaskProcessorBinaryResponseHandler(Thread parentThread, final CycConnection cycConnection, InputStream is) {
    this.cycConnection = cycConnection;
    this.parentThread = parentThread;
    ignoreMessage = new CycArrayList();
    ignoreMessage.add(new CycSymbolImpl("IGNORE"));
    
    this.hostName = hostName;
    this.port = port;
    initializeSynchronization();
    inboundStream = new CfaslInputStream(is);
    inboundStream.trace = cycConnection.getTrace();
  }

  /**
   * Opens the response socket with Cyc, blocks until the next task-processor
   * response is available, then awakens the client thread that made the
   * request.
   */
  public void run() {
    Thread.currentThread().setName("TaskProcessorBinaryResponseHandler");
    Exception closingException = null;
    try {
      if ((!isClosed) && (!isClosing)) {
        try {
          if (cycConnection.comm == null) {
            CycArrayList request = new CycArrayList();
            request.add(new CycSymbolImpl("INITIALIZE-JAVA-API-PASSIVE-SOCKET"));
            request.add(cycConnection.uuid.toString());
            // Open a second api socket connection and use it for asychronous api responses.
            inboundSocket = new Socket(cycConnection.hostName, cycConnection.cfaslPort);
            int val = inboundSocket.getReceiveBufferSize();
            inboundSocket.setReceiveBufferSize(val * 2);
            inboundSocket.setTcpNoDelay(true);
            inboundSocket.setKeepAlive(true);

            inboundStream = new CfaslInputStream(inboundSocket.getInputStream());
            inboundStream.trace = cycConnection.trace;
            inboundOutputStream = new CfaslOutputStream(inboundSocket.getOutputStream());
            // send a one-time request the to Cyc server to configure this connection for subsequent api reponses
            inboundOutputStream.writeObject(request);
            inboundOutputStream.flush();
            // read and ignore the status
            inboundStream.resetIsInvalidObject();
            inboundStream.readObject();
            // read and ignore the response
            inboundStream.resetIsInvalidObject();
            inboundStream.readObject();
            inboundStream.trace = cycConnection.getTrace();
          }
        } catch (Exception e) {
          if ((!isClosed) && (!isClosing)) {
            closingException = e;
            Log.current.printStackTrace(e);
            Log.current.errorPrintln("Communication with Cyc cannot be started: host-" + cycConnection.hostName + " port-" + cycConnection.cfaslPort);
            notifySetupCompleted(e);
          }
          return;
        }
      }
      // signal that we are ready to go
      notifySetupCompleted(null);
      // Handle messsages received on the asychronous inbound Cyc connection.
      while ((!isClosed) && (!isClosing)) {
        Object status = null;
        CycArrayList taskProcessorResponse = null;
        boolean isInvalidObject = false;
        if (isClosed || isClosing) {
          break;
        }
        try {
          // read status
          inboundStream.resetIsInvalidObject();
          status = inboundStream.readObject();
          // read task processor response
          inboundStream.resetIsInvalidObject();
          Object currentResponse = inboundStream.readObject();
          if (!(currentResponse instanceof CycArrayList)) {
            throw new BaseClientException("Invalid task processor response: " + currentResponse);
          }
          taskProcessorResponse = (CycArrayList) currentResponse;
          if (cycConnection.logger.isLoggable(Level.FINE)) {
            cycConnection.logger.fine("API response: " + taskProcessorResponse.stringApiValue());
          }
          isInvalidObject = inboundStream.isInvalidObject();
        } catch (Exception e) {
          if (cycConnection.taskProcessingEnded) {
            if (cycConnection.trace > CycConnection.API_TRACE_NONE) {
              Log.current.println("Ending binary mode task processor handler.");
            }
          }
          if ((!isClosed) && (!isClosing)) {
            cycConnection.logger.fine("Exception: " + e.getMessage());
            if (e instanceof CfaslInputStreamClosedException) {
              if (cycConnection.trace > CycConnection.API_TRACE_NONE) {
                Log.current.errorPrintln(e.getMessage());
                Log.current.printStackTrace(e);
              }
            } else if (e instanceof RuntimeException) {
              Log.current.errorPrintln(e.getMessage());
              Log.current.printStackTrace(e);
              continue;
            }
            closingException = e;
            Log.current.println("Cyc Server ended binary mode task processor handler.\n" + StringUtils.getStringForException(e));
          }
          return;
        }
        final boolean objectIsInvalid = isInvalidObject;
        cycConnection.logger.finest("API status: " + status);
        if (cycConnection.trace >= CycConnection.API_TRACE_DETAILED) {
          Log.current.println("cyc --> (" + status + ") " + taskProcessorResponse.toString());
        }
        if (taskProcessorResponse.equals(ignoreMessage)) {
          continue;
        }
        try {
          if (cycConnection.trace >= CycConnection.API_TRACE_MESSAGES) {
            Log.current.println(CycConnection.df.format(new Date()) + "\n    Got response: (" + taskProcessorResponse + ")");
          }
          if (!(taskProcessorResponse.get(TASK_PROCESSOR_RESPONSE_ID) instanceof Integer)) {
            Log.current.println(CycConnection.df.format(new Date()) + "\n    Got invalid response id: (" + taskProcessorResponse + ")");
          }
          final Integer id = (Integer) taskProcessorResponse.get(TASK_PROCESSOR_RESPONSE_ID);
          final Object taskStatus = taskProcessorResponse.get(TASK_PROCESSOR_RESPONSE_STATUS);
          // handle Cyc images that either support or do not support (legacy) the finished flag
          final Object finishedFlag = (taskProcessorResponse.size() > TASK_PROCESSOR_RESPONSE_FINISHED_FLAG) ? taskProcessorResponse.get(TASK_PROCESSOR_RESPONSE_FINISHED_FLAG) : CycObjectFactory.t;
          final boolean finished = !(finishedFlag == CycObjectFactory.nil);
          final CycConnection.WaitingWorkerInfo waitingWorkerInfo = (CycConnection.WaitingWorkerInfo) cycConnection.getWaitingReplyThreads().get(id);
          if (waitingWorkerInfo == null) {
            if (cycConnection.trace >= CycConnection.API_TRACE_MESSAGES) {
              Log.current.println(CycConnection.df.format(new Date()) + "\n    Got response with no waiting working: (" + taskProcessorResponse + ")");
            }
            continue;
          }
          final Worker worker = waitingWorkerInfo.getWorker();
          // used for example in the XML soap service where there is an upstream SOAPBinaryCycConnection object that
          // needs the whole task processor response.
          final Object response = StringUtils.cyclStringsToJavaStrings(
                  waitingWorkerInfo.isReturnWholeTaskProcessorResponse ? taskProcessorResponse : taskProcessorResponse.get(TASK_PROCESSOR_RESPONSE_RESPONSE));
          final Runnable notificationTask = new NotificationTask(taskStatus, objectIsInvalid, worker, response, finished, id, cycConnection);
          try {
            cycConnection.apiPool.execute(notificationTask);
          } catch (RejectedExecutionException e) {
            e.printStackTrace();
            System.err.println("Rejected notification from " + worker);
          }
        } catch (Exception xcpt) {
          if ((!isClosed) && (!isClosing)) {
            Log.current.errorPrintln(xcpt.getMessage());
            Log.current.printStackTrace(xcpt);
          }
          continue;
        }
      } // while-forever
      // while-forever
      // while-forever
    } catch (Exception e) {
      closingException = e;
    } finally {
      if (closingException != null) {
        cycConnection.logger.log(Level.SEVERE, "TaskProcessor terminated because of exception.", closingException);
      }
      cycConnection.taskProcessingThreadDead = true;
      cycConnection.logger.finer("TaskProcessor is closing now.");
      notifySetupCompleted(closingException);
      cycConnection.forciblyUnblockAllWaitingWorkers(closingException);
      close();
    }
  }

  /**
   * Closes the passive inbound api response socket.
   */
  public synchronized void close() {
    if (isClosed) {
      return;
    }
    isClosed = true;
    if (cycConnection.apiPool != null) {
      try {
        cycConnection.apiPool.shutdownNow();
      } catch (Exception e) {
      }
      ;
      try {
        cycConnection.apiPool.setMaximumPoolSize(0);
      } catch (Exception e) {
      }
      ;
      try {
        cycConnection.apiPool.setKeepAliveTime(0, TimeUnit.MILLISECONDS);
      } catch (Exception e) {
      }
      ;
    }
    if (inboundOutputStream != null) {
      try {
        inboundOutputStream.close();
      } catch (Exception e) {
        //ignore
        //ignore
        //ignore
      } finally {
        inboundOutputStream = null;
      }
    }
    if (inboundStream != null) {
      try {
        inboundStream.close();
      } catch (Exception e) {
        //ignore
        //ignore
        //ignore
      } finally {
        inboundStream = null;
      }
    }
    if (cycConnection.trace > CycConnection.API_TRACE_NONE) {
      Log.current.println("closed inbound socket associated with " + cycConnection.uuid);
    }
  }

  public void waitOnSetupToComplete() {
    // avoid blocking on this ptr, which would stop the
    // notifySetupCompleted method from working correctly
    try {
      initializedSemaphore.acquire();
    } catch (InterruptedException xcpt) {
      initializationError = new IllegalStateException("Unable to initialize Cyc communication.");
      System.err.println("Interrupted during wait(): " + xcpt);
    }
    if (initializationError != null) {
      throw new CycApiException("Cannot start communications to Cyc.", initializationError);
    }
  }

  private void initializeSynchronization() {
    synchronized (this) {
      initialized = false;
      initializedSemaphore = new Semaphore(0);
    }
  }

  private void notifySetupCompleted(Exception e) {
    synchronized (this) {
      initializationError = e;
      initialized = true;
    }
    initializedSemaphore.release();
  }
}
