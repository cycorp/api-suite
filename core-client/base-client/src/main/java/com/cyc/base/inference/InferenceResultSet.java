package com.cyc.base.inference;

/*
 * #%L
 * File: InferenceResultSet.java
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

import com.cyc.query.InferenceIdentifier;
import com.cyc.base.exception.CycApiException;
import com.cyc.base.exception.CycTimeOutException;
import com.cyc.base.cycobject.CycConstant;
import com.cyc.base.cycobject.DenotationalTerm;
import com.cyc.base.cycobject.Fort;
import com.cyc.base.cycobject.CycList;
import com.cyc.base.cycobject.Nart;
import com.cyc.base.cycobject.Naut;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.CycVariable;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;

/**
 * InferenceResultSet defines an interface for easy access and manipulation
 of inference results. It tries to closely mimic the java.sql.ResultSet class.
 * 
 * @author nwinant
 */
public interface InferenceResultSet extends ResultSet {
  
  /**
   * Releases this <code>InferenceResultSet</code> object's server side
   * inference resources. Failure to close a result set may leave significant
   * resources hanging around the server until the client <code>CycAccess</code>
   * object is closed.
   * <P>
   * Calling the method <code>close</code> on a <code>InferenceResultSet</code>
   * object that is already closed is a no-op.
   * 
   * @throws CycTimeOutException if doesn't get a response from
            the server before an excessive amount of time has elapsed
   * @throws CycApiException if an internal server error occurs
   */
  @Override
  void close() throws CycTimeOutException, CycApiException;

  /**
   * Returns the timeout for the {@link #close()} method. By default, returns the
   * timeout of the InferenceWorker which returned this object, or this class's
   * own internal default (typically 10,000 milliseconds), whichever is greater.
   * This value may be changed by calling {@link #setCloseTimeout(Long)}.
   * 
   * @return timeout in milliseconds
   */
  long getCloseTimeout();
  
  /**
   * Set the timeout for the {@link #close()} method returned by {@link #getCloseTimeout()}.
   * Specifying a non-null value here will, naturally, override the default value.
   * Specifying a null value will clear the overridden value, and 
   * {@link #getCloseTimeout()} will return its usual default.
   * 
   * @param timeoutMS timeout in milliseconds
   */
  void setCloseTimeout(Long timeoutMS);
  
  /**
   * Returns the one-based column index for <code>col</code>
   * @param col the column variable to look up
   * @return the column index for the given <code>col</code> variable.
   * @throws IllegalArgumentException if called with an invalid <code>col</code>
   */
  int findColumn(CycVariable col) throws IllegalArgumentException;

  /**
   * Returns, as a <code>boolean</code>, the value at the current row and at the
   * column identified by <code>col</code> which should be a
   * <code>CycVariable</code> in the original query. Returns <code>null</code>
   * if no value is set for the current row and given column.
   *
   * <p/><strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once
   * and the version of this method that takes an integer for maximum performance.
   *
   * @param col the name of the variable that represents the column of interest
   * @return the value, as a <code>boolean</code>, at the current row and
   * at the column identified by the <code>col</code> which is a
   * <code>CycVariable</code> in the original query. Returns <code>null</code>,
   * if no value is set for the current row and given column.
   * @throws CycApiException if called on a closed result set
   * @throws IllegalArgumentException if <code>col</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>boolean</code>
   */
  boolean getBoolean(CycVariable col) throws CycApiException, IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException;

    /**
   * Returns, as a <code>CycConstant</code>, the value at the current row and at the
   * column identified by <code>col</code> which should be a
   * <code>CycVariable</code> in the original query. Returns <code>null</code>
   * if no value is set for the current row and given column.
   *
   * <p/><strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once
   * and the version of this method that takes an integer for maximum performance.
   *
   * @param col the name of the variable that represents the column of interest
   * @return the value, as a <code>CycConstant</code>, at the current row and
   * at the column identified by the <code>col</code> which is a
   * <code>CycVariable</code> in the original query. Returns <code>null</code>,
   * if no value is set for the current row and given column.
   * @throws CycApiException if called on a closed result set
   * @throws IllegalArgumentException if <code>col</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>CycConstant</code>
   */
  CycConstant getConstant(CycVariable col) throws CycApiException, IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException;
  
