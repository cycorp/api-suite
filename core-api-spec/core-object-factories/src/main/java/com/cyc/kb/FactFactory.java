package com.cyc.kb;

/*
 * #%L
 * File: FactFactory.java
 * Project: Core API Object Factories
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

import com.cyc.core.service.CoreServicesLoader;
import com.cyc.kb.Assertion.Direction;
import com.cyc.kb.Assertion.Strength;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.InvalidFormulaInContextException;
import com.cyc.kb.exception.KbObjectNotFoundException;
import com.cyc.kb.exception.KbTypeException;
import com.cyc.kb.spi.FactService;

/**
 *
 * @author nwinant
 */
public class FactFactory {
  
  // Static
  
  private static final FactFactory ME = new FactFactory();

  protected static FactFactory getInstance() {
    return ME;
  }
  
    
  // Fields
    
  private final FactService service;

  
  // Construction
  
  private FactFactory() {
    service = CoreServicesLoader.getKbFactoryServices().getFactService();
  }

  protected FactService getService() {
    return service;
  }
  
  
  // Public
  
  /**
   * Get the <code>Fact</code> object that corresponds to <code>hlid</code>. Throws exceptions if
   * the <code>hlid</code> isn't in the KB, or if it's not already an assertion and a GAF.
   *
   * NOTE: The get (String) factory methods in other classes find objects based on <code>hlid</code>
   * or string representation of the object. They use nameOrId as the string variable name. But this
   * method finds objects based on <code>hlid</code> alone. This is because it is much more common
   * and easier to find assertions based on two strings, formula and context.
   *
   * @param hlid the <code>hlid</code> corresponding to the Fact in the KB
   *
   * @return a Fact based on <code>hlid</code>
   *
   * @throws KbTypeException if fact based on <code>hlid</code> is not an instance of assertion
   *
   * @throws CreateException
   */
  public static Fact get(String hlid) throws KbTypeException, CreateException {
    return getInstance().getService().get(hlid);
  }
  
  /**
   * Get the <code>Fact</code> object that corresponds to <code>formulaStr</code> in the context
   * corresponding to <code>ctxStr</code>. Throws exceptions if no such formula is found in the
   * specified context in the KB, or if it's not already an assertion and a GAF.
   *
   * @param formulaStr string representation of the formula to be found
   * @param ctxStr string representation of the context of the formula
   *
   * @return a Fact based on <code>formulaStr</code> and <code>ctxStr</code>
   *
   * @throws CreateException
   * @throws KbTypeException
   * @throws KbObjectNotFoundException if no fact with the given formula string is found in the
   * context
   */
  public static Fact get(String formulaStr, String ctxStr) 
          throws KbTypeException, CreateException, KbObjectNotFoundException {
    return getInstance().getService().get(formulaStr, ctxStr);
  }
  
  /**
   * Get the <code>Fact</code> object that corresponds to <code>formula</code> in the context
   * <code>ctx</code>. Throws exceptions if no such formula is found in the specified context in the
   * KB, or if it's not already an assertion and a GAF.
   *
   * @param formula formula to be found
   * @param ctx context of the formula
   *
   * @return a Fact based on formula and context
   *
   * @throws CreateException
   * @throws KbTypeException
   * @throws KbObjectNotFoundException if no fact with the given formula string is found in the
   * context
   */
  public static Fact get(Sentence formula, Context ctx) 
          throws KbTypeException, CreateException, KbObjectNotFoundException {
    return getInstance().getService().get(formula, ctx);
  }
  
  /**
   * Finds or creates the <code>Fact</code> object that corresponds to <code>formulaStr</code> in
   * the default assertion context. Tries to assert if no such formula is found in the specified
   * context in the KB. Throws an exception if it is unable to make such an assertion or it is not a
   * GAF.
   *
   * Strength is set to Strength.DEFAULT by default Direction is set to Direction.FORWARD by default
   *
   * @param formulaStr the string representation of the formula to be found or created
   *
   * @return a Fact based on <code>formulaStr</code> and the default assertion context
   *
   * @throws CreateException
   * @throws KbTypeException
   * @throws InvalidFormulaInContextException if no fact with the given formula string is found or
   * created in the context
   */
  public static Fact findOrCreate(String formulaStr) 
          throws CreateException, KbTypeException, InvalidFormulaInContextException {
    return getInstance().getService().findOrCreate(formulaStr);
  }
  
  /**
   * Finds or creates the <code>Fact</code> object that corresponds to <code>formulaStr</code> in
   * the context corresponding to <code>ctxStr</code>. Tries to assert if no such formula is found
   * in the specified context in the KB. Throws an exception if it is unable to make such an
   * assertion or it is not a GAF.
   *
   * Strength is set to Strength.DEFAULT by default Direction is set to Direction.FORWARD by default
   *
   * @param formulaStr the string representation of the formula to be found or created
   * @param ctxStr the string representation of the context of the formula
   *
   * @return a Fact based on <code>formulaStr</code> and <code>ctxStr</code>
   *
   * @throws CreateException
   * @throws KbTypeException
   * @throws InvalidFormulaInContextException if no fact with the given formula string is found or
   * created in the context
   */
  public static Fact findOrCreate(String formulaStr, String ctxStr) 
          throws CreateException, KbTypeException, InvalidFormulaInContextException {
    return getInstance().getService().findOrCreate(formulaStr, ctxStr);
  }
  
