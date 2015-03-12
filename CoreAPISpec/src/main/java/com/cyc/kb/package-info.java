/**
 * Provides basic interfaces for manipulating CycL-based java objects, both for
 * the purpose of making assertions and for retrieving terms and values
 * via simple queries.
 *
 * To use the KB API, you first need to ensure access to a Cyc server, which is
 * done by calling
 * {@link com.cyc.base.CycAccessManager#setCurrentAccess(com.cyc.base.CycAccess)}
 * with a <code>CycAccess</code> pointing to a live Cyc server. Because
 * <code>setCurrentAccess</code> sets a thread-local variable, any threads using
 * the KB API need to call <code>setCurrentAccess</code> before doing anything
 * that might need to access a Cyc server. In practice, this means that any
 * non-UI threads spawned in a KB API application need to call
 * <code>setCurrentAccess</code> before anything else happens in the thread.
 * Because methods accessing a Cyc server may not return instantaneously,
 * developers are strongly advised to avoid using KB API methods inside AWT
 * threads (or any threads on which a UI is critically dependent).
 * <p/>
 * The underlying API throws IOException and UnknownHostExeception to indicate
 * some arbitrary IO issue and Cyc server not being at the specified location
 * respectively. In the KB API both of these exceptions are wrapped inside a
 * KBAPIRuntimeException throughout the API and the cause field is set
 * appropriately.
 *
 * <p/>
 * The main uses of the KB API fall into two broad categories: (a) making terms
 * and assertions, and (b) running queries. In order to get the terms needed for
 * making either assertions or queries, the static factory methods
 * (<code>get</code> and <code>findOrCreate</code>) for the relevant
 * implementation classes in <code>com.cyc.kb.client</code> should be called.
 * These factory methods will retrieve the relevant object from the KB, and can
 * be set to create the object on the Cyc server if it doesn't already exist on
 * the server.
 * <p/>
 * Once the objects exists, <code>get</code> and <code>findOrCreate</code> static factory
 * methods in {@link com.cyc.kb.client.FactImpl} and {@link com.cyc.kb.client.AssertionImpl} 
 * can be used to create the actual
 * assertions. The KB API generally allows the creation and retrieval of Cyc KB
 * objects (KBIndividuals, KBCollections, Facts, etc.) from a variety of forms
 * including their string representations, their IDs, and from the objects that
 * will comprise them. (In many cases there are additional deprecated methods
 * that accept objects from the {@link com.cyc.base.cycobject.CycObject} class
 * hierarchy.) The string-based factory methods in <code>AssertionImpl</code> and
 * its sub-classes are a convenient way to create assertions without needing to
 * first create the <code>KBCollection</code> and <code>KBIndividual</code>
 * objects, but they do require correct syntax, and the terms must already exist
 * on the Cyc server.
 * <p/>
 * The API provides many methods that take Strings as inputs. This leads to most
 * methods having CreateException and KBTypeException. To limit the types of
 * exceptions in method signatures, we wrap them in IllegalArgumentException.
 * This is done only to the methods where to focus is not the creation of a new
 * term.
 * <p/>
 * All factory methods throw some exception under {@link com.cyc.kb.exception.CreateException} and
 * {@link com.cyc.kb.exception.KBTypeException}. To see the possible exceptions under the above
 * exceptions, see {@link com.cyc.kb.client.StandardKBObject} or the description
 * of subclasses of the exceptions.
 * <p/>
 * In many cases, the KB API includes methods that specify a context and similar
 * methods that don't require a context parameter. Default contexts for
 * asserting and querying can be set on a thread-by-thread basis using
 * {@link com.cyc.kb.config.KBAPIConfiguration#setDefaultContext(com.cyc.kb.config.DefaultContext)}.
 * <p/>
 * The current implementation of the KB API stores
 * {@link com.cyc.base.cycobject.CycObject}s as the core of each
 * <code>KBObject</code>. The <code>CycObject</code> can generally be retrieved
 * from each <code>KBObject</code> via the <code>getCore()</code> method. This
 * is useful for cases where lower-level APIs are necessary (e.g. using methods
 * from the {@link com.cyc.base.CycAccess} class directly).
 *
 *
 *
 */
package com.cyc.kb;

/*
 * #%L
 * File: package-info.java
 * Project: Core API Specification
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
