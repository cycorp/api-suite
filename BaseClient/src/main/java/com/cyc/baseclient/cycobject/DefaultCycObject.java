package com.cyc.baseclient.cycobject;

/*
 * #%L
 * File: DefaultCycObject.java
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

import com.cyc.base.BaseClientRuntimeException;
import com.cyc.base.CycAccess;
import com.cyc.base.CycConnectionException;
import com.cyc.base.cycobject.CycAssertion;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.CycSymbol;
import com.cyc.base.cycobject.CycVariable;
import com.cyc.base.cycobject.Fort;
import com.cyc.query.InferenceParameters;
import java.io.IOException;
import java.util.*;
import java.math.BigInteger;
import com.cyc.baseclient.api.CompactHLIDConverter;
import com.cyc.baseclient.xml.XMLWriter;
import com.cyc.baseclient.CycObjectFactory;
import com.cyc.baseclient.datatype.StringUtils;

/**
 * This is the default implementation of a CycL object.
 *
 * @version $Id: DefaultCycObject.java 158569 2015-05-19 21:51:08Z daves $
 * @author  tbrussea
 *
 */
public abstract class DefaultCycObject implements CycObject {

  /**
   * Field for storing the name of the XML tag for CycConstant objects
   */
  public static final String objectXMLTag = "cycl-object";

  /**
   * Returns a cyclified string representation of the CycL object.
   * By default, just returns the result of calling "toString()".
   * A cyclified string representation is one where constants have been
   * prefixed with #$.
   *
   * @return a cyclified <tt>String</tt>.
   */
  @Override
  public String cyclify() {
    return toString();
  }

  /**
   * Returns a cyclified string representation of the CycL object.
   * By default, just returns the result of calling "cyclify()".
   * A cyclified string representation with escape chars is one where
   * constants have been prefixed with #$ and Strings have had an escape
   * character inserted before each character that needs to be escaped in SubL.
   *
   * @return a cyclified <tt>String</tt> with escape characters.
   */
  @Override
  public String cyclifyWithEscapeChars() {
    return cyclify();
  }

  /**
   * Returns a cyclified string representation of the Base Client object.
   * Embedded constants are prefixed with "#$".  Embedded quote chars in strings
   * are escaped.
   *
   * @return a <tt>String</tt> representation in cyclified form.
   *
   */
  public static String cyclifyWithEscapeChars(Object obj) {
    return cyclifyWithEscapeChars(obj, false);
  }

  /**
   * Returns a cyclified string representation of the Base Client object.
   * Embedded constants are prefixed with "#$".  Embedded quote chars in strings
   * are escaped.
   *
   * @return a <tt>String</tt> representation in cyclified form.
   *
   */
  public static String cyclifyWithEscapeChars(Object obj, boolean isApi) {
    if ((obj == null) || (!isCycLObject(obj))) {
      throw new BaseClientRuntimeException("Cannot cyclify (escaped): '" + obj + "'.");
    }
    if (obj instanceof CycObject) {
      return ((CycObject)obj).cyclifyWithEscapeChars();
    }
    if (obj instanceof String) {
      String str = (String) obj;
      if (StringUtils.is7BitASCII(str)) {
        return "\"" + StringUtils.escapeDoubleQuotes(str) + "\"";
      } else {
        return StringUtils.unicodeEscaped(str, isApi);
      }
    }
    if (obj instanceof Character) {
      return StringUtils.escapeDoubleQuotes(cyclify(obj));
    }
    if (obj instanceof Boolean) {
      return (obj == Boolean.FALSE) ? "nil" : "t";
    }
    // Apparently the dwimming happens elsewhere, but I fixed that so it can 
    // take cyclish numbers too
    if (obj instanceof Double){
      return obj.toString().replaceFirst("E", "d");
    }
     if (obj instanceof Float){
      return obj.toString().replaceFirst("E", "d");
    }
    return obj.toString();
  }

