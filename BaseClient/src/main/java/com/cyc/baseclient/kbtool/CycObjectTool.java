package com.cyc.baseclient.kbtool;

/*
 * #%L
 * File: CycObjectTool.java
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

import com.cyc.base.CycAccess;
import com.cyc.base.CycApiException;
import com.cyc.base.CycConnectionException;
import com.cyc.base.cycobject.CycConstant;
import com.cyc.base.cycobject.Fort;
import com.cyc.base.cycobject.CycList;
import com.cyc.base.cycobject.Naut;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.CycSymbol;
import com.cyc.base.cycobject.DenotationalTerm;
import com.cyc.base.cycobject.ELMt;
import com.cyc.base.cycobject.Guid;
import com.cyc.base.kbtool.ObjectTool;
import com.cyc.baseclient.AbstractKBTool;
import com.cyc.baseclient.CycObjectFactory;
import static com.cyc.baseclient.CycObjectFactory.makeCycSymbol;
import com.cyc.baseclient.api.SubLAPIHelper;
import static com.cyc.baseclient.api.SubLAPIHelper.makeSubLStmt;
import com.cyc.baseclient.cycobject.CycArrayList;
import com.cyc.baseclient.cycobject.CycConstantImpl;
import com.cyc.baseclient.cycobject.CycFormulaSentence;
import com.cyc.baseclient.cycobject.CycListParser;
import com.cyc.baseclient.cycobject.NautImpl;
import com.cyc.baseclient.cycobject.ELMtConstant;
import com.cyc.baseclient.cycobject.ELMtCycNaut;
import com.cyc.baseclient.cycobject.ELMtNart;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import static com.cyc.base.cycobject.CycConstant.HD;
import com.cyc.base.cycobject.Nart;
import java.io.IOException;
import java.net.UnknownHostException;

/**
 *Tools for creating simple CycObjects, such as constants and lists. To perform 
 * complex assertions, use the {@link com.cyc.baseclient.kbtool.CycAssertTool}. To lookup
 * facts in the Cyc KB, use the {@link com.cyc.baseclient.kbtool.CycLookupTool}.
 * 
 * @see com.cyc.baseclient.kbtool.CycAssertTool
 * @see com.cyc.baseclient.kbtool.CycLookupTool
 * @author nwinant
 */
public class CycObjectTool extends AbstractKBTool implements ObjectTool {
  
  public CycObjectTool(CycAccess client) {
    super(client);
  }
  
  
  // Public
  
  /**
   * Returns the canonical Heuristic Level Microtheory (HLMT) given a list representation.
   *
   * @param cycList the given CycArrayList NART/NAUT representation
   *
   * @return the canonical Heuristic Level Microtheory (HLMT) given a list representation
   *
   * @throws IOException if a communication error occurs
   * @throws CycApiException if the Cyc server returns an error
   */
  @Override
  public ELMt canonicalizeHLMT(CycList cycList)
          throws CycConnectionException, CycApiException {
    ELMt mt;
    String command = makeSubLStmt("canonicalize-hlmt", cycList);
    final CycObject result = getConverse().converseCycObject(command);
    if (result instanceof DenotationalTerm) {
      mt = makeELMt(result);
    } else if (result instanceof List) {
      mt = ELMtCycNaut.makeELMtCycNaut((List) result);
    } else {
      throw new CycApiException("Can't canonicalize " + cycList);
    }
    return mt;
  }

  /**
   * Returns the canonical Heuristic Level Microtheory (HLMT) given a list representation.
   *
   * @param naut the given NAUT representation
   *
   * @return the canonical Heuristic Level Microtheory (HLMT) given a list representation
   *
   * @throws IOException if a communication error occurs
   * @throws CycApiException if the Cyc server returns an error
   */
  @Override
  public ELMt canonicalizeHLMT(Naut naut)
          throws CycConnectionException, CycApiException {
    return canonicalizeHLMT(naut.toCycList());
  }
  
  /**
   * Returns the given list with EL NARTS transformed to Nart objects.
   *
   * @param cycList the given list
   *
   * @return the given list with EL NARTS transformed to Nart objects
   *
   * @throws IOException if a communications error occurs
   * @throws UnknownHostException if the Cyc server cannot be found
   * @throws CycApiException if the Cyc server returns an error
   */
  @Override
  public CycArrayList canonicalizeList(CycList cycList)
          throws CycConnectionException, CycApiException {
    CycArrayList canonicalList = new CycArrayList();
    Iterator iter = cycList.iterator();

    while (iter.hasNext()) {
      Object obj = iter.next();

      if (obj instanceof CycArrayList) {
        canonicalList.add(getHLCycTerm(((CycArrayList) obj).cyclify()));
      } else if (obj instanceof Nart) {
        canonicalList.add(getHLCycTerm(((Nart) obj).cyclify()));
      } else {
        canonicalList.add(obj);
      }
    }

    return canonicalList;
  }
  
