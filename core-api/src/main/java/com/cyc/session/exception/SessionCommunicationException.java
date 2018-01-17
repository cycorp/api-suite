package com.cyc.session.exception;

import java.util.Objects;

/*
 * #%L
 * File: SessionCommunicationException.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2018 Cycorp, Inc
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
  
  //====|    Factory methods    |=================================================================//
  
  /**
   * Converts a Throwable to a SessionCommunicationException. If the Throwable is a
   * SessionCommunicationException, it will be passed through unmodified; otherwise, it will be wrapped
   * in a new SessionCommunicationException.
   *
   * @param cause the Throwable to convert
   *
   * @return a SessionCommunicationException
   */
  public static SessionCommunicationException fromThrowable(Throwable cause) {
    return (cause instanceof SessionCommunicationException)
                   ? (SessionCommunicationException) cause
                   : new SessionCommunicationException(cause);
  }

  /**
   * Converts a Throwable to a SessionCommunicationException with the specified detail message. If the
   * Throwable is a SessionCommunicationException and if the Throwable's message is identical to the
   * one supplied, the Throwable will be passed through unmodified; otherwise, it will be wrapped in
   * a new SessionCommunicationException with the detail message.
   *
   * @param cause       the Throwable to convert
   * @param message the specified detail message
   *
   * @return a SessionCommunicationException
   */
  public static SessionCommunicationException fromThrowable(String message, Throwable cause) {
    return (cause instanceof SessionCommunicationException && Objects.equals(message, cause.getMessage()))
                   ? (SessionCommunicationException) cause
                   : new SessionCommunicationException(message, cause);
  }

  //====|    Construction    |====================================================================//
  
  
  /**
   * Construct a SessionCommunicationException object with no specified message.
   */
  public SessionCommunicationException() {
    super();
  }
  
  /**
   * Construct a SessionCommunicationException object with a specified message.
   * @param msg a message describing the exception.
   */
  public SessionCommunicationException(String msg) {
    super(msg);
  }
  
  /**
   * Construct a SessionCommunicationException object with a specified message
   * and throwable.
   * @param msg the message string
   * @param cause the throwable that caused this exception
   */
  protected SessionCommunicationException(String msg, Throwable cause) {
    super(msg, cause);
  }
  
  /**
   * Construct a SessionCommunicationException object with a specified throwable.
   * @param cause the throwable that caused this exception
   */
  protected SessionCommunicationException(Throwable cause) {
    super(cause);
  }
}
