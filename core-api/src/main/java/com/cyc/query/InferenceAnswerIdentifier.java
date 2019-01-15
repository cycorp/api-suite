package com.cyc.query;

import com.cyc.session.CycSession;

/*
 * #%L
 * File: InferenceAnswerIdentifier.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2019 Cycorp, Inc
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
//// External Imports
/**
 * <P>InferenceAnswerIdentifier is designed to...
 *
 * @author jmoszko, May 13, 2014, 5:54:59 PM
 * @version $Id: InferenceAnswerIdentifier.java 185299 2019-01-14 16:15:35Z daves $
 */
public interface InferenceAnswerIdentifier {

  int getAnswerId();

  InferenceIdentifier getInferenceIdentifier();
  
  CycSession getSession();

  String stringApiValue();
  
  Object cycListApiValue();
  
}
