package com.cyc.baseclient.parser;

/*
 * #%L
 * File: UnsupportedVocabularyException.java
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
import com.cyc.base.BaseClientException;
import com.cyc.baseclient.cycobject.CycConstantImpl;

//// External Imports

/**
 * <P>Provides 
 *
 * @version $Id: UnsupportedVocabularyException.java 155483 2014-12-10 21:56:51Z nwinant $
 * @author Tony Brusseau
 */
public class UnsupportedVocabularyException extends BaseClientException {
  
  //// Constructors
  
  /** Creates a new instance of InvalidConstantName. */
  public UnsupportedVocabularyException(CycConstantImpl invalidConstant) {
    this.invalidConstant = invalidConstant;
  }
  
  //// Public Area
  
  public String getMessage() {
    return "The following vocabulary is not supported: '" + invalidConstant.cyclify() + "'.";
  }
  
  public CycConstantImpl getInvalidVocabulary() { return invalidConstant; }
  
  //// Protected Area
  
  //// Private Area
  
  //// Internal Rep
  
  private CycConstantImpl invalidConstant;
  
  //// Main
  
}
