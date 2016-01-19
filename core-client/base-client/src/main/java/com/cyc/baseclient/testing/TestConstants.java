package com.cyc.baseclient.testing;

/*
 * #%L
 * File: TestConstants.java
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

import com.cyc.base.annotation.CycObjectLibrary;
import com.cyc.base.cycobject.CycConstant;
import com.cyc.baseclient.CycObjectFactory;
import com.cyc.baseclient.cycobject.CycConstantImpl;
import com.cyc.baseclient.cycobject.GuidImpl;
import com.cyc.base.annotation.CycTerm;
import com.cyc.base.cycobject.CycVariable;
import com.cyc.base.cycobject.ElMt;
import com.cyc.baseclient.cycobject.CycVariableImpl;
import com.cyc.baseclient.cycobject.ElMtConstant;

/**
 *
 * @author nwinant
 */
@CycObjectLibrary(requireFieldAnnotations=false)
public class TestConstants {
  
  @Deprecated
  @CycTerm(cycl="#$CIAWorldFactbook1995Mt", includedInOpenCycKB=false)
  public static final CycConstant CIA_WORLD_FACTBOOK_1995_MT =
          new CycConstantImpl("CIAWorldFactbook1995Mt", new GuidImpl(
                  "c0a41a91-9c29-11b1-9dad-c379636f7270"));
  
  public static final CycConstant DOLLAR =
          new CycConstantImpl("USDollarFn", new GuidImpl(
                  "bf8330c4-9c29-11b1-9dad-c379636f7270"));
  
  public static final CycConstant TAME_ANIMAL =
          new CycConstantImpl("TameAnimal", new GuidImpl(
                  "c0fcd4a1-9c29-11b1-9dad-c379636f7270"));
  
