package com.cyc.session.internal;

/*
 * #%L
 * File: SimpleInteractiveLoader.java
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
import com.cyc.session.spi.SessionConfigurationLoader;
import com.cyc.session.CycSessionConfiguration;
import com.cyc.session.exception.SessionConfigurationException;

/**
 * An interactive SessionConfigurationLoader which prompts the user for a CycServer address with
 * the {@link CycServerPanel}.
 * @author nwinant
 */
public class SimpleInteractiveLoader extends AbstractConfigurationLoader implements SessionConfigurationLoader {

  public static final String NAME = "interactive";
  
  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public CycSessionConfiguration getConfiguration() throws SessionConfigurationException {
    if (!getEnvironment().isGuiInteractionAllowed()) {
      throw new SessionConfigurationException(this + " was requested to prompt user for configuration "
              + "information, but is not allowed to: " 
              + getEnvironment().getClass().getSimpleName() + "#isGuiInteractionAllowed() == false.");
    }
    if (!isCapableOfSuccess()) {
      throw new SessionConfigurationException(this + " is not capable of success: in a headless environment.");
    }
    CycServer server = obtainCycServer();
    if (server == null) {
      throw new SessionConfigurationException(this.toString() + " could not successfully obtain a cyc server address.");
    }
    return new ImmutableConfiguration(server, this.getClass());
  }
  
  @Override
  public boolean isCapableOfSuccess() {
    return !EnvironmentConfigurationLoader.isHeadlessEnvironment();
  }
  
  protected CycServer obtainCycServer() {
    return CycServerPanel.promptUser();
  }

}
