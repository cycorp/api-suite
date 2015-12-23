package com.cyc.session.mock;

/*
 * #%L
 * File: MockSession.java
 * Project: Session Client
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

import com.cyc.session.CycServerAddress;
import com.cyc.session.CycServerInfo;
import com.cyc.session.CycSession;
import com.cyc.session.CycSessionConfiguration;
import com.cyc.session.exception.SessionConfigurationException;
import com.cyc.session.internal.AbstractCycSession;
import com.cyc.session.internal.ImmutableConfiguration;

/**
 *
 * @author nwinant
 */
public class MockSession extends AbstractCycSession implements CycSession {
  
  private MockServerInfo info = null;
  private ConnectionStatus status = ConnectionStatus.UNINITIALIZED;
  private MockConnection connection = null;
  
  public MockSession(CycSessionConfiguration config) {
    super(config);
    this.info = new MockServerInfo(config);
    this.setOptions(new MockSessionOptions());
  }
  
  public MockSession(CycServerAddress server) throws SessionConfigurationException {
    this(new ImmutableConfiguration(server, MockSession.class));
  }
  
  
  // Public
  
  @Override
  public ConnectionStatus getConnectionStatus() {
    if (getConnection() == null) {
      return ConnectionStatus.UNINITIALIZED;
    }
    return this.status;
  }

  @Override
  public CycServerInfo getServerInfo() {
    return this.info;
  }
  
  
/*
  final private CycSessionConfiguration config;
  private SessionStatus status = SessionStatus.UNINITIALIZED;
  private MockConnection connection = null;
  private MockServerInfo info = null;
  private SessionOptions options = null;
  */
/*
  public MockSession(CycSessionConfiguration config) {
    this.config = config;
    this.info = new MockServerInfo(config);
  }
  
  public MockSession(CycServerInfo server) {
    this.config = config;
    this.info = new MockServerInfo(config);
  }
  

  @Override
  public CycSessionConfiguration getConfiguration() {
    return this.config;
  }

  @Override
  public SessionStatus getStatus() {
    if (getConnection() == null) {
      return SessionStatus.UNINITIALIZED;
    }
    return this.status;
  }
  
  @Override
  public CycServerInfo getServerInfo() {
    return this.info;
  }
  
  @Override
  public SessionOptions getOptions() {
    return this.options;
  }
  */
  
  
  // Non-canon methods...
  
  public void setStatus(ConnectionStatus status) {
    this.status = status;
  }

  public MockConnection getConnection() {
    return connection;
  }

  public void setConnection(MockConnection connection) {
    this.connection = connection;
  }
}
