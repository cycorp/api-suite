package com.cyc.baseclient.parser;

/*
 * #%L
 * File: CyclParserUtil.java
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
import com.cyc.base.exception.BaseClientRuntimeException;
import com.cyc.base.CycAccess;
import com.cyc.base.CycAccessManager;
import com.cyc.baseclient.cycobject.CycVariableImpl;
import com.cyc.baseclient.cycobject.DefaultCycObject;
import com.cyc.baseclient.cycobject.CycArrayList;
import com.cyc.baseclient.cycobject.CycConstantImpl;
import com.cyc.baseclient.cycobject.CycFormulaSentence;
import com.cyc.baseclient.exception.CycApiServerSideException;
import com.cyc.baseclient.CycObjectFactory;
import com.cyc.base.exception.CycApiException;
import com.cyc.baseclient.CommonConstants;
import com.cyc.base.exception.CycConnectionException;
import com.cyc.session.CycServer;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.Fort;
import com.cyc.base.cycobject.Nart;
import com.cyc.baseclient.kbtool.CycObjectTool;

//// External Imports
import java.io.*;
import java.util.*;

/**
 * <P>CycLParserUtil is designed to be the main entry point into parsing
 * CycL expressions.
 *
 * @version $Id: CyclParserUtil.java 162904 2015-12-02 18:35:34Z nwinant $
 * @author Tony Brusseau
 */
public class CyclParserUtil {
  
  //// Constructors
  
  /** Creates a new instance of CycLParserUtil. */
  private CyclParserUtil() {}
  
  //// Public Area
  
  public static Object parseCycLTerm(String toParse, boolean testForEOF, CycAccess access) 
  throws ParseException, CycConnectionException, CycApiException, CycApiServerSideException, 
  InvalidConstantNameException, InvalidConstantGuidException, 
  UnsupportedVocabularyException, TokenMgrError, IOException {
    return parseCycLTerm(new StringReader(toParse), testForEOF, access);
  }
  
  public static Object parseCycLTerm(Reader reader, boolean testForEOF, CycAccess access) 
  throws ParseException, CycConnectionException, CycApiException, CycApiServerSideException, 
  InvalidConstantNameException, InvalidConstantGuidException, 
  UnsupportedVocabularyException, TokenMgrError, IOException {
    CyclParser parser = new CyclParser(reader, access);
    return completeConstants(parser.term(testForEOF), access);
  }
  
  public static CycArrayList parseCycLTermList(String toParse, boolean testForEOF, CycAccess access) 
  throws ParseException, CycConnectionException, CycApiException, CycApiServerSideException, 
  InvalidConstantNameException, InvalidConstantGuidException, 
  UnsupportedVocabularyException, TokenMgrError, IOException {
    return parseCycLTermList(new StringReader(toParse), testForEOF, access);
  }
  
  public static CycArrayList parseCycLTermList(Reader reader, boolean testForEOF, CycAccess access) 
  throws ParseException, CycConnectionException, CycApiException, CycApiServerSideException, 
  InvalidConstantNameException, InvalidConstantGuidException, 
  UnsupportedVocabularyException, TokenMgrError, IOException {
    CyclParser parser = new CyclParser(reader, access);
    return (CycArrayList)completeConstants(parser.termList(testForEOF), access);
  }
  
  public static CycFormulaSentence parseCycLSentence(String toParse, boolean testForEOF, CycAccess access)
  throws ParseException, CycConnectionException, CycApiException, CycApiServerSideException, 
  InvalidConstantNameException, InvalidConstantGuidException, 
  UnsupportedVocabularyException, TokenMgrError, IOException {
    return parseCycLSentence(new StringReader(toParse), testForEOF, access);
  }
  
  public static CycFormulaSentence parseCycLSentence(Reader reader, boolean testForEOF, CycAccess access)
  throws ParseException, CycConnectionException, CycApiException, CycApiServerSideException, 
  InvalidConstantNameException, InvalidConstantGuidException, 
  UnsupportedVocabularyException, TokenMgrError, IOException {
    CyclParser parser = new CyclParser(reader, access);
    return new CycFormulaSentence((CycArrayList)completeConstants(parser.sentence(testForEOF), access));
  }
  
  public static String parseCycLString(String toParse, boolean testForEOF, CycAccess access) 
  throws ParseException, CycConnectionException, CycApiException, CycApiServerSideException, 
  TokenMgrError {
    return parseCycLString(new StringReader(toParse), testForEOF, access);
  }
  
