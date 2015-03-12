package com.cyc.kb.exception;

/*
 * #%L
 * File: InvalidNameException.java
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
 * Thrown when code attempts to create a KB entity with an invalid name.
 * 
 * @author Nathan Winant
 * @version $Id: InvalidNameException.java 157024 2015-03-11 16:57:41Z nwinant $ 
 */
// @todo specify what the requirements are for a Constant name.

public class InvalidNameException extends CreateException {
  
  public InvalidNameException(Throwable cause) {
    super(cause);
  }

  public InvalidNameException(String msg) {
    super(msg);
  }

  public InvalidNameException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
