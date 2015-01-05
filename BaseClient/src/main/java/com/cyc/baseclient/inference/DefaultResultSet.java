package com.cyc.baseclient.inference;

/*
 * #%L
 * File: DefaultResultSet.java
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
import com.cyc.base.BaseClientRuntimeException;
import com.cyc.base.CycAccess;
import com.cyc.base.CycAccessManager;
import com.cyc.base.inference.InferenceWorkerSynch;
import com.cyc.base.inference.InferenceWorker;
import com.cyc.base.CycConnectionException;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.DenotationalTerm;
import com.cyc.base.cycobject.CycVariable;
import com.cyc.base.inference.InferenceIdentifier;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import com.cyc.baseclient.CommonConstants;
import com.cyc.baseclient.CycClientManager;
import com.cyc.base.CycApiException;
import com.cyc.session.CycServer;
import com.cyc.base.cycobject.Fort;
import com.cyc.base.cycobject.Nart;
import com.cyc.base.cycobject.Naut;
import com.cyc.base.inference.InferenceResultSet;
import com.cyc.baseclient.cycobject.CycArrayList;
import com.cyc.baseclient.cycobject.CycConstantImpl;
import com.cyc.baseclient.cycobject.CycSymbolImpl;
import com.cyc.baseclient.cycobject.CycVariableImpl;

/**
 * <P>
 * DefaultResultSet provides a class for easy access and manipulation of
 * inference results. It tries to closely mimic the java.sql.ResultSet class.
 * One difference between java.sql.ResultSet and DefaultResultSet is that
 * DefaultResultSet is zero-based while java.sql.ResultSet is one-based.
 * Example:
 *
 * <code><pre>
 * System.out.println("Starting");
 * CycAccess access = null;
 * try {
 *   access = CycAccessManager.getAccess();
 *   ELMt inferencePSC = CommonConstants.INFERENCE_PSC;
 *   String query = "(#$isa ?X #$Dog)";
 *   InferenceWorkerSynch worker = new DefaultInferenceWorkerSynch(query,
 *       inferencePSC, null, access, 500000);
 *   DefaultResultSet rs = worker.executeQuery();
 *   try {
 *     int indexOfX = rs.findColumn("?X");
 *     while (rs.next()) {
 *       CycObject curDog = rs.getCycObject(indexOfX);
 *       System.out.println("Got dog: " + curDog.cyclify());
 *     }
 *   } finally {
 *     rs.close();
 *   }
 * } catch (Exception e) {
 *   e.printStackTrace();
 * } finally {
 *   if (access != null) {
 *     access.close();
 *   }
 * }
 * System.out.println("Finished");
 * System.out.flush();
 * </pre></code>
 * <p>
 * @author tbrussea
 * @date Mar 22, 2010, 11:55 AM
 * @version $Id: DefaultResultSet.java 155483 2014-12-10 21:56:51Z nwinant $
 */
@SuppressWarnings("deprecation")
public final class DefaultResultSet extends AbstractResultSet {

  //// Constructors

  /**
   * Creates a new <code>InferenceResultSet</code>.
   *
   * @param results The inference results as returned by "new-cyc-query".
   * @param inferenceWorker The inference worker that produced this result set.
   */
  public DefaultResultSet(List<Object> results, InferenceWorker inferenceWorker) {
    calcRows(results);
    this.inferenceWorker = inferenceWorker;
  }

  /**
   * Creates a new <code>InferenceResultSet</code>.
   *
   * @param results The inference results as returned by "new-cyc-query".
   */
  public DefaultResultSet(List<Object> results) {
    calcRows(results);
    this.inferenceWorker = null;
  }

  //// Public Area

  /**
   * Returns whether the <code>InferenceWorker</code> associated with this
   * <code>DefaultResultSet</code> object is finished working. Will return
   * <code>true</code> if there is no <code>InferenceWorker</code>.
   *
   * @return <code>true</code> if the <code>InferenceWorker</code> associated with this
   * <code>DefaultResultSet</code> or if the <code>InferenceWorker</code> is not set;
 <code>false</code> if the <code>InferenceWorker</code> associated with this
   * <code>DefaultResultSet</code> might possibly produce more results.
   */
  public boolean isInferenceComplete() {
    if (inferenceWorker != null) {
      return inferenceWorker.isDone();
    }
    return true;
  }

