package com.cyc.baseclient.subl.functions;

/*
 * #%L
 * File: FboundpFunctionTest.java
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
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * 
 * @author nwinant
 */
public class FboundpFunctionTest extends AbstractSubLFunctionTest {
  
  // Tests

  @Test
  public void testFunctionExistence() throws Exception {
    assertFunctionExistsAndIsBound("FBOUNDP", FBOUNDP);
  }
  
  @Test
  public void testEvalSelf() throws Exception {
    assertTrue(FBOUNDP.eval(access, FBOUNDP));
    assertTrue(FBOUNDP.eval(access, FBOUNDP.toString()));
  }
  
  @Test
  public void testEvalFunctions() throws Exception {
    assertTrue(FBOUNDP.eval(access, BOUNDP));
    assertTrue(FBOUNDP.eval(access, BOUNDP.getSymbol()));
    assertTrue(FBOUNDP.eval(access, BOUNDP.toString()));
    assertTrue(FBOUNDP.eval(access, CATEGORIZE_TERM_WRT_API));
    assertTrue(FBOUNDP.eval(access, CATEGORIZE_TERM_WRT_API.getSymbol()));
    assertTrue(FBOUNDP.eval(access, CATEGORIZE_TERM_WRT_API.toString()));
    assertTrue(FBOUNDP.eval(access, CYC_OPENCYC_FEATURE));
    assertTrue(FBOUNDP.eval(access, CYC_OPENCYC_FEATURE.getSymbol()));
    assertTrue(FBOUNDP.eval(access, CYC_OPENCYC_FEATURE.toString()));
  }
  
  @Test
  public void testEvalVariables() throws Exception {
    assertFalse(BOUNDP.eval(access, BOUNDP.getSymbol()));
    assertFalse(BOUNDP.eval(access, BOUNDP.toString()));
    assertFalse(BOUNDP.eval(access, FBOUNDP.getSymbol()));
    assertFalse(BOUNDP.eval(access, FBOUNDP.toString()));
  }
  
}
