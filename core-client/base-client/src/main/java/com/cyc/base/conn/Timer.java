package com.cyc.base.conn;

/*
 * #%L
 * File: Timer.java
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

import com.cyc.base.exception.CycTimeOutException;
import java.util.Date;

/**
 *
 * @author nwinant
 */
public interface Timer {
  Date APOCALYPSE = new Date(Long.MAX_VALUE);

  /**
   * Throws a @see CycTimeOutException if this timer has been running longer than
 the timeOut.
   *
   * @throws CycTimeOutException if this timer has run the specifier number of
 seconds.
   */
  void checkForTimeOut() throws CycTimeOutException;

  long getAllotedMSecs();

  /**
   * Returns the number of milliseconds that this timer has been running.
   *
   * @return the number of milliseconds that this timer has been running.
   */
  long getElapsedMilliSeconds();

  /**
   * Returns the number of seconds that this timer has been running.
   *
   * @return the number of seconds that this timer has been running.
   */
  int getElapsedSeconds();

  /**
   * Returns the number of milliseconds remaining till the timeout of this timer.
   *
   * @return the number of milliseconds remaining till the timeout of this timer.
   */
  long getRemainingMilliSeconds();

  /**
   * Returns the number of seconds remaining till the timeout of this timer.
   *
   * @return the number of seconds remaining till the timeout of this timer.
   */
  int getRemainingSeconds();

  /**
   * Returns <code>true</code> if this timer is timed out, <code>false</code> otherwise.
   *
   * @return <code>true</code> if this timer is timed out, <code>false</code> otherwise.
   */
  boolean isTimedOut();

  /**
   * Restarts this timer with the same timeout it had previously.
   */
  void start();
  
}
