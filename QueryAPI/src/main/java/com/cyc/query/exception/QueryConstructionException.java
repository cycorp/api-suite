/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cyc.query.exception;

/*
 * #%L
 * File: QueryConstructionException.java
 * Project: Query API
 * %%
 * Copyright (C) 2013 - 2014 Cycorp, Inc.
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
 * The class of {@link Exception} thrown when a {@link com.cyc.query.Query} cannot be
 * constructed as specified.
 *
 * @author baxter
 */
public class QueryConstructionException extends QueryApiException {

  public QueryConstructionException(Throwable t) {
    super(t);
  }
  
  public QueryConstructionException(String message, Throwable t) {
    super(message, t);
  }

}
