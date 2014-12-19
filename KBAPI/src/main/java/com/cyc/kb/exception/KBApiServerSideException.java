package com.cyc.kb.exception;

import com.cyc.base.CycApiException;
import com.cyc.baseclient.api.CycApiServerSideException;

/*
 * #%L
 * File: KBApiServerSideException.java
 * Project: KB API
 * %%
 * Copyright (C) 2013 - 2014 Cycorp, Inc
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
 * @version $Id: KBApiServerSideException.java 154990 2014-11-14 22:46:51Z nwinant $
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
