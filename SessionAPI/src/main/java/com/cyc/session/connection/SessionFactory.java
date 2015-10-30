package com.cyc.session.connection;

/*
 * #%L
 * File: SessionFactory.java
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
import com.cyc.session.CycSession;
import com.cyc.session.CycSessionConfiguration;
import com.cyc.session.SessionCommunicationException;
import com.cyc.session.SessionConfigurationException;
import com.cyc.session.SessionInitializationException;
import java.io.Closeable;

/**
 * Responsible for creating and initializing implementations of {@link CycSession}.
 * @param <T> - CycSession implementation
 */
public interface SessionFactory<T extends CycSession> extends Closeable {
  
  /**
   * Creates an uninitialized CycSession instance.
   * 
   * @param config
   * @return a new CycSession.
   * @throws SessionConfigurationException 
   */
  T createSession(CycSessionConfiguration config) throws SessionConfigurationException;
  
  /**
   * Initializes a CycSession instance.
   * 
   * @param session an uninitialized CycSession.
   * @return the CycSession, initialized.
   * @throws SessionCommunicationException
   * @throws SessionInitializationException 
   */
  T initializeSession(T session) throws SessionCommunicationException, SessionInitializationException;
  
  /**
   * Releases any server-related resources which a SessionFactory might be maintaining.
   * @param server 
   */
  void releaseResourcesForServer(CycServerAddress server);
  
  //void releaseResourcesForSession(T session);
  
  /**
   * Returns whether the SessionFactory is closed.
   * 
   * @return whether SessionFactory is closed
   */
  public boolean isClosed();
  
}
