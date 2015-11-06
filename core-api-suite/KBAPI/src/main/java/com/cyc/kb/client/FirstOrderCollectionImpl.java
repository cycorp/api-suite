package com.cyc.kb.client;

/*
 * #%L
 * File: FirstOrderCollectionImpl.java
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

import java.util.Collection;

import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.DenotationalTerm;
import com.cyc.base.cycobject.Guid;
import com.cyc.baseclient.cycobject.CycConstantImpl;
import com.cyc.kb.Context;
import com.cyc.kb.FirstOrderCollection;
import com.cyc.kb.KBCollection;
import com.cyc.kb.KBStatus;
import com.cyc.kb.KBAPIEnums.LookupType;
import com.cyc.kb.KBIndividual;
import com.cyc.kb.KBObject;
import com.cyc.kb.config.DefaultContext;
import com.cyc.kb.config.KBAPIConfiguration;
import com.cyc.kb.config.KBAPIDefaultContext;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.InvalidNameException;
import com.cyc.kb.exception.KBApiException;
import com.cyc.kb.exception.KBApiRuntimeException;
import com.cyc.kb.exception.KBObjectNotFoundException;
import com.cyc.kb.exception.KBTypeConflictException;
import com.cyc.kb.exception.KBTypeException;

/**
 * A <code>FirstOrderCollection</code> object is a facade for a 
 * <code>#$FirstOrderCollection</code> in Cyc KB.
 * 
 * A #$FirstOrderCollection is a specialization of #$Collection. It is a collection of
 * collections that have only #$Individual as its instances. The higher level APIs that use 
 * the KB API utilize this subclass (specialization) of KBCollection (#$Collection.)
 * So we have a class to support strongly typing such collections. 
 * 
 * @author Vijay Raj
 * @version $Id: FirstOrderCollectionImpl.java 157022 2015-03-11 16:19:37Z nwinant $
 */
public class FirstOrderCollectionImpl extends KBCollectionImpl implements FirstOrderCollection {
  private static final DenotationalTerm TYPE_CORE =
          new CycConstantImpl("FirstOrderCollection", new Guid("1c8052d2-1fd3-11d6-8000-0050dab92c2f"));

  static DenotationalTerm getClassTypeCore() {
    return TYPE_CORE;
  }
  
  /**
   * Not part of the KB API. This default constructor only has the effect of
   * ensuring that there is access to a Cyc server.
   */
  protected FirstOrderCollectionImpl() {
    super();
  }
  
  /**
   * EXPERIMENTAL!!! NOT PART OF THE KB API
   * 
   * A copy constructor to allow higher level APIs to construct
   * subclass objects using super class objects, when appropriate.
   * 
   * @param foCol the FirstOrderCollection to be copied
   */
  protected FirstOrderCollectionImpl (DefaultContext c, FirstOrderCollection foCol) {
	  super();
	  this.setCore(foCol);
  }
  
  /**
   * Not part of the KB API. An implementation-dependent constructor.
   * <p>
   * It is used when the result of query is a CycObject and is known to be or
   * requested to be cast as an instance of FirstOrderCollection.
   *
   * @param cycObject the CycObject wrapped by <code>FirstOrderCollection</code>. The constructor
   * verifies that the CycObject is an instance of #$FirstOrderCollection
   * 
   * @throws KBTypeException if cycObject is not or could not be made 
   * an instance of #$FirstOrderCollection
   */
  FirstOrderCollectionImpl(CycObject cycObject) throws KBTypeException {
    super(cycObject);
  }

  /**
   * This not part of the public, supported KB API. finds or creates an instance of #$FirstOrderCollection represented
   * by foColStr in the underlying KB
   * <p>
   *
   * @param foColStr  the string representing an instance of #$FirstOrderCollection in the KB
   * 
   * @throws CreateException if the #$FirstOrderCollection represented by foColStr is not found
   * and could not be created 
   * @throws KBTypeException if the term represented by foColStr is not an instance
   * of #$FirstOrderCollection and cannot be made into one. 
   */
  FirstOrderCollectionImpl(String foColStr) throws KBTypeException, CreateException {
    super(foColStr);
  }

