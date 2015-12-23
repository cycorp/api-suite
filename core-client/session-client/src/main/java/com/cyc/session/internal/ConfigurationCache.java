package com.cyc.session.internal;

/*
 * #%L
 * File: ConfigurationCache.java
 * Project: Session Client
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

import com.cyc.session.CycSessionConfiguration;
import com.cyc.session.EnvironmentConfiguration;
import com.cyc.session.exception.SessionConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A cache for CycSessionConfigurations.
 * 
 * @author nwinant
 */
public class ConfigurationCache {
  
  // Fields
  
  // FIXME: what fresh hell is this? - nwinant, 2015-10,26
  
  private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationCache.class); 
  private EnvironmentConfiguration cachedEnvironment = null;
  private CycSessionConfiguration cachedConfiguration = null;
  
  
  // Public
  
  synchronized public boolean hasConfiguration(EnvironmentConfiguration environment) {
    return (this.cachedConfiguration != null) 
            && (this.cachedEnvironment != null) 
            && (this.cachedEnvironment.equals(environment));
  }
  
  synchronized public CycSessionConfiguration remove() {
    this.cachedEnvironment = null;
    this.cachedConfiguration = null;
    return null;
  }
  
  synchronized public CycSessionConfiguration get(EnvironmentConfiguration environment) {
    if (hasConfiguration(environment)) {
      return this.cachedConfiguration;
    }
    return null;
  }
  
  /**
   * Caches a CycSessionConfiguration, if the configuration and the environment both
   * specify that caching is allowed.
   * Cached configurations are cached with their environment, since all configs 
   * are assumed to be dependent upon their environment.
   * Any time a configuration is cached under a new environment, all configs cached
   * under older environments are assumed to be dirty and are discarded.
   * 
   * @param config
   * @param environment
   * @return the configuration
   * @throws com.cyc.session.exception.SessionConfigurationException 
   */
  synchronized public CycSessionConfiguration put(EnvironmentConfiguration environment, CycSessionConfiguration config) throws SessionConfigurationException {
    if (config == null) {
      throw new SessionConfigurationException("Cannot cache null configurations.");
    }
    if (config instanceof EnvironmentConfiguration) {
      throw new SessionConfigurationException("Cannot cache instances of " + EnvironmentConfiguration.class.getSimpleName());
    }
    if (!hasConfiguration(environment)
            && isCachingAllowed(environment, config)) {
      LOGGER.debug("Caching new config {} for environment {}", config, environment);
      this.cachedEnvironment = environment;
      this.cachedConfiguration = config;
    }
    return config;
  }
    
  public boolean isCachingAllowed(EnvironmentConfiguration environment, CycSessionConfiguration config) {
    return environment.isConfigurationCachingAllowed()
            && config.isConfigurationCachingAllowed();
  }
}
