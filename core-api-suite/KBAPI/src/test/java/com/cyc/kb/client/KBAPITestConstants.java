/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cyc.kb.client;

/*
 * #%L
 * File: KBAPITestConstants.java
 * Project: KB API Implementation
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
import com.cyc.kb.KBCollection;
import com.cyc.kb.KBFunction;
import com.cyc.kb.KBIndividual;
import com.cyc.kb.KBPredicate;
import com.cyc.kb.exception.KBApiException;
import com.cyc.kb.exception.KBApiRuntimeException;
import java.util.logging.Logger;

/**
 *
 * @author vijay
 */
@CycObjectLibrary(accessor="getInstance")
public class KBAPITestConstants {
  
  private static KBAPITestConstants instance;
  
  @CycTerm(cycl="#$City")
  public final KBCollection city = KBCollectionImpl.get("City");
  
  @CycTerm(cycl="#$Person")
  public final KBCollection person = KBCollectionImpl.get("Person");
  
  @CycTerm(cycl="#$AirTransportationDevice")
  public final KBCollection airTransportationDevice = KBCollectionImpl.get("AirTransportationDevice");
  
  @CycTerm(cycl="#$holdsIn")
  public final KBPredicate holdsIn = KBPredicateImpl.get("holdsIn");
  
  @CycTerm(cycl="#$artifactFoundInLocation")
  public final KBPredicate artifactFoundInLocation = KBPredicateImpl.get("artifactFoundInLocation");
  
  @CycTerm(cycl="#$endingDate")
  public final KBPredicate endingDate = KBPredicateImpl.get("endingDate");
  
  @CycTerm(cycl="#$startingDate")
  public final KBPredicate startingDate = KBPredicateImpl.get("startingDate");
  
  @CycTerm(cycl="#$assertionUtility")
  public final KBPredicate assertionUtility = KBPredicateImpl.get("assertionUtility");
  
  @CycTerm(cycl="#$exampleNATs")
  public final KBPredicate exampleNATS = KBPredicateImpl.get("exampleNATs");
  
  @CycTerm(cycl="#$DatabaseTable")
  public final KBCollection databaseTable = KBCollectionImpl.get("DatabaseTable");
  
  @CycTerm(cycl="#$SimpleDatabase")
  public final KBCollection simpleDatabase = KBCollectionImpl.get("SimpleDatabase");
    
  @CycTerm(cycl="#$tableFieldNameList")
  public final KBPredicate tableFieldNameList = KBPredicateImpl.get("tableFieldNameList");
  
  @CycTerm(cycl="#$MappingMtFn")
  public final KBFunction MappingMtFn = KBFunctionImpl.get("MappingMtFn");
  
  @CycTerm(cycl="#$LogicalSchema")
  public final KBCollection logicalSchema = KBCollectionImpl.get("LogicalSchema");
    
  @CycTerm(cycl="#$meaningSentenceOfSchema")
  public final KBPredicateImpl meaningSentenceOfSchema = KBPredicateImpl.get("meaningSentenceOfSchema");
  
  @CycTerm(cycl="#$Product")
  public final KBCollection Product = KBCollectionImpl.get("Product");
    
  @CycTerm(cycl="#$PersonTypeByRegionalAffiliation")
  public final KBCollection PersonTypeByRegionalAffiliation = KBCollectionImpl.get("PersonTypeByRegionalAffiliation");
  
  @CycTerm(cycl="#$Emu")
  public final KBCollection Emu = KBCollectionImpl.get("Emu");
  
  @CycTerm(cycl="#$BiologicalSpecies")
  public final KBCollection BiologicalSpecies = KBCollectionImpl.get("BiologicalSpecies");
  
  @CycTerm(cycl="#$Volume")
  public final KBCollection Volume = KBCollectionImpl.get("Volume");
  
  @CycTerm(cycl="(#$CollectionUnionFn (#$TheSet #$DurableGood #$ServiceEvent #$Product))")
  public final KBCollection SetOfDGSEP = KBCollectionImpl.get("(CollectionUnionFn (TheSet DurableGood ServiceEvent Product))");

  @CycTerm(cycl="#$TemporallyExistingThing")
  public final KBCollection TemporallyExistingThing = KBCollectionImpl.get("TemporallyExistingThing");

  @CycTerm(cycl="#$Location-Underspecified")
  public final KBCollection LocationUnderspecified = KBCollectionImpl.get("Location-Underspecified");
  
  @CycTerm(cycl="#$Trajector-Underspecified")
  public final KBCollection TrajectorUnderspecified = KBCollectionImpl.get("Trajector-Underspecified");
  
  @CycTerm(cycl="(#$CollectionUnionFn (#$TheSet #$TemporalThing #$Collection))")
  public final KBCollection SetOfTTC = KBCollectionImpl.get("(CollectionUnionFn (TheSet TemporalThing Collection))");
  
