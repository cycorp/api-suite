package com.cyc.nl;

/*
 * #%L
 * File: HtmlLinkType.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2019 Cycorp, Inc
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
 *
 * @author daves
 */
public enum HtmlLinkType {
  
  DEFAULT(":DEFAULT"),
  CYC_BROWSER(":CYC-BROWSER"),
  NONE(":NONE");

  private final String name;

  private HtmlLinkType(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }

  public static HtmlLinkType fromValue(String string) {
    for (HtmlLinkType type : HtmlLinkType.values()) {
      if (string.equals(type.name)) {
        return type;
      }
    }
    return HtmlLinkType.valueOf(string);
  }
}
