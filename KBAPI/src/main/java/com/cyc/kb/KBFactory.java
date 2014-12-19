package com.cyc.kb;

/*
 * #%L
 * File: KBFactory.java
 * Project: KB API
 * %%
 * Copyright (C) 2013 - 2014 Cycorp, Inc
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
import com.cyc.kb.exception.InvalidNameException;
import com.cyc.kb.exception.KBTypeException;
import com.cyc.kb.exception.KBApiException;
import com.cyc.kb.exception.KBTypeConflictException;

/**
 * The root interface for all KB API object factories.
 * 
 * @author nwinant
 * @param <T> The class of Objects that this factory will create
 */
public interface KBFactory<T extends KBObject> {
  
  /**
   * Finds an entity in the KB, by either name or HL ID, and returns it wrapped in
   * a semantic object. 
   * Unlike {@link #findOrCreate(String)}, this method will never modify the
   * contents of the KB.
   * 
   * <p>When searching, preference is given to HL ID. In the unlikely event that 
   * there are two relevant entities in the KB, one with a matching HL ID and the
   * other with a matching name, the entity with the matching HL ID will be returned.
   * 
   * @param nameOrId either the name or HL ID of an entity in the KB
   * @return semantic object, or null if KB entity does not exist
   * @throws KBTypeException if entity exists in KB but does not currently meet 
   * the requirements of the particular semantic object type
   * @throws KBApiException if the semantic object could not be created
   * @todo figure out if it throws a KBApiException, or if it returns null.
   */
  public T get(String nameOrId) throws KBTypeException, KBApiException;
  
  /**
   * Finds an entity in the KB, represented by a CycObject, and returns it wrapped 
   * in a semantic object. 
   * Unlike {@link #findOrCreate(CycObject)}, this method will never modify the
   * contents of the KB.
   * 
   * @param object CycObject representation of KB entity
   * @return semantic object, or null if KB entity does not exist
   * @throws KBTypeException if entity exists in KB but does not currently meet 
   * the requirements of the particular semantic object type
   * @throws KBApiException if the semantic object could not be created
   */
  public T get(CycObject object) throws KBTypeException, KBApiException;
  
  /**
   * Finds or creates a semantic object, represented by name or HL ID, in the underlying KB.
   * If an entity exists in the KB that does not satisfy the requirements of the semantic
   * object, but which could be modified to do so solely by adding new assertions, this method will attempt to 
   * add the appropriate assertions to the KB. For example: if a binary predicate is expected, a 
   * simple predicate (with no known arity) could be easily modified to be a binary predicate.  It could not, however,
   * be turned into a function merely be adding new assertions.  Trying to create it as a <code>Function</code> would 
   * result in an KBTypeConflictException.
   * If you do not wish to risk modifying the KB, use {@link #get(String)} instead.
   * 
   * <p>When searching, preference is given to HL IDs. In the unlikely event that 
   * there are two relevant entities in the KB, one with a matching HL ID and the
   * other with a matching name, the entity with the matching HL ID will be returned.
   * 
   * <p>Note that this method will never create an object if the <code>nameOrId</code> string is a 
   * valid HL ID; instead, an InvalidNameException will be thrown. This is to 
   * prevent potentially confusing cases where a non-existent entity would be searched 
   * for by its HL ID and, in its absence, a new entity would be created which would be named 
   * after the expected ID.
   * 
   * @param nameOrId either the name or HL ID of an entity in the KB
   * @return semantic object
   * @throws KBTypeConflictException if entity exists in KB, but is not
   * of compatible type
   * @throws InvalidNameException if the entity does not exist in the KB, and cannot
   * be created under this name. This factory will reject names if they are valid HL IDs.
   * @throws KBApiException if the entity does not exist in the KB and a 
   * semantic object could not be created
   */
  public T findOrCreate(String nameOrId) throws KBTypeConflictException, InvalidNameException, KBApiException;
  