  @CycTerm(cycl="#$TemporalThing")
  public final KBCollection TemporalThing = KBCollectionImpl.get("TemporalThing");

  @CycTerm(cycl="#$HumanCyclist")
  public final KBCollection HumanCyclist = KBCollectionImpl.get("HumanCyclist");

  @CycTerm(cycl="#$ReifiableFunction")
  public final KBCollection ReifiableFunction = KBCollectionImpl.get("ReifiableFunction");
  
  @CycTerm(cycl="#$UnaryFunction")
  public final KBCollection UnaryFunction = KBCollectionImpl.get("UnaryFunction");

  @CycTerm(cycl="#$ComputerProgramTypeByPlatform")
  public final KBCollection ComputerProgramTypeByPlatform = KBCollectionImpl.get("ComputerProgramTypeByPlatform");
  
  @CycTerm(cycl="#$AppleTree")
  public final KBCollection AppleTree = KBCollectionImpl.get("AppleTree");
  @CycTerm(cycl="#$Person")
  public final KBCollection Person = KBCollectionImpl.get("Person");
  @CycTerm(cycl="#$MonetaryValue")
  public final KBCollection MonetaryValue = KBCollectionImpl.get("MonetaryValue");
  @CycTerm(cycl="#$NumericInterval")
  public final KBCollection NumericInterval = KBCollectionImpl.get("NumericInterval");
  @CycTerm(cycl="#$owns")
  public final KBPredicate owns = KBPredicateImpl.get("owns");
  @CycTerm(cycl="#$revenueForPeriodByAccountingCOC")
  public final KBPredicate revenueForPeriodByAccountingCOC = KBPredicateImpl.get("revenueForPeriodByAccountingCOC");
  @CycTerm(cycl="#$argsIsa")
  public final KBPredicate argsIsa = KBPredicateImpl.get("argsIsa");
  @CycTerm(cycl="#$FruitFn")
  public final KBFunction FruitFn = KBFunctionImpl.get("FruitFn");
  @CycTerm(cycl="#$CitizenFn")
  public final KBFunction CitizenFn = KBFunctionImpl.get("CitizenFn");
  @CycTerm(cycl="#$USDollarFn")
  public final KBFunction USDollarFn = KBFunctionImpl.get("USDollarFn");
  @CycTerm(cycl="#$FiscalYearFn")
  public final KBFunction FiscalYearFn = KBFunctionImpl.get("FiscalYearFn");
  @CycTerm(cycl="#$UnitedStatesOfAmerica")
  public final KBIndividual UnitedStatesOfAmerica = KBIndividualImpl.get("UnitedStatesOfAmerica");
  @CycTerm(cycl="#$Walmart-CommercialOrganization")
  public final KBIndividual WalmartCommercialOrganization = KBIndividualImpl.get("Walmart-CommercialOrganization");
  @CycTerm(cycl="#$ThePhysicalFieldValueFn")
  public final KBFunction ThePhysicalFieldValueFn = KBFunctionImpl.get("ThePhysicalFieldValueFn");
  @CycTerm(cycl="#$PhysicalSchema")
  public final KBCollection PhysicalSchema = KBCollectionImpl.get("PhysicalSchema");
  @CycTerm(cycl="#$TheLogicalFieldValueFn")
  public final KBFunction TheLogicalFieldValueFn = KBFunctionImpl.get("TheLogicalFieldValueFn");
  @CycTerm(cycl="#$LogicalSchema")
  public final KBCollection LogicalSchema = KBCollectionImpl.get("LogicalSchema");
  @CycTerm(cycl="#$Set-Mathematical")
  public final KBCollection SetMathematical = KBCollectionImpl.get("Set-Mathematical");
  @CycTerm(cycl="#$AlbertEinstein")
  public final KBIndividual AlbertEinstein = KBIndividualImpl.get("AlbertEinstein");
  @CycTerm(cycl="(#$GenericInstanceFn #$Dog)")
  public final KBIndividual GenericInstanceFnDog = KBIndividualImpl.get("(GenericInstanceFn Dog)");
  @CycTerm(cycl="#$GenericInstanceFn")
  public final KBFunction GenericInstanceFn = KBFunctionImpl.get("GenericInstanceFn");
  @CycTerm(cycl="#$Dog")
  public final KBCollection Dog = KBCollectionImpl.get("Dog");
  @CycTerm(cycl="#$Scientist")
  public final KBCollection Scientist = KBCollectionImpl.get("Scientist");
  //@CycTerm(cycl="#$Nonsense")
  //public final KBCollection Nonsense = KBCollectionImpl.get("Nonsense");
  @CycTerm(cycl="#$CanisGenus")
  public final KBCollection CanisGenus = KBCollectionImpl.get("CanisGenus");
  @CycTerm(cycl="#$physicalParts")
  public final KBPredicate physicalParts = KBPredicateImpl.get("physicalParts");
  @CycTerm(cycl="#$Heart")
  public final KBCollection Heart = KBCollectionImpl.get("Heart");
  @CycTerm(cycl="#$Supracommissure")
  public final KBCollection Supracommissure = KBCollectionImpl.get("Supracommissure");
  //@CycTerm(cycl="#$Pluto")
  //public final KBIndividual Pluto = KBIndividualImpl.get("Pluto");

