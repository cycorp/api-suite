package com.cyc.kb.spi;

/*
 * #%L
 * File: KbApiService.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2019 Cycorp, Inc
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
import com.cyc.CycApiEntryPoint;

/**
 * The primary entrypoint for a KB API implementation.
 * 
 * @author nwinant
 */
public interface KbApiService extends CycApiEntryPoint {

  AssertionService getAssertionService();

  BinaryPredicateService getBinaryPredicateService();

  ContextService getContextService();

  FactService getFactService();

  FirstOrderCollectionService getFirstOrderCollectionService();

  KbCollectionService getKbCollectionService();

  KbFunctionService getKbFunctionService();

  KbIndividualService getKbIndividualService();

  KbPredicateService getKbPredicateService();

  KbService getKbService();    // TODO: rename and/or reorganize? - nwinant, 2017-10-16

  KbTermService getKbTermService();

  RelationService getRelationService();

  RuleService getRuleService();

  SecondOrderCollectionService getSecondOrderCollectionService();

  SentenceService getSentenceService();

  SymbolService getSymbolService();

  VariableService getVariableService();

}
