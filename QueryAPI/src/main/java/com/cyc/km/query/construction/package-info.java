/**
 * Provides the classes specific to working with query searches.
 *
 * Query searches retrieve building blocks (and in some cases, fully specified
 * queries) for constructing useful queries based on natural-language input. The
 * building blocks can include terms identified in the NL input string, as well
 * as "query fragments" consisting of simple formulas with typically just one
 * predicate, some terms, and some variables. These can be combined to form a more
 * complex query that comes closer to the intended meaning of the NL input.
 * 
 * See com.cyc.km.query.construction.QuerySearchExamples in CoreAPIUseCases for examples of how this process can work.
 */
package com.cyc.km.query.construction;

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
