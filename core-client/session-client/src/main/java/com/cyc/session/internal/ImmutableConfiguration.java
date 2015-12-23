package com.cyc.session.internal;

/*
 * #%L
 * File: ImmutableConfiguration.java
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

import com.cyc.session.CycServer;
import static com.cyc.session.SessionConfigurationProperties.*;
import com.cyc.session.exception.SessionConfigurationException;
import com.cyc.session.CycServerAddress;
import com.cyc.session.CycSessionConfiguration;
import java.util.Properties;

/**
 * An immutable CycSessionConfiguration implementation.
 * @author nwinant
 */
public class ImmutableConfiguration extends AbstractSessionConfigurationProperties implements CycSessionConfiguration {
  
  // Fields
  
  final private Class loaderClass;
  final private DefaultSessionOptions defaultOptions;
  private CycServerAddress server = null;
  
  
  // Constructor
  
  public ImmutableConfiguration(Properties properties, Class loaderClass) throws SessionConfigurationException {
    super(properties);
    this.loaderClass = loaderClass;
    this.defaultOptions = new DefaultSessionOptionsImpl();
    processProperties();
  }
  
  public ImmutableConfiguration(CycServerAddress server, Class loaderClass) throws SessionConfigurationException {
    this(new Properties(), loaderClass);
    this.properties.put(SERVER_KEY, server.toString());
    processProperties();
  }
  
  
  // Public
  
  @Override
  public CycServerAddress getCycServer() {
    return this.server;
  }
  
  @Override
  public String getConfigurationLoaderName() {
    return properties.getProperty(CONFIGURATION_LOADER_KEY);
  }
  
  @Override
  public String getConfigurationFileName() {
    return properties.getProperty(CONFIGURATION_FILE_KEY);
  }
  
  @Override
  public Class getLoaderClass() {
    return this.loaderClass;
  }
  
  @Override
  public boolean isConfigurationCachingAllowed() {
    // TODO: this needs to be derived from a property.
    return true;
  }
  
  @Override
  public boolean isSessionCachingAllowed() {
    // TODO: this needs to be derived from a property.
    return true;
  }
  
  @Override
  public boolean isServerPatchingAllowed() {
    return getBooleanProperty(SERVER_PATCHING_ALLOWED_KEY, false);
  }
  
  @Override
  public DefaultSessionOptions getDefaultSessionOptions() {
    // TODO: this needs to be derived from properties.
    return this.defaultOptions;
  }
  
  @Override
  public boolean isGuiInteractionAllowed() {
    return false;
  }
  
  @Override
  public boolean isEquivalent(CycSessionConfiguration configuration) {
    if ((configuration == null) || (configuration.getCycServer() == null)) {
      return false;
    }
    return configuration.getCycServer().equals(this.getCycServer());
  }
  
  @Override
  public boolean equals(Object obj) {
    if ((obj == null) || !(obj instanceof CycSessionConfiguration)) {
      return false;
    }
    return obj.hashCode() == this.hashCode();
  }
  
  @Override
  public int hashCode() {
    int hash = 5;
    hash = 53 * hash + (this.loaderClass != null ? this.loaderClass.hashCode() : 0);
    hash = 53 * hash + (this.server != null ? this.server.hashCode() : 0);
    return hash;
  }
  
  @Override
  public String toString() {
    return "[" + this.getClass().getSimpleName() + "#" + this.hashCode()
            + " (via " + this.loaderClass.getSimpleName() + ")]"
            +  " -> [" + CycServerAddress.class.getSimpleName() + "=" + getCycServer() + "]";
  }
  
  
  // Protected
  
  final protected void processProperties() throws SessionConfigurationException {
    if (properties.containsKey(SERVER_KEY)) {
      this.server = CycServer.fromString(properties.getProperty(SERVER_KEY));
    }
    if (this.isMisconfigured()) {
      final StringBuilder sb = new StringBuilder();
      final Properties misconfigured = this.getMisconfigured();
      for (String key : misconfigured.stringPropertyNames()) {
        sb.append(" [").append(key).append("=").append(misconfigured.getProperty(key)).append("]");
      }
      throw new SessionConfigurationException("Properties were misconfigured:" + sb.toString());
    }
  }
}
