package com.cyc.base.inference;

/*
 * #%L
 * File: InferenceAnswerLanguage.java
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

import com.cyc.base.cycobject.CycSymbol;
import com.cyc.baseclient.CycObjectFactory;

/**
 * The CycL language used to express answers.
 *
 * @author baxter
 */
public enum InferenceAnswerLanguage implements InferenceParameterValue {

  /**
   * Epistemological Level. The way knowledge is expressed when Cyc communicates
   * with users or external programs.
   */
  EL,
  /**
   * Heuristic Level. The way knowledge is actually stored, and inference
   * implemented, in Cyc.
   */
  HL;

  @Override
  public String stringApiValue() {
    return getCycSymbol().stringApiValue();
  }

  @Override
  public Object cycListApiValue() {
    return getCycSymbol().cycListApiValue();
  }

  public CycSymbol getCycSymbol() {
    return CycObjectFactory.makeCycSymbol(":" + name());
  }

}