  /**
   * Returns, as a <code>CycConstant</code>, the value at the current row and at the
   * column identified by <code>colIndex</code>. Returns <code>null</code>
   * if no value is set for the current row and given column.

   * @param colIndex the column index of interest (one-based)
   * @return the value, as a <code>CycConstant</code>, at the current row and
   * at the column identified by <code>colIndex</code>. Returns
   * <code>null</code>, if no value is set for the current row and given column.
   * @throws IllegalArgumentException if <code>colIndex</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>CycConstant</code>
   */
  public CycConstant getConstant(int colIndex)
          throws IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException;
  
  /**
   * Returns, as a <code>CycConstant</code>, the value at the current row and at the
   * column identified by <code>col</code>. Returns <code>null</code>
   * if no value is set for the current row and given column.
   *
   * <p/><strong>Note:</strong> Use the method <code>int colindex = findColumn(col)</code> once
   * and the version of this method that takes an integer for maximum performance.
   *
   * @param col the name of the variable that represents the column of interest
   * @return the value, as a <code>CycConstant</code>, at the current row and
   * at the column identified by the <code>col</code>. Returns <code>null</code>,
   * if no value is set for the current row and given column.
   * @throws IllegalArgumentException if <code>col</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>CycConstant</code>
   */
  public CycConstant getConstant(String col)
          throws IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException;
  
  /**
   * Returns, as a <code>CycObject</code>, the value at the current row and at the
   * column identified by <code>col</code> which should be a
   * <code>CycVariable</code> in the original query. Returns <code>null</code>
   * if no value is set for the current row and given column.
   *
   * <p/><strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once
   * and the version of this method that takes an integer for maximum performance.
   *
   * @param col the name of the variable that represents the column of interest
   * @return the value, as a <code>CycObject</code>, at the current row and
   * at the column identified by the <code>col</code> which is a
   * <code>CycVariable</code> in the original query. Returns <code>null</code>,
   * if no value is set for the current row and given column.
   * @throws CycApiException if called on a closed result set
   * @throws IllegalArgumentException if <code>col</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>CycObject</code>
   */
  CycObject getCycObject(CycVariable col) throws CycApiException, IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException;

  /**
   * Returns, as a <code>CycObject</code>, the value at the current row and at the
   * column identified by <code>colIndex</code>. Returns <code>null</code>
   * if no value is set for the current row and given column. Note: literal
   * strings and numbers are not CycObjects and can possibly be returned by
   * some inferences. Only call this method if you are sure that literals
   * are not a valid return result for the given <code>colIndex</code> or
   * a ClassCastException will be thrown when a literal is encountered.
   * @param colIndex the column index of interest (one-based)
   * @return the value, as a <code>CycObject</code>, at the current row and
   * at the column identified by <code>colIndex</code>. Returns
   * <code>null</code>, if no value is set for the current row and given column.
   * @throws IllegalArgumentException if <code>colIndex</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>CycObject</code>
   */
  public CycObject getCycObject(int colIndex)
          throws IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException;

  /**
   * Returns, as a <code>CycObject</code>, the value at the current row and at the
   * column identified by <code>col</code>. Returns <code>null</code>
   * if no value is set for the current row and given column.
   *
   * <p/><strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once
   * and the version of this method that takes an integer for maximum performance.
   *
   * @param col the name of the variable that represents the column of interest
   * @return the value, as a <code>CycObject</code>, at the current row and
   * at the column identified by the <code>col</code>. Returns <code>null</code>,
   * if no value is set for the current row and given column.
   * @throws IllegalArgumentException if <code>col</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>CycObject</code>
   */
  public CycObject getCycObject(String col)
          throws IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException;
  
