package com.cyc.kb;

/*
 * #%L
 * File: VariableFactory.java
 * Project: Core API Object Factories
 * %%
 * Copyright (C) 2013 - 2017 Cycorp, Inc
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

import com.cyc.core.service.CoreServicesLoader;
import com.cyc.kb.exception.KbTypeException;
import com.cyc.kb.spi.VariableService;

/**
 *
 * @author nwinant
 */
public class VariableFactory {

  // Static
  
  private static final VariableFactory ME = new VariableFactory();

  protected static VariableFactory getInstance() {
    return ME;
  }
  
    
  // Fields
    
  private final VariableService service;

  
  // Construction
  
  private VariableFactory() {
    service = CoreServicesLoader.getKbFactoryServices().variable();
  }

  protected VariableService getService() {
    return service;
  }
  
  
  // Public
  
  /**
   * Creates an instance of #$CycLVariable represented by varStr in the underlying KB.
   *
   * @param varStr the string representing an #$CycLVariable in the KB
   * @return the Variable representing an #$CycLVariable in the KB
   *
   * @throws KbTypeException if the term represented by varStr is not an instance of #$CycLVariable
   * and cannot be made into one.
   *
   * Symbols are created on demand and are not expected to throw any exception
   */
  public static Variable get(String varStr) throws KbTypeException {
    return getInstance().getService().get(varStr);
  }
  
}
