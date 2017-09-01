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
 * File: KbTermService.java
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
import com.cyc.kb.KbTerm;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbTypeException;

/**
 *
 * Provides implementations of {@link com.cyc.kb.KbTerm}.
 * 
 * @author nwinant
 */
public interface KbTermService {

  /**
   * Implementation used by static method <tt>com.cyc.kb.KbTermFactory#get(java.lang.String)</tt>.
   *
   * @param nameOrId the string representation or the HLID of the term
   *
   * @return a new KbTerm
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  KbTerm get(String nameOrId) throws KbTypeException, CreateException;

  /**
   * Implementation used by static method <tt>com.cyc.kb.KbTermFactory#findOrCreate(java.lang.String)</tt>.
   *
   * @param nameOrId the string representation or the HLID of the term
   *
   * @return a new KbTerm
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  KbTerm findOrCreate(String nameOrId) throws CreateException, KbTypeException;

  /**
   * Implementation used by static method
   * <tt>com.cyc.kb.KbTermFactory#findOrCreate(java.lang.String, java.lang.String)</tt>.
   *
   * @param nameOrId the string representation or the HLID of the term
   * @param constraintColStr the string representation of the collection that this term will
   * instantiate
   *
   * @return a new KbTerm
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  KbTerm findOrCreate(String nameOrId, String constraintColStr)
          throws CreateException, KbTypeException;

  /**
   * Implementation used by static method
   * <tt>com.cyc.kb.KbTermFactory#findOrCreate(java.lang.String, java.lang.String,
   * java.lang.String)</tt>.
   *
   * @param nameOrId the string representation or the HLID of the term
   * @param constraintColStr the string representation of the collection that this term will
   * instantiate
   * @param ctxStr the string representation of the context in which the resulting object must be an
   * instance of constraintCol
   *
   * @return a new KbTerm
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  KbTerm findOrCreate(String nameOrId, String constraintColStr, String ctxStr)
          throws CreateException, KbTypeException;

  /**
   * Implementation used by static method 
   * <tt>com.cyc.kb.KbTermFactory#findOrCreate(java.lang.String, com.cyc.kb.KbCollection)</tt>.
   *
   * @param nameOrId the string representation or the HLID of the term
   * @param constraintCol the collection that this term will instantiate
   *
   * @return a new KbTerm
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  KbTerm findOrCreate(String nameOrId, KbCollection constraintCol)
          throws CreateException, KbTypeException;

  /**
   * Implementation used by static method
   * <tt>com.cyc.kb.KbTermFactory#findOrCreate(java.lang.String, com.cyc.kb.KbCollection,
   * com.cyc.kb.Context)</tt>.
   *
   * @param nameOrId the string representation or the HLID of the term
   * @param constraintCol the collection that this term will instantiate
   * @param ctx the context in which the resulting object must be an instance of constraintCol
   *
   * @return a new KbTerm
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  KbTerm findOrCreate(String nameOrId, KbCollection constraintCol, Context ctx)
          throws CreateException, KbTypeException;

  /**
   * Implementation used by static method <tt>com.cyc.kb.KbTermFactory#existsAsType(java.lang.String)</tt>.
   *
   * @param nameOrId either the name or HL ID of an entity in the KB
   * @return <code>true</code> if entity exists in KB and is an instance of #$Thing
   */
  boolean existsAsType(String nameOrId);

  /**
   * Implementation used by static method <tt>com.cyc.kb.KbTermFactory#getStatus(java.lang.String)</tt>.
   *
   * @param nameOrId either the name or HL ID of an entity in the KB
   * @return an enum describing the existential status of the entity in the KB
   */
  KbStatus getStatus(String nameOrId);

}