  public InferenceIdentifier getInferenceIdentifier() {
    if (inferenceWorker != null) {
      return inferenceWorker.getInferenceIdentifier();
    }
    return null;
  }

  /**
   * Releases this <code>DefaultResultSet</code> object's server side
   * inference resources. Failure to close a result set may leave significant
   * resources hanging around the server until the client <code>CycClient</code>
   * object is closed.
   * <P>
   * Calling the method <code>close</code> on a <code>DefaultResultSet</code>
   * object that is already closed is a no-op.
   *
   * @throws IOException if a communication error occurs
   * @throws TimeOutException if doesn't get a response from
            the server before an excessive amount of time has elapsed
   * @throws CycApiException if an internal server error occurs
   */
  @Override
  public synchronized void close() {
    if (isClosed()) {
      return;
    }
    setIsClosed(true);
    if (inferenceWorker != null) {
      try {
        inferenceWorker.releaseInferenceResources(getCloseTimeout());
      } catch (CycConnectionException ioe) {
        throw new BaseClientRuntimeException(ioe.getMessage(), ioe);
      }
    }
  }
    
  @Override
  public long getCloseTimeout() {
    if (this.closeTimeout != null) {
      return this.closeTimeout;
    }
    if ((this.inferenceWorker != null) 
            && (this.inferenceWorker.getTimeoutMsecs() > DEFAULT_CLOSE_TIMEOUT_MS)) {
      return this.inferenceWorker.getTimeoutMsecs();
    } else {
      return DEFAULT_CLOSE_TIMEOUT_MS;
    }
  }
  
  @Override
  public void setCloseTimeout(Long timeoutMS) {
    this.closeTimeout = timeoutMS;
  }

  /**
   * Returns, as an <code>Object</code>, the value at the current row and at the 
 column identified by <code>col</code> which should be a 
   * <code>CycVariableImpl</code> in the original query. Returns <code>null</code> 
 if no value is set for the current row and given column.

 <p/><strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once
 and the version of this method that takes an integer for maximum performance.
   *
   * @param col the name of the variable that represents the column of interest
   * @return the value, as an <code>Object</code>, at the current row and 
 at the column identified by the <code>col</code> which is a
   * <code>CycVariableImpl</code> in the original query. Returns <code>null</code>,
 if no value is set for the current row and given column.
   * @throws CycApiException if called on a closed result set
   * @throws IllegalArgumentException if <code>col</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   */
  public Object getObject(CycVariable col)
      throws CycApiException, IllegalArgumentException, ArrayIndexOutOfBoundsException {
    return getObject(findColumnStrict(col));
  }

  /**
   * Returns, as a <code>DenotationalTerm</code>, the value at the current row and at the
 column identified by <code>col</code> which should be a 
   * <code>CycVariableImpl</code> in the original query. Returns <code>null</code> 
 if no value is set for the current row and given column.

 <p/><strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once
 and the version of this method that takes an integer for maximum performance.
   *
   * @param col the name of the variable that represents the column of interest
   * @return the value, as a <code>DenotationalTerm</code>, at the current row and
 at the column identified by the <code>col</code> which is a
   * <code>CycVariableImpl</code> in the original query. Returns <code>null</code>,
 if no value is set for the current row and given column.
   * @throws CycApiException if called on a closed result set
   * @throws IllegalArgumentException if <code>col</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
 <code>DenotationalTerm</code>
   */
  public DenotationalTerm getDenotationalTerm(CycVariable col)
      throws CycApiException, IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException {
    return getDenotationalTerm(findColumnStrict(col));
  }

