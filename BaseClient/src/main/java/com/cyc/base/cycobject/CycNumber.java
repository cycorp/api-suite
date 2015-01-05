package com.cyc.base.cycobject;

/*
 * #%L
 * File: CycNumber.java
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

/**
 *
 * @author nwinant
 */
public interface CycNumber extends DenotationalTerm {

  Object cycExpressionApiValue();

  /**
   * Returns this object in a form suitable for use as an <tt>CycList</tt> api expression value.
   *
   * @return this object in a form suitable for use as an <tt>CycList</tt> api expression value
   */
  Object cycListApiValue();

  /**
   * Returns <tt>true</tt> some object equals this <tt>CycNumber</tt>.
   * In constrast to equals(), this will also return true if a java Number
   * is equals() to the number encapsulated by this CycNumber.
   *
   * @param object the <tt>Object</tt> for equality comparison
   * @return equals <tt>boolean</tt> value indicating equality or non-equality.
   */
  boolean equalsAtEL(Object object);

  /**
   * Gets the value of this CycNumber as a java Number.
   *
   * @return the number as a java Number
   */
  Number getNumber();

  /**
   * Convenience method to compare two CycNumbers numerically.
   * @param other
   * @return true iff this CycNumber is numerically larger than other.
   */
  boolean isGreaterThan(CycNumber other);
}
