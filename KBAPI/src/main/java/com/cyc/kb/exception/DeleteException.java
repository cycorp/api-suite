package com.cyc.kb.exception;

/*
 * #%L
 * File: DeleteException.java
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

import com.cyc.kb.exception.KBApiException;

/**
 * Thrown when code fails to delete something from the Cyc KB.
 * 
 * @author Nathan Winant
 * @version $Id: DeleteException.java 154990 2014-11-14 22:46:51Z nwinant $
 */
public class DeleteException extends KBApiException {
  
  public DeleteException(Throwable cause) {
    super(cause);
  }

  public DeleteException(String msg) {
    super(msg);
  }

  public DeleteException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
