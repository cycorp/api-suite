package com.cyc.kb.client;

/*
 * #%L
 * File: KBObjectFactory.java
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

import com.cyc.base.CycAccess;
import com.cyc.base.CycApiException;
import com.cyc.base.CycConnectionException;
import com.cyc.base.cycobject.CycAssertion;
import com.cyc.base.cycobject.CycObject;
import com.cyc.baseclient.CycObjectFactory;
import com.cyc.baseclient.cycobject.DefaultCycObject;
import com.cyc.kb.Context;
import com.cyc.kb.KBAPIEnums.LookupType;
import com.cyc.kb.KBCollection;
import com.cyc.kb.KBObject;
import com.cyc.kb.KBStatus;
import static com.cyc.kb.client.KBObjectImpl.getStaticAccess;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KBApiRuntimeException;
import com.cyc.kb.exception.KBObjectNotFoundException;
import com.cyc.kb.exception.KBTypeConflictException;
import com.cyc.kb.exception.KBTypeException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class provides factory methods to build instance of KBObjects and its subclasses. 
 * The class also provides caching for the retrieved objects. Note that the cache may be stale
 * and the API does not yet attempt to synchronize based on KB Events.  
 * 
 * The class and the methods of this class are not part of the KB API.
 *  
 * @author David Baxter
 * @version $Id: KBObjectFactory.java 155043 2014-11-18 18:40:24Z baxter $ 
 */

public class KBObjectFactory {

  private static Logger log = LoggerFactory.getLogger(KBObjectFactory.class.getCanonicalName());
  
  //a cache from the cyclified names/ids/non-cyclified names to classes to KBObjects
  private static final Map<String, Map<Class<?>, KBObjectImpl>> stringCache = new ConcurrentHashMap<String, Map<Class<?>, KBObjectImpl>>();  
  private static final List<Class<? extends KBObjectImpl>> KB_OBJECT_TYPES = Arrays.asList(AssertionImpl.class,
          BinaryPredicateImpl.class,
          ContextImpl.class,
          FactImpl.class,
          FirstOrderCollectionImpl.class,
          KBFunctionImpl.class,
          KBCollectionImpl.class,
          KBIndividualImpl.class,
          KBObjectImpl.class,
          KBTermImpl.class,
          KBPredicateImpl.class,
          QuantifierImpl.class,
          LogicalConnectiveImpl.class,
          ScopingRelationImpl.class,
          RelationImpl.class,
          RuleImpl.class,
          SecondOrderCollectionImpl.class,
          SentenceImpl.class,
          VariableImpl.class);


  /**
   * Find an instance of {@link KBObjectImpl} subclass <code>O</code>,
   * named <code>nameOrId</code>. If no object exists in the KB with the name
   * <code>nameOrId</code>, it will throw a {@link KBObjectNotFoundException}. 
   * 
   * If there is already an object in the KB called <code>nameOrId</code>, 
   * and it is already a {@link StandardKBObject#getType()}, it will be returned. 
   * If it is not already a {@link StandardKBObject#getType()}, a {@link KBTypeException} is thrown
   * 
   * @param nameOrId the string representation or the HLID of the candidate object to be returned 
   * @param c represents the class <code>O</code>, a subclass of {@link KBObjectImpl}
   * 
   * @return an instance of {@link KBObjectImpl} subclass <code>O</code>
   * 
   * @throws CreateException
   * @throws KBTypeException
   */
  private static <O extends KBObjectImpl> O find(String nameOrId, Class<O> c) throws CreateException, KBTypeException {
    O kbObj = KBObjectFactory.<O>getCached(nameOrId, c);
    if (kbObj != null) {
      log.trace("The object " + kbObj + " was retrieved from cache.");
      return kbObj;
    }
    // Try subclasses of c, using most specific that works.
    Class<? extends KBObjectImpl> bestClass = c;
    kbObj = (O) getAsInstanceOfSpecifiedClass(nameOrId, bestClass, LookupType.FIND);
    if (kbObj != null) {
      bestClass = kbObj.getClass();
    }
    if (kbObj == null || !kbObj.isVariable()){
      log.trace("Attempting to find a more specific class than " + bestClass + " for " + kbObj);
      for (final Class<? extends KBObjectImpl> subclass : KB_OBJECT_TYPES) {
        if (bestClass.isAssignableFrom(subclass) && !bestClass.equals(subclass)) {
          try {
            final O asSubclass = (O) getAsInstanceOfSpecifiedClass(nameOrId, subclass, LookupType.FIND);
            if (asSubclass != null) {
              kbObj = (O) cacheKBObject(asSubclass, nameOrId, subclass);
              log.trace("Found a more specific class " + subclass + " than " + bestClass + " for " + kbObj);
              bestClass = subclass;
            }
          } catch (Exception ex) {
            KBApiRuntimeException.rethrowIfCycConnectionException(ex);
            
            // Guess it's not one of those.
            
            // FIXME: do something here.
          } catch (Throwable t) {
            // FIXME: do something here.
          }
        }
      }
    }
    if (kbObj == null) {
      String msg = "No KB object \"" + nameOrId + "\" as " + c.getSimpleName() + ".";
      log.error(msg);
      throw new KBObjectNotFoundException(msg);
    } else {
      log.trace("Found " + kbObj + " and cached it");
      return (O) cacheKBObject(kbObj, nameOrId, c);
    }
  }

