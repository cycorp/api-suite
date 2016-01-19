package com.cyc.query;

/*
 * #%L
 * File: QueryTestConstants.java
 * Project: Query Client
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
import com.cyc.base.annotation.CycObjectLibrary;
import com.cyc.base.annotation.CycTerm;
import com.cyc.baseclient.CommonConstants;
import com.cyc.kb.Context;
import com.cyc.kb.KbCollection;
import static com.cyc.kb.KbFactory.getSentence;
import static com.cyc.kb.KbFactory.getVariable;
import com.cyc.kb.KbIndividual;
import com.cyc.kb.KbPredicate;
import com.cyc.kb.Sentence;
import com.cyc.kb.SentenceFactory;
import com.cyc.kb.client.BinaryPredicateImpl;
import com.cyc.kb.client.Constants;
import static com.cyc.kb.client.Constants.different;
import static com.cyc.kb.client.Constants.genls;
import com.cyc.kb.client.ContextImpl;
import com.cyc.kb.client.KbCollectionImpl;
import com.cyc.kb.client.KbIndividualImpl;
import com.cyc.kb.client.KbPredicateImpl;
import com.cyc.kb.client.KbTermImpl;
import com.cyc.kb.client.SentenceImpl;
import com.cyc.kb.client.VariableImpl;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbException;
import com.cyc.kb.exception.KbRuntimeException;
import com.cyc.kb.exception.KbTypeException;

/**
 *
 * @author baxter
 */
@CycObjectLibrary(accessor = "getInstance")
public class QueryTestConstants {

  public static synchronized QueryTestConstants getInstance() throws KbRuntimeException {
    try {
      if (instance == null) {
        instance = new QueryTestConstants();
      }
    } catch (KbException e) {
      throw new KbRuntimeException(
              "Once of the private final fields in com.cyc.query.QueryApiTestConstants could not be instantiated, can not proceed further.",
              e);
    }
    return instance;
  }

  @CycTerm(cycl = "#$Bird")
  private final KbCollection bird = KbCollectionImpl.get("Bird");

  @CycTerm(cycl = "#$Emu")
  private final KbCollection emu = KbCollectionImpl.get("Emu");

  @CycTerm(cycl = "#$Zebra")
  private final KbCollection zebra = KbCollectionImpl.get("Zebra");

  @CycTerm(cycl = "#$GeneralCycKETask-Allotment")
  public final KbIndividual generalCycKE = KbIndividualImpl.get("GeneralCycKETask-Allotment");

  @CycTerm(cycl = "#$AbrahamLincoln")
  public final KbIndividual abrahamLincoln = KbIndividualImpl.get("AbrahamLincoln");

  @CycTerm(cycl = "#$Plant")
  public final KbCollectionImpl plant = KbCollectionImpl.get("Plant");

  @CycTerm(cycl = "#$Animal")
  private final KbCollectionImpl animal = KbCollectionImpl.get("Animal");

  // (#$TheFn #$Animal) isn't in the OCyc 5.0 KB, so we add it if necessary - nwinant, 2015-04-17
  @CycTerm(cycl = "(#$TheFn #$Animal)")
  public final KbIndividual theAnimal = KbIndividualImpl.findOrCreate("(TheFn Animal)");

  public final Sentence theAnimalIsAnAnimal
          = new SentenceImpl(Constants.isa(), theAnimal, animal);

  public final Sentence xIsAnAnimal
          = new SentenceImpl(Constants.isa(), new VariableImpl("X"), animal);

  @CycTerm(cycl = "#$indexicalReferent")
  public final KbPredicate indexicalReferent = BinaryPredicateImpl.get("indexicalReferent");

  @CycTerm(cycl = "#$Now-Indexical")
  public final KbIndividual nowIndexical = KbIndividualImpl.get("Now-Indexical");
  public final Sentence whatTimeIsIt
          = new SentenceImpl(indexicalReferent, nowIndexical, new VariableImpl("NOW"));

  public final SentenceImpl yOwnsX = new SentenceImpl(BinaryPredicateImpl.get("owns"),
          new VariableImpl("Y"), new VariableImpl("X"));

  @CycTerm(cycl = "#$cellHasNumberOfChromosomes")
  public final KbPredicate cellHasNumberOfChromosomes = KbPredicateImpl.get("cellHasNumberOfChromosomes");

  @CycTerm(cycl = "#$assertionSentence")
  public final KbPredicate assertionSentence = KbPredicateImpl.get("assertionSentence");

  @CycTerm(cycl = "#$testQuerySpecification")
  public final KbPredicate testQuerySpecification = KbPredicateImpl.get("testQuerySpecification");

