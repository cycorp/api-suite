package com.cyc.query;

/*
 * #%L
 * File: ProofViewGenerator.java
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
import com.cyc.query.exception.ProofViewException;

/**
 * Generates a {@link ProofView} for a specific answer to a query.
 * 
 * @author nwinant
 */
public interface ProofViewGenerator extends QueryAnswerExplanationGenerator<ProofView> {

  //====|    Factory methods    |=================================================================//
  
  /**
   * Finds and returns a <code>ProofViewGenerator</code> instance.
   *
   * @param answer the answer for which to generate a ProofView.
   * @param spec   the configuration parameters for ProofView generation.
   *
   * @return a ProofViewGenerator
   */
  public static ProofViewGenerator get(QueryAnswer answer, ProofViewSpecification spec) {
    return Cyc.getProofViewService().getExplanationGenerator(answer, spec);
  }

  //====|    Methods    |=========================================================================//
  
  /**
   * Marshal this explanation into a DOM tree.
   *
   * @param destination
   */
  void marshal(org.w3c.dom.Node destination);
  
  ProofViewMarshaller getMarshaller() throws ProofViewException;
  
}
