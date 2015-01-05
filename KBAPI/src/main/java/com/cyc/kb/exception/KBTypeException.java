package com.cyc.kb.exception;

/*
 * #%L
 * File: KBTypeException.java
 * Project: KB API
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
 * Thrown when an entity exists in the KB, but does not currently meet the
 * requirements of a particular semantic object type.
 * 
 * @author Nathan Winant
 * @version $Id: KBTypeException.java 154990 2014-11-14 22:46:51Z nwinant $
 */
public class KBTypeException extends KBApiException {
  
  public KBTypeException(Throwable cause) {
    super(cause);
  }

  public KBTypeException(String msg) {
    super(msg);
  }

  public KBTypeException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
