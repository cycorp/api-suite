package com.cyc.query;

/*
 * #%L
 * File: QueryResultSet.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2018 Cycorp, Inc
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
import com.cyc.kb.Variable;
import com.cyc.kb.exception.KbException;

import java.sql.Date;
import java.util.List;

/**
 * QueryResultSet defines an interface for easy access and manipulation of inference results. It
 * tries to closely mimic the {@link java.sql.ResultSet} class, but also provides an
 * {@link java.lang.Iterable#iterator} method for {@link com.cyc.query.QueryAnswer} objects.
 */
public interface QueryResultSet extends Iterable<QueryAnswer> {

  /**
   * Returns the truth value for this query.
   *
   * @return the truth value for this boolean query
   *
   * @throws QueryRuntimeException if the query has open variables.
   */
  public boolean getTruthValue();

  /**
   * @return the InferenceIdentifier associated with this QueryResultSetImpl. If the inference is
   *         not available on the Cyc server, this will return null.
   */
  public InferenceIdentifier getInferenceIdentifier();

  /**
   * Returns the number of rows currently in the result set.
   *
   * @return the number of rows currently in the result set
   */
  public Integer getCurrentRowCount();

  /**
   * Moves the cursor forward one row from its current position. A <code>QueryResultSetImpl</code>
   * cursor is initially positioned before the first row; the first call to the method
   * <code>next</code> makes the first row the current row; the second call makes the second row the
   * current row, and so on.
   *
   * @return <code>true</code> if the new current row is valid; <code>false</code> if there are no
   *         more rows
   */
  public boolean next();
  
  /**
   * Return the binding for variable as an instance of type.
   *
   * @param <O>       the expected type of the value.
   * @param columnVar the variable for which a binding is sought.
   * @param type      the expected type of the value.
   *
   * @return the binding for <code>variable</code> as an instance of <code>type</code>.
   *
   * @throws IllegalArgumentException       if the binding for <code>variable</code> cannot be
   *                                        converted to <code>type</code>.
   * @throws KbException
   * @throws ArrayIndexOutOfBoundsException if columnIndex doesn't correspond to a valid column in
   *                                        the result set.
   */
  public <O> O getObject(Variable columnVar, Class<O> type);

  /**
   * Return the object at the column with columnLabel as an instance of type.
   *
   * @param <O>         the expected type of the value.
   * @param columnLabel the label of the column, i.e. the name of the variable, e.g. "?X".
   * @param type        the expected type of the value.
   *
   * @return the object at columnIndex as an instance of type
   *
   * @throws IllegalArgumentException       if the value cannot be converted to <code>type</code>
   * @throws ArrayIndexOutOfBoundsException if columnIndex doesn't correspond to a valid column in
   *                                        the result set.
   */
  public <O> O getObject(String columnLabel, Class<O> type);

  /**
   * Return the object at columnIndex as an instance of type.
   *
   * @param <O>         the expected type of the value.
   * @param columnIndex the index of the column.
   * @param type        the expected type of the value.
   *
   * @return the object at columnIndex as an instance of type
   *
   * @throws IllegalArgumentException       if the object at columnIndex cannot be converted to
   *                                        <code>type</code>
   * @throws ArrayIndexOutOfBoundsException if columnIndex doesn't correspond to a valid column in
   *                                        the result set.
   */
  public <O> O getObject(int columnIndex, Class<O> type);

  /**
   * Return the binding for <code>variable</code> as a {@link KbObject} or java primitive, with an
   * expected type of <code>T</code>. Note that you may get a ClassCastException later, because this
   * may not actually give you back a T.
   *
   * @param <O>       the expected type of the value.
   * @param columnVar the variable for which a binding is sought.
   * @param type      the expected type of the value.
   *
   * @return the value at the current row for the specified column.
   *
   * @throws IllegalArgumentException
   * @throws KbException
   * @throws ArrayIndexOutOfBoundsException
   */
  public <O> O getKbObject(Variable columnVar, Class<O> type)
          throws IllegalArgumentException, KbException;

  /**
   * Return the object at the column with <code>columnLabel</code> as a {@link KbObject} or java
   * primitive, with an expected type of <code>T</code>.
   *
   * @param <O>         the expected type of the value.
   * @param columnLabel
   * @param type
   *
   * @return the value at the current row for the specified column.
   *
   * @throws IllegalArgumentException
   * @throws KbException
   * @throws ArrayIndexOutOfBoundsException
   */
  public <O> O getKbObject(String columnLabel, Class<O> type)
          throws IllegalArgumentException, KbException;

