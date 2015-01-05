package com.cyc.session.internal;

/*
 * #%L
 * File: PropertiesReaderTest.java
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
import com.cyc.session.SessionConfigurationProperties;
import java.util.Properties;
import junit.framework.TestCase;
import org.junit.Test;

/**
 *
 * @author nwinant
 */
public class PropertiesReaderTest extends TestCase {
  
  public PropertiesReaderTest(String testName) {
    super(testName);
  }
  
  @Override
  protected void setUp() throws Exception {
    super.setUp();
  }
  
  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
  }
  
  @Test
  public void testEmptyProperties() throws SessionConfigurationException {
    PropertiesReader reader = new PropertiesReader();
    Properties props = reader.fromFile(TEST_PROPERTIES_FILE);
    for (String key : props.stringPropertyNames()) {
      System.out.println(" - [" + key + "]:[" + props.getProperty(key) + "]");
    }
    assertNotNull(props.getProperty(SessionConfigurationProperties.SERVER_KEY));
  }

  public static final String TEST_PROPERTIES_FILE = "cycsessiontest.properties";
}
