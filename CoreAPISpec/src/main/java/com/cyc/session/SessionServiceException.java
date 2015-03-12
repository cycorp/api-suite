package com.cyc.session;

/*
 * #%L
 * File: SessionServiceException.java
 * Project: Core API Specification
 * %%
 * Copyright (C) 2013 - 2015 Cycorp, Inc
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
 * SessionImplementationExceptions occur when the Session API has trouble loading
 * an implementation class from a service.
 * 
 * that a Cyc session has been mis-configured
 * or not been configured at all.
 * @author nwinant
 */
public class SessionServiceException extends SessionApiException {
  
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
   * @param s a message describing the exception.
   */
  public SessionServiceException(Class interfaceClass, String s) {
    super(makeMsg(interfaceClass, s));
    this.interfaceClass = interfaceClass;
  }
  
  /**
   * Construct a CycConfigurationException object with a specified message
   * and throwable.
   * @param interfaceClass
   * @param s the message string
   * @param cause the throwable that caused this exception
   */
  public SessionServiceException(Class interfaceClass, String s, Throwable cause) {
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
   * The interface for which the Session API expected an implementation to be provided by the service.
   * @return 
   */
  public Class getInterfaceClass() {
    return this.interfaceClass;
  }
  
  
  // Private
  
  static private String makeMsg(Class interfaceClass) {
    return "Exception loading implementation of " + interfaceClass.getName();
  }
  
  static private String makeMsg(Class interfaceClass, String msg) {
    return makeMsg(interfaceClass) + ": " + msg;
  }
  
  
  // Internal
  
  final private Class interfaceClass;
}
