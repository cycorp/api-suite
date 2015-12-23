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

package com.cyc.base.kbtool;

/*
 * #%L
 * File: InspectorToolTest.java
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
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.CycSymbol;
import com.cyc.base.cycobject.FormulaSentence;
import com.cyc.baseclient.cycobject.CycAssertionImpl;
import com.cyc.baseclient.cycobject.CycConstantImpl;
import com.cyc.baseclient.cycobject.CycFormulaSentence;
import com.cyc.baseclient.cycobject.CycSymbolImpl;
import com.cyc.baseclient.cycobject.GuidImpl;
import static com.cyc.baseclient.testing.TestUtils.isEnterpriseCyc;
import com.cyc.session.exception.SessionCommunicationException;
import com.cyc.session.exception.SessionConfigurationException;
import com.cyc.session.exception.SessionInitializationException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author vijay
 */
public class InspectorToolTest {
  
  private static CycAccess access = null;
  
  public InspectorToolTest() {
  }
  
  @BeforeClass
  public static void setUpClass() throws SessionConfigurationException, SessionCommunicationException, SessionInitializationException {
    access = CycAccessManager.getCurrentAccess();
  }
  
  @AfterClass
  public static void tearDownClass() {
  }
  
  @Before
  public void setUp() {
  }
  
  @After
  public void tearDown() {
  }
    
  /**
   * Test of InspectorTool#categorizeTermWRTApi().
   */
  @Test
  public void testCategorizeTermWRTApi() throws Exception {
    System.out.println("categorizeTermWRTApi");
    final InspectorTool instance = access.getInspectorTool();

    CycObject isa = new CycConstantImpl("isa", new GuidImpl("bd588104-9c29-11b1-9dad-c379636f7270"));
    CycObject binaryPredicate = new CycConstantImpl("BinaryPredicate", new GuidImpl("bd588102-9c29-11b1-9dad-c379636f7270"));
    CycObject result = instance.categorizeTermWRTApi(isa);
    assertEquals(binaryPredicate, result);
    
    CycObject foCol = new CycConstantImpl("FirstOrderCollection", new GuidImpl("1c8052d2-1fd3-11d6-8000-0050dab92c2f"));
    result = instance.categorizeTermWRTApi(binaryPredicate);
    assertEquals(foCol, result);
    
    CycObject soCol = new CycConstantImpl("SecondOrderCollection", new GuidImpl("1d075598-1fd3-11d6-8000-0050dab92c2f"));
    result = instance.categorizeTermWRTApi(foCol);
    assertEquals(soCol, result);
    
    CycObject col = new CycConstantImpl("Collection", new GuidImpl("bd5880cc-9c29-11b1-9dad-c379636f7270"));
    result = instance.categorizeTermWRTApi(soCol);
    assertEquals(col, result);

    CycObject obama = new CycConstantImpl("BarackObama", new GuidImpl("7cabb06c-7520-11dd-8000-0002b3a85b8f"));
    CycObject ind = new CycConstantImpl("Individual", new GuidImpl("bd58da02-9c29-11b1-9dad-c379636f7270"));
    result = instance.categorizeTermWRTApi(obama);
    assertEquals(ind, result);
    
    CycObject logicalTruthMt = new CycConstantImpl("LogicalTruthMt", new GuidImpl("c0604f82-9c29-11b1-9dad-c379636f7270"));
    CycObject mt = new CycConstantImpl("Microthoery", new GuidImpl("bd5880d5-9c29-11b1-9dad-c379636f7270"));
    result = instance.categorizeTermWRTApi(logicalTruthMt);
    assertEquals(mt, result);
    
    CycObject distance = new CycConstantImpl("distanceBetween", new GuidImpl("bd58eff2-9c29-11b1-9dad-c379636f7270"));
    CycObject pred = new CycConstantImpl("Predicate", new GuidImpl("bd5880d6-9c29-11b1-9dad-c379636f7270"));
    result = instance.categorizeTermWRTApi(distance);
    assertEquals(pred, result);
    
    CycObject func = new CycConstantImpl("Function-Denotational", new GuidImpl("bd5c40b0-9c29-11b1-9dad-c379636f7270"));
    CycObject fruit = new CycConstantImpl("FruitFn", new GuidImpl("bd58a976-9c29-11b1-9dad-c379636f7270"));
    result = instance.categorizeTermWRTApi(fruit);
    assertEquals(func, result);
    
    CycObject thing = new CycConstantImpl("Thing", new GuidImpl("bd5880f4-9c29-11b1-9dad-c379636f7270"));
    CycSymbol cs = new CycSymbolImpl("SOMEKEYWORD");
    result = instance.categorizeTermWRTApi(cs);
    assertEquals(thing, result);
  }
  
  /**
   * Test categorization of sentences by InspectorTool#categorizeTermWRTApi().
   * 
   * <p><strong>Currently disabled.</strong> #categorizeTermWRTApi() is currently used only for the
   * KB API, and the KB API never uses that method to process sentences; they are checked at the 
   * Java level as instances of FormulaSentence.
   */
  //@Test
  public void testCategorizeSentence() throws Exception {
    final InspectorTool instance = access.getInspectorTool();
    final CycObject ind = new CycConstantImpl("Individual", new GuidImpl("bd58da02-9c29-11b1-9dad-c379636f7270"));
    final CycObject thing = new CycConstantImpl("Thing", new GuidImpl("bd5880f4-9c29-11b1-9dad-c379636f7270"));
    
    final FormulaSentence isaPredicate = CycFormulaSentence.makeCycSentence(access, "(#$isa #$isa #$Predicate)");
    final CycObject resultIsaPredicate = instance.categorizeTermWRTApi(isaPredicate);
    if (access.isOpenCyc() || isEnterpriseCyc()) {
      assertEquals(ind, resultIsaPredicate);
    } else {
      assertEquals(thing, resultIsaPredicate);
    }
    
    final CycObject logicalTruthMt = new CycConstantImpl("LogicalTruthMt", new GuidImpl("c0604f82-9c29-11b1-9dad-c379636f7270"));
    final CycAssertionImpl caIsaPredicate = new CycAssertionImpl(isaPredicate, logicalTruthMt);
    CycObject resultCaIsaPredicate = instance.categorizeTermWRTApi(caIsaPredicate);
    if (!isEnterpriseCyc()) {
      assertEquals(thing, resultCaIsaPredicate);
    } else {
      assertEquals(ind, resultCaIsaPredicate);
    }
  }
  
}
