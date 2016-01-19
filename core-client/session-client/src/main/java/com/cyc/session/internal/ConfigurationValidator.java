package com.cyc.session.internal;

/*
 * #%L
 * File: ConfigurationValidator.java
 * Project: Session Client
 * %%
 * Copyright (C) 2013 - 2016 Cycorp, Inc.
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

import static com.cyc.session.SessionConfigurationProperties.*;
import com.cyc.session.CycServerAddress;
import com.cyc.session.CycSessionConfiguration;
import java.io.PrintStream;

/**
 * The ConfigurationValidator provides a set of simple checks to determine 
 * whether a CycSessionConfiguration object is well-formed.
 * 
 * @author nwinant
 */
public class ConfigurationValidator {
  
  final private CycSessionConfiguration config;
  
  public ConfigurationValidator(CycSessionConfiguration config) {
    this.config = config;
  }
  
  
  // Public
  
  public CycSessionConfiguration getConfiguration() {
    return this.config;
  }
  
  /**
   * Is a particular CycSessionConfiguration valid? I.e., is it not null, and 
   * does it have no fields with invalid values? Note that this function does not check 
   * whether configuration is <em>complete</em>; for that, see 
   * {@link #isSufficient()}.
   * 
   * @return is the configuration valid?
   */
  public boolean isValid() {
    return (getConfiguration() != null)
            && isCycServerValid()
            && isConfigurationLoaderNameValid()
            && isConfigurationFileNameValid();
  }
  
  /**
   * Is a particular CycSessionConfiguration sufficiently complete that a
   * valid CycSession could be created from it?
   * @return does the configuration have sufficient information?
   */
  public boolean isSufficient() {
    return isValid() && 
            (getConfiguration().getCycServer() != null);
  }
  
  /**
   * Note that this does not mean that this field is <em>populated</em>; merely
   * that it is not populated incorrectly.
   * @return are there errors in the server configuration?
   */
  public boolean isCycServerValid() {
    final CycServerAddress server = getConfiguration().getCycServer();
    return isUnpopulated(server) || server.isDefined();
  }
  
  /**
   * Note that this does not mean that this field is <em>populated</em>; merely
   * that it is not populated incorrectly.
   * @return is the configurationLoaderName valid (if set)?
   */
  public boolean isConfigurationLoaderNameValid() {
    final String name = this.getConfiguration().getConfigurationLoaderName();
    return isUnpopulated(name) || isConfigurationLoaderNameValid(getConfiguration().getConfigurationLoaderName());
  }
  
  /**
   * Note that this does not mean that this field is <em>populated</em>; merely
   * that it is not populated incorrectly.
   * @return is the configuration file name valid (if set)?
   */
  public boolean isConfigurationFileNameValid() {
    // TODO: if config.getConfigurationFileName(), validate path. Via org.apache.commons.io.FilenameUtils.normalize != null?
    return true;
  }
  
  public void print(PrintStream out) {
    if (getConfiguration() == null) {
      out.println("Configuration class: null");
      return;
    }
    out.println("Configuration class: " + getConfiguration().getClass().getName());
    out.println("         Loaded via: " + getConfiguration().getLoaderClass().getName());
    out.println(SERVER_KEY + ":     [" + getConfiguration().getCycServer() + "]");
    out.println(CONFIGURATION_LOADER_KEY + ": [" + getConfiguration().getConfigurationLoaderName() + "]");
    out.println(CONFIGURATION_FILE_KEY +": [" + getConfiguration().getConfigurationFileName() + "]");
  }
  
  public void print() {
    print(System.out);
  }
  
  
  // Protected
  
  protected boolean isUnpopulated(Object obj) {
    return (obj == null) || 
            ((obj instanceof String) && obj.toString().trim().isEmpty());
  }
  
  
  // Static
  
  public static boolean isConfigurationLoaderNameValid(String configLoaderName) {
    // TODO: should this be elsewhere? Possibly a ConfigurationLoaderName class?
    // TODO: flesh this out more via regex.
    return (configLoaderName != null) && !configLoaderName.trim().isEmpty();
  }
}
