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

package com.cyc.baseclient.subl.functions;

/*
 * #%L
 * File: CategorizeTermWrtApiFunctionResource.java
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

import com.cyc.base.cycobject.CycObject;
import com.cyc.baseclient.subl.SublSourceFile;
import com.cyc.baseclient.subl.subtypes.SublCycObjectSingleArgFunction;


/**
 * Represents CATEGORIZE-TERM-WRT-API, which supports KBObjectFactory.get(CycObject, Class) and
 * KBObjectImpl.convertToKBObject(CycObject).
 * 
 * @author nwinant
 */
public class CategorizeTermWrtApiFunctionResource extends SublCycObjectSingleArgFunction<CycObject> implements SublSourceFile {

  // Fields
  
  public static final String FUNCTION_NAME = "categorize-term-wrt-api";
  
  // Constructor
  
  /**
   * To access this function, call {@link SubLFunctions#CATEGORIZE_TERM_WRT_API }.
   */
  protected CategorizeTermWrtApiFunctionResource() {
    super(FUNCTION_NAME);
  }
  
}
