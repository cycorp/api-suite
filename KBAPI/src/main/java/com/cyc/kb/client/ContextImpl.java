package com.cyc.kb.client;

/*
 * #%L
 * File: ContextImpl.java
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

import com.cyc.base.CycApiException;
import com.cyc.base.CycConnectionException;
import com.cyc.base.cycobject.CycConstant;
import com.cyc.base.cycobject.CycList;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.CycVariable;
import com.cyc.base.cycobject.DenotationalTerm;
import com.cyc.base.cycobject.ELMt;
import com.cyc.base.cycobject.FormulaSentence;
import com.cyc.base.cycobject.Guid;
import com.cyc.baseclient.CycObjectFactory;
import com.cyc.baseclient.cycobject.CycConstantImpl;
import com.cyc.baseclient.cycobject.NautImpl;
import com.cyc.baseclient.datatype.TimeInterval;
import com.cyc.baseclient.datatype.TimeIntervalConverter;
import com.cyc.baseclient.util.ParseException;
import com.cyc.kb.Context;
import com.cyc.kb.KBAPIEnums.LookupType;
import com.cyc.kb.KBCollection;
import com.cyc.kb.KBObject;
import com.cyc.kb.KBStatus;
import com.cyc.kb.config.DefaultContext;
import com.cyc.kb.config.KBAPIDefaultContext;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.InvalidNameException;
import com.cyc.kb.exception.KBApiException;
import com.cyc.kb.exception.KBApiRuntimeException;
import com.cyc.kb.exception.KBObjectNotFoundException;
import com.cyc.kb.exception.KBTypeConflictException;
import com.cyc.kb.exception.KBTypeException;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




/**
 * A <code>Context</code> object is a facade for a <code>#$Microtheory</code> 
 * in Cyc KB.
 * 
 * A context is a grouping of assertions that share a set of common assumptions. 
 * Each Assertion has to be explicitly stated to be true in at least one context.
 *
 * @author Vijay Raj
 * @version $Id: ContextImpl.java 155051 2014-11-18 21:06:04Z baxter $
 */
public class ContextImpl extends KBIndividualImpl implements Context {

  private static final Logger log = LoggerFactory.getLogger(ContextImpl.class.getName());
  private static final CycConstant MT_SPACE = new CycConstantImpl("MtSpace",
          new Guid("abb96eb5-e798-11d6-8ac9-0002b3a333c3"));
  private static final CycConstant MT_TIME_DIM_FN = new CycConstantImpl(
          "MtTimeDimFn", new Guid("47537942-331d-11d7-922f-0002b3a333c3"));
  private static final DenotationalTerm TYPE_CORE =
          new CycConstantImpl("Microtheory", new Guid("bd5880d5-9c29-11b1-9dad-c379636f7270"));

  static DenotationalTerm getClassTypeCore() {
    return TYPE_CORE;
  }
  
  /**
   * Not part of the KB API. This default constructor only has the effect of
   * ensuring that there is access to a Cyc server.
   */
  ContextImpl () {
    super();
  }
  
  /**
   * EXPERIMENTAL!!! NOT PART OF THE KB API
   * 
   * A copy constructor to allow higher level APIs to construct
   * subclass objects using super class objects, when appropriate.
   * 
   * @param ctx the Context to be copied
   */
  protected ContextImpl (DefaultContext c, Context ctx) {
	  super();
	  this.setCore(ctx);
  }
  
  /**
   * Not part of the KB API. An implementation-dependent constructor.
   * <p>
   * It is used when the result of query is a CycObject and is known to be or
   * requested to be cast as an instance of Context.
   *
   * @param cycObject the CycObject wrapped by <code>Context</code>. The constructor
   * verifies that the CycObject is an instance of #$Microtheory
   * 
   * @throws KBTypeException if cycObject is not or could not be made 
   * an instance of #$Microtheory
   */
  ContextImpl(CycObject cycObject) throws KBTypeException {
    super(cycObject);
  }

