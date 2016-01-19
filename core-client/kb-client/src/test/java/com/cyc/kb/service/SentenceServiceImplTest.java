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
package com.cyc.kb.service;

/*
 * #%L
 * File: SentenceServiceImplTest.java
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

import static com.cyc.baseclient.CommonConstants.AND;
import static com.cyc.baseclient.CommonConstants.COLLECTION;
import static com.cyc.baseclient.CommonConstants.IMPLIES;
import static com.cyc.baseclient.CommonConstants.ISA;
import static com.cyc.baseclient.CommonConstants.OR;
import static com.cyc.baseclient.CommonConstants.PREDICATE;
import static com.cyc.baseclient.CommonConstants.THING;
import com.cyc.kb.Relation;
import com.cyc.kb.Sentence;
import com.cyc.kb.client.KbTermImpl;
import com.cyc.kb.client.RelationImpl;
import com.cyc.kb.client.SentenceImpl;
import java.util.Arrays;
import java.util.Collection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author nwinant
 */
public class SentenceServiceImplTest {
  
  public SentenceServiceImplTest() {
  }
  
  @BeforeClass
  public static void setUpClass() {
  }
  
  @AfterClass
  public static void tearDownClass() {
  }
  
  @Before
  public void setUp() {
    instance = new SentenceServiceImpl();
  }
  
  @After
  public void tearDown() {
  }
  
  
  // Fields
  
  SentenceServiceImpl instance;
  
  
  // Tests
  
  /**
   * Test of make method, of class SentenceServiceImpl.
   */
  @Test
  public void testMake_String() throws Exception {
    System.out.println("make");
    SentenceImpl expResult = new SentenceImpl(ISA, THING, PREDICATE);
    SentenceImpl result = instance.get("(#$isa #$Thing #$Predicate)");
    assertEquals(expResult, result);
  }

  /**
   * Test of make method, of class SentenceServiceImpl.
   */
  @Test
  public void testMake_Relation_ObjectArr() throws Exception {
    System.out.println("make");
    Relation pred = RelationImpl.get("resultResultIsa");
    Object[] args = new Object[]{KbTermImpl.get("VectorQuantityOfFn"), KbTermImpl.get("VectorQuantity")};
    SentenceImpl expResult = new SentenceImpl(pred, args[0], args[1]);
    SentenceImpl result = instance.get(pred, args);
    assertEquals(expResult, result);
  }

  /**
   * Test of make method, of class SentenceServiceImpl.
   */
  @Test
  public void testMake_ObjectArr() throws Exception {
    System.out.println("make");
    Object[] args = new Object[]{ISA, THING, PREDICATE};
    SentenceImpl expResult = new SentenceImpl(ISA, THING, PREDICATE);
    SentenceImpl result = instance.get(args);
    assertEquals(expResult, result);
  }

  /**
   * Test of and method, of class SentenceServiceImpl.
   */
  @Test
  public void testAnd_SentenceArr() throws Exception {
    System.out.println("and");
    Sentence[] sentences = new SentenceImpl[]{
      new SentenceImpl(ISA, THING, PREDICATE),
      new SentenceImpl(ISA, THING, COLLECTION)};
    SentenceImpl expResult = new SentenceImpl(AND,
            new SentenceImpl(ISA, THING, PREDICATE),
            new SentenceImpl(ISA, THING, COLLECTION));
    SentenceImpl result = instance.and(sentences);
    assertEquals(expResult, result);
  }

  /**
   * Test of and method, of class SentenceServiceImpl.
   */
  @Test
  public void testAnd_Iterable() throws Exception {
    System.out.println("and");
    Iterable<SentenceImpl> sentences = Arrays.asList(
            new SentenceImpl(ISA, THING, PREDICATE),
            new SentenceImpl(ISA, THING, COLLECTION));
    SentenceImpl expResult = new SentenceImpl(AND,
            new SentenceImpl(ISA, THING, PREDICATE),
            new SentenceImpl(ISA, THING, COLLECTION));
    SentenceImpl result = instance.and(sentences);
    assertEquals(expResult, result);
  }

  /**
   * Test of implies method, of class SentenceServiceImpl.
   */
  @Test
  public void testImplies_Collection_Sentence() throws Exception {
    System.out.println("implies");
    Collection<SentenceImpl> posLiterals = Arrays.asList(
            new SentenceImpl("(isa ?X Cat)"),
            new SentenceImpl("(isa ?X Logician)"));
    Sentence negLiteral = new SentenceImpl("(likesAsFriend ?X CycAdministrator)");
    SentenceImpl expResult = new SentenceImpl(IMPLIES,
            new SentenceImpl(AND,
                    new SentenceImpl("(isa ?X Cat)"),
                    new SentenceImpl("(isa ?X Logician)")),
            new SentenceImpl("(likesAsFriend ?X CycAdministrator)"));
    SentenceImpl result = instance.implies(posLiterals, negLiteral);
    assertEquals(expResult, result);
  }

  /**
   * Test of implies method, of class SentenceServiceImpl.
   */
  @Test
  public void testImplies_Sentence_Sentence() throws Exception {
    System.out.println("implies");
    Sentence posLiteral = new SentenceImpl("(isa ?X Cat)");
    Sentence negLiteral = new SentenceImpl("(likesAsFriend ?X CycAdministrator)");
    SentenceImpl expResult = new SentenceImpl(IMPLIES,
            new SentenceImpl("(isa ?X Cat)"),
            new SentenceImpl("(likesAsFriend ?X CycAdministrator)"));
    SentenceImpl result = instance.implies(posLiteral, negLiteral);
    assertEquals(expResult, result);
  }

  /**
   * Test of or method, of class SentenceServiceImpl.
   */
  @Test
  public void testOr_SentenceArr() throws Exception {
    System.out.println("or");
    Sentence[] sentences = new SentenceImpl[]{
      new SentenceImpl(ISA, THING, PREDICATE),
      new SentenceImpl(ISA, THING, COLLECTION)};
    SentenceImpl expResult = new SentenceImpl(OR,
            new SentenceImpl(ISA, THING, PREDICATE),
            new SentenceImpl(ISA, THING, COLLECTION));
    SentenceImpl result = instance.or(sentences);
    assertEquals(expResult, result);
  }

  /**
   * Test of or method, of class SentenceServiceImpl.
   */
  @Test
  public void testOr_Iterable() throws Exception {
    System.out.println("or");
    Iterable<SentenceImpl> sentences = Arrays.asList(
            new SentenceImpl(ISA, THING, PREDICATE),
            new SentenceImpl(ISA, THING, COLLECTION));
    SentenceImpl expResult = new SentenceImpl(OR,
            new SentenceImpl(ISA, THING, PREDICATE),
            new SentenceImpl(ISA, THING, COLLECTION));
    SentenceImpl result = instance.or(sentences);
    assertEquals(expResult, result);
  }
  
}
