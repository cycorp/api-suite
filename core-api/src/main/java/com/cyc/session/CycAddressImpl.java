package com.cyc.session;

/*
 * #%L
 * File: CycAddressImpl.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2018 Cycorp, Inc
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
import java.util.Objects;
import java.util.Optional;

import static com.cyc.session.CycAddress.STANDARD_ASCII_PORT_OFFSET;
import static com.cyc.session.CycAddress.STANDARD_CFASL_PORT_OFFSET;
import static com.cyc.session.CycAddress.STANDARD_HTTP_PORT_OFFSET;
import static com.cyc.session.CycAddress.STANDARD_SERVLET_PORT_OFFSET;

/**
 * Describes the address of a Cyc server. This class encapsulates a Cyc server's host name, base
 * port, and offset ports.
 *
 * It is easily serializable via its {@link #toString()} method to a string representation as
 * <code>host:port</code>, and can be instantiated from strings matching that format via its static
 * {@link #fromString(String string)} method.
 *
 * @author nwinant
 */
class CycAddressImpl extends ServerAddress implements CycAddress {

  //====|    Static methods    |==================================================================//
  
  /**
   * Attempts to guess the base port of a CycAddress from an offset port. For example, given a port
   * of 3654, we can <em>somewhat</em> safely guess that the base port would be 3640.
   *
   * <p>
   * Be <em>very careful</em> with this method. It is <em>only a guess</em>, and this method can
   * (and has!) been wrong before. There's no guarantee that a Cyc server will be running in the
   * 3600 block; if a Cyc server is running at, e.g., 3780 (which is rare, but entirely possible),
   * and this method is given '80', it will incorrectly guess that the base port is 3680. In short,
   * any time you use this method, make sure that any potential errors are visible to users, and can
   * reasonably be corrected by them.
   *
   * @param port
   *
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
   *
   * @param offset
   *
   * @return
   */
  private static boolean isAPortOffset(int offset) {
    return (offset == STANDARD_ASCII_PORT_OFFSET)
                   || (offset == STANDARD_HTTP_PORT_OFFSET)
                   || (offset == STANDARD_SERVLET_PORT_OFFSET)
                   || (offset == STANDARD_CFASL_PORT_OFFSET);
  }

  //====|    Fields    |==========================================================================//
  
  private static final long serialVersionUID = 1L;

  /**
   * Instance field for the Cyc server's ASCII port.
   */
  private int asciiPortOffset = STANDARD_ASCII_PORT_OFFSET;

  /**
   * Instance field for the Cyc server's binary (CFASL) port.
   */
  private int cfaslPortOffset = STANDARD_CFASL_PORT_OFFSET;

  /**
   * Instance field for the Cyc server's Subl HTTP port.
   */
  private int httpPortOffset = STANDARD_HTTP_PORT_OFFSET;

  /**
   * Instance field for the Cyc server's Servlet port.
   */
  private int servletPortOffset = STANDARD_SERVLET_PORT_OFFSET;

  /**
   * Number of simultaneous jobs this server can handle, if used in the server pool.
   */
  private Optional<Integer> concurrencyLevel;

  //====|    Construction    |====================================================================//
  
  /**
   * Creates a CycAddress for the given hostName, basePort, and concurrencyLevel.
   *
   * @param hostName
   * @param basePort
   */
  protected CycAddressImpl(String hostName, int basePort, Integer concurrencyLevel) {
    this(hostName, basePort, Optional.of(concurrencyLevel));
  }

  /**
   * Creates a CycAddress for the given hostName and basePort.
   *
   * @param hostName
   * @param basePort
   */
  protected CycAddressImpl(String hostName, int basePort) {
    this(hostName, basePort, Optional.empty());
  }

  /**
   * Creates a CycAddress for the given hostName, basePort, and Optional concurrencyLevel.
   *
   * @param hostName
   * @param basePort
   */
  protected CycAddressImpl(String hostName, int basePort, Optional<Integer> concurrencyLevel) {
    super(hostName, (basePort < 100) ? 3600 + basePort : basePort);
    this.concurrencyLevel = (concurrencyLevel != null) ? concurrencyLevel : Optional.empty();
  }

  /**
   * Copy-constructor which creates a new CycAddress from an existing CycAddress, but with a new
   * concurrencyLevel.
   *
   * @param hostName
   * @param basePort
   */
  protected CycAddressImpl(CycAddress address, Integer concurrencyLevel) {
    this(address.getHostName(), address.getBasePort(), concurrencyLevel);
  }

  /**
   * Copy-constructor which creates a new CycAddress from an existing CycAddress.
   *
   * @param hostName
   * @param basePort
   */
  protected CycAddressImpl(CycAddress address) {
    this(address.getHostName(), address.getBasePort(), address.getConcurrencyLevel());
  }

  /**
   * Creates an empty CycAddress. Mainly provided for use by serialization mechanisms.
   */
  protected CycAddressImpl() {
  }

  //====|    Public methods    |==================================================================//
  
  @Override
  public Integer getBasePort() {
    return super.getPort();
  }

  public Integer getAsciiPort() {
    return getBasePort() + asciiPortOffset;
  }

  @Override
  public Integer getCfaslPort() {
    return getBasePort() + cfaslPortOffset;
  }

  @Override
  public Integer getHttpPort() {
    return getBasePort() + httpPortOffset;
  }

  @Override
  public Integer getServletPort() {
    return getBasePort() + servletPortOffset;
  }

  @Override
  public Optional<Integer> getConcurrencyLevel() {
    return this.concurrencyLevel;
  }
  
  @Override
  public boolean isDefined() {
    return super.isDefined()
                   && (!getConcurrencyLevel().isPresent() || (getConcurrencyLevel().get() > 0));
  }
  
  @Override
  public CycAddress toBaseAddress() {
    return CycAddress.get(getHostName(), getBasePort());
  }
  
  @Override
  public String toString() {
    final String baseStr = super.toString();
    if (!super.isDefined()) {
      return baseStr;
    } else if (!isDefined()) {
      return "[Invalid concurrency level]";
    }
    return baseStr + getConcurrencyLevel().map(lvl -> ":" + lvl).orElse("");
  }

  @Override
  public boolean equals(Object obj) {
    return (super.equals(obj) && obj instanceof CycAddressImpl)
                   ? Objects.equals(this.concurrencyLevel, ((CycAddressImpl) obj).concurrencyLevel)
                   : false;
  }

  //====|    Protected    |=======================================================================//
  
  /**
   * Sets the base port.
   *
   * @param basePort
   */
  protected void setBasePort(Integer basePort) {
    super.setPort(basePort);
  }

}
