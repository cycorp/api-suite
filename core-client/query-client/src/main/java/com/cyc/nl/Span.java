/*
 */
package com.cyc.nl;

/*
 * #%L
 * File: Span.java
 * Project: Query Client
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
 * A class to represent a sub-sequence location specified by offset and end indices.
 * <code>Span</code>s are used to represent the locations of terms and queries in natural language Strings.
 * @author baxter
 */
public class Span {
  final private int offset;
  final private int end;

  @Override
  public String toString() {
    return "Span{" + "offset=" + offset + ", end=" + end + '}';
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Span other = (Span) obj;
    if (this.offset != other.offset) {
      return false;
    }
    if (this.end != other.end) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 17 * hash + this.offset;
    hash = 17 * hash + this.end;
    return hash;
  }

  /**
   * Get the end index of this span.
   * @return the end index
   */
  public int getEnd() {
    return end;
  }

  /**
   * Get the offset of this span.
   * @return the offset.
   */
  public int getOffset() {
    return offset;
  }

  /**
   *
   * @param offset
   * @param end
   */
  public Span(int offset, int end) {
    this.offset = offset;
    this.end = end;
  }
  
}
