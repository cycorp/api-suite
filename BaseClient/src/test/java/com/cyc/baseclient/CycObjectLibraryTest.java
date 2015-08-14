package com.cyc.baseclient;

/*
 * #%L
 * File: CycObjectLibraryTest.java
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
import com.cyc.baseclient.testing.TestUtils;
import com.cyc.baseclient.testing.TestGuids;
import com.cyc.baseclient.testing.TestConstants;
import com.cyc.base.CycApiException;
import com.cyc.base.CycConnectionException;
import com.cyc.base.cycobject.CycObject;
import com.cyc.baseclient.CycObjectLibraryLoader.CycLibraryField;
import com.cyc.baseclient.CycObjectLibraryLoader.CycLibraryFieldHandler;
import com.cyc.baseclient.datatype.ContinuousTimeInterval;
import com.cyc.baseclient.datatype.ContinuousTimeInterval.TimeIntervalFunction;
import com.cyc.baseclient.datatype.DateConverter;
import com.cyc.baseclient.testing.KBPopulator;
import com.cyc.baseclient.testing.TestSentences;
import com.cyc.session.SessionApiException;
import com.cyc.session.internal.TestEnvironmentProperties;
import java.util.Collection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author baxter
 */
public class CycObjectLibraryTest {

  public CycObjectLibraryTest() {
  }

