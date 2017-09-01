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
 * File: KbService.java
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
 * Provides various methods which facilitate working with the Cyc KB.
 * 
 * @author nwinant
 */
public interface KbService {
  
  
  
  // TODO: rename and/or reorganize w/ KbObjectService? - nwinant, 2017-07-27
  
  
  
  /**
   * Implementation used by static method <tt>com.cyc.kb.KbFactory#has(java.lang.String)</tt>.
   * 
   * @param nameOrId
   * @return 
   */
  boolean has(String nameOrId);
  
  /**
   * Implementation used by static method <tt>com.cyc.kb.KbFactory#kbObject(java.lang.String)</tt>.
   * 
   * @param cycLOrId
   * @return
   * @throws KbTypeException
   * @throws CreateException 
   */
  Object kbObject(String cycLOrId) throws KbTypeException, CreateException;

  /**
   * Implementation used by static method <tt>com.cyc.kb.KbFactory#apiObject(java.lang.Object)</tt>.
   * 
   * @param cycLOrId
   * @return
   * @throws KbTypeException
   * @throws CreateException 
   */
  Object apiObject(Object cycLOrId) throws KbTypeException, CreateException;
  
  /**
   * Implementation used by static method <tt>com.cyc.kb.KbFactory#apiObject(java.lang.String)</tt>.
   * 
   * @param cycLOrId
   * @return
   * @throws KbTypeException
   * @throws CreateException 
   */
  Object apiObject(String cycLOrId) throws KbTypeException, CreateException;

  /**
   * Implementation used by static method <tt>com.cyc.kb.KbFactory#apiObjectDwim(java.lang.String)</tt>.
   * 
   * @param cycLOrId
   * @return
   * @throws KbTypeException
   * @throws CreateException 
   */
  Object apiObjectDwim(String cycLOrId) throws KbTypeException, CreateException;
  
  /**
   * Implementation used by static method <tt>com.cyc.kb.KbFactory#clearCache()</tt>.
   * 
   */
  void clearCache();
  
}
