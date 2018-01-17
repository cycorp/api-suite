package com.cyc.session;

/*
 * #%L
 * File: SessionManagerConfigurationProperties.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2018 Cycorp, Inc
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

/**
 * This defines the canonical set of names for all {@link SessionManagerConfiguration} properties.
 * Although some implementations of the SessionManagerConfiguration interface may not require these 
 * keys, they should be used in any implementation backed by {@link java.util.Properties} or any
 * form of map with String-based keys.
 * 
 * @author nwinant
 */
public interface SessionManagerConfigurationProperties {
  
  /**
   * Does this configuration allow for the end-user to be prompted for configuration properties via
   * GUI elements in windowed environments?
   * 
   * @see SessionManagerConfiguration#isGuiInteractionAllowed()
   * @see #GUI_INTERACTION_ALLOWED_DEFAULT_VALUE
   */
  public static final String GUI_INTERACTION_ALLOWED_KEY
          = "cyc.session.guiInteractionAllowed";
  
  public static final boolean GUI_INTERACTION_ALLOWED_DEFAULT_VALUE = true;
  
  /**
   * Whether the APIs permitted to apply code patches to the Cyc server.
   *
   * @see SessionManagerConfiguration#isServerPatchingAllowed() 
   * @see #SERVER_PATCHING_ALLOWED_DEFAULT_VALUE
   */
  public static final String SERVER_PATCHING_ALLOWED_KEY 
          = "cyc.session.server.patchingAllowed";
  
  public static final boolean SERVER_PATCHING_ALLOWED_DEFAULT_VALUE = false;
  
  /**
   * When all sessions are closed, should any underlying connections be released?
   * 
   * @see SessionManagerConfiguration#isServerReleasedWhenAllSessionsAreClosed() 
   * @see #SERVER_RELEASED_WHEN_ALL_SESSIONS_CLOSED_DEFAULT_VALUE;
   */
  public static final String SERVER_RELEASED_WHEN_ALL_SESSIONS_CLOSED_KEY
          = "cyc.session.server.releaseWhenAllSessionsAreClosed";
  
  public static final boolean SERVER_RELEASED_WHEN_ALL_SESSIONS_CLOSED_DEFAULT_VALUE = false;
  
  /**
   * Can CycSessions be automatically created as necessary?
   * 
   * @see SessionManagerConfiguration#isSessionAutoCreationAllowed() 
   * @see #SESSION_AUTO_CREATION_ALLOWED_DEFAULT_VALUE
   */
  public static final String SESSION_AUTO_CREATION_ALLOWED_KEY
          = "cyc.session.sessionAutoCreationAllowed";
  
  public static final boolean SESSION_AUTO_CREATION_ALLOWED_DEFAULT_VALUE = true;
  
  /**
   * Returns an array of all property names.
   */
  public static final String[] ALL_KEYS = {
    GUI_INTERACTION_ALLOWED_KEY,
    SERVER_PATCHING_ALLOWED_KEY,
    SERVER_RELEASED_WHEN_ALL_SESSIONS_CLOSED_KEY,
    SESSION_AUTO_CREATION_ALLOWED_KEY
  };
  
}
