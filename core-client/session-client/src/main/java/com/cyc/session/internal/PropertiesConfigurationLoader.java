package com.cyc.session.internal;

/*
 * #%L
 * File: PropertiesConfigurationLoader.java
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

import com.cyc.session.exception.SessionConfigurationException;
import com.cyc.session.CycSessionConfiguration;
import com.cyc.session.spi.SessionConfigurationLoader;
import java.util.Properties;

/** 
 * A Properties file-based SessionConfigurationLoader loader.
 * @author nwinant
 */
public class PropertiesConfigurationLoader extends AbstractConfigurationLoader implements SessionConfigurationLoader {
  
  // Fields
  
  public static final String NAME = "properties";
  final private PropertiesReader reader = new PropertiesReader();
  
  
  // Public
  
  @Override
  public String getName() {
    return NAME;
  }
  
  @Override
  public CycSessionConfiguration getConfiguration() throws SessionConfigurationException {
    if (isValuePopulated(getEnvironment().getConfigurationFileName())) {
      final Properties props = reader.fromFile(getEnvironment().getConfigurationFileName());
      return getConfiguration(props);
    }
    return null;
  }

  @Override
  public boolean isCapableOfSuccess() {
    return true;
  }
  
  
  // Protected
    
  protected CycSessionConfiguration getConfiguration(Properties prop) throws SessionConfigurationException {
    return new ImmutableConfiguration(prop, this.getClass());
  }
  
  protected boolean isValuePopulated(String value) {
    return (value != null) && !value.trim().isEmpty();
  }
}
