package com.cyc.baseclient.xml;

/*
 * #%L
 * File: XmlUnitTest.java
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

import com.cyc.baseclient.xml.TextUtil;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Provides a suite of JUnit test cases for the <tt>com.cyc.baseclient.xml</tt> package.<p>
 *
 * @version $Id: UnitTest.java 126640 2008-12-04 13:39:36Z builder $
 * @author Stephen L. Reed
 */
public class XmlUnitTest {

  /**
   * Tests the TextUtil class.
   */
  @Test
  public void testTextUtil() {
    System.out.println("\n*** testTextUtil ***");
    String xmlText = "abc def";
    assertEquals(xmlText, TextUtil.doEntityReference(xmlText));
    assertEquals(xmlText, TextUtil.undoEntityReference(xmlText));
    xmlText = "abc&def<hij>klm";
    assertEquals("abc&amp;def&lt;hij&gt;klm",
            TextUtil.doEntityReference(xmlText));
    assertEquals(xmlText, TextUtil.undoEntityReference(
            "abc&amp;def&lt;hij&gt;klm"));
    System.out.println("*** testTextUtil OK ***");
  }
}
