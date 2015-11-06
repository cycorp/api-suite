package com.cyc.kb.client;

/*
 * #%L
 * File: KBCollectionImpl.java
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

import com.cyc.base.CycApiException;
import com.cyc.base.CycConnectionException;
import com.cyc.base.cycobject.CycList;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.DenotationalTerm;
import com.cyc.base.cycobject.FormulaSentence;
import com.cyc.base.cycobject.Guid;
import com.cyc.baseclient.cycobject.CycArrayList;
import com.cyc.baseclient.cycobject.CycConstantImpl;
import com.cyc.baseclient.cycobject.CycFormulaSentence;
import com.cyc.kb.Context;
import com.cyc.kb.KBAPIEnums.LookupType;
import com.cyc.kb.KBCollection;
import com.cyc.kb.KBFunction;
import com.cyc.kb.KBIndividual;
import com.cyc.kb.KBObject;
import com.cyc.kb.KBStatus;
import com.cyc.kb.Sentence;
import com.cyc.kb.Variable;
import com.cyc.kb.config.KBAPIConfiguration;
import com.cyc.kb.config.KBAPIDefaultContext;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.InvalidNameException;
import com.cyc.kb.exception.KBApiException;
import com.cyc.kb.exception.KBApiRuntimeException;
import com.cyc.kb.exception.KBObjectNotFoundException;
import com.cyc.kb.exception.KBTypeConflictException;
import com.cyc.kb.exception.KBTypeException;
import com.cyc.kb.quant.InstanceRestrictedVariable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A <code>KBCollection</code> object is a facade for a <code>#$Collection</code> 
 * in Cyc KB.
 * 
 * A collection is a thing whose instances share a certain property or feature. 
 * A collection is an abstract concept different from an #$Individual. 
 * Everything in Cyc KB is either an #$Individual or a #$Collection. The extent 
 * of a #$Collection is context dependent. 
 *
 * @author Vijay Raj
 * @version $Id: KBCollectionImpl.java 160801 2015-09-08 19:12:41Z vijay $
 */
public class KBCollectionImpl extends KBTermImpl implements KBCollection {

  private static final Logger log = LoggerFactory.getLogger(KBCollectionImpl.class.getName());
  private static final DenotationalTerm TYPE_CORE =
          new CycConstantImpl("Collection", new Guid("bd5880cc-9c29-11b1-9dad-c379636f7270"));

  static DenotationalTerm getClassTypeCore() {
    return TYPE_CORE;
  }

  /**
   * Not part of the KB API. This default constructor only has the effect of
   * ensuring that there is access to a Cyc server.
   */
  KBCollectionImpl() {
    super();
  }

  /**
   * This not part of the public, supported KB API. An implementation-dependent constructor.
   * <p>
   * It is used when the result of a query is a CycObject and is known to be or
   * requested to be cast as a KBCollection.
   *
   * @param cycObject	the CycObject wrapped by <code>KBCollection</code>. The
   * constructor verifies that the CycObject is a #$Collection
   * 
   * @throws KBTypeException if cycObject is not an #$Collection
   */
  KBCollectionImpl(CycObject cycObject) throws KBTypeException {
    super(cycObject);
  }

  /**
   * This not part of the public, supported KB API. Finds or creates a
   * <code>KBCollection</code> represented by colStr in the underlying KB
   * <p>
   *
   * @param colStr	the string representing a #$Collection in the KB
   * 
   * @throws CreateException if the collection represented by colStr is not found
   * and could not be created
   * @throws KBTypeException if the term represented by colStr is not an instance
   * of #$Collection and cannot be made into one
   */
  protected KBCollectionImpl(String colStr) throws KBTypeException, CreateException {
    super(colStr);
  }

  /**
   * EXPERIMENTAL!!! NOT PART OF THE KB API
   * @param colStr
   * @param l
   * @throws com.cyc.kb.exception.KBTypeException
   * @throws com.cyc.kb.exception.CreateException
   */   
  protected KBCollectionImpl(String colStr, List<Object> l) throws KBTypeException, CreateException {
    super(colStr, l);
  }
  
  public KBCollectionImpl (KBCollection col, Map<String, Object> kboData) {
    super();
    this.setCore(col);
    this.setKboData(kboData);
    if (kboData.get("typeCore") != null) {
      this.setTypeCore((KBCollection) kboData.get("typeCore"));
    }
  }
  