  /**
   * This not part of the public, supported KB API. finds or creates an instance of #$Microtheory represented
   * by ctxStr in the underlying KB
   * <p>
   *
   * @param ctxStr  the string representing an #$Microtheory in the KB
   * 
   * @throws CreateException if the #$Microtheory represented by ctxStr is not found
   * and could not be created 
   * @throws KBTypeException if the term represented by ctxStr is not an instance
   * of #$Microtheory and cannot be made into one. 
   */
  ContextImpl(String ctxStr) throws KBTypeException, CreateException {
    super(ctxStr);
  }

  /**
   * This not part of the public, supported KB API. finds or creates; or finds an instance of #$Microtheory
   * represented by ctxStr in the underlying KB based on input ENUM
   * <p>
   *
   * @param ctxStr  the string representing an instance of #$Microtheory in the KB
   * @param lookup the enum to specify LookupType: FIND or FIND_OR_CREATE
   * 
   * @throws CreateException 
   * @throws KBTypeException 
   *
   * @throws KBObjectNotFoundException  if the #$Microtheory represented by ctxStr
   * is not found and could not be created
   * @throws InvalidNameException if the string ctxStr does not conform to Cyc constant-naming
   * conventions
   * 
   * @throws KBTypeException  if the term represented by ctxStr is not an instance of #$Microtheory and lookup is
   * set to find only {@link LookupType#FIND}
   * @throws KBTypeConflictException if the term represented by ctxStr is not an instance of #$Microtheory,
   * and lookup is set to find or create; and if the term cannot be made an instance #$Microtheory by asserting
   * new knowledge.
   */
  ContextImpl(String ctxStr, LookupType lookup) throws KBTypeException, CreateException {
    super(ctxStr, lookup);
  }

  /**
   * Get the
   * <code>Context</code> with the name
   * <code>nameOrId</code>. Throws exceptions if there is no KB term by that
   * name, or if it is not already an instance of #$Microtheory.
   *
   * @param nameOrId the string representation or the HLID of the #$Microtheory
   * 
   * @return  a new Context
   * 
   * @throws KBTypeException
   * @throws CreateException 
   */
  public static ContextImpl get(String nameOrId) throws KBTypeException, CreateException{
    return KBObjectFactory.get(nameOrId, ContextImpl.class);
  }

  /**
   * Get the
   * <code>Context</code> object that corresponds to
   * <code>cycObject</code>. Throws exceptions if the object isn't in the KB, or if
   * it's not already an instance of
   * <code>#$Microtheory</code>.
   *
   * @param cycObject the CycObject wrapped by Context. The method
   * verifies that the CycObject is an instance of #$Microtheory
   * 
   * @return a new Context 
   * 
   * @throws CreateException 
   * @throws KBTypeException
   */
  @Deprecated
  public static ContextImpl get(CycObject cycObject) throws KBTypeException, CreateException {
    return KBObjectFactory.get(cycObject, ContextImpl.class);
  }

  /**
   * Return a
   * <code>Context</code> based on
   * <code>monad</code>, but temporally qualified to
   * <code>time</code>. There is no corresponding
   * <code>findOrCreate</code> method, because this does not require that the
   * temporally-qualified Microtheory already exist in the Cyc KB. If a context
   * returned by this method is used in an assertion, the temporally-qualified
   * microtheory will be automatically reified. The monad part of this context
   * must already exist in the KB.
   *
   * @param monad a monadic microtheory. If this monad has a time specification
   * (i.e. it isn't really a monad), the time specification will be replaced in
   * the resulting context.
   * @param time the temporal qualification for the    * resulting <code>Context</code>.
   * 
   * @return a new context
   * 
   * @throws KBTypeException
   * @throws CreateException 
   */
  @SuppressWarnings("deprecation")
  public static Context get(CycObject monad, TimeInterval time) throws KBTypeException, CreateException {
    CycObject mtNat = new NautImpl(MT_SPACE,
            monad, new NautImpl(MT_TIME_DIM_FN,
            time.toCycTerm()));
    return KBObjectFactory.get(mtNat, ContextImpl.class);
  }

