package com.cyc.query;

/*
 * #%L
 * File: InferenceStatus.java
 * Project: Core API Specification
 * %%
 * Copyright (C) 2013 - 2015 Cycorp, Inc
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
 *
 * @author nwinant
 */
//@todo this doesn't really implement parameter-value, but it's something very similar
public enum InferenceStatus implements InferenceParameterValue {

  /**
   * newly created, attached to a problem store, query & mt unspecified. *
   */
  NEW,
  /**
   * query & mt specified, resource constraints still unspecified. *
   */
  PREPARED,
  /**
   * resource constraints specified, ready to start running under those constraints      
   */
  READY,
  /**
   * The inference has not yet been started.      
   */
  NOT_STARTED,
  /**
   * The inference has been told to start, but may not actually be running yet.      
   */
  STARTED,
  /** 
   *    currently in the act of performing inference
   */ 
  RUNNING,
  /**
   * 
   */
   SUSPENDED,
   /**
    * explicitly destroyed; answers & problems cannot be accessed
    */
   DEAD,
   /**
    * The query itself is a tautology, and therefore no inference will be performed on it.
    */
   TAUTOLOGY,
   /**
    * The query contains an internal contradiction, and cannot be run.
    */
   CONTRADICTION,
   /**
    * The query is syntactically ill-formed and cannot be run.
    */
   ILL_FORMED
   ;

  
  /**
   * Does this status indicate that the inference is done? It may be
   * continuable, but no further work will be performed on it until
   * instructed.
   *
   * @return true iff this status indicates that the inference is done.
   */
  boolean indicatesDone() {
    throw new UnsupportedOperationException("Not implemented yet.");
  }
  
}
