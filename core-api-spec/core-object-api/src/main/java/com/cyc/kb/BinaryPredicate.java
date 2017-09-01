package com.cyc.kb;

import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbTypeException;
import java.util.Collection;

/*
 * #%L
 * File: BinaryPredicate.java
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

/**
 * The interface for {@link KbPredicate}s that take exactly two arguments.
 * @author vijay
 */
public interface BinaryPredicate extends KbPredicate {
  
  /**
   * Adds a new Fact in <code>ctx</code> using ______
   *
   * @param arg1 ______ // TODO: write!
   * @param arg2 ______ // TODO: write!
   * @param ctx the context where the fact is added
   * @return the newly added Fact
   * @throws CreateException
   * @throws KbTypeException
   */
  Fact addFact(Context ctx, Object arg1, Object arg2) throws KbTypeException, CreateException;
  
  /* *
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
   * @param   arg1  the arguments to the predicate. They must be KbObjects or Java objects
   *                such as Date, int, float, String.
   * @return  true if and only if the fact is determined to be true
   * /
  Boolean isAsserted(Context ctx, Object arg1, Object arg2);
  
  /* *
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
   * /
  // KB API does not do any introspection.. so if we want to use Query API, we should construct
  // a fully qualified sentence for the given predicate. That can only be possible when all
  // other variables are passed in.
  // 
  Sentence getSentence(Object arg1, Object arg2) throws KbTypeException, CreateException;
  
  /* *
   * Similar to {@link KbPredicate#getValuesForArgPosition(com.cyc.kb.Context, java.lang.Object...) },
   * but takes an argument for both arg1 and arg2. 
   * 
   * /
  <O> Collection<O> getValuesForArgPosition(Object arg1, Object arg2, Context ctx);
  */
  
  /**
   * Gets all facts for the binary predicate based on arg pattern. It takes an arg1 and an arg2, but
   * expects exactly one of them to be <tt>null</tt>. It then finds all facts for the predicate
   * that have <code>arg1</code> or <code>arg2</code> (the non-null arg) as visible from 
   * <code>ctx</code>, and returns as <code>O</code> objects based on the null argument position of 
   * the facts.
   * 
   * For example, assuming you had KbObjects for the BinaryPredicate <tt>isa</tt> and the individual
   * <tt>Plato</tt>, this:
   * 
   * <code>
   * isa.getValuesForArg(plato, null, inferencePSC);
   * </code>
   * 
   * Would be equivalent to the following query:
   * 
   * <code>
   * (isa Plato ?COL)
   * 
   * in InferencePSC
   * </code>
   * 
   * ... and would return all of the Collections to which Plato belongs, as visible from
   * InferencePSC.
   * 
   * @see KbPredicate#getValuesForArgPosition(java.lang.Object, int, int, com.cyc.kb.Context) 
   * 
   * @param <O>
   * @param arg1
   * @param arg2
   * @param ctx
   * @return 
   * 
   * @see #getValuesForArg(java.lang.Object, java.lang.Object) 
   * @see KbPredicate#getValuesForArgPosition(java.lang.Object, int, int, com.cyc.kb.Context) 
   * @see KbPredicate#getValuesForArgPositionWithMatchArg(java.lang.Object, int, int, java.lang.Object, int, com.cyc.kb.Context) 
   */
  <O> Collection<O> getValuesForArg(Object arg1, Object arg2, Context ctx);
  
  /**
   * Gets all facts for the binary predicate based on arg pattern, in the default context.
   * 
   * @param <O>
   * @param arg1
   * @param arg2
   * @return 
   * 
   * @see     BinaryPredicate#getValuesForArg(java.lang.Object, java.lang.Object, com.cyc.kb.Context) 
   */
  <O> Collection<O> getValuesForArg(Object arg1, Object arg2);
}