  /**
   * Finds or creates the <code>Fact</code> object that corresponds to <code>formulaStr</code> in
   * the context corresponding to <code>ctxStr</code>. Tries to assert if no such formula is found
   * in the specified context in the KB. Throws an exception if it is unable to make such an
   * assertion or it is not a GAF.
   *
   * @param formulaStr the string representation of the formula to be found or created
   * @param ctxStr the string representation of the context of the formula
   * @param strength the strength of the assertion
   * @param direction the direction of the assertion
   *
   * @return a Fact based on formula and context
   *
   * @throws CreateException
   * @throws KbTypeException
   * @throws InvalidFormulaInContextException if no fact with the given formula string is found or
   * created in the context
   */
  public static Fact findOrCreate(String formulaStr, String ctxStr, Strength strength,
          Direction direction) 
          throws CreateException, KbTypeException, InvalidFormulaInContextException {
    return getInstance().getService().findOrCreate(formulaStr, ctxStr, strength, direction);
  }
  
  /**
   * Finds or creates the <code>Fact</code> object that corresponds to <code>formula</code> in the
   * default assertion context. Tries to assert if no such formula is found in the specified context
   * in the KB. Throws an exception if it is unable to make such an assertion or it is not a GAF.
   *
   * NOTE: All findOrCreate factory methods that take CycObject do not eventually create any new
   * object in the assertion because (most of) the CycObjects can only be built based on objects in
   * the KB. Some exceptions are Sentences, Variables and Symbols. NAUTs are also not in the KB, but
   * in the API they are effectively treated as being in the KB.
   *
   * Strength is set to Strength.DEFAULT by default Direction is set to Direction.FORWARD by default
   *
   * @param formula the formula to be found or created
   *
   * @return a Fact based on formula and default assertion context
   *
   * @throws CreateException
   * @throws KbTypeException
   *
   * @throws InvalidFormulaInContextException if no fact with the given formula string is found or
   * created in the context
   */
  public static Fact findOrCreate(Sentence formula) 
          throws KbTypeException, CreateException {
    return getInstance().getService().findOrCreate(formula);
  }
  
  /**
   * Finds or creates the <code>Fact</code> object that corresponds to <code>formula</code> in
   * <code>ctx</code>. Tries to assert if no such formula is found in the specified context in the
   * KB. Throws an exception if it is unable to make such an assertion or it is not a GAF.
   *
   * NOTE: All findOrCreate factory methods that take CycObject do not eventually create any new
   * object in the assertion because (most of) the CycObjects can only be built based on objects in
   * the KB. Some exceptions are Sentences, Variables and Symbols. NAUTs are also not in the KB, but
   * in the API they are effectively treated as being in the KB.
   *
   * Strength is set to Strength.DEFAULT by default Direction is set to Direction.FORWARD by default
   *
   * @param formula the formula to be found or created
   * @param ctx the context of the formula
   *
   * @return a Fact based on formula and context
   *
   * @throws CreateException
   * @throws KbTypeException
   *
   * @throws InvalidFormulaInContextException if no fact with the given formula string is found or
   * created in the context
   */
  public static Fact findOrCreate(Sentence formula, Context ctx) 
          throws KbTypeException, CreateException, InvalidFormulaInContextException {
    return getInstance().getService().findOrCreate(formula, ctx);
  }
  
  /**
   * Finds or creates the <code>Fact</code> object that corresponds to <code>formula</code> in
   * <code>ctx</code>. Tries to assert if no such formula is found in the specified context in the
   * KB. Throws an exception if it is unable to make such an assertion or it is not a GAF.
   *
   * NOTE: All findOrCreate factory methods that take CycObject do not eventually create any new
   * object in the assertion because (most of) the CycObjects can only be built based on objects in
   * the KB. Some exceptions are Sentences, Variables and Symbols. NAUTs are also not in the KB, but
   * in the API they are effectively treated as being in the KB.
   *
   * @param formula the formula to be found or created
   * @param ctx the context of the formula
   * @param strength the strength of the assertion
   * @param direction the direction of the assertion
   *
   * @return a Fact based on formula and context
   *
   * @throws CreateException
   * @throws KbTypeException
   *
   * @throws InvalidFormulaInContextException if no Fact with the given formula string is found or
   * created in the context
   *
   */
  public static Fact findOrCreate(Sentence formula, Context ctx, Strength strength, 
          Direction direction) 
          throws CreateException, KbTypeException, InvalidFormulaInContextException {
    return getInstance().getService().findOrCreate(formula, ctx, strength, direction);
  }

}
