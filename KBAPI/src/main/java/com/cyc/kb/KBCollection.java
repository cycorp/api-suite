package com.cyc.kb;

/*
 * #%L
 * File: KBCollection.java
 * Project: KB API
 * %%
 * Copyright (C) 2013 - 2014 Cycorp, Inc
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
import java.util.Collection;

import com.cyc.base.cycobject.FormulaSentence;
import com.cyc.kb.config.KBAPIDefaultContext;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KBApiException;
import com.cyc.kb.exception.KBTypeException;
import com.cyc.kb.quant.InstanceRestrictedVariable;

/**
 * The top-level interface corresponding to Cyc <code>#$Collection</code>. A
 * <code>KBCollection</code> is defined primarily by its specializations and
 * generalizations in a multiple-inheritance subsumption hierarchy. Every
 * {@link KBTerm} is an instance of at least one <code>KBCollection</code>.
 *
 * @author vijay
 */
public interface KBCollection extends KBTerm {

  /**
   * Finds all the specializations of <code>this</code>
   * <code>KBCollection</code>, including the entire downward
   * transitive closure, acontextually (ignoring Microtheories.)
   *
   * @return all <code>KBCollection</code>s that are specializations of
   * <code>this</code> <code>KBCollection</code>
   *
   */
  public Collection<KBCollection> allSpecializations();

  /**
   * Finds all the specializations of <code>this</code>
   * <code>KBCollection</code> in the context including the entire downward
   * transitive closure.
   *
   * @param ctxStr the string representing the context of query
   *
   * @return all <code>KBCollection</code>s that are specializations of
   * <code>this</code> <code>KBCollection</code>
   */
  public Collection<KBCollection> allSpecializations(String ctxStr);

  /**
   * Finds all the specializations of <code>this</code>
   * <code>KBCollection</code> in the context including the entire downward
   * transitive closure
   *
   * @param ctx	the context of query
   *
   * @return	all <code>KBCollections</code> that are specializations of
   * <code>this</code> <code>KBCollection</code>
   */
  public Collection<KBCollection> allSpecializations(Context ctx);

  /**
   * Finds the asserted specializations of <code>this</code>
   * <code>KBCollection</code>, from the default context specified by
   * {@link KBAPIDefaultContext#forQuery()}.
   *
   * @return	the <code>KBCollection</code>s that are specializations of
   * <code>this</code> <code>KBCollection</code>.
   */
  // @todo Consider adding max-specs as another method.
  public Collection<KBCollection> getSpecializations();

  /**
   * Finds the asserted specializations of <code>this</code>
   * <code>KBCollection</code>, from the context <code>ctx</code>.
   *
   * @param ctxStr	the string representing the context of query
   *
   * @return	the <code>KBCollection</code>s that are specializations of
   * <code>this</code> <code>KBCollection</code>
   */
  public Collection<KBCollection> getSpecializations(String ctxStr);

  /**
   * Finds the asserted specializations of <code>this</code>
   * <code>KBCollection</code>, from a context <code>ctx</code>.
   *
   * @param ctx	the context of query
   *
   * @return	the <code>KBCollection</code>s that are specializations of
   * <code>this</code> <code>KBCollection</code>
   */
  public Collection<KBCollection> getSpecializations(Context ctx);

  /**
   * Creates a new Fact stating that the <code>#$Collection</code> represented
   * by <code>moreSpecificStr</code> is a specialization of <code>this</code> in
   * the default assertion context. The new assertion is added regardless of
   * whether or not <code>(#$genls moreSpecific this)</code> is already
   * provable.
   * <p>
   *
   * @param moreSpecificStr the string representing the specialization
   * (collection) of <code>this</code> <code>KBCollection</code>
   *
   * @return this
   *
   * @throws CreateException
   * @throws KBTypeException
   */
  public KBCollection addSpecialization(String moreSpecificStr)
          throws KBTypeException, CreateException;

