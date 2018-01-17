package com.cyc.kb.exception;

/*
 * #%L
 * File: KbTypeConflictException.java
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
 * Thrown when an entity exists in the KB, but is incompatible with a particular semantic object
 * type. The difference between this exception and {@link com.cyc.kb.exception.KbTypeException } is
 * that in this case, the KB entity cannot be turned into the expected type by addition of
 * assertions--instead, there is an existing assertion on the KB entity that specifically conflicts
 * with the desired type. In contrast, a <code>KbTypeException</code> signals that the KB entity is
 * not of the desired type, but does not signal that there is a conflict between some existing
 * assertion and the desired type. For a simple <code>KbTypeException</code>, it is possible that
 * simply adding more assertions will turn the KB entity into the desired type of entity.
 *
 * @author Nathan Winant
 * @version $Id: KbTypeConflictException.java 175435 2017-10-20 23:37:33Z nwinant $
 */
public class KbTypeConflictException extends KbTypeException {

  public KbTypeConflictException(Throwable cause) {
    super(cause);
  }

  public KbTypeConflictException(String msg) {
    super(msg);
  }

  public KbTypeConflictException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