  int getCurrentRowCount();

  
  /**
   * Returns, as a <code>java.util.Date</code> object, the value at the current row and at the
   * column identified by <code>col</code> which should be a
   * <code>CycVariable</code> in the original query. Returns <code>null</code>
   * if no value is set for the current row and given column.
   *
   * <p/><strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once
   * and the version of this method that takes an integer for maximum performance.
   *
   * @param col the name of the variable that represents the column of interest
   * @return the value, as a <code>java.util.Date</code>, at the current row and
   * at the column identified by the <code>col</code> which is a
   * <code>CycVariable</code> in the original query. Returns <code>null</code>,
   * if no value is set for the current row and given column.
   * @note Fails on dates that are not CycNauts using #$YearFn (i.e. it will not
   * work on skolemized dates, or other forms of dates that don't use the #$YearFn vocabulary).
   * @throws CycApiException if called on a closed result set
   * @throws IllegalArgumentException if <code>col</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>java.util.Date</code>
   */
  Date getDate(CycVariable col) throws CycApiException, IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException;
  
  /**
   * Returns, as a <code>DenotationalTerm</code>, the value at the current row and at the
   * column identified by <code>colIndex</code>. Returns <code>null</code> 
   * if no value is set for the current row and given column.

   * @param colIndex the column index of interest (one-based)
   * @return the value, as a <code>DenotationalTerm</code>, at the current row and
   * at the column identified by <code>colIndex</code>. Returns 
   * <code>null</code>, if no value is set for the current row and given column.
   * @throws IllegalArgumentException if <code>colIndex</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>DenotationalTerm</code>
   */
  public DenotationalTerm getDenotationalTerm(int colIndex)
          throws IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException;
  
  /**
   * Returns, as a <code>DenotationalTerm</code>, the value at the current row and at the
   * column identified by <code>col</code>. Returns <code>null</code> 
   * if no value is set for the current row and given column.
   *
   * <p/><strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once
   * and the version of this method that takes an integer for maximum performance.
   *
   * @param col the name of the variable that represents the column of interest
   * @return the value, as a <code>DenotationalTerm</code>, at the current row and
   * at the column identified by the <code>col</code>. Returns <code>null</code>,
   * if no value is set for the current row and given column.
   * @throws IllegalArgumentException if <code>col</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>DenotationalTerm</code>
   */
  public DenotationalTerm getDenotationalTerm(String col)
          throws IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException;

  /**
   * Returns, as a <code>DenotationalTerm</code>, the value at the current row and at the
   * column identified by <code>col</code> which should be a
   * <code>CycVariable</code> in the original query. Returns <code>null</code>
   * if no value is set for the current row and given column.
   *
   * <p/><strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once
   * and the version of this method that takes an integer for maximum performance.
   *
   * @param col the name of the variable that represents the column of interest
   * @return the value, as a <code>DenotationalTerm</code>, at the current row and
   * at the column identified by the <code>col</code> which is a
   * <code>CycVariable</code> in the original query. Returns <code>null</code>,
   * if no value is set for the current row and given column.
   * @throws CycApiException if called on a closed result set
   * @throws IllegalArgumentException if <code>col</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
 <code>DenotationalTerm</code>
   */
  DenotationalTerm getDenotationalTerm(CycVariable col) throws CycApiException, IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException;

  /**
   * Returns, as a <code>double</code>, the value at the current row and at the
   * column identified by <code>col</code> which should be a
   * <code>CycVariable</code> in the original query. Returns <code>null</code>
   * if no value is set for the current row and given column.
   *
   * <p/><strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once
   * and the version of this method that takes an integer for maximum performance.
   *
   * @param col the name of the variable that represents the column of interest
   * @return the value, as a <code>double</code>, at the current row and
   * at the column identified by the <code>col</code> which is a
   * <code>CycVariable</code> in the original query. Returns <code>null</code>,
   * if no value is set for the current row and given column.
   * @throws CycApiException if called on a closed result set
   * @throws IllegalArgumentException if <code>col</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
 <code>double</code>
   */
  double getDouble(CycVariable col) throws CycApiException, IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException;

  /**
   * Returns, as a <code>float</code>, the value at the current row and at the
   * column identified by <code>col</code> which should be a
   * <code>CycVariable</code> in the original query. Returns <code>null</code>
   * if no value is set for the current row and given column.
   *
   * <p/><strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once
   * and the version of this method that takes an integer for maximum performance.
   *
   * @param col the name of the variable that represents the column of interest
   * @return the value, as a <code>float</code>, at the current row and
   * at the column identified by the <code>col</code> which is a
   * <code>CycVariable</code> in the original query. Returns <code>null</code>,
   * if no value is set for the current row and given column.
   * @throws CycApiException if called on a closed result set
   * @throws IllegalArgumentException if <code>col</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>float</code>
   */
  float getFloat(CycVariable col) throws CycApiException, IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException;
  