  public static String parseCycLString(Reader reader, boolean testForEOF, CycAccess access) 
  throws ParseException, CycConnectionException, CycApiException, CycApiServerSideException,
  TokenMgrError{
    CyclParser parser = new CyclParser(reader, access);
    return parser.string(testForEOF);
  }
  
  public static Number parseCycLNumber(String toParse, boolean testForEOF, CycAccess access) 
  throws ParseException, CycConnectionException, CycApiException, CycApiServerSideException, 
  TokenMgrError {
    return parseCycLNumber(new StringReader(toParse), testForEOF, access);
  }
  
  public static Number parseCycLNumber(Reader reader, boolean testForEOF, CycAccess access) 
  throws ParseException, CycConnectionException, CycApiException, CycApiServerSideException, 
  TokenMgrError {
    CyclParser parser = new CyclParser(reader, access);
    return parser.number(testForEOF);
  }
  
  public static CycConstantImpl parseCycLConstant(String toParse, boolean testForEOF, CycAccess access) 
  throws ParseException, CycConnectionException, CycApiException, CycApiServerSideException, 
  InvalidConstantNameException, InvalidConstantGuidException, 
  UnsupportedVocabularyException, TokenMgrError, IOException {
    return parseCycLConstant(new StringReader(toParse), testForEOF, access);
  }
  
  public static CycConstantImpl parseCycLConstant(Reader reader, boolean testForEOF, CycAccess access) 
  throws ParseException, CycConnectionException, CycApiException, CycApiServerSideException, 
  InvalidConstantNameException, InvalidConstantGuidException, 
  UnsupportedVocabularyException, TokenMgrError, IOException {
    CyclParser parser = new CyclParser(reader, access);
    return (CycConstantImpl)completeConstants(parser.constant(testForEOF), access);
  }
  
  public static CycVariableImpl parseCycLVariable(String toParse, boolean testForEOF, CycAccess access) 
  throws ParseException, CycConnectionException, CycApiException, CycApiServerSideException, 
  TokenMgrError {
    return parseCycLVariable(new StringReader(toParse), testForEOF, access);
  }
  
  public static CycVariableImpl parseCycLVariable(Reader reader, boolean testForEOF, CycAccess access) 
  throws ParseException, CycConnectionException, CycApiException, CycApiServerSideException, 
  TokenMgrError {
    CyclParser parser = new CyclParser(reader, access);
    return parser.variable(testForEOF);
  }
  
  public static Object parseCycLDenotationalTerm(String toParse, boolean testForEOF, CycAccess access) 
  throws ParseException, CycConnectionException, CycApiException, CycApiServerSideException, 
  InvalidConstantNameException, InvalidConstantGuidException, 
  UnsupportedVocabularyException, TokenMgrError, IOException {
    return parseCycLDenotationalTerm(new StringReader(toParse), testForEOF, access);
  }
  
  public static Object parseCycLDenotationalTerm(Reader reader, boolean testForEOF, CycAccess access) 
  throws ParseException, CycConnectionException, CycApiException, CycApiServerSideException, 
  InvalidConstantNameException, InvalidConstantGuidException, 
  UnsupportedVocabularyException, TokenMgrError, IOException {
    CyclParser parser = new CyclParser(reader, access);
    return (Object)completeConstants(parser.denotationalTerm(testForEOF), access);
  }

  public static Fort parseCycLFORT(String toParse, boolean testForEOF, CycAccess access)
  throws ParseException, CycConnectionException, CycApiException, CycApiServerSideException,
  InvalidConstantNameException, InvalidConstantGuidException,
  UnsupportedVocabularyException, TokenMgrError, IOException {
    return parseCycLFORT(new StringReader(toParse), testForEOF, access);
  }

  public static Fort parseCycLFORT(Reader reader, boolean testForEOF, CycAccess access)
  throws ParseException, CycConnectionException, CycApiException, CycApiServerSideException,
  InvalidConstantNameException, InvalidConstantGuidException,
  UnsupportedVocabularyException, TokenMgrError, IOException {
    Object result = parseCycLDenotationalTerm(reader, testForEOF, access);
    if (result instanceof CycArrayList) {
      Object result2 = ((CycObjectTool)(access.getObjectTool())).getHLCycTerm(DefaultCycObject.cyclify(result));
      if (!(result2 instanceof Nart)) {
        throw new BaseClientRuntimeException("Unknown fort: " + result);
      }
      return (Nart)result2;
    } else if (result instanceof CycConstantImpl) {
      return (CycConstantImpl)result;
    } else if (result instanceof Nart) {
      return (Nart)result;
    }
    throw new BaseClientRuntimeException("Unable to find appropriate FORT.");
  }

