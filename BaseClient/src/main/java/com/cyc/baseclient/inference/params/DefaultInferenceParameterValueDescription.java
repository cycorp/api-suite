package com.cyc.baseclient.inference.params;

/*
 * #%L
 * File: DefaultInferenceParameterValueDescription.java
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

//// Internal Imports
import com.cyc.base.BaseClientRuntimeException;
import com.cyc.baseclient.cycobject.CycSymbolImpl;
import com.cyc.base.cycobject.CycSymbol;
import com.cyc.base.inference.InferenceParameterValueDescription;

//// External Imports
import java.util.*;

/**
 * <P>DefaultInferenceParameterValueDescription is designed to...
 *
 * <P>Copyright (c) 2004 - 2006 Cycorp, Inc.  All rights reserved.
 * <BR>This software is the proprietary information of Cycorp, Inc.
 * <P>Use is subject to license terms.
 *
 * @author zelal
 * @date August 14, 2005, 12:51 PM
 * @version $Id: DefaultInferenceParameterValueDescription.java 155703 2015-01-05 23:15:30Z nwinant $
 */
public class DefaultInferenceParameterValueDescription 
implements InferenceParameterValueDescription {
  
  //// Constructors
  
  /** Creates a new instance of DefaultInferenceParameterValueDescription. */
  public DefaultInferenceParameterValueDescription(Map propertyMap) {
    if (propertyMap == null) {
      throw new BaseClientRuntimeException("Got null property map");
    }
    if (propertyMap.size() < REQUIRED_SYMBOLS.length) {
      throw new BaseClientRuntimeException("Got too few symbols in map");
    }
    for (int i = 0, size = REQUIRED_SYMBOLS.length; i < size; i++) {
      if (propertyMap.get(REQUIRED_SYMBOLS[i]) == null) {
        throw new BaseClientRuntimeException("Expected key not found in map " +
        REQUIRED_SYMBOLS[i] +
        " for inference Parameter value" +
        propertyMap.get(VALUE_SYMBOL));
      }
    }
    Object valueObj = propertyMap.get(VALUE_SYMBOL);
    Object shortDescObj = verifyObjectType(propertyMap, AbstractInferenceParameter.
      SHORT_DESC_SYMBOL, String.class);
    Object longDescObj = verifyObjectType(propertyMap, AbstractInferenceParameter.
      LONG_DESC_SYMBOL, String.class);
    init(valueObj, (String)shortDescObj, (String)longDescObj);
  }

  DefaultInferenceParameterValueDescription(Object value, String shortDescription,
   String longDescription) {
    init(value, shortDescription, longDescription);
  }
  
  public Object getValue() {
    return value;
  }

  public void setValue(final Object value) {
    this.value = value;
  }
  
  public String getLongDescription() {
    return longDescription;
  }
  
  public String getShortDescription() {
    return shortDescription;
  }
  
  /**
   * Checks to see whether another DefaultInferenceParameterValueDescription is
   * equal to this one.
   *
   * 2005-09-22 bklimt - Previously, this function expected an object of a type
   * other than DefaultInferenceParameterValueDescription, specifically, whatever
   * type getValue() returned.  So, it was changed to fit the Java semantics of
   * boolean equals(Object).
   *
   * @param obj An instance of type DefaultInferenceParameterValueDescription
   */
  public boolean equals(Object obj) {
    if (obj instanceof DefaultInferenceParameterValueDescription) {
      return ((DefaultInferenceParameterValueDescription)obj).getValue().equals(getValue());
    } else {
      return getValue().equals(obj);
    }
  }
  
  public int hashCode() {
    return getValue().hashCode();
  }
  
  public String toString() {
    return getShortDescription();
    //return getValue().toString();
  }
  
  //// Protected Area
 
  static Object verifyObjectType(Map<CycSymbol, Object> propertyMap, CycSymbol property, Class expectedType) {  
    Object propertyValueObj = propertyMap.get(property);
    if (!expectedType.isInstance(propertyValueObj)) {
      throw new BaseClientRuntimeException("Expected a " + expectedType + " for " + property + "; got " + propertyValueObj);
    }
    return propertyValueObj;
  }
  
  //// Private Area
  private void init(Object value, String shortDescription, 
   String longDescription) {
    this.value = value;
    this.longDescription = longDescription;
    this.shortDescription = shortDescription;
  }

    
  //// Internal Rep
  
  private Object value;
  private String shortDescription;
  private String longDescription;
  
  private final static CycSymbol VALUE_SYMBOL = new CycSymbolImpl(":VALUE");
  private final static CycSymbol[] REQUIRED_SYMBOLS = 
   { VALUE_SYMBOL, 
     AbstractInferenceParameter.SHORT_DESC_SYMBOL, 
     AbstractInferenceParameter.LONG_DESC_SYMBOL };
  
  //// Main
  
  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
  }
}
