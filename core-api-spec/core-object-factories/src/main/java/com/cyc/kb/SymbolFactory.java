package com.cyc.kb;

/*
 * #%L
 * File: SymbolFactory.java
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
import com.cyc.kb.spi.SymbolService;

/**
 *
 * @author nwinant
 */
public class SymbolFactory {
  
  // Static
  
  private static final SymbolFactory ME = new SymbolFactory();

  protected static SymbolFactory getInstance() {
    return ME;
  }
  
    
  // Fields
    
  private final SymbolService service;

  
  // Construction
  
  private SymbolFactory() {
    service = CoreServicesLoader.getKbFactoryServices().symbol();
  }

  protected SymbolService getService() {
    return service;
  }
  
  
  // Public
  
  /**
   * Creates an instance of #$CycLSubLSymbol represented by symStr in the underlying KB.
   *
   * @param symStr the string representing an #$CycLSubLSymbol in the KB
   * @return the Symbol representing an #$CycLSubLSymbol in the KB
   *
   * @throws KbTypeException Symbols are created on demand and are not expected to throw any
   * exception
   */
  public static Symbol get(String symStr) throws KbTypeException {
    return getInstance().getService().get(symStr);
  }
  
}
