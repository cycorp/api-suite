package com.cyc.query;

/*
 * #%L
 * File: KBInferenceResultSet.java
 * Project: Query API Implementation
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
import com.cyc.base.CycApiException;
import com.cyc.base.cycobject.CycVariable;
import com.cyc.base.inference.InferenceIdentifier;
import com.cyc.base.inference.InferenceResultSet;
import com.cyc.baseclient.inference.DefaultResultSet;
import com.cyc.kb.Variable;
import com.cyc.kb.KBObject;
import com.cyc.kb.client.KBObjectImpl;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KBApiException;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <P>
 * KBInferenceResultSet provides a class for easy access and manipulation of
 * inference results. It tries to closely mimic the {@link java.sql.ResultSet}
 * interface, but does not implement all of its methods.
 * <p>
 * In this paradigm, the results of a Query are a table with one row for each
 * answer, and one column for each query variable. There is a cursor that can be
 * moved from row to row, and various getter methods that retrieve the binding
 * for a particular variable (i.e. the value in the column corresponding to that
 * variable) on the current row.
 * <p>
 * The cursor starts out just before the first row, and can be advanced to just
 * after the last row. Otherwise, it is on whatever the current row is.
 * <p>
 * In general, KBInferenceResult sets should not be created by application
 * developers, but should be retrieved from a {@link Query} object using
 * {@link Query#getResultSet()}.
 *
 * @author daves
 */
public class KBInferenceResultSet {

  private InferenceResultSet rs;

  /**
   * Create a new KBInferenceResultSet from a list of results.
   *
   * @param results
   */
  protected KBInferenceResultSet(List<Object> results) {
    this.rs = new DefaultResultSet(results);
  }

  /**
   * Construct a new KBInferenceResultSet from an InferenceResultSet.
   *
   * @param rs
   */
  protected KBInferenceResultSet(InferenceResultSet rs) {
    this.rs = rs;
  }

  @Override
  public String toString() {
    return rs.toString();
  }

