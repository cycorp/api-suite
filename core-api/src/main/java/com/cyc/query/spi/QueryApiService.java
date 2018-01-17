package com.cyc.query.spi;

/*
 * #%L
 * File: QueryApiService.java
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
import com.cyc.CoreServicesLoader;
import com.cyc.CycApiEntryPoint;
import com.cyc.query.QueryAnswer;
import com.cyc.query.QueryAnswerExplanation;
import com.cyc.query.QueryAnswerExplanationSpecification;
import java.util.List;

/**
 *
 * @author nwinant
 */
public interface QueryApiService extends CycApiEntryPoint {

  QueryService getQueryService();

  ProofViewService getProofViewService();

  List<QueryAnswerExplanationService> getQueryExplanationFactoryServices(CoreServicesLoader loader);

  <T extends QueryAnswerExplanation> QueryAnswerExplanationService<T> findExplanationService(
          CoreServicesLoader loader,
          QueryAnswer answer,
          QueryAnswerExplanationSpecification<T> spec);

}
