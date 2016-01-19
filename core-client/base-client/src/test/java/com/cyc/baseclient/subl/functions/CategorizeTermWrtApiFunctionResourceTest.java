package com.cyc.baseclient.subl.functions;

/*
 * #%L
 * File: CategorizeTermWrtApiFunctionResourceTest.java
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
import static com.cyc.baseclient.subl.functions.SublFunctions.CATEGORIZE_TERM_WRT_API;
import com.cyc.baseclient.testing.TestConstants;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * This tests the functions defined within {@link com.cyc.baseclient.subl.functions.SublFunctions}.
 * 
 * @author nwinant
 */
public class CategorizeTermWrtApiFunctionResourceTest extends AbstractSublFunctionTest {
  
  // Tests

  @Test
  public void testFunctionExistence() throws Exception {
    assertFunctionExistsAndIsBound("CATEGORIZE-TERM-WRT-API", CATEGORIZE_TERM_WRT_API);
  }
  
  @Test
  public void testEval() throws Exception {
    assertEquals(
            CommonConstants.FUNCTION_DENOTATIONAL, 
            CATEGORIZE_TERM_WRT_API.eval(access, TestConstants.THE_LIST));
  }
  
}
