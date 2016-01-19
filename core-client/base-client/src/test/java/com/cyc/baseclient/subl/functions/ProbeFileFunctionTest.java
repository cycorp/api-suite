package com.cyc.baseclient.subl.functions;

/*
 * #%L
 * File: ProbeFileFunctionTest.java
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

import static com.cyc.baseclient.subl.functions.SublFunctions.PROBE_FILE;
import java.io.File;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * 
 * @author nwinant
 */
public class ProbeFileFunctionTest extends AbstractSublFunctionTest {
  
  // Tests

  @Test
  public void testFunctionExistence() throws Exception {
    assertFunctionExistsAndIsBound("PROBE-FILE", PROBE_FILE);
  }
  
  @Test
  public void testEval() throws Exception {
    final String cycDirString = PROBE_FILE.eval(access, ".");
    printValue(PROBE_FILE, cycDirString);
    assertNotNilOrNull(cycDirString);
  }
  
  @Test
  public void testGetFile() throws Exception {
    final File cycDirFile = PROBE_FILE.getFile(access, ".");
    printValue(PROBE_FILE, cycDirFile);
    assertNotNilOrNull(cycDirFile);
    
    final String cycDirString = PROBE_FILE.eval(access, ".");
    assertEquals(cycDirString, cycDirFile.toString());
  }
  
  @Test
  public void testGetServerRootDirectory() throws Exception {
    final File cycDirFile = PROBE_FILE.getServerRootDirectory(access);
    printValue(PROBE_FILE, cycDirFile);
    assertNotNilOrNull(cycDirFile);
    
    final String cycDirString = PROBE_FILE.eval(access, ".");
    assertEquals(cycDirString, cycDirFile.toString() + File.separatorChar + ".");
  }
  
}
