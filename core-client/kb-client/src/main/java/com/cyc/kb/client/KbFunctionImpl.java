package com.cyc.kb.client;

/*
 * #%L
 * File: KbFunctionImpl.java
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

import com.cyc.base.exception.CycConnectionException;
import com.cyc.base.cycobject.CycList;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.DenotationalTerm;
import com.cyc.base.cycobject.FormulaSentence;
import com.cyc.base.cycobject.Guid;
import com.cyc.base.cycobject.Nart;
import com.cyc.baseclient.cycobject.CycArrayList;
import com.cyc.baseclient.cycobject.CycConstantImpl;
import com.cyc.baseclient.cycobject.NartImpl;
import com.cyc.baseclient.cycobject.NautImpl;
import com.cyc.baseclient.datatype.DateConverter;
import com.cyc.kb.Context;
import com.cyc.kb.client.LookupType;
import com.cyc.kb.KbCollection;
import com.cyc.kb.KbFunction;
import com.cyc.kb.KbObject;
import com.cyc.kb.KbStatus;
import com.cyc.kb.DefaultContext;
import com.cyc.kb.client.config.KbConfiguration;
import com.cyc.kb.client.config.KbDefaultContext;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.InvalidNameException;
import com.cyc.kb.exception.KbException;
import com.cyc.kb.exception.KbRuntimeException;
import com.cyc.kb.exception.KbObjectNotFoundException;
import com.cyc.kb.exception.KbTypeConflictException;
import com.cyc.kb.exception.KbTypeException;
import java.util.Collection;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * A <code>KBFunction</code> object is a facade for a <code>#$Function-Denotational</code> 
 * in the Cyc KB.
 * 
 * A N-ary function is a many-one relation, between the N-tuple of the domain elements to
 * a range element. Functions can be used to create "non-atomic terms" 
 * or #$CycLClosedNonAtomicTerm. 
 * 
 * The class provides a method to create new functional terms, which can in turn be used in 
 * other assertions, just as constants are.
 * <p>
 * A new function is by default made a <code>#$ReifiableFunction</code> in the underlying 
 * Cyc Knowledge Base. A future version of the API will support un-reifiable functions.
 *
 * @author Vijay Raj
 * @version	$Id: KbFunctionImpl.java 163117 2015-12-11 00:27:39Z nwinant $
 */
  // @TODO: Add examples
public class KbFunctionImpl extends RelationImpl implements KbFunction {

  private static final Logger log = LoggerFactory.getLogger(KbFunctionImpl.class.getName());
  private static final DenotationalTerm TYPE_CORE =
          new CycConstantImpl("Function-Denotational", new Guid("bd5c40b0-9c29-11b1-9dad-c379636f7270"));

  static DenotationalTerm getClassTypeCore() {
    return TYPE_CORE;
  }
  
  /**
   * Not part of the KB API. This default constructor only has the effect of
   * ensuring that there is access to a Cyc server.
   */
  KbFunctionImpl() {
    super ();
  }

  /**
   * Not part of the KB API. An implementation-dependent constructor.
   * <p>
   * It is used when the result of query is a CycObject and is known to be or
   * requested to be cast as an instance of KBFunction.
   *
   * @param cycObject the CycObject wrapped by <code>KBFunction</code>. The constructor
   * verifies that the CycObject is an instance of #$Function-Denotational
   * 
   * @throws KbTypeException if cycObject is not or could not be made 
   * an instance of #$Function-Denotational
   */
  KbFunctionImpl(CycObject cycObject) throws KbTypeException {
    super(cycObject);
  }

  /**
   * This not part of the public, supported KB API. finds or creates an instance of #$Function-Denotational represented
   * by funcStr in the underlying KB
   * <p>
   *
   * @param funcStr  the string representing an #$Function-Denotational in the KB
   * 
   * @throws CreateException if the #$Function-Denotational represented by funcStr is not found
   * and could not be created 
   * @throws KbTypeException if the term represented by funcStr is not an instance
   * of #$Function-Denotational and cannot be made into one. 
   */
  KbFunctionImpl(String funcStr) throws KbTypeException, CreateException {
    super(funcStr);
  }

