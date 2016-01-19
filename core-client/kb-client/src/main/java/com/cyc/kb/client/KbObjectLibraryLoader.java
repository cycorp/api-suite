package com.cyc.kb.client;

/*
 * #%L
 * File: KbObjectLibraryLoader.java
 * Project: KB Client
 * %%
 * Copyright (C) 2013 - 2016 Cycorp, Inc
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

import com.cyc.base.CycAccess;
import com.cyc.base.CycAccessManager;
import com.cyc.baseclient.CycObjectLibraryLoader;
import com.cyc.kb.KbObject;
import com.cyc.kb.exception.KbRuntimeException;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nwinant
 */
public class KbObjectLibraryLoader extends CycObjectLibraryLoader {
  
  // Fields

  private final Logger logger;
  
  
  // Constructor

  public KbObjectLibraryLoader() {
    super(null);
    logger = LoggerFactory.getLogger(this.getClass().getName());
  }
  
  
  // Public
    
  public Collection<KbObject> getAllKBObjectsForClass(Class libraryClass) {
    return getAllObjectsForClass(libraryClass, KbObject.class);
  }

  public Collection<KbObject> getAllKBObjects() {
    return getAllObjects(KbObject.class);
  }
  
  
  // Protected
  
  @Override
  protected CycAccess getAccess() {
    try {
      return CycAccessManager.getCurrentAccess();
    } catch (Exception ex) {
      throw new KbRuntimeException(ex);
    }
  }
  
  @Override
  protected String getObjectCycLValue(Object o) {
    if (KbObject.class.isInstance(o)) {
      return super.getObjectCycLValue(((KbObject) o).getCore());
    }
    return super.getObjectCycLValue(o);
  }
}
