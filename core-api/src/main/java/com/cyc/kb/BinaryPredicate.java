package com.cyc.kb;

import com.cyc.Cyc;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbTypeException;
import com.cyc.kb.spi.BinaryPredicateService;
import java.util.Collection;


/*
 * #%L
 * File: BinaryPredicate.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2019 Cycorp, Inc
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
  
  //====|    Factory methods    |=================================================================//
  
  /**
   * Get the <code>BinaryPredicate</code> with the name <code>nameOrId</code>. This static method
   * wraps a call to {@link BinaryPredicateService#get(java.lang.String) }; see that method's
   * documentation for more details.
   *
   * @param nameOrId the string representation or the HLID of the #$BinaryPredicate
   *
   * @return a new BinaryPredicate
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  public static BinaryPredicate get(String nameOrId) throws KbTypeException, CreateException {
    return Cyc.getBinaryPredicateService().get(nameOrId);
  }
  
  public static BinaryPredicate findOrCreate(String nameOrId) throws CreateException, KbTypeException {
    return Cyc.getBinaryPredicateService().findOrCreate(nameOrId);
  }
  
  public static BinaryPredicate findOrCreate(String nameOrId, String constraintColStr)
          throws CreateException, KbTypeException {
    return Cyc.getBinaryPredicateService().findOrCreate(nameOrId, constraintColStr);
  }
  
  public static BinaryPredicate findOrCreate(String nameOrId, String constraintColStr, String ctxStr)
          throws CreateException, KbTypeException {
    return Cyc.getBinaryPredicateService().findOrCreate(nameOrId, constraintColStr, ctxStr);
  }
  
  public static BinaryPredicate findOrCreate(String nameOrId, KbCollection constraintCol)
          throws CreateException, KbTypeException {
    return Cyc.getBinaryPredicateService().findOrCreate(nameOrId, constraintCol);
  }

  public static BinaryPredicate findOrCreate(String nameOrId, KbCollection constraintCol, Context ctx)
          throws CreateException, KbTypeException {
    return Cyc.getBinaryPredicateService().findOrCreate(nameOrId, constraintCol, ctx);
  }
  
  public static boolean existsAsType(String nameOrId) {
    return Cyc.getBinaryPredicateService().existsAsType(nameOrId);
  }
  
  public static KbStatus getStatus(String nameOrId) {
    return Cyc.getBinaryPredicateService().getStatus(nameOrId);
  }
  
  //====|    Interface methods    |===============================================================//
  
  /**
   * Finds or creates a new Fact in the underlying KB
   *
   * @param arg1 the first argument to the predicate. It must be a KbObject or a Java object such as
   *             Date, int, float, String
   * @param arg2 the first argument to the predicate. It must be a KbObject or a Java object such as
   *             Date, int, float, String
   * @param ctx  the context of the fact
   *
   * @return the fact found or created
   *
   * @throws CreateException
   * @throws KbTypeException
   */
  Fact addFact(Context ctx, Object arg1, Object arg2) throws KbTypeException, CreateException;

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
   * Gets all facts for the binary predicate based on arg pattern, in the default context. For more
   * details, see {@link #getValuesForArg(java.lang.Object, java.lang.Object, com.cyc.kb.Context)}.
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

