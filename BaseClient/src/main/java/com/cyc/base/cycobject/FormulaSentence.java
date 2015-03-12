package com.cyc.base.cycobject;

/*
 * #%L
 * File: FormulaSentence.java
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

import com.cyc.base.CycAccess;
import com.cyc.base.CycApiException;
import com.cyc.base.CycConnectionException;
import com.cyc.kb.ArgPosition;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nwinant
 */
public interface FormulaSentence extends Formula, CycSentence {

  Object clone();

  /**
   * Adds existential quantification for var in this sentence.
   *
   * @param var
   */
  void existentiallyBind(CycVariable var);

  /**
   * Removes existential quantification for var in this sentence.
   *
   * @param var
   */
  void existentiallyUnbind(CycVariable var);

  /**
   * Returns a collection of terms from cyc that could be plugged into position.
   * This functionality is supported by #$suggestionsForPred... assertions.
   *
   * @param position the position in this sentence for which replacements are sought.
   * @param mt the microtheory from which to perform necessary reasoning.
   * @param cyc the Cyc image that finds the candidate replacement terms.
   * @return a collection of candidate replacement terms.
   * @throws CycConnectionException if there is a problem talking to Cyc
   */
  //@TODO -- Promote to CycFormula?
  List<Object> getCandidateReplacements(ArgPosition position, ELMt mt, CycAccess cyc) throws CycConnectionException;

  /**
   * Return a canonical version of this. If two different sentences yield the same
   * sentence after calling this method, then those two sentences are equal at the EL.
   * In other words, they are merely syntactic variants of the same semantic meaning.
   *
   * @see #getCanonicalElSentence(com.cyc.base.CycAccess, com.cyc.base.cycobject.ELMt, java.lang.Boolean)
   * @param access
   * @return the canonical version of this sentence.
   * @throws CycConnectionException
   */
  FormulaSentence getCanonicalElSentence(CycAccess access) throws CycConnectionException;

  /**
   * @see #getCanonicalElSentence(com.cyc.base.CycAccess, com.cyc.base.cycobject.ELMt, java.lang.Boolean)
   * @param access
   * @param canonicalizeVars
   * @return the canonical version of this sentence.
   * @throws CycConnectionException
   */
  FormulaSentence getCanonicalElSentence(CycAccess access, Boolean canonicalizeVars) throws CycConnectionException;

  /**
   * Return a canonical version of this. If two different sentences yield the same sentence after calling this method (with
   * canonicalizeVars set to True), then those two sentences are equal at the EL.
   * In other words, they are merely syntactic variants of the same semantic meaning.
   *
   * @param access
   * @param mt
   * @param canonicalizeVars
   * @return the canonical version of this sentence.
   * @throws CycConnectionException
   */
  FormulaSentence getCanonicalElSentence(CycAccess access, ELMt mt, Boolean canonicalizeVars) throws CycConnectionException;

  /**
   *
   * @param access
   * @return a simplified version of this sentence
   * @throws CycConnectionException
   */
  CycSentence getEqualsFoldedSentence(CycAccess access) throws CycConnectionException;

  /**
   *
   * @param access
   * @param mt
   * @return a simplified version of this sentence
   * @throws CycConnectionException
   */
  CycSentence getEqualsFoldedSentence(CycAccess access, ELMt mt) throws CycConnectionException;

  /**
   * Return a version of this with all expandable relations expanded into their more
   * verbose forms. For example, this will expand Subcollection functions, as well as other
   * relations that have #$expansion's in the KB.
   *
   * @param access
   * @return the expanded version of this sentence.
   * @throws CycConnectionException
   */
  FormulaSentence getExpandedSentence(CycAccess access) throws CycConnectionException;

  /**
   * Return a version of this with all expandable relations expanded into their more
   * verbose forms. For example, this will expand Subcollection functions, as well as other
   * relations that have #$expansion's in the KB.
   *
   * @param access
   * @param mt the microtheory from which to look for expansions.
   * @return the expanded version of this sentence.
   * @throws CycConnectionException
   */
  FormulaSentence getExpandedSentence(CycAccess access, ELMt mt) throws CycConnectionException;

  /**
   * @see #getNonWffAssertExplanation(com.cyc.base.CycAccess, com.cyc.base.cycobject.ELMt)
   * @param access
   * @return the explanation
   */
  String getNonWffAssertExplanation(CycAccess access);

