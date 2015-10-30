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
 * File: SubLConstants.java
 * Project: KB API Implementation
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

import com.cyc.baseclient.subl.subtypes.BasicSubLFunction;
import com.cyc.baseclient.subl.subtypes.SubLBooleanSingleArgFunction;
import com.cyc.baseclient.subl.subtypes.SubLCycObjectSingleArgFunction;
import com.cyc.kb.exception.KBApiException;
import com.cyc.kb.exception.KBApiRuntimeException;

/**
 *
 * @author vijay
 */
public class SubLConstants {

  private static SubLConstants instance;
  public final BasicSubLFunction findAssertionCycl = new BasicSubLFunction("FIND-ASSERTION-CYCL");
  public final SubLCycObjectSingleArgFunction assertionAssertedAssertionSupports = new SubLCycObjectSingleArgFunction("assertion-asserted-assertion-supports");
  public final BasicSubLFunction allGenls = new BasicSubLFunction("all-genls");
  public final BasicSubLFunction quickQuietHasTypeQ = new BasicSubLFunction("quick-quiet-has-type?");
  public final BasicSubLFunction allSpecs = new BasicSubLFunction("all-specs");
  public final BasicSubLFunction removeDuplicates = new BasicSubLFunction("remove-duplicates");
  public final BasicSubLFunction withAllMts = new BasicSubLFunction("with-all-mts");
  public final BasicSubLFunction withInferenceMtRelevance = new BasicSubLFunction("WITH-INFERENCE-MT-RELEVANCE");
  public final SubLCycObjectSingleArgFunction assertionDirection = new SubLCycObjectSingleArgFunction("assertion-direction");
  public final SubLBooleanSingleArgFunction assertedAssertionQ = new SubLBooleanSingleArgFunction("asserted-assertion?");
  public final SubLCycObjectSingleArgFunction keChangeAssertionDirection = new SubLCycObjectSingleArgFunction("ke-change-assertion-direction");
  public final SubLBooleanSingleArgFunction deducedAssertionQ = new SubLBooleanSingleArgFunction("deduced-assertion?");
  public final BasicSubLFunction minCol = new BasicSubLFunction("min-col");
  public final BasicSubLFunction minArity = new BasicSubLFunction("min-arity");
  public final BasicSubLFunction maxArity = new BasicSubLFunction("max-arity");
  public final BasicSubLFunction gatherGafArgIndex = new BasicSubLFunction("GATHER-GAF-ARG-INDEX");
  
  private SubLConstants() throws KBApiException {
    super();
  }

  /**
   * This not part of the public, supported KB API
   * @throws KBApiRuntimeException
   */
  public static SubLConstants getInstance() throws KBApiRuntimeException {
    try {
      if (instance == null) {
        instance = new SubLConstants();
      }
      return instance;
    } catch (KBApiException e) {
      throw new KBApiRuntimeException(
          "One of the private final fields in com.cyc.kb.Constants could not be instantiated, can not proceed further.",
          e);
    }
  }  
}
