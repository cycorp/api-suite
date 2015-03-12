package com.cyc.baseclient.util;

/*
 * #%L
 * File: FreshLRUCache.java
 * Project: Base Client
 * %%
 * Copyright (C) 2013 - 2015 Cycorp, Inc.
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

//// Internal Imports

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/** 
 * <P>FreshLRUCache is designed to...
 *
 * @author tbrussea, Feb 28, 2010, 3:27:51 PM
 * @version $Id: FreshLRUCache.java 155703 2015-01-05 23:15:30Z nwinant $
 */
public class FreshLRUCache<K,V> implements Map<K,V> {

  //// Constructors
  
  /** Creates a new instance of LRUCache. */
  public FreshLRUCache(long defaultKeepAliveMsecs, int defaultSize, int maxSize) {
    this(defaultKeepAliveMsecs, defaultSize, maxSize, false);
  }

  /** Creates a new instance of LRUCache. */
  public FreshLRUCache(long defaultKeepAliveMsecs, int defaultSize, int maxSize,
      boolean isSynchronized) {
    this(defaultKeepAliveMsecs, defaultSize, maxSize, isSynchronized, .75f);
  }

  /** Creates a new instance of LRUCache. */
  public FreshLRUCache(long defaultKeepAliveMsecs, int defaultSize, final int maxSize,
      boolean isSynchronized, float loadFactor) {
    this.defaultKeepAliveMsecs = defaultKeepAliveMsecs;
    cache = new LRUCache<K, CachedValue<V>>(defaultSize, maxSize, isSynchronized, loadFactor);
  }

  //// Public Area

  public void clearStaleEntries() {
    List<K> expiredKeys = new ArrayList(128);
    Set<Map.Entry<K, CachedValue<V>>> entrySet = cache.entrySet();
    for (Map.Entry<K, CachedValue<V>> entry : entrySet) {
      CachedValue<V> cachedVal = entry.getValue();
      if (cachedVal.isExpired()) {
        expiredKeys.add(entry.getKey());
      }
    }
    for (K key : expiredKeys) {
      remove(key);
    }
  }

  public void clear() {
    cache.clear();
  }

  public boolean containsKey(Object key) {
    V val = get((K)key);
    return (val != null);
  }

  @SuppressWarnings("element-type-mismatch")
  public boolean containsValue(Object value) {
    return cache.containsValue(value);
  }

  /** note this is a copy and not backed by the map */
  public Set<Map.Entry<K, V>> entrySet() {
    Set<Map.Entry<K, CachedValue<V>>> entrySet = cache.entrySet();
    List<K> expiredKeys = new ArrayList<K>(128);
    Set<Map.Entry<K, V>> result = new HashSet<Map.Entry<K,V>>(entrySet.size());
    for (Map.Entry<K, CachedValue<V>> entry : entrySet) {
      CachedValue<V> cachedVal = entry.getValue();
      if (cachedVal.isExpired()) {
        expiredKeys.add(entry.getKey());
      } else {
        result.add(new AbstractMap.SimpleEntry<K, V>(entry.getKey(), cachedVal.get()));
      }
    }
    for (K key : expiredKeys) {
      remove(key);
    }
    return result;
  }

  public V get(Object key) {
    CachedValue<V> val = cache.get((K)key);
    if (val == null) {
      return null;
    }
    if (val.isExpired()) {
      cache.remove((K)key);
      return null;
    }
    return val.get();
  }

  public boolean isEmpty() {
    return cache.isEmpty();
  }

  public Set<K> keySet() {
    clearStaleEntries();
    return cache.keySet();
  }

  public V put(K key, V value) {
    return put(key, value, defaultKeepAliveMsecs);
  }

  public V put(K key, V value, long timeoutMsecs) {
    CachedValue<V> newVal = new CachedValue<V>(value, timeoutMsecs);
    CachedValue<V> oldVal = cache.put(key, newVal);
    return (oldVal == null) ? null : oldVal.get();
  }

  public void putAll(Map<? extends K,? extends V> m) {
    for (Map.Entry<? extends K,? extends V> entry : m.entrySet()) {
      put (entry.getKey(), entry.getValue());
    }
  }

  public V remove(Object key) {
    CachedValue<V> oldVal = cache.remove((K)key);
    return (oldVal == null) ? null : oldVal.get();
  }

  public int size() {
    return cache.size();
  }

  /** note this is a copy and not backed by the map */
  public Collection<V> values() {
    Set<Map.Entry<K, CachedValue<V>>> entrySet = cache.entrySet();
    List<K> expiredKeys = new ArrayList<K>(128);
    Collection<V> result = new ArrayList<V>(entrySet.size());
    for (Map.Entry<K, CachedValue<V>> entry : entrySet) {
      CachedValue<V> cachedVal = entry.getValue();
      if (cachedVal.isExpired()) {
        expiredKeys.add(entry.getKey());
      } else {
        result.add(cachedVal.get());
      }
    }
    for (K key : expiredKeys) {
      remove(key);
    }
    return result;
  }
  
  //// Protected Area

  //// Private Area

  //// Internal Rep

  private long defaultKeepAliveMsecs;

  private LRUCache<K, CachedValue<V>> cache;

  //// Main

  public static void main(String[] args) {
    System.out.println("Starting.");
    System.out.flush();
    try {
      FreshLRUCache cache1 = new FreshLRUCache(0, 3, 6);
      cache1.put(1, 2);
      try {
        Thread.sleep(10);
      } catch (InterruptedException ie) {}
      cache1.clearStaleEntries();
      if (cache1.size() != 1) {
        System.out.println("Cached entry was inapprorpriately cleared.");
        System.out.flush();
      } else {
        System.out.println("Cached entry was approrpriately not-cleared.");
        System.out.flush();
      }
      FreshLRUCache cache2 = new FreshLRUCache(5, 3, 6);
      cache2.put(1, 2);
      try {
        Thread.sleep(10);
      } catch (InterruptedException ie) {}
      cache2.clearStaleEntries();
      if (cache2.size() != 0) {
        System.out.println("Cached entry was inapprorpriately not-cleared.");
        System.out.flush();
      } else {
        System.out.println("Cached entry was approrpriately cleared.");
        System.out.flush();
      }
      cache2.put(2, 3);
      cache2.put(3, 4);
      cache2.put(4, 3);
      cache2.put(6, 4);
      cache2.put(7, 4);
      cache2.put(8, 4);
      if (cache2.size() > 6) {
        System.out.println("Cache grew inappropriately large.");
        System.out.flush();
      } else {
        System.out.println("Cache is approrpriate size.");
        System.out.flush();
      }
      FreshLRUCache cache3 = new FreshLRUCache(5, 3, 6);
      cache3.put(3, 2);
      try {
        Thread.sleep(10);
      } catch (InterruptedException ie) {}
      cache3.put(4, 5);
      if (cache3.size() != 1) {
        System.out.println("Cached entry was inapprorpriately not-cleared.");
        System.out.flush();
      } else {
        System.out.println("Cached entry was approrpriately cleared.");
        System.out.flush();
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.err.flush();
    } finally {
      System.out.println("Finished.");
      System.out.flush();
      System.exit(0);
    }
  }

}
