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

/**
 *
 * @author nwinant
 */
public class CycClientSession implements CycAccessSession<CycClient> {
  
  final private CycSessionConfiguration config;
  private CycClientOptions options;
  private CycClient client;

  public CycClientSession(CycSessionConfiguration config) {
    this.config = config;
  }

  @Override
  public SessionStatus getStatus() {
    if (this.getAccess() == null) {
      return SessionStatus.UNINITIALIZED;
    } else if (this.getAccess().isClosed()) {
      return SessionStatus.CLOSED;
    }
    return SessionStatus.CONNECTED;
  }
  
  @Override
  public CycClientOptions getOptions() {
    return this.options;
  }
  
  @Override
  public CycSessionConfiguration getConfiguration() {
    return this.config;
  }
  
  protected void setAccess(CycClient client) {
    this.client = client;
    setOptions(client);
  }
  
  @Override
  public CycClient getAccess() {
    return this.client;
  }

  @Override
  public CycServerInfo getServerInfo() {
    if (SessionStatus.UNINITIALIZED.equals(getStatus())) {
      return null;
    }
    return this.getAccess().getServerInfo();
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 11 * hash + (this.config != null ? this.config.hashCode() : 0);
    hash = 11 * hash + (this.client != null ? this.client.hashCode() : 0);
    return hash;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final CycClientSession other = (CycClientSession) obj;
    if (this.config != other.config && (this.config == null || !this.config.equals(other.config))) {
      return false;
    }
    if (this.client != other.client && (this.client == null || !this.client.equals(other.client))) {
      return false;
    }
    return true;
  }
  
  @Override
  public String toString() {
    return "[" + this.getClass().getSimpleName()
            + "#" + this.hashCode() + "]"
            + " -> " + CycSessionConfiguration.class.getSimpleName() + "=" + this.getConfiguration();
  }
  
  
  // Private
  
  private void setOptions(CycClient client) {
    this.options = new CycClientOptions(client);
  }
}
