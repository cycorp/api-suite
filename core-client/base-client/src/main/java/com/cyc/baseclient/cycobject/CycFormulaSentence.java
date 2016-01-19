package com.cyc.baseclient.cycobject;

/*
 * #%L
 * File: CycFormulaSentence.java
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
//// External Imports
import com.cyc.base.exception.BaseClientRuntimeException;
import com.cyc.base.cycobject.ElMt;
import com.cyc.base.cycobject.CycSentence;
import com.cyc.base.CycAccess;
import com.cyc.base.exception.CycConnectionException;
import com.cyc.kb.ArgPosition;
import com.cyc.base.cycobject.FormulaSentence;
import com.cyc.base.cycobject.CycList;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;
import com.cyc.base.exception.CycApiException;
import com.cyc.baseclient.CycObjectFactory;
import com.cyc.baseclient.connection.SublApiHelper;
import static com.cyc.baseclient.connection.SublApiHelper.makeSubLStmt;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.CycVariable;
import com.cyc.base.cycobject.Formula;
import com.cyc.base.cycobject.NonAtomicTerm;
import com.cyc.baseclient.CommonConstants;
import com.cyc.session.compatibility.CycSessionRequirementList;
import com.cyc.session.compatibility.NotOpenCycRequirement;
import com.cyc.session.exception.OpenCycUnsupportedFeatureException;

/**
 * <P>
 CycSentence is designed to be an object that represents Sentences composed of
 a truth function and a list of arguments
 *
 * @author baxter, Jul 6, 2009, 10:05:43 AM
 * @version $Id: CycFormulaSentence.java 163450 2016-01-08 21:40:25Z nwinant $
 *
 * @todo make it implement CycLFormula, or get rid of CycLFormula, as
 * appropriate
 */
public class CycFormulaSentence extends FormulaImpl implements CycSentence, FormulaSentence {

  public static final CycSessionRequirementList<OpenCycUnsupportedFeatureException> ADVANCED_SENTENCE_REQUIREMENTS = CycSessionRequirementList.fromList(
          NotOpenCycRequirement.NOT_OPENCYC
  );
  
  /**
   * Create and return a new CycSentence whose arguments are terms. CycArrayList
 arguments will be converted to CycNauts or CycSentences.
   *
   * @param terms
   */
  public CycFormulaSentence(Iterable<? extends Object> terms) {
    super(terms);
  }

  /**
   * Create and return a new CycSentence whose arguments are terms. CycArrayList
 arguments will be converted to CycNauts or CycSentences.
   *
   * @param terms
   * @return a new FormulaSentence
   * @todo add more documentation
   */
  public static FormulaSentence makeCycFormulaSentence(
          Iterable<? extends Object> terms) {
    return new CycFormulaSentence(terms);
  }

  /**
   * Build a new CycSentence from terms.
   *
   * @param terms
   * @return a new FormulaSentence
   */
  public static FormulaSentence makeCycFormulaSentence(Object... terms) {
    final CycFormulaSentence newSentence = new CycFormulaSentence();
    for (final Object arg : terms) {
      newSentence.addArg(arg);
    }
    return newSentence;
    //return new CycFormulaSentence(CycArrayList.makeCycList(terms));
  }

  /**
   * Build a new CycSentence from a String. This will add #$ wherever necessary.
   *
   * @param cyc
   * @param cycl
   * @return a new FormulaSentence
   * @throws CycApiException
   * @throws IOException
   */
  public static FormulaSentence makeCycSentence(CycAccess cyc, String cycl)
          throws CycApiException, CycConnectionException {
    return new CycFormulaSentence(cyc.getObjectTool().makeCycList(cyc.cyclifyString(cycl)));
  }

  /**
   * Make a new conjunction conjoining the arguments.
   *
   * @see #isConjunction()
   * @param conjuncts
   * @return a new FormulaSentence from conjunction of input sentences
   */
  public static FormulaSentence makeConjunction(
          FormulaSentence... conjuncts) {
    return makeConjunction(Arrays.asList(conjuncts));
  }

