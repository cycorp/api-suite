package com.cyc.kb.client;

/*
 * #%L
 * File: VariableImpl.java
 * Project: KB Client
 * %%
 * Copyright (C) 2013 - 2016 Cycorp, Inc
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
import com.cyc.base.cycobject.CycVariable;
import com.cyc.baseclient.CycObjectFactory;
import com.cyc.kb.KbObject;
import com.cyc.kb.Variable;
import com.cyc.kb.exception.KbException;
import com.cyc.kb.exception.KbRuntimeException;
import com.cyc.kb.exception.KbTypeException;



/**
 * A <code>Variable</code> object is a facade for a <code>#$CycLVariable</code>
 * in Cyc KB.
 * 
 * A character strings starting with a ? followed by one or more alpha numeric 
 * characters. This is used to represent a variable in a sentence. 
 * 
 * @author Vijay Raj
 * @version $Id: VariableImpl.java 163355 2016-01-04 20:53:24Z nwinant $
 */
// @todo DaveS review documentation
public class VariableImpl extends StandardKbObject implements Variable {

  /**
   * Not part of the KB API. This default constructor only has the effect of
   * ensuring that there is access to a Cyc server.
   */
  VariableImpl() {
    super();
  }

  /**
   * An implementation-dependent constructor. Variable does not have factory
   * methods in the current version of the API
   * <p>
   *
   * @param cycObject the CycObject wrapped by <code>Variable</code>. The constructor
 verifies that the CycObject is an instance of CycVariableImpl
   * 
   * @throws KbTypeException if it is not an instance of CycVariableImpl
   */
  @Deprecated
  public VariableImpl(CycObject cycObject) throws KbTypeException {
    super(cycObject);
  }

  /**
   * Creates an instance of #$CycLVariable represented
   * by varStr in the underlying KB
   * <p>
   *
   * @param varStr  the string representing an #$CycLVariable in the KB
   * 
   * @throws KbTypeException if the term represented by varStr is not an instance
   * of #$CycLVariable and cannot be made into one. 
   *  
   * Symbols are created on demand and are not expected to throw
   * any exception 
   */
  // TODO: verify what kind of exception is thrown for invalid name
  public VariableImpl(String varStr) throws KbTypeException {
    super(CycObjectFactory.makeCycVariable(varStr));
  }

  /**
   * Get the name of this variable.
   * @return the name.
   */
  @Override
  public String getName() {
    return ((CycVariable)getCore()).getName();
  }

  /**
   * This not part of the public, supported KB API. Check that the candidate core
 object is a valid CycVariableImpl.
   * 
   * @param cycObject
   *          the object that is checked to be an instance of CycVariable
   * 
   * @return if the cycObject is an instance of CycVriableI
   * 
   * @see StandardKBObject#isValidCore(CycObject) for more comments
   * 
   * Example: "?X", "??PER"
   */
  @Override
  protected boolean isValidCore(CycObject cycObject) {
    return cycObject instanceof CycVariable;
  }
    
  /**
   * Return the KBCollection as a KBObject of the Cyc term that 
   * underlies this class. 
   * 
   * @return KBCollectionImpl.get("#$CycLVariable");
   */
  @Override
  public KbObject getType() {
    return getClassType();
  }
  
  /**
   * Return the KBCollection as a KBObject of the Cyc term that 
   * underlies this class. 
   * 
   * @return KBCollectionImpl.get("#$CycLVariable");
   */
  public static KbObject getClassType() {
    try {
      return KbCollectionImpl.get(getClassTypeString());
    } catch (KbException kae) {
      throw new KbRuntimeException(kae.getMessage(), kae);
    }
  }  
  
  @Override
  String getTypeString() {
    return getClassTypeString();
  }
  
  static String getClassTypeString() {
    return "#$CycLVariable";
  }
}
