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
 * File: FactServiceImpl.java
 * Project: KB Client
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

import com.cyc.kb.spi.FactService;
import com.cyc.kb.Context;
import com.cyc.kb.Assertion.Direction;
import com.cyc.kb.Assertion.Strength;
import com.cyc.kb.Sentence;
import com.cyc.kb.client.FactImpl;
import com.cyc.kb.client.config.KbConfiguration;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbTypeException;

/**
 *
 * @author nwinant
 */
public class FactServiceImpl<T extends FactImpl> implements FactService<T> {

  @Override
  public T get(String hlid) throws KbTypeException, CreateException {
    return (T) FactImpl.get(hlid);
  }
  
  @Override
  public T get(String formulaStr, String ctxStr) throws KbTypeException, CreateException {
    return (T) FactImpl.get(formulaStr, ctxStr);
  }
  
  @Override
  public T get(Sentence formula, Context ctx) throws KbTypeException, CreateException {
    return (T) FactImpl.get(formula, ctx);
  }
  
  @Override
  public T findOrCreate(String formulaStr, String ctxStr, Strength s, Direction d) throws CreateException, KbTypeException {
    return (T) FactImpl.findOrCreate(formulaStr, ctxStr, s, d);
  }
  
  @Override
  public T findOrCreate(Sentence formula, Context ctx, Strength s, Direction d) throws CreateException, KbTypeException {
    return (T) FactImpl.findOrCreate(formula, ctx, s, d);
  }
  
  @Override
  public T findOrCreate(String formulaStr) throws CreateException, KbTypeException {
    return (T) FactImpl.findOrCreate(formulaStr, KbConfiguration.getDefaultContext().forAssertion().toString());
  }

  @Override
  public T findOrCreate(String formulaStr, String ctxStr) throws CreateException, KbTypeException {
    return (T) FactImpl.findOrCreate(formulaStr, ctxStr, Strength.AUTO, Direction.AUTO);
  }

  @Override
  public T findOrCreate(Sentence formula) throws KbTypeException, CreateException {
    return (T) FactImpl.findOrCreate(formula, KbConfiguration.getDefaultContext().forAssertion());
  }

  @Override
  public T findOrCreate(Sentence formula, Context ctx) throws KbTypeException, CreateException {
    return (T) FactImpl.findOrCreate(formula, ctx, Strength.AUTO, Direction.AUTO);
  }
  
}
