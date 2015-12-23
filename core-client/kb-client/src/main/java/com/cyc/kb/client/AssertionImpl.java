package com.cyc.kb.client;

/*
 * #%L
 * File: AssertionImpl.java
 * Project: KB Client
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

import com.cyc.base.CycAccess;
import com.cyc.base.exception.CycApiException;
import com.cyc.base.exception.CycConnectionException;
import com.cyc.base.cycobject.CycAssertion;
import com.cyc.base.cycobject.CycList;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.CycSymbol;
import com.cyc.base.cycobject.ElMt;
import com.cyc.base.cycobject.FormulaSentence;
import com.cyc.baseclient.CycObjectFactory;
import com.cyc.baseclient.cycobject.CycFormulaSentence;
import com.cyc.baseclient.cycobject.CycSymbolImpl;
import com.cyc.baseclient.cycobject.DefaultCycObject;
import com.cyc.kb.Assertion;
import com.cyc.kb.Context;
import com.cyc.kb.Assertion.Direction;
import com.cyc.kb.Assertion.Strength;
import com.cyc.kb.KbObject;
import com.cyc.kb.Sentence;
import com.cyc.kb.client.config.KbConfiguration;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.DeleteException;
import com.cyc.kb.exception.InvalidFormulaInContextException;
import com.cyc.kb.exception.KbException;
import com.cyc.kb.exception.KbRuntimeException;
import com.cyc.kb.exception.KbObjectNotFoundException;
import com.cyc.kb.exception.KbTypeException;
import java.util.ArrayList;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An Assertion object is a facade for a
 * <code>#$CycLAssertion</code> in Cyc KB. An assertion is a semantically well
 * formed Sentence, in a specific Context.
 * <p>
 * Sub-classes include Fact and Rule.
 *
 * @author Vijay Raj
 * @version $Id: AssertionImpl.java 163117 2015-12-11 00:27:39Z nwinant $
 * @since 1.0
 */
public class AssertionImpl extends StandardKbObject implements Assertion {

  private static final Logger log = LoggerFactory.getLogger(AssertionImpl.class.getCanonicalName());

  /**
   * This not part of the public, supported KB API. default constructor, calls the default
   * super constructor
   *
   * @throws KbRuntimeException if there is a problem connecting to Cyc
   */
  AssertionImpl() {
    super();
  }
  
  /**
   * This not part of the public, supported KB API. an implementation-dependent constructor
   * <p>
   * Return a new
   * <code>Assertion</code> based on the existing CycAssertion object
   * <code>cycAssert</code>. The KB assertion underlying
   * <code>cycAssert</code> must already be a #$CycLAssertion.
   * 
   * It is used when the result of query is a CycObject and is known to be or
   * requested to be cast as an Assertion.
   *
   * @param cycAssert	the CycObject wrapped by Assertion. The constructor
   * verifies that the CycObject is an #$CycLAssertion.
   * 
   * @throws KbTypeException  if cycAssert (which already exists) is not 
   * a #$CycLAssertion
   */
  // We have made this public for the reflection mechanism to see this class.
  // If made package private, reflection of getConstructor(CycObject.class) fails
  @Deprecated
  AssertionImpl(CycObject cycAssert) throws KbTypeException {
    super(cycAssert);
  }
  
  /*
   * This not part of the public, supported KB API. an implementation-dependent constructor
   * <p>
   * This constructor retrieves the an assertion based on the id <code>hlid</code>
   * and verifies that it matches <code>cycAssertStr</code>.
   * 
   * @param cycAssertStr  the string representation of the assertion
   * @param hlid          the id of the assertion
   * @throws KBApiException 
   * @todo The cycAssertStr has to match exactly to the object.toString(), probably
   * we need to relax string matching, by ignoring white-spaces.
   */
  // We will have a constructor with just hlid
  /*
  public Assertion(String cycAssertStr, String hlid) throws KBApiException {
    super(cycAssertStr, hlid);
  }
  */ 
 
  
  /**
   * Get the <code>Assertion</code> object that corresponds to
   * <code>cycAssert</code>. Throws exceptions if the object isn't in the KB, or if
   * it's not already an assertion. (Currently cycAssert is only checked to be an 
   * instance of CycAssertion. )
   * 
   * @param cycAssert candidate assertion object
   * 
   * @return an Assertion based on cycAssert
   * 
   * @throws KbTypeException if the cycAssert is not an instance of assertion
   * @throws CreateException 
   */
  @Deprecated
  public static Assertion get(CycObject cycAssert) throws KbTypeException, CreateException {
    return KbObjectFactory.get(cycAssert, AssertionImpl.class);
  }

