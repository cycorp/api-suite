package com.cyc.session.internal;

/*
 * #%L
 * File: PropertiesReader.java
 * Project: Session Client
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

import com.cyc.session.exception.SessionConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 * A simple utilities class that provides a few basic ways to read properties.
 * @author nwinant
 */
public class PropertiesReader {
  
  public Properties fromSystem() {
    Properties sysProps = System.getProperties();
    return filterProperties(sysProps);
  }
  /*
  public Properties fromArgs(String[] args) {
    Properties results = new Properties();
    for (String arg : args) {
      final String[] tokens = arg.split("=");
      final String key = tokens[0];
      final String value = tokens[1];
      if ((tokens.length == 2)
              && isValidProperty(key, value)) {
        results.put(key, value);
      }
    }
    return results;
  }
  */
  public Properties fromFile(String filename) throws SessionConfigurationException {
    Properties props = new Properties();
    InputStream input = null;
    IOException tmpIOException = null;
    try {
      input = getClass().getClassLoader().getResourceAsStream(filename);
      props.load(input);
    } catch (IOException io) {
      tmpIOException = io;
    } finally {
      if (input != null) {
        try {
          input.close();
        } catch (IOException io) {
          if (tmpIOException == null) {
            tmpIOException = io;
          }
        }
      }
      if (tmpIOException != null) {
        throw new SessionConfigurationException("Error when attempting to read file " + filename, tmpIOException);
      }
    }
    return filterProperties(props);
  }
  
  public Properties filterProperties(Properties source) {
    final Properties results = new Properties();
    final Enumeration<?> e = source.propertyNames();
    while (e.hasMoreElements()) {
      final String key = (String) e.nextElement();
      final String value = source.getProperty(key);
      if (isValidProperty(key, value)) {
        results.put(key, value);
      }
    }
    return results;
  }
  
  
  // Protected
          
  protected boolean isValidKey(String key) {
    return key != null;
  }
  
  protected boolean isValidProperty(String key, String value) {
    return isValidKey(key);
  }
}
