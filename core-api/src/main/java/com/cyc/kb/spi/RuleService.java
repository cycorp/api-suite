package com.cyc.kb.spi;

/*
 * #%L
 * File: RuleService.java
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
 * Provides implementations of {@link com.cyc.kb.Rule}.
 *
 * @author nwinant
 */
public interface RuleService extends AssertionService {

  /**
   * Get the <code>Rule</code> that corresponds to <code>hlid</code>. Throws exceptions if the
   * <code>hlid</code> doesn't correspond to an object in the KB, or if the object identified by
   * <code>hlid</code> is not an assertion with an #$implies operator.
   *
   * <p>
   * NOTE: Most of the get (String) factory methods in other classes find objects based on
   * <code>hlid</code>s or string representations of the object. They use nameOrId as the string
   * variable name. This method finds objects based on <code>hlid</code> alone. If you want to find
   * a rule based on its string representation, try using
   * {@link #get(java.lang.String, java.lang.String)}.
   *
   * @param hlid the <code>hlid</code> corresponding to the Rule in the KB
   *
   * @return a Rule based on <code>hlid</code>
   *
   * @throws KbTypeException if assertion based on <code>hlid</code> is not an instance of assertion
   *                         with #$implies operator
   * @throws CreateException
   */
  @Override
  Rule get(String hlid) throws KbTypeException, CreateException;

  /**
   * Get the <code>Rule</code> for <code>formulaStr</code> in the context <code>ctxStr</code>.
   * Throws exceptions if no such assertion is found in the context, or if formulaStr is not a rule
   * formula with #$implies.
   *
   * @param formulaStr string representation of the formula to be found
   * @param ctxStr     string representation of the context of the formula
   *
   * @return a Rule based on <code>formulaStr</code> and <code>ctxStr</code>
   *
   * @throws CreateException
   * @throws KbTypeException
   * @throws KbObjectNotFoundException if no Rule with the given formula is found in the context
   */
  @Override
  Rule get(String formulaStr, String ctxStr)
          throws KbTypeException, CreateException, KbObjectNotFoundException;

  /**
   * Get the <code>Rule</code> that corresponds to <code>formula</code> in the context
   * <code>ctx</code>. Throws exceptions if no such formula is found in the specified context in the
   * KB, or if it's not an assertion with the <code>#$implies</code> operator.
   *
   * @param formula formula to be found
   * @param ctx     context of the formula
   *
   * @return an Rule based on formula and context
   *
   * @throws CreateException
   * @throws KbTypeException
   * @throws KbObjectNotFoundException if no assertion with the given formula is found in the
   *                                   context
   */
  @Override
  Rule get(Sentence formula, Context ctx)
          throws KbTypeException, CreateException, KbObjectNotFoundException;

  /**
   * Get the <code>Rule</code> that corresponds to a formula with <code>antecedent</code> as its
   * antecedent and <code>consequent</code> as its consequent in the context <code>ctx</code>.
   * Throws exceptions if no such formula is found in the specified context, or if the formula is
   * not a rule formula using #$implies.
   *
   * @param antecedent the literal in the ANTECEDENT of the rule
   * @param consequent the literal in the CONSEQUENT of the rule
   * @param ctx        context of the formula
   *
   * @return a Rule based on antecedent and consequent is ctx
   *
   * @throws CreateException
   * @throws KbTypeException
   * @throws KbObjectNotFoundException if no assertion with the given formula is found in the
   *                                   context
   */
  Rule get(Sentence antecedent, Sentence consequent, Context ctx)
          throws KbTypeException, CreateException, KbObjectNotFoundException;

  /**
   * Finds or creates the <code>Rule</code> that corresponds to <code>formulaStr</code> in the
   * default assertion context. Tries to assert if no rule with that formula can be found in that
   * context. Throws an exception if it is unable to create the rule in the KB.
   *
   * Strength is set to Strength.DEFAULT by default. Direction is set to Direction.BACKWARD by
   * default.
   *
   * @param formulaStr the string representation of the rule to be found or created
   *
   * @return a Rule based on <code>formulaStr</code> in the default assertion context
   *
   * @throws CreateException
   * @throws KbTypeException
   * @throws InvalidFormulaInContextException if no assertion with the given formula can be found or
   *                                          created in the context
   */
  @Override
  Rule findOrCreate(String formulaStr)
          throws CreateException, KbTypeException, InvalidFormulaInContextException;

