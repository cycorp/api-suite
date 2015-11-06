package com.cyc.kb.exception;

/*
 * #%L
 * File: KBApiServerSideException.java
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
 * Wrap all Cyc server and Base API errors, when a more specific semantic error
 * can not be thrown.
 * 
 * For now, all {@link CycApiException}s and {@link CycApiServerSideException}s
 * will be wrapped with KBApiServerSideException. This is because the
 * KB API user is not expected to handle such exceptions. 
 *
 * @author Vijay Raj
 * @version $Id: KBApiServerSideException.java 157024 2015-03-11 16:57:41Z nwinant $
 */
public class KBApiServerSideException extends KBApiRuntimeException{
  
  public KBApiServerSideException(Throwable cause) {
    super(cause);
  }

  public KBApiServerSideException(String msg) {
    super(msg);
  }

  public KBApiServerSideException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
