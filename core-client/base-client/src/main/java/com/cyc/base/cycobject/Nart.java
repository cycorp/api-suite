package com.cyc.base.cycobject;

/*
 * #%L
 * File: Nart.java
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

import com.cyc.base.CycAccess;
import com.cyc.base.exception.CycConnectionException;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author nwinant
 */
public interface Nart extends Fort, NonAtomicTerm, Serializable {

  /**
   * Returns this object in a form suitable for use as an <tt>CycList</tt> api expression value.
   *
   * @return this object in a form suitable for use as an <tt>CycList</tt> api expression value
   */
  Object cycListApiValue();

  /**
   * Returns a cyclified string representation of the Cyc NART.
   * Embedded constants are prefixed with "#$".
   *
   * @return a cyclified <tt>String</tt>.
   */
  String cyclify();

  /**
   * Returns a cyclified string representation of the Cyc NART.
   * Embedded constants are prefixed with "#$".
   * Embedded strings have escape characters inserted in front of any double-quote
   *   or backslash characters which they contain.
   *
   * @return a cyclified <tt>String</tt>.
   */
  String cyclifyWithEscapeChars();
  
  /**
   * Ensure that this CycNart is an actual reified term in the Cyc server accessible from <code>access</code>.
   * @param access
   * @return the reified Nart
   * @throws CycConnectionException
   */
  Nart ensureReified(CycAccess access) throws CycConnectionException;

  /**
   * Returns <tt>true</tt> some object equals this <tt>CycNart</tt>
   *
   * @param object the <tt>Object</tt> for equality comparison
   * @return equals <tt>boolean</tt> value indicating equality or non-equality.
   */
  boolean equals(Object object);

  /**
   * Returns <tt>true</tt> some object equals this <tt>CycNart</tt>
   *
   * @param object the <tt>Object</tt> for equality comparison
   * @return equals <tt>boolean</tt> value indicating equality or non-equality.
   */
  boolean equalsAtEL(Object object);

  /**
   * Returns the arguments of the <tt>CycNart</tt>.
   * Modifications to this list will be reflected back to the original CycNart.
   *
   * @return the arguments of the <tt>CycNart</tt>
   */
  List getArguments();

//  CycNautI getFormula();

  /**
   * Returns the functor of the <tt>CycNart</tt>.
   *
   * @return the functor of the <tt>CycNart</tt>
   */
//  Fort getFunctor();

  /**
   * Returns a list of all constants referred to by this CycObject.
   * For example, a CycConstant will return a List with itself as the
   * value, a nart will return a list of its functor and all the constants referred
   * to by its arguments, a CycList will do a deep search for all constants,
   * a symbol or variable will return the empty list.
   * @return a list of all constants referred to by this CycObject
   **/
  List getReferencedConstants();

  /**
   * Returns true if the functor and arguments are instantiated.
   *
   * @return true if the functor and arguments are instantiated
   */
  boolean hasFunctorAndArgs();
  
  /**
   * Sets the specified argument of the <tt>CycNart</tt> to argument.
   *
   * @param argument
   */
  void setArgument(final int argNum, Object argument);

  /**
   * Sets the arguments of the <tt>CycNart</tt>.
   *
   * @param arguments the arguments of the <tt>CycNart</tt>
   */
  void setArguments(CycList arguments);

  /**
   * Sets the functor of the <tt>CycNart</tt>.
   *
   * @param functor the <tt>CycFort</tt> functor object of the <tt>CycNart</tt>
   */
  void setFunctor(Fort functor);

  /**
   * Returns this object in a form suitable for use as an <tt>String</tt> api expression value.
   *
   * @return this object in a form suitable for use as an <tt>String</tt> api expression value
   */
  String stringApiValue();

  /**
   * Returns a list representation of the Cyc NART.
   *
   * @return a <tt>CycList</tt> representation of the Cyc NART.
   */
//  CycList toCycList();

  /**
   * Returns a list representation of the Cyc NART and expands any embedded NARTs as well.
   *
   * @return a <tt>CycList</tt> representation of the Cyc NART.
   */
//  CycList toDeepCycList();
  
}
