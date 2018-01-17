package com.cyc.kb;

import com.cyc.Cyc;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.InvalidFormulaInContextException;
import com.cyc.kb.exception.KbObjectNotFoundException;
import com.cyc.kb.exception.KbTypeException;
import com.cyc.kb.spi.RuleService;

/*
 * #%L
 * File: Rule.java
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
/**
 * The interface for CycL rules (i.e. #$implies {@link Assertion}s).  
 * Refer to #$CycLRuleAssertion for more details. The rule relates two sentences, an
 * antecedent and a consequent.
 * Free variables are implicitly universally quantified, though other variables may be explicitly
 * existentially quantified.
 *
 * To create a Rule, see the
 * {@link com.cyc.kb.Rule#findOrCreate(String)}, {@link com.cyc.kb.Rule#get(String)}
 * and related methods.
 *
 * 
 * @author vijay
 * @version $Id: Rule.java 176345 2017-12-19 01:10:26Z nwinant $
 * @since 1.0
 */
public interface Rule extends Assertion {

  //====|    Factory methods    |=================================================================//
  
  /**
   * Get the <code>Rule</code> that corresponds to <code>hlid</code>. This static method wraps a
   * call to {@link RuleService#get(java.lang.String) }; see that method's documentation for more
   * details.
   *
   * @param hlid the <code>hlid</code> corresponding to the Rule in the KB
   *
   * @return a Rule based on <code>hlid</code>
   *
   * @throws KbTypeException if assertion based on <code>hlid</code> is not an instance of
   *                         assertion with #$implies operator
   * @throws CreateException
   */
  public static Rule get(String hlid) throws KbTypeException, CreateException {
    return Cyc.getRuleService().get(hlid);
  }

  /**
   * Get the <code>Rule</code> that corresponds to <code>formula</code> in the context
   * <code>ctx</code>. This static method wraps a call to
   * {@link RuleService#get(com.cyc.kb.Sentence, com.cyc.kb.Context)}; see that method's
   * documentation for more details.
   *
   * @param formula formula to be found
   * @param ctx     context of the formula
   *
   * @return an Rule based on formula and context
   *
   * @throws CreateException
   * @throws KbTypeException
   * @throws KbObjectNotFoundException if no assertion with the given formula is found in
   *                                   the context
   */
  public static Rule get(Sentence formula, Context ctx)
          throws KbTypeException, CreateException, KbObjectNotFoundException {
    return Cyc.getRuleService().get(formula, ctx);
  }

  /**
   * Get the <code>Rule</code> for <code>formulaStr</code> in the context
   * <code>ctxStr</code>. This static method wraps a call to
   * {@link RuleService#get(java.lang.String, java.lang.String)}; see that method's documentation 
   * for more details.
   *
   * @param formulaStr string representation of the formula to be found
   * @param ctxStr     string representation of the context of the formula
   *
   * @return a Rule based on <code>formulaStr</code> and <code>ctxStr</code>
   *
   * @throws CreateException
   * @throws KbTypeException
   * @throws KbObjectNotFoundException if no Rule with the given formula is found in the
   *                                   context
   */
  public static Rule get(String formulaStr, String ctxStr)
          throws KbTypeException, CreateException, KbObjectNotFoundException {
    return Cyc.getRuleService().get(formulaStr, ctxStr);
  }

  /**
   * Get the <code>Rule</code> that corresponds to a formula with <code>antecedent</code> as its
   * antecedent and <code>consequent</code> as its consequent in the context
   * <code>ctx</code>. This static method wraps a call to
   * {@link RuleService#get(com.cyc.kb.Sentence, com.cyc.kb.Sentence, com.cyc.kb.Context)}; see that
   * method's documentation for more details.
   *
   * @param antecedent the literal in the ANTECEDENT of the rule
   * @param consequent the literal in the CONSEQUENT of the rule
   * @param ctx        context of the formula
   *
   * @return a Rule based on antecedent and consequent is ctx
   *
   * @throws CreateException
   * @throws KbTypeException
   * @throws KbObjectNotFoundException if no assertion with the given formula is found in
   *                                   the context
   */
  public static Rule get(Sentence antecedent, Sentence consequent, Context ctx)
          throws KbTypeException, CreateException, KbObjectNotFoundException {
    return Cyc.getRuleService().get(antecedent, consequent, ctx);
  }
  
  public static Rule findOrCreate(Sentence formula, Context ctx, Strength strength, Direction direction)
          throws CreateException, KbTypeException, InvalidFormulaInContextException {
    return Cyc.getRuleService().findOrCreate(formula, ctx, strength, direction);
  }
  
  public static Rule findOrCreate(String formulaStr, String ctxStr, Strength strength, Direction direction)
          throws CreateException, KbTypeException, InvalidFormulaInContextException {
    return Cyc.getRuleService().findOrCreate(formulaStr, ctxStr, strength, direction);
  }
  
  public static Rule findOrCreate(Sentence formula, Context ctx)
          throws KbTypeException, CreateException, InvalidFormulaInContextException {
    return Cyc.getRuleService().findOrCreate(formula, ctx);
  }
  
  public static Rule findOrCreate(String formulaStr, String ctxStr)
          throws CreateException, KbTypeException, InvalidFormulaInContextException {
    return Cyc.getRuleService().findOrCreate(formulaStr, ctxStr);
  }

  public static Rule findOrCreate(Sentence formula) throws KbTypeException, CreateException {
    return Cyc.getRuleService().findOrCreate(formula);
  }

  public static Rule findOrCreate(String formulaStr)
          throws CreateException, KbTypeException, InvalidFormulaInContextException {
    return Cyc.getRuleService().findOrCreate(formulaStr);
  }
  
  public static Rule findOrCreate(Sentence antecedent, Sentence consequent, Context ctx)
          throws KbTypeException, CreateException {
    return Cyc.getRuleService().findOrCreate(antecedent, consequent, ctx);
  }
  
  //====|    Interface methods    |===============================================================//
  
  /**
   * Return the first argument of the Rule assertion. This is the antecedent of the rule and the
   * first argument of #$implies operator.
   *
   * @return the first argument of #$implies.
   */
  public Sentence getAntecedent();

  /**
   * Return the second argument of the Rule assertion. This is the consequent of the rule and the
   * second argument of the #$implies operator.
   *
   * @return the second argument of #$implies.
   */
  public Sentence getConsequent();
}
