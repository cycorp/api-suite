package com.cyc.kb.client;

/*
 * #%L
 * File: LogicalConnectiveImpl.java
 * Project: KB API Implementation
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

import com.cyc.base.cycobject.Guid;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.DenotationalTerm;
import com.cyc.baseclient.cycobject.CycConstantImpl;
import com.cyc.kb.KBAPIEnums;
import com.cyc.kb.KBStatus;
import com.cyc.kb.LogicalConnective;
import com.cyc.kb.KBAPIEnums.LookupType;
import com.cyc.kb.KBObject;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KBApiException;
import com.cyc.kb.exception.KBApiRuntimeException;
import com.cyc.kb.exception.KBTypeException;


/**
 * THIS IS NOT PART OF 1.0.0.
 * @author vijay
 */
public class LogicalConnectiveImpl extends RelationImpl implements LogicalConnective {
  
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
  @SuppressWarnings("unused")
private LogicalConnectiveImpl() {
    super();
  }

  
  protected LogicalConnectiveImpl(CycObject cycLogicalConn) throws KBTypeException {
    super(cycLogicalConn);
  }

  
  public LogicalConnectiveImpl(String logicalConnStr) throws KBTypeException, CreateException {
    super(logicalConnStr);
  }

  
  public LogicalConnectiveImpl (String logicalConnStr, LookupType lookup) throws KBTypeException, CreateException {
    super(logicalConnStr, lookup);
  }
  
  public static LogicalConnectiveImpl get(String nameOrId) throws KBTypeException, CreateException {
    return KBObjectFactory.get(nameOrId, LogicalConnectiveImpl.class);
  }

  @SuppressWarnings("deprecation")
public static LogicalConnectiveImpl get(CycObject object) throws KBTypeException, CreateException {
    return KBObjectFactory.get(object, LogicalConnectiveImpl.class);
  }

  @Deprecated
  public static LogicalConnectiveImpl findOrCreate(String nameOrId) {
    throw new UnsupportedOperationException("Creation of new Quantifiers is not allowed.  Please use a get method.");
  }

  @Deprecated
  public static LogicalConnectiveImpl findOrCreate(CycObject object) {
    throw new UnsupportedOperationException("Creation of new Quantifiers is not allowed.  Please use a get method.");
  }

  public static boolean existsAsType(String nameOrId) {
    return getStatus(nameOrId).equals(KBStatus.EXISTS_AS_TYPE);
  }

  public static boolean existsAsType(CycObject object) {
    return getStatus(object).equals(KBStatus.EXISTS_AS_TYPE);
  }

  public static KBStatus getStatus(String nameOrId) {
    return KBObjectFactory.getStatus(nameOrId, LogicalConnectiveImpl.class);

  }

  public static KBStatus getStatus(CycObject object) {
    return KBObjectFactory.getStatus(object, LogicalConnectiveImpl.class);
  }

  /**
   * Return the KBCollection as a KBObject of the Cyc term that 
   * underlies this class. 
   * 
   * @return KBCollectionImpl.get("#$LogicalConnective");
   */
  @Override
  public KBObject getType() {
    return getClassType();
  }
  
  /**
   * Return the KBCollection as a KBObject of the Cyc term that 
   * underlies this class. 
   * 
   * @return KBCollectionImpl.get("#$LogicalConnective");
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
    return "#$LogicalConnective";
  }
}
