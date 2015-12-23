package com.cyc.kb.exception;

import java.io.IOException;
import java.net.UnknownHostException;

/*
 * #%L
 * File: KbRuntimeException.java
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
 * Root class for runtime exceptions thrown by the KB API.
 *
 * For now, all {@link UnknownHostException}s and {@link IOException}s from
 * OpenCyc API will be wrapped with KbRuntimeException. This is because the
 * KB API user is not expected to handle such exceptions.
 *
 * @author Vijay Raj
 * @version $Id: KbRuntimeException.java 151668 2014-06-03 21:46:52Z jmoszko
 * $
 */
public class KbRuntimeException extends RuntimeException {

  public KbRuntimeException(Throwable cause) {
    super(cause);
  }

  public KbRuntimeException(String msg) {
    super(msg);
  }

  public KbRuntimeException(String msg, Throwable cause) {
    super(msg, cause);
  }
  
}
