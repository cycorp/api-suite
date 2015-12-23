package com.cyc.kb.client;

/*
 * #%L
 * File: BinaryPredicateImpl.java
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
import com.cyc.base.cycobject.DenotationalTerm;
import com.cyc.base.cycobject.Guid;
import com.cyc.baseclient.cycobject.CycConstantImpl;
import com.cyc.kb.BinaryPredicate;
import com.cyc.kb.Context;
import com.cyc.kb.KbCollection;
import com.cyc.kb.KbStatus;
import com.cyc.kb.client.LookupType;
import com.cyc.kb.KbObject;
import com.cyc.kb.client.config.KbDefaultContext;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.InvalidNameException;
import com.cyc.kb.exception.KbException;
import com.cyc.kb.exception.KbRuntimeException;
import com.cyc.kb.exception.KbObjectNotFoundException;
import com.cyc.kb.exception.KbTypeConflictException;
import com.cyc.kb.exception.KbTypeException;

/**
 * A <code>BinaryPredicate</code> object is a facade for a 
 * <code>#$BinaryPredicate</code> in Cyc KB.
 * 
 * A #$BinaryPredicate is a specialization of #$Predicate. It is a collection of
 * predicates that take only two arguments. Binary predicates are the most common
 * type of predicate in the Cyc KB and terser methods are provided for binary predicates in KBObject. 
 * So we have a class to support strongly typing such predicates. 
 * 
 * @author Vijay Raj
 * @version $Id: BinaryPredicateImpl.java 163117 2015-12-11 00:27:39Z nwinant $
 */
public class BinaryPredicateImpl extends KbPredicateImpl implements BinaryPredicate {

  private static final DenotationalTerm TYPE_CORE =
          new CycConstantImpl("BinaryPredicate", new Guid("bd588102-9c29-11b1-9dad-c379636f7270"));

  static DenotationalTerm getClassTypeCore() {
    return TYPE_CORE;
  }
  
  /**
   * Not part of the KB API. This default constructor only has the effect of
   * ensuring that there is access to a Cyc server.
   */
  BinaryPredicateImpl() {
    super();
  }

  /**
   * Not part of the KB API. An implementation-dependent constructor.
   * <p>
   * It is used when the result of query is a CycObject and is known to be or
   * requested to be cast as an instance of BinaryPredicate.
   *
   * @param cycObject the CycObject wrapped by <code>BinaryPredicate</code>. The constructor
   * verifies that the CycObject is an instance of #$BinaryPredicate
   * 
   * @throws KbTypeException if cycObject is not or could not be made 
   * an instance of #$BinaryPredicate
   */
  @Deprecated
  BinaryPredicateImpl(CycObject cycObject) throws KbTypeException {
    super(cycObject);
  }

  /**
   * This not part of the public, supported KB API. finds or creates an instance of #$BinaryPredicate represented
   * by binPredStr in the underlying KB
   * <p>
   *
   * @param binPredStr  the string representing an instance of #$BinaryPredicate in the KB
   * 
   * @throws CreateException if the #$BinaryPredicate represented by binPredStr is not found
   * and could not be created 
   * @throws KbTypeException if the term represented by binPredStr is not an instance
   * of #$BinaryPredicate and cannot be made into one. 
   */
  BinaryPredicateImpl(String binPredStr) throws KbTypeException, CreateException {
    super(binPredStr);
  }

  /**
   * This not part of the public, supported KB API. finds or creates; or finds an instance of #$BinaryPredicate
   * represented by binPredStr in the underlying KB based on input ENUM
   * <p>
   *
   * @param binPredStr  the string representing an instance of #$BinaryPredicate in the KB
   * @param lookup the enum to specify LookupType: FIND or FIND_OR_CREATE
   * 
   * @throws CreateException 
   * @throws KbTypeException 
   *
   * @throws KbObjectNotFoundException  if the #$BinaryPredicate represented by binPredStr
   * is not found and could not be created
   * @throws InvalidNameException if the string binPredStr does not conform to Cyc constant-naming
   * conventions
   * 
   * @throws KbTypeException  if the term represented by binPredStr is not an instance of #$BinaryPredicate and lookup is
   * set to find only {@link LookupType#FIND}
   * @throws KbTypeConflictException if the term represented by binPredStr is not an instance of #$BinaryPredicate,
   * and lookup is set to find or create; and if the term cannot be made an instance #$BinaryPredicate by asserting
   * new knowledge.
   */
  BinaryPredicateImpl(String binPredStr, LookupType lookup) throws KbTypeException, CreateException {
    super(binPredStr, lookup);
  }