  /**
   * Return the object at <code>columnIndex</code> as a {@link KbObject} or java primitive, with an
   * expected type of <code>T</code>.
   *
   * @param <O>         the expected type of the value.
   * @param columnIndex the index of the column. Cf.
   * @param type        the expected type of the value. {@link #findColumn(Variable)}.
   *
   * @return the value at the current row for the specified column.
   *
   * @throws IllegalArgumentException
   * @throws ArrayIndexOutOfBoundsException
   */
  public <O> O getKbObject(int columnIndex, Class<O> type)
          throws IllegalArgumentException, KbException;

  /**
   * Corresponds to {@link java.sql.ResultSet#afterLast()}.
   * <p>
   * Places the cursor just after the last row.
   *
   */
  public void afterLast();

  /**
   * Returns the column index for <code>variable</code>.
   *
   * @param columnVar The variable of interest.
   *
   * @return the column index for <code>variable</code>.
   *
   * @throws IllegalArgumentException
   */
  public int findColumn(Variable columnVar);

  /**
   * Returns the column index for <code>columnLabel</code>.
   *
   * @param columnLabel the column name to look up
   *
   * @return the column index for the given <code>columnLabel</code>.
   *
   * @throws IllegalArgumentException if called with an invalid <code>columnLabel</code>
   */
  public int findColumn(String columnLabel);

  /**
   * Returns a list of column names that are available.
   *
   * @return a list of column names that are available.
   */
  public List<String> getColumnNames();

  /**
   * Returns a list of column Variables that are available.
   *
   * @return a list of column Variables that are available.
   */
  public List<Variable> getColumns();

  /**
   * Returns whether the inference associated with this <code>InferenceResultSet</code> object is
   * finished.
   *
   * @return <code>true</code> if the inference associated with this <code>InferenceResultSet</code>
   *         object is finished;<code>false</code> if the inference associated with this
   *         <code>InferenceResultSet</code> might possibly produce more results.
   */
  public boolean isInferenceComplete();

  /**
   * Releases this <code>QueryResultSetImpl</code> object's server side inference resources. Failure
   * to close a result set may leave significant resources hanging around the server until the
   * client <code>CycAccess</code> object is closed.
   * <P>
   * Calling the method <code>close</code> on a <code>InferenceResultSet</code> object that is
   * already closed is a no-op.
   */
  public void close();

  /**
   * Corresponds to {@link java.sql.ResultSet#isClosed()}.
   *
   * @return true if this <code>QueryResultSetImpl</code> object is closed; false if it is still
   *         open
   */
  public boolean isClosed();

  /**
   * Corresponds to {@link java.sql.ResultSet#getRow()}.
   *
   * @return the current row number; <code>0</code> if there is no current row
   */
  public int getRow();

  /**
   * Returns, as a <code>String</code>, the value at the current row and at the column identified by
   * <code>columnIndex</code>. Returns <code>null</code> if no value is set for the current row and
   * given column.
   *
   * @param columnIndex the column index of interest (one-based)
   *
   * @return the value, as a <code>String</code>, at the current row and at the column identified by
   *         <code>columnIndex</code>. Returns <code>null</code>, if no value is set for the current
   *         row and given column.
   *
   * @throws IllegalArgumentException       if <code>columnIndex</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException             if the value is not convertible to a <code>String</code>
   */
  public String getString(int columnIndex);

  /**
   * Returns, as a <code>String</code>, the value at the current row and at the column identified by
   * <code>columnLabel</code>. Returns <code>null</code> if no value is set for the current row and
   * given column.
   *
   * <p/>
   * <strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once and the
   * version of this method that takes an integer for maximum performance.
   *
   * @param columnLabel the name of the variable that represents the column of interest
   *
   * @return the value, as a <code>String</code>, at the current row and at the column identified by
   *         the <code>columnLabel</code>. Returns <code>null</code>, if no value is set for the
   *         current row and given column.
   *
   * @throws IllegalArgumentException       if <code>columnLabel</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException             if the value is not convertible to a <code>String</code>
   */
  public String getString(String columnLabel);

