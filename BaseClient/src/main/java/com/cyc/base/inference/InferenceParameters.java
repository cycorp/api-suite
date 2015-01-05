package com.cyc.base.inference;

/*
 * #%L
 * File: InferenceParameters.java
 * Project: Base Client
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

//// Internal Imports
import com.cyc.base.cycobject.CycSymbol;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

/**
 * <P>
 * InferenceParameters is designed to encode the parameters that can be
 * specified for running a Cyc query.
 *
 * @author zelal
 * @date August 14, 2005, 2:41 PM
 * @version $Id: InferenceParameters.java 155483 2014-12-10 21:56:51Z nwinant $
 */
public interface InferenceParameters extends Cloneable, InferenceParameterGetter, 
        InferenceParameterSetter {

  void clear();

  Object parameterValueCycListApiValue(final CycSymbol key, final Object val);

  Set<Entry<CycSymbol, Object>> entrySet();

  Set<CycSymbol> keySet();

  void putAll(InferenceParameters properties);

  void remove(CycSymbol property);

  Object put(CycSymbol parameterName, Object value);

  Object get(CycSymbol parameterName);

  boolean containsKey(CycSymbol key);

  String stringApiValue();

  Object cycListApiValue();

  Object clone();

  /**
   * Update from a plist of the type used by Cyc's inference engine.
   *
   * @param plist
   */
  public void updateFromPlist(List plist);

  public void setProblemStorePath(String path);

  /**
   * Specify the problem store in which to run the query. *
   */
  public void setProblemStoreId(int id);

  public boolean usesLoadedProblemStore();

  /**
   * Adjust these parameters to give the inference engine at least as extensive
   * resources as newParams.
   *
   * @param newParams
   */
  public void makeAtLeastAsLooseAs(InferenceParameters newParams);
}
