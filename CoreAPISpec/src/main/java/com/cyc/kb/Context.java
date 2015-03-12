package com.cyc.kb;

/*
 * #%L
 * File: Context.java
 * Project: Core API Specification
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
import java.util.Collection;

import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KBTypeException;

/**
 * The interface for {@link KBIndividual}s that correspond to CycL
 * microtheories. <code>Context</code>s are organized in a multiple-inheritance
 * hierarchy. Facts and rules are asserted in a specific <code>Context</code>,
 * and are true both in the <code>Context</code> in which they are asserted and
 * in any <code>Context</code> that extends it.
 *
 * @author vijay
 */
public interface Context extends KBIndividual {

  /**
   * Finds the contexts that are asserted to extend this context, in 
   * {@link KBAPIDefaultContext#forQuery()}. Effectively,
   * this returns the asserted bindings for <code>?CONTEXT</code> from
   * <code>(#$genlMt ?CONTEXT this)</code>. 
   *
   * @return the contexts that are extensions of this context (i.e. the
   * specializations of this context)
   */
  public Collection<Context> getExtensions();

  /**
   * Creates a new <code>Fact</code> stating that the <code>#$Microtheory</code>
   * represented by moreSpecificStr is a specialization (an extension) of this
   * context. Essentially, this asserts
   * <code>(#$genlMt moreSpecific this)</code>
   * <p>
   * The <code>moreSpecific</code> context has access to all knowledge
   * (assertions) asserted in <code>this</code> context.
   * <p>
   * The new fact is added in #$BaseKB.
   *
   *
   * @param moreSpecificStr the string representing the new extension of this
   * context
   *
   * @return	this
   *
   * @throws CreateException
   * @throws KBTypeException
   */
  public Context addExtension(String moreSpecificStr)
          throws KBTypeException, CreateException;

  /**
   * Creates a new <code>Fact</code> stating that the <code>#$Microtheory</code>
   * represented by <code>moreSpecific</code> is a specialization (i.e.&nbsp;an
   * extension) of this context. Essentially, this asserts
   * <code>(#$genlMt moreSpecific this)</code>
   * <p>
   * The <code>moreSpecific</code> context has access to all knowledge
   * (assertions) asserted in <code>this</code> context.
   * <p>
   * The new fact is added in #$BaseKB
   *
   * @param moreSpecific	the new extension of this context
   *
   * @return	this
   *
   * @throws CreateException
   * @throws KBTypeException
   */
  public Context addExtension(Context moreSpecific)
          throws KBTypeException, CreateException;

  /**
   * Finds the contexts that this context is asserted to inherit from, in 
   * {@link KBAPIDefaultContext#forQuery()}.
   * Effectively, this returns all asserted values for ?CONTEXT from
   * <code>(#$genlMt this ?CONTEXT)</code>
   *
   * @return	the contexts that this context inherits from (i.e. those that are
   * generalizations of this context)
   */
  public Collection<Context> getInheritsFrom();

  /**
   * Creates a new Fact stating that this context inherits from
   * <code>moreGeneral</code>. Effectively, this asserts
   * <code>(#$genlMt this moreGeneral)</code>
   * <p>
   * The <code>this</code> context has access to all knowledge (assertions)
   * asserted in <code>moreGeneral</code> context.
   * <p>
   * The new fact is added in #$BaseKB
   *
   * @param moreGeneralStr	the string representing the generalized (context) of
   * this context
   *
   * @return	this
   *
   * @throws CreateException
   * @throws KBTypeException
   */
  public Context addInheritsFrom(String moreGeneralStr)
          throws KBTypeException, CreateException;

  /**
   * Creates a new Fact stating that this context inherits from
   * <code>moreGeneral</code>. Effectively, this asserts
   * <code>(#$genlMt this moreGeneral)</code>
   * <p>
   * The <code>this</code> context has access to all knowledge (assertions)
   * asserted in <code>moreGeneral</code> context.
   * <p>
   * The new fact is added in #$BaseKB
   *
   * @param moreGeneral	the generalized (context) of this context
   *
   * @return	this
   *
   * @throws CreateException
   * @throws KBTypeException
   */
  public Context addInheritsFrom(Context moreGeneral)
          throws KBTypeException, CreateException;

  /**
   * Returns the monad of this context.
   *
   * A context monad specifies the "dimension" of the Context. The monad of the
   * Context is the base context without additional context dimensional
   * constraints.
   *
   * The TimeInterval is one of the main context constraints supported.
   *
   * See #$mtMonad for more details
   *
   * @return the monad of this context.
   */
  public Context getMonad();
  
}
