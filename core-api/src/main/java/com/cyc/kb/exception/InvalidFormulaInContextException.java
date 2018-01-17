package com.cyc.kb.exception;

/*
 * #%L
 * File: InvalidFormulaInContextException.java
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
 * Thrown when the Cyc does not allow the specified formula to be true in the specified context. 
 * It does not distinguish between semantic and syntactic errors.
 *
 * @author Vijay Raj
 * @version $Id: InvalidFormulaInContextException.java 175435 2017-10-20 23:37:33Z nwinant $
 */
public class InvalidFormulaInContextException extends CreateException {

  public InvalidFormulaInContextException(Throwable cause) {
    super(cause);
  }

  public InvalidFormulaInContextException(String msg) {
    super(msg);
  }

  public InvalidFormulaInContextException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