  /**
   * This method tries to construct a object of Class <code>c</code> with <code>nameOrId</code>
   * and <code>lookup</code> as parameters to the constructor. 
   * 
   * Refer to {@link StandardKBObject#StandardKBObject(String, LookupType)} for the type of constructor
   * the method is looking for. 
   * 
   * @param nameOrId  the string representation or the HLID of the candidate object to be returned 
   * @param c the class of the object to be constructed
   * @param lookup  find or create the candidate object
   * 
   * @return a object of type <code>c</code> which is a subclass of KBObject 
   * 
   * @throws CreateException Refer to {@link StandardKBObject#StandardKBObject(String, LookupType)}
   * @throws KBTypeException Refer to {@link StandardKBObject#StandardKBObject(String, LookupType)}
   * 
   * @throws IllegalArgumentException Java run-time exception
   * @throws SecurityException  Java run-time exception
   */
  private static <O extends KBObjectImpl> O getAsInstanceOfSpecifiedClass(String nameOrId,
          Class<O> c, LookupType lookup) throws IllegalArgumentException, SecurityException, CreateException, KBTypeException {
    O kbObj = null;
    try {
      kbObj = (O) c.getDeclaredConstructor(String.class, LookupType.class).newInstance(nameOrId, lookup);
      kbObj = cacheKBObject(kbObj, nameOrId, c);
      log.trace("Found " + kbObj + " and cached it");
    } catch (InvocationTargetException ex) {
      KBApiRuntimeException.rethrowIfCycConnectionException(ex.getCause());
      if (ex.getCause() instanceof KBTypeException) {
        throw (KBTypeException) ex.getCause();
      } else if (ex.getCause() instanceof CreateException) {
        throw (CreateException) ex.getCause();
      } else {
        log.error(ex.getMessage());
      }
    } catch (NoSuchMethodException ex) {
      log.trace(ex.getMessage());
    } catch (InstantiationException ex) {
      log.trace(ex.getMessage());
    } catch (IllegalAccessException ex) {
      log.error(ex.getMessage());
    }
    return kbObj;
  }

  /**
   * Clear all caches relating
   * <code>KBObject</code>s to objects on the Cyc server. In most applications, this will not be
   * needed. However, it can be helpful in applications where the KB is modified externally. For
   * example, if a Cyc term is deleted and then recreated with the same name by some external
   * process, the KBAPI will still have the id information from the old constant, and will retrieve
   * the new constant. A call to
   * <code>clearKBObjectCache</code> will clear the cache and allow the KBAPI to successfully
   * retrieve the newly created constant.
   */
  public static void clearKBObjectCache() {
    log.info("Cleaning the cache");
    stringCache.clear();
    CycObjectFactory.resetCycConstantCaches();
  }
  