  /**
   * Get the
   * <code>BinaryPredicate</code> with the name
   * <code>nameOrId</code>. Throws exceptions if there is no KB term by that
   * name, or if it is not already an instance of #$BinaryPredicate.
   *
   * @param nameOrId the string representation or the HLID of the #$BinaryPredicate
   * 
   * @return  a new BinaryPredicate
   * 
   * @throws KbTypeException
   * @throws CreateException 
   */
  public static BinaryPredicateImpl get(String nameOrId) throws KbTypeException, CreateException {
    return KbObjectFactory.get(nameOrId, BinaryPredicateImpl.class);
  }

  /**
   * Get the
   * <code>BinaryPredicate</code> object that corresponds to
   * <code>cycObject</code>. Throws exceptions if the object isn't in the KB, or if
   * it's not already an instance of
   * <code>#$BinaryPredicate</code>.
   *
   * @param cycObject the CycObject wrapped by BinaryPredicate. The method
   * verifies that the CycObject is an instance of #$BinaryPredicate
   * 
   * @return a new BinaryPredicate 
   * 
   * @throws CreateException 
   * @throws KbTypeException
   */
  @Deprecated
  public static BinaryPredicateImpl get(CycObject cycObject) throws KbTypeException, CreateException {
    return KbObjectFactory.get(cycObject, BinaryPredicateImpl.class);
  }

  /**
   * Find or create a
   * <code>BinaryPredicate</code> object named
   * <code>nameOrId</code>. If no object exists in the KB with the name
   * <code>nameOrId</code>, one will be created, and it will be asserted to be
   * an instance of
   * <code>#$BinaryPredicate</code>. If there is already an object in the KB called
   * <code>nameOrId</code>, and it is already a
   * <code>#$BinaryPredicate</code>, it will be returned. If it is not already a
   * <code>#$BinaryPredicate</code>, but can be made into one by addition of
   * assertions to the KB, such assertions will be made, and the object will be
   * returned. If the object in the KB cannot be turned into a
   * <code>#$BinaryPredicate</code> by adding assertions (i.e. some existing
   * assertion prevents it from being a
   * <code>#$BinaryPredicate</code>), a
   * <code>KBTypeConflictException</code>will be thrown.
   *
   * @param nameOrId  the string representation or the HLID of the #$BinaryPredicate
   * 
   * @return a new BinaryPredicate
   * 
   * @throws KbTypeException 
   * @throws CreateException 
   */
  public static BinaryPredicateImpl findOrCreate(String nameOrId) throws CreateException, KbTypeException  {
    return KbObjectFactory.findOrCreate(nameOrId, BinaryPredicateImpl.class);
  }

  /**
   * Find or create a BinaryPredicate object from
   * <code>cycObject</code>. If
   * <code>cycObject</code> is already a
   * <code>#$BinaryPredicate</code>, an appropriate
   * <code>BinaryPredicate</code> object will be returned. If
   * <code>object</code> is not already a
   * <code>#$BinaryPredicate</code>, but can be made into one by addition of
   * assertions to the KB, such assertions will be made, and the relevant object
   * will be returned. If
   * <code>cycObject</code> cannot be turned into a
   * <code>#$BinaryPredicate</code> by adding assertions (i.e. some existing
   * assertion prevents it from being a
   * <code>#$BinaryPredicate</code>, a
   * <code>KBTypeConflictException</code>will be thrown.
   *
   * @param cycObject the CycObject wrapped by BinaryPredicate. The method
   * verifies that the CycObject is an #$BinaryPredicate
   * 
   * @return a new BinaryPredicate
   * 
   * @throws KbTypeException 
   * @throws CreateException 
   */
  @Deprecated
  public static BinaryPredicateImpl findOrCreate(CycObject cycObject) throws CreateException, KbTypeException {
    return KbObjectFactory.findOrCreate(cycObject, BinaryPredicateImpl.class);
  }

