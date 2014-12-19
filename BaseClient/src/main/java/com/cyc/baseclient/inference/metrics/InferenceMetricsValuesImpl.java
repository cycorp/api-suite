package com.cyc.baseclient.inference.metrics;

/*
 * #%L
 * File: InferenceMetricsValuesImpl.java
 * Project: Base Client
 * %%
 * Copyright (C) 2013 - 2014 Cycorp, Inc.
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

import com.cyc.base.CycConnectionException;
import com.cyc.base.cycobject.CycList;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.cyc.base.cycobject.CycSymbol;
import com.cyc.base.inference.InferenceIdentifier;
import com.cyc.base.inference.metrics.InferenceMetric;
import com.cyc.base.inference.metrics.InferenceMetricsValues;

/**
 * A map containing data about an inference.
 * The metrics whose values are returned are those specified in the inference
 * parameters for the inference when it was run.
 * See {@link com.cyc.base.inference.InferenceParameters#setMetrics(com.cyc.base.inference.metrics.InferenceMetrics)}
 *
 * @author baxter
 */
public class InferenceMetricsValuesImpl implements InferenceMetricsValues {

  private final Map<InferenceMetric, Object> map =
          new HashMap<InferenceMetric, Object>();

  /* (non-Javadoc)
 * @see com.cyc.baseclient.inference.metrics.InferenceMetricsValuesI#getValue(com.cyc.base.inference.metrics.InferenceMetric)
 */
@Override
public Object getValue(final InferenceMetric metric) {
    return map.get(metric);
  }

  /**
   * Create a new inference metrics object populated from the specified inference.
   *
   * @param inferenceID the identifier of the target inference.
   * @return data for all metrics collected for the specified inference.
   * @throws IOException if there is a problem communicating with Cyc.
   */
  static public InferenceMetricsValues fromInference(
          final InferenceIdentifier inferenceID) throws CycConnectionException {
    final InferenceMetricsValuesImpl metrics = new InferenceMetricsValuesImpl();
    final CycList plist = inferenceID.getCycAccess().converse().converseList(
            "(inference-metrics " + inferenceID.stringApiValue() + ")");
    for (int i = 0; i < plist.size(); i++) {
      final CycSymbol name = (CycSymbol) plist.get(i);
      final Object value = plist.get(++i);
      InferenceMetric inferenceMetric;
      inferenceMetric = StandardInferenceMetric.fromCycSymbol(name);
      if (inferenceMetric == null) {
        System.out.println(
                name + " does not correspond to a standard inference metric.");
        inferenceMetric = new InferenceMetric() {
          @Override
          public CycSymbol getName() {
            return name;
          }
        };
      }
      metrics.map.put(inferenceMetric, value);
    }
    return metrics;
  }
}
