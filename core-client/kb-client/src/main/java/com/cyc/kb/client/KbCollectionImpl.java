package com.cyc.kb.client;

/*
 * #%L
 * File: KbCollectionImpl.java
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

import com.cyc.base.exception.CycApiException;
import com.cyc.base.exception.CycConnectionException;
import com.cyc.base.cycobject.CycList;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.DenotationalTerm;
import com.cyc.base.cycobject.FormulaSentence;
import com.cyc.base.cycobject.Guid;
import com.cyc.baseclient.cycobject.CycArrayList;
import com.cyc.baseclient.cycobject.CycConstantImpl;
import com.cyc.baseclient.cycobject.CycFormulaSentence;
import com.cyc.kb.Context;
import com.cyc.kb.client.LookupType;
import com.cyc.kb.KbCollection;
import com.cyc.kb.KbFunction;
import com.cyc.kb.KbIndividual;
import com.cyc.kb.KbObject;
import com.cyc.kb.KbStatus;
import com.cyc.kb.Sentence;
import com.cyc.kb.Variable;
import com.cyc.kb.client.config.KbConfiguration;
import com.cyc.kb.client.config.KbDefaultContext;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.InvalidNameException;
import com.cyc.kb.exception.KbException;
import com.cyc.kb.exception.KbRuntimeException;
import com.cyc.kb.exception.KbObjectNotFoundException;
import com.cyc.kb.exception.KbTypeConflictException;
import com.cyc.kb.exception.KbTypeException;
import com.cyc.kb.client.quant.InstanceRestrictedVariable;
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
 * @version $Id: KbCollectionImpl.java 163117 2015-12-11 00:27:39Z nwinant $
 */
public class KbCollectionImpl extends KbTermImpl implements KbCollection {

  private static final Logger log = LoggerFactory.getLogger(KbCollectionImpl.class.getName());
  private static final DenotationalTerm TYPE_CORE =
          new CycConstantImpl("Collection", new Guid("bd5880cc-9c29-11b1-9dad-c379636f7270"));

  static DenotationalTerm getClassTypeCore() {
    return TYPE_CORE;
  }

