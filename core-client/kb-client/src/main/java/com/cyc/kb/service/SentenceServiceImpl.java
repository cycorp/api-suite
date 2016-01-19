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
 * File: SentenceServiceImpl.java
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

import com.cyc.kb.spi.SentenceService;
import com.cyc.kb.Relation;
import com.cyc.kb.Sentence;
import com.cyc.kb.client.SentenceImpl;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbTypeException;
import java.util.Collection;

/**
 *
 * @author nwinant
 */
public class SentenceServiceImpl<T extends SentenceImpl> implements SentenceService<T> {

  @Override
  public T get(String sentStr) throws KbTypeException, CreateException {
    return (T) new SentenceImpl(sentStr);
  }

  @Override
  public T get(Relation pred, Object... args) throws KbTypeException, CreateException {
    return (T) new SentenceImpl(pred, args);
  }

  @Override
  public T get(Object... args) throws KbTypeException, CreateException {
    return (T) new SentenceImpl(args);
  }

  @Override
  public T and(Sentence... sentences) throws KbTypeException, CreateException {
    return (T) SentenceImpl.and(sentences);
  }

  @Override
  public T and(Iterable<Sentence> sentences) throws KbTypeException, CreateException {
    return (T) SentenceImpl.and(sentences);
  }

  @Override
  public T implies(Collection<Sentence> posLiterals, Sentence negLiteral) throws KbTypeException, CreateException {
    return (T) SentenceImpl.implies(posLiterals, negLiteral);
  }

  @Override
  public T implies(Sentence posLiteral, Sentence negLiteral) throws KbTypeException, CreateException {
    return (T) SentenceImpl.implies(posLiteral, negLiteral);
  }

  @Override
  public T or(Sentence... sentences) throws KbTypeException, CreateException {
    return (T) SentenceImpl.or(sentences);
  }

  @Override
  public T or(Iterable<Sentence> sentences) throws KbTypeException, CreateException {
    return (T) SentenceImpl.or(sentences);
  }
  
}
