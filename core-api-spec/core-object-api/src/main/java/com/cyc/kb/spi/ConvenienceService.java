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

package com.cyc.kb.spi;

/*
 * #%L
 * File: ConvenienceService.java
 * Project: Core API Object Specification
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

import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbTypeException;

/**
 *
 * @author nwinant
 * @param <T>
 */
public interface ConvenienceService<T extends Object> extends KbObjectService<T> {
  
  /**
   * Provides implementation for {@link com.cyc.kb.KbFactory#existsInKb(java.lang.String) }.
   * 
   * @param nameOrId
   * @return 
   */
  boolean existsInKb(String nameOrId);
  
  /**
   * Provides implementation for {@link com.cyc.kb.KbFactory#getKbObject(java.lang.String) }.
   * 
   * @param cycLOrId
   * @return
   * @throws KbTypeException
   * @throws CreateException 
   */
  T getKbObject(String cycLOrId) throws KbTypeException, CreateException;

  /**
   * Provides implementation for {@link com.cyc.kb.KbFactory#getApiObject(java.lang.Object) }.
   * 
   * @param cycLOrId
   * @return
   * @throws KbTypeException
   * @throws CreateException 
   */
  T getApiObject(Object cycLOrId) throws KbTypeException, CreateException;
  
  /**
   * Provides implementation for {@link com.cyc.kb.KbFactory#getApiObject(java.lang.String) }.
   * 
   * @param cycLOrId
   * @return
   * @throws KbTypeException
   * @throws CreateException 
   */
  T getApiObject(String cycLOrId) throws KbTypeException, CreateException;

  /**
   * Provides implementation for {@link com.cyc.kb.KbFactory#getApiObjectDwim(java.lang.String) }.
   * 
   * @param cycLOrId
   * @return
   * @throws KbTypeException
   * @throws CreateException 
   */
  T getApiObjectDwim(String cycLOrId) throws KbTypeException, CreateException;
    
  /**
   * Provides implementation for {@link com.cyc.kb.KbFactory#clearCache() }.
   * 
   */
  void clearCache();

}
