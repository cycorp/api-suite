package com.cyc.kb.spi;

/*
 * #%L
 * File: SentenceService.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2018 Cycorp, Inc
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
 * Provides implementations of {@link com.cyc.kb.Sentence}, including methods to build Sentences of
 * Sentences.
 *
 * @author nwinant
 */
public interface SentenceService {

  /**
   * Attempts to convert a CycL string into a CycFormulaSentence and thus into a KbObject, Sentence.
   * <p>
   *
   * @param sentStr the string representing a Sentence in the KB, a CycL sentence
   *
   * @return a Sentence object
   *
   * @throws com.cyc.kb.exception.KbTypeException
   *
   * @throws CreateException                      if the Sentence represented by sentStr could not
   *                                              be parsed.
   */
  Sentence get(String sentStr) throws KbTypeException, CreateException;

  /**
   * Builds a sentence based on <code>pred</code> and other <code>args</code>. Note that
   * <code>args</code> should be KbObjects,
   * {@link java.lang.String Strings}, {@link java.lang.Number Numbers}, or
   * {@link java.util.Date Dates}. This constructor also handles {@link java.util.List Lists} and
   * {@link java.util.Set Sets} (and Lists of Lits or Sets of Lists, etc.) of those supported
   * objects.
   *
   * @param pred the first argument of the formula
   * @param args the other arguments of the formula in the order they appear in the list
   *
   * @return a Sentence object
   *
   * @throws KbTypeException                      is thrown if the built cycObject is not a instance
   *                                              of CycFormulaSentence. This should never happen.
   * @throws com.cyc.kb.exception.CreateException
   */
  Sentence get(Relation pred, Object... args) throws KbTypeException, CreateException;

  /**
   * Builds an arbitrary sentence based on the <code>args</code> provided. Note that
   * <code>args</code> should either be KbObjects or Java classes, String, Number or Date. This
   * constructor also handles java.util.List and java.util.Set of other supported KB API or Java
   * objects. It even supports, List of List etc.
   *
   * @param args the arguments of the formula in order
   *
   * @return a Sentence object
   *
   * @throws KbTypeException                      never thrown
   * @throws com.cyc.kb.exception.CreateException
   */
  Sentence get(Object... args) throws KbTypeException, CreateException;

  /* *
   * Builds an arbitrary sentence based on the <code>args</code> provided. Note that
   * <code>args</code> should either be KbObjects or Java classes, String, Number or Date. This
   * constructor also handles java.util.List and java.util.Set of other supported KB API or Java
   * objects. It even supports, List of List etc.
   *
   * @param args the arguments of the formula in order
   * @return a Sentence object
   *
   * @throws KbTypeException never thrown
   * @throws com.cyc.kb.exception.CreateException
   * /
  Sentence get(Iterable... args) throws KbTypeException, CreateException;
   */
  /**
   * Conjoin sentences. Creates a list and calls {@link #and(java.lang.Iterable)}
   *
   * @param sentences list of sentences to be conjoined
   *
   * @return a new conjoined sentence
   *
   * @throws KbTypeException
   * @throws com.cyc.kb.exception.CreateException
   */
  Sentence and(Sentence... sentences) throws KbTypeException, CreateException;

  /**
   * Conjoin sentences. Creates a new sentence with #$and as the relation and all other sentences as
   * the arguments.
   *
   * @param sentences list of sentences to be conjoined
   *
   * @return a new conjunction sentence
   *
   * @throws com.cyc.kb.exception.KbTypeException
   * @throws com.cyc.kb.exception.CreateException
   */
  Sentence and(Iterable<Sentence> sentences) throws KbTypeException, CreateException;

  /**
   * Make a new conditional sentence with the specified antecedent and consequent.
   *
   * @param antecedent
   * @param consequent
   *
   * @return a new conditional Sentence
   *
   * @throws com.cyc.kb.exception.KbTypeException
   * @throws com.cyc.kb.exception.CreateException
   */
  Sentence implies(Collection<Sentence> antecedent, Sentence consequent)
          throws KbTypeException, CreateException;

  /**
   * Make a new conditional sentence with the specified antecedent and consequent.
   *
   * @param antecedent
   * @param consequent
   *
   * @return a new conditional Sentence
   *
   * @throws com.cyc.kb.exception.KbTypeException
   * @throws com.cyc.kb.exception.CreateException
   */
  Sentence implies(Sentence antecedent, Sentence consequent) throws KbTypeException, CreateException;

  /**
   * Disjoin sentences. Creates a list and calls {@link #or(java.lang.Iterable)}
   *
   * @param sentences list of sentences to be disjoined
   *
   * @return a new disjunction sentence
   *
   * @throws KbTypeException
   * @throws com.cyc.kb.exception.CreateException
   */
  Sentence or(Sentence... sentences) throws KbTypeException, CreateException;

  /**
   * Disjoin sentences. Creates a new sentence with #$or as the relation and all other sentences as
   * the arguments.
   *
   * @param sentences list of sentences to be disjoined
   *
   * @return a new disjunction sentence
   *
   * @throws com.cyc.kb.exception.KbTypeException
   * @throws com.cyc.kb.exception.CreateException
   */
  Sentence or(Iterable<Sentence> sentences) throws KbTypeException, CreateException;

}
