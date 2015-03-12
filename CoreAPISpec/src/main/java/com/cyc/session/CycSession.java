package com.cyc.session;

/*
 * #%L
 * File: CycSession.java
 * Project: Core API Specification
 * %%
 * Copyright (C) 2013 - 2015 Cycorp, Inc
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

/**
 * An active (or potentially active) session with a Cyc server. 
 * Implementation to be provided by the SessionFactory.
 * @author nwinant
 */
public interface CycSession {
  
  /**
   * The connection status of a {@link CycSession}.
   */
  public enum SessionStatus {
    UNINITIALIZED,
    CONNECTED,
    CLOSED
  }
  
  /**
   * Returns the current status of the session.
   * @return the current SessionStatus
   */
  SessionStatus getStatus();
  
  /**
   * Returns a set of modifiable options, including the name of the cyclist
   * making assertions, and the project's KE purpose.
   * 
   * The session's configuration may provide defaults for these options, or 
   * possibly prevent certain options from being changed.
   * 
   * @return the current SessionOptions
   */
  SessionOptions getOptions();
  
  /**
   * Returns the CycSessionConfiguration used to configure this session. These
   * settings cannot be modified.
   * 
   * @return the original CycSessionConfiguration
   */
  CycSessionConfiguration getConfiguration();
  
  /**
   * Provides basic information about the state and location of the server to
   * which the CycSession is connected (or was connected.) If the CycSession has not
   * been initialized (i.e., <code>getStatus().equals(SessionStatus.UNINITIALIZED)</code>)
   * this method should return <code>null</code>.
   * 
   * @return CycServerInfo for the session's server, or null if CycSession is uninitialized.
   */
  CycServerInfo getServerInfo();
}
