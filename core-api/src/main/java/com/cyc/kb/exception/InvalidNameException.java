package com.cyc.kb.exception;

import java.util.Objects;

/*
 * #%L
 * File: InvalidNameException.java
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
 * Thrown when code attempts to create a KB entity with an invalid name.
 * 
 * @author Nathan Winant
 * @version $Id: InvalidNameException.java 176591 2018-01-09 17:27:27Z nwinant $ 
 */
// @todo specify what the requirements are for a Constant name.

public class InvalidNameException extends CreateException {
  
  //====|    Factory methods    |=================================================================//
  
  /**
   * Converts a Throwable to a InvalidNameException. If the Throwable is a
   * InvalidNameException, it will be passed through unmodified; otherwise, it will be wrapped
   * in a new InvalidNameException.
   *
   * @param cause the Throwable to convert
   *
   * @return a InvalidNameException
   */
  public static InvalidNameException fromThrowable(Throwable cause) {
    return (cause instanceof InvalidNameException)
                   ? (InvalidNameException) cause
                   : new InvalidNameException(cause);
  }

  /**
   * Converts a Throwable to a InvalidNameException with the specified detail message. If the
   * Throwable is a InvalidNameException and if the Throwable's message is identical to the
   * one supplied, the Throwable will be passed through unmodified; otherwise, it will be wrapped in
   * a new InvalidNameException with the detail message.
   *
   * @param cause       the Throwable to convert
   * @param message the specified detail message
   *
   * @return a InvalidNameException
   */
  public static InvalidNameException fromThrowable(String message, Throwable cause) {
    return (cause instanceof InvalidNameException && Objects.equals(message, cause.getMessage()))
                   ? (InvalidNameException) cause
                   : new InvalidNameException(message, cause);
  }

  //====|    Construction    |====================================================================//
  
  protected InvalidNameException(Throwable cause) {
    super(cause);
  }

  public InvalidNameException(String msg) {
    super(msg);
  }

  protected InvalidNameException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
