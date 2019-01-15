package com.cyc.session.exception;

import java.util.Objects;

/*
 * #%L
 * File: SessionConfigurationException.java
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
 * CycSessionException indicates that a Cyc session has been mis-configured or not been configured
 * at all.
 *
 * @author nwinant
 */
public class SessionConfigurationException extends SessionException {
  
  //====|    Factory methods    |=================================================================//
  
  /**
   * Converts a Throwable to a SessionConfigurationException. If the Throwable is a
   * SessionConfigurationException, it will be passed through unmodified; otherwise, it will be wrapped
   * in a new SessionConfigurationException.
   *
   * @param cause the Throwable to convert
   *
   * @return a SessionConfigurationException
   */
  public static SessionConfigurationException fromThrowable(Throwable cause) {
    return (cause instanceof SessionConfigurationException)
                   ? (SessionConfigurationException) cause
                   : new SessionConfigurationException(cause);
  }

  /**
   * Converts a Throwable to a SessionConfigurationException with the specified detail message. If the
   * Throwable is a SessionConfigurationException and if the Throwable's message is identical to the
   * one supplied, the Throwable will be passed through unmodified; otherwise, it will be wrapped in
   * a new SessionConfigurationException with the detail message.
   *
   * @param cause       the Throwable to convert
   * @param message the specified detail message
   *
   * @return a SessionConfigurationException
   */
  public static SessionConfigurationException fromThrowable(String message, Throwable cause) {
    return (cause instanceof SessionConfigurationException && Objects.equals(message, cause.getMessage()))
                   ? (SessionConfigurationException) cause
                   : new SessionConfigurationException(message, cause);
  }

  //====|    Construction    |====================================================================//
  
  
  /**
   * Construct a SessionConfigurationException object with no specified message.
   */
  public SessionConfigurationException() {
    super();
  }
  
  /**
   * Construct a SessionConfigurationException object with a specified message.
   * @param msg a message describing the exception.
   */
  public SessionConfigurationException(String msg) {
    super(msg);
  }
  
  /**
   * Construct a SessionConfigurationException object with a specified message
   * and throwable.
   * @param msg the message string
   * @param cause the throwable that caused this exception
   */
  protected SessionConfigurationException(String msg, Throwable cause) {
    super(msg, cause);
  }
  
  /**
   * Construct a SessionConfigurationException object with a specified throwable.
   * @param cause the throwable that caused this exception
   */
  protected SessionConfigurationException(Throwable cause) {
    super(cause);
  }
}
