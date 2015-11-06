package com.cyc.kb;

import java.util.Date;

/*
 * #%L
 * File: KBTerm.java
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
 * The top-level interface for most kinds of {@link KBObject}s that are not
 * {@link Sentence}s.
 *
 * @author vijay
 */
public interface KBTerm extends KBObject {

  /**
   * Determine whether <code>this</code> is provably not an instance of
   * <code>col</code>. This means that there is no way to convert
   * <code>this</code> into an instance of <code>col</code> without editing or
   * removing current KB assertions.
   *
   * @param col the collection which <code>this</code> is to be tested against
   * @param ctx the context in which the semantic test should be performed
   *
   * @return whether <code>this</code> is provably not an instance of
   * <code>col</code>
   */
  public boolean provablyNotInstanceOf(KBCollection col, Context ctx);

  /**
   * Determine whether <code>this</code> is provably not an instance of
   * <code>col</code>. This means that there is no way to convert
   * <code>this</code> into an instance of the collection without editing or
   * removing current KB assertions.
   *
   * @param colStr the string representing the collection which
   * <code>this</code> is to be tested against
   * @param ctxStr the context in which the semantic test should be performed
   *
   * @return whether <code>this</code> is provably not an instance
   * <code>col</code>
   */
  public boolean provablyNotInstanceOf(String colStr, String ctxStr);

  /**
   * Return the #$Cyclist (not necessarily #$HumanCyclist) who created this term. 
   * If not found or if there is an exception, return null.
   * 
   * @return the cyclist who created the term
   */
  public KBIndividual getCreator();
  
  /**
   * Return the date and time at which this term was created. If not found or if
   * there is an exception, return null.
   * 
   * @return the date and time at which the term was created
   */
  public Date getCreationDate();
}
