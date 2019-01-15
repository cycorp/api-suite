package com.cyc.session;

/*
 * #%L
 * File: CycAddress.java
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
import java.util.Optional;

/**
 * Describes the address of a Cyc server. This class encapsulates a Cyc server's host name, base
 * port, and offset ports. It is easily serializable via its {@link #toString()} method to a string
 * representation as <code>host:port</code>.
 *
 * @author nwinant
 */
public interface CycAddress extends Comparable, Serializable {

  //====|    Factory methods    |=================================================================//
  
  /**
   * Creates a CycAddress for the given hostName, basePort, and concurrencyLevel.
   *
   * @param hostName
   * @param basePort
   * @param concurrencyLevel
   *
   * @return
   */
  public static CycAddress get(String hostName, int basePort, Integer concurrencyLevel) {
    return new CycAddressImpl(hostName, basePort, Optional.of(concurrencyLevel));
  }

  /**
   * Creates a CycAddress for the given hostName and basePort.
   *
   * @param hostName
   * @param basePort
   *
   * @return
   */
  public static CycAddress get(String hostName, int basePort) {
    return new CycAddressImpl(hostName, basePort, Optional.empty());
  }

  /**
   * Tests whether a String could be deserialized into a valid CycAddress. The string must match the
   * format <code>host:port</code>, the host must be a valid hostname or IP address, and the port
   * must be a valid port integer. Any string which is validated by this method should be a
   * well-formed input for {@link #fromString(String string)}.
   *
   * @param string to evaluate
   *
   * @return whether it is a valid serialized CycAddress address
   */
  public static boolean isValidString(String string) {
    // validate concurrency setting if exists
    if (string.matches(".*:.*:.*")) {
      int lastSemicolonIdx = string.lastIndexOf(":");
      String possibleConcurrencyLevel = string.substring(lastSemicolonIdx + 1);
      try {
        int concurrencyLevel = Integer.parseInt(possibleConcurrencyLevel);
        if (concurrencyLevel <= 0) {
          return false;
        }
      } catch (Exception e) {
        return false;
      }
      string = string.substring(0, lastSemicolonIdx);
    }
    // validate host:port settings
    if (ServerAddress.isValidString(string)) {
      final ServerAddress server = ServerAddress.fromString(string);
      if (server.getPort() == null) {
        return false;
      }
      final String portString = server.getPort().toString();
      return (portString.length() == 1) || (portString.length() == 2)
                     || (server.getPort() > 999 && (server.getPort() < 65516)); //max TCP-IP port numberis 65535, so the highest base port number is 65516.
    }
    return false;
  }

  /**
   * Creates a new CycAddress instance from a string matching the format <code>host:port</code>.
   *
   * @param string a string matching the format <code>host:port</code> or
   *               <code>host:port:concurrencyLevel</code>.
   *
   * @return A new CycAddress instance based on the string representation
   */
  public static CycAddress fromString(String string) {
    String tokens[] = string.split(":");

    if (tokens.length > 2) {
      return new CycAddressImpl(tokens[0], Integer.valueOf(tokens[1]), Integer.valueOf(tokens[2]));
    } else {
      return new CycAddressImpl(tokens[0], Integer.valueOf(tokens[1]));
    }
  }

  /**
   * Creates a new CycAddress from an existing CycAddress, but with a new concurrencyLevel.
   *
   * @param address          CycAddress from which to create the pool
   * @param concurrencyLevel the concurrency level for the new CycAddress
   * 
   * @return A new CycAddress instance based on an existing CycAddress
   */
  public static CycAddress fromAddress(CycAddress address, Integer concurrencyLevel) {
    return new CycAddressImpl(address, concurrencyLevel);
  }

  /**
   * Creates a new CycAddress from an existing CycAddress.
   *
   * @param address CycAddress from which to create the new CycAddress
   * 
   * @return A new CycAddress instance based on an existing CycAddress
   */
  public static CycAddress fromAddress(CycAddress address) {
    return new CycAddressImpl(address);
  }
  
  /**
   * Creates a new CycAddress from an existing CycAddress, setting the concurrency level if
   * unspecified in the original address.
   *
   * @param address                 CycAddress from which to create the pool
   * @param defaultConcurrencyLevel the default concurrency level for the CycAddress, if unspecified
   * 
   * @return A new CycAddress instance based on an existing CycAddress
   */
  public static CycAddress fromAddressWithDefaults(CycAddress address,
                                                   Integer defaultConcurrencyLevel) {
    return fromAddress(address, address.getConcurrencyLevel().orElse(defaultConcurrencyLevel));
  }
  
  //====|    Constants    |=======================================================================//
  
  /**
   * Default offset for a Cyc server's ASCII port.
   */
  public static final int STANDARD_ASCII_PORT_OFFSET = 1;

  /**
   * Default offset for a Cyc server's Subl HTTP port.
   */
  public static final int STANDARD_HTTP_PORT_OFFSET = 2;

  /**
   * Default offset for a Cyc server's Servlet port.
   */
  public static final int STANDARD_SERVLET_PORT_OFFSET = 3;

  /**
   * Default offset for a Cyc server's binary (CFASL) port.
   */
  public static final int STANDARD_CFASL_PORT_OFFSET = 14;

  //====|    Methods    |=========================================================================//
  
  /**
   * The server's host name.
   *
   * @return host name, or null if unspecified
   */
  String getHostName();

  /**
   * The server's port number.
   *
   * @return port number, or null if unspecified
   */
  Integer getPort();

  /**
   * Returns the resolved hostname of this address. If the hostname is a loopback address (e.g.
   * 127.0.0.1 or localhost), it will be converted to a fully-qualified domain-name. Otherwise, it
   * will return the specified hostname as per {@link #getHostName()}.
   *
   * <p>
   * Note that this value is <em>not</em> used by {@link #hashCode()} or {@link #equals(Object)}.
   * Equality is based off of the explicitly-specified values.
   *
   * @return <code>String</code> the resolved hostname
   */
  String getResolvedHostName();

  /**
   * The Cyc server's base port number.
   *
   * @return port number, or null if unspecified
   */
  Integer getBasePort();

  /**
   * The Cyc server's binary (CFASL) communications port number.
   *
   * @return port number
   */
  Integer getCfaslPort();

  /**
   * The Cyc server's SubL HTTP server port number.
   *
   * @return port number
   */
  Integer getHttpPort();

  /**
   * The Cyc server's Servlet HTTP server port number.
   *
   * @return port number
   */
  Integer getServletPort();

  /**
   * Number of simultaneous jobs this server can handle, if used in the server pool.
   *
   * @return Optional containing the number of simultaneous jobs, if specified
   */
  Optional<Integer> getConcurrencyLevel();

  /**
   * Checks whether the CycAddress instance contains enough information to be useful (i.e., a
   * plausible hostname and base port).
   *
   * @return whether CycAddress is plausibly useful
   */
  boolean isDefined();
  
  /**
   * Returns a CycAddress consisting only of the host name and base port, excluding optional fields
   * like {@link #getConcurrencyLevel()}.
   * 
   * @return 
   */
  CycAddress toBaseAddress();
  
  /**
   * Returns a string representation of the server address <em>using the resolved hostname,</em> in
   * the format <code>host:port</code>. In other words, this methods behaves like
   * {@link #toString()}, but retrieves the hostname via {@link #getResolvedHostName()}.
   *
   * @return a string representation of the server address, using the resolved hostname
   */
  String toResolvedAddressString();
  
  /**
   * Returns a string representation of the server address in the format <code>host:port</code>.
   *
   * @return a string representation of the server address
   */
  @Override
  String toString();

}
