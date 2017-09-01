/*
 * Copyright 2015 Cycorp, Inc..
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

package com.cyc.kb.spi;

/*
 * #%L
 * File: KbApiService.java
 * Project: Core API Object Specification
 * %%
 * Copyright (C) 2013 - 2017 Cycorp, Inc
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

/**
 *
 * @author nwinant
 */
public interface KbApiService {
    
  AssertionService assertion();

  BinaryPredicateService binaryPredicate();

  KbCollectionService collection();

  ContextService context();

  FactService fact();

  FirstOrderCollectionService firstOrderCollection();
  
  KbFunctionService function();
  
  KbIndividualService individual();
  
  KbService kb();    // TODO: rename and/or reorganize w/ KbObjectService? - nwinant, 2017-07-27
  
  //KbObjectService kbObject();
  
  KbPredicateService predicate();
  
  RelationService relation();

  RuleService rule();

  SecondOrderCollectionService secondOrderCollection();

  SentenceService sentence();

  SymbolService symbol();

  KbTermService term();

  VariableService variable();

}
