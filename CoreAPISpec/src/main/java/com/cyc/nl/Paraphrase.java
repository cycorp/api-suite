package com.cyc.nl;

/*
 * #%L
 * File: Paraphrase.java
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
 * A class that bundles information about the rendering of a term in a natural language.
 *
 * @author baxter
 * @param <E>
 */
public interface Paraphrase<E> extends Comparable<Paraphrase<? extends Object>> {


  @Override
  public boolean equals(Object obj);

  @Override
  public int hashCode();

  @Override
  public int compareTo(Paraphrase<? extends Object> o);

  /**
   * Returns the NL string paraphrasing the term.
   *
   * @return the NL string paraphrasing the term.
   */
  public String getString();

  @Override
  public String toString();

  /**
   * Returns the CycL term of which this is a paraphrase.
   *
   * @return the CycL term of which this is a paraphrase.
   */
  public E getCycL();
  
}
