/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cyc.kb.client;

/*
 * #%L
 * File: ScopingRelationImpl.java
 * Project: KB API
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
import com.cyc.base.cycobject.DenotationalTerm;
import com.cyc.base.cycobject.Guid;
import com.cyc.baseclient.cycobject.CycConstantImpl;
import com.cyc.kb.KBAPIEnums;
import com.cyc.kb.KBStatus;
import com.cyc.kb.ScopingRelation;
import com.cyc.kb.KBAPIEnums.LookupType;
import com.cyc.kb.KBObject;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KBApiException;
import com.cyc.kb.exception.KBApiRuntimeException;
import com.cyc.kb.exception.KBTypeException;



/**
 * This not part of the public, supported KB API 1.0.0.
 * @author vijay
 */
public class ScopingRelationImpl extends RelationImpl implements ScopingRelation {
  
    private static final DenotationalTerm TYPE_CORE =
          new CycConstantImpl("LogicalConnective", new Guid("bd58b9f9-9c29-11b1-9dad-c379636f7270"));

  static DenotationalTerm getClassTypeCore() {
    return TYPE_CORE;
  }
  
  /**
   * default constructor, calls the default super constructor
   *
   * @throws Exception
   */
  protected ScopingRelationImpl() {
    super();
  }

  
  protected ScopingRelationImpl(CycObject cycScopingRel) throws KBTypeException {
    super(cycScopingRel);
  }

  
  public ScopingRelationImpl(String scopingRelStr) throws KBTypeException, CreateException {
    super(scopingRelStr);
  }

  
  public ScopingRelationImpl (String scopingRelStr, LookupType lookup) throws KBTypeException, CreateException {
    super(scopingRelStr, lookup);
  }
  
  
  public static ScopingRelationImpl get(String nameOrId) throws KBTypeException, CreateException {
    return KBObjectFactory.get(nameOrId, ScopingRelationImpl.class);
  }

  
  @SuppressWarnings("deprecation")
public static ScopingRelationImpl get(CycObject object) throws KBTypeException, CreateException {
    return KBObjectFactory.get(object, ScopingRelationImpl.class);
  }

  
  public static ScopingRelationImpl findOrCreate(String nameOrId) {
    throw new UnsupportedOperationException("Creation of new Quantifiers is not allowed.  Please use a get method.");
  }

  /**
   * The KBAPI does not support the creation of new quantifiers. The
   * <code>get</code> methods on quantifiers should be used to retrieve existing quantifiers.
   * @deprecated Creation of new quantifiers is not supported in the KBAPI.
   */
  public static ScopingRelationImpl findOrCreate(CycObject object) {
    throw new UnsupportedOperationException("Creation of new Quantifiers is not allowed.  Please use a get method.");
  }

  
  public static boolean existsAsType(String nameOrId) {
    return getStatus(nameOrId).equals(KBStatus.EXISTS_AS_TYPE);
  }

  
  public static boolean existsAsType(CycObject object) {
    return getStatus(object).equals(KBStatus.EXISTS_AS_TYPE);
  }

  
  public static KBStatus getStatus(String nameOrId) {
    return KBObjectFactory.getStatus(nameOrId, ScopingRelationImpl.class);

  }

  
  public static KBStatus getStatus(CycObject object) {
    return KBObjectFactory.getStatus(object, ScopingRelationImpl.class);
  }

  /**
   * Return the KBCollection as a KBObject of the Cyc term that 
   * underlies this class. 
   * 
   * @return KBCollectionImpl.get("#$ScopingRelation");
   */
  @Override
  public KBObject getType() {
    return getClassType();
  }
  
  /**
   * Return the KBCollection as a KBObject of the Cyc term that 
   * underlies this class. 
   * 
   * @return KBCollectionImpl.get("#$ScopingRelation");
   */
  public static KBObject getClassType() {
    try {
      return KBCollectionImpl.get(getClassTypeString());
    } catch (KBApiException kae) {
      throw new KBApiRuntimeException(kae.getMessage(), kae);
    }
  }
  
  @Override
  String getTypeString() {
    return getClassTypeString();
  }
  
  static String getClassTypeString() {
    return "#$ScopingRelation";
  }
}
