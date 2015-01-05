package com.cyc.baseclient.util;

/*
 * #%L
 * File: LRUCache.java
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

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

//// External Imports

/**
 * <P>
 * LRUCache is designed to...
 * 
 * @author tbrussea,  Feb 28, 2010, 3:27:51 PM
 * @version $Id: LRUCache.java 155483 2014-12-10 21:56:51Z nwinant $
 */
public class LRUCache<K, V> implements Map<K, V> {

	// // Constructors

	/** Creates a new synchronized instance of LRUCache. */
	public LRUCache(int defaultSize, int maxSize) {
		this(defaultSize, maxSize, true);
	}

	/** Creates a new instance of LRUCache. */
	public LRUCache(int defaultSize, int maxSize, boolean isSynchronized) {
		this(defaultSize, maxSize, isSynchronized, .75f);
	}

	/** Creates a new instance of LRUCache. */
	public LRUCache(int defaultSize, final int maxSize, boolean isSynchronized,
			float loadFactor) {
		this.maxSize = maxSize;
		this.cache = new LinkedHashMap<K, V>(defaultSize, loadFactor, true) {
			private static final long serialVersionUID = 7046745637375687927L;

			@SuppressWarnings("rawtypes")
			@Override
			protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
				V val = eldest.getValue();
				if (val instanceof CachedValue) {
					if (((CachedValue) val).isExpired()) {
						return true;
					}
				}
				return size() > maxSize;
			}
		};
		if (isSynchronized) {
			this.cache = Collections.synchronizedMap(cache);
		}
	}

	// // Public Area
	public void clear() {
		cache.clear();
	}

	public boolean containsKey(Object key) {
		return cache.containsKey((K) key);
	}

	public boolean containsValue(Object value) {
		return cache.containsValue((V) value);
	}

	public Set<Map.Entry<K, V>> entrySet() {
		return cache.entrySet();
	}

	public V get(Object key) {
		return cache.get((K) key);
	}

	public boolean isEmpty() {
		return cache.isEmpty();
	}

	public Set<K> keySet() {
		return cache.keySet();
	}

	public V put(K key, V value) {
		return cache.put(key, value);
	}

	public void putAll(Map<? extends K, ? extends V> m) {
		cache.putAll(m);
	}

	public V remove(Object key) {
		return cache.remove((K) key);
	}

	public int size() {
		return cache.size();
	}

	public Collection<V> values() {
		return cache.values();
	}

	// // Protected Area

	// // Private Area

	// // Internal Rep

	private int maxSize;
	private Map<K, V> cache;

	// // Main

}
