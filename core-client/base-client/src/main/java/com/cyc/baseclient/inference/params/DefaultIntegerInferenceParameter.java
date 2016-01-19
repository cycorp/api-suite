package com.cyc.baseclient.inference.params;

/*
 * #%L
 * File: DefaultIntegerInferenceParameter.java
 * Project: Base Client
 * %%
 * Copyright (C) 2013 - 2016 Cycorp, Inc.
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

//// External Imports
import com.cyc.base.exception.BaseClientRuntimeException;
import com.cyc.base.cycobject.Fort;
import com.cyc.base.cycobject.CycList;
import com.cyc.base.cycobject.CycSymbol;
import com.cyc.query.InferenceParameterValueDescription;
import java.util.Map;
import com.cyc.baseclient.CycObjectFactory;
import com.cyc.baseclient.cycobject.CycSymbolImpl;


/**
 * <P>DefaultBooleanInferenceParameter is designed to...
 *
 * <P>Copyright (c) 2004 - 2006 Cycorp, Inc.  All rights reserved.
 * <BR>This software is the proprietary information of Cycorp, Inc.
 * <P>Use is subject to license terms.
 *
 * @author zelal
 * @date August 9, 2005, 9:09 PM
 * @version $Id: DefaultIntegerInferenceParameter.java 163356 2016-01-04 20:55:47Z nwinant $
 */
public class DefaultIntegerInferenceParameter extends AbstractInferenceParameter
        implements IntegerInferenceParameter {

  //// Constructors
  /** Creates a new instance of DefaultBooleanInferenceParameter. */
  public DefaultIntegerInferenceParameter(Map<String, Object> propertyMap) {
    super(propertyMap);
    for (int i = 0, size = REQUIRED_SYMBOLS.length; i < size; i++) {
      if (propertyMap.get(REQUIRED_SYMBOLS[i]) == null) {
        throw new BaseClientRuntimeException("Expected key not found in map " +
                REQUIRED_SYMBOLS[i] +
                " for inference parameter " + propertyMap.get(AbstractInferenceParameter.ID_SYMBOL));
      }
    }
    Object maxValObj = DefaultInferenceParameterValueDescription.verifyObjectType(propertyMap, MAX_VALUE_SYMBOL, Number.class);
    Object minValObj = DefaultInferenceParameterValueDescription.verifyObjectType(propertyMap, MIN_VALUE_SYMBOL, Number.class);
    init(((Number) maxValObj).longValue(), ((Number) minValObj).longValue());
  }

  protected DefaultIntegerInferenceParameter(Object defaultValue, String keyword,
          Fort id, String shortDescription, String longDescription,
          CycSymbol isBasicParameter, CycSymbol isQueryStaticParameter, CycList alternateValue,
          Number minValue, Number maxValue) {
    super(defaultValue, keyword, id, shortDescription, longDescription, isBasicParameter,
            isQueryStaticParameter, alternateValue);
    init(maxValue.longValue(), minValue.longValue());
  }

  //// Public Area
  public boolean isValidValue(Object potentialValue) {
    if (isAlternateValue(potentialValue)) {
      return true;
    } else if (DefaultInferenceParameters.isInfiniteValue(potentialValue)) {
      return true;
    } else if (!(potentialValue instanceof Number)) {
      return false;
    }
    long potentialLong = ((Number) potentialValue).longValue();
    if (potentialLong > maxValue) {
      return false;
    } else if (potentialLong < minValue) {
      return false;
    } else {
      return true;
    }
  }

  @Override
  public Object canonicalizeValue(final Object value) {
    if (value != null && isNoncanonicalInfiniteValue(value)) {
      return DefaultInferenceParameters.getInfiniteValue();
    } else {
      return super.canonicalizeValue(value);
    }
  }

  public long getMaxValue() {
    return maxValue;
  }

  public long getMinValue() {
    return minValue;
  }

  @Override
  public String toString() {
    return super.toString() + " min=" + minValue + " max=" + maxValue;
  }


  public Object parameterValueCycListApiValue(Object val) {
    if (DefaultInferenceParameters.isInfiniteValue(val)) {
      return getInfiniteValueCycListApiValue();
    } else {
      return val;
    }
  }

  //// Protected Area
  //// Private Area
  private void init(long maxValue, long minValue) {
    this.maxValue = maxValue;
    this.minValue = minValue;
  }

  private boolean isNoncanonicalInfiniteValue(final Object value) {
    return "PlusInfinity".equals(value.toString()) ||
            CycObjectFactory.nil.equals(value) ||
            ":POSITIVE-INFINITY".equals(value.toString());
  }

  private Object getInfiniteValueCycListApiValue() {
    if (useNilForInfiniteValue()) {
      return CycObjectFactory.nil;
    } else {
      return POSITIVE_INFINITY;
    }
  }

  private boolean useNilForInfiniteValue() {
    final InferenceParameterValueDescription alternateValue = getAlternateValue();
    return alternateValue != null && alternateValue.getValue() == null;
  }
  //// Internal Rep
  private long maxValue;
  private long minValue;
  private final static String MAX_VALUE_SYMBOL = ":MAX-VALUE";
  private final static String MIN_VALUE_SYMBOL = ":MIN-VALUE";
  private final static String[] REQUIRED_SYMBOLS = {MAX_VALUE_SYMBOL,
    MIN_VALUE_SYMBOL};
  public static final CycSymbol POSITIVE_INFINITY = CycObjectFactory.makeCycSymbol(":POSITIVE-INFINITY");
}
