package com.cyc.kb.client;

/*
 * #%L
 * File: KbPredicateTest.java
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
import com.cyc.kb.Context;
import com.cyc.kb.Fact;
import com.cyc.kb.KbCollection;
import com.cyc.kb.KbObject;
import com.cyc.kb.KbPredicate;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbException;
import com.cyc.kb.exception.KbRuntimeException;
import com.cyc.kb.exception.KbTypeException;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.junit.AfterClass;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class KbPredicateTest {

  private static Logger log = null;
  private static Set<KbObjectImpl> testTerms = new HashSet<KbObjectImpl>();

  @BeforeClass
  public static void setUp() throws Exception {
    log = Logger.getLogger(KbPredicateTest.class.getName());
    TestConstants.ensureInitialized();
    if (!ContextImpl.existsAsType("AppleProductMt")) {
      testTerms.add(ContextImpl.findOrCreate("AppleProductMt"));
    }
    if (!KbPredicateImpl.existsAsType("iLikes")) {
      testTerms.add(KbPredicateImpl.findOrCreate("iLikes"));
    }
  }

  @AfterClass
  public static void tearDown() throws Exception {
    for (KbObject obj : testTerms) {
      obj.delete();
    }
  }

  @Test
  public void testPredicate() throws KbException, UnknownHostException, IOException {
    String str = "age";

    KbIndividualImpl i = KbIndividualImpl.get(str);
    assertEquals(i.getCore().cyclify(), "#$age");
  }

  @Test
  public void testGenls() throws Exception {
    KbPredicate p = KbPredicateImpl.get("iLikes");
    Context ctx = ContextImpl.get("AppleProductMt");
    p.addGeneralization("likesObject", "AppleProductMt");
    assertTrue(p.getGeneralizations("AppleProductMt").contains(KbPredicateImpl.get("likesObject")));
  }

  @Test
  public void testSpecs() throws KbException {
    KbPredicate p = KbPredicateImpl.get("likesObject");
    p.addSpecialization("iLikes", "AppleProductMt");
    assertTrue(p.getSpecializations("AppleProductMt").contains(KbPredicateImpl.get("iLikes")));
  }

  @Test
  public void testArgIsa() throws KbException {
    KbPredicateImpl p = KbPredicateImpl.get("iLikes");
    p.addArgIsa(1, "Person", "AppleProductMt");
    assertEquals(p.getArgIsa(1, "AppleProductMt").iterator().next().toString(), "Person");

    KbCollection iprod = new KbCollectionImpl("iProduct");
    iprod.addGeneralization("Product", "AppleProductMt");
    p.addArgIsa(2, iprod, new ContextImpl("AppleProductMt"));
  }

  @Test
  public void testArgGenl() throws KbException {
    KbPredicateImpl p = KbPredicateImpl.get("iLikes");

    // TODO: This assertion does not make sense logically (the arg2 of iLikes
    // is an Individual, not a Collection). Find a better assertion.
    p.addArgGenl(2, "Product", "AppleProductMt");
    assertEquals(p.getArgGenl(2, "AppleProductMt").iterator().next().toString(), "Product");
  }
  
  /*
  @Test
  public void testChaining() throws KBTypeConflictException, InvalidNameException, KBApiException {
      BinaryPredicate p1 = BinaryPredicate.findOrCreate("iLikes34");
      testTerms.add(p1);
      BinaryPredicate p2 = p1.addArgGenl(1, "Person", "BaseKB").addGeneralization("likesAsFriend", "BaseKB");
      assertEquals("Chained methods did not return the original predicate", p1, p2);
      assertTrue("Chained methods did not return the original predicate", p2.getGeneralizations().contains(Predicate.get("likesAsFriend")));
              
  }
  */
  
  @Test
  public void testDelete() {
    try {
      KbPredicateImpl p = KbPredicateImpl.findOrCreate("iLikes2");
      p.delete();

      KbCollectionImpl.findOrCreate("iProduct2").delete();
      ContextImpl.findOrCreate("AppleProductMt2").delete();

      assertEquals(p.getComments().size(), 0);
    } catch (KbRuntimeException ex) {
      assertEquals(ex.getMessage(),
              "The reference to iLikes2 object is stale. Possibly because it was delete using x.delete() method.");
    } catch (Exception e) {
      e.printStackTrace();
      fail("Failed to delete predicate");
    }
  }

  @Test
  public void testGetExtent() throws KbTypeException, CreateException {
    List<Fact> expected = new ArrayList();
    expected.add(FactImpl.get("(owns SomeAirline Plane-APITest)", "SomeAirlineEquipmentMt"));
    Collection<Fact> predExtent = KbPredicateImpl.get("owns").getExtent(ContextImpl.get("SomeAirlineEquipmentMt"));
    assertTrue("Could not find any assertions with owns as its predicate", predExtent.size() > 0);
    assertTrue(predExtent.containsAll(expected));
  }
}
