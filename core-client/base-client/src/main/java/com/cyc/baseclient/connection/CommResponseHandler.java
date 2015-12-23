package com.cyc.baseclient.connection;

/*
 * #%L
 * File: CommResponseHandler.java
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

import com.cyc.baseclient.exception.CfaslInputStreamClosedException;
import com.cyc.base.exception.BaseClientException;
import com.cyc.baseclient.CycObjectFactory;
import com.cyc.base.exception.CycApiException;
import com.cyc.base.conn.Worker;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import com.cyc.baseclient.comm.Comm;
import com.cyc.baseclient.cycobject.CycArrayList;
import com.cyc.baseclient.cycobject.CycSymbolImpl;
import com.cyc.baseclient.util.Log;
import com.cyc.baseclient.datatype.StringUtils;

/**
 * Class TaskProcessorBinaryResponseHandler handles responses from
 * task-processor requests in binary communication mode.
 */
class CommResponseHandler extends Thread {

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
  protected CycConnectionImpl cycConnection;
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
  private final CycConnectionImpl cycComm;

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
  public CommResponseHandler(Thread parentThread, CycConnectionImpl cycConnection, final CycConnectionImpl cycComm) {
    this.cycComm = cycComm;
    this.parentThread = parentThread;
    this.cycConnection = cycConnection;
    ignoreMessage = new CycArrayList();
    ignoreMessage.add(new CycSymbolImpl("IGNORE"));
  }

  public void start() {
    initializeSynchronization();
    super.start();
    waitOnSetupToComplete();
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
          //inboundStream = new CfaslInputStream(cycComm.c.getInputStream());
          inboundStream = cycComm.cfaslInputStream;
          inboundStream.trace = cycComm.trace;
        } catch (Exception e) {
          if ((!isClosed) && (!isClosing)) {
            closingException = e;
            Log.current.printStackTrace(e);
            Log.current.errorPrintln("Communication with Cyc cannot be started: host-" + cycComm.hostName + " port-" + cycComm.cfaslPort);
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
          if (cycComm.logger.isLoggable(Level.FINE)) {
            cycComm.logger.fine("API response: " + taskProcessorResponse.stringApiValue());
          }
          isInvalidObject = inboundStream.isInvalidObject();
        } catch (Exception e) {
          if (cycComm.taskProcessingEnded) {
            if (cycComm.trace > CycConnectionImpl.API_TRACE_NONE) {
              Log.current.println("Ending binary mode task processor handler.");
            }
          }
          if ((!isClosed) && (!isClosing)) {
            cycComm.logger.fine("Exception: " + e.getMessage());
            if (e instanceof CfaslInputStreamClosedException) {
              if (cycComm.trace > CycConnectionImpl.API_TRACE_NONE) {
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
        cycComm.logger.finest("API status: " + status);
        if (cycComm.trace >= CycConnectionImpl.API_TRACE_DETAILED) {
          Log.current.println("cyc --> (" + status + ") " + taskProcessorResponse.toString());
        }
        if (taskProcessorResponse.equals(ignoreMessage)) {
          continue;
        }
        try {
          if (cycComm.trace >= CycConnectionImpl.API_TRACE_MESSAGES) {
            Log.current.println(CycConnectionImpl.df.format(new Date()) + "\n    Got response: (" + taskProcessorResponse + ")");
          }
          if (!(taskProcessorResponse.get(TASK_PROCESSOR_RESPONSE_ID) instanceof Integer)) {
            Log.current.println(CycConnectionImpl.df.format(new Date()) + "\n    Got invalid response id: (" + taskProcessorResponse + ")");
          }
          final Integer id = (Integer) taskProcessorResponse.get(TASK_PROCESSOR_RESPONSE_ID);
          final Object taskStatus = taskProcessorResponse.get(TASK_PROCESSOR_RESPONSE_STATUS);
          // handle Cyc images that either support or do not support (legacy) the finished flag
          final Object finishedFlag = (taskProcessorResponse.size() > TASK_PROCESSOR_RESPONSE_FINISHED_FLAG) ? taskProcessorResponse.get(TASK_PROCESSOR_RESPONSE_FINISHED_FLAG) : CycObjectFactory.t;
          final boolean finished = !(finishedFlag == CycObjectFactory.nil);
          final CycConnectionImpl.WaitingWorkerInfo waitingWorkerInfo = (CycConnectionImpl.WaitingWorkerInfo) cycComm.getWaitingReplyThreads().get(id);
          if (waitingWorkerInfo == null) {
            if (cycComm.trace >= CycConnectionImpl.API_TRACE_MESSAGES) {
              Log.current.println(CycConnectionImpl.df.format(new Date()) + "\n    Got response with no waiting working: (" + taskProcessorResponse + ")");
            }
            continue;
          }
          final Worker worker = waitingWorkerInfo.getWorker();
          // used for example in the XML soap service where there is an upstream SOAPBinaryCycConnection object that
          // needs the whose task processor response.
          final Object response = waitingWorkerInfo.isReturnWholeTaskProcessorResponse ? taskProcessorResponse : taskProcessorResponse.get(TASK_PROCESSOR_RESPONSE_RESPONSE);
          final Runnable notificationTask = new NotificationTask(taskStatus, objectIsInvalid, worker, response, finished, id, cycComm);
          try {
            cycComm.apiPool.execute(notificationTask);
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
        cycComm.logger.log(Level.SEVERE, "TaskProcessor terminated because of exception.", closingException);
      }
      cycComm.taskProcessingThreadDead = true;
      cycComm.logger.finer("TaskProcessor is closing now.");
      notifySetupCompleted(closingException);
      cycComm.forciblyUnblockAllWaitingWorkers(closingException);
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
    if (cycComm.apiPool != null) {
      try {
        cycComm.apiPool.shutdownNow();
      } catch (Exception e) {
      }
      ;
      try {
        cycComm.apiPool.setMaximumPoolSize(0);
      } catch (Exception e) {
      }
      ;
      try {
        cycComm.apiPool.setKeepAliveTime(0, TimeUnit.MILLISECONDS);
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
    if (cycComm.trace > CycConnectionImpl.API_TRACE_NONE) {
      Log.current.println("closed inbound socket associated with " + cycComm.uuid);
    }
  }

  private void waitOnSetupToComplete() {
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
