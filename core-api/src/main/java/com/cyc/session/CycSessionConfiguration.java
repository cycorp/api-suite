package com.cyc.session;

/*
 * #%L
 * File: CycSessionConfiguration.java
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
import java.util.Properties;

/**
 * Configuration parameters used by the Session API to establish a new {@link CycSession}. 
 * Similarly, a {@link SessionManagerConfiguration} configures the {@link SessionManager} which
 * produces and manages CycSessions.
 * <p>
 * CycSessionConfiguration implementations should be immutable, or at least unmodifiable, and a
 * CycSession should receive exactly one instance, during its creation.
 * 
 * @see CycSessionConfigurationProperties
 * 
 * @author nwinant
 */
public interface CycSessionConfiguration {

  /**
   * Returns the address of the Cyc server to be connected to.
   *
   * @see CycSessionConfigurationProperties#SERVER_KEY
   *
   * @return a CycAddress address.
   */
  CycAddress getCycAddress();

  /**
   * Returns the name of a requested SessionConfigurationLoader to be used to load a
   * CycSessionConfiguration. In this way, one could specifically ask for a configuration to be
   * loaded by, say, interactively prompting the user.
   *
   * @see CycSessionConfigurationProperties#CONFIGURATION_LOADER_KEY
   *
   * @return the name of the requested SessionConfigurationLoader.
   */
  String getConfigurationLoaderName();

  /**
   * Returns the name of a file from which to load configuration information.
   *
   * @see CycSessionConfigurationProperties#CONFIGURATION_FILE_KEY
   *
   * @return the configuration file name.
   */
  String getConfigurationFileName();
  
  /**
   * Does this configuration share the same field values with another? Weaker than #equals, as it
   * ignores things like Class and loader Class.
   *
   * @param configuration
   *
   * @return are these two configurations equivalent
   */
  boolean isEquivalent(CycSessionConfiguration configuration);

  /**
   * Returns the class of the SessionConfigurationLoader implementation used to instantiate and
   * populate this instance of CycSessionConfiguration.
   *
   * @return the Class of the SessionConfigurationLoader implementation.
   */
  Class getLoaderClass();

  /**
   * Returns a copy of the original Properties object used to initialize the CycSessionConfiguration
   * implementation.
   *
   * <p>
   * If the implementing class was initialized with a Properties object, this method will return an
   * exact copy of that Properties object. This may mean that it will contain properties which are
   * not used by the Session API; for example, in the case of a CycSessionConfiguration initialized
   * by a call to {@link System#getProperties()}.
   *
   * <p>
   * If the implementing class was <em>not</em> initialized by a Properties object, it is up to the
   * implementation to determine what to return; see the implementation's javadoc.
   *
   * <p>
   * Regardless of implementation, this method should <em>never</em> return null; it should always
   * return, at least, an empty Properties object. Each call to this method will return a fresh copy
   * of the Properties; changes to the Properties will <em>not</em> be persisted. Likewise, changing
   * values in the resulting Properties object <em>will not change</em> the values returned by any
   * methods on the CycSessionConfiguration object.
   *
   * @return a non-null Properties object.
   */
  Properties getRawProperties();
  
}
