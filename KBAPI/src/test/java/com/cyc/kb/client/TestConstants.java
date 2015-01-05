/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cyc.kb.client;

/*
 * #%L
 * File: TestConstants.java
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

import com.cyc.base.CycAccess;
import com.cyc.base.CycAccessManager;
import com.cyc.base.cycobject.CycObject;
import com.cyc.kb.Assertion;
import com.cyc.kb.BinaryPredicate;
import com.cyc.kb.Context;
import com.cyc.kb.Fact;
import com.cyc.kb.FirstOrderCollection;
import com.cyc.kb.KBAPIEnums;
import com.cyc.kb.KBCollection;
import com.cyc.kb.KBFunction;
import com.cyc.kb.KBIndividual;
import com.cyc.kb.KBTerm;
import com.cyc.kb.Sentence;
import com.cyc.kb.Variable;
import com.cyc.kb.config.KBAPIConfiguration;
import com.cyc.kb.config.KBAPIDefaultContext;
import com.cyc.kb.exception.KBApiException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 *
 * @author vijay
 */
public class TestConstants {

  public static CycAccess cyc = null;
  public static Context baseKB;
  public static Context universalVocabularyMt;
  public static Assertion flyingRule;
  public static KBAPITestConstants kbapitc;

  private static Logger log = LoggerFactory.getLogger(TestConstants.class.getName());
  
  public static void ensureInitialized() throws Exception {
    log.info("Setting up...");
    if (TestConstants.baseKB == null) {
      TestConstants.cyc = CycAccessManager.getCurrentAccess();
      KBAPIConfiguration.setShouldTranscriptOperations(false);
      // Example usage of KBAPIConfiguration methods
      //KBAPIConfiguration.setCurrentCyclist("(#$UserOfProgramFn #$OWLImporter-Cyc #$ChrisDeaton)");
      KBAPIConfiguration.setCurrentCyclist("TheUser");
      //KBAPIConfiguration.setProject(KBIndividualImpl.get("ThePurpose"));
      KBAPIConfiguration.setDefaultContext(new KBAPIDefaultContext(Constants.uvMt(), Constants.inferencePSCMt()));
    }
    universalVocabularyMt = new ImmutableContext("UniversalVocabularyMt");
    baseKB = new ImmutableContext("BaseKB");
    
    kbapitc = KBAPITestConstants.getInstance();
    
    setupOEScript();
  }
  
