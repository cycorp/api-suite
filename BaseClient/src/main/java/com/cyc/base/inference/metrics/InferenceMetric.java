package com.cyc.base.inference.metrics;

/*
 * #%L
 * File: InferenceMetric.java
 * Project: Base Client
 * %%
 * Copyright (C) 2013 - 2015 Cycorp, Inc.
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

import com.cyc.base.cycobject.CycSymbol;
import com.cyc.baseclient.inference.metrics.StandardInferenceMetric;

/**
 * An interface representing an inference metric.
 * Inference metrics are a way of telling Cyc's inference engine to collect
 * a certain type of data for later inspection.
 * The most common metrics are in {@link StandardInferenceMetric}.
 * @author baxter
 */
public interface InferenceMetric {

  /** 
   * Return the name by which this metric is known to Cyc.
   * @return the name
   */
  CycSymbol getName();
  
}
