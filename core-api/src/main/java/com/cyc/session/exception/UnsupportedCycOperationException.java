package com.cyc.session.exception;

import java.util.Objects;

/*
 * #%L
 * File: UnsupportedCycOperationException.java
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
 * Thrown to indicate that a Cyc server does not support a particular operation.
 * 
 * @author nwinant
 */
public class UnsupportedCycOperationException extends SessionRuntimeException {
  
  //====|    Factory methods    |=================================================================//
  
  /**
   * Converts a Throwable to a UnsupportedCycOperationException. If the Throwable is a
   * UnsupportedCycOperationException, it will be passed through unmodified; otherwise, it will be wrapped
   * in a new UnsupportedCycOperationException.
   *
   * @param cause the Throwable to convert
   *
   * @return a UnsupportedCycOperationException
   */
  public static UnsupportedCycOperationException fromThrowable(Throwable cause) {
    return (cause instanceof UnsupportedCycOperationException)
                   ? (UnsupportedCycOperationException) cause
                   : new UnsupportedCycOperationException(cause);
  }

  /**
   * Converts a Throwable to a UnsupportedCycOperationException with the specified detail message. If the
   * Throwable is a UnsupportedCycOperationException and if the Throwable's message is identical to the
   * one supplied, the Throwable will be passed through unmodified; otherwise, it will be wrapped in
   * a new UnsupportedCycOperationException with the detail message.
   *
   * @param cause       the Throwable to convert
   * @param message the specified detail message
   *
   * @return a UnsupportedCycOperationException
   */
  public static UnsupportedCycOperationException fromThrowable(String message, Throwable cause) {
    return (cause instanceof UnsupportedCycOperationException && Objects.equals(message, cause.getMessage()))
                   ? (UnsupportedCycOperationException) cause
                   : new UnsupportedCycOperationException(message, cause);
  }

  //====|    Construction    |====================================================================//
  
  /**
   * Constructs a new exception with no specified message.
   */
  public UnsupportedCycOperationException() {
    super();
  }
  
  /**
   * Constructs a new exception with a specified message.
   * @param message a message describing the exception.
   */
  public UnsupportedCycOperationException(String message) {
    super(message);
  }
  
  /**
   * Constructs a new exception with a specified message and throwable.
   * @param message the message string
   * @param cause the throwable that caused this exception
   */
  protected UnsupportedCycOperationException(String message, Throwable cause) {
    super(message, cause);
  }
  
  /**
   * Constructs a new exception with a specified throwable.
   * @param cause the throwable that caused this exception
   */
  protected UnsupportedCycOperationException(Throwable cause) {
    super(cause);
  }
}