  /**
   * Make a new conjunction from the elements of conjuncts.
   *
   * @see #isConjunction()
   * @param conjuncts
   * @return a new FormulaSentence from conjunction of input sentences
   */
  public static FormulaSentence makeConjunction(
          Iterable<FormulaSentence> conjuncts) {
    final CycFormulaSentence newSentence = (CycFormulaSentence) makeCycFormulaSentence(CommonConstants.AND);
    for (final Object conjunct : conjuncts) {
      newSentence.addArg(conjunct);
    }
    return newSentence;
  }

  /**
   * Make a new disjunction from the elements of conjuncts.
   *
   * @param conjuncts
   * @return a new FormulaSentence from disjunction of input sentences
   */
  public static FormulaSentence makeDisjunction(
          Iterable<FormulaSentence> conjuncts) {
    final CycFormulaSentence newSentence = (CycFormulaSentence) makeCycFormulaSentence(CommonConstants.OR);
    for (final Object conjunct : conjuncts) {
      newSentence.addArg(conjunct);
    }
    return newSentence;
  }

  /**
   * Make a new conditional sentence with the specified antecedent and
 consequent.
   *
   * @see #isConditionalSentence()
   * @param antecedent
   * @param consequent
   * @return a new conditional FormulaSentence
   */
  public static FormulaSentence makeConditional(FormulaSentence antecedent,
          FormulaSentence consequent) {
    return makeCycFormulaSentence(CommonConstants.IMPLIES, antecedent, consequent);
  }

  /**
   * Make a negated form of the specified sentence.
   *
   * @param sentence
   * @return a new negated FormulaSentence
   */
  public static FormulaSentence makeNegation(FormulaSentence sentence) {
    return makeCycFormulaSentence(CommonConstants.NOT, sentence);
  }

  /**
   * Convert obj to a CycFormulaSentence if it looks like it could be one.
   *
   * @param obj
   * @return a FormulaSentence if possible
   */
  static public Object convertIfPromising(final Object obj) {
    if (obj instanceof List && !(obj instanceof CycFormulaSentence)) {
      final List<Object> termAsList = (List) obj;
      if (!termAsList.isEmpty() && termAsList.get(0) instanceof CycConstantImpl) {
        final CycConstantImpl arg0 = (CycConstantImpl) termAsList.get(0);
        if (Character.isLowerCase(arg0.getName().charAt(0))) {
          return new CycFormulaSentence(termAsList);
        }
      }
    }
    return obj;
  }

  private CycFormulaSentence() {
  }

  /**
   *
   * @return true iff this is a conditional sentence.
   */
  @Override
  public boolean isConditionalSentence() {
    if (CommonConstants.IMPLIES.equals(getOperator())) {
      return true;
    } else if (isConjunction() && getArity() == 1 && ((CycFormulaSentence) getArg(
            1)).isConditionalSentence()) {
      return true;
    } else {
      return false;
    }
  }

  /**
   *
   * @return true iff this is a conjunction.
   */
  @Override
  public boolean isConjunction() {
    return (CommonConstants.AND.equals(getOperator()));
  }

  /**
   *
   * @return true iff this is negated.
   */
  @Override
  public boolean isNegated() {
    return (CommonConstants.NOT.equals(getOperator()));
  }

  /**
   *
   * @return true iff the operator of this sentence is a logical operator.
   * @see #getOperator()
   */
  @Override
  public boolean isLogicalConnectorSentence() {
    return isLogicalOperatorFort(getOperator());
  }

  /**
   *
   * @return true iff this is an existential sentence.
   */
  @Override
  public boolean isExistential() {
    return CommonConstants.THERE_EXISTS.equals(getOperator());
  }

  /**
   * Adds existential quantification for var in this sentence.
   *
   * @param var
   */
  public void existentiallyBind(CycVariable var) {
    synchronized (args) {
      final Object oldArgs = this.clone();
      args.clear();
      args.add(oldArgs);
      args.add(0, var);
      args.add(0, CommonConstants.THERE_EXISTS);
    }
  }

