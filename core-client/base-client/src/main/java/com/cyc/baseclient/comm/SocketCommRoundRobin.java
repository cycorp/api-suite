package com.cyc.baseclient.comm;

/*
 * #%L
 * File: SocketCommRoundRobin.java
 * Project: Base Client
 * %%
 * Copyright (C) 2013 - 2015 Cycorp, Inc.
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

import com.cyc.baseclient.exception.CommException;
import com.cyc.base.exception.CycConnectionException;
import com.cyc.base.conn.CycConnection;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import com.cyc.baseclient.CycClient;
import static com.cyc.baseclient.comm.Comm.RequestType.INIT;
import static com.cyc.baseclient.comm.Comm.RequestType.NORMAL;

/**
 *
 * @author vijay
 */
public class SocketCommRoundRobin extends AbstractComm implements Comm {

  private List<SocketData> socketList;
  private int socketIdx;
  private int targetRoundRobinSize = 2;
  private boolean isInitialized = false;

  public SocketCommRoundRobin() {
    socketList = new ArrayList<SocketData>();
    socketIdx = 0;
  }

  public CommOutputStream createOutputStream() throws CommException, IOException {
    return null;
  }

  @Override
  public synchronized InputStream sendRequest(byte request[], String requestSummary, RequestType requestType) throws CommException, CycConnectionException {

    switch (requestType) {
      case INIT:
        try {
          // This will add upto 5 cyc images, running on localhost, 3600, 3620 to 3680.
          // This is only for testing purposes
          String host = "127.0.0.1";
          int port = 3600 + 20 * socketList.size();
          //System.out.println("Socket connection to: Host: " + host + " Port: " + port);
          SocketData socketData = new SocketData(host, port);
          
          socketList.add(socketData);
          CommOutputStream initOutputStream = socketData.getInitCos();
          
          synchronized (initOutputStream) {
            initOutputStream.write(request);
            initOutputStream.flush();
          }
          return socketData.getInitCis();
        } catch (IOException ioe) {
          throw new CycConnectionException(ioe);
        }
      case NORMAL:
        try {
          possiblyInitializeCommWIthServer();
          
          SocketData sd = socketList.get(socketIdx);
          
          synchronized (sd.getCos()) {
            sd.getCos().write(request);
            sd.getCos().flush();
          }
          socketIdx = (socketIdx + 1) % socketList.size();
          return sd.getInitCis();
        } catch (IOException ioe) {
          throw new CycConnectionException(ioe);
        }
      default:
        throw new IllegalArgumentException("Don't know about RequestType: " + requestType);
    }
  }

  public synchronized void possiblyInitializeCommWIthServer()
          throws CommException, CycConnectionException {
    try {
      if (isInitialized) {
        return;
      }
      if (socketList.size() >= targetRoundRobinSize - 1) {
        isInitialized = true;
      }
      byte[] initializationRequest = CycClient.getCycInitializationRequest(getCycConnection().getUuid());
      InputStream is = sendRequest(initializationRequest, makeRequestSummary("initialization request"), RequestType.INIT);
      
      CycClient.handleCycInitializationRequestResponse(is);
      getCycConnection().setupNewCommConnection(is);// @Note; this is required! It must happen after communication initialization.
    } catch (IOException ioe) {
      throw new CycConnectionException(ioe);
    }
  }

  @Override
  public String makeRequestSummary(String request) {
    return request;
  }
  
  @Override
  public synchronized void close() throws CycConnectionException {
    try {
      for (SocketData sd : socketList) {
        if (sd.getSocket() == null) {
          return;
        } else if (!sd.getSocket().isClosed()) {
          sd.getSocket().close();
        }
        if (sd.getInitSocket() == null) {
          return;
        } else if (!sd.getInitSocket().isClosed()) {
          sd.getInitSocket().close();
        }
      }
      socketList.clear();
    } catch (IOException ioe) {
      throw new CycConnectionException(ioe);
    }
  }

  public void setCycConnection(CycConnection conn) {
    isInitialized = false;
    super.setCycConnection(conn);
  }

  @Override
  public String toString() {
    return "SocketCommRoundRobin - Socket List: " + socketList.toString();
  }
}
