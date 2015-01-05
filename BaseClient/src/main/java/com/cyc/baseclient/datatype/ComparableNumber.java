package com.cyc.baseclient.datatype;

/*
 * #%L
 * File: ComparableNumber.java
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
 * <P>ComparableNumber is a wrapper class for Numbers, and specifically exists
 * to allow comparisons of different kinds of numbers (floats, ints, etc.)
 *
 * @author nwinant, Jun 7, 2010, 4:26:56 PM
 * @version $Id: ComparableNumber.java 155483 2014-12-10 21:56:51Z nwinant $
 */
public class ComparableNumber extends Number implements Comparable<ComparableNumber> {

  //// Constructors

  /** Creates a new instance of ComparableNumber. */
  public ComparableNumber(final Number number) {
    this.number = number;
  }

  //// Public Area
  
  @Override
  public int intValue() {
    return number.intValue();
  }

  @Override
  public long longValue() {
    return number.longValue();
  }

  @Override
  public float floatValue() {
    return number.floatValue();
  }

  @Override
  public double doubleValue() {
    return number.doubleValue();
  }

  @Override
  public boolean equals(final Object obj) {
    final ComparableNumber num;
    try {
      num = (ComparableNumber) obj;
    } catch (ClassCastException c) {
      return false;
    }
    return number.equals(num.getNumber());
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 97 * hash + (this.number != null ? this.number.hashCode() : 0);
    return hash;
  }

  public int compareTo(final ComparableNumber o) {
    if (number.getClass().equals(o.getNumber().getClass())) {
      return ((Comparable) number).compareTo((Comparable) o.getNumber());
    } else {
      return new Double(number.doubleValue()).compareTo(o.getNumber().doubleValue());
    }
  }

  /**
   * @return the number
   */
  public Number getNumber() {
    return number;
  }
  

  //// Protected Area

  //// Private Area

  //// Internal Rep

  private final Number number;


  //// Main

}
