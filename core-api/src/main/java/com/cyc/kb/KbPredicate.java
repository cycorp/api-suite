package com.cyc.kb;

/*
 * #%L
 * File: KbPredicate.java
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

import com.cyc.Cyc;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbException;
import com.cyc.kb.exception.KbTypeException;
import com.cyc.kb.spi.KbPredicateService;
import java.util.Collection;
import java.util.List;

/**
 * The interface for Cyc predicates. <code>KbPredicates</code> are applied to
 * one or more arguments to form a non-atomic {@link Sentence}.
 *
 * @author vijay
 */
public interface KbPredicate extends Relation {
  
  //====|    Factory methods    |=================================================================//
  
  /**
   * Get the <code>KbPredicate</code> with the name <code>nameOrId</code>. This static method wraps
   * a call to {@link KbPredicateService#get(java.lang.String) }; see that method's documentation
   * for more details.
   *
   * @param nameOrId the string representation or the HLID of the #$Predicate
   *
   * @return a new KbPredicate
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  public static KbPredicate get(String nameOrId) throws KbTypeException, CreateException {
    return Cyc.getKbPredicateService().get(nameOrId);
  }
  
  public static KbPredicate findOrCreate(String nameOrId) throws CreateException, KbTypeException {
    return Cyc.getKbPredicateService().findOrCreate(nameOrId);
  }
  
  public static KbPredicate findOrCreate(String nameOrId, String constraintColStr)
          throws CreateException, KbTypeException {
    return Cyc.getKbPredicateService().findOrCreate(nameOrId, constraintColStr);
  }
  
  public static KbPredicate findOrCreate(String nameOrId, String constraintColStr, String ctxStr)
          throws CreateException, KbTypeException {
    return Cyc.getKbPredicateService().findOrCreate(nameOrId, constraintColStr, ctxStr);
  }
  
  public static KbPredicate findOrCreate(String nameOrId, KbCollection constraintCol)
          throws CreateException, KbTypeException {
    return Cyc.getKbPredicateService().findOrCreate(nameOrId, constraintCol);
  }

  public static KbPredicate findOrCreate(String nameOrId, KbCollection constraintCol, Context ctx)
          throws CreateException, KbTypeException {
    return Cyc.getKbPredicateService().findOrCreate(nameOrId, constraintCol, ctx);
  }
  
  public static boolean existsAsType(String nameOrId) {
    return Cyc.getKbPredicateService().existsAsType(nameOrId);
  }
  
  public static KbStatus getStatus(String nameOrId) {
    return Cyc.getKbPredicateService().getStatus(nameOrId);
  }
  
  //====|    Interface methods    |===============================================================//
  
  /**
   * finds the asserted specializations of <code>this</code>
   * <code>Predicate</code>, from the default context specified by
   * {@link com.cyc.kb.DefaultContext#forQuery()}.
   *
   * @return the <code>KbPredicate</code>s that are specialization of
   * <code>this</code> <code>KbPredicate</code>
   */
  public Collection<KbPredicate> getSpecializations();

  /* *
   * finds the asserted specializations of <code>this</code>
   * <code>KbPredicate</code>, from the context <code>ctx</code>.
   *
   * @param ctxStr the string representing the context of query
   *
   * @return the <code>KbPredicate</code>s that are specializations of
   * <code>this</code> <code>KbPredicate</code>
   * /
  public Collection<KbPredicate> getSpecializations(String ctxStr);
  */
  
  /**
   * finds the asserted specializations of <code>this</code>
   * <code>KbPredicate</code>, from the context <code>ctx</code>
   *
   * @param ctx the context of query
   *
   * @return the <code>KbPredicate</code>s that are specializations of
   * <code>this</code> <code>KbPredicate</code>
   */
  public Collection<KbPredicate> getSpecializations(Context ctx);

  /* *
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
   * /
  public KbPredicate addSpecialization(String moreSpecificStr, String ctxStr)
          throws KbTypeException, CreateException;
  */
  
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
  public KbPredicate addSpecialization(KbPredicate moreSpecific, Context ctx) 
          throws KbTypeException, CreateException;

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
  
  /* *
   * finds the asserted generalizations of <code>this</code>, from a context
   * <code>ctx</code>.
   *
   * @param ctxStr the string representing the context
   *
   * @return the <code>KbPredicate</code>s that are generalizations of this
   * <code>KbPredicate</code>
   * /
  public Collection<KbPredicate> getGeneralizations(String ctxStr);
  */
  
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

