package com.cyc.kb.exception;

/*
 * #%L
 * File: KbObjectNotFoundException.java
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
 * Thrown when a term or a formula cannot be found from the specified
 * context.    
 * 
 * @author Vijay Raj
 * @version $Id: KbObjectNotFoundException.java 163206 2015-12-16 00:07:09Z nwinant $
 */
/*
*/
public class KbObjectNotFoundException extends CreateException {

  public KbObjectNotFoundException(Throwable cause) {
    super(cause);
  }

  public KbObjectNotFoundException(String msg) {
    super(msg);
  }

  public KbObjectNotFoundException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
