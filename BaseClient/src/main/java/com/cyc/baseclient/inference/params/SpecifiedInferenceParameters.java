package com.cyc.baseclient.inference.params;

/*
 * #%L
 * File: SpecifiedInferenceParameters.java
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

import com.cyc.base.inference.InferenceMode;
import com.cyc.base.inference.ResultUniqueness;
import com.cyc.base.CycAccess;
import com.cyc.base.cycobject.CycSymbol;
import com.cyc.base.inference.DisjunctionFreeELVarsPolicy;
import com.cyc.base.inference.InferenceAnswerLanguage;
import com.cyc.base.inference.InferenceParameters;
import com.cyc.base.inference.ProblemReusePolicy;
import com.cyc.base.inference.ProofValidationMode;
import com.cyc.base.inference.TransitiveClosureMode;
import com.cyc.base.inference.metrics.InferenceMetrics;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.cyc.baseclient.CycObjectFactory;
import com.cyc.baseclient.cycobject.CycArrayList;
import com.cyc.baseclient.inference.metrics.InferenceMetricsHashSet;

import static com.cyc.baseclient.inference.params.InferenceParametersSymbols.*;

import com.cyc.baseclient.inference.params.OpenCycInferenceParameterEnum.OpenCycInferenceMode;

/**
 * <P>
 * SpecifiedInferenceParameters is designed to be used when you want to carry
 * around inference parameters, but do not have access to a CycAccess instance.
 * It will not perform value canonicalization or other useful checks on the
 * names or values of the inference parameters. When the time comes to actually
 * run the query, this can be converted into a DefaultInferenceParameters object
 * by providing a CycAccess and calling the toDefaultInferenceParameters method.
 *
 * <P>
 * Copyright (c) 2011 Cycorp, Inc. All rights reserved.
 * <BR>This software is the proprietary information of Cycorp, Inc.
 * <P>
 * Use is subject to license terms.
 *
 * @author daves
 * @date March 15, 2011
 * @version $Id: SpecifiedInferenceParameters.java 151668 2014-06-03 21:46:52Z
 * jmoszko $
 */
public class SpecifiedInferenceParameters implements InferenceParameters {

  @Override
  public Object clone() {
    SpecifiedInferenceParameters copy = new SpecifiedInferenceParameters();
    Iterator<CycSymbol> iterator = this.keySet().iterator();
    while (iterator.hasNext()) {
      CycSymbol key = iterator.next();
      Object value = this.get(key); // note: this might should be cloned
      copy.put(key, value);
    }
    return copy;
  }

//  @Override
  public DefaultInferenceParameters toDefaultInferenceParameters(CycAccess cyc) {
    DefaultInferenceParameters copy = new DefaultInferenceParameters(cyc);
    Iterator<CycSymbol> iterator = this.keySet().iterator();
    while (iterator.hasNext()) {
      CycSymbol key = iterator.next();
      Object value = this.get(key); // note: this might should be cloned
      copy.put(key, value);
    }
    return copy;
  }

  @Override
  public void clear() {
    map.clear();
  }

  @Override
  public Object parameterValueCycListApiValue(CycSymbol key, Object val) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Set<Entry<CycSymbol, Object>> entrySet() {
    return Collections.unmodifiableSet(map.entrySet());
  }

  @Override
  public Integer getMaxNumber() {
    final Object rawValue = get(MAX_NUMBER);
    if (rawValue instanceof Integer) {
      return (Integer) rawValue;
    } else {
      return null;
    }
  }

  @Override
  public Integer getMaxTime() {
    final Object rawValue = get(MAX_TIME);
    if (rawValue instanceof Integer) {
      return (Integer) rawValue;
    } else {
      return null;
    }
  }

  @Override
  public Set<CycSymbol> keySet() {
    return Collections.unmodifiableSet(map.keySet());
  }

  @Override
  public Object get(CycSymbol parameterName) {
    return map.get(parameterName);
  }

  @Override
  public void putAll(InferenceParameters properties) {
    for (final CycSymbol key : properties.keySet()) {
      put(key, properties.get(key));
    }
  }

  @Override
  public void remove(CycSymbol property) {
    map.remove(property);
  }