  /** Attempt to find a CycObject <code>cycObject</code> in the cache, as an instance of 
   * a subclass <code>O</code> of KBObject
   * 
   * @param cycObject
   * @param c represents the class <code>O</code>, a subclass of {@link KBObjectImpl}
   * 
   * @return the cached {@link KBObjectImpl}, or null if there is no cached value
   */
  private static <O extends KBObject> O getCached(CycObject cycObject, Class<O> c) {
    return getCached(getCacheKey(cycObject), c);
  }

  /**
   * Attempt to find an object of class <code>c</code> represented by <code>nameOrId</code>
   * in the local API cache
   * 
   * @param nameOrId the string representation or the HLID of the candidate object to be returned 
   * @param c represents the class <code>O</code>, a subclass of {@link KBObjectImpl}
   * 
   * @return the cached {@link KBObjectImpl}, or null if there is no cached value
   */
  private static <O extends KBObject> O getCached(String nameOrId, Class<O> c) {
    O returnValue = null;
    List<String> invalidKeys = new ArrayList<String>();
    if (stringCache.containsKey(nameOrId)) {
      //if we find something that could be a c, return it.  It might be a subclass, but that's OK.
      log.trace("Found cache-key \"" + nameOrId + "\" in the cache");
      for (Entry<Class<?>, KBObjectImpl> e : stringCache.get(nameOrId).entrySet()) {
        if (c.isAssignableFrom(e.getValue().getClass()) && e.getValue().isValid()) {
          returnValue = (O) e.getValue();
          log.debug("Found \"" + returnValue + "\" in the cache");
        } else if (!e.getValue().isValid()) {
          log.info("The cached entry " + e + " is not valid anymore! Adding to the cache.");
          invalidKeys.add(nameOrId);
          invalidKeys.add(e.getValue().getCore().cyclify());
          invalidKeys.add(e.getValue().getCore().toString());
        }
      }
    }
    if (!invalidKeys.isEmpty()) {
      for (String key : invalidKeys) {
        stringCache.remove(key);
      }
    }
    return returnValue;
  }

  /**
   * Cache any newly created object. The keys to the object are, <code>nameOrId</code>, the cyclified
   * string representation of the "core" object and the toString() representation of the "core" object.  
   * 
   * @param kbObject the {@link KBObjectImpl} to be cached
   * @param nameOrId  one of the keys to the {@link KBObjectImpl} in the cache
   * @param c represents the class <code>O</code>, a subclass of {@link KBObjectImpl}
   * 
   * @return the kbObject casted to class <code>O</code>
   */
  private static <O extends KBObject> O cacheKBObject(KBObjectImpl kbObject, String nameOrId, Class<O> c) {
    CycObject core = kbObject.getCore();
    String cyclifiedCore = core.cyclify();
    log.trace("Storing " + kbObject + " in cache");
    if (stringCache.containsKey(cyclifiedCore)
            && stringCache.get(cyclifiedCore).containsKey(c)) {
      //if this is already in the cache, but not by the ID, use the existing one.
      if (!stringCache.containsKey(nameOrId)) {
        kbObject = stringCache.get(cyclifiedCore).get(c);
      }
    }
    String coreString = getCacheKey(core);
    cacheAs(nameOrId, c, kbObject);
    cacheAs(cyclifiedCore, c, kbObject);
    cacheAs(coreString, c, kbObject);
    return (O) kbObject;
  }

  /**
   * cache the <code>kbObject</code> with the key <code>key</code>
   * 
   * @param key the key to the {@link KBObjectImpl} cached
   * @param c represents the class <code>O</code>, a subclass of {@link KBObjectImpl}
   * @param kbObject the {@link KBObjectImpl} to be cached
   */
  private static <O extends KBObject> void cacheAs(String key, Class<O> c, KBObjectImpl kbObject) {
    if (!stringCache.containsKey(key)) {
      stringCache.put(key, new ConcurrentHashMap<Class<?>, KBObjectImpl>());
    }
    KBObjectImpl bestKBObject = kbObject;
    final Map<Class<?>, KBObjectImpl> cacheForKey = stringCache.get(key);
    // Ensure the most specific KBObject is used for all classes:
    for (final KBObjectImpl maybeBest : cacheForKey.values()) {
      if (bestKBObject != maybeBest
              && bestKBObject.getClass().isAssignableFrom(maybeBest.getClass())) {
        bestKBObject = maybeBest;
      }
    }
    cacheForKey.put(c, bestKBObject);
    for (final Class<?> oneClass : cacheForKey.keySet()) {
      cacheForKey.put(oneClass, bestKBObject);
    }
  }

