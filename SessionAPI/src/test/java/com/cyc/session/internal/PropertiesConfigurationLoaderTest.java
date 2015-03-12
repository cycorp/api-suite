package com.cyc.session.internal;

/*
 * #%L
 * File: PropertiesConfigurationLoaderTest.java
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

import com.cyc.session.CycServer;
import com.cyc.session.SessionConfigurationException;
import com.cyc.session.CycSessionConfiguration;
import com.cyc.session.EnvironmentConfiguration;
import com.cyc.session.*;
import com.cyc.session.SessionConfigurationProperties;
import java.util.Properties;
import junit.framework.TestCase;
import org.junit.Test;

/**
 *
 * @author nwinant
 */
public class PropertiesConfigurationLoaderTest extends TestCase {
  
  private EnvironmentConfigurationLoader envloader;
  private PropertiesConfigurationLoader propLoader;
    
  public PropertiesConfigurationLoaderTest(String testName) {
    super(testName);
  }
  
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    this.envloader = new EnvironmentConfigurationLoader();
    this.propLoader = new PropertiesConfigurationLoader();
  }
  
  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
  }
  
  @Test
  public void testEmptyProperties() throws SessionConfigurationException {
    final Properties props = new Properties();
    final CycSessionConfiguration config = loadProperties(props);
    assertNull(config.getCycServer());
    assertNull(config.getPolicyFileName());
    assertNull(config.getPolicyName());
  }
  
  @Test
  public void testEmptySystemProperties() throws SessionConfigurationException {
    if (EnvironmentConfigurationLoader.isEnvironmentEmpty()) {
      final CycSessionConfiguration config = envloader.getConfiguration();
      assertNull(config.getCycServer());
      assertNull(config.getPolicyFileName());
      assertNull(config.getPolicyName());
    }
  }
  
  @Test
  public void testEmptyProperty() {
    String exMsg = null;
    try {
      final Properties props = new Properties();
      props.put(SessionConfigurationProperties.POLICY_FILE_KEY, "  ");
      final CycSessionConfiguration config = loadProperties(props);
      assertNull(config.getCycServer());
      assertNull(config.getPolicyFileName());
      assertNull(config.getPolicyName());
    } catch (SessionConfigurationException ex) {
      exMsg = ex.getMessage();
    }
    assertEquals("Properties were misconfigured: [cyc.session.policyFile=  ]", exMsg);
  }
  
  @Test
  public void testEmptyCycServer() {
    String exMsg = null;
    try {
      final Properties props = new Properties();
      props.put(SessionConfigurationProperties.SERVER_KEY, " ");
      final CycSessionConfiguration config = loadProperties(props);
      assertNull(config.getCycServer());
      assertNull(config.getPolicyFileName());
      assertNull(config.getPolicyName());
    } catch (SessionConfigurationException ex) {
      exMsg = ex.getMessage();
    }
    assertEquals("Properties were misconfigured: [cyc.session.server= ]", exMsg);
  }

  @Test
  public void testBadCycServer() {
    String exMsg = null;
    try {
      final Properties props = new Properties();
      props.put(SessionConfigurationProperties.SERVER_KEY, "locsdfg:3a");
      final CycSessionConfiguration config = loadProperties(props);
      assertNull(config.getCycServer());
      assertNull(config.getPolicyFileName());
      assertNull(config.getPolicyName());
    } catch (SessionConfigurationException ex) {
      exMsg = ex.getMessage();
    }
    assertEquals("Properties were misconfigured: [cyc.session.server=locsdfg:3a]", exMsg);
  }
  
  @Test
  public void testCycServerProperty() throws SessionConfigurationException {
    final Properties props = new Properties();
    props.put(SessionConfigurationProperties.SERVER_KEY, "somehost:3620");
    final CycSessionConfiguration config = loadProperties(props);
    assertNotNull(config.getCycServer());
    assertEquals(new CycServer("somehost", 3620), config.getCycServer());
    assertNull(config.getPolicyFileName());
    assertNull(config.getPolicyName());
  }
  
  @Test
  public void testFileProperties() throws SessionConfigurationException {
    if (EnvironmentConfigurationLoader.isEnvironmentEmpty()) {
      // TODO: some of these variable names could be clarified, but otherwise the test is fine.
      
      clearSystemProperties();
      final CycSessionConfiguration config = envloader.getConfiguration();
      assertNull(config.getCycServer());
      assertNull(config.getPolicyFileName());
      assertNull(config.getPolicyName());
      
      System.setProperty(SessionConfigurationProperties.POLICY_FILE_KEY, PropertiesReaderTest.TEST_PROPERTIES_FILE);
      final EnvironmentConfiguration envconfig2 = envloader.getConfiguration();
      assertNull(envconfig2.getCycServer());
      assertEquals(PropertiesReaderTest.TEST_PROPERTIES_FILE, envconfig2.getPolicyFileName());
      assertNull(envconfig2.getPolicyName());
      
      propLoader.setEnvironment(envconfig2);
      final CycSessionConfiguration config2 = propLoader.getConfiguration();
      assertEquals(new CycServer("testserver", 3640), config2.getCycServer());
      assertNull(config2.getPolicyFileName());
      assertNull(config2.getPolicyName());
    }
  }
  
  
  // Protected
  
  protected CycSessionConfiguration loadProperties(Properties props) throws SessionConfigurationException {
    return this.propLoader.getConfiguration(props);
  }
  
  protected void clearSystemProperties() {
    System.clearProperty(SessionConfigurationProperties.POLICY_FILE_KEY);
  }
  
}
