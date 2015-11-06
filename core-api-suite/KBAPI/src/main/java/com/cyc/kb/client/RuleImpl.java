/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cyc.kb.client;

/*
 * #%L
 * File: RuleImpl.java
 * Project: KB API Implementation
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
import com.cyc.base.CycApiException;
import com.cyc.base.CycConnectionException;
import com.cyc.base.cycobject.CycAssertion;
import com.cyc.base.cycobject.CycConstant;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.FormulaSentence;
import com.cyc.base.cycobject.Guid;
import com.cyc.baseclient.cycobject.CycConstantImpl;
import com.cyc.baseclient.cycobject.DefaultCycObject;
import com.cyc.kb.Context;
import com.cyc.kb.KBAPIEnums.Direction;
import com.cyc.kb.KBAPIEnums.Strength;
import com.cyc.kb.Rule;
import com.cyc.kb.Sentence;
import com.cyc.kb.config.KBAPIConfiguration;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.InvalidFormulaInContextException;
import com.cyc.kb.exception.KBApiRuntimeException;
import com.cyc.kb.exception.KBObjectNotFoundException;
import com.cyc.kb.exception.KBTypeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Rule object is a facade for #$CycLRuleAssertion. A rule is a semantically well formed sentence,
 * where the ANTECEDENT implies the CONSEQUENT. The sentence is non-atomic and may contain open
 * variables.
 *
 * @author vijay
 * @version $Id: RuleImpl.java 157022 2015-03-11 16:19:37Z nwinant $
 * @since 1.0
 */
public class RuleImpl extends AssertionImpl implements Rule {

  private static final Logger log = LoggerFactory.getLogger(AssertionImpl.class.getCanonicalName());

  /**
   * This is not part of the public, supported KB API.
   *
   * @throws KBApiRuntimeException if there is a problem connecting to Cyc
   */
  RuleImpl() {
    super();
  }

  /**
   * This is not part of the public, supported KB API.
   * <p>
   * Return a new <code>Rule</code> based on the existing CycAssertion object
   * <code>cycAssert</code>. The KB assertion underlying <code>cycAssert</code> must already be a
   * #$CycLAssertion.
   *
   * It is used when the result of query is a CycObject and is known to be or requested to be cast
   * as a Rule.
   *
   * @param cycAssert	the CycObject wrapped by Rule. The constructor verifies that the CycObject is
   * an #$CycLAssertion with #$implies operator.
   *
   * @throws KBTypeException if cycAssert (which already exists) is not a #$CycLAssertion with
   * #$implies operator
   */
  @Deprecated
  RuleImpl(CycObject cycAssert) throws KBTypeException {
    super(cycAssert);
  }

  /**
   * Get the <code>Rule</code> that corresponds to <code>cycAssert</code>. Throws exceptions
   * if the object isn't in the KB, or if it is not actually a rule (i.e. it's not an instance of
   * CycAssertion with the #$implies operator. )
   *
   * @param cycAssert assertion object
   *
   * @return an Rule object encapsulating on cycAssert
   *
   * @throws KBTypeException if cycAssert is not an instance of assertion with the #$implies
   * operator
   * @throws CreateException
   */
  @Deprecated
  public static Rule get(CycObject cycAssert) throws KBTypeException, CreateException {
    return KBObjectFactory.get(cycAssert, RuleImpl.class);
  }

  /**
   * Get the <code>Rule</code> that corresponds to <code>hlid</code>. Throws exceptions if
   * the <code>hlid</code> doesn't correspond to an object in the KB, or if the object identified by <code>hlid</code> is not
   * an assertion with an #$implies operator.
   *
   * NOTE: Most of the get (String) factory methods in other classes find objects based on
   * <code>hlid</code>s or string representations of the object. They use nameOrId as the string
   * variable name. This method finds objects based on <code>hlid</code> alone. If you want to
   * find a rule based on its string representation, try using
   * {@link #get(java.lang.String, java.lang.String)}.
   *
   * @param hlid the <code>hlid</code> corresponding to the Rule in the KB
   *
   * @return a Rule based on <code>hlid</code>
   *
   * @throws KBTypeException if assertion based on <code>hlid</code> is not an instance of assertion
   * with #$implies operator
   * @throws CreateException
   */
  @SuppressWarnings("deprecation")
  public static Rule get(String hlid) throws KBTypeException, CreateException {
    Object o = null;
    // NOTE: The StandardKBObject was too geared towards Term (Constant, NAT) creation
    // Did not want to overload that with assertion creation as well. 
    // Also the get method here takes only hlid. For a factory method that takes String to 
    // find an assertion, see get(String formulaStr, String ctxStr)
    try {
      o = DefaultCycObject.fromPossibleCompactExternalId(hlid, getStaticAccess());
    } catch (CycConnectionException e) {
      throw new KBApiRuntimeException(e.getMessage(), e);
    }

    if (o instanceof CycAssertion) {
      log.debug("Found assertion: {} using HLID: {}", o, hlid);
      return KBObjectFactory.get((CycObject) o, RuleImpl.class);
    } else {
      String msg = "Could not find any Assertion with hlid: " + hlid + " in the KB.";
      log.error(msg);
      throw new KBObjectNotFoundException(msg);
    }
  }

