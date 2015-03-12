package com.cyc.kb.exception;

/*
 * #%L
 * File: StaleKBObjectException.java
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
import com.cyc.kb.KBObject;

/**
 * Thrown when a stale {@link KBObject} is passed to a method.
 *
 * The KB API factory methods that find or create terms and assertions cache the
 * KB Object they return. Multiple get/findOrCreate requests to the same object
 * will be return the identical underlying object from the cache. The cache can
 * be cleared using {@link KBObjectFactory#clearKBObjectCache()}.
 *
 * When an object is deleted using {@link KBObject#delete()}, the object is
 * marked "invalid". The validity of the object can be checked using
 * {@link KBObject#isValid()}.
 *
 *
 * @author Vijay Raj
 * @version $Id: StaleKBObjectException.java 151668 2014-06-03 21:46:52Z jmoszko
 * $
 */
public class StaleKBObjectException extends KBApiRuntimeException {

  public StaleKBObjectException(String msg) {
    super(msg);
  }

  public StaleKBObjectException(Throwable cause) {
    super(cause);
  }

  public StaleKBObjectException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