  /**
   * Returns a cyclified string representation of the given <tt>Object</tt>.
   * Embedded constants are prefixed with "#$".
   *
   * @return a <tt>String</tt> representation in cyclified form.
   *
   */
  public static String cyclify(Object obj) {
    if (obj == null) {
      throw new BaseClientRuntimeException("Cannot cyclify null obj");
    } else if (!isCycLObject(obj)) {
      throw new BaseClientRuntimeException("Cannot cyclify: '" + obj + "' (" + obj.getClass().getName() + ").");
    }
    if (obj instanceof CycObject) {
      return ((CycObject) obj).cyclify();
    }
    if (obj instanceof String) {
      return "\"" + (String) obj + "\"";
    }
    if (obj instanceof Character) {
      // @hack -- do better job of this. Need to support other non-printable characters!!!
      Character theChar = (Character) obj;
      if (theChar == ' ') {
        return "#\\Space";
      } else if (theChar == '\n') {
        return "#\\Newline";
      } else if (theChar == '\r') {
        return "#\\Return";
      } else if (theChar == '\t') {
        return "#\\Tab";
      }
      if (Character.isWhitespace(theChar)) {
        throw new IllegalArgumentException("Don't know how to trasmit the whitespace character: "
                + (int) theChar.charValue());
      }
      return "#\\" + obj;
    }
    return obj.toString();
  }

  public static List getReferencedConstants(Object obj) {
    if (obj == null) {
      return new ArrayList();
    }
    if ((obj == null) || (!isCycLObject(obj))) {
      throw new BaseClientRuntimeException("Got an object that is not a valid CycL term: '" + obj + "' (" + obj.getClass().getName() + ").");
    }
    if (!(obj instanceof CycObject)) {
      return new ArrayList();
    }
    return ((CycObject) obj).getReferencedConstants();
  }
  private static final List emptyList = Arrays.asList(new Object[0]);

  @Override
  public List getReferencedConstants() {
    return emptyList;
  }

  /**
   * Returns the given <tt>Object</tt> in a form suitable for use as a <tt>String</tt> api expression value.
   *
   * @return the given <tt>Object</tt> in a form suitable for use as a <tt>String</tt> api expression value
   */
  public static String stringApiValue(Object obj) {
    if ((obj == null) || (!isCycLObject(obj) && (obj instanceof List))) {
      throw new BaseClientRuntimeException(obj + " cannot be converted to a form suitable for use as a String api expression value.");
    }
    if (obj instanceof CycObject) {
      return ((CycObject) obj).stringApiValue();
    }
    if (obj instanceof List) {
      return CycArrayList.stringApiValue((List)obj);
    }
    if (obj instanceof InferenceParameters) {
      return ((InferenceParameters) obj).stringApiValue();
    }
    return cyclifyWithEscapeChars(obj, true);
  }

  public static String stringApiValue(boolean val) {
    return (val == false) ? "nil" : "t";
  }

  public static String currentOpenCycURIForHLID(String id) {
    return "http://sw.opencyc.org/concept/" + id;
  }

  public static String currentOpenCycURIForCycLString(String cycl) {
    return "http://sw.opencyc.org/concept/en/" + cycl;
  }

    /**
     * Returns true iff the given object is an object than can be (or be
     * contained in) a CycL expression.
     *
     * <P>In general, any {@link CycObject}, boolean, number, or string will
     * pass this test.
     *
     * <P><B>Note regarding strings and numbers:</B> The CycL language itself includes
     * only those integers, floats, and strings admitted by the SubL language as
     * primitives. When assertions or queries are made using other kinds of
     * numbers or strings, they may be converted to valid CycL equivalents. For
     * instance, non-ASCII strings may be converted into #$UnicodeStringFn
     * NAUTs.
     *
     * @return true iff the given object is an object than can be contained in a
     * CycL expression
     */
    public static boolean isCycLObject(Object obj) {
        return (obj instanceof CycObject
                || obj instanceof InferenceParameters//@hack this is wrong inference params are not CycL objects --APB
                || obj instanceof Boolean
                || obj instanceof String
                || obj instanceof Integer
                || obj instanceof Character
                || obj instanceof Long
                || obj instanceof BigInteger
                || obj instanceof GuidImpl //@hack this is wrong GUIDs are not CycL objects --APB
                || obj instanceof Float
                || obj instanceof Double);
    }

  /**
   * Returns a pretty CycL representation of this object.
   *
   * @return a string representation without causing additional api calls to determine
   * constant names
   */
  public static String toPrettyString(Object obj) {
    if (obj instanceof String) {
      return "\"" + obj.toString() + "\"";
    } else if (obj instanceof CycArrayList) {
      return ((CycArrayList) obj).toPrettyString("");
    }
    return obj.toString();
  }