  /**
   * This not part of the public, supported KB API. finds or creates; or finds an instance of #$FirstOrderCollection
   * represented by foColStr in the underlying KB based on input ENUM
   * <p>
   *
   * @param foColStr  the string representing an instance of #$FirstOrderCollection in the KB
   * @param lookup the enum to specify LookupType: FIND or FIND_OR_CREATE
   * 
   * @throws CreateException 
   * @throws KBTypeException 
   *
   * @throws KBObjectNotFoundException  if the #$FirstOrderCollection represented by foColStr
   * is not found and could not be created
   * @throws InvalidNameException if the string foColStr does not conform to Cyc constant-naming
   * conventions
   * 
   * @throws KBTypeException  if the term represented by foColStr is not an instance of #$FirstOrderCollection and lookup is
   * set to find only {@link LookupType#FIND}
   * @throws KBTypeConflictException if the term represented by foColStr is not an instance of #$FirstOrderCollection,
   * and lookup is set to find or create; and if the term cannot be made an instance #$FirstOrderCollection by asserting
   * new knowledge.
   */
  FirstOrderCollectionImpl(String foColStr, LookupType lookup) throws KBTypeException, CreateException {
    super(foColStr, lookup);
  }
    
  /**
   * Get the
   * <code>FirstOrderCollection</code> with the name
   * <code>nameOrId</code>. Throws exceptions if there is no KB term by that
   * name, or if it is not already an instance of #$FirstOrderCollection.
   *
   * @param nameOrId the string representation or the HLID of the #$FirstOrderCollection
   * 
   * @return  a new FirstOrderCollection
   * 
   * @throws KBTypeException
   * @throws CreateException 
   */
  public static FirstOrderCollectionImpl get(String nameOrId) throws KBTypeException, CreateException {
      return KBObjectFactory.get(nameOrId, FirstOrderCollectionImpl.class);
  }

  /**
   * Get the
   * <code>FirstOrderCollection</code> object that corresponds to
   * <code>cycObject</code>. Throws exceptions if the object isn't in the KB, or if
   * it's not already an instance of
   * <code>#$FirstOrderCollection</code>.
   *
   * @param cycObject the CycObject wrapped by FirstOrderCollection. The method
   * verifies that the CycObject is an instance of #$FirstOrderCollection
   * 
   * @return a new FirstOrderCollection 
   * 
   * @throws CreateException 
   * @throws KBTypeException
   */
  @Deprecated
    public static FirstOrderCollectionImpl get(CycObject cycObject) throws KBTypeException, CreateException {
      return KBObjectFactory.get(cycObject, FirstOrderCollectionImpl.class);
  }
  
  /**
   * Find or create a
   * <code>FirstOrderCollection</code> object named
   * <code>nameOrId</code>. If no object exists in the KB with the name
   * <code>nameOrId</code>, one will be created, and it will be asserted to be
   * an instance of
   * <code>#$FirstOrderCollection</code>. If there is already an object in the KB called
   * <code>nameOrId</code>, and it is already a
   * <code>#$FirstOrderCollection</code>, it will be returned. If it is not already a
   * <code>#$FirstOrderCollection</code>, but can be made into one by addition of
   * assertions to the KB, such assertions will be made, and the object will be
   * returned. If the object in the KB cannot be turned into a
   * <code>#$FirstOrderCollection</code> by adding assertions (i.e. some existing
   * assertion prevents it from being a
   * <code>#$FirstOrderCollection</code>), a
   * <code>KBTypeConflictException</code>will be thrown.
   *
   * @param nameOrId  the string representation or the HLID of the #$FirstOrderCollection
   * 
   * @return a new FirstOrderCollection
   * 
   * @throws KBTypeException 
   * @throws CreateException 
   */
    public static FirstOrderCollectionImpl findOrCreate(String nameOrId) throws CreateException, KBTypeException  {
      return KBObjectFactory.findOrCreate(nameOrId, FirstOrderCollectionImpl.class);
  }

