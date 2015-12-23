package com.cyc.baseclient.cycobject;

/*
 * #%L
 * File: NartImpl.java
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
import com.cyc.base.exception.BaseClientRuntimeException;
import com.cyc.base.CycAccess;
import com.cyc.base.exception.CycConnectionException;
import com.cyc.base.cycobject.Fort;
import com.cyc.base.cycobject.CycList;
import com.cyc.base.cycobject.Nart;
import com.cyc.base.cycobject.Naut;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.ListIterator;
import com.cyc.base.exception.CycApiException;
import com.cyc.base.cycobject.DenotationalTerm;
import com.cyc.baseclient.CycObjectFactory;
import com.cyc.baseclient.xml.XmlStringWriter;
import com.cyc.baseclient.xml.XmlWriter;

/**
 * This class implements the behavior and attributes of a
 * a Base Client NART (Non Atomic Reified Term).
 *
 * @version $Id: NartImpl.java 162904 2015-12-02 18:35:34Z nwinant $
 * @author Stefano Bertolo
 * @author Stephen L. Reed
 */
public class NartImpl extends FortImpl implements Nart {

  static final long serialVersionUID = -1344072553770319121L;
  /**
   * XML serialization tags.
   */
  public static final String natXMLtag = "nat";
  public static final String functorXMLtag = "functor";
  public static final String argXMLtag = "arg";
  /**
   * XML serialization indentation.
   */
  public static int indentLength = 2;
  private Naut formula;

  /**
   * Constructs a new incomplete <tt>CycNart</tt> object.
   */
  public NartImpl() {
  }

  /**
   * Constructs a new unary <tt>CycNart</tt> object from the NautImpl formula
   *
   */
  public NartImpl(final Naut formula) {
    this.formula = formula;
  }

  /**
   * Constructs a new unary <tt>CycNart</tt> object from the functor and
   * argument.
   *
   * @param functor a <tt>Fort</tt> which is the functor of this
   * <tt>CycNart</tt> object.
   * @param arguments an array of <tt>Objects</tt> most typically <tt>CycConstants</tt>
   * which are the arguments of this <tt>CycNart</tt> object.
   */
  public NartImpl(Fort functor, Object... arguments) {
    this.formula = new NautImpl(functor, arguments);
  }

  /**
   * Constructs a new <tt>CycNart</tt> object from the <tt>CycArrayList</tt> object.
   *
   * @param cycList a list representation of the <tt>CycNart</tt>
   */
  public NartImpl(CycList cycList) {
    if (cycList.size() == 0) {
      throw new BaseClientRuntimeException("Cannot make a CycNart from an empty CycList");
    }
    if (!(cycList.first() instanceof Fort)) {
      throw new BaseClientRuntimeException("CycNart functor must be a CycFort " + cycList.cyclify());
    }
    this.formula = new NautImpl(cycList);
  }
  
  /**
   * Ensure that this NartImpl is an actual reified term in the Cyc server accessible from <code>access</code>.
   * @param access
   * @return the Nart that as reified
   * @throws com.cyc.base.exception.CycConnectionException
   */
  public Nart ensureReified(CycAccess access) throws CycConnectionException {
    Object result;
    try {
      String command = "(canonicalize-term-assert " + this.stringApiValue() + ")";
      result = access.converse().converseObject(command);
    } catch (Exception e) {
      throw new CycApiException("Exception while ensuring that " + this + " is a NART.", e);
    }
    if (!(result instanceof Nart)) {
      throw new CycApiException("Unable to convert " + this + " into a Cyc NART.");

    }
    return this;
  }
  
  /** *  Constructs a the singleton invalid <tt>NartImpl</tt> object. 
   * This should only be called from CycObjectFactory.
   *
   * @return the invalid nart
   */
  public static Nart makeInvalidNart() {
    final NartImpl cycNart = new NartImpl();
    cycNart.isInvalid = true;
    return cycNart;
  }

  /**
   * Returns the given object if it is a <tt>Nart</tt>, otherwise the object is expected to be
   * a <tt>CycArrayList</tt> and a <tt>Nart</tt> object is returned using the given
 CycArrayList representation.
   *
   * @param object the object to be coerced into a Nart
   * @return the given object if it is a <tt>Nart</tt>, otherwise the object is expected to be
   * a <tt>CycArrayList</tt> and a <tt>Nart</tt> object is returned using the given
 CycArrayList representation
   */
  public static Nart coerceToCycNart(Object object) {
    if (object instanceof Nart) {
      return (Nart) object;
    }
    if (!(object instanceof CycArrayList)) {
      throw new BaseClientRuntimeException("Cannot coerce to CycNart " + object);
    }
    return new NartImpl((CycArrayList) object);
  }

  /**
   * Returns the functor of the <tt>NartImpl</tt>.
   *
   * @return the functor of the <tt>NartImpl</tt>
   */
  @Override
  public DenotationalTerm getFunctor() {
    return (DenotationalTerm) formula.getOperator();
  }

