package  com.cyc.baseclient.xml;

/*
 * #%L
 * File: XMLPrintWriter.java
 * Project: Base Client
 * %%
 * Copyright (C) 2013 - 2014 Cycorp, Inc.
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

import  java.io.PrintStream;
import  java.io.PrintWriter;


/**
 * Implements an XMLPrintWriter with facilities for surrounding data
 * elements with tags appropriately named and indented.<p>
 *
 * @version $Id: XMLPrintWriter.java 155483 2014-12-10 21:56:51Z nwinant $
 * @author Stefano Bertolo
 */
public class XMLPrintWriter extends XMLWriter {
    /**
     * The <code>PrintWriter</code> to which the actual printing is delegated.
     */
    PrintWriter writer;

    /**
     * Constructs an XMLPrintWriter object given a PrintStream.
     *
     * @param printStream the PrintStream on which XML is output
     */
    public XMLPrintWriter (java.io.PrintStream printStream) {
        writer = new PrintWriter(printStream, true);
    }

    /**
     * Prints a string to the <code>PrintWriter</code> stored in the field @see #writer indenting it
     * by the number of spaces indicated by @see #indent either relative to the
     * current indentation level (if @see #relative is <code>true</code>) or with
     * respect to the beginning of the line (if @see #relative is <code>false</code>).
     *
     * @param string the string to be printed.
     * @param indent the number of spaces by which the string needs to be indented.
     * @param relative id <code>true</code> the string is further indented with respect
     * to the current indentation level, if <code>false</code> is indented with respect to
     * the beginning of the line.
     */
    public void indentPrint (String string, int indent, boolean relative) {
        this.setIndent(indent, relative);
        writer.print(indentString + string);
    }


    /**
     * Prints a string to the <code>PrintWriter</code> stored in the field @see #writer.
     *
     * @param string the string to be printed.
     */
    public void print (String string) {
        writer.print(string);
    }

    /**
     * Flushes the <code>PrintWriter</code> in the field @see #writer.
     */
    public void flush () {
        writer.flush();
    }

  /**
   * Closes the <code>PrintWriter</code> in the field @see #writer.
   */
  public void close () {
    writer.close();
  }
}