  /**
   * Get the <code>Assertion</code> object that corresponds to
   * <code>hlid</code>. Throws exceptions if the <code>hlid</code> isn't in the KB, or if
   * it's not already an assertion.
   * 
   * NOTE: The get (String ) factory methods in other classes find objects based 
   * on <code>hlid</code> or string representation of the object. They use nameOrId
   * as the string variable name. But this method finds objects based on
   * <code>hlid</code> alone. This is because it is much more common and easier
   * to find assertions based on two strings, formula and context. 
   * 
   * @param hlid  the <code>hlid</code> corresponding to the assertion in the KB
   * 
   * @return an Assertion based on <code>hlid</code>
   * 
   * @throws KbTypeException  if assertion based on <code>hlid</code> is not an instance of assertion
   * @throws CreateException 
   */
  @SuppressWarnings("deprecation")
public static Assertion get(String hlid) throws KbTypeException, CreateException {
    Object o = null;
    // NOTE: The StandardKBObject was too geared towards Term (Constant, NAT) creation
    // Did not want to overload that with assertion creation as well. 
    // Also the get method here takes only hlid. For a factory method that takes String to 
    // find an assertion, see get(String formulaStr, String ctxStr)
    try {
      o = DefaultCycObject.fromPossibleCompactExternalId(hlid, getStaticAccess());
    } catch (CycConnectionException e){
      throw new KbRuntimeException(e.getMessage(), e);
    }
    
    if (o instanceof CycAssertion) {
      log.debug("Found assertion: {} using HLID: {}", o, hlid);
      return KbObjectFactory.get((CycObject)o, AssertionImpl.class);
    } else {
      String msg = "Could not find any Assertion with hlid: " + hlid + " in the KB.";
      log.error(msg);
      throw new KbObjectNotFoundException(msg);
    }
  }
  
  /**
   * Get the <code>Assertion</code> object that corresponds to
   * <code>formulaStr</code> in the context corresponding to <code>ctxStr</code>. Throws exceptions 
   * if no such formula
   * is found in the specified context in the KB, or if
   * it's not already an assertion. 
   * 
   * @param formulaStr string representation of the formula to be found
   * @param ctxStr     string representation of the context of the formula
   * 
   * @return an Assertion based on <code>formulaStr</code> and <code>ctxStr</code>
   * @throws CreateException 
   * @throws KbTypeException 
   * @throws KbObjectNotFoundException if no Assertion with the given formula 
   * string is found in the context
   */
  
