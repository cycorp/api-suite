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
 * File: BasicSublGlobalVariable.java
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
import com.cyc.base.cycobject.CycSymbol;
import static com.cyc.baseclient.CycObjectFactory.makeCycSymbol;
import com.cyc.baseclient.cycobject.DefaultCycObject;
import com.cyc.baseclient.subl.SublFunction.*;
import com.cyc.baseclient.subl.SublGlobalVariable;
import static com.cyc.baseclient.subl.functions.SublFunctions.BOUNDP;

/**
 * Provides a basic, common implementation of SubLGlobalVariable.
 * 
 * @author nwinant
 * @param <T> The variable's value type
 */
public class BasicSublGlobalVariable<T> extends DefaultCycObject implements SublGlobalVariable<T> {
  
  // Fields
  
  private final CycSymbol symbol;
  
  
  // Constructors
  
  /**
   * Creates an instance of BasicSubLGlobalVariable bound to a particular symbol, via a string 
   * representation of that symbol.
   * 
   * @param name
   */
  public BasicSublGlobalVariable(String name) {
    this.symbol = makeCycSymbol(validateName(name));
  }
  
  
  // Public
  
  /**
   * The CycSymbol which denotes the function.
   * 
   * @return the symbol denoting the function.
   */
  @Override
  public CycSymbol getSymbol() {
    return this.symbol;
  }
  
  /**
   * Checks whether a variable is bound to the symbol on a particular Cyc server.
   * 
   * @param access the Cyc server to check
   * @return whether a variable is bound to the symbol
   * @throws CycApiException
   * @throws CycConnectionException 
   */
  @Override
  public boolean isBound(CycAccess access) throws CycApiException, CycConnectionException {
    return BOUNDP.eval(access, this);
  }
  
  @Override
  public String toString() {
    return this.getSymbol().toString();
  }
  
  @Override
  public int compareTo(Object o) {
    return (o != null) ? toString().compareTo(o.toString()) : 1;
  }
  
  
  // Private
  
  private static String validateName(String name) {
    if ((name==null) || !name.startsWith("*") || !name.endsWith("*")) {
      throw new CycApiException("Error wrapping variable; name is malformed: " + name);
    }
    return name;
  }
  
}
