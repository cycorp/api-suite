package com.cyc.kb.client;

/*
 * #%L
 * File: ConstantsTest.java
 * Project: KB API Implementation
 * %%
 * Copyright (C) 2013 - 2015 Cycorp, Inc
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

import com.cyc.baseclient.CycObjectLibraryLoader;
import com.cyc.baseclient.CycObjectLibraryLoader.CycLibraryField;
import com.cyc.kb.Context;
import com.cyc.kb.KBObject;
import java.util.Collection;

import java.util.logging.Logger;

import org.junit.*;

import static org.junit.Assert.*;

/**
 *
 * @author vijay
 */
public class ConstantsTest {

    
    private static Logger log = null;

    @BeforeClass
    public static void setUp() throws Exception {
        log = Logger.getLogger(ConstantsTest.class.getName());
        TestConstants.ensureInitialized();
    }

    @AfterClass
    public static void tearDown() throws Exception {
    }
    
    @Test
    public void testGetInstance() throws Exception {
        System.out.println("getInstance");
    }

    @Test
    public void testDataMt() throws Exception {
        System.out.println("dataMt");
    }

    @Test
    public void testBaseKbMt() throws Exception {
        System.out.println("baseKbMt");
    }

    @Test
    public void testEverythingPSCMt() throws Exception {
        System.out.println("everythingPSCMt");
        Context result = Constants.everythingPSCMt();
        assertEquals(ContextImpl.get("EverythingPSC"), result);
    }

    @Test
    public void testInferencePSCMt() throws Exception {
        System.out.println("inferencePSCMt");
        Context result = Constants.inferencePSCMt();
        assertEquals(ContextImpl.get("InferencePSC"), result);
        assertFalse(ContextImpl.get("EverythingPSC").equals(result));
    }
    
  @Test
  public void testKBObjectLibraryLoader() {
    final KBObjectLibraryLoader loader = new KBObjectLibraryLoader();
    final Collection<KBObject> allObj = loader.getAllKBObjectsForClass(Constants.class);
    for (KBObject o : allObj) {
      System.out.println("  - " + o);
    }
    assertFalse(allObj.isEmpty());
    assertEquals(30, allObj.size());
    
    // Do a deeper inspection of constants classes by name:
    validateCycLibrary(loader, Constants.class);
  }
  
  
  // Protected
  
  protected void validateCycLibrary(KBObjectLibraryLoader loader, final Class<?> clazz) {
    final CycObjectLibraryLoader.CycLibraryFieldHandler handler = new CycObjectLibraryLoader.CycLibraryFieldHandler() {
      private int numFields = 0;
      private int numUnannotatedFields = 0;

      @Override
      public void onLibraryEvaluationBegin(Class libraryClass) {
        System.out.println("Validating CycLibrary " + clazz.getName() + "...");
      }

      @Override
      public void onFieldEvaluation(CycLibraryField cycField, String value, Boolean equivalent) {
        numFields++;
        final String fieldString = "  - " + cycField.getField().getType().getSimpleName() + ": " + cycField.getField().getName();
        final String valueString = "    " + value;
        if (cycField.isAnnotated()) {
          System.out.println(fieldString);
          System.out.println(valueString);
          assertEquals(cycField.getCycl(), value);
        } else {
          numUnannotatedFields++;
          System.out.println(fieldString + "      [WARNING! Field is not annotated]");
          System.out.println(valueString);
        }
      }

      @Override
      public void onException(CycLibraryField cycField, Exception ex) {
        throw new RuntimeException(ex);
      }

      @Override
      public void onLibraryEvaluationEnd(Class libraryClass, Collection<CycLibraryField> processedFields) {
        System.out.println("  " + numFields + " fields evaluated.");
        if (numUnannotatedFields == 0) {
          System.out.println("  All fields annotated, which is good!");
        } else {
          System.out.println("  " + numUnannotatedFields + " FIELDS WITHOUT ANNOTATION.");
        }
      }
    };
    loader.processAllFieldsForClass(clazz, handler);
  }
  
}
