package com.cyc.session.internal;

/*
 * #%L
 * File: SimpleInteractiveLoaderTest.java
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

import com.cyc.session.*;
import junit.framework.TestCase;
import org.junit.Test;

/**
 *
 * @author nwinant
 */
public class SimpleInteractiveLoaderTest extends TestCase {
  
  private EnvironmentConfigurationLoader envloader;
  
  public SimpleInteractiveLoaderTest(String testName) {
    super(testName);
  }
  
  @Override
  protected void setUp() throws Exception {
    this.envloader = new EnvironmentConfigurationLoader();
    super.setUp();
  }
  
  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
  }
  
  @Test
  public void testEmptyProperties() throws SessionConfigurationException {
    if (EnvironmentConfigurationLoader.isHeadlessEnvironment()) {
      // TODO: is there a better way of handling this?
      String msg = "Test cannot succeed, as it is being executed in a headless environment.";
      System.out.println(msg);
      assertTrue(true);
    } else {
      SimpleInteractiveLoader loader = new SimpleInteractiveLoader();
      loader.setEnvironment(envloader.getConfiguration());
      CycSessionConfiguration config = loader.getConfiguration();
      assertNotNull(config.getCycServer());
      assertNull(config.getPolicyFileName());
      assertNull(config.getPolicyName());
    }
  }
  
}
