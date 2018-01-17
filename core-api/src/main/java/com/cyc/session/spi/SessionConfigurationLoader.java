package com.cyc.session.spi;

/*
 * #%L
 * File: SessionConfigurationLoader.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2017 Cycorp, Inc
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
import com.cyc.session.CycSessionConfiguration;
import com.cyc.session.EnvironmentConfiguration;
import com.cyc.session.exception.SessionConfigurationException;

/**
 * This interface defines a mechanism for loading {@link CycSessionConfiguration}s. Implementations
 * may load configuration from System properties, Properties files, interactive dialogs, etc.
 *
 * @author nwinant
 */
public interface SessionConfigurationLoader {

  /**
   * The convenience name of this implementation, making it easier to specify a particular
   * configuration loader in System properties (or properties files, or whatever.) This name may be
   * almost anything, but should be reasonably descriptive: "interactive," "default-properties,"
   * etc.
   *
   * <p>
   * Names may only consist of alphanumeric character, underscores, and hyphens (i.e.,
   * <code>/^[a-zA-Z0-9-_]+$/</code>). Spaces are now allowed.
   *
   * @return The convenience name of the implementation.
   */
  String getName();

  /**
   * Returns a new CycSessionConfiguration. The resulting CycSession may not be sufficient to create
   * a new CycSession; that's for the SessionManager to determine. This method will throw a
   * SessionConfigurationException if configuration parameters are obviously malformed; however, the
   * configuration loader <em>may</em> allow missing or empty parameters, at its discretion.
   *
   * <p>
   * Note that {@link #setEnvironment(com.cyc.session.EnvironmentConfiguration)} must be called
   * sometime <em>before</em> calling this method.
   *
   * @return a new CycSessionConfiguration
   *
   * @throws SessionConfigurationException if configuration parameters are obviously malformed.
   *
   * However, the configuration loader <em>may</em> allow missing or empty parameters at its
   * discretion.
   */
  CycSessionConfiguration getConfiguration() throws SessionConfigurationException;

  /**
   * Passes an EnvironmentConfiguration to the configuration loader. Parameters in the
   * EnvironmentConfiguration may instruct the loader on what to do (or what not to do.)
   *
   * @param config
   */
  void setEnvironment(EnvironmentConfiguration config);

  /**
   * Whether the SessionConfigurationLoader believes it has a chance of success. This is only a
   * check for obvious failure modes; for example, a GUI-based loader would return
   * <code>false</code> when being executed in a headless environment.
   *
   * @return Whether the SessionConfigurationLoader believes it has a chance of success.
   */
  boolean isCapableOfSuccess();
}
