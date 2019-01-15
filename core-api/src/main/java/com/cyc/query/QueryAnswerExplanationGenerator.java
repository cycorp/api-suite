package com.cyc.query;

/*
 * #%L
 * File: QueryAnswerExplanationGenerator.java
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


import com.cyc.Cyc;
import com.cyc.session.exception.OpenCycUnsupportedFeatureException;

/**
 * An interface for representing and generating {@link QueryAnswerExplanation}s for a specific 
 * answer to a query.
 *
 * @author baxter
 * @param <T> type of QueryAnswerExplanation to be generated
 */
public interface QueryAnswerExplanationGenerator<T extends QueryAnswerExplanation> {
  
  //====|    Factory methods    |=================================================================//
  
  /**
   * Finds and returns an instance of a <code>QueryAnswerExplanationGenerator</code> suitable for
   * generating and returning a <code>QueryAnswerExplanation</code> for a given
   * <code>QueryAnswerExplanationSpecification</code>.
   *
   * @param <T>    type of QueryAnswerExplanation to be generated.
   * @param answer the answer for which to generate a QueryAnswerExplanation.
   * @param spec   the configuration parameters for QueryAnswerExplanation generation.
   *
   * @return a QueryAnswerExplanationGenerator.
   */
  public static <T extends QueryAnswerExplanation> 
        QueryAnswerExplanationGenerator<T> get(QueryAnswer answer, 
                                               QueryAnswerExplanationSpecification<T> spec) {
    return Cyc.findExplanationService(answer, spec).getExplanationGenerator(answer, spec);
  }
  
  //====|    Methods    |=========================================================================//
  
  /**
   * Return the explanation, generating one if necessary via a call to {@link #generate()}.
   *
   * @return  a QueryAnswerExplanation
   * @throws  OpenCycUnsupportedFeatureException when run against an OpenCyc server
   */
  T getExplanation() throws OpenCycUnsupportedFeatureException;

  /**
   * Flesh out this explanation, setting its root node and tree structure underneath the root.
   * 
   * @throws  OpenCycUnsupportedFeatureException when run against an OpenCyc server
   */
  void generate() throws OpenCycUnsupportedFeatureException;

  /**
   * Returns the inference answer justified by this object
   *
   * @return  the inference answer
   */
  QueryAnswer getAnswer();

}
