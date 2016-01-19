package com.cyc.baseclient.util;

/*
 * #%L
 * File: ConciseLoggingFormatter.java
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

//// Internal Imports


//// External Imports
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Provides a concise formatter for java logging output.<br>
 * 
 * @version $Id: ConciseLoggingFormatter.java 163356 2016-01-04 20:55:47Z nwinant $
 * @author Stephen L. Reed  
 */
public class ConciseLoggingFormatter extends SimpleFormatter {
  
  //// Constructors
  
  /** Creates a new instance of ConciseLoggingFormatter */
  public ConciseLoggingFormatter() {
  }
  
  //// Public Area
  
  /**
   * Formats the given log record.
   */
  public String format (LogRecord logRecord) {
    StringBuffer stringBuffer = new StringBuffer();
    if (logRecord == null)
      stringBuffer.append("null logRecord received by ConciseLoggingFormatter");
    else if (logRecord.getMessage() == null)
      stringBuffer.append("null logRecord message received by ConciseLoggingFormatter");
    else
      stringBuffer.append(logRecord.getMessage());
    stringBuffer.append('\n');
    return stringBuffer.toString();
  }
  
  //// Protected Area
  
  //// Private Area
  
  //// Internal Rep
    
  //// Main
  
}
