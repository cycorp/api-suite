/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cyc.kb.client;

/*
 * #%L
 * File: VariableTest.java
 * Project: KB Client
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

import com.cyc.baseclient.CycObjectFactory;
import com.cyc.kb.KbCollection;
import com.cyc.kb.exception.KbException;

import java.util.logging.Level;
import java.util.logging.Logger;

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
public class VariableTest {

  private static Logger log = null;

  public VariableTest() throws Exception {
    log = Logger.getLogger(KbFunctionTest.class.getName());
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

  /**
   * Test of getTypeString method, of class Variable.
   */
  @Test
  public void testGetTypeString() throws KbException {
    System.out.println("getTypeString");
    VariableImpl instance = new VariableImpl(CycObjectFactory.makeCycVariable("?VAR"));
    KbCollection expResult = KbCollectionImpl.get("#$CycLVariable");
    KbCollection result = (KbCollection) instance.getType();
    assertEquals(expResult, result);
    
    assertEquals("?VAR", instance.toString());
  }
}