  /**
   * Returns, as a <code>String</code>, the value at the current row and at the
 column identified by <code>col</code> which should be a
   * <code>CycVariableImpl</code> in the original query. Returns <code>null</code>
 if no value is set for the current row and given column.

 <p/><strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once
 and the version of this method that takes an integer for maximum performance.
   *
   * @param col the name of the variable that represents the column of interest
   * @return the value, as a <code>String</code>, at the current row and
 at the column identified by the <code>col</code> which is a
   * <code>CycVariableImpl</code> in the original query. Returns <code>null</code>,
 if no value is set for the current row and given column.
   * @throws CycApiException if called on a closed result set
   * @throws IllegalArgumentException if <code>col</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
 <code>String</code>
   */
  public String getString(CycVariable col)
      throws CycApiException, IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException {
    return getString(findColumnStrict(col));
  }

  /**
   * Returns, as a <code>long</code>, the value at the current row and at the
 column identified by <code>col</code> which should be a
   * <code>CycVariableImpl</code> in the original query. Returns <code>null</code>
 if no value is set for the current row and given column.

 <p/><strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once
 and the version of this method that takes an integer for maximum performance.
   *
   * @param col the name of the variable that represents the column of interest
   * @return the value, as a <code>long</code>, at the current row and
 at the column identified by the <code>col</code> which is a
   * <code>CycVariableImpl</code> in the original query. Returns <code>null</code>,
 if no value is set for the current row and given column.
   * @throws CycApiException if called on a closed result set
   * @throws IllegalArgumentException if <code>col</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
 <code>long</code>
   */
  public long getLong(CycVariable col)
      throws CycApiException, IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException {
    return getLong(findColumnStrict(col));
  }

  /**
   * Returns, as a <code>int</code>, the value at the current row and at the
 column identified by <code>col</code> which should be a
   * <code>CycVariableImpl</code> in the original query. Returns <code>null</code>
 if no value is set for the current row and given column.

 <p/><strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once
 and the version of this method that takes an integer for maximum performance.
   *
   * @param col the name of the variable that represents the column of interest
   * @return the value, as a <code>int</code>, at the current row and
 at the column identified by the <code>col</code> which is a
   * <code>CycVariableImpl</code> in the original query. Returns <code>null</code>,
 if no value is set for the current row and given column.
   * @throws CycApiException if called on a closed result set
   * @throws IllegalArgumentException if <code>col</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
 <code>int</code>
   */
  public int getInt(CycVariable col)
      throws CycApiException, IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException {
    return getInt(findColumnStrict(col));
  }

  /**
   * Returns, as a <code>double</code>, the value at the current row and at the
 column identified by <code>col</code> which should be a
   * <code>CycVariableImpl</code> in the original query. Returns <code>null</code>
 if no value is set for the current row and given column.

 <p/><strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once
 and the version of this method that takes an integer for maximum performance.
   *
   * @param col the name of the variable that represents the column of interest
   * @return the value, as a <code>double</code>, at the current row and
 at the column identified by the <code>col</code> which is a
   * <code>CycVariableImpl</code> in the original query. Returns <code>null</code>,
 if no value is set for the current row and given column.
   * @throws CycApiException if called on a closed result set
   * @throws IllegalArgumentException if <code>col</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
 <code>double</code>
   */
  public double getDouble(CycVariable col)
      throws CycApiException, IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException {
    return getDouble(findColumnStrict(col));
  }

  /**
   * Returns, as a <code>float</code>, the value at the current row and at the
 column identified by <code>col</code> which should be a
   * <code>CycVariableImpl</code> in the original query. Returns <code>null</code>
 if no value is set for the current row and given column.

 <p/><strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once
 and the version of this method that takes an integer for maximum performance.
   *
   * @param col the name of the variable that represents the column of interest
   * @return the value, as a <code>float</code>, at the current row and
 at the column identified by the <code>col</code> which is a
   * <code>CycVariableImpl</code> in the original query. Returns <code>null</code>,
 if no value is set for the current row and given column.
   * @throws CycApiException if called on a closed result set
   * @throws IllegalArgumentException if <code>col</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
 <code>float</code>
   */
  public float getFloat(CycVariable col)
      throws CycApiException, IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException {
    return getFloat(findColumnStrict(col));
  }

