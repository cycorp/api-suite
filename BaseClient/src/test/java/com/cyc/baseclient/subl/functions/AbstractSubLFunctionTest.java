package com.cyc.baseclient.subl.functions;

/*
 * #%L
 * File: AbstractSubLFunctionTest.java
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
import com.cyc.base.CycApiException;
import com.cyc.base.CycConnectionException;
import com.cyc.baseclient.CycObjectFactory;
import com.cyc.baseclient.cycobject.CycSymbolImpl;
import com.cyc.baseclient.subl.SubLFunction;
import com.cyc.session.SessionApiException;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;



abstract public class AbstractSubLFunctionTest {
  
  // Fields
  
  protected CycAccess access;
  
  
  // Setup
  
  @Before
  public void setUp() throws SessionApiException {
    access = CycAccessManager.getCurrentAccess();
  }
  
  
  // Assertions
  
  protected void assertFunctionExistsAndIsBound(String functionName, SubLFunction function) throws CycApiException, CycConnectionException {
    assertEquals(functionName, function.toString());
    assertEquals(function, function);
    assertTrue(function.isBound(access));
  }
  
  protected void assertNotNilOrNull(Object value) {
    assertNotNull("Value is null", value);
    assertFalse("Value is equivalent to NIL", CycObjectFactory.nil.equals(value));
  }
  
  protected void assertNotNilOrNull(String value) {
    if (value != null) {
      // Should we accept empty strings?
      assertFalse("Value is an empty string", value.trim().isEmpty());
      
      // Is this too strict?
      assertFalse("String representation of 'null'", "null".equals(value.trim().toLowerCase()));
      try {
        assertNotNilOrNull((Object) new CycSymbolImpl(value));
      } catch (IllegalArgumentException ex) {
        // If a symbol can't be created from a non-null, non-empty string, should that be an error
        //    or a success?
        fail(ex.getMessage());
      }
    } else {
      assertNotNilOrNull((Object) null);
    }
  }
  
  
  // Misc
  
  protected void printValue(SubLFunction function, Object value) {
    System.out.println(function.toString() + " : " + value);
  }
  
  protected void printValues(SubLFunction function, List values) {
    if (values != null) {
      System.out.println(function.toString() + " (" + values.size() + " items)");
      for (Object value : values) {
        System.out.println("  " + value);
      }
    } else {
      printValue(function, values);
    }
  }
  
  protected void skipTestIfOptionalFunctionNotBound(SubLFunction function) {
    try {
      if (!function.isRequired(access)) {
        org.junit.Assume.assumeTrue(function.isBound(access));
      }
    } catch (CycConnectionException ex) {
      ex.printStackTrace(System.err);
      throw new RuntimeException(ex);
    } catch (CycApiException ex) {
      ex.printStackTrace(System.err);
      throw new RuntimeException(ex);
    }
  }
}