  /**
   * Returns, as a <code>String</code>, the value at the current row for <code>variable</code>.
   * Returns <code>null</code> if no value is set for the current row and given column.
   *
   * <p/>
   * <strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once and the
   * version of this method that takes an integer for maximum performance.
   *
   * @param columnVar the variable of interest
   *
   * @return the value, as a <code>String</code>, at the current row for <code>variable</code>.
   *         Returns <code>null</code>, if no value is set for the current row and given column.
   *
   * @throws IllegalArgumentException       if <code>variable</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException             if the value is not convertible to a <code>String</code>
   */
  public String getString(Variable columnVar);

  /**
   * Implementation of {@link java.sql.ResultSet#getBoolean(int)}.
   *
   * @param columnIndex
   *
   * @return the value at the specified column in the current row.
   */
  public boolean getBoolean(int columnIndex);

  /**
   * Implementation of {@link java.sql.ResultSet#getBoolean(String)}.
   *
   * @param columnLabel
   *
   * @return the value at the specified column in the current row.
   */
  public boolean getBoolean(String columnLabel);

  /**
   * Returns, as a <code>boolean</code>, the value at the current row for <code>variable</code>.
   * Returns <code>null</code> if no value is set for the current row and given column.
   *
   * <p/>
   * <strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once and the
   * version of this method that takes an integer for maximum performance.
   *
   * @param columnVar the variable of interest
   *
   * @return the value, as a <code>boolean</code>, at the current row for <code>variable</code>.
   *         Returns <code>null</code>, if no value is set for the current row and given column.
   *
   * @throws IllegalArgumentException       if <code>variable</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException             if the value is not convertible to a
   *                                        <code>boolean</code>
   */
  public boolean getBoolean(Variable columnVar);

  /**
   * Returns, as a <code>int</code>, the value at the current row and at the column identified by
   * <code>columnIndex</code>. Returns <code>null</code> if no value is set for the current row and
   * given column.
   *
   * @param columnIndex the column index of interest (one-based)
   *
   * @return the value, as a <code>int</code>, at the current row and at the column identified by
   *         <code>columnIndex</code>. Returns <code>null</code>, if no value is set for the current
   *         row and given column.
   *
   * @throws IllegalArgumentException       if <code>columnIndex</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException             if the value is not convertible to a <code>int</code>
   */
  public int getInt(int columnIndex);

  /**
   * Returns, as a <code>int</code>, the value at the current row and at the column identified by
   * <code>columnLabel</code>. Returns <code>null</code> if no value is set for the current row and
   * given column.
   *
   * <p/>
   * <strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once and the
   * version of this method that takes an integer for maximum performance.
   *
   * @param columnLabel the name of the variable that represents the column of interest
   *
   * @return the value, as a <code>int</code>, at the current row and at the column identified by
   *         the <code>columnLabel</code>y. Returns <code>null</code>, if no value is set for the
   *         current row and given column.
   *
   * @throws IllegalArgumentException       if <code>columnLabel</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException             if the value is not convertible to a <code>int</code>
   */
  public int getInt(String columnLabel);

  /**
   * Returns, as a <code>int</code>, the value at the current row for <code>variable</code>. Returns
   * <code>null</code> if no value is set for the current row and given column.
   *
   * <p/>
   * <strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once and the
   * version of this method that takes an integer for maximum performance.
   *
   * @param columnVar the variable of interest
   *
   * @return the value, as a <code>int</code>, at the current row and for <code>variable</code>.
   *         Returns <code>null</code>, if no value is set for the current row and given column.
   *
   * @throws IllegalArgumentException       if <code>variable</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException             if the value is not convertible to a <code>int</code>
   */
  public int getInt(Variable columnVar);

  /**
   * Returns, as a <code>long</code>, the value at the current row and at the column identified by
   * <code>columnIndex</code>. Returns <code>null</code> if no value is set for the current row and
   * given column.
   *
   * @param columnIndex the column index of interest (one-based)
   *
   * @return the value, as a <code>long</code>, at the current row and at the column identified by
   *         <code>columnIndex</code>. Returns <code>null</code>, if no value is set for the current
   *         row and given column.
   *
   * @throws IllegalArgumentException       if <code>columnIndex</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException             if the value is not convertible to a <code>long</code>
   */
  public long getLong(int columnIndex);

  /**
   * Returns, as a <code>long</code>, the value at the current row and at the column identified by
   * <code>columnLabel</code>. Returns <code>null</code> if no value is set for the current row and
   * given column.
   *
   * <p/>
   * <strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once and the
   * version of this method that takes an integer for maximum performance.
   *
   * @param columnLabel the name of the variable that represents the column of interest
   *
   * @return the value, as a <code>long</code>, at the current row and at the column identified by
   *         the <code>columnLabel</code>. Returns <code>null</code>, if no value is set for the
   *         current row and given column.
   *
   * @throws IllegalArgumentException       if <code>columnLabel</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException             if the value is not convertible to a <code>long</code>
   */
  public long getLong(String columnLabel);

