/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cyc.kb.client;

/*
 * #%L
 * File: TypeFactTest.java
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

import com.cyc.base.CycAccess;
import com.cyc.base.CycAccessManager;
import com.cyc.base.cycobject.CycAssertion;
import com.cyc.base.cycobject.FormulaSentence;
import com.cyc.baseclient.cycobject.CycFormulaSentence;
import com.cyc.baseclient.cycobject.CycAssertionImpl;
import com.cyc.kb.Fact;
import com.cyc.kb.KbCollection;
import com.cyc.kb.KbPredicate;
import com.cyc.kb.exception.KbException;
import com.cyc.kb.client.quant.QuantifiedInstanceRestrictedVariable;

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
                    
    CycAssertion ca = new CycAssertionImpl(cfs, KbObjectImpl.getCore(Constants.uvMt()));
    System.out.println("CycAssertion: " + ca);
    
    // TODO make this work
    // TypeFact tf = new TypeFactImpl(ca);
    // System.out.println("Type Fact: " + tf);
  }
  
  @Test
  public void addTypeFacts() throws KbException {
    List<Object> argList = new ArrayList<Object>();
    KbCollection col_election = KbCollectionImpl.get("ElectionForOffice");
    QuantifiedInstanceRestrictedVariable election = new QuantifiedInstanceRestrictedVariable(new QuantifierImpl("thereExists"), col_election);
    KbCollection col_official = KbCollectionImpl.get("ElectedOfficial");
    QuantifiedInstanceRestrictedVariable officer = new QuantifiedInstanceRestrictedVariable(new QuantifierImpl("forAll"), col_official);
    KbPredicate pred_osel = KbPredicateImpl.get("objectSelected");
    argList.add(pred_osel);
    argList.add(election);
    argList.add(officer);
    TypeFactImpl tf = new TypeFactImpl(Constants.uvMt(), argList.toArray());
    // Can't just cast to Fact to assertEquals?
    Fact f = new FactImpl(tf.getCore());
    
    SentenceImpl s = new SentenceImpl(KbPredicateImpl.get("relationExistsAll"), pred_osel, col_election, col_official);
    Fact f_expt = new FactImpl(Constants.uvMt(), s);
    assertEquals(f_expt, f);
  }
}
