package com.cyc.baseclient.cycobject;

/*
 * #%L
 * File: FormulaImpl.java
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
//// External Imports
import com.cyc.base.exception.BaseClientRuntimeException;
import com.cyc.kb.ArgPosition;
import com.cyc.base.cycobject.CycConstant;
import com.cyc.base.cycobject.CycList;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.NonAtomicTerm;
import com.cyc.base.cycobject.DenotationalTerm;
import com.cyc.base.cycobject.Formula;
import com.cyc.base.cycobject.CycVariable;
import com.cyc.base.cycobject.Nart;
import java.io.IOException;
import java.util.*;
import com.cyc.baseclient.datatype.StringUtils;
import com.cyc.baseclient.xml.XmlWriter;

/**
 * A class representing CycL formulas.
 * <P>
 * Cyc formulas may be either sentential (see {@link CycFormulaSentence}) or
 * denotational (see {@link NautImpl}).
 * <P>
 * A formula consists of an operator, which must be a {@link CycObject} and a
 * sequence of arguments, which may be either {@link CycObject}s or certain
 * primitives, notably booleans, strings, and numbers. See
 * {@link DefaultCycObject#isCycLObject(java.lang.Object)} for syntactically
 * valid argument types.
 *
 * @author baxter
 */
public class FormulaImpl implements Formula {

  /**
   * Create and return a new CycFormula whose arguments are terms.
   *
   * <P>
   * CycArrayList arguments will be converted to CycNauts or CycSentences.
   * <P>
   * See {@link DefaultCycObject#isCycLObject(java.lang.Object)} for
   * syntactically valid argument types.
   *
   * @param terms
   */
  public FormulaImpl(Iterable<? extends Object> terms) {
    for (final Object arg : terms) {
      addArg(arg);
    }
  }

  /**
   * Create and return a new CycFormula with the specific functor and args.
   * <P>
   * CycArrayList arguments will be converted to CycNauts or CycSentences.
   * <P>
   * See {@link DefaultCycObject#isCycLObject(java.lang.Object)} for
   * syntactically valid argument types.
   *
   * @param functor
   * @param args
   */
  public FormulaImpl(final DenotationalTerm functor, final Object... args) {
    this();
    addArg(functor);
    for (final Object arg : args) {
      addArg(arg);
    }
  }

  /**
   * Build a new FormulaImpl from terms.
   * <P>
   * @see DefaultCycObject#isCycLObject(java.lang.Object) for syntactically
   * valid argument types.
   *
   * @param terms
   * @return the new formula
   */
  public static Formula makeCycFormula(Object... terms) {
    final FormulaImpl newFormula = new FormulaImpl();
    for (final Object arg : terms) {
      newFormula.addArg(arg);
    }
    return newFormula;
    //return new FormulaImpl(CycArrayList.makeCycList(terms));
  }

  protected FormulaImpl() {
  }

  /**
   * Add arg to the end of this formula.
   * <P>
   * See {@link DefaultCycObject#isCycLObject(java.lang.Object)} for
   * syntactically valid argument types.
   *
   * @param arg
   */
  protected final void addArg(final Object arg) {
    if (arg instanceof Iterable) {
      Object betterArg = NautImpl.convertIfPromising(arg);
      if (!(betterArg instanceof NautImpl)) {
        betterArg = new CycFormulaSentence((Iterable) arg);
      }
      addArgLow(betterArg);
    } else if (arg instanceof CycSymbolImpl) {
      Object betterArg = CycVariableImpl.convertIfPromising(arg);
      addArgLow(betterArg);
    } else {
      addArgLow(arg);
    }
  }

  private void addArgLow(final Object arg) {
    args.add(arg);
  }

  @Override
  public String toString() {
    return args.toString();
  }

  @Override
  public Object cycListApiValue() {
    return args.cycListApiValue(true);
  }

  @Override
  public String cyclify() {
    return args.cyclify();
  }

  @Override
  public String cyclifyWithEscapeChars() {
    return args.cyclifyWithEscapeChars();
  }

