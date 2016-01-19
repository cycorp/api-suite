/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cyc.kb.quant;

/*
 * #%L
 * File: ForAllQuantifiedInstanceLiteralTest.java
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

import com.cyc.kb.client.quant.ForAllQuantifiedInstanceRestrictedVariable;
import com.cyc.kb.client.KbCollectionImpl;
import com.cyc.kb.client.TestConstants;
import com.cyc.kb.exception.KbException;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author vijay
 */
public class ForAllQuantifiedInstanceLiteralTest {

  private static Logger log = null;

  public ForAllQuantifiedInstanceLiteralTest() throws Exception {
    log = Logger.getLogger(ForAllQuantifiedInstanceLiteralTest.class.getName());
    log.setLevel(Level.FINE);
    TestConstants.ensureInitialized();
  }

  @BeforeClass
  public static void setUpClass() {
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

  @Test
  public void testSomeMethod() throws KbException {
    ForAllQuantifiedInstanceRestrictedVariable faqc = new ForAllQuantifiedInstanceRestrictedVariable(KbCollectionImpl.get("#$Dog"));
    System.out.print(faqc);
    
    //forAll.quantified(null);
  }
}
