package  com.cyc.base;

/*
 * #%L
 * File: CycTimeOutException.java
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
 * Implements an exception class for notification when a Cyc server 
 * communication has timed out. When this is thrown, the outstanding
 * task on the Cyc server is arborted.
 *
 * @version $Id: CycTimeOutException.java 155483 2014-12-10 21:56:51Z nwinant $
 * @author Eric E. Allen<br>
 */
public class CycTimeOutException extends BaseClientRuntimeException {
  
  /**
   * Construct a TimeOutException object with no 
   * specified message.
   */
  public CycTimeOutException () {
    super();
  }
  
  /**
  * Construct a TimeOutException object with a 
  * specified message.
  * @param message a message describing the exception.
  */
  public CycTimeOutException(String message) {
    super(message);
  }
  
  /**
   * Construct a TimeOutException object with a 
   * specified message and throwable.
   * @param message the message string
   * @param t the throwable that caused this exception
   */
  public CycTimeOutException(String message, Throwable t) {
    super(message, t);
  }
    
  /**
   * Construct a TimeOutException object with the 
   * specified throwable.
   * @param t the throwable that caused this exception
   */
  public CycTimeOutException(Throwable t) {
    super(t);
  }
  
}
