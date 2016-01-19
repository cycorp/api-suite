package com.cyc.baseclient.parser;

/*
 * #%L
 * File: InvalidConstantNameException.java
 * Project: Base Client
 * %%
 * Copyright (C) 2013 - 2016 Cycorp, Inc.
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
import com.cyc.base.exception.BaseClientException;
import java.util.*;
import static com.cyc.base.cycobject.CycConstant.HD;

/**
 * <P>Thrown whenever the CycL parser tries to parse a constant
 * by its name, and the Cyc server does not already have a constant
 * by that name defined.
 *
 * @version $Id: InvalidConstantNameException.java 163356 2016-01-04 20:55:47Z nwinant $
 * @author Tony Brusseau
 */
public class InvalidConstantNameException extends BaseClientException {
  
  //// Constructors
  
  /** Creates a new instance of InvalidConstantName. */
  public InvalidConstantNameException(String invalidConstantName) {
    this.invalidConstantNames = new ArrayList();
    this.invalidConstantNames.add(invalidConstantName);
  }
  
  public InvalidConstantNameException() {
  }
  
  //// Public Area
  
  public String getMessage() {
    StringBuffer buf = new StringBuffer("Invalid constant name(s): ");
    if (getInvalidConstantNames() != null) {
      for (Iterator iter = invalidConstantNames.iterator(); iter.hasNext(); ) {
        buf.append(HD).append(iter.next());
        if (iter.hasNext()) {
          buf.append(", ");
        }
      }
    }
    return buf.toString();
  }
  
  public void addInvalidConstantName(String constantName) {
    if (invalidConstantNames == null) {
      invalidConstantNames = new ArrayList();
    }
    invalidConstantNames.add(constantName);
  }
  
  public List getInvalidConstantNames() { 
    return invalidConstantNames;
  }
  
  //// Protected Area
  
  //// Private Area
  
  //// Internal Rep
  
  private List invalidConstantNames;
  
  //// Main
  
}