    /**
     * Find or create a FirstOrderCollection object from
     * <code>cycObject</code>. If
     * <code>cycObject</code> is already a
     * <code>#$FirstOrderCollection</code>, an appropriate
     * <code>FirstOrderCollection</code> object will be returned. If
     * <code>object</code> is not already a
     * <code>#$FirstOrderCollection</code>, but can be made into one by addition of
     * assertions to the KB, such assertions will be made, and the relevant object
     * will be returned. If
     * <code>cycObject</code> cannot be turned into a
     * <code>#$FirstOrderCollection</code> by adding assertions (i.e. some existing
     * assertion prevents it from being a
     * <code>#$FirstOrderCollection</code>, a
     * <code>KBTypeConflictException</code>will be thrown.
     *
     * @param cycObject the CycObject wrapped by FirstOrderCollection. The method
     * verifies that the CycObject is an #$FirstOrderCollection
     * 
     * @return a new FirstOrderCollection
     * 
     * @throws KBTypeException 
     * @throws CreateException 
     */
    @Deprecated
    public static FirstOrderCollectionImpl findOrCreate(CycObject cycObject) throws CreateException, KBTypeException  {
        return KBObjectFactory.findOrCreate(cycObject, FirstOrderCollectionImpl.class);
  }
    
    /**
     * Find or create a
     * <code>FirstOrderCollection</code> object named
     * <code>nameOrId</code>, and also make it an instance of
     * <code>constraintCol</code> in the default context specified by
     * {@link KBAPIDefaultContext#forAssertion()}. If no object
     * exists in the KB with the name
     * <code>nameOrId</code>, one will be created, and it will be asserted to be
     * an instance of both
     * <code>#$FirstOrderCollection</code> and
     * <code>constraintCol</code>. If there is already an object in the
     * KB called
     * <code>nameOrId</code>, and it is already both a
     * <code>#$FirstOrderCollection</code> and a
     * <code>constraintCol</code>, it will be returned. If it is not
     * already both a
     * <code>#$FirstOrderCollection</code> and a
     * <code>constraintCol</code>, but can be made so by addition of
     * assertions to the KB, such assertions will be made, and the object will be
     * returned. If the object in the KB cannot be turned into both a
     * <code>#$FirstOrderCollection</code> and a
     * <code>constraintCol</code> by adding assertions, a
     * <code>KBTypeConflictException</code>will be thrown.
     *
     * @param nameOrId  the string representation or the HLID of the #$FirstOrderCollection
     * @param constraintCol the collection that this #$FirstOrderCollection will instantiate
     * 
     * @return a new FirstOrderCollection
     * 
     * @throws KBTypeException 
     * @throws CreateException 
     */
    public static FirstOrderCollectionImpl findOrCreate(String nameOrId, KBCollection constraintCol) throws CreateException, KBTypeException  {
        return KBObjectFactory.findOrCreate(nameOrId, constraintCol, FirstOrderCollectionImpl.class);
    }

    /**
     * Find or create a
     * <code>FirstOrderCollection</code> object named
     * <code>nameOrId</code>, and also make it an instance of
     * <code>constraintCol</code> in the default context specified by
     * {@link KBAPIDefaultContext#forAssertion()}. If no object
     * exists in the KB with the name
     * <code>nameOrId</code>, one will be created, and it will be asserted to be
     * an instance of both
     * <code>#$FirstOrderCollection</code> and
     * <code>constraintCol</code>. If there is already an object in the
     * KB called
     * <code>nameOrId</code>, and it is already both a
     * <code>#$FirstOrderCollection</code> and a
     * <code>constraintCol</code>, it will be returned. If it is not
     * already both a
     * <code>#$FirstOrderCollection</code> and a
     * <code>constraintCol</code>, but can be made so by addition of
     * assertions to the KB, such assertions will be made, and the object will be
     * returned. If the object in the KB cannot be turned into both a
     * <code>#$FirstOrderCollection</code> and a
     * <code>constraintCol</code> by adding assertions, a
     * <code>KBTypeConflictException</code>will be thrown.
     *
     * @param nameOrId  the string representation or the HLID of the #$FirstOrderCollection
     * @param constraintColStr the string representation of the collection that 
     * this #$FirstOrderCollection will instantiate
     * 
     * @return a new FirstOrderCollection
     * 
     * @throws KBTypeException 
     * @throws CreateException 
     */
    public static FirstOrderCollectionImpl findOrCreate(String nameOrId, String constraintColStr) throws CreateException, KBTypeException {
        return KBObjectFactory.findOrCreate(nameOrId, constraintColStr, FirstOrderCollectionImpl.class);
    }

