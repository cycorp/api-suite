package com.cyc.baseclient.util.query;

/*
 * #%L
 * File: Query.java
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

/**
 * @version $Id: Query.java 155483 2014-12-10 21:56:51Z nwinant $
 * @author  mreimers
 */
public interface Query {

  public Object getId();
  public QuerySpecification getOriginalQuerySpecification();
  public QuerySpecification getQuerySpecification();
  public void setQuerySpecification(QuerySpecification querySpecification);
  
  //public Query makeQuery(QuerySpecification querySpecification);
  
  /**
   * Revert this Query back to the original QuerySpecification.
   */
  public void revertQuerySpecification();
  
  public QueryStatus getQueryStatus();
  public void setQueryStatus(QueryStatus queryStatus);
  
  public QueryResultSet getQueryResultSet();
  public void setQueryResultSet(QueryResultSet resultSet);
  
  //public void notifyDataAvailable();
  //public void notifySpecificationChanged();
  //public void notifyStatusChanged();
  
  public void startQuery();
  public void stopQuery();
  public void pauseQuery();
  public void continueQuery();
  
  public void addQueryListener(QueryListener listener);
  public void removeQueryListener(QueryListener listener);
}
