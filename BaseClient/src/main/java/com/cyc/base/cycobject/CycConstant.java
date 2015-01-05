package com.cyc.base.cycobject;

/*
 * #%L
 * File: CycConstant.java
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

import java.io.Serializable;

/**
 *
 * @author nwinant
 */
public interface CycConstant extends Fort, Serializable {
  
  /**
   * Returns this object in a form suitable for use as an <tt>CycList</tt> api expression value.
   *
   * @return this object in a form suitable for use as an <tt>CycList</tt> api expression value
   */
 // Object cycListApiValue();
  
  /**
   * Returns the name of the <tt>CycConstant</tt> with "#$" prefixed.
   *
   * @return the name of the <tt>CycConstant</tt> with "#$" prefixed.
   */
 // String cyclify();
  
  /**
   * Returns <tt>true</tt> some object equals this <tt>CycConstant</tt>. The equality check uses only the guid.
   *
   * @param object the <tt>Object</tt> for equality comparison
   * @return equals <tt>boolean</tt> value indicating equality or non-equality.
   */
//  boolean equals(Object object);
  
  /**
   * Returns <tt>true</tt> some object equals this <tt>CycConstant</tt>. The equality check uses only the guid.
   *
   * @param object the <tt>Object</tt> for equality comparison
   * @return equals <tt>boolean</tt> value indicating equality or non-equality.
   */
//  boolean equalsAtEL(Object object);
  
  /**
   * Gets the guid
   *
   * @return the guid
   */
  public Guid getGuid();

  /**
   * Gets the name
   *
   * @return the name
   */
  public String getName();

  /**
   * Sets the guid
   * @param newGuid
   */
  public void setGuid(Guid newGuid);

  /**
   * Sets the name, which should only be called to update a renamed constant.
   *
   * @param name the name
   */
  public String setName(final String name);
  
  /**
   * #$ ("hash-dollar"), the prefix for all Cyc constants.
   */
  public static final String HD = "#$";
}
