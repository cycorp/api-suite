package com.cyc.kb.exception;

/*
 * #%L
 * File: CreateException.java
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
 * Thrown when code fails to find or create something from the Cyc KB. {@link KbTypeException} and
 * {@link KbTypeConflictException} are not subclasses because they do not indicate if the underlying
 * object is not in the KB.
 *
 * @author Vijay Raj
 * @version $Id: CreateException.java 175435 2017-10-20 23:37:33Z nwinant $
 */
public class CreateException extends KbException {

  public CreateException(Throwable cause) {
    super(cause);
  }

  public CreateException(String msg) {
    super(msg);
  }

  public CreateException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