  /**
   * Returns a constant whose name differs from the given name only by case. Used because Cyc by
   * default requires constant names to be unique by case.
   *
   * @param name the name used to lookup an existing constant
   *
   * @return a constant whose name differs from the given name only by case, otherwise null if none
   * exists
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public CycConstant constantNameCaseCollision(String name)
          throws CycConnectionException, CycApiException {
    Object object = getConverse().converseObject(
            "(constant-name-case-collision \"" + name + "\")");

    if (object instanceof CycConstant) {
      return (CycConstant) object;
    } else {
      return null;
    }
  }
  
    /**
   * Returns a list of disambiguation expressions, corresponding to each of the terms in the given
   * list of objects.
   * <pre>
   * (GENERATE-DISAMBIGUATION-PHRASES-AND-TYPES (QUOTE (#$Penguin #$PittsburghPenguins)))
   * ==>
   * ((#$Penguin "penguin" #$Bird "bird")
   *  (#$PittsburghPenguins "the Pittsburgh Penguins" #$IceHockeyTeam "ice hockey team"))
   * </pre>
   *
   * @param objects the list of terms to be disambiguated
   *
   * @return a list of disambiguation expressions, corresponding to each of the terms in the given
   * list of objects
   *
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public CycList generateDisambiguationPhraseAndTypes(CycList objects)
          throws CycConnectionException, CycApiException {
    String command = makeSubLStmt(
            makeCycSymbol("generate-disambiguation-phrases-and-types"), objects);
    return getConverse().converseList(command);
  }
  
  /**
   * Returns the Heuristic Level (HL) object represented by the given string.
   *
   * @param string the string which represents a number, quoted string, constant, naut or nart
   *
   * @return the Heuristic Level (HL) object represented by the given string
   *
   * @throws IOException if a communications error occurs
   * @throws UnknownHostException if the Cyc server cannot be found
   * @throws CycApiException if the Cyc server returns an error
   */
  public Object getHLCycTerm(String string)
          throws CycConnectionException, com.cyc.base.CycApiException {
    return getConverse().converseObject("(canonicalize-term  '" + string + ")");
  }

  /**
   * Returns the Epistimological Level (EL) object represented by the given string.
   *
   * @param string the string which represents a number, quoted string, constant, naut or nart
   *
   * @return the Epistimological Level (EL)object represented by the given string
   *
   * @throws IOException if a communications error occurs
   * @throws UnknownHostException if the Cyc server cannot be found
   * @throws CycApiException if the Cyc server returns an error
   */
  @Override
  public Object getELCycTerm(String string)
          throws CycConnectionException, com.cyc.base.CycApiException {
    return getConverse().converseObject("(identity '" + string + ")");
  }
  
  /**
   * Makes a known CycConstantImpl by using its GUID and name, adding it to the cache.
   *
   * @param guidString the known GUID string from which to make the constant
   * @param constantName the known name to associate with the constant
   *
   * @return the complete <tt>CycConstantImpl</tt> if found, otherwise return <tt>null</tt>
   */
  @Override
  public CycConstantImpl makeConstantWithGuidName(String guidString,
          String constantName) {
    return makeConstantWithGuidName(CycObjectFactory.makeGuid(
            guidString),
            constantName);
  }

  /**
   * Makes a known CycConstantImpl by using its GUID and name, adding it to the cache.
   *
   * @param guid the known GUID from which to make the constant
   * @param constantName the known name to associate with the constant
   *
   * @return the complete <tt>CycConstantImpl</tt> if found, otherwise return <tt>null</tt>
   */
  @Override
  public CycConstantImpl makeConstantWithGuidName(Guid guid,
          String constantName) {
    CycConstantImpl answer = CycObjectFactory.getCycConstantCacheByGuid(guid);
    if (answer != null) {
      return answer;
    }
    answer = new CycConstantImpl(constantName, guid);
    CycObjectFactory.addCycConstantCache(answer);
    return answer;
  }

