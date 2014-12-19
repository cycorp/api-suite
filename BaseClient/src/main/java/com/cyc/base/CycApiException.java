package com.cyc.base;

import com.cyc.session.SessionCommandException;
import com.cyc.session.SessionCommunicationException;

/*
 * #%L
 * File: CycApiException.java
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

/**
 * Class CycApiException indicates an error condition during a Cyc API call.
 * This type of exception is thrown when errors on the Java side are caught,
 * when errors on the Cyc server side are caught a CycApiServerSideException
 * is thrown instead.
 *
 * @version $Id: CycApiException.java 155483 2014-12-10 21:56:51Z nwinant $
 * @author Stephen L. Reed
 */
public class CycApiException extends BaseClientRuntimeException {
  
  /**
   * Construct a CycApiException object with no specified message.
   */
  public CycApiException() {
    super();
  }
  
  /**
   * Construct a CycApiException object with a specified message.
   * @param s a message describing the exception.
   */
  public CycApiException(String s) {
    super(s);
  }
  
  /**
   * Construct a CycApiException object with a specified message
   * and throwable.
   * @param s the message string
   * @param cause the throwable that caused this exception
   */
  public CycApiException(String s, Throwable cause) {
    super(s, cause);
  }
  
  /**
   * Construct a CycApiException object with a specified throwable.
   * @param cause the throwable that caused this exception
   */
  public CycApiException(Throwable cause) {
    super(cause);
  }
  
  @Override
  public void throwAsSessionException(String msg) throws SessionCommandException {
    throw new SessionCommandException(msg, this);
  }
  
  @Override
  public void throwAsSessionException() throws SessionCommandException {
    throw new SessionCommandException(this);
  }
}