  /**
   * Returns, as a <code>CycFort</code>, the value at the current row and at the
   * column identified by <code>colIndex</code>. Returns <code>null</code>
   * if no value is set for the current row and given column.

   * @param colIndex the column index of interest (one-based)
   * @return the value, as a <code>CycFort</code>, at the current row and
   * at the column identified by <code>colIndex</code>. Returns
   * <code>null</code>, if no value is set for the current row and given column.
   * @throws IllegalArgumentException if <code>colIndex</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>CycFort</code>
   */
  public Fort getFort(int colIndex)
          throws IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException;
  
  /**
   * Returns, as a <code>CycFort</code>, the value at the current row and at the
   * column identified by <code>col</code>. Returns <code>null</code>
   * if no value is set for the current row and given column.
   *
   * <p/><strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once
   * and the version of this method that takes an integer for maximum performance.
   *
   * @param col the name of the variable that represents the column of interest
   * @return the value, as a <code>CycFort</code>, at the current row and
   * at the column identified by the <code>col</code>. Returns <code>null</code>,
   * if no value is set for the current row and given column.
   * @throws IllegalArgumentException if <code>col</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>CycFort</code>
   */
  public Fort getFort(String col)
          throws IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException;
  
  /**
   * Returns, as a <code>CycFort</code>, the value at the current row and at the
   * column identified by <code>col</code> which should be a
   * <code>CycVariable</code> in the original query. Returns <code>null</code>
   * if no value is set for the current row and given column.
   *
   * <p/><strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once
   * and the version of this method that takes an integer for maximum performance.
   *
   * @param col the name of the variable that represents the column of interest
   * @return the value, as a <code>CycFort</code>, at the current row and
   * at the column identified by the <code>col</code> which is a
   * <code>CycVariable</code> in the original query. Returns <code>null</code>,
   * if no value is set for the current row and given column.
   * @throws CycApiException if called on a closed result set
   * @throws IllegalArgumentException if <code>col</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
 <code>CycFort</code>
   */
  Fort getFort(CycVariable col) throws CycApiException, IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException;

  InferenceIdentifier getInferenceIdentifier();

  /**
   * Returns, as a <code>int</code>, the value at the current row and at the
   * column identified by <code>col</code> which should be a
   * <code>CycVariable</code> in the original query. Returns <code>null</code>
   * if no value is set for the current row and given column.
   *
   * <p/><strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once
   * and the version of this method that takes an integer for maximum performance.
   *
   * @param col the name of the variable that represents the column of interest
   * @return the value, as a <code>int</code>, at the current row and
   * at the column identified by the <code>col</code> which is a
   * <code>CycVariable</code> in the original query. Returns <code>null</code>,
   * if no value is set for the current row and given column.
   * @throws CycApiException if called on a closed result set
   * @throws IllegalArgumentException if <code>col</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>int</code>
   */
  int getInt(CycVariable col) throws CycApiException, IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException;
  
  /**
   * Returns, as a <code>int</code>, the value at the current row and at the
   * column identified by <code>col</code>. Returns <code>null</code>
   * if no value is set for the current row and given column.
   *
   * <p/><strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once
   * and the version of this method that takes an integer for maximum performance.
   *
   * @param col the name of the variable that represents the column of interest
   * @return the value, as a <code>int</code>, at the current row and
   * at the column identified by the <code>col</code>y. Returns <code>null</code>,
   * if no value is set for the current row and given column.
   * @throws IllegalArgumentException if <code>col</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>int</code>
   */
  @Override
  public int getInt(String col)
          throws IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException;

  /**
   * Returns, as a <code>int</code>, the value at the current row and at the
   * column identified by <code>colIndex</code>. Returns <code>null</code>
   * if no value is set for the current row and given column.

   * @param colIndex the column index of interest (one-based)
   * @return the value, as a <code>int</code>, at the current row and
   * at the column identified by <code>colIndex</code>. Returns
   * <code>null</code>, if no value is set for the current row and given column.
   * @throws IllegalArgumentException if <code>colIndex</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>int</code>
   */
  @Override
  public int getInt(int colIndex)
          throws IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException;
  
