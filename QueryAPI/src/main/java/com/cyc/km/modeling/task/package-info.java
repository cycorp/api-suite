/**
 * Provides the classes for representing and working with tasks.
 * 
 * Tasks are represented in the KB, and access to them via this package is entirely
 * read-only. They are instances of the Cyc collection #$CAEAnalysisTask.
 * 
 * Associating a {@link com.cyc.km.query.construction.QuerySearch} with a task constrains the query results
 * according to various filters and expansion rules associated with that task.
 * The goal is to return all and only those queries that are relevant to that task.
 * 
 */
package com.cyc.km.modeling.task;

/*
 * #%L
 * File: package-info.java
 * Project: Query API Implementation
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

