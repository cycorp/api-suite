package com.cyc.kb;

/*
 * #%L
 * File: Context.java
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
import com.cyc.Cyc;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbTypeException;
import com.cyc.kb.spi.ContextService;
import java.util.Collection;

/**
 * The interface for {@link KbIndividual}s that correspond to CycL microtheories.
 * <code>Context</code>s are organized in a multiple-inheritance hierarchy. Facts and rules are
 * asserted in a specific <code>Context</code>, and are true both in the <code>Context</code> in
 * which they are asserted and in any <code>Context</code> that extends it.
 *
 * @author vijay
 */
public interface Context extends KbIndividual {
  
  //====|    Factory methods    |=================================================================//
  
  /**
   * Get the <code>Context</code> with the name <code>nameOrId</code>. This static method wraps a
   * call to {@link ContextService#get(java.lang.String) }; see that method's documentation for more
   * details.
   *
   * @param nameOrId the string representation or the HLID of the #$Microtheory
   *
   * @return a new Context
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  public static Context get(String nameOrId) throws KbTypeException, CreateException {
    return Cyc.getContextService().get(nameOrId);
  }

  /**
   * Find or create a <code>Context</code> object named <code>nameOrId</code>. This static method
   * wraps a call to {@link ContextService#findOrCreate(java.lang.String) }; see that method's
   * documentation for more details.
   *
   * @param nameOrId the string representation or the HLID of the #$Microtheory
   *
   * @return a new Context
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  public static Context findOrCreate(String nameOrId) throws CreateException, KbTypeException {
    return Cyc.getContextService().findOrCreate(nameOrId);
  }

  /**
   * Find or create a <code>Context</code> object named <code>nameOrId</code>, and also make it an
   * instance of <code>constraintCol</code> in the default context specified by
   * {@link DefaultContext#forAssertion()}. This static method wraps a call to
   * {@link ContextService#findOrCreate(java.lang.String, java.lang.String)}; see that method's
   * documentation for more details.
   *
   * @param nameOrId         the string representation or the HLID of the #$Microtheory
   * @param constraintColStr the string representation of the collection that this #$Microtheory
   *                         will instantiate
   *
   * @return a new Context
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  public static Context findOrCreate(String nameOrId, String constraintColStr)
          throws CreateException, KbTypeException {
    return Cyc.getContextService().findOrCreate(nameOrId, constraintColStr);
  }

  /**
   * Find or create a <code>Context</code> object named <code>nameOrId</code>, and also make it an
   * instance of <code>constraintCol</code> in <code>ctx</code>. This static method wraps a call to
   * {@link ContextService#findOrCreate(java.lang.String, java.lang.String, java.lang.String)}; see
   * that method's documentation for more details.
   *
   * @param nameOrId         the string representation or the HLID of the term
   * @param constraintColStr the string representation of the collection that this #$Microtheory
   *                         will instantiate
   * @param ctxStr           the context in which the resulting object must be an instance of
   *                         constraintCol
   *
   * @return a new Context
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  public static Context findOrCreate(String nameOrId, String constraintColStr, String ctxStr)
          throws CreateException, KbTypeException {
    return Cyc.getContextService().findOrCreate(nameOrId, constraintColStr, ctxStr);
  }

  /**
   * Find or create a <code>Context</code> object named <code>nameOrId</code>, and also make it an
   * instance of <code>constraintCol</code> in the default context specified by
   * {@link DefaultContext#forAssertion()}. This static method wraps a call to
   * {@link ContextService#findOrCreate(java.lang.String, com.cyc.kb.KbCollection)}; see that
   * method's documentation for more details.
   *
   * @param nameOrId      the string representation or the HLID of the #$Microtheory
   * @param constraintCol the collection that this #$Microtheory will instantiate
   *
   * @return a new Context
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  public static Context findOrCreate(String nameOrId, KbCollection constraintCol)
          throws CreateException, KbTypeException {
    return Cyc.getContextService().findOrCreate(nameOrId, constraintCol);
  }

  /**
   * Find or create a <code>Context</code> object named <code>nameOrId</code>, and also make it an
   * instance of <code>constraintCol</code> in <code>ctx</code>. This static method wraps a call to
   * {@link ContextService#findOrCreate(java.lang.String, com.cyc.kb.KbCollection, com.cyc.kb.Context)};
   * see that method's documentation for more details.
   *
   * @param nameOrId      the string representation or the HLID of the #$Microtheory
   * @param constraintCol the collection that this #$Microtheory will instantiate
   * @param ctx           the context in which the resulting object must be an instance of
   *                      constraintCol
   *
   * @return a new Context
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  public static Context findOrCreate(String nameOrId, KbCollection constraintCol, Context ctx)
          throws CreateException, KbTypeException {
    return Cyc.getContextService().findOrCreate(nameOrId, constraintCol, ctx);
  }
  
  public static boolean existsAsType(String nameOrId) {
    return Cyc.getContextService().existsAsType(nameOrId);
  }
  
  public static KbStatus getStatus(String nameOrId) {
    return Cyc.getContextService().getStatus(nameOrId);
  }
  
  public static DefaultContext getDefaultContext(Context assertionContext, Context queryContext) {
    return Cyc.getContextService().getDefaultContext(assertionContext, queryContext);
  }
  
  public static DefaultContext getDefaultContext(String assertionCtxStr, String queryCtxStr)
          throws KbTypeException, CreateException {
    return Cyc.getContextService().getDefaultContext(assertionCtxStr, queryCtxStr);
  }
  
  //====|    Interface methods    |===============================================================//
  
  /**
   * Finds the contexts that are asserted to extend this context, in
   * {@link com.cyc.kb.DefaultContext#forQuery()}. Effectively, this returns the asserted bindings
   * for <code>?CONTEXT</code> from <code>(#$genlMt ?CONTEXT this)</code>.
   *
   * @return the contexts that are extensions of this context (i.e. the specializations of this
   * context)
   */
  Collection<Context> getExtensions();

