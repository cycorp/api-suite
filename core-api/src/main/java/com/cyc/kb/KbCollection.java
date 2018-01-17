package com.cyc.kb;

/*
 * #%L
 * File: KbCollection.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2017 Cycorp, Inc
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
import com.cyc.Cyc;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbTypeException;
import com.cyc.kb.spi.KbCollectionService;
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
  
  //====|    Factory methods    |=================================================================//
  
  /**
   * Get the <code>KbCollection</code> with the name <code>nameOrId</code>. This static method wraps
   * a call to {@link KbCollectionService#get(java.lang.String) }; see that method's documentation
   * for more details.
   *
   * @param nameOrId the string representation or the HLID of the #$Collection
   *
   * @return a new KbCollection
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  public static KbCollection get(String nameOrId) throws KbTypeException, CreateException {
    return Cyc.getKbCollectionService().get(nameOrId);
  }

  /**
   * Find or create a <code>KbCollection</code> object named <code>nameOrId</code>. This static
   * method wraps a call to {@link KbCollectionService#findOrCreate(java.lang.String) }; see that
   * method's documentation for more details.
   *
   * @param nameOrId the string representation or the HLID of the #$Collection
   *
   * @return a new KbCollection
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  public static KbCollection findOrCreate(String nameOrId) throws CreateException, KbTypeException {
    return Cyc.getKbCollectionService().findOrCreate(nameOrId);
  }

  /**
   * Find or create a <code>KbCollection</code> object named <code>nameOrId</code>, and also make it
   * an instance of <code>constraintCol</code> in the default context specified by
   * {@link DefaultContext#forAssertion()}. This static method wraps a call to
   * {@link KbCollectionService#findOrCreate(java.lang.String, java.lang.String)}; see that method's
   * documentation for more details.
   *
   * @param nameOrId         the string representation or the HLID of the #$Collection
   * @param constraintColStr the string representation of the collection that this #$Collection will
   *                         instantiate
   *
   * @return a new KbCollection
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  public static KbCollection findOrCreate(String nameOrId, String constraintColStr)
          throws CreateException, KbTypeException {
    return Cyc.getKbCollectionService().findOrCreate(nameOrId, constraintColStr);
  }

  /**
   * Find or create a <code>KbCollection</code> object named <code>nameOrId</code>, and also make it
   * an instance of <code>constraintCol</code> in <code>ctx</code>. This static method wraps a call
   * to
   * {@link KbCollectionService#findOrCreate(java.lang.String, java.lang.String, java.lang.String)};
   * see that method's documentation for more details.
   *
   * @param nameOrId         the string representation or the HLID of the term
   * @param constraintColStr the string representation of the collection that this #$Collection will
   *                         instantiate
   * @param ctxStr           the context in which the resulting object must be an instance of
   *                         constraintCol
   *
   * @return a new KbCollection
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  public static KbCollection findOrCreate(String nameOrId, String constraintColStr, String ctxStr)
          throws CreateException, KbTypeException {
    return Cyc.getKbCollectionService().findOrCreate(nameOrId, constraintColStr, ctxStr);
  }

  /**
   * Find or create a <code>KbCollection</code> object named <code>nameOrId</code>, and also make it
   * an instance of <code>constraintCol</code> in the default context specified by
   * {@link DefaultContext#forAssertion()}. This static method wraps a call to
   * {@link KbCollectionService#findOrCreate(java.lang.String, com.cyc.kb.KbCollection)}; see that
   * method's documentation for more details.
   *
   * @param nameOrId      the string representation or the HLID of the #$Collection
   * @param constraintCol the collection that this #$Collection will instantiate
   *
   * @return a new KbCollection
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  public static KbCollection findOrCreate(String nameOrId, KbCollection constraintCol)
          throws CreateException, KbTypeException {
    return Cyc.getKbCollectionService().findOrCreate(nameOrId, constraintCol);
  }

  /**
   * Find or create a <code>KbCollection</code> object named <code>nameOrId</code>, and also make it
   * an instance of <code>constraintCol</code> in <code>ctx</code>. This static method wraps a call
   * to
   * {@link KbCollectionService#findOrCreate(java.lang.String, com.cyc.kb.KbCollection, com.cyc.kb.Context)};
   * see that method's documentation for more details.
   *
   * @param nameOrId      the string representation or the HLID of the #$Collection
   * @param constraintCol the collection that this #$Collection will instantiate
   * @param ctx           the context in which the resulting object must be an instance of
   *                      constraintCol
   *
   * @return a new KbCollection
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  public static KbCollection findOrCreate(String nameOrId, KbCollection constraintCol, Context ctx)
          throws CreateException, KbTypeException {
    return Cyc.getKbCollectionService().findOrCreate(nameOrId, constraintCol, ctx);
  }
  
  public static boolean existsAsType(String nameOrId) {
    return Cyc.getKbCollectionService().existsAsType(nameOrId);
  }
  
  public static KbStatus getStatus(String nameOrId) {
    return Cyc.getKbCollectionService().getStatus(nameOrId);
  }
  
  //====|    Interface methods    |===============================================================//
  
  /**
   * Finds all the specializations of this <code>KbCollection</code>, including the entire downward
   * transitive closure, acontextually (ignoring Microtheories.)
   *
   * @return all <code>KbCollection</code>s that are specializations of this
   *         <code>KbCollection</code>
   *
   */
  Collection<KbCollection> allSpecializations();

  /* *
   * Finds all the specializations of this <code>KbCollection</code> in the context including the
   * entire downward transitive closure.
   *
   * @param ctxStr the string representing the context of query
   *
   * @return all <code>KbCollection</code>s that are specializations of this <code>KbCollection</code>
   * /
  Collection<KbCollection> allSpecializations(String ctxStr);
   */
  
  /**
   * Finds all the specializations of this <code>KbCollection</code> in the context including the
   * entire downward transitive closure
   *
   * @param ctx the context of query
   *
   * @return all <code>KbCollections</code> that are specializations of this
   *         <code>KbCollection</code>
   */
  Collection<KbCollection> allSpecializations(Context ctx);

  /**
   * Finds the asserted specializations of this <code>KbCollection</code>, from the default context
   * specified by {@link com.cyc.kb.DefaultContext#forQuery()}.
   *
   * @return the <code>KbCollection</code>s that are specializations of this
   *         <code>KbCollection</code>.
   */
  // @todo Consider adding max-specs as another method.
  Collection<KbCollection> getSpecializations();

  /* *
   * Finds the asserted specializations of this <code>KbCollection</code>, from the context <code>ctx</code>.
   *
   * @param ctxStr the string representing the context of query
   *
   * @return the <code>KbCollection</code>s that are specializations of this <code>KbCollection</code>
   * /
  Collection<KbCollection> getSpecializations(String ctxStr);
   */
  
  /**
   * Finds the asserted specializations of this <code>KbCollection</code>, from a context
   * <code>ctx</code>.
   *
   * @param ctx the context of query
   *
   * @return the <code>KbCollection</code>s that are specializations of this
   *         <code>KbCollection</code>
   */
  Collection<KbCollection> getSpecializations(Context ctx);

  /* *
   * Creates a new Fact stating that the <code>#$Collection</code> represented
   * by <code>moreSpecificStr</code> is a specialization of this <code>KbCollection</code> in
   * the default assertion context. The new assertion is added regardless of
   * whether or not <code>(#$genls moreSpecific this)</code> is already
   * provable.
   * <p>
   *
   * @param moreSpecificStr the string representing the specialization
   * (collection) of this <code>KbCollection</code>
   *
   * @return this
   *
   * @throws CreateException
   * @throws KbTypeException
   * /
  KbCollection addSpecialization(String moreSpecificStr)
          throws KbTypeException, CreateException;
   */
  
 /* *
   * Creates a new Fact stating that the <code>#$Collection</code> represented
   * by <code>moreSpecificStr</code> is a specialization of this <code>KbCollection</code> in
   * the context. The new assertion is added regardless of whether or not
   * <code>(#$genls moreSpecific this)</code> is already provable.
   * <p>
   *
   * @param moreSpecificStr the string representing the specialization
   * (collection) of this <code>KbCollection</code>
   * @param ctxStr the string representing the context where the fact is
   * asserted
   *
   * @return this
   *
   * @throws CreateException
   * @throws KbTypeException
   * /
  KbCollection addSpecialization(String moreSpecificStr, String ctxStr) 
          throws KbTypeException, CreateException;
   */
  
  /**
   * Creates a new Fact stating that <code>moreSpecific</code> is a specialization of
   * this <code>KbCollection</code> in the default assertion context. The new assertion is added regardless of
   * whether or not <code>(#$genls moreSpecific this)</code> is already provable.
   * <p>
   *
   * @param moreSpecific the specialization (collection) of this <code>KbCollection</code>
   *
   * @return <code>this</code>
   *
   * @throws CreateException
   * @throws KbTypeException
   */
  KbCollection addSpecialization(KbCollection moreSpecific) throws KbTypeException, CreateException;

  /**
   * Creates a new Fact stating that <code>moreSpecific</code> is a specialization of
   * this <code>KbCollection</code> in the Context ctx. The new assertion is added regardless of whether or not
   * <code>(#$genls moreSpecific this)</code> is already provable.
   * <p>
   *
   * @param moreSpecific the specialization (collection) of this <code>KbCollection</code>
   * @param ctx          the context where the fact is asserted
   *
   * @return <code>this</code>
   *
   * @throws CreateException
   * @throws KbTypeException
   */
  KbCollection addSpecialization(KbCollection moreSpecific, Context ctx)
          throws KbTypeException, CreateException;

  /*
   * genls methods
   */
  
  /**
   * Finds all the generalizations of this <code>KbCollection</code>, including the entire upward
   * transitive closure, in any microtheory.
   *
   * @return all the <code>KbCollection</code>s which are generalizations of this
   *         <code>KbCollection</code>
   */
  Collection<KbCollection> allGeneralizations();

  /* *
   * Finds all the generalizations of this <code>KbCollection</code> in the
   * context <code>ctx</code>, including the entire upward transitive closure.
   *
   * @param ctxStr the string representing the context of the query
   *
   * @return all the <code>KbCollection</code>s which are generalizations of this <code>KbCollection</code>
   * /
  Collection<KbCollection> allGeneralizations(String ctxStr);
   */
  
  /**
   * Finds all the generalizations of this <code>KbCollection</code> in the context including the
   * entire upward transitive closure.
   *
   * @param ctx the context of the query
   *
   * @return all the <code>KbCollection</code>s which are generalizations of this
   *         <code>KbCollection</code>
   */
  Collection<KbCollection> allGeneralizations(Context ctx);

  /**
   * Finds the asserted generalizations of this <code>KbCollection</code>, from the default context
   * specified by {@link com.cyc.kb.DefaultContext#forQuery()}.
   *
   * @return the <code>KbCollection</code>s that are generalizations of this
   *         <code>KbCollection</code>
   */
  Collection<? extends KbCollection> getGeneralizations();

  /* *
   * Finds the asserted generalizations of this <code>KbCollection</code>, from a context specified by <code>ctx</code>.
   *
   * @param ctxStr the string representing the context
   *
   * @return the <code>KbCollection</code>s that are generalizations of this <code>KbCollection</code>
   * /
  Collection<? extends KbCollection> getGeneralizations(String ctxStr);
   */
  
  /**
   * Finds the asserted generalizations of this <code>KbCollection</code>, from a context specified
   * by <code>ctx</code>.
   *
   * @param ctx the context
   *
   * @return the <code>KbCollection</code>s that are generalizations of * * this
   *         <code>KbCollection</code>
   */
  Collection<? extends KbCollection> getGeneralizations(Context ctx);

  /* *
   * Creates a new <code>Fact</code> stating that the #$Collection represented
   * by <code>moreGeneralStr</code> is a generalization of this <code>KbCollection</code> in
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
   * /
  KbCollection addGeneralization(String moreGeneralStr) throws KbTypeException, CreateException;
   */
  
 /* *
   * Creates a new <code>Fact</code> stating that the #$Collection represented
   * by <code>moreGeneralStr</code> is a generalization of this <code>KbCollection</code> in
   * the context. The new assertion is added regardless of whether or not
   * <code>(#$genls this moreGeneral)</code> is already provable.
   *
   * @param moreGeneralStr the string representing the generalization of
   * this @param ctxStr the string representing the context where the fact is
   * asserted
   *
   * @return <code>this</code>
   *
   * @throws CreateException
   * @throws KbTypeException
   * /
  KbCollection addGeneralization(String moreGeneralStr, String ctxStr) 
          throws KbTypeException, CreateException;
   */
  
  /**
   * Creates a new <code>Fact</code> stating that the <code>moreGeneral</code> is a generalization
   * of this <code>KbCollection</code> in the default assertion context. The new assertion is added regardless of
   * whether or not <code>(#$genls this moreGeneral)</code> is already provable.
   *
   * @param moreGeneral the generalization of <code>this</code>
   *
   * @return <code>this</code>
   *
   * @throws CreateException
   * @throws KbTypeException
   */
  KbCollection addGeneralization(KbCollection moreGeneral) throws KbTypeException, CreateException;

  /**
   * Creates a new <code>Fact</code> stating that the <code>moreGeneral</code> is a generalization
   * of this <code>KbCollection</code> in the context. The new assertion is added regardless of whether or not
   * <code>(#$genls this moreGeneral)</code> is already provable.
   *
   * @param moreGeneral the generalization of this @param ctx the context where the fact is asserted
   *
   * @return <code>this</code>
   *
   * @throws CreateException
   * @throws KbTypeException
   */
  KbCollection addGeneralization(KbCollection moreGeneral, Context ctx)
          throws KbTypeException, CreateException;

  /**
   * This method returns the Sentence <code>(#$genls this moreGeneral)</code>. The key difference
   * between this and {@link #addGeneralization(com.cyc.kb.KbCollection) } is that, this method does
   * not make any assertion in the KB. The sentence form of the assertion is generally useful when
   * seeking user feedback before asserting into the KB. Use {@link Sentence#assertIn(com.cyc.kb.Context)
   * } to assert the sentence in a Context.
   *
   * @param moreGeneral the generalization of this
   *
   * @return the #$genls sentence between this and moreGeneral
   *
   * @throws KbTypeException
   */
  Sentence addGeneralizationSentence(KbCollection moreGeneral)
          throws KbTypeException, CreateException;

  /**
   * Finds the asserted instances of this <code>KbCollection</code> from the default
   * context specified by {@link com.cyc.kb.DefaultContext#forQuery()}. For
   * <code>KbCollection</code>s that are instances of <code>#$FirstOrderCollection</code>, this can
   * only return Individuals. For other collections, it can only return <code>KbCollection</code>s
   * or its subclasses.
   *
   * @param <O> the class the instances are cast to. Typically <code>Individual</code> or
   *            <code>KbCollection</code>.
   *
   * @return collection of objects of Type <code>O</code>
   */
  <O> Collection<? extends O> getInstances();

  /* *
   * Finds the asserted instances of this <code>KbCollection</code>
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
   * /
  <O> Collection<O> getInstances(String ctxStr);
   */
  
  /**
   * Finds the asserted instances of this <code>KbCollection</code> from a particular
   * context. For <code>KbCollection</code>s that are instances of
   * <code>#$FirstOrderCollection</code>, this can only return <code>Individual</code>s. For other
   * collections, it can only return <code>KbCollection</code>s or its subclasses.
   * <p>
   *
   * @param ctx the context of query
   * @param <O> the class the instances are cast to Typically <code>Individual</code> or
   *            <code>KbCollection</code>.
   *
   * @return objects of <code>O</code>
   */
  <O> Collection<O> getInstances(Context ctx);

  /**
   * Finds the asserted types this <code>KbCollection</code> belongs to, from the
   * default context specified by {@link com.cyc.kb.DefaultContext#forQuery()}. In other words, this
   * returns the asserted values of <code>?X</code> in <code>(#$isa this ?X)</code>
   *
   * @return Collections (Second Order or higher) this <code>KbCollection</code> belongs to
   */
  Collection<KbCollection> instancesOf();

  /* *
   * Finds the asserted types this <code>KbCollection</code>
   * belongs to, from a context <code>ctx</code>. In other words, this returns
   * the asserted values of <code>?X</code> in <code>(#$isa this ?X)</code>
   *
   * @param ctxStr the string representing the context
   *
   * @return Collections (Second Order or higher) this <code>KbCollection</code> belongs to
   * /
  Collection<KbCollection> instancesOf(String ctxStr);
   */
  
  /**
   * Finds the asserted types this <code>KbCollection</code> belongs to, from a context
   * <code>ctx</code>. In other words, this returns the asserted values of <code>?X</code> in
   * <code>(#$isa this ?X)</code>
   *
   * @param ctx the context
   *
   * @return Collections (Second Order or higher) * * this <code>KbCollection</code> belongs to
   */
  Collection<KbCollection> instancesOf(Context ctx);

  /* *
   * Creates a new Fact stating that this <code>KbCollection</code> object is an instance of
   * the <code>#$Collection</code> represented by <code>col</code> in the
   * context <code>ctx</code>. In other words, this method asserts
   * <code>(#$isa this col)</code>. The new assertion is added regardless of
   * whether or not <code>(#$isa this col)</code> is already provable.
   *
   * @param colStr the String representing the higher order collection this will become an instance of.
   * @param ctxStr the string representing the context where the fact is
   * asserted
   *
   * @return <code>this</code>
   *
   * @throws CreateException
   * @throws KbTypeException
   * /
  @Override
  KbCollection instantiates(String colStr, String ctxStr) throws KbTypeException, CreateException;
   */
  
  /**
   * Creates a new Fact stating that this <code>KbCollection</code> object is an instance of the
   * <code>#$Collection</code> represented by <code>col</code> in the context <code>ctx</code>. In
   * other words, this method asserts <code>(#$isa this col)</code>. The new assertion is added
   * regardless of whether or not <code>(#$isa this col)</code> is already provable.
   *
   * @param col the collection that this <code>KbCollection</code> will become an instance of.
   * @param ctx the context where the fact is asserted
   *
   * @return <code>this</code>
   *
   * @throws CreateException
   * @throws KbTypeException
   */
  @Override
  KbCollection instantiates(KbCollection col, Context ctx) throws KbTypeException, CreateException;

  /**
   * Determine whether this <code>KbCollection</code> is a generalization of <code>moreSpecific</code> in some
   * context. Essentially, this amounts to determining whether
   * <code>(#$genls moreSpecific this)</code> is true. Note that
   * <code>(#$genls moreSpecific this)</code> need not be asserted to be true.
   *
   * @param moreSpecific the possibly more specific collection
   * @param ctx          the context where the fact is queried
   *
   * @return true if (#$genls moreSpecific this) is true
   */
  boolean isGeneralizationOf(KbCollection moreSpecific, Context ctx);

  /* *
   * Determine whether this <code>KbCollection</code> is a generalization of
   * <code>moreSpecific</code> in some context. Essentially, this amounts to
   * determining whether <code>(#$genls moreSpecific this)</code> is true. Note
   * that <code>(#$genls moreSpecific this)</code> need not be asserted to be
   * true.
   *
   * @param moreSpecificStr the string representing the possibly more specific
   * collection
   *
   * @return true if (#$genls moreSpecific this) is true
   * /
  boolean isGeneralizationOf(String moreSpecificStr);
   */
  
  /**
   * Determine whether this <code>KbCollection</code> is a generalization of <code>moreSpecific</code> in a
   * context. Essentially, this amounts to determining whether
   * <code>(#$genls moreSpecific this)</code> is true. Note that
   * <code>(#$genls moreSpecific this)</code> need not be asserted to be true.
   *
   * @param moreSpecific the possibly more specific collection
   *
   * @return true if (#$genls moreSpecific this) is true
   */
  boolean isGeneralizationOf(KbCollection moreSpecific);

}
