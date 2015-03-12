package com.cyc.base.cycobject;

/*
 * #%L
 * File: Formula.java
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

import com.cyc.kb.ArgPosition;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author nwinant
 */
public interface Formula extends CycObject {

  /**
   * Destructively replace one set of terms with another.
   *
   * @param substitutions Map from terms to be replaced to their replacements
   */
  public <K extends Object, V extends Object> void applySubstitutionsDestructive(Map<K, V> substitutions);

  /**
   * Non-destructively replace one set of terms with another.
   *
   * @param substitutions Map from terms to be replaced to their replacements
   * @return A new formula, with the specified substitutions performed.
   */
  public <K extends Object, V extends Object> Formula applySubstitutionsNonDestructive(Map<K, V> substitutions);
  
  /**
   * Returns true iff obj is a top-level argument in this formula.
   *
   * @param obj
   * @return true iff obj is a top-level argument in this formula.
   * @see #treeContains(java.lang.Object)
   */
  public boolean contains(final Object obj);

  /**
   *
   * @return a deep copy of this formula.
   */
  public Formula deepCopy();

  public boolean equalsAtEL(final Object other);

  /**
   * Get all the free variables in this formula.
   *
   * @return the free variables
   */
  public List<CycVariable> findFreeVariables();

  /**
   * Get all the queryable variables in this formula.
   *
   * @return the queryable variables.
   */
  public List<CycVariable> findQueryableVariables();

  /**
   * Get the nth argument of this formula, where the predicate is arg 0.
   *
   * @param argNum
   * @return the argument.
   */
  public Object getArg(final int argNum);

  /**
   *
   * @return The arg0 (predicate or function) of this formula.
   */
  public Object getArg0();

  /**
   *
   * @return The arg1 of this formula.
   */
  public Object getArg1();

  /**
   *
   * @return The arg2 of this formula.
   */
  public Object getArg2();

  /**
   *
   * @return The arg3 of this formula.
   */
  public Object getArg3();

  /**
   * Returns a set of arg positions that describe all the locations where the
   * given term can be found in this formula.
   *
   * @param term The term to search for
   * @return The set of all arg positions where term can be found
   */
  public Set<ArgPosition> getArgPositionsForTerm(final Object term);

  /**
   *
   * @return The arguments of this formula (including the arg0, or operator)
   * as a CycList. Modifications to this list will be reflected back to the
   * original Formula.
   * @see #getArgsUnmodifiable()  for non-modifiable version.
   */
  public CycList<Object> getArgs();

  /**
   *
   * @return The arguments of this formula (including the arg0, or operator)
   * as an unmodifiable List.
   * @see #getArgs() for modifiable version.
   */
  public List<Object> getArgsUnmodifiable();

  /**
   * Get the arity of this formula, that is, the number of arguments it has.
   *
   * @return the arity of this formula.
   */
  public int getArity();

  /**
   * Returns an argument position that describes a location where the given
   * term can be found in this formula.
   *
   * @param term The term to search for
   * @return Arg position where term can be found, null if it does not occur
   * in this formula.
   */
  public ArgPosition getFirstArgPositionForTerm(final Object term);

  /**
   *
   * @return The operator (arg0) of this formula.
   */
  public Object getOperator();

  /**
   * Get all variables anywhere in this formula.
   *
   * @return the variables
   */
  public Collection<CycVariable> getReferencedVariables();

  /**
   * Returns the object from the this sentence specified by the given arg
   * position.
   *
   * @param argPosition the given arg position
   * @return the object from this sentence according to the path specified by
 the given ArgPosition.
   */
  public Object getSpecifiedObject(final ArgPosition argPosition);

  /**
   *
   * Set the arguments of this formula (including the arg0, or operator) to
   * the elements of the specified List.
   */
  public void setArgs(List<Object> newArgs);

  /**
   * Destructively modify this formula, replacing the current value at
   * argPosition with newArg.
   *
   * @param argPosition
   * @param newArg
   */
  public void setSpecifiedObject(ArgPosition argPosition, Object newArg);

  /**
   *
   * @return The arguments of this formula (including the arg0, or operator)
 as a CycList. Modifications to this list will be reflected back to the
 original Formula.
   */
  public CycList<Object> toCycList();
  
  public String toPrettyCyclifiedString(String indent);

  public String toPrettyString(String indent);

  /**
   * Gather any constants anywhere inside this formula.
   *
   * @return a set of the constants found in this formula.
   */
  @SuppressWarnings(value = "unchecked")
  public Set<CycConstant> treeConstants();

  /**
   * Does this formula contain term anywhere inside it?
   *
   * @param term
   * @return true iff this formula contains <tt>term</tt>
   */
  public boolean treeContains(Object term);

  /**
   * Gather all instances of cls anywhere inside this formula.
   *
   * @param cls
   * @return a set of instances of <tt>cls</tt> found in this formula.
   */
  @SuppressWarnings(value = "unchecked")
  public <T> Set<T> treeGather(Class<T> cls);

  /**
   * Returns a list representation of the Base Client formula and converts any
   * embedded formulas as well.
   *
   * @return a <tt>CycList</tt> representation of the formula.
   */
  CycList toDeepCycList();
  
}
