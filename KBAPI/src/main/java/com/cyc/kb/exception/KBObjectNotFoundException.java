package com.cyc.kb.exception;

/*
 * #%L
 * File: KBObjectNotFoundException.java
 * Project: KB API
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
 * @version $Id: KBObjectNotFoundException.java 155123 2014-11-20 16:10:13Z baxter $
 */
/*
*/
public class KBObjectNotFoundException extends CreateException {

  public KBObjectNotFoundException(Throwable cause) {
    super(cause);
  }

  public KBObjectNotFoundException(String msg) {
    super(msg);
  }

  public KBObjectNotFoundException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
