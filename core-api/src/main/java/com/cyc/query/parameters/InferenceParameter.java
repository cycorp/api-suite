package com.cyc.query.parameters;

/*
 * #%L
 * File: InferenceParameter.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2017 Cycorp, Inc
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

/**
 * @author tbrussea
 * @date August 2, 2005, 10:25 AM
 * @version $Id: InferenceParameter.java 175435 2017-10-20 23:37:33Z nwinant $
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
