package com.cyc.query.metrics;

/*
 * #%L
 * File: InferenceMetrics.java
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
import com.cyc.query.parameters.InferenceParameterValue;
import java.util.Set;

/**
 * A Set of {@link InferenceMetric} values.
 *
 * Inference metrics are a way of telling Cyc's inference engine to collect a certain type of data
 * for later inspection. The most common metrics are in {@link StandardInferenceMetric}.
 *
 * @author nwinant
 */
public interface InferenceMetrics extends Set<InferenceMetric>, InferenceParameterValue {

}