  /**
   * Find an instance of {@link KBObjectImpl} subclass <code>O</code>, based on
   * <code>cycObject</code>. If no object exists based on cycObject in the KB, 
   * it will throw a {@link KBObjectNotFoundException}. 
   * 
   * If there is already an object in the KB based on <code>cycObject</code>, 
   * and it is already a {@link StandardKBObject#getType()}, it will be returned. 
   * If it is not already a {@link StandardKBObject#getType()}, a {@link KBTypeException} is thrown
   * 
   * @param <O> The class of object to be returned
   * @param cycObject the candidate CycObject 
   * @param c represents the class <code>O</code>, a subclass of {@link KBObjectImpl} to be returned
   * 
   * @return an instance of {@link KBObjectImpl} subclass <code>O</code>
   * 
   * @throws CreateException
   * @throws KBTypeException
   */
  //if there's already a CycObject, then just assume that it's a FIND, not a FIND_OR_CREATE
  //@todo document
  @Deprecated
  public static <O extends KBObjectImpl> O get(CycObject cycObject, Class<O> c) throws KBTypeException, CreateException {
    O kbObj = KBObjectFactory.<O>getCached(cycObject, c);
    if (kbObj != null) {
      log.trace("The object " + kbObj + " was retrieved from cache.");
      return kbObj;
    }
    Class<? extends O> bestClass = c;
    for (final Class<? extends KBObjectImpl> subClass : KB_OBJECT_TYPES) {
      if (bestClass.isAssignableFrom(subClass)) {
        try {
          kbObj = (O) subClass.getDeclaredConstructor(CycObject.class).newInstance(cycObject);
          kbObj = cacheKBObject(kbObj, getCacheKey(cycObject), c);
          log.trace("Found a more specific class " + subClass + " than " + bestClass + " for " + kbObj);
          bestClass = (Class<? extends O>) subClass;
        } catch (Exception e) {
        }
      }
    }
    if (kbObj == null) {
      return get(cycObject.cyclify(), c);
    } else {
      return kbObj;
    }
  }
  
  /**
   * Find an instance of {@link KBObjectImpl} subclass <code>O</code>,
   * named <code>nameOrId</code>. If no object exists in the KB with the name
   * <code>nameOrId</code>, it will throw a {@link KBObjectNotFoundException}. 
   * 
   * If there is already an object in the KB called <code>nameOrId</code>, 
   * and it is already a {@link StandardKBObject#getType()}, it will be returned. 
   * If it is not already a {@link StandardKBObject#getType()}, a {@link KBTypeException} is thrown
   * 
   * @param <O> the class of object to be returned
   * @param nameOrId the string representation or the HLID of the candidate object to be returned 
   * @param c represents the class <code>O</code>, a subclass of {@link KBObjectImpl}
   * 
   * @return an instance of {@link KBObjectImpl} subclass <code>O</code>
   * 
   * @throws CreateException
   * @throws KBTypeException
   */
  
  static <O extends KBObjectImpl> O get(String nameOrId, Class<O> c) throws KBTypeException, CreateException {
    return KBObjectFactory.<O>find(nameOrId, c);
  }
  