  /**
   * Get the nth argument of this formula, where the predicate is arg 0.
   *
   * @param argNum
   * @return the argument.
   */
  public Object getArg(final int argNum) {
    return args.get(argNum);
  }

  /**
   *
   * @return a deep copy of this formula.
   */
  public FormulaImpl deepCopy() {
    final List<Object> newArgs = new ArrayList<Object>(args.size());
    for (final Object arg : args) {
      if (arg instanceof FormulaImpl) {
        newArgs.add(((FormulaImpl) arg).deepCopy());
      } else {
        newArgs.add(arg);
      }
    }
    return new FormulaImpl(newArgs);
  }

  /**
   *
   * @return The arguments of this formula (including the arg0, or operator) as
   * a CycArrayList. Modifications to this list will be reflected back to the
   * original FormulaImpl.
   * @see #getArgsUnmodifiable() for non-modifiable version.
   */
  public CycArrayList<Object> getArgs() {
    return args;
  }

  /**
   *
   * @return The arguments of this formula (including the arg0, or operator) as
   * a CycArrayList. Modifications to this list will be reflected back to the
   * original FormulaImpl.
   */
  public CycArrayList<Object> toCycList() {
    return getArgs();
  }

  /**
   *
   * Set the arguments of this formula (including the arg0, or operator) to the
   * elements of the specified List.
   */
  public void setArgs(List<Object> newArgs) {
    args.clear();
    args.addAll(newArgs);
  }

  /**
   *
   * @return The arguments of this formula (including the arg0, or operator) as
   * an unmodifiable List.
   * @see #getArgs() for modifiable version.
   */
  public List<Object> getArgsUnmodifiable() {
    return Collections.unmodifiableList(args);
  }

  /**
   * Get the arity of this formula, that is, the number of arguments it has.
   *
   * @return the arity of this formula.
   */
  public int getArity() {
    return args.size() - 1;
  }

  /**
   *
   * @return The operator (arg0) of this formula.
   */
  public Object getOperator() {
    return args.get(0);
  }

  /**
   *
   * @return The arg0 (predicate or function) of this formula.
   */
  public Object getArg0() {
    return args.get(0);
  }

  /**
   *
   * @return The arg1 of this formula.
   */
  public Object getArg1() {
    return args.get(1);
  }

  /**
   *
   * @return The arg2 of this formula.
   */
  public Object getArg2() {
    return args.get(2);
  }

  /**
   *
   * @return The arg3 of this formula.
   */
  public Object getArg3() {
    return args.get(3);
  }

  @Override
  public List getReferencedConstants() {
    return args.getReferencedConstants();
  }

  /**
   * Get all variables anywhere in this formula.
   *
   * @return the variables
   */
  public Collection<CycVariable> getReferencedVariables() {
    return treeGather(CycVariable.class);
  }

  /**
   * Returns true iff obj is a top-level argument in this formula.
   *
   * @param obj
   * @return true iff obj is a top-level argument in this formula.
   * @see FormulaImpl#treeContains(java.lang.Object)
   */
  public boolean contains(final Object obj) {
    return args.contains(obj);
  }

