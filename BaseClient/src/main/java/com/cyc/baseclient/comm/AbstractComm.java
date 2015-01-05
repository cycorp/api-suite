package com.cyc.baseclient.comm;

/*
 * #%L
 * File: AbstractComm.java
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

//// Internal Imports

import com.cyc.base.conn.CycConnectionInterface;


//// External Imports

/** 
 * <P>AbstractComm is designed to provide a convient location for methods that
 * can be used across all Comm implementations.
 *
 * @author tbrussea, May 7, 2013, 12:19:20 PM
 * @version $Id: AbstractComm.java 155483 2014-12-10 21:56:51Z nwinant $
 */
public abstract class AbstractComm implements Comm {

  //// Constructors

  /** Creates a new instance of AbstractComm. */
  public AbstractComm() {}

  //// Public Area
  
  @Override
  public synchronized CycConnectionInterface getCycConnection() {
    return cycConnection;
  }
  
  @Override
  public synchronized void setCycConnection(CycConnectionInterface conn){
    if (conn == this.cycConnection) {
      return;
    }
    this.cycConnection = conn;
  }

  //// Protected Area

  //// Private Area

  //// Internal Rep
  
  private CycConnectionInterface cycConnection;

}
