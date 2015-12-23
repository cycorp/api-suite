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
 * File: SublFunctions.java
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
import com.cyc.baseclient.subl.SublSourceFile;
import com.cyc.baseclient.subl.subtypes.BasicSublFunction;
import com.cyc.baseclient.subl.subtypes.SublBooleanNoArgFunction;
import com.cyc.baseclient.subl.subtypes.SublCycListNoArgFunction;
import com.cyc.baseclient.subl.subtypes.SublStringNoArgFunction;
import java.util.Arrays;
import java.util.List;

/**
 * A collection of SubLFunction instances.
 * 
 * @author nwinant
 */
public class SublFunctions {
  
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
  public static final SublBooleanNoArgFunction CYC_OPENCYC_FEATURE = new SublBooleanNoArgFunction("cyc-opencyc-feature");
  
  /**
   * CATEGORIZE-TERM-WRT-API, which supports KBObjectFactory.get(CycObject, Class) and 
   * KBObjectImpl.convertToKBObject(CycObject).
   */
  public static final CategorizeTermWrtApiFunctionResource CATEGORIZE_TERM_WRT_API = new CategorizeTermWrtApiFunctionResource();
  
  public static final SublStringNoArgFunction CYC_SYSTEM_CODE_STRING = new SublStringNoArgFunction("cyc-system-code-string");
  
  public static final SublCycListNoArgFunction CYC_REVISION_NUMBERS = new SublCycListNoArgFunction("cyc-revision-numbers");

  public static final SublStringNoArgFunction CYC_REVISION_STRING = new SublStringNoArgFunction("cyc-revision-string");

  public static final BasicSublFunction DEFINE_EXTERNAL = new BasicSublFunction("define-external");

  public static final SublStringNoArgFunction KB_VERSION_STRING = new SublStringNoArgFunction("kb-version-string");
  
  public static final ProbeFileFunction PROBE_FILE = new ProbeFileFunction();  
  
  
  
  /**
   * SubL function wrappers with corresponding source files.
   */
  public static List<SublSourceFile> SOURCES = Arrays.<SublSourceFile>asList(
          CATEGORIZE_TERM_WRT_API
  );

}