  /**
   * Removes existential quantification for var in this sentence.
   *
   * @param var
   */
  public void existentiallyUnbind(CycVariable var) {
    synchronized (args) {
      final Set<ArgPosition> existentialPositions = new HashSet<ArgPosition>(1);
      for (final ArgPositionTrackingTreeWalker argWalker = new ArgPositionTrackingTreeWalker();
              argWalker.hasNext();) {
        if (CommonConstants.THERE_EXISTS.equals(argWalker.next()) && argWalker.getCurrentArgPosition().last() == 0) {
          final ArgPosition quantPosition = argWalker.getCurrentArgPosition();
          final ArgPosition parentPosition = quantPosition.deepCopy().toParent();
          final ArgPosition varPosition = parentPosition.deepCopy().extend(1);
          if (var.equals(getSpecifiedObject(varPosition))) {
            existentialPositions.add(parentPosition);
          }
        }
      }
      for (final ArgPosition existentialPosition : existentialPositions) {
        setSpecifiedObject(existentialPosition, getSpecifiedObject(
                existentialPosition.deepCopy().extend(2)));
      }
    }
  }

  /**
   *
   * @return true iff this is a universally quantified sentence.
   */
  @Override
  public boolean isUniversal() {
    return CommonConstants.FOR_ALL.equals(getOperator());
  }

  private static boolean isLogicalOperatorFort(final Object obj) {
    return (CommonConstants.LOGICAL_OPERATOR_FORTS.contains(obj));
  }

  /**
   * Suggest mnemonic names for variables in this sentence.
   *
   * @param access
   * @return mapping from variables to suggested new names for them.
   * @throws UnknownHostException
   * @throws IOException
   */
  public Map<CycVariable, String> getOptimizedVarNames(CycAccess access) throws CycConnectionException {
    Map<CycVariable, String> retMap = new HashMap<CycVariable, String>();
    String command = makeSubLStmt(PPH_OPTIMIZED_NAMES_FOR_VARIABLES, this);
    @SuppressWarnings("unchecked")
    CycList<CycObject> resultList = access.converse().converseList(command);
    for (CycObject singleValue : resultList) {
      if (singleValue instanceof CycArrayList) {
        final CycArrayList dottedPair = (CycArrayList) singleValue;
        if (dottedPair.first() instanceof CycVariableImpl) {
          if (dottedPair.getDottedElement() instanceof String) {
            retMap.put((CycVariableImpl) dottedPair.first(),
                    (String) (dottedPair.getDottedElement()));
          }
        } else {
          optimizedVarProblemResult(singleValue);
        }
      } else {
        optimizedVarProblemResult(singleValue);
      }
    }
    return retMap;
  }

  private void optimizedVarProblemResult(CycObject singleValue) throws BaseClientRuntimeException {
    throw new BaseClientRuntimeException(
            "Unable to interpret " + singleValue + " as an optimized variable name.");
  }

  /**
   * Insert <tt>toInsert</tt> into this sentence at <tt>argPosition</tt>, using
   * <tt>access</tt> to attempt to unify and rename variables.
   *
   * @param toInsert
   * @param argPosition
   * @param access
   * @return the new sentence
   * @throws com.cyc.base.exception.CycConnectionException
   * @throws com.cyc.session.exception.OpenCycUnsupportedFeatureException
   */
  @Override
  public CycFormulaSentence splice(FormulaSentence toInsert,
          ArgPosition argPosition,
          CycAccess access) throws CycConnectionException, OpenCycUnsupportedFeatureException {
    ADVANCED_SENTENCE_REQUIREMENTS.throwRuntimeExceptionIfIncompatible();
    final String command = SublApiHelper.makeSubLStmt(
            "combine-formulas-at-position", this, toInsert, argPosition);
    final List result = access.converse().converseList(command);
    return new CycFormulaSentence(result);
  }

