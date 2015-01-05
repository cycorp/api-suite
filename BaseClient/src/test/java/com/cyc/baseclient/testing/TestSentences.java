package com.cyc.baseclient.testing;

/*
 * #%L
 * File: TestSentences.java
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

import com.cyc.base.annotation.CycFormula;
import com.cyc.base.annotation.CycObjectLibrary;
import com.cyc.base.cycobject.FormulaSentence;
import com.cyc.baseclient.CommonConstants;
import static com.cyc.baseclient.CommonConstants.*;
import static com.cyc.baseclient.testing.TestConstants.*;
import static com.cyc.baseclient.datatype.DateConverter.*;
import com.cyc.baseclient.cycobject.CycFormulaSentence;
import com.cyc.baseclient.cycobject.NautImpl;
import com.cyc.baseclient.datatype.DateConverter;

/**
 * Sentences used by unit tests. They are not expected to exist in the KB 
 (although they may) but any constants they reference are expected to exist.
 Sentences may be defined as strings or as FormulaSentences, whichever is more
 convenient.
 
 <p>Any required constants should be defined in the appropriate file
 * ({@link CommonConstants) or {@link TestConstants}) and referenced as constants
 here.
 * 
 * @author nwinant
 */
@CycObjectLibrary
public class TestSentences {
  
  @CycFormula(cycl="(#$isa #$WilliamHenryHarrison (#$FormerFn #$UnitedStatesPresident))")
  public static final FormulaSentence ISA_WILLIAM_HENRY_HARRISON_US_PRESIDENT
          = CycFormulaSentence.makeCycFormulaSentence(ISA, WILLIAM_HENRY_HARRISON,
                  new NautImpl(
                          FORMER_FN,
                          UNITED_STATES_PRESIDENT));
  
  public static final String ISA_WILLIAM_HENRY_HARRISON_US_PRESIDENT_STRING = ISA_WILLIAM_HENRY_HARRISON_US_PRESIDENT.cyclify();

  @CycFormula(cycl="(#$genls #$WilliamHenryHarrison #$BiologicalLivingObject)",
          optional=true)
  public static final FormulaSentence genlsWilliamHenryHarrisonBLO =
          CycFormulaSentence.makeCycFormulaSentence(GENLS, WILLIAM_HENRY_HARRISON, BIOLOGICAL_LIVING_OBJECT);
  
  public static final String genlsWilliamHenryHarrisonBLO_STRING = genlsWilliamHenryHarrisonBLO.cyclify();
  
  @CycFormula(cycl="(#$isa #$WilliamHenryHarrison #$BiologicalLivingObject)")
  public static final FormulaSentence isaWilliamHenryHarrisonBLO =
          CycFormulaSentence.makeCycFormulaSentence(ISA, WILLIAM_HENRY_HARRISON, BIOLOGICAL_LIVING_OBJECT);
  
  public static final String isaWilliamHenryHarrisonBLO_STRING = isaWilliamHenryHarrisonBLO.cyclify();
  
  @CycFormula(cycl="(#$evaluate ?X (#$PlusFn 1 1))")
  public static final FormulaSentence WHAT_IS_ONE_PLUS_ONE =
          CycFormulaSentence.makeCycFormulaSentence(EVALUATE, VAR_X, 
                  CycFormulaSentence.makeCycFormulaSentence(PLUS_FN, 1, 1));
  
  public static final String WHAT_IS_ONE_PLUS_ONE_STRING = WHAT_IS_ONE_PLUS_ONE.cyclify();

  @CycFormula(cycl="(#$objectFoundInLocation ?WHAT #$CityOfAustinTX)")
  public static final FormulaSentence WHAT_IS_IN_AUSTIN =
          CycFormulaSentence.makeCycFormulaSentence(OBJECT_FOUND_IN_LOCATION, VAR_WHAT, CITY_OF_AUSTIN_TX);
  
  public static final String WHAT_IS_IN_AUSTIN_STRING = WHAT_IS_IN_AUSTIN.cyclify();
  
  
  @CycFormula(cycl="(#$isa #$Thing #$Thing)")
  public static final FormulaSentence isaThingThing =
          CycFormulaSentence.makeCycFormulaSentence(ISA, THING, THING);
  
  public static final String ISA_THING_THING_STRING = isaThingThing.cyclify();

  
  @CycFormula(cycl="(#$isa #$Dog #$BiologicalSpecies)")
  public static final FormulaSentence ISA_DOG_BIOLOGICAL_SPECIES =
          CycFormulaSentence.makeCycFormulaSentence(ISA, DOG, BIOLOGICAL_SPECIES);
  