    /**
     * Find or create a
     * <code>FirstOrderCollection</code> object named
     * <code>nameOrId</code>, and also make it an instance of
     * <code>constraintCol</code> in
     * <code>ctx</code>. If no object exists in the KB with the name
     * <code>nameOrId</code>, one will be created, and it will be asserted to be
     * an instance of both
     * <code>#$FirstOrderCollection</code> and
     * <code>constraintCol</code>. If there is already an object in the
     * KB called
     * <code>nameOrId</code>, and it is already both a
     * <code>#$FirstOrderCollection</code> and a
     * <code>constraintCol</code>, it will be returned. If it is not
     * already both a
     * <code>#$FirstOrderCollection</code> and a
     * <code>constraintCol</code>, but can be made so by addition of
     * assertions to the KB, such assertions will be made, and the object will be
     * returned. If the object in the KB cannot be turned into both a
     * <code>#$FirstOrderCollection</code> and a
     * <code>constraintCol</code> by adding assertions, a
     * <code>KBTypeConflictException</code>will be thrown.
     *
     * @param nameOrId  the string representation or the HLID of the #$FirstOrderCollection
     * @param constraintCol the collection that this #$FirstOrderCollection will instantiate
     * @param ctx the context in which the resulting object must be an instance of
     * constraintCol
     * 
     * @return a new FirstOrderCollection
     * 
     * @throws KBTypeException 
     * @throws CreateException 
     */
    public static FirstOrderCollectionImpl findOrCreate(String nameOrId, KBCollection constraintCol, Context ctx) throws CreateException, KBTypeException {
        return KBObjectFactory.findOrCreate(nameOrId, constraintCol, ctx, FirstOrderCollectionImpl.class);
    }

    /**
     * Find or create a
     * <code>FirstOrderCollection</code> object named
     * <code>nameOrId</code>, and also make it an instance of
     * <code>constraintCol</code> in
     * <code>ctx</code>. If no object exists in the KB with the name
     * <code>nameOrId</code>, one will be created, and it will be asserted to be
     * an instance of both
     * <code>#$FirstOrderCollection</code> and
     * <code>constraintCol</code>. If there is already an object in the
     * KB called
     * <code>nameOrId</code>, and it is already both a
     * <code>#$FirstOrderCollection</code> and a
     * <code>constraintCol</code>, it will be returned. If it is not
     * already both a
     * <code>#$FirstOrderCollection</code> and a
     * <code>constraintCol</code>, but can be made so by addition of
     * assertions to the KB, such assertions will be made, and the object will be
     * returned. If the object in the KB cannot be turned into both a
     * <code>#$FirstOrderCollection</code> and a
     * <code>constraintCol</code> by adding assertions, a
     * <code>KBTypeConflictException</code>will be thrown.
     *
     * @param nameOrId the string representation or the HLID of the term
     * @param constraintColStr the string representation of the collection that 
     * this #$FirstOrderCollection will instantiate
     * @param ctxStr the context in which the resulting object must be an instance of
     * constraintCol
     * 
     * @return a new FirstOrderCollection 
     * 
     * @throws KBTypeException 
     * @throws CreateException 
     */
    public static FirstOrderCollectionImpl findOrCreate(String nameOrId, String constraintColStr, String ctxStr) throws CreateException, KBTypeException {
        return KBObjectFactory.findOrCreate(nameOrId, constraintColStr, ctxStr, FirstOrderCollectionImpl.class);
    }
  
    
    /**
     * Checks whether entity exists in KB and is an instance of #$FirstOrderCollection. If
     * false, {@link #getStatus(String)} may yield more information. This method
     * is equivalent to
     * <code>getStatus(nameOrId).equals(KBStatus.EXISTS_AS_TYPE)</code>.
     *
     * @param nameOrId either the name or HL ID of an entity in the KB
     * @return <code>true</code> if entity exists in KB and is an instance of
     * #$FirstOrderCollection
     */
    public static boolean existsAsType(String nameOrId) {
        return getStatus(nameOrId).equals(KBStatus.EXISTS_AS_TYPE);
    }

