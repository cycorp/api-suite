package com.cyc.session.exception;

/*
 * #%L
 * File: SessionServiceException.java
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
 * SessionServiceException occur when the Session API has trouble loading
 * a service provider.
 * 
 * @author nwinant
 */
public class SessionServiceException extends SessionException {
  
  /**
   * Construct a CycConfigurationException object with no specified message.
   * @param interfaceClass
   */
  public SessionServiceException(Class interfaceClass) {
    super(makeMsg(interfaceClass));
    this.interfaceClass = interfaceClass;
  }
  
  /**
   * Construct a CycConfigurationException object with a specified message.
   * @param interfaceClass
   * @param msg a message describing the exception.
   */
  public SessionServiceException(Class interfaceClass, String msg) {
    super(makeMsg(interfaceClass, msg));
    this.interfaceClass = interfaceClass;
  }
  
  /**
   * Construct a CycConfigurationException object with a specified message
   * and throwable.
   * @param interfaceClass
   * @param msg the message string
   * @param cause the throwable that caused this exception
   */
  public SessionServiceException(Class interfaceClass, String msg, Throwable cause) {
    super(makeMsg(interfaceClass), cause);
    this.interfaceClass = interfaceClass;
  }
  
  /**
   * Construct a CycConfigurationException object with a specified throwable.
   * @param interfaceClass
   * @param cause the throwable that caused this exception
   */
  public SessionServiceException(Class interfaceClass, Throwable cause) {
    super(makeMsg(interfaceClass), cause);
    this.interfaceClass = interfaceClass;
  }
  
  /**
   * The interface for which the Session API expected an implementation to be provided by the 
   * service.
   * @return 
   */
  public Class getInterfaceClass() {
    return this.interfaceClass;
  }
  
  
  // Private
  
  private static String makeMsg(Class interfaceClass) {
    return "Exception loading implementation of " + interfaceClass.getName();
  }
  
  private static String makeMsg(Class interfaceClass, String msg) {
    return makeMsg(interfaceClass) + ": " + msg;
  }
  
  
  // Internal
  
  private final Class interfaceClass;
}