  /**
   * This not part of the public, supported KB API. finds or creates; or finds an instance of #$Function-Denotational
   * represented by funcStr in the underlying KB based on input ENUM
   * <p>
   *
   * @param funcStr  the string representing an instance of #$Function-Denotational in the KB
   * @param lookup the enum to specify LookupType: FIND or FIND_OR_CREATE
   * 
   * @throws CreateException 
   * @throws KbTypeException 
   *
   * @throws KbObjectNotFoundException  if the #$Function-Denotational represented by funcStr
   * is not found and could not be created
   * @throws InvalidNameException if the string funcStr does not conform to Cyc constant-naming
   * conventions
   * 
   * @throws KbTypeException  if the term represented by funcStr is not an instance of #$Function-Denotational and lookup is
   * set to find only {@link LookupType#FIND}
   * @throws KbTypeConflictException if the term represented by funcStr is not an instance of #$Function-Denotational,
   * and lookup is set to find or create; and if the term cannot be made an instance #$Function-Denotational by asserting
   * new knowledge.
   */
  KbFunctionImpl(String funcStr, LookupType lookup) throws KbTypeException, CreateException {
    super(funcStr, lookup);
  }

   
  protected KbFunctionImpl (DefaultContext c, KbFunction func) {
	  super();
	  this.setCore(func);
  }
  /**
   * Get the
   * <code>KBFunction</code> with the name
   * <code>nameOrId</code>. Throws exceptions if there is no KB term by that
   * name, or if it is not already an instance of #$Function-Denotational.
   *
   * @param nameOrId the string representation or the HLID of the #$Function-Denotational
   * 
   * @return  a new KBFunction
   * 
   * @throws KbTypeException
   * @throws CreateException 
   */
  public static KbFunctionImpl get(String nameOrId) throws KbTypeException, CreateException {
    return KbObjectFactory.get(nameOrId, KbFunctionImpl.class);
  }

  /**
   * Get the
   * <code>KBFunction</code> object that corresponds to
   * <code>cycObject</code>. Throws exceptions if the object isn't in the KB, or if
   * it's not already an instance of
   * <code>#$Function-Denotational</code>.
   *
   * @param cycObject the CycObject wrapped by KBFunction. The method
   * verifies that the CycObject is an instance of #$Function-Denotational
   * 
   * @return a new KBFunction 
   * 
   * @throws CreateException 
   * @throws KbTypeException
   */
  @Deprecated
  public static KbFunctionImpl get(CycObject cycObject) throws KbTypeException, CreateException {
    return KbObjectFactory.get(cycObject, KbFunctionImpl.class);
  }

  /**
   * Find or create a
   * <code>KBFunction</code> object named
   * <code>nameOrId</code>. If no object exists in the KB with the name
   * <code>nameOrId</code>, one will be created, and it will be asserted to be
   * an instance of
   * <code>#$Function-Denotational</code>. If there is already an object in the KB called
   * <code>nameOrId</code>, and it is already a
   * <code>#$Function-Denotational</code>, it will be returned. If it is not already a
   * <code>#$Function-Denotational</code>, but can be made into one by addition of
   * assertions to the KB, such assertions will be made, and the object will be
   * returned. If the object in the KB cannot be turned into a
   * <code>#$Function-Denotational</code> by adding assertions (i.e. some existing
   * assertion prevents it from being a
   * <code>#$Function-Denotational</code>), a
   * <code>KBTypeConflictException</code>will be thrown.
   *
   * @param nameOrId  the string representation or the HLID of the #$Function-Denotational
   * 
   * @return a new KBFunction
   * 
   * @throws KbTypeException 
   * @throws CreateException 
   */
  public static KbFunctionImpl findOrCreate(String nameOrId) throws CreateException, KbTypeException {
    return KbObjectFactory.findOrCreate(nameOrId, KbFunctionImpl.class);
  }

  /**
   * Find or create a KBFunction object from
   * <code>cycObject</code>. If
   * <code>cycObject</code> is already a
   * <code>#$Function-Denotational</code>, an appropriate
   * <code>KBFunction</code> object will be returned. If
   * <code>object</code> is not already a
   * <code>#$Function-Denotational</code>, but can be made into one by addition of
   * assertions to the KB, such assertions will be made, and the relevant object
   * will be returned. If
   * <code>cycObject</code> cannot be turned into a
   * <code>#$Function-Denotational</code> by adding assertions (i.e. some existing
   * assertion prevents it from being a
   * <code>#$Function-Denotational</code>, a
   * <code>KBTypeConflictException</code>will be thrown.
   *
   * @param cycObject the CycObject wrapped by KBFunction. The method
   * verifies that the CycObject is an #$Function-Denotational
   * 
   * @return a new KBFunction
   * 
   * @throws KbTypeException 
   * @throws CreateException 
   */
  @Deprecated
  public static KbFunctionImpl findOrCreate(CycObject cycObject) throws CreateException, KbTypeException {
    return KbObjectFactory.findOrCreate(cycObject, KbFunctionImpl.class);
  }

