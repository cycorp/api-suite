package com.cyc.baseclient.cycobject;

/*
 * #%L
 * File: CycVariableImpl.java
 * Project: Base Client
 * %%
 * Copyright (C) 2013 - 2016 Cycorp, Inc.
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

import com.cyc.baseclient.xml.XmlWriter;
import com.cyc.baseclient.xml.TextUtil;
import com.cyc.baseclient.xml.XmlStringWriter;
import com.cyc.base.cycobject.CycVariable;
import java.io.*;
import com.cyc.baseclient.connection.SublApiHelper;

/**
 * Provides the behavior and attributes of a Base Client variable, typically used
 * in rule and query expressions.
 *
 * @version $0.1$
 * @author Stephen L. Reed
 */
public class CycVariableImpl extends DefaultCycObject implements CycVariable {

  public static final String META_VARIABLE_PREFIX = ":";
  public static final String NORMAL_VARIABLE_PREFIX = "?";
  public static final String DONT_CARE_VARIABLE_PREFIX = "??";

  /**
   * The name of the XML tag for this object.
   */
  public static final String cycVariableXMLTag = "variable";
  /** the HL variable id */
  public Integer hlVariableId = null;
  /**
   * The variable represented as a <tt>String</tt>.
   */
  private final String name;
  
  @Override
  public String getName() {
	  return this.name;
  }
  /**
   * Whether this variable is a meta variable.
   */
  private boolean isMetaVariable = false;
  
  private boolean isDontCareVariable = false;

  /**
   * Constructs a new <tt>CycVariable</tt> object.
   *
   * @param name the <tt>String</tt> name of the <tt>CycVariable</tt>.
   * @param hlVariableId the HL variable id
   */
  public CycVariableImpl(final String name, final Integer hlVariableId) {
    this(name);
    if (hlVariableId == null) {
      throw new IllegalArgumentException("id must not be null");
    }

    this.hlVariableId = hlVariableId;
  }

  /**
   * Constructs a new <tt>CycVariable</tt> object.
   *
   * @param name the <tt>String</tt> name of the <tt>CycVariable</tt>.
   */
  public CycVariableImpl(String nameStr) {
    if (nameStr == null) {
      throw new IllegalArgumentException("name must not be null");
    }
    final String prefix = getVariablePrefix(nameStr);
    this.isDontCareVariable = DONT_CARE_VARIABLE_PREFIX.equals(prefix);
    this.isMetaVariable = META_VARIABLE_PREFIX.equals(prefix);
    String myName = getVariableNameString(nameStr);
    /*
    String myName = nameStr;
    if (myName.startsWith(META_VARIABLE_PREFIX)) {
      this.isMetaVariable = true;
      myName = myName.substring(1);
    } else if (myName.startsWith(NORMAL_VARIABLE_PREFIX)) {
      myName = myName.substring(1);
    }
    */
    myName = CycSymbolImpl.canonicalizeName(myName);
    myName = myName.replace(' ', '-');
    if (!isValidVariableName(myName, false)) {
      throw new IllegalArgumentException("Invalid variable name: " + myName);
    }
    this.name = myName;
  }

  /** Return the CycVariableImpl corresponding to obj if one can be identified.
   *
   * @param obj
   * @return a CycVariableImpl or obj itself.
   */
  static Object convertIfPromising(Object obj) {
    if (obj instanceof CycSymbolImpl) {
      final String symbolName = ((CycSymbolImpl) obj).getSymbolName();
      if (symbolName.startsWith(CycVariableImpl.META_VARIABLE_PREFIX)
              || symbolName.startsWith(CycVariableImpl.NORMAL_VARIABLE_PREFIX)) {
        return new CycVariableImpl(symbolName);
      }
    }
    return obj;
  }

  /**
   * Returns whether this is a meta variable.
   *
   * @return whether this is a meta variable
   */
  @Override
  public boolean isMetaVariable() {
    return isMetaVariable;
  }

  /**
   * Returns whether this is an HL variable.
   *
   * @return whether this is an HL variable
   */
  @Override
  public boolean isHLVariable() {
    return hlVariableId != null;
  }

  /**
   * Returns the string representation of the <tt>CycVariableImpl</tt>
   *
   * @return the representation of the <tt>CycVariableImpl</tt> as a <tt>String</tt>
   */
  @Override
  public String toString() {
    return cyclify();
  }

  @Override
  public String toCanonicalString() {
    return CycSymbolImpl.canonicalizeName(toString());
  }

  @Override
  public boolean isDontCareVariable() {
    //return name.startsWith("?");
    return isDontCareVariable;
  }

  /**
   * Returns the Base Client representation of the <tt>CycVariableImpl</tt>
   *
   * @return the Base Client representation of the <tt>CycVariableImpl</tt> as a
   * <tt>String</tt> prefixed by "?"
   */
  @Override
  public String cyclify() {
    if (isMetaVariable) {
      return META_VARIABLE_PREFIX + name;
    } else {
      return NORMAL_VARIABLE_PREFIX + name;
    }
  }

  /**
   * Returns this object in a form suitable for use as an <tt>String</tt> api expression value.
   *
   * @return this object in a form suitable for use as an <tt>String</tt> api expression value
   */
  @Override
  public String stringApiValue() {
    if (isHLVariable()) {
      return SublApiHelper.makeSubLStmt("GET-VARIABLE", this.hlVariableId);
    } else {
      return "'" + cyclifyWithEscapeChars();
    }
  }