  /**
   * Creates a new Fact stating that the <code>#$Collection</code> represented
   * by <code>moreSpecificStr</code> is a specialization of <code>this</code> in
   * the context. The new assertion is added regardless of whether or not
   * <code>(#$genls moreSpecific this)</code> is already provable.
   * <p>
   *
   * @param moreSpecificStr the string representing the specialization
   * (collection) of <code>this</code> <code>KBCollection</code>
   * @param ctxStr the string representing the context where the fact is
   * asserted
   *
   * @return this
   *
   * @throws CreateException
   * @throws KBTypeException
   */
  public KBCollection addSpecialization(String moreSpecificStr,
          String ctxStr) throws KBTypeException, CreateException;

  /**
   * Creates a new Fact stating that <code>moreSpecific</code> is a
   * specialization of <code>this</code> in the default assertion context. The
   * new assertion is added regardless of whether or not
   * <code>(#$genls moreSpecific this)</code> is already provable.
   * <p>
   *
   * @param moreSpecific the specialization (collection) of <code>this</code>
   * <code>KBCollection</code>
   *
   * @return <code>this</code>
   *
   * @throws CreateException
   * @throws KBTypeException
   */
  public KBCollection addSpecialization(KBCollection moreSpecific)
          throws KBTypeException, CreateException;

  /**
   * Creates a new Fact stating that <code>moreSpecific</code> is a
   * specialization of <code>this</code> in the Context ctx. The new assertion
   * is added regardless of whether or not
   * <code>(#$genls moreSpecific this)</code> is already provable.
   * <p>
   *
   * @param moreSpecific the specialization (collection) of <code>this</code>
   * <code>KBCollection</code>
   * @param ctx the context where the fact is asserted
   *
   * @return <code>this</code>
   *
   * @throws CreateException
   * @throws KBTypeException
   */
  public KBCollection addSpecialization(KBCollection moreSpecific,
          Context ctx) throws KBTypeException, CreateException;

  /*
   * genls methods
   */
  /**
   * Finds all the generalizations of <code>this</code>
   * <code>KBCollection</code>, including the entire upward
   * transitive closure, in any microtheory.
   *
   * @return	all the <code>KBCollection</code>s which are generalizations of
   * <code>this</code> <code>KBCollection</code>
   */
  public Collection<KBCollection> allGeneralizations();

  /**
   * Finds all the generalizations of this <code>KBCollection</code> in the
   * context <code>ctx</code>, including the entire upward transitive closure.
   *
   * @param ctxStr	the string representing the context of the query
   *
   * @return	all the <code>KBCollection</code>s which are generalizations of
   * <code>this</code> <code>KBCollection</code>
   */
  public Collection<KBCollection> allGeneralizations(String ctxStr);

  /**
   * Finds all the generalizations of <code>this</code>
   * <code>KBCollection</code> in the context including the entire upward
   * transitive closure.
   *
   * @param ctx	the context of the query
   *
   * @return	all the <code>KBCollection</code>s which are generalizations of
   * <code>this</code> <code>KBCollection</code>
   */
  public Collection<KBCollection> allGeneralizations(Context ctx);

  /**
   * Finds the asserted generalizations of <code>this</code>
   * <code>KBCollection</code>, from the default context specified by
   * {@link KBAPIDefaultContext#forQuery()}.
   *
   * @return	the <code>KBCollection</code>s that are generalizations of
   * <code>this</code> <code>KBCollection</code>
   */
  public Collection<? extends KBCollection> getGeneralizations();

  /**
   * Finds the asserted generalizations of <code>this</code>
   * <code>KBCollection</code>, from a context specified by <code>ctx</code>.
   *
   * @param ctxStr	the string representing the context
   *
   * @return	the <code>KBCollection</code>s that are generalizations of
   * <code>this</code> <code>KBCollection</code>
   */
  public Collection<? extends KBCollection> getGeneralizations(String ctxStr);

