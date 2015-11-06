package com.cyc.kb.quant;

/*
 * #%L
 * File: RestrictedVariable.java
 * Project: KB API Implementation
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

import com.cyc.kb.client.SentenceImpl;
import com.cyc.kb.exception.KBApiException;
import com.cyc.kb.Context;
import com.cyc.kb.Variable;

import java.util.Arrays;
import java.util.List;

/**
 * THIS CLASS IS NOT SUPPORTED IN KB API 1.0.
 * @author vijay
 */
public class RestrictedVariable extends SentenceImpl {
  
  private Context istContext;
  private Variable variable;
  private List<Object> sentenceArguments;
  
  /*
  protected RestrictedVariable (CycFormulaSentence cfs) throws KBApiException {
    super(cfs);
    // Should we attempt to set quantifier and collection from
    // the CycFormulaSentence.
  }
  */
  
  /**
   *
   * @param ctx Can be null. If null decide the default context. Also construct only the literal without the 
   * ist  in such case.
   * @param var The quantification, for this literal will be over var
   * @todo Address the case of multiple variables in a literal and their quantification. 
   * Make the paradigm recursive and modular
   * @param args
   */
  public RestrictedVariable (Context ctx, Variable var, Object... args) throws KBApiException {
    super(args);
    sentenceArguments = Arrays.asList(args);
    if (!sentenceArguments.contains(var)){
      throw new KBApiException("The variable " + var + " must be one of the arguments in " + args);
    }
    istContext = ctx;
    variable = var;
    
  }
     
  
  public Context getIstContext() {
    return istContext;
  }

  public void setIstContext(Context istContext) {
    this.istContext = istContext;
  }

  public Variable getVariable() {
    return variable;
  }

  public void setVariable(Variable variable) {
    this.variable = variable;
  }

  public List<Object> getSentenceArguments() {
    return sentenceArguments;
  }

  public void setSentenceArguments(List<Object> sentenceArguments) {
    this.sentenceArguments = sentenceArguments;
  } 

  
}
