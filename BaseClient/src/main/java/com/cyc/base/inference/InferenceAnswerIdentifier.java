package com.cyc.base.inference;

/*
 * #%L
 * File: InferenceAnswerIdentifier.java
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

//// Internal Imports

import com.cyc.base.cycobject.CycList;
//// Internal Imports
//// External Imports
/**
 * <P>InferenceAnswerIdentifierI is designed to...
 *
 * @author jmoszko, May 13, 2014, 5:54:59 PM
 * @version $Id: InferenceAnswerIdentifier.java 155483 2014-12-10 21:56:51Z nwinant $
 */
public interface InferenceAnswerIdentifier {

  CycList<Integer> cycListApiValue();

  int getAnswerID();

  InferenceIdentifier getInferenceID();

  String stringApiValue();
  
}
