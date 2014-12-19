package com.cyc.kb.config;

/*
 * #%L
 * File: KBAPIBuildInfo.java
 * Project: KB API
 * %%
 * Copyright (C) 2013 - 2014 Cycorp, Inc
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

import com.cyc.baseclient.BuildInfo;
import java.io.IOException;

/**
 *
 * @author nwinant
 */
public class KBAPIBuildInfo extends BuildInfo {
  
  public KBAPIBuildInfo() throws IOException {
    super();
  }
  
  
  // Main
  
  public static void main(String[] args) {
    try {
      final KBAPIBuildInfo info = new KBAPIBuildInfo();
      info.printAll();
    } catch (Exception ex) {
      ex.printStackTrace();
      System.exit(1);
    } finally {
      System.exit(0);
    }
  }
}
