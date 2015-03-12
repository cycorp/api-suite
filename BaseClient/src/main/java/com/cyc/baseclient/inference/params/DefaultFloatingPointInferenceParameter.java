package com.cyc.baseclient.inference.params;

/*
 * #%L
 * File: DefaultFloatingPointInferenceParameter.java
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

//// External Imports
import com.cyc.base.BaseClientRuntimeException;
import java.util.Map;

//// Internal Imports
import com.cyc.base.CycApiException;
import com.cyc.baseclient.CycObjectFactory;
import com.cyc.baseclient.cycobject.CycArrayList;
import com.cyc.baseclient.cycobject.CycSymbolImpl;

/**
 * <P>DefaultBooleanInferenceParameter is designed to...
 *
 * <P>Copyright (c) 2004 - 2009 Cycorp, Inc.  All rights reserved.
 * <BR>This software is the proprietary information of Cycorp, Inc.
 * <P>Use is subject to license terms.
 *
 * @author zelal
 * @date August 9, 2005, 9:09 PM
 * @version $Id: DefaultFloatingPointInferenceParameter.java 155703 2015-01-05 23:15:30Z nwinant $
 */
public class DefaultFloatingPointInferenceParameter extends AbstractInferenceParameter implements FloatingPointInferenceParameter {

  //// Constructors
  /** Creates a new instance of DefaultBooleanInferenceParameter. */
  public DefaultFloatingPointInferenceParameter(Map propertyMap) {
    super(propertyMap);
    for (int i = 0, size = REQUIRED_SYMBOLS.length; i < size; i++) {
      if (propertyMap.get(REQUIRED_SYMBOLS[i]) == null) {
        throw new BaseClientRuntimeException("Expected key not found in map " +
                REQUIRED_SYMBOLS[i] +
                " for inference parameter " + propertyMap.get(AbstractInferenceParameter.ID_SYMBOL));
      }
    }
    Object maxValueObj = DefaultInferenceParameterValueDescription.verifyObjectType(propertyMap, MAX_VALUE_SYMBOL, Number.class);
    Object minValueObj = DefaultInferenceParameterValueDescription.verifyObjectType(propertyMap, MIN_VALUE_SYMBOL, Number.class);
    init(((Number) maxValueObj).doubleValue(), ((Number) minValueObj).doubleValue());
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
    double potentialDouble = ((Number) potentialValue).doubleValue();
    if (potentialDouble > maxValue) {
      return false;
    } else if (potentialDouble < minValue) {
      return false;
    } else {
      return true;
    }
  }

  public double getMaxValue() {
    return maxValue;
  }

  public double getMinValue() {
    return minValue;
  }

  public String toString() {
    return super.toString() + " min=" + minValue + " max=" + maxValue;
  }

  @Override
  public Object canonicalizeValue(final Object value) {
    if (value != null && "PlusInfinity".equals(value.toString()) ||
            CycObjectFactory.nil.equals(value)) {
      return DefaultInferenceParameters.getInfiniteValue();
    } else {
      return super.canonicalizeValue(value);
    }
  }


  public Object parameterValueCycListApiValue(Object val) {
    return val;
  }

  //// Protected Area

  //// Private Area
  private void init(double maxValue, double minValue) {
    this.maxValue = maxValue;
    this.minValue = minValue;
  }
  //// Internal Rep
  private double maxValue;
  private double minValue;
  private final static CycSymbolImpl MAX_VALUE_SYMBOL = new CycSymbolImpl(":MAX-VALUE");
  private final static CycSymbolImpl MIN_VALUE_SYMBOL = new CycSymbolImpl(":MIN-VALUE");
  private final static CycSymbolImpl[] REQUIRED_SYMBOLS = {MAX_VALUE_SYMBOL,
    MIN_VALUE_SYMBOL};

  //// Main
  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
  }
}