  /**
   * Returns this object in a form suitable for use as an <tt>String</tt> api expression value.
   *
   * @return this object in a form suitable for use as an <tt>String</tt> api expression value
   */
  @Override
  public String stringApiValue() {
    return cyclifyWithEscapeChars();
  }

  /**
   * Returns this object in a form suitable for use as an <tt>CycArrayList</tt> api expression value.
   *
   * @return this object in a form suitable for use as an <tt>CycArrayList</tt> api expression value
   */
  @Override
  public Object cycListApiValue() {
    return cyclify();
  }

  /**
   * Prints the XML representation of the CycConstant to an <code>XMLWriter</code>
   *
   * @param xmlWriter an <tt>XMLWriter</tt>
   * @param indent an int that specifies by how many spaces to indent
   * @param relative a boolean; if true indentation is relative, otherwise absolute
   */
  @Deprecated
  public void toXML(XMLWriter xmlWriter, int indent, boolean relative)
          throws IOException {
    xmlWriter.printXMLStartTag(objectXMLTag, indent, relative, true);
    xmlWriter.print(stringApiValue());
    xmlWriter.printXMLEndTag(objectXMLTag, -indent, true);
  }
  
  @Deprecated
  protected void convertCycObjectToXML(CycObject cobj, XMLWriter xmlWriter, int indent, boolean relative)
          throws IOException {
    if ((cobj != null) && (cobj instanceof DefaultCycObject)) {
      ((DefaultCycObject) cobj).toXML(xmlWriter, indent, relative);
    }
  }

  public static String toCompactExternalId(String cycObject) throws IOException {
    return CompactHLIDConverter.converter().toCompactHLId(cycObject);
  }

  public static String toCompactExternalId(Number cycObject) throws IOException {
    return CompactHLIDConverter.converter().toCompactHLId(cycObject);
  }

  public static String toCompactExternalId(Object cycObject, CycAccess access)
          throws CycConnectionException {
    if ((cycObject == null) || (!DefaultCycObject.isCycLObject(cycObject))) {
      throw new IllegalArgumentException(cycObject + "is not a valid CycL object.");
    }
    try {
      if (cycObject instanceof Number) {
        return CompactHLIDConverter.converter().toCompactHLId((Number) cycObject);
      }
      if (cycObject instanceof CycNumberImpl) {
        return CompactHLIDConverter.converter().toCompactHLId(((CycNumberImpl)cycObject).getNumber());
      }
      if (cycObject instanceof String) {
        return CompactHLIDConverter.converter().toCompactHLId((String) cycObject);
      }
    } catch (IOException ioe) {
      throw new CycConnectionException(ioe);
    }
    Object result = cycObjectToCompactExternalIdCache.get(cycObject);
    if (result != null) {
      return (String) result;
    }
    result = access.converse().converseObject("(compact-hl-external-id-string " + DefaultCycObject.stringApiValue(cycObject) + ")");
    if (!(result instanceof String)) {
      throw new BaseClientRuntimeException("Got invalid result: " + result);
    }
    cycObjectToCompactExternalIdCache.put(cycObject, (String) result);
    return (String) result;
  }

  /**
   * Decode compactExternalId and return the resulting object.  This is intended to be used only in
   * cases where it's certain that compactExternalId is in fact a valid compact HL-ID, as it does not 
   * perform checks to make sure that the string is in fact the HL-ID for the returned object.  Thus, 
   * it may return false positives for strings that aren't actually compact external HL-IDs.
   * @param compactExternalId
   * @param access
   * @return the object uniquely identified by compactExternalId
   * @throws com.cyc.base.CycConnectionException
   * @see #fromPossibleCompactExternalId(java.lang.String, com.cyc.base.CycAccess) 
   */
  public static Object fromCompactExternalId(String compactExternalId, CycAccess access)
          throws CycConnectionException {
    if ((compactExternalId == null) || ("".equals(compactExternalId))) {
      throw new IllegalArgumentException(compactExternalId + "is not a valid compact external id.");
    }
    // @todo support CompactHLIDConverter.converter() once we can simply identify
    // if a compact external id is a String or Number
    Object result = compactExternalIdToCycObjectCache.get(compactExternalId);
    if (result != null) {
      return result;
    }
    try {
      if (CompactHLIDConverter.converter().isNumberCompactHLId(compactExternalId)) {
        Number num = (Number) CompactHLIDConverter.converter().fromCompactHLId(compactExternalId);
        result = CycObjectFactory.makeCycNumber(num);
      } else {
        result = access.converse().converseObject("(find-cycl-object-by-compact-hl-external-id-string " + DefaultCycObject.stringApiValue(compactExternalId) + ")");
      }
    } catch (IOException ioe) {
      throw new CycConnectionException(ioe);
    }
    compactExternalIdToCycObjectCache.put(compactExternalId, result);
    return result;
  }

