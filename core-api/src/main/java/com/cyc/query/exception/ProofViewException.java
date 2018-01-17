package com.cyc.query.exception;

/*
 * #%L
 * File: ProofViewException.java
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
import java.util.Objects;

/**
 * The class of {@link Exception} thrown when a {@link com.cyc.query.ProofView} cannot be 
 * provided as specified.
 *
 * @author baxter
 */
public class ProofViewException extends QueryException {

  //====|    Factory methods    |=================================================================//
  
  /**
   * Converts a Throwable to a ProofViewException. If the Throwable is a
   * ProofViewException, it will be passed through unmodified; otherwise, it will be wrapped
   * in a new ProofViewException.
   *
   * @param cause the Throwable to convert
   *
   * @return a ProofViewException
   */
  public static ProofViewException fromThrowable(Throwable cause) {
    return (cause instanceof ProofViewException)
                   ? (ProofViewException) cause
                   : new ProofViewException(cause);
  }

  /**
   * Converts a Throwable to a ProofViewException with the specified detail message. If the
   * Throwable is a ProofViewException and if the Throwable's message is identical to the
   * one supplied, the Throwable will be passed through unmodified; otherwise, it will be wrapped in
   * a new ProofViewException with the detail message.
   *
   * @param cause       the Throwable to convert
   * @param message the specified detail message
   *
   * @return a ProofViewException
   */
  public static ProofViewException fromThrowable(String message, Throwable cause) {
    return (cause instanceof ProofViewException && Objects.equals(message, cause.getMessage()))
                   ? (ProofViewException) cause
                   : new ProofViewException(message, cause);
  }

  //====|    Construction    |====================================================================//
  
  protected ProofViewException(Throwable throwable) {
    super(throwable);
  }

  protected ProofViewException(String message, Throwable throwable) {
    super(message, throwable);
  }

  public ProofViewException(String message) {
    super(message);
  }

}