  /**
   * Returns a string that attempts to explain why this is not well-formed for
   * assertion. Return null if this is well-formed for assertion.
   *
   * @param access
   * @param mt
   * @return An explanation, or null if no problems found.
   */
  String getNonWffAssertExplanation(CycAccess access, ELMt mt);

  /**
   * @see #getNonWffAssertExplanation(com.cyc.base.CycAccess, com.cyc.base.cycobject.ELMt)
   * @param access
   * @return An explanation, or null if no problems found.
   */
  String getNonWffExplanation(CycAccess access);

  /**
   * Returns a string that attempts to explain why this is not well-formed for
   * any purpose. Return null if this is well-formed. If you want to make an assertion with your sentence,
   * use the much more constraining {@link com.cyc.base.cycobject.FormulaSentence#getNonWffAssertExplanation(com.cyc.base.CycAccess, com.cyc.base.cycobject.ELMt) getNonWffAssertExplanation}.
   *
   * @param access
   * @param mt
   * @return An explanation, or null if no problems found.
   */
  String getNonWffExplanation(CycAccess access, ELMt mt);

  /**
   * Suggest mnemonic names for variables in this sentence.
   *
   * @param access
   * @return mapping from variables to suggested new names for them.
   * @throws CycConnectionException
   */
  Map<CycVariable, String> getOptimizedVarNames(CycAccess access) throws CycConnectionException;

  /**
   * Get a simplified version of this sentence.
   *
   * @param access
   * @return the simplified version of this sentence
   * @throws CycConnectionException
   */
  CycSentence getSimplifiedSentence(CycAccess access) throws CycConnectionException;

  /**
   * Get a simplified version of this sentence.
   *
   * @param access
   * @param mt the microtheory to use for semantic requirements and checks
   * @return the simplified version of this sentence
   * @throws CycConnectionException
   */
  CycSentence getSimplifiedSentence(CycAccess access, ELMt mt) throws CycConnectionException;

  /**
   * Is this sentence inconsistent with any of its constraints (e.g. predicate argument constraints)? A false return value does not
   * mean that this meets all the constraints, but it means that it is not inconsistent with them. For example, if an argument position
   * is constrained to be a spec of #$Mammal, and the argument is merely known to be a spec of #$Animal, then the argument does not meet all
   * of the constraints, but there are no constraint violations, and this method should return false.
   *
   * @param access
   * @param mt
   * @return true if violations are found
   */
  boolean hasWffConstraintViolations(CycAccess access, ELMt mt);

  /**
   * Determines whether newTerm is valid at position.
   *
   * @param position the position to be checked
   * @param newTerm the candidate new term
   * @param cyc the Cyc image that issues the judgment
   * @param mt the microtheory to use for constraints
   * @return true iff newTerm is valid at position in mt
   */
  boolean isValidReplacement(ArgPosition position, Object newTerm, ELMt mt, CycAccess cyc);

  /**
   * Insert <tt>toInsert</tt> into this sentence at <tt>argPosition</tt>,
   * using <tt>access</tt> to attempt to unify and rename variables.
   *
   * @param toInsert
   * @param argPosition
   * @param access
   * @return the new sentence
   * @throws CycConnectionException if there is a problem talking to Cyc
   */
  FormulaSentence splice(FormulaSentence toInsert, ArgPosition argPosition, CycAccess access) throws CycConnectionException;

  /**
   * Replace original with replacement in this sentence.
   *
   * @param original
   * @param replacement
   */
  void substituteDestructive(Object original, Object replacement);

  /**
   *
   * @param original
   * @param replacement
   * @return A copy of this sentence, with original replaced with replacement throughout.
   */
  FormulaSentence substituteNonDestructive(Object original, Object replacement);

  /**
   * Returns the result of a tree substitution on the sentence. Note that this
   * leaves the original sentence unmodified.
   *
   * @param access
   * @param substitutions
   * @return The FormulaSentence resulting from the tree substitution.
   * @throws CycApiException
   * @throws CycConnectionException
   */
  FormulaSentence treeSubstitute(CycAccess access, Map<CycObject, Object> substitutions) throws CycApiException, CycConnectionException;
  
  /**
   *
   * @return a deep copy of this sentence.
   */
  @Override
  public FormulaSentence deepCopy();
}
