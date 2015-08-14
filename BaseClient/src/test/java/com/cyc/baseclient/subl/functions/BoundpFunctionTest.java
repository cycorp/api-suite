package com.cyc.baseclient.subl.functions;

/*
 * #%L
 * File: BoundpFunctionTest.java
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

import static com.cyc.baseclient.subl.functions.SubLFunctions.BOUNDP;
import static com.cyc.baseclient.subl.functions.SubLFunctions.CATEGORIZE_TERM_WRT_API;
import static com.cyc.baseclient.subl.functions.SubLFunctions.CYC_OPENCYC_FEATURE;
import static com.cyc.baseclient.subl.functions.SubLFunctions.FBOUNDP;
import static com.cyc.baseclient.subl.variables.SubLGlobalVariables.KE_PURPOSE;
import static com.cyc.baseclient.subl.variables.SubLGlobalVariables.THE_CYCLIST;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * 
 * @author nwinant
 */
public class BoundpFunctionTest extends AbstractSubLFunctionTest {
  
  // Tests

  @Test
  public void testFunctionExistence() throws Exception {
    assertFunctionExistsAndIsBound("BOUNDP", BOUNDP);
  }

  @Test
  public void testEvalVariables() throws Exception {
    assertTrue(BOUNDP.eval(access, KE_PURPOSE));
    assertTrue(BOUNDP.eval(access, KE_PURPOSE.getSymbol()));
    assertTrue(BOUNDP.eval(access, KE_PURPOSE.toString()));
    assertTrue(BOUNDP.eval(access, THE_CYCLIST));
    assertTrue(BOUNDP.eval(access, THE_CYCLIST.getSymbol()));
    assertTrue(BOUNDP.eval(access, THE_CYCLIST.toString()));
  }
  
  @Test
  public void testEvalFunctions() throws Exception {
    assertFalse(BOUNDP.eval(access, BOUNDP.getSymbol()));
    assertFalse(BOUNDP.eval(access, BOUNDP.toString()));
    assertFalse(BOUNDP.eval(access, FBOUNDP.getSymbol()));
    assertFalse(BOUNDP.eval(access, FBOUNDP.toString()));
    assertFalse(BOUNDP.eval(access, CATEGORIZE_TERM_WRT_API.getSymbol()));
    assertFalse(BOUNDP.eval(access, CATEGORIZE_TERM_WRT_API.toString()));
    assertFalse(BOUNDP.eval(access, CYC_OPENCYC_FEATURE.getSymbol()));
    assertFalse(BOUNDP.eval(access, CYC_OPENCYC_FEATURE.toString()));
  }
  
}
