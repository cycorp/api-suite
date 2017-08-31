package com.cyc.kb;

/*
 * #%L
 * File: KbCollection.java
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
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbTypeException;

import java.util.Collection;

/**
 * The top-level interface corresponding to Cyc <code>#$Collection</code>. A
 * <code>KbCollection</code> is defined primarily by its specializations and
 * generalizations in a multiple-inheritance subsumption hierarchy. Every
 * {@link KbTerm} is an instance of at least one <code>KbCollection</code>.
 *
 * @author vijay
 */
public interface KbCollection extends KbTerm {

  /**
   * Finds all the specializations of <code>this</code>
   * <code>KbCollection</code>, including the entire downward
   * transitive closure, acontextually (ignoring Microtheories.)
   *
   * @return all <code>KbCollection</code>s that are specializations of
   * <code>this</code> <code>KbCollection</code>
   *
   */
  public Collection<KbCollection> allSpecializations();

  /**
   * Finds all the specializations of <code>this</code>
   * <code>KbCollection</code> in the context including the entire downward
   * transitive closure.
   *
   * @param ctxStr the string representing the context of query
   *
   * @return all <code>KbCollection</code>s that are specializations of
   * <code>this</code> <code>KbCollection</code>
   */
  public Collection<KbCollection> allSpecializations(String ctxStr);

  /**
   * Finds all the specializations of <code>this</code>
   * <code>KbCollection</code> in the context including the entire downward
   * transitive closure
   *
   * @param ctx the context of query
   *
   * @return all <code>KbCollections</code> that are specializations of
   * <code>this</code> <code>KbCollection</code>
   */
  public Collection<KbCollection> allSpecializations(Context ctx);

  /**
   * Finds the asserted specializations of <code>this</code>
   * <code>KbCollection</code>, from the default context specified by
   * {@link com.cyc.kb.DefaultContext#forQuery()}.
   *
   * @return the <code>KbCollection</code>s that are specializations of
   * <code>this</code> <code>KbCollection</code>.
   */
  // @todo Consider adding max-specs as another method.
  public Collection<KbCollection> getSpecializations();

  /**
   * Finds the asserted specializations of <code>this</code>
   * <code>KbCollection</code>, from the context <code>ctx</code>.
   *
   * @param ctxStr the string representing the context of query
   *
   * @return the <code>KbCollection</code>s that are specializations of
   * <code>this</code> <code>KbCollection</code>
   */
  public Collection<KbCollection> getSpecializations(String ctxStr);

  /**
   * Finds the asserted specializations of <code>this</code>
   * <code>KbCollection</code>, from a context <code>ctx</code>.
   *
   * @param ctx the context of query
   *
   * @return the <code>KbCollection</code>s that are specializations of
   * <code>this</code> <code>KbCollection</code>
   */
  public Collection<KbCollection> getSpecializations(Context ctx);

  /**
   * Creates a new Fact stating that the <code>#$Collection</code> represented
   * by <code>moreSpecificStr</code> is a specialization of <code>this</code> in
   * the default assertion context. The new assertion is added regardless of
   * whether or not <code>(#$genls moreSpecific this)</code> is already
   * provable.
   * <p>
   *
   * @param moreSpecificStr the string representing the specialization
   * (collection) of <code>this</code> <code>KbCollection</code>
   *
   * @return this
   *
   * @throws CreateException
   * @throws KbTypeException
   */
  public KbCollection addSpecialization(String moreSpecificStr)
          throws KbTypeException, CreateException;

  /**
   * Creates a new Fact stating that the <code>#$Collection</code> represented
   * by <code>moreSpecificStr</code> is a specialization of <code>this</code> in
   * the context. The new assertion is added regardless of whether or not
   * <code>(#$genls moreSpecific this)</code> is already provable.
   * <p>
   *
   * @param moreSpecificStr the string representing the specialization
   * (collection) of <code>this</code> <code>KbCollection</code>
   * @param ctxStr the string representing the context where the fact is
   * asserted
   *
   * @return this
   *
   * @throws CreateException
   * @throws KbTypeException
   */
  public KbCollection addSpecialization(String moreSpecificStr,
          String ctxStr) throws KbTypeException, CreateException;

