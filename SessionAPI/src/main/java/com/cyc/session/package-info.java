/**
 * The primary interfaces of the Cyc Session API.
 * 
 * The Session API defines simple, yet flexible configuration-driven management
 * of Cyc sessions -- such as acquiring server addresses, managing connections, 
 * etc. -- so that library and application developers don't need to write it
 * into their own code. At the same time, the API is extensible enough 
 * developers can reasonably write their own modules to address unique 
 * configuration needs.
 * 
 * End users should only need to directly interact with the classes and 
 * interfaces defined in com.cyc.session. Interfaces and classes in sub-packages 
 * are for implementation providers.
 */
package com.cyc.session;

/*
 * #%L
 * File: package-info.java
 * Project: Session API
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
