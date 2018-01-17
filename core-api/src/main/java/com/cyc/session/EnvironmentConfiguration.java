package com.cyc.session;

/*
 * #%L
 * File: EnvironmentConfiguration.java
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
/**
 * A {@link CycSessionConfiguration} drawn specifically from the System properties. An instance of
 * EnvironmentConfiguration is a snapshot of the relevant System properties from the time at which
 * it was created. EnvironmentConfigurations always take precedence over all over configurations,
 * except to the extent that an EnvironmentConfiguration may specify another configuration.
 *
 * @author nwinant
 */
public interface EnvironmentConfiguration extends CycSessionConfiguration {

}
