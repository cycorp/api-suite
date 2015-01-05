package com.cyc.base;

/*
 * #%L
 * File: CycAccess.java
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

import com.cyc.session.CycServer;
import com.cyc.base.conn.LeaseManager;
import com.cyc.base.conn.CycConnectionInterface;

import com.cyc.base.cycobject.DenotationalTerm;
import com.cyc.base.cycobject.Fort;
import com.cyc.base.kbtool.AssertTool;
import com.cyc.base.kbtool.ComparisonTool;
import com.cyc.base.kbtool.InferenceTool;
import com.cyc.base.kbtool.InspectorTool;
import com.cyc.base.kbtool.LookupTool;
import com.cyc.base.kbtool.ObjectTool;
import com.cyc.base.kbtool.UnassertTool;
import com.cyc.session.CycServerInfo;
import com.cyc.session.CycSession;

/**
 *
 * @author nwinant
 */
public interface CycAccess {

  /**
   * Closes the CycConnection object. Modified by APB to be able to handle multiple calls to
   * close() safely.
   */
  void close();

  /**
   * Adds #$ to string for all CycConstantIs mentioned in the string that don't already have them
   *
   * @param str the String that will have #$'s added to it.
   *
   * @return a copy of str with #$'s added where appropriate.
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  String cyclifyString(String str) throws CycConnectionException, CycApiException;
  
  /**
   * Returns the CycConnection object.
   *
   * @return the CycConnection object
   */
  CycConnectionInterface getCycConnection();

  /**
   * Returns the Cyc image ID.
   *
   * @return the Cyc image ID string
   *
   * @throws CycConnectionException if a communications error occurs or the Cyc server cannot be found
   * @throws CycApiException if the Cyc server returns an error
   */
  String getCycImageID() throws CycConnectionException, CycApiException;
  
  /**
   * Returns the Cyc api services lease manager.
   *
   * @return the Cyc api services lease manager
   */
  @Deprecated
  LeaseManager getCycLeaseManager();

  /**
   * Gets the CycServer description of the server to which this CycAccess is connected.
   * @return a CycServer object
   */
  CycServer getCycServer();
  
  /**
   * Gets the hostname of the connection
   * 
   * deprecated, use getCycServer().getHostName()
   * @return the hostname of the connection
   */
  @Deprecated
  String getHostName();
  
  /**
   * gets the baseport of the connection
   *
   * deprecated, use getCycServer().getBasePort()
   * @return the baseport of the connection
   */
  @Deprecated
  int getBasePort();
  
  /**
   * deprecated, use getCycServer().getHttpPort()
   * @return the http of server the connection is connected to.
   */
  @Deprecated
  int getHttpPort();
  
  /*
   * Should the connection to the Cyc server be re-established if it for some reason becomes unreachable?
   * If true, a reconnect will be attempted, and a connection will be established with whatever Cyc
   * server is reachable to the original address, even if it is a different server process."
   */
  boolean getReestablishClosedConnections();
  
  /**
   * Does this CycAccess access a single Cyc?
   * @return true if this CycAccess will always access the same Cyc server,
 and returns false if the CycAccess was constructed with
 a {@link com.cyc.baseclient.comm.Comm} object that may result in different calls
   * being sent to different Cyc servers.
   */
  boolean hasStaticCycServer();

  /**
   * Returns whether or not we have a valid lease with the Cyc server.
   *
   * @return whether or not we have a valid lease with the Cyc server
   */
  boolean hasValidLease();

  /* *
   * Imports a MUC (Message Understanding Conference) formatted symbolic expression into cyc via
   * the function which parses the expression and creates assertions for the contained concepts
   * and relations between them.
   *
   * @param mucExpression the MUC (Message Understanding Conference) formatted symbolic expression
   * @param mtName the name of the microtheory in which the imported assertions will be made
   *
   * @return the number of assertions imported from the input MUC expression
   *
   * @throws CycConnectionException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
//  @Deprecated
//  int importMucExpression(CycList mucExpression, String mtName) throws CycConnectionException, CycApiException;

  /** Returns whether the connection is closed
   *
   * @return whether the connection is closed
   */
  boolean isClosed();
  
  /**
   * Returns true if this KB is an OpenCyc image.
   *
   * @return true if this KB is an OpenCyc image, otherwise false
   *
   * @throws CycConnectionException if a data communication error occurs or the Cyc server cannot be found
   */
  boolean isOpenCyc() throws CycConnectionException;

  /** Try to enhance
   * <code>urlString</code> to log
   * <code>cyclist</code> in and redirect to
   * the page it would otherwise go to directly.
   */
  String maybeAddLoginRedirect(final String urlString, final Fort cyclist, final DenotationalTerm applicationTerm);

  /**
   * Verify that this is not an OpenCyc image.
   *
   * @throws CycConnectionException if we can't communicate with Cyc.
   * @throws UnsupportedOperationException if it is an OpenCyc image.
   */
  void requireNonOpenCyc() throws CycConnectionException;

  /**
   * Sets the print-readable-narts feature on.
   *
   * @throws CycConnectionException if cyc server host not found on the network or the Cyc server cannot be found
   * @throws CycApiException if the api request results in a cyc server error
   */
  void setReadableNarts() throws CycConnectionException, CycApiException;

  /*
   * If true, the CycAccess will try to re-establish a connection with a Cyc server at the
   * provided address. Note that this may end up connecting to a different Cyc server than
   * it was originally connected to. If it is important for your application that the CycAccess only
   * ever connect to a single server, and not connect to a different server if the original one
   * becomes inaccessible, be sure to set this to false.
   * Default value is true.
   */
  void setReestablishClosedConnections(boolean value);
  
  /**
   * Turns off the diagnostic trace of socket messages.
   */
  void traceOff();

  /**
   * Turns on the diagnostic trace of socket messages.
   */
  void traceOn();

  /**
   * Turns on the detailed diagnostic trace of socket messages.
   */
  void traceOnDetailed();
  
  /**
   * Send commands to a Cyc server in the SubL language, and
   * receive the results expressed as Java objects.
   * 
   * @return CommandTool
   */
  CommandTool converse();
  
  /**
   * Provides tools for asserting facts to the Cyc KB.
   * 
   * @return AssertTool
   */
  AssertTool getAssertTool();
  
  /**
   * Provides tools for comparing different CycObjects.
   * 
   * @return ComparisonTool
   */
  ComparisonTool getComparisonTool();
  
  /**
   * Provides tools for performing inferences over the Cyc KB.
   * 
   * @return InferenceTool
   */
  InferenceTool getInferenceTool();
  
  /**
   * Provides tools for examining individual CycObjects.
   * 
   * @return InspectorTool
   */
  InspectorTool getInspectorTool();
  
  /**
   * Provides tools for looking up CycObjects in the Cyc KB.
   * 
   * @return LookupTool
   */
  LookupTool getLookupTool();
  
  /**
   * Provides tools for creating simple CycObjects, such as constants and lists.
   * 
   * @return ObjectTool
   */
  ObjectTool getObjectTool();
  
  /**
   * Provides tools for unasserting facts to the Cyc KB.
   * 
   * @return UnassertTool
   */
  UnassertTool getUnassertTool();
  
  /**
   * Provides basic information about the state and location of the current Cyc server.
   * 
   * @return ServerInfo
   */
  CycServerInfo getServerInfo();
  
  /**
   * Returns the CycSession object wrapping this CycAccess instance. If this
   * CycAccess was created independently of the Session API, this method will 
   * return null.
   * 
   * @return 
   */
  CycSession getCycSession();
  
  /**
   * 
   */
  CycAccessOptions getOptions();
}
