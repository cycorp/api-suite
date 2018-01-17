package com.cyc.query;

import com.cyc.query.parameters.InferenceParameterValue;

/*
 * #%L
 * File: InferenceStatus.java
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


/**
 *
 * @author nwinant
 */
//@todo this doesn't really implement parameter-value, but it's something very similar
public enum InferenceStatus implements InferenceParameterValue {

  /**
   * Newly created, attached to a problem store, query & mt unspecified.
   */
  NEW,
  
  /**
   * Query & mt specified, resource constraints still unspecified.
   */
  PREPARED,
  
  /**
   * Resource constraints specified, ready to start running under those constraints.
   */
  READY,
  
  /**
   * The inference has not yet been run.      
   */
  NOT_STARTED,
  
  /**
   * The inference has been told to start, but may not actually be running yet.      
   */
  STARTED,
  
  /** 
   * Currently in the act of performing inference.
   */ 
  RUNNING,
  
  /**
   * Inference has been suspended, but may be possible to continue.
   */
  SUSPENDED,
  
  /**
   * Inference was explicitly destroyed; answers & problems cannot be accessed.
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
  ILL_FORMED;
  
  
  // Public
  /**
   * Does this status indicates that an inference may be currently available for this query?
   *
   * @return true if has been run, or is currently running, and the inference has not been
   * destroyed.
   */
  public boolean indicatesInferenceExists() {
    return this.equals(STARTED)
            //|| this.equals(NEW)
            || this.equals(RUNNING)
            || this.equals(SUSPENDED);
  }

  /**
   * Does this status indicate that the inference is done? It may be continuable, but no further
   * work will be performed on it until instructed.
   *
   * @return true iff this status indicates that the inference is done.
   */
  boolean indicatesDone() {
    return this.equals(SUSPENDED)
            || this.equals(DEAD);
  }

  /**
   * Does this status indicate that there is a problem with the query, such that an inference cannot
   * be run?
   *
   * @return true iff this status indicates that the query cannot be run.
   */
  boolean indicatesQueryError() {
    return this.equals(TAUTOLOGY)
            || this.equals(CONTRADICTION)
            || this.equals(ILL_FORMED);
  }

}
