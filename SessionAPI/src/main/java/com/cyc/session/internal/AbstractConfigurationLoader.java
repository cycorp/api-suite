package com.cyc.session.internal;

/*
 * #%L
 * File: AbstractConfigurationLoader.java
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

import com.cyc.session.SessionConfigurationLoader;
import com.cyc.session.EnvironmentConfiguration;
import com.cyc.session.SessionConfigurationException;

/**
 * Abstract implementation of SessionConfigurationLoader.
 * @author nwinant
 */
abstract public class AbstractConfigurationLoader implements SessionConfigurationLoader {
  
  private EnvironmentConfiguration envConfig;
  
  @Override
  public void setEnvironment(EnvironmentConfiguration config) {
    this.envConfig = config;
  }
  
  protected EnvironmentConfiguration getEnvironment() throws SessionConfigurationException {
    if (envConfig == null) {
      final String thisclass = this.getClass().getSimpleName() ;
      throw new SessionConfigurationException(
              "Environment properties were not set! You must call " 
                      + thisclass + ".setEnvironment(" + EnvironmentConfiguration.class.getSimpleName() + ") first.");
    }
    return this.envConfig;
  }
  
  @Override
  public String toString() {
    return getName() + "=" + this.getClass().getName();
  }
}