  public static final CycConstant DOG =
          new CycConstantImpl("#$Dog", CycObjectFactory.makeGuid(
                  "bd58daa0-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$Muffet")
  public static final CycConstant MUFFET =
          new CycConstantImpl("Muffet", CycObjectFactory.makeGuid(
                  "c0f7d2cc-9c29-11b1-9dad-c379636f7270"));
  
  public static final CycConstant CAT =
          new CycConstantImpl("#$Cat", CycObjectFactory.makeGuid(
                  "bd590573-9c29-11b1-9dad-c379636f7270"));
  
  public static final CycConstant BRAZIL =
          new CycConstantImpl("#$Brazil", CycObjectFactory.makeGuid(
                  "bd588f01-9c29-11b1-9dad-c379636f7270"));
  
  public static final CycConstantImpl TRANSPORTATION_DEVICE_VEHICLE =
          new CycConstantImpl("#$TransportationDevice-Vehicle",
                  CycObjectFactory.makeGuid("c0bce169-9c29-11b1-9dad-c379636f7270"));
  
  public static final CycConstant CONVEY_FN =
          new CycConstantImpl("ConveyFn", CycObjectFactory.makeGuid(
                  "c10afb3b-9c29-11b1-9dad-c379636f7270"));
  
  public static final CycConstant THE_LIST =
          new CycConstantImpl("TheList", CycObjectFactory.makeGuid(
                  "bdcc9f7c-9c29-11b1-9dad-c379636f7270"));
  
  public static final CycConstant CITY_NAMED_FN =
          new CycConstantImpl("CityNamedFn", CycObjectFactory.makeGuid(
                  "bd6870a6-9c29-11b1-9dad-c379636f7270"));
  
  public static final CycConstant ONTARIO_CANADIAN_PROVINCE =
          new CycConstantImpl("Ontario-CanadianProvince",
                  CycObjectFactory.makeGuid("bd58b6d5-9c29-11b1-9dad-c379636f7270"));
  
  public static final CycConstant REGISTRY_KEY_FN =
          new CycConstantImpl("RegistryKeyFn", CycObjectFactory.makeGuid(
                  "e475c6b0-1695-11d6-8000-00a0c9efe6b4"));
  
  public static final CycConstant SOMETHING_EXISTING =
          new CycConstantImpl("SomethingExisting", new GuidImpl("bd58b6e7-9c29-11b1-9dad-c379636f7270"));
  
  public static final CycConstant SOUTH_DAKOTA = new CycConstantImpl("SouthDakota-State",
          new GuidImpl("bd58b684-9c29-11b1-9dad-c379636f7270"));
  
  public static final CycConstantImpl CYC_ADMINISTRATOR =
          new CycConstantImpl("CycAdministrator", new GuidImpl("c0bf7a98-9c29-11b1-9dad-c379636f7270"));
  
  public static final CycConstantImpl GENERAL_CYC_KE =
          new CycConstantImpl("GeneralCycKE", new GuidImpl("bd8345f2-9c29-11b1-9dad-c379636f7270"));

  @CycTerm(cycl="#$ChicagoManualOfStyleStandard")
  public static final CycConstantImpl CHICAGO_MANUAL_OF_STYLE_STANDARD =
          new CycConstantImpl("ChicagoManualOfStyleStandard", new GuidImpl("3b8c6b56-e82b-11d9-8000-0002b3a85caa"));

  @CycTerm(cycl="#$Lenat")
  public static final CycConstantImpl LENAT =
          new CycConstantImpl("Lenat", new GuidImpl("bd588052-9c29-11b1-9dad-c379636f7270"));

  @CycTerm(cycl="#$CityOfAustinTX")
  public static final CycConstantImpl CITY_OF_AUSTIN_TX =
          new CycConstantImpl("CityOfAustinTX", new GuidImpl("bd58b939-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$WilliamHenryHarrison")
  public static final CycConstantImpl WILLIAM_HENRY_HARRISON =
          new CycConstantImpl("WilliamHenryHarrison", new GuidImpl("50514f94-8e05-11d6-8000-0002b34b8539"));
  
  @CycTerm(cycl="#$BiologicalLivingObject")
  public static final CycConstantImpl BIOLOGICAL_LIVING_OBJECT =
          new CycConstantImpl("BiologicalLivingObject", new GuidImpl("bd58a6ed-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$objectFoundInLocation")
  public static final CycConstantImpl OBJECT_FOUND_IN_LOCATION =
          new CycConstantImpl("objectFoundInLocation", new GuidImpl("bd58d0e4-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$geopoliticalSubdivision")
  public static final CycConstantImpl GEOPOLITICAL_SUBDIVISION =
          new CycConstantImpl("geopoliticalSubdivision", new GuidImpl("bdf19a4d-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$Texas-State")
  public static final CycConstantImpl TEXAS_STATE =
          new CycConstantImpl("Texas-State", new GuidImpl("bd590193-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$nameString")
  public static final CycConstantImpl NAMESTRING =
          new CycConstantImpl("nameString", new GuidImpl("c0fdf7e8-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$evaluate")
  public static final CycConstantImpl EVALUATE =
          new CycConstantImpl("evaluate", new GuidImpl("c03afa6d-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$Person")
  public static final CycConstantImpl PERSON =
          new CycConstantImpl("Person", new GuidImpl("bd588092-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$Female")
  public static final CycConstantImpl FEMALE =
          new CycConstantImpl("Female", new GuidImpl("f67af796-9091-41d7-80a1-9e1abaf5b6ce"));
  
  @CycTerm(cycl="#$Agent-PartiallyTangible")
  public static final CycConstantImpl AGENT_PARTIALLY_TANGIBLE  =
          new CycConstantImpl("Agent-PartiallyTangible", new GuidImpl("bd588007-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$likesAsFriend")
  public static final CycConstantImpl LIKES_AS_FRIEND =
          new CycConstantImpl("likesAsFriend", new GuidImpl("bd58ba6c-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$Plant")
  public static final CycConstantImpl PLANT =
          new CycConstantImpl("Plant", new GuidImpl("bd58c6e1-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$Animal")
  public static final CycConstantImpl ANIMAL =
          new CycConstantImpl("Animal", new GuidImpl("bd58b031-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$BiologicalSpecies")
  public static final CycConstantImpl BIOLOGICAL_SPECIES =
          new CycConstantImpl("BiologicalSpecies", new GuidImpl("bd58caeb-9c29-11b1-9dad-c379636f7270"));
      
  @CycTerm(cycl="#$DomesticatedAnimal")
  public static final CycConstantImpl DOMESTICATED_ANIMAL = new CycConstantImpl("DomesticatedAnimal",
          new GuidImpl("c10c22cd-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$NonPersonAnimal")
  public static final CycConstantImpl NON_PERSON_ANIMAL = new CycConstantImpl("NonPersonAnimal",
          new GuidImpl("bd58e066-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$FluidFlow-Complete")
  public static final CycConstantImpl FLUID_FLOW_COMPLETE = new CycConstantImpl("FluidFlow-Complete",
          new GuidImpl("bd59050b-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$NthSubSituationTypeOfTypeFn")
  public static final CycConstantImpl NTH_SUB_SITUATION_TYPE_OF_TYPE_FN = new CycConstantImpl("NthSubSituationTypeOfTypeFn",
          new GuidImpl("bd793309-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$PreparingFoodItemFn")
  public static final CycConstantImpl PREPARING_FOOD_ITEM_FN = new CycConstantImpl("PreparingFoodItemFn",
          new GuidImpl("950e12c2-e94c-41d7-86ae-a6bb1a7cca31"));
  
  @CycTerm(cycl="#$SpaghettiMarinara")
  public static final CycConstantImpl SPAGHETTI_MARINARA = new CycConstantImpl("SpaghettiMarinara",
          new GuidImpl("d8b5b65c-e94b-41d7-814a-f52a41f330e1"));
          
  @CycTerm(cycl="#$HumanActivitiesMt")
  public static final CycConstantImpl HUMAN_ACTIVITIES_MT = new CycConstantImpl("HumanActivitiesMt",
          new GuidImpl("bd58fe73-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$RemotelyExploitableFn")
  public static final CycConstantImpl REMOTELY_EXPLOITABLE_FN = new CycConstantImpl("RemotelyExploitableFn",
          new GuidImpl("bfa7d43a-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$VulnerableToDTMLMethodExecution")
  public static final CycConstantImpl VULNERABLE_TO_DTML_METHOD_EXECUTION = new CycConstantImpl("VulnerableToDTMLMethodExecution",
          new GuidImpl("984d9836-3b97-11d6-8000-0001031bfeec"));
  
  @CycTerm(cycl="#$JuvenileFn")
  public static final CycConstantImpl JUVENILE_FN = new CycConstantImpl("JuvenileFn",
          new GuidImpl("c10c2004-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$The")
  public static final CycConstantImpl THE = new CycConstantImpl("The",
          new GuidImpl("bd5880bf-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$UniverseDataMt")
  public static final CycConstantImpl UNIVERSE_DATA_MT = new CycConstantImpl("UniverseDataMt",
          new GuidImpl("bd58d0f3-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$weightOnPlanet")
  public static final CycConstantImpl WEIGHT_ON_PLANET = new CycConstantImpl("weightOnPlanet",
          new GuidImpl("09c94530-5ce5-41d8-81ff-ff75a178ba0c"));
  
  @CycTerm(cycl="#$PlanetMars")
  public static final CycConstantImpl PLANET_MARS = new CycConstantImpl("PlanetMars",
          new GuidImpl("bd59018b-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$PlanetVenus")
  public static final CycConstantImpl PLANET_VENUS = new CycConstantImpl("PlanetVenus",
          new GuidImpl("bd58d1fa-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$suggestionsForPredRelativeToIsaInArg")
  public static final CycConstantImpl SUGGESTION_FOR_PRED_RELATIVE_TO_ISA_IN_ARG =
          new CycConstantImpl("suggestionsForPredRelativeToIsaInArg",
                  new GuidImpl("a702e090-6084-11db-8000-0002b3620a7d"));
  
  @CycTerm(cycl="#$CelestialBody")
  public static final CycConstantImpl CELESTIAL_BODY = new CycConstantImpl("CelestialBody",
          new GuidImpl("bd58d0b3-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl = "#$ModernMilitaryOrganization")
  public static final CycConstantImpl MODERN_MILITARY_ORGANIZATION = new CycConstantImpl("ModernMilitaryOrganization",
          new GuidImpl("bd58f1ae-9c29-11b1-9dad-c379636f7270"));

  @CycTerm(cycl = "#$Illinois-State")
  public static final CycConstantImpl ILLINOIS_STATE = new CycConstantImpl("Illinois-State",
          new GuidImpl("bd58ad94-9c29-11b1-9dad-c379636f7270"));

  @CycTerm(cycl = "#$behaviorCapable")
  public static final CycConstantImpl BEHAVIOR_CAPABLE = new CycConstantImpl("behaviorCapable",
          new GuidImpl("bd5891ae-9c29-11b1-9dad-c379636f7270"));

  @CycTerm(cycl = "#$ReactionToSituationTypeFn")
  public static final CycConstantImpl REACTION_TO_SITUATION_TYPE_FN = new CycConstantImpl("ReactionToSituationTypeFn",
          new GuidImpl("b6f36046-ce8e-41d7-9bfd-e3ab348bb885"));

  @CycTerm(cycl = "#$ChemicalAttack")
  public static final CycConstantImpl CHEMICAL_ATTACK = new CycConstantImpl("ChemicalAttack",
          new GuidImpl("be5a10f2-9c29-11b1-9dad-c379636f7270"));

  @CycTerm(cycl = "#$performedBy")
  public static final CycConstantImpl PERFORMED_BY = new CycConstantImpl("performedBy",
          new GuidImpl("bd58a962-9c29-11b1-9dad-c379636f7270"));

  @CycTerm(cycl="#$CyclistsMt")
  public static final CycConstantImpl CYCLISTS_MT = new CycConstantImpl("CyclistsMt",
          new GuidImpl("c0625164-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$MtTimeWithGranularityDimFn")
  public static final CycConstantImpl MT_TIME_POINT_WITH_GRANULARITY_DIM_FN = new CycConstantImpl("MtTimeWithGranularityDimFn",
          new GuidImpl("47537943-331d-11d7-922f-0002b3a333c3"));
  
  @CycTerm(cycl="#$TimePoint")
  public static final CycConstantImpl TIME_POINT = new CycConstantImpl("TimePoint",
          new GuidImpl("bd58ca05-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$ist-Asserted")
  public static final CycConstantImpl IST_ASSERTED = new CycConstantImpl("ist-Asserted",
          new GuidImpl("c0cd0537-9c29-11b1-9dad-c379636f7270"));
        
  @CycTerm(cycl="#$totalInvestmentEarningsForStockTypeBoughtDuring")
  public static final CycConstantImpl TOTAL_INVESTMENT_EARNINGS_FOR_STOCK_TYPE_BOUGHT_DURING
          = new CycConstantImpl("totalInvestmentEarningsForStockTypeBoughtDuring",
          new GuidImpl("bd50f95e-4a15-41d9-92de-e67c721bbd37"));
  
  @CycTerm(cycl="#$TechStock")
  public static final CycConstantImpl TECH_STOCK = new CycConstantImpl("TechStock",
          new GuidImpl("e6512b74-4a03-41d9-8c2c-e9420322d328"));
  
    @CycTerm(cycl="#$MinusFn")
  public static final CycConstantImpl MINUS_FN = new CycConstantImpl("MinusFn",
          new GuidImpl("bd5880ad-9c29-11b1-9dad-c379636f7270"));
        
  @CycTerm(cycl="#$Pound-GreatBritain")
  public static final CycConstantImpl POUND_GREAT_BRITAIN = new CycConstantImpl("Pound-GreatBritain",
          new GuidImpl("bd58eb3a-9c29-11b1-9dad-c379636f7270"));
  
   @CycTerm(cycl="#$EarlyPartFn")
  public static final CycConstantImpl EARLY_PART_FN = new CycConstantImpl("EarlyPartFn",
          new GuidImpl("c0fcdf30-9c29-11b1-9dad-c379636f7270"));
        
  @CycTerm(cycl="#$TheMotleyFoolUKCorpusMt")
  public static final CycConstantImpl THE_MOTLEY_FOOL_UK_CORPUS_MT = new CycConstantImpl("TheMotleyFoolUKCorpusMt",
          new GuidImpl("1665043a-399b-41d9-8972-b631931507f6"));
  
  @CycTerm(cycl="#$InstanceNamedFn")
  public static final CycConstantImpl INSTANCE_NAMED_FN = new CycConstantImpl("InstanceNamedFn",
          new GuidImpl("df9cfa31-6908-11d6-85c4-0001031a3cae"));

  @CycTerm(cycl="#$BiologicalTaxon")
  public static final CycConstantImpl BIOLOGICAL_TAXON = new CycConstantImpl("BiologicalTaxon",
          new GuidImpl("bd58e2e8-9c29-11b1-9dad-c379636f7270"));

  @CycTerm(cycl="#$OrganismClassificationType")
  public static final CycConstantImpl ORGANISM_CLASSIFICATION_TYPE = new CycConstantImpl("OrganismClassificationType",
          new GuidImpl("bd58dfe4-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$CycLTerm")
  public static final CycConstantImpl CYCL_TERM = new CycConstantImpl("CycLTerm",
          new GuidImpl("c107fffb-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$CollectionType")
  public static final CycConstantImpl COLLECTION_TYPE = new CycConstantImpl("CollectionType",
          new GuidImpl("beda6953-9c29-11b1-9dad-c379636f7270"));
        
  @CycTerm(cycl="#$FoodServiceOrganization")
  public static final CycConstantImpl FOOD_SERVICE_ORGANIZATION = new CycConstantImpl("FoodServiceOrganization",
          new GuidImpl("bd58f1fa-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$UniversityOfTexasAtAustin")
  public static final CycConstantImpl UT_AUSTIN = new CycConstantImpl("UniversityOfTexasAtAustin",
          new GuidImpl("bd58ae1f-9c29-11b1-9dad-c379636f7270"));
        
  @CycTerm(cycl="#$CityOfHoustonTX")
  public static final CycConstantImpl CITY_OF_HOUSTON_TX = new CycConstantImpl("CityOfHoustonTX",
          new GuidImpl("bd590b85-9c29-11b1-9dad-c379636f7270"));
    
  @CycTerm(cycl="#$France")
  public static final CycConstantImpl FRANCE = new CycConstantImpl("France",
          new GuidImpl("bd58fa10-9c29-11b1-9dad-c379636f7270"));
        
  @CycTerm(cycl="#$CityOfParisFrance")
  public static final CycConstantImpl CITY_OF_PARIS_FRANCE = new CycConstantImpl("CityOfParisFrance",
          new GuidImpl("bd58f98c-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$capitalCity")
  public static final CycConstantImpl CAPITAL_CITY = new CycConstantImpl("capitalCity",
          new GuidImpl("bd590b5f-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$IndependentCountry")
  public static final CycConstantImpl INDEPENDENT_COUNTRY = new CycConstantImpl("IndependentCountry",
          new GuidImpl("bd58e4e7-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$WorldPoliticalGeographyDataVocabularyMt")
  public static final CycConstantImpl WORLD_POLITICAL_GEO_DATA_VOCAB_MT = new CycConstantImpl("WorldPoliticalGeographyDataVocabularyMt",
          new GuidImpl("c08b4949-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$Event")
  public static final CycConstantImpl EVENT = new CycConstantImpl("Event",
          new GuidImpl("bd58800d-9c29-11b1-9dad-c379636f7270"));
    
  @CycTerm(cycl="#$CalendarDay")
  public static final CycConstantImpl CALENDAR_DAY = new CycConstantImpl("CalendarDay",
          new GuidImpl("bd58de08-9c29-11b1-9dad-c379636f7270"));
    
    @CycTerm(cycl="#$TheCovering")
  public static final CycConstantImpl THE_COVERING = new CycConstantImpl("TheCovering",
          new GuidImpl("c0c4ce5a-9c29-11b1-9dad-c379636f7270"));
        
  @CycTerm(cycl="#$Watercraft-Surface")
  public static final CycConstantImpl WATERCRAFT_SURFACE = new CycConstantImpl("Watercraft-Surface",
          new GuidImpl("be99e24c-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$Watercraft-Subsurface")
  public static final CycConstantImpl WATERCRAFT_SUBSURFACE = new CycConstantImpl("Watercraft-Subsurface",
          new GuidImpl("bf7b8b03-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$GeorgeWashington")
  public static final CycConstantImpl GEORGE_WASHINGTON = new CycConstantImpl("GeorgeWashington",
          new GuidImpl("c0fcd378-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$LexicalMtForLanguageFn")
  public static final CycConstantImpl LEXICAL_MT_FOR_LANGUAGE_FN = new CycConstantImpl("LexicalMtForLanguageFn",
          new GuidImpl("bf213fe0-9c29-11b1-9dad-c379636f7270"));
        
  @CycTerm(cycl="#$AzeriLanguage")
  public static final CycConstantImpl AZERI_LANGUAGE = new CycConstantImpl("AzeriLanguage",
          new GuidImpl("bd58dcc2-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$givenNames")
  public static final CycConstantImpl GIVEN_NAMES = new CycConstantImpl("givenNames",
          new GuidImpl("bd590c74-9c29-11b1-9dad-c379636f7270"));
        
  @CycTerm(cycl="#$Guest")
  public static final CycConstantImpl GUEST = new CycConstantImpl("Guest",
          new GuidImpl("bd5ad700-9c29-11b1-9dad-c379636f7270"));
  
    @CycTerm(cycl="#$Now")
  public static final CycConstantImpl NOW = new CycConstantImpl("Now",
          new GuidImpl("bd58a068-9c29-11b1-9dad-c379636f7270"));
    
  @CycTerm(cycl="#$MtTimeDimFn")
  public static final CycConstantImpl MT_TIME_DM_FN = new CycConstantImpl("MtTimeDimFn",
          new GuidImpl("47537942-331d-11d7-922f-0002b3a333c3"));
    
  @CycTerm(cycl="#$MtTimeWithGranularityDimFn")
  public static final CycConstantImpl MT_TIME_WITH_GRANULARITY_DM_FN = new CycConstantImpl("MtTimeWithGranularityDimFn",
          new GuidImpl("47537943-331d-11d7-922f-0002b3a333c3"));
  
    @CycTerm(cycl="#$Always-TimeInterval")
  public static final CycConstantImpl ALWAYS_TIME_INTERVAL = new CycConstantImpl("Always-TimeInterval",
          new GuidImpl("c0ea3419-9c29-11b1-9dad-c379636f7270"));
        
  @CycTerm(cycl="#$Null-TimeParameter")
  public static final CycConstantImpl NULL_TIME_PARAMETER = new CycConstantImpl("Null-TimeParameter",
          new GuidImpl("4f22f93a-a66d-11d6-8000-00a0c9c6d17e"));
  
  @CycTerm(cycl="#$salientAssertions")
  public static final CycConstantImpl SALIENT_ASSERTIONS = new CycConstantImpl("salientAssertions",
          new GuidImpl("bd5db814-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$Tourist")
  public static final CycConstantImpl TOURIST = new CycConstantImpl("Tourist",
          new GuidImpl("c0f7ce23-9c29-11b1-9dad-c379636f7270"));
        
  @CycTerm(cycl="#$GroupFn")
  public static final CycConstantImpl GROUP_FN = new CycConstantImpl("GroupFn",
          new GuidImpl("c0fd79df-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$ComputationalSystem")
  public static final CycConstantImpl COMPUTATIONAL_SYSTEM = new CycConstantImpl("ComputationalSystem",
          new GuidImpl("c06abb01-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$FormerFn")
  public static final CycConstantImpl FORMER_FN = new CycConstantImpl("FormerFn",
          new GuidImpl("0f672b96-600a-41d9-8b3a-bf3c4a88b9f6"));
  
  @CycTerm(cycl="#$ContextOfPCWFn")
  public static final CycConstantImpl CONTEXT_OF_PCW_FN = new CycConstantImpl("ContextOfPCWFn",
          new GuidImpl("c08122ae-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$UnitedStatesPresident")
  public static final CycConstantImpl UNITED_STATES_PRESIDENT = new CycConstantImpl("UnitedStatesPresident",
          new GuidImpl("c10052d1-9c29-11b1-9dad-c379636f7270"));
  
  @CycTerm(cycl="#$HistoricalPeopleDataMt")
  public static final ElMt HISTORICAL_PEOPLE_DATA_MT = ElMtConstant.makeElMtConstant(
          new CycConstantImpl("HistoricalPeopleDataMt", new GuidImpl(
                          "c0852ac3-9c29-11b1-9dad-c379636f7270")));
  
  
  public static final CycVariable X = CycObjectFactory.makeCycVariable("X");
  public static final CycVariable VAR_X = CycObjectFactory.makeCycVariable("X");
  public static final CycVariable VAR_Y = CycObjectFactory.makeCycVariable("Y");
  public static final CycVariable VAR_Z = CycObjectFactory.makeCycVariable("Z");
  public static final CycVariable VAR_VARIABLE = CycObjectFactory.makeCycVariable("VARIABLE");
  public static final CycVariable VAR_WHAT = CycObjectFactory.makeCycVariable("WHAT");
  public static final CycVariable VAR_WHERE = CycObjectFactory.makeCycVariable("WHERE");
  public static final CycVariable VAR_0 = new CycVariableImpl("VAR0", 0);
}
