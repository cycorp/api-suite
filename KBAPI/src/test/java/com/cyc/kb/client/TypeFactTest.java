/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cyc.kb.client;

/*
 * #%L
 * File: TypeFactTest.java
 * Project: KB API
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

import com.cyc.base.CycAccess;
import com.cyc.base.CycAccessManager;
import com.cyc.base.cycobject.CycAssertion;
import com.cyc.base.cycobject.FormulaSentence;
import com.cyc.baseclient.cycobject.CycFormulaSentence;
import com.cyc.baseclient.cycobject.CycAssertionImpl;
import com.cyc.kb.Fact;
import com.cyc.kb.KBCollection;
import com.cyc.kb.KBPredicate;
import com.cyc.kb.exception.KBApiException;
import com.cyc.kb.quant.QuantifiedInstanceRestrictedVariable;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 *
 * @author vijay
 */
public class TypeFactTest {
  
  public TypeFactTest() {
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
  public void testSomeMethod() throws Exception {
    CycAccess cyc = CycAccessManager.getCurrentAccess();
    FormulaSentence cfs = CycFormulaSentence.makeCycSentence(cyc, "(relationAllExists inputsDestroyed BoilingWater (LiquidFn Water))");
                    
    CycAssertion ca = new CycAssertionImpl(cfs, Constants.uvMt().getCore());
    System.out.println("CycAssertion: " + ca);
    
    // TODO make this work
    // TypeFact tf = new TypeFactImpl(ca);
    // System.out.println("Type Fact: " + tf);
  }
  
  @Test
  public void addTypeFacts() throws KBApiException {
    List<Object> argList = new ArrayList<Object>();
    KBCollection col_election = KBCollectionImpl.get("ElectionForOffice");
    QuantifiedInstanceRestrictedVariable election = new QuantifiedInstanceRestrictedVariable(new QuantifierImpl("thereExists"), col_election);
    KBCollection col_official = KBCollectionImpl.get("ElectedOfficial");
    QuantifiedInstanceRestrictedVariable officer = new QuantifiedInstanceRestrictedVariable(new QuantifierImpl("forAll"), col_official);
    KBPredicate pred_osel = KBPredicateImpl.get("objectSelected");
    argList.add(pred_osel);
    argList.add(election);
    argList.add(officer);
    TypeFactImpl tf = new TypeFactImpl(Constants.uvMt(), argList.toArray());
    // Can't just cast to Fact to assertEquals?
    Fact f = new FactImpl(tf.getCore());
    
    SentenceImpl s = new SentenceImpl(KBPredicateImpl.get("relationExistsAll"), pred_osel, col_election, col_official);
    Fact f_expt = new FactImpl(Constants.uvMt(), s);
    assertEquals(f_expt, f);
  }
}
