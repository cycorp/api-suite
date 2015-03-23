/*
 * Copyright 2015 Cycorp, Inc..
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

package com.cyc.session.internal;

/*
 * #%L
 * File: TestEnvironmentProperties.java
 * Project: Session API Implementation
 * %%
 * Copyright (C) 2013 - 2015 Cycorp, Inc.
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
 * Retrieves various test-scope configuration settings from the System properties.
 * These properties may be set within the maven-surefire-plugin configuration in
 * the project's pom.xml:
 * 
 * <build>
 *   <plugins>
 *     <plugin>
 *       <groupId>org.apache.maven.plugins</groupId>
 *       <artifactId>maven-surefire-plugin</artifactId>
 *       <configuration>
 *         <systemProperties>
 *           <property>
 *             <name>cyc.test.includeGuiElementTests</name>
 *             <value>false</value>
 *           </property>
 *           ...
 *         </systemProperties>
 *       </configuration>
 *     </plugin>
 *     ...
 *   </plugins>
 * </build>
 * 
 * @author nwinant
 */
public class TestEnvironmentProperties {
  
  // Fields
  
  public static final String INCLUDE_GUI_ELEMENT_TESTS_PROPERTY = "cyc.test.includeGuiElementTests";
  public static final String CONNECTION_FACTORY_EXPECTED_ON_CLASSPATH_PROPERTY = "cyc.test.connectionFactoryExpectedOnClassPath";
  public static final String EXPECTED_NUMBER_OF_CYC_OBJECT_LIBRARIES_PROPERTY = "cyc.test.expectedNumberOfCycObjectLibraries";
  
  private static TestEnvironmentProperties ME;
  
  
  // Constructors
  
  private TestEnvironmentProperties() {
    // TODO: read values from a Java .properties file, if present.
  }
  
  public static synchronized TestEnvironmentProperties get() {
    if (ME == null) {
      ME = new TestEnvironmentProperties();
    }
    return ME;
  }
  
  
  // Public
  
  public boolean isTestingGuiElements() {
    return getBooleanProperty(INCLUDE_GUI_ELEMENT_TESTS_PROPERTY, false);
  }
  
  public boolean isConnectionFactoryExpectedOnClassPath() {
    return getBooleanProperty(CONNECTION_FACTORY_EXPECTED_ON_CLASSPATH_PROPERTY, true);
  }
  
  public int getExpectedNumberOfCycObjectLibraries() {
    return getIntegerProperty(EXPECTED_NUMBER_OF_CYC_OBJECT_LIBRARIES_PROPERTY, 0);
  }
  
  
  // Private
  
  private boolean getBooleanProperty(String name, boolean defaultValue) {
    final String val = System.getProperty(name);
    if ((val == null) || val.trim().isEmpty()) {
      return defaultValue;
    }
    return Boolean.parseBoolean(val);
  }
  
  private int getIntegerProperty(String name, int defaultValue) {
    final String val = System.getProperty(name);
    if ((val == null) || val.trim().isEmpty()) {
      return defaultValue;
    }
    return Integer.parseInt(val);
  }
}
