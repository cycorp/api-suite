/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cyc.baseclient.datatype;

/*
 * #%L
 * File: StringUtilsTest.java
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

import com.cyc.baseclient.CommonConstants;
import org.junit.*;
import static org.junit.Assert.*;
import com.cyc.baseclient.cycobject.NautImpl;



/**
 *
 * @author baxter
 */
public class StringUtilsTest {

  public StringUtilsTest() {
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  /**
   * Test of isNumeric method, of class StringUtils.
   */
  @Test
  public void testIsNumeric() {
  }

  /**
   * Test of hasNumericChar method, of class StringUtils.
   */
  @Test
  public void testHasNumericChar() {
  }

  /**
   * Test of isWhitespace method, of class StringUtils.
   */
  @Test
  public void testIsWhitespace() {
  }

  /**
   * Test of stripLeading method, of class StringUtils.
   */
  @Test
  public void testStripLeading() {
  }

  /**
   * Test of stripTrailing method, of class StringUtils.
   */
  @Test
  public void testStripTrailing() {
  }

  /**
   * Test of stripLeadingBlanks method, of class StringUtils.
   */
  @Test
  public void testStripLeadingBlanks() {
  }

  /**
   * Test of stripTrailingBlanks method, of class StringUtils.
   */
  @Test
  public void testStripTrailingBlanks() {
  }

  /**
   * Test of stripBlanks method, of class StringUtils.
   */
  @Test
  public void testStripBlanks() {
  }

  /**
   * Test of change method, of class StringUtils.
   */
  @Test
  public void testChange() {
  }

  /**
   * Test of isDelimitedString method, of class StringUtils.
   */
  @Test
  public void testIsDelimitedString() {
  }

  /**
   * Test of removeDelimiters method, of class StringUtils.
   */
  @Test
  public void testRemoveDelimiters() {
  }

  /**
   * Test of wordsToPhrase method, of class StringUtils.
   */
  @Test
  public void testWordsToPhrase() {
  }

  /**
   * Test of escapeDoubleQuotes method, of class StringUtils.
   */
  @Test
  public void testEscapeDoubleQuotes() {

    String str = "\\s \"werj 9234 \\3";

    String expResult = "\\\\s \\\"werj 9234 \\\\3";
    String result = StringUtils.escapeDoubleQuotes(str);
    assertEquals(expResult, result);
  }

  /**
   * Test of unescapeDoubleQuotes method, of class StringUtils.
   */
  @Test
  public void testUnescapeDoubleQuotes() {
  }

  /**
   * Test of escapeQuoteChars method, of class StringUtils.
   */
  @Test
  public void testEscapeQuoteChars() {
  }

  /**
   * Test of getStringForException method, of class StringUtils.
   */
  @Test
  public void testGetStringForException() {
  }

  /**
   * Test of is7BitASCII method, of class StringUtils.
   */
  @Test
  public void testIs7BitASCII() {
  }

  /**
   * Test of cyclStringToJavaString method, of class StringUtils.
   */
  @Test
  public void testCyclStringToJavaString() {
    System.out.println("cyclStringToJavaString");
    Object cyclString = "test";
    String expResult = "test";
    String result = StringUtils.cyclStringToJavaString(cyclString);
    assertEquals(expResult, result);

    cyclString = new NautImpl(CommonConstants.UNICODE_STRING_FN, "Brade&u161;ko");
    expResult = NonAsciiStrings.get("bradeshko");
    result = StringUtils.cyclStringToJavaString(cyclString);
    assertEquals(expResult, result);
  }

  /**
   * Test of unicodeEscaped method, of class StringUtils.
   */
  @Test
  public void testUnicodeEscaped_String() {

    StringBuffer ex_ascii = new StringBuffer("abc&289;");
    ex_ascii.append((char) 0xac);
    System.out.println("Original: " + ex_ascii.toString() + "|");
    System.out.println("Sublisp Unicode escaped: |" + StringUtils.unicodeEscaped(ex_ascii.toString()) + "|");
  }

  /**
   * Test of unicodeEscaped method, of class StringUtils.
   */
  @Test
  public void testUnicodeEscaped_String_boolean() {
  }

  /**
   * Test of deEscapeHTMLescapedString method, of class StringUtils.
   */
  @Test
  public void testDeEscapeHTMLescapedString() {

    String tests[] = {"simple", "&amp;", "abc&a", "&#64", "&#64;&#20;", "sadf&whatever;sdfsd", "", "&u64;"};

    for (int i = 0; i < tests.length; i++) {
      try {
        System.out.println("DeEscape: org: |" + tests[i] + "| deEscaped: |" + StringUtils.deEscapeHTMLescapedString(tests[i]) + "|.");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
