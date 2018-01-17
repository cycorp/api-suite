package com.cyc.session.exception;

import java.util.Objects;

/*
 * #%L
 * File: SessionCommandException.java
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
 * SessionCommandException indicates that Session implementation code could communicate with a Cyc
 * server, but encountered a problem issuing a command or interpreting a result.
 *
 * @author nwinant
 */
public class SessionCommandException extends SessionRuntimeException {
  

  //====|    Factory methods    |=================================================================//
  
  /**
   * Converts a Throwable to a SessionCommandException. If the Throwable is a
   * SessionCommandException, it will be passed through unmodified; otherwise, it will be wrapped
   * in a new SessionCommandException.
   *
   * @param cause the Throwable to convert
   *
   * @return a SessionCommandException
   */
  public static SessionCommandException fromThrowable(Throwable cause) {
    return (cause instanceof SessionCommandException)
                   ? (SessionCommandException) cause
                   : new SessionCommandException(cause);
  }

  /**
   * Converts a Throwable to a SessionCommandException with the specified detail message. If the
   * Throwable is a SessionCommandException and if the Throwable's message is identical to the
   * one supplied, the Throwable will be passed through unmodified; otherwise, it will be wrapped in
   * a new SessionCommandException with the detail message.
   *
   * @param cause       the Throwable to convert
   * @param message the specified detail message
   *
   * @return a SessionCommandException
   */
  public static SessionCommandException fromThrowable(String message, Throwable cause) {
    return (cause instanceof SessionCommandException && Objects.equals(message, cause.getMessage()))
                   ? (SessionCommandException) cause
                   : new SessionCommandException(message, cause);
  }

  //====|    Construction    |====================================================================//
  
  
  /**
   * Construct a SessionCommandException object with no specified message.
   */
  public SessionCommandException() {
    super();
  }
  
  /**
   * Construct a SessionCommandException object with a specified message.
   * @param msg a message describing the exception.
   */
  public SessionCommandException(String msg) {
    super(msg);
  }
  
  /**
   * Construct a SessionCommandException object with a specified message
   * and throwable.
   * @param msg the message string
   * @param cause the throwable that caused this exception
   */
  protected SessionCommandException(String msg, Throwable cause) {
    super(msg, cause);
  }
  
  /**
   * Construct a SessionCommandException object with a specified throwable.
   * @param cause the throwable that caused this exception
   */
  protected SessionCommandException(Throwable cause) {
    super(cause);
  }
}
