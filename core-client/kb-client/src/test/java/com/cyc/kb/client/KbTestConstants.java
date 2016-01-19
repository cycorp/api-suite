/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cyc.kb.client;

/*
 * #%L
 * File: KbTestConstants.java
 * Project: KB Client
 * %%
 * Copyright (C) 2013 - 2016 Cycorp, Inc
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
import com.cyc.kb.KbCollection;
import com.cyc.kb.KbFunction;
import com.cyc.kb.KbIndividual;
import com.cyc.kb.KbPredicate;
import com.cyc.kb.exception.KbException;
import com.cyc.kb.exception.KbRuntimeException;
import java.util.logging.Logger;

/**
 *
 * @author vijay
 */
@CycObjectLibrary(accessor="getInstance")
public class KbTestConstants {
  
  private static KbTestConstants instance;
  
  @CycTerm(cycl="#$City")
  public final KbCollection city = KbCollectionImpl.get("City");
  
  @CycTerm(cycl="#$Person")
  public final KbCollection person = KbCollectionImpl.get("Person");
  
  @CycTerm(cycl="#$AirTransportationDevice")
  public final KbCollection airTransportationDevice = KbCollectionImpl.get("AirTransportationDevice");
  
  @CycTerm(cycl="#$holdsIn")
  public final KbPredicate holdsIn = KbPredicateImpl.get("holdsIn");
  
  @CycTerm(cycl="#$artifactFoundInLocation")
  public final KbPredicate artifactFoundInLocation = KbPredicateImpl.get("artifactFoundInLocation");
  
  @CycTerm(cycl="#$endingDate")
  public final KbPredicate endingDate = KbPredicateImpl.get("endingDate");
  
  @CycTerm(cycl="#$startingDate")
  public final KbPredicate startingDate = KbPredicateImpl.get("startingDate");
  
  @CycTerm(cycl="#$assertionUtility")
  public final KbPredicate assertionUtility = KbPredicateImpl.get("assertionUtility");
  
  @CycTerm(cycl="#$exampleNATs")
  public final KbPredicate exampleNATS = KbPredicateImpl.get("exampleNATs");
  
  @CycTerm(cycl="#$DatabaseTable")
  public final KbCollection databaseTable = KbCollectionImpl.get("DatabaseTable");
  
  @CycTerm(cycl="#$SimpleDatabase")
  public final KbCollection simpleDatabase = KbCollectionImpl.get("SimpleDatabase");
    
  @CycTerm(cycl="#$tableFieldNameList")
  public final KbPredicate tableFieldNameList = KbPredicateImpl.get("tableFieldNameList");
  
  @CycTerm(cycl="#$MappingMtFn")
  public final KbFunction MappingMtFn = KbFunctionImpl.get("MappingMtFn");
  
  @CycTerm(cycl="#$LogicalSchema")
  public final KbCollection logicalSchema = KbCollectionImpl.get("LogicalSchema");
    
  @CycTerm(cycl="#$meaningSentenceOfSchema")
  public final KbPredicateImpl meaningSentenceOfSchema = KbPredicateImpl.get("meaningSentenceOfSchema");
  
  @CycTerm(cycl="#$Product")
  public final KbCollection Product = KbCollectionImpl.get("Product");
    
  @CycTerm(cycl="#$PersonTypeByRegionalAffiliation")
  public final KbCollection PersonTypeByRegionalAffiliation = KbCollectionImpl.get("PersonTypeByRegionalAffiliation");
  
  @CycTerm(cycl="#$Emu")
  public final KbCollection Emu = KbCollectionImpl.get("Emu");
  
  @CycTerm(cycl="#$BiologicalSpecies")
  public final KbCollection BiologicalSpecies = KbCollectionImpl.get("BiologicalSpecies");
  
  @CycTerm(cycl="#$Volume")
  public final KbCollection Volume = KbCollectionImpl.get("Volume");
  
  @CycTerm(cycl="(#$CollectionUnionFn (#$TheSet #$DurableGood #$ServiceEvent #$Product))")
  public final KbCollection SetOfDGSEP = KbCollectionImpl.get("(CollectionUnionFn (TheSet DurableGood ServiceEvent Product))");

  @CycTerm(cycl="#$TemporallyExistingThing")
  public final KbCollection TemporallyExistingThing = KbCollectionImpl.get("TemporallyExistingThing");

  @CycTerm(cycl="#$Location-Underspecified")
  public final KbCollection LocationUnderspecified = KbCollectionImpl.get("Location-Underspecified");
  
  @CycTerm(cycl="#$Trajector-Underspecified")
  public final KbCollection TrajectorUnderspecified = KbCollectionImpl.get("Trajector-Underspecified");
  
  @CycTerm(cycl="(#$CollectionUnionFn (#$TheSet #$TemporalThing #$Collection))")
  public final KbCollection SetOfTTC = KbCollectionImpl.get("(CollectionUnionFn (TheSet TemporalThing Collection))");
  
