package com.cyc.baseclient.exception;

/*
 * #%L
 * File: CycApiInvalidObjectException.java
 * Project: Base Client
 * %%
 * Copyright (C) 2013 - 2016 Cycorp, Inc.
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
 * Class CycApiInvalidObjectException indicates that an invalid object of
 * some sort was detected during a Cyc API call that occurred on the Cyc
 * server. If an error is detected on the Java client, then a CycApiException
 * is thrown instead.
 *
 * @version $Id: CycApiInvalidObjectException.java 163356 2016-01-04 20:55:47Z nwinant $
 * @author daves
 */

public class CycApiInvalidObjectException extends CycApiServerSideException {

  /**
   * Construct a CycApiInvalidObjectException object with no
   * specified message.
   */
  public CycApiInvalidObjectException() {
    super();
  }

  /**
  * Construct a CycApiInvalidObjectException object with a
  * specified message.
  * @param s a message describing the exception.
  */
  public CycApiInvalidObjectException(String s) {
    super(s);
  }

  /**
   * Construct a CycApiInvalidObjectException object with a
   * specified message and throwable.
   * @param s the message string
   * @param cause the throwable that caused this exception
   */
  public CycApiInvalidObjectException(String s, Throwable cause) {
    super(s, cause);
  }

  /**
   * Construct a CycApiInvalidObjectException object with the
   * specified throwable.
   * @param cause the throwable that caused this exception
   */
  public CycApiInvalidObjectException(Throwable cause) {
    super(cause);
  }
}
