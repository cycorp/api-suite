package com.cyc.session.exception;

import java.util.Objects;

/*
 * #%L
 * File: OpenCycUnsupportedFeatureException.java
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
 * Indicates that a particular piece of functionality is categorically unsupported by OpenCyc 
 * servers. This exception marks an inherent limitation of the OpenCyc <em>feature set</em> which is
 * shared by all current (and presumably, future) releases of OpenCyc.
 * 
 * @author nwinant
 */
public class OpenCycUnsupportedFeatureException extends UnsupportedCycOperationException {
  
  //====|    Factory methods    |=================================================================//
  
  /**
   * Converts a Throwable to a OpenCycUnsupportedFeatureException. If the Throwable is a
   * OpenCycUnsupportedFeatureException, it will be passed through unmodified; otherwise, it will be wrapped
   * in a new OpenCycUnsupportedFeatureException.
   *
   * @param cause the Throwable to convert
   *
   * @return a OpenCycUnsupportedFeatureException
   */
  public static OpenCycUnsupportedFeatureException fromThrowable(Throwable cause) {
    return (cause instanceof OpenCycUnsupportedFeatureException)
                   ? (OpenCycUnsupportedFeatureException) cause
                   : new OpenCycUnsupportedFeatureException(cause);
  }

  /**
   * Converts a Throwable to a OpenCycUnsupportedFeatureException with the specified detail message. If the
   * Throwable is a OpenCycUnsupportedFeatureException and if the Throwable's message is identical to the
   * one supplied, the Throwable will be passed through unmodified; otherwise, it will be wrapped in
   * a new OpenCycUnsupportedFeatureException with the detail message.
   *
   * @param cause       the Throwable to convert
   * @param message the specified detail message
   *
   * @return a OpenCycUnsupportedFeatureException
   */
  public static OpenCycUnsupportedFeatureException fromThrowable(String message, Throwable cause) {
    return (cause instanceof OpenCycUnsupportedFeatureException && Objects.equals(message, cause.getMessage()))
                   ? (OpenCycUnsupportedFeatureException) cause
                   : new OpenCycUnsupportedFeatureException(message, cause);
  }
  
  //====|    Fields    |====================================================================//  
    
  public static final String DEFAULT_MSG = 
          "This functionality is not supported by OpenCyc servers.";
          
  //====|    Construction    |====================================================================//
  
  /**
   * Constructs a new exception with a specified message.
   * @param message a message describing the exception.
   */
  public OpenCycUnsupportedFeatureException(String message) {
    super(message);
  }
    
  /**
   * Constructs a new exception with no specified message.
   */
  public OpenCycUnsupportedFeatureException() {
    this(DEFAULT_MSG);
  }
  
  /**
   * Constructs a new exception with a specified message and throwable.
   * @param message the message string
   * @param cause the throwable that caused this exception
   */
  protected OpenCycUnsupportedFeatureException(String message, Throwable cause) {
    super(message, cause);
  }
  
  /**
   * Constructs a new exception with a specified throwable.
   * @param cause the throwable that caused this exception
   */
  protected OpenCycUnsupportedFeatureException(Throwable cause) {
    this(DEFAULT_MSG, cause);
  }
}
