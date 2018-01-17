package com.cyc.kb.exception;

import java.util.Objects;

/*
 * #%L
 * File: KbObjectNotFoundException.java
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
 * Thrown when a term or a formula cannot be found from the specified
 * context.    
 * 
 * @author Vijay Raj
 * @version $Id: KbObjectNotFoundException.java 176267 2017-12-13 04:02:46Z nwinant $
 */
/*
*/
public class KbObjectNotFoundException extends CreateException {

  //====|    Factory methods    |=================================================================//
  
  /**
   * Converts a Throwable to a KbObjectNotFoundException. If the Throwable is a
   * KbObjectNotFoundException, it will be passed through unmodified; otherwise, it will be wrapped
   * in a new KbObjectNotFoundException.
   *
   * @param cause the Throwable to convert
   *
   * @return a KbObjectNotFoundException
   */
  public static KbObjectNotFoundException fromThrowable(Throwable cause) {
    return (cause instanceof KbObjectNotFoundException)
                   ? (KbObjectNotFoundException) cause
                   : new KbObjectNotFoundException(cause);
  }

  /**
   * Converts a Throwable to a KbObjectNotFoundException with the specified detail message. If the
   * Throwable is a KbObjectNotFoundException and if the Throwable's message is identical to the
   * one supplied, the Throwable will be passed through unmodified; otherwise, it will be wrapped in
   * a new KbObjectNotFoundException with the detail message.
   *
   * @param cause       the Throwable to convert
   * @param message the specified detail message
   *
   * @return a KbObjectNotFoundException
   */
  public static KbObjectNotFoundException fromThrowable(String message, Throwable cause) {
    return (cause instanceof KbObjectNotFoundException && Objects.equals(message, cause.getMessage()))
                   ? (KbObjectNotFoundException) cause
                   : new KbObjectNotFoundException(message, cause);
  }

  //====|    Construction    |====================================================================//
  
  protected KbObjectNotFoundException(Throwable cause) {
    super(cause);
  }

  public KbObjectNotFoundException(String msg) {
    super(msg);
  }

  protected KbObjectNotFoundException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