  /**
   * Return a
   * <code>Context</code> based on
   * <code>monad</code>, but temporally qualified to
   * <code>time</code>. There is no corresponding
   * <code>findOrCreate</code> method, because this does not require that the
   * temporally-qualified Microtheory already exist in the Cyc KB. If a context
   * returned by this method is used in an assertion, the temporally-qualified
   * microtheory will be automatically reified. The monad part of this context
   * must already exist in the KB.
   *
   * @param monad a monadic context. If this context has a time specification,
   * it will be replaced in the resulting context.
   * @param time the temporal qualification for the    * resulting <code>Context</code>.
   * 
   * @return a new context
   * 
   * @throws KBTypeException
   * @throws CreateException 
   */
  public static Context get(Context monad, TimeInterval time) throws KBTypeException, CreateException {
    return ContextImpl.get(monad.getCore(), time);
  }

  /**
   * Find or create a
   * <code>Context</code> object named
   * <code>nameOrId</code>. If no object exists in the KB with the name
   * <code>nameOrId</code>, one will be created, and it will be asserted to be
   * an instance of
   * <code>#$Microtheory</code>. If there is already an object in the KB called
   * <code>nameOrId</code>, and it is already a
   * <code>#$Microtheory</code>, it will be returned. If it is not already a
   * <code>#$Microtheory</code>, but can be made into one by addition of
   * assertions to the KB, such assertions will be made, and the object will be
   * returned. If the object in the KB cannot be turned into a
   * <code>#$Microtheory</code> by adding assertions (i.e. some existing
   * assertion prevents it from being a
   * <code>#$Microtheory</code>), a
   * <code>KBTypeConflictException</code>will be thrown.
   *
   * @param nameOrId  the string representation or the HLID of the #$Microtheory
   * 
   * @return a new Context
   * 
   * @throws KBTypeException 
   * @throws CreateException 
   */
  public static ContextImpl findOrCreate(String nameOrId) throws CreateException, KBTypeException{
    return KBObjectFactory.findOrCreate(nameOrId, ContextImpl.class);
  }

  /**
   * Find or create a Context object from
   * <code>cycObject</code>. If
   * <code>cycObject</code> is already a
   * <code>#$Microtheory</code>, an appropriate
   * <code>Context</code> object will be returned. If
   * <code>object</code> is not already a
   * <code>#$Microtheory</code>, but can be made into one by addition of
   * assertions to the KB, such assertions will be made, and the relevant object
   * will be returned. If
   * <code>cycObject</code> cannot be turned into a
   * <code>#$Microtheory</code> by adding assertions (i.e. some existing
   * assertion prevents it from being a
   * <code>#$Microtheory</code>, a
   * <code>KBTypeConflictException</code>will be thrown.
   *
   * @param cycObject the CycObject wrapped by Context. The method
   * verifies that the CycObject is an #$Microtheory
   * 
   * @return a new Context
   * 
   * @throws KBTypeException 
   * @throws CreateException 
   */
  @Deprecated
  public static ContextImpl findOrCreate(CycObject cycObject) throws CreateException, KBTypeException {
    return KBObjectFactory.findOrCreate(cycObject, ContextImpl.class);
  }

