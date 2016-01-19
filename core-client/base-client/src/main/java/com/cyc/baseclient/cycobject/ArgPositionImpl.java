package com.cyc.baseclient.cycobject;

/*
 * #%L
 * File: ArgPositionImpl.java
 * Project: Base Client
 * %%
 * Copyright (C) 2013 - 2016 Cycorp, Inc.
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

import com.cyc.kb.ArgPosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author baxter
 */
public class ArgPositionImpl extends DefaultCycObject implements ArgPosition {

  protected final List<Integer> path = new ArrayList<Integer>();

  /**
   * Create a new arg position for the specified path.
   *
   * @param path
   */
  public ArgPositionImpl(final List<Integer> path) {
    this.path.addAll(path);
  }
  /** arg position for the top-level term/formula itself. */
  public static final ArgPositionImpl TOP = new UnmodifiableArgPosition("TOP");
  // Some common arg positions, for re-use and code readability:
  public static final ArgPositionImpl ARG0 = new UnmodifiableArgPosition(0);
  public static final ArgPositionImpl ARG1 = new UnmodifiableArgPosition(1);
  public static final ArgPositionImpl ARG2 = new UnmodifiableArgPosition(2);
  public static final ArgPositionImpl ARG3 = new UnmodifiableArgPosition(3);
  public static final ArgPositionImpl ARG4 = new UnmodifiableArgPosition(4);
  public static final ArgPositionImpl ARG5 = new UnmodifiableArgPosition(5);
  public static final ArgPositionImpl ARG6 = new UnmodifiableArgPosition(6);

  /**
   * Create a new arg position for the specified argnums.
   * If argnums is empty, then returns an arg position for the top-level term/formula itself.
   *
   * @param argNums
   */
  public ArgPositionImpl(final Integer... argNums) {
    this(Arrays.asList(argNums));
  }

  /**
   * Get the list of argnums for this arg position, from top level to deepest level.
   *
   * @return the list of argnums for this arg position,
   */
  @Override
  public List<Integer> getPath() {
    return Collections.unmodifiableList(path);
  }

  /**
   * Destructively extend this arg position by another arg position.
   *
   * @param otherArgPos
   * @return this ArgPositionImpl.
   */
  @Override
  public ArgPosition extend(ArgPosition otherArgPos) {
    path.addAll(otherArgPos.getPath());
    return this;
  }

  /**
   * Destructively extend this arg position by one argnum.
   *
   * @param argnum
   * @return this ArgPositionImpl.
   */
  @Override
  public ArgPositionImpl extend(Integer argnum) {
    path.add(argnum);
    return this;
  }

  /**
   * Get the first element in this arg position's path.
   * This is the argument number of the argument of the top-level formula
   * that contains this position.
   * @return the argument number.
   */
  @Override
  public Integer first() {
    return getPath().get(0);
  }

  /**
   * Get the argnum of the designated argument in its immediate context.
   *
   * @return the argnum of the designated argument
   */
  @Override
  public Integer last() {
    final List<Integer> myPath = getPath();
    return myPath.get(myPath.size() - 1);
  }

  /**
   * Get the nesting depth of this arg position. Top-level argument positions have depth 1.
   *
   * @return the nesting depth of this arg position
   */
  @Override
  public int depth() {
    return getPath().size();
  }

  @Override
  public String toString() {
    return path.toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof ArgPositionImpl) {
      return path.equals(((ArgPositionImpl) obj).getPath());
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return path.hashCode();
  }

  /**
   * Destructively modify this ArgPositionImpl to be its parent arg position.
   *
   * @return this ArgPositionImpl.
   */
  @Override
  public ArgPosition toParent() {
    path.remove(path.size() - 1);
    return this;
  }

  @Override
  public ArgPosition deepCopy() {
    return new ArgPositionImpl(path);
  }

  /**
   * Check if this arg position is for an ancestor of another arg position.
   *
   * @param otherArgPosition
   * @return true iff his arg position is for an ancestor of otherCycArgPosition
   */
  @Override
  public boolean isPrefixOf(ArgPosition otherArgPosition) {
    if (otherArgPosition == null || otherArgPosition.depth() < depth()) {
      return false;
    } else {
      return path.equals(otherArgPosition.getPath().subList(0, path.size()));
    }
  }

  @Override
  public String stringApiValue() {
    return new CycArrayList(getPath()).stringApiValue();
  }

  /**
   * Does this arg position match candidate?
   *
   * @param candidate
   * @param matchEmpty Should we match the null arg position?
   * @return true iff this arg position matches candidate
   */
  @Override
  public boolean matchingArgPosition(ArgPosition candidate, boolean matchEmpty) {
    if (candidate == null) {
      return matchEmpty;
    } else {
      return isPrefixOf(candidate);
    }
  }

  @Override
  public int compareTo(Object o) {
    if (o instanceof ArgPositionImpl) {
      ArgPositionImpl other = (ArgPositionImpl) o;
      int i = 0;
      while (this.path.size() > i && other.path.size() > i) {
        final int result = this.path.get(i).compareTo(other.path.get(i));
        if (result != 0) {
          return result;
        }
        i++;
      }
      if (this.path.size() <= i) {
        return -1;
      } else if (other.path.size() <= i) {
        return 1;
      } else {
        return 0;
      }
    } else {
      return 0;
    }
  }

  private static class UnmodifiableArgPosition extends ArgPositionImpl {

    private final String name;

    public UnmodifiableArgPosition(final String name, final Integer... argNums) {
      super(argNums);
      this.name = name;
    }

    public UnmodifiableArgPosition(final Integer... argNums) {
      this(null, argNums);
    }

    @Override
    public String toString() {
      if (name == null) {
        return super.toString();
      } else {
        return name;
      }
    }

    @Override
    public ArgPositionImpl extend(ArgPosition otherArgPos) {
      throw new UnsupportedOperationException();
    }

    @Override
    public ArgPositionImpl extend(Integer argnum) {
      throw new UnsupportedOperationException();
    }

    @Override
    public ArgPositionImpl toParent() {
      throw new UnsupportedOperationException();
    }
  }
}
