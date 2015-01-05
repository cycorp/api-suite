package com.cyc.baseclient.inference.params;

/*
 * #%L
 * File: InferenceParameter.java
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

import com.cyc.base.cycobject.Fort;
import com.cyc.base.cycobject.CycSymbol;
import com.cyc.base.inference.InferenceParameterValueDescription;




//// External Imports
/**
 * <P>InferenceParameter is designed to...
 *
 * <P>Copyright (c) 2004 - 2006 Cycorp, Inc.  All rights reserved.
 * <BR>This software is the proprietary information of Cycorp, Inc.
 * <P>Use is subject to license terms.
 *
 * @author tbrussea
 * @date August 2, 2005, 10:25 AM
 * @version $Id: InferenceParameter.java 155483 2014-12-10 21:56:51Z nwinant $
 */
public interface InferenceParameter {

  /* @return the CycList API value for val qua value for this parameter. */
  Object parameterValueCycListApiValue(Object val);

  /**
   * Canonicalize a value for this parameter.
   * @param value
   * @return the canonical form of the value.
   */
  Object canonicalizeValue(Object value);

  /**
   * Get the Cyc term that represents this parameter.
   * @return the Cyc term.
   */
  Fort getId();

  /**
   * Get the Cyc symbol that represents this parameter.
   * @return the Cyc symbol.
   */
  CycSymbol getKeyword();

  /**
   * Get a short description of this parameter.
   * @return the description
   */
  String getShortDescription();

  /**
   * Get a long description of this parameter.
   * @return the description.
   */
  String getLongDescription();

  /**
   * Get a human-friendly string representation of the specified value.
   * @param value
   * @return a pretty representation of the value.
   */
  String getPrettyRepresentation(Object value);

  InferenceParameterValueDescription getAlternateValue();

  boolean isAlternateValue(Object value);

  /**
   * Determine whether the specified value is valid for this parameter.
   * @param potentialValue
   * @return true if it is a valid value.
   */
  boolean isValidValue(Object potentialValue);

  /**
   * 
   * @return true if this is a basic parameter.
   */
  boolean isBasicParameter();

  /**
   * 
   * @return true if this is a query static parameter.
   */
  boolean isQueryStaticParameter();

  /**
   * Get the default value for this parameter.
   * @return the default value.
   */
  Object getDefaultValue();
}
