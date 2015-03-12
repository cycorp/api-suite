/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cyc.kb.client;

/*
 * #%L
 * File: SymbolImpl.java
 * Project: KB API Implementation
 * %%
 * Copyright (C) 2013 - 2015 Cycorp, Inc
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
import com.cyc.baseclient.cycobject.CycSymbolImpl;
import com.cyc.kb.KBObject;
import com.cyc.kb.Symbol;
import com.cyc.kb.exception.KBApiException;
import com.cyc.kb.exception.KBApiRuntimeException;
import com.cyc.kb.exception.KBTypeException;



/**
 * A <code>Symbol</code> object is a facade for a quoted instance of
 * <code>#$CycLSubLSymbol</code> in Cyc KB.
 * 
 * This class of objects are used in construction of sentences with replacement
 * arguments, for example in stored queries for testing. The usage of this is 
 * rare, but the API supports it for completeness. 
 * 
 * @author Vijay Raj
 * @version $Id: SymbolImpl.java 157022 2015-03-11 16:19:37Z nwinant $  
 */
public class SymbolImpl extends StandardKBObject implements Symbol {

  /**
   * Not part of the KB API. This default constructor only has the effect of
   * ensuring that there is access to a Cyc server.
   */
  SymbolImpl() {
    super();
  }

  /**
   * An implementation-dependent constructor. Symbol does not have factory
   * methods in the current version of the API
   * <p>
   *
   * @param cycObject the CycObject wrapped by <code>Symbol</code>. The constructor
 verifies that the CycObject is an instance of CycSymbolImpl
   * 
   * @throws KBTypeException if it is not an instance of CycSymbolImpl
   */
  @Deprecated
  public SymbolImpl(CycObject cycObject) throws KBTypeException {
    super(cycObject);
  }

  /**
   * Creates an instance of #$CycLSubLSymbol represented
   * by symStr in the underlying KB
   * <p>
   *
   * @param symStr  the string representing an #$CycLSubLSymbol in the KB
   *  
   * @throws KBTypeException Symbols are created on demand and are not expected to throw
   * any exception 
   */
  // TODO: verify what kind of exception is thrown for an invalid name
  public SymbolImpl(String symStr) throws KBTypeException {
    super(new CycSymbolImpl(symStr));
  }

  /**
   * This not part of the public, supported KB API. Check that the candidate core
 object is a valid CycSymbolImpl.
 
 Example: ":PERSON" 
   * 
   * @param cycObject
   *          the object that is checked to be an instance of CycSymbolImpl
   * 
   * @return if a cycObject is of an instance of CycSymbolImpl
   * 
   * @see StandardKBObject#isValidCore(CycObject)  for more comments
   */
  @Override
  protected boolean isValidCore(CycObject cycObject) {
    if (cycObject instanceof CycSymbolImpl) {
      return true;
    } else {
      return false;
    }
  }
    
  /**
   * Return the KBCollection as a KBObject of the Cyc term that 
   * underlies this class. 
   * 
   * @return KBCollectionImpl.get("#$CycLSubLSymbol");
   */
  @Override
  public KBObject getType() {
    return getClassType();
  }
  
  /**
   * Return the KBCollection as a KBObject of the Cyc term that 
   * underlies this class. 
   * 
   * @return KBCollectionImpl.get("#$CycLSubLSymbol");
   */
  public static KBObject getClassType() {
    try {
      return KBCollectionImpl.get(getClassTypeString());
    } catch (KBApiException kae) {
      throw new KBApiRuntimeException(kae.getMessage(), kae);
    }
  }  
  
  @Override
  String getTypeString() {
    return getClassTypeString();
  }
  
  static String getClassTypeString() {
    return "#$CycLSubLSymbol";
  }
}