  @BeforeClass
  public static void setUpClass() {
  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() throws CycConnectionException {
    resetConstantCounts(TestUtils.getCyc().isOpenCyc());
    objLoader = new CycObjectLibraryLoader(TestUtils.getCyc());
  }

  @After
  public void tearDown() {
  }
  
  @Test
  public void testKBPopulator() throws CycConnectionException, SessionApiException {
    TestUtils.ensureTestEnvironmentInitialized(); 
    final CycAccess cyc = TestUtils.getCyc();
    assertFalse(cyc.getInspectorTool().isConstantNameAvailable(KBPopulator.WHH_WP_PCW_STR));
    assertTrue(TestUtils.isCurrentKBAlreadyPopulated());
  }

  /**
   * Disabled until this can be rewritten to rewritten to be less of a configuration hassle.
   */
  @Deprecated
  //@Test
  public void testGetAllLibraries() {
    System.out.println("== Cyc Object Libraries:");
    Collection<Class> libraries = objLoader.getAllLibraries();
    for (Class library : libraries) {
      System.out.println(" - " + library.getName());
    }
    System.out.println("==");
    assertFalse(libraries.isEmpty());
    assertEquals(
            TestEnvironmentProperties.get().getExpectedNumberOfCycObjectLibraries(), 
            libraries.size());
  }
  
  /**
   * Disabled until this can be rewritten to rewritten to be less of a configuration hassle.
   */
  @Deprecated
  //@Test
  public void testGetAllClassFields() {
    System.out.println("Beginning...");
    Collection<CycObject> objs = objLoader.getAllCycObjects();
    printFields(objs);
    System.out.println("... done! Total: " + objs.size());
    assertFalse(objs.isEmpty());
    assertEquals(expectedTotalConstants, objs.size());
  }

  @Test
  public void testCommonConstants() throws CycConnectionException, CycApiException {
    validateCycLibrary(CommonConstants.class, EXPECTED_NUM_COMMON_CONSTANTS);
    testLibrary(CommonConstants.class, EXPECTED_NUM_COMMON_CONSTANTS);
  }
  
  @Test
  public void testDateConstants() throws CycConnectionException, CycApiException {
    validateCycLibrary(DateConverter.class, EXPECTED_NUM_DATE_CONSTANTS);
    testLibrary(DateConverter.class, EXPECTED_NUM_DATE_CONSTANTS);
  }
  
  @Test
  public void testContinuousTimeInterval() throws CycConnectionException, CycApiException {
    validateCycLibrary(ContinuousTimeInterval.class, EXPECTED_NUM_TIME_INTERVAL_CONSTANTS);
    testLibrary(ContinuousTimeInterval.class, EXPECTED_NUM_TIME_INTERVAL_CONSTANTS);
    validateCycLibrary(TimeIntervalFunction.class, EXPECTED_NUM_TIME_INTERVAL_FUNCTION_CONSTANTS);
    testLibrary(TimeIntervalFunction.class, EXPECTED_NUM_TIME_INTERVAL_FUNCTION_CONSTANTS);
  }
  
  @Test
  public void testTestConstants() throws CycConnectionException, CycApiException {
    validateCycLibrary(TestConstants.class, expectedNumTestConstants);
    testLibrary(TestConstants.class, expectedNumTestConstants);
    // TODO: We might want to test whether any of these occur in CommonConstants. - nwinant, 2014-07-30
  }
  
  @Test
  public void testTestSentences() throws CycConnectionException, CycApiException {
    validateCycLibrary(TestSentences.class, expectedNumTestSentences);
    testLibrary(TestSentences.class, expectedNumTestSentences);
  }
  
  /**
   * Disabled until this can be rewritten to rewritten to be less of a configuration hassle.
   */
  @Deprecated
  //@Test
  public void testKBContents() {
    System.out.println("Beginning...");
    try {
      final Collection<CycObject> allTerms = objLoader.getAllCycObjects();
      final Collection<CycObject> allMissingTerms = objLoader.findMissingCycObjects();
      printMissingTerms(allMissingTerms, allTerms.size());
      assertTrue(allMissingTerms.isEmpty());
    } catch (Exception ex) {
      ex.printStackTrace();
      assertTrue(false);
    }
  }

  @Test
  public void testTestGuids() throws CycConnectionException, CycApiException {
    final TestGuids library = new TestGuids();
    final Collection<String> allFields = objLoader.getAllObjectsForClass(TestGuids.class, String.class);
    printFields(allFields);
    final Collection<String> missingStrings = library.findMissingGuids(TestUtils.getCyc());
    printMissingTerms(missingStrings, allFields.size());
    assertTrue(missingStrings.isEmpty());
    
    // TODO: We might want to test whether any of these occur in CommonConstants. - nwinant, 2014-07-30
  }

  /**
   * Disabled until this can be rewritten to rewritten to be less of a configuration hassle.
   */
  @Deprecated
  //@Test
  public void testCycLibraryLoader() {
    final Collection<CycObject> allObj = objLoader.getAllCycObjects();
    for (CycObject o : allObj) {
      System.out.println("  - " + o);
    }
    assertEquals(expectedTotalConstants, allObj.size());
    for (Class libraryClass : objLoader.getAllLibraries()) {
      validateCycLibrary(libraryClass, null);
    }
  }
  
  
  // Protected
  
  protected void testLibrary(Class libraryClass, int expectedNumConstants) throws CycConnectionException, CycApiException {
    System.out.println("Testing " + libraryClass.getName() + "...");
    final Collection<Object> objs = objLoader.getAllObjectsForClass(libraryClass, Object.class);
    printFields(objs);
    System.out.println("... Total: " + objs.size());
    
    final Collection<CycObject> allTerms = objLoader.getAllCycObjectsForClass(libraryClass);
    final Collection<CycObject> allMissingTerms = objLoader.findMissingCycObjects(allTerms);
    printMissingTerms(allMissingTerms, allTerms.size());
    assertTrue(allMissingTerms.isEmpty());
    
    System.out.println("... done!");
    assertEquals(expectedNumConstants, objs.size());
  }
  
  protected void validateCycLibrary(final Class<?> clazz, final Integer expectedNumFields) {    
    final CycLibraryFieldHandler handler = new CycLibraryFieldHandler() {
      private int numFields = 0;
      private int numUnannotatedFields = 0;
      
      @Override
      public void onLibraryEvaluationBegin(Class libraryClass) {
        System.out.println("Validating CycLibrary " + clazz.getName() + "...");
      }
      
      @Override
      public void onFieldEvaluation(CycLibraryField cycField, String cyclValue, Boolean equivalent) {
        numFields++;
        final String fieldString = "  - " + cycField.getField().getType().getSimpleName() + ": " + cycField.getField().getName();
        final String valueString = "    " + cyclValue;
        if (cycField.isAnnotated()) {
          System.out.println(fieldString);
          System.out.println(valueString);
          assertEquals(cycField.getCycl(), cyclValue);
        } else {
          numUnannotatedFields++;
          System.out.println(fieldString + "      [WARNING! Field is not annotated]");
          System.out.println(valueString);
        }
      }
      
      @Override
      public void onException(CycLibraryField field, Exception ex) {
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
        if (expectedNumFields != null) {
          assertEquals((int) expectedNumFields, processedFields.size());
        }
      }
    };
    if (isBaseClientClass(clazz)) {
      objLoader.processAllFieldsForClass(clazz, handler);
    }
  }
  
  protected boolean isBaseClientClass(Class clazz) {
    if(clazz.getPackage().getName().startsWith("com.cyc.base")) {
      return true;
    }
    System.out.println("IGNORING non-Base Client class: " + clazz.getName());
    return false;
  }
  
  protected void printFields(Collection objs) {
    System.out.println("==");
    for (Object obj : objs) {
      System.out.println("- " + obj);
    }
    System.out.println("==");
  }

  protected void printMissingTerms(Collection missingTerms, int totalTerms) {
    if (missingTerms.isEmpty()) {
      System.out.println("... No missing terms!");
    } else {
      System.out.println("MISSING " + missingTerms.size() + " TERMS:");
      printFields(missingTerms);
      System.out.println("... missing " + missingTerms.size() + " terms out of " + totalTerms);
    }
  }
  
  
  // Internal
  
  private CycObjectLibraryLoader objLoader;
  
  private static final int EXPECTED_NUM_COMMON_CONSTANTS = 83;
  private static final int EXPECTED_NUM_DATE_CONSTANTS = 25;
  private static final int EXPECTED_NUM_TIME_INTERVAL_CONSTANTS = 4;
  private static final int EXPECTED_NUM_TIME_INTERVAL_FUNCTION_CONSTANTS = 3;
  private static final int EXPECTED_NUM_TEST_CONSTANTS_FULL = 111;
  private static final int EXPECTED_NUM_TEST_SENTENCES_FULL = 16;
  private int expectedNumTestConstants;
  private int expectedNumTestSentences;
  private int expectedTotalConstants;
  
  private void resetConstantCounts(boolean isOpenCyc) {
    expectedNumTestConstants = EXPECTED_NUM_TEST_CONSTANTS_FULL;
    expectedNumTestSentences = EXPECTED_NUM_TEST_SENTENCES_FULL;
    if (isOpenCyc) {
      expectedNumTestConstants -= 1;
      expectedNumTestSentences -= 1;
    }
    expectedTotalConstants
            = EXPECTED_NUM_COMMON_CONSTANTS
            + EXPECTED_NUM_DATE_CONSTANTS
            + EXPECTED_NUM_TIME_INTERVAL_CONSTANTS
            + EXPECTED_NUM_TIME_INTERVAL_FUNCTION_CONSTANTS
            + expectedNumTestConstants
            + expectedNumTestSentences;
  }
  
}
