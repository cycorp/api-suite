package com.cyc.session;

/*
 * #%L
 * File: ServerAddress.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2019 Cycorp, Inc
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
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ServerAddress describes a server's hostname and port. For example, "localhost:80"
 *
 * It is easily serializable via its {@link #toString()} method to a string representation as
 * <code>host:port</code>, and can be instantiated from strings matching that format via its static
 * {@link #fromString()} method.
 *
 * @author nwinant
 */
public class ServerAddress implements Serializable, Comparable {

  // Fields
  private static final long serialVersionUID = 1L;

  /**
   * Regex matching a valid IP address.
   */
  public static final String VALID_IP_ADDR_REGEX
          = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";

  /**
   * Regex matching a valid host name.
   */
  public static final String VALID_HOST_NAME_REGEX
          = "^(([a-zA-Z]|[a-zA-Z][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)*([A-Za-z]|[A-Za-z][A-Za-z0-9\\-]*[A-Za-z0-9])$";

  private String hostName = null;
  private String resolvedHostName = null;
  private Integer port = null;

  // Constructors
  
  /**
   * Creates a ServerAddress for the given hostName and port
   *
   * @param hostName
   * @param port
   */
  protected ServerAddress(String hostName, Integer port) {
    setHostName(hostName);
    setPort(port);
  }

  /**
   * Creates an empty ServerAddress. Mainly provided for use by serialization mechanisms.
   */
  protected ServerAddress() {
  }

  /**
   * Tests whether a String could be deserialized into a valid ServerAddress. The string must match
   * the format <code>host:port</code>, the host must be a valid hostname or IP address, and the
   * port must be a valid port integer. Any string which is validated by this method should be a
   * well-formed input for {@link #fromString()}.
   *
   * @param string to evaluate
   *
   * @return whether it is a valid serialized ServerAddress address.
   */
  public static boolean isValidString(String string) {
    if ((string != null) && !string.isEmpty()) {
      String tokens[] = string.split(":");
      if ((tokens.length >= 1) && (tokens.length <= 2)) {
        if (tokens[0].matches(VALID_HOST_NAME_REGEX) || tokens[0].matches(VALID_IP_ADDR_REGEX)) {
          if (tokens.length == 1) {
            return string.length() == tokens[0].length();
          } else {
            if (!tokens[1].isEmpty()) {
              try {
                Integer.parseInt(tokens[1]);
                return (tokens[1].length() <= 5);
              } catch (Exception ex) {
              }
            }
          }
        }
      }
    }
    return false;
  }

  /**
   * Creates a new ServerAddress instance from a string matching the format
   * <code>host:port</code>.
   *
   * @param string a string matching the format <code>host:port</code>.
   *
   * @return A new ServerAddress instance based on the string representation.
   */
  public static ServerAddress fromString(String string) {
    String tokens[] = string.split(":");
    if (tokens.length == 1) {
      return new ServerAddress(tokens[0], null);
    }
    return new ServerAddress(tokens[0], Integer.valueOf(tokens[1]));
  }

  // Public
  
  /**
   * The server's host name
   *
   * @return host name, or null if unspecified.
   */
  public String getHostName() {
    return hostName;
  }

  /**
   * Returns the resolved hostname of this address. If the hostname is a loopback address (e.g.
   * 127.0.0.1 or localhost), it will be converted to a fully-qualified domain-name. Otherwise, it
   * will return the specified hostname as per {@link #getHostName()}.
   *
   * <p>
   * Note that this value is <em>not</em> used by {@link #hashCode()} or {@link #equals(Object)}.
   * Equality is based off of the explicitly-specified values.
   *
   * @return <code>String</code> the resolved hostname.
   */
  public String getResolvedHostName() {
    if ((this.resolvedHostName == null)
                && (this.getHostName() != null)) {
      this.resolvedHostName = ServerAddress.resolveHostName(this.getHostName());
    }
    return this.resolvedHostName;
  }

  /**
   * The server's port number.
   *
   * @return port number, or null if unspecified.
   */
  public Integer getPort() {
    return port;
  }

  /**
   * Checks whether the ServerAddress instance contains enough information to be useful (i.e., at
   * least a plausible hostname.)
   *
   * @return whether ServerAddress is plausibly useful.
   */
  public boolean isDefined() {
    return (getHostName() != null) && !getHostName().trim().isEmpty();
  }

  // Overridden
  
  /**
   * Returns a string representation of the server address in the format <code>host:port</code>.
   *
   * @return a string representation of the server address.
   */
  @Override
  public String toString() {
    if (!isDefined()) {
      return "Server unspecified";
    }
    return getHostName() + ((getPort() != null) ? ":" + getPort() : "");
  }

  /**
   * Returns a string representation of the server address <em>using the resolved hostname,</em> in
   * the format <code>host:port</code>. In other words, this methods behaves like
   * {@link #toString()}, but retrieves the hostname via {@link #getResolvedHostName()}.
   *
   * @return a string representation of the server address, using the resolved hostname.
   */
  public String toResolvedAddressString() {
    if (!isDefined()) {
      return "Server unspecified";
    }
    return getResolvedHostName() + ((getPort() != null) ? ":" + getPort() : "");
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 31 * hash + (null == getHostName() ? 0 : getHostName().hashCode());
    hash = 31 * hash + getPort();
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if ((obj == null) || (obj.getClass() != this.getClass())) {
      return false;
    }
    return hashCode() == ((ServerAddress) obj).hashCode();
  }

  @Override
  public int compareTo(Object o) {
    if ((o == null) || !(o instanceof ServerAddress)) {
      return 1;
    }
    final ServerAddress addr = (ServerAddress) o;
    final int hostNameValue = getHostName().compareTo(addr.getHostName());
    if (hostNameValue != 0) {
      return hostNameValue;
    }
    return getPort().compareTo(addr.getPort());
  }

  // Protected
  
  /**
   * Sets the hostname.
   *
   * @param hostName
   */
  protected void setHostName(String hostName) {
    this.hostName = hostName;
    this.resolvedHostName = null;
  }

  /**
   * Sets the port.
   *
   * @param port
   */
  protected void setPort(Integer port) {
    this.port = port;
  }

  // Other static methods...
  
  /**
   * Resolves a hostname. If the hostname is a loopback address (e.g. 127.0.0.1 or localhost), it
   * will be converted to the fully-qualified domain-name of the local address. If the hostname is
   * <em>not</em> a loopback address, the original hostName parameter will be returned, unchanged.
   *
   * @param hostName the hostname to be resolved.
   *
   * @return <code>String</code> the resolved hostname.
   */
  public static String resolveHostName(String hostName) {
    Boolean knownLoopback = null;
    try {
      knownLoopback = InetAddress.getByName(hostName).isLoopbackAddress();
    } catch (UnknownHostException ex) {
      knownLoopback = (hostName.equals("127.0.0.1") || hostName.equals("localhost"));
    }
    if (knownLoopback) {
      try {
        return InetAddress.getLocalHost().getCanonicalHostName();
      } catch (UnknownHostException ex) {
        Logger.getLogger(ServerAddress.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    return hostName;
  }
  
}
