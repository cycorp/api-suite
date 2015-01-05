package com.cyc.baseclient.cycobject;

/*
 * #%L
 * File: NautImpl.java
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
import com.cyc.base.cycobject.NonAtomicTerm;
import com.cyc.base.cycobject.DenotationalTerm;
import com.cyc.base.cycobject.Fort;
import com.cyc.base.cycobject.Naut;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

//// Internal Imports
import com.cyc.baseclient.datatype.DateConverter;

/** 
 * <P>NautImpl is designed to...
 * 
 * @author baxter, Jul 6, 2009, 10:05:20 AM
 * @version $Id: NautImpl.java 155483 2014-12-10 21:56:51Z nwinant $
 */
public class NautImpl extends FormulaImpl implements NonAtomicTerm, DenotationalTerm, Naut {

  //// Constructors
  /** Creates a new instance of CycNaut. */
  public NautImpl(final Iterable<Object> terms) {
    super(terms);
  }

  public NautImpl(final DenotationalTerm functor, final Object... args) {
    super(functor, args);
  }

  /** Convert term to a NautImpl if it looks like it ought to be one. */
  static public Object convertIfPromising(final Object term) {
    if (term instanceof List && !(term instanceof NautImpl)) {
      final List<Object> termAsList = (List) term;
      if (!termAsList.isEmpty() && termAsList.get(0) instanceof CycConstantImpl) {
        final CycConstantImpl arg0 = (CycConstantImpl) termAsList.get(0);
        if (Character.isUpperCase(arg0.getName().charAt(0))) {
          return new NautImpl(termAsList);
        }
      }
    }
    return term;
  }

  //// Public Area
  public Fort getFunctor() {
    return (Fort) getOperator();
  }

  public NautImpl getFormula() {
    return this;
  }

  
  /**
   * Get the arguments of this non-atomic term, not including the functor,
   * or arg0.
   * @see com.cyc.baseclient.cycobject.FormulaImpl#getArgs()  for version that includes the functor.
   * @return the list of arguments.
   */
  @Override
  public List getArguments() {
    return getArgsUnmodifiable().subList(1, getArity() + 1);
  }

  @Override
  public NautImpl deepCopy() {
    return new NautImpl(super.deepCopy().getArgsUnmodifiable());
  }

  /**
   * Returns <tt>true</tt> some object equals this <tt>NautImpl</tt>
   *
   * @param object the <tt>Object</tt> for equality comparison
   * @return equals <tt>boolean</tt> value indicating equality or non-equality.
   */
  @Override
  public boolean equalsAtEL(Object object) {
    if (!(object instanceof NonAtomicTerm)) {
      return false;
    }
    NonAtomicTerm thatNAT = (NonAtomicTerm) object;
    if (getFunctor().equalsAtEL(thatNAT.getFunctor())
            && getArity() == thatNAT.getArity()) {
      for (int argNum = 1; argNum <= getArity(); argNum++) {
        final Object arg = getArgument(argNum);
        final Object thatArg = thatNAT.getArgument(argNum);
        if (arg.equals(thatArg)) {
          continue;
        } else if (arg instanceof FormulaImpl
                && ((FormulaImpl) arg).equalsAtEL(thatArg)) {
          continue;
        } else if (arg instanceof DenotationalTerm
                && ((DenotationalTerm) arg).equalsAtEL(thatArg)) {
          continue;
        } else {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  /**
   * Returns a list representation of the Base Client NAT.
   *
   * @return a <tt>CycArrayList</tt> representation of the Base Client NART.
   */
  @Override
  public CycArrayList toCycList() {
    CycArrayList cycList = new CycArrayList();
    final Fort functor = getFunctor();
    if (functor instanceof NonAtomicTerm) {
      cycList.add(((NonAtomicTerm) functor).toCycList());
    } else {
      cycList.add(functor);
    }
    for (final Object argument : this.getArguments()) {
      cycList.add(argument);
    }
    return cycList;
  }

  public Object getArgument(int argnum) {
    return getArg(argnum);
  }

  @Override
  public Object cycListApiValue() {
    return super.cycListApiValue();
  }

  /** @return the Date denoted by this term in the default time zone, if it denotes a Date, null otherwise. */
  public Date asDate() {
    return asDate(TimeZone.getDefault());
  }

  /** @return the Date denoted by this term in <code>timeZone</code>, if it denotes a Date, null otherwise. */
  public Date asDate(final TimeZone timeZone) {
    return (isDate()) ? lookupOrComputeDate(timeZone) : null;
  }

  /** @return true iff this NautImpl is a standard CycL date. */
  public boolean isDate() {
    if (dateStatus == null) {
      computeDateStatus();
    }
    return dateStatus;
  }

  /** @return the Quantity denoted by this term, if it denotes a Quantity, null otherwise. */
  public CycQuantity asQuantity() {
    return (isQuantity()) ? quantity : null;
  }

  /** @return true iff this NautImpl is a standard CycL quantity. */
  public boolean isQuantity() {
    if (quantityStatus == null) {
      computeQuantityStatus();
    }
    return quantityStatus;
  }

  //// Private Area
  private Date lookupOrComputeDate(final TimeZone timeZone) {
    if (dates != null && dates.containsKey(timeZone)) {
      return dates.get(timeZone);
    } else {
      final Date date = DateConverter.parseCycDate(this, timeZone, false);
      if (date != null) {
        if (dates == null) {
          dates = new HashMap<TimeZone, Date>();
        }
        dates.put(timeZone, date);
      }
      return date;
    }
  }

  private void computeDateStatus() {
    lookupOrComputeDate(TimeZone.getDefault());
    dateStatus = (dates != null);
  }
  private Map<TimeZone, Date> dates = null;
  private Boolean dateStatus = null;

  private void computeQuantity() {
    quantity = CycQuantity.valueOf(this);
  }

  private void computeQuantityStatus() {
    computeQuantity();
    quantityStatus = (quantity != null);
  }
  private CycQuantity quantity = null;
  private Boolean quantityStatus = null;
  //// Protected Area
  //// Internal Rep
  //// Main
}
