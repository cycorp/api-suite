package com.cyc.baseclient.cycobject;

/*
 * #%L
 * File: CycAssertionImpl.java
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
import com.cyc.base.CycAccess;
import com.cyc.base.CycConnectionException;
import com.cyc.base.cycobject.CycAssertion;
import com.cyc.base.cycobject.FormulaSentence;
import com.cyc.base.cycobject.CycObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//// Internal Imports
import com.cyc.base.CycApiException;
import com.cyc.base.cycobject.CycList;
import com.cyc.baseclient.CycObjectFactory;
import static com.cyc.baseclient.api.SubLAPIHelper.makeSubLStmt;
import com.cyc.baseclient.xml.XMLStringWriter;
import com.cyc.baseclient.xml.XMLWriter;

/**
 * Provides the behavior and attributes of Cyc assertions.<p>
 * <p>
 * Assertions are communicated over the binary API using their Id number (an int).
 * The associated formula, microtheory, truth-value, direction, and remaining attributes are
 * is fetched later.
 *
 * @version $Id: CycAssertionImpl.java 155483 2014-12-10 21:56:51Z nwinant $
 * @author Stephen L. Reed
 * @author Dan Lipofsky
 */
public class CycAssertionImpl extends DefaultCycObject implements CycAssertion {

  /**
   * The name of the XML tag for this object.
   */
  public static final String cycAssertionXMLTag = "assertion";
  /**
   * The default indentation for printing objects to XML
   */
  public static int indentLength = 2;
  private static final CycSymbolImpl ASSERTION_EL_FORMULA = CycObjectFactory.makeCycSymbol("assertion-el-formula");
  /** the assertion in HL form */
  private final CycArrayList<FormulaSentence> negLits = new CycArrayList<FormulaSentence>();
  private final CycArrayList<FormulaSentence> posLits = new CycArrayList<FormulaSentence>();
  private final CycArrayList<CycList<FormulaSentence>> hlFormula =
          new CycArrayList<CycList<FormulaSentence>>(negLits, posLits);
  /** the assertion mt */
  private CycObject mt;
  /**
   * When true, indicates that the assertion is invalid.
   */
  private boolean isInvalid = false;

  /** Constructs an assertion object. */
  private CycAssertionImpl() {
  }

  /**
   * Constructs an assertion object given its HL formula (in conjunctive normal form) and assertion mt.
   *
   * @param hlFormula the assertion in HL (conjunctive normal) form
   * @param mt the assertion mt
   */
  public CycAssertionImpl(CycList hlFormula, CycObject mt) {
    //// Preconditions
    assert hlFormula != null : "hlFormula cannot be null";
    assert hlFormula.size() == 2 : "hlFormula must be a doubleton";
    assert mt != null : "mt cannot be null";
    {
      Object newNegLits = hlFormula.get(0);
      if (CycObjectFactory.nil.equals(newNegLits)) {
        newNegLits = Collections.emptyList();
      }
      assert (newNegLits instanceof Iterable) : "hlFormula must contain lists of literals";
      for (final Object lit : (Iterable) newNegLits) {
        if (lit instanceof CycFormulaSentence) {
          this.negLits.add((CycFormulaSentence) lit);
        } else {
          this.negLits.add(new CycFormulaSentence((Iterable<? extends Object>) lit));
        }
      }
    }
    {
      Object newPosLits = hlFormula.get(1);
      if (CycObjectFactory.nil.equals(newPosLits)) {
        newPosLits = Collections.emptyList();
      }
      assert (newPosLits instanceof Iterable) : "hlFormula must contain lists of literals";
      for (final Object lit : (Iterable) newPosLits) {
        if (lit instanceof CycFormulaSentence) {
          this.posLits.add((CycFormulaSentence) lit);
        } else {
          this.posLits.add(new CycFormulaSentence((Iterable<? extends Object>) lit));
        }
      }
    }
    this.mt = mt;
  }

