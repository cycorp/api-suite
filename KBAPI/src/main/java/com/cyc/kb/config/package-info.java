/**
 * This package contains useful configuration utilities of the KB API. 
 * 
 * {@link com.cyc.kb.config.KBAPIConfiguration} provides thread local configuration
 * parameters to set the current cyclist, project, default contexts for assertion
 * and query, and transcription. 
 * 
 * Note: Bookkeeping is enabled by default, but it will not work if current cyclist
 * is not set.
 * 
 * {@link com.cyc.kb.config.DefaultContext} is usually used in get and add/set methods that do not take 
 * a context parameter. The method documentation will specify the behavior in detail.
 */

package com.cyc.kb.config;

/*
 * #%L
 * File: package-info.java
 * Project: KB API
 * %%
 * Copyright (C) 2013 - 2015 Cycorp, Inc
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
