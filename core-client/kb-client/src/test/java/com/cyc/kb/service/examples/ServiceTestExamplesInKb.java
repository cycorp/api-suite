/*
 * Copyright 2015 Cycorp, Inc.
 *
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
 */
package com.cyc.kb.service.examples;

/*
 * #%L
 * File: ServiceTestExamplesInKb.java
 * Project: KB Client
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

import com.cyc.kb.Context;
import com.cyc.kb.Fact;
import com.cyc.kb.Sentence;
import com.cyc.kb.client.ContextImpl;
import com.cyc.kb.client.FactImpl;
import com.cyc.kb.client.SentenceImpl;
import com.cyc.kb.exception.KbException;
import static com.cyc.kb.service.examples.ServiceTestExamplesNotInKb.FLYING_DONE_BY_PILOT_SENTENCE_STRING;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author nwinant
 */
public class ServiceTestExamplesInKb {
  
  // Assertions
  
  static final public String CTX_STR = "SomeAirlineLogMt";
  static final public Context CTX;
  static final public Sentence FLYING_DONE_BY_PILOT_SENTENCE;
  static final public Fact FLYING_DONE_BY_PILOT_FACT;
  static final public String FLYING_DONE_BY_PILOT_HLID;
  
  static final private List<String> ASSERTION_HLIDS = Collections.unmodifiableList(Arrays.asList(
          //"MyENggwNgQ2DHiu9WIEEnCkRsZ2tw3ljb3JwHw2CHiu_e0ysnCkRsZ2tw3ljb3JwHiu_1SygnCkRsZ2tw3ljb3JwHiu9WIDMnCkRsZ2tw3ljb3JwHivf9KBBTaIR1oLAAAKzTHyf",
          "MyENggwNgQ2EHiu-4i09nCkRsZ2tw3ljb3JwHisWcSv4V6MR2oAAAAKzi8-Wgx4rv21MfpwpEbGdrcN5Y29ycB4r3_SgQU2iEdaCwAACs0x8nw",
          "MyENggwNgQ2DHiu9WIEOnCkRsZ2tw3ljb3JwHiu9WNwqnCkRsZ2tw3ljb3JwHiu9WIyfnCkRsZ2tw3ljb3JwHivf9KBBTaIR1oLAAAKzTHyf",
          //"MyENggwNgQ2DHiu9WIEOnCkRsZ2tw3ljb3JwHivBAIawnCkRsZ2tw3ljb3JwHiu9WOaMnCkRsZ2tw3ljb3JwHivf9KBBTaIR1oLAAAKzTHyf",
          "MyENggwNgQ2DHiu9WIDOnCkRsZ2tw3ljb3JwHiu-bZg4nCkRsZ2tw3ljb3JwgR4r3_SgQU2iEdaCwAACs0x8nw"
  ));
  static final private List<String> FACT_HLIDS = Collections.unmodifiableList(Arrays.<String>asList());
  static final private List<String> RULE_HLIDS = Collections.unmodifiableList(Arrays.<String>asList());
  
  static final public List<String> ALL_FACT_HLIDS_IN_KB;
  static final public List<String> ALL_ASSERTION_HLIDS_IN_KB;

  static {
    try {
      CTX = ContextImpl.get(CTX_STR);
      FLYING_DONE_BY_PILOT_SENTENCE = new SentenceImpl(FLYING_DONE_BY_PILOT_SENTENCE_STRING);
      FLYING_DONE_BY_PILOT_FACT = FactImpl.findOrCreate(FLYING_DONE_BY_PILOT_SENTENCE, CTX);
      FLYING_DONE_BY_PILOT_HLID = FLYING_DONE_BY_PILOT_FACT.getId();
    } catch (KbException ex) {
      throw new RuntimeException(ex);
    }
    final List<String> allFactHlidsInKb = new ArrayList();
    allFactHlidsInKb.add(FLYING_DONE_BY_PILOT_HLID);
    allFactHlidsInKb.addAll(FACT_HLIDS);
    ALL_FACT_HLIDS_IN_KB = Collections.unmodifiableList(allFactHlidsInKb);
    
    final List<String> allAssertionHlidsInKb = new ArrayList();
    allAssertionHlidsInKb.addAll(ASSERTION_HLIDS);
    allAssertionHlidsInKb.addAll(ALL_FACT_HLIDS_IN_KB);
    allAssertionHlidsInKb.addAll(RULE_HLIDS);
    ALL_ASSERTION_HLIDS_IN_KB = Collections.unmodifiableList(allAssertionHlidsInKb);
  }
  
  
  // Terms
  
