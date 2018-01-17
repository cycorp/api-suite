package com.cyc.kb.exception;

import java.util.Objects;

/*
 * #%L
 * File: KbTypeException.java
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
 * Thrown when an entity exists in the KB, but does not currently meet the
 * requirements of a particular semantic object type.
 * 
 * @author Nathan Winant
 * @version $Id: KbTypeException.java 176591 2018-01-09 17:27:27Z nwinant $
 */
public class KbTypeException extends KbException {
  
  //====|    Factory methods    |=================================================================//
  
  /**
   * Converts a Throwable to a KbTypeException. If the Throwable is a
   * KbTypeException, it will be passed through unmodified; otherwise, it will be wrapped
   * in a new KbTypeException.
   *
   * @param cause the Throwable to convert
   *
   * @return a KbTypeException
   */
  public static KbTypeException fromThrowable(Throwable cause) {
    return (cause instanceof KbTypeException)
                   ? (KbTypeException) cause
                   : new KbTypeException(cause);
  }

  /**
   * Converts a Throwable to a KbTypeException with the specified detail message. If the
   * Throwable is a KbTypeException and if the Throwable's message is identical to the
   * one supplied, the Throwable will be passed through unmodified; otherwise, it will be wrapped in
   * a new KbTypeException with the detail message.
   *
   * @param cause       the Throwable to convert
   * @param message the specified detail message
   *
   * @return a KbTypeException
   */
  public static KbTypeException fromThrowable(String message, Throwable cause) {
    return (cause instanceof KbTypeException && Objects.equals(message, cause.getMessage()))
                   ? (KbTypeException) cause
                   : new KbTypeException(message, cause);
  }

  //====|    Construction    |====================================================================//
  
  protected KbTypeException(Throwable cause) {
    super(cause);
  }

  public KbTypeException(String msg) {
    super(msg);
  }

  protected KbTypeException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
