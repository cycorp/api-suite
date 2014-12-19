package com.cyc.kb.client;

/*
 * #%L
 * File: RelationTest.java
 * Project: KB API
 * %%
 * Copyright (C) 2013 - 2014 Cycorp, Inc
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

import com.cyc.kb.exception.KBApiException;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import org.junit.AfterClass;

import static org.junit.Assert.*;

import org.junit.BeforeClass;

public class RelationTest {

  private static Logger log = null;

  @BeforeClass
  public static void setUp() throws Exception {
    log = Logger.getLogger(RelationTest.class.getName());
    TestConstants.ensureInitialized();
  }

  @AfterClass
  public static void tearDown() throws Exception {
  }

  //@Test
  public void testArity() throws KBApiException, UnknownHostException, IOException, Exception {
    KBPredicateImpl p = KBPredicateImpl.get("isa");
    assertTrue(p.getArityMin() == 2);
    assertTrue(p.getArityMax() == 2);
    assertTrue(p.getArity() == 2);
    final String predName = "testArityTestPred";
    assertFalse(KBPredicateImpl.existsAsType(predName));
    KBPredicateImpl np = KBPredicateImpl.findOrCreate(predName);
    FactImpl factImpl = new FactImpl("BaseKB", "(#$arityMax " + predName + " 12)");
    FactImpl factImpl1 = new FactImpl("BaseKB", "(#$arityMin " + predName + " 1)");

    assertTrue(np.getArityMin() == 1);
    assertTrue("Got " + np.getArityMax() + " as max arity, but expected 12", np.getArityMax() == 12);
    //assertTrue(p.setArity() == 2);

    np.delete();
  }
}