  /* *
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
   * /
  public KbPredicate addGeneralization(String moreGeneralStr, String ctxStr)
          throws KbTypeException, CreateException;
  */
  
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
  public KbPredicate addGeneralization(KbPredicate moreGeneral, Context ctx) 
          throws KbTypeException, CreateException;

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
  public boolean isGeneralizationOf(KbPredicate moreSpecific, Context ctx);

  public List<Fact> getExtent();
  
  public List<Fact> getExtent(Context ctx);
  
  /**
   * Finds or creates a new Fact in the underlying KB
   * 
   * <p>The method finds a Fact in the KB with predicate <code>pred</code>, and
   * <code>this</code> at the argument position <code>thisArgPos</code>, in the
   * context <code>ctx</code>. The otherArgs specify all the arguments of the
   * Fact, completely. If the fact specified by the input arguments is not
   * found, creates and persists a new fact in the underlying KB.
   *
   * <p>Note: Not all subclasses of the KB objects are directly assertible. For
   * example, Sentence, Variable and Symbol need to be quoted, using Quote
   * method, before they can participate in closed assertions.
   *
   * <p>Note: The context is the first argument in methods that have variable
   * number of arguments. In all other methods, context is the last argument.
   *
   * @param   ctx   the context of the fact
   * @param   args  the arguments to the predicate. They must be KbObjects or Java objects
   *                such as Date, int, float, String.
   * @return  the fact found or created
   * @throws  KbTypeException
   * @throws  CreateException
   */
  Fact addFact(Context ctx, Object... args) throws KbTypeException, CreateException;
    
  /**
   * find the specific existing Fact, in <code>ctx</code>, that has
   * <code>pred</code> as its predicate, <code>this</code> in the
   * <code>thisArgPos</code> argument position, and <code>otherArgs</code> as
   * its other arguments. Effectively, <code>this</code> is inserted into
   * position <code>thisArgPos</code> of <code>otherArgs</code> to form the
   * arguments for a Fact that is searched for in the KB. If an assertion using
   * that sentence is found in the context, the Fact is returned. If no such
   * assertion is found, appropriate exception is thrown.
   *
   * Note: The context is the first argument in methods that have variable
   * number of arguments. In all other methods, context is the last argument.
   *
   * @param   ctx   the context of the fact
   * @param   args  the arguments to the predicate. They must be KbObjects or Java objects
   *                such as Date, int, float, String.
   * @return  the fact represented by the parameters in context <code>ctx</code>
   * @throws  CreateException
   * @throws  KbTypeException
   */
  Fact getFact(Context ctx, Object... args) 
          throws KbTypeException, CreateException;
  
  /**
   * Gets the asserted Facts visible from <code>ctx</code>, using the predicate
   * <code>predicate</code> with <code>this</code> at position <code>thisArgPos</code>
   * of the Fact
   *
   * @param   arg   the Predicate of the returned fact
   * @param   argPosition  the position where <code>this</code> is found in the fact
   * @param   ctx         the Context. If null, returns facts from the default context
   *                      {@link com.cyc.kb.DefaultContext#forQuery()}
   * @return  a collection of facts, empty if none are found
   */
  Collection<Fact> getFacts(Object arg, int argPosition, Context ctx);
  
  /* *
   * Similar to {@link #getFacts(java.lang.Object, int, com.cyc.kb.Context) }, but _______
   *
   * @param   arg   the Predicate of the returned fact
   * @param   argPosition  the position where <code>this</code> is found in the fact
   * @param   ctx         the Context. If null, returns facts from the default context
   *                      {@link com.cyc.kb.DefaultContext#forQuery()}
   * @return  a collection of facts, empty if none are found
   * /
  Collection<Fact> getFacts(Context ctx, Object... args);
  */
  
  /**
   * Is there an assertion in <code>ctx</code> with a sentence using
   * <code>pred</code>, with <code>this</code> in the <code>thisArgPos</code>
   * argument position, and with the arguments in <code>otherArgs</code> as the
   * rest of its arguments? This method will not throw exceptions. Effectively,
   * this is a wrapper around 
   * {@link #getFact(com.cyc.kb.Context, com.cyc.kb.KbPredicate, int, java.lang.Object...) }
   * that returns false if there are any exceptions.
   *
   * <p>Note: The context is the first argument in methods that have variable
   * number of arguments. In all other methods, context is the last argument.
   *
   * @param   ctx         the context in which the sentence is asserted
   * @param   args  the arguments to the predicate. They must be KbObjects or Java objects
   *                such as Date, int, float, String.
   * @return  true if and only if the fact is determined to be true
   */
  Boolean isAsserted(Context ctx, Object... args);
  
