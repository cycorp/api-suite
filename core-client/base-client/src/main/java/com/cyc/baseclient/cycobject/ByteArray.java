package  com.cyc.baseclient.cycobject;

/*
 * #%L
 * File: ByteArray.java
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

import com.cyc.baseclient.xml.XmlWriter;
import com.cyc.baseclient.xml.XmlStringWriter;
import java.io.*;

/*****************************************************************************
 * Contains an array of bytes, as an object that directly represents a SubL
 * byte vector.  The primitive element in java is a byte having a signed numerical
 * value between -128 and +127.  The corresponding primitive element in SubL is a
 * byte having an unsigned numerical value between 0 and 255.<p>
 *
 * The ByteArray object can only be used in the OpenCyc binary api because it does not
 * have a string representation in either SubL or java.
 *
 * @version $Id: ByteArray.java 163356 2016-01-04 20:55:47Z nwinant $
 * @author Bjorn Aldag
 */
public class ByteArray extends DefaultCycObject implements Serializable {

    static final long serialVersionUID = -6247169945186440269L;
    
    /**
     * the name of the XML tag for Cyc byte-vector objects
     */
    public static final String byteVectorXMLTag = "byte-vector";

    /**
     * the name of the XML tag for the length of Cyc byte-vector objects
     */
    public static final String lengthXMLTag = "length";

    /**
     * the name of the XML tag for the byte elements of Cyc byte-vector objects
     */
    public static final String byteXMLTag = "byte";

    /**
     * the default indentation for printing CycConstant objects to XML
     */
    public static int indentLength = 2;

    /**
     * The actual array of bytes of this byte array.
     */
    private byte[] bytes;

    /**
     * Creates a new byte array from the specified array of bytes.
     */
    public ByteArray (byte[] bytes) {
        this.bytes = bytes;
    }

    /**
     * Returns the array of bytes of this ByteArray.
     * @return the array of bytes of this ByteArray.
     */
    public byte[] byteArrayValue () {
        return  bytes;
    }

    /**
     * Returns <tt>true</tt> iff some object equals this <tt>ByteArray</tt>
     *
     * @param object the <tt>Object</tt> for equality comparison
     * @return equals <tt>boolean</tt> value indicating equality or non-equality.
     */
    public boolean equals(Object object) {
        if (! (object instanceof ByteArray))
            return false;
        if (bytes.length != ((ByteArray) object).bytes.length)
            return false;
        for (int i = 0; i < bytes.length; i++)
            if (bytes[i] != ((ByteArray) object).bytes[i])
                return false;
        return true;
    }

    /**
     * Returns the string representation of the <tt>ByteArray</tt>
     *
     * @return the representation of the <tt>ByteArray</tt> as a <tt>String</tt>
     */
    public String toString() {
        StringBuffer result = new StringBuffer("[ByteArray len:");
        result.append(bytes.length);
        result.append(" ");
        if (bytes.length > 0)
            result.append((new Byte(bytes[0])).toString());
        for (int i = 1; i < bytes.length; i++) {
            result.append(",");
            result.append((new Byte(bytes[i])).toString());
        }
        result.append("]");
        return result.toString();
    }

    /**
     * Marshalls this ByteArray object into its CYC-ML XML expression.
     *
     * @return the CYC-ML XML representation string
     */
    @Deprecated
    public String toXMLString() throws IOException {
        XmlStringWriter xmlStringWriter = new XmlStringWriter();
        toXML(xmlStringWriter, 0, false);
        return xmlStringWriter.toString();
    }

    /**
     * Prints the XML representation of this ByteArray to an <code>XMLWriter</code>
     *
     * @param xmlWriter an <tt>XMLWriter</tt>
     * @param indent an int that specifies by how many spaces to indent
     * @param relative a boolean; if true indentation is relative, otherwise absolute
     */
    @Deprecated
    public void toXML (XmlWriter xmlWriter, int indent, boolean relative)
        throws IOException {
        xmlWriter.printXMLStartTag(byteVectorXMLTag, indent, relative, true);
        xmlWriter.printXMLStartTag(lengthXMLTag, indentLength, true, false);
        xmlWriter.print(Integer.toString(bytes.length));
        xmlWriter.printXMLEndTag(lengthXMLTag);
        for (int i = 0; i < bytes.length; i++) {
            xmlWriter.printXMLStartTag(byteXMLTag, 0, true, false);
            xmlWriter.print(Byte.toString(bytes[i]));
            xmlWriter.printXMLEndTag(byteXMLTag);
        }
        xmlWriter.printXMLEndTag(byteVectorXMLTag, -indentLength, true);
    }
    
    public String stringApiValue() {
      StringBuffer buf = new StringBuffer(bytes.length*4);
      buf.append( "(read-from-string \"#(");
      for (int i = 0; i < bytes.length; i++) {
        buf.append( ' ');
        int value = bytes[i];
        if (value < 0) {
          value += 256;
        }
        buf.append( value);
      }
      buf.append( ")\")");
      return buf.toString();
    }

    //// Private Area
  private void writeObject(ObjectOutputStream stream) throws java.io.IOException {
    stream.defaultWriteObject();
    stream.writeInt( this.byteArrayValue().length);
    stream.write( byteArrayValue());
  }
   
  private void readObject(ObjectInputStream stream) throws java.io.IOException, 
  java.lang.ClassNotFoundException {
    stream.defaultReadObject();
    int size = stream.readInt();
    this.bytes = new byte[size];
    int index = 0, remainder = size;
    while (index < size) {
      int numOfBytes = stream.read(bytes, index, remainder);
      if (numOfBytes == -1) 
        throw new java.io.EOFException( "Unexpected EOF at index " + index + " of " + size);
      else {
        index += numOfBytes;
        remainder -= numOfBytes;
      }
    }
  }

  public int compareTo(Object o){
    if(!(o instanceof ByteArray)) return this.toString().compareTo(o.toString());
    int thisbound=bytes.length;
    ByteArray cmp=(ByteArray)o;
    int obound=cmp.bytes.length;
    int bound=thisbound>obound?obound:thisbound;
    for(int i=0;i<bound;i++){
      if(bytes[i]==cmp.bytes[i]) continue;
      return (int)bytes[i]-(int)cmp.bytes[i];
    }
    return bound-obound;
  }


}








