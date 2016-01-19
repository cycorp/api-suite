package com.cyc.baseclient.exception;

import com.cyc.base.exception.BaseClientException;

/*
 * #%L
 * File: CycParseException.java
 * Project: Base Client
 * %%
 * Copyright (C) 2013 - 2016 Cycorp, Inc.
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
 * <P>ParseException is designed to...
 *
 * @author nwinant, May 24, 2010, 10:37:18 AM
 * @version $Id: CycParseException.java 163356 2016-01-04 20:55:47Z nwinant $
 */
public class CycParseException extends BaseClientException {

  /** Creates a new instance of ParseException. */
  public CycParseException(final String message) {
    super(message);
  }

}