  /**
   * Returns, as a <code>CycList</code>, the value at the current row and at the
   * column identified by <code>col</code> which should be a
   * <code>CycVariable</code> in the original query. Returns <code>null</code>
   * if no value is set for the current row and given column.
   *
   * <p/><strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once
   * and the version of this method that takes an integer for maximum performance.
   *
   * @param col the name of the variable that represents the column of interest
   * @return the value, as a <code>CycList</code>, at the current row and
   * at the column identified by the <code>col</code> which is a
   * <code>CycVariable</code> in the original query. Returns <code>null</code>,
   * if no value is set for the current row and given column.
   * @throws CycApiException if called on a closed result set
   * @throws IllegalArgumentException if <code>col</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>CycList</code>
   */
  CycList<Object> getList(CycVariable col) throws CycApiException, IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException;

  /**
   * Returns, as a <code>CycList</code>, the value at the current row and at the
   * column identified by <code>col</code>. Returns <code>null</code>
   * if no value is set for the current row and given column.
   *
   * <p/><strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once
   * and the version of this method that takes an integer for maximum performance.
   *
   * @param col the name of the variable that represents the column of interest
   * @return the value, as a <code>CycList</code>, at the current row and
   * at the column identified by the <code>col</code>. Returns <code>null</code>,
   * if no value is set for the current row and given column.
   * @throws IllegalArgumentException if <code>col</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>CycList</code>
   */
  public CycList<Object> getList(String col)
          throws IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException;

  /**
   * Returns, as a <code>CycList</code>, the value at the current row and at the
   * column identified by <code>colIndex</code>. Returns <code>null</code>
   * if no value is set for the current row and given column.

   * @param colIndex the column index of interest (one-based)
   * @return the value, as a <code>CycList</code>, at the current row and
   * at the column identified by <code>colIndex</code>. Returns
   * <code>null</code>, if no value is set for the current row and given column.
   * @throws IllegalArgumentException if <code>colIndex</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>CycList</code>
   */
  public CycList<Object> getList(int colIndex)
          throws IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException;
  
  /**
   * Returns, as a <code>long</code>, the value at the current row and at the
   * column identified by <code>colIndex</code>. Returns <code>null</code>
   * if no value is set for the current row and given column.

   * @param colIndex the column index of interest (one-based)
   * @return the value, as a <code>long</code>, at the current row and
   * at the column identified by <code>colIndex</code>. Returns
   * <code>null</code>, if no value is set for the current row and given column.
   * @throws IllegalArgumentException if <code>colIndex</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>long</code>
   */
  @Override
  public long getLong(int colIndex)
          throws IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException;
  
  /**
   * Returns, as a <code>long</code>, the value at the current row and at the
   * column identified by <code>col</code>. Returns <code>null</code>
   * if no value is set for the current row and given column.
   *
   * <p/><strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once
   * and the version of this method that takes an integer for maximum performance.
   *
   * @param col the name of the variable that represents the column of interest
   * @return the value, as a <code>long</code>, at the current row and
   * at the column identified by the <code>col</code>. Returns <code>null</code>,
   * if no value is set for the current row and given column.
   * @throws IllegalArgumentException if <code>col</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>long</code>
   */
  @Override
  public long getLong(String col)
          throws IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException;
  
  /**
   * Returns, as a <code>long</code>, the value at the current row and at the
   * column identified by <code>col</code> which should be a
   * <code>CycVariable</code> in the original query. Returns <code>null</code>
   * if no value is set for the current row and given column.
   *
   * <p/><strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once
   * and the version of this method that takes an integer for maximum performance.
   *
   * @param col the name of the variable that represents the column of interest
   * @return the value, as a <code>long</code>, at the current row and
   * at the column identified by the <code>col</code> which is a
   * <code>CycVariable</code> in the original query. Returns <code>null</code>,
   * if no value is set for the current row and given column.
   * @throws CycApiException if called on a closed result set
   * @throws IllegalArgumentException if <code>col</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
 <code>long</code>
   */
  long getLong(CycVariable col) throws CycApiException, IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException;

