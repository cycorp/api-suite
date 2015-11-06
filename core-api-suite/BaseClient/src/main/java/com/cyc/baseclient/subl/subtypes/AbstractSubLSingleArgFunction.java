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
 * File: AbstractSubLSingleArgFunction.java
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
import com.cyc.base.CycApiException;
import com.cyc.base.CycConnectionException;
import com.cyc.baseclient.api.SubLAPIHelper;

/**
 * Extends BasicSubLFunction to provide support methods for evaluating the function with a single
 * argument. This class does not provide a method for <em>evaluating</em> the SubL function, but 
 * there are classes which extend this one and do provide such methods.
 * 
 * <p>Because this class extends BasicSubLFunction, it provides all of the methods necessary to 
 * satisfy the {@link com.cyc.baseclient.subl.SubLSourceFile} interface.
 * 
 * @author nwinant
 * @param <T> evaluation argument type
 * @param <V> evaluation return type
 */
abstract public class AbstractSubLSingleArgFunction<T extends Object, V extends Object> extends BasicSubLFunction {

  /**
   * Creates an instance of SubLSingleArgFunction bound to a particular symbol, via a string 
   * representation of that symbol.
   * 
   * @param name
   */
  protected AbstractSubLSingleArgFunction(String name) {
    super(name);
  }
  
  
  // Public
  
  abstract V eval(CycAccess access, T arg) throws CycConnectionException, CycApiException;
  
  
  // Protected
  
  /**
   * Checks whether the argument is valid. This method only checks see whether the arg is null. 
   * Inheriting classes may wish to override this method with more specific behavior.
   * 
   * @param arg
   * @return whether the argument is valid
   */
  protected boolean isArgValid(T arg) throws CycApiException {
    return arg != null;
  }
  
  /**
   * Builds a SubL command string incorporating the function's symbol and the argument.
   * 
   * @param arg
   * @throws CycApiException if the argument is invalid per {@link #isArgValid(java.lang.Object...) }.
   * @return the SubL command string
   */
  public String buildCommand(T arg) throws CycApiException {
    if (!isArgValid(arg)) {
      handleInvalidArg(arg);
    }
    return SubLAPIHelper.makeSubLStmt(this.getSymbol(), arg);
  }
  
  /**
   * Handles an invalid arguments by throwing a runtime exception. Inheriting classes may wish
   * to override this method with more specific behavior.
   * 
   * @param arg
   * @throws CycApiException with a message explaining that the argument is invalid.
   */
  protected void handleInvalidArg(T arg) {
    throw new CycApiException("Error building command for " + toString() + ". Invalid argument: " + arg);
  }

}