  /**
   * Find or create an instance of {@link KBObjectImpl} subclass <code>O</code>,
   * named <code>nameOrId</code>. If no object exists in the KB with the name
   * <code>nameOrId</code>, one will be created, and it will be asserted to be
   * an instance of {@link StandardKBObject#getType()} in the KB. 
   * 
   * If there is already an object in the KB called <code>nameOrId</code>, 
   * and it is already a {@link StandardKBObject#getType()}, it will be returned. 
   * If it is not already a {@link StandardKBObject#getType()}, but can be 
   * made into one by addition of
   * assertions to the KB, such assertions will be made, and the object will be
   * returned. If the object in the KB cannot be turned into a
   * {@link StandardKBObject#getType()} by adding assertions (i.e. some existing
   * assertion prevents it from being a
   * {@link StandardKBObject#getType()}), a
   * <code>KBTypeConflictException</code>will be thrown.
   * 
   * @param <O> the class of object to be returned
   * @param nameOrId the string representation or the HLID of the candidate object to be returned 
   * @param c represents the class <code>O</code>, a subclass of {@link KBObjectImpl}
   * 
   * @return an instance of {@link KBObjectImpl} subclass <code>O</code>
   * 
   * @throws CreateException
   * @throws KBTypeException
   */
  static <O extends KBObjectImpl> O findOrCreate(String nameOrId, Class<O> c) throws CreateException, KBTypeException {
    try {
      return (O) find(nameOrId, c);
    } catch (KBObjectNotFoundException ex) {
      return getAsInstanceOfSpecifiedClass(nameOrId, c, LookupType.FIND_OR_CREATE);
    } catch (KBTypeConflictException ex) {
      throw ex;
    } catch (KBTypeException ex) {
      // Coerce to desired type:
      return getAsInstanceOfSpecifiedClass(nameOrId, c, LookupType.FIND_OR_CREATE);
    } 
  }

  /**
   * Find or create an instance of {@link KBObjectImpl} subclass <code>O</code>,
   * based on <code>cycObject</code>. In most cases, existence of <code>cycObject</code>
   * implies that the underlying concept is already in the KB.  
   * 
   * Check if <code>cycObject</code> is already a {@link StandardKBObject#getType()}, it will be returned. 
   * If it is not already a {@link StandardKBObject#getType()}, but can be 
   * made into one by addition of
   * assertions to the KB, such assertions will be made, and the object will be
   * returned. If the object in the KB cannot be turned into a
   * {@link StandardKBObject#getType()} by adding assertions (i.e. some existing
   * assertion prevents it from being a
   * {@link StandardKBObject#getType()}), a
   * <code>KBTypeConflictException</code>will be thrown.
   * 
   * @param <O> the class of object to be returned
   * @param cycObject the candidate CycObject 
   * @param c represents the class <code>O</code>, a subclass of {@link KBObjectImpl} to be returned
   * 
   * @return an instance of {@link KBObjectImpl} subclass <code>O</code>
   * 
   * @throws CreateException
   * @throws KBTypeException
   */
  @Deprecated
  static <O extends KBObjectImpl> O findOrCreate(CycObject cycObject, Class<O> c) 
      throws CreateException, KBTypeException {
    return findOrCreate(cycObject.cyclify(), c);
  }

  /**
   * Find or create an instance of {@link KBObjectImpl} subclass <code>O</code>,
   * named <code>nameOrId</code>, and also make it in instance of <code>constrainingCollection</code>.
   * If no object exists in the KB with the name
   * <code>nameOrId</code>, one will be created, and it will be asserted to be
   * an instance of both {@link StandardKBObject#getType()} and <code>constrainingCollection</code> in the KB. 
   * 
   * If there is already an object in the KB called <code>nameOrId</code>, 
   * and it is already a {@link StandardKBObject#getType()} and a <code>constrainingCollection</code>,
   * it will be returned. 
   * If it is not already a {@link StandardKBObject#getType()} and a <code>constrainingCollection</code>, but can be 
   * made into one by addition of
   * assertions to the KB, such assertions will be made, and the object will be
   * returned. If the object in the KB cannot be turned into both a
   * {@link StandardKBObject#getType()} and a <code>constrainingCollection</code> by adding assertions, a
   * <code>KBTypeConflictException</code>will be thrown.
   * 
   * @param <O> the class of object to be returned
   * @param nameOrId the string representation or the HLID of the candidate object to be returned
   * @param constrainingCollection the additional constraining collection
   * @param c represents the class <code>O</code>, a subclass of {@link KBObjectImpl}
   * 
   * @return an instance of {@link KBObjectImpl} subclass <code>O</code>
   * 
   * @throws CreateException
   * @throws KBTypeException
   */
  static <O extends KBObjectImpl> O findOrCreate(String nameOrId, KBCollection constrainingCollection, Class<O> c) 
      throws CreateException, KBTypeException {
    O kbObject = findOrCreate(nameOrId, c);
    SentenceImpl s = new SentenceImpl(Constants.isa(), kbObject, constrainingCollection);
    FactImpl.findOrCreate(s, Constants.uvMt());
    /*try {
      new Fact(Constants.uvMt(), Constants.isa(), kbObject, constrainingCollection);
    } catch (Exception e) {
      throw new KBTypeException("The object \"" + kbObject + "\" can not be made an instance of \"" + constrainingCollection + "\"");
    }*/
    return kbObject;
  }

