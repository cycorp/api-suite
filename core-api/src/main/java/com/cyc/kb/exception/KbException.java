package com.cyc.kb.exception;

import java.util.Objects;

/*
 * #%L
 * File: KbException.java
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
 * Root class for exceptions thrown by the KB API.
 *
 * @author David Baxter
 * @version $Id: KbException.java 185299 2019-01-14 16:15:35Z daves $
 */
public class KbException extends Exception {

  //====|    Factory methods    |=================================================================//
  
  /**
   * Converts a Throwable to a KbException. If the Throwable is a
   * KbException, it will be passed through unmodified; otherwise, it will be wrapped
   * in a new KbException.
   *
   * @param cause the Throwable to convert
   *
   * @return a KbException
   */
  public static KbException fromThrowable(Throwable cause) {
    return (cause instanceof KbException)
                   ? (KbException) cause
                   : new KbException(cause);
  }

  /**
   * Converts a Throwable to a KbException with the specified detail message. If the
   * Throwable is a KbException and if the Throwable's message is identical to the
   * one supplied, the Throwable will be passed through unmodified; otherwise, it will be wrapped in
   * a new KbException with the detail message.
   *
   * @param cause       the Throwable to convert
   * @param message the specified detail message
   *
   * @return a KbException
   */
  public static KbException fromThrowable(String message, Throwable cause) {
    return (cause instanceof KbException && Objects.equals(message, cause.getMessage()))
                   ? (KbException) cause
                   : new KbException(message, cause);
  }

  //====|    Construction    |====================================================================//
  
  protected KbException(Throwable cause) {
    super(cause);
  }

  public KbException(String msg) {
    super(msg);
  }

  protected KbException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
