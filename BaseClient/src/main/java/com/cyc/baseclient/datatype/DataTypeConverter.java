package com.cyc.baseclient.datatype;

/*
 * #%L
 * File: DataTypeConverter.java
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
import java.math.BigDecimal;
import java.util.Map;
import com.cyc.baseclient.cycobject.CycArrayList;
import com.cyc.baseclient.cycobject.NautImpl;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.Naut;
import com.cyc.baseclient.util.ParseException;

/** 
 * <P>DataTypeConverter is an abstract base class for building classes which
 * convert Java datatypes to their corresponding CycL representations and vice 
 * versa.
 *
 * @author nwinant, May 21, 2010, 4:01:30 PM
 * @version $Id: DataTypeConverter.java 155483 2014-12-10 21:56:51Z nwinant $
 */
abstract public class DataTypeConverter<E> {
  
  protected E handleParseException(ParseException ex, final boolean shouldReportFailure) {
    if (shouldReportFailure) {
      ex.printStackTrace();
    }
    return null;
  }

  //// Constructors
  //// Public Area
  /** Try to parse <code>cycList</code> into a Java <code>E</code>.
   *
   * If the parse fails, prints a stack trace iff <code>shouldReportFailure</code>
   * is non-null, and returns null.
   * @deprecated Use CycObject version.
   */
  protected E parse(final CycArrayList cycList, final boolean shouldReportFailure) {
    final Object naut = NautImpl.convertIfPromising(cycList);
    if (naut instanceof Naut) {
      return parse((Naut) naut, shouldReportFailure);
    } else if (shouldReportFailure) {
      new IllegalArgumentException(cycList + " cannot be interpreted as a NAUT").printStackTrace();
    }
    return null;
  }

  /** Try to parse <code>cycList</code> into a Java <code>E</code>.
   *
   * If the parse fails, prints a stack trace iff <code>shouldReportFailure</code>
   * is non-null, and returns null.
   */
  protected E parse(final CycObject cycObject, final boolean shouldReportFailure) {
    try {
      return fromCycTerm(cycObject);
    } catch (ParseException ex) {
      return handleParseException(ex, shouldReportFailure);
    }
  }

  /** Try to parse <code>cycList</code> into a Java <code>E</code>.
   *
   * Prints stack trace and returns null if the parse fails.
   * @deprecated Use CycObject version.
   */
  protected E parse(final CycArrayList cycList) {
    return parse(cycList, true);
  }

  /** Try to parse <code>naut</code> into a Java <code>E</code>.
   *
   * Prints stack trace and returns null if the parse fails.
   */
  protected E parse(final CycObject cycObject) {
    return parse(cycObject, true);
  }

  protected boolean isOfType(final Object object) {
    if (object instanceof CycArrayList) {
      return parse((CycArrayList) object, false) != null;
    } else if (object instanceof CycObject) {
      return parse((CycObject) object, false) != null;
    } else {
      return false;
    }
  }

  /**
   * Convert Java object of type <code>E</code> to <code>naut</code>.
   *
   * Throws a ParseException if the parse fails.
   */
  protected abstract CycObject toCycTerm(final E obj) throws ParseException;

  //// Protected Area
  /** Try to parse <code>naut</code> into a Java <code>E</code>. This method
   * should typically be accessed via the <code>parse()</code> methods.
   *
   * Throws a ParseException if the parse fails.
   */
  protected abstract E fromCycTerm(final CycObject cycObject) throws ParseException;

  protected static Integer parseInteger(final Object obj, final String type) throws ParseException {
    final Integer result;
    try {
      result = Integer.valueOf(obj.toString());
    } catch (NumberFormatException nfe) {
      throw new ParseException(obj + " is not a valid " + type + ".");
    }
    return result;
  }

  protected static BigDecimal parseBigDecimal(final Object obj, final String type) throws ParseException {
    final BigDecimal result;
    try {
      result = new BigDecimal(obj.toString());
    } catch (NumberFormatException nfe) {
      throw new ParseException(obj + " is not a valid " + type + ".");
    }
    return result;
  }

  protected static Float parseFloat(final Object obj, final String type) throws ParseException {
    final Float floatValue;
    try {
      floatValue = Float.valueOf(obj.toString());
    } catch (NumberFormatException nfe) {
      throw new ParseException(obj + " is not a valid " + type + ".");
    }
    return floatValue;
  }

  static protected void throwParseException(final Object obj, final String detail) throws ParseException {
    throw new ParseException("Can't parse " + obj + ": " + detail);
  }

  static protected void throwParseException(final Object obj) throws ParseException {
    throw new ParseException("Can't parse " + obj);
  }

  protected static <K, V> K lookupKeyByValue(Map<K, V> map, V value) {
    for (final Map.Entry<K, V> entry : map.entrySet()) {
      if (value.equals(entry.getValue())) {
        return entry.getKey();
      }
    }
    return null;
  }
  //// Private Area
  //// Internal Rep
  //// Main
}