  /**
   * Find or create a
   * <code>Context</code> object named
   * <code>nameOrId</code>, and also make it an instance of
   * <code>constraintCol</code> in the default context specified by
   * {@link KBAPIDefaultContext#forAssertion()}. If no object
   * exists in the KB with the name
   * <code>nameOrId</code>, one will be created, and it will be asserted to be
   * an instance of both
   * <code>#$Microtheory</code> and
   * <code>constraintCol</code>. If there is already an object in the
   * KB called
   * <code>nameOrId</code>, and it is already both a
   * <code>#$Microtheory</code> and a
   * <code>constraintCol</code>, it will be returned. If it is not
   * already both a
   * <code>#$Microtheory</code> and a
   * <code>constraintCol</code>, but can be made so by addition of
   * assertions to the KB, such assertions will be made, and the object will be
   * returned. If the object in the KB cannot be turned into both a
   * <code>#$Microtheory</code> and a
   * <code>constraintCol</code> by adding assertions, a
   * <code>KBTypeConflictException</code>will be thrown.
   *
   * @param nameOrId  the string representation or the HLID of the #$Microtheory
   * @param constraintCol the collection that this #$Microtheory will instantiate
   * 
   * @return a new Context
   * 
   * @throws KBTypeException 
   * @throws CreateException 
   */
  public static ContextImpl findOrCreate(String nameOrId, KBCollection constraintCol) throws CreateException, KBTypeException {
    return KBObjectFactory.findOrCreate(nameOrId, constraintCol, ContextImpl.class);
  }

  /**
   * Find or create a
   * <code>Context</code> object named
   * <code>nameOrId</code>, and also make it an instance of
   * <code>constraintCol</code> in the default context specified by
   * {@link KBAPIDefaultContext#forAssertion()}. If no object
   * exists in the KB with the name
   * <code>nameOrId</code>, one will be created, and it will be asserted to be
   * an instance of both
   * <code>#$Microtheory</code> and
   * <code>constraintCol</code>. If there is already an object in the
   * KB called
   * <code>nameOrId</code>, and it is already both a
   * <code>#$Microtheory</code> and a
   * <code>constraintCol</code>, it will be returned. If it is not
   * already both a
   * <code>#$Microtheory</code> and a
   * <code>constraintCol</code>, but can be made so by addition of
   * assertions to the KB, such assertions will be made, and the object will be
   * returned. If the object in the KB cannot be turned into both a
   * <code>#$Microtheory</code> and a
   * <code>constraintCol</code> by adding assertions, a
   * <code>KBTypeConflictException</code>will be thrown.
   *
   * @param nameOrId  the string representation or the HLID of the #$Microtheory
   * @param constraintColStr the string representation of the collection that 
   * this #$Microtheory will instantiate
   * 
   * @return a new Context
   * 
   * @throws KBTypeException 
   * @throws CreateException 
   */
  public static ContextImpl findOrCreate(String nameOrId, String constraintColStr) throws CreateException, KBTypeException {
    return KBObjectFactory.findOrCreate(nameOrId, constraintColStr, ContextImpl.class);
  }

  /**
   * Find or create a
   * <code>Context</code> object named
   * <code>nameOrId</code>, and also make it an instance of
   * <code>constraintCol</code> in
   * <code>ctx</code>. If no object exists in the KB with the name
   * <code>nameOrId</code>, one will be created, and it will be asserted to be
   * an instance of both
   * <code>#$Microtheory</code> and
   * <code>constraintCol</code>. If there is already an object in the
   * KB called
   * <code>nameOrId</code>, and it is already both a
   * <code>#$Microtheory</code> and a
   * <code>constraintCol</code>, it will be returned. If it is not
   * already both a
   * <code>#$Microtheory</code> and a
   * <code>constraintCol</code>, but can be made so by addition of
   * assertions to the KB, such assertions will be made, and the object will be
   * returned. If the object in the KB cannot be turned into both a
   * <code>#$Microtheory</code> and a
   * <code>constraintCol</code> by adding assertions, a
   * <code>KBTypeConflictException</code>will be thrown.
   *
   * @param nameOrId  the string representation or the HLID of the #$Microtheory
   * @param constraintCol the collection that this #$Microtheory will instantiate
   * @param ctx the context in which the resulting object must be an instance of
   * constraintCol
   * 
   * @return a new Context
   * 
   * @throws KBTypeException 
   * @throws CreateException 
   */
  public static ContextImpl findOrCreate(String nameOrId, KBCollection constraintCol, Context ctx) throws CreateException, KBTypeException {
    return KBObjectFactory.findOrCreate(nameOrId, constraintCol, ctx, ContextImpl.class);
  }

