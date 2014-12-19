package com.cyc.session.internal;

/*
 * #%L
 * File: GenericServerAddress.java
 * Project: Session API
 * %%
 * Copyright (C) 2013 - 2014 Cycorp, Inc.
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
 * GenericServer is a class for describing any server. Primarily, it captures
 * the hostname and port. It is easily serializable, and contains methods for 
 * being instantiated from a string.
 * @author nwinant
 */
public class GenericServerAddress implements Serializable, Comparable<GenericServerAddress> {
  
  // Fields
  
  private static final long serialVersionUID = 1L;
  
  /**
   * Regex matching a valid IP address.
   */
  public static final String VALID_IP_ADDR_REGEX = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";
  
  /**
   * Regex matching a valid host name.
   */
  public static final String VALID_HOST_NAME_REGEX = "^(([a-zA-Z]|[a-zA-Z][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)*([A-Za-z]|[A-Za-z][A-Za-z0-9\\-]*[A-Za-z0-9])$";
  
  private String hostName = null;
  private String resolvedHostName = null;
  private Integer port = null;
  
  
  // Constructors
  
  /**
   * Creates a GenericServerAddress for the given hostName and port
   * @param hostName
   * @param basePort 
   */
  public GenericServerAddress(String hostName, Integer port) {
    setHostName(hostName);
    setPort(port);
  }
  
  /**
   * Creates an empty GenericServerAddress. Mainly provided for use by serialization mechanisms.
   */
  protected GenericServerAddress() {
  }
  
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
              } catch (Exception ex) { }
            }
          }
        }
      }
    }
    return false;
  }
  
  public static GenericServerAddress fromString(String string) {
    String tokens[] = string.split(":");
    if (tokens.length == 1) {
      return new GenericServerAddress(tokens[0], null);
    }
    return new GenericServerAddress(tokens[0], Integer.valueOf(tokens[1]));
  }
  
  
  // Public
  
  /**
   * The server's host name
   * @return host name, or null if unspecified.
   */
  public String getHostName() {
    return hostName;
  }
  
  /**
   * Returns the resolved hostname of this address. If the hostname is a 
   * loopback address (e.g. 127.0.0.1 or localhost), it will be converted to a
   * fully-qualified domain-name. Otherwise, it will return the specified 
   * hostname as per {@link #getHostName()}.
   * 
   * <p>Note that this value is <em>not</em> used by {@link #hashCode()} or 
   * {@link #equals(Object)}. Equality is based off of the explicitly-specified
   * values.
   * 
   * @return <code>String</code> the resolved hostname.
   */
  public String getResolvedHostName() {
    if ((this.resolvedHostName == null) 
            && (this.getHostName() != null)) {
      this.resolvedHostName = GenericServerAddress.resolveHostName(this.getHostName());
    }
    return this.resolvedHostName;
  }
  
  /**
   * The server's port number.
   * @return port number, or null if unspecified.
   */
  public Integer getPort() {
    return port;
  }
  
  /**
   * Checks whether the GenericServerAddress instance contains enough information to be useful 
   * (i.e., at least a plausible hostname.)
   * @return whether GenericServerAddress is plausibly useful.
   */
  public boolean isDefined() {
    return (getHostName() != null) && !getHostName().trim().isEmpty();
  }
  
  
  // Overridden
  
  @Override
  public String toString() {
    if (!isDefined()) {
      return "Server unspecified";
    }
    return getHostName() + ((getPort() != null) ? ":" + getPort() : "");
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
    return hashCode() == ((GenericServerAddress) obj).hashCode();
  }
  
  @Override
  public int compareTo(GenericServerAddress o) {
    if (o == null) {
      return 1;
    }
    if (o != null) {
      if (getHostName().compareTo(o.getHostName()) != 0) {
        return getHostName().compareTo(o.getHostName());
      }
      if (getHostName().compareTo(o.getHostName()) != 0) {
        return getHostName().compareTo(o.getHostName());
      }
    }
    return new Integer(getPort()).compareTo(o.getPort());
  }
  
  
  // Protected
  
  /**
   * Sets the hostname.
   * @param hostName 
   */
  protected void setHostName(String hostName) {
    this.hostName = hostName;
    this.resolvedHostName = null;
  }
  
  /**
   * Sets the port.
   * @param port 
   */
  protected void setPort(Integer port) {
    this.port = port;
  }
  
  
  // Other static methods...
  
  /**
   * Resolves a hostname. If the hostname is a loopback address 
   * (e.g. 127.0.0.1 or localhost), it will be converted to the fully-qualified 
   * domain-name of the local address.
   * 
   * If the hostname is <em>not</em> a loopback address, the original hostName 
   * parameter will be returned, unchanged.
   * 
   * @param hostName the hostname to be resolved.
   * @return <code>String</code> the resolved hostname.
   */
  static public String resolveHostName(String hostName) {
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
        Logger.getLogger(GenericServerAddress.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    return hostName;
  }
}
