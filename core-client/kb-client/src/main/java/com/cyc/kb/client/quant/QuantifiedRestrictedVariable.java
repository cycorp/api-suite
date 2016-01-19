package com.cyc.kb.client.quant;

/*
 * #%L
 * File: QuantifiedRestrictedVariable.java
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

import com.cyc.base.cycobject.CycObject;
import com.cyc.kb.client.KbCollectionImpl;
import com.cyc.kb.client.KbPredicateImpl;
import com.cyc.kb.client.QuantifierImpl;
import com.cyc.kb.client.SentenceImpl;
import com.cyc.kb.client.VariableImpl;
import com.cyc.kb.exception.KbException;


/**
 * THIS CLASS IS NOT SUPPORTED IN KB API 1.0.
 * @author vijay
 */
public class QuantifiedRestrictedVariable extends SentenceImpl {
  private QuantifierImpl quantifier;
  // private KBCollection collection;
  
  // Unquantified Literal
  private RestrictedVariable restrictedVariable;
  
  protected QuantifiedRestrictedVariable (CycObject core) throws KbException {
    super(core);
    // Should we attempt to set quantifier and collection from
    // the CycFormulaSentence.
  }
  
//  
//  public QuantifiedRestrictedVariable (Quantifier q, KBCollection c) throws KBApiException{
//    // Send in CycFormulaSentence here.
//    this(doSomething(q, c));
//    this.quantifier = q;
//    this.collection = c;
//  }
//  
//  private static CycFormulaSentence doSomething(Quantifier q, KBCollection c) throws KBApiException {
//    /*
//    CycFormulaSentence cfs = null;
//    Variable v = new Variable("?IND");
//    Predicate p = Predicate.get("isa");
//    
//    cfs = CycFormulaSentence.makeCycFormulaSentence(p.getCore(), v.getCore(), c.getCore());
//    return CycFormulaSentence.makeCycFormulaSentence(q.getCore(), v.getCore(), cfs);
//    */
//
//    CycFormulaSentence cfs = c.toSentence();
//    return CycFormulaSentence.makeCycFormulaSentence(q.getCore(), c.getVariable().getCore(), cfs);
//
//  }

  public QuantifiedRestrictedVariable (QuantifierImpl q, RestrictedVariable ul) throws KbException {
    this(ul.getCore());
    this.setQuantifier(q);
    this.setRestrictedVariable(ul);
  }
  
  
  public QuantifierImpl getQuantifier() {
    return quantifier;
  }

  private void setQuantifier(QuantifierImpl quantifier) {
    this.quantifier = quantifier;
  }

  public RestrictedVariable getRestrictedVariable() {
    return restrictedVariable;
  }

  private void setRestrictedVariable(RestrictedVariable restrictedVariable) {
    this.restrictedVariable = restrictedVariable;
  }
  
  @Override
  protected boolean isValidCore(CycObject tempCore)  {
    // We are constructing the sentence, we will be sure it is WFF.
    // If we check the WFF of the CycLSentence we are generating, WFF will create
    // a skolem term for the "thereExists ?IND" of the QuantifiedCollection.
    return true;
  }
  
}
