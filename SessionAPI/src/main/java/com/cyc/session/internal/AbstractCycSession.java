package com.cyc.session.internal;

/*
 * #%L
 * File: AbstractCycSession.java
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

import com.cyc.session.CycSession;
import com.cyc.session.CycSessionConfiguration;
import com.cyc.session.SessionOptions;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nwinant
 * @param <T>
 */
abstract public class AbstractCycSession<T extends SessionOptions> implements CycSession {
  static final private Logger LOGGER = LoggerFactory.getLogger(AbstractCycSession.class);
  final private long creationTime;
  final private CycSessionConfiguration config;
  final private List<CycSession.SessionListener> listeners = new ArrayList();
  private T options;
  private boolean closed = false;
  
  public AbstractCycSession(CycSessionConfiguration config) {
    this.creationTime = System.currentTimeMillis();
    this.config = config;
    LOGGER.debug("Created new CycClientSession: {}", creationTime);
  }
  
  @Override
  public T getOptions() {
    return this.options;
  }
  
  protected void setOptions(T options) {
    this.options = options;
  }
  
  @Override
  public CycSessionConfiguration getConfiguration() {
    return this.config;
  }
  
  @Override
  public void close() {
    if (isClosed()) {
      return;
    }
    LOGGER.info("Session closed {}", this);
    this.closed = true; // Set this before firing listeners, or risk an infinite loop.
    for (CycSession.SessionListener listener : this.listeners) {
      listener.onClose(Thread.currentThread());
    }
  }
  
  @Override
  public boolean isClosed() {
    return this.closed;
  }
  
  @Override
  public SessionListener addListener(SessionListener listener) {
    this.listeners.add(listener);
    return listener;
  }
  
  @Override
  public String toString() {
    return "[" + this.getClass().getSimpleName()
            + "#" + this.hashCode() + "]"
            + " -> " + CycSessionConfiguration.class.getSimpleName() + "=" + this.getConfiguration();
  }
  
}
