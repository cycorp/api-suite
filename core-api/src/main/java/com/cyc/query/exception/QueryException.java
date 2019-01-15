package com.cyc.query.exception;

/*
 * #%L
 * File: QueryException.java
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
import java.util.Objects;

/**
 * The root of the checked exception hierarchy in the Query API.
 *
 * @author baxter
 */
public class QueryException extends Exception {
  
  //====|    Factory methods    |=================================================================//
  
  /**
   * Converts a Throwable to a QueryException. If the Throwable is a QueryException, it will be
   * passed through unmodified; otherwise, it will be wrapped in a new QueryException.
   *
   * @param cause the Throwable to convert
   *
   * @return a QueryException
   */
  public static QueryException fromThrowable(Throwable cause) {
    return (cause instanceof QueryException)
                   ? (QueryException) cause
                   : new QueryException(cause);
  }

  /**
   * Converts a Throwable to a QueryException with the specified detail message. If the Throwable is
   * a QueryException and if the Throwable's message is identical to the one supplied, the Throwable
   * will be passed through unmodified; otherwise, it will be wrapped in a new QueryException with
   * the detail message.
   *
   * @param cause       the Throwable to convert
   * @param message the specified detail message
   *
   * @return a QueryException
   */
  public static QueryException fromThrowable(String message, Throwable cause) {
    return (cause instanceof QueryException && Objects.equals(message, cause.getMessage()))
                   ? (QueryException) cause
                   : new QueryException(message, cause);
  }
  
  //====|    Construction    |====================================================================//
  
  public QueryException(final String message) {
    super(message);
  }

  protected QueryException(final String message, final Throwable cause) {
    super(message, cause);
  }

  protected QueryException(final Throwable cause) {
    super(cause);
  }

}
