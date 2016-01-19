package com.cyc.baseclient.connection;

/*
 * #%L
 * File: ConnectionTimer.java
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

import com.cyc.baseclient.util.Log;
import com.cyc.base.exception.CycTimeOutException;

/** Provides a timer thread for cancelling the connection if it takes too long to establish. */
class ConnectionTimer extends Thread {
  private final CycConnectionImpl cycConnection;

  /** Constucts a new ConnectionTimer instance. */
  ConnectionTimer(final CycConnectionImpl cycConnection) {
    this.cycConnection = cycConnection;
  }

  /** Waits for either the CycConnection constructor thread to set the done indicator, or kills the
   * connection after the timeout is exceeded. */
  public void run() {
    try {
      while (!isCycConnectionEstablished) {
        Thread.sleep(WAIT_TIME_INCREMENT);
        timerMillis = timerMillis + WAIT_TIME_INCREMENT;
        if (timerMillis > TIMEOUT_MILLIS) {
          throw new CycTimeOutException("Timeout exceeded when connecting to Cyc.");
        }
      }
    } catch (InterruptedException e) {
      Log.current.println("Interruption while waiting on Cyc connection establishment, closing sockets");
      // close the socket connections to Cyc and kill any awaiting api request threads
      if (cycConnection.trace == CycConnectionImpl.API_TRACE_NONE) {
        cycConnection.trace = CycConnectionImpl.API_TRACE_MESSAGES;
      }
      cycConnection.close();
      Thread.currentThread().interrupt();
      throw new IllegalStateException("Interrupted while establishing Cyc connection.", e);
    } catch (CycTimeOutException e) {
      Log.current.println("Timed out while waiting Cyc connection establishment, closing sockets");
      // close the socket connections to Cyc and kill any awaiting api request threads
      if (cycConnection.trace == CycConnectionImpl.API_TRACE_NONE) {
        cycConnection.trace = CycConnectionImpl.API_TRACE_MESSAGES;
      }
      cycConnection.close();
      throw e;
    }
  }
  /** the timeout duration in milliseconds (one minute) */
  final long TIMEOUT_MILLIS = 60000;
  /** the wait time increment */
  final long WAIT_TIME_INCREMENT = 1000;
  /** the wait time so far in milliseconds */
  long timerMillis = 0;
  /** set by the CycConnection constructor process to indicate that the connection to Cyc is established */
  volatile boolean isCycConnectionEstablished = false;
  
}
