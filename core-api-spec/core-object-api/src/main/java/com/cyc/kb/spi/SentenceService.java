/*
 * Copyright 2015 Cycorp, Inc..
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

package com.cyc.kb.spi;

/*
 * #%L
 * File: SentenceService.java
 * Project: Core API Object Specification
 * %%
 * Copyright (C) 2013 - 2017 Cycorp, Inc
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

import com.cyc.kb.Relation;
import com.cyc.kb.Sentence;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbTypeException;

import java.util.Collection;

/**
 *
 * @author nwinant
 * 
 * @param <T>
 */
public interface SentenceService<T extends Sentence> extends KbObjectService<T> {

  /**
   * Provides implementation for {@link com.cyc.kb.SentenceFactory#get(java.lang.String) }.
   *
   * @param   sentStr  the string representing a Sentence in the KB, a CycL sentence
   * @return  a Sentence object
   * @throws  KbTypeException
   * @throws  CreateException if the Sentence represented by sentStr could not be parsed.
   */
  T get(String sentStr) throws KbTypeException, CreateException;

  /**
   * Provides implementation for 
   * {@link com.cyc.kb.SentenceFactory#get(com.cyc.kb.Relation, java.lang.Object...) }.
   *
   * @param   pred  the first argument of the formula
   * @param   args  the other arguments of the formula in the order they appear in the list
   * @return  a Sentence object
   * @throws  KbTypeException  if the built cycObject is not a instance of CycFormulaSentence. This 
   *                           should never happen.
   * @throws  CreateException
   */
  T get(Relation pred, Object... args) throws KbTypeException, CreateException;

  /**
   * Provides implementation for {@link com.cyc.kb.SentenceFactory#get(java.lang.Object...) }.
   *
   * @param   args  the arguments of the formula in order
   * @return  a Sentence object
   * @throws  KbTypeException never thrown
   * @throws  CreateException
   */
  T get(Object... args) throws KbTypeException, CreateException;

  /**
   * Provides implementation for {@link com.cyc.kb.SentenceFactory#and(com.cyc.kb.Sentence...) }.
   *
   * @param   sentences  list of sentences to be conjoined
   * @return  a new conjoined sentence
   * @throws  KbTypeException
   * @throws  CreateException
   */
  T and(Sentence... sentences) throws KbTypeException, CreateException;

  /**
   * Provides implementation for {@link com.cyc.kb.SentenceFactory#and(java.lang.Iterable) }.
   *
   * @param   sentences list of sentences to be conjoined
   * @return  a new conjunction sentence
   * @throws  KbTypeException
   * @throws  CreateException
   */
  T and(Iterable<Sentence> sentences) throws KbTypeException, CreateException;

  /**
   * Provides implementation for 
   * {@link com.cyc.kb.SentenceFactory#implies(java.util.Collection, com.cyc.kb.Sentence) }.
   *
   * @param   antecedent
   * @param   consequent
   * @return  a new conditional Sentence
   * @throws  KbTypeException
   * @throws  CreateException
   */
  T implies(Collection<Sentence> antecedent, Sentence consequent) 
          throws KbTypeException, CreateException;
  
  /**
   * Provides implementation for
   * {@link com.cyc.kb.SentenceFactory#implies(com.cyc.kb.Sentence, com.cyc.kb.Sentence) }.
   *
   * @param   antecedent
   * @param   consequent
   * @return  a new conditional Sentence
   * @throws  KbTypeException
   * @throws  CreateException
   */
  T implies(Sentence antecedent, Sentence consequent) throws KbTypeException, CreateException;

  /**
   * Provides implementation for {@link com.cyc.kb.SentenceFactory#or(com.cyc.kb.Sentence...) }.
   *
   * @param   sentences  list of sentences to be disjoined
   * @return  a new disjunction sentence
   * @throws  KbTypeException
   * @throws  CreateException
   */
  T or(Sentence... sentences) throws KbTypeException, CreateException;

  /**
   * Provides implementation for {@link com.cyc.kb.SentenceFactory#or(java.lang.Iterable) }.
   *
   * @param   sentences  list of sentences to be disjoined
   * @return  a new disjunction sentence
   * @throws  KbTypeException
   * @throws  CreateException
   */
  T or(Iterable<Sentence> sentences) throws KbTypeException, CreateException;

}
