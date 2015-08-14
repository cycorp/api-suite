package com.cyc.baseclient.cycobject;

/*
 * #%L
 * File: CycAssertionTest.java
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

import com.cyc.base.CycConnectionException;
import com.cyc.base.cycobject.CycAssertion;
import com.cyc.base.cycobject.ELMt;
import com.cyc.base.cycobject.Fort;
import com.cyc.baseclient.CommonConstants;
import java.util.Arrays;
import java.util.HashSet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.cyc.baseclient.testing.TestUtils.getCyc;
import static com.cyc.baseclient.cycobject.CycObjectUnitTest.*;
import static com.cyc.baseclient.testing.TestSentences.ISA_DOG_BIOLOGICAL_SPECIES_STRING;
import java.io.IOException;

// FIXME: TestSentences - nwinant

/**
 *
 * @author baxter
 */
public class CycAssertionTest {

  private CycAssertion assertion;

  public CycAssertionTest() {
  }

  @BeforeClass
  public static void setUpClass() {
  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() throws CycConnectionException {
    assertion = getCyc().getLookupTool().getRandomAssertion();
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testMakeInvalidAssertion() {
  }

  @Test
  public void testEquals() {
    assertEquals(assertion, new CycAssertionImpl(assertion.getFormula(), assertion.getMt()));
  }

  @Test
  public void testHashCode() {
    final CycAssertion assertion2 = new CycAssertionImpl(assertion.getFormula(), assertion.getMt());
    assertTrue(new HashSet<CycAssertion>(Arrays.asList(assertion)).contains(assertion2));
    assertEquals(assertion.hashCode(), assertion2.hashCode());
  }

  @Test
  public void testToString() {
  }

  @Test
  public void testCyclifyWithEscapeChars() {
  }

  @Test
  public void testStringApiValue() throws CycConnectionException {
    // stringApiValue() on a random assertion
    testCycObjectRetrievable(assertion);
    assertNotNull(assertion);
    String assertionAsString = assertion.stringApiValue();
    final Object assertionObject2 = getCyc().converse().converseObject(assertionAsString);
    if (assertionObject2 instanceof CycAssertion) {
      final CycAssertion assertion2 = (CycAssertion) assertionObject2;
      assertEquals(assertion, assertion2);
    } else {
      System.err.println(
              assertionAsString + "\ndoes not returns the following which is not the expected assertion:\n" + assertionObject2);
    }
  }

  @Test
  public void testCycListApiValue() {
  }

  @Test
  public void testGetELFormula() throws Exception {
  }

  @Test
  public void testGetFormula() {
  }

  @Test
  public void testGetGaf_0args() {
  }

  @Test
  public void testGetGaf_CycAccess() {
  }

  @Test
  public void testIsGaf() {
  }

  @Test
  public void testGetMt() {
  }

  @Test
  public void testToXMLString() throws Exception {
  }

  @Test
  public void testToXML() throws Exception {
  }

  @Test
  public void testGetReferencedConstants() {
  }

  @Test
  public void testCompareTo() {
  }
  
  @Test
  public void testTranscriptWithStrengthAndDirection() {
    System.out.println("testTranscriptWithStrengthAndDirection");
    // Check if any of the following throws an exceptions. 
    // There is no way to verify if strength and direction were set properly since the get methods
    // are not available in this api. It will be verified by KB API.
    try {
      ELMt uvmt = CommonConstants.UNIVERSAL_VOCABULARY_MT;
      getCyc().getAssertTool().assertSentence("'" + ISA_DOG_BIOLOGICAL_SPECIES_STRING, uvmt, null, null, false, false, (Fort) null);

      getCyc().getAssertTool().assertSentence("'" + ISA_DOG_BIOLOGICAL_SPECIES_STRING, uvmt, null, ":backward", false, false, (Fort) null);
      
      getCyc().getAssertTool().assertSentence("'" + ISA_DOG_BIOLOGICAL_SPECIES_STRING, uvmt, ":monotonic", ":backward", false, true, (Fort) null);
      
      getCyc().getAssertTool().assertSentence("'" + ISA_DOG_BIOLOGICAL_SPECIES_STRING, uvmt, null, null, false, true, (Fort) null);

    } catch (Exception e) {
      fail (e.getMessage());
    }
  }
  
  /**
   * Tests the CycAssertionImpl class.
   */
  @Test
  public void testCycAssertion() throws CycConnectionException, IOException {
    System.out.println("\n*** testCycAssertion ***");

    // toXMLString()() on a random assertion
    final CycAssertionImpl assertion = (CycAssertionImpl) getCyc().getLookupTool().getRandomAssertion();
    assertNotNull(assertion);
    final String assertionAsXML = assertion.toXMLString();
    assertNotNull(assertionAsXML);
    System.out.println();

    //TODO
        /*
     // toXML, toXMLString, unmarshal
     XMLStringWriter xmlStringWriter = new XMLStringWriter();
     try {
     String xmlString =
     "<assertion>\n" +
     "  <id>1000</id>\n" +
     "</assertion>\n";
     Object object = CycObjectFactory.unmarshal(xmlString);
     assertNotNull(object);
     assertTrue(object instanceof CycAssertionImpl);
     CycAssertionImpl cycAssertion = (CycAssertionImpl) object;
     cycAssertion.toXML(xmlStringWriter, 0, false);
     assertEquals(xmlString, xmlStringWriter.toString());
     assertEquals(xmlString, cycAssertion.toXMLString());
     CycAssertionImpl cycAssertion2 = new CycAssertionImpl(new Integer (1000));
     assertEquals(cycAssertion2, cycAssertion);
     CycArrayList cycList = new CycArrayList();
     cycList.add(cycAssertion);
     //System.out.println(cycList.toXMLString());
    
     }
     catch (Exception e) {
     e.printStackTrace();
     fail(e.getMessage());
     }
     */
    System.out.println("*** testCycAssertion OK ***");
  }
}
