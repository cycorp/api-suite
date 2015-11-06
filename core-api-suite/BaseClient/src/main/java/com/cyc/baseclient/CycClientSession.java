package com.cyc.baseclient;

/*
 * #%L
 * File: CycClientSession.java
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

import com.cyc.base.CycAccessSession;
import com.cyc.session.CycServerInfo;
import com.cyc.session.CycSessionConfiguration;
import com.cyc.session.internal.AbstractCycSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nwinant
 */
public class CycClientSession extends AbstractCycSession<CycClientOptions> implements CycAccessSession<CycClient> {
        
  // Fields
  
  static final private Logger LOGGER = LoggerFactory.getLogger(CycClientSession.class);
  private CycClient client;

  
  // Constructors
  
  public CycClientSession(CycSessionConfiguration config) {
    super(config);
  }
  
  
  // Public
  
  @Override
  public CycClient getAccess() {
    return this.client;
  }
  
  @Override
  public ConnectionStatus getConnectionStatus() {
    if (getAccess() == null) {
      return ConnectionStatus.UNINITIALIZED;
    } else if (getAccess().isClosed()) {
      return ConnectionStatus.DISCONNECTED;
    }
    return ConnectionStatus.CONNECTED;
  }
  
  @Override
  public CycServerInfo getServerInfo() {
    if (ConnectionStatus.UNINITIALIZED.equals(getConnectionStatus())) {
      return null;
    }
    return this.getAccess().getServerInfo();
  }
  /*
  //TODO: This causes an NPE to be thrown when SessionManagerImpl#releaseCycServerIfAppropriate 
  //      calls CycClientSession#getServerInfo()#getCycServer() - nwinant, 2015-10-28
  @Override
  public void close() {
    final boolean alreadyClosed = isClosed();
    super.close();
    if (!alreadyClosed) {
      LOGGER.debug("Session {} releasing {}", this, getAccess());
      setAccess(null);
    }
  }
  */
  
  // Protected
  
  protected void setAccess(CycClient client) {
    this.client = client;
    if (client != null) {
      setOptions(new CycClientOptions(this));
    }
  }
  
}