  @CycTerm(cycl="#$TemporalThing")
  public final KbCollection TemporalThing = KbCollectionImpl.get("TemporalThing");

  @CycTerm(cycl="#$HumanCyclist")
  public final KbCollection HumanCyclist = KbCollectionImpl.get("HumanCyclist");

  @CycTerm(cycl="#$ReifiableFunction")
  public final KbCollection ReifiableFunction = KbCollectionImpl.get("ReifiableFunction");
  
  @CycTerm(cycl="#$UnaryFunction")
  public final KbCollection UnaryFunction = KbCollectionImpl.get("UnaryFunction");

  @CycTerm(cycl="#$ComputerProgramTypeByPlatform")
  public final KbCollection ComputerProgramTypeByPlatform = KbCollectionImpl.get("ComputerProgramTypeByPlatform");
  
  @CycTerm(cycl="#$AppleTree")
  public final KbCollection AppleTree = KbCollectionImpl.get("AppleTree");
  @CycTerm(cycl="#$Person")
  public final KbCollection Person = KbCollectionImpl.get("Person");
  @CycTerm(cycl="#$MonetaryValue")
  public final KbCollection MonetaryValue = KbCollectionImpl.get("MonetaryValue");
  @CycTerm(cycl="#$NumericInterval")
  public final KbCollection NumericInterval = KbCollectionImpl.get("NumericInterval");
  @CycTerm(cycl="#$owns")
  public final KbPredicate owns = KbPredicateImpl.get("owns");
  @CycTerm(cycl="#$revenueForPeriodByAccountingCOC")
  public final KbPredicate revenueForPeriodByAccountingCOC = KbPredicateImpl.get("revenueForPeriodByAccountingCOC");
  @CycTerm(cycl="#$argsIsa")
  public final KbPredicate argsIsa = KbPredicateImpl.get("argsIsa");
  @CycTerm(cycl="#$FruitFn")
  public final KbFunction FruitFn = KbFunctionImpl.get("FruitFn");
  @CycTerm(cycl="#$CitizenFn")
  public final KbFunction CitizenFn = KbFunctionImpl.get("CitizenFn");
  @CycTerm(cycl="#$USDollarFn")
  public final KbFunction USDollarFn = KbFunctionImpl.get("USDollarFn");
  @CycTerm(cycl="#$FiscalYearFn")
  public final KbFunction FiscalYearFn = KbFunctionImpl.get("FiscalYearFn");
  @CycTerm(cycl="#$UnitedStatesOfAmerica")
  public final KbIndividual UnitedStatesOfAmerica = KbIndividualImpl.get("UnitedStatesOfAmerica");
  @CycTerm(cycl="#$Walmart-CommercialOrganization")
  public final KbIndividual WalmartCommercialOrganization = KbIndividualImpl.get("Walmart-CommercialOrganization");
  @CycTerm(cycl="#$ThePhysicalFieldValueFn")
  public final KbFunction ThePhysicalFieldValueFn = KbFunctionImpl.get("ThePhysicalFieldValueFn");
  @CycTerm(cycl="#$PhysicalSchema")
  public final KbCollection PhysicalSchema = KbCollectionImpl.get("PhysicalSchema");
  @CycTerm(cycl="#$TheLogicalFieldValueFn")
  public final KbFunction TheLogicalFieldValueFn = KbFunctionImpl.get("TheLogicalFieldValueFn");
  @CycTerm(cycl="#$LogicalSchema")
  public final KbCollection LogicalSchema = KbCollectionImpl.get("LogicalSchema");
  @CycTerm(cycl="#$Set-Mathematical")
  public final KbCollection SetMathematical = KbCollectionImpl.get("Set-Mathematical");
  @CycTerm(cycl="#$AlbertEinstein")
  public final KbIndividual AlbertEinstein = KbIndividualImpl.get("AlbertEinstein");
  @CycTerm(cycl="(#$GenericInstanceFn #$Dog)")
  public final KbIndividual GenericInstanceFnDog = KbIndividualImpl.get("(GenericInstanceFn Dog)");
  @CycTerm(cycl="#$GenericInstanceFn")
  public final KbFunction GenericInstanceFn = KbFunctionImpl.get("GenericInstanceFn");
  @CycTerm(cycl="#$Dog")
  public final KbCollection Dog = KbCollectionImpl.get("Dog");
  @CycTerm(cycl="#$Scientist")
  public final KbCollection Scientist = KbCollectionImpl.get("Scientist");
  //@CycTerm(cycl="#$Nonsense")
  //public final KBCollection Nonsense = KBCollectionImpl.get("Nonsense");
  @CycTerm(cycl="#$CanisGenus")
  public final KbCollection CanisGenus = KbCollectionImpl.get("CanisGenus");
  @CycTerm(cycl="#$physicalParts")
  public final KbPredicate physicalParts = KbPredicateImpl.get("physicalParts");
  @CycTerm(cycl="#$Heart")
  public final KbCollection Heart = KbCollectionImpl.get("Heart");
  @CycTerm(cycl="#$Supracommissure")
  public final KbCollection Supracommissure = KbCollectionImpl.get("Supracommissure");
  //@CycTerm(cycl="#$Pluto")
  //public final KBIndividual Pluto = KBIndividualImpl.get("Pluto");