  /**
   * Returns, as a <code>v</code>, the value at the current row and at the
   * column identified by <code>col</code> which should be a
   * <code>CycVariable</code> in the original query. Returns <code>null</code>
   * if no value is set for the current row and given column.
   *
   * <p/><strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once
   * and the version of this method that takes an integer for maximum performance.
   *
   * @param col the name of the variable that represents the column of interest
   * @return the value, as a <code>CycNart</code>, at the current row and
   * at the column identified by the <code>col</code> which is a
   * <code>CycVariable</code> in the original query. Returns <code>null</code>,
   * if no value is set for the current row and given column.
   * @throws CycApiException if called on a closed result set
   * @throws IllegalArgumentException if <code>col</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>CycNart</code>
   */
  Nart getNart(CycVariable col) throws CycApiException, IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException;
  
  /**
   * Returns, as a <code>CycNart</code>, the value at the current row and at the
   * column identified by <code>col</code>. Returns <code>null</code>
   * if no value is set for the current row and given column.
   *
   * <p/><strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once
   * and the version of this method that takes an integer for maximum performance.
   *
   * @param col the name of the variable that represents the column of interest
   * @return the value, as a <code>CycNart</code>, at the current row and
   * at the column identified by the <code>col</code>. Returns <code>null</code>,
   * if no value is set for the current row and given column.
   * @throws IllegalArgumentException if <code>col</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>CycNart</code>
   */
  public Nart getNart(String col)
          throws IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException;

  /**
   * Returns, as a <code>CycNart</code>, the value at the current row and at the
   * column identified by <code>colIndex</code>. Returns <code>null</code>
   * if no value is set for the current row and given column.

   * @param colIndex the column index of interest (one-based)
   * @return the value, as a <code>CycNart</code>, at the current row and
   * at the column identified by <code>colIndex</code>. Returns
   * <code>null</code>, if no value is set for the current row and given column.
   * @throws IllegalArgumentException if <code>colIndex</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>CycNart</code>
   */
  public Nart getNart(int colIndex)
          throws IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException;
  
  /**
   * Returns, as a <code>CycNaut</code>, the value at the current row and at the
   * column identified by <code>col</code> which should be a
   * <code>CycVariable</code> in the original query. Returns <code>null</code>
   * if no value is set for the current row and given column.
   *
   * <p/><strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once
   * and the version of this method that takes an integer for maximum performance.
   *
   * @param col the name of the variable that represents the column of interest
   * @return the value, as a <code>CycNaut</code>, at the current row and
   * at the column identified by the <code>col</code> which is a
   * <code>CycVariable</code> in the original query. Returns <code>null</code>,
   * if no value is set for the current row and given column.
   * @throws CycApiException if called on a closed result set
   * @throws IllegalArgumentException if <code>col</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>CycNaut</code>
   */
  Naut getNaut(CycVariable col) throws CycApiException, IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException;

  /**
   * Returns, as a <code>CycNaut</code>, the value at the current row and at the
   * column identified by <code>col</code>. Returns <code>null</code>
   * if no value is set for the current row and given column.
   *
   * <p/><strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once
   * and the version of this method that takes an integer for maximum performance.
   *
   * @param col the name of the variable that represents the column of interest
   * @return the value, as a <code>CycNaut</code>, at the current row and
   * at the column identified by the <code>col</code>. Returns <code>null</code>,
   * if no value is set for the current row and given column.
   * @throws IllegalArgumentException if <code>col</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>CycNaut</code>
   */
  public Naut getNaut(String col)
          throws IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException;

  /**
   * Returns, as a <code>CycNaut</code>, the value at the current row and at the
   * column identified by <code>colIndex</code>. Returns <code>null</code>
   * if no value is set for the current row and given column.

   * @param colIndex the column index of interest (one-based)
   * @return the value, as a <code>CycNaut</code>, at the current row and
   * at the column identified by <code>colIndex</code>. Returns
   * <code>null</code>, if no value is set for the current row and given column.
   * @throws IllegalArgumentException if <code>colIndex</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>CycNaut</code>
   */
  public Naut getNaut(int colIndex)
          throws IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException;
  