  /**
   * Creates a new Fact stating that <code>moreSpecific</code> is a
   * specialization of <code>this</code> in the default assertion context. The
   * new assertion is added regardless of whether or not
   * <code>(#$genls moreSpecific this)</code> is already provable.
   * <p>
   *
   * @param moreSpecific the specialization (collection) of <code>this</code>
   * <code>KbCollection</code>
   *
   * @return <code>this</code>
   *
   * @throws CreateException
   * @throws KbTypeException
   */
  public KbCollection addSpecialization(KbCollection moreSpecific)
          throws KbTypeException, CreateException;

  /**
   * Creates a new Fact stating that <code>moreSpecific</code> is a
   * specialization of <code>this</code> in the Context ctx. The new assertion
   * is added regardless of whether or not
   * <code>(#$genls moreSpecific this)</code> is already provable.
   * <p>
   *
   * @param moreSpecific the specialization (collection) of <code>this</code>
   * <code>KbCollection</code>
   * @param ctx the context where the fact is asserted
   *
   * @return <code>this</code>
   *
   * @throws CreateException
   * @throws KbTypeException
   */
  public KbCollection addSpecialization(KbCollection moreSpecific,
          Context ctx) throws KbTypeException, CreateException;

  /*
   * genls methods
   */
  /**
   * Finds all the generalizations of <code>this</code>
   * <code>KbCollection</code>, including the entire upward
   * transitive closure, in any microtheory.
   *
   * @return all the <code>KbCollection</code>s which are generalizations of
   * <code>this</code> <code>KbCollection</code>
   */
  public Collection<KbCollection> allGeneralizations();

  /**
   * Finds all the generalizations of this <code>KbCollection</code> in the
   * context <code>ctx</code>, including the entire upward transitive closure.
   *
   * @param ctxStr the string representing the context of the query
   *
   * @return all the <code>KbCollection</code>s which are generalizations of
   * <code>this</code> <code>KbCollection</code>
   */
  public Collection<KbCollection> allGeneralizations(String ctxStr);

  /**
   * Finds all the generalizations of <code>this</code>
   * <code>KbCollection</code> in the context including the entire upward
   * transitive closure.
   *
   * @param ctx the context of the query
   *
   * @return all the <code>KbCollection</code>s which are generalizations of
   * <code>this</code> <code>KbCollection</code>
   */
  public Collection<KbCollection> allGeneralizations(Context ctx);

  /**
   * Finds the asserted generalizations of <code>this</code>
   * <code>KbCollection</code>, from the default context specified by
   * {@link com.cyc.kb.DefaultContext#forQuery()}.
   *
   * @return the <code>KbCollection</code>s that are generalizations of
   * <code>this</code> <code>KbCollection</code>
   */
  public Collection<? extends KbCollection> getGeneralizations();

  /**
   * Finds the asserted generalizations of <code>this</code>
   * <code>KbCollection</code>, from a context specified by <code>ctx</code>.
   *
   * @param ctxStr the string representing the context
   *
   * @return the <code>KbCollection</code>s that are generalizations of
   * <code>this</code> <code>KbCollection</code>
   */
  public Collection<? extends KbCollection> getGeneralizations(String ctxStr);

  /**
   * Finds the asserted generalizations of <code>this</code>
   * <code>KbCollection</code>, from a context specified by <code>ctx</code>.
   *
   * @param ctx the context
   *
   * @return the <code>KbCollection</code>s that are generalizations of * *
   * <code>this</code> <code>KbCollection</code>
   */
  public Collection<? extends KbCollection> getGeneralizations(
          Context ctx);

  /**
   * Creates a new <code>Fact</code> stating that the #$Collection represented
   * by <code>moreGeneralStr</code> is a generalization of <code>this</code> in
   * the default assertion context. The new assertion is added regardless of
   * whether or not <code>(#$genls this moreGeneral)</code> is already provable.
   *
   * @param moreGeneralStr the string representing the generalization of
   * <code>this</code>
   *
   * @return <code>this</code>
   *
   * @throws CreateException
   * @throws KbTypeException
   */
  public KbCollection addGeneralization(String moreGeneralStr)
          throws KbTypeException, CreateException;

