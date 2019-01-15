package com.cyc;

/*
 * #%L
 * File: CycApiEntryPoint.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2019 Cycorp, Inc
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
 * A marker service provider interface (SPI) for Cyc API entrypoints. This represents the primary
 * entry point into a Cyc API (e.g., Session API, KB API, Query API, etc.) and any API which extends
 * it should do so exactly <em>once</em>.
 *
 * <p>
 * An API may have multiple services defined, but only the API's CycApiEntryPoint will be loaded,
 * and the primary service may then provide additional services. For example, the KB API's
 * {@link com.cyc.kb.spi.KbApiService} provides a number of other KB-related services
 * ({@link com.cyc.kb.spi.AssertionService}, {@link com.cyc.kb.spi.KbTermService}, etc.) To simplify
 * service loading, CycApiEntryPoints may have methods which accept a class that extends
 * {@link com.cyc.CycServicesLoader} to provide access to whatever service loader methods would be
 * appropriate; an example of this is 
 * {@link com.cyc.query.spi.QueryApiService#getQueryExplanationFactoryServices(com.cyc.CoreServicesLoader) }.
 *
 * <p>
 * Note that a CycApiEntryPoint may not be directly exposed to the calling application. Although
 * this is possible, method calls to a CycApiEntryPoint are often made through wrapper methods on
 * {@link Cyc}, and/or via static interface methods.
 *
 * @author nwinant
 */
public interface CycApiEntryPoint {

}
