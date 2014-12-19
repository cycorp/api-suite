package com.cyc.kb.client;

/*
 * #%L
 * File: FactImpl.java
 * Project: KB API
 * %%
 * Copyright (C) 2013 - 2014 Cycorp, Inc
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
import com.cyc.base.cycobject.CycList;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.FormulaSentence;
import com.cyc.baseclient.cycobject.DefaultCycObject;
import com.cyc.kb.Context;
import com.cyc.kb.Fact;
import com.cyc.kb.KBAPIEnums.Direction;
import com.cyc.kb.KBAPIEnums.Strength;
import com.cyc.kb.KBObject;
import com.cyc.kb.KBPredicate;
import com.cyc.kb.Sentence;
import com.cyc.kb.config.KBAPIConfiguration;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.DeleteException;
import com.cyc.kb.exception.InvalidFormulaInContextException;
import com.cyc.kb.exception.KBApiException;
import com.cyc.kb.exception.KBApiRuntimeException;
import com.cyc.kb.exception.KBObjectNotFoundException;
import com.cyc.kb.exception.KBTypeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A fact object is an assertion with a variable-free sentence with no conjunctions or operators
 * (a ground atomic formula), and a facade for #$CycLGAFAssertion. In general, this class 
 * is used to create and remove arbitrary assertions from Cyc.
 * 
 * @author Vijay Raj
 * @version $Id: FactImpl.java 154990 2014-11-14 22:46:51Z nwinant $
 * @since 1.0
 */

/*
 * @todo add factory class, as well as method to confirm possible facts. Enum
 * values might include ASSERTED, FORWARD_DERIVED, TRIVIALLY_DERIVABLE, UNKNOWN
 * (or we might not want the TRIVIALLY_DERIVABLE part)
 */

// @todo DaveS review documentation
public class FactImpl extends AssertionImpl implements Fact {

  private static final Logger log = LoggerFactory.getLogger(FactImpl.class.getCanonicalName());
  
  /**
   * This not part of the public, supported KB API. default constructor, calls the default
   * super constructor
   * 
   * @throws KBApiRuntimeException
   *           if there is a problem connecting to Cyc
   */
  protected FactImpl() {
    super();
  }

  /**
   * This not part of the public, supported KB API. an implementation-dependent constructor
   * <p>
   * Return a new
   * <code>Fact</code> based on the existing CycAssertion object
   * <code>cycFact</code>. The KB assertion underlying
   * <code>cycFact</code> must already be a #$CycLGAFAssertion. The constructor
   * verifies that cycFact is CycAssertion and cycFact.isGaf() is true.
   * 
   * It is used when the result of query is a CycObject and is known to be or
   * requested to be cast as an Assertion.
   *
   * @param cycFact the CycObject wrapped by Assertion. The constructor
   * verifies that the CycObject is an #$CycLGAFAssertion.
   * 
   * @throws KBTypeException  if cycAssert (which already exists) is not 
   * a #$CycLGAFAssertion
   */
  // We have made this public for the reflection mechanism to see this class.
  // If made package private, reflection of getConstructor(CycObject.class) fails
  // TODO: Check if this is a problem for other classes as well.
  @SuppressWarnings("deprecation")
  FactImpl(CycObject cycAssert) throws KBTypeException {
    super(cycAssert);
  }

  /**
   * find or create a <code>Fact</code> using <code>factStr</code> in the
   * context represented by <code>ctxStr</code>.
   * <p>
   * As elsewhere in the KB API, <code>factStr</code> and <code>ctxStr</code>
   * may contain the CycL constant prefix "#$" on CycL terms, but the prefix is
   * not required. Thus, either of the following strings would be acceptable:
   * <ul>
   * <li>
   * "(birthDate GeorgeWashington (DayFn 22 (MonthFn February (YearFn 1732))))"
   * <li>
   * "(#$birthDate #$GeorgeWashington (#$DayFn 22 (#$MonthFn #$February (#$YearFn 1732))))"
   * </ul>
   * 
   * @param ctxStr
   *          the string representing the context where the fact will be
   *          asserted
   * @param factStr
   *          the CycL string of the fact to be asserted.
   * 
   * @throws KBApiException
   */
  FactImpl(String ctxStr, String factStr) throws KBApiException {
    this(false, ctxStr, factStr);
  }

