package com.cyc.query;

/*
 * #%L
 * File: KBInferenceResultSetTest.java
 * Project: Query API
 * %%
 * Copyright (C) 2013 - 2014 Cycorp, Inc.
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
import com.cyc.base.CycConnectionException;
import com.cyc.base.cycobject.CycObject;
import com.cyc.kb.FirstOrderCollection;
import com.cyc.kb.KBCollection;
import com.cyc.kb.KBIndividual;
import com.cyc.kb.KBObject;
import static com.cyc.query.TestUtils.*;
import com.cyc.kb.Variable;
import com.cyc.kb.client.Constants;
import com.cyc.kb.client.SentenceImpl;
import com.cyc.kb.exception.KBApiException;
import com.cyc.kb.exception.KBApiRuntimeException;
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
public class KBInferenceResultSetTest {

  private final SentenceImpl genlsThingThing;
  private final SentenceImpl commentBillClintonX;
  private final SentenceImpl evaluateThreeMinusOne;
  private final SentenceImpl evaluateOneMinusPointFive;

  public KBInferenceResultSetTest() {
    try {
      genlsThingThing = new SentenceImpl(Constants.genls(), testConstants().thing, testConstants().thing);
      commentBillClintonX = new SentenceImpl(testConstants().comment, testConstants().billClinton, X);
      evaluateThreeMinusOne = new SentenceImpl(testConstants().evaluate, X, testConstants().threeMinusOne);
      evaluateOneMinusPointFive = new SentenceImpl(testConstants().evaluate, X, testConstants().oneMinusPointFive);
      equalsXTrue = new SentenceImpl(testConstants().cycEquals, X, testConstants().cycTrue);
    } catch (Exception ex) {
      throw new RuntimeException("Exception initializing test.", ex);
    }
  }

  @BeforeClass
  public static void setUpClass() throws KBApiException, IOException, CycConnectionException {
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
   * Test of getObject method, of class KBInferenceResultSet.
   */
  @Test
  public void testGetObject_int_Class() throws Exception {
    System.out.println("getObject");
    KBInferenceResultSet resultSet = q.getResultSet();
    assertTrue(resultSet.next());
    Object result = resultSet.getObject(1, KBCollection.class);
    assertNotNull(result);
  }

  /**
   * Test of getKBObject method, of class KBInferenceResultSet.
   */
  @Test
  public void testGetKBObject_int() {
    System.out.println("getKBObject");
    KBInferenceResultSet resultSet = q.getResultSet();
    assertTrue(resultSet.next());
    KBObject result = resultSet.getKBObject(resultSet.findColumn(X));
    assertNotNull(result);
  }

  /**
   * Test of getObject method, of class KBInferenceResultSet.
   */
  @Test
  public void testGetObject_String_Class() throws Exception {
    System.out.println("getObject");
    KBInferenceResultSet resultSet = q.getResultSet();
    assertTrue(resultSet.next());
    KBCollection result = resultSet.getObject("?" + X.getName(), KBCollection.class);
    assertNotNull(result);
  }

  /**
   * Test of getObject method, of class KBInferenceResultSet.
   */
  @Test
  public void testGetObject_int() throws Exception {
    System.out.println("getObject");
    KBInferenceResultSet resultSet = q.getResultSet();
    assertTrue(resultSet.next());
    Object result = resultSet.getObject(1);
    assertNotNull(result);
  }

  /**
   * Test of getObject method, of class KBInferenceResultSet.
   */
  @Test
  public void testGetObject_String() throws Exception {
    System.out.println("getObject");
    KBInferenceResultSet resultSet = q.getResultSet();
    assertTrue(resultSet.next());
    KBCollection result = (KBCollection) resultSet.getObject("?" + X.getName());
    assertNotNull(result);
  }

  /**
   * Test of getKBObject method, of class KBInferenceResultSet.
   */
  //@Test
  public void testGetKBObject_String() throws Exception {
    // TODO: currently throwing ClassCastException, which it should not.
    System.out.println("getKBObject");
    String columnLabel = "?X";
    KBInferenceResultSet resultSet = q.getResultSet();
    resultSet.next();
    FirstOrderCollection result = resultSet.getKBObject(columnLabel);
    assertTrue(result instanceof KBObject);
  }

  /**
   * Test of getKBObject method, of class KBInferenceResultSet.
   */
  @Test(expected = ClassCastException.class)
  public void testGetKBObject_String_Exception() throws Exception {
    System.out.println("getKBObject Exception");
    String columnLabel = "?X";
    KBInferenceResultSet resultSet = q.getResultSet();
    resultSet.next();
    // The actual answers are all collections, so this should throw an exception:
    KBIndividual result = resultSet.getKBObject(columnLabel);
  }

  /**
   * Test of getColumnNames method, of class KBInferenceResultSet.
   */
  @Test
  public void testGetColumnNames() throws Exception {
    System.out.println("getKBObject");
    KBInferenceResultSet resultSet = q.getResultSet();
    List<String> result = resultSet.getColumnNames();
    assertEquals(result.size(), 1);
    assertEquals(result.get(0).toString(), "?X");
    q.close();
  }

  private void resetQ() throws QueryConstructionException, KBApiRuntimeException {
    q = new Query(testConstants().genlsAnimalX, Constants.inferencePSCMt());
  }

  /**
   * Test of isInferenceComplete method, of class KBInferenceResultSet.
   */
  @Test
  public void testIsInferenceComplete() throws CycConnectionException, QueryConstructionException {
    System.out.println("isInferenceComplete");
    resetQ();
    KBInferenceResultSet resultSet = q.getResultSet();
    assertTrue(resultSet.isInferenceComplete());
    q.close();
  }

  /**
   * Test of close method, of class KBInferenceResultSet.
   */
  @Test
  public void testClose() {
    System.out.println("close");
    q.setContinuable(true);
    KBInferenceResultSet resultSet = q.getResultSet();
    resultSet.close();
    assertTrue(resultSet.isClosed());
    //@todo Make sure the inference and problem store were really removed from the Cyc server.
  }

  /**
   * Test of next method, of class KBInferenceResultSet.
   */
  @Test
  public void testNext() {
    System.out.println("next");
    KBInferenceResultSet resultSet = q.getResultSet();
    assertTrue(q.isProvable());
    while (resultSet.getRow() < q.getAnswerCount()) {
      assertTrue(resultSet.next());
    }
    assertFalse(resultSet.next());
  }

  /**
   * Test of findColumn method, of class KBInferenceResultSet.
   */
  @Test
  public void testFindColumn_Variable() {
    System.out.println("findColumn");
    KBInferenceResultSet resultSet = q.getResultSet();
    assertEquals(1, resultSet.findColumn(X));
  }

  /**
   * Test of getCurrentRowCount method, of class KBInferenceResultSet.
   */
  @Test
  public void testGetCurrentRowCount() throws Exception {
    System.out.println("getCurrentRowCount");
    q = new Query(equalsXTrue, inferencePSC);
    KBInferenceResultSet resultSet = q.getResultSet();
    int result = resultSet.getCurrentRowCount();
    assertEquals(1, result);
    resultSet.close();

    q = new Query(genlsThingThing, inferencePSC);
    resultSet = q.getResultSet();
    assertEquals("Expected no rows for " + q.getQuerySentence(), 0, resultSet.getCurrentRowCount());
    resultSet.close();
    //@todo add more tests for asynchronous queries.
  }

  private QueryApiTestConstants testConstants() throws KBApiRuntimeException {
    return QueryApiTestConstants.getInstance();
  }

  /**
   * Test of getTruthValue method, of class KBInferenceResultSet.
   */
  @Test
  public void testGetTruthValue() throws Exception {
    System.out.println("getTruthValue");
    q = new Query(genlsThingThing, inferencePSC);
    KBInferenceResultSet resultSet = q.getResultSet();
    boolean expResult = true;
    boolean result = resultSet.getTruthValue();
    assertEquals(expResult, result);
    resultSet.close();
  }

  /**
   * Test of getString method, of class KBInferenceResultSet.
   */
  @Test
  public void testGetString_int() throws Exception {
    System.out.println("getString");
    q = new Query(commentBillClintonX, inferencePSC);
    KBInferenceResultSet resultSet = q.getResultSet();
    resultSet.next();
    String result = resultSet.getString(1);
    assertTrue(result instanceof String);
  }

  /**
   * Test of getString method, of class KBInferenceResultSet.
   */
  @Test
  public void testGetString_Variable() throws Exception {
    System.out.println("getString");
    q = new Query(commentBillClintonX, inferencePSC);
    KBInferenceResultSet resultSet = q.getResultSet();
    resultSet.next();
    String result = resultSet.getString(X);
    assertTrue(result instanceof String);
  }

  /**
   * Test of getBoolean method, of class KBInferenceResultSet.
   */
  @Test
  public void testGetBoolean_int() throws Exception {
    System.out.println("getBoolean");
    q = new Query(equalsXTrue, inferencePSC);
    int columnIndex = 1;
    KBInferenceResultSet resultSet = q.getResultSet();
    resultSet.next();
    boolean expResult = true;
    boolean result = resultSet.getBoolean(columnIndex);
    assertEquals(expResult, result);
  }
  private final SentenceImpl equalsXTrue;

  /**
   * Test of getInt method, of class KBInferenceResultSet.
   */
  @Test
  public void testGetInt_int() throws Exception {
    System.out.println("getInt");
    q = new Query(evaluateThreeMinusOne, inferencePSC);
    int columnIndex = 1;
    KBInferenceResultSet resultSet = q.getResultSet();
    resultSet.next();
    int expResult = 2;
    int result = resultSet.getInt(columnIndex);
    assertEquals(expResult, result);
  }

  /**
   * Test of getInt method, of class KBInferenceResultSet.
   */
  @Test
  public void testGetInt_String() throws Exception {
    System.out.println("getInt");
    q = new Query("(evaluate ?X (DifferenceFn 3 1))");
    KBInferenceResultSet resultSet = q.getResultSet();
    resultSet.next();
    int expResult = 2;
    int result = resultSet.getInt("?X");
    assertEquals(expResult, result);
  }

  /**
   * Test of getLong method, of class KBInferenceResultSet.
   */
  @Test
  public void testGetLong_int() throws Exception {
    System.out.println("getLong");
    q = new Query("(evaluate ?X (DifferenceFn 3 1))");
    int columnIndex = 1;
    KBInferenceResultSet resultSet = q.getResultSet();
    resultSet.next();
    long expResult = 2;
    long result = resultSet.getLong(columnIndex);
    assertEquals(expResult, result);
  }

  /**
   * Test of getLong method, of class KBInferenceResultSet.
   */
  @Test
  public void testGetLong_String() throws Exception {
    System.out.println("getLong");
    q = new Query("(evaluate ?X (DifferenceFn 3 1))");
    KBInferenceResultSet resultSet = q.getResultSet();
    resultSet.next();
    long expResult = 2;
    long result = resultSet.getLong("?X");
    assertEquals(expResult, result);
  }

  /**
   * Test of getFloat method, of class KBInferenceResultSet.
   */
  @Test
  public void testGetFloat_int() throws Exception {
    System.out.println("getFloat");
    q = new Query(evaluateOneMinusPointFive, Constants.baseKbMt());
    int columnIndex = 1;
    KBInferenceResultSet resultSet = q.getResultSet();
    resultSet.next();
    float expResult = 0.5F;
    float result = resultSet.getFloat(columnIndex);
    assertEquals(expResult, result, 0);
  }

  /**
   * Test of getFloat method, of class KBInferenceResultSet.
   */
  @Test
  public void testGetFloat_String() throws Exception {
    System.out.println("getFloat");
    q = new Query(evaluateOneMinusPointFive, Constants.baseKbMt());
    KBInferenceResultSet resultSet = q.getResultSet();
    resultSet.next();
    float expResult = 0.5F;
    float result = resultSet.getFloat("?X");
    assertEquals(expResult, result, 0);
  }

  /**
   * Test of getDouble method, of class KBInferenceResultSet.
   */
  @Test
  public void testGetDouble_int() throws Exception {
    System.out.println("getDouble");
    q = new Query(evaluateOneMinusPointFive, Constants.baseKbMt());
    int columnIndex = 1;
    KBInferenceResultSet resultSet = q.getResultSet();
    resultSet.next();
    double expResult = 0.5D;
    double result = resultSet.getDouble(columnIndex);
    assertEquals(expResult, result, 0);
  }

  /**
   * Test of getDouble method, of class KBInferenceResultSet.
   */
  @Test
  public void testGetDouble_String() throws Exception {
    System.out.println("getDouble");
    q = new Query(evaluateOneMinusPointFive, Constants.baseKbMt());
    KBInferenceResultSet resultSet = q.getResultSet();
    resultSet.next();
    double expResult = 0.5D;
    double result = resultSet.getDouble("?X");
    assertEquals(expResult, result, 0);
  }

  /**
   * Test of getString method, of class KBInferenceResultSet.
   */
  @Test
  public void testGetString_String() throws Exception {
    System.out.println("getString");
    q = new Query(commentBillClintonX, inferencePSC);
    KBInferenceResultSet resultSet = q.getResultSet();
    resultSet.next();
    String result = resultSet.getString("?X");
    assertTrue(result instanceof String);
  }

  /**
   * Test of getBoolean method, of class KBInferenceResultSet.
   */
  @Test
  public void testGetBoolean_String() throws Exception {
    System.out.println("getBoolean");
    q = new Query("(equals ?X True)");
    String var = "?X";
    KBInferenceResultSet resultSet = q.getResultSet();
    resultSet.next();
    boolean expResult = true;
    boolean result = resultSet.getBoolean(var);
    assertEquals(expResult, result);
  }

  /**
   * Test of getDate method, of class KBInferenceResultSet.
   */
  @Test
  public void testGetDate_String() throws Exception {
    System.out.println("getDate");
    KBInferenceResultSet resultSet = new Query("(#$equals ?NOW (#$IndexicalReferentFn #$Now-Indexical))").getResultSet();
    try {
      resultSet.next();
      assertTrue(resultSet.getDate("?NOW") instanceof Date);
    } finally {
      resultSet.close();
    }
  }

  /**
   * Test of findColumn method, of class KBInferenceResultSet.
   */
  @Test
  public void testFindColumn_String() throws Exception {
    System.out.println("findColumn");
    KBInferenceResultSet resultSet = q.getResultSet();
    assertEquals(1, resultSet.findColumn("?X"));
  }

  /**
   * Test of isBeforeFirst method, of class KBInferenceResultSet.
   */
  @Test
  public void testIsBeforeFirst() throws Exception {
    System.out.println("isBeforeFirst");
    KBInferenceResultSet resultSet = q.getResultSet();
    assertTrue(resultSet.isBeforeFirst());
    resultSet.last();
    assertFalse(resultSet.isBeforeFirst());
  }

  /**
   * Test of isAfterLast method, of class KBInferenceResultSet.
   */
  @Test
  public void testIsAfterLast() throws Exception {
    System.out.println("isAfterLast");
    KBInferenceResultSet resultSet = q.getResultSet();
    assertFalse(resultSet.isAfterLast());
    resultSet.last();
    assertFalse(resultSet.isAfterLast());
    resultSet.next();
    assertTrue(resultSet.isAfterLast());
  }

  /**
   * Test of isFirst method, of class KBInferenceResultSet.
   */
  @Test
  public void testIsFirst() throws Exception {
    System.out.println("isFirst");
    KBInferenceResultSet resultSet = q.getResultSet();
    resultSet.next();
    assertTrue(resultSet.isFirst());
    resultSet.last();
    assertFalse(resultSet.isFirst());
  }

  /**
   * Test of isLast method, of class KBInferenceResultSet.
   */
  @Test
  public void testIsLast() throws Exception {
    System.out.println("isLast");
    KBInferenceResultSet resultSet = q.getResultSet();
    assertFalse(resultSet.isLast());
    resultSet.last();
    assertTrue(resultSet.isLast());
  }

  /**
   * Test of beforeFirst method, of class KBInferenceResultSet.
   */
  @Test
  public void testBeforeFirst() throws Exception {
    System.out.println("beforeFirst");
    KBInferenceResultSet resultSet = q.getResultSet();
    resultSet.beforeFirst();
    assertTrue(resultSet.isBeforeFirst());
  }

  /**
   * Test of afterLast method, of class KBInferenceResultSet.
   */
  @Test
  public void testAfterLast() throws Exception {
    System.out.println("afterLast");
    KBInferenceResultSet resultSet = q.getResultSet();
    resultSet.afterLast();
    assertTrue(resultSet.isAfterLast());
  }

  /**
   * Test of first method, of class KBInferenceResultSet.
   */
  @Test
  public void testFirst() throws Exception {
    System.out.println("first");
    q = new Query(evaluateThreeMinusOne, inferencePSC);
    q.setMaxNumber(1);
    KBInferenceResultSet resultSet = q.getResultSet();
    assertEquals(1, q.getAnswerCount());
    boolean result = resultSet.first();
    assertEquals(true, result);
    assertEquals(1, resultSet.getRow());
  }

  /**
   * Test of last method, of class KBInferenceResultSet.
   */
  @Test
  public void testLast() throws Exception {
    System.out.println("last");
    q = new Query(evaluateThreeMinusOne, inferencePSC);
    q.setMaxNumber(1);
    KBInferenceResultSet resultSet = q.getResultSet();
    boolean result = resultSet.last();
    assertEquals(true, result);
    assertEquals(1, resultSet.getRow());
  }

  /**
   * Test of getRow method, of class KBInferenceResultSet.
   */
  @Test
  public void testGetRow() throws Exception {
    System.out.println("getRow");
    q = new Query(evaluateThreeMinusOne, inferencePSC);
    q.setMaxNumber(1);
    assertEquals(1, q.getAnswerCount());
    KBInferenceResultSet resultSet = q.getResultSet();
    int expResult = 0;
    int result = resultSet.getRow();
    assertEquals(expResult, result);
  }

  /**
   * Test of absolute method, of class KBInferenceResultSet.
   */
  @Test
  public void testAbsolute() throws Exception {
    System.out.println("absolute");
    q = new Query(evaluateThreeMinusOne, inferencePSC);
    q.setMaxNumber(1);
    assertEquals(1, q.getAnswerCount());
    KBInferenceResultSet resultSet = q.getResultSet();
    assertTrue(resultSet.absolute(1));
    assertFalse(resultSet.absolute(0));
  }

  /**
   * Test of relative method, of class KBInferenceResultSet.
   */
  @Test
  public void testRelative() throws Exception {
    System.out.println("relative");
    KBInferenceResultSet resultSet = q.getResultSet();
    resultSet.first();
    assertTrue(resultSet.relative(2));
    assertEquals(3, resultSet.getRow());
    assertTrue(resultSet.relative(-1));
    assertEquals(2, resultSet.getRow());
  }

  /**
   * Test of previous method, of class KBInferenceResultSet.
   */
  @Test
  public void testPrevious() throws Exception {
    System.out.println("previous");
    KBInferenceResultSet resultSet = q.getResultSet();
    assertTrue(resultSet.absolute(3));
    assertEquals(3, resultSet.getRow());
    assertTrue(resultSet.previous());
    assertEquals(2, resultSet.getRow());
  }

  /**
   * Test of isClosed method, of class KBInferenceResultSet.
   */
  @Test
  public void testIsClosed() throws Exception {
    System.out.println("isClosed");
    KBInferenceResultSet resultSet = q.getResultSet();
    assertFalse(resultSet.isClosed());
    resultSet.close();
    assertTrue(resultSet.isClosed());
  }

}
