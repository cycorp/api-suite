package com.cyc.kb;

/*
 * #%L
 * File: ArgPosition.java
 * Project: Core API Object Specification
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

import java.util.List;

/**
 * @author nwinant
 */
public interface ArgPosition {

  public ArgPosition deepCopy();

  /**
   * Get the nesting depth of this arg position. Top-level argument positions have depth 1.
   *
   * @return the nesting depth of this arg position
   */
  public int depth();

  /**
   * Destructively extend this arg position by another arg position.
   *
   * @return this ArgPosition.
   */
  public ArgPosition extend(ArgPosition otherArgPos);

  /**
   * Destructively extend this arg position by one argnum.
   *
   * @return this ArgPosition.
   */
  public ArgPosition extend(Integer argnum);

  /**
   * Get the first element in this arg position's path. This is the argument number of the argument
   * of the top-level formula that contains this position.
   *
   * @return the argument number.
   */
  public Integer first();

  /**
   * Get the list of argnums for this arg position, from top level to deepest level.
   *
   * @return the list of argnums for this arg position,
   */
  public List<Integer> getPath();

  /**
   * Check if this arg position is for an ancestor of another arg position.
   *
   * @return true iff his arg position is for an ancestor of otherArgPosition
   */
  public boolean isPrefixOf(ArgPosition otherArgPositionI);

  /**
   * Get the argnum of the designated argument in its immediate context.
   *
   * @return the argnum of the designated argument
   */
  public Integer last();

  /**
   * Does this arg position match candidate?
   *
   * @param matchEmpty Should we match the null arg position?
   * @return true iff this arg position matches candidate
   */
  public boolean matchingArgPosition(ArgPosition candidate, boolean matchEmpty);

  /**
   * Destructively modify this ArgPosition to be its parent arg position.
   *
   * @return this ArgPosition.
   */
  public ArgPosition toParent();

}