  /**
   * Finds the asserted generalizations of <code>this</code>
   * <code>KBCollection</code>, from a context specified by <code>ctx</code>.
   *
   * @param ctx	the context
   *
   * @return	the <code>KBCollection</code>s that are generalizations of * *
   * <code>this</code> <code>KBCollection</code>
   */
  public Collection<? extends KBCollection> getGeneralizations(
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
   * @throws KBTypeException
   */
  public KBCollection addGeneralization(String moreGeneralStr)
          throws KBTypeException, CreateException;

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
   * @throws KBTypeException
   */
  public KBCollection addGeneralization(String moreGeneralStr, String ctxStr)
          throws KBTypeException, CreateException;

  /**
   * Creates a new <code>Fact</code> stating that the <code>moreGeneral</code>
   * is a generalization of <code>this</code> in the default assertion context.
   * The new assertion is added regardless of whether or not
   * <code>(#$genls this moreGeneral)</code> is already provable.
   *
   * @param moreGeneral the generalization of <code>this</code>
   *
   * @return	<code>this</code>
   *
   * @throws CreateException
   * @throws KBTypeException
   */
  public KBCollection addGeneralization(KBCollection moreGeneral)
          throws KBTypeException, CreateException;

  /**
   * Creates a new <code>Fact</code> stating that the <code>moreGeneral</code>
   * is a generalization of <code>this</code> in the context. The new assertion
   * is added regardless of whether or not
   * <code>(#$genls this moreGeneral)</code> is already provable.
   *
   * @param moreGeneral the generalization of <code>this</code>
   * @param ctx the context where the fact is asserted
   *
   * @return	<code>this</code>
   *
   * @throws CreateException
   * @throws KBTypeException
   */
  public KBCollection addGeneralization(KBCollection moreGeneral,
          Context ctx) throws KBTypeException, CreateException;

  /**
   * This method returns the Sentence <code>(#$genls this moreGeneral)</code>. The key
   * difference between this and {@link #addGeneralization(com.cyc.kb.KBCollection) } is
   * that, this method does not make any assertion in the KB. The sentence form of the
   * assertion is generally useful when seeking user feedback before asserting into the
   * KB. Use {@link Sentence#assertIn(com.cyc.kb.Context) } to assert the sentence in
   * a Context.
   * 
   * @param moreGeneral the generalization of <code>this</code>
   * 
   * @return the #$genls sentence between this and moreGeneral
   * 
   * @throws KBTypeException 
   */
  public Sentence addGeneralizationSentence(KBCollection moreGeneral) throws KBTypeException, CreateException;
  
  /**
   * Finds the asserted instances of <code>this</code> <code>KBCollection</code>
   * from the default context specified by
   * {@link KBAPIDefaultContext#forQuery()}. For <code>KBCollection</code>s that
   * are instances of <code>#$FirstOrderCollection</code>, this can only return
   * Individuals. For other collections, it can only return
   * <code>KBCollection</code>s or its subclasses.
   *
   * @param <O>	the class the instances are cast to. Typically
   * <code>Individual</code> or <code>KBCollection</code>.
   *
   * @return	collection of objects of Type <code>O</code>
   */
  public <O> Collection<? extends O> getInstances();

  /**
   * Finds the asserted instances of <code>this</code> <code>KBCollection</code>
   * from a particular context. For <code>KBCollection</code>s that are
   * instances of <code>#$FirstOrderCollection</code>, this can only return
   * <code>Individual</code>s. For other collections, it can only return
   * <code>KBCollection</code>s.
   *
   * @param ctxStr	the string representing the context of query
   * @param <O>	the class the instances are cast to Typically
   * <code>Individual</code> or <code>KBCollection</code>.
   *
   * @return	collection of objects of Type <code>O</code>
   */
  public <O> Collection<O> getInstances(String ctxStr);

  /**
   * Finds the asserted instances of <code>this</code> <code>KBCollection</code>
   * from a particular context. For <code>KBCollection</code>s that are
   * instances of <code>#$FirstOrderCollection</code>, this can only return
   * <code>Individual</code>s. For other collections, it can only return
   * <code>KBCollection</code>s or its subclasses.
   * <p>
   *
   * @param ctx	the context of query
   * @param <O>	the class the instances are cast to Typically
   * <code>Individual</code> or <code>KBCollection</code>.
   *
   * @return	objects of <code>O</code>
   */
  public <O> Collection<O> getInstances(Context ctx);

  /**
   * Finds the asserted types <code>this</code> <code>KBCollection</code>
   * belongs to, from the default context specified by
   * {@link KBAPIDefaultContext#forQuery()}. In other words, this returns the
   * asserted values of <code>?X</code> in <code>(#$isa this ?X)</code>
   *
   * @return	Collections (Second Order or higher) <code>this</code>
   * <code>KBCollection</code> belongs to
   */
  public Collection<KBCollection> instancesOf();

  /**
   * Finds the asserted types <code>this</code> <code>KBCollection</code>
   * belongs to, from a context <code>ctx</code>. In other words, this returns
   * the asserted values of <code>?X</code> in <code>(#$isa this ?X)</code>
   *
   * @param ctxStr	the string representing the context
   *
   * @return	Collections (Second Order or higher) <code>this</code>
   * <code>KBCollection</code> belongs to
   */
  public Collection<KBCollection> instancesOf(String ctxStr);

  /**
   * Finds the asserted types <code>this</code> <code>KBCollection</code>
   * belongs to, from a context <code>ctx</code>. In other words, this returns
   * the asserted values of <code>?X</code> in <code>(#$isa this ?X)</code>
   *
   * @param ctx	the context
   *
   * @return	Collections (Second Order or higher) * * <code>this</code>
   * <code>KBCollection</code> belongs to
   */
  public Collection<KBCollection> instancesOf(Context ctx);

  /**
   * Creates a new Fact stating that <code>this</code> object is an instance of
   * the <code>#$Collection</code> represented by <code>col</code> in the
   * context <code>ctx</code>. In other words, this method asserts
   * <code>(#$isa this col)</code>. The new assertion is added regardless of
   * whether or not <code>(#$isa this col)</code> is already provable.
   *
   * @param colStr the String representing the higher order collection
   * <code>this</code> will become an instance of.
   * @param ctxStr	the string representing the context where the fact is
   * asserted
   *
   * @return	<code>this</code>
   *
   * @throws CreateException
   * @throws KBTypeException
   */
  public KBCollection instantiates(String colStr, String ctxStr)
          throws KBTypeException, CreateException;

  /**
   * Creates a new Fact stating that <code>this</code> object is an instance of
   * the <code>#$Collection</code> represented by <code>col</code> in the
   * context <code>ctx</code>. In other words, this method asserts
   * <code>(#$isa this col)</code>. The new assertion is added regardless of
   * whether or not <code>(#$isa this col)</code> is already provable.
   *
   * @param col the collection that <code>this</code> will become an instance
   * of.
   * @param ctx	the context where the fact is asserted
   *
   * @return	<code>this</code>
   *
   * @throws CreateException
   * @throws KBTypeException
   */
  public KBCollection instantiates(KBCollection col, Context ctx)
          throws KBTypeException, CreateException;

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
  public boolean isGeneralizationOf(KBCollection moreSpecific,
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
  public boolean isGeneralizationOf(KBCollection moreSpecific);

  /**
   * This not part of the public, supported KB API
   *
   * @return FormulaSentence
   * @throws KBApiException
   */
  public FormulaSentence toSentence() throws KBApiException;

  /**
   * This not part of the public, supported KB API
   *
   * @return Variable
   * @throws KBApiException
   */
  public Variable getVariable() throws KBApiException;

  /**
   * This not part of the public, supported KB API
   *
   * @param ctx the context
   * @return InstanceRestrictedVariable
   * @throws KBApiException
   */
  public InstanceRestrictedVariable toInstanceRestrictedVariable(Context ctx)
          throws KBApiException;

  /**
   * This not part of the public, supported KB API
   *
   * @return InstanceRestrictedVariable
   * @throws KBApiException
   */
  public InstanceRestrictedVariable toInstanceRestrictedVariable()
          throws KBApiException;

  /**
   * This not part of the public, supported KB API
   *
   * @param var the variable
   * @return InstanceRestrictedVariable
   * @throws KBApiException
   */
  public InstanceRestrictedVariable toInstanceRestrictedVariable(
          Variable var) throws KBApiException;

  /**
   * This not part of the public, supported KB API
   *
   * @param ctx the context
   * @param var the variable
   * @return InstanceRestrictedVariable
   * @throws KBApiException
   */
  public InstanceRestrictedVariable toInstanceRestrictedVariable(
          Context ctx, Variable var) throws KBApiException;

}
