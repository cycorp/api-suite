package com.cyc.baseclient.api;

/*
 * #%L
 * File: CycApiServerSideException.java
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

import com.cyc.base.CycApiException;

/**
 * Class CycApiServerException indicates an error condition during 
 * a Cyc API call that occurred on the Cyc server. If an error is 
 * detected on the Java client, then a CycApiException is thrown 
 * instead.
 *
 * @version $Id: CycApiServerSideException.java 155483 2014-12-10 21:56:51Z nwinant $
 * @author tbrussea
 */

public class CycApiServerSideException extends CycApiException {
  
  /**
   * Construct a CycApiServerSideException object with no 
   * specified message.
   */
  public CycApiServerSideException() {
    super();
  }
  
  /**
  * Construct a CycApiServerSideException object with a 
  * specified message.
  * @param s a message describing the exception.
  */
  public CycApiServerSideException(String s) {
    super(s);
  }
  
  /**
   * Construct a CycApiServerSideException object with a 
   * specified message and throwable.
   * @param s the message string
   * @param cause the throwable that caused this exception
   */
  public CycApiServerSideException(String s, Throwable cause) {
    super(s, cause);
  }
  
  /**
   * Construct a CycApiServerSideException object with the 
   * specified throwable.
   * @param cause the throwable that caused this exception
   */
  public CycApiServerSideException(Throwable cause) {
    super(cause);
  }
}