  /**
   * Does this formula contain term anywhere inside it?
   *
   * @param term
   * @return true iff this formula contains <tt>term</tt>
   */
  public boolean treeContains(Object term) {
    for (final TreeWalker tw = new TreeWalker(); tw.hasNext();) {
      if (tw.next().equals(term)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Gather all instances of cls anywhere inside this formula.
   *
   * @param cls
   * @return a set of instances of <tt>cls</tt> found in this formula.
   */
  @SuppressWarnings("unchecked")
  public <T> Set<T> treeGather(Class<T> cls) {
    final Set<T> found = new HashSet<T>();
    for (final TreeWalker tw = new TreeWalker(); tw.hasNext();) {
      final Object arg = tw.next();
      if (cls.isInstance(arg)) {
        found.add((T) arg);
      }
    }
    return found;
  }

  /**
   * Returns a set of arg positions that describe all the locations where the
   * given term can be found in this formula.
   *
   * @param term The term to search for
   * @return The set of all arg positions where term can be found
   */
  @Override
  public Set<ArgPosition> getArgPositionsForTerm(final Object term) {
    final Set<ArgPosition> argPositions = new HashSet<ArgPosition>();
    for (final ArgPositionTrackingTreeWalker tw = new ArgPositionTrackingTreeWalker(); tw.hasNext();) {
      final Object arg = tw.next();
      if (termMatchesForArgPositionSearch(term, arg)) {
        argPositions.add(tw.getCurrentArgPosition());
      }
    }
    return argPositions;
  }

  static private boolean termMatchesForArgPositionSearch(final Object term, final Object arg) {
    return term.equals(arg)
            || (term instanceof Nart && termMatchesForArgPositionSearch(((Nart) term).getFormula(), arg))
            || (arg instanceof Nart && termMatchesForArgPositionSearch(term, ((Nart) arg).getFormula()));
  }

  /**
   * Returns an argument position that describes a location where the given term
   * can be found in this formula.
   *
   * @param term The term to search for
   * @return Arg position where term can be found, null if it does not occur in
   * this formula.
   */
  @Override
  public ArgPosition getFirstArgPositionForTerm(final Object term) {
    for (final ArgPositionTrackingTreeWalker tw = new ArgPositionTrackingTreeWalker(); tw.hasNext();) {
      final Object arg = tw.next();
      if (termMatchesForArgPositionSearch(term, arg)) {
        return tw.getCurrentArgPosition();
      }
    }
    return null;
  }

  /**
   * Gather any constants anywhere inside this formula.
   *
   * @return a set of the constants found in this formula.
   */
  @SuppressWarnings("unchecked")
  public Set<CycConstant> treeConstants() {
    final Set<CycConstant> constants = new HashSet<CycConstant>();
    constants.addAll(treeGather(CycConstant.class));
    return constants;
  }

  /**
   * Non-destructively replace one set of terms with another.
   *
   * @param substitutions Map from terms to be replaced to their replacements
   * @return A new formula, with the specified substitutions performed.
   */
  public <K extends Object, V extends Object> FormulaImpl applySubstitutionsNonDestructive(
          Map<K, V> substitutions) {
    final FormulaImpl newFormula = deepCopy();
    newFormula.applySubstitutionsDestructive(substitutions);
    return newFormula;
  }

  /**
   * Destructively replace one set of terms with another.
   *
   * @param substitutions Map from terms to be replaced to their replacements
   */
  public <K extends Object, V extends Object> void applySubstitutionsDestructive(
          Map<K, V> substitutions) {
    for (final Map.Entry<K, V> entry : substitutions.entrySet()) {
      for (final ArgPosition argPos : getArgPositionsForTerm(entry.getKey())) {
        final V replacement = entry.getValue();
        if (!DefaultCycObject.isCycLObject(replacement)) {
          throw new IllegalArgumentException(
                  replacement + " is not a CycL object.");
        }
        setSpecifiedObject(argPos, replacement);
      }
    }
  }

  /**
   * Returns a list representation of the Base Client formula and converts any
   * embedded formulas and non-atomic terms as well.
   *
   * @return a <tt>CycList</tt> representation of the formula.
   */
  @Override
  public CycList toDeepCycList() {
    CycArrayList cycList = new CycArrayList();
    for (final Object argument : this.getArgsUnmodifiable()) {
      if (argument instanceof Formula) {
        cycList.add(((Formula) argument).toDeepCycList());
      } else if (argument instanceof NonAtomicTerm) {
        cycList.add(((NonAtomicTerm) argument).toDeepCycList());
      } else {
        cycList.add(argument);
      }
    }
    return cycList;
  }

  /**
   * Iterator over the non-formula arguments in this formula, descending into
   * formula arguments.
   *
   */
  public class TreeWalker implements Iterator<Object> {

    private final Stack<Object> stack = new Stack<Object>();

    /**
     * Construct a new tree walker for this formula.
     */
    public TreeWalker() {
      initStack();
    }

    protected void initStack() {
      stack.push(FormulaImpl.this);
    }

    @Override
    public boolean hasNext() {
      return !stack.isEmpty();
    }

    @Override
    public Object next() {
      final Object item = popStack();
      if (item instanceof FormulaImpl) {
        for (final Object arg : ((FormulaImpl) item).args) {
          pushStack(arg);
        }
      }
      return item;
    }

    protected Object popStack() {
      return stack.pop();
    }

    protected Object pushStack(final Object arg) {
      return stack.push(arg);
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException("Not supported.");
    }
  }

  /**
   * A tree walker that keeps track of argument position.
   */
  public class ArgPositionTrackingTreeWalker extends TreeWalker {

    private ArgPosition currentArgPosition = null;

    /**
     *
     * @return the current argument position.
     */
    public ArgPosition getCurrentArgPosition() {
      return currentArgPosition;
    }

    @Override
    protected void initStack() {
      pushStack(new ObjectWithArgPosition(FormulaImpl.this, ArgPositionImpl.TOP));
    }

    @Override
    protected ObjectWithArgPosition popStack() {
      return (ObjectWithArgPosition) super.popStack();
    }

    @Override
    protected Object pushStack(Object arg) {
      return super.pushStack((ObjectWithArgPosition) arg);
    }

    @Override
    public Object next() {
      final ObjectWithArgPosition itemPlus = popStack();
      final Object item = itemPlus.object;
      currentArgPosition = itemPlus.argPosition;
      if (item instanceof FormulaImpl) {
        final FormulaImpl formula = (FormulaImpl) item;
        for (int argNum = 0; argNum <= formula.getArity(); argNum++) {
          final Object arg = formula.getArg(argNum);
          final ArgPosition newArgPos = currentArgPosition.deepCopy();
          newArgPos.extend(argNum);
          pushStack(new ObjectWithArgPosition(arg, newArgPos));
        }
      }
      return item;
    }

    private class ObjectWithArgPosition {

      private final Object object;
      private final ArgPosition argPosition;

      private ObjectWithArgPosition(final Object object,
              final ArgPosition argPosition) {
        this.object = object;
        this.argPosition = argPosition;
      }
    }
  }

  /**
   * Get all the free variables in this formula.
   *
   * @return the free variables
   */
  public List<CycVariable> findFreeVariables() {
    return findFreeVariables(new ArrayList<CycVariable>(),
            new HashSet<CycVariable>(), false);
  }

  /**
   * Get all the queryable variables in this formula.
   *
   * @return the queryable variables.
   */
  public List<CycVariable> findQueryableVariables() {
    return findFreeVariables(new ArrayList<CycVariable>(),
            new HashSet<CycVariable>(), true);
  }

  private List<CycVariable> findFreeVariables(List<CycVariable> freeVarsSoFar,
          Set<CycVariable> boundVars, boolean includeQueryable) {
    if (this instanceof CycFormulaSentence
            && (((CycFormulaSentence) this).isExistential() || ((CycFormulaSentence) this).isUniversal())) {
      final CycVariableImpl boundVar = (CycVariableImpl) getArg1();
      boundVars.add(boundVar);
      ((FormulaImpl) getArg2()).findFreeVariables(freeVarsSoFar, boundVars,
              includeQueryable);
      boundVars.remove(boundVar);
    } else if (includeQueryable
            && this instanceof CycFormulaSentence
            && (((CycFormulaSentence) this).isConditionalSentence())) {
      List<CycVariable> antecedentVars
              = ((FormulaImpl) getArg1()).findFreeVariables(
                      new ArrayList<CycVariable>(), boundVars, includeQueryable);
      for (final CycVariable antecedentVar : antecedentVars) {
        boundVars.add(antecedentVar);
        freeVarsSoFar.remove(antecedentVar);
      }
      ((FormulaImpl) getArg2()).findFreeVariables(freeVarsSoFar, boundVars,
              includeQueryable);
      for (final CycVariable antecedentVar : antecedentVars) {
        boundVars.remove(antecedentVar);
      }
    } else {
      for (final Object arg : getArgsUnmodifiable()) {
        if (arg instanceof CycVariableImpl) {
          final CycVariableImpl var = (CycVariableImpl) arg;
          if (!boundVars.contains(var) && !freeVarsSoFar.contains(var)) {
            freeVarsSoFar.add(var);
          }
        } else if (arg instanceof FormulaImpl) {
          ((FormulaImpl) arg).findFreeVariables(freeVarsSoFar, boundVars,
                  includeQueryable);
        }
      }
    }
    return freeVarsSoFar;
  }

  /**
   * Returns the object from the this sentence specified by the given arg
   * position.
   *
   * @param argPosition the given arg position
   * @return the object from this sentence according to the path specified by
   * the given ArgPositionImpl.
   */
  public Object getSpecifiedObject(final ArgPosition argPosition) {
    if (argPosition.depth() == 0) {
      return this;
    }
    Object answer = this;
    final List<Integer> tempPath = new ArrayList(argPosition.getPath());
    int index = 0;
    try {
      while (!tempPath.isEmpty()) {
        index = tempPath.get(0);
        if (answer instanceof NonAtomicTerm) {
          if (index == 0) {
            answer = ((NonAtomicTerm) answer).getFunctor();
          } else {
            answer = ((NonAtomicTerm) answer).getArgument(index);
          }
        } else if (answer instanceof FormulaImpl) {
          answer = ((FormulaImpl) answer).getArg(index);
        } else {
          answer = ((CycArrayList) answer).get(index);
        }
        tempPath.remove(0);
      }
      return answer;
    } catch (Exception e) {
      throw new BaseClientRuntimeException("Can\'t get object specified by \'" + argPosition + "\' in forumla: \'" + this + "\'.  answer: " + answer + " index: " + index + "\n" + StringUtils.getStringForException(
              e));
    }
  }

  /**
   * Destructively modify this formula, replacing the current value at
   * argPosition with newArg.
   *
   * @param argPosition
   * @param newArg
   */
  public void setSpecifiedObject(ArgPosition argPosition, Object newArg) {
    if (ArgPositionImpl.TOP.equals(argPosition)) {
      setArgs(((FormulaImpl) newArg).getArgs());
      return;
    }
    Object context = this;
    for (final Iterator<Integer> it = argPosition.getPath().iterator(); it.hasNext();) {
      final int index = it.next();
      if (!it.hasNext()) {
        setSpecifiedObject(context, index, newArg);
      } else if (context instanceof FormulaImpl) {
        context = ((FormulaImpl) context).getArg(index);
      } else if (context instanceof List) {
        context = ((List) context).get(index);
      } else {
        throw new BaseClientRuntimeException(
                "Can't find position " + argPosition + " in " + this);
      }
    }
  }

  private void setSpecifiedObject(Object context, int index, Object newArg) {
    if (context instanceof FormulaImpl) {
      ((FormulaImpl) context).args.set(index, newArg);
    } else if (context instanceof Nart) {
      ((Nart) context).setArgument(index, newArg);
    } else if (context instanceof List) {
      ((List) context).set(index, newArg);
    } else {
      throw new BaseClientRuntimeException("Can't set " + index + " of " + context);
    }
  }

  @Override
  public String stringApiValue() {
    return args.stringApiValue();
  }

  public String toPrettyCyclifiedString(String indent) {
    return args.toPrettyCyclifiedString(indent);
  }

  public String toPrettyString(String indent) {
    return args.toPrettyString(indent);
  }

  @Deprecated
  public String toHTMLPrettyString(String indent) {
    return args.toHTMLPrettyString(indent);
  }

  @Deprecated
  public void toXML(XmlWriter xmlWriter, int indent, boolean relative) throws IOException {
    args.toXML(xmlWriter, indent, relative);
  }

  @Override
  public int compareTo(Object o) {
    if (o instanceof FormulaImpl) {
      return args.compareTo(((FormulaImpl) o).args);
    } else {
      return 0;
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final FormulaImpl other = (FormulaImpl) obj;
    if (this.args != other.args && (this.args == null || !this.args.equals(
            other.args))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (this.args != null ? this.args.hashCode() : 0);
    return hash;
  }

  public boolean equalsAtEL(final Object other) {
    return other instanceof FormulaImpl && args.equalsAtEL(
            ((FormulaImpl) other).args);
  }
  //// Internal Representation
  protected final CycArrayList<Object> args = new CycArrayList<Object>();
}
