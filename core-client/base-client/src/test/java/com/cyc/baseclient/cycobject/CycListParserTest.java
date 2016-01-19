package com.cyc.baseclient.cycobject;

/*
 * #%L
 * File: CycListParserTest.java
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
import com.cyc.base.CycAccess;
import com.cyc.base.exception.BaseClientRuntimeException;
import com.cyc.base.exception.CycConnectionException;
import com.cyc.baseclient.testing.TestUtils;
import java.math.BigInteger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author baxter
 */
public class CycListParserTest {

  public CycListParserTest() {
  }

  @BeforeClass
  public static void setUpClass() {
  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() throws CycConnectionException {
    if (cycAccess == null || cycAccess.isClosed()) {
      cycAccess = TestUtils.getCyc();
    }
  }

  @After
  public void tearDown() {
  }

  
  // Fields
  
  private static CycAccess cycAccess;
  
  
  // Tests
  
  @Test(expected=BaseClientRuntimeException.class)
  public void testReadEmptyString() {
    System.out.println("testReadEmptyString");
    final CycArrayList result = new CycListParser(cycAccess).read("");
    System.out.println(result);
    fail("Expected an exception to be thrown.");
  }
  
  @Test(expected=BaseClientRuntimeException.class)
  public void testReadWhitespace() {
    System.out.println("testReadEmptyString");
    final CycArrayList result = new CycListParser(cycAccess).read("       ");
    System.out.println(result);
    fail("Expected an exception to be thrown.");
  }
  
  @Test
  public void testReadEmptyList() {
    System.out.println("testReadEmptyList");
    final CycArrayList result = new CycListParser(cycAccess).read("()");
    System.out.println(result);
    assertEquals(0, result.size());
  }
  
  @Test
  public void testReadSingleElement() {
    System.out.println("testReadSingleElement");
    final CycArrayList result = new CycListParser(cycAccess).read("(3)");
    System.out.println(result);
    assertEquals(1, result.size());
    assertEquals(3, result.get(0));
  }
  
  @Test
  public void testScanNumber_BigInt() {
    System.out.println("testScanNumber");
    final String intString = "40114214521980734688215";
    final CycArrayList result = new CycListParser(cycAccess).read(
            "(list #$DecimalFractionFn " + intString + " 12)");
    System.out.println(result);
    assertEquals(4, result.size());
    
    final Object arg2 = result.get(2);
    final BigInteger expected = new BigInteger(intString);
    System.out.println("[" + expected + "]");
    assertTrue(arg2 instanceof BigInteger);
    assertEquals(expected, arg2);
  }

}
