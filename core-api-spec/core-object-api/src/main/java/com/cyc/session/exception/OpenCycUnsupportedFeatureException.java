package com.cyc.session.exception;

/*
 * #%L
 * File: OpenCycUnsupportedFeatureException.java
 * Project: Core API Object Specification
 * %%
 * Copyright (C) 2013 - 2017 Cycorp, Inc
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
  
  // Fields
  
  public static final String DEFAULT_MSG = 
          "This functionality is not supported by OpenCyc servers.";
          
  
  // Constructors
  
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
  public OpenCycUnsupportedFeatureException(String message, Throwable cause) {
    super(message, cause);
  }
  
  /**
   * Constructs a new exception with a specified throwable.
   * @param cause the throwable that caused this exception
   */
  public OpenCycUnsupportedFeatureException(Throwable cause) {
    this(DEFAULT_MSG, cause);
  }
}
