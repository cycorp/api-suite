package com.cyc.kb.exception;

import java.util.Objects;

/*
 * #%L
 * File: DeleteException.java
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
 * Thrown when code fails to delete something from the Cyc KB.
 * 
 * @author Nathan Winant
 * @version $Id: DeleteException.java 176591 2018-01-09 17:27:27Z nwinant $
 */
public class DeleteException extends KbException {
  
  //====|    Factory methods    |=================================================================//
  
  /**
   * Converts a Throwable to a DeleteException. If the Throwable is a
   * DeleteException, it will be passed through unmodified; otherwise, it will be wrapped
   * in a new DeleteException.
   *
   * @param cause the Throwable to convert
   *
   * @return a DeleteException
   */
  public static DeleteException fromThrowable(Throwable cause) {
    return (cause instanceof DeleteException)
                   ? (DeleteException) cause
                   : new DeleteException(cause);
  }

  /**
   * Converts a Throwable to a DeleteException with the specified detail message. If the
   * Throwable is a DeleteException and if the Throwable's message is identical to the
   * one supplied, the Throwable will be passed through unmodified; otherwise, it will be wrapped in
   * a new DeleteException with the detail message.
   *
   * @param cause       the Throwable to convert
   * @param message the specified detail message
   *
   * @return a DeleteException
   */
  public static DeleteException fromThrowable(String message, Throwable cause) {
    return (cause instanceof DeleteException && Objects.equals(message, cause.getMessage()))
                   ? (DeleteException) cause
                   : new DeleteException(message, cause);
  }

  //====|    Construction    |====================================================================//
  
  protected DeleteException(Throwable cause) {
    super(cause);
  }

  public DeleteException(String msg) {
    super(msg);
  }

  protected DeleteException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