  /**
   * Find or create a
   * <code>KBFunction</code> object named
   * <code>nameOrId</code>, and also make it an instance of
   * <code>constraintCol</code> in the default context specified by
   * {@link KBAPIDefaultContext#forAssertion()}. If no object
   * exists in the KB with the name
   * <code>nameOrId</code>, one will be created, and it will be asserted to be
   * an instance of both
   * <code>#$Function-Denotational</code> and
   * <code>constraintCol</code>. If there is already an object in the
   * KB called
   * <code>nameOrId</code>, and it is already both a
   * <code>#$Function-Denotational</code> and a
   * <code>constraintCol</code>, it will be returned. If it is not
   * already both a
   * <code>#$Function-Denotational</code> and a
   * <code>constraintCol</code>, but can be made so by addition of
   * assertions to the KB, such assertions will be made, and the object will be
   * returned. If the object in the KB cannot be turned into both a
   * <code>#$Function-Denotational</code> and a
   * <code>constraintCol</code> by adding assertions, a
   * <code>KBTypeConflictException</code>will be thrown.
   *
   * @param nameOrId  the string representation or the HLID of the #$Function-Denotational
   * @param constraintCol the collection that this #$Function-Denotational will instantiate
   * 
   * @return a new KBFunction
   * 
   * @throws KbTypeException 
   * @throws CreateException 
   */
  public static KbFunctionImpl findOrCreate(String nameOrId, KbCollection constraintCol) throws CreateException, KbTypeException {
    return KbObjectFactory.findOrCreate(nameOrId, constraintCol, KbFunctionImpl.class);
  }

  /**
   * Find or create a
   * <code>KBFunction</code> object named
   * <code>nameOrId</code>, and also make it an instance of
   * <code>constraintCol</code> in the default context specified by
   * {@link KBAPIDefaultContext#forAssertion()}. If no object
   * exists in the KB with the name
   * <code>nameOrId</code>, one will be created, and it will be asserted to be
   * an instance of both
   * <code>#$Function-Denotational</code> and
   * <code>constraintCol</code>. If there is already an object in the
   * KB called
   * <code>nameOrId</code>, and it is already both a
   * <code>#$Function-Denotational</code> and a
   * <code>constraintCol</code>, it will be returned. If it is not
   * already both a
   * <code>#$Function-Denotational</code> and a
   * <code>constraintCol</code>, but can be made so by addition of
   * assertions to the KB, such assertions will be made, and the object will be
   * returned. If the object in the KB cannot be turned into both a
   * <code>#$Function-Denotational</code> and a
   * <code>constraintCol</code> by adding assertions, a
   * <code>KBTypeConflictException</code>will be thrown.
   *
   * @param nameOrId  the string representation or the HLID of the #$Function-Denotational
   * @param constraintColStr the string representation of the collection that 
   * this #$Function-Denotational will instantiate
   * 
   * @return a new KBFunction
   * 
   * @throws KbTypeException 
   * @throws CreateException 
   */
  public static KbFunctionImpl findOrCreate(String nameOrId, String constraintColStr) throws CreateException, KbTypeException {
    return KbObjectFactory.findOrCreate(nameOrId, constraintColStr, KbFunctionImpl.class);
  }

  /**
   * Find or create a
   * <code>KBFunction</code> object named
   * <code>nameOrId</code>, and also make it an instance of
   * <code>constraintCol</code> in
   * <code>ctx</code>. If no object exists in the KB with the name
   * <code>nameOrId</code>, one will be created, and it will be asserted to be
   * an instance of both
   * <code>#$Function-Denotational</code> and
   * <code>constraintCol</code>. If there is already an object in the
   * KB called
   * <code>nameOrId</code>, and it is already both a
   * <code>#$Function-Denotational</code> and a
   * <code>constraintCol</code>, it will be returned. If it is not
   * already both a
   * <code>#$Function-Denotational</code> and a
   * <code>constraintCol</code>, but can be made so by addition of
   * assertions to the KB, such assertions will be made, and the object will be
   * returned. If the object in the KB cannot be turned into both a
   * <code>#$Function-Denotational</code> and a
   * <code>constraintCol</code> by adding assertions, a
   * <code>KBTypeConflictException</code>will be thrown.
   *
   * @param nameOrId  the string representation or the HLID of the #$Function-Denotational
   * @param constraintCol the collection that this #$Function-Denotational will instantiate
   * @param ctx the context in which the resulting object must be an instance of
   * constraintCol
   * 
   * @return a new KBFunction
   * 
   * @throws KbTypeException 
   * @throws CreateException 
   */
  public static KbFunctionImpl findOrCreate(String nameOrId, KbCollection constraintCol, Context ctx) 
      throws CreateException, KbTypeException {
    return KbObjectFactory.findOrCreate(nameOrId, constraintCol, ctx, KbFunctionImpl.class);
  }

