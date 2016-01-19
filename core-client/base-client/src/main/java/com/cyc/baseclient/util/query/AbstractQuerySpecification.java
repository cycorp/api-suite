package com.cyc.baseclient.util.query;

/*
 * #%L
 * File: AbstractQuerySpecification.java
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

//// Internal Imports

//// External Imports
import java.util.*;

/**
 * <P>AbstractQuerySpecification is designed to...
 *
 * <P>Copyright (c) 2004 - 2006 Cycorp, Inc.  All rights reserved.
 * <BR>This software is the proprietary information of Cycorp, Inc.
 * <P>Use is subject to license terms.
 *
 * @author mreimers
 * @date August 11, 2004, 1:56 PM
 * @version $Id: AbstractQuerySpecification.java 163356 2016-01-04 20:55:47Z nwinant $
 */
public class AbstractQuerySpecification implements QuerySpecification {
  protected Set constraints;
  protected Object id;
  protected Object question;
  
  //// Constructors
  
  /** Creates a new instance of AbstractQuerySpecification. */
  public AbstractQuerySpecification(Object question, Set constraints) {
    this.question = question;
    this.constraints = constraints;
  }
  
  public Set getConstraints() {
    return constraints;
  }
  
  public java.util.Set getFilteredConstraints(Class constraintType) {
    HashSet result = new HashSet();
    return result;
//    if(constraints == null)
//      return result;
//    Iterator it = constraints.iterator();
  }
  
  public String getGloss() {
    return "";
  }
  
  public Object getQuestion() {
    return null;
  }
  
  public Object clone() {
    return null;
  }
  
  public Object getId() { return this.id; }
  public void setId(Object id) {
    this.id = id;
  }
  
  public void addQueryListener(QueryListener listener) {
    
  }
  
  public void removeQueryListener(QueryListener listener) {
    
  }
  
  public void setQueryResultSet(QueryResultSet resultSet) {
    
  }
  
  public QueryResultSet getQueryResultSet() {
    return null;
  }
  
  public void setQueryStatus(QueryStatus queryStatus) {
    
  }
  
  public QueryStatus getQueryStatus() {
    return null;
  }
  
  public void revertQuerySpecification() {
    
  }
  
  public void setQuerySpecification(QuerySpecification querySpecification) {
    
  }
  
  public QuerySpecification getQuerySpecification() {
    return null;
  }
  
  public QuerySpecification getOriginalQuerySpecification() {
    return null;
  }
  //// Public Area
  
  //// Protected Area
  
  //// Private Area
  
  //// Internal Rep
  
  //// Main
  
}