  /**
   * Returns, as a <code>boolean</code>, the value at the current row and at the
 column identified by <code>col</code> which should be a
   * <code>CycVariableImpl</code> in the original query. Returns <code>null</code>
 if no value is set for the current row and given column.

 <p/><strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once
 and the version of this method that takes an integer for maximum performance.
   *
   * @param col the name of the variable that represents the column of interest
   * @return the value, as a <code>boolean</code>, at the current row and
 at the column identified by the <code>col</code> which is a
   * <code>CycVariableImpl</code> in the original query. Returns <code>null</code>,
 if no value is set for the current row and given column.
   * @throws CycApiException if called on a closed result set
   * @throws IllegalArgumentException if <code>col</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
 <code>boolean</code>
   */
  public boolean getBoolean(CycVariable col)
      throws CycApiException, IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException {
    return getBoolean(findColumnStrict(col));
  }

  /**
   * Returns, as a <code>CycConstantImpl</code>, the value at the current row and at the
 column identified by <code>col</code> which should be a
   * <code>CycVariableImpl</code> in the original query. Returns <code>null</code>
 if no value is set for the current row and given column.

 <p/><strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once
 and the version of this method that takes an integer for maximum performance.
   *
   * @param col the name of the variable that represents the column of interest
   * @return the value, as a <code>CycConstantImpl</code>, at the current row and
 at the column identified by the <code>col</code> which is a
   * <code>CycVariableImpl</code> in the original query. Returns <code>null</code>,
 if no value is set for the current row and given column.
   * @throws CycApiException if called on a closed result set
   * @throws IllegalArgumentException if <code>col</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
 <code>CycConstantImpl</code>
   */
  public CycConstantImpl getConstant(CycVariable col)
      throws CycApiException, IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException {
    return getConstant(findColumnStrict(col));
  }

  /**
   * Returns, as a <code>CycObject</code>, the value at the current row and at the
 column identified by <code>col</code> which should be a
   * <code>CycVariableImpl</code> in the original query. Returns <code>null</code>
 if no value is set for the current row and given column.

 <p/><strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once
 and the version of this method that takes an integer for maximum performance.
   *
   * @param col the name of the variable that represents the column of interest
   * @return the value, as a <code>CycObject</code>, at the current row and
 at the column identified by the <code>col</code> which is a
   * <code>CycVariableImpl</code> in the original query. Returns <code>null</code>,
 if no value is set for the current row and given column.
   * @throws CycApiException if called on a closed result set
   * @throws IllegalArgumentException if <code>col</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
 <code>CycObject</code>
   */
  public CycObject getCycObject(CycVariable col)
      throws CycApiException, IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException {
    return getCycObject(findColumnStrict(col));
  }

  /**
   * Returns, as a <code>Fort</code>, the value at the current row and at the
 column identified by <code>col</code> which should be a
   * <code>CycVariableImpl</code> in the original query. Returns <code>null</code>
 if no value is set for the current row and given column.

 <p/><strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once
 and the version of this method that takes an integer for maximum performance.
   *
   * @param col the name of the variable that represents the column of interest
   * @return the value, as a <code>Fort</code>, at the current row and
 at the column identified by the <code>col</code> which is a
   * <code>CycVariableImpl</code> in the original query. Returns <code>null</code>,
 if no value is set for the current row and given column.
   * @throws CycApiException if called on a closed result set
   * @throws IllegalArgumentException if <code>col</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
 <code>Fort</code>
   */
  public Fort getFort(CycVariable col)
      throws CycApiException, IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException {
    return getFort(findColumnStrict(col));
  }

  /**
   * Returns, as a <code>CycArrayList</code>, the value at the current row and at the
 column identified by <code>col</code> which should be a
   * <code>CycVariableImpl</code> in the original query. Returns <code>null</code>
 if no value is set for the current row and given column.

 <p/><strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once
 and the version of this method that takes an integer for maximum performance.
   *
   * @param col the name of the variable that represents the column of interest
   * @return the value, as a <code>CycArrayList</code>, at the current row and
 at the column identified by the <code>col</code> which is a
   * <code>CycVariableImpl</code> in the original query. Returns <code>null</code>,
 if no value is set for the current row and given column.
   * @throws CycApiException if called on a closed result set
   * @throws IllegalArgumentException if <code>col</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
 <code>CycArrayList</code>
   */
  public CycArrayList<Object> getList(CycVariable col)
      throws CycApiException, IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException {
    return getList(findColumnStrict(col));
  }