  /**
   * Returns a COLLECTION of terms from cyc that could be plugged into position.
   * This functionality is supported by #$suggestionsForPred... assertions.
   *
   * @param position the position in this sentence for which replacements are
 sought.
   * @param mt the microtheory from which to perform necessary reasoning.
   * @param cyc the Cyc image that finds the candidate replacement terms.
   * @return a COLLECTION of candidate replacement terms.
   * @throws IOException if there is a problem talking to Cyc
   */
  //@TODO -- Promote to FormulaImpl?
  @Override
  public List<Object> getCandidateReplacements(ArgPosition position,
          ElMt mt, CycAccess cyc) throws CycConnectionException, OpenCycUnsupportedFeatureException {
    ADVANCED_SENTENCE_REQUIREMENTS.throwRuntimeExceptionIfIncompatible();
    final String command = SublApiHelper.makeSubLStmt(
            "candidate-replacements-for-arg", this, position, mt);
    return cyc.converse().converseList(command);
  }

  /**
   * Determines whether newTerm is valid at position.
   *
   * @param position the position to be checked
   * @param newTerm the candidate new term
   * @param cyc the Cyc image that issues the judgment
   * @param mt the microtheory to use for constraints
   * @return true iff newTerm is valid at position in mt
   */
  public boolean isValidReplacement(ArgPosition position, Object newTerm,
          ElMt mt,
          CycAccess cyc) {
    final CycFormulaSentence subbed = this.deepCopy();
    subbed.setSpecifiedObject(position, newTerm);
    return subbed.getNonWffExplanation(cyc, mt) == null;
  }

  /**
   *
   * @param access
   * @return a simplified CycL sentence
   * @throws CycConnectionException
   */
  public CycSentence getEqualsFoldedSentence(CycAccess access) throws CycConnectionException {
    return getEqualsFoldedSentence(access, CommonConstants.CURRENT_WORLD_DATA_MT);
  }

  /**
   *
   * @param access
   * @param mt
   * @return a simplified CycL sentence
   * @throws com.cyc.base.exception.CycConnectionException
   */
  @Override
  public CycSentence getEqualsFoldedSentence(CycAccess access, ElMt mt) throws CycConnectionException {
    String command = null;
    try {
      command = "(with-inference-mt-relevance " + mt.stringApiValue() + " (fold-equals " + this.stringApiValue() + "))";
    } catch (Exception ex) {
      throw (new BaseClientRuntimeException(ex));
    }
    Object rawResult = access.converse().converseObject(command);
    CycSentence result;
    if (rawResult instanceof CycArrayList) {
      result = new CycFormulaSentence((CycArrayList) rawResult);
    } else if (rawResult instanceof CycConstantImpl) {
      result = new CycConstantSentence((CycConstantImpl) rawResult);
    } else {
      throw new CycApiException(
              "getEqualsFoldedSentence returned " + rawResult
              + ", which is not a CycSentence.\nOriginal input: " + this.toString());
    }
    //System.out.println("FOLDED TO: "+result.toString());
    return result;
  }

  /**
   * Get a simplified version of this sentence.
   *
   * @param access
   * @return a simplified version of this sentence
   * @throws CycConnectionException
   * @throws OpenCycUnsupportedFeatureException
   */
  @Override
  public CycSentence getSimplifiedSentence(CycAccess access) throws CycConnectionException, OpenCycUnsupportedFeatureException {
    return getSimplifiedSentence(access, getDefaultSimplifierMt());
  }

  static synchronized ElMt getDefaultSimplifierMt() {
    if (simplifierMt == null && CommonConstants.MT_SPACE != null) {
      simplifierMt = ElMtCycNaut.makeElMtCycNaut(Arrays.asList(CommonConstants.MT_SPACE,
              CommonConstants.CURRENT_WORLD_DATA_MT, CommonConstants.ANYTIME_PSC));
    }
    return simplifierMt;
  }

