package com.cyc.session;

/*
 * #%L
 * File: CycSessionConfiguration.java
 * Project: Core API Specification
 * %%
 * Copyright (C) 2013 - 2015 Cycorp, Inc
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
 * This interface defines the set of configuration parameters used by the Session API to establish
 * a new {@link CycSession}.
 * 
 * @author nwinant
 */
public interface CycSessionConfiguration {
  
  /**
   * Returns the address of the Cyc server to be connected to.
   * @return a CycServer address.
   */
  CycServer getCycServer();
  
  /**
   * Returns the name of a requested SessionConfigurationLoader to be used to load a 
   * CycSessionConfiguration. In this way, one could specifically ask for a configuration to be 
   * loaded by, say, interactively prompting the user.
   * 
   * A SessionConfigurationLoader's name may be determined via 
   * {@link SessionConfigurationLoader#getName()}.
   * 
   * @return the name of the requested SessionConfigurationLoader.
   */
  String getPolicyName();
  
  /**
   * Returns the name of a file from which to load configuration information.
   * @return the configuration file name.
   */
  String getPolicyFileName();
  
  /**
   * Does this configuration allow for the end-user to be prompted for configuration properties via
   * GUI elements?
   * 
   * If so, <em>and</em> if no other sufficient configuration has specified, <em>and</em> if the
   * application is not running in a headless environment, the Session API may prompt the end-user
   * for configuration information.
   * 
   * @return is system allowed to prompt end-user for configuration properties via GUI elements?
   */
  boolean isGuiInteractionAllowed();
  
  /**
   * Is the SessionManager permitted to cache configurations? This is useful
   * when loading configurations is expensive or annoying. If this option is
   * set to false, the SessionManager will attempt to load a configuration for
   * each and every session it creates, which can be fantastically annoying if
   * it is doing so by interactively prompting the user.
   * @return can configurations be cached?
   */
  boolean isConfigurationCachingAllowed();
  
  /**
   * Is the SessionManager permitted to cache sessions?
   * @return can sessions be cached?
   */
  boolean isSessionCachingAllowed();
  
  /**
   * Does this configuration share the same field values with another? Weaker
   * than #equals, as it ignores things like Class and loader Class.
   * @param configuration
   * @return are these two configurations equivalent
   */
  boolean isEquivalent(CycSessionConfiguration configuration);
  
  /**
   * Returns the class of the SessionConfigurationLoader implementation used to instantiate and
   * populate this instance of CycSessionConfiguration.
   * @return the Class of the SessionConfigurationLoader implementation.
   */
  Class getLoaderClass();
  
  /**
   * Returns a copy of the original Properties object used to initialize the CycSessionConfiguration
   * implementation.
   * 
   * <p>If the implementing class was initialized with a Properties object, this method will
   * return an exact copy of that Properties object. This may mean that it will contain properties
   * which are not used by the Session API; for example, in the case of a CycSessionConfiguration 
   * initialized by a call to {@link System#getProperties()}.
   * 
   * <p>If the implementing class was <em>not</em> initialized by a Properties object, it is up to 
   * the implementation to determine what to return; see the implemention's javadoc. 
   * 
   * <p>Regardless of implementation, this method should <em>never</em> return null; it should 
   * always return, at least, an empty Properties object. Each call to this method will return a
   * fresh copy of the Properties; changes to the Properties will <em>not</em> be persisted.
   * Likewise, changing values in the resulting Properties object <em>will not change</em> the 
   * values returned by any methods on the CycSessionConfiguration object.
   * 
   * @return a non-null Properties object.
   */
  Properties getRawProperties();
}
