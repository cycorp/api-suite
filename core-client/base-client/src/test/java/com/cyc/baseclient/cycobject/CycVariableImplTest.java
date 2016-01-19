package com.cyc.baseclient.cycobject;

/*
 * #%L
 * File: CycVariableImplTest.java
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

import com.cyc.baseclient.testing.TestIterator;
import com.cyc.baseclient.testing.TestIterator.IteratedTest;
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
public class CycVariableImplTest {

  public CycVariableImplTest() {
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
  public void testNullVariableName() {
    System.out.println("testNullVariableName");
    CycVariableImpl.isValidVariableName(null);
  }

  @Test
  public void testValidVariableNames() {
    System.out.println("testValidVariableNames");
    assertEquals(0, NAME_TESTER.testValidAndInvalidObjects(
            Arrays.asList(
                    "?X", "?VAR", "??X", "??VAR", "?VAR-NAME",
                    ":X", ":METAVAR",
                    "?X", "?XX", "?X-X", "?ABC-234", "?ABC-ABC", "?UIO-123-UIO",
                    "??X", "??XX", "??X-X", "??ABC-234", "??ABC-ABC", "??UIO-123-UIO"),
            
            Arrays.asList(
                    "", " ", "     ",
                    "?", "??", "???",
                    "?x", "??x", "???x",
                    "?var", "??var", "???var",
                    " ?X", "        ?X", "?X ", "?X      ", " ?X ",
                    " ?VAR", "        ?VAR", "?VAR ", "?VAR      ", " ?VAR ",
                    "???VAR", "::VAR", ":::VAR",
                    "X", "VAR", "XX",
                    ":", ":?METAVAR",
                    "?:X", "?:VAR", "?:",
                    "?VAR NAME", "?X Y", "?X ?Y", "?VAR ?NAME",
                    "?VAR?NAME", "?VAR:NAME", ":VAR?NAME",
                    "?VAR??NAME", "?VAR::NAME", ":VAR??NAME", "?VAR:?NAME", ":VAR?:NAME",
                    "?-", "??234", "qawerpiouasdf",
                    "?1", "?.", "?@", "?!", "?234a2354dsf", "?234-ABC", "??234-ABC"),
            
            new IteratedTest<String>() {
              @Override
              public boolean isValidObject(String varName) {
                // TODO: add call to (valid-el-var-name? "?VAR-NAME") - nwinant, 2015-11-11
                return CycVariableImpl.isValidVariableName(varName);
              }
            }).size());
  }
  
  @Test
  public void testMetaVariableNames() {
    System.out.println("testMetaVariableNames");
    assertEquals(0, NAME_TESTER.testValidAndInvalidObjects(
            Arrays.asList(
                    ":X", ":VAR",
                    ":X", ":XX", ":X-X", ":ABC-234", ":ABC-ABC", ":UIO-123-UIO"),
            
            Arrays.asList(
                    "?X", "?VAR",
                    "??X", "??VAR",
                    "::X", "::VAR",
                    ":-", "::234", "", "XX", "qawerpiouasdf", "::",
                    "::X", ":1", ":.", ":@", ":!", ":234a2354dsf", ":234-ABC", "::234-ABC"),
            
            new IteratedTest<String>() {
              @Override
              public boolean isValidObject(String varName) {
                if (!CycVariableImpl.isValidVariableName(varName)) {
                  return false;
                }
                return new CycVariableImpl(varName).isMetaVariable();
              }
            }).size());
  }

  @Test
  public void testDontCareVariableNames() {
    System.out.println("testDontCareVariableNames");
    assertEquals(0, NAME_TESTER.testValidAndInvalidObjects(
            Arrays.asList(
                    "??X", "??VAR"),
            
            Arrays.asList(
                    "?X", "?VAR",
                    ":X", ":VAR"),
            
            new IteratedTest<String>() {
              @Override
              public boolean isValidObject(String varName) {
                if (!CycVariableImpl.isValidVariableName(varName)) {
                  return false;
                }
                return new CycVariableImpl(varName).isDontCareVariable();
              }
            }).size());
  }

}