  /**
   * Finds or creates a semantic object representing the same underlying KB object as <code>object</code>.
   * If an entity exists in the KB that does not satisfy the requirements of the semantic
   * object, but which could be modified to do so solely by adding new assertions, this method will attempt to 
   * add the appropriate assertions to the KB. For example: if a binary predicate is expected, a 
   * simple predicate (with no known arity) could be easily modified to be a binary predicate.  It could not, however,
   * be turned into a function merely be adding new assertions.  Trying to create it as a <code>Function</code> would 
   * result in an KBTypeConflictException.
   * If you do not wish to risk modifying the KB, use {@link #get(String)} instead.
   * 
   * <p>When searching, preference is given to HL IDs. In the unlikely event that 
   * there are two relevant entities in the KB, one with a matching HL ID and the
   * other with a matching name, the entity with the matching HL ID will be returned.
   * 
   * <p>Note that this method will never create an object if the <code>nameOrId</code> string is a 
   * valid HL ID; instead, an InvalidNameException will be thrown. This is to 
   * prevent potentially confusing cases where a non-existent entity would be searched 
   * for by its HL ID and, in its absence, a new entity would be created which would be named 
   * after the expected ID.
   * 
   * @param object CycObject representation of KB entity
   * @return semantic object
   * @throws KBTypeConflictException if entity exists in KB, but is not
   * of compatible type
   * @throws KBApiException if the entity does not exist in the KB and could not 
   * be created
   */
  public T findOrCreate(CycObject object) throws KBTypeConflictException, KBApiException;
  
  /**
   * Finds or creates a semantic object, represented by name or HL ID, in the underlying KB, as an instance of <code>constrainingCollection</code>.
   * If an entity exists in the KB that does not satisfy the requirements of the semantic
   * object and the <code>constrainingCollection</code>, but which could be made to do so solely by adding new assertions, this method will attempt to 
   * add the appropriate assertions to the KB. For example: if a binary predicate is expected, a 
   * simple predicate (with no known arity) could be easily modified to be a binary predicate.  It could not, however,
   * be turned into a function merely be adding new assertions.  Trying to create it as a <code>Function</code> would 
   * result in an KBTypeConflictException.
   * If you do not wish to risk modifying the KB, use {@link #get(String)} instead.
   * 
   * <p>When searching, preference is given to HL IDs. In the unlikely event that 
   * there are two relevant entities in the KB, one with a matching HL ID and the
   * other with a matching name, the entity with the matching HL ID will be returned.
   * 
   * <p>Note that this method will never create an object if the <code>nameOrId</code> string is a 
   * valid HL ID; instead, an InvalidNameException will be thrown. This is to 
   * prevent potentially confusing cases where a non-existent entity would be searched 
   * for by its HL ID and, in its absence, a new entity would be created which would be named 
   * after the expected ID.
   * 
   * @param nameOrId
   * @param constrainingCollection the constraining collection of which the entity is an instance
   * @return semantic object
   * 
   * @throws KBTypeConflictException if entity exists in KB, but is not
   * of compatible type
   * @throws InvalidNameException if the entity does not exist in the KB, and cannot
   * be created under this name. This factory will reject names if they are valid HL IDs.
   * @throws KBApiException if the entity does not exist in the KB and could not 
   * be created
   */
  public T findOrCreate(String nameOrId, KBCollection constrainingCollection) throws KBTypeConflictException, InvalidNameException, KBApiException;
  
  /**
   * Finds or creates a semantic object, represented by name or HL ID, in the underlying KB, as an instance of <code>constrainingCollection</code>.
   * If an entity exists in the KB that does not satisfy the requirements of the semantic
   * object and the <code>constrainingCollection</code>, but which could be made to do so solely by adding new assertions, this method will attempt to 
   * add the appropriate assertions to the KB. For example: if a binary predicate is expected, a 
   * simple predicate (with no known arity) could be easily modified to be a binary predicate.  It could not, however,
   * be turned into a function merely be adding new assertions.  Trying to create it as a <code>Function</code> would 
   * result in an KBTypeConflictException.
   * If you do not wish to risk modifying the KB, use {@link #get(String)} instead.
   * 
   * <p>When searching, preference is given to HL IDs. In the unlikely event that 
   * there are two relevant entities in the KB, one with a matching HL ID and the
   * other with a matching name, the entity with the matching HL ID will be returned.
   * 
   * <p>Note that this method will never create an object if the <code>nameOrId</code> string is a 
   * valid HL ID; instead, an InvalidNameException will be thrown. This is to 
   * prevent potentially confusing cases where a non-existent entity would be searched 
   * for by its HL ID and, in its absence, a new entity would be created which would be named 
   * after the expected ID.
   * 
   * @param nameOrId either the name or HL ID of an entity in the KB
   * @param constrainingCollection the constraining collection of which the entity is an instance
   * @return semantic object
   * 
   * @throws KBTypeConflictException if entity exists in KB, but is not
   * of compatible type
   * @throws InvalidNameException if the entity does not exist in the KB, and cannot
   * be created under this name. This factory will reject names if they are valid HL IDs.
   * @throws KBApiException if the entity does not exist in the KB and could not 
   * be created
   */
  public T findOrCreate(String nameOrId, String constrainingCollection) throws KBTypeConflictException, InvalidNameException, KBApiException;
  
