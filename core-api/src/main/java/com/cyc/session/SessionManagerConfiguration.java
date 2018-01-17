package com.cyc.session;

/*
 * #%L
 * File: SessionManagerConfiguration.java
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

/*
 * Copyright 2017 Cycorp, Inc.
 *
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
 */

import com.cyc.session.SessionOptions.DefaultSessionOptions;
import java.util.Properties;

/**
 * Configuration parameters for a {@link SessionManager}. Any given CycSession will be configured by
 * a {@link CycSessionConfiguration}, whereas a SessionManagerConfiguration configures the
 * SessionManager which produces and manages CycSessions.
 * <p>
 * SessionManagerConfiguration implementations should be immutable, or at least unmodifiable, and a
 * SessionManager should receive exactly one instance, during its instantiation.
 * 
 * @see SessionManagerConfigurationProperties
 * 
 * @author nwinant
 */
public interface SessionManagerConfiguration {

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
   * Does this configuration allow for the end-user to be prompted for configuration properties via
   * GUI elements in windowed environments?
   *
   * If so, <em>and</em> if no other sufficient configuration has specified, <em>and</em> if the
   * application is not running in a headless environment, the Session API may prompt the end-user
   * for configuration information.
   *
   * @return is system allowed to prompt end-user for configuration properties via GUI elements?
   * 
   * @see SessionManagerConfigurationProperties#GUI_INTERACTION_ALLOWED_KEY
   * @see SessionManagerConfigurationProperties#GUI_INTERACTION_ALLOWED_DEFAULT_VALUE
   */
  boolean isGuiInteractionAllowed();

  /**
   * Are the APIs permitted to apply code patches to the Cyc server? Note that this setting only
   * determines whether the rules of the <em>CycSession</em> will allow code patches; patches may
   * still be prohibited by the Cyc server or the API implementation, regardless of this setting.
   * Defaults to false.
   * 
   * @see SessionManagerConfigurationProperties#SERVER_PATCHING_ALLOWED_KEY
   * @see SessionManagerConfigurationProperties#SERVER_PATCHING_ALLOWED_DEFAULT_VALUE
   *
   * @return can code patches be applied to the Cyc server?
   */
  boolean isServerPatchingAllowed();
  
  /**
   * When all sessions are closed, should any underlying connections be released? 
   * 
   * @return should connections be released when all sessions are closed?
   * 
   * @see SessionManagerConfigurationProperties#SERVER_RELEASED_WHEN_ALL_SESSIONS_CLOSED_KEY
   * @see SessionManagerConfigurationProperties#SERVER_RELEASED_WHEN_ALL_SESSIONS_CLOSED_DEFAULT_VALUE
   */
  boolean isServerReleasedWhenAllSessionsAreClosed();
  
  /**
   * Can CycSessions be automatically created as necessary?
   * 
   * @return whether CycSessions be automatically created as necessary?
   * 
   * @see SessionManagerConfigurationProperties#SESSION_AUTO_CREATION_ALLOWED_KEY
   * @see SessionManagerConfigurationProperties#SESSION_AUTO_CREATION_ALLOWED_DEFAULT_VALUE
   */
  boolean isSessionAutoCreationAllowed();
  
  /**
   * Is the SessionManager permitted to cache sessions?
   *
   * @return can sessions be cached?
   */
  boolean isSessionCachingAllowed();
  
  /**
   * Returns an immutable set of options, which may provide the default values for 
   * {@link CycSession#getOptions() }.
   *
   * @return An immutable set of default options
   */
  DefaultSessionOptions getDefaultSessionOptions();
  
  /**
   * Returns a copy of the original Properties object used to initialize the 
   * SessionManagerConfiguration implementation.
   *
   * <p>
   * If the implementing class was initialized with a Properties object, this method will return an
   * exact copy of that Properties object. This may mean that it will contain properties which are
   * not used by the Session API; for example, in the case of a SessionManagerConfiguration
   * initialized by a call to {@link System#getProperties()}.
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
   * methods on the SessionManagerConfiguration object.
   *
   * @return a non-null Properties object.
   */
  Properties getRawProperties();
  
}
