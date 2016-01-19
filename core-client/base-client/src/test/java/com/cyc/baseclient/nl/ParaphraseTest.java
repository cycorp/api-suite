/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cyc.baseclient.nl;

/*
 * #%L
 * File: ParaphraseTest.java
 * Project: Base Client
 * %%
 * Copyright (C) 2013 - 2016 Cycorp, Inc.
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

import com.cyc.nl.Paraphrase;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author baxter
 */
public class ParaphraseTest {
  
  public ParaphraseTest() {
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
   * Test of compareTo method, of class ParaphraseImpl.
   */
  @Test
  public void testCompareTo() {
    System.out.println("compareTo");
    final Paraphrase<Integer> firstOne = new ParaphraseImpl<Integer>("one", 1);
    final Paraphrase<Integer> secondOne = new ParaphraseImpl<Integer>("one", 1);
    final Paraphrase<Integer> two = new ParaphraseImpl<Integer>("two", 2);
    assertEquals(0, firstOne.compareTo(secondOne));
    final List<Paraphrase<Integer>> paraphrases = Arrays.asList(two, firstOne);
    Collections.sort(paraphrases);
    assertEquals(two, paraphrases.get(1));
  }
}
