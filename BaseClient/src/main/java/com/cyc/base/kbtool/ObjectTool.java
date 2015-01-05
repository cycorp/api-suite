package com.cyc.base.kbtool;

/*
 * #%L
 * File: ObjectTool.java
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

import com.cyc.base.CycApiException;
import com.cyc.base.CycConnectionException;
import com.cyc.base.cycobject.CycConstant;
import com.cyc.base.cycobject.FormulaSentence;
import com.cyc.base.cycobject.Fort;
import com.cyc.base.cycobject.CycList;
import com.cyc.base.cycobject.Naut;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.CycSymbol;
import com.cyc.base.cycobject.ELMt;
import com.cyc.base.cycobject.Guid;

/**
 * Tools for creating simple CycObjects, such as constants and lists. To perform 
 * complex assertions, use the {@link com.cyc.base.kbtool.AssertTool}. To lookup
 * facts in the Cyc KB, use the {@link com.cyc.base.kbtool.LookupTool}.
 * 
 * @see com.cyc.base.kbtool.AssertTool
 * @see com.cyc.base.kbtool.LookupTool
 * @author nwinant
 */
public interface ObjectTool {
  
  /**
   * Returns the canonical Heuristic Level Microtheory (HLMT) given a list representation.
   *
   * @param cycList the given CycList NART/NAUT representation
   *
   * @return the canonical Heuristic Level Microtheory (HLMT) given a list representation
   *
   * @throws CycConnectionException if a communication error occurs
   * @throws CycApiException if the Cyc server returns an error
   */
  ELMt canonicalizeHLMT(CycList cycList) throws CycConnectionException, CycApiException;

  /**
   * Returns the canonical Heuristic Level Microtheory (HLMT) given a list representation.
   *
   * @param naut the given NAUT representation
   *
   * @return the canonical Heuristic Level Microtheory (HLMT) given a list representation
   *
   * @throws CycConnectionException if a communication error occurs
   * @throws CycApiException if the Cyc server returns an error
   */
  ELMt canonicalizeHLMT(Naut naut) throws CycConnectionException, CycApiException;
  
  /**
   * Returns the given list with EL NARTS transformed to CycNartI objects.
   *
   * @param cycList the given list
   *
   * @return the given list with EL NARTS transformed to CycNartI objects
   *
   * @throws CycConnectionException if a communications error occurs or the Cyc server cannot be found
   * @throws CycApiException if the Cyc server returns an error
   */
  CycList canonicalizeList(CycList cycList) throws CycConnectionException, CycApiException;
  
  /**
   * Returns a constant whose name differs from the given name only by case. Used because Cyc by
   * default requires constant names to be unique by case.
   *
   * @param name the name used to lookup an existing constant
   *
   * @return a constant whose name differs from the given name only by case, otherwise null if none
   * exists
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  CycConstant constantNameCaseCollision(String name) throws CycConnectionException, CycApiException;
  
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
   * @throws CycConnectionException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  CycList generateDisambiguationPhraseAndTypes(CycList objects) throws CycConnectionException, CycApiException;
  
  /**
   * Returns the Epistimological Level (EL) object represented by the given string.
   *
   * @param string the string which represents a number, quoted string, constant, naut or nart
   *
   * @return the Epistimological Level (EL)object represented by the given string
   *
   * @throws CycConnectionException if a communications error occurs
   * @throws CycConnectionException if the Cyc server cannot be found
   * @throws CycApiException if the Cyc server returns an error
   */
  Object getELCycTerm(String string) throws CycConnectionException, CycConnectionException, CycApiException;
  
  /**
   * Makes a known CycConstant by using its GUID and name, adding it to the cache.
   *
   * @param guidString the known GUID string from which to make the constant
   * @param constantName the known name to associate with the constant
   *
   * @return the complete <tt>CycConstant</tt> if found, otherwise return <tt>null</tt>
   */
  CycConstant makeConstantWithGuidName(String guidString, String constantName);

  /**
   * Makes a known CycConstant by using its GUID and name, adding it to the cache.
   *
   * @param guid the known GUID from which to make the constant
   * @param constantName the known name to associate with the constant
   *
   * @return the complete <tt>CycConstant</tt> if found, otherwise return <tt>null</tt>
   */
  CycConstant makeConstantWithGuidName(Guid guid, String constantName);

  /**
   * Returns a new <tt>CycConstant</tt> object using the constant name, recording bookkeeping
   * information and archiving to the Cyc transcript.
   *
   * @param name Name of the constant. If prefixed with "#$", then the prefix is removed for
   * canonical representation.
   *
   * @return a new <tt>CycConstant</tt> object using the constant name, recording bookkeeping
   * information and archiving to the Cyc transcript
   *
   * @throws CycConnectionException if cyc server host not found on the network
   * @throws CycConnectionException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  CycConstant makeCycConstant(String name) throws CycConnectionException, CycConnectionException, CycApiException;

  /**
   * Returns a new <tt>CycConstant</tt> object using the constant name
   *
   * @param name Name of the constant. If prefixed with "#$", then the prefix is removed for
   * canonical representation.
   * @param bookkeeping If true, bookkeeping data will be added to the KB.
   * @param transcript If true, the operation(s) will be added to the transcript.
   *
   * @return a new <tt>CycConstant</tt> object using the constant name, recording bookkeeping
   * information and archiving to the Cyc transcript
   *
   * @throws CycConnectionException if cyc server host not found on the network
   * @throws CycConnectionException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  CycConstant makeCycConstant(String name, boolean bookkeeping, boolean transcript) throws CycConnectionException, CycConnectionException, CycApiException;

  /**
   * Constructs a new CycList object by parsing a string.
   *
   * @param string the string in CycL external (EL). For example: (#$isa #$Dog #$TameAnimal)
   *
   * @return the new CycList object from parsing the given string
   *
   * @throws CycApiException if the api request results in a cyc server error
   */
  CycList<Object> makeCycList(String string) throws CycApiException;