  /** Takes a CycL formula represented in CycArrayList form and replaces all the subcomponents
 that are NARTs in the KB with Nart objects. All other CycL objects are returned unchanged.
 Note, this function calls a non-api level SubL call, therefore it may error when called
 against OpenCyc images but will be accessible from ResearchCyc. It has the advantage over "toHL"
 because nartSubstitute does not do other sorts of canonicalizations like reordering arguments
 and such. Note: this isn't a parsing function and passing a string of a formula will just
 return the string unchanged. Note: NARTs should be an implementation detail of the
 inference engine, however, there are quite a few expections where api methods
 behave differently whether they are used or not.
   **/
  public static Object nartSubstitute(Object cyclObject, CycAccess access)
  throws CycConnectionException {
    if (!DefaultCycObject.isCycLObject(cyclObject)) {
      throw new BaseClientRuntimeException(DefaultCycObject.cyclify(cyclObject) + " is not a valid Cyc object.");
    }
    if (!(cyclObject instanceof CycObject)) { // @todo need a test that sees if the CycObject
                                              // contains any CycLists any fast fail if not
      return cyclObject;
    }
    return access.converse().converseObject("(nart-substitute  '" + DefaultCycObject.cyclifyWithEscapeChars(cyclObject, true) + ")");
  }

   /** Takes a CycL formula represented in CycArrayList form and replaces all the subcomponents
 that are NARTs in the KB with Nart objects as well as doing arguments reordering and
 variable renaming and conversion. All other CycL objects are returned unchanged.
 Note: this isn't a parsing function and passing a string of a formula will just
 return the string unchanged. Note: NARTs and HL constructs should be an implementation
 detail of the inference engine, however, there are quite a few expections where api methods
 behave differently whether they are used or not.
   **/
  public static Object toHL(Object cyclObject, CycAccess access)
  throws CycConnectionException {
    if (!DefaultCycObject.isCycLObject(cyclObject)) {
      throw new BaseClientRuntimeException(DefaultCycObject.cyclify(cyclObject) + " is not a valid Cyc object.");
    }
    if (!(cyclObject instanceof CycObject)) {
      return cyclObject;
    }
    return access.converse().converseObject("(canonicalize-term  '" + DefaultCycObject.cyclifyWithEscapeChars(cyclObject, true) + ")");
  }
  
  public static Object parseCycLNonAtomicDenotationalTerm(String toParse, boolean testForEOF, CycAccess access) 
  throws ParseException, CycConnectionException, CycApiException, CycApiServerSideException, 
  InvalidConstantNameException, InvalidConstantGuidException, 
  UnsupportedVocabularyException, TokenMgrError, IOException {
    return parseCycLNonAtomicDenotationalTerm(new StringReader(toParse), testForEOF, access);
  }
  
  public static Object parseCycLNonAtomicDenotationalTerm(Reader reader, boolean testForEOF, CycAccess access) 
  throws ParseException, CycConnectionException, CycApiException, CycApiServerSideException, 
  InvalidConstantNameException, InvalidConstantGuidException, 
  UnsupportedVocabularyException, TokenMgrError, IOException {
    CyclParser parser = new CyclParser(reader, access);
    return (Object)completeConstants(parser.nonAtomicDenotationalTerm(testForEOF), access);
  }

