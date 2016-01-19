package com.cyc.baseclient;

/*
 * #%L
 * File: BuildInfo.java
 * Project: Base Client
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

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 *
 * @author nwinant
 */
abstract public class BuildInfo extends Properties {
  
  // Fields
  
  public static final String ARTIFACT_ID_KEY = "artifactId";
  public static final String GROUP_ID_KEY = "groupId";
  public static final String VERSION_KEY = "version";
  public static final String NAME_KEY = "name";
  public static final String TIME_STAMP_KEY = "timeStamp";
  public static final String SCM_REVISION_KEY = "scmRevision";
  public static final List<String> ALL_BUILD_KEYS = Arrays.asList(
          GROUP_ID_KEY,
          ARTIFACT_ID_KEY, 
          VERSION_KEY, 
          NAME_KEY, 
          TIME_STAMP_KEY,
          SCM_REVISION_KEY);
  
  
  // Constructors
  
  public BuildInfo() throws IOException {
    final InputStream is = getClass().getClassLoader().getResourceAsStream(getResourcePath());
    if (is == null) {
      throw new IOException("Could not load resource [" + getResourcePath() + "]");
    }
    load(is);
  }
  
  
  // Public
  
  public String getArtifactId() { return this.getProperty(ARTIFACT_ID_KEY); }
  
  public String getGroupId() { return this.getProperty(GROUP_ID_KEY); }
  
  public String getVersion() { return this.getProperty(VERSION_KEY); }
  
  public String getName() { return this.getProperty(NAME_KEY); }
  
  public String getTimeStamp() { return this.getProperty(TIME_STAMP_KEY); }
  
  public String getScmRevision() { return this.getProperty(SCM_REVISION_KEY); }
  
  public String getResourcePath() {
    return getClass().getPackage().getName().replace(".", System.getProperty("file.separator"))
            + "/BuildInfo.properties";
  }
  
  public void printAll(PrintStream out) {
    final Set<String> allKeys = new HashSet(stringPropertyNames());
    allKeys.addAll(ALL_BUILD_KEYS);
    final List<String> otherKeys = new ArrayList();
    int maxKeyLength = 0;
    for (String key : allKeys) {
      if (key.length() > maxKeyLength) {
        maxKeyLength = key.length();
      }
      if (!ALL_BUILD_KEYS.contains(key)) {
        otherKeys.add(key);
      }
    }
    Collections.sort(otherKeys);
    
    final String header = "==  " + this.getClass().getSimpleName() + "  ===================================================";
    out.println(header);
    out.println("Resource Path: " + getResourcePath());
    
    out.println("Build properties:");
    for (String key : ALL_BUILD_KEYS) {
      printValue(key, maxKeyLength, out);
    }
    out.println("Other properties:");
    for (String key : otherKeys) {
      printValue(key, maxKeyLength, out);
    }
    if (otherKeys.isEmpty()) {
      out.println("  - none -");
    }
    
    out.println(String.format("%" + header.length() + "s", "").replace(' ', '='));
    out.println();
  }
  
  public void printAll() {
    printAll(System.out);
  }
  
  
  // Private
  
  private void printValue(String key, int maxKeyLength, PrintStream out) {
    int length = maxKeyLength + 2;
    out.println("  " + String.format("%1$-" + length + "s", key + ": ") + getProperty(key));
  }
}
