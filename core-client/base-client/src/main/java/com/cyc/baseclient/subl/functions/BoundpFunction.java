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
 * File: BoundpFunction.java
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

import com.cyc.base.CycAccess;
import com.cyc.base.exception.CycApiException;
import com.cyc.base.exception.CycConnectionException;
import com.cyc.base.cycobject.CycSymbol;
import static com.cyc.baseclient.CycObjectFactory.makeCycSymbol;
import com.cyc.baseclient.subl.SublGlobalVariable;
import com.cyc.baseclient.subl.subtypes.SublBooleanSingleArgFunction;

/**
 * Represents BOUNDP, which is used to check whether a symbol is bound to a variable.
 * 
 * <p>To check if a <em>function</em> is bound, see {@link SubLFunctions#BOUNDP }.
 * 
 * @author nwinant
 */
public class BoundpFunction extends SublBooleanSingleArgFunction<CycSymbol> {
  
  // Fields
  
  public static final String FUNCTION_NAME = "boundp";
  
  
  // Constructor
  
  /**
   * To access this function, call {@link SubLFunctions#BOUNDP }.
   */
  protected BoundpFunction() {
    super(FUNCTION_NAME);
  }
  
  
  // Public
  
  public boolean eval(CycAccess access, SublGlobalVariable variable) throws CycConnectionException, CycApiException {
    return eval(access, variable.getSymbol());
  }
  
  public boolean eval(CycAccess access, String symbolName) throws CycConnectionException, CycApiException {
    return eval(access, makeCycSymbol(symbolName));
  }
}