  /**
   * Finds or creates the <code>Rule</code> that corresponds to <code>formulaStr</code> in the
   * context <code>ctxStr</code>. Tries to assert if no rule with that formula can be found in that
   * context. Throws an exception if it is unable to find or create the rule.
   *
   * Strength is set to Strength.DEFAULT by default. Direction is set to Direction.BACKWARD by
   * default.
   *
   * @param formulaStr the string representation of the rule formula
   * @param ctxStr     the string representation of the rule context
   *
   * @return a Rule based on <code>formulaStr</code> and <code>ctxStr</code>
   *
   * @throws CreateException
   * @throws KbTypeException
   * @throws InvalidFormulaInContextException if no assertion with the provided formula string can
   *                                          be found or created in the context
   */
  @Override
  Rule findOrCreate(String formulaStr, String ctxStr)
          throws CreateException, KbTypeException, InvalidFormulaInContextException;

  /**
   * Finds or creates the <code>Rule</code> that corresponds to <code>formulaStr</code> in the
   * context <code>ctxStr</code> with the specified strength and direction. Tries to assert if no
   * rule with that formula can be found in that context. Throws an exception if it is unable to
   * find or create such a rule.
   *
   * @param formulaStr the string representation of the formula to be found or created
   * @param ctxStr     the string representation of the context of the formula
   * @param strength   the strength of the assertion
   * @param direction  the direction of the assertion
   *
   * @return a Rule based on <code>formulaStr</code> in <code>ctxStr</code>
   *
   * @throws CreateException
   * @throws KbTypeException
   * @throws InvalidFormulaInContextException if no assertion with the provided formula can be found
   *                                          or created in the context
   */
  @Override
  Rule findOrCreate(String formulaStr, String ctxStr, Strength strength, Direction direction)
          throws CreateException, KbTypeException, InvalidFormulaInContextException;

  /**
   * Finds or creates the <code>Rule</code> object that corresponds to <code>formula</code> in the
   * default assertion context {@link DefaultContext}. Tries to assert if no rule with that formula
   * can be found in that context. Throws an exception if it is unable to find or create such an
   * assertion.
   *
   * Strength is set to Strength.DEFAULT by default. Direction is set to Direction.BACKWARD by
   * default.
   *
   * @param formula the formula to be found or created
   *
   * @return a Rule based on the formula in the default assertion context
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  @Override
  Rule findOrCreate(Sentence formula) throws KbTypeException, CreateException;

  /**
   * Finds or creates the <code>Rule</code> object that corresponds to <code>formula</code> in
   * <code>ctx</code>. Tries to assert if no rule with that formula can be found in that context.
   * Throws an exception if it is unable to make such an assertion.
   *
   * Strength is set to Strength.DEFAULT by default. Direction is set to Direction.BACKWARD by
   * default
   *
   * @param formula the formula to be found or created
   * @param ctx     the context of the formula
   *
   * @return a Rule based on formula and context
   *
   * @throws CreateException
   * @throws KbTypeException
   *
   * @throws InvalidFormulaInContextException if no assertion with the provided formula can be found
   *                                          or created in the context
   */
  @Override
  Rule findOrCreate(Sentence formula, Context ctx)
          throws KbTypeException, CreateException, InvalidFormulaInContextException;

  /**
   * Finds or creates the <code>Rule</code> object that corresponds to <code>formula</code> in
   * <code>ctx</code> using the specified strength and direction. Tries to assert if no rule with
   * that formula can be found in that context. Throws an exception if it is unable to find or
   * create such an assertion. an assertion.
   *
   * @param formula   the formula to be found or created
   * @param ctx       the context of the formula
   * @param strength  the strength of the assertion
   * @param direction the direction of the assertion
   *
   * @return a Rule based on formula and context
   *
   * @throws CreateException
   * @throws KbTypeException
   *
   * @throws InvalidFormulaInContextException if no assertion with the provided formula can be found
   *                                          or created in the context
   */
  @Override
  Rule findOrCreate(Sentence formula, Context ctx, Strength strength, Direction direction)
          throws CreateException, KbTypeException, InvalidFormulaInContextException;

  /**
   * Finds or creates the <code>Rule</code> that corresponds to a formula with
   * <code>antecedent</code> and <code>consequent</code> in the context <code>ctx</code>. Throws
   * exceptions if no such rule can be found or created in the specified context.
   *
   * @param antecedent the literal in the antecedent of the rule
   * @param consequent the literal in the antecedent of the rule
   * @param ctx        context of the formula
   *
   * @return an Rule based on posLiteral and negLiteral in context
   *
   * @throws CreateException
   * @throws KbTypeException
   */
  Rule findOrCreate(Sentence antecedent, Sentence consequent, Context ctx)
          throws KbTypeException, CreateException;

}
