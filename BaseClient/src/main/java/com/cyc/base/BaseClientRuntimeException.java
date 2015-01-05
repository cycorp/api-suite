package com.cyc.base;

import com.cyc.session.SessionApiException;

/*
 * #%L
 * File: BaseClientRuntimeException.java
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

/**
 * Class BaseClientRuntimeException indicates an error condition...
 *
 * @version $Id: BaseClientRuntimeException.java 155483 2014-12-10 21:56:51Z nwinant $
 * @author Stephen L. Reed
 */
public class BaseClientRuntimeException extends RuntimeException {
  
  /**
   * Construct a CycApiException object with no specified message.
   */
  public BaseClientRuntimeException() {
    super();
  }
  
  /**
   * Construct a CycApiException object with a specified message.
   * @param s a message describing the exception.
   */
  public BaseClientRuntimeException(String s) {
    super(s);
  }
  
  /**
   * Construct a CycApiException object with a specified message
   * and throwable.
   * @param s the message string
   * @param cause the throwable that caused this exception
   */
  public BaseClientRuntimeException(String s, Throwable cause) {
    super(s, cause);
  }
  
  /**
   * Construct a CycApiException object with a specified throwable.
   * @param cause the throwable that caused this exception
   */
  public BaseClientRuntimeException(Throwable cause) {
    super(cause);
  }
  
  public void throwAsSessionException(String msg) throws SessionApiException {
    throw new SessionApiException(msg, this);
  }
    
  public void throwAsSessionException() throws SessionApiException {
    throw new SessionApiException(this);
  }
}
