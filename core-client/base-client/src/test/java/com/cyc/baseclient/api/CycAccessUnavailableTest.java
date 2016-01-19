package com.cyc.baseclient.api;

/*
 * #%L
 * File: CycAccessUnavailableTest.java
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

/**
 * Units test for CycAccess which assume that a Cyc server is <strong>not</strong>
 * available.
 * 
 * @author daves
 */
public class CycAccessUnavailableTest  {
  /*
  
  TODO: reimplement, to take CycSessionManager into account? - nwinant
  
  @BeforeClass
  public static void setUpClass() throws MalformedURLException, Exception {
    CycClientManager.get().setCurrentAccess((CycAccess) null);
  }
  
  @Test
  public void testHasCurrent() {
    boolean errorFree = false;
    try {
      boolean hasCurrent = CycClientManager.getClientManager().hasCurrentAccess();
      errorFree = true;
      assertFalse(hasCurrent);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    assertTrue(errorFree);
  }
  */
}
