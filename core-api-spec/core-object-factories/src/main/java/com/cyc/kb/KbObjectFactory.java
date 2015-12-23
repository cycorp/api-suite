package com.cyc.kb;

/*
 * #%L
 * File: KbObjectFactory.java
 * Project: Core API Object Factories
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

import com.cyc.core.service.CoreServicesLoader;
import com.cyc.kb.spi.KbObjectService;

/**
 *
 * @author nwinant
 */
public class KbObjectFactory {
  
  // Static
  
  private static final KbObjectFactory ME = new KbObjectFactory();

  protected static KbObjectFactory getInstance() {
    return ME;
  }
  
    
  // Fields
    
  private final KbObjectService service;

  
  // Construction
  
  private KbObjectFactory() {
    service = CoreServicesLoader.getKbFactoryServices().getKbObjectService();
  }

  protected KbObjectService getService() {
    return service;
  }
  
  
  // Public
  
}
