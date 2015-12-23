package com.cyc.baseclient.parser;

/*
 * #%L
 * File: InvalidConstantGuidException.java
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
import com.cyc.base.exception.BaseClientException;
import com.cyc.baseclient.cycobject.GuidImpl;

//// External Imports
import java.util.*;

/**
 * <P>Thrown whenever the CycL parser tries to parse a constant
 by its GuidImpl, and the Cyc server does not already have a constant
 by that GuidImpl defined.
 *
 * @version $Id: InvalidConstantGuidException.java 162904 2015-12-02 18:35:34Z nwinant $
 * @author Tony Brusseau
 */
public class InvalidConstantGuidException extends BaseClientException {
  
  //// Constructors
  
  /** Creates a new instance of InvalidConstantName. */
  public InvalidConstantGuidException(GuidImpl invalidConstantGuid) {
    this.invalidConstantGuids = new ArrayList();
    this.invalidConstantGuids.add(invalidConstantGuid);
  }
  
  public InvalidConstantGuidException() {
  }
  
  //// Public Area
  
  public String getMessage() {
    StringBuffer buf = new StringBuffer("Invalid constant GUID(s): ");
    if (getInvalidConstantNames() != null) {
      for (Iterator iter = getInvalidConstantNames().iterator(); iter.hasNext(); ) {
        buf.append("#G").append(iter.next());
        if (iter.hasNext()) {
          buf.append(", ");
        }
      }
    }
    return buf.toString();
  }
  
  public void addInvalidConstantGuid(GuidImpl invalidConstantGuid) {
    if (invalidConstantGuids == null) {
      invalidConstantGuids = new ArrayList();
    }
    invalidConstantGuids.add(invalidConstantGuid);
  }
  
  public List getInvalidConstantNames() { 
    return invalidConstantGuids;
  }
  
  //// Protected Area
  
  //// Private Area
  
  //// Internal Rep
  
  private List invalidConstantGuids;
  
  //// Main
  
}