  /**
   * find or create a fact using the arguments in <code>argList</code> in the
   * context.
   * <p>
   * Elements of <code>argList</code> are expected to be instances of
   * <code>KBObject</code>, or 'primitive' java objects (i.e. Numbers, Strings),
   * or {@link java.util.Date}. If arguments are Strings, they will not be
   * interpreted as Cyc terms, but will be treated as raw Strings.
   * 
   * @param ctx
   *          the context where the fact will be asserted
   * @param pred the predicate of the GAF
   * @param argList
   *          the arguments of the fact
   * 
   * @throws KBApiException
   */
  FactImpl(Context ctx, KBPredicate pred, Object... argList)
      throws KBApiException {
    this(false, ctx, pred, argList);
  }

  /**
   * find or create a fact using <code>sentence</code> in the context.
   * <p>
   * 
   * @param ctx
   *          the context where the fact will be asserted
   * @param sentence
   *          the sentence of the fact
   * 
   * @throws KBApiException
   */
  FactImpl(Context ctx, Sentence sentence) throws KBApiException {
    this(false, ctx, sentence);
  }

  public FactImpl(boolean findOnly, String ctxStr, String factStr)
      throws KBApiException {
    super();
    try {
      CycAssertion ca = findAssertion(factStr, ctxStr);

      if (ca != null) {
        core = ca;
      } else if (findOnly == true) {
        throw new KBObjectNotFoundException("Could not find the assertion: "
            + factStr + " in Mt: " + ctxStr);
      } else {
        // For facts, the direction is always backward.
        core = assertSentence(factStr, ctxStr, null, null);
      }
    } catch (CreateException ex) {
      throw new KBApiException(ex);
    } catch (KBTypeException ex) {
      throw new KBApiException(ex);
    }
  }

  @SuppressWarnings("deprecation")
 FactImpl(boolean findOnly, Context ctx, KBPredicate pred, Object... argList)
      throws KBApiException {
    super();
    try {
      // Turn on the findOnly flag when Context is #$EverythingPSC or
      // #$InferencePSC
      // TODO: More generally, we need to have query-only MTs and assertion MTs
      // and
      // check if the Context is query only MT.

      if (findOnly == false
          && (ctx.equals(Constants.everythingPSCMt()) || ctx.equals(Constants
              .inferencePSCMt()))) {
        findOnly = true;
        log.warn("Overriding 'findOnly' flag to true because Context is either EverythingPSC or InferencePSC");
      }
      // @todo above should error rather than do something that you told it not
      // to, probably

      // CycFormulaSentence factSentence = constructSentence(pred, argListArray);
      FormulaSentence factSentence = (FormulaSentence) new SentenceImpl(pred, argList).getCore();
      
      // @todo get rid of cut-n-paste between this method and the following
      // one...
      log.trace("fact Sentence: {}", factSentence);

      CycAssertion ca = findAssertion(factSentence, ctx.asELMt());

      if (ca != null) {
        core = ca;
      } else if (findOnly == true) {
        throw new KBObjectNotFoundException("Could not find the assertion: "
            + factSentence + " in Mt: " + ctx);
      } else {
        // core = assertSentence(ctx, factSentence);
        // For Facts, the direction is always backward
        core = AssertionImpl.assertSentence(factSentence, ctx, null, null);
      }
    } catch (Exception ex) {
      throw new KBApiException(ex);
    }
  }

