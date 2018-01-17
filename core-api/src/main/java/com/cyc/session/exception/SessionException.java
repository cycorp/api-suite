package com.cyc.session.exception;

import java.util.Objects;

/*
 * #%L
 * File: SessionException.java
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
 * SessionException is the base exception which indicates that an application library has
 * encountered a problem in interacting with a Cyc server. Subclasses of CycSessionException
 * indicate specific types of issues which the calling application might reasonably be expected to
 * prevent or respond to, such as misconfigured sessions.
 *
 * @see com.cyc.session.exception.SessionRuntimeException
 * 
 * @author nwinant
 */
public class SessionException extends Exception {
  
  //====|    Factory methods    |=================================================================//
  
  /**
   * Converts a Throwable to a SessionException. If the Throwable is a
   * SessionException, it will be passed through unmodified; otherwise, it will be wrapped
   * in a new SessionException.
   *
   * @param cause the Throwable to convert
   *
   * @return a SessionException
   */
  public static SessionException fromThrowable(Throwable cause) {
    return (cause instanceof SessionException)
                   ? (SessionException) cause
                   : new SessionException(cause);
  }

  /**
   * Converts a Throwable to a SessionException with the specified detail message. If the
   * Throwable is a SessionException and if the Throwable's message is identical to the
   * one supplied, the Throwable will be passed through unmodified; otherwise, it will be wrapped in
   * a new SessionException with the detail message.
   *
   * @param cause       the Throwable to convert
   * @param message the specified detail message
   *
   * @return a SessionException
   */
  public static SessionException fromThrowable(String message, Throwable cause) {
    return (cause instanceof SessionException && Objects.equals(message, cause.getMessage()))
                   ? (SessionException) cause
                   : new SessionException(message, cause);
  }

  //====|    Construction    |====================================================================//
  
  /**
   * Constructs a new exception with no specified message.
   */
  public SessionException() {
    super();
  }
  
  /**
   * Constructs a new exception with a specified message.
   * @param message a message describing the exception.
   */
  public SessionException(String message) {
    super(message);
  }
  
  /**
   * Constructs a new exception with a specified message and throwable.
   * @param message the message string
   * @param cause the Throwable that caused this exception
   */
  protected SessionException(String message, Throwable cause) {
    super(message, cause);
  }
  
  /**
   * Constructs a new exception with a specified throwable.
   * @param cause the throwable that caused this exception
   */
  protected SessionException(Throwable cause) {
    super(cause);
  }
}