  /**
   * Returns this object in a form suitable for use as an <tt>CycList</tt> api expression value.
   *
   * @return this object in a form suitable for use as an <tt>CycList</tt> api expression value
   */
  @Override
  public Object cycListApiValue() {
    return this;
  }

  /**
   * Returns <tt>true</tt> some object equals this <tt>CycVariableImpl</tt>
   *
   * @note Comparing a CycVariable to a CycSymbol gives the right behavior
   * iff the names are equal.
   * @param object the <tt>Object</tt> for equality comparison
   * @return equals <tt>boolean</tt> value indicating equality or non-equality.
   */
  @Override
  public boolean equals(Object object) {
    if (object == null) {
      return false;
    }
    if (object instanceof CycVariableImpl) {
      CycVariableImpl var = (CycVariableImpl) object;
      return (isMetaVariable() == var.isMetaVariable())
              && isDontCareVariable() == var.isDontCareVariable()
              && var.getName().equals(this.getName());
    } else if (object instanceof CycSymbolImpl) {
      final CycSymbolImpl other = (CycSymbolImpl) object;
      if (name.equals(other.getSymbolName())) {
        if (isMetaVariable()) {
          return other.isKeyword();
        } else {
          return !other.isKeyword();
        }
      }
    }
    return false;
  }

  /**
   * Provides the hash code appropriate for this object.
   *
   * @return the hash code appropriate for this object
   */
  @Override
  public int hashCode() {
    return name.hashCode();
  }

  /**
   * Compares this object with the specified object for order.
   * Returns a negative integer, zero, or a positive integer as this
   * object is less than, equal to, or greater than the specified object.
   *
   * @param object the reference object with which to compare.
   * @return a negative integer, zero, or a positive integer as this
   * object is less than, equal to, or greater than the specified object
   */
  @Override
  public int compareTo(Object object) {
    if (!(object instanceof CycVariableImpl)) {
      throw new ClassCastException("Must be a CycVariable object");
    }
    return this.name.compareTo(((CycVariableImpl) object).name);
  }

  /**
   * Returns the XML representation of this object.
   *
   * @return the XML representation of this object
   */
  @Deprecated
  public String toXMLString() throws IOException {
    XmlStringWriter xmlStringWriter = new XmlStringWriter();
    toXML(xmlStringWriter, 0, false);
    return xmlStringWriter.toString();
  }

  /**
   * Prints the XML representation of the CycVariableImpl to an <code>XMLWriter</code>
   *
   * @param xmlWriter an <tt>XMLWriter</tt>
   * @param indent an int that specifies by how many spaces to indent
   * @param relative a boolean; if true indentation is relative, otherwise absolute
   */
  @Override
  @Deprecated
  public void toXML(XmlWriter xmlWriter, int indent, boolean relative)
          throws IOException {
    xmlWriter.printXMLStartTag(cycVariableXMLTag, indent, relative, false);
    xmlWriter.print(TextUtil.doEntityReference(name));
    xmlWriter.printXMLEndTag(cycVariableXMLTag);
  }
  
  static public String getVariablePrefix(String nameStr) {
    if (nameStr.startsWith(DONT_CARE_VARIABLE_PREFIX)) {
      return DONT_CARE_VARIABLE_PREFIX;
    } else if (nameStr.startsWith(NORMAL_VARIABLE_PREFIX)) {
      return NORMAL_VARIABLE_PREFIX;
    } else if (nameStr.startsWith(META_VARIABLE_PREFIX)) {
      return META_VARIABLE_PREFIX;
    }
    return null;
  }
  
  static public String getVariableNameString(String nameStr) {
    final String name;
    if (nameStr.startsWith(DONT_CARE_VARIABLE_PREFIX)) {
      name = nameStr.substring(2);
    } else if (nameStr.startsWith(NORMAL_VARIABLE_PREFIX) 
            || nameStr.startsWith(META_VARIABLE_PREFIX)) {
      name = nameStr.substring(1);
    } else {
      name = nameStr;
    }
    return name;
    //return CycSymbolImpl.canonicalizeName(name).replace(' ', '-');
  }
  
  /**
   * Is <code>name</code> a valid Cyc variable name?
   * @param nameStr a potential CycVariable name
   * @param requirePrefix
   * @return whether name String is in fact a valid CycVariable name
   */
  static public boolean isValidVariableName(String nameStr, boolean requirePrefix) {
    if (requirePrefix && getVariablePrefix(nameStr) == null) {
      return false;
    }
    final String name = getVariableNameString(nameStr);
    if (name.length() < 1) {
      return false;
    }
    for (int i = 0; i < name.length(); i++) {
      final char thisChar = name.charAt(i);
      if ((i == 0) && !Character.isLetter(thisChar)) {
        return false;
      }
      if (Character.isUpperCase(thisChar)) {
        continue;
      }
      if (Character.isDigit(thisChar)) {
        continue;
      }
      if (thisChar == '-' && i > 0 && name.charAt(i - 1) != '-') {
        continue;
      }
      return false;
    }
    return true;
  }
  
  static public boolean isValidVariableName(String nameStr) {
    return isValidVariableName(nameStr, true);
  }

}
