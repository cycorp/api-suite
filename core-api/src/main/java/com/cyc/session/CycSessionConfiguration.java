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
import com.cyc.kb.DefaultContext;
import java.util.Properties;

/**
 * This interface defines the set of configuration parameters used by the Session API to establish a
 * new {@link CycSession}.
 *
 * @author nwinant
 */
public interface CycSessionConfiguration {

  /**
   * Returns the address of the Cyc server to be connected to.
   *
   * @see SessionConfigurationProperties#SERVER_KEY
   *
   * @return a CycAddress address.
   */
  CycAddress getCycAddress();

  /**
   * Returns the name of a requested SessionConfigurationLoader to be used to load a
   * CycSessionConfiguration. In this way, one could specifically ask for a configuration to be
   * loaded by, say, interactively prompting the user.
   *
   * A SessionConfigurationLoader's name may be determined via
   * {@link SessionConfigurationLoader#getName()}.
   *
   * @see SessionConfigurationProperties#CONFIGURATION_LOADER_KEY
   *
   * @return the name of the requested SessionConfigurationLoader.
   */
  String getConfigurationLoaderName();

  /**
   * Returns the name of a file from which to load configuration information.
   *
   * @see SessionConfigurationProperties#CONFIGURATION_FILE_KEY
   *
   * @return the configuration file name.
   */
  String getConfigurationFileName();

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
   * Is the SessionManager permitted to cache configurations? This is useful when loading
   * configurations is expensive or annoying. If this option is set to false, the SessionManager
   * will attempt to load a configuration for each and every session it creates, which can be
   * fantastically annoying if it is doing so by interactively prompting the user.
   *
   * @return can configurations be cached?
   */
  boolean isConfigurationCachingAllowed();

  /**
   * Is the SessionManager permitted to cache sessions?
   *
   * @return can sessions be cached?
   */
  boolean isSessionCachingAllowed();

  // TODO: add javadocs
  boolean isServerReleasedWhenAllSessionsAreClosed();

  /**
   * Are the APIs permitted to apply code patches to the Cyc server? Note that this setting only
   * determines whether the rules of the <em>CycSession</em> will allow code patches; patches may
   * still be prohibited by the Cyc server or the API implementation, regardless of this setting.
   * Defaults to false.
   *
   * @see SessionConfigurationProperties#SERVER_PATCHING_ALLOWED_KEY
   *
   * @return can code patches be applied to the Cyc server?
   */
  boolean isServerPatchingAllowed();

  /**
   * Returns an immutable set of options, which may provide the default values for 
   * {@link CycSession#getOptions() }.
   *
   * @return An immutable set of default options
   */
  DefaultSessionOptions getDefaultSessionOptions();

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

  //====|    DefaultSessionOptions    |===========================================================//
  
  /**
   * An <b>immutable</b> set of options, including the name of the cyclist making assertions, and
   * the project's KE purpose. This provides the defaults for the mutable SessionOptions interface.
   *
   * @author nwinant
   */
  public interface DefaultSessionOptions {

    /**
     * Returns the value of the Cyclist. If it has not been set, return null.
     *
     * @return the value of the Cyclist
     */
    String getCyclistName();

    /**
     * Returns the value of the project (KE purpose).
     *
     * @return the value of the project (KE purpose)
     */
    String getKePurposeName();

    /**
     * Will actions in the current thread that modify the KB be transcripted by the Cyc server?
     *
     * @return will KB operations from the current thread be transcripted?
     */
    boolean getShouldTranscriptOperations();

    /**
     * Returns the current default contexts
     *
     * @return the contents of the DefaultContest ThreadLocal
     */
    DefaultContext getDefaultContext();
  }
  
}
