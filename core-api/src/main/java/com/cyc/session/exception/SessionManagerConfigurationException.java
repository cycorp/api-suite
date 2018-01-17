package com.cyc.session.exception;

import com.cyc.session.SessionManagerConfiguration;
import java.util.Objects;

/*
 * #%L
 * File: SessionManagerConfigurationException.java
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
 * Indicates that a {@link SessionManagerConfiguration} has been mis-configured or not been 
 * configured at all.
 * 
 * @author nwinant
 */
public class SessionManagerConfigurationException extends SessionException {
  
  //====|    Factory methods    |=================================================================//
  
  /**
   * Converts a Throwable to a SessionManagerConfigurationException. If the Throwable is a
   * SessionManagerConfigurationException, it will be passed through unmodified; otherwise, it will be wrapped
   * in a new SessionManagerConfigurationException.
   *
   * @param cause the Throwable to convert
   *
   * @return a SessionManagerConfigurationException
   */
  public static SessionManagerConfigurationException fromThrowable(Throwable cause) {
    return (cause instanceof SessionManagerConfigurationException)
                   ? (SessionManagerConfigurationException) cause
                   : new SessionManagerConfigurationException(cause);
  }

  /**
   * Converts a Throwable to a SessionManagerConfigurationException with the specified detail message. If the
   * Throwable is a SessionManagerConfigurationException and if the Throwable's message is identical to the
   * one supplied, the Throwable will be passed through unmodified; otherwise, it will be wrapped in
   * a new SessionManagerConfigurationException with the detail message.
   *
   * @param cause       the Throwable to convert
   * @param message the specified detail message
   *
   * @return a SessionManagerConfigurationException
   */
  public static SessionManagerConfigurationException fromThrowable(String message, Throwable cause) {
    return (cause instanceof SessionManagerConfigurationException && Objects.equals(message, cause.getMessage()))
                   ? (SessionManagerConfigurationException) cause
                   : new SessionManagerConfigurationException(message, cause);
  }

  //====|    Construction    |====================================================================//
  
  
  /**
   * Construct a SessionManagerConfigurationException object with no specified message.
   */
  public SessionManagerConfigurationException() {
    super();
  }
  
  /**
   * Construct a SessionManagerConfigurationException object with a specified message.
   * 
   * @param msg a message describing the exception.
   */
  public SessionManagerConfigurationException(String msg) {
    super(msg);
  }
  
  /**
   * Construct a SessionManagerConfigurationException object with a specified message and throwable.
   * 
   * @param msg the message string
   * @param cause the throwable that caused this exception
   */
  protected SessionManagerConfigurationException(String msg, Throwable cause) {
    super(msg, cause);
  }
  
  /**
   * Construct a SessionManagerConfigurationException object with a specified throwable.
   * 
   * @param cause the throwable that caused this exception
   */
  protected SessionManagerConfigurationException(Throwable cause) {
    super(cause);
  }
}
