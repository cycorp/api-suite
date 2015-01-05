package com.cyc.baseclient.comm;

/*
 * #%L
 * File: SocketData.java
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

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import com.cyc.baseclient.api.CycConnection;

/**
 *
 * @author vijay
 */
public class SocketData {

  private String hostName;
  private int port;
  private Socket socket;
  private InputStream cis;
  private CommOutputStream cos;
  
  private Socket initSocket;
  private InputStream initCis;
  private CommOutputStream initCos;

  public SocketData(String hostName, int portNum) throws IOException {
    this.hostName = hostName;
    this.port = portNum + CycConnection.CFASL_PORT_OFFSET;

    socket = new Socket(this.hostName, this.port);
    int val = socket.getReceiveBufferSize();
    socket.setReceiveBufferSize(val * 2);
    socket.setTcpNoDelay(true);
    socket.setKeepAlive(true);
    cis = socket.getInputStream();
    cos = new DefaultCommOutputStream(socket.getOutputStream());

    initSocket = new Socket(this.hostName, this.port);
    int val2 = initSocket.getReceiveBufferSize();
    initSocket.setReceiveBufferSize(val2 * 2);
    initSocket.setTcpNoDelay(true);
    initSocket.setKeepAlive(true);
    initCis = initSocket.getInputStream();
    initCos = new DefaultCommOutputStream(initSocket.getOutputStream());
  }

  public String getHostName() {
    return hostName;
  }

  public int getPort() {
    return port;
  }

  public InputStream getCis() {
    return cis;
  }

  public CommOutputStream getCos() {
    return cos;
  }

  public Socket getSocket() {
    return socket;
  }

  public Socket getInitSocket() {
    return initSocket;
  }

  public InputStream getInitCis() {
    return initCis;
  }

  public CommOutputStream getInitCos() {
    return initCos;
  }

  public String toString() {
    return "Socket with Host: " + hostName + " Port: " + port;
  }
}