  @CycTerm(cycl = "#$testAnswers-Cardinality-Exact")
  public final KbPredicate testAnswersCardinalityExact = KbPredicateImpl.get(
          "testAnswers-Cardinality-Exact");

  @CycTerm(cycl = "#$testAnswers-Cardinality-Min")
  public final KbPredicateImpl testAnswersCardinalityMin = KbPredicateImpl.get(
          "testAnswers-Cardinality-Min");

  @CycTerm(cycl = "#$testAnswersCycL-Exact")
  public final KbPredicate testAnswersCycLExact = KbPredicateImpl.get("testAnswersCycL-Exact");

  @CycTerm(cycl = "#$testAnswersCycL-Wanted")
  public final KbPredicate testAnswersCycLWanted = KbPredicateImpl.get("testAnswersCycL-Wanted");

  @CycTerm(cycl = "#$equals")
  public KbPredicate cycEquals = KbPredicateImpl.get("equals");

  @CycTerm(cycl = "#$comment")
  public KbPredicate comment = KbPredicateImpl.get("comment");

  @CycTerm(cycl = "#$evaluate")
  public KbPredicate evaluate = KbPredicateImpl.get("evaluate");

  @CycTerm(cycl = "#$academyAwardWinner")
  public KbPredicate academyAwardWinner = KbPredicateImpl.get("academyAwardWinner");

  @CycTerm(cycl = "#$True")
  public KbIndividual cycTrue = KbIndividualImpl.get("True");

  @CycTerm(cycl = "(#$DifferenceFn 3 1)")
  public KbIndividual threeMinusOne = KbIndividualImpl.get("(#$DifferenceFn 3 1)");

  @CycTerm(cycl = "(#$DifferenceFn 1 0.5)")
  public KbIndividual oneMinusPointFive = KbIndividualImpl.get("(#$DifferenceFn 1 0.5)");

  @CycTerm(cycl = "#$Thing")
  public KbCollection thing = KbCollectionImpl.get("Thing");

  @CycTerm(cycl = "#$BillClinton")
  public KbIndividual billClinton = KbIndividualImpl.get("BillClinton");

  @CycTerm(cycl = "(#$AssistedReaderSourceSpindleCollectorForTaskFn #$GeneralCycKETask-Allotment)")
  public Context generalCycKECollector = ContextImpl.get(
          "(AssistedReaderSourceSpindleCollectorForTaskFn GeneralCycKETask-Allotment)");

  public final Sentence genlsEmuBird = new SentenceImpl(Constants.genls(), emu, bird);
  public final Sentence genlsAnimalX = new SentenceImpl(Constants.genls(), animal, new VariableImpl("X"));
  public final Sentence academyAwardWinners = new SentenceImpl(
          academyAwardWinner, new VariableImpl("X"), new VariableImpl("Y"), new VariableImpl("Z"));
  public final Sentence genlsSpecGenls = SentenceFactory
          .and(
          getSentence(genls(), getVariable("?SPEC"), getVariable("?GENLS")),
          getSentence(genls(), animal, getVariable("?SPEC")),
          getSentence(different(), getVariable("?SPEC"), getVariable("?GENLS")));
  
  public final Sentence whatIsAbe = new SentenceImpl(
          Constants.isa(), abrahamLincoln, new VariableImpl("TYPE"));

  public final Sentence queryAnimals = new SentenceImpl(
          CommonConstants.ELEMENT_OF, new VariableImpl("N"),
          KbTermImpl.get("(TheSet Emu Zebra)"));

  @CycTerm(cycl="#$GeneralCycKETask-Allotment")
  public final KbIndividual GENERAL_CYC_KE_TASK_ALLOTMENT = KbIndividualImpl.get("GeneralCycKETask-Allotment");
  
  
  @CycTerm(cycl="#$cellHasNumberOfChromosomes")
  public final KbPredicate CELL_HAS_NUMBER_OF_CHROMOSOMES =  KbPredicateImpl.get("cellHasNumberOfChromosomes");
  private static QueryTestConstants instance = null;

  @CycTerm(cycl = "#$EnglishParaphraseMt")
  public Context englishParaphraseMt = ContextImpl.get("EnglishParaphraseMt");

  @CycTerm(cycl = "#$PeopleDataMt")
  public Context peopleDataMt = ContextImpl.get("PeopleDataMt");

  private QueryTestConstants() throws KbTypeException, CreateException, KbException {
    super();
  }

  public static KbCollection bird() throws KbException {
    return getInstance().bird;
  }

  public static KbCollection emu() throws KbException {
    return getInstance().emu;
  }

  public static KbCollection zebra() throws KbException {
    return getInstance().zebra;
  }

  public static KbCollection animal() throws KbException {
    return getInstance().animal;
  }

}
