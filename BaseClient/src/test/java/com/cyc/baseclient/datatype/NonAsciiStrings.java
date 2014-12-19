/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cyc.baseclient.datatype;

/*
 * #%L
 * File: NonAsciiStrings.java
 * Project: Base Client
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
import java.io.InputStream;
import java.util.Properties;
import org.junit.Assert;

/**
 * Helper class to serve up non-ASCII strings for tests.
 * Strings are stored in nonascii.properties.
 *
 * @author baxter
 */
public final class NonAsciiStrings {

  private final Properties properties = new Properties();

  private static final NonAsciiStrings INSTANCE = new NonAsciiStrings();

  private NonAsciiStrings() {
    try {
      final String resourceName = getClass().getPackage().getName().replace(".", System.getProperty("file.separator")) + "/nonascii.properties";
      final InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(resourceName);
      properties.load(resourceAsStream);
    } catch (Exception ex) {
      ex.printStackTrace(System.err);
      Assert.fail("Got exception: " + ex.getLocalizedMessage());
    }
  }

  public static String get(final String key) {
    return INSTANCE.properties.getProperty(key);
  }

}
