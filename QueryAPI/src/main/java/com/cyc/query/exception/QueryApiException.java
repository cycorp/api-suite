/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cyc.query.exception;

/*
 * #%L
 * File: QueryApiException.java
 * Project: Query API
 * %%
 * Copyright (C) 2013 - 2015 Cycorp, Inc.
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
 * The root of the checked exception hierarchy in the Query API.
 * @author baxter
 */
public class QueryApiException extends Exception {

  public QueryApiException(final String message) {
    super(message);
  }

  public QueryApiException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public QueryApiException(final Throwable cause) {
    super(cause);
  }

}