  /**
   * Returns, as a <code>v</code>, the value at the current row and at the
 column identified by <code>col</code> which should be a
   * <code>CycVariableImpl</code> in the original query. Returns <code>null</code>
 if no value is set for the current row and given column.

 <p/><strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once
 and the version of this method that takes an integer for maximum performance.
   *
   * @param col the name of the variable that represents the column of interest
   * @return the value, as a <code>Nart</code>, at the current row and
 at the column identified by the <code>col</code> which is a
   * <code>CycVariableImpl</code> in the original query. Returns <code>null</code>,
 if no value is set for the current row and given column.
   * @throws CycApiException if called on a closed result set
   * @throws IllegalArgumentException if <code>col</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
 <code>NartImpl</code>
   */
  public Nart getNart(CycVariable col)
      throws CycApiException, IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException {
    return getNart(findColumnStrict(col));
  }

  /**
   * Returns, as a <code>Naut</code>, the value at the current row and at the
 column identified by <code>col</code> which should be a
   * <code>CycVariableImpl</code> in the original query. Returns <code>null</code>
 if no value is set for the current row and given column.

 <p/><strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once
 and the version of this method that takes an integer for maximum performance.
   *
   * @param col the name of the variable that represents the column of interest
   * @return the value, as a <code>Naut</code>, at the current row and
 at the column identified by the <code>col</code> which is a
   * <code>CycVariableImpl</code> in the original query. Returns <code>null</code>,
 if no value is set for the current row and given column.
   * @throws CycApiException if called on a closed result set
   * @throws IllegalArgumentException if <code>col</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
 <code>Naut</code>
   */
  public Naut getNaut(CycVariable col)
      throws CycApiException, IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException {
    return getNaut(findColumnStrict(col));
  }

  /**
   * Returns, as a <code>java.util.Date</code> object, the value at the current row and at the
 column identified by <code>col</code> which should be a
   * <code>CycVariableImpl</code> in the original query. Returns <code>null</code>
 if no value is set for the current row and given column.

 <p/><strong>Note:</strong> Use the method <code>int colIindex = findColumn(col)</code> once
 and the version of this method that takes an integer for maximum performance.
   *
   * @param col the name of the variable that represents the column of interest
   * @return the value, as a <code>java.util.Date</code>, at the current row and
 at the column identified by the <code>col</code> which is a
   * <code>CycVariableImpl</code> in the original query. Returns <code>null</code>,
 if no value is set for the current row and given column.
   * @note Fails on dates that are not CycNauts using #$YearFn (i.e. it will not
   * work on skolemized dates, or other forms of dates that don't use the #$YearFn vocabulary).
   * @throws CycApiException if called on a closed result set
   * @throws IllegalArgumentException if <code>col</code> is not valid
   * @throws ArrayIndexOutOfBoundsException if the current cursor is not on a valid row
   * @throws ClassCastException if the value is not convertible to a
 <code>java.util.Date</code>
   */
  public Date getDate(CycVariable col)
      throws CycApiException, IllegalArgumentException, ArrayIndexOutOfBoundsException, ClassCastException {
    return getDate(findColumnStrict(col));
  }

  
  
  /**
   * Returns the one-based column index for <code>col</code>.
   *
   * @param col the column variable to look up
   * @return the column index for the given <code>col</code> variable.
   * @throws IllegalArgumentException if called with an invalid <code>col</code>
   */
  public int findColumn(CycVariable col) throws IllegalArgumentException {
    return findColumn(col.toString());
  }

  //// Protected Area

  /**
   * Returns the one-based column index for the given <code>col</code>. This version differs
 from the non-strict version in that it throws detailed error messages.
   *
   * @param col the index name to look up
   * @return the column index for the given <code>col</code>
   * @throws IllegalArgumentException if called with an invalid <code>col</code>
   */
  protected int findColumnStrict(CycVariable col) throws IllegalArgumentException {
    if (col == null) {
      throw new IllegalArgumentException("Got null column name.");
    }
    return findColumnStrict(col.toString());
  }

