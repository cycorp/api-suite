package com.cyc.session.exception;

/*
 * #%L
 * File: SessionCommunicationException.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2017 Cycorp, Inc
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
 * SessionCommunicationException indicates that an application library encountered a problem when
 * attempting to communicate with a Cyc server.
 *
 * @author nwinant
 */
public class SessionCommunicationException extends SessionRuntimeException {
  /**
   * Construct a CycConfigurationException object with no specified message.
   */
  public SessionCommunicationException() {
    super();
  }
  
  /**
   * Construct a CycConfigurationException object with a specified message.
   * @param msg a message describing the exception.
   */
  public SessionCommunicationException(String msg) {
    super(msg);
  }
  
  /**
   * Construct a CycConfigurationException object with a specified message
   * and throwable.
   * @param msg the message string
   * @param cause the throwable that caused this exception
   */
  public SessionCommunicationException(String msg, Throwable cause) {
    super(msg, cause);
  }
  
  /**
   * Construct a CycConfigurationException object with a specified throwable.
   * @param cause the throwable that caused this exception
   */
  public SessionCommunicationException(Throwable cause) {
    super(cause);
  }
}