  /**
   * Find or create a
   * <code>KBFunction</code> object named
   * <code>nameOrId</code>, and also make it an instance of
   * <code>constraintCol</code> in
   * <code>ctx</code>. If no object exists in the KB with the name
   * <code>nameOrId</code>, one will be created, and it will be asserted to be
   * an instance of both
   * <code>#$Function-Denotational</code> and
   * <code>constraintCol</code>. If there is already an object in the
   * KB called
   * <code>nameOrId</code>, and it is already both a
   * <code>#$Function-Denotational</code> and a
   * <code>constraintCol</code>, it will be returned. If it is not
   * already both a
   * <code>#$Function-Denotational</code> and a
   * <code>constraintCol</code>, but can be made so by addition of
   * assertions to the KB, such assertions will be made, and the object will be
   * returned. If the object in the KB cannot be turned into both a
   * <code>#$Function-Denotational</code> and a
   * <code>constraintCol</code> by adding assertions, a
   * <code>KBTypeConflictException</code>will be thrown.
   *
   * @param nameOrId the string representation or the HLID of the term
   * @param constraintColStr the string representation of the collection that 
   * this #$Function-Denotational will instantiate
   * @param ctxStr the context in which the resulting object must be an instance of
   * constraintCol
   * 
   * @return a new KBFunction 
   * 
   * @throws KbTypeException 
   * @throws CreateException 
   */
  public static KbFunctionImpl findOrCreate(String nameOrId, String constraintColStr, String ctxStr) throws CreateException, KbTypeException {
    return KbObjectFactory.findOrCreate(nameOrId, constraintColStr, ctxStr, KbFunctionImpl.class);
  }

  /**
   * Checks whether entity exists in KB and is an instance of #$Function-Denotational. If
   * false, {@link #getStatus(String)} may yield more information. This method
   * is equivalent to
   * <code>getStatus(nameOrId).equals(KBStatus.EXISTS_AS_TYPE)</code>.
   *
   * @param nameOrId either the name or HL ID of an entity in the KB
   * @return <code>true</code> if entity exists in KB and is an instance of
   * #$Function-Denotational
   */
  public static boolean existsAsType(String nameOrId) {
    return getStatus(nameOrId).equals(KbStatus.EXISTS_AS_TYPE);
  }

  /**
   * Checks whether entity exists in KB and is an instance of #$Function-Denotational. If
   * false, {@link #getStatus(CycObject)} may yield more information. This
   * method is equivalent to
   * <code>getStatus(object).equals(KBStatus.EXISTS_AS_TYPE)</code>.
   *
   * @param cycObject the CycObject representation of a KB entity
   * @return <code>true</code> if entity exists in KB and is an instance of
   * #$Function-Denotational
   */
  public static boolean existsAsType(CycObject cycObject) {
    return getStatus(cycObject).equals(KbStatus.EXISTS_AS_TYPE);
  }

  /**
   * Returns an KBStatus enum which describes whether
   * <code>nameOrId</code> exists in the KB and is an instance of
   * <code>#$Function-Denotational</code>.
   *
   * @param nameOrId either the name or HL ID of an entity in the KB
   * @return an enum describing the existential status of the entity in the KB
   */
  public static KbStatus getStatus(String nameOrId) {
    return KbObjectFactory.getStatus(nameOrId, KbFunctionImpl.class);
  }

