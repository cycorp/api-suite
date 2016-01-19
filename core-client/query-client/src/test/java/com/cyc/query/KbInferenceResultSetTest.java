package com.cyc.query;

/*
 * #%L
 * File: KbInferenceResultSetTest.java
 * Project: Query Client
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
import com.cyc.query.exception.QueryConstructionException;
import com.cyc.base.exception.CycConnectionException;
import com.cyc.kb.FirstOrderCollection;
import com.cyc.kb.KbCollection;
import com.cyc.kb.KbIndividual;
import com.cyc.kb.KbObject;
import static com.cyc.query.TestUtils.*;
import com.cyc.kb.client.Constants;
import com.cyc.kb.client.SentenceImpl;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbException;
import com.cyc.kb.exception.KbRuntimeException;
import com.cyc.kb.exception.KbTypeException;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author daves
 */
public class KbInferenceResultSetTest {

  private final SentenceImpl genlsThingThing;
  private final SentenceImpl commentBillClintonX;
  private final SentenceImpl evaluateThreeMinusOne;
  private final SentenceImpl evaluateOneMinusPointFive;

  public KbInferenceResultSetTest() throws KbTypeException, CreateException {
    genlsThingThing = new SentenceImpl(Constants.genls(), testConstants().thing, testConstants().thing);
    commentBillClintonX = new SentenceImpl(testConstants().comment, testConstants().billClinton, X);
    evaluateThreeMinusOne = new SentenceImpl(testConstants().evaluate, X, testConstants().threeMinusOne);
    evaluateOneMinusPointFive = new SentenceImpl(testConstants().evaluate, X, testConstants().oneMinusPointFive);
    equalsXTrue = new SentenceImpl(testConstants().cycEquals, X, testConstants().cycTrue);
  }

  @BeforeClass
  public static void setUpClass() throws KbException, IOException, CycConnectionException {
    TestUtils.ensureConstantsInitialized();
  }

  @AfterClass
  public static void tearDownClass() {
  }

  private Query q = null;

  @After
  public void tearDown() {
    if (q != null) {
      q.close();
    }
  }

  @Before
  public void setup() throws QueryConstructionException {
    resetQ();
  }

  /**
   * Test of getObject method, of class QueryResultSetImpl.
   */
  @Test
  public void testGetObject_int_Class() {
    System.out.println("getObject");
    QueryResultSet resultSet = q.getResultSet();
    assertTrue(resultSet.next());
    Object result = resultSet.getObject(1, KbCollection.class);
    assertNotNull(result);
  }

  /**
   * Test of getKBObject method, of class QueryResultSetImpl.
   */
  @Test
  public void testGetKBObject_int() throws IllegalArgumentException, KbException {
    System.out.println("getKBObject");
    QueryResultSet resultSet = q.getResultSet();
    assertTrue(resultSet.next());
    KbObject result = resultSet.getKbObject(resultSet.findColumn(X), KbCollection.class);
    assertNotNull(result);
  }

  /**
   * Test of getObject method, of class QueryResultSetImpl.
   */
  @Test
  public void testGetObject_String_Class() {
    System.out.println("getObject");
    QueryResultSet resultSet = q.getResultSet();
    assertTrue(resultSet.next());
    KbCollection result = (KbCollection)resultSet.getObject("?" + X.getName(), KbCollection.class);
    assertNotNull(result);
  }

  /**
   * Test of getObject method, of class QueryResultSetImpl.
   */
  @Test
  public void testGetObject_int() {
    System.out.println("getObject");
    QueryResultSet resultSet = q.getResultSet();
    assertTrue(resultSet.next());
    Object result = resultSet.getObject(1, KbCollection.class);
    assertNotNull(result);
  }

  /**
   * Test of getObject method, of class QueryResultSetImpl.
   */
  @Test
  public void testGetObject_String() {
    System.out.println("getObject");
    QueryResultSet resultSet = q.getResultSet();
    assertTrue(resultSet.next());
    KbCollection result = (KbCollection) resultSet.getObject("?" + X.getName(), KbCollection.class);
    assertNotNull(result);
  }