  /**
   * @see #findOrCreate(java.lang.String, com.cyc.kb.KBCollection, java.lang.Class) 
   * 
   * Instead of a KBCollection, a string representation of the KBCollection is the input.
   * 
   */
  static <O extends KBObjectImpl> O findOrCreate(String nameOrId, String constrainingCollectionStr,
          Class<O> c) throws CreateException, KBTypeException {
    O kbObject = findOrCreate(nameOrId, c);
    SentenceImpl s = new SentenceImpl(Constants.isa(), kbObject, KBCollectionImpl.get(constrainingCollectionStr));
    FactImpl.findOrCreate(s, Constants.uvMt());
    
    /*try {
      new Fact(Constants.uvMt(), Constants.isa(),
            kbObject, KBCollection.get(constrainingCollectionStr));
    } catch (Exception e){
      throw new KBTypeException("The object \"" + kbObject + "\" can not be made an instance of \"" + constrainingCollectionStr + "\"");
    }*/
    return kbObject;
  }

  /**
   * Find or create an instance of {@link KBObjectImpl} subclass <code>O</code>,
   * named <code>nameOrId</code>, and also make it in instance of <code>constrainingCollection</code>.
   * If no object exists in the KB with the name
   * <code>nameOrId</code>, one will be created, and it will be asserted to be
   * an instance of both {@link StandardKBObject#getType()} and <code>constrainingCollection</code> 
   * in <code>ctx</code> in the KB. 
   * 
   * If there is already an object in the KB called <code>nameOrId</code>, 
   * and it is already a {@link StandardKBObject#getType()} and a <code>constrainingCollection</code>,
   * it will be returned. 
   * If it is not already a {@link StandardKBObject#getType()} and a <code>constrainingCollection</code>, but can be 
   * made into one by addition of
   * assertions to the KB, such assertions will be made, and the object will be
   * returned. If the object in the KB cannot be turned into both a
   * {@link StandardKBObject#getType()} and a <code>constrainingCollection</code> by adding assertions, a
   * <code>KBTypeConflictException</code>will be thrown.
   * 
   * @param <O> the class of object to be returned
   * @param nameOrId the string representation or the HLID of the candidate object to be returned 
   * @param constrainingCollection  the additional constraining collection
   * @param ctx ctx the context in which the resulting object must be an instance of
   * constrainingCollection
   * @param c represents the class <code>O</code>, a subclass of {@link KBObjectImpl} 
   * 
   * @return an instance of {@link KBObjectImpl} subclass <code>O</code>
   * 
   * @throws CreateException
   * @throws KBTypeException
   */
  static <O extends KBObjectImpl> O findOrCreate(String nameOrId,
      KBCollection constrainingCollection, Context ctx, Class<O> c) throws CreateException, KBTypeException {
    O kbObject = findOrCreate(nameOrId, c);
    SentenceImpl s = new SentenceImpl(Constants.isa(), kbObject, constrainingCollection);
    FactImpl.findOrCreate(s, ctx);
    /*try {
      new Fact(ctx, Constants.isa(), kbObject, constrainingCollection);
    } catch (Exception e) {
      throw new KBTypeException("The object \"" + kbObject  + "\" can not be made an instance of \"" + constrainingCollection + "\"");
    }*/
    return kbObject;
  }