  @SuppressWarnings("deprecation") 
  FactImpl(boolean findOnly, Context ctx, Sentence factSentence)
      throws KBApiException {
    super();
    try {
      // Turn on the findOnly flag when Context is #$EverythingPSC or
      // #$InferencePSC
      // TODO: More generally, we need to have query-only MTs and assertion MTs
      // and
      // check if the Context is query only MT.

      if (findOnly == false
          && (ctx.equals(Constants.everythingPSCMt()) || ctx.equals(Constants
              .inferencePSCMt()))) {
        findOnly = true;
        log.warn("Overriding 'findOnly' flag to true because Context is either EverythingPSC or InferencePSC");
      }
      // @todo above should error rather than do something that you told it not
      // to, probably

      log.trace("fact Sentence: {}", factSentence);

      CycAssertion ca = findAssertion(
          (FormulaSentence) factSentence.getCore(), ctx.asELMt());

      if (ca != null) {
        core = ca;
      } else if (findOnly == true) {
        throw new KBObjectNotFoundException("Could not find the assertion: "
            + factSentence + " in Mt: " + ctx);
      } else {
        // core = assertSentence(ctx,
        // (CycFormulaSentence)factSentence.getCore());
        core = assertSentence((FormulaSentence) factSentence.getCore(), ctx,
            null, null);
      }
    } catch (Exception ex) {
      throw new KBApiException(ex);
    }
  }

  /**
   * Get the <code>Fact</code> object that corresponds to
   * <code>cycFact</code>. Throws exceptions if the object isn't in the KB, or if
   * it's not already an assertion. (Currently cycFact is only checked to be an 
   * instance of CycAssertion and to be a GAF - Ground Atomic Formula. )
   * 
   * @param cycFact candidate fact object
   * 
   * @return a Fact based on cycFact
   * 
   * @throws KBTypeException if the cycFact is not an instance of assertion and 
   * a ground atomic formula
   * @throws CreateException 
   */
  @Deprecated
  public static FactImpl get(CycObject cycFact) throws KBTypeException, CreateException {
    return KBObjectFactory.get(cycFact, FactImpl.class);
  }

  /**
   * Get the <code>Fact</code> object that corresponds to
   * <code>hlid</code>. Throws exceptions if the <code>hlid</code> isn't in the KB, or if
   * it's not already an assertion and a GAF.
   * 
   * NOTE: The get (String) factory methods in other classes find objects based 
   * on <code>hlid</code> or string representation of the object. They use nameOrId
   * as the string variable name. But this method finds objects based on
   * <code>hlid</code> alone. This is because it is much more common and easier
   * to find assertions based on two strings, formula and context. 
   * 
   * @param hlid  the <code>hlid</code> corresponding to the Fact in the KB
   * 
   * @return a Fact based on <code>hlid</code>
   * 
   * @throws KBTypeException  if fact based on <code>hlid</code> is not an instance of assertion 
   * 
   * @throws CreateException 
   */
  @SuppressWarnings("deprecation")
public static FactImpl get(String hlid) throws KBTypeException, CreateException {
    Object o = null;
    // NOTE: The StandardKBObject was too geared towards Term (Constant, NAT) creation
    // Did not want to overload that with assertion creation as well. 
    // Also the get method here takes only hlid. For a factory method that takes String to 
    // find an assertion, see get(String formulaStr, String ctxStr)
    try {
      o = DefaultCycObject.fromPossibleCompactExternalId(hlid, getStaticAccess());
    } catch (CycConnectionException e){
      throw new KBApiRuntimeException(e.getMessage(), e);
    }
    
    if (o instanceof CycObject) {
      return KBObjectFactory.get((CycObject)o, FactImpl.class);
    } else {
      String msg = "Could not find any Assertion with hlid: " + hlid + " in the KB.";
      throw new KBObjectNotFoundException(msg);
    }
  }
  
  /**
   * Get the <code>Fact</code> object that corresponds to
   * <code>formulaStr</code> in the context corresponding to <code>ctxStr</code>. Throws exceptions 
   * if no such formula
   * is found in the specified context in the KB, or if
   * it's not already an assertion and a GAF. 
   * 
   * @param formulaStr string representation of the formula to be found
   * @param ctxStr     string representation of the context of the formula
   * 
   * @return a Fact based on <code>formulaStr</code> and <code>ctxStr</code>
   * 
   * @throws CreateException 
   * @throws KBTypeException 
   * @throws KBObjectNotFoundException if no fact with the given formula 
   * string is found in the context
   */
  
