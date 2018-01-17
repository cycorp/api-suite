package com.cyc.session;

/*
 * #%L
 * File: CycSession.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2017 Cycorp, Inc
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
import com.cyc.session.exception.SessionCommunicationException;
import com.cyc.session.exception.SessionConfigurationException;
import com.cyc.session.exception.SessionInitializationException;
import com.cyc.session.spi.SessionManager;
import java.io.Closeable;

/**
 * An active (or potentially active) session with a Cyc server. Implementation is provided by a
 * SessionFactory.
 *
 * @author nwinant
 */
public interface CycSession extends Closeable {

  //====|    Factory methods    |=================================================================//
  
  /**
   * Returns the CycSession currently assigned to this thread. This static method wraps a call to 
   * {@link SessionManager#getCurrentSession() }; see that method's documentation for more details.
   *
   * @return CycSession
   *
   * @throws SessionConfigurationException  if the application is not sufficiently configured for
   *                                        the CycConfigurationManager to connect to a Cyc server.
   * @throws SessionCommunicationException  if the application encounters problems communicating
   *                                        with a Cyc server.
   * @throws SessionInitializationException if the application encounters problems initializing the
   *                                        CycSession.
   */
  public static CycSession getCurrentSession()
          throws SessionConfigurationException, SessionCommunicationException,
                 SessionInitializationException {
    return CycSessionManager.getInstance().getCurrentSession();
  }

  //====|    ConnectionStatus enum    |===========================================================//
  
  /**
   * The connection status of a {@link CycSession}.
   */
  public enum ConnectionStatus {

    /**
     * Session has not been connected to the Cyc server.
     */
    UNINITIALIZED,
    /**
     * Session is currently connected to the Cyc server.
     */
    CONNECTED,
    /**
     * Session has been disconnected from the Cyc server.
     */
    DISCONNECTED
  }

  /*
  public enum SessionStatusChangeReason {

    CHANGED_BY_APPLICATION("Status changed by application"),
    CHANGED_BY_USER("Status changed by user"),
    CHANGED_BY_CYC_SERVER("Status changed by Cyc server"),
    OTHER("Other reason"),
    UNKNOWN("Unknown reason");

    private String description;

    private SessionStatusChangeReason(String description) {
      this.description = description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public String getDescription() {
      return this.description;
    }
  }
   */
  //====|    SessionListener    |=================================================================//
  
  /**
   * Provides notifications about changes to a CycSession.
   *
   * @see CycSession#addListener(com.cyc.session.CycSession.SessionListener)
   */
  public interface SessionListener {

    //public void onStatusChange(SessionStatus oldStatus, SessionStatus newStatus, 
    //                           SessionStatusChangeReason reason);
    
    public void onClose(Thread closingThread);
    
  }

  //====|    Interface methods    |===============================================================//
  
  /**
   * Returns the current status of the session's connection.
   *
   * @return the current SessionStatus
   */
  ConnectionStatus getConnectionStatus();

  /**
   * Returns a set of modifiable options, including the name of the cyclist making assertions, and
   * the project's KE purpose. The session's configuration may provide defaults for these options,
   * or possibly prevent certain options from being changed.
   *
   * @return the current SessionOptions
   */
  SessionOptions getOptions();

  /**
   * Returns the CycSessionConfiguration used to configure this session. These settings cannot be
   * modified.
   *
   * @return the original CycSessionConfiguration
   */
  CycSessionConfiguration getConfiguration();

  /**
   * Provides basic information about the state and location of the server to which the CycSession
   * is connected (or was connected). If the CycSession has not been initialized (i.e.,
   * <code>getStatus().equals(SessionStatus.UNINITIALIZED)</code>) this method should return
   * <code>null</code>.
   *
   * @return CycServerInfo for the session's server, or null if CycSession is uninitialized.
   */
  CycServerInfo getServerInfo();

  /* *
   * The SessionCriteria by which this session was created, if any.
   * @return the criteria by which this session was created.
   */
  //public SessionCriteria getCreationCriteria();
  /**
   * Closes a session, and releases it from the local thread. This method should always be called
   * when you know that you are finished with a particular session.
   *
   * <p>
   * Note that this method will not necessarily close the <em>underlying connection</em> to the Cyc
   * server; such details are handled by the SessionManager, which may allow resources to be shared
   * between sessions. Calling this method informs the SessionManager that a particular session is
   * closed, allowing the manager to make better decisions about resource management.
   *
   * @see <a href="http://dev.cyc.com/api/core/session/connection-management/">Session API
   * Connection Management</a>
   */
  @Override
  void close();

  /**
   * Returns whether the CycSession is closed.
   *
   * @return whether CycSession is closed
   */
  public boolean isClosed();

  /**
   * Adds a {@link com.cyc.session.CycSession.SessionListener} to this session.
   *
   * @param listener
   *
   * @return the SessionListener which was added to the session
   */
  SessionListener addListener(SessionListener listener);

}