  /**
   * Find or create a
   * <code>BinaryPredicate</code> object named
   * <code>nameOrId</code>, and also make it an instance of
   * <code>constraintCol</code> in the default context specified by
   * {@link KBAPIDefaultContext#forAssertion()}. If no object
   * exists in the KB with the name
   * <code>nameOrId</code>, one will be created, and it will be asserted to be
   * an instance of both
   * <code>#$BinaryPredicate</code> and
   * <code>constraintCol</code>. If there is already an object in the
   * KB called
   * <code>nameOrId</code>, and it is already both a
   * <code>#$BinaryPredicate</code> and a
   * <code>constraintCol</code>, it will be returned. If it is not
   * already both a
   * <code>#$BinaryPredicate</code> and a
   * <code>constraintCol</code>, but can be made so by addition of
   * assertions to the KB, such assertions will be made, and the object will be
   * returned. If the object in the KB cannot be turned into both a
   * <code>#$BinaryPredicate</code> and a
   * <code>constraintCol</code> by adding assertions, a
   * <code>KBTypeConflictException</code>will be thrown.
   *
   * @param nameOrId  the string representation or the HLID of the #$BinaryPredicate
   * @param constraintCol the collection that this #$BinaryPredicate will instantiate
   * 
   * @return a new BinaryPredicate
   * 
   * @throws KbTypeException 
   * @throws CreateException 
   */
  public static BinaryPredicateImpl findOrCreate(String nameOrId, KbCollection constraintCol) throws CreateException, KbTypeException {
    return KbObjectFactory.findOrCreate(nameOrId, constraintCol, BinaryPredicateImpl.class);
  }

  /**
   * Find or create a
   * <code>BinaryPredicate</code> object named
   * <code>nameOrId</code>, and also make it an instance of
   * <code>constraintCol</code> in the default context specified by
   * {@link KBAPIDefaultContext#forAssertion()}. If no object
   * exists in the KB with the name
   * <code>nameOrId</code>, one will be created, and it will be asserted to be
   * an instance of both
   * <code>#$BinaryPredicate</code> and
   * <code>constraintCol</code>. If there is already an object in the
   * KB called
   * <code>nameOrId</code>, and it is already both a
   * <code>#$BinaryPredicate</code> and a
   * <code>constraintCol</code>, it will be returned. If it is not
   * already both a
   * <code>#$BinaryPredicate</code> and a
   * <code>constraintCol</code>, but can be made so by addition of
   * assertions to the KB, such assertions will be made, and the object will be
   * returned. If the object in the KB cannot be turned into both a
   * <code>#$BinaryPredicate</code> and a
   * <code>constraintCol</code> by adding assertions, a
   * <code>KBTypeConflictException</code>will be thrown.
   *
   * @param nameOrId  the string representation or the HLID of the #$BinaryPredicate
   * @param constraintColStr the string representation of the collection that 
   * this #$BinaryPredicate will instantiate
   * 
   * @return a new BinaryPredicate
   * 
   * @throws KbTypeException 
   * @throws CreateException 
   */
  public static BinaryPredicateImpl findOrCreate(String nameOrId, String constraintColStr) throws CreateException, KbTypeException {
    return KbObjectFactory.findOrCreate(nameOrId, constraintColStr, BinaryPredicateImpl.class);
  }

  /**
   * Find or create a
   * <code>BinaryPredicate</code> object named
   * <code>nameOrId</code>, and also make it an instance of
   * <code>constraintCol</code> in
   * <code>ctx</code>. If no object exists in the KB with the name
   * <code>nameOrId</code>, one will be created, and it will be asserted to be
   * an instance of both
   * <code>#$BinaryPredicate</code> and
   * <code>constraintCol</code>. If there is already an object in the
   * KB called
   * <code>nameOrId</code>, and it is already both a
   * <code>#$BinaryPredicate</code> and a
   * <code>constraintCol</code>, it will be returned. If it is not
   * already both a
   * <code>#$BinaryPredicate</code> and a
   * <code>constraintCol</code>, but can be made so by addition of
   * assertions to the KB, such assertions will be made, and the object will be
   * returned. If the object in the KB cannot be turned into both a
   * <code>#$BinaryPredicate</code> and a
   * <code>constraintCol</code> by adding assertions, a
   * <code>KBTypeConflictException</code>will be thrown.
   *
   * @param nameOrId  the string representation or the HLID of the #$BinaryPredicate
   * @param constraintCol the collection that this #$BinaryPredicate will instantiate
   * @param ctx the context in which the resulting object must be an instance of
   * constraintCol
   * 
   * @return a new BinaryPredicate
   * 
   * @throws KbTypeException 
   * @throws CreateException 
   */
  public static BinaryPredicateImpl findOrCreate(String nameOrId, KbCollection constraintCol, Context ctx) 
      throws CreateException, KbTypeException {
    return KbObjectFactory.findOrCreate(nameOrId, constraintCol, ctx, BinaryPredicateImpl.class);
  }

