package com.cyc.kb;

import com.cyc.Cyc;
import com.cyc.kb.exception.KbTypeException;
import com.cyc.kb.spi.SymbolService;


/*
 * #%L
 * File: Symbol.java
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
 * The interface for symbols of the <code>SubL</code> language. Symbols are
 * occasionally referenced in <code>CycL</code> assertions, most notably those
 * that denote <code>SubL</code> functions that test for membership in a
 * collection or perform simple functions like string manipulation or
 * arithmetic.
 *
 * @author baxter
 */
public interface Symbol extends KbObject {

  //====|    Factory methods    |=================================================================//
  
  /**
   * Creates an instance of #$CycLSubLSymbol represented by symStr in the underlying KB. This static
   * method wraps a call to {@link SymbolService#get(java.lang.String) }; see that method's
   * documentation for more details.
   *
   * @param symStr the string representing an #$CycLSubLSymbol in the KB
   *
   * @return the Symbol representing an #$CycLSubLSymbol in the KB
   *
   * @throws KbTypeException Symbols are created on demand and are not expected to throw any
   *                         exception
   */
  public static Symbol get(String symStr) throws KbTypeException {
    return Cyc.getSymbolService().get(symStr);
  }

  //====|    Interface methods    |===============================================================//
  
}
