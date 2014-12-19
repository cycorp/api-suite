package com.cyc.baseclient.cycobject;

/*
 * #%L
 * File: CycConstantImpl.java
 * Project: Base Client
 * %%
 * Copyright (C) 2013 - 2014 Cycorp, Inc.
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

import com.cyc.base.BaseClientRuntimeException;
import com.cyc.base.cycobject.CycConstant;
import com.cyc.base.cycobject.Guid;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.cyc.baseclient.xml.TextUtil;
import com.cyc.baseclient.xml.XMLStringWriter;
import com.cyc.baseclient.xml.XMLWriter;

/**
 * Provides the behavior and attributes of a Base Client Constant.
 *
 * @version $Id: CycConstantImpl.java 155483 2014-12-10 21:56:51Z nwinant $
 * @author Stefano Bertolo
 * @author Stephen L. Reed
 */
public class CycConstantImpl extends FortImpl implements CycConstant {
  
  static final long serialVersionUID = -8728481441510819469L;
  
  /**
   * Field for storing the name of the XML tag for CycConstantImpl objects
   */
  public static final String constantXMLTag = "constant";
  
  /**
   * Field for storing the name of the XML tag for the name of CycConstantImpl objects
   */
  public static final String nameXMLTag = "name";
  
  /**
   * The default indentation for printing CycConstantImpl objects to XML
   */
  public static int indentLength = 2;
  
  /**
   * The GUID (Globally Unique IDentifier) of the <tt>CycConstantImpl</tt> object.
   * A string such as "c10af8ae-9c29-11b1-9dad-c379636f7270"
   */
  public GuidImpl guid;
  
  /**
   * The name of the <tt>CycConstantImpl</tt> object. A string such as "HandGrenade"
   */
  public String name;
  
  /**
   * When true, indicates that the constant has been deleted and the guid is not present.
   */
  protected boolean isFree = false;
  
  private CycConstantImpl() {
  }
  
  /**
   * Constructs a new <tt>CycConstant</tt> object from name, guid and id.
   *
   * @param name the constant name
   * @param guid the GUID that uniquely identifies the constant everywhere
   */
  public CycConstantImpl(final String name, final Guid guid) {
    if ((name == null) && (guid == null)) {
      throw new IllegalArgumentException("Name and GUID must not be null.");
    }
    if ((name != null) && (name.startsWith(HD))) {
      this.name = name.substring(2);
    } else {
      this.name = name;
    }
    this.guid = GuidImpl.fromGuid(guid);
  }
  
  /** *  Constructs the singleton free <tt>CycConstantImpl</tt> object. 
   * This should only be called from CycObjectFactory.
   *
   * @return the free cyc constant
   */
  public static CycConstant makeFreeConstant() {
    final CycConstantImpl cycConstant = new CycConstantImpl();
    cycConstant.isFree = true;
    return cycConstant;
  }
  
  /** *  Constructs a the singleton invalid <tt>CycConstantImpl</tt> object. 
   * This should only be called from CycObjectFactory.
   *
   * @return the invalid cyc constant
   */
  public static CycConstant makeInvalidConstant() {
    final CycConstantImpl cycConstant = new CycConstantImpl();
    cycConstant.isInvalid = true;
    return cycConstant;
  }
  
  /**
   * Gets the name
   *
   * @return the name
   */
  public String getName() {
    if (isFree) { return ("FREE"); }
    if (isInvalid) { return ("INVALID-CONSTANT"); }
    return name;
  }
  
  /**
   * Sets the name, which should only be called to update a renamed constant.
   *
   * @param name the name
   */
  public String setName(final String name) {
    if (name == null)
      throw new IllegalArgumentException("name must not be null");
    if (name.length() == 0)
      throw new IllegalArgumentException("name must not be an empty string");
    this.name = name;
    return name;
  }
  
  /**
   * Gets the guid
   *
   * @return the guid
   */
  @Override
  public GuidImpl getGuid() {
    return guid;
  }
  
  /**
   * Sets the guid
   */
  @Override
  public void setGuid(Guid newGuid) {
    if (newGuid == null) {
      throw new BaseClientRuntimeException("Guid must not be null.");
    }
    if (guid != null) {
      throw new BaseClientRuntimeException("Can only set GUID on a constant with an existing NULL guid.");
    }
    guid = GuidImpl.fromGuid(newGuid);
  }
  
  /**
   * Returns the XML representation of this object.
   *
   * @return the XML representation of this object
   */
  @Deprecated
  public String toXMLString() throws IOException {
    XMLStringWriter xmlStringWriter = new XMLStringWriter();
    toXML(xmlStringWriter, 0, false);
    return xmlStringWriter.toString();
  }
  
