package com.cyc.baseclient;

/*
 * #%L
 * File: CommonConstants.java
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
import com.cyc.base.annotation.CycObjectLibrary;
import com.cyc.base.annotation.CycTerm;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.ELMt;
import com.cyc.base.cycobject.Fort;
import com.cyc.baseclient.cycobject.CycConstantImpl;
import com.cyc.baseclient.cycobject.ELMtConstant;
import com.cyc.baseclient.cycobject.GuidImpl;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author nwinant
 */
@CycObjectLibrary()
public class CommonConstants {

  /**
   * Convenient reference to #$isa.
   */
  @CycTerm(cycl="#$isa")
  public static final CycConstantImpl ISA = new CycConstantImpl("isa", new GuidImpl(
          "bd588104-9c29-11b1-9dad-c379636f7270"));

  /**
   * Convenient reference to #$genls.
   */
  @CycTerm(cycl="#$genls")
  public static final CycConstantImpl GENLS = new CycConstantImpl("genls", new GuidImpl(
          "bd58810e-9c29-11b1-9dad-c379636f7270"));

  /**
   * Convenient reference to #$genlMt.
   */
  @CycTerm(cycl="#$genlMt")
  public static final CycConstantImpl GENL_MT = new CycConstantImpl("genlMt", new GuidImpl(
          "bd5880e5-9c29-11b1-9dad-c379636f7270"));

  /**
   * Convenient reference to #$comment.
   */
  @CycTerm(cycl="#$comment")
  public static final CycConstantImpl COMMENT = new CycConstantImpl("comment", new GuidImpl(
          "bd588109-9c29-11b1-9dad-c379636f7270"));

  /**
   * Convenient reference to #$Collection.
   */
  @CycTerm(cycl="#$Collection")
  public static final CycConstantImpl COLLECTION = new CycConstantImpl("Collection",
          new GuidImpl("bd5880cc-9c29-11b1-9dad-c379636f7270"));

  /**
   * Convenient reference to #$Predicate.
   */
  @CycTerm(cycl="#$Predicate")
  public static final CycConstantImpl PREDICATE = new CycConstantImpl(
          "Predicate", new GuidImpl("bd5880d6-9c29-11b1-9dad-c379636f7270"));

  /**
   * Convenient reference to #$BinaryPredicate.
   */
  @CycTerm(cycl="#$BinaryPredicate")
  public static final CycConstantImpl BINARY_PREDICATE = new CycConstantImpl(
          "BinaryPredicate", new GuidImpl("bd588102-9c29-11b1-9dad-c379636f7270"));

  /**
   * Convenient reference to #$elementOf.
   */
  @CycTerm(cycl="#$elementOf")
  public static final CycConstantImpl ELEMENT_OF = new CycConstantImpl("elementOf",
          new GuidImpl("c0659a2b-9c29-11b1-9dad-c379636f7270"));

  /**
   * Convenient reference to #$numericallyEquals.
   */
  @CycTerm(cycl="#$numericallyEquals")
  public static final CycConstantImpl NUMERICALLY_EQUALS = new CycConstantImpl(
          "numericallyEquals", new GuidImpl("bd589d90-9c29-11b1-9dad-c379636f7270"));

  /**
   * *********************** constants needed by CycL parser ********
   */
  /**
   * Convenient reference to #$True.
   */
  @CycTerm(cycl="#$True")
  public static final CycConstantImpl TRUE = new CycConstantImpl("True", new GuidImpl(
          "bd5880d9-9c29-11b1-9dad-c379636f7270"));

  /**
   * Convenient reference to #$False.
   */
  @CycTerm(cycl="#$False")
  public static final CycConstantImpl FALSE = new CycConstantImpl("False",
          new GuidImpl("bd5880d8-9c29-11b1-9dad-c379636f7270"));

  /**
   * Convenient reference to #$not.
   */
  @CycTerm(cycl="#$not")
  public static final CycConstantImpl NOT = new CycConstantImpl("not", new GuidImpl(
          "bd5880fb-9c29-11b1-9dad-c379636f7270"));

