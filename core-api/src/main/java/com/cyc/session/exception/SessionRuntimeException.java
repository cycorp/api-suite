package com.cyc.session.exception;

import java.util.Objects;

/*
 * #%L
 * File: SessionRuntimeException.java
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
 * SessionRuntimeException is the base runtime exception which indicates that an application library
 * has encountered a problem in interacting with a Cyc server. Subclasses of SessionRuntimeException
 * indicate specific types of issues which are likely to be beyond the control of the calling
 * application, such as communications issues with Cyc, or internal Cyc errors.
 *
 * @see com.cyc.session.exception.SessionException
 * 
 * @author nwinant
 */
public class SessionRuntimeException extends RuntimeException {
  
  //====|    Factory methods    |=================================================================//
  
  /**
   * Converts a Throwable to a SessionRuntimeException. If the Throwable is a
   * SessionRuntimeException, it will be passed through unmodified; otherwise, it will be wrapped
   * in a new SessionRuntimeException.
   *
   * @param cause the Throwable to convert
   *
   * @return a SessionRuntimeException
   */
  public static SessionRuntimeException fromThrowable(Throwable cause) {
    return (cause instanceof SessionRuntimeException)
                   ? (SessionRuntimeException) cause
                   : new SessionRuntimeException(cause);
  }

  /**
   * Converts a Throwable to a SessionRuntimeException with the specified detail message. If the
   * Throwable is a SessionRuntimeException and if the Throwable's message is identical to the
   * one supplied, the Throwable will be passed through unmodified; otherwise, it will be wrapped in
   * a new SessionRuntimeException with the detail message.
   *
   * @param cause       the Throwable to convert
   * @param message the specified detail message
   *
   * @return a SessionRuntimeException
   */
  public static SessionRuntimeException fromThrowable(String message, Throwable cause) {
    return (cause instanceof SessionRuntimeException && Objects.equals(message, cause.getMessage()))
                   ? (SessionRuntimeException) cause
                   : new SessionRuntimeException(message, cause);
  }

  //====|    Construction    |====================================================================//
  
  /**
   * Constructs a new exception with no specified message.
   */
  public SessionRuntimeException() {
    super();
  }
  
  /**
   * Constructs a new exception with a specified message.
   * @param message a message describing the exception.
   */
  public SessionRuntimeException(String message) {
    super(message);
  }
  
  /**
   * Constructs a new exception with a specified message and throwable.
   * @param message the message string
   * @param cause the Throwable that caused this exception
   */
  protected SessionRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }
  
  /**
   * Constructs a new exception with a specified throwable.
   * @param cause the throwable that caused this exception
   */
  protected SessionRuntimeException(Throwable cause) {
    super(cause);
  }
}
