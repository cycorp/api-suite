package com.cyc.session.internal;

/*
 * #%L
 * File: ConfigurationLoaderManager.java
 * Project: Session API Implementation
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
import com.cyc.session.SessionConfigurationException;
import com.cyc.session.SessionConfigurationLoader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The ConfigurationLoaderManager is responsible for loading 
 * {@link SessionConfigurationLoader} services, and calling them to load 
 * configurations.
 * 
 * @author nwinant
 */
public class ConfigurationLoaderManager {
  
  // Fields
  
  private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationLoaderManager.class);
  final private Map<String, SessionConfigurationLoader> configLoaders = new HashMap<String, SessionConfigurationLoader>();
  
  
  // Constructor
  
  public ConfigurationLoaderManager() throws SessionConfigurationException {
    reloadConfigurationLoaders();
  }
  
  
  // Public
  
  public CycSessionConfiguration getConfiguration(EnvironmentConfiguration environment) throws SessionConfigurationException {
    final CycSessionConfiguration config = expandConfiguration(environment, environment);
    final ConfigurationValidator configUtils = new ConfigurationValidator(config);
    if (!configUtils.isValid()) {
      throw new SessionConfigurationException("Configuration " + config + "is not valid; could not retrieve a valid configuration from " + config.getLoaderClass().getName());
    }
    return config;
  }
  
  /**
   * Recurses over CycSessionConfiguration if it specifies another configuration.
   * 
   * CycSessionConfigurations can specify other CycSessionConfigurations. For
   * example, an EnvironmentConfiguration could specify to load a configuration 
   * from a particular properties file, which in turn says to interactively 
   * prompt the user for configuration data. Typically you wouldn't want to 
   * chain beyond two levels -- an environmental configuration specifying 
   * another configuration method -- but there are cases where you might, and 
   * this allows that flexibility.
   * 
   * Order of precedence:
   * <ul>
   * <li>If the CycSessionConfiguration is sufficiently configured, use it.</li>
   * <li>Load a policy file, if specified.</li>
   * <li>Use a {@link SessionConfigurationLoader}, if one is specified.</li>
   * <li>Poll the user interactively via a GUI prompt, if allowed and if in a graphical environment.</li>
   * <li>Otherwise, return the original (presumably insufficient) configuration to be dealt with somehow.</li>
   * </ul>
   * 
   * @param config
   * @param environment
   * @return the configuration
   * @throws SessionConfigurationException 
   */
  public CycSessionConfiguration expandConfiguration(CycSessionConfiguration config, EnvironmentConfiguration environment) throws SessionConfigurationException {
    // TODO: add an enforced max depth param.
    // TODO: add loop checking.
   
    final ConfigurationValidator configUtils = new ConfigurationValidator(config);
    if (configUtils.isSufficient()) {
      LOGGER.debug("Found a sufficient configuration: {}", config);
      return config;
    }
    if (config.getPolicyFileName() != null) {
      return expandConfiguration(loadConfigurationViaPolicyFileName(config.getPolicyName(), environment), environment);
    }
    if (config.getPolicyName() != null) {
      return expandConfiguration(loadConfigurationViaPolicyName(config.getPolicyName(), environment), environment);
    }
    if (isGuiAllowed(config, environment)) {
      return expandConfiguration(loadConfigurationViaGUI(environment), environment);
    }
    LOGGER.warn("Configuration is not sufficient, but cannot be expanded: {}", config);
    
    // TODO: should we throw an exception when a configuration can't be expanded and isn't sufficient?
    return config;
  }
  
  public CycSessionConfiguration loadConfiguration(SessionConfigurationLoader loader, EnvironmentConfiguration environment) throws SessionConfigurationException {
    LOGGER.debug("Retrieving configuration from {}.", loader.getClass());
    loader.setEnvironment(environment);
    if (!loader.isCapableOfSuccess()) {
      throw new SessionConfigurationException("Loader " + loader.getClass() + " was selected, but is not capable of success.");
    }
    return loader.getConfiguration();
  }
  
  public CycSessionConfiguration loadConfigurationViaPolicyName(String policyName, EnvironmentConfiguration environment) throws SessionConfigurationException {
    LOGGER.debug("Attempting to load configuration named '{}'...", policyName);
    if (EnvironmentConfigurationLoader.NAME.equals(policyName)) {
      // Internal EnvironmentConfigurationLoader always gets precedence
      return loadConfiguration(new EnvironmentConfigurationLoader(), environment);
    }
    if (getConfigurationLoaders().containsKey(policyName)) {
      final SessionConfigurationLoader loader = getConfigurationLoaders().get(policyName);
      return loadConfiguration(loader, environment);
    }
    throw new SessionConfigurationException("Configuration policy named '" + policyName + "' was requested, but no such policy could be found.");
  }
  
  public CycSessionConfiguration loadConfigurationViaPolicyFileName(String filename, EnvironmentConfiguration environment) throws SessionConfigurationException {
    LOGGER.debug("Attempting to load configuration via policy file {}...", filename);
    return loadConfiguration(new PropertiesConfigurationLoader(), environment);
  }
  
  public CycSessionConfiguration loadConfigurationViaGUI(EnvironmentConfiguration environment) throws SessionConfigurationException {
    LOGGER.debug("Attempting to load configuration interactively via GUI...");
    return loadConfiguration(new SimpleInteractiveLoader(), environment);
  }
  
  
  // Protected
  
  protected boolean isGuiAllowed(CycSessionConfiguration config, EnvironmentConfiguration environment) {
    return environment.isGuiInteractionAllowed()
            && config.isGuiInteractionAllowed()
            && !EnvironmentConfigurationLoader.isHeadlessEnvironment();
  }
    
  protected Map<String, SessionConfigurationLoader> getConfigurationLoaders() {
    return Collections.unmodifiableMap(this.configLoaders);
  }
    
  final protected void reloadConfigurationLoaders() throws SessionConfigurationException {
    // Note: The relevant service provider file in META-INF/services
    //       is generated by the serviceloader-maven-plugin, specified
    //       in the pom.xml file.
    configLoaders.clear();
    ServiceLoader<SessionConfigurationLoader> loader
            = ServiceLoader.load(SessionConfigurationLoader.class);
    for (SessionConfigurationLoader configLoader : loader) {
      final String name = configLoader.getName();
      if (configLoaders.containsKey(name)) {
        Class oldClass = configLoaders.get(name).getClass();
        throw new SessionConfigurationException(
                "Error attempting to add " + SessionConfigurationLoader.class.getSimpleName()
                        + " named '" + name + "' (" + configLoader.getClass() + "):"
                        + " Loader " + oldClass + " already registered under that name");
      }
      configLoaders.put(name, configLoader);
    }
  }
}
