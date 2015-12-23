package com.cyc.baseclient.cycobject;

/*
 * #%L
 * File: CycSymbolImplTest.java
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
import com.cyc.baseclient.testing.TestIterator;
import java.util.Arrays;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author baxter
 */
public class CycSymbolImplTest {

  public CycSymbolImplTest() {
  }

  @BeforeClass
  public static void setUpClass() {
  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  
  // Fields
  
  private static final TestIterator<String> NAME_TESTER = new TestIterator<String>();
  
  
  // Tests
  
  @Test(expected = NullPointerException.class)
  public void testNullSymbolName() {
    System.out.println("testNullSymbolName");
    CycSymbolImpl.isValidSymbolName(null);
  }
  
  @Test
  public void testValidSymbolNames() {
    System.out.println("testValidSymbolNames");
    assertEquals(0, NAME_TESTER.testValidAndInvalidObjects(
            Arrays.asList(
                    ":X", ":SYMBOL", ":x", ":symbol",
                    ":this-is-a-symbol",
                    "this-is-a-symbol",
                    "SYMBOL", "X", "VAR",
                    "symbol", "x", "var",
                    "package-name::symbol-name",
                    //"|symbol with whitespace|",
                    //"package:exported-symbol", 
                    //"#:uninterned-symbol"
                    ":", "::", ":::",
                    "::SYMBOL", ":::SYMBOL",
                    "?X", "?VAR", "??VAR", "???VAR", "?"),
            
            Arrays.asList(
                    "", " ", "       ",
                    //":", "::", ":::",
                    " :X", "     :X", ":X ", ":X      ", " :X    ",
                    " :SYMBOL", "     :SYMBOL", ":SYMBOL ", ":SYMBOL      ", " :SYMBOL    ",
                    //"::SYMBOL", ":::SYMBOL",
                    //"?X", "?VAR", "??VAR", "???VAR", "?",
                    "SYMBOL NAME",
                    "symbol name"),
            
            new TestIterator.IteratedTest<String>() {
              @Override
              public boolean isValidObject(String symbolName) {
                return CycSymbolImpl.isValidSymbolName(symbolName);
              }
            }).size());
  }

}
