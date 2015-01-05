/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cyc.kb.client;

/*
 * #%L
 * File: KBTermImpl.java
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

import com.cyc.base.CycConnectionException;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.DenotationalTerm;
import com.cyc.base.cycobject.Fort;
import com.cyc.base.cycobject.Guid;
import com.cyc.baseclient.cycobject.CycConstantImpl;
import com.cyc.kb.Context;
import com.cyc.kb.KBAPIEnums;
import com.cyc.kb.KBAPIEnums.LookupType;
import com.cyc.kb.KBCollection;
import com.cyc.kb.KBIndividual;
import com.cyc.kb.KBObject;
import com.cyc.kb.KBStatus;
import com.cyc.kb.KBTerm;
import com.cyc.kb.config.DefaultContext;
import com.cyc.kb.config.KBAPIDefaultContext;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KBApiException;
import com.cyc.kb.exception.KBApiRuntimeException;
import com.cyc.kb.exception.KBObjectNotFoundException;
import com.cyc.kb.exception.KBTypeException;
import java.util.Date;
import java.util.List;

/**
 * A <code>KBTerm</code> is a facade for any #$CycLDenotationalTerm, but in the 
 * API its purpose is to create terms that are only known to be #$Thing. 
 *    
 * @author Dave Schneider
 * @version $Id: KBTermImpl.java 154990 2014-11-14 22:46:51Z nwinant $
 */
public class KBTermImpl extends StandardKBObject implements KBTerm {

  private static final DenotationalTerm TYPE_CORE =
          new CycConstantImpl("Thing", new Guid("bd5880f4-9c29-11b1-9dad-c379636f7270"));

  static DenotationalTerm getClassTypeCore() {
    return TYPE_CORE;
  }

  /**
   * Not part of the KB API. This default constructor only has the effect of
   * ensuring that there is access to a Cyc server.
   */
  KBTermImpl() {
    super();
  }

  /**
   * Not part of the KB API. An implementation-dependent constructor.
   * <p>
   * It is used when the result of query is a CycObject and is known to be or
   * requested to be cast as an KBTerm.
   *
   * @param cycObject	the CycObject wrapped by KBTerm.
   * 
   * @throws KBTypeException 
   */
  KBTermImpl(CycObject cycObject) throws KBTypeException  {
    super(cycObject);
  }

  /**
   * EXPERIMENTAL!!! NOT PART OF THE KB API
   */
  protected KBTermImpl(String termStr, List<Object> l) throws KBTypeException, CreateException {
    super(termStr, l);
  }
  
  /**
   * This not part of the public, supported KB API. finds or creates an kb Term (#$Thing)
   * represented by termStr in the underlying KB
   * <p>
   *
   * @param termStr	the string representing a #$Thing in the KB
   * 
   * @throws CreateException if the #$Thing represented by termStr is not found
   * and could not be created
   * @throws KBTypeException is unlikely to be thrown, since everything is a #$Thing
   */
  KBTermImpl(String termStr) throws KBTypeException, CreateException  {
    super(termStr);
  }

  /**
   * This not part of the public, supported KB API. finds or creates; or finds an individual
   * represented by termStr in the underlying KB based on input ENUM
   * <p>
   *
   * @param termStr	the string representing a #$Thing in the KB
   * @param lookup the enum to specify LookupType: FIND or FIND_OR_CREATE
   * 
   * @throws CreateException 
   * @throws KBTypeException 
   *
   * @throws KBObjectNotFoundException	if the #$Thing represented by termStr is
   * not found and could not be created
   * @throws KBTypeException is unlikely to be thrown, since everything is a #$Thing
   */
  KBTermImpl(String termStr, LookupType lookup) throws KBTypeException, CreateException  {
    super(termStr, lookup);
  }

  
  protected KBTermImpl(DefaultContext contexts, KBTerm term) {
    super();
    this.setCore(term);
  }
  
  
  /**
   * Get the
   * <code>KBTerm</code> with the name
   * <code>nameOrId</code>. Throws exceptions if there is no KB term by that
   * name, or if it is not already an instance of #$Thing.
   *
   * @param nameOrId  the string representation or the HLID of the term
   * 
   * @return a new KBTerm
   * 
   * @throws KBTypeException
   * @throws CreateException 
   */
  public static KBTermImpl get(String nameOrId) throws KBTypeException, CreateException {
    return KBObjectFactory.get(nameOrId, KBTermImpl.class);
  }

