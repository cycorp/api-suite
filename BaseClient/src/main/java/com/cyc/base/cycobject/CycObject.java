package com.cyc.base.cycobject;

/*
 * #%L
 * File: CycObject.java
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

import java.util.List;

/**
 * This interface marks an object as being a
 * CycL object.
 *
 * @version $Id: CycObject.java 155483 2014-12-10 21:56:51Z nwinant $
 * @author  tbrussea
 */
public interface CycObject extends Comparable<Object> {
  
  static final int CYCOBJECT_UNKNOWN      = 0;
  static final int CYCOBJECT_BYTEARRAY    = 1;
  static final int CYCOBJECT_CYCASSERTION = 2;
  static final int CYCOBJECT_CYCFORT      = 3;
  static final int CYCOBJECT_CYCLIST      = 4;
  static final int CYCOBJECT_CYCSYMBOL    = 5;
  static final int CYCOBJECT_CYCVARIABLE  = 6;
  static final int CYCOBJECT_DOUBLE       = 7;
  static final int CYCOBJECT_FLOAT        = 8;
  static final int CYCOBJECT_GUID         = 9;
  static final int CYCOBJECT_INTEGER      =10;
  static final int CYCOBJECT_LONG         =11;
  static final int CYCOBJECT_STRING       =12;
  static final int CYCOBJECT_BIGINTEGER   =13;
 
  /**
   * Returns a cyclified string representation of the Cyc object.
   * A cyclified string representation is one where constants have been
   * prefixed with #$.
   *
   * @return a cyclified <tt>String</tt>.
   */
  public String cyclify();
      
  /**
   * Returns a cyclified string representation of the Cyc object.
   * A cyclified string representation with escape chars is one where
   * constants have been prefixed with #$ and Strings have had an escape
   * character inserted before each character that needs to be escaped in SubL.
   *
   * @return a cyclified <tt>String</tt> with escape characters.
   */ 
  public String cyclifyWithEscapeChars();
  
  /**
   * Returns a list of all constants refered to by this CycObject.
   * For example, a CycConstant will return a List with itself as the
   * value, a nart will return a list of its functor and all the constants refered
   * to by its arguments, a CycList will do a deep search for all constants,
   * a symbol or variable will return the empty list.
   * @return a list of all constants refered to by this CycObject
   **/
  public List getReferencedConstants();
  
  /**
   * Returns this object in a form suitable for use as an <tt>String</tt> api expression value.
   *
   * @return this object in a form suitable for use as an <tt>String</tt> api expression value
   */
  public String stringApiValue();
  
  /**
   * Returns this object in a form suitable for use as an <tt>CycList</tt> api expression value.
   *
   * @return this object in a form suitable for use as an <tt>CycList</tt> api expression value
   */
  public Object cycListApiValue();
}
