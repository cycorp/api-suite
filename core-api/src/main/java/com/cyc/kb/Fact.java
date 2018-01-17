package com.cyc.kb;

/*
 * #%L
 * File: Fact.java
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
import com.cyc.kb.exception.InvalidFormulaInContextException;
import com.cyc.kb.exception.KbObjectNotFoundException;
import com.cyc.kb.exception.KbTypeException;
import com.cyc.kb.spi.FactService;

/**
 * The interface for {@link Assertion}s that relate {@link KbCollection}s and
 * {@link KbIndividual}s to each other and to primitive values such as numbers
 * and strings. The vast majority of {@link Assertion}s are of this type.
 * 
 * @author vijay
 */
public interface Fact extends Assertion {

  //====|    Factory methods    |=================================================================//
  
  /**
   * Get the <code>Fact</code> object that corresponds to <code>hlid</code>. This static method
   * wraps a call to {@link FactService#get(java.lang.String) }; see that method's documentation for
   * more details.
   *
   * @param hlid the <code>hlid</code> corresponding to the Fact in the KB
   *
   * @return a Fact based on <code>hlid</code>
   *
   * @throws KbTypeException if fact based on <code>hlid</code> is not an instance of
   *                         assertion
   *
   * @throws CreateException
   */
  public static Fact get(String hlid) throws KbTypeException, CreateException {
    return Cyc.getFactService().get(hlid);
  }

  /**
   * Get the <code>Fact</code> object that corresponds to <code>formula</code> in the
   * context
   * <code>ctx</code>. This static method wraps a call to
   * {@link FactService#get(com.cyc.kb.Sentence, com.cyc.kb.Context)}; see that method's
   * documentation for more details.
   *
   * @param formula formula to be found
   * @param ctx     context of the formula
   *
   * @return a Fact based on formula and context
   *
   * @throws CreateException
   * @throws KbTypeException
   * @throws KbObjectNotFoundException if no fact with the given formula string is found
   *                                   in the context
   */
  public static Fact get(Sentence formula, Context ctx)
          throws KbTypeException, CreateException, KbObjectNotFoundException {
    return Cyc.getFactService().get(formula, ctx);
  }

  /**
   * Get the <code>Fact</code> object that corresponds to <code>formulaStr</code> in the
   * context corresponding to <code>ctxStr</code>. This static method wraps a call to
   * {@link FactService#get(java.lang.String, java.lang.String)}; see that method's documentation
   * for more details.
   *
   * @param formulaStr string representation of the formula to be found
   * @param ctxStr     string representation of the context of the formula
   *
   * @return a Fact based on <code>formulaStr</code> and <code>ctxStr</code>
   *
   * @throws CreateException
   * @throws KbTypeException
   * @throws KbObjectNotFoundException if no fact with the given formula string is found
   *                                   in the context
   */
  public static Fact get(String formulaStr, String ctxStr)
          throws KbTypeException, CreateException, KbObjectNotFoundException {
    return Cyc.getFactService().get(formulaStr, ctxStr);
  }
  
  public static Fact findOrCreate(Sentence formula, Context ctx, Strength strength, Direction direction)
          throws CreateException, KbTypeException, InvalidFormulaInContextException {
    return Cyc.getFactService().findOrCreate(formula, ctx, strength, direction);
  }
  
  public static Fact findOrCreate(String formulaStr, String ctxStr, Strength strength, Direction direction)
          throws CreateException, KbTypeException, InvalidFormulaInContextException {
    return Cyc.getFactService().findOrCreate(formulaStr, ctxStr, strength, direction);
  }
  
  public static Fact findOrCreate(Sentence formula, Context ctx)
          throws KbTypeException, CreateException, InvalidFormulaInContextException {
    return Cyc.getFactService().findOrCreate(formula, ctx);
  }
  
  public static Fact findOrCreate(String formulaStr, String ctxStr)
          throws CreateException, KbTypeException, InvalidFormulaInContextException {
    return Cyc.getFactService().findOrCreate(formulaStr, ctxStr);
  }

  public static Fact findOrCreate(Sentence formula) throws KbTypeException, CreateException {
    return Cyc.getFactService().findOrCreate(formula);
  }

  public static Fact findOrCreate(String formulaStr)
          throws CreateException, KbTypeException, InvalidFormulaInContextException {
    return Cyc.getFactService().findOrCreate(formulaStr);
  }
  
  //====|    Interface methods    |===============================================================//

}