  /**
   * Creates a new <code>Fact</code> stating that the #$Collection represented
   * by <code>moreGeneralStr</code> is a generalization of <code>this</code> in
   * the context. The new assertion is added regardless of whether or not
   * <code>(#$genls this moreGeneral)</code> is already provable.
   *
   * @param moreGeneralStr the string representing the generalization of
   * <code>this</code>
   * @param ctxStr the string representing the context where the fact is
   * asserted
   *
   * @return <code>this</code>
   *
   * @throws CreateException
   * @throws KbTypeException
   */
  public KbCollection addGeneralization(String moreGeneralStr, String ctxStr)
          throws KbTypeException, CreateException;

  /**
   * Creates a new <code>Fact</code> stating that the <code>moreGeneral</code>
   * is a generalization of <code>this</code> in the default assertion context.
   * The new assertion is added regardless of whether or not
   * <code>(#$genls this moreGeneral)</code> is already provable.
   *
   * @param moreGeneral the generalization of <code>this</code>
   *
   * @return <code>this</code>
   *
   * @throws CreateException
   * @throws KbTypeException
   */
  public KbCollection addGeneralization(KbCollection moreGeneral)
          throws KbTypeException, CreateException;

  /**
   * Creates a new <code>Fact</code> stating that the <code>moreGeneral</code>
   * is a generalization of <code>this</code> in the context. The new assertion
   * is added regardless of whether or not
   * <code>(#$genls this moreGeneral)</code> is already provable.
   *
   * @param moreGeneral the generalization of <code>this</code>
   * @param ctx the context where the fact is asserted
   *
   * @return <code>this</code>
   *
   * @throws CreateException
   * @throws KbTypeException
   */
  public KbCollection addGeneralization(KbCollection moreGeneral,
          Context ctx) throws KbTypeException, CreateException;

  /**
   * This method returns the Sentence <code>(#$genls this moreGeneral)</code>. The key
   * difference between this and {@link #addGeneralization(com.cyc.kb.KbCollection) } is
   * that, this method does not make any assertion in the KB. The sentence form of the
   * assertion is generally useful when seeking user feedback before asserting into the
   * KB. Use {@link Sentence#assertIn(com.cyc.kb.Context) } to assert the sentence in
   * a Context.
   * 
   * @param moreGeneral the generalization of <code>this</code>
   * 
   * @return the #$genls sentence between this and moreGeneral
   * 
   * @throws KbTypeException 
   */
  public Sentence addGeneralizationSentence(KbCollection moreGeneral) 
          throws KbTypeException, CreateException;
  
  /**
   * Finds the asserted instances of <code>this</code> <code>KbCollection</code>
   * from the default context specified by
   * {@link com.cyc.kb.DefaultContext#forQuery()}. For <code>KbCollection</code>s that
   * are instances of <code>#$FirstOrderCollection</code>, this can only return
   * Individuals. For other collections, it can only return
   * <code>KbCollection</code>s or its subclasses.
   *
   * @param <O> the class the instances are cast to. Typically
   * <code>Individual</code> or <code>KbCollection</code>.
   *
   * @return collection of objects of Type <code>O</code>
   */
  public <O> Collection<? extends O> getInstances();

  /**
   * Finds the asserted instances of <code>this</code> <code>KbCollection</code>
   * from a particular context. For <code>KbCollection</code>s that are
   * instances of <code>#$FirstOrderCollection</code>, this can only return
   * <code>Individual</code>s. For other collections, it can only return
   * <code>KbCollection</code>s.
   *
   * @param ctxStr the string representing the context of query
   * @param <O> the class the instances are cast to Typically
   * <code>Individual</code> or <code>KbCollection</code>.
   *
   * @return collection of objects of Type <code>O</code>
   */
  public <O> Collection<O> getInstances(String ctxStr);

  /**
   * Finds the asserted instances of <code>this</code> <code>KbCollection</code>
   * from a particular context. For <code>KbCollection</code>s that are
   * instances of <code>#$FirstOrderCollection</code>, this can only return
   * <code>Individual</code>s. For other collections, it can only return
   * <code>KbCollection</code>s or its subclasses.
   * <p>
   *
   * @param ctx the context of query
   * @param <O> the class the instances are cast to Typically
   * <code>Individual</code> or <code>KbCollection</code>.
   *
   * @return objects of <code>O</code>
   */
  public <O> Collection<O> getInstances(Context ctx);