  /**
   * Get the <code>Rule</code> for <code>formulaStr</code> in the context
   * <code>ctxStr</code>. Throws exceptions if no such assertion is found in the context, or if
   * formulaStr is not a rule formula with #$implies.
   *
   * @param formulaStr string representation of the formula to be found
   * @param ctxStr string representation of the context of the formula
   *
   * @return a Rule based on <code>formulaStr</code> and <code>ctxStr</code>
   * @throws CreateException
   * @throws KBTypeException
   * @throws KBObjectNotFoundException if no Rule with the given formula is found in the
   * context
   */
  @SuppressWarnings("deprecation")
  public static Rule get(String formulaStr, String ctxStr) throws KBTypeException, CreateException {
    CycAssertion ca = null;
    // @todo: There are two purposes of caching
    // 1. Reduce round trip to KB
    // 2. Use the same object if core is equal.
    // Since the cache key is cyclify() and hlid, we have to find the assertion
    // using formulaStr and ctxStr to get any of the cache keys. Which means we have to
    // do one trip to the KB anyways. But we still use KBObjectFactory.get to reuse the
    // same KBObject.
    // A separate KBObjectFactory method that takes the ist sentence of formula and mt,
    // could also eliminate the lookup step.
    ca = findAssertion(formulaStr, ctxStr);

    if (ca == null) {
      String msg = "Could not find any Assertion with: " + formulaStr + " in context: " + ctxStr;
      throw new KBObjectNotFoundException(msg);
    }
    return KBObjectFactory.get(ca, RuleImpl.class);
  }

  /**
   * Get the <code>Rule</code> that corresponds to <code>formula</code> in the context
   * <code>ctx</code>. Throws exceptions if no such formula is found in the specified context in the
   * KB, or if it's not an assertion with the #$imples operator.
   *
   * @param formula formula to be found
   * @param ctx context of the formula
   *
   * @return an Rule based on formula and context
   * @throws CreateException
   * @throws KBTypeException
   * @throws KBObjectNotFoundException if no assertion with the given formula is found in the
   * context
   */
  @SuppressWarnings("deprecation")
  public static Rule get(Sentence formula, Context ctx) throws KBTypeException, CreateException {
    CycAssertion ca = null;
    //@todo: There are two purposes of caching
    // 1. Reduce round trip to KB
    // 2. Use the same object if core is equal.
    // Since the cache key is cyclify() and hlid, we have to find the assertion
    // using formulaStr and ctxStr to get any of the cache keys. Which means we have to
    // do one trip to the KB anyways. But we still use KBObjectFactory.get to reuse the
    // same KBObject.       
    // A separate KBObjectFactory method that takes the ist sentence of formula and mt,
    // could also eliminate the lookup step.
    ca = findAssertion(FormulaSentence.class.cast(formula.getCore()), ContextImpl.asELMt(ctx));

    if (ca == null) {
      String msg = "Could not find the assertion: " + formula + " in context: " + ctx;
      throw new KBObjectNotFoundException(msg);
    }
    return KBObjectFactory.get(ca, RuleImpl.class);
  }

  /**
   * Get the <code>Rule</code> that corresponds to a formula with <code>antecedent</code> as
   * its antecedent and <code>consequent</code> as its consequent in the context <code>ctx</code>.
   * Throws exceptions if no such formula is found in the specified context, or if the formula
   * is not a rule formula using #$implies.
   *
   * @param antecedent the literal in the ANTECEDENT of the rule
   * @param consequent the literal in the CONSEQUENT of the rule
   * @param ctx context of the formula
   *
   * @return a Rule based on antecedent and consequent is ctx
   * @throws CreateException
   * @throws KBTypeException
   * @throws KBObjectNotFoundException if no assertion with the given formula is found in the
   * context
   */
  public static Rule get(Sentence antecedent, Sentence consequent, Context ctx)
          throws KBTypeException, CreateException {
    return RuleImpl.get(SentenceImpl.implies(antecedent, consequent), ctx);
  }

