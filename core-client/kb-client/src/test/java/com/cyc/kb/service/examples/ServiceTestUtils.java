/*
 * Copyright 2015 Cycorp, Inc.
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
package com.cyc.kb.service.examples;

/*
 * #%L
 * File: ServiceTestUtils.java
 * Project: KB Client
 * %%
 * Copyright (C) 2013 - 2016 Cycorp, Inc
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
import com.cyc.base.cycobject.CycAssertion;
import com.cyc.base.cycobject.CycConstant;
import com.cyc.base.cycobject.Nart;
import com.cyc.baseclient.cycobject.DefaultCycObject;
import com.cyc.baseclient.testing.TestIterator;
import com.cyc.kb.Assertion;
import com.cyc.kb.KbTerm;
import com.cyc.kb.client.AssertionImpl;
import com.cyc.kb.client.KbTermImpl;
import com.cyc.kb.exception.KbException;
import com.cyc.kb.exception.KbObjectNotFoundException;
import static com.cyc.kb.service.examples.ServiceTestExamplesInKb.ALL_TERMS_IN_KB;
import static com.cyc.kb.service.examples.ServiceTestExamplesNotInKb.ALL_TERMS_NOT_IN_KB;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nwinant
 */
public class ServiceTestUtils {

  // Fields
  
  static final public TestIterator<String> TEST_ITERATOR = new TestIterator<String>(true);
  static final public List<String> ALL_TERMS = new ArrayList();
  
  static {
    ALL_TERMS.addAll(ALL_TERMS_IN_KB);
    ALL_TERMS.addAll(ALL_TERMS_NOT_IN_KB);
  }
  
  
  // Public static methods
  
  static public String trimString(String string) {
    return string.trim();
  }
  
  static public boolean removeTerm(String termStr) {
    try {
      if (!KbTermImpl.existsAsType(termStr)) {
        KbTerm term = KbTermImpl.get(termStr.trim());
        term.delete();
      }
    } catch (KbObjectNotFoundException ex) {
      return true;
    } catch (KbException ex) {
      ex.printStackTrace(System.out);
      return false;
    }
    return true;
  }
  
  static public List<String> removeTerms(List<String> termStrings) {
    final List<String> failures = new ArrayList<String>();
    for (String termStr : termStrings) {
      if (!removeTerm(termStr)) {
        failures.add(termStr);
      }
    }
    return failures;
  }
  
  static public boolean removeAssertion(String hlid) {
    try { 
      Assertion assertion = AssertionImpl.get(hlid.trim());
      assertion.delete();
    } catch (KbObjectNotFoundException ex) {
      return true;
    } catch (KbException ex) {
      ex.printStackTrace(System.out);
      return false;
    }
    return true;
  }
  
  static public List<String> removeAssertions(List<String> hlids) {
    final List<String> failures = new ArrayList<String>();
    for (String hlid : hlids) {
      if (!removeAssertion(hlid)) {
        failures.add(hlid);
      }
    }
    return failures;
  }
  
  
  // Private methods
  
  private void generateAssertionFodder(int numExamples) throws Exception {
    final CycAccess cyc = CycAccessManager.getCurrentAccess();
    for (int i = 0; i < numExamples; i++) {
      final CycAssertion randomAssertion = cyc.getLookupTool().getRandomAssertion();
      final String hlid = DefaultCycObject.toCompactExternalId(randomAssertion, cyc);
      System.out.println(randomAssertion);
      System.out.println("      " + hlid);
      System.out.println();
    }
  }

  private void generateCycConstantFodder(int numExamples) throws Exception {
    final CycAccess cyc = CycAccessManager.getCurrentAccess();
    for (int i = 0; i < numExamples; i++) {
      final CycConstant randomConstant = cyc.getLookupTool().getRandomConstant();
      System.out.println(randomConstant);
    }
  }

  private void generateNARTFodder(int numExamples) throws Exception {
    final CycAccess cyc = CycAccessManager.getCurrentAccess();
    for (int i = 0; i < numExamples; i++) {
      final Nart randomNart = cyc.getLookupTool().getRandomNart();
      System.out.println(randomNart);
    }
  }
  

  // Main
  
  public static void main(String[] args) {
    try {
      final ServiceTestUtils utils = new ServiceTestUtils();
      final int numExamples = 10;
      CycAccessManager.getCurrentAccess();
      System.out.println();
      System.out.println();

      System.out.println("Assertion HLIDs:");
      System.out.println();
      utils.generateAssertionFodder(numExamples);
      System.out.println();
      System.out.println();

      System.out.println("Cyc Constants:");
      System.out.println();
      utils.generateCycConstantFodder(numExamples);
      System.out.println();
      System.out.println();

      System.out.println("NARTS:");
      System.out.println();
      utils.generateNARTFodder(numExamples);
      System.out.println();
      System.out.println();

      System.out.println("... Done!");
    } catch (Exception ex) {
      ex.printStackTrace(System.err);
      System.exit(1);
    } finally {
      System.exit(0);
    }
  }

}