  @SuppressWarnings("deprecation")
public static Assertion get(String formulaStr, String ctxStr) throws KbTypeException, CreateException {
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
      throw new KbObjectNotFoundException(msg);
    }
    return KbObjectFactory.get(ca, AssertionImpl.class);
  }

  /**
   * Get the <code>Assertion</code> object that corresponds to
   * <code>formula</code> in the context <code>ctx</code>. Throws exceptions if no such formula
   * is found in the specified context in the KB, or if
   * it's not already an assertion.
   * 
   * @param formula formula to be found
   * @param ctx     context of the formula
   * 
   * @return an Assertion based on formula and context
   * @throws CreateException 
   * @throws KbTypeException 
   * @throws KbObjectNotFoundException if no assertion with the given formula 
   * is found in the context
   */
  @SuppressWarnings("deprecation")
  public static Assertion get(Sentence formula, Context ctx) throws KbTypeException, CreateException {
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
    
    if (ca == null){
      String msg = "Could not find the assertion: " + formula + " in context: " + ctx;
      throw new KbObjectNotFoundException(msg);
    }
    return KbObjectFactory.get(ca, AssertionImpl.class);
  }
  
  /**
   * Finds or creates the <code>Assertion</code> object that corresponds to
   * <code>formulaStr</code> in the default assertion context. Tries to assert if no such formula
   * is found in the specified context in the KB. Throws an exception if it is unable
   * to make such an assertion.
   * 
   * Strength is set to Strength.DEFAULT by default
   * Direction is set to Direction.FORWARD by default 
   *  
   * @param formulaStr the string representation of the formula to be found or created
   * 
   * @return an Assertion based on <code>formulaStr</code> and the default assertion context
   * 
   * @throws CreateException 
   * @throws KbTypeException 
   * @throws InvalidFormulaInContextException if no assertion with the given formula 
   * string is found or created in the context
   */
  public static Assertion findOrCreate(String formulaStr) throws KbTypeException, CreateException {
	  return AssertionImpl.findOrCreate(formulaStr, KbConfiguration.getDefaultContext().forAssertion().toString());
  }
  
  /**
   * Finds or creates the <code>Assertion</code> object that corresponds to
   * <code>formulaStr</code> in the context corresponding to <code>ctxStr</code>. Tries to assert if no such formula
   * is found in the specified context in the KB. Throws an exception if it is unable
   * to make such an assertion.
   * 
   * Strength is set to Strength.DEFAULT by default
   * Direction is set to Direction.FORWARD by default 
   *  
   * @param formulaStr the string representation of the formula to be found or created
   * @param ctxStr     the string representation of the context of the formula
   * 
   * @return an Assertion based on <code>formulaStr</code> and <code>ctxStr</code>
   * 
   * @throws CreateException 
   * @throws KbTypeException 
   * @throws InvalidFormulaInContextException if no assertion with the given formula 
   * string is found or created in the context
   */
  public static Assertion findOrCreate(String formulaStr, String ctxStr) 
      throws KbTypeException, CreateException {
	  return AssertionImpl.findOrCreate(formulaStr, ctxStr, Strength.AUTO, Direction.AUTO);
  }
  
  /**
   * Finds or creates the <code>Assertion</code> object that corresponds to
   * <code>formulaStr</code> in the context corresponding to <code>ctxStr</code>. Tries to assert if no such formula
   * is found in the specified context in the KB. Throws an exception if it is unable
   * to make such an assertion.
   * 
   * @param formulaStr the string representation of the formula to be found or created
   * @param ctxStr     the string representation of the context of the formula
   * @param s		   the strength of the assertion
   * @param d          the direction of the assertion  
   * 
   * @return an Assertion based on formula and context
   * 
   * @throws CreateException 
   * @throws KbTypeException 
   * @throws InvalidFormulaInContextException if no assertion with the given formula 
   * string is found or created in the context
   */
  
  @SuppressWarnings("deprecation")
