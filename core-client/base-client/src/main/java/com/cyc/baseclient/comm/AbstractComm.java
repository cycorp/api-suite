package com.cyc.baseclient.comm;

/*
 * #%L
 * File: AbstractComm.java
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

import com.cyc.base.conn.CycConnection;
import com.cyc.baseclient.connection.CfaslInputStream;
import com.cyc.baseclient.connection.CfaslOutputStream;
import com.cyc.baseclient.cycobject.CycArrayList;
import com.cyc.baseclient.cycobject.CycSymbolImpl;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;


//// External Imports

/** 
 * <P>AbstractComm is designed to provide a convient location for methods that
 * can be used across all Comm implementations.
 *
 * @author tbrussea, May 7, 2013, 12:19:20 PM
 * @version $Id: AbstractComm.java 163397 2016-01-05 23:58:08Z nwinant $
 */
public abstract class AbstractComm implements Comm {

  // Static methods
  
  public static byte[] getCycInitializationRequest(UUID uuid) {
    CycArrayList request = new CycArrayList();
    request.add(new CycSymbolImpl("INITIALIZE-JAVA-API-PASSIVE-SOCKET"));
    request.add(uuid.toString());
    ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
    CfaslOutputStream cfo = new CfaslOutputStream(baos);
    try {
      cfo.writeObject(request);
      cfo.flush();
    } catch (IOException ioe) {} // ignore, should never happen
    return baos.toByteArray();
  }
  
  public static void handleCycInitializationRequestResponse(InputStream is) throws IOException {
    CfaslInputStream inboundStream = new CfaslInputStream(is);
    // read and ignore the status
    inboundStream.resetIsInvalidObject();
    Object status = inboundStream.readObject();
    // read and ignore the response
    inboundStream.resetIsInvalidObject();
    Object response = inboundStream.readObject();
  }
  
  //// Constructors

  /** Creates a new instance of AbstractComm. */
  public AbstractComm() {}

  //// Public Area
  
  @Override
  public synchronized CycConnection getCycConnection() {
    return cycConnection;
  }
  
  @Override
  public synchronized void setCycConnection(CycConnection conn){
    if (conn == this.cycConnection) {
      return;
    }
    this.cycConnection = conn;
  }

  //// Protected Area

  //// Private Area

  //// Internal Rep
  
  private CycConnection cycConnection;

}
