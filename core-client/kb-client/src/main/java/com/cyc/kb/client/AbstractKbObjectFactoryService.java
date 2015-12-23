/*
 * Copyright 2015 Cycorp, Inc.
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
package com.cyc.kb.client;

/*
 * #%L
 * File: AbstractKbObjectFactoryService.java
 * Project: KB Client
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

import com.cyc.base.cycobject.CycObject;
import com.cyc.kb.Context;
import com.cyc.kb.KbCollection;
import com.cyc.kb.KbStatus;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbRuntimeException;
import com.cyc.kb.exception.KbTypeException;

/**
 *
 * @author nwinant
 */
abstract public class AbstractKbObjectFactoryService<T extends KbObjectImpl> {
  
  // Public
  
  public boolean existsAsType(String nameOrId) {
    return getStatus(cleanString(nameOrId)).equals(KbStatus.EXISTS_AS_TYPE);
  }
  
  public T findOrCreate(String nameOrId) throws CreateException, KbTypeException {
    return KbObjectFactory.findOrCreate(cleanString(nameOrId), getObjectType());
  }
  
  public T findOrCreate(String nameOrId, String constraintColStr)
          throws CreateException, KbTypeException {
    return KbObjectFactory.findOrCreate(cleanString(nameOrId), cleanString(constraintColStr), getObjectType());
  }
  
  public T findOrCreate(String nameOrId, KbCollection constraintCol)
          throws CreateException, KbTypeException {
    return KbObjectFactory.findOrCreate(cleanString(nameOrId), constraintCol, getObjectType());
  }
  
  public T findOrCreate(String nameOrId, String constraintColStr, String ctxStr)
          throws CreateException, KbTypeException {
    return KbObjectFactory.findOrCreate(cleanString(nameOrId), cleanString(constraintColStr), cleanString(ctxStr), getObjectType());
  }
  
  public T findOrCreate(String nameOrId, KbCollection constraintCol, Context ctx)
          throws CreateException, KbTypeException {
    return KbObjectFactory.findOrCreate(cleanString(nameOrId), constraintCol, ctx, getObjectType());
  }
  
  public T get(String nameOrId) throws KbTypeException, CreateException {
    return KbObjectFactory.get(cleanString(nameOrId), getObjectType());
  }
  
  public KbStatus getStatus(String nameOrId)  {
    return KbObjectFactory.getStatus(cleanString(nameOrId), getObjectType());
  }
  
  @Deprecated
  public KbStatus getStatus(CycObject cycObject)  {
    return KbObjectFactory.getStatus(cycObject, KbTermImpl.class);
  }
  
  @Deprecated
  public boolean existsAsType(CycObject cycObject) {
    return getStatus(cycObject).equals(KbStatus.EXISTS_AS_TYPE);
  }
  
  
  // Protected
  
  abstract protected Class<T> getObjectType();
  
  @Deprecated
  protected T findOrCreate(CycObject cycObject) throws CreateException, KbTypeException {
    return KbObjectFactory.findOrCreate(cycObject.cyclify(), getObjectType());
  }
  
  @Deprecated
  protected T get(CycObject cycObject) throws KbTypeException, CreateException {
    return KbObjectFactory.get(cycObject, getObjectType());
  }
  
  private String cleanString(String inputString) {
    if (inputString == null) {
      NullPointerException npe = new NullPointerException("String cannot be null");
      throw new KbRuntimeException(npe);
    }
    final String trimmedString = inputString.trim();
    if (trimmedString.isEmpty()) {
      throw new KbRuntimeException("String cannot be empty");
    }
    return trimmedString;
  }
  
}