  @SuppressWarnings("deprecation")
  public static FactImpl get(String formulaStr, String ctxStr) throws KBTypeException, CreateException {
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

    if (ca == null){
      String msg = "Could not find any Assertion with: " + formulaStr + " in context: " + ctxStr;
      throw new KBObjectNotFoundException(msg);
    }
    return KBObjectFactory.get(ca, FactImpl.class);
  }

  /**
   * Get the <code>Fact</code> object that corresponds to
   * <code>formula</code> in the context <code>ctx</code>. Throws exceptions if no such formula
   * is found in the specified context in the KB, or if
   * it's not already an assertion and a GAF.
   * 
   * @param formula formula to be found
   * @param ctx     context of the formula
   * 
   * @return a Fact based on formula and context
   * 
   * @throws CreateException 
   * @throws KBTypeException 
   * @throws KBObjectNotFoundException if no fact with the given formula 
   * string is found in the context
   */
  @SuppressWarnings("deprecation")
public static FactImpl get(SentenceImpl formula, Context ctx) throws KBTypeException, CreateException {
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
    ca = findAssertion(FormulaSentence.class.cast(formula.getCore()), ctx.asELMt());
          
    if (ca == null){
      String msg = "Could not find the assertion: " + formula + " in context: " + ctx;
      throw new KBObjectNotFoundException(msg);
    }
    return KBObjectFactory.get(ca, FactImpl.class);
  }

  /**
   * Finds or creates the <code>Fact</code> object that corresponds to
   * <code>formulaStr</code> in the default assertion context. Tries to assert if no such formula
   * is found in the specified context in the KB. Throws an exception if it is unable
   * to make such an assertion or it is not a GAF.
   * 
   * Strength is set to Strength.DEFAULT by default
   * Direction is set to Direction.FORWARD by default 
   *  
   * @param formulaStr the string representation of the formula to be found or created
   * 
   * @return a Fact based on <code>formulaStr</code> and the default assertion context
   * 
   * @throws CreateException 
   * @throws KBTypeException 
   * @throws InvalidFormulaInContextException if no fact with the given formula 
   * string is found or created in the context
   */
  public static FactImpl findOrCreate(String formulaStr) throws KBTypeException, CreateException {
	  return FactImpl.findOrCreate(formulaStr, KBAPIConfiguration.getDefaultContext().forAssertion().toString());
  }
  
  /**
   * Finds or creates the <code>Fact</code> object that corresponds to
   * <code>formulaStr</code> in the context corresponding to <code>ctxStr</code>. Tries to assert if no such formula
   * is found in the specified context in the KB. Throws an exception if it is unable
   * to make such an assertion or it is not a GAF.
   * 
   * Strength is set to Strength.DEFAULT by default
   * Direction is set to Direction.FORWARD by default 
   *  
   * @param formulaStr the string representation of the formula to be found or created
   * @param ctxStr     the string representation of the context of the formula
   * 
   * @return a Fact based on <code>formulaStr</code> and <code>ctxStr</code>
   * 
   * @throws CreateException 
   * @throws KBTypeException 
   * @throws InvalidFormulaInContextException if no fact with the given formula 
   * string is found or created in the context
   */
  public static FactImpl findOrCreate(String formulaStr, String ctxStr) 
      throws KBTypeException, CreateException {
    return FactImpl.findOrCreate(formulaStr, ctxStr, Strength.AUTO, Direction.AUTO);
  }
  
  /**
   * Finds or creates the <code>Fact</code> object that corresponds to
   * <code>formulaStr</code> in the context corresponding to <code>ctxStr</code>. Tries to assert if no such formula
   * is found in the specified context in the KB. Throws an exception if it is unable
   * to make such an assertion or it is not a GAF.
   * 
   * @param formulaStr the string representation of the formula to be found or created
   * @param ctxStr     the string representation of the context of the formula
   * @param s      the strength of the assertion
   * @param d          the direction of the assertion  
   * 
   * @return a Fact based on formula and context
   * 
   * @throws CreateException 
   * @throws KBTypeException 
   * @throws InvalidFormulaInContextException if no fact with the given formula 
   * string is found or created in the context
   */
  
