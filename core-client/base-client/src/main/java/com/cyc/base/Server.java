package com.cyc.base;

/*
 * #%L
 * File: Server.java
 * Project: Base Client
 * %%
 * Copyright (C) 2013 - 2016 Cycorp, Inc.
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
 *
 * @author nwinant
 */
public class Server implements Serializable, Comparable<Server> {
  
  public Server(String hostName, Integer port) {
    setHostName(hostName);
    setPort(port);
  }
  
  protected Server() {
    // Used by GWT's serialization mechanism
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
  
  public static Server fromString(String string) {
    String tokens[] = string.split(":");
    if (tokens.length == 1) {
      return new Server(tokens[0], null);
    }
    return new Server(tokens[0], Integer.valueOf(tokens[1]));
  }
  
  
  // Public
  
  public void setHostName(String hostName) {
    this.hostName = hostName;
  }

  public String getHostName() {
    return hostName;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  public Integer getPort() {
    return port;
  }
  
  public boolean isDefined() {
    return getHostName() != null;
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
    return hashCode() == ((Server) obj).hashCode();
  }
  
  @Override
  public int compareTo(Server o) {
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
  
  
  // Internal rep
  
  private static final long serialVersionUID = 1L;
  
  public static final String VALID_IP_ADDR_REGEX = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";
  public static final String VALID_HOST_NAME_REGEX = "^(([a-zA-Z]|[a-zA-Z][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)*([A-Za-z]|[A-Za-z][A-Za-z0-9\\-]*[A-Za-z0-9])$";
  
  private String hostName = null;
  private Integer port = null;
}
