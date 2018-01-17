package com.cyc.kb.exception;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Objects;

/*
 * #%L
 * File: KbRuntimeException.java
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
 * Root class for runtime exceptions thrown by the KB API.
 *
 * For now, all {@link UnknownHostException}s and {@link IOException}s from
 * OpenCyc API will be wrapped with KbRuntimeException. This is because the
 * KB API user is not expected to handle such exceptions.
 *
 * @author Vijay Raj
 * @version $Id: KbRuntimeException.java 151668 2014-06-03 21:46:52Z jmoszko
 * $
 */
public class KbRuntimeException extends RuntimeException {

  //====|    Factory methods    |=================================================================//
  
  /**
   * Converts a Throwable to a KbRuntimeException. If the Throwable is a
   * KbRuntimeException, it will be passed through unmodified; otherwise, it will be wrapped
   * in a new KbRuntimeException.
   *
   * @param cause the Throwable to convert
   *
   * @return a KbRuntimeException
   */
  public static KbRuntimeException fromThrowable(Throwable cause) {
    return (cause instanceof KbRuntimeException)
                   ? (KbRuntimeException) cause
                   : new KbRuntimeException(cause);
  }

  /**
   * Converts a Throwable to a KbRuntimeException with the specified detail message. If the
   * Throwable is a KbRuntimeException and if the Throwable's message is identical to the
   * one supplied, the Throwable will be passed through unmodified; otherwise, it will be wrapped in
   * a new KbRuntimeException with the detail message.
   *
   * @param cause       the Throwable to convert
   * @param message the specified detail message
   *
   * @return a KbRuntimeException
   */
  public static KbRuntimeException fromThrowable(String message, Throwable cause) {
    return (cause instanceof KbRuntimeException && Objects.equals(message, cause.getMessage()))
                   ? (KbRuntimeException) cause
                   : new KbRuntimeException(message, cause);
  }

  //====|    Construction    |====================================================================//
  
  protected KbRuntimeException(Throwable cause) {
    super(cause);
  }

  public KbRuntimeException(String msg) {
    super(msg);
  }

  protected KbRuntimeException(String msg, Throwable cause) {
    super(msg, cause);
  }
  
}