public static Assertion findOrCreate(String formulaStr, String ctxStr, Strength s, Direction d) 
      throws KbTypeException, CreateException {
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
    return KbObjectFactory.get(ca, AssertionImpl.class);
  }

  /**
   * Finds or creates the <code>Assertion</code> object that corresponds to
   * <code>formula</code> in <code>ctx</code>. Tries to assert if no such formula
   * is found in the specified context in the KB. Throws an exception if it is unable
   * to make such an assertion.
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
   * @return an Assertion based on formula and context
   * @throws CreateException 
   * @throws KbTypeException 
   * 
   * @throws InvalidFormulaInContextException if no assertion with the given formula 
   * string is found or created in the context
   */
  public static Assertion findOrCreate(Sentence formula, Context ctx) throws KbTypeException, CreateException 
       {
	  return AssertionImpl.findOrCreate(formula, ctx, Strength.AUTO, Direction.AUTO);
  }
  
  /**
   * Find or creates the <code>Assertion</code> object that corresponds to 
   * <code>formula</code> in the default assertion context (@link KBAPIConfiguration}.
   * Tries to assert if no such formula is found in the KB.  Throws an exception
   * if it is unable to make such an assertion.
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
   * @return an Assertion based on the formula in the default assertion context
   * 
   * @throws KbTypeException
   * @throws CreateException
   */
  public static Assertion findOrCreate(Sentence formula) throws KbTypeException, CreateException
  {
	  return AssertionImpl.findOrCreate(formula, KbConfiguration.getDefaultContext().forAssertion());
  }
  
  /**
   * Finds or creates the <code>Assertion</code> object that corresponds to
   * <code>formula</code> in <code>ctx</code>. Tries to assert if no such formula
   * is found in the specified context in the KB. Throws an exception if it is unable
   * to make such an assertion.
   * 
   * NOTE: All findOrCreate factory methods that take CycObject do not eventually create 
   * any new object in the assertion because (most of) the CycObjects can only be built 
   * based on objects in the KB. Some exceptions are Sentences, Variables and Symbols.
   * NAUTs are also not in the KB, but in the API they are effectively treated as being in 
   * the KB. 
   * 
   * @param formula the formula to be found or created
   * @param ctx     the context of the formula
   * @param s		the strength of the assertion
   * @param d       the direction of the assertion  
   * 
   * @return an Assertion based on formula and context
   * @throws CreateException 
   * @throws KbTypeException 
   * 
   * @throws InvalidFormulaInContextException   if no assertion with the given formula 
   * string is found or created in the context
   * 
   */
  @SuppressWarnings("deprecation")
  public static Assertion findOrCreate(Sentence formula, Context ctx, Strength s, Direction d) 
      throws KbTypeException, CreateException {
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
      log.error(msg);
      throw new InvalidFormulaInContextException(msg);
    }
    return KbObjectFactory.get(ca, AssertionImpl.class);  
  }

  @Override
  public Sentence getFormula() {
    try {
      FormulaSentence assertionFormula = ((CycAssertion)this.getCore()).getELFormula(getAccess());
      return new SentenceImpl(assertionFormula);
    } catch (CycApiException ex) {
      log.error(ex.getMessage());
      throw new KbRuntimeException(ex.getMessage(), ex);
    } catch (CycConnectionException ex) {
      log.error(ex.getMessage());
      throw new KbRuntimeException(ex.getMessage(), ex);
    } catch (KbTypeException ex) {
      log.error(ex.getMessage());
      throw new KbRuntimeException(ex.getMessage(), ex);
    } catch (CreateException ex) {
      throw new KbRuntimeException(ex.getMessage(), ex);
    }
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.Assertion#getContext()
   */
  @Override
  @SuppressWarnings("deprecation")
  public Context getContext() {
    CycAssertion ca = (CycAssertion) core;
    ContextImpl ctx;
    try {
      ctx = ContextImpl.get(ca.getMt());
    } catch (CreateException te) {
      // The assertion is already created, that means the context should
      // already be there. 
      throw new KbRuntimeException(te.getMessage(), te);
    } catch (KbTypeException te) {
      // The assertion is already created, that means the context should
      // already be there.
      throw new KbRuntimeException(te.getMessage(), te);
    }
    return ctx;
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.Assertion#getSupportingAssertions()
   */
  @Override
  public Collection<Assertion> getSupportingAssertions() throws KbTypeException, CreateException {
    try {

      //String command = SublConstants.getInstance().assertionAssertedAssertionSupports.buildCommand(this.getCore());
      //CycList<?> result = getAccess().converse().converseList(command);
      CycList<?> result = SublConstants.getInstance().assertionAssertedAssertionSupports.eval(getAccess(), this.getCore());
      Collection<Assertion> asserts = new ArrayList<Assertion>();
      for (Object o : result) {
        asserts.add(AssertionImpl.get((CycObject) o));
      }
      return asserts;
    } catch (CycConnectionException e) {
      throw new KbRuntimeException(e);
    }
  }

  /*
   *
   * @return the collection of all assertions that directly or indirectly
   * support this assertion. Effectively this is a transitive version of
   * {@link getSupportingAssertions}.
   */
  /*  this needs to be completed, and it should be basically the same as getSupportingAssertions with a high depth limit.
   public Collection<Assertion> getAllSupportingAssertions () {
   throw new UnsupportedOperationException();
   }
   */
  
  /* (non-Javadoc)
   * @see com.cyc.kb.Assertion#isDeducedAssertion()
   */
  @Override
  public boolean isDeducedAssertion() {
    try {
      String command = SublConstants.getInstance().deducedAssertionQ.buildCommand(this.getCore());
      return getAccess().converse().converseBoolean(command);
    } catch (CycConnectionException e) {
      throw new KbRuntimeException(e);
    }
  }
  
  /* (non-Javadoc)
   * @see com.cyc.kb.Assertion#isGroundAtomicFormula()
   */
  @Override
  public boolean isGroundAtomicFormula() {
	 return ((CycAssertion) this.getCore()).isGaf();
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.Assertion#isAssertedAssertion()
   */
  @Override
  public boolean isAssertedAssertion() {
    try {
      String command = SublConstants.getInstance().assertedAssertionQ.buildCommand(this.getCore());
      return getAccess().converse().converseBoolean(command);
    } catch (CycConnectionException e) {
      throw new KbRuntimeException(e);
    }
  }
  
  /* (non-Javadoc)
   * @see com.cyc.kb.Assertion#getDirection()
   */
  @Override
  public Direction getDirection() {
    try {
      String command = SublConstants.getInstance().assertionDirection.buildCommand(this.getCore());
      CycObject co = getAccess().converse().converseCycObject(command);
      if (co instanceof CycSymbol){
          CycSymbol cs = (CycSymbol) co;
          if (cs.equals(new CycSymbolImpl(":BACKWARD"))){
              return Direction.BACKWARD;
          } else if (cs.equals(new CycSymbolImpl(":FORWARD"))){
              return Direction.FORWARD;
          } else {
            // This should never happen for CycAssertion, so a runtime exception
              throw new KbRuntimeException("Unknown or :CODE Direction");
          }
      } else {
          throw new KbRuntimeException("Unknown Direction");
      }
    } catch (CycConnectionException e) {
      throw new KbRuntimeException(e.getMessage(), e);
    }
  }

  @Override
  public Assertion changeDirection(Direction d) throws KbException {
    try {
      
      if (this.getDirection().equals(d)) {
        log.info("The input direction " + d + " is the same as the assertion direction. Nothing to do.");
        return this;
      }
      
      if (KbConfiguration.getCurrentCyclist() == null) {
        throw new KbException("Set the Cyclist using KBAPIConfiguration.setCurrentCyclist()");
      }
      
      String command = "(clet "
              + "((*the-cyclist* " + KbConfiguration.getCurrentCyclist().stringApiValue() + ")) "
              + "(" + SublConstants.getInstance().keChangeAssertionDirection.stringApiValue() + " " + this.getCore().stringApiValue() + " :" + d.name() + "))";
      CycObject co = getAccess().converse().converseCycObject(command);
      if (co instanceof CycAssertion){
        log.debug("Changed the assertion direction of " + this + " to: " + d);
        CycAssertion ca = (CycAssertion) co;
        return this;
      } else {
          throw new KbRuntimeException("Failed to change the direction of the assertion: " + this);
      }
    } catch (CycConnectionException e) {
      throw new KbRuntimeException(e.getMessage(), e);
    }
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.Assertion#delete(boolean)
   */
  @Deprecated
  public void delete(boolean force) throws DeleteException{
    if (!force) {
      delete();
    } else {
      try {
        getAccess().getUnassertTool().blastAssertion((CycAssertion)(this.getCore()), true, KbConfiguration.getShouldTranscriptOperations());
        setIsValid(false);
      } catch (CycConnectionException ex) {
        log.warn("Unable to forcefully delete assertion {}", this);
        throw new KbRuntimeException("Couldn't forcefully delete fact: " + core.toString(), ex);
      } catch (CycApiException ex) {
        log.warn("Unable to forcefully delete assertion {}", this);
        throw new KbRuntimeException("Couldn't forcefully delete fact: " + core.toString(), ex);
      }
  }
  }
  /* (non-Javadoc)
   * @see com.cyc.kb.Assertion#delete()
   */
  @Override
  public void delete() throws DeleteException {
    CycAssertion ca = (CycAssertion) core;
    try {
      getAccess().getUnassertTool().unassertAssertion(ca, true, KbConfiguration.getShouldTranscriptOperations());
      setIsValid(false);
    } catch (CycConnectionException ex) {
      throw new KbRuntimeException(
              "Couldn't delete the fact: " + core.toString(), ex);
    }
    try {
      if (findAssertion(ca.getELFormula(getAccess()), 
          getAccess().getObjectTool().makeELMt(ca.getMt())) instanceof CycAssertion) {
        log.error("Unable to delete assertion: {}", ca);
        throw new DeleteException("Unable to delete assertion: " + ca);
      }
    } catch (CycConnectionException ex) {
      throw new KbRuntimeException("Couldn't delete the fact: " + core.toString(), ex);
    } 
  }

    
  /**
   * This not part of the public, supported KB API. Check that the candidate core
   * object is valid CycAssertion. In the CycKB the object would be
   * valid #$CycLAssertion
   * 
   * @return true if the core is valid for a given class of KBObject
   * @see StandardKBObject#isValidCore(CycObject)  for more comments
   */
  @Override
  protected boolean isValidCore(CycObject cycObject) {
    return cycObject instanceof CycAssertion;
  }
    
  /**
   * Return the KBCollection as a KBObject of the Cyc term that 
   * underlies this class. 
   * 
   * @return KBCollectionImpl.get("#$CycLAssertion");
   */
  @Override
  public KbObject getType() {
    return getClassType();
  }
  
  /**
   * Return the KBCollection as a KBObject of the Cyc term that 
   * underlies this class. 
   * 
   * @return KBCollectionImpl.get("#$CycLAssertion");
   */
  public static KbObject getClassType() {
    try {
      return KbCollectionImpl.get(getClassTypeString());
    } catch (KbException kae) {
      throw new KbRuntimeException(kae.getMessage(), kae);
    }
  }

  @Override
  String getTypeString() {
    return getClassTypeString();
  }
  
  static String getClassTypeString() {
    return "#$CycLAssertion";
  }
    
  /**
   * find or create an assertion using
   * <code>factSentence</code> in the context
   * <code>ctx</code>
   * <p>
   *
   * @param fact	the CycL sentence to be asserted
   * @param ctx		the context where the fact will be asserted
   * @param s   	the strength of the assertion if asserted
   * @param d		the strength of the assertion if asserted
   */
  // @TODO: Clarify the strength and direction if "found". See SEMAPI-43
  @SuppressWarnings("deprecation")
static CycAssertion assertSentence(FormulaSentence factSentence, Context ctx, Strength s, Direction d) {
	  try {
      CycAssertion ca = findAssertion(factSentence, ContextImpl.asELMt(ctx));
      if (d == null || d.equals(Direction.AUTO)){
    	  d = Direction.FORWARD;
      }
      if (s == null || s.equals(Strength.AUTO)){
    	  s = Strength.DEFAULT;
      }
      if (ca == null) {
        log.debug("Attempting to assert the formula: {} in context {}", factSentence, ctx);
        getStaticAccess().getAssertTool().assertSentence(factSentence.stringApiValue(), 
            getStaticAccess().getObjectTool().makeELMt(ctx.getCore()),
        		":"+s.name(), ":"+d.name(), true, KbConfiguration.getShouldTranscriptOperations());
        ca = findAssertion(factSentence, ContextImpl.asELMt(ctx));
      }
      return ca;
    } catch (CycConnectionException ex) {
      throw new KbRuntimeException(ex.getMessage(), ex);
    } 
  }

  /**
   * find or create a fact using factStr in the context represented by ctxStr
   * <p>
   *
   * @param factStr	the CycL string of the fact to be asserted
   * @param ctxStr	the string representing the context where the fact will be
   * asserted
   * @param s   	the strength of the assertion if asserted
   * @param d		the strength of the assertion if asserted
   *
   * @throws CreateException 
   * @throws KbTypeException 
   * 
   * @TODO: Clarify the strength and direction if "found". See SEMAPI-43
   */
  static CycAssertion assertSentence(String factStr, String ctxStr, Strength s, Direction d) 
      throws KbTypeException, CreateException {
    try {
      FormulaSentence factSentence = CycFormulaSentence.makeCycSentence(getStaticAccess(), factStr);
      Context ctx = ContextImpl.get(ctxStr);
      
      return assertSentence(factSentence, ctx, s, d);
    } catch (CycConnectionException exception) {
      throw new KbRuntimeException(exception.getMessage(), exception);
    }
  }

  //TODO preserve this and make it work
  /*
   * private CycAssertion findAssertion(Context ctx, Object...argList) throws Exception {
   * List<Object> argListArray = new ArrayList<Object> (Arrays.asList(argList));
   *
   * // TODO: handle all types of arguments in the constructAssertion
   * // It could be anything from String, or CycL in string form, or SemanticObjects
   * String factString = constructFact(argListArray);
   * String cyclifiedFactString = cyc.cyclifyString(factString);
   *
   * return findAssertion(ctx.stringApiValue(), cyclifiedFactString);
   * }
   */
  /**
   * finds an assertion with
   * <code>factSentence</code> in the context
   * <code>ctx</code>. In contrast to many other methods, this method requires
   * that the assertion be found in the specified context, not merely in some
   * context visible from the specified context.
   * <p>
   *
   * @param factSentenceStr	the CycL string of the sentence to be asserted
   * @param ctxStr	the string representing the context
   * 
   * @return the assertion, if it exists. Returns null if the assertion isn't in the KB.
   *
   */
  static CycAssertion findAssertion(String factSentenceStr, String ctxStr) {
    try {
      CycAccess cyc = getStaticAccess();
      FormulaSentence factFormulaSentence = CycFormulaSentence.makeCycSentence(cyc, factSentenceStr);
      return findAssertion(factFormulaSentence, cyc.getObjectTool().makeELMt(cyc.cyclifyString(ctxStr)));
    } catch (CycApiException ex) {
      // throw new KBApiException(ex);
      return null;
    } catch (CycConnectionException ex) {
      throw new KbRuntimeException(ex.getMessage(), ex);
    }
  }

  /**
   * finds an assertion with
   * <code>factSentence</code> in the context
   * <code>ctx</code>. In contrast to many other methods, this method requires
   * that the assertion be found in the specified context, not merely in some
   * context visible from the specified context.
   * <p>
   *
   * 
   * @param factSentence	the CycL string of the fact to be asserted
   * @param ctx				the string representing the context
   * 
   * @return the assertion, if it exists. Returns null if the assertion isn't in the KB.
   * 
   * @TODO Major issues.
   * 1. This asks with-inference-mt-relevance : which means the returned assertions are visible in ctx
   * not necessarily asserted in ctx
   * 2. This calls find-assertion-cycl which returns some assertion if multiple assertions with the same
   * formula and same visibility are found
   * Need to decide what we want to do
   */
  static CycAssertion findAssertion(FormulaSentence factSentence, ElMt ctx) {
          
    String command = "(" + SublConstants.getInstance().withInferenceMtRelevance.stringApiValue() + " " + ctx.stringApiValue() 
            + " (" + SublConstants.getInstance().findAssertionCycl.stringApiValue() + " " + factSentence.stringApiValue() + " ))";
    log.trace("Find Fact: {}", command);
    Object res;
    try {
      res = getStaticAccess().converse().converseObject(command);
    } catch (CycApiException ex) {
      throw new KbRuntimeException(ex.getMessage(), ex);
    } catch (CycConnectionException ex) {
      throw new KbRuntimeException(ex.getMessage(), ex);
    }
    log.trace("Fact response: {}", res);
    
    if (res instanceof CycAssertion) {
      log.debug ("Found assertion {} using formula sentence: {} in mt: {}", res, factSentence, ctx);
      return (CycAssertion) res;
    } else if (res.equals(CycObjectFactory.nil)) {
      String msg = "Couldn't find fact: '" + factSentence + "' in Context: '" + ctx + "'";
      log.trace(msg);
      // throw new KBObjectNotFoundException(msg);
      return null;
    } else {
      throw new KbRuntimeException(
          "Unknown error in converseObject result parsing.");
    }
   
  }
}
