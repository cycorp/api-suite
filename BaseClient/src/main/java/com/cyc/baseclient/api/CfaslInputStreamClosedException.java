package com.cyc.baseclient.api;

import com.cyc.base.BaseClientRuntimeException;

/*
 * #%L
 * File: CfaslInputStreamClosedException.java
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

//// External Imports

/**
 * Class CfaslInputStreamClosedException indicates that the peer (usually the Cyc server)
 * closed the socket connection.
 *
 * @version $Id: CfaslInputStreamClosedException.java 155483 2014-12-10 21:56:51Z nwinant $
 * @author Stephen L. Reed
 */
public class CfaslInputStreamClosedException extends BaseClientRuntimeException {
  
  //// Constructors
  
  /** Creates a new instance of CfaslInputStreamClosedException. */
  public CfaslInputStreamClosedException() {
  }
  
  //// Public Area
  
    /**
     * Construct a CfaslInputStreamClosedException object with a specified message.
     * @param s a message describing the exception.
     */
    public CfaslInputStreamClosedException(String s) {
        super(s);
    }
    
    public CfaslInputStreamClosedException(String s, Throwable cause) {
      super(s, cause);
    }
    
    public CfaslInputStreamClosedException(Throwable cause) {
      super(cause);
    }
    
  //// Protected Area
  
  //// Private Area
  
  //// Internal Rep
  
  //// Main
  
}
