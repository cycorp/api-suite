package com.cyc.kb.exception;

/*
 * #%L
 * File: StaleKbObjectException.java
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
import com.cyc.kb.Assertion;
import com.cyc.kb.KbObject;
import com.cyc.kb.KbTerm;
import com.cyc.kb.spi.KbService;
import java.util.Objects;

/**
 * Thrown when a stale {@link KbObject} is passed to a method.
 * <p>
 * The KB API factory methods that find or create terms and assertions cache the KB Object they 
 * return. Multiple get/findOrCreate requests to the same object will be return the identical 
 * underlying object from the cache. The cache can be cleared using {@link KbService#clearCache()},
 * which can be called like so: {@code Cyc.getKbService().clearCache(); }
 * <p>
 * When an object is deleted using {@link Assertion#delete()} or {@link KbTerm#delete()}, the object
 * is marked "invalid". The validity of the object can be checked using {@link Assertion#isValid()}
 * or {@link KbTerm#isValid()}, respectively.
 *
 * @author Vijay Raj
 * @version $Id: StaleKbObjectException.java 151668 2014-06-03 21:46:52Z jmoszko
 * $
 */
public class StaleKbObjectException extends KbRuntimeException {

  //====|    Factory methods    |=================================================================//
  
  /**
   * Converts a Throwable to a StaleKbObjectException. If the Throwable is a
   * StaleKbObjectException, it will be passed through unmodified; otherwise, it will be wrapped
   * in a new StaleKbObjectException.
   *
   * @param cause the Throwable to convert
   *
   * @return a StaleKbObjectException
   */
  public static StaleKbObjectException fromThrowable(Throwable cause) {
    return (cause instanceof StaleKbObjectException)
                   ? (StaleKbObjectException) cause
                   : new StaleKbObjectException(cause);
  }

  /**
   * Converts a Throwable to a StaleKbObjectException with the specified detail message. If the
   * Throwable is a StaleKbObjectException and if the Throwable's message is identical to the
   * one supplied, the Throwable will be passed through unmodified; otherwise, it will be wrapped in
   * a new StaleKbObjectException with the detail message.
   *
   * @param cause       the Throwable to convert
   * @param message the specified detail message
   *
   * @return a StaleKbObjectException
   */
  public static StaleKbObjectException fromThrowable(String message, Throwable cause) {
    return (cause instanceof StaleKbObjectException && Objects.equals(message, cause.getMessage()))
                   ? (StaleKbObjectException) cause
                   : new StaleKbObjectException(message, cause);
  }

  //====|    Construction    |====================================================================//
  
  public StaleKbObjectException(String msg) {
    super(msg);
  }

  protected StaleKbObjectException(Throwable cause) {
    super(cause);
  }

  protected StaleKbObjectException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
