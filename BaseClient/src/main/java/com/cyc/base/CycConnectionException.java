package com.cyc.base;

import com.cyc.session.SessionCommunicationException;

/*
 * #%L
 * File: CycConnectionException.java
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
 *
 * @author nwinant
 */
public class CycConnectionException extends BaseClientException {
  /**
   * Construct a CycApiException object with no specified message.
   */
  public CycConnectionException() {
    super();
  }
  
  /**
   * Construct a CycApiException object with a specified message.
   * @param s a message describing the exception.
   */
  public CycConnectionException(String s) {
    super(s);
  }
  
  /**
   * Construct a CycApiException object with a specified message
   * and throwable.
   * @param s theMake sure Base Client declares all exceptions it throws message string
   * @param cause the throwable that caused this exception
   */
  public CycConnectionException(String s, Throwable cause) {
    super(s, cause);
  }
  
  /**
   * Construct a CycApiException object with a specified throwable.
   * @param cause the throwable that caused this exception
   */
  public CycConnectionException(Throwable cause) {
    super(cause);
  }
  
  @Override
  public void throwAsSessionException(String msg) throws SessionCommunicationException {
    throw new SessionCommunicationException(msg, this);
  }
  
  @Override
  public void throwAsSessionException() throws SessionCommunicationException {
    throw new SessionCommunicationException(this);
  }
  
  
  // Static methods
  
  /**
   * Check if a Throwable's underlying cause is a CycConnectionException.
   * This is useful because many libraries wrap Base Client exceptions in higher-
   * level exceptions, yet connection exceptions are uniquely disruptive. For
   * example, the KB API may throw an exception that simply states 
   * 'No KB object "name" of type "ContextImpl" found.' This method makes it easier
   * for libraries (and the code which calls them) to determine when a connection
   * problem is the real issue.
   * 
   * @param thrown
   * @return true if a Throwable's underlying cause is a CycConnectionException
   */
  public static boolean isUnderlyingCause(final Throwable thrown) {
    Throwable cause = thrown;
    while (cause != null) {
      if (cause instanceof CycConnectionException) {
        return true;
      }
      cause = cause.getCause();
    }
    return false;
  }
  
  
  // Static instances
  
  public static final CycConnectionException CURRENT_ACCESS_NOT_SET = 
          new CycConnectionException("Expected a current (threadlocal) CycAccess, but none was set.");
}
