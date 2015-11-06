package com.cyc.session.exception;

/*
 * #%L
 * File: UnsupportedCycOperationException.java
 * Project: Core API Specification
 * %%
 * Copyright (C) 2013 - 2015 Cycorp, Inc
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
public class UnsupportedCycOperationException extends SessionApiRuntimeException {
  
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
  public UnsupportedCycOperationException(String message, Throwable cause) {
    super(message, cause);
  }
  
  /**
   * Constructs a new exception with a specified throwable.
   * @param cause the throwable that caused this exception
   */
  public UnsupportedCycOperationException(Throwable cause) {
    super(cause);
  }
}
