package com.cyc.kb.exception;

/*
 * #%L
 * File: KBApiException.java
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
 * Root class for exceptions thrown by the KB API.
 *
 * @author David Baxter
 * @version $Id: KBApiException.java 157024 2015-03-11 16:57:41Z nwinant $
 */
public class KBApiException extends Exception {

  public KBApiException(Throwable cause) {
    super(cause);
  }

  public KBApiException(String msg) {
    super(msg);
  }

  public KBApiException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