  /**
   * Finds or creates a semantic object, represented by name or HL ID, in the underlying KB, as an instance of <code>constrainingCollection</code> in <code>ctx</code>.
   * If an entity exists in the KB that does not satisfy the requirements of the semantic
   * object and the <code>constrainingCollection</code>, but which could be made to do so solely by adding new assertions, this method will attempt to 
   * add the appropriate assertions to the KB. For example: if a binary predicate is expected, a 
   * simple predicate (with no known arity) could be easily modified to be a binary predicate.  It could not, however,
   * be turned into a function merely be adding new assertions.  Trying to create it as a <code>Function</code> would 
   * result in an KBTypeConflictException.
   * If you do not wish to risk modifying the KB, use {@link #get(String)} instead.
   * 
   * <p>When searching, preference is given to HL IDs. In the unlikely event that 
   * there are two relevant entities in the KB, one with a matching HL ID and the
   * other with a matching name, the entity with the matching HL ID will be returned.
   * 
   * <p>Note that this method will never create an object if the <code>nameOrId</code> string is a 
   * valid HL ID; instead, an InvalidNameException will be thrown. This is to 
   * prevent potentially confusing cases where a non-existent entity would be searched 
   * for by its HL ID and, in its absence, a new entity would be created which would be named 
   * after the expected ID.
   * 
   * @param nameOrId either the name or HL ID of an entity in the KB
   * @param constrainingCollection the constraining collection of which the entity is an instance
   * @param ctx the microtheory under which the search for the entity is performed
   * @return semantic object
   * @throws KBTypeConflictException if entity exists in KB, but is not
   * of compatible type
   * @throws InvalidNameException if the entity does not exist in the KB, and cannot
   * be created under this name. This factory will reject names if they are valid HL IDs.
   * @throws KBApiException if the entity does not exist in the KB and a 
   * semantic object could not be created
   */
  public T findOrCreate(String nameOrId, KBCollection constrainingCollection, Context ctx) throws KBTypeConflictException, InvalidNameException, KBApiException;
  
  /**
   * Finds or creates a semantic object, represented by name or HL ID, in the underlying KB, as an instance of <code>constrainingCollection</code> in <code>ctx</code>.
   * If an entity exists in the KB that does not satisfy the requirements of the semantic
   * object and the <code>constrainingCollection</code>, but which could be made to do so solely by adding new assertions, this method will attempt to 
   * add the appropriate assertions to the KB. For example: if a binary predicate is expected, a 
   * simple predicate (with no known arity) could be easily modified to be a binary predicate.  It could not, however,
   * be turned into a function merely be adding new assertions.  Trying to create it as a <code>Function</code> would 
   * result in an KBTypeConflictException.
   * If you do not wish to risk modifying the KB, use {@link #get(String)} instead.
   * 
   * <p>When searching, preference is given to HL IDs. In the unlikely event that 
   * there are two relevant entities in the KB, one with a matching HL ID and the
   * other with a matching name, the entity with the matching HL ID will be returned.
   * 
   * <p>Note that this method will never create an object if the <code>nameOrId</code> string is a 
   * valid HL ID; instead, an InvalidNameException will be thrown. This is to 
   * prevent potentially confusing cases where a non-existent entity would be searched 
   * for by its HL ID and, in its absence, a new entity would be created which would be named 
   * after the expected ID.
   * 
   * @param nameOrId either the name or HL ID of an entity in the KB
   * @param constrainingCollection the constraining collection of which the entity is an instance
   * @param ctx the microtheory under which the search for the entity is performed
   * @return semantic object
   * @throws KBTypeConflictException if entity exists in KB, but is not
   * of compatible type
   * @throws InvalidNameException if the entity does not exist in the KB, and cannot
   * be created under this name. This factory will reject names if they are valid HL IDs.
   * @throws KBApiException if the entity does not exist in the KB and a 
   * semantic object could not be created
   */
  public T findOrCreate(String nameOrId, String constrainingCollection, String ctx) throws KBTypeConflictException, InvalidNameException, KBApiException;
  
}