  /**
   * Get a simplified version of this sentence.
   *
   * @param access
   * @param mt the microtheory to use for semantic requirements and checks
   * @return a simplified version of this sentence
   * @throws CycConnectionException
   * @throws OpenCycUnsupportedFeatureException
   */
  @Override
  public CycSentence getSimplifiedSentence(CycAccess access, ElMt mt) throws CycConnectionException, OpenCycUnsupportedFeatureException {
    ADVANCED_SENTENCE_REQUIREMENTS.throwRuntimeExceptionIfIncompatible();
    String command = null;
    try {
      command = "(with-inference-mt-relevance " + mt.stringApiValue() + " (simplify-cycl-sentence (fold-equals "
              + this.stringApiValue() + ")))";
      //System.out.println("TRYING TO SIMPLIFY WITH:"+ command);
    } catch (Exception ex) {
      throw (new BaseClientRuntimeException(ex));
    }
    Object rawResult = access.converse().converseObject(command);
    CycSentence result;
    if (rawResult instanceof CycArrayList) {
      result = new CycFormulaSentence((CycArrayList) rawResult);
    } else if (rawResult instanceof CycConstantImpl) {
      result = new CycConstantSentence((CycConstantImpl) rawResult);
    } else {
      throw new CycApiException(
              "getSimplifiedSentence returned " + rawResult
              + ", which is not a CycSentence.\nOriginal input: " + this.toString());
    }
    //System.out.println("SIMPLIFIED TO: "+result.toString());
    return result;
  }

  /**
   * Return a version of this with all expandable relations expanded into their
   * more verbose forms. For example, this will expand SubCOLLECTION functions,
   * as well as other relations that have #$expansion's in the KB.
   *
   * @param access
   * @return an expanded FormulaSentence
   * @throws UnknownHostException
   * @throws IOException
   */
  public CycFormulaSentence getExpandedSentence(CycAccess access) throws CycConnectionException {
    return getExpandedSentence(access, getDefaultSimplifierMt());
  }

  /**
   * Return a version of this with all expandable relations expanded into their
   * more verbose forms. For example, this will expand SubCOLLECTION functions,
   * as well as other relations that have #$expansion's in the KB.
   *
   * @param access
   * @param mt the microtheory from which to look for expansions.
   * @return the expanded version of this sentence.
   * @throws UnknownHostException
   * @throws IOException
   */
  public CycFormulaSentence getExpandedSentence(CycAccess access, ElMt mt) throws CycConnectionException {
    String command = null;
    try {
      command = "(el-expand-all " + this.stringApiValue() + " " + mt.stringApiValue() + ")";
    } catch (Exception ex) {
      throw (new BaseClientRuntimeException(ex));
    }
    Object rawResult = access.converse().converseObject(command);
    CycFormulaSentence result;
    if (rawResult instanceof CycArrayList) {
      result = new CycFormulaSentence((CycArrayList) rawResult);
    } else {
      throw new CycApiException(
              "getExpandedSentence returned " + rawResult
              + ", which is not a CycFormulaSentence.\nOriginal input: " + this.toString());
    }
    //System.out.println("SIMPLIFIED TO: "+result.toString());
    return result;
  }

  /**
   * Return a canonical version of this. If two different sentences yield the
 same sentence after calling this method, then those two sentences are equal
 at the EL. In other words, they are merely syntactic variants of the same
 semantic meaning.
   *
   * @throws com.cyc.base.exception.CycConnectionException
   * @see #getCanonicalElSentence(com.cyc.base.CycAccess,
   * com.cyc.base.cycobject.ElMt, java.lang.Boolean)
   * @param access
   * @return the canonical version of this sentence.
   */
  public CycFormulaSentence getCanonicalElSentence(CycAccess access) throws CycConnectionException {
    return getCanonicalElSentence(access, getDefaultSimplifierMt(), true);
  }

  /**
   * @throws com.cyc.base.exception.CycConnectionException
   * @see #getCanonicalElSentence(com.cyc.base.CycAccess,
   * com.cyc.base.cycobject.ElMt, java.lang.Boolean)
   * @param access
   * @param canonicalizeVars
   * @return the canonical version of this sentence.
   */
  public CycFormulaSentence getCanonicalElSentence(CycAccess access,
          Boolean canonicalizeVars) throws CycConnectionException {
    return getCanonicalElSentence(access, getDefaultSimplifierMt(),
            canonicalizeVars);
  }

