package com.cyc.baseclient.cycobject;

/*
 * #%L
 * File: CycNumberImpl.java
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

//// External Imports
import com.cyc.base.cycobject.CycNumber;
import com.cyc.base.cycobject.DenotationalTerm;
import java.io.IOException;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

//// Internal Imports
import com.cyc.baseclient.xml.XMLWriter;

/**
 * Provides the behavior and attributes of a Base Client Number.
 *
 * @version $Id: CycNumberImpl.java 155703 2015-01-05 23:15:30Z nwinant $
 * @author Dave Schneider
 */
public class CycNumberImpl implements DenotationalTerm, CycNumber {

  /**
   * The Number that this term is wrapping.
   */
  private final Number number;
  /**
   * The Number as a Double. (initialized lazily)
   */
  private Double doubleValue = null;

  /**
   * Zero-arg constructor. Should generally not be used.
   */
  private CycNumberImpl() {
    number = null;
  }

  /**
   * Constructs a new <tt>CycNumber</tt> object from a numeric string.
   *
   * @param numberString the constant name
   */
  public CycNumberImpl(final String numberString) {
    if (numberString == null) {
      throw new IllegalArgumentException("number string must not be null.");
    } else {
      try {
        final NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);
        number = (Number) nf.parseObject(numberString);
      } catch (ParseException ex) {
        Logger.getLogger(CycNumberImpl.class.getName()).log(Level.SEVERE, null, ex);
        throw new IllegalArgumentException(numberString + " must be a number");
      }
    }
  }

  /**
   * Constructs a new <tt>CycNumber</tt> object from a java Number.
   *
   * @param num  the java number
   */
  public CycNumberImpl(final Number num) {
    if (num == null) {
      throw new IllegalArgumentException("Number must not be null.");
    } else {
      number = num;
    }
  }

  /**
   * Gets the value of this CycNumberImpl as a java Number.
   *
   * @return the number as a java Number
   */
  public Number getNumber() {
    return number;
  }

  /**
   * Convenience method to compare two CycNumbers numerically.
   * @param other
   * @return true iff this CycNumberImpl is numerically larger than other.
   */
  public boolean isGreaterThan(CycNumber other) {
    return this.compareTo(other) > 0;
  }

  /**
   * Returns the XML representation of this object.
   *
   * @return the XML representation of this object
   */
  @Deprecated
  public String toXMLString() {
    return number.toString();
  }

  /**
   * Prints the XML representation of the CycConstant to an <code>XMLWriter</code>
   *
   * @param xmlWriter an <tt>XMLWriter</tt>
   * @param indent an int that specifies by how many spaces to indent
   * @param relative a boolean; if true indentation is relative, otherwise absolute
   * @throws IOException
   */
  @Deprecated
  public void toXML(XMLWriter xmlWriter, int indent, boolean relative)
          throws IOException {
    xmlWriter.print(this.toXMLString());
  }

  /**
   * Provides the hash code appropriate for the <tt>CycNumberImpl</tt>.
   *
   * @return the hash code for the <tt>CycNumberImpl</tt>
   */
  @Override
  public int hashCode() {
    return getNumber().hashCode();
  }

  /**
   * Returns <tt>true</tt> some object equals this <tt>CycNumberImpl</tt>.
   *
   * @param object the <tt>Object</tt> for equality comparison
   * @return equals <tt>boolean</tt> value indicating equality or non-equality.
   */
  @Override
  public boolean equals(Object object) {
    if (!(object instanceof CycNumberImpl)) {
      return false;
    } else {
      return this.getNumber().equals(((CycNumberImpl) object).getNumber());
    }
  }

  /**
   * Returns <tt>true</tt> some object equals this <tt>CycNumberImpl</tt>.
   * In constrast to equals(), this will also return true if a java Number
 is equals() to the number encapsulated by this CycNumberImpl.
   *
   * @param object the <tt>Object</tt> for equality comparison
   * @return equals <tt>boolean</tt> value indicating equality or non-equality.
   */
  @Override
  public boolean equalsAtEL(Object object) {
    return (this.getNumber().equals(object) || this.equals(object));
  }

  /**
   * Returns a String representation of the <tt>CycNumberImpl</tt>.
   */
  @Override
  public String toString() {
    if (number != null) {
      return number.toString();
    }
    return "[CycNumber: " + number + "]";
  }

  /**
   * Returns this object in a form suitable for use as an <tt>CycList</tt> api expression value.
   *
   * @return this object in a form suitable for use as an <tt>CycList</tt> api expression value
   */
  @Override
  public Object cycListApiValue() {
    return this.getNumber();
  }

  public Object cycExpressionApiValue() {
    return this.getNumber();
  }

  @Override
  public String cyclifyWithEscapeChars() {
    return cyclify();
  }

  @Override
  public String stringApiValue() {
    return getNumber().toString();
  }

  @Override
  public int compareTo(Object o) {
    if (o instanceof CycNumberImpl) {
      final CycNumberImpl other = (CycNumberImpl) o;
      final Class thisNumberClass = this.number.getClass();
      if (thisNumberClass.equals(other.number.getClass())
              && Comparable.class.isAssignableFrom(thisNumberClass)) {
        return ((Comparable)this.number).compareTo((Comparable)other.number);
      } else {
        return this.doubleValue().compareTo(other.doubleValue());
      }
    } else {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  }

  /**
   * Gets the number as a Double
   *
   * @return the number as a Double
   */
  private synchronized Double doubleValue() {
    if (doubleValue == null) {
      doubleValue = number.doubleValue();
    }
    return doubleValue;
  }

  @Override
  public String cyclify() {
    return stringApiValue();
  }

  @Override
  public List getReferencedConstants() {
    return Collections.emptyList();
  }
}