  /**
   * Find or create a
   * <code>Context</code> object named
   * <code>nameOrId</code>, and also make it an instance of
   * <code>constraintCol</code> in
   * <code>ctx</code>. If no object exists in the KB with the name
   * <code>nameOrId</code>, one will be created, and it will be asserted to be
   * an instance of both
   * <code>#$Microtheory</code> and
   * <code>constraintCol</code>. If there is already an object in the
   * KB called
   * <code>nameOrId</code>, and it is already both a
   * <code>#$Microtheory</code> and a
   * <code>constraintCol</code>, it will be returned. If it is not
   * already both a
   * <code>#$Microtheory</code> and a
   * <code>constraintCol</code>, but can be made so by addition of
   * assertions to the KB, such assertions will be made, and the object will be
   * returned. If the object in the KB cannot be turned into both a
   * <code>#$Microtheory</code> and a
   * <code>constraintCol</code> by adding assertions, a
   * <code>KBTypeConflictException</code>will be thrown.
   *
   * @param nameOrId the string representation or the HLID of the term
   * @param constraintColStr the string representation of the collection that 
   * this #$Microtheory will instantiate
   * @param ctxStr the context in which the resulting object must be an instance of
   * constraintCol
   * 
   * @return a new Context 
   * 
   * @throws KBTypeException 
   * @throws CreateException 
   */
  public static ContextImpl findOrCreate(String nameOrId, String constraintColStr, String ctxStr) throws CreateException, KBTypeException {
    return KBObjectFactory.findOrCreate(nameOrId, constraintColStr, ctxStr, ContextImpl.class);
  }

  /**
   * Checks whether entity exists in KB and is an instance of #$Microtheory. If
   * false, {@link #getStatus(String)} may yield more information. This method
   * is equivalent to
   * <code>getStatus(nameOrId).equals(KBStatus.EXISTS_AS_TYPE)</code>.
   *
   * @param nameOrId either the name or HL ID of an entity in the KB
   * @return <code>true</code> if entity exists in KB and is an instance of
   * #$Microtheory
   */
  public static boolean existsAsType(String nameOrId) {
    return getStatus(nameOrId).equals(KBStatus.EXISTS_AS_TYPE);
  }

  /**
   * Checks whether entity exists in KB and is an instance of #$Microtheory. If
   * false, {@link #getStatus(CycObject)} may yield more information. This
   * method is equivalent to
   * <code>getStatus(object).equals(KBStatus.EXISTS_AS_TYPE)</code>.
   *
   * @param cycObject the CycObject representation of a KB entity
   * @return <code>true</code> if entity exists in KB and is an instance of
   * #$Microtheory
   */
  public static boolean existsAsType(CycObject cycObject) {
    return getStatus(cycObject).equals(KBStatus.EXISTS_AS_TYPE);
  }

  /**
   * Returns an KBStatus enum which describes whether
   * <code>nameOrId</code> exists in the KB and is an instance of
   * <code>#$Microtheory</code>.
   *
   * @param nameOrId either the name or HL ID of an entity in the KB
   * @return an enum describing the existential status of the entity in the KB
   */
  public static KBStatus getStatus(String nameOrId) {
    return KBObjectFactory.getStatus(nameOrId, ContextImpl.class);
  }

