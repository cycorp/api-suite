package com.cyc.baseclient.nl;

/*
 * #%L
 * File: Paraphrase.java
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
 * A class that bundles information about the rendering of a term in a natural language.
 *
 * @author baxter
 */
public class Paraphrase<E> implements Comparable<Paraphrase<? extends Object>> {

  /**
   * Creates a new Paraphrase with the specified term and nl.
   *
   * @param nl
   * @param term
   */
  public Paraphrase(String nl, E term) {
    this.nl = nl;
    this.term = term;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Paraphrase other = (Paraphrase) obj;
    if ((this.nl == null) ? (other.nl != null) : !this.nl.equals(other.nl)) {
      return false;
    }
    if (this.term != other.term && (this.term == null || !this.term.equals(
            other.term))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 89 * hash + (this.nl != null ? this.nl.hashCode() : 0);
    hash = 89 * hash + (this.term != null ? this.term.hashCode() : 0);
    return hash;
  }

  @Override
  public int compareTo(Paraphrase<? extends Object> o) {
    final String thisString = this.nl == null ? "" : this.nl;
    final String oString = o == null || o.nl == null ? "" : o.nl;
    return thisString.compareTo(oString);
  }

  /**
   * Returns the NL string paraphrasing the term.
   *
   * @return the NL string paraphrasing the term.
   */
  public String getString() {
    return nl;
  }

  @Override
  public String toString() {
    return getString();
  }

  /**
   * Returns the CycL term of which this is a paraphrase.
   *
   * @return the CycL term of which this is a paraphrase.
   */
  public E getCycL() {
    return term;
  }
  /**
   *
   */
  protected final String nl;
  /**
   *
   */
  protected final E term;
}