  @Override
  public Object put(CycSymbol parameterName, Object value) {
    return map.put(parameterName, value);
  }

  @Override
  public boolean containsKey(CycSymbol key) {
    return map.containsKey(key);
  }

  @Override
  public InferenceParameters setMaxNumber(Integer max) {
    put(MAX_NUMBER, max);
    return this;
  }

  @Override
  public InferenceParameters setMaxTime(Integer max) {
    put(MAX_TIME, max);
    return this;
  }

  /**
   * Get the set of inference metrics to gather.
   *
   * @return the inference metrics.
   */
  @Override
  public synchronized InferenceMetricsHashSet getMetrics() {
    InferenceMetricsHashSet metrics = (InferenceMetricsHashSet) get(METRICS);
    if (metrics == null) {
      metrics = new InferenceMetricsHashSet();
      setMetrics(metrics);
    }
    return metrics;
  }

  /**
   * Specify the set of inference metrics to gather.
   *
   * @param metrics
   */
  @Override
  public InferenceParameters setMetrics(InferenceMetrics metrics) {
    put(METRICS, metrics);
    return this;
  }

  @Override
  public InferenceParameters setInferenceMode(InferenceMode mode) {
    put(INFERENCE_MODE, mode.getDescription().getValue());
    return this;
  }

  @Override
  public InferenceMode getInferenceMode() {
    final Object modeSymbol = get(INFERENCE_MODE);
    return (modeSymbol == null) ? null : OpenCycInferenceMode.fromString(
            modeSymbol.toString());
  }

  @Override
  public String stringApiValue() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Object cycListApiValue() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public CycAccess getCycAccess() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void updateFromPlist(final List plist) {
    for (int i = 0; i < plist.size(); i++) {
      final CycSymbol paramKey = (CycSymbol) plist.get(i++);
      final Object paramValue = plist.get(i);
      put(paramKey, paramValue);
    }
  }

  @Override
  public boolean getAbductionAllowed() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public InferenceParameters setMaxTransformationDepth(Integer i) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Integer getMaxTransformationDepth() {
    final Object rawValue = get(MAX_TRANSFORMATION_DEPTH);
    if (rawValue instanceof Integer) {
      return (Integer) rawValue;
    } else {
      return null;
    }
  }

  @Override
  public void setProblemStorePath(String path) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void setProblemStoreId(int id) {
    put(PROBLEM_STORE, new CycArrayList(FIND_PROBLEM_STORE_BY_ID, id));
  }

  @Override
  public boolean usesLoadedProblemStore() {
    final Object value = get(PROBLEM_STORE);
    return (value instanceof CycArrayList
            && LOAD_PROBLEM_STORE.equals(((CycArrayList) value).first()));
  }

  @Override
  public void makeAtLeastAsLooseAs(final InferenceParameters newParams) {
    if (newParams.getMaxTransformationDepth() == null || newParams.getMaxTransformationDepth() > this.getMaxTransformationDepth()) {
      setMaxTransformationDepth(newParams.getMaxTransformationDepth());
    }
    if (newParams.getMaxTime() == null || newParams.getMaxTime() > this.getMaxTime()) {
      setMaxTime(newParams.getMaxTime());
    }
    if (newParams.getMaxNumber() == null || newParams.getMaxNumber() > this.getMaxNumber()) {
      setMaxNumber(newParams.getMaxNumber());
    }
    //@TODO -- Add more as needed.
  }

  @Override
  public Boolean isBrowsable() {
    return (Boolean) get(BROWSABLE);
  }

  @Override
  public InferenceParameters setBrowsable(boolean b) {
    final CycSymbol value = b ? CycObjectFactory.t : CycObjectFactory.nil;
    put(BROWSABLE, value);
    return this;
  }

  @Override
  public boolean equals(Object rhs) {
    // Can't guarantee that this works on subclasses right now, so use reflection
    //  to verify that we're not looking at subclasses.
    // TODO:  Generalize this to work on subclasses as well, or override .equals
    //  in those classes in some appropriate fashion
    return this.getClass().equals(SpecifiedInferenceParameters.class)
            && rhs.getClass().equals(SpecifiedInferenceParameters.class)
            && map.equals(((SpecifiedInferenceParameters) rhs).map);
  }