    /**
     * Checks whether entity exists in KB and is an instance of #$FirstOrderCollection. If
     * false, {@link #getStatus(CycObject)} may yield more information. This
     * method is equivalent to
     * <code>getStatus(object).equals(KBStatus.EXISTS_AS_TYPE)</code>.
     *
     * @param cycObject the CycObject representation of a KB entity
     * @return <code>true</code> if entity exists in KB and is an instance of
     * #$FirstOrderCollection
     */
    public static boolean existsAsType(CycObject cycObject) {
        return getStatus(cycObject).equals(KBStatus.EXISTS_AS_TYPE);
    }

    /**
     * Returns an KBStatus enum which describes whether
     * <code>nameOrId</code> exists in the KB and is an instance of
     * <code>#$FirstOrderCollection</code>.
     *
     * @param nameOrId either the name or HL ID of an entity in the KB
     * @return an enum describing the existential status of the entity in the KB
     */
    public static KBStatus getStatus(String nameOrId) {
        return KBObjectFactory.getStatus(nameOrId, FirstOrderCollectionImpl.class);
    }

    /**
     * Returns an KBStatus enum which describes whether
     * <code>cycObject</code> exists in the KB and is an instance of
     * <code>#$FirstOrderCollection</code>.
     *
     * @param cycObject the CycObject representation of a KB entity
     * @return an enum describing the existential status of the entity in the KB
     */
    public static KBStatus getStatus(CycObject cycObject) {
        return KBObjectFactory.getStatus(cycObject, FirstOrderCollectionImpl.class);
    }


  

    /* (non-Javadoc)
     * @see com.cyc.kb.FirstOrderCollection#getGeneralizations()
     */
  // This method need not be defined, but is to make sure that the return value
  // is explicitly a list of FirstOrderCollection, not just list of KBCollection
  // which actually does contain FirstOrderCollection getInstances.
  @Override
  public Collection<FirstOrderCollection> getGeneralizations() {
    return getGeneralizations(KBAPIConfiguration.getDefaultContext().forQuery());
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.FirstOrderCollection#getGeneralizations(java.lang.String)
   */
  @Override
  public Collection<FirstOrderCollection> getGeneralizations(String ctxStr) {
    return getGeneralizations(KBUtils.getKBObjectForArgument(ctxStr, ContextImpl.class));
  }

    
  @Override
  public Collection<FirstOrderCollection> getGeneralizations(Context ctx) {
    return (this.<FirstOrderCollection>getValues(Constants.genls(), 1, 2, ctx));
  }
    
  /* (non-Javadoc)
   * @see com.cyc.kb.FirstOrderCollection#addGeneralization(java.lang.String, java.lang.String)
   */
  @Override
  public FirstOrderCollection addGeneralization(String moreGeneralStr, String ctxStr) throws KBTypeException, CreateException {
    FirstOrderCollectionImpl c;
    ContextImpl ctx;
    try {
      c = FirstOrderCollectionImpl.get(moreGeneralStr);
      ctx = ContextImpl.get(ctxStr);
    } catch (KBApiException e){
      throw new IllegalArgumentException(e.getMessage(), e);
    }
    return addGeneralization(c, ctx);
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.FirstOrderCollection#addGeneralization(com.cyc.kb.KBCollection, com.cyc.kb.ContextImpl)
   */
  @Override
  public FirstOrderCollection addGeneralization(KBCollection moreGeneral, Context ctx) throws KBTypeException, CreateException {
    Context ctx1 = Constants.uvMt();
    if (moreGeneral.isInstanceOf((KBCollection)FirstOrderCollectionImpl.getClassType(), ctx1)) { 
    	//this check is not strictly necessary, but will reduce the wff-checking that needs to happen Cyc-side.
      super.addGeneralization(moreGeneral, ctx);
    } else {
      throw new CreateException("The collection " + moreGeneral + " is not an instance of #$FirstOrderCollection");
    }      
    return this;
  }

  
  /**
   * Return the KBCollection as a KBObject of the Cyc term that 
   * underlies this class. 
   * 
   * @return KBCollectionImpl.get("#$FirstOrderCollection");
   */
  @Override
  public KBObject getType() {
    return getClassType();
  }
  
  /**
   * Return the KBCollection as a KBObject of the Cyc term that 
   * underlies this class. 
   * 
   * @return KBCollectionImpl.get("#$FirstOrderCollection");
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
    return "#$FirstOrderCollection";
  }
}
