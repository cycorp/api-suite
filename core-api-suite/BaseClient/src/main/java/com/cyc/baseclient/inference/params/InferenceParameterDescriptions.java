package com.cyc.baseclient.inference.params;

/*
 * #%L
 * File: InferenceParameterDescriptions.java
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
import com.cyc.query.InferenceParameter;
import com.cyc.base.CycAccess;
import com.cyc.base.cycobject.CycSymbol;
import com.cyc.query.InferenceParameters;
import java.util.Map;

/**
 * <P>InferenceParameterDescriptions maps the Cyc symbols representing
 * inference parameters to their InferenceParameter versions.
 *
 * <P>Copyright (c) 2004 - 2006 Cycorp, Inc. All rights reserved.
 * <BR>This software is the proprietary information of Cycorp, Inc.
 * <P>Use is subject to license terms.
 *
 * @author tbrussea
 * @date August 2, 2005, 10:21 AM
 * @version $Id: InferenceParameterDescriptions.java 158569 2015-05-19 21:51:08Z daves $
 * 
 * @see InferenceParameter
 */
public interface InferenceParameterDescriptions extends Map<String, InferenceParameter> {

  /**
   * Get the string API representation for this map.
   *
   * @return the string API representation.
   */
  String stringApiValue();

  /**
   * Get the Cyc access used by this map.
   *
   * @return the Cyc access.
   */
  CycAccess getCycAccess();

  /**
   * Get the default inference parameters for this map.
   *
   * @return the default parameters
   */
  InferenceParameters getDefaultInferenceParameters();
}