  /**
   * Returns an KBStatus enum which describes whether
   * <code>cycObject</code> exists in the KB and is an instance of
   * <code>#$Function-Denotational</code>.
   *
   * @param cycObject the CycObject representation of a KB entity
   * @return an enum describing the existential status of the entity in the KB
   */
  public static KbStatus getStatus(CycObject cycObject) {
    return KbObjectFactory.getStatus(cycObject, KbFunctionImpl.class);
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBFunction#findOrCreateFunctionalTerm(java.lang.Class, java.lang.Object)
   */
  @Override
  @SuppressWarnings("deprecation")
  public <O> O findOrCreateFunctionalTerm(Class<O> retType, Object... args) throws KbTypeException, CreateException {
    try {
      CycList<Object> natArgs = new CycArrayList<Object>();
      natArgs.add(this.getCore());

      FormulaSentence fs = SentenceImpl.convertKBObjectArrayToCycFormulaSentence(args);
      natArgs.addAll(fs.getArgs());

      CycObject co = null;
      if (this.isInstanceOf(Constants.getInstance().REIFIABLE_FUNC, Constants.uvMt())) {
        co = new NartImpl(natArgs);
        ((Nart) co).ensureReified(getAccess());
      } else {
        co = new NautImpl(natArgs);
      }

      return KbObjectImpl.<O>checkAndCastObject(co);
    } catch (CycConnectionException ex) {
      throw new KbRuntimeException(ex.getMessage(), ex);
    } 
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBFunction#getResultIsa()
   */
  @Override
  public Collection<KbCollection> getResultIsa() {
    return getResultIsa(KbConfiguration.getDefaultContext().forQuery());
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBFunction#getResultIsa(java.lang.String)
   */
  @Override
  public Collection<KbCollection> getResultIsa(String ctxStr) {
    return getResultIsa(KbUtils.getKBObjectForArgument(ctxStr, ContextImpl.class));
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBFunction#getResultIsa(com.cyc.kb.ContextImpl)
   */
  @Override
  public Collection<KbCollection> getResultIsa(Context ctx) {
    return (this.<KbCollection>getValues(Constants.resultIsa(), 1, 2, ctx));
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBFunction#addResultIsa(java.lang.String, java.lang.String)
   */
  @Override
  public KbFunction addResultIsa(String colStr, String ctxStr) throws KbTypeException, CreateException  {
    return addResultIsa(KbCollectionImpl.get(colStr), ContextImpl.get(ctxStr));
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBFunction#addResultIsa(com.cyc.kb.KBCollection, com.cyc.kb.ContextImpl)
   */
  @Override
  public KbFunction addResultIsa(KbCollection col, Context ctx) throws KbTypeException, CreateException {
    addFact(ctx, Constants.resultIsa(), 1, (Object) col);
    return this;
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBFunction#getResultGenl()
   */
  @Override
  public Collection<KbCollection> getResultGenl()
          throws KbException {
    return getResultGenl(KbConfiguration.getDefaultContext().forQuery());
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBFunction#getResultGenl(java.lang.String)
   */
  @Override
  public Collection<KbCollection> getResultGenl(String ctxStr) {
    return getResultGenl(KbUtils.getKBObjectForArgument(ctxStr, ContextImpl.class));
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBFunction#getResultGenl(com.cyc.kb.ContextImpl)
   */
  @Override
  public java.util.Collection<KbCollection> getResultGenl(Context ctx) {
    return (this.<KbCollection>getValues(Constants.resultGenl(), 1, 2, ctx));
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBFunction#addResultGenl(java.lang.String, java.lang.String)
   */
  @Override
  public KbFunction addResultGenl(String colStr, String ctxStr) throws KbTypeException, CreateException {
    return addResultGenl(KbCollectionImpl.get(colStr), ContextImpl.get(ctxStr));
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBFunction#addResultGenl(com.cyc.kb.KBCollection, com.cyc.kb.ContextImpl)
   */
  @Override
  public KbFunction addResultGenl(KbCollection col, Context ctx) throws KbTypeException, CreateException {
    addFact(ctx, Constants.resultGenl(), 1, (Object) col);
    return this;
  }

  //TODO: Add get/addInterArgResultIsa, get/addInterArgResultGenls
  
  /**
   * Return the KBCollection as a KBObject of the Cyc term that 
   * underlies this class. 
   * 
   * @return KBCollectionImpl.get("#$Function-Denotational");
   */
  @Override
  public KbObject getType() {
    return getClassType();
  }
  
  /**
   * Return the KBCollection as a KBObject of the Cyc term that 
   * underlies this class. 
   * 
   * @return KBCollectionImpl.get("#$Function-Denotational");
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
    return "#$Function-Denotational";
  }
}
