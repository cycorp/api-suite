/*
 * Copyright 2015 Cycorp, Inc..
 *
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
 */
package com.cyc.baseclient.testing;

/*
 * #%L
 * File: TestIterator.java
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

import java.util.ArrayList;
import java.util.List;

/**
 * A wrapper which allows you to write one test and then call it iteratively for a list of objects.
 * Instead of the test ending at the first failure, it will return a list of all failures. Useful
 * when, e.g., testing a bunch of names against a method which is supposed to determine whether 
 * they are valid.
 * 
 * <p>Note that this class will <em>not invoke JUnit,</em> but will return a list of objects for
 * which IteratedTest#isValidObject(T) returned null. You can handle these failures however you 
 * wish; one simple solution is to call org.junit.Assert#assertEquals() on the size of the failure
 * result list, like so:
 * <code>
 * @Test
 * public void testIsValidString() {
 *   TestIterator<String> testIterator = new TestIterator<String>();
 *   List<String> failures = testIterator.testValidObjects(
 *           testIterator.testValidObjects(
 *                   Arrays.asList(
 *                           "string 1",
 *                           "string 2"),
 *                   new IteratedTest<String>() {
 *                     @Override
 *                     public boolean isValidObject(String str) {
 *                       return testedObject.isValidString(str);
 *                     }
 *                   };
 *   assertEquals(0, failures.size());
 * }
 * </code>
 * 
 * <p>This class is intended to fill a different niche than JUnit parameterized test classes. 
 * Parameterized test classes create instances for the cross-product of the test methods and the 
 * test data, making them more convenient for cases when all of the methods tested by the class
 * accept the same input. By contrast, the TestIterator is intended as a simple solution for cases
 * where a test class might be testing different methods with differing expectations about input.
 * 
 * @see <a href="http://junit.sourceforge.net/javadoc/org/junit/runners/Parameterized.html">JUnit Parameterized runner javadoc</a>
 * 
 * @author nwinant
 */
public class TestIterator<T extends Object> {
  
  
  /**
   * The interface defining your test.
   * @param <T> 
   */
  public static interface IteratedTest<T extends Object> {
    boolean isValidObject(T testObject) throws Exception;
  }
  
  
  // Fields
  
  final private boolean verbose;

  
  // Construction
  
  public TestIterator(boolean verbose) {
    this.verbose = verbose;
  }
  
  public TestIterator() {
    this.verbose = false;
  }
  
  
  // Public methods
  
  /**
   * Tests a list of objects to find which are valid.
   * 
   * @param expectedValidObjects
   * @param tester
   * @return 
   */
  public List<T> testValidObjects(List<T> expectedValidObjects, IteratedTest<T> tester) {
    printMsg("Valid objects:", false);
    final List<T> failures = new ArrayList();
    for (T testObject : expectedValidObjects) {
      if (!evalTest(tester, testObject)) {
        failures.add(testObject);
      }
    }
    if (!failures.isEmpty()) {
      printMsg("These objects SHOULD have been found valid, but weren't:", true);
      printObjects(failures, true);
    }
    return failures;
  }
  
  /**
   * Tests a list of objects to find which is NOT valid; basically the inverse of #testValidObjects().
   * 
   * @param expectedInvalidObjects
   * @param tester
   * @return 
   */
  public List<T> testInvalidObjects(List<T> expectedInvalidObjects, IteratedTest<T> tester) {
    printMsg("NON-valid objects:", false);
    final List<T> failures = new ArrayList();
    for (T testObject : expectedInvalidObjects) {
      if (!evalTest(tester, testObject, true)) {
        failures.add(testObject);
      }
    }
    if (!failures.isEmpty()) {
      printMsg("These objects should NOT have been found valid, but were:", true);
      printObjects(failures, true);
    }
    return failures;
  }
  
  /**
   * Tests a list of expected-valid objects, and another list of expected-invalid objects.
   * 
   * @param tester
   * @param expectedValidObjects
   * @param expectedInvalidObjects
   * @return 
   */
  public List<T> testValidAndInvalidObjects(List<T> expectedValidObjects, List<T> expectedInvalidObjects, IteratedTest<T> tester) {
    final List<T> results = new ArrayList();
    results.addAll(testValidObjects(expectedValidObjects, tester));
    results.addAll(testInvalidObjects(expectedInvalidObjects, tester));
    return results;
  }
  
  
  // Protected
  
  protected boolean evalTest(IteratedTest<T> tester, T testObject, boolean invertResult) {
    printObject(testObject, false);
    try {
      final boolean result = tester.isValidObject(testObject);
      return (!invertResult) ? result : !result;
    } catch (Exception ex) {
      return handleException(testObject, ex);
    }
  }
  
  protected boolean evalTest(IteratedTest<T> tester, T testObject) {
    return evalTest(tester, testObject, false);
  }
  
  protected boolean handleException(T testObject, Exception ex) {
    if (verbose) {
      ex.printStackTrace(System.out);
    } else {
      printObject(testObject, true);
      System.out.println(ex);
    }
    return false;
  }
  
  protected void printMsg(String msg, boolean isImportant) {
    if (verbose || isImportant) {
      System.out.println(msg);
    }
  }
  
  protected void printObject(T object, boolean isImportant) {
    if (verbose || isImportant) {
      System.out.println("- [" + object + "]");
    }
  }

  protected void printObjects(List<T> objects, boolean isImportant) {
    for (T object : objects) {
      printObject(object, isImportant);
    }
  }
  
}
