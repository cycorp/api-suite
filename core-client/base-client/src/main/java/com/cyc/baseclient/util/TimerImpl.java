package  com.cyc.baseclient.util;

/*
 * #%L
 * File: TimerImpl.java
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

import com.cyc.base.exception.CycTimeOutException;
import com.cyc.base.conn.Timer;
import  java.util.Date;

/**
 * Implements A timer that can be set to a specified number of seconds or milliseconds
 * before it times out, and can be checked for the time elapsed since the start
 * or time remaining until the timeout.<p>
 *
 * @version $Id: TimerImpl.java 163356 2016-01-04 20:55:47Z nwinant $
 * @author Eric E. Allen<br>
 * @author Bjorn Aldag<br>
 */
public class TimerImpl implements Timer {
    public static final Date APOCALYPSE = new Date(Long.MAX_VALUE);
    private Date timeOut = APOCALYPSE;
    private Date start;
    private long timeAlloted;

    /**
     * Creates and starts a new timer that will run the specified number of
     * seconds before timing out.
     *
     * @param timeAlloted the number of seconds the timer will run before timing
     * out.
     */
    public TimerImpl (int timeAlloted) {
        this.start = new Date();
        this.timeAlloted = timeAlloted * 1000L;
        this.timeOut = new Date(start.getTime() + (((long)timeAlloted)*1000L));
    }

    /**
     * Creates and starts a new timer that will run the specified number of
     * milliseconds before timing out.
     *
     * @param timeAlloted the number of milliseconds the timer will run before
     * timing out.
     */
    public TimerImpl (long timeAlloted) {
        this.start = new Date();
        this.timeAlloted = timeAlloted;
        this.timeOut = new Date(start.getTime() + timeAlloted);
    }

    /**
     * Creates and starts a new timer that will run forever.
     */
    public TimerImpl () {
        this.start = new Date();
        this.timeAlloted = 0;
    }
    
    public long getAllotedMSecs() {
      return timeAlloted;
    }

    /**
     * Restarts this timer with the same timeout it had previously.
     */
    public void start () {
        Date now = new Date();
        this.timeOut = new Date(2*timeOut.getTime() - now.getTime());
        start = now;
    }

    /**
     * Returns the number of seconds that this timer has been running.
     *
     * @return the number of seconds that this timer has been running.
     */
    public int getElapsedSeconds () {
        return  (int)(getElapsedMilliSeconds()/1000L);
    }

    /**
     * Returns the number of milliseconds that this timer has been running.
     *
     * @return the number of milliseconds that this timer has been running.
     */
    public long getElapsedMilliSeconds () {
        return  new Date().getTime() - start.getTime();
    }

    /**
     * Returns the number of seconds remaining till the timeout of this timer.
     *
     * @return the number of seconds remaining till the timeout of this timer.
     */
    public int getRemainingSeconds () {
        return  (int)(getRemainingMilliSeconds()/1000L);
    }

    /**
     * Returns the number of milliseconds remaining till the timeout of this timer.
     *
     * @return the number of milliseconds remaining till the timeout of this timer.
     */
    public long getRemainingMilliSeconds () {
        return  timeOut.getTime() - new Date().getTime();
    }

    /**
     * Returns <code>true</code> if this timer is timed out, <code>false</code> otherwise.
     *
     * @return <code>true</code> if this timer is timed out, <code>false</code> otherwise.
     */
    public boolean isTimedOut () {
        return  (new Date().after(timeOut));
    }

    /**
     * Throws a @see CycTimeOutException if this timer has been running longer than
 the timeOut.
     *
   * @throws CycTimeOutException if this timer has run the specifier number of
 seconds.
     */
    public void checkForTimeOut () throws CycTimeOutException {
        if (isTimedOut()) {
            throw  new CycTimeOutException("");
        }
    }
}



