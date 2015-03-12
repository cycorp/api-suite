package com.cyc.session.internal;

/*
 * #%L
 * File: ConfigurationValidatorTest.java
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
import com.cyc.session.CycSessionConfiguration;
import com.cyc.session.*;
import com.cyc.session.SessionConfigurationProperties;
import java.util.Properties;
import junit.framework.TestCase;
import org.junit.Test;

/**
 *
 * @author nwinant
 */
public class ConfigurationValidatorTest extends TestCase {
  
  public ConfigurationValidatorTest(String testName) {
    super(testName);
  }
  
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    final Properties emptyProps = new Properties();
    final CycSessionConfiguration emptyConfig = loadProperties(emptyProps);
    empty = new ConfigurationValidator(emptyConfig);
    
    final Properties serverOnlyProps = new Properties();
    serverOnlyProps.setProperty(SessionConfigurationProperties.SERVER_KEY, "testserver:3640");
    final CycSessionConfiguration serverOnlyConfig = loadProperties(serverOnlyProps);
    serverOnly = new ConfigurationValidator(serverOnlyConfig);
  }
  
  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
  }
  
  private ConfigurationValidator empty;
  private ConfigurationValidator serverOnly;
  
  @Test
  public void testEmptyConfigurationValid() throws SessionConfigurationException {
    assertTrue(empty.isValid());
  }
  
  @Test
  public void testEmptyConfigurationSufficient() throws SessionConfigurationException {
    assertFalse(empty.isSufficient());
  }
  
  @Test
  public void testCycServerValid() throws SessionConfigurationException {
    assertTrue(serverOnly.isValid());
  }
  
  @Test
  public void testCycServerSufficient() throws SessionConfigurationException {
    assertTrue(serverOnly.isSufficient());
  }
  
  
  // Protected
  
  protected CycSessionConfiguration loadProperties(Properties props) throws SessionConfigurationException {
    final PropertiesConfigurationLoader propLoader = new PropertiesConfigurationLoader();
    assertNotNull(propLoader);
    CycSessionConfiguration config = propLoader.getConfiguration(props);
    assertNotNull(config);
    return config;
  }
}
