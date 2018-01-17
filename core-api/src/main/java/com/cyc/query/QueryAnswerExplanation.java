package com.cyc.query;

/*
 * #%L
 * File: QueryAnswerExplanation.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2018 Cycorp, Inc
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

/**
 * An explanation for a specific answer to a query. There is currently only one type of explanation
 * provided by the APIs -- {@link com.cyc.query.ProofView} -- but this interface exists to allow the
 * possibility of alternate types.
 *
 * <p>
 * Note that in earlier API versions, this was sometimes referred to as a "Justification". To reduce
 * ambiguity, this interface has been renamed as of Core API 1.0.0.
 *
 * @author daves
 */
public interface QueryAnswerExplanation {

  //====|    Factory methods    |=================================================================//
  
  /**
   * Returns an explanation for an answer, generating one if necessary via a call to
   * {@link QueryAnswerExplanationGenerator#generate()}.
   *
   * @param <T>    type of QueryAnswerExplanation to be generated.
   * @param answer the answer for which to generate a QueryAnswerExplanation.
   * @param spec   the configuration parameters for QueryAnswerExplanation generation.
   *
   * @return a QueryAnswerExplanation.
   */
  public static <T extends QueryAnswerExplanation>
          T get(QueryAnswer answer, QueryAnswerExplanationSpecification<T> spec) {
    return Cyc.findExplanationService(answer, spec).getExplanation(answer, spec);
  }

  //====|    Methods    |=========================================================================//
          
}