  private static void setupOEScript() throws KBApiException {
    
    log.info("Entering the setupOEScript");
    
    KBIndividual i = KBIndividualImpl.findOrCreate("TestIndividual001");
    i.instantiates("(#$CitizenFn #$UnitedStatesOfAmerica)", "UniversalVocabularyMt");
    KBCollection nonSense = KBCollectionImpl.findOrCreate("Nonsense");
    
    KBIndividual airline = KBIndividualImpl.findOrCreate("SomeAirline");
    airline.instantiates(KBCollectionImpl.get("AirlineCompany"), Constants.uvMt());
    // Create a temporary CycKBSubsetCollection to gather the newly created constants
    // Demonstrate the use of quote, quotedIsa
    //KBCollection cycSubsetCol = KBCollectionImpl.get("CycKBSubsetCollection");
    //FirstOrderCollection tempTestSubset = FirstOrderCollectionImpl.findOrCreate("ApiTestingSubset", cycSubsetCol, Constants.uvMt());
    
    FirstOrderCollection flying1Col = FirstOrderCollectionImpl.findOrCreate("FlyingAnObject-Operate");
    flying1Col.addGeneralization(KBCollectionImpl.get("ControllingSomething"), Constants.uvMt()); 
    // Change this to OperatingSomething, where we are operating a PhysicalDevice
    
    SecondOrderCollectionImpl flyingType = SecondOrderCollectionImpl.findOrCreate("FlyingTypeByTypeOfObjectFlown");
    KBCollection cPlane = KBCollectionImpl.get("CommercialAircraft");
    
    KBFunction flying1Fun = KBFunctionImpl.findOrCreate("FlyingATypeOfObject-Operate-Fn", KBCollectionImpl.get("ReifiableFunction"));
    flying1Fun.addArgGenl(1, KBCollectionImpl.get("PhysicalDevice"), Constants.uvMt());
    flying1Fun.addResultGenl(flying1Col, Constants.uvMt());
    flying1Fun.addResultIsa(flyingType, Constants.uvMt());
    flying1Fun.setArity(1);
    
    KBTerm flyingAPlaneTemp = flying1Fun.findOrCreateFunctionalTerm(KBTermImpl.class, cPlane);
    flyingAPlaneTemp.instantiates((KBCollection)FirstOrderCollectionImpl.getClassType());
    FirstOrderCollection flyingAPlane = flying1Fun.findOrCreateFunctionalTerm(FirstOrderCollectionImpl.class, cPlane);
    
    BinaryPredicate flying1Pred1 = BinaryPredicateImpl.findOrCreate("flyingAnObject-Operate", KBCollectionImpl.get("ActorSlot"));
    flying1Pred1.addArgIsa(1, flying1Col, Constants.uvMt());
    // flying1Pred1.addArgIsa(2, KBCollection.get("PhysicalDevice"), Constants.uvMt());
    SentenceImpl sargisa2 = new SentenceImpl (KBPredicateImpl.get("argIsa"), flying1Pred1, 2, KBCollectionImpl.get("PhysicalDevice"));
    AssertionImpl.findOrCreate (sargisa2, Constants.uvMt());
    flying1Pred1.addGeneralization(KBPredicateImpl.get("objectActedOn"), Constants.uvMt());
    // Rule that FlyinATypeOfObject => objectActedOn is a of certain type
    
        
    BinaryPredicateImpl flying1Pred2 = BinaryPredicateImpl.findOrCreate("flyingDoneBySomething-Operate", "ActorSlot");
    flying1Pred2.addArgIsa(1, flying1Col, Constants.uvMt());
    flying1Pred2.addArgIsa(2, KBCollectionImpl.get("IntelligentAgent"), Constants.uvMt());
    KBPredicateImpl.get("performedBy").addSpecialization(flying1Pred2, Constants.uvMt()); 
    
    FirstOrderCollectionImpl flying2Col = FirstOrderCollectionImpl.findOrCreate("Flying-Move");
    FirstOrderCollectionImpl move = FirstOrderCollectionImpl.findOrCreate("MovementThroughAir");
    move.addSpecialization(flying2Col, Constants.uvMt());
    
    BinaryPredicateImpl flying2Pred1 = BinaryPredicateImpl.findOrCreate("flyingDoneBySomething-Move", KBCollectionImpl.get("ActorSlot"), Constants.uvMt());
    flying2Pred1.addArgIsa(1, flying2Col, Constants.uvMt());
    flying2Pred1.addArgIsa(2, KBCollectionImpl.get("SomethingExisting"), Constants.uvMt());
    flying2Pred1.addGeneralization(KBPredicateImpl.get("doneBy"), Constants.uvMt());
        
    // objectActedOn is on self in Flying-Move
    
    
    FirstOrderCollection flying3Col = FirstOrderCollectionImpl.findOrCreate("Flying-Travel");
    FirstOrderCollectionImpl travel = FirstOrderCollectionImpl.get("Travel-TripEvent");
    //travel.addSpecialization(flying3Col, Constants.uvMt());
    // The above can be rewritten using addArg2 as follows:
    travel.addArg2(BinaryPredicateImpl.get("genls"), flying3Col, Constants.uvMt());
    
    BinaryPredicateImpl flying3Pred1 = BinaryPredicateImpl.findOrCreate("flyingDoneBySomeone-Travel", "ActorSlot", "UniversalVocabularyMt");
    // flying3Pred1.addArgIsa(1, flying2Col, Constants.uvMt());
    // The above can be rewritten using addFact as follows:
    // Expect: (ist UniversalVocabularyMt (argIsa flyingDoneBySomeone-Travel 1 Flying-Travel))
    flying3Pred1.addFact(Constants.uvMt(), KBPredicateImpl.get("argIsa"), 1, 1, flying2Col);
    
    flying3Pred1.addArgIsa(2, KBCollectionImpl.get("Person"), Constants.uvMt());
    flying3Pred1.addGeneralization(KBPredicateImpl.get("performedBy"), Constants.uvMt());
    
    // Setup a context hierarchy
    Context airlineLogMt = ContextImpl.findOrCreate("SomeAirlineLogMt");
    Context airlineEmpMt = ContextImpl.findOrCreate("SomeAirlineEmployeeMt");
    Context airlineEquipMt = ContextImpl.findOrCreate("SomeAirlineEquipmentMt");
    
    KBFunction equipmentMtFunc = KBFunctionImpl.findOrCreate("SomeAirlineEquipmentLogFn", KBCollectionImpl.get("UnaryFunction"));
    equipmentMtFunc.instantiates(KBCollectionImpl.get("ReifiableFunction"));
    equipmentMtFunc.setArity(1);
    equipmentMtFunc.addResultIsa((KBCollection)ContextImpl.getClassType(), Constants.uvMt());
    equipmentMtFunc.addArgIsa(1, KBCollectionImpl.get("CommercialAircraft"), Constants.uvMt());
            
    
    airlineLogMt.addInheritsFrom(airlineEmpMt);
    airlineEquipMt.addExtension(airlineLogMt);
    
    // Not asserting specializations for fromLocation and toLocation
    KBIndividualImpl apilot = KBIndividualImpl.findOrCreate("Pilot-APITest", "AirplanePilot", "SomeAirlineEmployeeMt");
    
    KBIndividualImpl aplane = KBIndividualImpl.findOrCreate("Plane-APITest", cPlane, airlineEquipMt);
    Fact owns = FactImpl.findOrCreate(new SentenceImpl(KBPredicateImpl.get("owns"), airline, aplane), airlineEquipMt);
    
    Context airplaneContext = equipmentMtFunc.findOrCreateFunctionalTerm(Context.class, aplane);
            
    //KBIndividual operate = KBIndividual.findOrCreate("FlyingAPlane-APITest", flying1Col, airlineLogMt);
    KBIndividual operate = KBIndividualImpl.findOrCreate("FlyingAPlane-APITest", flyingAPlane, airlineLogMt);
    apilot.addArg1(flying1Pred2, operate, airlineLogMt);
    aplane.addArg1(flying1Pred1, operate, airlineLogMt);
    
    KBIndividualImpl flight = KBIndividualImpl.findOrCreate("FlightXYZ-APITest", flying2Col, airlineLogMt);
    flight.addArg2(flying2Pred1, aplane, airlineLogMt);
    
    KBIndividual city1 = KBIndividualImpl.findOrCreate("TestCity001", kbapitc.city);
    SentenceImpl s = new SentenceImpl (KBPredicateImpl.get("fromLocation"), flight, city1);
    
    FactImpl f1 = new FactImpl(airlineLogMt, s); // From        
    // Adding comment on a GAF
    f1.addComment("A flight from Test City 001", airlineLogMt);
    
    // Adding GAFs using fact
    KBIndividual city2 = KBIndividualImpl.findOrCreate("TestCity002", kbapitc.city);
    FactImpl f2 = new FactImpl(airlineLogMt, KBPredicateImpl.get("toLocation"), flight, city2); // To
    f2.addComment("A flight to Test City 002", "SomeAirlineLogMt");
    
    
    KBCollection publicData = KBCollectionImpl.findOrCreate("SomeAirlinePublicData");
            
    flight.addQuotedIsa(KBCollectionImpl.findOrCreate("SomeAirlinePublicData"), airlineLogMt);
    f1.addQuotedIsa(publicData, airlineLogMt);
    f2.addQuotedIsa(publicData, airlineLogMt);
    
    Assertion cause = AssertionImpl.findOrCreate("(causes-EventEvent FlyingAPlane-APITest FlightXYZ-APITest)", "SomeAirlineLogMt");
    
    // artifactFoundInLocation
    // aircraftInAirport
    
    // Transport by Air
    /*
     * (implies 
    (and 
      (isa ?PLANE CommercialAircraft) 
      (flyingDoneBySomething-Move ?FLIGHT ?PLANE) 
      (endingDate ?FLIGHT ?END-DATE) 
      (toLocation ?FLIGHT ?TO)) 
    (holdsIn ?END-DATE 
      (artifactFoundInLocation ?PLANE ?TO)))
      
     */
    
    Variable varp = new VariableImpl("?PLANE");
    Variable varf = new VariableImpl("?FLIGHT");
    Variable varend = new VariableImpl("?END-DATE");
    Variable vart = new VariableImpl("?TO");
    SentenceImpl s1 = new SentenceImpl (KBPredicateImpl.get("isa"), varp, cPlane);
    SentenceImpl s2 = new SentenceImpl (flying2Pred1, varf, varp);
    SentenceImpl s3 = new SentenceImpl (KBPredicateImpl.get("endingDate"), varf, varend);
    SentenceImpl s4 = new SentenceImpl (KBPredicateImpl.get("toLocation"), varf, vart);
    Sentence s5 = new SentenceImpl (KBPredicateImpl.get("artifactFoundInLocation"), varp, vart);
    Sentence s6 = new SentenceImpl (KBPredicateImpl.get("holdsIn"), varend, s5);
    List<Sentence> sandlist = new ArrayList<Sentence>();
    sandlist.add(s1);
    sandlist.add(s2);
    sandlist.add(s3);
    sandlist.add(s4);
    Sentence sand = SentenceImpl.and(sandlist);

    SentenceImpl rule = new SentenceImpl(KBIndividualImpl.get("implies"), sand, s6);
    
    // flyingRule = AssertionImpl.findOrCreate(rule, Constants.baseKbMt(), null, Direction.FORWARD);
    // flyingRule = RuleImpl.findOrCreate(SentenceImpl.implies(sandlist, s6), Constants.baseKbMt(), KBAPIEnums.Strength.AUTO, Direction.FORWARD);
    flyingRule = rule.assertIn(Constants.baseKbMt());
    flyingRule.changeDirection(KBAPIEnums.Direction.FORWARD);
    
    
    log.debug("Rule assertion hlid: " + flyingRule.getId());
    		
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd HH:mm");
    try {
		Date d = sdf.parse("2014 03 15 10:20");
		Fact f = FactImpl.findOrCreate(new SentenceImpl(KBPredicateImpl.get("endingDate"), flight, d), airlineLogMt);
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
    /*
    <Present Participle><NP>-[Verb Specialization]-Fn
    FlyingAnObject-Operate FlyingAnObject-Operate-Fn AnObject;
    
    FlyingAnObject-Operate;
    
    flyingAnObject-Operate FlyingAnObject-Operate AnObject;
    
    flyingDoneBySomething-Operate FlyingAnObject-Operate Something;
    */
    
    // For intransitive sense, don't add anything
    // Flying;
    
    // Dying - intransitive; Working
    // KillingAnObject - instead of just Killing - monotransitive
    // GivingAnObjectToSomeone - ditransitive
    // GivingAnObject
    log.info("Done setupOEScript");
            
  }

  private static class ImmutableContext extends ContextImpl {

    public ImmutableContext(CycObject cycCtx) throws Exception {
      super(cycCtx);
    }

    private ImmutableContext(String cycName) throws Exception {
      super(cycName);
    }

    @Override //Because we want to allow equality with mutable contexts.
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (!(obj instanceof Context)) {
        return false;
      }
      ContextImpl other = (ContextImpl) obj;
      if (core == null) {
        if (other.core != null) {
          return false;
        }
      } else if (!core.equals(other.core)) {
        return false;
      }
      return true;
    }
  }
}
