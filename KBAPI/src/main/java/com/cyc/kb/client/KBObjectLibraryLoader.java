package com.cyc.kb.client;

/*
 * #%L
 * File: KBObjectLibraryLoader.java
 * Project: KB API
 * %%
 * Copyright (C) 2013 - 2014 Cycorp, Inc
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

import com.cyc.baseclient.CycObjectLibraryLoader;
import com.cyc.kb.KBObject;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nwinant
 */
public class KBObjectLibraryLoader extends CycObjectLibraryLoader {
  
  
  // Constructor

  protected KBObjectLibraryLoader() {
    logger = LoggerFactory.getLogger(this.getClass().getName());
  }

  synchronized public static KBObjectLibraryLoader get() {
    if (ME == null) {
      ME = new KBObjectLibraryLoader();
    }
    return ME;
  }

  
  // Public
  
  public Collection<KBObject> getAllKBObjectsForClass(Class libraryClass) {
    return getAllObjectsForClass(libraryClass, KBObject.class);
  }

  public Collection<KBObject> getAllKBObjects() {
    return getAllObjects(KBObject.class);
  }
  
  
  // Protected
  
  @Override
  protected String getObjectCycLValue(Object o) {
    if (KBObject.class.isInstance(o)) {
      return super.getObjectCycLValue(((KBObject) o).getCore());
    }
    return super.getObjectCycLValue(o);
  }
  
  
  // Internal
  
  private static KBObjectLibraryLoader ME;
  private final Logger logger;
}
