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
 * File: RuleServiceImpl.java
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

import com.cyc.kb.spi.RuleService;
import com.cyc.kb.Context;
import com.cyc.kb.Assertion.Direction;
import com.cyc.kb.Assertion.Strength;
import com.cyc.kb.Sentence;
import com.cyc.kb.client.RuleImpl;
import com.cyc.kb.client.config.KbConfiguration;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbObjectNotFoundException;
import com.cyc.kb.exception.KbTypeException;

/**
 *
 * @author nwinant
 */
public class RuleServiceImpl<T extends RuleImpl> implements RuleService<T> {

  @Override
  public T get(String hlid) throws KbTypeException, CreateException {
    return (T) RuleImpl.get(hlid);
  }
  
  @Override
  public T get(String formulaStr, String ctxStr) throws KbTypeException, CreateException {
    return (T) RuleImpl.get(formulaStr, ctxStr);
  }
  
  @Override
  public T get(Sentence formula, Context ctx) throws KbTypeException, CreateException {
    return (T) RuleImpl.get(formula, ctx);
  }
  
  @Override
  public T get(Sentence antecedent, Sentence consequent, Context ctx) throws KbTypeException, CreateException, KbObjectNotFoundException {
    return (T) RuleImpl.get(antecedent, consequent, ctx);
  }
  
  @Override
  public T findOrCreate(String formulaStr, String ctxStr, Strength s, Direction d) throws CreateException, KbTypeException {
    return (T) RuleImpl.findOrCreate(formulaStr, ctxStr, s, d);
  }
  
  @Override
  public T findOrCreate(Sentence formula, Context ctx, Strength s, Direction d) throws CreateException, KbTypeException {
    return (T) RuleImpl.findOrCreate(formula, ctx, s, d);
  }
  
  @Override
  public T findOrCreate(String formulaStr) throws CreateException, KbTypeException {
    return (T) RuleImpl.findOrCreate(formulaStr, KbConfiguration.getDefaultContext().forAssertion().toString());
  }

  @Override
  public T findOrCreate(String formulaStr, String ctxStr) throws CreateException, KbTypeException {
    return (T) RuleImpl.findOrCreate(formulaStr, ctxStr, Strength.AUTO, Direction.AUTO);
  }

  @Override
  public T findOrCreate(Sentence formula) throws KbTypeException, CreateException {
    return (T) RuleImpl.findOrCreate(formula, KbConfiguration.getDefaultContext().forAssertion());
  }

  @Override
  public T findOrCreate(Sentence formula, Context ctx) throws KbTypeException, CreateException {
    return (T) RuleImpl.findOrCreate(formula, ctx, Strength.AUTO, Direction.AUTO);
  }
  
  @Override
  public T findOrCreate(Sentence antecedent, Sentence consequent, Context ctx) throws KbTypeException, CreateException {
    return (T) RuleImpl.findOrCreate(antecedent, consequent, ctx);
  }

}
