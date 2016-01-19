package  com.cyc.baseclient.xml;

/*
 * #%L
 * File: XmlFileWriter.java
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

import  java.io.FileWriter;


/**
 * Implements an XMLFileWriter with facilities for surrounding data
 * elements with tags appropriately named and indented.<p>
 *
 * @version $Id: XmlFileWriter.java 163356 2016-01-04 20:55:47Z nwinant $
 * @author Stefano Bertolo
 */
public class XmlFileWriter extends XmlWriter {
    /**
     * The <code>FileWriter</code> to which the actual printing is delegated.
     */
    FileWriter writer;

    /**
     * Constructs a XMLFileWriter object given an output file name
     *
     * @param outputFile the output file name
     */
    public XmlFileWriter (String outputFile) throws java.io.IOException
    {
        writer = new FileWriter(outputFile);
    }

    /**
     * Prints a string to the <code>FileWriter</code> stored in the field @see #writer indenting it
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
    public void indentPrint (String string, int indent, boolean relative) throws java.io.IOException {
        this.setIndent(indent, relative);
        String newString = this.indentString + string;
        int newStringLength = newString.length();
        writer.write(newString, 0, newStringLength);
    }

    /**
     * Prints a string to the <code>FileWriter</code> stored in the field @see #writer.
     *
     * @param string the string to be printed.
     */
    public void print (String string) throws java.io.IOException {
        writer.write(string, 0, string.length());
    }

    /**
     * Flushes the <code>FileWriter</code> in the field @see #writer.
     */
    public void flush () throws java.io.IOException {
        writer.flush();
    }

  /**
   * Closes the <code>FileWriter</code> in the field @see #writer.
   */
  public void close() throws java.io.IOException {
    writer.close();
  }

}