  /**
   * Return the object at columnIndex as an instance of type.
   *
   * @param <T> the expected type of the value.
   * @param columnIndex the index of the column.
   * @param type the expected type of the value.
   * @return the object at columnIndex as an instance of type
   * @throws IllegalArgumentException if the object at columnIndex cannot be
   * converted to <code>type</code>
   * @throws ArrayIndexOutOfBoundsException if columnIndex doesn't correspond to
   * a valid column in the result set.
   */
  public <T> T getObject(int columnIndex, Class<T> type) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
    Object obj = rs.getObject(columnIndex);
    try {
      return KBObjectImpl.<T>checkAndCastObject(obj);
    } catch (CreateException ex) {
      throw new IllegalArgumentException(ex);
    }
  }

  /**
   * Return the binding for variable as an instance of type.
   *
   * @param <T> the expected type of the value.
   * @param variable the variable for which a binding is sought.
   * @param type the expected type of the value.
   * @return the binding for <code>variable</code> as an instance of
   * <code>type</code>.
   * @throws IllegalArgumentException if the binding for <code>variable</code>
   * cannot be converted to <code>type</code>.
   * @throws com.cyc.kb.exception.KBApiException
   * @throws ArrayIndexOutOfBoundsException if columnIndex doesn't correspond to
   * a valid column in the result set.
   */
  public <T> T getObject(Variable variable, Class<T> type) throws IllegalArgumentException, KBApiException {
    return getObject("?" + variable.getName(), type);
  }

  /**
   * Return the object at the column with columnLabel as an instance of type.
   *
   * @param <T> the expected type of the value.
   * @param columnLabel the label of the column, i.e. the name of the variable,
   * e.g. "?X".
   * @param type the expected type of the value.
   * @return the object at columnIndex as an instance of type
   * @throws IllegalArgumentException if the value cannot be converted to
   * <code>type</code>
   * @throws com.cyc.kb.exception.KBApiException
   * @throws ArrayIndexOutOfBoundsException if columnIndex doesn't correspond to
   * a valid column in the result set.
   */
  public <T> T getObject(String columnLabel, Class<T> type) throws IllegalArgumentException, KBApiException {
    try {
      Object obj = rs.getObject(columnLabel);
      return KBObjectImpl.<T>checkAndCastObject(obj);
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Return the object at <code>columnIndex</code>.
   *
   * @param columnIndex the index of the column. Cf.
   * {@link #findColumn(Variable)}. The first column is 1.
   * @return the value at the current row for the specified column.
   * @throws IllegalArgumentException
   * @throws ArrayIndexOutOfBoundsException
   */
  public Object getObject(int columnIndex) {
    return getKBObject(columnIndex);
  }

  /**
   * Return the object at the column with <code>columnLabel</code>.
   *
   * @param columnLabel
   * @return the value at the current row for the specified column.
   * @throws com.cyc.kb.exception.KBApiException
   * @throws IllegalArgumentException
   * @throws ArrayIndexOutOfBoundsException
   */
  public Object getObject(String columnLabel) throws KBApiException {
    return getKBObject(columnLabel);
  }

  /**
   * Return the object at <code>columnIndex</code> as a {@link KBObject} or java
   * primitive, with an expected type of <code>T</code>.
   *
   * @param <T> the expected type of the value.
   * @param columnIndex the index of the column. Cf.
   * {@link #findColumn(Variable)}.
   * @return the value at the current row for the specified column.
   * @throws IllegalArgumentException
   * @throws ArrayIndexOutOfBoundsException
   */
  public <T> T getKBObject(int columnIndex) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
    Object obj = rs.getObject(columnIndex);
    try {
      return KBObjectImpl.<T>checkAndCastObject(obj);
    } catch (KBApiException ex) {
      logger.error("Problem getting KBObject.", ex);
      throw new RuntimeException(ex);
    }
  }

  /**
   * Return the binding for <code>variable</code> as a {@link KBObject} or java
   * primitive, with an expected type of <code>T</code>.
   *
   * @param <T> the expected type of the value.
   * @param variable the variable for which a binding is sought.
   * @return the value at the current row for the specified column.
   * @throws IllegalArgumentException
   * @throws com.cyc.kb.exception.KBApiException
   * @throws ArrayIndexOutOfBoundsException
   */
  public <T> T getKBObject(Variable variable) throws IllegalArgumentException, KBApiException {
    return getKBObject("?" + variable.getName());
  }

  /**
   * Return the object at the column with <code>columnLabel</code> as a
   * {@link KBObject} or java primitive, with an expected type of
   * <code>T</code>.
   *
   * @param <T> the expected type of the value.
   * @param columnLabel
   * @return the value at the current row for the specified column.
   * @throws IllegalArgumentException
   * @throws com.cyc.kb.exception.KBApiException
   * @throws ArrayIndexOutOfBoundsException
   */
  public <T> T getKBObject(String columnLabel) throws IllegalArgumentException, KBApiException {
    try {
      Object obj = rs.getObject(columnLabel);
      return KBObjectImpl.<T>checkAndCastObject(obj);
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * @return the InferenceIdentifier associated with this KBInferenceResultSet.
   * If the inference is not available on the Cyc server, this will return null.
   */
  InferenceIdentifier getInferenceIdentifier() {
    return rs.getInferenceIdentifier();
  }

  /**
   * Returns the InferenceResultSet that underlies this KBInferenceResultSet.
   *
   * @return the InferenceResultSet that underlies this KBInferenceResultSet
   */
  InferenceResultSet getInferenceResultSet() {
    return rs;
  }

  /**
   * Returns a list of column names that are available.
   *
   * @return a list of column names that are available.
   */
  public List<String> getColumnNames() {
    return rs.getColumnNames();
  }

  /**
   * Returns whether the inference associated with this
   * <code>InferenceResultSet</code> object is finished.
   *
   * @return <code>true</code> if the inference associated with this
   * <code>InferenceResultSet</code> object is finished;<code>false</code> if
   * the inference associated with this <code>InferenceResultSet</code> might
   * possibly produce more results.
   */
  public boolean isInferenceComplete() {
    return rs.isInferenceComplete();
  }

  /**
   * Releases this <code>KBInferenceResultSet</code> object's server side
   * inference resources. Failure to close a result set may leave significant
   * resources hanging around the server until the client <code>CycAccess</code>
   * object is closed.
   * <P>
   * Calling the method <code>close</code> on a <code>InferenceResultSet</code>
   * object that is already closed is a no-op.
   */
  public synchronized void close() {
    rs.close();
  }

  /**
   * Moves the cursor forward one row from its current position. A
   * <code>KBInferenceResultSet</code> cursor is initially positioned before the
   * first row; the first call to the method <code>next</code> makes the first
   * row the current row; the second call makes the second row the current row,
   * and so on.
   *
   * @return <code>true</code> if the new current row is valid;
   * <code>false</code> if there are no more rows
   */
  public boolean next() {
    return rs.next();
  }

  /**
   * Returns the column index for <code>variable</code>.
   *
   * @param variable The variable of interest.
   * @return the column index for <code>variable</code>.
   * @throws IllegalArgumentException
   */
  public int findColumn(Variable variable) throws IllegalArgumentException {
    return rs.findColumn((CycVariable) (variable.getCore()));
  }

  /**
   * Returns the number of rows currently in the result set.
   *
   * @return the number of rows currently in the result set
   */
  public int getCurrentRowCount() {
    return rs.getCurrentRowCount();
  }

  /**
   * Returns the truth value for this query.
   *
   * @return the truth value for this boolean query
   * @throws RuntimeException if the query has open variables.
   */
  public boolean getTruthValue() {
    return rs.getTruthValue();
  }

  /**
   * Returns, as a <code>String</code>, the value at the current row and at the
   * column identified by <code>columnIndex</code>. Returns <code>null</code> if
   * no value is set for the current row and given column.
   *
   * @param columnIndex the column index of interest (one-based)
   * @return the value, as a <code>String</code>, at the current row and at the
   * column identified by <code>columnIndex</code>. Returns <code>null</code>,
   * if no value is set for the current row and given column.
   * @throws IllegalArgumentException if <code>columnIndex</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a
   * valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>String</code>
   */
  public String getString(int columnIndex) {
    return rs.getString(columnIndex);
  }

  /**
   * Implementation of {@link java.sql.ResultSet#getBoolean(int)}.
   *
   * @param columnIndex
   * @return the value at the specified column in the current row.
   */
  public boolean getBoolean(int columnIndex) {
    try {
      return rs.getBoolean(columnIndex);
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Returns, as a <code>int</code>, the value at the current row and at the
   * column identified by <code>columnIndex</code>. Returns <code>null</code> if
   * no value is set for the current row and given column.
   *
   * @param columnIndex the column index of interest (one-based)
   * @return the value, as a <code>int</code>, at the current row and at the
   * column identified by <code>columnIndex</code>. Returns <code>null</code>,
   * if no value is set for the current row and given column.
   * @throws IllegalArgumentException if <code>columnIndex</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a
   * valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>int</code>
   */
  public int getInt(int columnIndex) {
    return rs.getInt(columnIndex);
  }

  /**
   * Returns, as a <code>long</code>, the value at the current row and at the
   * column identified by <code>columnIndex</code>. Returns <code>null</code> if
   * no value is set for the current row and given column.
   *
   * @param columnIndex the column index of interest (one-based)
   * @return the value, as a <code>long</code>, at the current row and at the
   * column identified by <code>columnIndex</code>. Returns <code>null</code>,
   * if no value is set for the current row and given column.
   * @throws IllegalArgumentException if <code>columnIndex</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a
   * valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>long</code>
   */
  public long getLong(int columnIndex) {
    return rs.getLong(columnIndex);
  }

  /**
   * Implementation of {@link java.sql.ResultSet#getFloat(int)}.
   *
   * @param columnIndex
   * @return the value at the specified column in the current row.
   */
  public float getFloat(int columnIndex) {
    try {
      return rs.getFloat(columnIndex);
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Implementation of {@link java.sql.ResultSet#getDouble(int)}.
   *
   * @param columnIndex
   * @return the value at the specified column in the current row.
   */
  public double getDouble(int columnIndex) {
    try {
      return rs.getDouble(columnIndex);
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Returns, as a <code>String</code>, the value at the current row for
   * <code>variable</code>. Returns <code>null</code> if no value is set for the
   * current row and given column.
   *
   * <p/>
   * <strong>Note:</strong> Use the method
   * <code>int colIindex = findColumn(col)</code> once and the version of this
   * method that takes an integer for maximum performance.
   *
   * @param variable the variable of interest
   * @return the value, as a <code>String</code>, at the current row for
   * <code>variable</code>. Returns <code>null</code>, if no value is set for
   * the current row and given column.
   * @throws IllegalArgumentException if <code>variable</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a
   * valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>String</code>
   */
  public String getString(Variable variable) {
    return getString("?" + variable.getName());
  }

  /**
   * Returns, as a <code>String</code>, the value at the current row and at the
   * column identified by <code>columnLabel</code>. Returns <code>null</code> if
   * no value is set for the current row and given column.
   *
   * <p/>
   * <strong>Note:</strong> Use the method
   * <code>int colIindex = findColumn(col)</code> once and the version of this
   * method that takes an integer for maximum performance.
   *
   * @param columnLabel the name of the variable that represents the column of
   * interest
   * @return the value, as a <code>String</code>, at the current row and at the
   * column identified by the <code>columnLabel</code>. Returns
   * <code>null</code>, if no value is set for the current row and given column.
   * @throws IllegalArgumentException if <code>columnLabel</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a
   * valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>String</code>
   */
  public String getString(String columnLabel) {
    return rs.getString(columnLabel);
  }

  /**
   * Returns, as a <code>boolean</code>, the value at the current row for
   * <code>variable</code>. Returns <code>null</code> if no value is set for the
   * current row and given column.
   *
   * <p/>
   * <strong>Note:</strong> Use the method
   * <code>int colIindex = findColumn(col)</code> once and the version of this
   * method that takes an integer for maximum performance.
   *
   * @param variable the variable of interest
   * @return the value, as a <code>boolean</code>, at the current row for
   * <code>variable</code>. Returns <code>null</code>, if no value is set for
   * the current row and given column.
   * @throws IllegalArgumentException if <code>variable</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a
   * valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>boolean</code>
   */
  public boolean getBoolean(Variable variable) {
    return getBoolean("?" + variable.getName());
  }

  /**
   * Implementation of {@link java.sql.ResultSet#getBoolean(String)}.
   *
   * @param columnLabel
   * @return the value at the specified column in the current row.
   */
  public boolean getBoolean(String columnLabel) {
    try {
      return rs.getBoolean(columnLabel);
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Returns, as a <code>int</code>, the value at the current row for
   * <code>variable</code>. Returns <code>null</code> if no value is set for the
   * current row and given column.
   *
   * <p/>
   * <strong>Note:</strong> Use the method
   * <code>int colIindex = findColumn(col)</code> once and the version of this
   * method that takes an integer for maximum performance.
   *
   * @param variable the variable of interest
   * @return the value, as a <code>int</code>, at the current row and for
   * <code>variable</code>. Returns <code>null</code>, if no value is set for
   * the current row and given column.
   * @throws IllegalArgumentException if <code>variable</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a
   * valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>int</code>
   */
  public int getInt(Variable variable) {
    return getInt("?" + variable.getName());
  }

  /**
   * Returns, as a <code>int</code>, the value at the current row and at the
   * column identified by <code>columnLabel</code>. Returns <code>null</code> if
   * no value is set for the current row and given column.
   *
   * <p/>
   * <strong>Note:</strong> Use the method
   * <code>int colIindex = findColumn(col)</code> once and the version of this
   * method that takes an integer for maximum performance.
   *
   * @param columnLabel the name of the variable that represents the column of
   * interest
   * @return the value, as a <code>int</code>, at the current row and at the
   * column identified by the <code>columnLabel</code>y. Returns
   * <code>null</code>, if no value is set for the current row and given column.
   * @throws IllegalArgumentException if <code>columnLabel</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a
   * valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>int</code>
   */
  public int getInt(String columnLabel) {
    return rs.getInt(columnLabel);
  }

  /**
   * Returns, as a <code>long</code>, the value at the current row for
   * <code>variable</code>. Returns <code>null</code> if no value is set for the
   * current row and given column.
   *
   * <p/>
   * <strong>Note:</strong> Use the method
   * <code>int colIindex = findColumn(col)</code> once and the version of this
   * method that takes an integer for maximum performance.
   *
   * @param variable the name of the variable that represents the column of
   * interest
   * @return the value, as a <code>long</code>, at the current row for
   * <code>variable</code>. Returns <code>null</code>, if no value is set for
   * the current row and given column.
   * @throws IllegalArgumentException if <code>variable</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a
   * valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>long</code>
   */
  public long getLong(Variable variable) {
    return getLong("?" + variable.getName());
  }

  /**
   * Returns, as a <code>long</code>, the value at the current row and at the
   * column identified by <code>columnLabel</code>. Returns <code>null</code> if
   * no value is set for the current row and given column.
   *
   * <p/>
   * <strong>Note:</strong> Use the method
   * <code>int colIindex = findColumn(col)</code> once and the version of this
   * method that takes an integer for maximum performance.
   *
   * @param columnLabel the name of the variable that represents the column of
   * interest
   * @return the value, as a <code>long</code>, at the current row and at the
   * column identified by the <code>columnLabel</code>. Returns
   * <code>null</code>, if no value is set for the current row and given column.
   * @throws IllegalArgumentException if <code>columnLabel</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a
   * valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>long</code>
   */
  public long getLong(String columnLabel) {
    return rs.getLong(columnLabel);
  }

  /**
   * Returns, as a <code>float</code>, the value at the current row for
   * <code>variable</code>. Returns <code>null</code> if no value is set for the
   * current row and given column.
   *
   * <p/>
   * <strong>Note:</strong> Use the method
   * <code>int colIindex = findColumn(col)</code> once and the version of this
   * method that takes an integer for maximum performance.
   *
   * @param variable the name of the variable that represents the column of
   * interest
   * @return the value, as a <code>float</code>, at the current row for
   * <code>variable</code>. Returns <code>null</code>, if no value is set for
   * the current row and given column.
   * @throws IllegalArgumentException if <code>variable</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a
   * valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>float</code>
   */
  public float getFloat(Variable variable) {
    return getFloat("?" + variable.getName());
  }

  /**
   * Returns, as a <code>float</code>, the value at the current row and at the
   * column identified by <code>columnLabel</code>. Returns <code>null</code> if
   * no value is set for the current row and given column.
   *
   * <p/>
   * <strong>Note:</strong> Use the method
   * <code>int colIindex = findColumn(col)</code> once and the version of this
   * method that takes an integer for maximum performance.
   *
   * @param columnLabel the name of the variable that represents the column of
   * interest
   * @return the value, as a <code>float</code>, at the current row and at the
   * column identified by the <code>columnLabel</code>. Returns
   * <code>null</code>, if no value is set for the current row and given column.
   * @throws IllegalArgumentException if <code>columnLabel</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a
   * valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>float</code>
   */
  public float getFloat(String columnLabel) {
    try {
      return rs.getFloat(columnLabel);
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Returns, as a <code>double</code>, the value at the current row for
   * <code>variable</code>. Returns <code>null</code> if no value is set for the
   * current row and given column.
   *
   * <p/>
   * <strong>Note:</strong> Use the method
   * <code>int colIindex = findColumn(col)</code> once and the version of this
   * method that takes an integer for maximum performance.
   *
   * @param variable the name of the variable that represents the column of
   * interest
   * @return the value, as a <code>double</code>, at the current row for
   * <code>variable</code>. Returns <code>null</code>, if no value is set for
   * the current row and given column.
   * @throws IllegalArgumentException if <code>variable</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a
   * valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>double</code>
   */
  public double getDouble(Variable variable) {
    return getDouble("?" + variable.getName());
  }

  /**
   * Returns, as a <code>double</code>, the value at the current row and at the
   * column identified by <code>columnLabel</code>. Returns <code>null</code> if
   * no value is set for the current row and given column.
   *
   * <p/>
   * <strong>Note:</strong> Use the method
   * <code>int colIindex = findColumn(col)</code> once and the version of this
   * method that takes an integer for maximum performance.
   *
   * @param columnLabel the name of the variable that represents the column of
   * interest
   * @return the value, as a <code>double</code>, at the current row and at the
   * column identified by the <code>columnLabel</code>. Returns
   * <code>null</code>, if no value is set for the current row and given column.
   * @throws IllegalArgumentException if <code>columnLabel</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a
   * valid row
   * @throws ClassCastException if the value is not convertible to a
   * <code>double</code>
   */
  public double getDouble(String columnLabel) {
    try {
      return rs.getDouble(columnLabel);
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Returns, as a <code>java.sql.Date</code> object, the value in the current
   * row at the column identified by <code>columnLabel</code>, which should be
   * the name of a <code>Variable</code> in the original query. Returns
   * <code>null</code> if no value is set for the current row and given column.
   *
   * This method fails on dates that do not use #$YearFn (i.e. it will not
   * work on skolemized dates, or other forms of dates that don't use the
   * #$YearFn vocabulary).
   *
   * @param columnLabel
   * @return the value at the current row and at the column identified by
   * columnLabel as a Date object
   * @throws IllegalArgumentException if <code>columnLabel</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a
   * valid row
   * @throws ClassCastException if the value is not convertible to a
   * {@link java.sql.Date}
   */
  public Date getDate(String columnLabel) {
    try {
      return rs.getDate(columnLabel);
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Returns, as a <code>java.sql.Date</code> object, the value in the current
   * row for variable. Returns <code>null</code> if no value is set for the
   * current row and given variable.
   *
   * This method fails on dates that do not use #$YearFn (i.e. it will not
   * work on skolemized dates, or other forms of dates that don't use the
   * #$YearFn vocabulary).
   *
   * @param variable
   * @return the value at the current row and at the column identified by
   * variable as a Date object
   * @throws IllegalArgumentException if <code>variable</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a
   * valid row
   * @throws ClassCastException if the value is not convertible to a
   * {@link java.sql.Date}
   */
  public Date getDate(Variable variable) {
    return getDate("?" + variable.getName());
  }

  /**
   * Returns the column index for <code>columnLabel</code>.
   *
   * @param columnLabel the column name to look up
   * @return the column index for the given <code>columnLabel</code>.
   * @throws IllegalArgumentException if called with an invalid
   * <code>columnLabel</code>
   */
  public int findColumn(String columnLabel) {
    try {
      return rs.findColumn(columnLabel);
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Implementation of {@link java.sql.ResultSet#isBeforeFirst()}.
   *
   * @return <code>true</code> if the cursor is before the first row;
   * <code>false</code> if the cursor is at any other position or the result set
   * contains no rows
   */
  public boolean isBeforeFirst() {
    try {
      return rs.isBeforeFirst();
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Implementation of {@link java.sql.ResultSet#isAfterLast()}.
   *
   * @return <code>true</code> if the cursor is after the last row;
   * <code>false</code> if the cursor is at any other position or the result set
   * contains no rows
   */
  public boolean isAfterLast() {
    try {
      return rs.isAfterLast();
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Implementation of {@link java.sql.ResultSet#isFirst()}.
   *
   * @return <code>true</code> if the cursor is on the first row;
   * <code>false</code> otherwise
   */
  public boolean isFirst() {
    try {
      return rs.isFirst();
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Implementation of {@link java.sql.ResultSet#isLast()}.
   *
   * @return <code>true</code> if the cursor is on the last row;
   * <code>false</code> otherwise
   */
  public boolean isLast() {
    try {
      return rs.isLast();
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Implementation of {@link java.sql.ResultSet#beforeFirst()}.
   * <p>
   * Places the cursor just before the first row.
   *
   */
  public void beforeFirst() {
    try {
      rs.beforeFirst();
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Implementation of {@link java.sql.ResultSet#afterLast()}.
   * <p>
   * Places the cursor just after the last row.
   *
   */
  public void afterLast() {
    try {
      rs.afterLast();
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Implementation of {@link java.sql.ResultSet#first()}. Moves the cursor to
   * the first row.
   *
   * @return <code>true</code> if the cursor is on a valid row;
   * <code>false</code> if there are no rows in the result set
   */
  public boolean first() {
    try {
      return rs.first();
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Implementation of {@link java.sql.ResultSet#last()}. Moves the cursor to
   * the last row.
   *
   * @return <code>true</code> if the cursor is on a valid row;
   * <code>false</code> if there are no rows in the result set
   */
  public boolean last() {
    try {
      return rs.last();
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Implementation of {@link java.sql.ResultSet#getRow()}.
   *
   * @return the current row number; <code>0</code> if there is no current row
   */
  public int getRow() {
    try {
      return rs.getRow();
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Implementation of {@link java.sql.ResultSet#absolute(int)}. Moves the
   * cursor to the specified row.
   *
   * @param row the row to which to move the cursor
   * @return <code>true</code> if the cursor is moved to a position in this
   * <code>KBInferenceResultSet</code> object; <code>false</code> if the cursor
   * is before the first row or after the last row
   */
  public boolean absolute(int row) {
    try {
      return rs.absolute(row);
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Implementation of {@link java.sql.ResultSet#relative(int)}. Moves the
   * cursor up or down the specified number of rows.
   *
   * @param rows the number of rows to move the cursor.
   * @return <code>true</code> if the cursor is on a row; <code>false</code>
   * otherwise
   */
  public boolean relative(int rows) {
    try {
      return rs.relative(rows);
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Implementation of {@link java.sql.ResultSet#previous()}. Moves the cursor
   * to the previous row.
   *
   * @return <code>true</code> if the cursor is now positioned on a valid row;
   * <code>false</code> if the cursor is positioned before the first row
   */
  public boolean previous() {
    try {
      return rs.previous();
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Implementation of {@link java.sql.ResultSet#isClosed()}.
   *
   * @return true if this <code>KBInferenceResultSet</code> object is closed;
   * false if it is still open
   */
  public boolean isClosed() {
    try {
      return rs.isClosed();
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }
  private final Logger logger = LoggerFactory.getLogger(KBInferenceResultSet.class.getName());
}