  /**
   * Prints the XML representation of the CycConstantImpl to an <code>XMLWriter</code>
   *
   * @param xmlWriter an <tt>XMLWriter</tt>
   * @param indent an int that specifies by how many spaces to indent
   * @param relative a boolean; if true indentation is relative, otherwise absolute
   */
  @Deprecated
  public void toXML(XMLWriter xmlWriter, int indent, boolean relative)
  throws IOException {
    xmlWriter.printXMLStartTag(constantXMLTag, indent, relative, true);
    int subIndent = indentLength;
    if (guid != null) {
      guid.toXML(xmlWriter, subIndent, true);
      subIndent = 0;
    }
    if (name != null) {
      xmlWriter.printXMLStartTag(nameXMLTag, subIndent, true, false);
      xmlWriter.print(TextUtil.doEntityReference(this.getName()));
      xmlWriter.printXMLEndTag(nameXMLTag);
      if (subIndent == indentLength)
        subIndent = 0;
    }
    xmlWriter.printXMLEndTag(constantXMLTag, -indentLength, true);
  }
  
  /**
   * Provides the hash code appropriate for the <tt>CycConstantImpl</tt>.
   *
   * @return the hash code for the <tt>CycConstantImpl</tt>
   */
  public int hashCode() {
    return getGuid().hashCode();
  }
  
  /**
   * Returns <tt>true</tt> some object equals this <tt>CycConstantImpl</tt>. The equality check uses only the guid.
   *
   * @param object the <tt>Object</tt> for equality comparison
   * @return equals <tt>boolean</tt> value indicating equality or non-equality.
   */
  public boolean equals(Object object) {
    if (! (object instanceof CycConstantImpl))
      return false;
    GuidImpl thisGuid = getGuid();
    GuidImpl thatGuid = ((CycConstantImpl) object).getGuid();
    return thisGuid.equals(thatGuid);
  }
  
  /**
   * Returns <tt>true</tt> some object equals this <tt>CycConstantImpl</tt>. The equality check uses only the guid.
   *
   * @param object the <tt>Object</tt> for equality comparison
   * @return equals <tt>boolean</tt> value indicating equality or non-equality.
   */
  public boolean equalsAtEL(Object object) {
    return equals(object);
  }
  
  /**
   * Sets this constant as deleted
   */
  protected void setFree() {
    isFree = true;
  }
  
  /**
   * Returns a String representation of the <tt>CycConstantImpl</tt>.
   */
  public String toString() {
    if (isFree)
      return "FREE";
    else if (isInvalid)
      return "INVALID-CONSTANT";
    else if (name != null)
      return name;
    else
      return "[CycConstant: " + guid.toString() + "]";
  }
  
  /**
   * Returns the name of the <tt>CycConstantImpl</tt> with "#$" prefixed.
   *
   * @return the name of the <tt>CycConstantImpl</tt> with "#$" prefixed.
   */
  public String cyclify() {
    if (isFree)
      return "FREE";
    else if (isInvalid)
      return "INVALID-CONSTANT";
    else
      return HD + getName();
  }
  
  /**
   * Returns this object in a form suitable for use as an <tt>CycList</tt> api expression value.
   *
   * @return this object in a form suitable for use as an <tt>CycList</tt> api expression value
   */
  public Object cycListApiValue() {
    return this;
  }
  
  /**
   * Makes a valid constant name from the candidate name by substituting
   * an underline character for the invalid characters.
   */
  public static String makeValidConstantName(String candidateName) {
    String answer = candidateName;
    for (int i = 0; i < answer.length(); i++) {
      char c = answer.charAt(i);
      if (Character.isLetterOrDigit(c) || c == '-' || c == '_' || c == '?')
        continue;
      StringBuffer answerBuf = new StringBuffer(answer);
      answerBuf.setCharAt(i, '_');
      answer = answerBuf.toString();
    }
    return answer;
  }
  
  /**
   * Returns a list of all constants refered to by this CycObject.
   * For example, a CycConstantImpl will return a List with itself as the
 value, a nart will return a list of its functor and all the constants refered
 to by its arguments, a CycList will do a deep search for all constants,
 a symbol or variable will return the empty list.
   * @return a list of all constants refered to by this CycObject
   **/
  public List getReferencedConstants() {
    List result = new ArrayList();
    result.add(this);
    return result;
  }
  
  //// serialization implementation
  private void writeObject(ObjectOutputStream stream) throws IOException {
    stream.defaultWriteObject();
    stream.writeUTF(guid.getGuidString());
    stream.writeUTF(name);
  }
  
  private void readObject(ObjectInputStream stream) throws IOException, java.lang.ClassNotFoundException {
    stream.defaultReadObject();
    guid = new GuidImpl(stream.readUTF());
    name = stream.readUTF();
  }
  
}