  @SuppressWarnings("deprecation")
public static FactImpl findOrCreate(String formulaStr, String ctxStr, Strength s, Direction d) 
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

    if (ca == null){
      String msg = "Could not find or create the assertion: " + formulaStr + " in context: " + ctxStr;
      throw new InvalidFormulaInContextException(msg);
    }
    return KBObjectFactory.get(ca, FactImpl.class);
  }
  
  /**
   * Finds or creates the <code>Fact</code> object that corresponds to
   * <code>formula</code> in the default assertion context. Tries to assert if no such formula
   * is found in the specified context in the KB. Throws an exception if it is unable
   * to make such an assertion or it is not a GAF.
   * 
   * NOTE: All findOrCreate factory methods that take CycObject do not eventually create 
   * any new object in the assertion because (most of) the CycObjects can only be built 
   * based on objects in the KB. Some exceptions are Sentences, Variables and Symbols.
   * NAUTs are also not in the KB, but in the API they are effectively treated as being in 
   * the KB. 
   * 
   * Strength is set to Strength.DEFAULT by default
   * Direction is set to Direction.FORWARD by default 
   *  
   * @param formula the formula to be found or created
   * 
   * @return a Fact based on formula and default assertion context
   * 
   * @throws CreateException 
   * @throws KBTypeException 
   * 
   * @throws InvalidFormulaInContextException if no fact with the given formula 
   * string is found or created in the context
   */
  public static FactImpl findOrCreate(Sentence formula) throws KBTypeException, CreateException {
	  return FactImpl.findOrCreate(formula, KBAPIConfiguration.getDefaultContext().forAssertion());
  }

  /**
   * Finds or creates the <code>Fact</code> object that corresponds to
   * <code>formula</code> in <code>ctx</code>. Tries to assert if no such formula
   * is found in the specified context in the KB. Throws an exception if it is unable
   * to make such an assertion or it is not a GAF.
   * 
   * NOTE: All findOrCreate factory methods that take CycObject do not eventually create 
   * any new object in the assertion because (most of) the CycObjects can only be built 
   * based on objects in the KB. Some exceptions are Sentences, Variables and Symbols.
   * NAUTs are also not in the KB, but in the API they are effectively treated as being in 
   * the KB. 
   * 
   * Strength is set to Strength.DEFAULT by default
   * Direction is set to Direction.FORWARD by default 
   *  
   * @param formula the formula to be found or created
   * @param ctx     the context of the formula
   * 
   * @return a Fact based on formula and context
   * 
   * @throws CreateException 
   * @throws KBTypeException 
   * 
   * @throws InvalidFormulaInContextException if no fact with the given formula 
   * string is found or created in the context
   */
  public static FactImpl findOrCreate(Sentence formula, Context ctx) 
      throws KBTypeException, CreateException {
    return FactImpl.findOrCreate(formula, ctx, Strength.AUTO, Direction.AUTO);
  }
  
  /**
   * Finds or creates the <code>Fact</code> object that corresponds to
   * <code>formula</code> in <code>ctx</code>. Tries to assert if no such formula
   * is found in the specified context in the KB. Throws an exception if it is unable
   * to make such an assertion or it is not a GAF.
   * 
   * NOTE: All findOrCreate factory methods that take CycObject do not eventually create 
   * any new object in the assertion because (most of) the CycObjects can only be built 
   * based on objects in the KB. Some exceptions are Sentences, Variables and Symbols.
   * NAUTs are also not in the KB, but in the API they are effectively treated as being in 
   * the KB. 
   * 
   * @param formula the formula to be found or created
   * @param ctx     the context of the formula
   * @param s   the strength of the assertion
   * @param d       the direction of the assertion  
   * 
   * @return a Fact based on formula and context
   * 
   * @throws CreateException 
   * @throws KBTypeException 
   * 
   * @throws InvalidFormulaInContextException   if no Fact with the given formula 
   * string is found or created in the context
   * 
   */
  @SuppressWarnings("deprecation")