  @Override
  public int hashCode() {
    return map.hashCode();
  }
  final Map<CycSymbol, Object> map = new HashMap<CycSymbol, Object>();

  @Override
  public String toString() {
    final int maxLen = 10;
    StringBuilder builder = new StringBuilder();
    builder.append("SpecifiedInferenceParameters [");
    if (map != null) {
      builder.append("map=").append(toString(map.entrySet(), maxLen));
    }
    builder.append("]");
    return builder.toString();
  }

  private String toString(Collection<?> collection, int maxLen) {
    StringBuilder builder = new StringBuilder();
    builder.append("[");
    int i = 0;
    for (Iterator<?> iterator = collection.iterator(); iterator.hasNext() && i < maxLen; i++) {
      if (i > 0) {
        builder.append(", ");
      }
      builder.append(iterator.next());
    }
    builder.append("]");
    return builder.toString();
  }

  @Override
  public Boolean isContinuable() {
    return (Boolean) get(CONTINUABLE);
  }

  @Override
  public InferenceParameters setContinuable(boolean b) {
    final CycSymbol value = b ? CycObjectFactory.t : CycObjectFactory.nil;
    put(CONTINUABLE, value);
    return this;
  }

  @Override
  public InferenceParameters setResultUniqueness(ResultUniqueness value) {
    put(RESULT_UNIQUENESS, value);
    return this;
  }

  @Override
  public ResultUniqueness getResultUniqueness() {
    return getAs(RESULT_UNIQUENESS, ResultUniqueness.class);
  }

  @Override
  public InferenceParameters setAnswerLanguage(InferenceAnswerLanguage value) {
    put(ANSWER_LANGUAGE, value);
    return this;
  }

  @Override
  public InferenceAnswerLanguage getAnswerLanguage() {
    return getAs(ANSWER_LANGUAGE, InferenceAnswerLanguage.class);
  }

  @Override
  public InferenceParameters setTransitiveClosureMode(TransitiveClosureMode value) {
    put(TRANSITIVE_CLOSURE_MODE, value);
    return this;
  }

  @Override
  public TransitiveClosureMode getTransitiveClosureMode() {
    return getAs(TRANSITIVE_CLOSURE_MODE, TransitiveClosureMode.class);
  }

  @Override
  public InferenceParameters setProofValidationMode(ProofValidationMode value) {
    put(INTERMEDIATE_STEP_VALIDATION_LEVEL, value);
    return this;
  }

  @Override
  public ProofValidationMode getProofValidationMode() {
    return getAs(INTERMEDIATE_STEP_VALIDATION_LEVEL, ProofValidationMode.class);
  }

  @Override
  public InferenceParameters setDisjunctionFreeELVarsPolicy(DisjunctionFreeELVarsPolicy value) {
    put(DISJUNCTION_FREE_EL_VARS_POLICY, value);
    return this;
  }

  @Override
  public DisjunctionFreeELVarsPolicy getDisjunctionFreeELVarsPolicy() {
    return getAs(DISJUNCTION_FREE_EL_VARS_POLICY, DisjunctionFreeELVarsPolicy.class);
  }

  @Override
  public InferenceParameters setProblemReusePolicy(ProblemReusePolicy value) {
    put(EQUALITY_REASONING_DOMAIN, value);
    return this;
  }

  @Override
  public ProblemReusePolicy getProblemReusePolicy() {
    return getAs(EQUALITY_REASONING_DOMAIN, ProblemReusePolicy.class);
  }

  private <T extends Enum> T getAs(CycSymbol key, Class<T> type) {
    return coerceToEnum(get(key), type);
  }

  private static <T extends Enum> T coerceToEnum(final Object value, final Class<T> type) {
    if (value == null) {
      return null;
    } else if (type.isInstance(value)) {
      return type.cast(value);
    } else if (value instanceof CycSymbol) {
      return type.cast(Enum.valueOf((Class<T>) type, ((CycSymbol) value).getSymbolName().replace('-', '_')));
    } else {
      throw new IllegalStateException("Bad " + type.getSimpleName() + " value " + value);
    }
  }
}
