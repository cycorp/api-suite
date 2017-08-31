package com.cyc.kb;

/*
 * #%L
 * File: DefaultContext.java
 * Project: Core API Object Specification
 * %%
 * Copyright (C) 2013 - 2017 Cycorp, Inc
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
 * An immutable object interface to store default contexts for assertions
 * and queries.
 * 
 * @author Vijay Raj
 * @version $Id: DefaultContext.java 169908 2017-01-11 23:19:09Z nwinant $
 */
public interface DefaultContext {

  public Context forAssertion();
  
  public Context forQuery();
}