  /**
   * This not part of the public, supported KB API. Finds or creates; or finds a collection 
   * represented by colStr in the underlying KB based on input ENUM. 
   * <p>
   *
   * @param colStr	the string representing a #$Collection in the KB
   * @param lookup the enum to specify LookupType: FIND or FIND_OR_CREATE
   * 
   * @throws CreateException 
   * @throws KBTypeException 
   *
   * @throws KBObjectNotFoundException  if the #$Collection represented by colStr
   * is not found and could not be created
   * @throws InvalidNameException if the string colStr does not conform to Cyc constant-naming
   * conventions
   * 
   * @throws KBTypeException  if the term represented by colStr is not a #$Collection and lookup is
   * set to find only {@link LookupType#FIND} a #$Collection
   * @throws KBTypeConflictException if the term represented by colStr is not a #$Collection,
   * and lookup is set to find or create; and if the term cannot be made a #$Collection by asserting
   * new knowledge. 
   */
  KBCollectionImpl(String colStr, LookupType lookup) throws KBTypeException, CreateException {
    super(colStr, lookup);
  }

  /**
   * Get the
   * <code>KBCollection</code> with the name
   * <code>nameOrId</code>. Throws exceptions if there is no KB term by that
   * name, or if it is not already an instance of #$Collection.
   *
   * @param nameOrId the string representation or the HLID of the #$Collection
   * 
   * @return a new KBCollection
   * 
   * @throws KBTypeException
   * @throws CreateException 
   */
  public static KBCollectionImpl get(String nameOrId) throws KBTypeException, CreateException {
    return KBObjectFactory.get(nameOrId, KBCollectionImpl.class);
  }

  /**
   * Get the
   * <code>KBCollection</code> object that corresponds to
   * <code>cycObject</code>. Throws exceptions if the object isn't in the KB, or if
   * it's not already an instance of
   * <code>#$Collection</code>.
   *
   * @param cycObject the CycObject wrapped by KBCollection. The method
   * verifies that the CycObject is a #$Collection
   * 
   * @return a new KBCollection
   * 
   * @throws CreateException 
   * @throws KBTypeException
   */
  @Deprecated
  public static KBCollectionImpl get(CycObject cycObject) throws KBTypeException, CreateException  {
    return KBObjectFactory.get(cycObject, KBCollectionImpl.class);
  }

  /**
   * Find or create a
   * <code>KBCollection</code> object named
   * <code>nameOrId</code>. If no object exists in the KB with the name
   * <code>nameOrId</code>, one will be created, and it will be asserted to be
   * an instance of
   * <code>#$Collection</code>. If there is already an object in the KB called
   * <code>nameOrId</code>, and it is already a
   * <code>#$Collection</code>, it will be returned. If it is not already a
   * <code>#$Collection</code>, but can be made into one by addition of
   * assertions to the KB, such assertions will be made, and the object will be
   * returned. If the object in the KB cannot be turned into a
   * <code>#$Collection</code> by adding assertions (i.e. some existing
   * assertion prevents it from being a
   * <code>#$Collection</code>), a
   * <code>KBTypeConflictException</code>will be thrown.
   *
   * @param nameOrId the string representation or the HLID of the #$Collection
   * 
   * @return a new KBCollection
   * 
   * @throws KBTypeException 
   * @throws CreateException 
   */
  public static KBCollectionImpl findOrCreate(String nameOrId) throws CreateException, KBTypeException  {
    return KBObjectFactory.findOrCreate(nameOrId, KBCollectionImpl.class);
  }

  /**
   * Find or create a KBCollection object from
   * <code>cycObject</code>. If
   * <code>cycObject</code> is already a
   * <code>#$Collection</code>, an appropriate
   * <code>KBCollection</code> object will be returned. If
   * <code>cycObject</code> is not already a
   * <code>#$Collection</code>, but can be made into one by addition of
   * assertions to the KB, such assertions will be made, and the relevant object
   * will be returned. If
   * <code>cycObject</code> cannot be turned into a
   * <code>#$Collection</code> by adding assertions (i.e. some existing
   * assertion prevents it from being a
   * <code>#$Collection</code>, a
   * <code>KBTypeConflictException</code>will be thrown.
   *
   * @param cycObject the CycObject wrapped by KBCollection. The method
   * verifies that the CycObject is a #$Collection
   * 
   * @return a new KBCollection
   * 
   * @throws KBTypeException 
   * @throws CreateException 
   */
  @Deprecated
  public static KBCollectionImpl findOrCreate(CycObject cycObject) throws CreateException, KBTypeException  {
    return KBObjectFactory.findOrCreate(cycObject, KBCollectionImpl.class);
  }

