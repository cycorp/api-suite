package com.cyc.kb.client;

/*
 * #%L
 * File: Constants.java
 * Project: KB API
 * %%
 * Copyright (C) 2013 - 2015 Cycorp, Inc
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


import com.cyc.base.annotation.CycObjectLibrary;
import com.cyc.base.annotation.CycTerm;
import com.cyc.kb.BinaryPredicate;
import com.cyc.kb.Context;
import com.cyc.kb.KBCollection;
import com.cyc.kb.KBFunction;
import com.cyc.kb.KBPredicate;
import com.cyc.kb.LogicalConnective;
import com.cyc.kb.exception.KBApiException;
import com.cyc.kb.exception.KBApiRuntimeException;

/**
 * A convenience class for frequently accessed Cyc constants. It should be rare
 * to use this class in application making use of the KB API.
 * 
 * @author Vijay Raj
 * @version $Id: Constants.java 154990 2014-11-14 22:46:51Z nwinant $
 * @since 1.0
 */
@CycObjectLibrary(accessor="getInstance")
public class Constants {

  private static Constants instance;
  
  @CycTerm(cycl="#$DataMicrotheory")
  public final KBCollection DATA_MT = KBCollectionImpl.get("DataMicrotheory");
  
  @CycTerm(cycl="#$BaseKB")
  public final Context BASE_KB_CTX = ContextImpl.get("BaseKB");
  
  @CycTerm(cycl="#$UniversalVocabularyMt")
  public final Context UV_MT_CTX = ContextImpl.get("UniversalVocabularyMt");
  
  @CycTerm(cycl="#$InferencePSC")
  public final Context INFERENCE_PSC_CTX = ContextImpl.get("InferencePSC");
  
  @CycTerm(cycl="#$EverythingPSC")
  public final Context EVERYTHING_PSC_CTX = ContextImpl.get("EverythingPSC");
  
@CycTerm(cycl="#$isa")
  public final KBPredicate ISA_PRED  = KBPredicateImpl.get("isa");
  
  @CycTerm(cycl="#$genls")
  public final KBPredicate GENLS_PRED = KBPredicateImpl.get("genls");
  
  @CycTerm(cycl="#$genlMt")
  public final KBPredicate GENLMT_PRED = KBPredicateImpl.get("genlMt");
  
  @CycTerm(cycl="#$genlPreds")
  public final KBPredicate GENLPREDS_PRED = KBPredicateImpl.get("genlPreds");
  
  @CycTerm(cycl="#$genlInverse")
  public final KBPredicate GENLINVERSEPREDS_PRED = KBPredicateImpl.get("genlInverse");
  
  @CycTerm(cycl="#$quotedIsa")
  public final BinaryPredicate QUOTEDISA_PRED = BinaryPredicateImpl.get("quotedIsa");
  
  @CycTerm(cycl="#$argIsa")
  public final KBPredicate ARGISA_PRED = KBPredicateImpl.get("argIsa");
  
  @CycTerm(cycl="#$argGenl")
  public final KBPredicate ARGGENL_PRED = KBPredicateImpl.get("argGenl");
  
  @CycTerm(cycl="#$arity")
  public final BinaryPredicate ARITY_PRED = BinaryPredicateImpl.get("arity");
  
  @CycTerm(cycl="#$resultIsa")
  public final KBPredicate RESULTISA_PRED = KBPredicateImpl.get("resultIsa");
  
  @CycTerm(cycl="#$resultGenl")
  public final KBPredicate RESULTGENL_PRED = KBPredicateImpl.get("resultGenl");
  
  @CycTerm(cycl="#$mtMonad")
  public final KBPredicate MTMONAD_PRED = KBPredicateImpl.get("mtMonad");
  
  @CycTerm(cycl="#$mtTimeIndex")
  public final KBPredicate MTTIMEIDX_PRED = KBPredicateImpl.get("mtTimeIndex");
  
  @CycTerm(cycl="#$ReifiableFunction")
  public final KBCollection REIFIABLE_FUNC = KBCollectionImpl.get("ReifiableFunction");
  
  @CycTerm(cycl="#$comment")
  public final BinaryPredicate COMMENT_PRED = BinaryPredicateImpl.get("comment");
  
  @CycTerm(cycl="#$Quote")
  public final KBFunction QUOTE_FUNC = KBFunctionImpl.get("Quote");
  
  @CycTerm(cycl="#$VariableArityFunction")
  public final KBCollection VAR_ARITY_COL = KBCollectionImpl.get("VariableArityFunction");
  
  @CycTerm(cycl="#$UnreifiableFunction")
  public final KBCollection UNREIFIABLE_FUNC_COL = KBCollectionImpl.get("UnreifiableFunction");
  
  @CycTerm(cycl="#$TheList")
  public final KBFunction THELIST_FUNC = KBFunctionImpl.get("TheList");
  
  @CycTerm(cycl="#$TheSet")
  public final KBFunction THESET_FUNC = KBFunctionImpl.get("TheSet");
  
  @CycTerm(cycl="#$interArgDifferent")
  public final KBPredicate INTER_ARG_DIFF_PRED = KBPredicateImpl.get("interArgDifferent");
  
  @CycTerm(cycl="#$assertedSentence")
  public final KBPredicate ASSERTED_SENT_PRED = KBPredicateImpl.get("assertedSentence");
  
  @CycTerm(cycl="#$checkSentence")
  public final KBPredicate CHECK_SENT_PRED = KBPredicateImpl.get("checkSentence");

  @CycTerm(cycl="#$not")
  public final LogicalConnective NOT_LC = LogicalConnectiveImpl.get("not");
  
