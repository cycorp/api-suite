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

package com.cyc.baseclient.subl.subtypes;

/*
 * #%L
 * File: AbstractSublNoArgFunction.java
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
import com.cyc.baseclient.connection.SublApiHelper;

/**
 * Extends BasicSubLFunction to provide support methods for evaluating the function with no
 arguments. This class does not provide a method for <em>evaluating</em> the SubL function, but 
 * there are classes which extend this one and do provide such methods.
 * 
 * <p>Because this class extends BasicSubLFunction, it provides all of the methods necessary to 
 satisfy the {@link com.cyc.baseclient.subl.SubLSourceFile} interface.
 * 
 * @author nwinant
 * @param <V> evaluation return type
 */
abstract public class AbstractSublNoArgFunction<V extends Object> extends BasicSublFunction {

  /**
   * Creates an instance of SubLNoArgFunction bound to a particular symbol, via a string 
   * representation of that symbol.
   * 
   * @param name
   */
  protected AbstractSublNoArgFunction(String name) {
    super(name);
  }
  
  
  // Public
  
  abstract V eval(CycAccess access) throws CycConnectionException, CycApiException;
  
  
  // Protected
  
  /**
   * Builds a SubL command string incorporating the function's symbol and no arguments.
   * 
   * @return the SubL command string
   */
  protected String buildCommand() {
    return SublApiHelper.makeSubLStmt(this.getSymbol());
  }
  
}
