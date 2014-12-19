package com.cyc.baseclient.util.query;

/*
 * #%L
 * File: QueryChangeEvent.java
 * Project: Base Client
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

import java.util.EventObject;

/**
 * @version $Id: QueryChangeEvent.java 155483 2014-12-10 21:56:51Z nwinant $
 * @author  mreimers
 */
public class QueryChangeEvent extends EventObject {
  private QueryEventReason reason;
  
  public static final QueryEventReason SPECIFICATION_CHANGED = 
    new QueryChangeEvent.QueryEventReason("SPECIFICATION_CHANGED");

  public static final QueryEventReason DATA_AVAILABLE = 
    new QueryChangeEvent.QueryEventReason("DATA_AVAILABLE");
  
  public static final QueryEventReason STATUS_CHANGED = 
    new QueryChangeEvent.QueryEventReason("STATUS_CHANGED");
  
  public QueryChangeEvent(Query source, QueryEventReason reason) {
    super(source);
    this.reason = reason;
  }
  
  public QueryEventReason getReason() { return this.reason; }
  public Query getQuery() { return (Query)getSource(); }
  
  public static class QueryEventReason /*extends com.cyc.common.util.Enumeration*/ {
    
    //// Constructors
    
    /** Creates a new instance of GKEChangeReason.
     * @param name the name of the reason...must be unique for this enumeration.
     */
    public QueryEventReason(String name) { /*super(name);*/ }
    
  }
  //// Public Area
  
  
}
