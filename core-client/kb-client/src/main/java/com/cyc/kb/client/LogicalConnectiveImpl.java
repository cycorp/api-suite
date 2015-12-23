package com.cyc.kb.client;

/*
 * #%L
 * File: LogicalConnectiveImpl.java
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

import com.cyc.base.cycobject.Guid;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.DenotationalTerm;
import com.cyc.baseclient.cycobject.CycConstantImpl;
import com.cyc.kb.KbStatus;
import com.cyc.kb.LogicalConnective;
import com.cyc.kb.client.LookupType;
import com.cyc.kb.KbObject;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbException;
import com.cyc.kb.exception.KbRuntimeException;
import com.cyc.kb.exception.KbTypeException;


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

  
  protected LogicalConnectiveImpl(CycObject cycLogicalConn) throws KbTypeException {
    super(cycLogicalConn);
  }

  
  public LogicalConnectiveImpl(String logicalConnStr) throws KbTypeException, CreateException {
    super(logicalConnStr);
  }

  
  public LogicalConnectiveImpl (String logicalConnStr, LookupType lookup) throws KbTypeException, CreateException {
    super(logicalConnStr, lookup);
  }
  
  public static LogicalConnectiveImpl get(String nameOrId) throws KbTypeException, CreateException {
    return KbObjectFactory.get(nameOrId, LogicalConnectiveImpl.class);
  }

  @SuppressWarnings("deprecation")
public static LogicalConnectiveImpl get(CycObject object) throws KbTypeException, CreateException {
    return KbObjectFactory.get(object, LogicalConnectiveImpl.class);
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
    return getStatus(nameOrId).equals(KbStatus.EXISTS_AS_TYPE);
  }

  public static boolean existsAsType(CycObject object) {
    return getStatus(object).equals(KbStatus.EXISTS_AS_TYPE);
  }

  public static KbStatus getStatus(String nameOrId) {
    return KbObjectFactory.getStatus(nameOrId, LogicalConnectiveImpl.class);

  }

  public static KbStatus getStatus(CycObject object) {
    return KbObjectFactory.getStatus(object, LogicalConnectiveImpl.class);
  }

  /**
   * Return the KBCollection as a KBObject of the Cyc term that 
   * underlies this class. 
   * 
   * @return KBCollectionImpl.get("#$LogicalConnective");
   */
  @Override
  public KbObject getType() {
    return getClassType();
  }
  
  /**
   * Return the KBCollection as a KBObject of the Cyc term that 
   * underlies this class. 
   * 
   * @return KBCollectionImpl.get("#$LogicalConnective");
   */
  public static KbObject getClassType() {
    try {
      return KbCollectionImpl.get(getClassTypeString());
    } catch (KbException kae) {
      throw new KbRuntimeException(kae.getMessage(), kae);
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
