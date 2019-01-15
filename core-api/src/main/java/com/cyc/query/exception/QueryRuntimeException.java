package com.cyc.query.exception;

/*
 * #%L
 * File: QueryRuntimeException.java
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
 * The root of the unchecked exception hierarchy in the Query API.
 *
 * @author baxter
 */
public class QueryRuntimeException extends RuntimeException {
  
  //====|    Factory methods    |=================================================================//
  
  /**
   * Converts a Throwable to a QueryRuntimeException. If the Throwable is a QueryRuntimeException,
   * it will be passed through unmodified; otherwise, it will be wrapped in a new
   * QueryRuntimeException.
   *
   * @param t the Throwable to convert
   *
   * @return a QueryRuntimeException
   */
  public static QueryRuntimeException fromThrowable(Throwable t) {
    return (t instanceof QueryRuntimeException)
                   ? (QueryRuntimeException) t
                   : new QueryRuntimeException(t);
  }

  /**
   * Converts a Throwable to a QueryRuntimeException with the specified detail message. If the
   * Throwable is a QueryRuntimeException and if the Throwable's message is identical to the one
   * supplied, the Throwable will be passed through unmodified; otherwise, it will be wrapped in a
   * new QueryRuntimeException with the detail message.
   *
   * @param t       the Throwable to convert
   * @param message the specified detail message
   *
   * @return a QueryRuntimeException
   */
  public static QueryRuntimeException fromThrowable(String message, Throwable t) {
    return (t instanceof QueryRuntimeException && Objects.equals(message, t.getMessage()))
                   ? (QueryRuntimeException) t
                   : new QueryRuntimeException(message, t);
  }
  
  //====|    Construction    |====================================================================//
  
  public QueryRuntimeException(final String message) {
    super(message);
  }

  protected QueryRuntimeException(final String message, final Throwable cause) {
    super(message, cause);
  }

  protected QueryRuntimeException(final Throwable cause) {
    super(cause);
  }

}