  public static final String ISA_DOG_BIOLOGICAL_SPECIES_STRING = ISA_DOG_BIOLOGICAL_SPECIES.cyclify();
  
  @CycFormula(cycl="(#$implies (#$isa ?X #$Cat) (#$likesAsFriend ?X #$CycAdministrator))")
  public static final FormulaSentence CATS_LIKE_CYC_ADMIN =
          CycFormulaSentence.makeCycFormulaSentence(IMPLIES, 
                  CycFormulaSentence.makeCycFormulaSentence(ISA, VAR_X, CAT),
                  CycFormulaSentence.makeCycFormulaSentence(LIKES_AS_FRIEND, VAR_X, CYC_ADMINISTRATOR));  
  
  public static final String CATS_LIKE_CYC_ADMIN_STRING = CATS_LIKE_CYC_ADMIN.cyclify();
  
  
  
  
  @CycFormula(cycl="(#$The #$Dog)", optional=true)
  public static final FormulaSentence THE_DOG =
          CycFormulaSentence.makeCycFormulaSentence(THE, DOG);
  
  public static final String THE_DOG_STRING = THE_DOG.cyclify();

    
  @CycFormula(cycl="(#$isa (#$The #$Dog) #$Dog)")
  public static final FormulaSentence ISA_THE_DOG_DOG =
          CycFormulaSentence.makeCycFormulaSentence(ISA, THE_DOG, DOG);
  
  public static final String ISA_THE_DOG_DOG_STRING = ISA_THE_DOG_DOG.cyclify();

  @CycFormula(cycl="(#$isa #$Muffet #$Dog)")
  public static final FormulaSentence ISA_MUFFET_DOG =
          CycFormulaSentence.makeCycFormulaSentence(ISA, MUFFET, DOG);
  
  public static final String ISA_MUFFET_DOG_STRING = ISA_MUFFET_DOG.cyclify();
  
  @CycFormula(cycl="(#$MtSpace #$CyclistsMt "
          + "(#$MtTimeWithGranularityDimFn "
          + "(#$MonthFn #$January (#$YearFn 2004))"
          + " #$TimePoint))",
          optional=true)
  public static final FormulaSentence MT_SPACE_CYCLISTS_MT_TIME_POINT
          = CycFormulaSentence.makeCycFormulaSentence(
                  MT_SPACE, CYCLISTS_MT,
                  CycFormulaSentence.makeCycFormulaSentence(
                          MT_TIME_POINT_WITH_GRANULARITY_DIM_FN,
                          CycFormulaSentence.makeCycFormulaSentence(
                                  DateConverter.MONTH_FN, DateConverter.JANUARY,
                                  CycFormulaSentence.makeCycFormulaSentence(DateConverter.YEAR_FN, 2004)),
                          TIME_POINT));

  /*
  @CycFormula(cycl="(#$ist-Asserted \n"
              + "  (#$totalInvestmentEarningsForStockTypeBoughtDuring  \n"
              + "    #$TechStock  \n"
              + "    (#$MinusFn (#$Pound-GreatBritain 330000000000))  \n"
              + "    (#$EarlyPartFn (#$YearFn 2000)))  \n"
              + "  #$TheMotleyFoolUKCorpusMt))")
  */
  
  @CycFormula(cycl="(#$BiologicalTaxon "
              + "#$BiologicalSpecies "
              + "#$OrganismClassificationType "
              + "#$CycLTerm "
              + "#$CollectionType)",
          optional=true)
  public static final FormulaSentence BIOLOGICAL_TAXON_ETC = CycFormulaSentence.makeCycFormulaSentence(
            BIOLOGICAL_TAXON,
            BIOLOGICAL_SPECIES,
            ORGANISM_CLASSIFICATION_TYPE,
            CYCL_TERM,
            COLLECTION_TYPE);

  @CycFormula(cycl="(#$unknownSentence (#$isa ?WHAT #$FoodServiceOrganization))")
  public static final FormulaSentence UNKNOWN_SENTENCE_FOOD_ORG = CycFormulaSentence.makeCycFormulaSentence(
          UNKNOWN_SENTENCE,
          CycFormulaSentence.makeCycFormulaSentence(
                  ISA,
                  VAR_WHAT,
                  FOOD_SERVICE_ORGANIZATION));
  
