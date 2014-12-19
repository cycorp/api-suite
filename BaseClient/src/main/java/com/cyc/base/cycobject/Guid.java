package com.cyc.base.cycobject;

/*
 * #%L
 * File: Guid.java
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

import java.io.*;
import java.util.UUID;

/**
 * Provides the behavior and attributes of a Cyc GUID (Globally Unique
 * IDentifier). Each Cyc constant has an associated guid.
 *
 * @version $0.1$
 * @author Stephen L. Reed
 */
public class Guid implements Serializable {
  
  /**
   * The name of the XML tag for this object.
   */
  //public static final String guidXMLTag = "guid";
  
  /**
   * Constructs a new <tt>Guid</tt> object.
   * @param guid
   */
  public Guid(UUID guid) {
    this(guid, guid.toString());
  }
  
  public Guid(String guidString) {
    this(UUID.fromString(guidString), guidString);
  }
  
  public Guid(UUID guid, String guidString) {
    this.guid = guid;
    this.guidString = guidString;
  }
  
  public Guid(byte[] data) {
    this(new UUID(guidMSB(data), guidLSB(data)));
  }
  
  /**
   * Returns <tt>true</tt> if the object equals this object.
   *
   * @param object
   * @return <tt>boolean</tt> indicating equality of an object with this object.
   */
  @Override
  public boolean equals(Object object) {
    if (object instanceof Guid &&
            this.guid.equals(((Guid) object).guid)) {
      return true;
    }
    else
      return false;
  }
  
  /**
   * Returns the hash code for this object.
   *
   * @return the hash code for this object
   */
  @Override
  public int hashCode () {
    return guid.hashCode();
  }
  
  /**
   * Returns a string representation of the <tt>Guid</tt>.
   *
   * @return the <tt>Guid</tt> formated as a <tt>String</tt>.
   */
  @Override
  public String toString() {
    return getGuidString();
  }
  
  public String getGuidString() {
    return guidString;
  }
  
  
  // private
  
  private static final long guidMSB(byte[] data) {
    long msb = 0;
    assert data.length == 16;
    for (int i=0; i<8; i++)
      msb = (msb << 8) | (data[i] & 0x0ff);
    return msb;
  }
  
  private static final long guidLSB(byte[] data) {
    long lsb = 0;
    assert data.length == 16;
    for (int i=8; i<16; i++)
      lsb = (lsb << 8) | (data[i] & 0x0ff);
    return lsb;
  }
  
  // Internal Rep
  
  /**
   * The GUID in string form.
   * @deprecated @see getGuidString()
   */
  public  final String guidString;
  private final UUID   guid;
  
  
  /*
  public static void main (String[] args) {
    final byte[] daGuidta = {(byte) 189,  (byte)  88, (byte) 128, (byte) 244, (byte) 156, (byte)  41, (byte)  17, (byte) 177,
      (byte) 157,  (byte) 173, (byte) 195, (byte) 121, (byte)  99, (byte) 111, (byte) 114, (byte) 112 };
    final Guid guid1 = new Guid(data);
    final Guid guid2 = new Guid("bd5880f4-9c29-11b1-9dad-c379636f7270");
    System.out.println("guid1 = " + guid1);
    System.out.println("guid2 = " + guid2);
    if (!guid1.equals(guid2)) {
      throw new BaseClientRuntimeException(guid1 + " does not equal " + guid2);
    }
  }
  */
}
