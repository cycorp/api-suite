package com.cyc.baseclient.cycobject;

/*
 * #%L
 * File: GuidImpl.java
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

import com.cyc.base.exception.BaseClientRuntimeException;
import com.cyc.baseclient.xml.XmlWriter;
import com.cyc.baseclient.xml.XmlStringWriter;
import com.cyc.base.cycobject.Guid;
import java.io.*;
import java.util.UUID;

/**
 * Provides the behavior and attributes of a Base Client GUID (Globally Unique
 * IDentifier). Each Base Client constant has an associated guid.
 *
 * @version $0.1$
 * @author Stephen L. Reed
 */
public class GuidImpl extends com.cyc.base.cycobject.Guid {

    /**
     * The name of the XML tag for this object.
     */
    public static final String guidXMLTag = "guid";

     /**
     * Constructs a new <tt>Guid</tt> object.
     */
    
    public GuidImpl(UUID guid) {
      super(guid);
    }
    
    public GuidImpl(String guidString) {
      super(guidString);
    }
    
    public GuidImpl(UUID guid, String guidString) {
      super(guid, guidString);
    }
    
    public GuidImpl(byte[] data) {
      super(data);      
    }



    /**
     * Returns the XML representation of this object.
     *
     * @return the XML representation of this object
     */
    @Deprecated
    public String toXMLString () throws IOException {
        XmlStringWriter xmlStringWriter = new XmlStringWriter();
        toXML(xmlStringWriter, 0, false);
        return xmlStringWriter.toString();
    }

    /**
     * Prints the XML representation of the GuidImpl to an <code>XMLWriter</code>
     *
     * @param xmlWriter an <tt>XMLWriter</tt>
     * @param indent an int that specifies by how many spaces to indent
     * @param relative a boolean; if true indentation is relative, otherwise absolute
     */
    @Deprecated
    public void toXML (XmlWriter xmlWriter, int indent, boolean relative)
        throws IOException {
        xmlWriter.printXMLStartTag(guidXMLTag, indent, relative, false);
        xmlWriter.print(guidString);
        xmlWriter.printXMLEndTag(guidXMLTag);
    }
    
    public static GuidImpl fromGuid(Guid guid) {
      if ((guid != null) && !GuidImpl.class.isInstance(guid)) {
        return new GuidImpl(guid.getGuidString());
      }
      return (GuidImpl) guid;
    }

    
    // private

    // Internal Rep
    
    
    
    public static void main (String[] args) {
      final byte[] data = {(byte) 189,  (byte)  88, (byte) 128, (byte) 244, (byte) 156, (byte)  41, (byte)  17, (byte) 177,
                           (byte) 157,  (byte) 173, (byte) 195, (byte) 121, (byte)  99, (byte) 111, (byte) 114, (byte) 112 };
      final GuidImpl guid1 = new GuidImpl(data);
      final GuidImpl guid2 = new GuidImpl("bd5880f4-9c29-11b1-9dad-c379636f7270");
      System.out.println("guid1 = " + guid1);
      System.out.println("guid2 = " + guid2);
     if (!guid1.equals(guid2)) {
        throw new BaseClientRuntimeException(guid1 + " does not equal " + guid2);     
      }
    }
}