  /**
   * Returns an KBStatus enum which describes whether
   * <code>cycObject</code> exists in the KB and is an instance of
   * <code>#$Microtheory</code>.
   *
   * @param cycObject the CycObject representation of a KB entity
   * @return an enum describing the existential status of the entity in the KB
   */
  public static KBStatus getStatus(CycObject cycObject) {
    return KBObjectFactory.getStatus(cycObject, ContextImpl.class);
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.Context#getExtensions()
   */
  @Override
  public Collection<Context> getExtensions() {
    return (this.<Context>getValues(Constants.genlMt(), 2, 1, null));
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.Context#addExtension(java.lang.String)
   */
  @Override
  public Context addExtension(String moreSpecificStr) throws KBTypeException, CreateException {
    Context c = ContextImpl.get(moreSpecificStr);
    return addExtension(c);
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.Context#addExtension(com.cyc.kb.Context)
   */
  @Override
  public Context addExtension(Context moreSpecific) throws KBTypeException, CreateException {
    addFact(Constants.baseKbMt(), Constants.genlMt(), 2, (Object) moreSpecific);
    return this;
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.Context#getInheritsFrom()
   */
  @Override
  public Collection<Context> getInheritsFrom() {
    return (this.<Context>getValues(Constants.genlMt(), 1, 2, null));
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.Context#addInheritsFrom(java.lang.String)
   */
  @Override
  public Context addInheritsFrom(String moreGeneralStr) throws KBTypeException, CreateException  {
    return addInheritsFrom(KBUtils.getKBObjectForArgument(moreGeneralStr, ContextImpl.class));
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.Context#addInheritsFrom(com.cyc.kb.Context)
   */
  @Override
  public Context addInheritsFrom(Context moreGeneral) throws KBTypeException, CreateException {
    addFact(Constants.baseKbMt(), Constants.genlMt(), 1, (Object) moreGeneral);
    return this;
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.Context#getMonad()
   */
  @Override
  public Context getMonad() {
    try {
      final CycVariable var = CycObjectFactory.makeCycVariable("MONAD");
      final FormulaSentence sentence = getAccess().getObjectTool().makeCycSentence("( " + Constants.mtMonad().stringApiValue() + " "
              + stringApiValue() + " " + var.toCanonicalString() + ")");
      final CycList<Object> result = getAccess().getInferenceTool().queryVariable(var, sentence,
              Constants.uvMt().asELMt());
      if (!result.isEmpty()) {
    	  // @todo: What if it has multiple Monads??
        return ContextImpl.get((CycObject)result.get(0));
      }
      return null;
    } catch (CycConnectionException ex) {
      throw new KBApiRuntimeException("Unable to get monad for context " + this, ex);
    } catch (CycApiException ex) {
      throw new KBApiRuntimeException("Unable to get monad for context " + this, ex);
    } catch (KBApiException ex) {
      throw new KBApiRuntimeException(ex.getMessage(), ex);
    } 
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.Context#getTimeInterval()
   */
  @Override
  public TimeInterval getTimeInterval() {
    try {
      final CycVariable var = CycObjectFactory.makeCycVariable("INT");
      final FormulaSentence sentence = getAccess().getObjectTool().makeCycSentence("(" + Constants.mtTimeIndex().stringApiValue() + " "
              + core.cyclify() + " " + var.toCanonicalString() + ")");
      final CycList<Object> result = getAccess().getInferenceTool().queryVariable(var, sentence,
          Constants.uvMt().asELMt());
      if (!result.isEmpty()) {
        return TimeIntervalConverter.parseCycInterval((CycObject) result.get(0));
      }
      return null;
    } catch (CycConnectionException ex) {
      throw new KBApiRuntimeException("Unable to get time interval for context " + this, ex);
    } catch (CycApiException ex) {
      throw new KBApiRuntimeException("Unable to get time interval for context " + this, ex);
    } catch (ParseException e) {
      throw new KBApiRuntimeException("Unable to get time interval for context " + this, e);
    } 
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.Context#asELMt()
   */
  @Override
  @Deprecated
  public ELMt asELMt() {
    try {
      return getAccess().getObjectTool().makeELMt(core);
    } catch (CycConnectionException e){
      throw new KBApiRuntimeException(e.getMessage(), e);
    }
  }

  /**
   * Return the KBCollection as a KBObject of the Cyc term that 
   * underlies this class. 
   * 
   * @return KBCollectionImpl.get("#$Microtheory");
   */
  @Override
  public KBObject getType() {
    return getClassType();
  }
  
  /**
   * Return the KBCollection as a KBObject of the Cyc term that 
   * underlies this class. 
   * 
   * @return KBCollectionImpl.get("#$Microtheory");
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
    return "#$Microtheory";
  }
}
