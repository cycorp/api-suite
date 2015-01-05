package com.cyc.session.internal;

/*
 * #%L
 * File: AbstractSessionConfigurationProperties.java
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

import static com.cyc.session.SessionConfigurationProperties.*;
import com.cyc.session.CycServer;
import com.cyc.session.CycSessionConfiguration;
import java.util.Properties;

/**
 * Abstract Properties-based implementation of CycSessionConfiguration. 
 * @author nwinant
 */
abstract public class AbstractSessionConfigurationProperties implements CycSessionConfiguration {
  
  // Internal
  
  final private Properties rawProperties;
  final protected Properties properties;
  private Properties misconfigured = null;
  
  
  // Constructor
  
  public AbstractSessionConfigurationProperties(Properties properties) {
    if (properties == null) {
      throw new NullPointerException("Received a null Properties object.");
    }
    this.rawProperties = properties;
    this.properties = READER.filterProperties(properties);
  }
  
  
  // Public
  
  @Override
  public Properties getRawProperties() {
    return (Properties) this.rawProperties.clone();
  }
  
  
  // Protected
  
  protected boolean isSufficientlyConfigured() {
    return properties.containsKey(POLICY_NAME_KEY)
            || properties.containsKey(POLICY_FILE_KEY)
            || properties.contains(SERVER_KEY);
  }
  
  protected Properties getMisconfigured() {
    return this.misconfigured;
  }
  
  protected boolean isMisconfigured() {
    return (this.misconfigured != null) && !this.misconfigured.isEmpty();
  }
  
  protected void putMisconfigured(String key, String value) {
    if (this.misconfigured == null) {
      this.misconfigured = new Properties();
    }
    this.misconfigured.put(key, value);
  }
  
  
  // Static
  
  private final PropertiesReader READER = new PropertiesReader() {
    @Override
    protected boolean isValidKey(String key) {
      if(super.isValidKey(key)) {
        for (String knownKey : ALL_KEYS) {
          if (knownKey.equals(key)) {
            return true;
          }
        }
      }
      return false;
    }
    
    @Override
    protected boolean isValidProperty(String key, String value) {
      if (SERVER_KEY.equals(key)) {
        if (!CycServer.isValidString(value)) {
          putMisconfigured(key, value);
          return false;
        }
      }
      else if (this.isValidKey(key)) {
        if ((value == null) || value.trim().isEmpty()) {
          putMisconfigured(key, value);
          return false;
        }
      }
      return true;
    }
  };
}
