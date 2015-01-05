package com.cyc.base.inference;

/*
 * #%L
 * File: InferenceSuspendReason.java
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

/**
 *
 * @author nwinant
 */
public interface InferenceSuspendReason {

  /** Can an inference suspended for this reason be reset? */
  boolean allowReset();

  /** Can an inference suspended for this reason be stopped? */
  boolean allowStop();

  /** A string to describe the status of the inference. */
  String getInferenceStatusString();

  /** Can an inference suspended for this reason be continued? */
  boolean isContinuable();

  /** Was inference suspended because of an error? */
  boolean isError();
  
}