  /**
   * @see #findOrCreate(java.lang.String, com.cyc.kb.KBCollection, com.cyc.kb.Context, java.lang.Class) 
   * 
   * Instead of a KBCollection and a Context, the string representations of them is the input.
   */
  static <O extends KBObjectImpl> O findOrCreate(String nameOrId, String constrainingCollectionStr,
 String ctxStr, Class<O> c) throws CreateException, KBTypeException {
    O kbObject = findOrCreate(nameOrId, c);
    SentenceImpl s = new SentenceImpl(Constants.isa(), kbObject, KBCollectionImpl.get(constrainingCollectionStr));
    FactImpl.findOrCreate(s, ContextImpl.get(ctxStr));
    /*try {
      new Fact(Context.get(ctxStr), Constants.isa(), kbObject,
          KBCollection.get(constrainingCollectionStr));
    } catch (Exception e) {
      throw new KBTypeException("The object \"" + kbObject
          + "\" can not be made an instance of \"" + constrainingCollectionStr
          + "\"");
    }*/
    return kbObject;
  }

  /**
   * Returns a KBStatus enum which describes whether
   * <code>nameOrId</code> exists in the KB and is an instance of
   * {@link StandardKBObject#getType()}.
   *
   * @param nameOrId either the name or HL ID of an entity in the KB
   * @param c represents the class <code>O</code>, a subclass of {@link KBObjectImpl} 
   * @return an enum describing the existential status of the entity in the KB 
   */
  public static KBStatus getStatus(String nameOrId, Class<? extends KBObjectImpl> c) {
    CycAccess cyc = getStaticAccess();
    try {
      CycObject cycObject = (CycObject) DefaultCycObject.fromPossibleCompactExternalId(nameOrId, cyc); //also check from names
      if (cycObject == null) {
        String cyclifiedIndStr = cyc.cyclifyString(nameOrId);
        try {
          cycObject = cyc.getLookupTool().getKnownFortByName(cyclifiedIndStr);
        } catch (CycApiException ex) {
          //do nothing, since this exception indicates that it couldn't find a fort by that name
        }
      }
      if (cycObject == null) {
        return KBStatus.DOES_NOT_EXIST;
      }
      return getStatus(cycObject, c);
    } catch (CycConnectionException ex) {
      throw new KBApiRuntimeException(ex.getMessage(), ex);
    }
  }

  /**
   * Returns a KBStatus enum which describes whether
   * <code>cycObject</code> exists in the KB and is an instance of
   * {@link StandardKBObject#getType()}.
   *
   * @param cycObject the candidate CycObject in the KB
   * @param c represents the class <code>O</code>, a subclass of {@link KBObjectImpl} 
   * @return an enum describing the existential status of the entity in the KB
   */
  public static KBStatus getStatus(CycObject cycObject, Class<? extends KBObjectImpl> c) {
    CycAccess cyc = getStaticAccess();
    try {
      final CycObject baseCycTypeCore = KBObjectImpl.getBaseCycTypeCore(c);
      if (cyc.getInspectorTool().isa(cycObject, baseCycTypeCore, cyc.getObjectTool().makeELMt("InferencePSC"))) {
        return KBStatus.EXISTS_AS_TYPE;
      }
      if (cyc.getComparisonTool().provablyNotIsa(cycObject, baseCycTypeCore, cyc.getObjectTool().makeELMt("InferencePSC"))) { 
        //this won't work for NAUT collections, but we shouldn't ever need those...
        return KBStatus.EXISTS_WITH_TYPE_CONFLICT;
      } else {
        return KBStatus.EXISTS_WITH_COMPATIBLE_TYPE;
      }
    } catch (CycConnectionException ex) {
      throw new KBApiRuntimeException(ex.getMessage(), ex);
    }
  }

  /**
   * convert a CycObject to its string representation, which is one of the keys to 
   * the <code>cycObject</code> if it is present in the cache.
   * 
   * @param cycObject
   * @return a string representation of the CycObject
   */
  private static String getCacheKey(CycObject cycObject) {
    return (cycObject instanceof CycAssertion) ? String.valueOf(cycObject.hashCode()) : cycObject.toString();
  }
}