  /**
   * Convenient reference to #$and.
   */
  @CycTerm(cycl="#$and")
  public static final CycConstantImpl AND = new CycConstantImpl("and", new GuidImpl(
          "bd5880f9-9c29-11b1-9dad-c379636f7270"));

  /**
   * Convenient reference to #$or.
   */
  @CycTerm(cycl="#$or")
  public static final CycConstantImpl OR = new CycConstantImpl("or", new GuidImpl(
          "bd5880fa-9c29-11b1-9dad-c379636f7270"));

  /**
   * Convenient reference to #$xor.
   */
  @CycTerm(cycl="#$xor")
  public static final CycConstantImpl XOR = new CycConstantImpl("xor", new GuidImpl(
          "bde7f9f2-9c29-11b1-9dad-c379636f7270"));

  /**
   * Convenient reference to #$equiv.
   */
  @CycTerm(cycl="#$equiv")
  public static final CycConstantImpl EQUIV = new CycConstantImpl("equiv",
          new GuidImpl("bda887b6-9c29-11b1-9dad-c379636f7270"));

  /**
   * Convenient reference to #$implies.
   */
  @CycTerm(cycl="#$implies")
  public static final CycConstantImpl IMPLIES = new CycConstantImpl("implies",
          new GuidImpl("bd5880f8-9c29-11b1-9dad-c379636f7270"));

  /**
   * Convenient reference to #$forAll.
   */
  @CycTerm(cycl="#$forAll")
  public static final CycConstantImpl FOR_ALL = new CycConstantImpl("forAll",
          new GuidImpl("bd5880f7-9c29-11b1-9dad-c379636f7270"));

  /**
   * Convenient reference to #$thereExists.
   */
  @CycTerm(cycl="#$thereExists")
  public static final CycConstantImpl THERE_EXISTS = new CycConstantImpl(
          "thereExists", new GuidImpl("bd5880f6-9c29-11b1-9dad-c379636f7270"));

  /**
   * Convenient reference to #$thereExistExactly.
   */
  @CycTerm(cycl="#$thereExistExactly")
  public static final CycConstantImpl THERE_EXIST_EXACTLY = new CycConstantImpl(
          "thereExistExactly", new GuidImpl("c10ae7b8-9c29-11b1-9dad-c379636f7270"));

  /**
   * Convenient reference to #$thereExistAtMost.
   */
  @CycTerm(cycl="#$thereExistAtMost")
  public static final CycConstantImpl THERE_EXIST_AT_MOST = new CycConstantImpl(
          "thereExistAtMost", new GuidImpl("c10af932-9c29-11b1-9dad-c379636f7270"));

  /**
   * Convenient reference to #$thereExistAtLeast.
   */
  @CycTerm(cycl="#$thereExistAtLeast")
  public static final CycConstantImpl THERE_EXIST_AT_LEAST = new CycConstantImpl(
          "thereExistAtLeast", new GuidImpl("c10af5e7-9c29-11b1-9dad-c379636f7270"));

  /**
   * Convenient reference to #$ExpandSubLFn.
   */
  @CycTerm(cycl="#$ExpandSubLFn")
  public static final CycConstantImpl EXPAND_SUBL_FN = new CycConstantImpl(
          "ExpandSubLFn", new GuidImpl("c0b2bc13-9c29-11b1-9dad-c379636f7270"));

  /**
   * Convenient reference to #$SubLQuoteFn.
   */
  @CycTerm(cycl="#$SubLQuoteFn")
  public static final CycConstantImpl SUBL_QUOTE_FN = new CycConstantImpl(
          "SubLQuoteFn", new GuidImpl("94f07021-8b0d-11d7-8701-0002b3a8515d"));

  /**
   * Convenient reference to #$PlusFn.
   */
  @CycTerm(cycl="#$PlusFn")
  public static final CycConstantImpl PLUS_FN = new CycConstantImpl("PlusFn", new GuidImpl(
          "bd5880ae-9c29-11b1-9dad-c379636f7270"));

  /**
   * Convenient reference to #$different.
   */
  @CycTerm(cycl="#$different")
  public static final CycConstantImpl DIFFERENT = new CycConstantImpl("different",
          new GuidImpl("bd63f343-9c29-11b1-9dad-c379636f7270"));

