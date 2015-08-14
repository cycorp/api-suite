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
import com.cyc.session.SessionCommunicationException;
import com.cyc.session.SessionConfigurationException;
import com.cyc.session.SessionInitializationException;
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
    access = CycAccessManager.getAccess();
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
   * Test of categorizeTermWRTApi method, of class InspectorTool.
   */
  @Test
  public void testCategorizeTermWRTApi() throws Exception {
    System.out.println("categorizeTermWRTApi");
    CycObject isa = new CycConstantImpl("isa", new GuidImpl("bd588104-9c29-11b1-9dad-c379636f7270"));
    InspectorTool instance = access.getInspectorTool();
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
    
    FormulaSentence isaPredicate = CycFormulaSentence.makeCycSentence(access, "(#$isa #$isa #$Predicate)");
    CycObject logicalTruthMt = new CycConstantImpl("LogicalTruthMt", new GuidImpl("c0604f82-9c29-11b1-9dad-c379636f7270"));
    CycAssertionImpl caIsaPredicate = new CycAssertionImpl(isaPredicate, logicalTruthMt);
    result = instance.categorizeTermWRTApi(caIsaPredicate);
    CycObject thing = new CycConstantImpl("Thing", new GuidImpl("bd5880f4-9c29-11b1-9dad-c379636f7270"));
    if (!isEnterpriseCyc()) {
      assertEquals(thing, result);
    } else {
      assertEquals(ind, result); // TODO: is this correct? - nwinant, 2015-06-30
    }

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
            
    CycSymbol cs = new CycSymbolImpl("SOMEKEYWORD");
    result = instance.categorizeTermWRTApi(cs);
    assertEquals(thing, result);
    
    result = instance.categorizeTermWRTApi(isaPredicate);
    if (access.isOpenCyc() || isEnterpriseCyc()) {
      assertEquals(ind, result);
    } else {
      assertEquals(thing, result);
    }
  }
  
}
