package com.cyc.baseclient.inference.params;

/*
 * #%L
 * File: DefaultBooleanInferenceParameter.java
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
import com.cyc.base.cycobject.CycList;
import com.cyc.base.cycobject.CycSymbol;
import com.cyc.base.cycobject.Fort;
import java.util.Map;
import com.cyc.baseclient.CycObjectFactory;

/**
 * <P>DefaultBooleanInferenceParameter is designed to...
 *
 * <P>Copyright (c) 2004 - 2006 Cycorp, Inc.  All rights reserved.
 * <BR>This software is the proprietary information of Cycorp, Inc.
 * <P>Use is subject to license terms.
 *
 * @author zelal
 * @date August 9, 2005, 9:09 PM
 * @version $Id: DefaultBooleanInferenceParameter.java 155703 2015-01-05 23:15:30Z nwinant $
 */
public class DefaultBooleanInferenceParameter extends AbstractInferenceParameter implements BooleanInferenceParameter {

  //// Constructors
  /** Creates a new instance of DefaultBooleanInferenceParameter.
   * @param propertyMap a map of parameters to their values.
   */
  public DefaultBooleanInferenceParameter(Map<CycSymbol, Object> propertyMap) {
    super(propertyMap);
  }

  protected DefaultBooleanInferenceParameter(Boolean defaultValue, CycSymbol keyword,
          Fort id, String shortDescription, String longDescription,
          CycSymbol isBasicParameter, CycSymbol isQueryStaticParameter, CycList alternateValue) {
    super(defaultValue, keyword, id, shortDescription, longDescription, isBasicParameter,
            isQueryStaticParameter, alternateValue);
  }

  //// Public Area
  @Override
  public boolean isValidValue(Object potentialValue) {
    if (isAlternateValue(potentialValue)) {
      return true;
    } else if (potentialValue instanceof Boolean) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public Object canonicalizeValue(final Object value) {
    if (isValidValue(value)) {
      return value;
    } else if (value == null) {
      throw new BaseClientRuntimeException("Got invalid boolean value " + value);
    } else if (value.toString().equals(CycObjectFactory.nil.toString())) {
      return false;
    } else if (value.toString().equals(CycObjectFactory.t.toString())) {
      return true;
    } else {
      throw new BaseClientRuntimeException("Got invalid boolean value " + value);
    }
  }

  @Override
  public String getPrettyRepresentation(Object value) {
    if (value instanceof Boolean) {
      return (Boolean) value ? "Yes" : "No";
    } else {
      return super.getPrettyRepresentation(value);
    }
  }


  public Object parameterValueCycListApiValue(Object val) {
    if (((Boolean) val).booleanValue()) {
      return (CycObjectFactory.t);
    } else {
      return (CycObjectFactory.nil);
    }
  }

//// Protected Area
//// Private Area
//// Internal Rep
//// Main
  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
  }
}