  /**
   * Find or create a
   * <code>KBCollection</code> object named
   * <code>nameOrId</code>, and also make it an instance of
   * <code>constraintCol</code> in the default context specified by
   * {@link KBAPIDefaultContext#forAssertion()}. If no object
   * exists in the KB with the name
   * <code>nameOrId</code>, one will be created, and it will be asserted to be
   * an instance of both
   * <code>#$Collection</code> and
   * <code>constraintCol</code>. If there is already an object in the
   * KB called
   * <code>nameOrId</code>, and it is already both a
   * <code>#$Collection</code> and a
   * <code>constraintCol</code>, it will be returned. If it is not
   * already both a
   * <code>#$Collection</code> and a
   * <code>constraintCol</code>, but can be made so by addition of
   * assertions to the KB, such assertions will be made, and the object will be
   * returned. If the object in the KB cannot be turned into both a
   * <code>#$Collection</code> and a
   * <code>constraintCol</code> by adding assertions, a
   * <code>KBTypeConflictException</code>will be thrown.
   *
   * @param nameOrId  the string representation or the HLID of the #$Collection
   * @param constraintCol the collection that <code>this</code> #$Collection will instantiate
   * 
   * @return a new KBCollection
   * 
   * @throws KBTypeException 
   * @throws CreateException 
   */
  public static KBCollectionImpl findOrCreate(String nameOrId, KBCollection constraintCol) throws CreateException, KBTypeException  {
    return KBObjectFactory.findOrCreate(nameOrId, constraintCol, KBCollectionImpl.class);
  }

  /**
   * Find or create a
   * <code>KBCollection</code> object named
   * <code>nameOrId</code>, and also make it an instance of
   * <code>constraintCol</code> in the default context specified by
   * {@link KBAPIDefaultContext#forAssertion()}. If no object
   * exists in the KB with the name
   * <code>nameOrId</code>, one will be created, and it will be asserted to be
   * an instance of both
   * <code>#$Collection</code> and
   * <code>constraintCol</code>. If there is already an object in the
   * KB called
   * <code>nameOrId</code>, and it is already both a
   * <code>#$Collection</code> and a
   * <code>constraintCol</code>, it will be returned. If it is not
   * already both a
   * <code>#$Collection</code> and a
   * <code>constraintCol</code>, but can be made so by addition of
   * assertions to the KB, such assertions will be made, and the object will be
   * returned. If the object in the KB cannot be turned into both a
   * <code>#$Collection</code> and a
   * <code>constraintCol</code> by adding assertions, a
   * <code>KBTypeConflictException</code>will be thrown.
   *
   * @param nameOrId the string representation or the HLID of the #$Collection
   * @param constraintColStr the string representation of the collection that 
   * <code>this</code> #$Collection will instantiate
   * 
   * @return a new KBCollection
   * 
   * @throws KBTypeException 
   * @throws CreateException 
   */
  public static KBCollectionImpl findOrCreate(String nameOrId, String constraintColStr) throws CreateException, KBTypeException {
    return KBObjectFactory.findOrCreate(nameOrId, constraintColStr, KBCollectionImpl.class);
  }

  /**
   * Find or create a
   * <code>KBCollection</code> object named
   * <code>nameOrId</code>, and also make it an instance of
   * <code>constraintCol</code> in
   * <code>ctx</code>. If no object exists in the KB with the name
   * <code>nameOrId</code>, one will be created, and it will be asserted to be
   * an instance of both
   * <code>#$Collection</code> and
   * <code>constraintCol</code>. If there is already an object in the
   * KB called
   * <code>nameOrId</code>, and it is already both a
   * <code>#$Collection</code> and a
   * <code>constraintCol</code>, it will be returned. If it is not
   * already both a
   * <code>#$Collection</code> and a
   * <code>constraintCol</code>, but can be made so by addition of
   * assertions to the KB, such assertions will be made, and the object will be
   * returned. If the object in the KB cannot be turned into both a
   * <code>#$Collection</code> and a
   * <code>constraintCol</code> by adding assertions, a
   * <code>KBTypeConflictException</code>will be thrown.
   *
   * @param nameOrId  the string representation or the HLID of the #$Collection
   * @param constraintCol the collection that <code>this</code> #$Collection will instantiate
   * @param ctx the context in which the resulting object must be an instance of
   * constraintCol
   * 
   * @return a new KBCollection
   * 
   * @throws KBTypeException 
   * @throws CreateException 
   */
  public static KBCollectionImpl findOrCreate(String nameOrId, KBCollection constraintCol, ContextImpl ctx) 
      throws CreateException, KBTypeException  {
    return KBObjectFactory.findOrCreate(nameOrId, constraintCol, ctx, KBCollectionImpl.class);
  }

