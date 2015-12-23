package com.cyc.kb.client;

/*
 * #%L
 * File: KbObjectCache.java
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
import com.cyc.base.cycobject.Nart;
import com.cyc.kb.KbObject;
import com.cyc.kb.exception.KbTypeException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author nwinant
 */
public class KbObjectCache {

  // Fields
  
  private final InnerCache cache = new InnerCache();
  
  
  // Constructor
  
  protected KbObjectCache() {}
  
  
  // Public
  
  /**
   * Caches a KBObject by its core CycObject, and an array of name Strings.
   * Note that this method will call {@link #processNames(KBObject, java.lang.String[])}
   * to inspect the CycObject itself for any 
   * @param kbobj
   * @param names 
   * @return KBObject
   */
  synchronized public KbObject put(KbObject kbobj, String[] names) {
    final String[] processedNames = processNames(kbobj, names);
    final Class clazz = kbobj.getClass();
    cache.store(KbObjectImpl.getCore(kbobj), clazz, kbobj);
    for (String name : processedNames) {
      cache.store(name, clazz, kbobj);
    }
    return kbobj;
  }
  
  /**
   * 
   * @param kbobj 
   */
  public void put(KbObject kbobj) {
    put(kbobj, processNames(kbobj, new String[0]));
  }
  
  /**
   * 
   * @param <O>
   * @param obj
   * @param clazz
   * @return <O extends KBObject> O
   * @throws KbTypeException 
   */
  public <O extends KbObject> O get(CycObject obj, Class<O> clazz) throws KbTypeException {
    return typeCheck(cache.lookup(obj).get(clazz), clazz);
  }
  
  /**
   * 
   * @param <O>
   * @param name
   * @param clazz
   * @return <O extends KBObject> O
   * @throws KbTypeException 
   */
  public <O extends KbObject> O get(String name, Class<O> clazz) throws KbTypeException {
    return typeCheck(cache.lookup(name).get(clazz), clazz);
  }
  
  /**
   * Returns an <em>unmodifiable</em> Collection of all KBObjects indexed by a particular
   * CycObject.
   * 
   * @param obj
   * @return n <em>unmodifiable</em> Collection of all KBObjects indexed by a particular
   * CycObject
   */
  public Collection<KbObject> getAll(CycObject obj) {
    return Collections.unmodifiableCollection(cache.lookup(obj).values());
  }
  
  /**
   * Returns an <em>unmodifiable</em> Collection of all KBObjects indexed by a particular
   * String.
   * 
   * @param name
   * @return n <em>unmodifiable</em> Collection of all KBObjects indexed by a particular
   * String
   */
  public Collection<KbObject> getAll(String name) {
    return Collections.unmodifiableCollection(cache.lookup(name).values());
  }
  
  /**
   * 
   * @param obj
   * @return true if obj contains a key
   */
  public boolean containsKey(CycObject obj) {
    return !cache.lookup(obj).isEmpty();
  }
  
  /**
   * 
   * @param name
   * @return true if name contains a key
   */
  public boolean containsKey(String name) {
    return !cache.lookup(name).isEmpty();
  }
  
  
  // Protected

  /** 
   * Examines a list of names for a {@link com.cyc.base.cycobject.CycObject}, and
   * adds any names which may be missing.
   * @param kbobj
   * @param names
   * @return a list of names
   */
  protected String[] processNames(KbObject kbobj, String[] names) {
    throw new UnsupportedOperationException("yo!");
    
    //return new String[]{"",""};
  }
  
  /**
   * 
   * @param <O>
   * @param kbobj
   * @param clazz
   * @return <O extends KBObject> O
   * @throws KbTypeException 
   */
  protected <O extends KbObject> O typeCheck(KbObject kbobj, Class<O> clazz) throws KbTypeException {
    if (kbobj == null) {
      return null;
    }
    if (clazz.isInstance(kbobj)) {
      return (O) kbobj;
    }
    throw new KbTypeException("The object \"" + kbobj + "\" can not be made an instance of \"" + clazz + "\"");
  }
  
  
  // Inner Classes
  
  /**
   * This is the true heart of the cache. It indexes KBObjects by both CycObject 
   * and String, and then by Class.
   */
  private static class InnerCache {

    /**
     * The CycObject-indexed cache. This field should never, <em>ever</em> be accessed directly. 
     * Always go through {@link #lookup(com.cyc.base.cycobject.CycObject)} and
     * {@link #store(com.cyc.base.cycobject.CycObject, java.lang.Class, com.cyc.kb.KBObject)).
     */
    final private Map<CycObject, Map<Class<?>, KbObject>> coreCache = new HashMap<CycObject, Map<Class<?>, KbObject>>();
    
    /**
     * The String-indexed cache. This field should never, <em>ever</em> be accessed directly.
     * Always go through {@link #lookup(java.lang.String)} 
     * and {@link #store(java.lang.String, java.lang.Class, com.cyc.kb.KBObject)}.
     */
    final private Map<String, Map<Class<?>, KbObject>> nameCache = new HashMap<String, Map<Class<?>, KbObject>>();

    /**
     * 
     * @param obj
     * @return 
     */
    private CycObject makeCycObjectIndexable(CycObject obj) {
      if (Nart.class.isInstance(obj)) {
        return ((Nart) obj).getFormula();
      }
      return obj;
    }

    /**
     * 
     * @param obj
     * @return 
     */
    synchronized protected Map<Class<?>, KbObject> lookup(CycObject obj) {
      final CycObject key = makeCycObjectIndexable(obj);
      if (!coreCache.containsKey(key) || (coreCache.get(key) == null)) {
        coreCache.put(key, new HashMap<Class<?>, KbObject>());
      }
      return coreCache.get(key);
    }

    /**
     * 
     * @param name
     * @return 
     */
    synchronized protected Map<Class<?>, KbObject> lookup(String name) {
      if (!nameCache.containsKey(name) || (nameCache.get(name) == null)) {
        nameCache.put(name, new HashMap<Class<?>, KbObject>());
      }
      return nameCache.get(name);
    }

    /**
     * 
     * @param obj
     * @param clazz
     * @param kbobj
     * @return 
     */
    protected KbObject store(CycObject obj, Class<?> clazz, KbObject kbobj) {
      return lookup(obj).put(clazz, kbobj);
    }

    /**
     * 
     * @param name
     * @param clazz
     * @param kbobj
     * @return 
     */
    protected KbObject store(String name, Class<?> clazz, KbObject kbobj) {
      return lookup(name).put(clazz, kbobj);
    }
  }
}