  /**
   * Return a canonical version of this. If two different sentences yield the
 same sentence after calling this method (with canonicalizeVars set to
 True), then those two sentences are equal at the EL. In other words, they
 are merely syntactic variants of the same semantic meaning.
   *
   * @param access
   * @param mt
   * @param canonicalizeVars
   * @return the canonical version of this sentence.
   * @throws UnknownHostException
   * @throws IOException
   */
  public CycFormulaSentence getCanonicalElSentence(CycAccess access, ElMt mt,
          Boolean canonicalizeVars) throws CycConnectionException {
    String command = null;
    //need to add the following to the command..." " + DefaultCycObject.stringApiValue(canonicalizeVars) +
    try {
      command = "(canonicalize-el-sentence " + this.stringApiValue() + " " + mt.stringApiValue() + " " + DefaultCycObject.stringApiValue(
              canonicalizeVars) + ")";
    } catch (Exception ex) {
      throw (new BaseClientRuntimeException(ex));
    }
    Object rawResult = access.converse().converseObject(command);
    CycFormulaSentence result;
    if (rawResult instanceof CycArrayList) {
      result = new CycFormulaSentence((CycArrayList) rawResult);
    } else {
      throw new CycApiException(
              "getCanonicalElSentence returned " + rawResult
              + ", which is not a CycFormulaSentence.\nOriginal input: " + this.toString());
    }
    return result;
  }

  /**
   * Is this sentence inconsistent with any of its constraints (e.g. PREDICATE
   * argument constraints)? A false return value does not mean that this meets
   * all the constraints, but it means that it is not inconsistent with them.
   * For example, if an argument position is constrained to be a spec of
   * #$Mammal, and the argument is merely known to be a spec of #$Animal, then
   * the argument does not meet all of the constraints, but there are no
   * constraint violations, and this method should return false.
   *
   * @param access
   * @param mt
   * @return true if violations are found
   */
  public boolean hasWffConstraintViolations(CycAccess access, ElMt mt) {
    try {
      String command = "(el-lenient-wff-assertible? "
              + this.stringApiValue() + " " + mt.stringApiValue() + ")";
      Object rawResult = access.converse().converseObject(command);
      boolean equalsT = CycObjectFactory.t.equals(rawResult);
      return (!equalsT);
    } catch (Exception ex) {
      throw (new CycApiException(
              "Unable to decide whether " + this + " is well-formed in " + mt,
              ex));
    }
  }

  /**
   * @see #getNonWffAssertExplanation(com.cyc.base.CycAccess,
   * com.cyc.base.cycobject.ElMt)
   * @param access
   * @return the explanation
   */
  public String getNonWffAssertExplanation(CycAccess access) {
    try {
      return getNonWffAssertExplanation(access, CommonConstants.CURRENT_WORLD_DATA_MT);
    } catch (Exception ex) {
      throw (new CycApiException(
              "Unable to retrieve explanation for why " + this + " is not well-formed in " + CommonConstants.CURRENT_WORLD_DATA_MT,
              ex));
    }
  }

  /**
   * Returns a string that attempts to explain why this is not well-formed for
 assertion. Return null if this is well-formed for assertion.
   *
   * @param access
   * @param mt
   * @return An explanation, or null if no problems found.
   */
  public String getNonWffAssertExplanation(CycAccess access, ElMt mt) {
    try {
      String command = "(with-inference-mt-relevance " + mt.stringApiValue()
              + " (opencyc-explanation-of-why-not-wff-assert "
              + this.stringApiValue() + " " + mt.stringApiValue() + "))";
      Object rawResult = access.converse().converseObject(command);
      if (rawResult instanceof String) {
        return (String) rawResult;
      } else {
        return null;
      }
    } catch (Exception ex) {
      throw (new CycApiException(
              "Unable to retrieve explanation for why " + this + " is not well-formed in " + mt,
              ex));
    }
  }