  /**
   * Find or create a
   * <code>KBCollection</code> object named
   * <code>nameOrId</code>, and also make it an instance of
   * <code>constraintCol</code> in
   * <code>ctx</code>. If no object exists in the KB with the name
   * <code>nameOrId</code>, one will be created, and it will be asserted to be
   * an instance of both
   * <code>#$Collection</code> and
   * <code>constraintCol</code>. If there is already an object in the
   * KB called
   * <code>nameOrId</code>, and it is already both a
   * <code>#$Collection</code> and a
   * <code>constraintCol</code>, it will be returned. If it is not
   * already both a
   * <code>#$Collection</code> and a
   * <code>constraintCol</code>, but can be made so by addition of
   * assertions to the KB, such assertions will be made, and the object will be
   * returned. If the object in the KB cannot be turned into both a
   * <code>#$Collection</code> and a
   * <code>constraintCol</code> by adding assertions, a
   * <code>KBTypeConflictException</code>will be thrown.
   *
   * @param nameOrId the string representation or the HLID of the term
   * @param constraintColStr the string representation of the collection that 
   * <code>this</code> #$Collection will instantiate
   * @param ctxStr the context in which the resulting object must be an instance of
   * constraintCol
   * 
   * @return a new KBCollection 
   * 
   * @throws KBTypeException 
   * @throws CreateException 
   */
  public static KBCollectionImpl findOrCreate(String nameOrId, String constraintColStr, String ctxStr) 
      throws CreateException, KBTypeException  {
    return KBObjectFactory.findOrCreate(nameOrId, constraintColStr, ctxStr, KBCollectionImpl.class);
  }

  /**
   * Checks whether nameOrId exists in KB and is an instance of #$Collection. If
   * false, {@link #getStatus(String)} may yield more information. This method
   * is equivalent to
   * <code>getStatus(nameOrId).equals(KBStatus.EXISTS_AS_TYPE)</code>.
   *
   * @param nameOrId either the name or HL ID of an entity in the KB
   * @return <code>true</code> if entity exists in KB and is an instance of
   * #$Collection
   */
  public static boolean existsAsType(String nameOrId) {
    return getStatus(nameOrId).equals(KBStatus.EXISTS_AS_TYPE);
  }

  /**
   * Checks whether object exists in KB and is an instance of #$Collection. If
   * false, {@link #getStatus(CycObject)} may yield more information. This
   * method is equivalent to
   * <code>getStatus(object).equals(KBStatus.EXISTS_AS_TYPE)</code>.
   *
   * @param cycObject the CycObject representation of a KB entity
   * @return <code>true</code> if entity exists in KB and is an instance of
   * #$Collection
   */
  public static boolean existsAsType(CycObject cycObject) {
    return getStatus(cycObject).equals(KBStatus.EXISTS_AS_TYPE);
  }

  /**
   * Returns a KBStatus enum which describes whether
   * <code>nameOrId</code> exists in the KB and is an instance of
   * <code>#$Collection</code>.
   *
   * @param nameOrId either the name or HL ID of an entity in the KB
   * @return an enum describing the existential status of the entity in the KB
   */
  public static KBStatus getStatus(String nameOrId) {
    return KBObjectFactory.getStatus(nameOrId, KBCollectionImpl.class);

  }

  /**
   * Returns a KBStatus enum which describes whether
   * <code>cycObject</code> exists in the KB and is an instance of
   * <code>#$Collection</code>.
   *
   * @param cycObject the CycObject representation of a KB entity
   * @return an enum describing the existential status of the entity in the KB
   */
  public static KBStatus getStatus(CycObject cycObject) {
    return KBObjectFactory.getStatus(cycObject, KBCollectionImpl.class);
  }