  /**
   * Processes the results from "new-cyc-query" to store them conveniently in
 this DefaultResultSet.
   *
   * @param results the results from "new-cyc-query"
   */
  @SuppressWarnings("unchecked")
  protected void calcRows(List results) {
    if (results.size() == 0){
      setTruthValue(Boolean.FALSE);
      return;
    }
    if (results.size() == 1) {
      Object val = results.get(0);
      if ((val instanceof CycSymbolImpl) && (((CycSymbolImpl)val).toString().equalsIgnoreCase("nil"))) {
        setTruthValue(Boolean.TRUE);
        return;
      }
    }
    for (List<CycArrayList> bindingSet : (List<List>)results) {
      List<Object> row = addEmptyRow();
      for (CycArrayList binding : bindingSet) {
        CycVariableImpl colVar = (CycVariableImpl)binding.get(0);
        int colIndex = possiblyAddColVar(colVar);
        String col = colVar.toString();
        Object val = binding.rest();
        row.set(colIndex, val);
      }
    }
  }

  /**
   * If the given column name isn't known, add it and make sure
 current results have an extra column added. Return the new
 (or existing) column index for the <code>colVar</code>.
   *
   * @return the column index of <code>colVar</code>
   * @param colVar the CycVariableImpl which represents the column to add
   */
  protected int possiblyAddColVar(CycVariable colVar) {
    String col = colVar.toString();
    int colIndex = -1;
    List<String> columnNames = getColumnNamesUnsafe();
    if ((colIndex = columnNames.indexOf(col)) < 0) {
      columnNames.add(col);
      for (List<Object> row : getRS()) {
        row.add(null);
      }
      return columnNames.size() - 1;
    }
    return colIndex;
  }

  /**
   * Wait until the inference work completes.
   */
  @Override
  protected void waitTillProcessingDone() {
    if (inferenceWorker == null) {
      return;
    }
    if (inferenceWorker.isDone()) {
      return;
    }
    while (true) {
      synchronized (inferenceWorker) {
        try {
          inferenceWorker.wait(10);
        } catch (InterruptedException ie) {
          // @todo set warning
          return;
        }
        if (inferenceWorker.isDone()) {
          return;
        }
      }
    }
  }

  //// Private Area

  //// Internal Rep

  /** 
   * Maximum time to wait in milliseconds when closing the inference worker, if
   * closeTimeout is null.
   */
  private static final long DEFAULT_CLOSE_TIMEOUT_MS = 10000;

  /** 
   * The user-specified time to wait in milliseconds when closing the inference 
   * worker. May be null.
   */
  private Long closeTimeout = null;

  
  /** 
   * The inference worker for whose results this DefaultResultSet represents.
   * The value may be null.
   */
  private final InferenceWorker inferenceWorker;

  //// Main
  
  /**
   * Provides a working demonstration and sanity check main method.
   *
   * @param args the command line arguments (ignored)
   */
  public static void main(String[] args) {
    System.out.println("Starting");
    CycAccess access = null;
    try {
      access = CycAccessManager.getAccess();
      String query = "(#$and (#$isa ?X #$Dog) (#$isa ?Y #$Cat))";
      InferenceWorkerSynch worker = new DefaultInferenceWorkerSynch(query,
        CommonConstants.INFERENCE_PSC, null, access, 50000);
      InferenceResultSet rs = worker.executeQuery();
      try {
        int indexOfX = rs.findColumn("?X");
        int indexOfY = rs.findColumn("?Y");
        while (rs.next()) {
          CycObject curDog = rs.getCycObject(indexOfX);
          CycObject curCat = rs.getCycObject(indexOfY);
          System.out.println("Got dog/cat pair: " + curDog.cyclify() + " " + curCat.cyclify());
        }
        System.out.println("Result Set: " + rs);
      } finally {
        rs.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (access != null) {
        access.close();
      }
    }
    System.out.println("Finished");
    System.out.flush();
    System.exit(0);
  }

  public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