  /**
   * Convenient reference to #$Thing.
   */
  @CycTerm(cycl="#$Thing")
  public static final CycConstantImpl THING = new CycConstantImpl("Thing", new GuidImpl(
          "bd5880f4-9c29-11b1-9dad-c379636f7270"));

  /**
   * Convenient reference to #$MtSpace.
   */
  @CycTerm(cycl="#$MtSpace")
  public static final CycConstantImpl MT_SPACE = new CycConstantImpl("MtSpace", new GuidImpl(
          "abb96eb5-e798-11d6-8ac9-0002b3a333c3"));

  /**
   * Convenient reference to #$BaseKB.
   */
  @CycTerm(cycl="#$BaseKB")
  public static final ELMt BASE_KB = ELMtConstant.makeELMtConstant(
          new CycConstantImpl("BaseKB", new GuidImpl(
                          "bd588111-9c29-11b1-9dad-c379636f7270")));

  /**
   * Convenient reference to #$CurrentWorldDataCollectorMt-NonHomocentric.
   */
  @CycTerm(cycl="#$CurrentWorldDataCollectorMt-NonHomocentric")
  public static final ELMt CURRENT_WORLD_DATA_MT = ELMtConstant.makeELMtConstant(
          new CycConstantImpl("CurrentWorldDataCollectorMt-NonHomocentric",
                  new GuidImpl("bf192b1e-9c29-11b1-9dad-c379636f7270")));

  /**
   * Convenient reference to #$InferencePSC.
   */
  @CycTerm(cycl="#$InferencePSC")
  public static final ELMt INFERENCE_PSC = ELMtConstant.makeELMtConstant(
          new CycConstantImpl("InferencePSC", new GuidImpl(
                          "bd58915a-9c29-11b1-9dad-c379636f7270")));

  /**
   * Convenient reference to #$AnytimePSC.
   */
  @CycTerm(cycl="#$AnytimePSC")
  public static final ELMt ANYTIME_PSC = ELMtConstant.makeELMtConstant(
          new CycConstantImpl("AnytimePSC", new GuidImpl(
                          "28392742-b00f-41d8-98de-8399027785de")));

  /**
   * Convenient reference to #$EverythingPSC.
   */
  @CycTerm(cycl="#$EverythingPSC")
  public static final ELMt EVERYTHING_PSC = ELMtConstant.makeELMtConstant(
          new CycConstantImpl("EverythingPSC", new GuidImpl(
                          "be7f041b-9c29-11b1-9dad-c379636f7270")));

  /**
   * Convenient reference to #$UniversalVocabularyMt.
   */
  @CycTerm(cycl="#$UniversalVocabularyMt")
  public static final ELMt UNIVERSAL_VOCABULARY_MT = ELMtConstant.makeELMtConstant(
          new CycConstantImpl("UniversalVocabularyMt", new GuidImpl(
                          "dff4a041-4da2-11d6-82c0-0002b34c7c9f")));

  /**
   * Convenient reference to #$BookkeepingMt.
   */
  @CycTerm(cycl="#$BookkeepingMt")
  public static final ELMt BOOKKEEPING_MT = ELMtConstant.makeELMtConstant(
          new CycConstantImpl("BookkeepingMt", new GuidImpl(
                          "beaed5bd-9c29-11b1-9dad-c379636f7270")));
  
  @CycTerm(cycl="#$EnglishParaphraseMt")
  public static final ELMt ENGLISH_PARAPHRASE_MT = ELMtConstant.makeELMtConstant(
          new CycConstantImpl("EnglishParaphraseMt", new GuidImpl(
                          "bda16220-9c29-11b1-9dad-c379636f7270")));

  @CycTerm(cycl="#$backchainRequired")
  public static final CycConstantImpl BACKCHAIN_REQUIRED = new CycConstantImpl("backchainRequired", new GuidImpl(
          "beaa3d29-9c29-11b1-9dad-c379636f7270"));