  /**
   * Constructs a GAF assertion object from a single (positive) literal and assertion mt.
   *
   * @param posLit the GAF literal
   * @param mt the assertion mt
   */
  public CycAssertionImpl(FormulaSentence posLit, CycObject mt) {
    //// Preconditions
    assert mt != null : "mt cannot be null";
    this.posLits.add(posLit);
    this.mt = mt;
  }

  /** *  Constructs a the singleton invalid <tt>CycAssertionImpl</tt> object. 
   * This should only be called from CycObjectFactory.
   *
   * @return the invalid cyc assertion
   */
  public static CycAssertion makeInvalidAssertion() {
    final CycAssertionImpl cycAssertion = new CycAssertionImpl();
    cycAssertion.isInvalid = true;
    return cycAssertion;
  }

  /**
   * Indicates whether the object is equal to this object.
   *
   * @return <tt>true</tt> if the object is equal to this object, otherwise
   * returns <tt>false</tt>
   */
  @Override
  public boolean equals(Object object) {
    if (!(object instanceof CycAssertionImpl)) {
      return false;
    }
    CycAssertionImpl that = (CycAssertionImpl) object;
    if (this.isInvalid && that.isInvalid) {
      return true;
    } else if (!this.mt.equals(that.mt)) {
      return false;
    } else {
      return this.hlFormula.equals(that.hlFormula);
    }
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 41 * hash + (this.hlFormula != null ? this.hlFormula.hashCode() : 0);
    hash = 41 * hash + (this.mt != null ? this.mt.hashCode() : 0);
    hash = 41 * hash + (this.isInvalid ? 1 : 0);
    return hash;
  }
 

  /**
   * Returns a <tt>String</tt> representation of the <tt>CycAssertionImpl</tt>.
   *
   * @return a <tt>String</tt> representation of the <tt>CycAssertionImpl</tt>
   */
  @Override
  public String toString() {
    if (isInvalid) {
      return "INVALID-ASSERTION";
    } else {
      return hlFormula.cyclify();
    }
  }

  /**
   * Returns a cyclified string representation of the CycL assertion.
   * A cyclified string representation with escape chars is one where
   * constants have been prefixed with #$ and Strings have had an escape
   * character inserted before each character that needs to be escaped in SubL.
   *
   * @return a cyclified <tt>String</tt> with escape characters.
   */
  @Override
  public String cyclifyWithEscapeChars() {
    if (isInvalid) {
      return "INVALID-ASSERTION";
    } else {
      return hlFormula.cyclifyWithEscapeChars();
    }
  }

  /**
   * Returns this object in a form suitable for use as an <tt>String</tt> api expression value.
   *
   * @return this object in a form suitable for use as an <tt>String</tt> api expression value
   */
  @Override
  public String stringApiValue() {
    if (isInvalid) {
      return "INVALID-ASSERTION";
    } else {
      return "(find-assertion " + hlFormula.stringApiValue() + " " + mt.stringApiValue() + ")";
    }
  }

  /**
   * Returns this object in a form suitable for use as an <tt>CycArrayList</tt> api expression value.
   *
   * @return this object in a form suitable for use as an <tt>CycArrayList</tt> api expression value
   */
  @Override
  public Object cycListApiValue() {
    return this;
  }
  
  public CycObject getArg0(final CycAccess cycAccess) throws CycApiException, CycConnectionException {
    if (isGaf()) {
      return (CycObject)(getGaf().getArg0());
    } else {
      return (CycObject)(getELFormula(cycAccess).getArg0());
    }    
  }

  public Object getArg(int argNum, final CycAccess cycAccess) throws CycApiException, CycConnectionException {
    if (isGaf()) {
      return getGaf().getArg(argNum);
    } else {
      return (getELFormula(cycAccess).getArg(argNum));
    }    
  }


  /**
   * Returns the EL formula for this assertion.
   *
   * @return the EL formula for this assertion
   */
  @Override
  public FormulaSentence getELFormula(final CycAccess access) throws CycApiException, CycConnectionException {
    if (isGaf()) {
      return getGaf();
    } else {
      return getFormulaFromCyc(access);
    }
  }