  /* *
   * Creates a new <code>Fact</code> stating that the <code>#$Microtheory</code> represented by
   * moreSpecificStr is a specialization (an extension) of this context. Essentially, this asserts
   * <code>(#$genlMt moreSpecific this)</code>
   * <p>
   * The <code>moreSpecific</code> context has access to all knowledge (assertions) asserted in
   * <code>this</code> context.
   * <p>
   * The new fact is added in #$BaseKB.
   *
   *
   * @param moreSpecificStr the string representing the new extension of this context
   *
   * @return this
   *
   * @throws CreateException
   * @throws KbTypeException
   * /
  Context addExtension(String moreSpecificStr) throws KbTypeException, CreateException;
  */
  
  /**
   * Creates a new <code>Fact</code> stating that the <code>#$Microtheory</code> represented by
   * <code>moreSpecific</code> is a specialization (i.e.&nbsp;an extension) of this context.
   * Essentially, this asserts <code>(#$genlMt moreSpecific this)</code>
   * <p>
   * The <code>moreSpecific</code> context has access to all knowledge (assertions) asserted in
   * <code>this</code> context.
   * <p>
   * The new fact is added in #$BaseKB
   *
   * @param moreSpecific the new extension of this context
   *
   * @return this
   *
   * @throws CreateException
   * @throws KbTypeException
   */
  Context addExtension(Context moreSpecific) throws KbTypeException, CreateException;

  /**
   * Finds the contexts that this context is asserted to inherit from, in
   * {@link com.cyc.kb.DefaultContext#forQuery()}. Effectively, this returns all asserted values for
   * ?CONTEXT from <code>(#$genlMt this ?CONTEXT)</code>
   *
   * @return the contexts that this context inherits from (i.e. those that are generalizations of
   * this context)
   */
  Collection<Context> getInheritsFrom();

  /* *
   * Creates a new Fact stating that this context inherits from <code>moreGeneral</code>.
   * Effectively, this asserts <code>(#$genlMt this moreGeneral)</code>
   * <p>
   * The <code>this</code> context has access to all knowledge (assertions) asserted in
   * <code>moreGeneral</code> context.
   * <p>
   * The new fact is added in #$BaseKB
   *
   * @param moreGeneralStr the string representing the generalized (context) of this context
   *
   * @return this
   *
   * @throws CreateException
   * @throws KbTypeException
   * /
  Context addInheritsFrom(String moreGeneralStr) throws KbTypeException, CreateException;
  */
  
  /**
   * Creates a new Fact stating that this context inherits from <code>moreGeneral</code>.
   * Effectively, this asserts <code>(#$genlMt this moreGeneral)</code>
   * <p>
   * The <code>this</code> context has access to all knowledge (assertions) asserted in
   * <code>moreGeneral</code> context.
   * <p>
   * The new fact is added in #$BaseKB
   *
   * @param moreGeneral the generalized (context) of this context
   *
   * @return this
   *
   * @throws CreateException
   * @throws KbTypeException
   */
  Context addInheritsFrom(Context moreGeneral) throws KbTypeException, CreateException;

  /**
   * Returns the monad of this context.
   *
   * A context monad specifies the "dimension" of the Context. The monad of the Context is the base
   * context without additional context dimensional constraints.
   *
   * The TimeInterval is one of the main context constraints supported.
   *
   * See #$mtMonad for more details
   *
   * @return the monad of this context.
   */
  Context getMonad();
  
}
