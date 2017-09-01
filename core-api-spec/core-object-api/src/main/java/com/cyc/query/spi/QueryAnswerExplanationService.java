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
 * File: QueryAnswerExplanationService.java
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

import com.cyc.query.QueryAnswer;
import com.cyc.query.QueryAnswerExplanation;
import com.cyc.query.QueryAnswerExplanationGenerator;
import com.cyc.query.QueryAnswerExplanationSpecification;

/**
 * Service provider interface for factories which produce QueryAnswerExplanationGenerators.
 * 
 * @author nwinant
 * @param <T> type of QueryAnswerExplanation
 */
public interface QueryAnswerExplanationService 
        <T extends QueryAnswerExplanation> {
  
  /**
   * The type of QueryAnswerExplanation for which this service produces potentially suitable 
   * QueryAnswerExplanationGenerators. This method is primarily used during application startup to
   * check that basic types of QueryAnswerExplanation are generally supported. Before an actual
   * QueryAnswerExplanationGenerator is requested, a more specific check will be performed with
   * {@link #isSuitableForSpecification(com.cyc.query.QueryAnswer, com.cyc.query.QueryAnswerExplanationSpecification) }.
   * 
   * @return 
   */
  Class<T> forExplanationType();
  
  /**
   * Whether this service can provide a QueryAnswerExplanationGenerator suitable for a 
   * <em>specific</em> QueryAnswer with a <em>specific</em> QueryAnswerExplanationSpecification.
   * This method is called whenever the QueryFactory is considering whether to request a 
   * QueryAnswerExplanationGenerator from the service instance.
   * 
   * @param answer
   * @param spec
   * @return 
   */
  boolean isSuitableForSpecification(QueryAnswer answer, QueryAnswerExplanationSpecification<T> spec);

  /**
   * Finds and returns an instance of a <code>QueryAnswerExplanationGenerator</code> suitable for
   * generating and returning a <code>QueryAnswerExplanation</code> for a given
   * <code>QueryAnswerExplanationSpecification</code>.
   * 
   * @param answer the answer for which to generate a QueryAnswerExplanation.
   * @param spec the configuration parameters for QueryAnswerExplanation generation.
   * @return a QueryAnswerExplanationGenerator.
   */
  QueryAnswerExplanationGenerator<T> getExplanationGenerator(
          QueryAnswer answer, QueryAnswerExplanationSpecification<T> spec);
  
  /**
   * Returns an explanation for an answer, generating one if necessary via a call to 
   * {@link QueryAnswerExplanationGenerator#generate()}.
   * 
   * @param answer the answer for which to generate a QueryAnswerExplanation.
   * @param spec the configuration parameters for QueryAnswerExplanation generation.
   * @return a QueryAnswerExplanation.
   */
  T getExplanation(QueryAnswer answer, QueryAnswerExplanationSpecification<T> spec);
  
}