  /**
   * Returns a new <tt>CycConstantImpl</tt> object using the constant name, recording bookkeeping
   * information and archiving to the Cyc transcript.
   *
   * @param name Name of the constant. If prefixed with "#$", then the prefix is removed for
   * canonical representation.
   *
   * @return a new <tt>CycConstantImpl</tt> object using the constant name, recording bookkeeping
   * information and archiving to the Cyc transcript
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public CycConstantImpl makeCycConstant(String name) throws CycConnectionException, CycApiException {
    return makeCycConstant(name, true, true);
  }

  /**
   * Returns a new <tt>CycConstantImpl</tt> object using the constant name
   *
   * @param name Name of the constant. If prefixed with "#$", then the prefix is removed for
   * canonical representation.
   * @param bookkeeping If true, bookkeeping data will be added to the KB.
   * @param transcript If true, the operation(s) will be added to the transcript.
   *
   * @return a new <tt>CycConstantImpl</tt> object using the constant name, recording bookkeeping
   * information and archiving to the Cyc transcript
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public CycConstantImpl makeCycConstant(String name, boolean bookkeeping, boolean transcript)
          throws CycConnectionException, CycApiException {
    String constantName = name;

    if (constantName.startsWith(HD)) {
      constantName = constantName.substring(2);
    }

    CycConstantImpl cycConstant = getConstantByName_inner(name);

    if (cycConstant != null) {
      return cycConstant;
    }

    final String fn = (transcript) ? "ke-create-now" : "fi-create-int";
    String command = "(" + fn + " \"" + constantName + "\")";
    if (bookkeeping) {
      command = getConverse().wrapBookkeeping(command);
    }
    Object object = getConverse().converseObject(command);

    if (object instanceof CycConstantImpl) {
      cycConstant = (CycConstantImpl) object;
    } else {
      throw new com.cyc.base.CycApiException("Cannot create new constant for " + name);
    }
    CycObjectFactory.addCycConstantCache(cycConstant);
    if (getCurrentTransaction() != null) {
      getCurrentTransaction().noteCreation(cycConstant);
    }
    return cycConstant;
  }

  /**
   * Constructs a new CycArrayList object by parsing a string.
   *
   * @param string the string in CycL external (EL). For example: (#$isa #$Dog #$TameAnimal)
   *
   * @return the new CycArrayList object from parsing the given string
   *
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public CycArrayList<Object> makeCycList(String string)
          throws CycApiException {
    return (new CycListParser(this.getCyc())).read(string);
  }

  /**
   * Constructs a new Naut object by parsing a string.
   *
   * @param string the string in CycL external (EL). For example: (#$MotherFn #$GeorgeWashington)
   *
   * @return the new Naut object from parsing the given string
   *
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public Naut makeCycNaut(String string) throws CycApiException {
    return new NautImpl(makeCycList_inner(string));
  }

  /**
   * Constructs a new CycFormulaSentence object by parsing a string.
   *
   * @param string the string in CycL external (EL). For example: (#$isa #$Dog #$TameAnimal)
   *
   * @return the new CycFormulaSentence object from parsing the given string
   *
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public CycFormulaSentence makeCycSentence(String string) throws CycApiException {
    return new CycFormulaSentence(makeCycList_inner(string));
  }

  /**
   * Constructs a new CycFormulaSentence object by parsing a string from a string that
   * may or may not include all appropriate "#$"s.
   *
   * @param string the string in CycL external (EL). For example: (#$isa Dog TameAnimal)
   *
   * @return the new CycFormulaSentence object from parsing the given string
   * 
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public CycFormulaSentence makeCyclifiedSentence(String string)
          throws CycApiException, CycConnectionException {
    String cyclified = getCyc().cyclifyString(string);
    return makeCycSentence(cyclified);
  }

  /**
   * Constructs a new ELMt object by the given CycObject.
   *
   * @param object the given CycObject from which the ELMt is derived
   *
   * @return the new ELMt object by the given CycObject
   *
   * @throws CycConnectionException if a communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   * @throws IllegalArgumentException if the cycObject is not the correct type of thing for
   * making into an ELMt
   */
  @Override
  public ELMt makeELMt(Object object)
          throws CycConnectionException, CycApiException {
    if (object instanceof ELMt) {
      return (ELMt) object;
    } else if (object instanceof CycArrayList) {
      return canonicalizeHLMT((CycArrayList) object);
    } else if (object instanceof Naut) {
      return canonicalizeHLMT((Naut) object);
    } else if (object instanceof CycConstantImpl) {
      return ELMtConstant.makeELMtConstant((CycConstantImpl) object);
    } else if (object instanceof Nart) {
      return ELMtNart.makeELMtNart((Nart) object);
    } else if (object instanceof String) {
      String elmtString = object.toString().trim();
      if (elmtString.startsWith("(")) {
        @SuppressWarnings("unchecked")
        CycList<Object> elmtCycList = makeCycList_inner(elmtString);
        return makeELMt_inner(elmtCycList);
      } else {
        return makeELMt(getKnownConstantByName_inner(elmtString));
      }
    } else {
      throw new IllegalArgumentException("Can't make an ELMt from " + object
              + " class: " + object.getClass().getSimpleName());
    }
  }

