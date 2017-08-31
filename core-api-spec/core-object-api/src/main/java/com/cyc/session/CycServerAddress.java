/*
 * Copyright 2015 Cycorp, Inc.
 *
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
 */

package com.cyc.session;

/*
 * #%L
 * File: CycServerAddress.java
 * Project: Core API Object Specification
 * %%
 * Copyright (C) 2013 - 2017 Cycorp, Inc
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

import java.io.Serializable;

/**
 * Describes the address of a Cyc server. This class encapsulates a Cyc server's host name, base 
 * port, and offset ports. It is easily serializable via its {@link #toString()} method to a string
 * representation as <code>host:port</code>.
 * 
 * @author nwinant
 */
public interface CycServerAddress extends Comparable, Serializable {
  
  /**
   * The server's host name
   * @return host name, or null if unspecified.
   */
  String getHostName();

  /**
   * The server's port number.
   * @return port number, or null if unspecified.
   */
  Integer getPort();

  /**
   * Returns the resolved hostname of this address. If the hostname is a loopback address (e.g.
   * 127.0.0.1 or localhost), it will be converted to a fully-qualified domain-name. Otherwise, it
   * will return the specified hostname as per {@link #getHostName()}.
   *
   * <p>Note that this value is <em>not</em> used by {@link #hashCode()} or {@link #equals(Object)}.
   * Equality is based off of the explicitly-specified values.
   *
   * @return <code>String</code> the resolved hostname.
   */
  String getResolvedHostName();

  /**
   * The Cyc server's base port number.
   * @return port number, or null if unspecified.
   */
  Integer getBasePort();

  /**
   * The Cyc server's binary (CFASL) communications port number.
   * @return port number.
   */
  Integer getCfaslPort();

  /**
   * The Cyc server's SubL HTTP server port number.
   * @return port number.
   */
  Integer getHttpPort();

  /**
   * The Cyc server's Servlet HTTP server port number.
   * @return port number.
   */
  Integer getServletPort();

  /**
   * Checks whether the CycServerAddress instance contains enough information to be useful
   * (i.e., a plausible hostname and base port.)
   * @return whether CycServerAddress is plausibly useful.
   */
  boolean isDefined();

  /**
   * Returns a string representation of the server address <em>using the resolved hostname,</em> in
   * the format <code>host:port</code>. In other words, this methods behaves like
   * {@link #toString()}, but retrieves the hostname via {@link #getResolvedHostName()}.
   *
   * @return a string representation of the server address, using the resolved hostname.
   */
  String toResolvedAddressString();
  
  /**
   * Returns a string representation of the server address in the format <code>host:port</code>.
   *
   * @return a string representation of the server address.
   */
  @Override
  String toString();
}
