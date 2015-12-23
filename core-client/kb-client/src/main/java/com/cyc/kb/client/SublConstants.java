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
package com.cyc.kb.client;

/*
 * #%L
 * File: SublConstants.java
 * Project: KB Client
 * %%
 * Copyright (C) 2013 - 2015 Cycorp, Inc
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

import com.cyc.baseclient.subl.subtypes.BasicSublFunction;
import com.cyc.baseclient.subl.subtypes.SublBooleanSingleArgFunction;
import com.cyc.baseclient.subl.subtypes.SublCycListSingleArgFunction;
import com.cyc.baseclient.subl.subtypes.SublCycObjectSingleArgFunction;
import com.cyc.kb.exception.KbException;
import com.cyc.kb.exception.KbRuntimeException;

/**
 *
 * @author vijay
 */
public class SublConstants {

  private static SublConstants instance;
  public final BasicSublFunction findAssertionCycl = new BasicSublFunction("FIND-ASSERTION-CYCL");
  public final SublCycListSingleArgFunction assertionAssertedAssertionSupports = new SublCycListSingleArgFunction("assertion-asserted-assertion-supports");
  public final BasicSublFunction allGenls = new BasicSublFunction("all-genls");
  public final BasicSublFunction quickQuietHasTypeQ = new BasicSublFunction("quick-quiet-has-type?");
  public final BasicSublFunction allSpecs = new BasicSublFunction("all-specs");
  public final BasicSublFunction removeDuplicates = new BasicSublFunction("remove-duplicates");
  public final BasicSublFunction withAllMts = new BasicSublFunction("with-all-mts");
  public final BasicSublFunction withInferenceMtRelevance = new BasicSublFunction("WITH-INFERENCE-MT-RELEVANCE");
  public final SublCycObjectSingleArgFunction assertionDirection = new SublCycObjectSingleArgFunction("assertion-direction");
  public final SublBooleanSingleArgFunction assertedAssertionQ = new SublBooleanSingleArgFunction("asserted-assertion?");
  public final SublCycObjectSingleArgFunction keChangeAssertionDirection = new SublCycObjectSingleArgFunction("ke-change-assertion-direction");
  public final SublBooleanSingleArgFunction deducedAssertionQ = new SublBooleanSingleArgFunction("deduced-assertion?");
  public final BasicSublFunction minCol = new BasicSublFunction("min-col");
  public final BasicSublFunction minArity = new BasicSublFunction("min-arity");
  public final BasicSublFunction maxArity = new BasicSublFunction("max-arity");
  public final BasicSublFunction gatherGafArgIndex = new BasicSublFunction("GATHER-GAF-ARG-INDEX");
  
  private SublConstants() throws KbException {
    super();
  }

  /**
   * This not part of the public, supported KB API
   * @throws KbRuntimeException
   */
  public static SublConstants getInstance() throws KbRuntimeException {
    try {
      if (instance == null) {
        instance = new SublConstants();
      }
      return instance;
    } catch (KbException e) {
      throw new KbRuntimeException(
          "One of the private final fields in com.cyc.kb.Constants could not be instantiated, can not proceed further.",
          e);
    }
  }  
}