  public static Object completeConstants(Object obj, CycAccess access) 
    throws CycConnectionException, 
           CycApiException, 
           CycApiServerSideException, 
           InvalidConstantNameException, 
           InvalidConstantGuidException, 
           UnsupportedVocabularyException {
    List allConstants = DefaultCycObject.getReferencedConstants(obj);
    if ((allConstants == null) || (allConstants.size() == 0)) { return obj; }
    CycArrayList incompleteConstantsWithNames = null;
    CycArrayList incompleteConstantsWithGuids = null;
    //Find incomplete constants
    for ( Iterator iter = allConstants.iterator(); iter.hasNext(); ) {
      CycConstantImpl curConst = (CycConstantImpl)iter.next();
      if ((curConst.name == null) && (curConst.guid == null)) {
        throw new IllegalArgumentException("Can't deal with completely bare constants.");
      }
      if (curConst.name == null) {
        if (incompleteConstantsWithGuids == null) { incompleteConstantsWithGuids = new CycArrayList(); }
        incompleteConstantsWithGuids.add(curConst);
      }
      if (curConst.guid == null) {
        if (incompleteConstantsWithNames == null) { incompleteConstantsWithNames = new CycArrayList(); }
        incompleteConstantsWithNames.add(curConst);
      }
    }
    //Find invalid constant names from the list of incomplete constants
    List cycConstants = access.getLookupTool().findConstantsForNames(incompleteConstantsWithNames);
    if ((cycConstants != null) && (incompleteConstantsWithNames != null)) {
      InvalidConstantNameException icne = null;
      for (Iterator iter = cycConstants.iterator(), 
          oldIter = incompleteConstantsWithNames.iterator(); iter.hasNext(); ) {
        Object curConstant = iter.next();
        CycConstantImpl oldConstant = (CycConstantImpl)oldIter.next();
        if (!(curConstant instanceof CycConstantImpl)) {
          if (icne == null) { icne = new InvalidConstantNameException(); }
          icne.addInvalidConstantName(oldConstant.getName());
        } else {
          oldConstant.setGuid(((CycConstantImpl)curConstant).getGuid());
          CycObjectFactory.addCycConstantCache(oldConstant);
        }
      }
      if (icne != null) { throw icne; }
    }
    //Find invlaid GUIDs from the list of incomplete constants
    cycConstants = access.getLookupTool().findConstantsForGuids(incompleteConstantsWithGuids);
    if ((cycConstants != null) && (incompleteConstantsWithGuids != null)) {
      InvalidConstantGuidException icge = null;
      for (Iterator iter = cycConstants.iterator(), 
          oldIter = incompleteConstantsWithGuids.iterator(); iter.hasNext(); ) {
        Object curConstant = iter.next();
        CycConstantImpl oldConstant = (CycConstantImpl)oldIter.next();
        if (!(curConstant instanceof CycConstantImpl)) {
          if (icge == null) { icge = new InvalidConstantGuidException(); }
          icge.addInvalidConstantGuid(oldConstant.getGuid());
        } else {
          oldConstant.setName(((CycConstantImpl)curConstant).getName());
          CycObjectFactory.addCycConstantCache(oldConstant);
        }
      }
      if (icge != null) { throw icge; }
    }
    //Find unsupported constants
    for ( Iterator iter = allConstants.iterator(); iter.hasNext(); ){
      CycConstantImpl curConst = (CycConstantImpl)iter.next();
      if (CommonConstants.SUBL_QUOTE_FN.guid.equals(curConst.getGuid())) {
        throw new UnsupportedVocabularyException(CommonConstants.SUBL_QUOTE_FN);
      }
      if (CommonConstants.EXPAND_SUBL_FN.guid.equals(curConst.getGuid())) {
        throw new UnsupportedVocabularyException(CommonConstants.EXPAND_SUBL_FN);
      }
    }
    return obj;
  }
  
  //// Protected Area
  
  protected static void sanityCheck() {
    try {
      CycAccess access = CycAccessManager.getCurrentAccess();
      Object obj = parseCycLConstant("Dog", true, access);
      System.out.println("Got result: " + obj);
      obj = parseCycLConstant("#G\"bd590573-9c29-11b1-9dad-c379636f7270\"", true, access);
      System.out.println("Got result: " + obj);
      try {
        obj = parseCycLConstant("Dogqweqr", true, access);
        System.out.println("Got result: " + obj);
      } catch (Exception e) { e.printStackTrace(System.out); }
      try {
        obj = parseCycLConstant("#G\"bd590573-9c29-11b1-9dad-c379636f7279\"", true, access);
        System.out.println("Got result: " + obj);
      } catch (Exception e) { e.printStackTrace(System.out); }
      try {
        obj = parseCycLFORT("(FruitFn AppleTree)", true, access);
        System.out.println("Got FORT: " + obj);
      } catch (Exception e) { e.printStackTrace(System.out); }
      try {
        obj = parseCycLDenotationalTerm("(FruitFn AppleTree)", true, access);
        System.out.println("Got FORT: " + obj + " of type: " + obj.getClass());
        obj = nartSubstitute(obj, access);
        System.out.println("Got FORT: " + obj + " of type: " + obj.getClass());
        obj = toHL(obj, access);
        System.out.println("Got FORT: " + obj + " of type: " + obj.getClass());
        obj = nartSubstitute("\"", access);
        System.out.println("Got single double quote string as: " + obj);
        obj = toHL("\"", access);
        System.out.println("Got single double quote string as: " + obj);
      } catch (Exception e) { e.printStackTrace(System.out); }
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Finished abnormally");
      System.exit(-1);
    }
  }
  
  //// Private Area
  
  //// Internal Reader
  
  //// Main
  
  public static void main(String[] args) {
    System.out.println("Starting");
    try {
      sanityCheck();
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Finished abnormally");
      System.exit(-1);
    }
    System.out.println("Finished");
    System.exit(0);
  }
  
}
