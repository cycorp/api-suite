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
 * File: ServiceTestExamplesNotInKb.java
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author nwinant
 */
public class ServiceTestExamplesNotInKb {
  
  // Fields
  
  static final public String FLYING_DONE_BY_PILOT_SENTENCE_STRING = "(flyingDoneBySomething-Operate FlyingAPlane-APITest Pilot-APITest)";
  static final public String IST_FLYING_DONE_BY_PILOT_SENTENCE_STRING = "(ist SomeAirlineLogMt (flyingDoneBySomething-Operate FlyingAPlane-APITest Pilot-APITest))";
  static final public List<String> SENTENCES = Arrays.asList(
          "(isa Collection Collection)",
          "(isa Collection Thing)",
          "(genls ?FOO SpatialThing)",
          //            "(PackageFn ?X)",
          "(genls ?FOO SpatialThing EiffelTower)",
          "(#$genls ?FOO SpatialThing EiffelTower)",
          "(   genls ?FOO SpatialThing EiffelTower)",
          "(   #$genls ?FOO SpatialThing EiffelTower)",
          FLYING_DONE_BY_PILOT_SENTENCE_STRING,
          IST_FLYING_DONE_BY_PILOT_SENTENCE_STRING
  );
  
  static final public List<String> SYMBOLS = Arrays.asList(
          ":SUBJECT", ":ACTION", ":subject", ":action",
          ":some-keyword"
          /*
          "ISA", "somenonexistentsymbol", "SOMENONEXISTENTSYMBOL", "someNonExistentSymbol",
          "some-non-existent-symbol", "some-Non-Existent-Symbol", "SOME-NON-EXISTENT-SYMBOL"
          */
          );
  static final public List<String> INVALID_SYMBOLS = Arrays.asList(
          "(somenonexistentsymbol)", "(someNonExistentSymbol)", "(SOMENONEXISTENTSYMBOL)",
          "(some-non-existent-symbol)", "(some-Non-Existent-Symbol)", "(SOME-NON-EXISTENT-SYMBOL)");
  
  static final public List<String> VARIABLES = Arrays.asList(
          "?X", "?VAR", "?x", "?var");
  
  static final public List<String> ASSERTION_SENTENCES_NOT_IN_KB = Arrays.asList();
  /*
  static final private List<String> MISSING_CONSTANTS = Arrays.asList(
          
  );
  */
  static final private List<String> VALID_NATS = Arrays.asList(
          //"(TypeIncapableFn SpatialThing)",
          //"(TypeIncapableFn ?X)",
          //"(PackageFn ?X)",
          //"(DefaultSemanticsForStringFn \"ztarch pizza\")",
          //"(JuvenileFn isa ?X genls JuvenileFn)",
          //"(JuvenileFn ?X)",
          "(TheSetOf ?X (objectHasColor ?X GreenColor))",
          "(#$TheSetOf ?X (objectHasColor ?X GreenColor))",
          "(   TheSetOf ?X (objectHasColor ?X GreenColor))",
          "(   #$TheSetOf ?X (objectHasColor ?X GreenColor))");
  static final private List<String> INVALID_NATS = Arrays.asList(
          "(TypeIncapableFn SpatialThing)",
          "(TypeIncapableFn ?X)",
          "(PackageFn ?X)",
          //"(DefaultSemanticsForStringFn \"ztarch pizza\")",
          "(JuvenileFn isa ?X genls JuvenileFn)",
          "(JuvenileFn ?X)"
          //"(TheSetOf ?X (objectHasColor ?X GreenColor))"
          );
  
  static final public List<String> ALL_TERMS_NOT_IN_KB = new ArrayList();
    
  static {
    //ALL_ASSERTIONS_NOT_IN_KB.addAll(ASSERTION_SENTENCES_NOT_IN_KB);
    
    ALL_TERMS_NOT_IN_KB.addAll(VALID_NATS);
    //ALL_TERMS_NOT_IN_KB.addAll(INVALID_NATS);
  }
  
  static final public List<String> ALL_EXAMPLES_NOT_IN_KB = new ArrayList();
  
  static {
    ALL_EXAMPLES_NOT_IN_KB.addAll(SENTENCES);
    ALL_EXAMPLES_NOT_IN_KB.addAll(SYMBOLS);
    ALL_EXAMPLES_NOT_IN_KB.addAll(VARIABLES);
    ALL_EXAMPLES_NOT_IN_KB.addAll(INVALID_SYMBOLS);
    ALL_EXAMPLES_NOT_IN_KB.addAll(ASSERTION_SENTENCES_NOT_IN_KB);
    ALL_EXAMPLES_NOT_IN_KB.addAll(ALL_TERMS_NOT_IN_KB);
  }
}
