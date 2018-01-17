package com.cyc.kb.exception;

/*
 * #%L
 * File: VariableArityException.java
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
import com.cyc.kb.Relation;

/**
 * Thrown when the arity of a Relation without a fixed arity is requested. For
 * variable-arity relations, {@link Relation#getArityMin()} and
 * {@link Relation#getArityMax()} methods should be used.
 *
 * @author Dave Schneider
 * @version $Id: VariableArityException.java 151668 2014-06-03 21:46:52Z jmoszko
 * $
 */
public class VariableArityException extends KbRuntimeException {

  public VariableArityException(String message) {
    super(message);
  }

  public VariableArityException(String message, Throwable cause) {
    super(message, cause);
  }

}
