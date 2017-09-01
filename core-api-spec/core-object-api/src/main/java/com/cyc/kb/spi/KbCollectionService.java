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
 * File: KbCollectionService.java
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

import com.cyc.kb.Context;
import com.cyc.kb.KbCollection;
import com.cyc.kb.KbStatus;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbTypeException;

/**
 *
 * Provides implementations of {@link com.cyc.kb.KbCollection}.
 * 
 * @author nwinant
 */
public interface KbCollectionService extends KbTermService {

  /**
   * Implementation used by static method <tt>com.cyc.kb.KbCollectionFactory#get(java.lang.String)</tt>.
   *
   * @param nameOrId the string representation or the HLID of the #$Collection
   *
   * @return a new KbCollection
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  @Override
  KbCollection get(String nameOrId) throws KbTypeException, CreateException;

  /**
   * Implementation used by static method
   * <tt>com.cyc.kb.KbCollectionFactory#findOrCreate(java.lang.String)</tt>.
   *
   * @param nameOrId the string representation or the HLID of the #$Collection
   *
   * @return a new KbCollection
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  @Override
  KbCollection findOrCreate(String nameOrId) throws CreateException, KbTypeException;

  /**
   * Implementation used by static method
   * <tt>com.cyc.kb.KbCollectionFactory#findOrCreate(java.lang.String, java.lang.String)</tt>.
   *
   * @param nameOrId the string representation or the HLID of the #$Collection
   * @param constraintColStr the string representation of the collection that <code>this</code>
   * #$Collection will instantiate
   *
   * @return a new KbCollection
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  @Override
  KbCollection findOrCreate(String nameOrId, String constraintColStr)
          throws CreateException, KbTypeException;

  /**
   * Implementation used by static method
   * <tt>com.cyc.kb.KbCollectionFactory#findOrCreate(java.lang.String, java.lang.String, 
   * java.lang.String)</tt>.
   *
   * @param nameOrId the string representation or the HLID of the term
   * @param constraintColStr the string representation of the collection that <code>this</code>
   * #$Collection will instantiate
   * @param ctxStr the context in which the resulting object must be an instance of constraintCol
   *
   * @return a new KbCollection
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  @Override
  KbCollection findOrCreate(String nameOrId, String constraintColStr, String ctxStr)
          throws CreateException, KbTypeException;

  /**
   * Implementation used by static method
   * <tt>com.cyc.kb.KbCollectionFactory#findOrCreate(java.lang.String, com.cyc.kb.KbCollection)}.
   *
   * @param nameOrId the string representation or the HLID of the #$Collection
   * @param constraintCol the collection that <code>this</code> #$Collection will instantiate
   *
   * @return a new KbCollection
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  @Override
  KbCollection findOrCreate(String nameOrId, KbCollection constraintCol)
          throws CreateException, KbTypeException;

  /**
   * Implementation used by static method
   * <tt>com.cyc.kb.KbCollectionFactory#findOrCreate(java.lang.String, com.cyc.kb.KbCollection, 
   * com.cyc.kb.Context)</tt>.
   *
   * @param nameOrId the string representation or the HLID of the #$Collection
   * @param constraintCol the collection that <code>this</code> #$Collection will instantiate
   * @param ctx the context in which the resulting object must be an instance of constraintCol
   *
   * @return a new KbCollection
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  @Override
  KbCollection findOrCreate(String nameOrId, KbCollection constraintCol, Context ctx)
          throws CreateException, KbTypeException;

  /**
   * Implementation used by static method 
   * <tt>com.cyc.kb.KbCollectionFactory#existsAsType(java.lang.String)</tt>.
   *
   * @param nameOrId either the name or HL ID of an entity in the KB
   * @return <code>true</code> if entity exists in KB and is an instance of #$Collection
   */
  @Override
  boolean existsAsType(String nameOrId);

  /**
   * Implementation used by static method 
   * <tt>com.cyc.kb.KbCollectionFactory#getStatus(java.lang.String)</tt>.
   *
   * @param nameOrId either the name or HL ID of an entity in the KB
   * @return an enum describing the existential status of the entity in the KB
   */
  @Override
  KbStatus getStatus(String nameOrId);

}
