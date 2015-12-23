package com.cyc.baseclient.connection;

/*
 * #%L
 * File: NotificationTask.java
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

import com.cyc.baseclient.exception.CycApiInvalidObjectException;
import com.cyc.baseclient.exception.CycApiServerSideException;
import com.cyc.base.exception.BaseClientRuntimeException;
import com.cyc.baseclient.CycObjectFactory;
import com.cyc.base.conn.WorkerStatus;
import com.cyc.base.conn.Worker;
import com.cyc.base.conn.Worker.NotificationTaskI;
import java.util.concurrent.TimeUnit;
import com.cyc.baseclient.cycobject.CycSymbolImpl;

/**
 *
 * @author vijay
 */
class NotificationTask implements NotificationTaskI {
  private final Object taskStatus;
  private final boolean objectIsInvalid;
  private final Worker worker;
  private final Object response;
  private final boolean finished;
  private final Integer id;
  private volatile boolean workOnThisTask = false;
  private final CycConnectionImpl cycComm;

  public NotificationTask(final Object taskStatus, final boolean objectIsInvalid, final Worker worker, final Object response, final boolean finished, final Integer id, final CycConnectionImpl cycComm) {
    this.cycComm = cycComm;
    this.taskStatus = taskStatus;
    this.objectIsInvalid = objectIsInvalid;
    this.worker = worker;
    this.response = response;
    this.finished = finished;
    this.id = id;
    worker.getNotificationQueue().add(this);
  }

  public void run() {
    while (worker.getNotificationQueue().peek() != this) {
      try {
        Thread.sleep(1);
      } catch (InterruptedException ie) {
        Thread.currentThread().interrupt();
        return;
      }
    }
    try {
      if (taskStatus.equals(CycObjectFactory.nil)) {
        if (!objectIsInvalid) {
          // no error occurred, no exception
          worker.fireSubLWorkerDataAvailableEvent(new SublWorkerEvent(worker, response, -1.0f));
          if (finished) {
            worker.fireSubLWorkerTerminatedEvent(new SublWorkerEvent(worker, WorkerStatus.FINISHED_STATUS, null));
          }
        } else {
          // no API error sent from the server but the response contains an invalid object
          worker.fireSubLWorkerTerminatedEvent(new SublWorkerEvent(worker, WorkerStatus.EXCEPTION_STATUS, new CycApiInvalidObjectException("API response contains an invalid object: " + response.toString())));
        }
      } else {
        // Error, status contains the error message
        //@ToDo need to diferrentiate between exceptions and cancel messages!!!!!!!!!
        if (taskStatus instanceof String) {
          worker.fireSubLWorkerTerminatedEvent(new SublWorkerEvent(worker, WorkerStatus.EXCEPTION_STATUS, new CycApiServerSideException(taskStatus.toString())));
        } else if (taskStatus instanceof CycSymbolImpl) {
          worker.fireSubLWorkerTerminatedEvent(new SublWorkerEvent(worker, WorkerStatus.CANCELED_STATUS, null));
        }
      }
      if (worker.isDone()) {
        cycComm.getWaitingReplyThreads().remove(id);
      }
    } finally {
      try {
        NotificationTaskI notification = worker.getNotificationQueue().poll(1, TimeUnit.MICROSECONDS);
        if (notification != this) {
          throw new BaseClientRuntimeException("bad notification");
        }
      } catch (InterruptedException ie) {
        Thread.currentThread().interrupt();
        return;
      }
    }
  }
  
}
