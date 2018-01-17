package com.cyc.kb;

import com.cyc.Cyc;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbTypeException;
import com.cyc.kb.spi.SecondOrderCollectionService;

/*
 * #%L
 * File: SecondOrderCollection.java
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

/**
 * The interface for {@link KbCollection}s whose instances are
 * {@link FirstOrderCollection}s.
 *
 * @author vijay
 */
public interface SecondOrderCollection extends KbCollection {

  //====|    Factory methods    |=================================================================//
  
  /**
   * Get the <code>SecondOrderCollection</code> with the name <code>nameOrId</code>. This static
   * method wraps a call to {@link SecondOrderCollectionService#get(java.lang.String) }; see that
   * method's documentation for more details.
   *
   * @param nameOrId the string representation or the HLID of the #$SecondOrderCollection
   *
   * @return a new SecondOrderCollection
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  public static SecondOrderCollection get(String nameOrId)
          throws KbTypeException, CreateException {
    return Cyc.getSecondOrderCollectionService().get(nameOrId);
  }
  
  public static SecondOrderCollection findOrCreate(String nameOrId) throws CreateException, KbTypeException {
    return Cyc.getSecondOrderCollectionService().findOrCreate(nameOrId);
  }
  
  public static SecondOrderCollection findOrCreate(String nameOrId, String constraintColStr)
          throws CreateException, KbTypeException {
    return Cyc.getSecondOrderCollectionService().findOrCreate(nameOrId, constraintColStr);
  }
  
  public static SecondOrderCollection findOrCreate(String nameOrId, String constraintColStr, String ctxStr)
          throws CreateException, KbTypeException {
    return Cyc.getSecondOrderCollectionService().findOrCreate(nameOrId, constraintColStr, ctxStr);
  }
  
  public static SecondOrderCollection findOrCreate(String nameOrId, KbCollection constraintCol)
          throws CreateException, KbTypeException {
    return Cyc.getSecondOrderCollectionService().findOrCreate(nameOrId, constraintCol);
  }

  public static SecondOrderCollection findOrCreate(String nameOrId, KbCollection constraintCol, Context ctx)
          throws CreateException, KbTypeException {
    return Cyc.getSecondOrderCollectionService().findOrCreate(nameOrId, constraintCol, ctx);
  }
  
  public static boolean existsAsType(String nameOrId) {
    return Cyc.getSecondOrderCollectionService().existsAsType(nameOrId);
  }
  
  public static KbStatus getStatus(String nameOrId) {
    return Cyc.getSecondOrderCollectionService().getStatus(nameOrId);
  }
  
  //====|    Interface methods    |===============================================================//
  
}
