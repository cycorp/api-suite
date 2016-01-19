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
 * File: AbstractSublMultiArgFunction.java
 * Project: Base Client
 * %%
 * Copyright (C) 2013 - 2016 Cycorp, Inc.
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
 * Extends BasicSubLFunction to provide support methods for evaluating the function with multiple
 arguments. This class does not provide a method for <em>evaluating</em> the SubL function, but 
 * there are classes which extend this one and do provide such methods.
 * 
 * <p>Because this class extends BasicSubLFunction, it provides all of the methods necessary to 
 satisfy the {@link com.cyc.baseclient.subl.SubLSourceFile} interface.
 * 
 * @author nwinant
 * @param <T> evaluation argument types
 * @param <V> evaluation return type
 */
abstract public class AbstractSublMultiArgFunction<T extends Object, V extends Object> extends BasicSublFunction {
  
  /**
   * Creates an instance of SubLMultiArgFunction bound to a particular symbol, via a string 
   * representation of that symbol.
   * 
   * @param name
   */
  protected AbstractSublMultiArgFunction(String name) {
    super(name);
  }
  
  
  // Public
  
  abstract V eval(CycAccess access, T... args) throws CycConnectionException, CycApiException;
  
  
  // Protected
  
  /**
   * Checks whether the arguments are valid. This method only checks see whether the args array is
   * null. Inheriting classes may wish to override this method with more specific behavior.
   * 
   * @param args
   * @return whether the argument is valid
   */
  protected boolean isArgsValid(T... args) {
    return args != null;
  }
  
  /**
   * Builds a SubL command string incorporating the function's symbol and the arguments.
   * 
   * @param args
   * @throws CycApiException if the arguments are invalid per {@link #isArgsValid(java.lang.Object...) }.
   * @return the SubL command string
   */
  protected String buildCommand(T... args) throws CycApiException {
    if (!isArgsValid(args)) {
      handleInvalidArgs(args);
    }
    return SublApiHelper.makeSubLStmt(this.getSymbol(), args);
  }
  
  /**
   * Handles a set of invalid arguments by throwing a runtime exception. Inheriting classes may wish
   * to override this method with more specific behavior.
   * 
   * @param args 
   * @throws CycApiException with a message explaining that the arguments are invalid.
   */
  protected void handleInvalidArgs(T... args) throws CycApiException {
    throw new CycApiException("Error building command for " + toString() + ". Invalid argument: " + args);
  }
}
