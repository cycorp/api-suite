/*
 * Copyright 2017 Cycorp, Inc.
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
package com.cyc.query.spi;

/*
 * #%L
 * File: ProofViewService.java
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

import com.cyc.query.ProofView;
import com.cyc.query.ProofViewGenerator;
import com.cyc.query.ProofViewSpecification;
import com.cyc.query.QueryAnswer;
import com.cyc.query.QueryAnswerExplanationSpecification;

/**
 * Service provider interface for factories which produce ProofViewGenerators.
 * 
 * @author nwinant
 */
public interface ProofViewService 
        extends QueryAnswerExplanationService<ProofView> {
  
  /**
   * Returns a new ProofViewSpecification object.
   * 
   * @return a ProofViewSpecification.
   */
  ProofViewSpecification getSpecification();
  
  /**
   * Finds and returns a <code>ProofViewGenerator</code> instance.
   * 
   * @param answer the answer for which to generate a ProofView.
   * @param spec the configuration parameters for ProofView generation.
   * @return a ProofViewGenerator.
   */
  @Override
  ProofViewGenerator getExplanationGenerator(QueryAnswer answer, QueryAnswerExplanationSpecification<ProofView> spec);
  
  /**
   * Returns a <code>ProofView</code> for an answer, generating one if necessary via a call to 
   * {@link ProofViewGenerator#generate()}.
   * 
   * @param answer the answer for which to generate a ProofView.
   * @param spec the configuration parameters for ProofView generation.
   * @return a ProofView.
   */
  @Override
  ProofView getExplanation(QueryAnswer answer, QueryAnswerExplanationSpecification<ProofView> spec);
  
}