  /**
   * Constructs a new ELMt object by the given CycObject.
   *
   * @param cycObject the given CycObject from which the ELMt is derived
   *
   * @return the new ELMt object by the given CycObject
   *
   * @note this is redundant with the Object version, but leaving in for compatability
   *
   * @throws CycConnectionException if a communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   * @throws IllegalArgumentException if the cycObject is not the correct type of thing for
   * making into an ELMt
   */
  @Override
  public ELMt makeELMt(CycObject cycObject)
          throws CycConnectionException, CycApiException {
    if (cycObject instanceof ELMt) {
      return (ELMt) cycObject;
    } else if (cycObject instanceof CycArrayList) {
      return canonicalizeHLMT((CycArrayList) cycObject);
    } else if (cycObject instanceof Naut) {
      return canonicalizeHLMT((Naut) cycObject);
    } else if (cycObject instanceof Fort) {
      return makeELMt((Fort) cycObject);
    } else {
      throw new IllegalArgumentException("Can't make an ELMt from " + cycObject
              + " class: " + cycObject.getClass().getSimpleName());
    }
  }

  /**
   * Constructs a new ELMt object by the given CycObject.
   * 
   * @param cycObject the given Fort from which the ELMt is derived
   * @return the new ELMt object by the given CycObject
   * @note this is redundant with the Object version, but leaving in for compatability
   */
  @Override
  public ELMt makeELMt(Fort cycObject) {
    ELMt result = null;
    if (cycObject instanceof CycConstantImpl) {
      result = ELMtConstant.makeELMtConstant((CycConstantImpl) cycObject);
    } else if (cycObject instanceof Nart) {
      result = ELMtNart.makeELMtNart((Nart) cycObject);
    } else {
      throw new IllegalArgumentException("CycObject: " + cycObject.cyclify()
              + "is not a valid ELMt.");
    }
    return result;
  }

  /**
   * Constructs a new ELMt object by the given String.
   *
   * @param elmtString the given CycObject from which the ELMt is derived
   *
   * @return the new ELMt object by the given CycObject
   *
   * @note this is redundant with the Object version, but leaving in for compatability
   *
   * @throws CycConnectionException if a communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public ELMt makeELMt(String elmtString)
          throws CycConnectionException, CycApiException {
    elmtString = elmtString.trim();
    if (elmtString.startsWith("(")) {
      CycList elmtCycList = makeCycList_inner(elmtString);
      return makeELMt_inner(elmtCycList);
    } else {
      return makeELMt(getKnownConstantByName_inner(elmtString));
    }
  }

  /**
   * Returns a new unique <tt>CycConstantImpl</tt> object using the constant start name prefixed by
   * TMP-, recording bookkeeping information and archiving to the Cyc transcript. If
   * the start name begins with #$ that portion of the start name is ignored.
   *
   * @param startName the starting name of the constant which will be made unique using a suffix.
   *
   * @return a new <tt>CycConstantImpl</tt> object using the constant starting name, recording
   * bookkeeping information and archiving to the Cyc transcript
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public CycConstant makeUniqueCycConstant(final String startName)
          throws CycConnectionException, CycApiException {
    String constantName = startName;

    if (constantName.startsWith(HD)) {
      constantName = constantName.substring(2);
    }
    String suffix = "";
    int suffixNum = 0;
    while (true) {
      String command = "(constant-name-available \"" + startName + suffix + "\")";
      if (getConverse().converseBoolean(command)) {
        break;
      }
      if (suffix.length() == 0) {
        suffixNum = ((int) (9 * Math.random())) + 1;
      } else {
        suffixNum = (suffixNum * 10) + ((int) (10 * Math.random()));
      }
      suffix = String.valueOf(suffixNum);
    }
    return makeCycConstant(startName + suffix);
  }

  /**
   * Returns a new unique <tt>CycConstantImpl</tt> object using the constant start name and prefix,
   * recording bookkeeping information and but without archiving to the Cyc transcript. If the
   * start name begins with #$ that portion of the start name is ignored.
   *
   * @param startName the starting name of the constant which will be made unique using a suffix.
   * @param prefix the prefix
   *
   * @return a new <tt>CycConstantImpl</tt> object using the constant starting name, recording
   * bookkeeping information and archiving to the Cyc transcript
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public CycConstantImpl makeUniqueCycConstant(String startName, String prefix)
          throws CycConnectionException, CycApiException {
    String constantName = startName;

    if (constantName.startsWith(HD)) {
      constantName = constantName.substring(2);
    }

    String command = getConverse().wrapBookkeeping("(gentemp-constant \"" + constantName + "\" \"" + prefix
            + "\")");
    CycConstantImpl cycConstant = (CycConstantImpl) getConverse().converseObject(
            command);
    CycObjectFactory.addCycConstantCache(cycConstant);
    if (getCurrentTransaction() != null) {
      getCurrentTransaction().noteCreation(cycConstant);
    }
    return cycConstant;
  }
  
  /** 
   * Record the information that <tt>focalTerm</tt> is known to have a fact sheet accessible to this CycClient 
   */
  public void noteTermHasPrecachedFactSheet(final CycObject focalTerm) {
    termsKnownToHavePrecachedFactSheets.add(focalTerm);
  }
  