  /**
   * Returns the single minimally-general (the most specific) among KBCollections <code>cols</code>.
   * Ties are broken by comparing the count of <code>allGeneralizations</code> which is a rough depth estimate."
   *
   * @param cols the KBCollections among which the most specific is identified
   * 
   * @return The KBCollection that is most specific among cols
   */
  public static KBCollection getMinCol(Collection<KBCollection> cols) {
    try {
      CycList<CycObject> cl = new CycArrayList<CycObject>();
      for (KBCollection col : cols) {
        cl.add(KBObjectImpl.getCore(col));
      }
      String command = "(" + SubLConstants.getInstance().withAllMts.stringApiValue() 
              + " (" + SubLConstants.getInstance().minCol.stringApiValue() + " " + cl.stringApiValue() + "))";
      
      CycObject co = getStaticAccess().converse().converseCycObject(command);
      return KBCollectionImpl.get(co);
    } catch (CycConnectionException e) {
      throw new KBApiRuntimeException(e.getMessage(), e);
    } catch (CreateException ce){
      // We are guaranteed to find concepts in the KB and they will be KBCollection in the unlikely
      // event something goes wrong, because in the middle of the operation the CycKB changes,
      // we throw a runtime exception
      throw new KBApiRuntimeException("The min-col identified could not be found at construction time.", ce);
    } catch (KBTypeException te){
      throw new KBApiRuntimeException("The min-col identified is not a #$Collection at construction time.", te);
    }
  }
  
  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#allSpecializations()
   */
  @Override
  public Collection<KBCollection> allSpecializations() {
    return allSpecializations((Context) null);
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#allSpecializations(java.lang.String)
   */
  @Override
  public Collection<KBCollection> allSpecializations(String ctxStr) {
    return allSpecializations(KBUtils.getKBObjectForArgument(ctxStr, ContextImpl.class));
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#allSpecializations(com.cyc.kb.ContextImpl)
   */
  @Override
  public Collection<KBCollection> allSpecializations(Context ctx) {
    @SuppressWarnings("rawtypes")
    CycList cycResults;
    Set<KBCollection> results = new HashSet<KBCollection>();
    try {
      if (ctx != null) {
        String command = "(" + SubLConstants.getInstance().allSpecs.stringApiValue() + " " + this.getCore().stringApiValue() + " " + ctx.stringApiValue() + ")";
        cycResults = getAccess().converse().converseList(command);
      } else {
        String command = "(" + SubLConstants.getInstance().removeDuplicates.stringApiValue()
                + " (" + SubLConstants.getInstance().withAllMts.stringApiValue()
                + " (" + SubLConstants.getInstance().allSpecs.stringApiValue() + " " + this.getCore().stringApiValue() + ")))";
        cycResults = getAccess().converse().converseList(command);
      }

      for (Object o : cycResults) {
        try {
          results.add(KBCollectionImpl.get((CycObject) o));
        } catch (CreateException ce){ // ignore
        } catch (KBTypeException te){ // ignore
        }
      }

      return results;
    } catch (CycConnectionException ex) {
      throw new KBApiRuntimeException(ex);
    } catch (CycApiException ex) {
      throw new KBApiRuntimeException(ex);
    }
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#getSpecializations()
   */
  // @todo Consider adding max-specs as another method.
  @Override
  public Collection<KBCollection> getSpecializations() {
    return getSpecializations(KBAPIConfiguration.getDefaultContext().forQuery());
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#getSpecializations(java.lang.String)
   */
  @Override
  public Collection<KBCollection> getSpecializations(String ctxStr) {
    return getSpecializations(KBUtils.getKBObjectForArgument(ctxStr, ContextImpl.class));
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#getSpecializations(com.cyc.kb.ContextImpl)
   */
  @Override
  public Collection<KBCollection> getSpecializations(Context ctx) {
    return this.<KBCollection>getValues(Constants.genls(), 2, 1, ctx);
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#addSpecialization(java.lang.String)
   */
  @Override
  public KBCollection addSpecialization(String moreSpecificStr) throws KBTypeException, CreateException {
    return addSpecialization(KBUtils.getKBObjectForArgument(moreSpecificStr, KBCollectionImpl.class));
  }
  
  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#addSpecialization(java.lang.String, java.lang.String)
   */
  @Override
  public KBCollection addSpecialization(String moreSpecificStr, String ctxStr) throws KBTypeException, CreateException {
    KBCollection c;
    ContextImpl ctx; 
    try {
      ctx = ContextImpl.get(ctxStr);
      c = KBCollectionImpl.get(moreSpecificStr);
    } catch (KBApiException e){
      throw new IllegalArgumentException(e.getMessage(), e);
    }
    
    return addSpecialization(c, ctx);
  }
  
  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#addSpecialization(com.cyc.kb.KBCollection)
   */
  @Override
  public KBCollection addSpecialization(KBCollection moreSpecific) throws KBTypeException, CreateException {
    return addSpecialization(moreSpecific, KBAPIConfiguration.getDefaultContext().forAssertion());
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#addSpecialization(com.cyc.kb.KBCollection, com.cyc.kb.ContextImpl)
   */
  @Override
  public KBCollection addSpecialization(KBCollection moreSpecific, Context ctx) throws KBTypeException, CreateException {
    addFact(ctx, Constants.genls(), 2, (Object) moreSpecific);
    return (KBCollection) this;
  }

  /*
   * genls methods
   */
  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#allGeneralizations()
   */
  @Override
  public Collection<KBCollection> allGeneralizations() {
    return allGeneralizations((Context) null);
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#allGeneralizations(java.lang.String)
   */
  @Override
  public Collection<KBCollection> allGeneralizations(String ctxStr) {
    return allGeneralizations(KBUtils.getKBObjectForArgument(ctxStr, ContextImpl.class));
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#allGeneralizations(com.cyc.kb.ContextImpl)
   */
  @Override
  @SuppressWarnings("unchecked")
  public Collection<KBCollection> allGeneralizations(Context ctx) {
    CycList<Object> cycResults;
    Set<KBCollection> results = new HashSet<KBCollection>();
    try {
      if (ctx != null) {
        String command = "(" + SubLConstants.getInstance().removeDuplicates.stringApiValue() 
                + " (" + SubLConstants.getInstance().allGenls.stringApiValue() 
                + " " + this.getCore().stringApiValue() + " " + ctx.stringApiValue() + "))";
        cycResults = (CycList<Object>) getAccess().converse().converseList(command);
      } else {
        String command = "(" + SubLConstants.getInstance().removeDuplicates.stringApiValue() 
                + " (" + SubLConstants.getInstance().withAllMts + " (" + SubLConstants.getInstance().allGenls + " " 
                + this.getCore().stringApiValue() + ")))";
        cycResults = getAccess().converse().converseList(command);
      }

      for (Object o : cycResults) {
        try {
          results.add(KBCollectionImpl.get((CycObject) o));
        } catch (CreateException ce){ // ignore
        } catch (KBTypeException te){ // ignore
        }
      }

      return results;
    } catch (CycConnectionException ex) {
      throw new KBApiRuntimeException(ex);
    } catch (CycApiException ex) {
      throw new KBApiRuntimeException(ex);
    }
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#getGeneralizations()
   */
  @Override
  public Collection<? extends KBCollection> getGeneralizations() {
    return getGeneralizations(KBAPIConfiguration.getDefaultContext().forQuery());
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#getGeneralizations(java.lang.String)
   */
  @Override
  public Collection<? extends KBCollection> getGeneralizations(String ctxStr) {
    return getGeneralizations(KBUtils.getKBObjectForArgument(ctxStr, ContextImpl.class));
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#getGeneralizations(com.cyc.kb.ContextImpl)
   */
  @Override
  public Collection<? extends KBCollection> getGeneralizations(Context ctx) {
    return (this.<KBCollectionImpl>getValues(Constants.genls(), 1, 2, ctx));
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#addGeneralization(java.lang.String)
   */
  @Override
  public KBCollection addGeneralization(String moreGeneralStr) throws KBTypeException, CreateException {
    return addGeneralization(KBUtils.getKBObjectForArgument(moreGeneralStr, KBCollectionImpl.class));
  }
  
  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#addGeneralization(java.lang.String, java.lang.String)
   */
  @Override
  public KBCollection addGeneralization(String moreGeneralStr, String ctxStr) throws KBTypeException, CreateException {
    KBCollectionImpl c;
    ContextImpl ctx;
    try {
      c = KBCollectionImpl.get(moreGeneralStr);
      ctx = ContextImpl.get(ctxStr);
    } catch (KBApiException e){
      throw new IllegalArgumentException(e.getMessage(), e);
    }
    return addGeneralization(c, ctx);
  }
  
  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#addGeneralization(com.cyc.kb.KBCollection)
   */
  @Override
  public KBCollection addGeneralization(KBCollection moreGeneral) throws KBTypeException, CreateException {
    return addGeneralization(moreGeneral, KBAPIConfiguration.getDefaultContext().forAssertion());
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#addGeneralization(com.cyc.kb.KBCollectionImpl, com.cyc.kb.ContextImpl)
   */
  @Override
  public KBCollection addGeneralization(KBCollection moreGeneral, Context ctx) throws KBTypeException, CreateException {
    addFact(ctx, Constants.genls(), 1, (Object) moreGeneral);
    return this;
  }
  
  @Override
  public Sentence addGeneralizationSentence(KBCollection moreGeneral) throws KBTypeException, CreateException {
    return new SentenceImpl(Constants.genls(), this, (Object) moreGeneral);
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#getInstances()
   */
  @Override
  public <O> Collection<? extends O> getInstances() {
    return getInstances(KBAPIConfiguration.getDefaultContext().forQuery());
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#getInstances(java.lang.String)
   */
  @Override
  public <O> Collection<O> getInstances(String ctxStr) {
    return getInstances(KBUtils.getKBObjectForArgument(ctxStr, ContextImpl.class));
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#getInstances(com.cyc.kb.ContextImpl)
   */
  @Override
  public <O> Collection<O> getInstances(Context ctx) {
    return (this.<O>getValues(Constants.isa(), 2, 1, ctx));
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#instancesOf()
   */
  @Override
  public Collection<KBCollection> instancesOf() {
    return instancesOf(KBAPIConfiguration.getDefaultContext().forQuery());
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#instancesOf(java.lang.String)
   */
  @Override
  public Collection<KBCollection> instancesOf(String ctxStr) {
    return instancesOf(KBUtils.getKBObjectForArgument(ctxStr, ContextImpl.class));
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#instancesOf(com.cyc.kb.ContextImpl)
   */
  @Override
  public Collection<KBCollection> instancesOf(Context ctx) {
    return (this.<KBCollection>getValues(Constants.isa(), 1, 2, ctx));
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#instantiates(java.lang.String, java.lang.String)
   */
  @Override
  public KBCollectionImpl instantiates(String colStr, String ctxStr) throws KBTypeException, CreateException {
    ContextImpl ctx;
    KBCollection col;
    try {
      ctx = ContextImpl.get(ctxStr);
      col = KBCollectionImpl.get(colStr);
    } catch (KBApiException e){
      throw new IllegalArgumentException(e.getMessage(), e);
    }
    return instantiates(col, ctx);
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#instantiates(com.cyc.kb.KBCollection, com.cyc.kb.ContextImpl)
   */
  @Override
  public KBCollectionImpl instantiates(KBCollection col, Context ctx) throws KBTypeException, CreateException {
    addFact(ctx, Constants.isa(), 1, (Object) col);
    return this;
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#isGeneralizationOf(com.cyc.kb.KBCollectionImpl, com.cyc.kb.ContextImpl)
   */
  @Override
  public boolean isGeneralizationOf(KBCollection moreSpecific, Context ctx) {
    try {
      return getAccess().getInspectorTool().isGenlOf(core, getCore(moreSpecific), getCore(ctx));
    } catch (CycConnectionException e) {
      throw new KBApiRuntimeException(e);
    }
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#isGeneralizationOf(java.lang.String)
   */
  @Override
  public boolean isGeneralizationOf(String moreSpecificStr) {
    try {
      return getAccess().getInspectorTool().isGenlOf(core, KBCollectionImpl.get(moreSpecificStr).getCore());
    } catch (CycConnectionException e) {
      throw new KBApiRuntimeException(e);
    } catch (KBApiException e){
      throw new IllegalArgumentException(e.getMessage(), e);
    }
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#isGeneralizationOf(com.cyc.kb.KBCollectionImpl)
   */
  @Override
  public boolean isGeneralizationOf(KBCollection moreSpecific) {
    try {
      return getAccess().getInspectorTool().isGenlOf(core, getCore(moreSpecific));
    } catch (CycConnectionException e) {
      throw new KBApiRuntimeException(e);
    }
  }
  
  /**
   * Return the KBCollection as a KBObject of the Cyc term that 
   * underlies this class. 
   * 
   * @return KBCollectionImpl.get("#$Collection");
   */
  @Override
  public KBObject getType() {
    return getClassType();
  }
  
  /**
   * Return the KBCollection as a KBObject of the Cyc term that 
   * underlies this class. 
   * 
   * @return KBCollectionImpl.get("#$Collection");
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
    return "#$Collection";
  }
  
  /**
   * This not part of the public, supported KB API
   *
   * @return FormulaSentence
   * @throws KBApiException
   */
  public FormulaSentence toSentence() throws KBApiException {
    FormulaSentence cfs = null;
    Variable v = this.getVariable();
    cfs = CycFormulaSentence.makeCycFormulaSentence(Constants.isa().getCore(), v.getCore(), core);
    return cfs;
  }
   
    /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#getVariable()
   */
  @Override
  public Variable getVariable() throws KBApiException {
    return new VariableImpl(getVariableName());
  }
  
  private String getVariableName()  {
    String varName = null;
    if (!this.isAtomic()) {
      try {
        KBFunction f = this.<KBFunction>getArgument(0);
        if (f.isInstanceOf(KBCollectionImpl.get("SubcollectionRelationFunction"))) {
          varName = "SUB-" + ((KBCollectionImpl)this.<KBCollection>getArgument(1)).getVariableName();
        } 
      } catch (Exception e) {
        log.warn("Tried to get variable name intelligently and failed. " + e.getMessage());
        // Just get it by string manipulation
      }
    }
    if (varName == null) {
      varName = this.toString().replaceAll("\\W+", "");
      String capitals = varName.replaceAll("[a-z]+", "");
      if (capitals.length() < 3) {
        return varName.toUpperCase().substring(0, 3);
      } else {
        // Return upto five capital letter of a compound word
        return capitals.length() > 5 ? capitals.substring(0, 5) : capitals;
      }
    } else {
      return varName;
    }
  }

  public KBIndividual getTypedIndividual() throws KBApiException {
    KBIndividual indVar = KBIndividualImpl.get( this.getVariable().toString() ); 
    return new KBIndividualImpl(this,  indVar);
  }
  
  public KBIndividual getTypedIndividualWithData(Map<String, Object> kboData) throws KBApiException {
    KBIndividual indVar = KBIndividualImpl.get( this.getVariable().toString() ); 
    kboData.put("typeCore", this);
    kboData.put("constantName", deriveRandomizedInstanceName(this));
    return new KBIndividualImpl(indVar, kboData);
  }
  
  private static String deriveRandomizedInstanceName(KBObject kbo){
    String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    StringBuffer sb = new StringBuffer();
    int charLength = characters.length();
    for (int i=0; i<10; i++) {
      double idx = Math.random() * charLength;
      sb.append(characters.charAt((int) idx));
    }
    return kbo.toString().replaceAll("\\W+", "") + "-" + sb.toString();
  }
  
  @Deprecated
  public static KBCollectionImpl from(KBCollection col) {
    return (KBCollectionImpl) col;
  }

  /**
   * This not part of the public, supported KB API
   *
   * @param ctx the context
   * @return InstanceRestrictedVariable
   * @throws KBApiException
   */
  public InstanceRestrictedVariable toInstanceRestrictedVariable(Context ctx) throws KBApiException {
    return new InstanceRestrictedVariable(ctx, this);
  }

  /**
   * This not part of the public, supported KB API
   *
   * @return InstanceRestrictedVariable
   * @throws KBApiException
   */
  public InstanceRestrictedVariable toInstanceRestrictedVariable() throws KBApiException {
    return toInstanceRestrictedVariable((Context) null);
  }

  /**
   * This not part of the public, supported KB API
   *
   * @param var the variable
   * @return InstanceRestrictedVariable
   * @throws KBApiException
   */
  public InstanceRestrictedVariable toInstanceRestrictedVariable(Variable var) throws KBApiException {
    return toInstanceRestrictedVariable(null, var);
  }

  /**
   * This not part of the public, supported KB API
   *
   * @param ctx the context
   * @param var the variable
   * @return InstanceRestrictedVariable
   * @throws KBApiException
   */
  public InstanceRestrictedVariable toInstanceRestrictedVariable(Context ctx, Variable var) throws KBApiException {
    return new InstanceRestrictedVariable(ctx, this, var);
  }
}
