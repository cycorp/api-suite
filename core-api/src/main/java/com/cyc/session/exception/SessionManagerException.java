package com.cyc.session.exception;

import com.cyc.session.SessionManager;
import java.util.Objects;

/*
 * #%L
 * File: SessionManagerException.java
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
 * Indicates an error with an existing {@link SessionManager}.
 *
 * @author nwinant
 */
public class SessionManagerException extends SessionException {
  
  //====|    Factory methods    |=================================================================//
  
  /**
   * Converts a Throwable to a SessionManagerException. If the Throwable is a
   * SessionManagerException, it will be passed through unmodified; otherwise, it will be wrapped
   * in a new SessionManagerException.
   *
   * @param cause the Throwable to convert
   *
   * @return a SessionManagerException
   */
  public static SessionManagerException fromThrowable(Throwable cause) {
    return (cause instanceof SessionManagerException)
                   ? (SessionManagerException) cause
                   : new SessionManagerException(cause);
  }

  /**
   * Converts a Throwable to a SessionManagerException with the specified detail message. If the
   * Throwable is a SessionManagerException and if the Throwable's message is identical to the
   * one supplied, the Throwable will be passed through unmodified; otherwise, it will be wrapped in
   * a new SessionManagerException with the detail message.
   *
   * @param cause       the Throwable to convert
   * @param message the specified detail message
   *
   * @return a SessionManagerException
   */
  public static SessionManagerException fromThrowable(String message, Throwable cause) {
    return (cause instanceof SessionManagerException && Objects.equals(message, cause.getMessage()))
                   ? (SessionManagerException) cause
                   : new SessionManagerException(message, cause);
  }

  //====|    Construction    |====================================================================//
  
  
  /**
   * Construct a SessionManagerException object with no specified message.
   */
  public SessionManagerException() {
    super();
  }
  
  /**
   * Construct a SessionManagerException object with a specified message.
   * 
   * @param msg a message describing the exception.
   */
  public SessionManagerException(String msg) {
    super(msg);
  }
  
  /**
   * Construct a SessionManagerException object with a specified message and throwable.
   * 
   * @param msg the message string
   * @param cause the throwable that caused this exception
   */
  protected SessionManagerException(String msg, Throwable cause) {
    super(msg, cause);
  }
  
  /**
   * Construct a SessionManagerException object with a specified throwable.
   * 
   * @param cause the throwable that caused this exception
   */
  protected SessionManagerException(Throwable cause) {
    super(cause);
  }
}