  /**
   * Finds the asserted types <code>this</code> <code>KbCollection</code>
   * belongs to, from the default context specified by
   * {@link com.cyc.kb.DefaultContext#forQuery()}. In other words, this returns the
   * asserted values of <code>?X</code> in <code>(#$isa this ?X)</code>
   *
   * @return Collections (Second Order or higher) <code>this</code>
   * <code>KbCollection</code> belongs to
   */
  public Collection<KbCollection> instancesOf();

  /**
   * Finds the asserted types <code>this</code> <code>KbCollection</code>
   * belongs to, from a context <code>ctx</code>. In other words, this returns
   * the asserted values of <code>?X</code> in <code>(#$isa this ?X)</code>
   *
   * @param ctxStr the string representing the context
   *
   * @return Collections (Second Order or higher) <code>this</code>
   * <code>KbCollection</code> belongs to
   */
  public Collection<KbCollection> instancesOf(String ctxStr);

  /**
   * Finds the asserted types <code>this</code> <code>KbCollection</code>
   * belongs to, from a context <code>ctx</code>. In other words, this returns
   * the asserted values of <code>?X</code> in <code>(#$isa this ?X)</code>
   *
   * @param ctx the context
   *
   * @return Collections (Second Order or higher) * * <code>this</code>
   * <code>KbCollection</code> belongs to
   */
  public Collection<KbCollection> instancesOf(Context ctx);

  /**
   * Creates a new Fact stating that <code>this</code> object is an instance of
   * the <code>#$Collection</code> represented by <code>col</code> in the
   * context <code>ctx</code>. In other words, this method asserts
   * <code>(#$isa this col)</code>. The new assertion is added regardless of
   * whether or not <code>(#$isa this col)</code> is already provable.
   *
   * @param colStr the String representing the higher order collection
   * <code>this</code> will become an instance of.
   * @param ctxStr the string representing the context where the fact is
   * asserted
   *
   * @return <code>this</code>
   *
   * @throws CreateException
   * @throws KbTypeException
   */
  public KbCollection instantiates(String colStr, String ctxStr)
          throws KbTypeException, CreateException;

  /**
   * Creates a new Fact stating that <code>this</code> object is an instance of
   * the <code>#$Collection</code> represented by <code>col</code> in the
   * context <code>ctx</code>. In other words, this method asserts
   * <code>(#$isa this col)</code>. The new assertion is added regardless of
   * whether or not <code>(#$isa this col)</code> is already provable.
   *
   * @param col the collection that <code>this</code> will become an instance
   * of.
   * @param ctx the context where the fact is asserted
   *
   * @return <code>this</code>
   *
   * @throws CreateException
   * @throws KbTypeException
   */
  public KbCollection instantiates(KbCollection col, Context ctx)
          throws KbTypeException, CreateException;

  /**
   * Determine whether <code>this</code> is a generalization of
   * <code>moreSpecific</code> in some context. Essentially, this amounts to
   * determining whether <code>(#$genls moreSpecific this)</code> is true. Note
   * that <code>(#$genls moreSpecific this)</code> need not be asserted to be
   * true.
   *
   * @param moreSpecific the possibly more specific collection
   * @param ctx the context where the fact is queried
   *
   * @return true if (#$genls moreSpecific this) is true
   */
  public boolean isGeneralizationOf(KbCollection moreSpecific,
          Context ctx);

  /**
   * Determine whether <code>this</code> is a generalization of
   * <code>moreSpecific</code> in some context. Essentially, this amounts to
   * determining whether <code>(#$genls moreSpecific this)</code> is true. Note
   * that <code>(#$genls moreSpecific this)</code> need not be asserted to be
   * true.
   *
   * @param moreSpecificStr the string representing the possibly more specific
   * collection
   *
   * @return true if (#$genls moreSpecific this) is true
   */
  public boolean isGeneralizationOf(String moreSpecificStr);

  /**
   * Determine whether <code>this</code> is a generalization of
   * <code>moreSpecific</code> in a context. Essentially, this amounts to
   * determining whether <code>(#$genls moreSpecific this)</code> is true. Note
   * that <code>(#$genls moreSpecific this)</code> need not be asserted to be
   * true.
   *
   * @param moreSpecific the possibly more specific collection
   *
   * @return true if (#$genls moreSpecific this) is true
   */
  public boolean isGeneralizationOf(KbCollection moreSpecific);
  
}