public static FactImpl findOrCreate(Sentence formula, Context ctx, Strength s, Direction d) 
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
       
    if (ca == null){
      String msg = "Could not find or create the assertion: " + formula + " in context: " + ctx;
      log.warn(msg);
      throw new InvalidFormulaInContextException(msg);
    }
    return KBObjectFactory.get(ca, FactImpl.class);  
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.Fact#getArgument(int)
   */
  @Override
  public <O> O getArgument(int getPos) throws KBTypeException, CreateException  {
    CycAssertion ca = (CycAssertion) this.getCore();
    CycList<Object> g = ca.getGaf().getArgs();
    Object o = g.get(getPos);
    return KBObjectImpl.<O> checkAndCastObject(o);
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.Fact#delete()
   */
  @Override
  public void delete() throws DeleteException {
    CycAssertion ca = (CycAssertion) core;
    FormulaSentence sentence = ca.getGaf();
    CycObject mt = ca.getMt();
    try {
      getAccess().getUnassertTool().unassertGaf(sentence, mt, true,
          KBAPIConfiguration.getShouldTranscriptOperations());
      setIsValid(false);
    } catch (CycConnectionException ex) {
      throw new KBApiRuntimeException("Couldn't delete the fact: "
          + core.toString(), ex);
    }
    try {
      if (findAssertion(sentence, getAccess().getObjectTool().makeELMt(mt)) instanceof CycAssertion) {
        throw new DeleteException("Unable to delete Fact " + sentence + " in "
            + mt);
      }
    } catch (CycConnectionException ex) {
      throw new KBApiRuntimeException("Couldn't delete the fact: "
          + core.toString(), ex);
    } catch (CycApiException ex) {
      throw new KBApiRuntimeException("Couldn't delete the fact: "
          + core.toString(), ex);
    }
  }

  // If this is a meta-fact, then the toString will not have the nice
  // "(ist ctx fact")
  /* (non-Javadoc)
   * @see com.cyc.kb.Fact#toString()
   */
  @Override
  public String toString() {
    CycAssertion ca = (CycAssertion) core;
    String retStr;

    if (ca.isGaf()) {
      retStr = "(ist " + ca.getMt().toString() + " " + ca.getGaf().toString()
          + ")";
      log.trace("String API value of CycAssertion: {}", ca.stringApiValue());
    } else {
      retStr = core.toString();
    }

    return retStr;
  }

  /**
   * This not part of the public, supported KB API. Check that the candidate core object is
   * valid Ground Automic Formula. In the CycKB the object would be valid
   * #$CycLGAFAssertion
   * 
   * Internally this method checks if the the <code>cycObject</code> is an
   * instance of CycAssertion and it is fully grounded, i.e. has not open
   * variables.
   * 
   * @see StandardKBObject#StandardKBObject(CycObject)  for more comments
   */
  @Override
  protected boolean isValidCore(CycObject cycObject) {
    return (cycObject instanceof CycAssertion) && ((CycAssertion) cycObject).isGaf();
  }

  /**
   * Return the KBCollection as a KBObject of the Cyc term that 
   * underlies this class. 
   * 
   * @return KBCollectionImpl.get("#$CycLGAFAssertion");
   */
  @Override
  public KBObject getType() {
    return getClassType();
  }
  
  /**
   * Return the KBCollection as a KBObject of the Cyc term that 
   * underlies this class. 
   * 
   * @return KBCollectionImpl.get("#$CycLGAFAssertion");
   */
  public static KBObject getClassType() {
    try {
      return KBCollectionImpl.get(getClassTypeString());
    } catch (KBApiException kae) {
      throw new KBApiRuntimeException(kae.getMessage(), kae);
    }
  }
  
  @Override
  String getTypeString() {
    return getClassTypeString();
  }
  
  static String getClassTypeString() {
    return "#$CycLGAFAssertion";
  }
}
