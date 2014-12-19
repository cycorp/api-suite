package com.cyc.baseclient.xml.cycml;

/*
 * #%L
 * File: CycMLDecoderTest.java
 * Project: Base Client
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

import java.io.ByteArrayInputStream;
import junit.framework.TestCase;

/**
 *
 * @author baxter
 */
public class CycMLDecoderTest extends TestCase {

  public CycMLDecoderTest(String testName) {
    super(testName);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
  }

  /**
   * Test of decode method, of class CycMLDecoder.
   */
  public void testDecode_String() throws Exception {
    System.out.println("decode");
    CycMLDecoder instance = new CycMLDecoder();
    for (final CycMLDecoderTestCase testCase : CycMLDecoderTestCase.values()) {
      Object expResult = testCase.cycl;
      Object result = instance.decode(testCase.xml);
      System.out.println(result);
      assertEquals(expResult, result.toString());
    }
  }

  /**
   * Test of decode method, of class CycMLDecoder.
   */
  public void testDecode_InputStream() throws Exception {
    System.out.println("decode");
    CycMLDecoder instance = new CycMLDecoder();
    for (final CycMLDecoderTestCase testCase : CycMLDecoderTestCase.values()) {
      Object expResult = testCase.cycl;
      Object result = instance.decode(new ByteArrayInputStream(
              testCase.xml.getBytes()));
      System.out.println(result);
      assertEquals(expResult, result.toString());
    }
  }
}