  /**
   * Finds or creates the <code>Rule</code> that corresponds to <code>formulaStr</code> in
   * the default assertion context. Tries to assert if no rule with that formula can be found in that context.
   * Throws an exception if it is unable to create the rule in the KB.
   *
   * Strength is set to Strength.DEFAULT by default.
   * Direction is set to Direction.BACKWARD by default.
   *
   * @param formulaStr the string representation of the rule to be found or created
   *
   * @return a Rule based on <code>formulaStr</code> in the default assertion context
   *
   * @throws CreateException
   * @throws KBTypeException
   * @throws InvalidFormulaInContextException if no assertion with the given formula can be found
   * or created in the context
   */
  public static Rule findOrCreate(String formulaStr) throws KBTypeException, CreateException {
    return RuleImpl.findOrCreate(formulaStr, KBAPIConfiguration.getDefaultContext().forAssertion().toString());
  }

  /**
   * Finds or creates the <code>Rule</code> that corresponds to <code>formulaStr</code> in
   * the context <code>ctxStr</code>. Tries to assert if no rule with that formula can be found in that context. Throws an exception if it is unable to find or create the rule.
   *
   * Strength is set to Strength.DEFAULT by default.
   * Direction is set to Direction.BACKWARD by default.
   *
   * @param formulaStr the string representation of the rule formula
   * @param ctxStr the string representation of the rule context 
   *
   * @return a Rule based on <code>formulaStr</code> and <code>ctxStr</code>
   *
   * @throws CreateException
   * @throws KBTypeException
   * @throws InvalidFormulaInContextException if no assertion with the provided formula string can be found
   * or created in the context
   */
  public static Rule findOrCreate(String formulaStr, String ctxStr)
          throws KBTypeException, CreateException {
    return RuleImpl.findOrCreate(formulaStr, ctxStr, Strength.AUTO, Direction.BACKWARD);
  }

  /**
   * Finds or creates the <code>Rule</code> that corresponds to <code>formulaStr</code> in
   * the context <code>ctxStr</code> with the specified strength and direction.
   * Tries to assert if no rule with that formula can be found in that context. Throws an
   * exception if it is unable to find or create such a rule.
   *
   * @param formulaStr the string representation of the formula to be found or created
   * @param ctxStr the string representation of the context of the formula
   * @param s	the strength of the assertion
   * @param d the direction of the assertion
   *
   * @return a Rule based on <code>formulaStr</code> in <code>ctxStr</code>
   *
   * @throws CreateException
   * @throws KBTypeException
   * @throws InvalidFormulaInContextException if no assertion with the provided formula can be found
   * or created in the context
   */
  @SuppressWarnings("deprecation")
  public static Rule findOrCreate(String formulaStr, String ctxStr, Strength s, Direction d)
          throws KBTypeException, CreateException {
    CycAssertion ca = null;

    // @todo: There are two purposes of caching
    // 1. Reduce round trip to KB
    // 2. Use the same object if core is equal.
    // Since the cache key is cyclify() and hlid, we have to find the assertion
    // using formulaStr and ctxStr to get any of the cache keys. Which means we have to
    // do one trip to the KB anyways. But we still use KBObjectFactory.get to reuse the
    // same KBObject.
    // A separate KBObjectFactory method that takes the ist sentence of formula and mt,
    // could also eliminate the lookup step.
    // The assertSentence tries to find the assertion anyways, before actually trying
    // to assert.
    ca = assertSentence(formulaStr, ctxStr, s, d);

    if (ca == null) {
      String msg = "Could not find or create the assertion: " + formulaStr + " in context: " + ctxStr;
      throw new InvalidFormulaInContextException(msg);
    }
    return KBObjectFactory.get(ca, RuleImpl.class);
  }

  /**
   * Finds or creates the <code>Rule</code> object that corresponds to <code>formula</code> in
   * <code>ctx</code>. Tries to assert if no rule with that formula can be found in that context.
   * Throws an exception if it is unable to make such an assertion.
   *
   * Strength is set to Strength.DEFAULT by default.
   * Direction is set to Direction.BACKWARD by default
   *
   * @param formula the formula to be found or created
   * @param ctx the context of the formula
   *
   * @return a Rule based on formula and context
   * @throws CreateException
   * @throws KBTypeException
   *
   * @throws InvalidFormulaInContextException if no assertion with the provided formula can be found
   * or created in the context
   */
  public static Rule findOrCreate(Sentence formula, Context ctx)
          throws KBTypeException, CreateException {
    return RuleImpl.findOrCreate(formula, ctx, Strength.AUTO, Direction.BACKWARD);
  }

  /**
   * Finds or creates the <code>Rule</code> object that corresponds to <code>formula</code> in the
   * default assertion context {@link KBAPIConfiguration}. Tries to assert if no rule with that formula can be found in that context.
   * Throws an exception if it is unable to find or create such an assertion.
   *
   * Strength is set to Strength.DEFAULT by default.
   * Direction is set to Direction.BACKWARD by default.
   *
   * @param formula the formula to be found or created
   *
   * @return a Rule based on the formula in the default assertion context
   *
   * @throws KBTypeException
   * @throws CreateException
   */
  public static Rule findOrCreate(Sentence formula) throws KBTypeException, CreateException {
    return RuleImpl.findOrCreate(formula, KBAPIConfiguration.getDefaultContext().forAssertion());
  }