  /**
   * @return true iff <tt>focalTerm</tt> is known to have a fact sheet accessible to this CycClient 
   */
  public boolean termKnownToHavePrecachedFactSheet(final CycObject focalTerm) {
    return termsKnownToHavePrecachedFactSheets.contains(focalTerm);
  }
  
  public CycList phraseStructureParse(String str)
          throws CycConnectionException, CycApiException {
    String command = makeSubLStmt(
            makeCycSymbol("ps-get-cycls-for-phrase"), str);
    return getConverse().converseList(command);
  }

  /**
   * Renames the given constant.
   *
   * @param cycConstant the constant term to be renamed
   * @param newName the new constant name
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public synchronized void rename(final CycConstant cycConstant,
          final String newName)
          throws CycConnectionException, CycApiException {
    rename(cycConstant, newName, true, true);
  }

  /**
   * Renames the given constant.
   *
   * @param cycConstant the constant term to be renamed
   * @param newName the new constant name
   * @param bookkeeping Should bookkeeping data be recorded?
   * @param transcript Should the KB operation(s) be transcripted?
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public synchronized void rename(final CycConstant cycConstant,
          final String newName, final boolean bookkeeping,
          final boolean transcript)
          throws CycConnectionException, CycApiException {
    final String fn = (transcript) ? "ke-rename-now" : "rename-constant";
    String command = "(" + fn + " " + cycConstant.stringApiValue() + "\"" + newName + "\")";
    if (bookkeeping) {
      command = getConverse().wrapBookkeeping(command);
    }
    Object result = getConverse().converseObject(command);
    if (result.equals(CycObjectFactory.nil)) {
      throw new CycApiException(
              newName + " is an invalid new name for " + cycConstant.cyclify());
    }
    CycObjectFactory.removeCaches(cycConstant);
    cycConstant.setName(newName);
    CycObjectFactory.addCycConstantCache(cycConstant);
  }
  
  /**
   * Gets the value of a given KB symbol. This is intended mainly for test case setup.
   *
   * @param cycSymbol the KB symbol which will have a value bound
   *
   * @return the value assigned to the symbol
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public Object getSymbolValue(CycSymbol cycSymbol)
          throws CycConnectionException, CycApiException {
    return getConverse().converseObject("(symbol-value " + cycSymbol.stringApiValue() + ")");
  }
  /**
   * Sets a KB symbol to have the specified value. This is intended mainly for test case setup. If
   * the symbol does not exist at the KB, then it will be created and assigned the value.
   *
   * @param cycSymbol the KB symbol which will have a value bound
   * @param value the value assigned to the symbol
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void setSymbolValue(CycSymbol cycSymbol, Object value)
          throws CycConnectionException, com.cyc.base.CycApiException {
    getConverse().converseVoid(makeSubLStmt("csetq", new SubLAPIHelper.AsIsTerm(cycSymbol),
            value));
  }
  
 
  // Internal
  
  final private Set<CycObject> termsKnownToHavePrecachedFactSheets = new HashSet<CycObject>();
}