  /**
   * Not part of the KB API. This default constructor only has the effect of
   * ensuring that there is access to a Cyc server.
   */
  KbCollectionImpl() {
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
   * @throws KbTypeException if cycObject is not an #$Collection
   */
  KbCollectionImpl(CycObject cycObject) throws KbTypeException {
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
   * @throws KbTypeException if the term represented by colStr is not an instance
   * of #$Collection and cannot be made into one
   */
  protected KbCollectionImpl(String colStr) throws KbTypeException, CreateException {
    super(colStr);
  }

  /**
   * EXPERIMENTAL!!! NOT PART OF THE KB API
   * @param colStr
   * @param l
   * @throws com.cyc.kb.exception.KbTypeException
   * @throws com.cyc.kb.exception.CreateException
   */   
  protected KbCollectionImpl(String colStr, List<Object> l) throws KbTypeException, CreateException {
    super(colStr, l);
  }
  
  public KbCollectionImpl (KbCollection col, Map<String, Object> kboData) {
    super();
    this.setCore(col);
    this.setKboData(kboData);
    if (kboData.get("typeCore") != null) {
      this.setTypeCore((KbCollection) kboData.get("typeCore"));
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
   * @throws KbTypeException 
   *
   * @throws KbObjectNotFoundException  if the #$Collection represented by colStr
   * is not found and could not be created
   * @throws InvalidNameException if the string colStr does not conform to Cyc constant-naming
   * conventions
   * 
   * @throws KbTypeException  if the term represented by colStr is not a #$Collection and lookup is
   * set to find only {@link LookupType#FIND} a #$Collection
   * @throws KbTypeConflictException if the term represented by colStr is not a #$Collection,
   * and lookup is set to find or create; and if the term cannot be made a #$Collection by asserting
   * new knowledge. 
   */
  KbCollectionImpl(String colStr, LookupType lookup) throws KbTypeException, CreateException {
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
   * @throws KbTypeException
   * @throws CreateException 
   */
  public static KbCollectionImpl get(String nameOrId) throws KbTypeException, CreateException {
    return KbObjectFactory.get(nameOrId, KbCollectionImpl.class);
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
   * @throws KbTypeException
   */
  @Deprecated
  public static KbCollectionImpl get(CycObject cycObject) throws KbTypeException, CreateException  {
    return KbObjectFactory.get(cycObject, KbCollectionImpl.class);
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
   * @throws KbTypeException 
   * @throws CreateException 
   */
  public static KbCollectionImpl findOrCreate(String nameOrId) throws CreateException, KbTypeException  {
    return KbObjectFactory.findOrCreate(nameOrId, KbCollectionImpl.class);
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
   * @throws KbTypeException 
   * @throws CreateException 
   */
  @Deprecated
  public static KbCollectionImpl findOrCreate(CycObject cycObject) throws CreateException, KbTypeException  {
    return KbObjectFactory.findOrCreate(cycObject, KbCollectionImpl.class);
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
   * @throws KbTypeException 
   * @throws CreateException 
   */
  public static KbCollectionImpl findOrCreate(String nameOrId, KbCollection constraintCol) throws CreateException, KbTypeException  {
    return KbObjectFactory.findOrCreate(nameOrId, constraintCol, KbCollectionImpl.class);
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
   * @throws KbTypeException 
   * @throws CreateException 
   */
  public static KbCollectionImpl findOrCreate(String nameOrId, String constraintColStr) throws CreateException, KbTypeException {
    return KbObjectFactory.findOrCreate(nameOrId, constraintColStr, KbCollectionImpl.class);
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
   * @throws KbTypeException 
   * @throws CreateException 
   */
  public static KbCollectionImpl findOrCreate(String nameOrId, KbCollection constraintCol, ContextImpl ctx) 
      throws CreateException, KbTypeException  {
    return KbObjectFactory.findOrCreate(nameOrId, constraintCol, ctx, KbCollectionImpl.class);
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
   * @throws KbTypeException 
   * @throws CreateException 
   */
  public static KbCollectionImpl findOrCreate(String nameOrId, String constraintColStr, String ctxStr) 
      throws CreateException, KbTypeException  {
    return KbObjectFactory.findOrCreate(nameOrId, constraintColStr, ctxStr, KbCollectionImpl.class);
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
    return getStatus(nameOrId).equals(KbStatus.EXISTS_AS_TYPE);
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
    return getStatus(cycObject).equals(KbStatus.EXISTS_AS_TYPE);
  }

  /**
   * Returns a KBStatus enum which describes whether
   * <code>nameOrId</code> exists in the KB and is an instance of
   * <code>#$Collection</code>.
   *
   * @param nameOrId either the name or HL ID of an entity in the KB
   * @return an enum describing the existential status of the entity in the KB
   */
  public static KbStatus getStatus(String nameOrId) {
    return KbObjectFactory.getStatus(nameOrId, KbCollectionImpl.class);

  }

  /**
   * Returns a KBStatus enum which describes whether
   * <code>cycObject</code> exists in the KB and is an instance of
   * <code>#$Collection</code>.
   *
   * @param cycObject the CycObject representation of a KB entity
   * @return an enum describing the existential status of the entity in the KB
   */
  public static KbStatus getStatus(CycObject cycObject) {
    return KbObjectFactory.getStatus(cycObject, KbCollectionImpl.class);
  }

  /**
   * Returns the single minimally-general (the most specific) among KBCollections <code>cols</code>.
   * Ties are broken by comparing the count of <code>allGeneralizations</code> which is a rough depth estimate."
   *
   * @param cols the KBCollections among which the most specific is identified
   * 
   * @return The KBCollection that is most specific among cols
   */
  public static KbCollection getMinCol(Collection<KbCollection> cols) {
    try {
      CycList<CycObject> cl = new CycArrayList<CycObject>();
      for (KbCollection col : cols) {
        cl.add(KbObjectImpl.getCore(col));
      }
      String command = "(" + SublConstants.getInstance().withAllMts.stringApiValue() 
              + " (" + SublConstants.getInstance().minCol.stringApiValue() + " " + cl.stringApiValue() + "))";
      
      CycObject co = getStaticAccess().converse().converseCycObject(command);
      return KbCollectionImpl.get(co);
    } catch (CycConnectionException e) {
      throw new KbRuntimeException(e.getMessage(), e);
    } catch (CreateException ce){
      // We are guaranteed to find concepts in the KB and they will be KBCollection in the unlikely
      // event something goes wrong, because in the middle of the operation the CycKB changes,
      // we throw a runtime exception
      throw new KbRuntimeException("The min-col identified could not be found at construction time.", ce);
    } catch (KbTypeException te){
      throw new KbRuntimeException("The min-col identified is not a #$Collection at construction time.", te);
    }
  }
  
  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#allSpecializations()
   */
  @Override
  public Collection<KbCollection> allSpecializations() {
    return allSpecializations((Context) null);
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#allSpecializations(java.lang.String)
   */
  @Override
  public Collection<KbCollection> allSpecializations(String ctxStr) {
    return allSpecializations(KbUtils.getKBObjectForArgument(ctxStr, ContextImpl.class));
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#allSpecializations(com.cyc.kb.ContextImpl)
   */
  @Override
  public Collection<KbCollection> allSpecializations(Context ctx) {
    @SuppressWarnings("rawtypes")
    CycList cycResults;
    Set<KbCollection> results = new HashSet<KbCollection>();
    try {
      if (ctx != null) {
        String command = "(" + SublConstants.getInstance().allSpecs.stringApiValue() + " " + this.getCore().stringApiValue() + " " + ctx.stringApiValue() + ")";
        cycResults = getAccess().converse().converseList(command);
      } else {
        String command = "(" + SublConstants.getInstance().removeDuplicates.stringApiValue()
                + " (" + SublConstants.getInstance().withAllMts.stringApiValue()
                + " (" + SublConstants.getInstance().allSpecs.stringApiValue() + " " + this.getCore().stringApiValue() + ")))";
        cycResults = getAccess().converse().converseList(command);
      }

      for (Object o : cycResults) {
        try {
          results.add(KbCollectionImpl.get((CycObject) o));
        } catch (CreateException ce){ // ignore
        } catch (KbTypeException te){ // ignore
        }
      }

      return results;
    } catch (CycConnectionException ex) {
      throw new KbRuntimeException(ex);
    } catch (CycApiException ex) {
      throw new KbRuntimeException(ex);
    }
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#getSpecializations()
   */
  // @todo Consider adding max-specs as another method.
  @Override
  public Collection<KbCollection> getSpecializations() {
    return getSpecializations(KbConfiguration.getDefaultContext().forQuery());
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#getSpecializations(java.lang.String)
   */
  @Override
  public Collection<KbCollection> getSpecializations(String ctxStr) {
    return getSpecializations(KbUtils.getKBObjectForArgument(ctxStr, ContextImpl.class));
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#getSpecializations(com.cyc.kb.ContextImpl)
   */
  @Override
  public Collection<KbCollection> getSpecializations(Context ctx) {
    return this.<KbCollection>getValues(Constants.genls(), 2, 1, ctx);
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#addSpecialization(java.lang.String)
   */
  @Override
  public KbCollection addSpecialization(String moreSpecificStr) throws KbTypeException, CreateException {
    return addSpecialization(KbUtils.getKBObjectForArgument(moreSpecificStr, KbCollectionImpl.class));
  }
  
  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#addSpecialization(java.lang.String, java.lang.String)
   */
  @Override
  public KbCollection addSpecialization(String moreSpecificStr, String ctxStr) throws KbTypeException, CreateException {
    KbCollection c;
    ContextImpl ctx; 
    try {
      ctx = ContextImpl.get(ctxStr);
      c = KbCollectionImpl.get(moreSpecificStr);
    } catch (KbException e){
      throw new IllegalArgumentException(e.getMessage(), e);
    }
    
    return addSpecialization(c, ctx);
  }
  
  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#addSpecialization(com.cyc.kb.KBCollection)
   */
  @Override
  public KbCollection addSpecialization(KbCollection moreSpecific) throws KbTypeException, CreateException {
    return addSpecialization(moreSpecific, KbConfiguration.getDefaultContext().forAssertion());
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#addSpecialization(com.cyc.kb.KBCollection, com.cyc.kb.ContextImpl)
   */
  @Override
  public KbCollection addSpecialization(KbCollection moreSpecific, Context ctx) throws KbTypeException, CreateException {
    addFact(ctx, Constants.genls(), 2, (Object) moreSpecific);
    return (KbCollection) this;
  }

  /*
   * genls methods
   */
  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#allGeneralizations()
   */
  @Override
  public Collection<KbCollection> allGeneralizations() {
    return allGeneralizations((Context) null);
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#allGeneralizations(java.lang.String)
   */
  @Override
  public Collection<KbCollection> allGeneralizations(String ctxStr) {
    return allGeneralizations(KbUtils.getKBObjectForArgument(ctxStr, ContextImpl.class));
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#allGeneralizations(com.cyc.kb.ContextImpl)
   */
  @Override
  @SuppressWarnings("unchecked")
  public Collection<KbCollection> allGeneralizations(Context ctx) {
    CycList<Object> cycResults;
    Set<KbCollection> results = new HashSet<KbCollection>();
    try {
      if (ctx != null) {
        String command = "(" + SublConstants.getInstance().removeDuplicates.stringApiValue() 
                + " (" + SublConstants.getInstance().allGenls.stringApiValue() 
                + " " + this.getCore().stringApiValue() + " " + ctx.stringApiValue() + "))";
        cycResults = (CycList<Object>) getAccess().converse().converseList(command);
      } else {
        String command = "(" + SublConstants.getInstance().removeDuplicates.stringApiValue() 
                + " (" + SublConstants.getInstance().withAllMts + " (" + SublConstants.getInstance().allGenls + " " 
                + this.getCore().stringApiValue() + ")))";
        cycResults = getAccess().converse().converseList(command);
      }

      for (Object o : cycResults) {
        try {
          results.add(KbCollectionImpl.get((CycObject) o));
        } catch (CreateException ce){ // ignore
        } catch (KbTypeException te){ // ignore
        }
      }

      return results;
    } catch (CycConnectionException ex) {
      throw new KbRuntimeException(ex);
    } catch (CycApiException ex) {
      throw new KbRuntimeException(ex);
    }
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#getGeneralizations()
   */
  @Override
  public Collection<? extends KbCollection> getGeneralizations() {
    return getGeneralizations(KbConfiguration.getDefaultContext().forQuery());
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#getGeneralizations(java.lang.String)
   */
  @Override
  public Collection<? extends KbCollection> getGeneralizations(String ctxStr) {
    return getGeneralizations(KbUtils.getKBObjectForArgument(ctxStr, ContextImpl.class));
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#getGeneralizations(com.cyc.kb.ContextImpl)
   */
  @Override
  public Collection<? extends KbCollection> getGeneralizations(Context ctx) {
    return (this.<KbCollectionImpl>getValues(Constants.genls(), 1, 2, ctx));
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#addGeneralization(java.lang.String)
   */
  @Override
  public KbCollection addGeneralization(String moreGeneralStr) throws KbTypeException, CreateException {
    return addGeneralization(KbUtils.getKBObjectForArgument(moreGeneralStr, KbCollectionImpl.class));
  }
  
  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#addGeneralization(java.lang.String, java.lang.String)
   */
  @Override
  public KbCollection addGeneralization(String moreGeneralStr, String ctxStr) throws KbTypeException, CreateException {
    KbCollectionImpl c;
    ContextImpl ctx;
    try {
      c = KbCollectionImpl.get(moreGeneralStr);
      ctx = ContextImpl.get(ctxStr);
    } catch (KbException e){
      throw new IllegalArgumentException(e.getMessage(), e);
    }
    return addGeneralization(c, ctx);
  }
  
  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#addGeneralization(com.cyc.kb.KBCollection)
   */
  @Override
  public KbCollection addGeneralization(KbCollection moreGeneral) throws KbTypeException, CreateException {
    return addGeneralization(moreGeneral, KbConfiguration.getDefaultContext().forAssertion());
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#addGeneralization(com.cyc.kb.KBCollectionImpl, com.cyc.kb.ContextImpl)
   */
  @Override
  public KbCollection addGeneralization(KbCollection moreGeneral, Context ctx) throws KbTypeException, CreateException {
    addFact(ctx, Constants.genls(), 1, (Object) moreGeneral);
    return this;
  }
  
  @Override
  public Sentence addGeneralizationSentence(KbCollection moreGeneral) throws KbTypeException, CreateException {
    return new SentenceImpl(Constants.genls(), this, (Object) moreGeneral);
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#getInstances()
   */
  @Override
  public <O> Collection<? extends O> getInstances() {
    return getInstances(KbConfiguration.getDefaultContext().forQuery());
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#getInstances(java.lang.String)
   */
  @Override
  public <O> Collection<O> getInstances(String ctxStr) {
    return getInstances(KbUtils.getKBObjectForArgument(ctxStr, ContextImpl.class));
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
  public Collection<KbCollection> instancesOf() {
    return instancesOf(KbConfiguration.getDefaultContext().forQuery());
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#instancesOf(java.lang.String)
   */
  @Override
  public Collection<KbCollection> instancesOf(String ctxStr) {
    return instancesOf(KbUtils.getKBObjectForArgument(ctxStr, ContextImpl.class));
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#instancesOf(com.cyc.kb.ContextImpl)
   */
  @Override
  public Collection<KbCollection> instancesOf(Context ctx) {
    return (this.<KbCollection>getValues(Constants.isa(), 1, 2, ctx));
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#instantiates(java.lang.String, java.lang.String)
   */
  @Override
  public KbCollectionImpl instantiates(String colStr, String ctxStr) throws KbTypeException, CreateException {
    ContextImpl ctx;
    KbCollection col;
    try {
      ctx = ContextImpl.get(ctxStr);
      col = KbCollectionImpl.get(colStr);
    } catch (KbException e){
      throw new IllegalArgumentException(e.getMessage(), e);
    }
    return instantiates(col, ctx);
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#instantiates(com.cyc.kb.KBCollection, com.cyc.kb.ContextImpl)
   */
  @Override
  public KbCollectionImpl instantiates(KbCollection col, Context ctx) throws KbTypeException, CreateException {
    addFact(ctx, Constants.isa(), 1, (Object) col);
    return this;
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#isGeneralizationOf(com.cyc.kb.KBCollectionImpl, com.cyc.kb.ContextImpl)
   */
  @Override
  public boolean isGeneralizationOf(KbCollection moreSpecific, Context ctx) {
    try {
      return getAccess().getInspectorTool().isGenlOf(core, getCore(moreSpecific), getCore(ctx));
    } catch (CycConnectionException e) {
      throw new KbRuntimeException(e);
    }
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#isGeneralizationOf(java.lang.String)
   */
  @Override
  public boolean isGeneralizationOf(String moreSpecificStr) {
    try {
      return getAccess().getInspectorTool().isGenlOf(core, KbCollectionImpl.get(moreSpecificStr).getCore());
    } catch (CycConnectionException e) {
      throw new KbRuntimeException(e);
    } catch (KbException e){
      throw new IllegalArgumentException(e.getMessage(), e);
    }
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#isGeneralizationOf(com.cyc.kb.KBCollectionImpl)
   */
  @Override
  public boolean isGeneralizationOf(KbCollection moreSpecific) {
    try {
      return getAccess().getInspectorTool().isGenlOf(core, getCore(moreSpecific));
    } catch (CycConnectionException e) {
      throw new KbRuntimeException(e);
    }
  }
  
  /**
   * Return the KBCollection as a KBObject of the Cyc term that 
   * underlies this class. 
   * 
   * @return KBCollectionImpl.get("#$Collection");
   */
  @Override
  public KbObject getType() {
    return getClassType();
  }
  
  /**
   * Return the KBCollection as a KBObject of the Cyc term that 
   * underlies this class. 
   * 
   * @return KBCollectionImpl.get("#$Collection");
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
    return "#$Collection";
  }
  
  /**
   * This not part of the public, supported KB API
   *
   * @return FormulaSentence
   * @throws KbException
   */
  public FormulaSentence toSentence() throws KbException {
    FormulaSentence cfs = null;
    Variable v = this.getVariable();
    cfs = CycFormulaSentence.makeCycFormulaSentence(Constants.isa().getCore(), v.getCore(), core);
    return cfs;
  }
   
  /* (non-Javadoc)
   * @see com.cyc.kb.KBCollection#getVariable()
   */
  public Variable getVariable() throws KbException {
    return new VariableImpl(getVariableName());
  }
  
  private String getVariableName()  {
    String varName = null;
    if (!this.isAtomic()) {
      try {
        KbFunction f = this.<KbFunction>getArgument(0);
        if (f.isInstanceOf(KbCollectionImpl.get("SubcollectionRelationFunction"))) {
          varName = "SUB-" + ((KbCollectionImpl)this.<KbCollection>getArgument(1)).getVariableName();
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

  public KbIndividual getTypedIndividual() throws KbException {
    KbIndividual indVar = KbIndividualImpl.get( this.getVariable().toString() ); 
    return new KbIndividualImpl(this,  indVar);
  }
  
  public KbIndividual getTypedIndividualWithData(Map<String, Object> kboData) throws KbException {
    KbIndividual indVar = KbIndividualImpl.get( this.getVariable().toString() ); 
    kboData.put("typeCore", this);
    kboData.put("constantName", deriveRandomizedInstanceName(this));
    return new KbIndividualImpl(indVar, kboData);
  }
  
  private static String deriveRandomizedInstanceName(KbObject kbo){
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
  public static KbCollectionImpl from(KbCollection col) {
    return (KbCollectionImpl) col;
  }

  /**
   * This not part of the public, supported KB API
   *
   * @param ctx the context
   * @return InstanceRestrictedVariable
   * @throws KbException
   */
  public InstanceRestrictedVariable toInstanceRestrictedVariable(Context ctx) throws KbException {
    return new InstanceRestrictedVariable(ctx, this);
  }

  /**
   * This not part of the public, supported KB API
   *
   * @return InstanceRestrictedVariable
   * @throws KbException
   */
  public InstanceRestrictedVariable toInstanceRestrictedVariable() throws KbException {
    return toInstanceRestrictedVariable((Context) null);
  }

  /**
   * This not part of the public, supported KB API
   *
   * @param var the variable
   * @return InstanceRestrictedVariable
   * @throws KbException
   */
  public InstanceRestrictedVariable toInstanceRestrictedVariable(Variable var) throws KbException {
    return toInstanceRestrictedVariable(null, var);
  }

  /**
   * This not part of the public, supported KB API
   *
   * @param ctx the context
   * @param var the variable
   * @return InstanceRestrictedVariable
   * @throws KbException
   */
  public InstanceRestrictedVariable toInstanceRestrictedVariable(Context ctx, Variable var) throws KbException {
    return new InstanceRestrictedVariable(ctx, this, var);
  }
}
