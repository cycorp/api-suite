package com.cyc.kb;

/*
 * #%L
 * File: BinaryPredicateFactory.java
 * Project: Core API Object Factories
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

import com.cyc.core.service.CoreServicesLoader;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbTypeException;
import com.cyc.kb.spi.BinaryPredicateService;

/**
 *
 * @author nwinant
 */
public class BinaryPredicateFactory {
  
  // Static
  
  private static final BinaryPredicateFactory ME = new BinaryPredicateFactory();

  protected static BinaryPredicateFactory getInstance() {
    return ME;
  }
  
    
  // Fields
    
  private final BinaryPredicateService service;

  
  // Construction
  
  private BinaryPredicateFactory() {
    service = CoreServicesLoader.getKbFactoryServices().getBinaryPredicateService();
  }

  protected BinaryPredicateService getService() {
    return service;
  }
  
  
  // Public
  
  /**
   * Get the <code>BinaryPredicate</code> with the name <code>nameOrId</code>. Throws exceptions if
   * there is no KB term by that name, or if it is not already an instance of #$BinaryPredicate.
   *
   * @param nameOrId the string representation or the HLID of the #$BinaryPredicate
   *
   * @return a new BinaryPredicate
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  public static BinaryPredicate get(String nameOrId) throws KbTypeException, CreateException {
    return getInstance().getService().get(nameOrId);
  }

  /**
   * Find or create a <code>BinaryPredicate</code> object named <code>nameOrId</code>. If no object
   * exists in the KB with the name <code>nameOrId</code>, one will be created, and it will be
   * asserted to be an instance of <code>#$BinaryPredicate</code>. If there is already an object in
   * the KB called <code>nameOrId</code>, and it is already a <code>#$BinaryPredicate</code>, it
   * will be returned. If it is not already a <code>#$BinaryPredicate</code>, but can be made into
   * one by addition of assertions to the KB, such assertions will be made, and the object will be
   * returned. If the object in the KB cannot be turned into a <code>#$BinaryPredicate</code> by
   * adding assertions (i.e. some existing assertion prevents it from being a
   * <code>#$BinaryPredicate</code>), a <code>KbTypeConflictException</code>will be thrown.
   *
   * @param nameOrId the string representation or the HLID of the #$BinaryPredicate
   *
   * @return a new BinaryPredicate
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  public static BinaryPredicate findOrCreate(String nameOrId)
          throws CreateException, KbTypeException {
    return getInstance().getService().findOrCreate(nameOrId);
  }

  /**
   * Find or create a <code>BinaryPredicate</code> object named <code>nameOrId</code>, and also make
   * it an instance of <code>constraintCol</code> in the default context specified by
   * {@link DefaultContext#forAssertion()}. If no object exists in the KB with the name
   * <code>nameOrId</code>, one will be created, and it will be asserted to be an instance of both
   * <code>#$BinaryPredicate</code> and <code>constraintCol</code>. If there is already an object in
   * the KB called <code>nameOrId</code>, and it is already both a <code>#$BinaryPredicate</code>
   * and a <code>constraintCol</code>, it will be returned. If it is not already both a
   * <code>#$BinaryPredicate</code> and a <code>constraintCol</code>, but can be made so by addition
   * of assertions to the KB, such assertions will be made, and the object will be returned. If the
   * object in the KB cannot be turned into both a <code>#$BinaryPredicate</code> and a
   * <code>constraintCol</code> by adding assertions, a <code>KbTypeConflictException</code>will be
   * thrown.
   *
   * @param nameOrId the string representation or the HLID of the #$BinaryPredicate
   * @param constraintColStr the string representation of the collection that this #$BinaryPredicate
   * will instantiate
   *
   * @return a new BinaryPredicate
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  public static BinaryPredicate findOrCreate(String nameOrId, String constraintColStr)
          throws CreateException, KbTypeException {
    return getInstance().getService().findOrCreate(nameOrId, constraintColStr);
  }

  /**
   * Find or create a <code>BinaryPredicate</code> object named <code>nameOrId</code>, and also make
   * it an instance of <code>constraintCol</code> in <code>ctx</code>. If no object exists in the KB
   * with the name <code>nameOrId</code>, one will be created, and it will be asserted to be an
   * instance of both <code>#$BinaryPredicate</code> and <code>constraintCol</code>. If there is
   * already an object in the KB called <code>nameOrId</code>, and it is already both a
   * <code>#$BinaryPredicate</code> and a <code>constraintCol</code>, it will be returned. If it is
   * not already both a <code>#$BinaryPredicate</code> and a <code>constraintCol</code>, but can be
   * made so by addition of assertions to the KB, such assertions will be made, and the object will
   * be returned. If the object in the KB cannot be turned into both a
   * <code>#$BinaryPredicate</code> and a <code>constraintCol</code> by adding assertions, a
   * <code>KbTypeConflictException</code>will be thrown.
   *
   * @param nameOrId the string representation or the HLID of the term
   * @param constraintColStr the string representation of the collection that this #$BinaryPredicate
   * will instantiate
   * @param ctxStr the context in which the resulting object must be an instance of constraintCol
   *
   * @return a new BinaryPredicate
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  public static BinaryPredicate findOrCreate(String nameOrId, String constraintColStr, 
          String ctxStr) throws CreateException, KbTypeException {
    return getInstance().getService().findOrCreate(nameOrId, constraintColStr, ctxStr);
  }

  /**
   * Find or create a <code>BinaryPredicate</code> object named <code>nameOrId</code>, and also make
   * it an instance of <code>constraintCol</code> in the default context specified by
   * {@link DefaultContext#forAssertion()}. If no object exists in the KB with the name
   * <code>nameOrId</code>, one will be created, and it will be asserted to be an instance of both
   * <code>#$BinaryPredicate</code> and <code>constraintCol</code>. If there is already an object in
   * the KB called <code>nameOrId</code>, and it is already both a <code>#$BinaryPredicate</code>
   * and a <code>constraintCol</code>, it will be returned. If it is not already both a
   * <code>#$BinaryPredicate</code> and a <code>constraintCol</code>, but can be made so by addition
   * of assertions to the KB, such assertions will be made, and the object will be returned. If the
   * object in the KB cannot be turned into both a <code>#$BinaryPredicate</code> and a
   * <code>constraintCol</code> by adding assertions, a <code>KbTypeConflictException</code>will be
   * thrown.
   *
   * @param nameOrId the string representation or the HLID of the #$BinaryPredicate
   * @param constraintCol the collection that this #$BinaryPredicate will instantiate
   *
   * @return a new BinaryPredicate
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  public static BinaryPredicate findOrCreate(String nameOrId, KbCollection constraintCol)
          throws CreateException, KbTypeException {
    return getInstance().getService().findOrCreate(nameOrId, constraintCol);
  }

  /**
   * Find or create a <code>BinaryPredicate</code> object named <code>nameOrId</code>, and also make
   * it an instance of <code>constraintCol</code> in <code>ctx</code>. If no object exists in the KB
   * with the name <code>nameOrId</code>, one will be created, and it will be asserted to be an
   * instance of both <code>#$BinaryPredicate</code> and <code>constraintCol</code>. If there is
   * already an object in the KB called <code>nameOrId</code>, and it is already both a
   * <code>#$BinaryPredicate</code> and a <code>constraintCol</code>, it will be returned. If it is
   * not already both a <code>#$BinaryPredicate</code> and a <code>constraintCol</code>, but can be
   * made so by addition of assertions to the KB, such assertions will be made, and the object will
   * be returned. If the object in the KB cannot be turned into both a
   * <code>#$BinaryPredicate</code> and a <code>constraintCol</code> by adding assertions, a
   * <code>KbTypeConflictException</code>will be thrown.
   *
   * @param nameOrId the string representation or the HLID of the #$BinaryPredicate
   * @param constraintCol the collection that this #$BinaryPredicate will instantiate
   * @param ctx the context in which the resulting object must be an instance of constraintCol
   *
   * @return a new BinaryPredicate
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  public static BinaryPredicate findOrCreate(String nameOrId, KbCollection constraintCol, 
          Context ctx) throws CreateException, KbTypeException {
    return getInstance().getService().findOrCreate(nameOrId, constraintCol, ctx);
  }

  /**
   * Checks whether entity exists in KB and is an instance of #$BinaryPredicate. If false,
   * {@link #getStatus(String)} may yield more information. This method is equivalent to
   * <code>getStatus(nameOrId).equals(KbStatus.EXISTS_AS_TYPE)</code>.
   *
   * @param nameOrId either the name or HL ID of an entity in the KB
   * @return <code>true</code> if entity exists in KB and is an instance of #$BinaryPredicate
   */
  public static boolean existsAsType(String nameOrId) {
    return getInstance().getService().existsAsType(nameOrId);
  }
  
  /**
   * Returns an KbStatus enum which describes whether <code>nameOrId</code> exists in the KB and is
   * an instance of <code>#$BinaryPredicate</code>.
   *
   * @param nameOrId either the name or HL ID of an entity in the KB
   * @return an enum describing the existential status of the entity in the KB
   */
  public static KbStatus getStatus(String nameOrId) {
    return getInstance().getService().getStatus(nameOrId);
  }
  
}
