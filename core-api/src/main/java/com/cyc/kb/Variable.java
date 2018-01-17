package com.cyc.kb;

import com.cyc.Cyc;
import com.cyc.kb.exception.KbTypeException;
import com.cyc.kb.spi.VariableService;

/*
 * #%L
 * File: Variable.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2017 Cycorp, Inc
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
/**
 * The interface for <code>CycL</code> variables. Variables are used in
 * quantified {@link Sentence}s and in Queries.
 *
 * @author vijay
 */
public interface Variable extends KbObject {
  
  //====|    Factory methods    |=================================================================//
  
  /**
   * Creates an instance of #$CycLVariable represented by varStr in the underlying KB. This static
   * method wraps a call to {@link VariableService#get(java.lang.String) }; see that method's
   * documentation for more details.
   *
   * @param varStr the string representing an #$CycLVariable in the KB
   *
   * @return the Variable representing an #$CycLVariable in the KB
   *
   * @throws KbTypeException if the term represented by varStr is not an instance of #$CycLVariable
   *                         and cannot be made into one.
   *
   * Symbols are created on demand and are not expected to throw any exception
   */
  public static Variable get(String varStr) throws KbTypeException {
    return Cyc.getVariableService().get(varStr);
  }
  
  //====|    Interface methods    |===============================================================//
  
  /**
   * Get the name of this variable. Does not include the leading '?' character.
   *
   * @return the name.
   */
  String getName();

}