  @CycTerm(cycl="#$unknownSentence")
  public final KBPredicate UNKNOWN_SENT_PRED = KBPredicateImpl.get("unknownSentence");
  // There is no way to get to these variables
  // Preferred way is to get them from their respective classes using getType or getClassType
  /*
  private final KBCollection THING_COL = new KBCollectionImpl("#$Thing");
  private final KBCollection INDIVIDUAL_COL = new KBCollectionImpl("#$Individual");
  private final KBCollection RELATION_COL = new KBCollectionImpl("#$Relation");
  private final KBCollection FUNCTION_COL = new KBCollectionImpl("#$Function-Denotational");
  private final KBCollection PREDICATE_COL = new KBCollectionImpl("#$Predicate");
  private final KBCollection BPRED_COL = new KBCollectionImpl("#$BinaryPredicate");
  private final KBCollection SCOPE_REL_COL = new KBCollectionImpl("#$ScopingRelation");
  private final KBCollection QUANTIFIER_COL = new KBCollectionImpl("#$Quantifier");
  private final KBCollection LOG_CON_COL = new KBCollectionImpl("#$LogicalConnective");
  private final KBCollection COLLECTION_COL = new KBCollectionImpl("#$Collection");
  private final KBCollection FIRST_ORD_COL = new KBCollectionImpl("#$FirstOrderCollection");
  private final KBCollection SECOND_ORD_COL = new KBCollectionImpl("#$SecondOrderCollection");
  private final KBCollection ASSERTION_COL = new KBCollectionImpl("#$CycLAssertion");
  private final KBCollection GAF_COL = new KBCollectionImpl("#$CycLGAFAssertion");
  private final KBCollection VARIABLE_COL = new KBCollectionImpl("#$CycLVariable");
  private final KBCollection SYMBOL_COL = new KBCollectionImpl("#$CycLSubLSymbol");
*/
  
  
  
  private Constants() throws KBApiException {
    super();
  }

  /**
   * This not part of the public, supported KB API
   * 
   * @return a instance of Constants class which instantiates the following commonly used
   * KB terms.
   * 
   * Contexts:
   * #$UniversalVocabularyMt
   * #$BaseKB
   * #$EverythingPSC
   * #$InferencePSC
   * 
   * Predicates
   * #$isa
   * #$genls
   * #$genlMt
   * #$genlPreds
   * #$quotedIsa
   * 
   * @throws KBApiRuntimeException
   */
  public static Constants getInstance() throws KBApiRuntimeException {
    try {
      if (instance == null) {
        instance = new Constants();
      }
      return instance;
    } catch (KBApiException e) {
      throw new KBApiRuntimeException(
          "Once of the private final fields in com.cyc.kb.Constants could not be instantiated, can not proceed further.",
          e);
    }
  }

  // Main contexts
  /**
   * @return Collection.get("DataMicrotheory")
   */
  public static KBCollection dataMt() {
    return getInstance().DATA_MT;
  }

  /**
   * @return Context.get("BaseKB")
   */
  public static Context baseKbMt() {
    return getInstance().BASE_KB_CTX;
  }

  /**
   * @return Context.get("EverythingPSC")
   */
  public static Context everythingPSCMt() {
    return getInstance().EVERYTHING_PSC_CTX;
  }

  /**
   * @return Context.get("InferencePSC")
   */
  public static Context inferencePSCMt() {
    return getInstance().INFERENCE_PSC_CTX;
  }

  /**
   * @return Context.get("UniversalVocabularyMt")
   */
  public static Context uvMt() {
    return getInstance().UV_MT_CTX;
  }

  // Main predicates
  /**
   * @return Predicate.get("isa")
   */
  public static KBPredicate isa() {
    return getInstance().ISA_PRED;
  }
  
  /**
   * @return Predicate.get("genls")
   */
  public static KBPredicate genls() {
    return getInstance().GENLS_PRED;
  }

  /**
   * @return Predicate.get("genlMt")
   */
  public static KBPredicate genlMt() {
    return getInstance().GENLMT_PRED;
  }

  /**
   * @return Predicate.get("genlPreds")
   */
  public static KBPredicate genlPreds() {
    return getInstance().GENLPREDS_PRED;
  }
  
  /**
   * @return Predicate.get("quotedIsa")
   */
  public static BinaryPredicate quotedIsa() {
    return getInstance().QUOTEDISA_PRED;
  }
  
  /**
   * @return Predicate.get("argIsa")
   */
  public static KBPredicate argIsa() {
    return getInstance().ARGISA_PRED;
  }

  /**
   * @return Predicate.get("argGenl")
   */
  public static KBPredicate argGenl() {
    return getInstance().ARGGENL_PRED;
  }

  /**
   * @return BinaryPredicate.get("arity")
   */
  public static BinaryPredicate arity() {
    return getInstance().ARITY_PRED;
  }

  /**
   * @return Predicate.get("resultIsa")
   */
  public static KBPredicate resultIsa() {
    return getInstance().RESULTISA_PRED;
  }

  /**
   * @return Predicate.get("resultGenl")
   */
  public static KBPredicate resultGenl() {
    return getInstance().RESULTGENL_PRED;
  }

  /**
   * @return Predicate.get("resultGenl")
   */
  public static KBPredicate mtMonad() {
    return getInstance().MTMONAD_PRED;
  }

  /**
   * @return Predicate.get("resultGenl")
   */
  public static KBPredicate mtTimeIndex() {
    return getInstance().MTTIMEIDX_PRED;
  }

  // Main collections

}