  /**
   * Test of getKBObject method, of class QueryResultSetImpl.
   */
  @Test
  public void testGetKBObject_String() throws IllegalArgumentException, KbException {
    assumeNotOpenCyc();
    // TODO: currently throwing ClassCastException, which it should not.
    System.out.println("getKBObject");
    String columnLabel = "?X";
    QueryResultSet resultSet = q.getResultSet();
    resultSet.next();
    KbCollection result = (KbCollection)resultSet.getKbObject(columnLabel, KbCollection.class);
    assertTrue(result instanceof KbObject);
  }

  /**
   * Test of getKBObject method, of class QueryResultSetImpl.
   */
  @Test(expected = ClassCastException.class)
  public void testGetKBObject_String_Exception() throws IllegalArgumentException, KbException {
    System.out.println("getKBObject Exception");
    String columnLabel = "?X";
    QueryResultSet resultSet = q.getResultSet();
    resultSet.next();
    // The actual answers are all collections, so this should throw an exception:
    KbIndividual result = (KbIndividual)resultSet.getKbObject(columnLabel, KbIndividual.class);
  }

  /**
   * Test of getColumnNames method, of class QueryResultSetImpl.
   */
  @Test
  public void testGetColumnNames() {
    System.out.println("getKBObject");
    QueryResultSet resultSet = q.getResultSet();
    List<String> result = resultSet.getColumnNames();
    assertEquals(result.size(), 1);
    assertEquals(result.get(0).toString(), "?X");
    q.close();
  }

  private void resetQ() throws QueryConstructionException, KbRuntimeException {
    q = QueryFactory.getQuery(testConstants().genlsAnimalX, Constants.inferencePSCMt());
  }

  /**
   * Test of isInferenceComplete method, of class QueryResultSetImpl.
   */
  @Test
  public void testIsInferenceComplete() throws CycConnectionException, QueryConstructionException {
    System.out.println("isInferenceComplete");
    resetQ();
    QueryResultSet resultSet = q.getResultSet();
    assertTrue(resultSet.isInferenceComplete());
    q.close();
  }

  /**
   * Test of close method, of class QueryResultSetImpl.
   */
  @Test
  public void testClose() {
    System.out.println("close");
    q.setContinuable(true);
    QueryResultSet resultSet = q.getResultSet();
    resultSet.close();
    assertTrue(resultSet.isClosed());
    //@todo Make sure the inference and problem store were really removed from the Cyc server.
  }

  /**
   * Test of next method, of class QueryResultSetImpl.
   */
  @Test
  public void testNext() {
    System.out.println("next");
    QueryResultSet resultSet = q.getResultSet();
    assertTrue(q.isProvable());
    while (resultSet.getRow() < q.getAnswerCount()) {
      assertTrue(resultSet.next());
    }
    assertFalse(resultSet.next());
  }

  /**
   * Test of findColumn method, of class QueryResultSetImpl.
   */
  @Test
  public void testFindColumn_Variable() {
    System.out.println("findColumn");
    QueryResultSet resultSet = q.getResultSet();
    assertEquals(1, resultSet.findColumn(X));
  }

  /**
   * Test of getCurrentRowCount method, of class QueryResultSetImpl.
   */
  @Test
  public void testGetCurrentRowCount() throws QueryConstructionException, KbException {
    System.out.println("getCurrentRowCount");
    q = QueryFactory.getQuery(equalsXTrue, inferencePSC);
    QueryResultSet resultSet = q.getResultSet();
    int result = resultSet.getCurrentRowCount();
    assertEquals(1, result);
    resultSet.close();

    q = QueryFactory.getQuery(genlsThingThing, inferencePSC);
    resultSet = q.getResultSet();
    assertEquals("Expected no rows for " + q.getQuerySentence(), 0, resultSet.getCurrentRowCount().longValue());
    resultSet.close();
    //@todo add more tests for asynchronous queries.
  }

  private QueryTestConstants testConstants() throws KbRuntimeException {
    return QueryTestConstants.getInstance();
  }

  /**
   * Test of getTruthValue method, of class QueryResultSetImpl.
   */
  @Test
  public void testGetTruthValue() throws QueryConstructionException {
    System.out.println("getTruthValue");
    q = QueryFactory.getQuery(genlsThingThing, inferencePSC);
    QueryResultSet resultSet = q.getResultSet();
    boolean expResult = true;
    boolean result = resultSet.getTruthValue();
    assertEquals(expResult, result);
    resultSet.close();
  }