  /**
   * Get the
   * <code>KBTerm</code> object that corresponds to
   * <code>cycObject</code>. Throws exceptions if the object isn't in the KB, or if
   * it's not already an instance of
   * <code>#$Thing</code>.
   *
   * @param cycObject the CycObject wrapped by KBTerm. The method
   * verifies that the CycObject is an #$Thing
   * 
   * @return a new KBTerm
   * 
   * @throws KBTypeException
   * @throws CreateException 
   */
  @Deprecated
  public static KBTermImpl get(CycObject cycObject) throws KBTypeException, CreateException {
    return KBObjectFactory.get(cycObject, KBTermImpl.class);
  }

  /**
   * Find or create a
   * <code>KBTerm</code> object named
   * <code>nameOrId</code>. If no object exists in the KB with the name
   * <code>nameOrId</code>, one will be created, and it will be asserted to be
   * an instance of
   * <code>#$Thing</code>. If there is already an object in the KB called
   * <code>nameOrId</code>, it will be returned.
   *
   * @param nameOrId the string representation or the HLID of the term
   * 
   * @return  a new KBTerm
   * 
   * @throws KBTypeException 
   * @throws CreateException 
   */
  public static KBTermImpl findOrCreate(String nameOrId) throws CreateException, KBTypeException {
    return KBObjectFactory.findOrCreate(nameOrId, KBTermImpl.class);
  }

  /**
   * Find or create a KBTerm object from
   * <code>cycObject</code>. If
   * <code>cycObject</code> exists in the KB, an appropriate
   * <code>KBTerm</code> object will be returned.
   *
   * @param cycObject the CycObject wrapped by KBTerm. The method
   * verifies that the CycObject is an #$Thing
   * 
   * @return  a new KBTerm
   * 
   * @throws KBTypeException 
   * @throws CreateException 
   */
  @Deprecated
  public static KBTermImpl findOrCreate(CycObject cycObject) throws CreateException, KBTypeException {
    return KBObjectFactory.findOrCreate(cycObject, KBTermImpl.class);
  }

  /**
   * Find or create a
   * <code>KBTerm</code> object named
   * <code>nameOrId</code>, and also make it an instance of
   * <code>constraintCol</code> in the default context specified by
   * {@link KBAPIDefaultContext#forAssertion()}. If no object
   * exists in the KB with the name
   * <code>nameOrId</code>, one will be created, and it will be asserted to be
   * an instance of
   * <code>constraintCol</code>. If there is already an object in the
   * KB called
   * <code>nameOrId</code>, and it is already a
   * <code>constraintCol</code>, it will be returned. If it is not
   * already a
   * <code>constraintCol</code>, but can be made so by addition of
   * assertions to the KB, such assertions will be made, and the object will be
   * returned. If the object in the KB cannot be turned into a
   * <code>constraintCol</code> by adding assertions, a
   * <code>KBTypeConflictException</code>will be thrown.
   *
   * @param nameOrId  the string representation or the HLID of the term
   * @param constraintCol the collection that this term will instantiate
   * 
   * @return a new KBTerm
   * 
   * @throws KBTypeException 
   * @throws CreateException 
   */
  public static KBTerm findOrCreate(String nameOrId, KBCollection constraintCol) 
      throws CreateException, KBTypeException {
    return KBObjectFactory.findOrCreate(nameOrId, constraintCol, KBTermImpl.class);
  }

