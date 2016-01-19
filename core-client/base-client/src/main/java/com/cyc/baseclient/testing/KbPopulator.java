package com.cyc.baseclient.testing;

/*
 * #%L
 * File: KbPopulator.java
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

import com.cyc.base.CycAccess;
import com.cyc.base.exception.CycConnectionException;
import com.cyc.base.cycobject.CycConstant;
import com.cyc.base.cycobject.ElMt;
import com.cyc.baseclient.CommonConstants;
import com.cyc.baseclient.cycobject.CycArrayList;
import com.cyc.baseclient.cycobject.ElMtConstant;
import com.cyc.baseclient.cycobject.ElMtCycNaut;
import static com.cyc.baseclient.testing.TestConstants.CONTEXT_OF_PCW_FN;
import com.cyc.session.CycServerAddress;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author nwinant
 */
public class KbPopulator {  
  
  static private KbPopulator me = new KbPopulator();
  
  synchronized public static void ensureKBPopulated(CycAccess access) throws CycConnectionException {
    if (!me.isAlreadyCalledForKB(access)){
      me.populate(access);
    }
  }
  // Public
  
  public synchronized void populate(CycAccess cyc) throws CycConnectionException {
    final CycConstant WHH_WP_PCW = makeConstant(WHH_WP_PCW_STR, cyc);
    makeSentence(
            "'(#$isa " + WHH_WP_PCW_STR + " #$WorldWideWebPage-PCW)",
            TestConstants.HISTORICAL_PEOPLE_DATA_MT, cyc);
    makeSentence(
            "'(#$definiteDescriptions " + WHH_WP_PCW_STR + " \"Wikipedia Article: William Henry Harrison\")",
            TestConstants.HISTORICAL_PEOPLE_DATA_MT, cyc);
    makeSentence(
            "'(#$urlOfCW " + WHH_WP_PCW_STR + " (#$URLFn \"http://en.wikipedia.org/wiki/William_Henry_Harrison\"))",
            TestConstants.HISTORICAL_PEOPLE_DATA_MT, cyc);
    makeSentence(
            "'(#$firstPublicationDate-CW " + WHH_WP_PCW_STR + " (#$DayFn  18 (#$MonthFn #$August (#$YearFn  2014 ))))",
            TestConstants.HISTORICAL_PEOPLE_DATA_MT, cyc);
    makeSentence(
            "'(#$containsInformationAbout-Focally " + WHH_WP_PCW_STR + " #$WilliamHenryHarrison)",
            TestConstants.HISTORICAL_PEOPLE_DATA_MT, cyc);
    makeSentence(
            "'(#$referenceWorkHasEntry #$Wikipedia-WebSite " + WHH_WP_PCW_STR + ")",
            TestConstants.HISTORICAL_PEOPLE_DATA_MT, cyc);
    makeSentence(
            "'(#$citationStringForStyleAndRendering " + WHH_WP_PCW_STR + " #$ChicagoManualOfStyleStandard  #$HypertextMarkupLanguage " + 
                    "\"Wikipedia contributors, \\\"William Henry Harrison,\\\" <i>Wikipedia, The Free Encyclopedia,</i> http://en.wikipedia.org/w/index.php?title=William_Henry_Harrison&oldid=619200023 (accessed August 20, 2014).\")",
            TestConstants.HISTORICAL_PEOPLE_DATA_MT, cyc);
    makeSentence("'(#$isa #$WilliamHenryHarrison (#$FormerFn #$UnitedStatesPresident))",
            ElMtCycNaut.makeElMtCycNaut(
                    CycArrayList.makeCycList(CONTEXT_OF_PCW_FN, WHH_WP_PCW)),
            cyc);
    
    makeSentence(
            "'(#$objectFoundInLocation #$Cycorp #$CityOfAustinTX)",
            "#$OrganizationDataMt", cyc);
    makeSentence(
            "'(#$residenceOfOrganization #$UniversityOfTexasAtAustin #$CityOfAustinTX)",
            "#$UniversityDataMt", cyc);
    makeSentence(
            "'(#$backchainRequired #$keRequirement)",
            CommonConstants.BASE_KB, cyc);
    
    if (!cyc.isOpenCyc()) {
      makeSentence(
              "'(#$percentOfTerritoryIs #$Algeria #$Field-Agricultural (#$Percent 0))",
              "#$CIAWorldFactbook1995Mt", cyc);
    }
    
    makeSentence(
            "'(#$capitalCity #$France #$CityOfParisFrance)",
            "#$WorldGeographyMt", cyc);
    makeSentence("  '(#$implies " +
            "   (#$and " +
            "    (#$isa ?EVENT #$WorkingEvent) " +
            "    (#$performedBy ?EVENT ?AGENT)) " +
            "   (#$subjectivelyEvaluates ?AGENT ?EVENT #$Challenging))",
            ElMtConstant.makeElMtConstant(TestConstants.HUMAN_ACTIVITIES_MT), cyc);
    makeSentence(
            "'(#$salientAssertions #$performedBy " +
                    " (#$ist #$HumanActivitiesMt " +
                    "  (#$implies " +
                    "   (#$and " +
                    "    (#$isa ?EVENT #$WorkingEvent) " +
                    "    (#$performedBy ?EVENT ?AGENT)) " +
                    "   (#$subjectivelyEvaluates ?AGENT ?EVENT #$Challenging))))",
            CommonConstants.BASE_KB, cyc);
    makeSentence(
            "'(#$isa #$TreatyOak #$Plant)",
            CommonConstants.UNIVERSAL_VOCABULARY_MT, cyc);
    makeSentence(
            "'(#$isa #$BurningBushOldTestament #$Plant)",
            CommonConstants.UNIVERSAL_VOCABULARY_MT, cyc);
    
    populatedKBs.add(cyc.getCycServer());
  }
  
  public synchronized boolean isAlreadyCalledForKB(CycAccess access) {
    return populatedKBs.contains(access.getCycServer());
  }
  
  
  // Protected
  
  protected CycConstant makeConstant(String name, CycAccess cyc) throws CycConnectionException {
    return cyc.getObjectTool().makeCycConstant(name, BOOKKEEPING, TRANSCRIPT);
  }
  
  protected void makeSentence(String sentence, ElMt mt, CycAccess cyc) throws CycConnectionException {
    cyc.getAssertTool().assertSentence(sentence, mt, BOOKKEEPING, TRANSCRIPT);
  }
  
  protected void makeSentence(String sentence, String mtString, CycAccess cyc) throws CycConnectionException {
    final ElMt mt = cyc.getObjectTool().makeElMt(mtString);
    makeSentence(sentence, mt, cyc);
  }
  
  
  // Internal
  
  public static final String WHH_WP_PCW_STR = "#$TestFactEntrySource-WikipediaArticle-WilliamHenryHarrison";

  private final Set<CycServerAddress> populatedKBs = new HashSet();
  private static final boolean BOOKKEEPING = true;
  private static final boolean TRANSCRIPT = false;
}
