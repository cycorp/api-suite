package com.cyc.baseclient.subl.functions;

/*
 * #%L
 * File: SublFunctionsTest.java
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

import static com.cyc.baseclient.subl.functions.SublFunctions.*;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * This tests the functions defined within {@link com.cyc.baseclient.subl.functions.SublFunctions}.
 * 
 * <p>Functions which are defined within their own classes (such as {@link FboundpFunction} will
 * typically have their own test class (such as {@link FboundpFunctionTest}).
 * 
 * @author nwinant
 */
public class SublFunctionsTest extends AbstractSublFunctionTest {
  
  // Tests
  
  @Test
  public void test_CYC_OPENCYC_FEATURE() throws Exception {
    assertFunctionExistsAndIsBound("CYC-OPENCYC-FEATURE", CYC_OPENCYC_FEATURE);
    assertEquals(
            access.isOpenCyc(),
            CYC_OPENCYC_FEATURE.eval(access));
  }
  
  @Test
  public void test_CYC_SYSTEM_CODE_STRING() throws Exception {
    assertFunctionExistsAndIsBound("CYC-SYSTEM-CODE-STRING", CYC_SYSTEM_CODE_STRING);
    final String value = CYC_SYSTEM_CODE_STRING.eval(access);
    printValue(CYC_SYSTEM_CODE_STRING, value);
    assertNotNilOrNull(value);
  }
  
  @Test
  public void test_CYC_REVISION_NUMBERS() throws Exception {
    assertFunctionExistsAndIsBound("CYC-REVISION-NUMBERS", CYC_REVISION_NUMBERS);
    final List values = CYC_REVISION_NUMBERS.eval(access);
    printValues(CYC_REVISION_NUMBERS, values);
    assertNotNilOrNull(values);
  }
  
  @Test
  public void test_CYC_REVISION_STRING() throws Exception {
    assertFunctionExistsAndIsBound("CYC-REVISION-STRING", CYC_REVISION_STRING);
    final String value = CYC_REVISION_STRING.eval(access);
    printValue(CYC_REVISION_STRING, value);
    assertNotNilOrNull(value);
  }
  
  @Test
  public void test_KB_VERSION_STRING() throws Exception {
    assertFunctionExistsAndIsBound("KB-VERSION-STRING", KB_VERSION_STRING);
    final String value = KB_VERSION_STRING.eval(access);
    printValue(KB_VERSION_STRING, value);
    assertNotNilOrNull(value);
  }
  
}