  /**
   * Find or create a
   * <code>KBTerm</code> object named
   * <code>nameOrId</code>, and also make it an instance of
   * <code>constraintCol</code> in the default context specified by
   * {@link KBAPIDefaultContext#forAssertion()}. If no object
   * exists in the KB with the name
   * <code>nameOrId</code>, one will be created, and it will be asserted to be
   * an instance of
   * <code>constraintCol</code>. If there is already an object in the
   * KB called
   * <code>nameOrId</code>, and it is already a
   * <code>constraintCol</code>, it will be returned. If it is not
   * already a
   * <code>constraintCol</code>, but can be made so by addition of
   * assertions to the KB, such assertions will be made, and the object will be
   * returned. If the object in the KB cannot be turned into a
   * <code>constraintCol</code> by adding assertions, a
   * <code>KBTypeConflictException</code>will be thrown.
   *
   * @param nameOrId  the string representation or the HLID of the term
   * @param constraintColStr the string representation of the collection that 
   * this term will instantiate
   * 
   * @return a new KBTerm
   * 
   * @throws KBTypeException 
   * @throws CreateException 
   */
  public static KBTerm findOrCreate(String nameOrId, String constraintColStr) 
      throws CreateException, KBTypeException {
    return KBObjectFactory.findOrCreate(nameOrId, constraintColStr, KBTermImpl.class);
  }

  /**
   * Find or create a
   * <code>KBTerm</code> object named
   * <code>nameOrId</code>, and also make it an instance of
   * <code>constraintCol</code> in
   * <code>ctx</code>. If no object exists in the KB with the name
   * <code>nameOrId</code>, one will be created, and it will be asserted to be
   * an instance of
   * <code>constraintCol</code>. If there is already an object in the
   * KB called
   * <code>nameOrId</code>, and it is already a
   * <code>constraintCol</code>, it will be returned. If it is not
   * already a
   * <code>constraintCol</code>, but can be made so by addition of
   * assertions to the KB, such assertions will be made, and the object will be
   * returned. If the object in the KB cannot be turned into a
   * <code>constraintCol</code> by adding assertions, a
   * <code>KBTypeConflictException</code>will be thrown.
   *
   * @param nameOrId  the string representation or the HLID of the term
   * @param constraintCol the collection that this term will instantiate
   * @param ctx the context in which the resulting object must be an instance of
   * constraintCol
   * 
   * @return a new KBTerm
   * 
   * @throws KBTypeException 
   * @throws CreateException 
   */
  public static KBTerm findOrCreate(String nameOrId, KBCollection constraintCol, Context ctx) 
      throws CreateException, KBTypeException{
    return KBObjectFactory.findOrCreate(nameOrId, constraintCol, ctx, KBTermImpl.class);
  }

  /**
   * Find or create a
   * <code>KBTerm</code> object named
   * <code>nameOrId</code>, and also make it an instance of
   * <code>constraintCol</code> in
   * <code>ctx</code>. If no object exists in the KB with the name
   * <code>nameOrId</code>, one will be created, and it will be asserted to be
   * an instance of
   * <code>constraintCol</code>. If there is already an object in the
   * KB called
   * <code>nameOrId</code>, and it is already a
   * <code>constraintCol</code>, it will be returned. If it is not
   * already a
   * <code>constraintCol</code>, but can be made so by addition of
   * assertions to the KB, such assertions will be made, and the object will be
   * returned. If the object in the KB cannot be turned into a
   * <code>constraintCol</code> by adding assertions, a
   * <code>KBTypeConflictException</code>will be thrown.
   *
   * @param nameOrId the string representation or the HLID of the term
   * @param constraintColStr the string representation of the collection that 
   * this term will instantiate
   * @param ctxStr the string representation of the context in which the 
   * resulting object must be an instance of constraintCol
   * 
   * @return a new KBTerm
   * 
   * @throws KBTypeException 
   * @throws CreateException 
   */
  public static KBTerm findOrCreate(String nameOrId, String constraintColStr, String ctxStr) 
      throws CreateException, KBTypeException {
    return KBObjectFactory.findOrCreate(nameOrId, constraintColStr, ctxStr, KBTermImpl.class);
  }

