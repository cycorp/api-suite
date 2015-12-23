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

package com.cyc.baseclient.subl;

/*
 * #%L
 * File: SublGlobalVariable.java
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
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.CycSymbol;

/**
 * A Java representation of a SubL global variable.
 * 
 * <p>Note that this interface does not currently define a method for <em>evaluating</em> the 
 * variable. Because there can be a lot of variability to return types, this is left to the 
 * implementation. However, classes may be added which will extend 
 * {@link com.cyc.baseclient.subl.generaltypes.BasicSubLVariable} to provide evaluation methods.
 * 
 * @author nwinant
 * @param <T> The variable's value type
 */
public interface SublGlobalVariable<T extends Object> extends CycObject {
  
  /**
   * The CycSymbol which denotes the variable.
   * 
   * @return the symbol denoting the variable.
   */
  CycSymbol getSymbol();
  
  /**
   * Checks whether a variable is bound to the symbol on a particular Cyc server.
   * 
   * @param cyc the Cyc server to check
   * @return whether a variable is bound to the symbol
   * @throws CycApiException
   * @throws CycConnectionException 
   */
  boolean isBound(CycAccess cyc) throws CycApiException, CycConnectionException;
  
  
  // TODO: add classes which extend {@link com.cyc.baseclient.subl.subtypes.BasicSubLGlobalVariable}
  //      to provide getters & setters (e.g., SubLBooleanGlobalVariable, etc.)
  /*
  public T getValue(CycAccess access) throws CycConnectionException, CycApiException;
  
  public T setValue(CycAccess access, T value) throws CycConnectionException, CycApiException;
  */
}
