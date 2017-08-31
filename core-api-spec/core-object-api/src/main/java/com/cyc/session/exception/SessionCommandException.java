package com.cyc.session.exception;

/*
 * #%L
 * File: SessionCommandException.java
 * Project: Core API Object Specification
 * %%
 * Copyright (C) 2013 - 2017 Cycorp, Inc
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
 * SessionCommandException indicates that Session implementation code could
 * communicate with a Cyc server, but encountered a problem issuing a command
 * or interpreting a result.
 * 
 * @author nwinant
 */
public class SessionCommandException extends SessionException {
  /**
   * Construct a SessionCommandException object with no specified message.
   */
  public SessionCommandException() {
    super();
  }
  
  /**
   * Construct a SessionCommandException object with a specified message.
   * @param msg a message describing the exception.
   */
  public SessionCommandException(String msg) {
    super(msg);
  }
  
  /**
   * Construct a SessionCommandException object with a specified message
   * and throwable.
   * @param msg the message string
   * @param cause the throwable that caused this exception
   */
  public SessionCommandException(String msg, Throwable cause) {
    super(msg, cause);
  }
  
  /**
   * Construct a SessionCommandException object with a specified throwable.
   * @param cause the throwable that caused this exception
   */
  public SessionCommandException(Throwable cause) {
    super(cause);
  }
}