  /**
   * Checks whether entity exists in KB and is an instance of #$Thing. If
   * false, {@link #getStatus(String)} may yield more information. This method
   * is equivalent to
   * <code>getStatus(nameOrId).equals(KBStatus.EXISTS_AS_TYPE)</code>.
   *
   * @param nameOrId either the name or HL ID of an entity in the KB
   * @return <code>true</code> if entity exists in KB and is an instance of
   * #$Thing
   */
  public static boolean existsAsType(String nameOrId)  {
    return getStatus(nameOrId).equals(KBStatus.EXISTS_AS_TYPE);
  }

  /**
   * Checks whether entity exists in KB and is an instance of #$Thing. If
   * false, {@link #getStatus(CycObject)} may yield more information. This
   * method is equivalent to
   * <code>getStatus(object).equals(KBStatus.EXISTS_AS_TYPE)</code>.
   *
   * @param cycObject the CycObject representation of a KB entity
   * @return <code>true</code> if entity exists in KB and is an instance of
   * #$Thing
   */
  public static boolean existsAsType(CycObject cycObject)  {
    return getStatus(cycObject).equals(KBStatus.EXISTS_AS_TYPE);
  }

  /**
   * Returns an KBStatus enum which describes whether
   * <code>nameOrId</code> exists in the KB and is an instance of
   * <code>#$Thing</code>.
   *
   * @param nameOrId either the name or HL ID of an entity in the KB
   * @return an enum describing the existential status of the entity in the KB
   */
  public static KBStatus getStatus(String nameOrId)  {
    return KBObjectFactory.getStatus(nameOrId, KBTermImpl.class);

  }

  /**
   * Returns an KBStatus enum which describes whether
   * <code>object</code> exists in the KB and is an instance of
   * <code>#$Thing</code>.
   *
   * @param cycObject the CycObject representation of a KB entity
   * @return an enum describing the existential status of the entity in the KB
   */
  public static KBStatus getStatus(CycObject cycObject)  {
    return KBObjectFactory.getStatus(cycObject, KBTermImpl.class);
  }

  
  /* (non-Javadoc)
   * @see com.cyc.kb.KBTerm#provablyNotInstanceOf(com.cyc.kb.KBCollectionImpl, com.cyc.kb.ContextImpl)
   */
  @Override
  public boolean provablyNotInstanceOf(KBCollection col, Context ctx) {
    try {
      return getAccess().getInspectorTool().isa(this.getCore(), col.getCore(), ctx.getCore());
    } catch (CycConnectionException e) {
      throw new KBApiRuntimeException(e.getMessage(), e);
    }
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBTerm#provablyNotInstanceOf(java.lang.String, java.lang.String)
   */
  @Override
  public boolean provablyNotInstanceOf(String colStr, String ctxStr) {
    ContextImpl ctx;
    KBCollectionImpl col;
    try {
      ctx = ContextImpl.get(ctxStr);
      col = KBCollectionImpl.get(colStr);
    } catch (KBApiException kae){
      throw new IllegalArgumentException(kae.getMessage(), kae);
    }
    return provablyNotInstanceOf(col, ctx);
  }

  @Override
  public KBIndividual getCreator() {
    try {
      if (this.getCore() instanceof Fort) {
        Fort cyclist = getAccess().getLookupTool().getTermCreator((Fort) this.getCore());
        return KBIndividualImpl.get(cyclist);
      } else {
        return null;
      }
    } catch (Exception e) {
      return null;
    }
  }
  
  @Override
  public Date getCreationDate() {
    try {
      if (this.getCore() instanceof Fort) {
        return getAccess().getLookupTool().getTermCreationDate((Fort) this.getCore());
      } else {
        return null;
      }
    } catch (Exception e) {
      return null;
    }
  }
  
  /**
   * Return the KBCollection as a KBObject of the Cyc term that 
   * underlies this class. 
   * 
   * @return KBCollectionImpl.get("#$Thing");
   */
  @Override
  public KBObject getType() {
    return getClassType();
  }
  
  /**
   * Return the KBCollection as a KBObject of the Cyc term that 
   * underlies this class. 
   * 
   * @return KBCollectionImpl.get("#$Thing");
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
    return "#$Thing";
  }
}
