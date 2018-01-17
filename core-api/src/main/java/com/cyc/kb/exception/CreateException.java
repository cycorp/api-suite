package com.cyc.kb.exception;

import java.util.Objects;

/*
 * #%L
 * File: CreateException.java
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
 * Thrown when code fails to find or create something from the Cyc KB. {@link KbTypeException} and
 * {@link KbTypeConflictException} are not subclasses because they do not indicate if the underlying
 * object is not in the KB.
 *
 * @author Vijay Raj
 * @version $Id: CreateException.java 176591 2018-01-09 17:27:27Z nwinant $
 */
public class CreateException extends KbException {

  //====|    Factory methods    |=================================================================//
  
  /**
   * Converts a Throwable to a CreateException. If the Throwable is a
   * CreateException, it will be passed through unmodified; otherwise, it will be wrapped
   * in a new CreateException.
   *
   * @param cause the Throwable to convert
   *
   * @return a CreateException
   */
  public static CreateException fromThrowable(Throwable cause) {
    return (cause instanceof CreateException)
                   ? (CreateException) cause
                   : new CreateException(cause);
  }

  /**
   * Converts a Throwable to a CreateException with the specified detail message. If the
   * Throwable is a CreateException and if the Throwable's message is identical to the
   * one supplied, the Throwable will be passed through unmodified; otherwise, it will be wrapped in
   * a new CreateException with the detail message.
   *
   * @param cause       the Throwable to convert
   * @param message the specified detail message
   *
   * @return a CreateException
   */
  public static CreateException fromThrowable(String message, Throwable cause) {
    return (cause instanceof CreateException && Objects.equals(message, cause.getMessage()))
                   ? (CreateException) cause
                   : new CreateException(message, cause);
  }

  //====|    Construction    |====================================================================//
  
  protected CreateException(Throwable cause) {
    super(cause);
  }

  public CreateException(String msg) {
    super(msg);
  }

  protected CreateException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
