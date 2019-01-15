package com.cyc.session.exception;

import java.util.Objects;

/*
 * #%L
 * File: SessionInitializationException.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2019 Cycorp, Inc
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
 * SessionInitializationException indicates that a problem was encountered when initializing a
 * CycSession.
 *
 * @author nwinant
 */
public class SessionInitializationException extends SessionException {
  
  //====|    Factory methods    |=================================================================//
  
  /**
   * Converts a Throwable to a SessionInitializationException. If the Throwable is a
   * SessionInitializationException, it will be passed through unmodified; otherwise, it will be wrapped
   * in a new SessionInitializationException.
   *
   * @param cause the Throwable to convert
   *
   * @return a SessionInitializationException
   */
  public static SessionInitializationException fromThrowable(Throwable cause) {
    return (cause instanceof SessionInitializationException)
                   ? (SessionInitializationException) cause
                   : new SessionInitializationException(cause);
  }

  /**
   * Converts a Throwable to a SessionInitializationException with the specified detail message. If the
   * Throwable is a SessionInitializationException and if the Throwable's message is identical to the
   * one supplied, the Throwable will be passed through unmodified; otherwise, it will be wrapped in
   * a new SessionInitializationException with the detail message.
   *
   * @param cause       the Throwable to convert
   * @param message the specified detail message
   *
   * @return a SessionInitializationException
   */
  public static SessionInitializationException fromThrowable(String message, Throwable cause) {
    return (cause instanceof SessionInitializationException && Objects.equals(message, cause.getMessage()))
                   ? (SessionInitializationException) cause
                   : new SessionInitializationException(message, cause);
  }

  //====|    Construction    |====================================================================//
  
  /**
   * Construct a SessionInitializationException object with no specified message.
   */
  public SessionInitializationException() {
    super();
  }
  
  /**
   * Construct a SessionInitializationException object with a specified message.
   * @param msg a message describing the exception.
   */
  public SessionInitializationException(String msg) {
    super(msg);
  }
  
  /**
   * Construct a SessionInitializationException object with a specified message
   * and throwable.
   * @param msg the message string
   * @param cause the throwable that caused this exception
   */
  protected SessionInitializationException(String msg, Throwable cause) {
    super(msg, cause);
  }
  
  /**
   * Construct a SessionInitializationException object with a specified throwable.
   * @param cause the throwable that caused this exception
   */
  protected SessionInitializationException(Throwable cause) {
    super(cause);
  }
}