  /**
   * Constructs a Sentence, that has <code>pred</code> as its predicate,
   * <code>this</code> in the <code>thisArgPos</code> argument position, and
   * <code>otherArgs</code> as its other arguments. Effectively,
   * <code>this</code> is inserted into position <code>thisArgPos</code> of
   * <code>otherArgs</code> to form the sentence.
   *
   * @param   args  the arguments to the predicate. They must be KbObjects or Java objects
   *                such as Date, int, float, String.
   * @return  the sentence constructed by the parameters
   * @throws  KbTypeException
   */
  // KB API does not do any introspection.. so if we want to use Query API, we should construct
  // a fully qualified sentence for the given predicate. That can only be possible when all
  // other variables are passed in.
  // 
  Sentence getSentence(Object... args) throws KbTypeException, CreateException;
  
  /**
   * This method gets all facts for the predicate that have <code>arg</code> at the <code>thisArgPos</code> arg position of the fact,
   * as visible from <code>ctx</code>, and returns as <code>O</code> objects
   * based on the arguments in the <code>getArgPos</code> argument position of
   * the facts.
   * 
   * @param   <O>         the type of the objects returned
   * @param   arg        the predicate of the facts
   * @param   argPosition  the argument position of this object in the candidate facts
   * @param   valuePosition   the argument position of the returned objects in the candidate facts
   * @param   ctx         the context where the facts are found. If null, returns facts from the
   *                      default context {@link com.cyc.kb.DefaultContext#forQuery()}
   * @return  a collection of objects of type O
   * @see     #getFacts(KbPredicate, int, Context) for a method that returns the facts, rather than
   *          just the objects at a specific argument position of the facts
   * @see     #getValuesForArgPositionWithMatchArg(java.lang.Object, int, int, java.lang.Object, int, com.cyc.kb.Context) 
   * @see     BinaryPredicate#getValuesForArg(java.lang.Object, java.lang.Object, com.cyc.kb.Context) 
   */
  <O> Collection<O> getValuesForArgPosition(
          Object arg, int argPosition, int valuePosition, Context ctx);
  
  /**
   * This method gets all facts visible from <code>ctx</code> that use the predicate,
   * have <code>arg</code> in the <code>argPosition</code>, and also have <code>matchArg</code> in 
   * the <code>matchArgPos</code> arg position; it returns a list of <code>O</code>
   * objects from the <code>valuePosition</code> argument position of the fact.
   *
   * @param   <O>          the type of the objects returned
   * @param   arg         the predicate of the facts
   * @param   argPosition   the argument position of <code>this</code> in the candidate facts
   * @param   valuePosition    the argument position of the returned objects in the candidate facts
   * @param   matchArg     the object in the argument position matchArgPos
   * @param   matchArgPos  the argument position that must be filled with matchArg
   * @param   ctx          the context. If null, returns facts from the default context
   *                       {@link com.cyc.kb.DefaultContext#forQuery()}
   * @return  a collection of objects of type O
   * @see     #getValuesForArgPosition(java.lang.Object, int, int, com.cyc.kb.Context) 
   * @see     BinaryPredicate#getValuesForArg(java.lang.Object, java.lang.Object, com.cyc.kb.Context) 
   */
  <O> Collection<O> getValuesForArgPositionWithMatchArg(
          Object arg, int argPosition, int valuePosition, 
          Object matchArg, int matchArgPos, Context ctx);
  
  /* *
   * Similar to {@link #getValuesForArgPosition(java.lang.Object, int, int, com.cyc.kb.Context) }, but ______
   * 
   * @param   <O>         the type of the objects returned
   * @param   arg        the predicate of the facts
   * @param   argPosition  the argument position of this object in the candidate facts
   * @param   valuePosition   the argument position of the returned objects in the candidate facts
   * @param   ctx         the context where the facts are found. If null, returns facts from the
   *                      default context {@link com.cyc.kb.DefaultContext#forQuery()}
   * @return  a collection of objects of type O
   * @see     #getFacts(KbPredicate, int, Context) for a method that returns the facts, rather than
   *          just the objects at a specific argument position of the facts
   * /
  <O> Collection<O> getValuesForArgPosition(Context ctx, Object... args);
  */
  
}
