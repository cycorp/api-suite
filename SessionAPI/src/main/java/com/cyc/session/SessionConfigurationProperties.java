package com.cyc.session;

/*
 * #%L
 * File: SessionConfigurationProperties.java
 * Project: Session API
 * %%
 * Copyright (C) 2013 - 2014 Cycorp, Inc.
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

/**
 * This defines the canonical set of names for all CycSessionConfiguration properties.
 * Although some implementation of the CycSessionConfiguration interface may not
 * require these keys, they should be used in any implementation backed by
 * {@link java.util.Properties} or any form of map with String-based keys.
 * 
 * @author nwinant
 */
public interface SessionConfigurationProperties {
  
  /**
   * A requested {@link SessionConfigurationLoader}, as either the loader class's canonical name or
   * convenience name (i.e., matching {@link SessionConfigurationLoader#getName()}).
   */
  public static final String POLICY_NAME_KEY = "cyc.session.policyName";
  
  /**
   * The name of a configuration file to be loaded.
   */
  public static final String POLICY_FILE_KEY = "cyc.session.policyFile";
  
  /**
   * The address of Cyc server to be connected to, as a serialized {@link CycServer} object; 
   * e.g. <code>localhost:3600</code>.
   */
  public static final String SERVER_KEY = "cyc.session.server";
  
  /**
   * Returns an array of all property names.
   */
  public static final String[] ALL_KEYS = {
    POLICY_NAME_KEY,
    POLICY_FILE_KEY,
    SERVER_KEY
  };
}
