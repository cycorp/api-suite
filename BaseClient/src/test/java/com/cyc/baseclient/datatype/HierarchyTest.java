package com.cyc.baseclient.datatype;

/*
 * #%L
 * File: HierarchyTest.java
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

import com.cyc.baseclient.datatype.Hierarchy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import com.cyc.baseclient.datatype.Hierarchy.HierarchyComparator;

/**
 *
 * @author baxter
 */
public class HierarchyTest {

  public HierarchyTest() {
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
  public void testHierarchyBuilder() {
    System.out.println("Testing hierarchy builder.");
    final List<Integer> numbers = new ArrayList<Integer>();
    for (int i = 2; i <= 100; i++) {
      numbers.add(i);
    }
    //Collections.shuffle(numbers);
    final Hierarchy.Builder<Integer> builder = new Hierarchy.Builder<Integer>() {
      @Override
      public boolean isSuperior(Integer obj1, Integer obj2) {
        return obj2 > obj1 && obj2 % obj1 == 0;
      }
    };
    final List<Hierarchy<Integer>> hierarchies = new ArrayList(builder.organize(
            numbers));
    Collections.sort(hierarchies, new HierarchyComparator<Integer>());
    for (final Hierarchy<Integer> hierarchy : hierarchies) {
      final Integer rootNumber = hierarchy.getRoot().getContent();
      assertTrue(rootNumber + " is not prime!", isPrime(rootNumber));
      //hierarchy.printIndented(System.out);
    }
  }

  boolean isPrime(int n) {
    //check if n is a multiple of 2
    if (n % 2 == 0) {
      return n == 2;
    }
    //if not, then just check the odds
    for (int i = 3; i * i <= n; i += 2) {
      if (n % i == 0) {
        return false;
      }
    }
    return true;
  }
}
