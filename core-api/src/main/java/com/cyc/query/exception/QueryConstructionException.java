package com.cyc.query.exception;

/*
 * #%L
 * File: QueryConstructionException.java
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
 * The class of {@link Exception} thrown when a {@link com.cyc.query.Query} cannot be constructed as
 * specified.
 *
 * @author baxter
 */
public class QueryConstructionException extends QueryException {

  //====|    Factory methods    |=================================================================//
  
  /**
   * Converts a Throwable to a QueryConstructionException. If the Throwable is a
   * QueryConstructionException, it will be passed through unmodified; otherwise, it will be wrapped
   * in a new QueryConstructionException.
   *
   * @param cause the Throwable to convert
   *
   * @return a QueryConstructionException
   */
  public static QueryConstructionException fromThrowable(Throwable cause) {
    return (cause instanceof QueryConstructionException)
                   ? (QueryConstructionException) cause
                   : new QueryConstructionException(cause);
  }

  /**
   * Converts a Throwable to a QueryConstructionException with the specified detail message. If the
   * Throwable is a QueryConstructionException and if the Throwable's message is identical to the
   * one supplied, the Throwable will be passed through unmodified; otherwise, it will be wrapped in
   * a new QueryConstructionException with the detail message.
   *
   * @param cause       the Throwable to convert
   * @param message the specified detail message
   *
   * @return a QueryConstructionException
   */
  public static QueryConstructionException fromThrowable(String message, Throwable cause) {
    return (cause instanceof QueryConstructionException && Objects.equals(message, cause.getMessage()))
                   ? (QueryConstructionException) cause
                   : new QueryConstructionException(message, cause);
  }

  //====|    Construction    |====================================================================//
  
  protected QueryConstructionException(Throwable throwable) {
    super(throwable);
  }

  protected QueryConstructionException(String message, Throwable throwable) {
    super(message, throwable);
  }

  public QueryConstructionException(String message) {
    super(message);
  }

}
