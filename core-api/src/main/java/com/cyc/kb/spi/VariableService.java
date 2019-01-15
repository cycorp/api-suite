package com.cyc.kb.spi;

/*
 * #%L
 * File: VariableService.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2019 Cycorp, Inc
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
import com.cyc.kb.Variable;
import com.cyc.kb.exception.KbTypeException;

/**
 * Provides implementations of {@link com.cyc.kb.Variable}.
 *
 * @author nwinant
 */
public interface VariableService {

  /**
   * Creates an instance of #$CycLVariable represented by varStr in the underlying KB.
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
  Variable get(String varStr) throws KbTypeException;

}
