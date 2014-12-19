package com.cyc.query;

/*
 * #%L
 * File: QueryApiTestConstants.java
 * Project: Query API
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
import com.cyc.base.annotation.CycObjectLibrary;
import com.cyc.base.annotation.CycTerm;
import com.cyc.baseclient.CommonConstants;
import com.cyc.kb.Context;
import com.cyc.kb.KBCollection;
import com.cyc.kb.KBIndividual;
import com.cyc.kb.KBPredicate;
import com.cyc.kb.Sentence;
import com.cyc.kb.client.BinaryPredicateImpl;
import com.cyc.kb.client.Constants;
import com.cyc.kb.client.ContextImpl;
import com.cyc.kb.client.KBCollectionImpl;
import com.cyc.kb.client.KBIndividualImpl;
import com.cyc.kb.client.KBPredicateImpl;
import com.cyc.kb.client.KBTermImpl;
import com.cyc.kb.client.SentenceImpl;
import com.cyc.kb.client.VariableImpl;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KBApiException;
import com.cyc.kb.exception.KBApiRuntimeException;
import com.cyc.kb.exception.KBTypeException;

/**
 *
 * @author baxter
 */
@CycObjectLibrary(accessor = "getInstance")
public class QueryApiTestConstants {

  public static synchronized QueryApiTestConstants getInstance() throws KBApiRuntimeException {
    try {
      if (instance == null) {
        instance = new QueryApiTestConstants();
      }
    } catch (KBApiException e) {
      throw new KBApiRuntimeException(
              "Once of the private final fields in com.cyc.query.QueryApiTestConstants could not be instantiated, can not proceed further.",
              e);
    }
    return instance;
  }

  @CycTerm(cycl = "#$Bird")
  private final KBCollection bird = KBCollectionImpl.get("Bird");

  @CycTerm(cycl = "#$Emu")
  private final KBCollection emu = KBCollectionImpl.get("Emu");

  @CycTerm(cycl = "#$Zebra")
  private final KBCollection zebra = KBCollectionImpl.get("Zebra");

  @CycTerm(cycl = "#$GeneralCycKETask-Allotment")
  public final KBIndividual generalCycKE = KBIndividualImpl.get("GeneralCycKETask-Allotment");

  @CycTerm(cycl = "#$AbrahamLincoln")
  public final KBIndividual abrahamLincoln = KBIndividualImpl.get("AbrahamLincoln");

  @CycTerm(cycl = "#$Plant")
  public final KBCollectionImpl plant = KBCollectionImpl.get("Plant");

  @CycTerm(cycl = "#$Animal")
  private final KBCollectionImpl animal = KBCollectionImpl.get("Animal");

  @CycTerm(cycl = "(#$TheFn #$Animal)")
  public final KBIndividual theAnimal = KBIndividualImpl.get("(TheFn Animal)");

  public final Sentence theAnimalIsAnAnimal
          = new SentenceImpl(Constants.isa(), theAnimal, animal);

  public final Sentence xIsAnAnimal
          = new SentenceImpl(Constants.isa(), new VariableImpl("X"), animal);

  @CycTerm(cycl = "#$indexicalReferent")
  public final KBPredicate indexicalReferent = BinaryPredicateImpl.get("indexicalReferent");

  @CycTerm(cycl = "#$Now-Indexical")
  public final KBIndividual nowIndexical = KBIndividualImpl.get("Now-Indexical");
  public final Sentence whatTimeIsIt
          = new SentenceImpl(indexicalReferent, nowIndexical, new VariableImpl("NOW"));

  public final SentenceImpl yOwnsX = new SentenceImpl(BinaryPredicateImpl.get("owns"),
          new VariableImpl("Y"), new VariableImpl("X"));

  @CycTerm(cycl = "#$cellHasNumberOfChromosomes")
  public final KBPredicate cellHasNumberOfChromosomes = KBPredicateImpl.get("cellHasNumberOfChromosomes");

  @CycTerm(cycl = "#$assertionSentence")
  public final KBPredicate assertionSentence = KBPredicateImpl.get("assertionSentence");

  @CycTerm(cycl = "#$testQuerySpecification")
  public final KBPredicate testQuerySpecification = KBPredicateImpl.get("testQuerySpecification");

  @CycTerm(cycl = "#$testAnswers-Cardinality-Exact")
  public final KBPredicate testAnswersCardinalityExact = KBPredicateImpl.get(
          "testAnswers-Cardinality-Exact");