  @CycFormula(cycl="(#$objectFoundInLocation ?WHAT ?WHERE)")
  public static final FormulaSentence OBJECT_FOUND_WHAT_WHERE = CycFormulaSentence.makeCycFormulaSentence(
          OBJECT_FOUND_IN_LOCATION,
          VAR_WHAT,
          VAR_WHERE);
  
  @CycFormula(cycl="(#$objectFoundInLocation #$UniversityOfTexasAtAustin #$CityOfAustinTX)")
  public static final FormulaSentence UT_AUSTIN_IN_AUSTIN = CycFormulaSentence.makeCycFormulaSentence(
          OBJECT_FOUND_IN_LOCATION,
          UT_AUSTIN,
          CITY_OF_AUSTIN_TX);
  
  @CycFormula(cycl="(#$objectFoundInLocation #$UniversityOfTexasAtAustin #$CityOfHoustonTX)")
  public static final FormulaSentence UT_AUSTIN_IN_HOUSTON = CycFormulaSentence.makeCycFormulaSentence(
          OBJECT_FOUND_IN_LOCATION,
          UT_AUSTIN,
          CITY_OF_HOUSTON_TX);
  
  @CycFormula(cycl="(#$isa #$CycAdministrator #$Person)")
  public static final FormulaSentence ISA_CYC_ADMIN_PERSON = CycFormulaSentence.makeCycFormulaSentence(
          ISA,
          CYC_ADMINISTRATOR,
          PERSON);
  
  @CycFormula(cycl="(#$isa (#$DayFn 1 (#$MonthFn #$March (#$YearFn 2004))) #$Event)")
  public static final FormulaSentence DAY_MARCH_1_2004_EVENT = CycFormulaSentence.makeCycFormulaSentence(
          ISA,
          CycFormulaSentence.makeCycFormulaSentence(DAY_FN, 1,
                  CycFormulaSentence.makeCycFormulaSentence(MONTH_FN, MARCH,
                          CycFormulaSentence.makeCycFormulaSentence(YEAR_FN, 2004))),
          EVENT);
  
  @CycFormula(cycl="(#$isa (#$DayFn 1 (#$MonthFn #$March (#$YearFn 2004))) #$CalendarDay)")
  public static final FormulaSentence DAY_MARCH_1_2004_CALENDAR_DAY = CycFormulaSentence.makeCycFormulaSentence(
          ISA,
          CycFormulaSentence.makeCycFormulaSentence(DAY_FN, 1,
                  CycFormulaSentence.makeCycFormulaSentence(MONTH_FN, MARCH,
                          CycFormulaSentence.makeCycFormulaSentence(YEAR_FN, 2004))),
          CALENDAR_DAY);
  
  @CycFormula(cycl = "(#$TheCovering #$Watercraft-Surface #$Watercraft-Subsurface)",
          optional=true)
  public static final FormulaSentence WATERCRAFT_COVERING= CycFormulaSentence.makeCycFormulaSentence(
          THE_COVERING,
          WATERCRAFT_SURFACE,
          WATERCRAFT_SUBSURFACE);
  
  @CycFormula(cycl="(#$LexicalMtForLanguageFn #$AzeriLanguage)",
          optional=true)
  public static final FormulaSentence AZERI_LANGUAGE_LEXICAL_MT = CycFormulaSentence.makeCycFormulaSentence(
          LEXICAL_MT_FOR_LANGUAGE_FN, AZERI_LANGUAGE);
    
  @CycFormula(cycl="(#$MtSpace #$BaseKB (#$MtTimeDimFn #$Now))",
          optional=true)
  public static final FormulaSentence MT_SPACE_TIME_NOW = CycFormulaSentence.makeCycFormulaSentence(
          MT_SPACE, 
          BASE_KB,
          CycFormulaSentence.makeCycFormulaSentence(
                  MT_TIME_DM_FN,
                  NOW));

  @CycFormula(cycl="(#$MtSpace #$BaseKB (#$MtTimeWithGranularityDimFn #$Always-TimeInterval #$Null-TimeParameter))",
          optional=true)
  public static final FormulaSentence MT_SPACE_TIME_ALWAYS = CycFormulaSentence.makeCycFormulaSentence(
          MT_SPACE, 
          BASE_KB,
          CycFormulaSentence.makeCycFormulaSentence(
                  MT_TIME_WITH_GRANULARITY_DM_FN,
                  ALWAYS_TIME_INTERVAL,
                  NULL_TIME_PARAMETER));
}
