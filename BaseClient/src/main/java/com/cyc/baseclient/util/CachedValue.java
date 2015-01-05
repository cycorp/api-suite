package com.cyc.baseclient.util;

/*
 * #%L
 * File: CachedValue.java
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

//// External Imports

/** 
 * <P>CachedValue is designed to...
 *
 * @author tbrussea, Apr 1, 2010, 3:32:56 PM
 * @version $Id: CachedValue.java 155483 2014-12-10 21:56:51Z nwinant $
 */
public class CachedValue<V> {

  CachedValue(V payload, long keepAliveTimeMsecs) {
    if (payload == null) {
      throw new IllegalArgumentException("Cannot accept null values.");
    }
    if (keepAliveTimeMsecs < 0) {
      throw new IllegalArgumentException("Invalid keepAliveTime" + keepAliveTimeMsecs);
    }
    this.payload = payload;
    this.timeEntered = System.currentTimeMillis();
    this.keepAliveTimeMsecs = keepAliveTimeMsecs;
  }

  public V get() {
    return payload;
  }

  public long getEntryTime() {
    return timeEntered;
  }

  public boolean isExpired() {
    if (keepAliveTimeMsecs == 0) {
      return false;
    }
    return ((System.currentTimeMillis() - keepAliveTimeMsecs) >= timeEntered);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null) { // fast fail
      return false;
    }
    if ((o == this) || (o == payload)) { // fast success
      return true;
    }
    if (!(o instanceof CachedValue)) { // cope with non-cached values
      return o.equals(payload);
    }
    return ((CachedValue) o).payload.equals(payload); // cope with cached values
  }

  @Override
  public String toString() {
    return "" + payload;
  }

  @Override
  public int hashCode() {
    return payload.hashCode();
  }

  private long timeEntered;
  private long keepAliveTimeMsecs;
  private V payload;
}
