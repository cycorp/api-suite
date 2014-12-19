/*
 * A representation of a start/end pair of ints.
 */
package com.cyc.baseclient.datatype;

/*
 * #%L
 * File: Span.java
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

import com.cyc.baseclient.datatype.AbstractPair;

/**
 *
 * @author baxter
 */
public class Span extends AbstractPair {

  public Span(Object component1, Object component2) {
    throw new IllegalArgumentException("Span components must be ints.");
  }

  public Span(int start, int end) {
    super(start, end);
  }

  public int getStart() {
    return (Integer) component1;
  }

  public int getEnd() {
    return (Integer) component2;
  }
}
