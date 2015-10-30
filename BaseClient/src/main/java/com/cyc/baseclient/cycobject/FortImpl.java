package com.cyc.baseclient.cycobject;

/*
 * #%L
 * File: FortImpl.java
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

import com.cyc.base.cycobject.DenotationalTerm;
import com.cyc.base.cycobject.Fort;
import com.cyc.base.cycobject.Nart;
import java.io.*;

/**
 * This class implements a Cyc Fort (First Order Reified Term).
 *
 * @version $Id: FortImpl.java 155703 2015-01-05 23:15:30Z nwinant $
 * @author Stephen L. Reed
 */
public abstract class FortImpl extends DefaultCycObject implements Fort, DenotationalTerm, Serializable {
  
  /**
   * Compares this object with the specified object for order.
   * Returns a negative integer, zero, or a positive integer as this
   * object is less than, equal to, or greater than the specified object.
   *
   * @param object the reference object with which to compare.
   * @return a negative integer, zero, or a positive integer as this
   * object is less than, equal to, or greater than the specified object
   */
  public int compareTo(Object object) {
    if (this instanceof CycConstantImpl) {
      if (object instanceof CycConstantImpl)
        return this.toString().compareTo(object.toString());
      else if (object instanceof Nart)
        return this.toString().compareTo(object.toString().substring(1));
      else
        throw new ClassCastException("Must be a CycFort object");
    }
    else {
      if (object instanceof Nart)
        return this.toString().compareTo(object.toString());
      else if (object instanceof CycConstantImpl)
        return this.toString().substring(1).compareTo(object.toString());
      else
        throw new ClassCastException("Must be a CycFort object");
    }
  }
  
  /**
   * Returns <tt>true</tt> some object equals this <tt>CycConstantImpl</tt>. The equality check uses only the guid.
   *
   * @param object the <tt>Object</tt> for equality comparison
   * @return equals <tt>boolean</tt> value indicating equality or non-equality.
   */
  abstract public boolean equals(Object object);
  
  /**
   * Returns <tt>true</tt> some object equals this <tt>FortImpl</tt> at the EL level.
   *
   * @param object the <tt>Object</tt> for equality comparison
   * @return equals <tt>boolean</tt> value indicating equality or non-equality.
   */
  abstract public boolean equalsAtEL(Object object);
  
  /**
   * When true, indicates that the fort is invalid.
   */
  protected boolean isInvalid = false;
  
}