  /**
   * Find or create a
   * <code>BinaryPredicate</code> object named
   * <code>nameOrId</code>, and also make it an instance of
   * <code>constraintCol</code> in
   * <code>ctx</code>. If no object exists in the KB with the name
   * <code>nameOrId</code>, one will be created, and it will be asserted to be
   * an instance of both
   * <code>#$BinaryPredicate</code> and
   * <code>constraintCol</code>. If there is already an object in the
   * KB called
   * <code>nameOrId</code>, and it is already both a
   * <code>#$BinaryPredicate</code> and a
   * <code>constraintCol</code>, it will be returned. If it is not
   * already both a
   * <code>#$BinaryPredicate</code> and a
   * <code>constraintCol</code>, but can be made so by addition of
   * assertions to the KB, such assertions will be made, and the object will be
   * returned. If the object in the KB cannot be turned into both a
   * <code>#$BinaryPredicate</code> and a
   * <code>constraintCol</code> by adding assertions, a
   * <code>KBTypeConflictException</code>will be thrown.
   *
   * @param nameOrId the string representation or the HLID of the term
   * @param constraintColStr the string representation of the collection that 
   * this #$BinaryPredicate will instantiate
   * @param ctxStr the context in which the resulting object must be an instance of
   * constraintCol
   * 
   * @return a new BinaryPredicate 
   * 
   * @throws KbTypeException 
   * @throws CreateException 
   */
  public static BinaryPredicateImpl findOrCreate(String nameOrId, String constraintColStr, String ctxStr) throws CreateException, KbTypeException {
    return KbObjectFactory.findOrCreate(nameOrId, constraintColStr, ctxStr, BinaryPredicateImpl.class);
  }

  /**
   * Checks whether entity exists in KB and is an instance of #$BinaryPredicate. If
   * false, {@link #getStatus(String)} may yield more information. This method
   * is equivalent to
   * <code>getStatus(nameOrId).equals(KBStatus.EXISTS_AS_TYPE)</code>.
   *
   * @param nameOrId either the name or HL ID of an entity in the KB
   * @return <code>true</code> if entity exists in KB and is an instance of
   * #$BinaryPredicate
   */
  public static boolean existsAsType(String nameOrId) {
    return getStatus(nameOrId).equals(KbStatus.EXISTS_AS_TYPE);
  }

  /**
   * Checks whether entity exists in KB and is an instance of #$BinaryPredicate. If
   * false, {@link #getStatus(CycObject)} may yield more information. This
   * method is equivalent to
   * <code>getStatus(object).equals(KBStatus.EXISTS_AS_TYPE)</code>.
   *
   * @param cycObject the CycObject representation of a KB entity
   * @return <code>true</code> if entity exists in KB and is an instance of
   * #$BinaryPredicate
   */
  public static boolean existsAsType(CycObject cycObject) {
    return getStatus(cycObject).equals(KbStatus.EXISTS_AS_TYPE);
  }

  /**
   * Returns an KBStatus enum which describes whether
   * <code>nameOrId</code> exists in the KB and is an instance of
   * <code>#$BinaryPredicate</code>.
   *
   * @param nameOrId either the name or HL ID of an entity in the KB
   * @return an enum describing the existential status of the entity in the KB
   */
  public static KbStatus getStatus(String nameOrId) {
    return KbObjectFactory.getStatus(nameOrId, BinaryPredicateImpl.class);
  }

  /**
   * Returns an KBStatus enum which describes whether
   * <code>cycObject</code> exists in the KB and is an instance of
   * <code>#$BinaryPredicate</code>.
   *
   * @param cycObject the CycObject representation of a KB entity
   * @return an enum describing the existential status of the entity in the KB
   */
  public static KbStatus getStatus(CycObject cycObject) {
    return KbObjectFactory.getStatus(cycObject, BinaryPredicateImpl.class);
  }

  /**
   * Return the KBCollection as a KBObject of the Cyc term that 
   * underlies this class. 
   * 
   * @return KBCollectionImpl.get("#$BinaryPredicate");
   */
  @Override
  public KbObject getType() {
    return getClassType();
  }
  
  /**
   * Return the KBCollection as a KBObject of the Cyc term that 
   * underlies this class. 
   * 
   * @return KBCollectionImpl.get("#$BinaryPredicate");
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
    return "#$BinaryPredicate";
  }

}
