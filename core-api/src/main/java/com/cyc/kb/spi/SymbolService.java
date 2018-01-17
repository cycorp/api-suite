package com.cyc.kb.spi;

/*
 * #%L
 * File: SymbolService.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2018 Cycorp, Inc
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
import com.cyc.kb.Symbol;
import com.cyc.kb.exception.KbTypeException;

/**
 * Provides implementations of {@link com.cyc.kb.Symbol}.
 *
 * @author nwinant
 */
public interface SymbolService {

  /**
   * Creates an instance of #$CycLSubLSymbol represented by symStr in the underlying KB.
   *
   * @param symStr the string representing an #$CycLSubLSymbol in the KB
   *
   * @return the Symbol representing an #$CycLSubLSymbol in the KB
   *
   * @throws KbTypeException Symbols are created on demand and are not expected to throw any
   *                         exception
   */
  Symbol get(String symStr) throws KbTypeException;

}