  /**
   * Returns, as a <code>long</code>, the value at the current row for <code>variable</code>.
   * Returns <code>null</code> if no value is set for the current row and given column.
   *
   * <p/>
   * <strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once and the
   * version of this method that takes an integer for maximum performance.
   *
   * @param columnVar the name of the variable that represents the column of interest
   *
   * @return the value, as a <code>long</code>, at the current row for <code>variable</code>.
   *         Returns <code>null</code>, if no value is set for the current row and given column.
   *
   * @throws IllegalArgumentException       if <code>variable</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException             if the value is not convertible to a <code>long</code>
   */
  public long getLong(Variable columnVar);

  /**
   * Implementation of {@link java.sql.ResultSet#getFloat(int)}.
   *
   * @param columnIndex
   *
   * @return the value at the specified column in the current row.
   */
  public float getFloat(int columnIndex);

  /**
   * Returns, as a <code>float</code>, the value at the current row and at the column identified by
   * <code>columnLabel</code>. Returns <code>null</code> if no value is set for the current row and
   * given column.
   *
   * <p/>
   * <strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once and the
   * version of this method that takes an integer for maximum performance.
   *
   * @param columnLabel the name of the variable that represents the column of interest
   *
   * @return the value, as a <code>float</code>, at the current row and at the column identified by
   *         the <code>columnLabel</code>. Returns <code>null</code>, if no value is set for the
   *         current row and given column.
   *
   * @throws IllegalArgumentException       if <code>columnLabel</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException             if the value is not convertible to a <code>float</code>
   */
  public float getFloat(String columnLabel);

  /**
   * Returns, as a <code>float</code>, the value at the current row for <code>variable</code>.
   * Returns <code>null</code> if no value is set for the current row and given column.
   *
   * <p/>
   * <strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once and the
   * version of this method that takes an integer for maximum performance.
   *
   * @param columnVar the name of the variable that represents the column of interest
   *
   * @return the value, as a <code>float</code>, at the current row for <code>variable</code>.
   *         Returns <code>null</code>, if no value is set for the current row and given column.
   *
   * @throws IllegalArgumentException       if <code>variable</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException             if the value is not convertible to a <code>float</code>
   */
  public float getFloat(Variable columnVar);

  /**
   * Implementation of {@link java.sql.ResultSet#getDouble(int)}.
   *
   * @param columnIndex
   *
   * @return the value at the specified column in the current row.
   */
  public double getDouble(int columnIndex);

  /**
   * Returns, as a <code>double</code>, the value at the current row and at the column identified by
   * <code>columnLabel</code>. Returns <code>null</code> if no value is set for the current row and
   * given column.
   *
   * <p/>
   * <strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once and the
   * version of this method that takes an integer for maximum performance.
   *
   * @param columnLabel the name of the variable that represents the column of interest
   *
   * @return the value, as a <code>double</code>, at the current row and at the column identified by
   *         the <code>columnLabel</code>. Returns <code>null</code>, if no value is set for the
   *         current row and given column.
   *
   * @throws IllegalArgumentException       if <code>columnLabel</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException             if the value is not convertible to a <code>double</code>
   */
  public double getDouble(String columnLabel);

  /**
   * Returns, as a <code>double</code>, the value at the current row for <code>variable</code>.
   * Returns <code>null</code> if no value is set for the current row and given column.
   *
   * <p/>
   * <strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once and the
   * version of this method that takes an integer for maximum performance.
   *
   * @param columnVar the name of the variable that represents the column of interest
   *
   * @return the value, as a <code>double</code>, at the current row for <code>variable</code>.
   *         Returns <code>null</code>, if no value is set for the current row and given column.
   *
   * @throws IllegalArgumentException       if <code>variable</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException             if the value is not convertible to a <code>double</code>
   */
  public double getDouble(Variable columnVar);

