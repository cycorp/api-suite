/*
 * Copyright 2015 Cycorp, Inc..
 *
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
 */

package com.cyc.kb.spi;

/*
 * #%L
 * File: RuleService.java
 * Project: Core API Object Specification
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

import com.cyc.kb.Assertion.Direction;
import com.cyc.kb.Assertion.Strength;
import com.cyc.kb.Context;
import com.cyc.kb.Rule;
import com.cyc.kb.Sentence;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.InvalidFormulaInContextException;
import com.cyc.kb.exception.KbObjectNotFoundException;
import com.cyc.kb.exception.KbTypeException;

/**
 *
 * @author nwinant
 */
public interface RuleService<T extends Rule> extends AssertionService<T> {

  /**
   * Provides implementation for {@link com.cyc.kb.RuleFactory#get(java.lang.String) }.
   *
   * @param hlid the <code>hlid</code> corresponding to the Rule in the KB
   *
   * @return a Rule based on <code>hlid</code>
   *
   * @throws KbTypeException if assertion based on <code>hlid</code> is not an instance of assertion
   * with #$implies operator
   * @throws CreateException
   */
  @Override
  T get(String hlid) throws KbTypeException, CreateException;

  /**
   * Provides implementation for 
   * {@link com.cyc.kb.RuleFactory#get(java.lang.String, java.lang.String) }.
   *
   * @param formulaStr string representation of the formula to be found
   * @param ctxStr string representation of the context of the formula
   *
   * @return a Rule based on <code>formulaStr</code> and <code>ctxStr</code>
   * @throws CreateException
   * @throws KbTypeException
   * @throws KbObjectNotFoundException if no Rule with the given formula is found in the context
   */
  @Override
  T get(String formulaStr, String ctxStr)
          throws KbTypeException, CreateException, KbObjectNotFoundException;

  /**
   * Provides implementation for 
   * {@link com.cyc.kb.RuleFactory#get(com.cyc.kb.Sentence, com.cyc.kb.Context) }.
   *
   * @param formula formula to be found
   * @param ctx context of the formula
   *
   * @return an Rule based on formula and context
   * @throws CreateException
   * @throws KbTypeException
   * @throws KbObjectNotFoundException if no assertion with the given formula is found in the
   * context
   */
  @Override
  T get(Sentence formula, Context ctx)
          throws KbTypeException, CreateException, KbObjectNotFoundException;

  /**
   * Provides implementation for 
   * {@link com.cyc.kb.RuleFactory#get(com.cyc.kb.Sentence, com.cyc.kb.Sentence, 
   * com.cyc.kb.Context) }.
   *
   * @param antecedent the literal in the ANTECEDENT of the rule
   * @param consequent the literal in the CONSEQUENT of the rule
   * @param ctx context of the formula
   *
   * @return a Rule based on antecedent and consequent is ctx
   * @throws CreateException
   * @throws KbTypeException
   * @throws KbObjectNotFoundException if no assertion with the given formula is found in the
   * context
   */
  T get(Sentence antecedent, Sentence consequent, Context ctx) 
          throws KbTypeException, CreateException, KbObjectNotFoundException;

  /**
   * Provides implementation for {@link com.cyc.kb.RuleFactory#findOrCreate(java.lang.String) }.
   *
   * @param formulaStr the string representation of the rule to be found or created
   *
   * @return a Rule based on <code>formulaStr</code> in the default assertion context
   *
   * @throws CreateException
   * @throws KbTypeException
   * @throws InvalidFormulaInContextException if no assertion with the given formula can be found or
   * created in the context
   */
  @Override
  T findOrCreate(String formulaStr)
          throws CreateException, KbTypeException, InvalidFormulaInContextException;

  /**
   * Provides implementation for 
   * {@link com.cyc.kb.RuleFactory#findOrCreate(java.lang.String, java.lang.String) }.
   *
   * @param formulaStr the string representation of the rule formula
   * @param ctxStr the string representation of the rule context
   *
   * @return a Rule based on <code>formulaStr</code> and <code>ctxStr</code>
   *
   * @throws CreateException
   * @throws KbTypeException
   * @throws InvalidFormulaInContextException if no assertion with the provided formula string can
   * be found or created in the context
   */
  @Override
  T findOrCreate(String formulaStr, String ctxStr)
          throws CreateException, KbTypeException, InvalidFormulaInContextException;

  /**
   * Provides implementation for 
   * {@link com.cyc.kb.RuleFactory#findOrCreate(java.lang.String, java.lang.String, 
   * com.cyc.kb.Assertion.Strength, com.cyc.kb.Assertion.Direction) }.
   *
   * @param formulaStr the string representation of the formula to be found or created
   * @param ctxStr the string representation of the context of the formula
   * @param strength the strength of the assertion
   * @param direction the direction of the assertion
   *
   * @return a Rule based on <code>formulaStr</code> in <code>ctxStr</code>
   *
   * @throws CreateException
   * @throws KbTypeException
   * @throws InvalidFormulaInContextException if no assertion with the provided formula can be found
   * or created in the context
   */
  @Override
  T findOrCreate(String formulaStr, String ctxStr, Strength strength, Direction direction)
          throws CreateException, KbTypeException, InvalidFormulaInContextException;

  /**
   * Provides implementation for {@link com.cyc.kb.RuleFactory#findOrCreate(com.cyc.kb.Sentence) }.
   *
   * @param formula the formula to be found or created
   *
   * @return a Rule based on the formula in the default assertion context
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  @Override
  T findOrCreate(Sentence formula) throws KbTypeException, CreateException;

  /**
   * Provides implementation for 
   * {@link com.cyc.kb.RuleFactory#findOrCreate(com.cyc.kb.Sentence, com.cyc.kb.Context) }.
   *
   * @param formula the formula to be found or created
   * @param ctx the context of the formula
   *
   * @return a Rule based on formula and context
   * @throws CreateException
   * @throws KbTypeException
   *
   * @throws InvalidFormulaInContextException if no assertion with the provided formula can be found
   * or created in the context
   */
  @Override
  T findOrCreate(Sentence formula, Context ctx)
          throws KbTypeException, CreateException, InvalidFormulaInContextException;

  /**
   * Provides implementation for 
   * {@link com.cyc.kb.RuleFactory#findOrCreate(com.cyc.kb.Sentence, com.cyc.kb.Context, 
   * com.cyc.kb.Assertion.Strength, com.cyc.kb.Assertion.Direction) }.
   *
   * @param formula the formula to be found or created
   * @param ctx the context of the formula
   * @param strength the strength of the assertion
   * @param direction the direction of the assertion
   *
   * @return a Rule based on formula and context
   * @throws CreateException
   * @throws KbTypeException
   *
   * @throws InvalidFormulaInContextException if no assertion with the provided formula can be found
   * or created in the context
   */
  @Override
  T findOrCreate(Sentence formula, Context ctx, Strength strength, Direction direction)
          throws CreateException, KbTypeException, InvalidFormulaInContextException;

  /**
   * Provides implementation for
   * {@link com.cyc.kb.RuleFactory#findOrCreate(com.cyc.kb.Sentence, 
   * com.cyc.kb.Sentence, com.cyc.kb.Context) }.
   *
   * @param antecedent the literal in the antecedent of the rule
   * @param consequent the literal in the antecedent of the rule
   * @param ctx context of the formula
   *
   * @return an Rule based on posLiteral and negLiteral in context
   * @throws CreateException
   * @throws KbTypeException
   */
  T findOrCreate(Sentence antecedent, Sentence consequent, Context ctx) 
          throws KbTypeException, CreateException;

}
