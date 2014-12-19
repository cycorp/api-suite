package com.cyc.query;

/*
 * #%L
 * File: QueryApiConstantsTest.java
 * Project: Query API
 * %%
 * Copyright (C) 2013 - 2014 Cycorp, Inc.
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
import com.cyc.kb.KBObject;
import com.cyc.kb.client.Constants;
import com.cyc.kb.client.KBObjectLibraryLoader;
import java.lang.reflect.Field;
import java.util.Collection;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author baxter
 */
public class QueryApiConstantsTest {

  public QueryApiConstantsTest() {
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
    TestUtils.ensureConstantsInitialized();
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testKBObjectLibraryLoader() {
    final KBObjectLibraryLoader loader = KBObjectLibraryLoader.get();
    final Collection<KBObject> allObj = loader.getAllKBObjectsForClass(QueryApiConstants.class);
    for (KBObject o : allObj) {
      System.out.println("  - " + o);
    }
    assertFalse(allObj.isEmpty());
    assertEquals(8, allObj.size());
    
    // Do a deeper inspection of constants classes by name:
    validateCycLibrary(loader, QueryApiConstants.class);
    validateCycLibrary(loader, QueryApiTestConstants.class);
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
      public void onFieldEvaluation(Field field, String value, boolean hasAnnotation, String expectedValue, Boolean equivalent) {
        numFields++;
        final String fieldString = "  - " + field.getType().getSimpleName() + ": " + field.getName();
        final String valueString = "    " + value;
        if (hasAnnotation) {
          System.out.println(fieldString);
          System.out.println(valueString);
          assertEquals(expectedValue, value);
        } else {
          numUnannotatedFields++;
          System.out.println(fieldString + "      [WARNING! Field is not annotated]");
          System.out.println(valueString);
        }
      }

      @Override
      public void onException(Field field, Exception ex) {
        throw new RuntimeException(ex);
      }

      @Override
      public void onLibraryEvaluationEnd(Class libraryClass) {
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