  @Override
  public Naut getFormula() {
    return formula;
  }

  @Override
  public int getArity() {
    return getFormula().getArity();
  }

  /**
   * Sets the functor of the <tt>NartImpl</tt>.
   *
   * @param functor the <tt>Fort</tt> functor object of the <tt>NartImpl</tt>
   */
  public void setFunctor(Fort functor) {
    formula.getArgs().set(0, functor);
  }

  /**
   * Returns the arguments of the <tt>NartImpl</tt>.
   * Modifications to this list will be reflected back to the original NartImpl.
   *
   * @return the arguments of the <tt>NartImpl</tt>
   */
  public List getArguments() {
    return (List) formula.getArgs().rest();
  }

  public Object getArgument(final int argnum) {
    return formula.getArg(argnum);
  }

  /**
   * Sets the arguments of the <tt>NartImpl</tt>.
   *
   * @param arguments the arguments of the <tt>NartImpl</tt>
   */
  public void setArguments(CycList arguments) {
    formula.setArgs(arguments);
  }

  /**
   * Sets the specified argument of the <tt>NartImpl</tt> to argument.
   *
   * @param argument
   */
  public void setArgument(final int argNum, Object argument) {
    formula.getArgs().set(argNum, argument);
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
   * Prints the XML representation of the <ttt>NartImpl</tt> to an <tt>XMLWriter</tt>
   * It is supposed to look like this:<p>
   * <pre>
   * <nat>
   *  <functor>
   *   <constant>
   *    <guid>bd58a976-9c29-11b1-9dad-c379636f7270</guid>
   *    <name>FruitFn</name>
   *   </constant>
   *  </functor>
   *  <arg>
   *   <constant>
   *    <guid>bd58c19d-9c29-11b1-9dad-c379636f7270</guid>
   *    <name>AppleTree</name>
   *   </constant>
   *  </arg>
   * </nat>
   * </pre>
   *
   * The parameter [int indent] specifies by how many spaces the XML
   * output should be indented.<p>
   *
   * The parameter [boolean relative] specifies whether the
   * indentation should be absolute -- indentation with respect to
   * the beginning of a new line, relative = false -- or relative
   * to the indentation currently specified in the indent_string field
   * of the xml_writer object, relative = true.
   *
   */
  @Deprecated
  @Override
  public void toXML(XmlWriter xmlWriter, int indent, boolean relative)
          throws IOException {
    xmlWriter.printXMLStartTag(natXMLtag, indent, relative, true);
    xmlWriter.printXMLStartTag(functorXMLtag, indentLength, true, true);
    //this.getFunctor().toXML(xmlWriter, indentLength, true);
    this.convertCycObjectToXML(this.getFunctor(), xmlWriter, indentLength, true);
    xmlWriter.printXMLEndTag(functorXMLtag, -indentLength, true);
    ListIterator iterator = this.getArguments().listIterator();
    Object arg;
    while (iterator.hasNext()) {
      xmlWriter.printXMLStartTag(argXMLtag, 0, true, true);
      arg = iterator.next();
      // Use a shared method with CycArrayList for arbitrary elements.
      CycArrayList.toXML(arg, xmlWriter, indentLength, true);
      xmlWriter.printXMLEndTag(argXMLtag, 0, true);
    }
    xmlWriter.printXMLEndTag(natXMLtag, -indentLength, true);
  }

  /**
   * Returns a list representation of the Base Client NART.
   *
   * @return a <tt>CycArrayList</tt> representation of the Base Client NART.
   */
  @Override
  public CycList toCycList() {
    return getFormula().toCycList();
  }

  /**
   * Returns a list representation of the Base Client NART and expands any embedded NARTs as well.
   *
   * @return a <tt>CycArrayList</tt> representation of the Base Client NART.
   */
  @Override
  public CycList toDeepCycList() {
    return getFormula().toDeepCycList();
  }

  /**
   * Returns a string representation of the Base Client NART.
   *
   * @return a <tt>String</tt> representation of the Base Client NART.
   */
  public String toString() {
    if (isInvalid) {
      return "INVALID-NART";
    }
    return getFormula().toString();
  }

  /**
   * Returns a cyclified string representation of the Base Client NART.
   * Embedded constants are prefixed with "#$".
   *
   * @return a cyclified <tt>String</tt>.
   */
  public String cyclify() {
    if (isInvalid) {
      return "INVALID-NART";
    }
    return getFormula().cyclify();
  }

  /**
   * Returns a cyclified string representation of the Base Client NART.
   * Embedded constants are prefixed with "#$".
   * Embedded strings have escape characters inserted in front of any double-quote
   *   or backslash characters which they contain.
   *
   * @return a cyclified <tt>String</tt>.
   */
  public String cyclifyWithEscapeChars() {
    if (isInvalid) {
      return "INVALID-NART";
    }
    return getFormula().cyclifyWithEscapeChars();
  }

  /**
   * Returns this object in a form suitable for use as an <tt>String</tt> api expression value.
   *
   * @return this object in a form suitable for use as an <tt>String</tt> api expression value
   */
  public String stringApiValue() {
    return "(canonicalize-term '" + cyclifyWithEscapeChars() + ")";
  }

  /**
   * Returns this object in a form suitable for use as an <tt>CycArrayList</tt> api expression value.
   *
   * @return this object in a form suitable for use as an <tt>CycArrayList</tt> api expression value
   */
  public Object cycListApiValue() {
    CycArrayList apiValue = new CycArrayList();
    apiValue.add(CycObjectFactory.makeCycSymbol("canonicalize-term"));
    apiValue.addQuoted(this.toCycList());
    return apiValue;
  }

  /**
   * Returns a string representation of the <ttt>NartImpl</tt> with the guid in place
   * of the constant name.
   *
   * @return a <tt>String</tt> representation of the <ttt>NartImpl</tt> with <tt>GuidImpl</tt>
   * external forms in place of the <tt>CycConstantImpl</tt> names.
   */
  public String metaGuid() {
    final DenotationalTerm functor = getFunctor();
    String functorGuid =
            (functor instanceof CycConstantImpl
            ? ((CycConstantImpl) functor).getGuid().toString() : ((NartImpl) functor).metaGuid());
    ListIterator iterator = getArguments().listIterator();
    StringBuffer result = new StringBuffer("(");
    result.append(functorGuid);
    Object arg;
    String argGuid;
    while (iterator.hasNext()) {
      arg = iterator.next();
      if (arg instanceof CycConstantImpl) {
        argGuid = ((CycConstantImpl) arg).getGuid().toString();
      } else if (arg instanceof NartImpl) {
        argGuid = ((NartImpl) arg).metaGuid();
      } else {
        argGuid = (String) arg;
      }
      result.append(" ");
      result.append(argGuid);
    }
    return result.append(")").toString();
  }

  /**
   * Returns a metaName representation of the <tt>NartImpl</tt>.
   *
   * @return a <tt>String</tt> metaName representation
   */
  public String metaName() {
    String functorName =
            (this.getFunctor() instanceof CycConstantImpl
            ? ((CycConstantImpl) this.getFunctor()).getName()
            : ((NartImpl) this.getFunctor()).metaName());
    ListIterator iterator = this.getArguments().listIterator();
    StringBuffer result = new StringBuffer("(");
    result.append(functorName);
    Object arg;
    String argName;
    while (iterator.hasNext()) {
      arg = iterator.next();
      if (arg instanceof CycConstantImpl) {
        argName = ((CycConstantImpl) arg).getName();
      } else if (arg instanceof NartImpl) {
        argName = ((NartImpl) arg).metaName();
      } else {
        argName = (String) arg;
      }
      result.append(" ");
      result.append(argName);
    }
    return result.append(")").toString();
  }

  /**
   * Return a hash value for this object.
   *
   * @return a hash value for this object
   */
  public int hashCode() {
    return formula.hashCode();
  }

  /**
   * Returns <tt>true</tt> some object equals this <tt>NartImpl</tt>
   *
   * @param object the <tt>Object</tt> for equality comparison
   * @return equals <tt>boolean</tt> value indicating equality or non-equality.
   */
  public boolean equals(Object object) {
    if (!(object instanceof NartImpl)) {
      return false;
    }
    NartImpl thatNart = (NartImpl) object;
    return formula.equals(thatNart.formula);
  }

  /**
   * Returns <tt>true</tt> some object equals this <tt>NartImpl</tt>
   *
   * @param object the <tt>Object</tt> for equality comparison
   * @return equals <tt>boolean</tt> value indicating equality or non-equality.
   */
  public boolean equalsAtEL(Object object) {
    return getFormula().equalsAtEL(object);
  }

  /**
   * Returns true if the functor and arguments are instantiated.
   *
   * @return true if the functor and arguments are instantiated
   */
  public boolean hasFunctorAndArgs() {
    return formula.getArity() > 0;
  }

  /**
   * Returns a list of all constants refered to by this CycObject.
   * For example, a CycConstantImpl will return a List with itself as the
 value, a nart will return a list of its functor and all the constants refered
 to by its arguments, a CycArrayList will do a deep search for all constants,
 a symbol or variable will return the empty list.
   * @return a list of all constants refered to by this CycObject
   **/
  public List getReferencedConstants() {
    return getFormula().getReferencedConstants();
  }

  //// serialization implementation
  private void writeObject(ObjectOutputStream stream) throws java.io.IOException {
    stream.defaultWriteObject();
    stream.writeObject(getFunctor());
    stream.writeObject(getArguments());
  }

  private void readObject(ObjectInputStream stream) throws java.io.IOException,
          java.lang.ClassNotFoundException {
    stream.defaultReadObject();
    final Fort functor = (Fort) stream.readObject();
    final CycArrayList arguments = (CycArrayList) stream.readObject();
    formula = new NautImpl(functor, arguments);
  }
}

