/**
 * Provides the core classes for constructing, specifying, and running Cyc
 * queries. Queries are requests to Cyc to either confirm that something is
 * true, or else to find bindings for open {@link com.cyc.kb.Variable}s that result in true
 * sentences. They can be asked from different {@link com.cyc.kb.Context}s, and given
 * different resource constraints, using {@link com.cyc.base.inference.InferenceParameters}.
 * <p/>
 * Results can be inspected either while a query is running, or after it has
 * finished. {@link com.cyc.query.QueryListener}s can be defined to handle query-related
 * events such as when new answers are found or the query exhausts.
 * <p/>
 * Results can be processed either as {@link com.cyc.query.QueryAnswer}s, or as SQL-style
 * {@link com.cyc.query.KBInferenceResultSet}s.
 *
 */
package com.cyc.query;

/*
 * #%L
 * File: package-info.java
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
