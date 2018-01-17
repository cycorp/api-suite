/**
 * Interfaces for manipulating CycL-based java objects, both for the purpose of
 * making assertions and for retrieving terms and values via simple queries.
 *
 * <p/>
 * Because methods accessing a Cyc server may not return instantaneously, developers are strongly
 * advised to avoid using KB API methods inside AWT threads (or any threads on which a UI is
 * critically dependent).
 *
 * <p/>
 * The underlying API throws IOException and UnknownHostExeception to indicate some arbitrary IO
 * issue and Cyc server not being at the specified location respectively. In the KB API both of
 * these exceptions are wrapped inside a KbRuntimeException throughout the API and the cause field
 * is set appropriately.
 *
 * <p/>
 * The main uses of the KB API fall into two broad categories: (a) making terms and assertions, and
 * (b) running queries. You may get terms, assertions, and queries via the static {@code #get(...)}
 * and {@code #findOrCreate(...)} factory methods on the interfaces themselves; sub-classes also
 * have such factory methods. These factory methods will retrieve the relevant object from the KB,
 * and can be set to create the object on the Cyc server if it doesn't already exist on the server.
 * E.g.:
 * <p>
 * <pre>
 * Context.get("SomeMt");
 * 
 * KbTerm.findOrCreate("SomeTerm");
 * 
 * Sentence.get(predicate, arg1, arg2);
 * 
 * Assertion.get("someassertionid");
 * 
 * Query.get(sentence, context);
 * </pre>
 * <p>
 * Once the objects exists, the {@code #get(...)} and {@code #findOrCreate(...)} methods on
 * {@link com.cyc.kb.Assertion}, {@link com.cyc.kb.Fact}, and {@link com.cyc.kb.Rule} can be used to
 * create the actual assertions. The KB API generally allows the creation and retrieval of Cyc KB
 * objects (KbIndividuals, KbCollections, Facts, etc.) from a variety of forms including their
 * string representations, their IDs, and from the objects that will comprise them. The string-based
 * factory methods in <code>Assertion</code> and its sub-class factories are a convenient way to 
 * create assertions without needing to first create the <code>KbCollection</code> and
 * <code>KbIndividual</code> objects, but they do require correct syntax, and the terms must already
 * exist on the Cyc server.
 *
 * <p/>
 * The API provides many methods that take Strings as inputs. This leads to most methods having
 * CreateException and KbTypeException. To limit the types of exceptions in method signatures, we
 * wrap them in IllegalArgumentException. This is done only to the methods where the focus is not 
 * the creation of a new term.
 *
 * <p/>
 * All factory methods throw exceptions as {@link com.cyc.kb.exception.CreateException},
 * {@link com.cyc.kb.exception.KbTypeException}, or a subclass of those exceptions.
 *
 * <p/>
 * In many cases, the KB API includes methods that specify a context and similar methods that don't
 * require a context parameter. Default contexts for asserting and querying can be set on a
 * thread-by-thread basis using
 * {@link com.cyc.session.SessionOptions#setDefaultContext(com.cyc.kb.DefaultContext)}.
 */
package com.cyc.kb;

/*
 * #%L
 * File: package-info.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2017 Cycorp, Inc
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
