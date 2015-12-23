package com.cyc.kb.exception;

/*
 * #%L
 * File: KbTypeConflictException.java
 * Project: Core API Object Specification
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
 * Thrown when an entity exists in the KB, but is incompatible with a particular
 * semantic object type. The difference between this exception and
 * <code>KbTypeException</code> is that in this case, the KB entity cannot be
 * turned into the expected type by addition of assertions--instead, there is an
 * existing assertion on the KB entity that specifically conflicts with the
 * desired type. In contrast, a <code>KbTypeException</code> signals that the KB
 * entity is not of the desired type, but does not signal that there is a
 * conflict between some existing assertion and the desired type. For a simple
 * <code>KbTypeException</code>, it is possible that simply adding more
 * assertions will turn the KB entity into the desired type of entity.
 *
 * @author Nathan Winant
 * @version $Id: KbTypeConflictException.java 151668 2014-06-03 21:46:52Z
 * jmoszko $
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
