package com.cyc.session.mock;

/*
 * #%L
 * File: MockServerInfo.java
 * Project: Session API Implementation
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
import com.cyc.session.CycServerReleaseType;
import com.cyc.session.CycSessionConfiguration;
import com.cyc.session.SessionCommandException;
import com.cyc.session.SessionCommunicationException;

/**
 *
 * @author nwinant
 */
public class MockServerInfo implements CycServerInfo {
  final private CycSessionConfiguration config;

  public MockServerInfo(CycSessionConfiguration config) {
    this.config = config;
  }

  @Override
  public CycServerAddress getCycServer() {
    return config.getCycServer();
  }

  @Override
  public String getBrowserUrl() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public String getCycKBVersionString() throws SessionCommunicationException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public String getCycRevisionString() throws SessionCommunicationException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public boolean isAPICompatible() throws SessionCommunicationException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public boolean isOpenCyc() throws SessionCommunicationException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public CycServerReleaseType getSystemReleaseType() throws SessionCommunicationException, SessionCommandException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public int getCycMinorRevisionNumber() throws SessionCommunicationException, SessionCommandException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}