  /**
   * Test of getString method, of class QueryResultSetImpl.
   */
  @Test
  public void testGetString_int() throws QueryConstructionException {
    System.out.println("getString");
    q = QueryFactory.getQuery(commentBillClintonX, inferencePSC);
    QueryResultSet resultSet = q.getResultSet();
    resultSet.next();
    String result = resultSet.getString(1);
    assertTrue(result instanceof String);
  }

  /**
   * Test of getString method, of class QueryResultSetImpl.
   */
  @Test
  public void testGetString_Variable() throws QueryConstructionException {
    System.out.println("getString");
    q = QueryFactory.getQuery(commentBillClintonX, inferencePSC);
    QueryResultSet resultSet = q.getResultSet();
    resultSet.next();
    String result = resultSet.getString(X);
    assertTrue(result instanceof String);
  }

  /**
   * Test of getBoolean method, of class QueryResultSetImpl.
   */
  @Test
  public void testGetBoolean_int() throws QueryConstructionException {
    System.out.println("getBoolean");
    q = QueryFactory.getQuery(equalsXTrue, inferencePSC);
    int columnIndex = 1;
    QueryResultSet resultSet = q.getResultSet();
    resultSet.next();
    boolean expResult = true;
    boolean result = resultSet.getBoolean(columnIndex);
    assertEquals(expResult, result);
  }
  private final SentenceImpl equalsXTrue;

  /**
   * Test of getInt method, of class QueryResultSetImpl.
   */
  @Test
  public void testGetInt_int() throws QueryConstructionException {
    System.out.println("getInt");
    q = QueryFactory.getQuery(evaluateThreeMinusOne, inferencePSC);
    int columnIndex = 1;
    QueryResultSet resultSet = q.getResultSet();
    resultSet.next();
    int expResult = 2;
    int result = resultSet.getInt(columnIndex);
    assertEquals(expResult, result);
  }

  /**
   * Test of getInt method, of class QueryResultSetImpl.
   */
  @Test
  public void testGetInt_String() throws QueryConstructionException {
    System.out.println("getInt");
    q = QueryFactory.getQuery("(evaluate ?X (DifferenceFn 3 1))");
    QueryResultSet resultSet = q.getResultSet();
    resultSet.next();
    int expResult = 2;
    int result = resultSet.getInt("?X");
    assertEquals(expResult, result);
  }

  /**
   * Test of getLong method, of class QueryResultSetImpl.
   */
  @Test
  public void testGetLong_int() throws QueryConstructionException {
    System.out.println("getLong");
    q = QueryFactory.getQuery("(evaluate ?X (DifferenceFn 3 1))");
    int columnIndex = 1;
    QueryResultSet resultSet = q.getResultSet();
    resultSet.next();
    long expResult = 2;
    long result = resultSet.getLong(columnIndex);
    assertEquals(expResult, result);
  }

  /**
   * Test of getLong method, of class QueryResultSetImpl.
   */
  @Test
  public void testGetLong_String() throws QueryConstructionException {
    System.out.println("getLong");
    q = QueryFactory.getQuery("(evaluate ?X (DifferenceFn 3 1))");
    QueryResultSet resultSet = q.getResultSet();
    resultSet.next();
    long expResult = 2;
    long result = resultSet.getLong("?X");
    assertEquals(expResult, result);
  }

  /**
   * Test of getFloat method, of class QueryResultSetImpl.
   */
  @Test
  public void testGetFloat_int() throws QueryConstructionException {
    System.out.println("getFloat");
    q = QueryFactory.getQuery(evaluateOneMinusPointFive, Constants.baseKbMt());
    int columnIndex = 1;
    QueryResultSet resultSet = q.getResultSet();
    resultSet.next();
    float expResult = 0.5F;
    float result = resultSet.getFloat(columnIndex);
    assertEquals(expResult, result, 0);
  }

