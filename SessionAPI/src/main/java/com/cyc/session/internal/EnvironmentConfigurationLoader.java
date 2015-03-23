package com.cyc.session.internal;

/*
 * #%L
 * File: EnvironmentConfigurationLoader.java
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

import com.cyc.session.SessionConfigurationException;
import com.cyc.session.EnvironmentConfiguration;
import com.cyc.session.SessionConfigurationLoader;
import com.cyc.session.SessionConfigurationProperties;
import java.awt.GraphicsEnvironment;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A SessionConfigurationLoader devoted specifically to providing {@link EnvironmentConfiguration}s
 * from System properties.
 * @author nwinant
 */
public class EnvironmentConfigurationLoader implements SessionConfigurationLoader {
  
  // Fields
  
  public static final String NAME = "environment";
  private static final Logger LOGGER = LoggerFactory.getLogger(EnvironmentConfigurationLoader.class);
  final private PropertiesReader reader = new PropertiesReader();
  
  
  // Public
  
  @Override
  public void setEnvironment(EnvironmentConfiguration config) {
    // For this class, this is a no-op.
  }
  
  @Override
  public EnvironmentConfiguration getConfiguration() throws SessionConfigurationException {
    return new EnvironmentConfigurationImpl(reader.fromSystem(), this.getClass());
  }
  
  @Override
  public String getName() {
    return NAME;
  }
  
  @Override
  public boolean isCapableOfSuccess() {
    return true;
  }
  
  
  // Static

  static public boolean isHeadlessEnvironment() {
    // TODO: should this be in EnvironmentConfiguration?
    //       return javax.swing.SwingUtilities.isEventDispatchThread();
    if (!"true".equals(System.getProperty("java.awt.headless"))) {
      try {
        // See http://www.oracle.com/technetwork/articles/javase/headless-136834.html
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        return ge.isHeadlessInstance();
      } catch (UnsatisfiedLinkError ule) {
        LOGGER.warn("Assuming headless environment: {}", ule.getMessage());
      }
    }
    return true;
  }
  
  public static boolean isEnvironmentEmpty() {
    return (System.getProperty(SessionConfigurationProperties.SERVER_KEY) == null)
            && (System.getProperty(SessionConfigurationProperties.POLICY_FILE_KEY) == null)
            && (System.getProperty(SessionConfigurationProperties.POLICY_NAME_KEY) == null);
  }
  
  
  // Inner classes
  
  /**
   * Implementation of {@link EnvironmentConfiguration}.
   */
  public static class EnvironmentConfigurationImpl extends ImmutableConfiguration implements EnvironmentConfiguration {
    
    // Constructor
    
    public EnvironmentConfigurationImpl(Properties properties, Class loaderClass) throws SessionConfigurationException {
      super(properties, loaderClass);
    }
    
    @Override
    public boolean isGuiInteractionAllowed() {
      return true;
    }
  }
}