  /**
   * Test to see if <code>compactExternalId</code> is a compact HL-ID, and return the relevant object iff
   * <code>compactExternalId</code> is the canonical id for the object.  This is intended to be used in
   * cases where it's not known whether compactExternalId is in fact a valid compact HL-ID, as it
   * performs extra checks to make sure that the string is in fact the HL-ID for the returned object.
   * @param compactExternalId
   * @param access
   * @return the object uniquely identified by compactExternalId
   * @see #fromCompactExternalId(java.lang.String, com.cyc.base.CycAccess) 
   * @throws IOException 
   */
  public static Object fromPossibleCompactExternalId(String compactExternalId, CycAccess access)
          throws CycConnectionException {
    if ((compactExternalId == null) || ("".equals(compactExternalId))) {
      throw new IllegalArgumentException(compactExternalId + "is not a valid compact external id.");
    }
    // @todo support CompactHLIDConverter.converter() once we can simply identify
    // if a compact external id is a String or Number
    Object result = compactExternalIdToCycObjectCache.get(compactExternalId);
    if (result != null) {
      return result;
    }
    try {
      if (CompactHLIDConverter.converter().isNumberCompactHLId(compactExternalId)) {
        Number num = (Number) CompactHLIDConverter.converter().fromCompactHLId(compactExternalId);
        result = CycObjectFactory.makeCycNumber(num);
      } else {
        result = access.converse().converseList("(multiple-value-list (maybe-find-object-by-compact-hl-external-id-string " + DefaultCycObject.stringApiValue(compactExternalId) + "))");
        if (((CycArrayList)result).second() == CycObjectFactory.nil) {
          return null;
        }
      }
    } catch (IOException ioe) {
      throw new CycConnectionException(ioe);
    }
    compactExternalIdToCycObjectCache.put(compactExternalId, ((CycArrayList)result).first());
    return ((CycArrayList)result).first();
  }
    
  //@todo provide separate cache per CycAccess
  private static final int MAX_ENTRIES = 20000;
  private static final LinkedHashMap<String, Object> compactExternalIdToCycObjectCache = new LinkedHashMap() {

    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
      return size() > MAX_ENTRIES;
    }
  };
  private static final LinkedHashMap<Object, String> cycObjectToCompactExternalIdCache = new LinkedHashMap() {

    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
      return size() > MAX_ENTRIES;
    }
  };

  /**
   * Returns whether the given Object represents a Cyc Collection.
   *
   * @return whether the given Object represents a Cyc Collection.
   */
  public static boolean isCollection(Object term, CycAccess cycAccess) throws CycConnectionException {
    return cycAccess.getInspectorTool().isCollection(term);
  }

  public static int getCycObjectType(Object object) {
    if (object instanceof ByteArray) {
      return CYCOBJECT_BYTEARRAY;
    } else if (object instanceof CycAssertion) {
      return CYCOBJECT_CYCASSERTION;
    } else if (object instanceof Fort) {
      return CYCOBJECT_CYCFORT;
    } else if (object instanceof CycSymbol) {
      return CYCOBJECT_CYCSYMBOL;
    } else if (object instanceof CycVariable) {
      return CYCOBJECT_CYCVARIABLE;
    } else if (object instanceof CycArrayList) {
      return CYCOBJECT_CYCLIST;
    } else if (object instanceof Double) {
      return CYCOBJECT_DOUBLE;
    } else if (object instanceof Float) {
      return CYCOBJECT_FLOAT;
    } else if (object instanceof GuidImpl) {
      return CYCOBJECT_GUID;
    } else if (object instanceof Integer) {
      return CYCOBJECT_INTEGER;
    } else if (object instanceof Long) {
      return CYCOBJECT_LONG;
    } else if (object instanceof BigInteger) {
      return CYCOBJECT_BIGINTEGER;
    } else if (object instanceof String) {
      return CYCOBJECT_STRING;
    } else {
      return CYCOBJECT_UNKNOWN;
    }
  }
}














