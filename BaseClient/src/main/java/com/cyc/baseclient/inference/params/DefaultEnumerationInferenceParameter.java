package com.cyc.baseclient.inference.params;

/*
 * #%L
 * File: DefaultEnumerationInferenceParameter.java
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
import com.cyc.base.cycobject.Fort;
import com.cyc.base.inference.InferenceParameterValue;
import com.cyc.base.inference.InferenceParameterValueDescription;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//// Internal Imports
import com.cyc.baseclient.cycobject.CycArrayList;
import com.cyc.baseclient.cycobject.CycSymbolImpl;

/**
 * <P>
 * DefaultBooleanInferenceParameter is designed to...
 *
 * <P>
 * Copyright (c) 2004 - 2006 Cycorp, Inc. All rights reserved.
 * <BR>This software is the proprietary information of Cycorp, Inc.
 * <P>
 * Use is subject to license terms.
 *
 * @author zelal
 * @date August 9, 2005, 9:09 PM
 * @version $Id: DefaultEnumerationInferenceParameter.java 151668 2014-06-03
 * 21:46:52Z jmoszko $
 */
public class DefaultEnumerationInferenceParameter extends AbstractInferenceParameter implements EnumerationInferenceParameter {

  //// Constructors
  /**
   * Creates a new instance of DefaultBooleanInferenceParameter.
   */
  public DefaultEnumerationInferenceParameter(Map propertyMap) {
    super(propertyMap);
    for (int i = 0, size = REQUIRED_SYMBOLS.length; i < size; i++) {
      if (propertyMap.get(REQUIRED_SYMBOLS[i]) == null) {
        throw new BaseClientRuntimeException("Expected key not found in map "
                + REQUIRED_SYMBOLS[i]
                + " for inference parameter " + propertyMap.get(AbstractInferenceParameter.ID_SYMBOL));
      }
    }
    Object potentialValuesObj = DefaultInferenceParameterValueDescription.verifyObjectType(propertyMap, POTENTIAL_VALUES_SYMBOL, List.class);
    init((List) potentialValuesObj);
  }

  DefaultEnumerationInferenceParameter(Object defaultValue, CycSymbolImpl keyword,
          Fort id, String shortDescription, String longDescription,
          CycSymbolImpl isBasicParameter, CycSymbolImpl isQueryStaticParameter, CycArrayList alternateValue,
          List<InferenceParameterValueDescription> potentialValues) {
    super(defaultValue, keyword, id, shortDescription, longDescription, isBasicParameter,
            isQueryStaticParameter, alternateValue);
    this.potentialValues.addAll(potentialValues);
    this.potentialValues = Collections.unmodifiableList(this.potentialValues);
  }

  //// Public Area
  @Override
  public Object canonicalizeValue(Object value) {
    if (value instanceof InferenceParameterValue) {
      return ((InferenceParameterValue) value).getCycSymbol();
    } else {
      return super.canonicalizeValue(value);
    }
  }

  public boolean isValidValue(Object potentialValue) {
    if (isAlternateValue(potentialValue)) {
      return true;
    }
    if (potentialValues.contains(potentialValue)) {
      return true;
    }
    return false;
  }

  public List<InferenceParameterValueDescription> getPotentialValues() {
    return Collections.unmodifiableList(potentialValues);
  }

  @Override
  public String getPrettyRepresentation(Object value) {
    Iterator<InferenceParameterValueDescription> iterator
            = getPotentialValues().iterator();
    while (iterator.hasNext()) {
      InferenceParameterValueDescription description = iterator.next();
      if (description.getValue().equals(value)) {
        return description.getShortDescription();
      }
    }
    return super.getPrettyRepresentation(value);
  }

  @Override
  public String toString() {
    String str = super.toString() + " values={";
    Iterator iterator = getPotentialValues().iterator();
    while (iterator.hasNext()) {
      Object value = iterator.next();
      str += value.toString();
      if (iterator.hasNext()) {
        str += ",";
      } else {
        str += "}";
      }
    }
    return str;
  }

  @Override
  public Object parameterValueCycListApiValue(final Object val) {
    return val;
  }

  //// Protected Area
  //// Private Area
  private void init(List potentialValues) {
    if (potentialValues == null) {
      throw new IllegalArgumentException("Got null potentialValues");
    }
    for (Iterator iter = potentialValues.iterator(); iter.hasNext();) {
      Object potentialValueObj = iter.next();
      if (!(potentialValueObj instanceof CycArrayList)) {
        throw new BaseClientRuntimeException("Expected a CycList; got " + potentialValueObj);
      }
      InferenceParameterValueDescription potentialValue
              = new DefaultInferenceParameterValueDescription(DefaultInferenceParameterDescriptions.parsePropertyList((CycArrayList) potentialValueObj));
      this.potentialValues.add(potentialValue);
    }
    this.potentialValues = Collections.unmodifiableList(this.potentialValues);
  }
  //// Internal Rep
  private List<InferenceParameterValueDescription> potentialValues
          = new ArrayList<InferenceParameterValueDescription>() {

            @Override
            public boolean contains(Object obj) {
              for (Iterator iter = iterator(); iter.hasNext();) {
                if (iter.next().equals(obj)) {
                  return true;
                }
              }
              return false;
            }
          };
  private final static CycSymbolImpl POTENTIAL_VALUES_SYMBOL = new CycSymbolImpl(":POTENTIAL-VALUES");
  private final static CycSymbolImpl[] REQUIRED_SYMBOLS = {POTENTIAL_VALUES_SYMBOL};

  //// Main
  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
  }
}
