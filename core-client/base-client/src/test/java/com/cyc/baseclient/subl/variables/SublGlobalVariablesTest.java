package com.cyc.baseclient.subl.variables;

/*
 * #%L
 * File: SublGlobalVariablesTest.java
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

import com.cyc.base.CycAccess;
import com.cyc.base.CycAccessManager;
import com.cyc.base.exception.CycApiException;
import com.cyc.base.exception.CycConnectionException;
import com.cyc.baseclient.subl.SublGlobalVariable;
import static com.cyc.baseclient.subl.variables.SublGlobalVariables.*;
import com.cyc.baseclient.testing.TestUtils;
import com.cyc.session.exception.SessionException;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


public class SublGlobalVariablesTest {
  
  private CycAccess access;
  
  @Before
  public void setUp() throws SessionException {
    access = CycAccessManager.getCurrentAccess();
  }
  
  
  // Tests
  
  @Test
  public void testExistence() throws Exception {
    assertVariableExistsAndIsBound("*KE-PURPOSE*", KE_PURPOSE);
    assertVariableExistsAndIsBound("*THE-CYCLIST*", THE_CYCLIST);
  }
  
  @Test
  public void testExistenceObfuscated() throws Exception {
    TestUtils.assumeNotObfuscated();
    assertVariableExistsAndIsBound("*REQUIRE-CASE-INSENSITIVE-NAME-UNIQUENESS*", REQUIRE_CASE_INSENSITIVE_NAME_UNIQUENESS);
  }
  
  @Test
  public void testInequality() throws Exception {
    assertNotEquals(KE_PURPOSE, THE_CYCLIST);
    assertNotEquals(KE_PURPOSE, REQUIRE_CASE_INSENSITIVE_NAME_UNIQUENESS);
    assertNotEquals(THE_CYCLIST, REQUIRE_CASE_INSENSITIVE_NAME_UNIQUENESS);
  }
  
  
  // Protected
  
  protected void assertVariableExistsAndIsBound(String name, SublGlobalVariable variable) throws CycApiException, CycConnectionException {
    assertEquals(name, variable.toString());
    assertEquals(variable, variable);
    assertTrue(variable.isBound(access));
  }

}
