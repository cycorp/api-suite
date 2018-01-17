package com.cyc.session.exception;

/*
 * #%L
 * File: SessionConfigurationException.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2017 Cycorp, Inc
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
 * CycSessionException indicates that a Cyc session has been mis-configured or not been configured
 * at all.
 *
 * @author nwinant
 */
public class SessionConfigurationException extends SessionException {
  /**
   * Construct a CycConfigurationException object with no specified message.
   */
  public SessionConfigurationException() {
    super();
  }
  
  /**
   * Construct a CycConfigurationException object with a specified message.
   * @param msg a message describing the exception.
   */
  public SessionConfigurationException(String msg) {
    super(msg);
  }
  
  /**
   * Construct a CycConfigurationException object with a specified message
   * and throwable.
   * @param msg the message string
   * @param cause the throwable that caused this exception
   */
  public SessionConfigurationException(String msg, Throwable cause) {
    super(msg, cause);
  }
  
  /**
   * Construct a CycConfigurationException object with a specified throwable.
   * @param cause the throwable that caused this exception
   */
  public SessionConfigurationException(Throwable cause) {
    super(cause);
  }
}