  @CycTerm(cycl="(#$SubcollectionOfWithRelationToTypeFn #$PettingAnAnimal #$doneBy #$Person)")
  public final KBCollection SubcolPAnimalPerson = KBCollectionImpl.get("(SubcollectionOfWithRelationToTypeFn PettingAnAnimal doneBy Person)");
  @CycTerm(cycl="#$SubcollectionOfWithRelationToTypeFn")
  public final KBFunction SubcollectionOfWithRelationToTypeFn = KBFunctionImpl.get("SubcollectionOfWithRelationToTypeFn");
  @CycTerm(cycl="#$doneBy")
  public final KBPredicate doneBy = KBPredicateImpl.get("doneBy");
  @CycTerm(cycl="#$PettingAnAnimal")
  public final KBCollection PettingAnAnimal = KBCollectionImpl.get("PettingAnAnimal");
  @CycTerm(cycl="#$ConceptualWork")
  public final KBCollection ConceptualWork = KBCollectionImpl.get("ConceptualWork");
  @CycTerm(cycl="#$beliefs")
  public final KBPredicate beliefs = KBPredicateImpl.get("beliefs");
  @CycTerm(cycl="#$MinuteFn")
  public final KBFunction MinuteFn = KBFunctionImpl.get("MinuteFn");
  @CycTerm(cycl="#$HourFn")
  public final KBFunction HourFn = KBFunctionImpl.get("HourFn");
  @CycTerm(cycl="#$DayFn")
  public final KBFunction DayFn = KBFunctionImpl.get("DayFn");
  @CycTerm(cycl="#$MonthFn")
  public final KBFunction MonthFn = KBFunctionImpl.get("MonthFn");
  @CycTerm(cycl="#$YearFn")
  public final KBFunction YearFn = KBFunctionImpl.get("YearFn");
  @CycTerm(cycl="#$CommercialAircraft")
  public final KBCollection CommercialAircraft = KBCollectionImpl.get("CommercialAircraft");
  @CycTerm(cycl="#$TheEmptyList")
  public final KBIndividual TheEmptyList = KBIndividualImpl.get("TheEmptyList");
  @CycTerm(cycl="#$likesObject")
  public final KBPredicate likesObject = KBPredicateImpl.get("likesObject");
  @CycTerm(cycl="#$BillClinton")
  public final KBIndividual BillClinton = KBIndividualImpl.get("BillClinton");
  @CycTerm(cycl="#$arityMax")
  public final KBPredicate arityMax = KBPredicateImpl.get("arityMax");
  @CycTerm(cycl="#$arityMin")
  public final KBPredicate arityMin = KBPredicateImpl.get("arityMin");
  @CycTerm(cycl="#$sentenceParameterValueInSpecification")
  public final KBPredicate sentenceParameterValueInSpecification = KBPredicateImpl.get("sentenceParameterValueInSpecification");
  @CycTerm(cycl="#$Quote")
  public final KBFunction Quote = KBFunctionImpl.get("Quote");
  @CycTerm(cycl="#$AttackByComputerOperation")
  public final KBCollection AttackByComputerOperation = KBCollectionImpl.get("AttackByComputerOperation");
  @CycTerm(cycl="#$QuantificationalRuleMacroPredicate-Canonical")
  public final KBCollection QuantificationalRuleMacroPredicateCanonical = KBCollectionImpl.get("QuantificationalRuleMacroPredicate-Canonical");
  @CycTerm(cycl="#$Event")
  public final KBCollection Event = KBCollectionImpl.get("Event");

  private static Logger log = Logger.getLogger(KBAPITestConstants.class.getName());
  private KBAPITestConstants() throws KBApiException {
    super();
  }

  /**
   * This not part of the public, supported KB API
   * 
   * @return a instance of Constants class which instantiates the following commonly used
   * KB terms.
   * 
   * @throws KBApiRuntimeException
   */
  public static KBAPITestConstants getInstance() throws KBApiRuntimeException {
    try {
      log.info("Instantiating KBAPITestConstants..");
      if (instance == null) {
        instance = new KBAPITestConstants();
      }
      log.info("DONE: Instantiating KBAPITestConstants..");
      return instance;
    } catch (KBApiException e) {
      throw new KBApiRuntimeException(
          "One of the final fields in com.cyc.kb.client.KBAPITestConstants.java could not be instantiated, can not proceed further.",
          e);
    }
  }
}