  /**
   * Test of getFloat method, of class QueryResultSetImpl.
   */
  @Test
  public void testGetFloat_String() throws QueryConstructionException {
    System.out.println("getFloat");
    q = QueryFactory.getQuery(evaluateOneMinusPointFive, Constants.baseKbMt());
    QueryResultSet resultSet = q.getResultSet();
    resultSet.next();
    float expResult = 0.5F;
    float result = resultSet.getFloat("?X");
    assertEquals(expResult, result, 0);
  }

  /**
   * Test of getDouble method, of class QueryResultSetImpl.
   */
  @Test
  public void testGetDouble_int() throws QueryConstructionException {
    System.out.println("getDouble");
    q = QueryFactory.getQuery(evaluateOneMinusPointFive, Constants.baseKbMt());
    int columnIndex = 1;
    QueryResultSet resultSet = q.getResultSet();
    resultSet.next();
    double expResult = 0.5D;
    double result = resultSet.getDouble(columnIndex);
    assertEquals(expResult, result, 0);
  }

  /**
   * Test of getDouble method, of class QueryResultSetImpl.
   */
  @Test
  public void testGetDouble_String() throws QueryConstructionException {
    System.out.println("getDouble");
    q = QueryFactory.getQuery(evaluateOneMinusPointFive, Constants.baseKbMt());
    QueryResultSet resultSet = q.getResultSet();
    resultSet.next();
    double expResult = 0.5D;
    double result = resultSet.getDouble("?X");
    assertEquals(expResult, result, 0);
  }

  /**
   * Test of getString method, of class QueryResultSetImpl.
   */
  @Test
  public void testGetString_String() throws QueryConstructionException {
    System.out.println("getString");
    q = QueryFactory.getQuery(commentBillClintonX, inferencePSC);
    QueryResultSet resultSet = q.getResultSet();
    resultSet.next();
    String result = resultSet.getString("?X");
    assertTrue(result instanceof String);
  }

  /**
   * Test of getBoolean method, of class QueryResultSetImpl.
   */
  @Test
  public void testGetBoolean_String() throws QueryConstructionException {
    System.out.println("getBoolean");
    q = QueryFactory.getQuery("(equals ?X True)");
    String var = "?X";
    QueryResultSet resultSet = q.getResultSet();
    resultSet.next();
    boolean expResult = true;
    boolean result = resultSet.getBoolean(var);
    assertEquals(expResult, result);
  }

  /**
   * Test of getDate method, of class QueryResultSetImpl.
   */
  @Test
  public void testGetDate_String() throws QueryConstructionException {
    System.out.println("getDate");
    //KBInferenceResultSet resultSet = new QueryImpl("(#$equals ?NOW (#$IndexicalReferentFn #$Now-Indexical))").getResultSet();
    QueryResultSet resultSet = QueryFactory.getQuery("(#$indexicalReferent #$Now-Indexical ?NOW)").getResultSet();
    try {
      resultSet.next();
      assertTrue(resultSet.getDate("?NOW") instanceof Date);
    } finally {
      resultSet.close();
    }
  }

  /**
   * Test of findColumn method, of class QueryResultSetImpl.
   */
  @Test
  public void testFindColumn_String() {
    System.out.println("findColumn");
    QueryResultSet resultSet = q.getResultSet();
    assertEquals(1, resultSet.findColumn("?X"));
  }

  /**
   * Test of isBeforeFirst method, of class QueryResultSetImpl.
   */
  @Test
  public void testIsBeforeFirst() {
    System.out.println("isBeforeFirst");
    QueryResultSet resultSet = q.getResultSet();
    assertTrue(resultSet.isBeforeFirst());
    resultSet.last();
    assertFalse(resultSet.isBeforeFirst());
  }

  /**
   * Test of isAfterLast method, of class QueryResultSetImpl.
   */
  @Test
  public void testIsAfterLast() {
    System.out.println("isAfterLast");
    QueryResultSet resultSet = q.getResultSet();
    assertFalse(resultSet.isAfterLast());
    resultSet.last();
    assertFalse(resultSet.isAfterLast());
    resultSet.next();
    assertTrue(resultSet.isAfterLast());
  }

  /**
   * Test of isFirst method, of class QueryResultSetImpl.
   */
  @Test
  public void testIsFirst() {
    System.out.println("isFirst");
    QueryResultSet resultSet = q.getResultSet();
    resultSet.next();
    assertTrue(resultSet.isFirst());
    resultSet.last();
    assertFalse(resultSet.isFirst());
  }

