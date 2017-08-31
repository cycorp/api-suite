/*
 * Copyright 2015 Cycorp, Inc.
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

package com.cyc.session;

/*
 * #%L
 * File: CycServerReleaseType.java
 * Project: Core API Object Specification
 * %%
 * Copyright (C) 2013 - 2017 Cycorp, Inc
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
 * Enum representing Cyc server types. E.g., OpenCyc, ResearchCyc, EnterpriseCyc, etc.
 * 
 * @author nwinant
 */
public enum CycServerReleaseType {

  OPENCYC("OpenCyc", "OCyc"),
  RESEARCHCYC("ResearchCyc", "RCyc"),
  ENTERPRISECYC("EnterpriseCyc", "ECyc"),
  MAINT("Cyc-Maint", "Maint"),
  OTHER(null, null);

  private final String name;
  private final String abbreviation;

  private CycServerReleaseType(String name, String abbreviation) {
    this.name = name;
    this.abbreviation = abbreviation;
  }

  public String getName() {
    return this.name;
  }

  public String getAbbreviation() {
    return this.abbreviation;
  }

  /**
   * Returns the CycServerReleaseType constant matching the specified String. This method is more
   * liberal than {@link com.cyc.session.CycServerReleaseType#valueOf(java.lang.String) } in that it
   * will match on either <em>name</em> or <em>abbreviation</em>, and is case-insensitive. (Like
   * CycServerReleaseType#valueOf(java.lang.String), extraneous whitespace characters are not
   * permitted.)
   *
   * @param string
   * @return string
   */
  public static CycServerReleaseType fromString(String string) {
    for (final CycServerReleaseType type : values()) {
      if (!OTHER.equals(type)) {
        if (type.getName().equalsIgnoreCase(string) 
                || type.getAbbreviation().equalsIgnoreCase(string)) {
          return type;
        }
      }
    }
    return OTHER;
  }
}
