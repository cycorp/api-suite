package com.cyc.kb;

/*
 * #%L
 * File: KbPredicate.java
 * Project: Core API Object Specification
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

import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbException;
import com.cyc.kb.exception.KbTypeException;

import java.util.Collection;
import java.util.List;

/**
 * The interface for Cyc predicates. <code>KbPredicates</code> are applied to
 * one or more arguments to form a non-atomic {@link Sentence}.
 *
 * @author vijay
 */
public interface KbPredicate extends Relation {

  /**
   * finds the asserted specializations of <code>this</code>
   * <code>Predicate</code>, from the default context specified by
   * {@link com.cyc.kb.DefaultContext#forQuery()}.
   *
   * @return the <code>KbPredicate</code>s that are specialization of
   * <code>this</code> <code>KbPredicate</code>
   */
  public Collection<KbPredicate> getSpecializations();

  /**
   * finds the asserted specializations of <code>this</code>
   * <code>KbPredicate</code>, from the context <code>ctx</code>.
   *
   * @param ctxStr the string representing the context of query
   *
   * @return the <code>KbPredicate</code>s that are specializations of
   * <code>this</code> <code>KbPredicate</code>
   */
  public Collection<KbPredicate> getSpecializations(String ctxStr);

  /**
   * finds the asserted specializations of <code>this</code>
   * <code>KbPredicate</code>, from the context <code>ctx</code>
   *
   * @param ctx the context of query
   *
   * @return the <code>KbPredicate</code>s that are specializations of
   * <code>this</code> <code>KbPredicate</code>
   */
  public Collection<KbPredicate> getSpecializations(
          Context ctx);

  /**
   * creates a new <code>Fact</code> stating that <code>this</code> is a more
   * specific predicate than <code>moreSpecific</code> in the specified context.
   * Essentially, it asserts <code>(#$genlPreds moreSpecific this)</code>
   * <p>
   * genlPreds relates a predicate to its generalization. Any sentence that is
   * true with a given predicate in argument position zero, is also true with
   * the predicate replaced by its generalization.
   *
   * @param moreSpecificStr the string representing the specialization
   * (predicate) of this KbPredicate
   * @param ctxStr the string representing the context where the fact is
   * asserted
   *
   * @return this
   *
   * @throws CreateException
   * @throws KbTypeException
   */
  public KbPredicate addSpecialization(String moreSpecificStr, String ctxStr)
          throws KbTypeException, CreateException;

  /**
   * creates a new <code>Fact</code> stating that <code>this</code> is a more
   * specific predicate than <code>moreSpecific</code> in the specified context.
   * Essentially, it asserts <code>(#$genlPreds moreSpecific this)</code>
   * <p>
   * genlPreds relates a predicate to its generalization. Any sentence that is
   * true with a given predicate in argument position zero, is also true with
   * the predicate replaced by its generalization.
   *
   * @param ctx the context where the fact is asserted
   * @param moreSpecific the specialization (predicate) of this KbPredicate
   *
   * @return this
   *
   * @throws CreateException
   * @throws KbTypeException
   */
  public KbPredicate addSpecialization(KbPredicate moreSpecific,
          Context ctx) throws KbTypeException, CreateException;

  /**
   * finds the asserted generalizations of <code>this</code>, from the default
   * context specified by {@link com.cyc.kb.DefaultContext#forQuery()}
   *
   * @return the <code>KbPredicate</code>s that are generalizations of this
   * <code>KbPredicate</code>
   * @throws KbException
   */
  // @todo throw more specific exception
  public Collection<KbPredicate> getGeneralizations() throws KbException;
  
  /**
   * finds the asserted generalizations of <code>this</code>, from a context
   * <code>ctx</code>.
   *
   * @param ctxStr the string representing the context
   *
   * @return the <code>KbPredicate</code>s that are generalizations of this
   * <code>KbPredicate</code>
   */
  public Collection<KbPredicate> getGeneralizations(String ctxStr);
  
