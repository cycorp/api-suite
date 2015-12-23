package com.cyc.session;



/*
 * #%L
 * File: CycServer.java
 * Project: Session Client
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

/**
 * Describes the address of a Cyc server. This class encapsulates a Cyc server's
 * host name, base port, and offset ports.
 * 
 * It is easily serializable via its {@link #toString()} method to a string representation as 
 * <code>host:port</code>, and can be instantiated from strings matching that format via its static
 * {@link #fromString()} method.
 * 
 * @author nwinant
 */
public class CycServer extends ServerAddress implements CycServerAddress {

  // Fields
  
  private static final long serialVersionUID = 1L;
  
  /**
   * Default offset for a Cyc server's ASCII port.
   */
  public static final int CYC_ASCII_PORT_OFFSET = 1;
  
  /**
   * Default offset for a Cyc server's Subl HTTP port.
   */
  public static final int CYC_HTTP_PORT_OFFSET = 2;
  
  /**
   * Default offset for a Cyc server's Servlet port.
   */
  public static final int CYC_SERVLET_PORT_OFFSET = 3;
  
  /**
   * Default offset for a Cyc server's binary (CFASL) port.
   */
  public static final int CYC_CFASL_PORT_OFFSET = 14;
    
  /**
   * Instance field for the Cyc server's ASCII port.
   */
  protected int asciiPortOffset = CYC_ASCII_PORT_OFFSET;
  
  /**
   * Instance field for the Cyc server's binary (CFASL) port.
   */
  protected int cfaslPortOffset = CYC_CFASL_PORT_OFFSET;
  
  /**
   * Instance field for the Cyc server's Subl HTTP port.
   */
  protected int httpPortOffset = CYC_HTTP_PORT_OFFSET;
  
  /**
   * Instance field for the Cyc server's Servlet port.
   */
  protected int servletPortOffset = CYC_SERVLET_PORT_OFFSET;
  
  
  // Constructors
  
  /**
   * Creates a CycServer for the given hostName and basePort
   * @param hostName
   * @param basePort 
   */
  public CycServer(String hostName, int basePort) {
    super(hostName, (basePort < 100) ? 3600 + basePort : basePort);
  }

  /**
   * Creates an empty CycServer. Mainly provided for use by serialization mechanisms.
   */
  protected CycServer() {
  }
  
  
  // Static
  
  /**
   * Tests whether a String could be deserialized into a valid CycServer. The string must match the
   * format <code>host:port</code>, the host must be a valid hostname or IP address, and the port 
   * must be a valid port integer. Any string which is validated by this method should be a 
   * well-formed input for {@link #fromString()}.
   * 
   * @param string to evaluate
   * @return whether it is a valid serialized CycServer address.
   */
  public static boolean isValidString(String string) {
    if (ServerAddress.isValidString(string)) {
      final ServerAddress server = ServerAddress.fromString(string);
      if (server.getPort() == null) {
        return false;
      }
      final String portString = server.getPort().toString();
      return (portString.length() == 1) || (portString.length() == 2) || (portString.length() == 4);
    }
    return false;
  }

  /**
   * Creates a new CycServer instance from a string matching the format <code>host:port</code>.
   * 
   * @param string a string matching the format <code>host:port</code>.
   * @return A new CycServer instance based on the string representation.
   */
  public static CycServer fromString(String string) {
    String tokens[] = string.split(":");
    return new CycServer(tokens[0], Integer.valueOf(tokens[1]));
  }
  
  /**
   * Attempts to guess the base port of a CycServer from an offset port. 
   * For example, given a port of 3654, we can <em>somewhat</em> safely guess that the
   * base port would be 3640. 
   * 
   * <p>Be <em>very careful</em> with this method. It is <em>only a guess</em>, and this method can 
   * (and has!) been wrong before. There's no guarantee that a Cyc server will be running in the 
   * 3600 block; if a Cyc server is running at, e.g., 3780 (which is rare, but entirely possible), 
   * and this method is given '80', it will incorrectly guess that the base port is 3680. In short,
   * any time you use this method, make sure that any potential errors are visible to users, and
   * can reasonably be corrected by them.
   * 
   * @param port
   * @return the base port number
   */
  public static Integer guessBasePortFromOffsetPort(final int port) {
    if ((port >= 3600) && (port <= 3699)) {
      final int suffix = port % 100;
      int image = suffix / 10 * 10;
      int offset = suffix % 10;
      if (suffix / 10 % 2 == 1) {
        image = image - 10;
        offset = offset + 10;
      }
      if ((offset == 0) || isAPortOffset(offset)) {
        return 3600 + image;
      }
    }
    return null;
  }
  
  /**
   * Tests whether an int is a known port offset.
   * @param offset
   * @return 
   */
  private static boolean isAPortOffset(int offset) {
    return (offset == CYC_ASCII_PORT_OFFSET)
            || (offset == CYC_HTTP_PORT_OFFSET)
            || (offset == CYC_SERVLET_PORT_OFFSET)
            || (offset == CYC_CFASL_PORT_OFFSET);
  }
  
  
  // Public

  /**
   * The Cyc server's base port number.
   * @return port number, or null if unspecified.
   */
  @Override
  public Integer getBasePort() {
    return super.getPort();
  }
  
  /**
   * The Cyc server's ASCII communications port number.
   * @return port number.
   */
  public Integer getAsciiPort() {
    return getBasePort() + asciiPortOffset;
  }
  
  /**
   * The Cyc server's binary (CFASL) communications port number.
   * @return port number.
   */
  @Override
  public Integer getCfaslPort() {
    return getBasePort() + cfaslPortOffset;
  }

  /**
   * The Cyc server's SubL HTTP server port number.
   * @return port number.
   */
  @Override
  public Integer getHttpPort() {
    return getBasePort() + httpPortOffset;
  }

  /**
   * The Cyc server's Servlet HTTP server port number.
   * @return port number.
   */
  @Override
  public Integer getServletPort() {
    return getBasePort() + servletPortOffset;
  }
  
  /**
   * Checks whether the CycServer instance contains enough information to be useful 
   * (i.e., a plausible hostname & base port.)
   * @return whether CycServer is plausibly useful.
   */
  @Override
  public boolean isDefined() {
    return super.isDefined() && (getPort() != null);
  }
  
  
  // Protected
  
  /**
   * Sets the base port.
   * @param basePort 
   */
  protected void setBasePort(Integer basePort) {
    super.setPort(basePort);
  }
}