  @CycTerm(cycl="(#$SubcollectionOfWithRelationToTypeFn #$PettingAnAnimal #$doneBy #$Person)")
  public final KbCollection SubcolPAnimalPerson = KbCollectionImpl.get("(SubcollectionOfWithRelationToTypeFn PettingAnAnimal doneBy Person)");
  @CycTerm(cycl="#$SubcollectionOfWithRelationToTypeFn")
  public final KbFunction SubcollectionOfWithRelationToTypeFn = KbFunctionImpl.get("SubcollectionOfWithRelationToTypeFn");
  @CycTerm(cycl="#$doneBy")
  public final KbPredicate doneBy = KbPredicateImpl.get("doneBy");
  @CycTerm(cycl="#$PettingAnAnimal")
  public final KbCollection PettingAnAnimal = KbCollectionImpl.get("PettingAnAnimal");
  @CycTerm(cycl="#$ConceptualWork")
  public final KbCollection ConceptualWork = KbCollectionImpl.get("ConceptualWork");
  @CycTerm(cycl="#$beliefs")
  public final KbPredicate beliefs = KbPredicateImpl.get("beliefs");
  @CycTerm(cycl="#$MinuteFn")
  public final KbFunction MinuteFn = KbFunctionImpl.get("MinuteFn");
  @CycTerm(cycl="#$HourFn")
  public final KbFunction HourFn = KbFunctionImpl.get("HourFn");
  @CycTerm(cycl="#$DayFn")
  public final KbFunction DayFn = KbFunctionImpl.get("DayFn");
  @CycTerm(cycl="#$MonthFn")
  public final KbFunction MonthFn = KbFunctionImpl.get("MonthFn");
  @CycTerm(cycl="#$YearFn")
  public final KbFunction YearFn = KbFunctionImpl.get("YearFn");
  @CycTerm(cycl="#$CommercialAircraft")
  public final KbCollection CommercialAircraft = KbCollectionImpl.get("CommercialAircraft");
  @CycTerm(cycl="#$TheEmptyList")
  public final KbIndividual TheEmptyList = KbIndividualImpl.get("TheEmptyList");
  @CycTerm(cycl="#$likesObject")
  public final KbPredicate likesObject = KbPredicateImpl.get("likesObject");
  @CycTerm(cycl="#$BillClinton")
  public final KbIndividual BillClinton = KbIndividualImpl.get("BillClinton");
  @CycTerm(cycl="#$arityMax")
  public final KbPredicate arityMax = KbPredicateImpl.get("arityMax");
  @CycTerm(cycl="#$arityMin")
  public final KbPredicate arityMin = KbPredicateImpl.get("arityMin");
  @CycTerm(cycl="#$sentenceParameterValueInSpecification")
  public final KbPredicate sentenceParameterValueInSpecification = KbPredicateImpl.get("sentenceParameterValueInSpecification");
  @CycTerm(cycl="#$Quote")
  public final KbFunction Quote = KbFunctionImpl.get("Quote");
  @CycTerm(cycl="#$AttackByComputerOperation")
  public final KbCollection AttackByComputerOperation = KbCollectionImpl.get("AttackByComputerOperation");
  @CycTerm(cycl="#$QuantificationalRuleMacroPredicate-Canonical")
  public final KbCollection QuantificationalRuleMacroPredicateCanonical = KbCollectionImpl.get("QuantificationalRuleMacroPredicate-Canonical");
  @CycTerm(cycl="#$Event")
  public final KbCollection Event = KbCollectionImpl.get("Event");

  private static Logger log = Logger.getLogger(KbTestConstants.class.getName());
  private KbTestConstants() throws KbException {
    super();
  }

  /**
   * This not part of the public, supported KB API
   * 
   * @return a instance of Constants class which instantiates the following commonly used
   * KB terms.
   * 
   * @throws KbRuntimeException
   */
  public static KbTestConstants getInstance() throws KbRuntimeException {
    try {
      log.info("Instantiating KBAPITestConstants..");
      if (instance == null) {
        instance = new KbTestConstants();
      }
      log.info("DONE: Instantiating KBAPITestConstants..");
      return instance;
    } catch (KbException e) {
      throw new KbRuntimeException(
          "One of the final fields in com.cyc.kb.client.KBAPITestConstants.java could not be instantiated, can not proceed further.",
          e);
    }
  }
}