  /**
   * Returns the HL formula (in conjunctive normal form) for this assertion.
   *
   * @return the HL formula for this assertion
   */
  public CycList getFormula() {
    return hlFormula;
  }

  private FormulaSentence getFormulaFromCyc(CycAccess access) throws CycApiException, CycConnectionException {
    String command = makeSubLStmt(ASSERTION_EL_FORMULA, this);
    return access.converse().converseSentence(command);
  }

  /**
   * Returns the Ground Atomic Formula (gaf) for this assertion.
   *
   * @return the Ground Atomic Formula (gaf) for this assertion
   */
  public FormulaSentence getGaf() {
    if (!isGaf()) {
      return null;
    }
    //// Preconditions
    assert !hlFormula.isEmpty() : "hlFormula cannot be empty";
    assert negLits.isEmpty() : ((CycArrayList) hlFormula).cyclify() + " negativeLiterals must be nil";
    if (posLits.size() == 1) {
      return posLits.first();
    }
    return null;
  }

  /**
   * Returns the Ground Atomic Formula (gaf) for this assertion.
   *
   * @param cycAccess the Cyc communications object
   * @return the Ground Atomic Formula (gaf) for this assertion
   * @deprecated cycAccess is not necessary.
   */
  public FormulaSentence getGaf(final CycAccess cycAccess) {
    return getGaf();
  }
    
  /** @return true iff this assertion's formula is both ground and atomic. */
  public boolean isGaf() {
    if (negLits.isEmpty()) {
      if (posLits.size() == 1) {
        return noVars(posLits.first());
      }
    }
    return false;
  }

  private boolean noVars(final Object term) {
    if (term instanceof CycVariableImpl) {
      return false;
    } else if (term instanceof CycArrayList) {
      for (final Object arg : (CycArrayList) term) {
        if (!noVars(arg)) {
          return false;
        }
      }
      return true;
    } else {
      return true;
    }
  }

  /**
   * Returns the microtheory for this assertion.
   *
   * @return the microtheory for this assertion
   */
  public CycObject getMt() {
    return mt;
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
   * Prints the XML representation of the CycAssertionImpl to an <code>XMLWriter</code>
   *
   * @param xmlWriter an <tt>XMLWriter</tt>
   * @param indent an int that specifies by how many spaces to indent
   * @param relative a boolean; if true indentation is relative, otherwise absolute
   */
  @Deprecated
  public void toXML(XMLWriter xmlWriter, int indent, boolean relative)
          throws IOException {
    xmlWriter.printXMLStartTag(cycAssertionXMLTag, indent, relative, true);
    hlFormula.toXML(xmlWriter, indent, relative);
    convertCycObjectToXML(mt, xmlWriter, indent, relative);
    xmlWriter.printXMLEndTag(cycAssertionXMLTag, indent, relative);
  }

  /**
   * Returns a list of all constants referred to by this CycObject.
   * For example, a CycConstant will return a List with itself as the
 value, a nart will return a list of its functor and all the constants refered
 to by its arguments, a CycArrayList will do a deep search for all constants,
 a symbol or variable will return the empty list.
   * @return a list of all constants refered to by this CycObject
   **/
  public List getReferencedConstants() {
    List result = null;
    if (getFormula() != null) {
      result = DefaultCycObject.getReferencedConstants(getFormula());
      if (getMt() != null) {
        result.addAll(getMt().getReferencedConstants());
      }
      return result;
    }
    if (getMt() != null) {
      result = DefaultCycObject.getReferencedConstants(getMt());
    }
    return (result == null) ? new ArrayList() : result;
  }

  public int compareTo(Object o) {
    if (!(o instanceof CycAssertionImpl)) {
      return toString().compareTo(o.toString());
    }
    CycAssertionImpl cao = (CycAssertionImpl) o;
    int ret = this.getMt().compareTo(cao.getMt());
    if (ret != 0) {
      return ret;
    }
    return this.getFormula().compareTo(cao.getFormula());
  }
}







