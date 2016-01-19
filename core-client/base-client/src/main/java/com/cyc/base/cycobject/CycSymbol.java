package com.cyc.base.cycobject;

/*
 * #%L
 * File: CycSymbol.java
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

import java.io.Serializable;

/**
 *
 * @author nwinant
 */
public interface CycSymbol extends CycObject, Serializable {

  public String getPackageName();

  public String getPackageNamePrecise();

  public String getSymbolName();

  public String getSymbolNamePrecise();

  /**
   * Returns <tt>true</tt> iff this symbol is a SubL keyword.
   *
   * @return <tt>true</tt> iff this symbol is a SubL keyword
   */
  public boolean isKeyword();

  /**
   * Returns <tt>true</tt> iff this symbol should be quoted.
   *
   * @return <tt>true</tt> iff this symbol should be quoted.
   */
  public boolean shouldQuote();

  public boolean toBoolean();

  public String toCanonicalString();

  public String toFullString();

  public String toFullString(String relativePackageName);

  public String toFullStringForced();
}
