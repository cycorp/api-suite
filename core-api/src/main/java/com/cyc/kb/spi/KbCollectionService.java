package com.cyc.kb.spi;

/*
 * #%L
 * File: KbCollectionService.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2019 Cycorp, Inc
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
   * Get the <code>KbCollection</code> with the name <code>nameOrId</code>. Throws exceptions if
   * there is no KB term by that name, or if it is not already an instance of #$Collection.
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
   * Find or create a <code>KbCollection</code> object named <code>nameOrId</code>. If no object
   * exists in the KB with the name <code>nameOrId</code>, one will be created, and it will be
   * asserted to be an instance of <code>#$Collection</code>. If there is already an object in the
   * KB called <code>nameOrId</code>, and it is already a <code>#$Collection</code>, it will be
   * returned. If it is not already a <code>#$Collection</code>, but can be made into one by
   * addition of assertions to the KB, such assertions will be made, and the object will be
   * returned. If the object in the KB cannot be turned into a <code>#$Collection</code> by adding
   * assertions (i.e. some existing assertion prevents it from being a <code>#$Collection</code>), a
   * <code>KbTypeConflictException</code>will be thrown.
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
   * Find or create a <code>KbCollection</code> object named <code>nameOrId</code>, and also make it
   * an instance of <code>constraintCol</code> in the default context specified by
   * {@link DefaultContext#forAssertion()}. If no object exists in the KB with the name
   * <code>nameOrId</code>, one will be created, and it will be asserted to be an instance of both
   * <code>#$Collection</code> and <code>constraintCol</code>. If there is already an object in the
   * KB called <code>nameOrId</code>, and it is already both a <code>#$Collection</code> and a
   * <code>constraintCol</code>, it will be returned. If it is not already both a
   * <code>#$Collection</code> and a <code>constraintCol</code>, but can be made so by addition of
   * assertions to the KB, such assertions will be made, and the object will be returned. If the
   * object in the KB cannot be turned into both a <code>#$Collection</code> and a
   * <code>constraintCol</code> by adding assertions, a <code>KbTypeConflictException</code>will be
   * thrown.
   *
   * @param nameOrId         the string representation or the HLID of the #$Collection
   * @param constraintColStr the string representation of the collection that <code>this</code>
   *                         #$Collection will instantiate
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
   * Find or create a <code>KbCollection</code> object named <code>nameOrId</code>, and also make it
   * an instance of <code>constraintCol</code> in <code>ctx</code>. If no object exists in the KB
   * with the name <code>nameOrId</code>, one will be created, and it will be asserted to be an
   * instance of both <code>#$Collection</code> and <code>constraintCol</code>. If there is already
   * an object in the KB called <code>nameOrId</code>, and it is already both a
   * <code>#$Collection</code> and a <code>constraintCol</code>, it will be returned. If it is not
   * already both a <code>#$Collection</code> and a <code>constraintCol</code>, but can be made so
   * by addition of assertions to the KB, such assertions will be made, and the object will be
   * returned. If the object in the KB cannot be turned into both a <code>#$Collection</code> and a
   * <code>constraintCol</code> by adding assertions, a <code>KbTypeConflictException</code>will be
   * thrown.
   *
   * @param nameOrId         the string representation or the HLID of the term
   * @param constraintColStr the string representation of the collection that <code>this</code>
   *                         #$Collection will instantiate
   * @param ctxStr           the context in which the resulting object must be an instance of
   *                         constraintCol
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
   * Find or create a <code>KbCollection</code> object named <code>nameOrId</code>, and also make it
   * an instance of <code>constraintCol</code> in the default context specified by
   * {@link DefaultContext#forAssertion()}. If no object exists in the KB with the name
   * <code>nameOrId</code>, one will be created, and it will be asserted to be an instance of both
   * <code>#$Collection</code> and <code>constraintCol</code>. If there is already an object in the
   * KB called <code>nameOrId</code>, and it is already both a <code>#$Collection</code> and a
   * <code>constraintCol</code>, it will be returned. If it is not already both a
   * <code>#$Collection</code> and a <code>constraintCol</code>, but can be made so by addition of
   * assertions to the KB, such assertions will be made, and the object will be returned. If the
   * object in the KB cannot be turned into both a <code>#$Collection</code> and a
   * <code>constraintCol</code> by adding assertions, a <code>KbTypeConflictException</code>will be
   * thrown.
   *
   * @param nameOrId      the string representation or the HLID of the #$Collection
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
   * Find or create a <code>KbCollection</code> object named <code>nameOrId</code>, and also make it
   * an instance of <code>constraintCol</code> in <code>ctx</code>. If no object exists in the KB
   * with the name <code>nameOrId</code>, one will be created, and it will be asserted to be an
   * instance of both <code>#$Collection</code> and <code>constraintCol</code>. If there is already
   * an object in the KB called <code>nameOrId</code>, and it is already both a
   * <code>#$Collection</code> and a <code>constraintCol</code>, it will be returned. If it is not
   * already both a <code>#$Collection</code> and a <code>constraintCol</code>, but can be made so
   * by addition of assertions to the KB, such assertions will be made, and the object will be
   * returned. If the object in the KB cannot be turned into both a <code>#$Collection</code> and a
   * <code>constraintCol</code> by adding assertions, a <code>KbTypeConflictException</code>will be
   * thrown.
   *
   * @param nameOrId      the string representation or the HLID of the #$Collection
   * @param constraintCol the collection that <code>this</code> #$Collection will instantiate
   * @param ctx           the context in which the resulting object must be an instance of
   *                      constraintCol
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
   * Checks whether nameOrId exists in KB and is an instance of #$Collection. If false,
   * {@link #getStatus(String)} may yield more information. This method is equivalent to
   * <code>getStatus(nameOrId).equals(KbStatus.EXISTS_AS_TYPE)</code>.
   *
   * @param nameOrId either the name or HL ID of an entity in the KB
   *
   * @return <code>true</code> if entity exists in KB and is an instance of #$Collection
   */
  @Override
  boolean existsAsType(String nameOrId);

  /**
   * Returns a KbStatus enum which describes whether <code>nameOrId</code> exists in the KB and is
   * an instance of <code>#$Collection</code>.
   *
   * @param nameOrId either the name or HL ID of an entity in the KB
   *
   * @return an enum describing the existential status of the entity in the KB
   */
  @Override
  KbStatus getStatus(String nameOrId);

}
