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
 * File: SubLFunctions.java
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
import com.cyc.baseclient.subl.SubLSourceFile;
import com.cyc.baseclient.subl.subtypes.SubLBooleanNoArgFunction;
import com.cyc.baseclient.subl.subtypes.SubLCycListNoArgFunction;
import com.cyc.baseclient.subl.subtypes.SubLStringNoArgFunction;
import java.util.Arrays;
import java.util.List;

/**
 * A collection of SubLFunction instances.
 * 
 * @author nwinant
 */
public class SubLFunctions {
  
  /**
   * BOUNDP, used to check whether a symbol is bound to a variable.
   * To check if a <em>function</em> is bound, see {@link SubLFunctions#FBOUNDP }.
   */
  public static final BoundpFunction BOUNDP = new BoundpFunction();
  
  /**
   * FBOUNDP, used to check whether a symbol is bound to a function.
   * To check if a <em>variable</em> is bound, see {@link SubLFunctions#BOUNDP }.
   */
  public static final FboundpFunction FBOUNDP = new FboundpFunction();
  
  /**
   * CYC-OPENCYC-FEATURE, which returns true if the server is an OpenCyc image.
   */
  public static final SubLBooleanNoArgFunction CYC_OPENCYC_FEATURE = new SubLBooleanNoArgFunction("cyc-opencyc-feature");
  
  /**
   * CATEGORIZE-TERM-WRT-API, which supports KBObjectFactory.get(CycObject, Class) and 
   * KBObjectImpl.convertToKBObject(CycObject).
   */
  public static final CategorizeTermWrtApiFunctionResource CATEGORIZE_TERM_WRT_API = new CategorizeTermWrtApiFunctionResource();
  
  public static final SubLStringNoArgFunction CYC_SYSTEM_CODE_STRING = new SubLStringNoArgFunction("cyc-system-code-string");
  
  public static final SubLCycListNoArgFunction CYC_REVISION_NUMBERS = new SubLCycListNoArgFunction("cyc-revision-numbers");

  public static final SubLStringNoArgFunction CYC_REVISION_STRING = new SubLStringNoArgFunction("cyc-revision-string");
  
  public static final SubLStringNoArgFunction KB_VERSION_STRING = new SubLStringNoArgFunction("kb-version-string");
  
  public static final ProbeFileFunction PROBE_FILE = new ProbeFileFunction();
  
  
  
  /**
   * SubL function wrappers with corresponding source files.
   */
  public static List<SubLSourceFile> SOURCES = Arrays.<SubLSourceFile>asList(
          CATEGORIZE_TERM_WRT_API
  );

}
