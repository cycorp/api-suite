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
 * File: SubLFunction.java
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
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.CycSymbol;

/**
 * A Java representation of a SubL function. 
 * 
 * <p>Note that this interface does not currently define a method for <em>evaluating</em> the 
 * function. Because there can be a lot of variability to how particular SubL functions may be 
 * evaluated, this is left to the implementation. However, there are a number of classes which 
 * extend {@link com.cyc.baseclient.subl.subtypes.BasicSubLFunction} to provide evaluation
 * methods.
 * 
 * @author nwinant
 */
public interface SubLFunction extends CycObject {
  
  /**
   * The CycSymbol which denotes the function.
   * 
   * @return the symbol denoting the function.
   */
  CycSymbol getSymbol();
  
  /**
   * Checks whether a function is bound to the symbol returned by {@link #getSymbol() } for a 
   * particular Cyc server.
   * 
   * @param cyc the Cyc server to check
   * @return whether a function is bound to the symbol
   * @throws CycApiException
   * @throws CycConnectionException 
   */
  boolean isBound(CycAccess cyc) throws CycApiException, CycConnectionException;
  
  /**
   * Determines whether the function is required to be in present in a particular Cyc server.
   * 
   * @param cyc the Cyc server to check
   * @return whether the function is required to be present
   * @throws CycApiException
   * @throws CycConnectionException 
   */
  boolean isRequired(CycAccess cyc) throws CycApiException, CycConnectionException;
  
  
  // TODO (maybe): If it becomes necessary, we could add SubLFunction sub-types to describe their  
  // expected return values; instances of SubLFunction which can have multiple return types could
  // implement multiple interfaces. A general approach might look something like the following...
  // - nwinant, 2015-05-22
  /*
  public interface SubLBooleanFunction<T extends Object> extends SubLFunction {
    boolean evalBoolean(CycAccess access, T... args) throws CycConnectionException, CycApiException;
  }
  
  public interface SubLCycObjectFunction<T extends Object> extends SubLFunction {
    CycObject evalCycObject(CycAccess access, T... args) throws CycConnectionException, CycApiException;
  }
  
  public interface SubLIntFunction<T extends Object> extends SubLFunction {
    int evalInt(CycAccess access, T... args) throws CycConnectionException, CycApiException;
  }
  
  public interface SubLListFunction<T extends Object> extends SubLFunction {
    CycList evalList(CycAccess access, T... args) throws CycConnectionException, CycApiException;
  }
  
  public interface SubLObjectFunction<T extends Object> extends SubLFunction {
    Object evalObject(CycAccess access, T... args) throws CycConnectionException, CycApiException;
  }
  
  public interface SubLSentenceFunction<T extends Object> extends SubLFunction {
    FormulaSentence evalSentence(CycAccess access, T... args) throws CycConnectionException, CycApiException;
  }
  
  public interface SubLStringFunction<T extends Object> extends SubLFunction {
    String evalString(CycAccess access, T... args) throws CycConnectionException, CycApiException;
  }
  
  public interface SubLVoidFunction<T extends Object> extends SubLFunction {
    void evalVoid(CycAccess access, T... args) throws CycConnectionException, CycApiException;
  }
  */
}