  /**
   * @see #getNonWffAssertExplanation(com.cyc.base.CycAccess,
   * com.cyc.base.cycobject.ElMt)
   * @param access
   * @return An explanation, or null if no problems found.
   */
  public String getNonWffExplanation(CycAccess access) {
    return getNonWffExplanation(access, getDefaultSimplifierMt());
  }

  /**
   * Returns a string that attempts to explain why this is not well-formed for
 any purpose. Return null if this is well-formed. If you want to make an
 assertion with your sentence, use the much more constraining
 {@link com.cyc.baseclient.cycobject.CycFormulaSentence#getNonWffAssertExplanation(com.cyc.base.CycAccess, com.cyc.base.cycobject.ElMt)  getNonWffAssertExplanation}.
   *
   * @param access
   * @param mt
   * @return An explanation, or null if no problems found.
   */
  public String getNonWffExplanation(CycAccess access, ElMt mt) {
    try {
      String command = "(with-inference-mt-relevance " + mt.stringApiValue()
              + " (opencyc-explanation-of-why-not-wff "
              + this.stringApiValue() + " " + mt.stringApiValue() + "))";
      Object rawResult = access.converse().converseObject(command);
      if (rawResult instanceof String) {
        return (String) rawResult;
      } else {
        return null;
      }
    } catch (Exception ex) {
      throw (new CycApiException(
              "Unable to retrieve explanation for why " + this + " is not well-formed in " + mt,
              ex));
    }
  }

  /**
   *
   * @return a deep copy of this sentence.
   */
  @Override
  public CycFormulaSentence deepCopy() {
    return new CycFormulaSentence(super.deepCopy().getArgsUnmodifiable());
  }

  /**
   *
   * @param original
   * @param replacement
   * @return A copy of this sentence, with original replaced with replacement
 throughout.
   */
  public CycFormulaSentence substituteNonDestructive(Object original,
          Object replacement) {
    Map<Object, Object> map = new HashMap<Object, Object>();
    map.put(original, replacement);
    return (CycFormulaSentence) this.applySubstitutionsNonDestructive(map);
  }

  /**
   * Replace original with replacement in this sentence.
   *
   * @param original
   * @param replacement
   */
  public void substituteDestructive(Object original, Object replacement) {
    Map<Object, Object> map = new HashMap<Object, Object>();
    map.put(original, replacement);
    this.applySubstitutionsDestructive(map);
  }

  /**
   * Returns the result of a tree substitution on the sentence. Note that this
 leaves the original sentence unmodified.
   *
   * @param access
   * @param substitutions
   * @return The CycFormulaSentence resulting from the tree substitution.
   * @throws CycApiException
   */
    @Override
  public FormulaSentence treeSubstitute(CycAccess access,
          Map<CycObject, Object> substitutions) throws CycApiException {
    CycList terms = this.toDeepCycList();
    if (substitutions != null) {
      for (final Map.Entry<CycObject, Object> entry : substitutions.entrySet()) {
        final CycObject oldTerm = entry.getKey();
        final Object newTerm = entry.getValue();
        terms = terms.subst(maybeListify(newTerm), maybeListify(oldTerm));
      }
    }
    return new CycFormulaSentence(terms);
  }

  private static Object maybeListify(Object term) {
    if (term instanceof NonAtomicTerm) {
      return ((NonAtomicTerm) term).toDeepCycList();
    } else if (term instanceof Formula) {
      return ((Formula) term).toDeepCycList();
    } else {
      return term;
    }
  }

  @Override
  public Object clone() {
    return new CycFormulaSentence(args);
  }

  @Override
  public int compareTo(Object o) {
    if (o instanceof CycFormulaSentence) {
      return args.compareTo(((CycFormulaSentence) o).args);
    } else {
      return 0;
    }
  }
  private static final CycSymbolImpl PPH_OPTIMIZED_NAMES_FOR_VARIABLES
          = CycObjectFactory.makeCycSymbol("pph-optimized-names-for-variables");
  private static ElMt simplifierMt = null;
}