  /**
   * Returns, as an <code>Object</code>, the value at the current row and at the
   * column identified by <code>col</code> which should be a
   * <code>CycVariable</code> in the original query. Returns <code>null</code>
   * if no value is set for the current row and given column.
   *
   * <p/><strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once
   * and the version of this method that takes an integer for maximum performance.
   *
   * @param col the name of the variable that represents the column of interest
   * @return the value, as an <code>Object</code>, at the current row and
   * at the column identified by the <code>col</code> which is a
   * <code>CycVariable</code> in the original query. Returns <code>null</code>,
   * if no value is set for the current row and given column.
   * @throws CycApiException if called on a closed result set
   * @throws IllegalArgumentException if <code>col</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   */
  Object getObject(CycVariable col) throws CycApiException, IllegalArgumentException, ArrayIndexOutOfBoundsException;
  
  @Override
  public Object getObject(int colIndex)
          throws IllegalArgumentException, ArrayIndexOutOfBoundsException;
  
  /**
   * Returns, as a <code>String</code>, the value at the current row and at the
   * column identified by <code>colIndex</code>. Returns <code>null</code>
   * if no value is set for the current row and given column.

   * @param colIndex the column index of interest (one-based)
   * @return the value, as a <code>String</code>, at the current row and
   * at the column identified by <code>colIndex</code>. Returns
   * <code>null</code>, if no value is set for the current row and given column.
   * @throws IllegalArgumentException if <code>colIndex</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>String</code>
   */
  @Override
  public String getString(int colIndex)
          throws IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException;

  /**
   * Returns, as a <code>String</code>, the value at the current row and at the
   * column identified by <code>col</code>. Returns <code>null</code>
   * if no value is set for the current row and given column.
   *
   * <p/><strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once
   * and the version of this method that takes an integer for maximum performance.
   *
   * @param col the name of the variable that represents the column of interest
   * @return the value, as a <code>String</code>, at the current row and
   * at the column identified by the <code>col</code>. Returns <code>null</code>,
   * if no value is set for the current row and given column.
   * @throws IllegalArgumentException if <code>col</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>String</code>
   */
  @Override
  public String getString(String col)
          throws IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException;
  
  /**
   * Returns, as a <code>String</code>, the value at the current row and at the
   * column identified by <code>col</code> which should be a
   * <code>CycVariable</code> in the original query. Returns <code>null</code>
   * if no value is set for the current row and given column.
   *
   * <p/><strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once
   * and the version of this method that takes an integer for maximum performance.
   *
   * @param col the name of the variable that represents the column of interest
   * @return the value, as a <code>String</code>, at the current row and
   * at the column identified by the <code>col</code> which is a
   * <code>CycVariable</code> in the original query. Returns <code>null</code>,
   * if no value is set for the current row and given column.
   * @throws CycApiException if called on a closed result set
   * @throws IllegalArgumentException if <code>col</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
 <code>String</code>
   */
  String getString(CycVariable col) throws CycApiException, IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException;

  /**
   * Returns whether the query is a simple truth query (no open variables).
   *
   * @return whether the query is a simple truth query (no open variables).
   */
  public boolean hasTruthValue();

  /**
   * Returns the truth value for this query.
   *
   * @return the truth value for this query
   * @throws BaseClientRuntimeException if the query has open variables.
   */
  public boolean getTruthValue();
  
  /**
   * Returns the maximum number of columns.
   *
   * @return the maximum number of columns.
   */
  public int getMaxColumns();

  /**
   * Returns a list (cloned) of column names that are available.
   *
   * @return a list (cloned) of column names that are available.
   */
  public List<String> getColumnNames();
  
  
  
  /**
   * Returns whether the <code>InferenceWorker</code> associated with this
   * <code>InferenceResultSet</code> object is finished working. Will return
   * <code>true</code> if there is no <code>InferenceWorker</code>.
   *
   * @return <code>true</code> if the <code>InferenceWorker</code> associated with this
   * <code>InferenceResultSet</code> or if the <code>InferenceWorker</code> is not set;
   * <code>false</code> if the <code>InferenceWorker</code> associated with this
   * <code>InferenceResultSet</code> might possibly produce more results.
   */
  boolean isInferenceComplete();
  

  @Override
  public boolean next();
  
}