  @CycTerm(cycl = "#$testAnswers-Cardinality-Min")
  public final KBPredicateImpl testAnswersCardinalityMin = KBPredicateImpl.get(
          "testAnswers-Cardinality-Min");

  @CycTerm(cycl = "#$testAnswersCycL-Exact")
  public final KBPredicate testAnswersCycLExact = KBPredicateImpl.get("testAnswersCycL-Exact");

  @CycTerm(cycl = "#$testAnswersCycL-Wanted")
  public final KBPredicate testAnswersCycLWanted = KBPredicateImpl.get("testAnswersCycL-Wanted");

  @CycTerm(cycl = "#$equals")
  public KBPredicate cycEquals = KBPredicateImpl.get("equals");

  @CycTerm(cycl = "#$comment")
  public KBPredicate comment = KBPredicateImpl.get("comment");

  @CycTerm(cycl = "#$evaluate")
  public KBPredicate evaluate = KBPredicateImpl.get("evaluate");

  @CycTerm(cycl = "#$academyAwardWinner")
  public KBPredicate academyAwardWinner = KBPredicateImpl.get("academyAwardWinner");

  @CycTerm(cycl = "#$True")
  public KBIndividual cycTrue = KBIndividualImpl.get("True");

  @CycTerm(cycl = "(#$DifferenceFn 3 1)")
  public KBIndividual threeMinusOne = KBIndividualImpl.get("(#$DifferenceFn 3 1)");

  @CycTerm(cycl = "(#$DifferenceFn 1 0.5)")
  public KBIndividual oneMinusPointFive = KBIndividualImpl.get("(#$DifferenceFn 1 0.5)");

  @CycTerm(cycl = "#$Thing")
  public KBCollection thing = KBCollectionImpl.get("Thing");

  @CycTerm(cycl = "#$BillClinton")
  public KBIndividual billClinton = KBIndividualImpl.get("BillClinton");

  @CycTerm(cycl = "(#$AssistedReaderSourceSpindleCollectorForTaskFn #$GeneralCycKETask-Allotment)")
  public Context generalCycKECollector = ContextImpl.get(
          "(AssistedReaderSourceSpindleCollectorForTaskFn GeneralCycKETask-Allotment)");

  public final Sentence genlsEmuBird = new SentenceImpl(Constants.genls(), emu, bird);
  public final Sentence genlsAnimalX = new SentenceImpl(Constants.genls(), animal, new VariableImpl("X"));
  public final Sentence academyAwardWinners = new SentenceImpl(
          academyAwardWinner, new VariableImpl("X"), new VariableImpl("Y"), new VariableImpl("Z"));

  public final Sentence whatIsAbe = new SentenceImpl(
          Constants.isa(), abrahamLincoln, new VariableImpl("TYPE"));

  public final Sentence queryAnimals = new SentenceImpl(
          CommonConstants.ELEMENT_OF, new VariableImpl("N"),
          KBTermImpl.get("(TheSet Emu Zebra)"));

  @CycTerm(cycl="#$GeneralCycKETask-Allotment")
  public final KBIndividual GENERAL_CYC_KE_TASK_ALLOTMENT = KBIndividualImpl.get("GeneralCycKETask-Allotment");
  
  
  @CycTerm(cycl="#$cellHasNumberOfChromosomes")
  public final KBPredicate CELL_HAS_NUMBER_OF_CHROMOSOMES =  KBPredicateImpl.get("cellHasNumberOfChromosomes");
  private static QueryApiTestConstants instance = null;

  @CycTerm(cycl = "#$EnglishParaphraseMt")
  public Context englishParaphraseMt = ContextImpl.get("EnglishParaphraseMt");

  @CycTerm(cycl = "#$PeopleDataMt")
  public Context peopleDataMt = ContextImpl.get("PeopleDataMt");

  private QueryApiTestConstants() throws KBTypeException, CreateException, KBApiException {
    super();
  }

  public static KBCollection bird() throws KBApiException {
    return getInstance().bird;
  }

  public static KBCollection emu() throws KBApiException {
    return getInstance().emu;
  }

  public static KBCollection zebra() throws KBApiException {
    return getInstance().zebra;
  }

  public static KBCollection animal() throws KBApiException {
    return getInstance().animal;
  }

}
