package com.cyc.kb.exception;

import java.util.Objects;

/*
 * #%L
 * File: KbServerSideException.java
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
 * Wraps all Cyc server errors, when a more specific semantic error can not be thrown.
 *
 * @author Vijay Raj
 * @version $Id: KbServerSideException.java 176267 2017-12-13 04:02:46Z nwinant $
 */
public class KbServerSideException extends KbRuntimeException{
  
  //====|    Factory methods    |=================================================================//
  
  /**
   * Converts a Throwable to a KbServerSideException. If the Throwable is a
   * KbServerSideException, it will be passed through unmodified; otherwise, it will be wrapped
   * in a new KbServerSideException.
   *
   * @param cause the Throwable to convert
   *
   * @return a KbServerSideException
   */
  public static KbServerSideException fromThrowable(Throwable cause) {
    return (cause instanceof KbServerSideException)
                   ? (KbServerSideException) cause
                   : new KbServerSideException(cause);
  }

  /**
   * Converts a Throwable to a KbServerSideException with the specified detail message. If the
   * Throwable is a KbServerSideException and if the Throwable's message is identical to the
   * one supplied, the Throwable will be passed through unmodified; otherwise, it will be wrapped in
   * a new KbServerSideException with the detail message.
   *
   * @param cause       the Throwable to convert
   * @param message the specified detail message
   *
   * @return a KbServerSideException
   */
  public static KbServerSideException fromThrowable(String message, Throwable cause) {
    return (cause instanceof KbServerSideException && Objects.equals(message, cause.getMessage()))
                   ? (KbServerSideException) cause
                   : new KbServerSideException(message, cause);
  }

  //====|    Construction    |====================================================================//
  
  protected KbServerSideException(Throwable cause) {
    super(cause);
  }

  public KbServerSideException(String msg) {
    super(msg);
  }

  protected KbServerSideException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
