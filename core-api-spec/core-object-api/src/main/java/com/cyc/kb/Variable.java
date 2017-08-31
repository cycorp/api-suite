package com.cyc.kb;

/*
 * #%L
 * File: Variable.java
 * Project: Core API Object Specification
 * %%
 * Copyright (C) 2013 - 2017 Cycorp, Inc
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
 * The interface for <code>CycL</code> variables. Variables are used in
 * quantified {@link Sentence}s and in Queries.
 *
 * @author vijay
 */
public interface Variable extends KbObject {

  /**
   * Get the name of this variable. Does not include the leading '?' character.
   *
   * @return the name.
   */
  String getName();

}