  /**
   * finds the asserted generalizations of <code>this</code>, from the context
   * <code>ctx</code>.
   *
   * @param ctx the context
   *
   * @return the <code>KbPredicate</code>s that are generalizations of this
   * <code>KbPredicate</code>
   */
  public Collection<KbPredicate> getGeneralizations(Context ctx);
  
  /**
   * This method returns the Sentence <code>(#$genlPreds this moreGeneral)</code>. The key
   * difference between this and that, this method does not make any assertion in the KB. The
   * sentence form of the assertion is generally useful when seeking user feedback before asserting
   * into the KB. Use {@link Sentence#assertIn(com.cyc.kb.Context) } to assert the sentence in a
   * Context.
   *
   * @param moreGeneral the more general predicate of <code>this</code>
   * 
   * @return the sentence <code>(#$genlPreds this moreGeneral)</code>
   * 
   * @throws KbTypeException 
   */
  public Sentence getGeneralizationSentence(KbPredicate moreGeneral) 
          throws KbTypeException, CreateException;

  /**
   * This method returns the Sentence <code>(#$genlInverse this moreGeneral)</code>. 
   * The sentence form of the assertion is generally useful when seeking user 
   * feedback before asserting into the
   * KB. Use {@link Sentence#assertIn(com.cyc.kb.Context) } to assert the sentence in
   * a Context.
   * 
   * @param moreGeneral the more general predicate of <code>this</code>
   * 
   * @return the sentence <code>(#$genlInverse this moreGeneral)</code>
   * 
   * @throws KbTypeException 
   */
  public Sentence getInverseGeneralizationSentence(KbPredicate moreGeneral) 
          throws KbTypeException, CreateException;

  /**
   * creates a new <code>Fact</code> stating that <code>moreGeneral</code> is a
   * more general predicate than <code>this</code> in the context
   * <code>ctx</code>. Essentially, this asserts
   * <code>(genlPreds this moreGeneral)</code>
   * <p>
   * genlPreds relates a predicate to its generalization. Any sentence that is
   * true with a given predicate in argument position zero, is also true with
   * the predicate replaced by its generalization.
   *
   * @param moreGeneralStr the string representing the <code>KbPredicate</code>
   * that is to become more general than this <code>KbPredicate</code>
   * @param ctxStr the string representing the context where the fact is
   * asserted
   *
   * @return this
   *
   * @throws CreateException
   * @throws KbTypeException
   */
  public KbPredicate addGeneralization(String moreGeneralStr, String ctxStr)
          throws KbTypeException, CreateException;

  /**
   * creates a new <code>Fact</code> stating that <code>moreGeneral</code> is a
   * more general predicate than <code>this</code> in the context. Essentially,
   * this asserts <code>(genlPreds this moreGeneral)</code>
   * <p>
   *
   * @param moreGeneral the <code>KbPredicate</code> that is to become more
   * general than this
   * @param ctx the context where the fact is asserted
   *
   * @return this
   *
   * @throws CreateException
   * @throws KbTypeException
   */
  public KbPredicate addGeneralization(KbPredicate moreGeneral,
          Context ctx) throws KbTypeException, CreateException;

  /**
   * Determine whether <code>this</code> is a generalization of
   * <code>moreSpecific</code> in the context <code>ctx</code>. Essentially,
   * this amounts to determining whether
   * <code>(#$genlPreds moreSpecific this)</code> is true. Note that
   * <code>(#$genlPreds moreSpecific this)</code> need not be asserted to be
   * true.
   *
   * @param moreSpecific the possibly more specific predicate
   * @param ctx the context of query
   *
   * @return true if (#$genlPreds moreSpecific this) is true
   *
   */
  public boolean isGeneralizationOf(KbPredicate moreSpecific,
          Context ctx);

  public List<Fact> getExtent();
  
  public List<Fact> getExtent(Context ctx);
  
}