  /*
  // Not present in OpenCyc:
  static final private List<String> TERMS = Collections.unmodifiableList(Arrays.asList(
          "(DefaultSemanticsForStringFn \"starch pizza\")",
          "(DefaultSemanticsForStringFn \"halibut lasagna\")"
  ));
  */
  // Individuals
  static final public String CITY_NAMED_FN_AUSTIN_SENTENCE_STRING = "(CityNamedFn \"Austin\" Texas-State)";
  static final public String CITY_NAMED_FN_AUSTIN_HLID = "Mw2DHiu9aHCmnCkRsZ2tw3ljb3JwD4ZBdXN0aW4eK71ZAZOcKRGxna3DeWNvcnA";
  static final private List<String> INDIVIDUALS = Collections.unmodifiableList(Arrays.<String>asList(
          CITY_NAMED_FN_AUSTIN_SENTENCE_STRING,
          CITY_NAMED_FN_AUSTIN_HLID,
          "(QuantitySlotForArg2Fn Zinc annualProductionVolume)",
          //"(TypeIncapableFn intendedPrimaryFunction)",
          "(CityNamedFn \"North Bergen\" NewJersey-State)",
          //"(FocalFieldOfStudyFn Hospitalist)",
          //"(BuilderQueryForMeaningSentenceOfSKSFn ComputerSoftwareDescriptionTable-SampleDatabase productHasAssociatedVersion-SampleDB)",
          "isa",
          "#$isa",
          " isa",
          "isa ",
          "#$True",
          "Muffet",
          "TheEarthsAtmosphere",
          //"EmpressDowagerCixi-Person",
          "SeaportOfShuaiba",
          //"datumTreatmentType",
          //"212",
          "similarity-Propositional"));
  static final private List<String> ATOMIC_TERMS = Collections.unmodifiableList(Arrays.asList(
          "isa",
          "#$isa",
          " isa",
          "isa ",
          "#$True",
          "Muffet",
          "TheEarthsAtmosphere",
          //"EmpressDowagerCixi-Person",
          "SeaportOfShuaiba",
          //"datumTreatmentType",
          //"212",
          "similarity-Propositional"));
  static final private List<String> NARTS = Collections.unmodifiableList(Collections.unmodifiableList(Arrays.asList(
          CITY_NAMED_FN_AUSTIN_SENTENCE_STRING,
          CITY_NAMED_FN_AUSTIN_HLID,
          "(QuantitySlotForArg2Fn Zinc annualProductionVolume)",
          //"(TypeIncapableFn intendedPrimaryFunction)",
          "(CityNamedFn \"North Bergen\" NewJersey-State)",
          //"(FocalFieldOfStudyFn Hospitalist)",
          //"(BuilderQueryForMeaningSentenceOfSKSFn ComputerSoftwareDescriptionTable-SampleDatabase productHasAssociatedVersion-SampleDB)",
          "(PackageFn OatBran)")));
  static final private List<String> CONTEXTS = Collections.unmodifiableList(Arrays.<String>asList());
  // Relations
  static final private List<String> RELATIONS = Collections.unmodifiableList(Arrays.<String>asList());
  static final private List<String> FUNCTIONS = Collections.unmodifiableList(Arrays.<String>asList());
  static final private List<String> PREDICATES = Collections.unmodifiableList(Arrays.<String>asList());
  static final private List<String> BINARY_PREDICATES = Collections.unmodifiableList(Arrays.<String>asList());
  // Collections
  static final private List<String> COLLECTIONS = Collections.unmodifiableList(Arrays.<String>asList(
          "(PackageFn OatBran)"));
  static final private List<String> FIRST_ORDER_COLLECTIONS = Collections.unmodifiableList(Arrays.asList(
          "Allotment",
          "CorrectionFluid",
          "WeatherQuantity"));
  static final private List<String> SECOND_ORDER_COLLECTIONS = Collections.unmodifiableList(Arrays.<String>asList());
  
  static final public List<String> ALL_RELATIONS_IN_KB;
  static final public List<String> ALL_COLLECTIONS_IN_KB;
  static final public List<String> ALL_INDIVIDUALS_IN_KB;
  static final public List<String> ALL_TERMS_IN_KB;
  
  static {
    final List<String> allCollectionsInKb = new ArrayList();
    allCollectionsInKb.addAll(COLLECTIONS);
    allCollectionsInKb.addAll(FIRST_ORDER_COLLECTIONS);
    allCollectionsInKb.addAll(SECOND_ORDER_COLLECTIONS);
    ALL_COLLECTIONS_IN_KB = Collections.unmodifiableList(allCollectionsInKb);
    
    final List<String> allRelationsInKb = new ArrayList();
    allRelationsInKb.addAll(RELATIONS);
    allRelationsInKb.addAll(FUNCTIONS);
    allRelationsInKb.addAll(PREDICATES);
    allRelationsInKb.addAll(BINARY_PREDICATES);
    ALL_RELATIONS_IN_KB = Collections.unmodifiableList(allRelationsInKb);
    
    final List<String> allIndividualsInKb = new ArrayList();
    allIndividualsInKb.addAll(INDIVIDUALS);
    allIndividualsInKb.addAll(CONTEXTS);
    allIndividualsInKb.addAll(ALL_RELATIONS_IN_KB);
    ALL_INDIVIDUALS_IN_KB = Collections.unmodifiableList(allIndividualsInKb);
    
    final List<String> allTermsInKb = new ArrayList();
    allTermsInKb.addAll(ALL_INDIVIDUALS_IN_KB);
    allTermsInKb.addAll(ALL_COLLECTIONS_IN_KB);
    //allTermsInKb.addAll(TERMS);                     // TODO: ????
    allTermsInKb.addAll(ATOMIC_TERMS);              // TODO: ????
    allTermsInKb.addAll(NARTS);                     // TODO: ????
    ALL_TERMS_IN_KB = Collections.unmodifiableList(allTermsInKb);
  }
  
  static final public List<String> ALL_EXAMPLES_IN_KB;
  
  static {
    final List<String> allExamplesInKb = new ArrayList();
    allExamplesInKb.addAll(ALL_ASSERTION_HLIDS_IN_KB);
    allExamplesInKb.addAll(ALL_TERMS_IN_KB);
    ALL_EXAMPLES_IN_KB = Collections.unmodifiableList(allExamplesInKb);
  }
}
