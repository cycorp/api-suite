package com.cyc.baseclient.subl.subtypes;

/*
 * #%L
 * File: BasicSublFunctionTest.java
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

import com.cyc.baseclient.CommonConstants;
import com.cyc.baseclient.subl.functions.AbstractSublFunctionTest;
import static com.cyc.baseclient.subl.functions.SublFunctions.BOUNDP;
import static com.cyc.baseclient.subl.functions.SublFunctions.CATEGORIZE_TERM_WRT_API;
import static com.cyc.baseclient.subl.functions.SublFunctions.CYC_OPENCYC_FEATURE;
import static com.cyc.baseclient.subl.functions.SublFunctions.FBOUNDP;
import static com.cyc.baseclient.subl.variables.SublGlobalVariables.KE_PURPOSE;
import static com.cyc.baseclient.subl.variables.SublGlobalVariables.THE_CYCLIST;
import com.cyc.baseclient.testing.TestConstants;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * This tests the functions defined within 
 * {@link com.cyc.baseclient.subl.subtypes.BasicSubLFunction} by testing specific implementations
 * of it.
 * 
 * @author nwinant
 */
public class BasicSublFunctionTest extends AbstractSublFunctionTest {
  
  // Tests
  
  @Test
  public void testToString() throws Exception {
    assertEquals("BOUNDP", BOUNDP.toString());
    assertEquals("FBOUNDP", FBOUNDP.toString());
    assertEquals("CYC-OPENCYC-FEATURE", CYC_OPENCYC_FEATURE.toString());
  }
  
  @Test
  public void testEquality() throws Exception {
    assertEquals(BOUNDP, BOUNDP);
    assertEquals(FBOUNDP, FBOUNDP);
    assertEquals(CYC_OPENCYC_FEATURE, CYC_OPENCYC_FEATURE);
    
    assertNotEquals(CYC_OPENCYC_FEATURE, BOUNDP);
    assertNotEquals(CYC_OPENCYC_FEATURE, FBOUNDP);
    assertNotEquals(CYC_OPENCYC_FEATURE, CATEGORIZE_TERM_WRT_API);
  }
}