  @CycTerm(cycl="#$backchainEncouraged")
  public static final CycConstantImpl BACKCHAIN_ENCOURAGED = new CycConstantImpl("backchainEncouraged", new GuidImpl(
          "c09d1cea-9c29-11b1-9dad-c379636f7270"));

  @CycTerm(cycl="#$backchainDiscouraged")
  public static final CycConstantImpl BACKCHAIN_DISCOURAGED = new CycConstantImpl("backchainDiscouraged", new GuidImpl(
          "bfcbce14-9c29-11b1-9dad-c379636f7270"));

  @CycTerm(cycl="#$backchainForbidden")
  public static final CycConstantImpl BACKCHAIN_FORBIDDEN = new CycConstantImpl("backchainForbidden", new GuidImpl(
          "bfa4e9d2-9c29-11b1-9dad-c379636f7270"));

  @CycTerm(cycl="#$IrreflexiveBinaryPredicate")
  public static final CycConstantImpl IRREFLEXIVE_BINARY_PREDICATE = new CycConstantImpl("IrreflexiveBinaryPredicate", new GuidImpl(
          "bd654be7-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$UnaryPredicate")
  public static final CycConstantImpl UNARY_PREDICATE = new CycConstantImpl("UnaryPredicate", new GuidImpl(
          "bd5e7a9e-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$coExtensional")
  public static final CycConstantImpl CO_EXTENSIONAL = new CycConstantImpl("coExtensional", new GuidImpl(
          "bd59083a-9c29-11b1-9dad-c379636f7270"));

  @CycTerm(cycl="#$argIsa")
  public static final CycConstantImpl ARG_ISA = new CycConstantImpl("argIsa", new GuidImpl(
          "bee22d3d-9c29-11b1-9dad-c379636f7270"));

  @CycTerm(cycl="#$arg1Genl")
  public static final CycConstantImpl ARG_1_GENL = new CycConstantImpl("arg1Genl", new GuidImpl(
          "bd588b1d-9c29-11b1-9dad-c379636f7270"));

  @CycTerm(cycl="#$arg2Genl")
  public static final CycConstantImpl ARG_2_GENL = new CycConstantImpl("arg2Genl", new GuidImpl(
          "bd58dcda-9c29-11b1-9dad-c379636f7270"));

  @CycTerm(cycl="#$arg3Genl")
  public static final CycConstantImpl ARG_3_GENL = new CycConstantImpl("arg3Genl", new GuidImpl(
          "bd58b8c3-9c29-11b1-9dad-c379636f7270"));

  @CycTerm(cycl="#$resultIsa")
  public static final CycConstantImpl RESULT_ISA = new CycConstantImpl("resultIsa", new GuidImpl(
          "bd5880f1-9c29-11b1-9dad-c379636f7270"));

  @CycTerm(cycl="#$resultGenl")
  public static final CycConstantImpl RESULT_GENL = new CycConstantImpl("resultGenl", new GuidImpl(
          "bd58d6ab-9c29-11b1-9dad-c379636f7270"));

  @CycTerm(cycl="#$termDependsOn")
  public static final CycConstantImpl TERM_DEPENDS_ON = new CycConstantImpl("termDependsOn", new GuidImpl(
          "bdf02d74-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$definingMt")
  public static final CycConstantImpl DEFINING_MT = new CycConstantImpl("definingMt", new GuidImpl(
          "bde5ec9c-9c29-11b1-9dad-c379636f7270"));

  @CycTerm(cycl="#$SingleEntry")
  public static final CycConstantImpl SINGLE_ENTRY = new CycConstantImpl("SingleEntry", new GuidImpl(
          "bd5880eb-9c29-11b1-9dad-c379636f7270"));

  @CycTerm(cycl="#$argFormat")
  public static final CycConstantImpl ARG_FORMAT = new CycConstantImpl("argFormat", new GuidImpl(
          "bd8a36e1-9c29-11b1-9dad-c379636f7270"));

  @CycTerm(cycl="#$synonymousExternalConcept")
  public static final CycConstantImpl SYNONYMOUS_EXTERNAL_CONCEPT = new CycConstantImpl("synonymousExternalConcept", new GuidImpl(
          "c0e2af4e-9c29-11b1-9dad-c379636f7270"));

  @CycTerm(cycl="#$genlPreds")
  public static final CycConstantImpl GENL_PREDS = new CycConstantImpl("genlPreds", new GuidImpl(
          "bd5b4951-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$quotedIsa")
  public static final CycConstantImpl QUOTED_ISA = new CycConstantImpl("quotedIsa", new GuidImpl(
          "055544a2-4371-11d6-8000-00a0c9da2002"));
  
  @CycTerm(cycl="#$nameString")
  public static final CycConstantImpl NAME_STRING = new CycConstantImpl("nameString", new GuidImpl(
          "c0fdf7e8-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$genFormat")
  public static final CycConstantImpl GEN_FORMAT = new CycConstantImpl("genFormat", new GuidImpl(
          "beed06de-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$conceptuallyRelated")
  public static final CycConstantImpl CONCEPTUALLY_RELATED = new CycConstantImpl("conceptuallyRelated", new GuidImpl(
          "bd58803e-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$VariedOrderCollection")
  public static final Fort VARIED_ORDER_COLLECTION = new CycConstantImpl("VariedOrderCollection", new GuidImpl(
          "36cf85d0-20a1-11d6-8000-0050dab92c2f"));
  
  @CycTerm(cycl="#$WorldLikeOursCollectorMt-NonHomocentric")
  public static final CycConstantImpl WORLD_LIKE_OURS_MT = new CycConstantImpl("WorldLikeOursCollectorMt-NonHomocentric", new GuidImpl(
          "bf4c781d-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$genlMt-Vocabulary")
  public static final CycConstantImpl GENL_MT_VOCABULARY = new CycConstantImpl("genlMt-Vocabulary", new GuidImpl(
          "c054a49e-9c29-11b1-9dad-c379636f7270"));
  
  //public static final CycConstantImpl THEORY_MICROTHEORY = new CycConstantImpl("TheoryMicrotheory", new GuidImpl(
  //        "c0ae227f-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$VocabularyMicrotheory")
  public static final CycConstantImpl VOCABULARY_MICROTHEORY = new CycConstantImpl("VocabularyMicrotheory", new GuidImpl(
          "bda19dfd-9c29-11b1-9dad-c379636f7270"));

  @CycTerm(cycl="#$DataMicrotheory")
  public static final CycConstantImpl DATA_MICROTHEORY = new CycConstantImpl("DataMicrotheory", new GuidImpl(
          "be5275a8-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$UnaryFunction")
  public static final CycConstantImpl UNARY_FUNCTION = new CycConstantImpl("UnaryFunction", new GuidImpl(
          "bd58af89-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$ReifiableFunction")
  public static final CycConstantImpl REIFIABLE_FUNCTION = new CycConstantImpl("ReifiableFunction", new GuidImpl(
          "bd588002-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$IndividualDenotingFunction")
  public static final CycConstantImpl INDIVIDUAL_DENOTING_FUNCTION = new CycConstantImpl("IndividualDenotingFunction", new GuidImpl(
          "bd58fad9-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$Function-Denotational")
  public static final CycConstantImpl FUNCTION_DENOTATIONAL = new CycConstantImpl("Function-Denotational", new GuidImpl(
          "bd5c40b0-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$CollectionDenotingFunction")
  public static final CycConstantImpl COLLECTION_DENOTING_FUNCTION = new CycConstantImpl("CollectionDenotingFunction", new GuidImpl(
          "bd58806a-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$BinaryFunction")
  public static final CycConstantImpl BINARY_FUNCTION = new CycConstantImpl("BinaryFunction", new GuidImpl(
          "c0e7247c-9c29-11b1-9dad-c379636f7270"));
  
  // Commented out regarding BASEAPI-63 - nwinant, 2014-08-18
  /*
  @CycTerm(cycl="#$CycKBSubsetCollection")
  public static final CycConstantImpl CYC_KB_SUBSET_COLLECTION = new CycConstantImpl("CycKBSubsetCollection", new GuidImpl(
          "c0d68226-9c29-11b1-9dad-c379636f7270"));
  */
  
  @CycTerm(cycl="#$CollectorMicrotheory")
  public static final CycConstantImpl COLLECTOR_MICROTHEORY = new CycConstantImpl("CollectorMicrotheory", new GuidImpl(
          "c1102468-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$SpindleMicrotheory")
  public static final CycConstantImpl SPINDLE_MICROTHEORY = new CycConstantImpl("SpindleMicrotheory", new GuidImpl(
          "312d4454-c6a6-11d6-8000-0002b3891c1f"));
  
  @CycTerm(cycl="#$unknownSentence")
  public static final CycConstantImpl UNKNOWN_SENTENCE = new CycConstantImpl("unknownSentence", new GuidImpl(
          "be1e5136-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$HypertextMarkupLanguage")
  public static final CycConstantImpl HYPERTEXT_MARKUP_LANGUAGE = new CycConstantImpl("HypertextMarkupLanguage",
          new GuidImpl("bd656e90-9c29-11b1-9dad-c379636f7270"));

  @CycTerm(cycl="#$UnicodeStringFn")
  public static final CycConstantImpl UNICODE_STRING_FN = new CycConstantImpl("UnicodeStringFn",
          new GuidImpl("dbc65ea5-e65c-11d8-89df-0002b3620a7d"));
    
  @CycTerm(cycl="#$ist")
  public static final CycConstantImpl IST = new CycConstantImpl("ist",
          new GuidImpl("bd5880d3-9c29-11b1-9dad-c379636f7270"));
  
  // Commented out regarding BASEAPI-63 - nwinant, 2014-08-18
  /*
  @CycTerm(cycl="#$PublicConstant")
  public static final CycConstantImpl PUBLIC_CONSTANT = new CycConstantImpl("PublicConstant",
          new GuidImpl("bd7abd90-9c29-11b1-9dad-c379636f7270"));
  */
  
  @CycTerm(cycl="#$Microtheory")
  public static final CycConstantImpl MICROTHEORY = new CycConstantImpl("Microtheory",
          new GuidImpl("bd5880d5-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$Individual")
  public static final CycConstantImpl INDIVIDUAL = new CycConstantImpl("Individual",
          new GuidImpl("bd58da02-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$coextensionalSetOrCollections")
  public static final CycConstantImpl COEXTENSIONAL_SET_OR_COLLECTIONS = new CycConstantImpl("coextensionalSetOrCollections",
          new GuidImpl("bec944f2-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$Dollar-UnitedStates")
  public static final CycConstantImpl DOLLAR_UNITED_STATES = new CycConstantImpl("Dollar-UnitedStates",
          new GuidImpl("bd58a636-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$Administrator")
  public static final CycConstantImpl ADMINISTRATOR = new CycConstantImpl("Administrator", 
          new GuidImpl("bd58aedc-9c29-11b1-9dad-c379636f7270"));

  @CycTerm(cycl="#$TheSet")
  public static final CycConstantImpl THE_SET = new CycConstantImpl("TheSet", 
          new GuidImpl("bd58e476-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$CollectionUnionFn")
  public static final CycConstantImpl COLLECTION_UNION_FN = new CycConstantImpl("CollectionUnionFn", 
          new GuidImpl("beb84af8-9c29-11b1-9dad-c379636f7270"));

  
  
  
  
  public static final Collection<? extends Object> LOGICAL_OPERATOR_FORTS = Collections.unmodifiableCollection(
          Arrays.asList(AND, OR, NOT, UNKNOWN_SENTENCE, IMPLIES));

  
  // Main
  
  public static void main(String[] args) {
    try {
      System.out.println("Beginning...");
      final Collection<CycObject> objs = CycObjectLibraryLoader.get().getAllCycObjectsForClass(CommonConstants.class);
      System.out.println();
      System.out.println("==");
      for (CycObject obj : objs) {
        System.out.println("- " + obj);
      }
      System.out.println("==");
      System.out.println("... done! Total: " + objs.size());
    } catch (Exception ex) {
      ex.printStackTrace();
      System.exit(1);
    } finally {
      System.exit(0);
    }
  }
}
