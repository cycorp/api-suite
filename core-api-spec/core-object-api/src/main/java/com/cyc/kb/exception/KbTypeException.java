package com.cyc.kb.exception;

/*
 * #%L
 * File: KbTypeException.java
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
 * Thrown when an entity exists in the KB, but does not currently meet the
 * requirements of a particular semantic object type.
 * 
 * @author Nathan Winant
 * @version $Id: KbTypeException.java 169908 2017-01-11 23:19:09Z nwinant $
 */
public class KbTypeException extends KbException {
  
  public KbTypeException(Throwable cause) {
    super(cause);
  }

  public KbTypeException(String msg) {
    super(msg);
  }

  public KbTypeException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
