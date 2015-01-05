/**
 * The implementation of various interfaces specified in com.cyc.kb, refer to that package for
 * a more detailed discussion of using the API.
 * 
 * The main reason for having interfaces in the KB API is to allow for multiple-inheritance 
 * in Java classes of real-world concepts in the KB, which extend some of the interfaces in 
 * this API. For example, A WhiteHorse real-world object would be represented as a Java interface,
 * and would need to extend, Horse and WhiteObject interfaces. The Horse interface extends KBIndividual.
 * 
 * This package contains {@link com.cyc.kb.client.KBUtils} class and (soon) other utilities that are not object
 * properties, but provide useful functionalities. For example, min-collection defines the most specific
 * collection among a set of KBCollections. It would potentially be in KBCollectionUtils.
 * 
 * Finally, {@link com.cyc.kb.client.StandardKBObject} abstract class and {@link com.cyc.kb.client.KBObjectFactory}
 * consolidate all the common constructor and factory related code.
 */

/*
 * Perhaps the description here, part of it, should be in com.cyc.kb instead of .client, but 
 * having description of classes in interface makes sence since interface is what we want users
 * to focus on.
 */
package com.cyc.kb.client;

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
