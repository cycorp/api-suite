/**
 * KB API exception hierarchy.
 * 
 * {@link com.cyc.kb.exception.KbException} is the root level exception for KB API. All other
 * exceptions thrown by the API are sub-classes of this.
 * 
 * {@link com.cyc.kb.exception.StaleKbObjectException} is thrown when the Java program uses
 * an object reference that points to a KB concept that has been deleted, by calling the API
 * delete method.
 * 
 * {@link com.cyc.kb.exception.KbRuntimeException} is thrown mostly wrapping any lower-level 
 * connection exceptions. 
 * 
 * Refer to individual exception documentation for more details.
 */
package com.cyc.kb.exception;

/*
 * #%L
 * File: package-info.java
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