  /**
   * Finds or creates the <code>Rule</code> object that corresponds to <code>formula</code> in
   * <code>ctx</code> using the specified strength and direction. Tries to assert if no rule with that formula can be found in that context.
   * Throws an exception if it is unable to find or create such an assertion.
   * an assertion.
   *
   * @param formula the formula to be found or created
   * @param ctx the context of the formula
   * @param s the strength of the assertion
   * @param d the direction of the assertion
   *
   * @return a Rule based on formula and context
   * @throws CreateException
   * @throws KBTypeException
   *
   * @throws InvalidFormulaInContextException if no assertion with the provided formula can be found
   * or created in the context
   *
   */
  @SuppressWarnings("deprecation")
  public static Rule findOrCreate(Sentence formula, Context ctx, Strength s, Direction d)
          throws KBTypeException, CreateException {
    CycAssertion ca = null;

    //@todo: There are two purposes of caching
    // 1. Reduce round trip to KB
    // 2. Use the same object if core is equal.
    // Since the cache key is cyclify() and hlid, we have to find the assertion
    // using formulaStr and ctxStr to get any of the cache keys. Which means we have to
    // do one trip to the KB anyways. But we still use KBObjectFactory.get to reuse the
    // same KBObject.       
    // A separate KBObjectFactory method that takes the ist sentence of formula and mt,
    // could also eliminate the lookup step.
    // The assertSentence tries to find the assertion anyways, before actually trying
    // to assert.
    ca = assertSentence(FormulaSentence.class.cast(formula.getCore()), ctx, s, d);

    if (ca == null) {
      String msg = "Could not find or create the assertion: " + formula + " in context: " + ctx;
      log.error(msg);
      throw new InvalidFormulaInContextException(msg);
    }
    return KBObjectFactory.get(ca, RuleImpl.class);
  }


  /**
   * Finds or creates the <code>Rule</code> that corresponds to a formula with
   * <code>antecedent</code> and <code>consequent</code> in the
   * context <code>ctx</code>. Throws exceptions if no such rule can be found or created in the
   * specified context.
   *
   * @param antecedent the literal in the antecedent of the rule
   * @param consequent the literal in the antecedent of the rule
   * @param ctx context of the formula
   *
   * @return an Rule based on posLiteral and negLiteral in context
   * @throws CreateException
   * @throws KBTypeException
   */
  public static Rule findOrCreate(Sentence antecedent, Sentence consequent, Context ctx)
          throws KBTypeException, CreateException {
    return RuleImpl.findOrCreate(SentenceImpl.implies(antecedent, consequent), ctx);
  }

  @Override
  public Sentence getAntecedent() {
    CycAssertion ca = (CycAssertion) this.getCore();
    try {
      FormulaSentence obj = (FormulaSentence) ca.getELFormula(getAccess()).getArg1();
      return KBObjectImpl.<Sentence>checkAndCastObject(obj);
    } catch (Exception ex) {
      throw new KBApiRuntimeException(ex.getMessage(), ex);
    }
  }

  @Override
  public Sentence getConsequent() {
    CycAssertion ca = (CycAssertion) this.getCore();
    try {
      FormulaSentence obj = (FormulaSentence) ca.getELFormula(getAccess()).getArg2();
      return KBObjectImpl.<Sentence>checkAndCastObject(obj);
    } catch (Exception ex) {
      throw new KBApiRuntimeException(ex.getMessage(), ex);
    }
  }

  static private final CycConstant IMPLIES = new CycConstantImpl("implies", new Guid("bd5880f8-9c29-11b1-9dad-c379636f7270"));

  /**
   * This is not part of the supported, public KB API. Check that the candidate core object is a
   * valid implication formula, a non-atomic sentence with possibly open sentences. In the CycKB the
   * object would be valid #$CycLRuleAssertion
   *
   * Internally this method checks if the the <code>cycObject</code> is an instance of CycAssertion
   * and the operator is #$implies.
   *
   * @see StandardKBObject#StandardKBObject(CycObject) for more comments
   */
  @Override
  protected boolean isValidCore(CycObject cycObject) {
    boolean isRule = false;
    if ((cycObject instanceof CycAssertion) && !((CycAssertion) cycObject).isGaf()) {

      CycObject operator;
      try {
        operator = ((CycAssertion) cycObject).getArg0(getAccess());
      } catch (CycApiException ex) {
        throw new KBApiRuntimeException(ex.getMessage(), ex);
      } catch (CycConnectionException ex) {
        throw new KBApiRuntimeException(ex.getMessage(), ex);
      }
      if (operator.equals(IMPLIES)) {
        isRule = true;
      }
    }
    return isRule;
  }
}
