package com.cyc.kb.exception;

import java.util.Objects;

/*
 * #%L
 * File: InvalidFormulaInContextException.java
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
 * Thrown when the Cyc does not allow the specified formula to be true in the specified context. 
 * It does not distinguish between semantic and syntactic errors.
 *
 * @author Vijay Raj
 * @version $Id: InvalidFormulaInContextException.java 185299 2019-01-14 16:15:35Z daves $
 */
public class InvalidFormulaInContextException extends CreateException {

  //====|    Factory methods    |=================================================================//
  
  /**
   * Converts a Throwable to a InvalidFormulaInContextException. If the Throwable is a
   * InvalidFormulaInContextException, it will be passed through unmodified; otherwise, it will be wrapped
   * in a new InvalidFormulaInContextException.
   *
   * @param cause the Throwable to convert
   *
   * @return a InvalidFormulaInContextException
   */
  public static InvalidFormulaInContextException fromThrowable(Throwable cause) {
    return (cause instanceof InvalidFormulaInContextException)
                   ? (InvalidFormulaInContextException) cause
                   : new InvalidFormulaInContextException(cause);
  }

  /**
   * Converts a Throwable to a InvalidFormulaInContextException with the specified detail message. If the
   * Throwable is a InvalidFormulaInContextException and if the Throwable's message is identical to the
   * one supplied, the Throwable will be passed through unmodified; otherwise, it will be wrapped in
   * a new InvalidFormulaInContextException with the detail message.
   *
   * @param cause       the Throwable to convert
   * @param message the specified detail message
   *
   * @return a InvalidFormulaInContextException
   */
  public static InvalidFormulaInContextException fromThrowable(String message, Throwable cause) {
    return (cause instanceof InvalidFormulaInContextException && Objects.equals(message, cause.getMessage()))
                   ? (InvalidFormulaInContextException) cause
                   : new InvalidFormulaInContextException(message, cause);
  }

  //====|    Construction    |====================================================================//
  
  protected InvalidFormulaInContextException(Throwable cause) {
    super(cause);
  }

  public InvalidFormulaInContextException(String msg) {
    super(msg);
  }

  protected InvalidFormulaInContextException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