  /**
   * Returns, as a <code>java.sql.Date</code> object, the value in the current row at the column
   * identified by <code>columnIndex</code> position. Returns <code>null</code> if no value is set
   * for the current row and given column.
   *
   * This method fails on dates that do not use #$YearFn (i.e. it will not work on skolemized dates,
   * or other forms of dates that don't use the #$YearFn vocabulary).
   *
   * @param columnIndex
   *
   * @return the value at the current row and at the column identified by columnLabel as a Date
   *         object
   *
   * @throws IllegalArgumentException       if <code>columnLabel</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException             if the value is not convertible to a
   *                                        {@link java.sql.Date}
   */
  public Date getDate(int columnIndex);

  /**
   * Returns, as a <code>java.sql.Date</code> object, the value in the current row at the column
   * identified by <code>columnLabel</code>, which should be the name of a <code>Variable</code> in
   * the original query. Returns <code>null</code> if no value is set for the current row and given
   * column.
   *
   * This method fails on dates that do not use #$YearFn (i.e. it will not work on skolemized dates,
   * or other forms of dates that don't use the #$YearFn vocabulary).
   *
   * @param columnLabel
   *
   * @return the value at the current row and at the column identified by columnLabel as a Date
   *         object
   *
   * @throws IllegalArgumentException       if <code>columnLabel</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException             if the value is not convertible to a
   *                                        {@link java.sql.Date}
   */
  public Date getDate(String columnLabel);

  /**
   * Returns, as a <code>java.sql.Date</code> object, the value in the current row for variable.
   * Returns <code>null</code> if no value is set for the current row and given variable.
   *
   * This method fails on dates that do not use #$YearFn (i.e. it will not work on skolemized dates,
   * or other forms of dates that don't use the #$YearFn vocabulary).
   *
   * @param columnVar
   *
   * @return the value at the current row and at the column identified by variable as a Date object
   *
   * @throws IllegalArgumentException       if <code>variable</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException             if the value is not convertible to a
   *                                        {@link java.sql.Date}
   */
  public Date getDate(Variable columnVar);

  /**
   * Corresponds to {@link java.sql.ResultSet#beforeFirst()}.
   * <p>
   * Places the cursor just before the first row.
   *
   */
  public void beforeFirst();

  /**
   * Corresponds to {@link java.sql.ResultSet#first()}. Moves the cursor to the first row.
   *
   * @return <code>true</code> if the cursor is on a valid row; <code>false</code> if there are no
   *         rows in the result set
   */
  public boolean first();

  /**
   * Corresponds to {@link java.sql.ResultSet#last()}. Moves the cursor to the last row.
   *
   * @return <code>true</code> if the cursor is on a valid row; <code>false</code> if there are no
   *         rows in the result set
   */
  public boolean last();

  /**
   * Corresponds to {@link java.sql.ResultSet#absolute(int)}. Moves the cursor to the specified row.
   *
   * @param row the row to which to move the cursor
   *
   * @return <code>true</code> if the cursor is moved to a position in this
   *         <code>QueryResultSetImpl</code> object; <code>false</code> if the cursor is before the
   *         first row or after the last row
   */
  public boolean absolute(int row);

  /**
   * Corresponds to {@link java.sql.ResultSet#relative(int)}. Moves the cursor up or down the
   * specified number of rows.
   *
   * @param rows the number of rows to move the cursor.
   *
   * @return <code>true</code> if the cursor is on a row; <code>false</code> otherwise
   */
  public boolean relative(int rows);

  /**
   * Corresponds to {@link java.sql.ResultSet#isBeforeFirst()}.
   *
   * @return <code>true</code> if the cursor is before the first row; <code>false</code> if the
   *         cursor is at any other position or the result set contains no rows
   */
  public boolean isBeforeFirst();

  /**
   * Corresponds to {@link java.sql.ResultSet#isAfterLast()}.
   *
   * @return <code>true</code> if the cursor is after the last row; <code>false</code> if the cursor
   *         is at any other position or the result set contains no rows
   */
  public boolean isAfterLast();

  /**
   * Corresponds to {@link java.sql.ResultSet#isFirst()}.
   *
   * @return <code>true</code> if the cursor is on the first row; <code>false</code> otherwise
   */
  public boolean isFirst();

  /**
   * Corresponds to {@link java.sql.ResultSet#isLast()}.
   *
   * @return <code>true</code> if the cursor is on the last row; <code>false</code> otherwise
   */
  public boolean isLast();

  /**
   * Corresponds to {@link java.sql.ResultSet#previous()}. Moves the cursor to the previous row.
   *
   * @return <code>true</code> if the cursor is now positioned on a valid row; <code>false</code> if
   *         the cursor is positioned before the first row
   */
  public boolean previous();
}