  /**
   * Test of isLast method, of class QueryResultSetImpl.
   */
  @Test
  public void testIsLast() {
    System.out.println("isLast");
    QueryResultSet resultSet = q.getResultSet();
    assertFalse(resultSet.isLast());
    resultSet.last();
    assertTrue(resultSet.isLast());
  }

  /**
   * Test of beforeFirst method, of class QueryResultSetImpl.
   */
  @Test
  public void testBeforeFirst() {
    System.out.println("beforeFirst");
    QueryResultSet resultSet = q.getResultSet();
    resultSet.beforeFirst();
    assertTrue(resultSet.isBeforeFirst());
  }

  /**
   * Test of afterLast method, of class QueryResultSetImpl.
   */
  @Test
  public void testAfterLast() {
    System.out.println("afterLast");
    QueryResultSet resultSet = q.getResultSet();
    resultSet.afterLast();
    assertTrue(resultSet.isAfterLast());
  }

  /**
   * Test of first method, of class QueryResultSetImpl.
   */
  @Test
  public void testFirst() throws QueryConstructionException {
    System.out.println("first");
    q = QueryFactory.getQuery(evaluateThreeMinusOne, inferencePSC);
    q.setMaxAnswerCount(1);
    QueryResultSet resultSet = q.getResultSet();
    assertEquals(1, q.getAnswerCount());
    boolean result = resultSet.first();
    assertEquals(true, result);
    assertEquals(1, resultSet.getRow());
  }

  /**
   * Test of last method, of class QueryResultSetImpl.
   */
  @Test
  public void testLast() throws QueryConstructionException {
    System.out.println("last");
    q = QueryFactory.getQuery(evaluateThreeMinusOne, inferencePSC);
    q.setMaxAnswerCount(1);
    QueryResultSet resultSet = q.getResultSet();
    boolean result = resultSet.last();
    assertEquals(true, result);
    assertEquals(1, resultSet.getRow());
  }

  /**
   * Test of getRow method, of class QueryResultSetImpl.
   */
  @Test
  public void testGetRow() throws QueryConstructionException {
    System.out.println("getRow");
    q = QueryFactory.getQuery(evaluateThreeMinusOne, inferencePSC);
    q.setMaxAnswerCount(1);
    assertEquals(1, q.getAnswerCount());
    QueryResultSet resultSet = q.getResultSet();
    int expResult = 0;
    int result = resultSet.getRow();
    assertEquals(expResult, result);
  }

  /**
   * Test of absolute method, of class QueryResultSetImpl.
   */
  @Test
  public void testAbsolute() throws QueryConstructionException {
    System.out.println("absolute");
    q = QueryFactory.getQuery(evaluateThreeMinusOne, inferencePSC);
    q.setMaxAnswerCount(1);
    assertEquals(1, q.getAnswerCount());
    QueryResultSet resultSet = q.getResultSet();
    assertTrue(resultSet.absolute(1));
    assertFalse(resultSet.absolute(0));
  }

  /**
   * Test of relative method, of class QueryResultSetImpl.
   */
  @Test
  public void testRelative() {
    System.out.println("relative");
    QueryResultSet resultSet = q.getResultSet();
    resultSet.first();
    assertTrue(resultSet.relative(2));
    assertEquals(3, resultSet.getRow());
    assertTrue(resultSet.relative(-1));
    assertEquals(2, resultSet.getRow());
  }

  /**
   * Test of previous method, of class QueryResultSetImpl.
   */
  @Test
  public void testPrevious() {
    System.out.println("previous");
    QueryResultSet resultSet = q.getResultSet();
    assertTrue(resultSet.absolute(3));
    assertEquals(3, resultSet.getRow());
    assertTrue(resultSet.previous());
    assertEquals(2, resultSet.getRow());
  }

  /**
   * Test of isClosed method, of class QueryResultSetImpl.
   */
  @Test
  public void testIsClosed() {
    System.out.println("isClosed");
    QueryResultSet resultSet = q.getResultSet();
    assertFalse(resultSet.isClosed());
    resultSet.close();
    assertTrue(resultSet.isClosed());
  }

}
