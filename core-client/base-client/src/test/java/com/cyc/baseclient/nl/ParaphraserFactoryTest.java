/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cyc.baseclient.nl;

/*
 * #%L
 * File: ParaphraserFactoryTest.java
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

import com.cyc.base.CycAccess;
import com.cyc.base.cycobject.CycConstant;
import com.cyc.base.cycobject.FormulaSentence;
import com.cyc.base.exception.CycConnectionException;
import static com.cyc.baseclient.nl.ParaphraserFactory.isBasicParaphraser;
import static com.cyc.baseclient.testing.TestGuids.BRAZIL_GUID_STRING;
import static com.cyc.baseclient.testing.TestGuids.DOG_GUID_STRING;
import static com.cyc.baseclient.testing.TestGuids.DONE_BY_GUID_STRING;
import static com.cyc.baseclient.testing.TestSentences.WHAT_IS_IN_AUSTIN_STRING;
import com.cyc.baseclient.testing.TestUtils;
import com.cyc.nl.Paraphraser;
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
public class ParaphraserFactoryTest {
  
  public ParaphraserFactoryTest() {
  }
  
  @BeforeClass
  public static void setUpClass() {
  }
  
  @AfterClass
  public static void tearDownClass() {
  }
  
  @Before
  public void setUp() throws CycConnectionException {
    if (cycAccess == null || cycAccess.isClosed()) {
      cycAccess = TestUtils.getCyc();
    }
    paraphraser = ParaphraserFactory.getInstance(ParaphraserFactory.ParaphrasableType.KBOBJECT);
  }
  
  @After
  public void tearDown() {
  }

  
  // Fields
  
  private static CycAccess cycAccess;
  private Paraphraser paraphraser = ParaphraserFactory
          .getInstance(ParaphraserFactory.ParaphrasableType.KBOBJECT);
  
  
  // Tests
  
  @Test
  public void testGetInstance() {
    System.out.println("testGetInstance");
    assertNotNull(paraphraser);
  }
  
  @Test
  public void testBasicParaphrase_Dog() throws CycConnectionException {
    System.out.println("testBasicParaphrase_Dog");
    skipIfNotBasicParaphraser(paraphraser);
    
    final CycConstant dog = cycAccess.getLookupTool().getKnownConstantByGuid(DOG_GUID_STRING);
    final String phrase = paraphraser.paraphrase(dog).getString();
    assertNotNull(phrase);
    if (isBasicParaphraser(paraphraser)) {
      assertEquals("dog", phrase.toLowerCase());
    } else {
      assertEquals("canis familiaris", phrase.toLowerCase());
    }
  }
  
  @Test
  public void testBasicParaphrase_Brazil() throws CycConnectionException {
    System.out.println("testBasicParaphrase_Brazil");
    skipIfNotBasicParaphraser(paraphraser);
    
    final CycConstant brazil = cycAccess.getLookupTool().getKnownConstantByGuid(BRAZIL_GUID_STRING);
    final String phrase = paraphraser.paraphrase(brazil).getString();
    
    assertNotNull(phrase);
    assertTrue(phrase.toLowerCase().contains("bra"));
  }
  
  @Test
  public void testBasicParaphrase_doneBy() throws CycConnectionException {
    System.out.println("testBasicParaphrase_doneBy");
    skipIfNotBasicParaphraser(paraphraser);
    
    final CycConstant doneBy = cycAccess.getLookupTool()
            .getKnownConstantByGuid(DONE_BY_GUID_STRING);
    final String phrase = paraphraser.paraphrase(doneBy).getString();
    assertNotNull(phrase);
    assertTrue(phrase.contains("doer"));
    /*
    if (isDefaultParaphraser(paraphraser)) {
      assertTrue(phrase.contains("doneBy"));
    } else {
      assertTrue(phrase.contains("doer"));
    }
    */
  }

  @Test(expected=UnsupportedOperationException.class)
  public void testBasicParaphrase_sentence() throws CycConnectionException {
    System.out.println("testBasicParaphrase_doneBy");
    skipIfNotBasicParaphraser(paraphraser);
    
    final FormulaSentence sentence = cycAccess.getObjectTool()
            .makeCycSentence(WHAT_IS_IN_AUSTIN_STRING);
    final String phrase = paraphraser.paraphrase(sentence).getString();
    fail("Should have thrown an UnsupportedOperationException, but returned: " + phrase);
  }
  
  
  // Private
  
  private void skipIfNotBasicParaphraser(Paraphraser paraphraser) {
    org.junit.Assume.assumeTrue("Using Paraphraser other than BasicParaphraser",
            isBasicParaphraser(paraphraser));
  }
  
}