  /**
   * Constructs a new CycNaut object by parsing a string.
   *
   * @param string the string in CycL external (EL). For example: (#$MotherFn #$GeorgeWashington)
   *
   * @return the new CycNaut object from parsing the given string
   *
   * @throws CycApiException if the api request results in a cyc server error
   */
  Naut makeCycNaut(String string) throws CycApiException;

  /**
   * Constructs a new FormulaSentence object by parsing a string.
   *
   * @param string the string in CycL external (EL). For example: (#$isa #$Dog #$TameAnimal)
   *
   * @return the new FormulaSentence object from parsing the given string
   *
   * @throws CycApiException if the api request results in a cyc server error
   */
  FormulaSentence makeCycSentence(String string) throws CycApiException;

  /**
   * Constructs a new FormulaSentence object by parsing a string from a string that
   * may or may not include all appropriate "#$"s.
   *
   * @param string the string in CycL external (EL). For example: (#$isa Dog TameAnimal)
   *
   * @return the new FormulaSentence object from parsing the given string
   *
   * @throws CycApiException if the api request results in a cyc server error
   */
  FormulaSentence makeCyclifiedSentence(String string) throws CycApiException, CycConnectionException, CycConnectionException;

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
  ELMt makeELMt(Object object) throws CycConnectionException, CycApiException;

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
  ELMt makeELMt(CycObject cycObject) throws CycConnectionException, CycApiException;

  /** @note this is redundant with the Object version, but leaving in for compatability */
  ELMt makeELMt(Fort cycObject);

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
  ELMt makeELMt(String elmtString) throws CycConnectionException, CycApiException;

  /**
   * Returns a new unique <tt>CycConstant</tt> object using the constant start name prefixed by
   * TMP-, recording bookkeeping information and archiving to the Cyc transcript. If
   * the start name begins with #$ that portion of the start name is ignored.
   *
   * @param startName the starting name of the constant which will be made unique using a suffix.
   *
   * @return a new <tt>CycConstant</tt> object using the constant starting name, recording
   * bookkeeping information and archiving to the Cyc transcript
   *
   * @throws CycConnectionException if cyc server host not found on the network
   * @throws CycConnectionException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  CycConstant makeUniqueCycConstant(final String startName) throws CycConnectionException, CycConnectionException, CycApiException;

  /**
   * Returns a new unique <tt>CycConstant</tt> object using the constant start name and prefix,
   * recording bookkeeping information and but without archiving to the Cyc transcript. If the
   * start name begins with #$ that portion of the start name is ignored.
   *
   * @param startName the starting name of the constant which will be made unique using a suffix.
   * @param prefix the prefix
   *
   * @return a new <tt>CycConstant</tt> object using the constant starting name, recording
   * bookkeeping information and archiving to the Cyc transcript
   *
   * @throws CycConnectionException if cyc server host not found on the network
   * @throws CycConnectionException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  CycConstant makeUniqueCycConstant(String startName, String prefix) throws CycConnectionException, CycConnectionException, CycApiException;
  
  /**
   * Renames the given constant.
   *
   * @param cycConstant the constant term to be renamed
   * @param newName the new constant name
   *
   * @throws CycConnectionException if cyc server host not found on the network or the Cyc server cannot be found
   * @throws CycApiException if the api request results in a cyc server error
   */
  void rename(final CycConstant cycConstant, final String newName) throws CycConnectionException, CycApiException;

  /**
   * Renames the given constant.
   *
   * @param cycConstant the constant term to be renamed
   * @param newName the new constant name
   * @param bookkeeping Should bookkeeping data be recorded?
   * @param transcript Should the KB operation(s) be transcripted?
   *
   * @throws CycConnectionException if cyc server host not found on the network or the Cyc server cannot be found
   * @throws CycApiException if the api request results in a cyc server error
   */
  void rename(final CycConstant cycConstant, final String newName, final boolean bookkeeping, final boolean transcript) throws CycConnectionException, CycApiException;

  /**
   * Gets the value of a given KB symbol. This is intended mainly for test case setup.
   *
   * @param cycSymbol the KB symbol which will have a value bound
   *
   * @return the value assigned to the symbol
   *
   * @throws CycConnectionException if cyc server host not found on the network
   * @throws CycConnectionException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  Object getSymbolValue(CycSymbol cycSymbol) throws CycConnectionException, CycConnectionException, CycApiException;
  
  /**
   * Sets a KB symbol to have the specified value. This is intended mainly for test case setup. If
   * the symbol does not exist at the KB, then it will be created and assigned the value.
   *
   * @param cycSymbol the KB symbol which will have a value bound
   * @param value the value assigned to the symbol
   *
   * @throws CycConnectionException if cyc server host not found on the network or the Cyc server cannot be found
   * @throws CycApiException if the api request results in a cyc server error
   */
  void setSymbolValue(CycSymbol cycSymbol, Object value) throws CycConnectionException, CycApiException;

}
