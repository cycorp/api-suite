package com.cyc.query;

/*
 * #%L
 * File: ProofViewSpecification.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2017 Cycorp, Inc
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
import com.cyc.Cyc;
import com.cyc.kb.Context;

/**
 * The configuration parameters which determine how a {@link ProofView} should be generated.
 *
 * @author daves
 */
public interface ProofViewSpecification extends QueryAnswerExplanationSpecification<ProofView> {

  //====|    Factory methods    |=================================================================//
  
  /**
   * Returns a new ProofViewSpecification object.
   *
   * @return a ProofViewSpecification.
   */
  public static ProofViewSpecification get() {
    return Cyc.getProofViewService().getSpecification();
  }

  //====|    Methods    |=========================================================================//
  
  public ProofViewSpecification setIncludeDetails(boolean includeDetails);

  public Boolean isIncludeDetails();

  public ProofViewSpecification setIncludeLinear(boolean includeLinear);

  public Boolean isIncludeLinear();

  public ProofViewSpecification setIncludeSummary(boolean includeSummary);

  public Boolean isIncludeSummary();

  public ProofViewSpecification setDomainContext(Context domainContext);

  public Context getDomainContext();

  public ProofViewSpecification setLanguageContext(Context languageContext);

  public Context getLanguageContext();

  public ProofViewSpecification setIncludeAssertionBookkeeping(boolean includeBookkeeping);

  public Boolean isIncludeAssertionBookkeeping();

  public ProofViewSpecification setIncludeAssertionCyclists(boolean includeAssertionCyclists);

  public Boolean isIncludeAssertionCyclists();

}
