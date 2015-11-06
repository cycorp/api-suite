/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cyc.kb.client;

/*
 * #%L
 * File: TypeFactImpl.java
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

import com.cyc.base.cycobject.CycAssertion;
import com.cyc.base.cycobject.CycObject;
import com.cyc.kb.Context;
import com.cyc.kb.KBCollection;
import com.cyc.kb.KBObject;
import com.cyc.kb.KBPredicate;
import com.cyc.kb.Quantifier;
import com.cyc.kb.TypeFact;
import com.cyc.kb.exception.KBApiException;
import com.cyc.kb.quant.ForAllQuantifiedInstanceRestrictedVariable;
import com.cyc.kb.quant.QuantifiedInstanceRestrictedVariable;
import com.cyc.kb.quant.ThereExistsQuantifiedInstanceRestrictedVariable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * THIS CLASS IS NOT SUPPORTED IN KB API 1.0.
 * 
 * @author vijay
 */
public class TypeFactImpl extends FactImpl implements TypeFact {

  private static final Logger log = LoggerFactory.getLogger(TypeFactImpl.class.getCanonicalName());
  
  List<Object> modifiedTypeLevelArguments = new ArrayList<Object>();

  private static KBPredicate relationAllExists = null;
  private static KBPredicate relationExistsAll = null;
  static {
    try {
      relationAllExists = KBPredicateImpl.get("relationAllExists");
      relationExistsAll = KBPredicateImpl.get("relationExistsAll");
    } catch (Exception e){
    }
  }

  @Deprecated
  public TypeFactImpl(CycObject cycAssert) throws KBApiException {
    super(cycAssert);
    try {
      KBPredicate p = this.<KBPredicate>getArgument(0);
      if (!p.isInstanceOf(KBCollectionImpl.get("RuleMacroPredicate"))) {
        String msg = "The predicate \"" + ((CycAssertion)cycAssert).getGaf().getOperator().toString() 
                + "\" is not an instance of #$RuleMacroPredicate.";
        log.trace(msg);
        throw new KBApiException(msg);
      }
    } catch (Exception e) {
      throw new KBApiException(e.getMessage(), e);
    }
    identifyTypeObjectsFromAssertion((CycAssertion)core);
  }

  public TypeFactImpl(Context ctx, Object... argList) throws KBApiException {
    this(ctx, identifyRelationForQuantifiers(argList));
  }
  
  private TypeFactImpl(Context ctx, PredAndArgs content) throws KBApiException {
    super(false, ctx, content.getPredicate(), content.getArgs());
    identifyTypeObjectsFromAssertion((CycAssertion)core);
  }
  
  private void identifyTypeObjectsFromAssertion(CycAssertion ca) throws KBApiException {
    KBPredicate rmp = this.<KBPredicate>getArgument(0);
    KBPredicate p = this.<KBPredicate>getArgument(1);
    KBCollection c1 = this.<KBCollection>getArgument(2);
    KBCollection c2 = this.<KBCollection>getArgument(3);
    this.addModifiedTypeLevelArguments(p);
    if (rmp.equals(relationAllExists)){
      this.addModifiedTypeLevelArguments(new ForAllQuantifiedInstanceRestrictedVariable(c1));
      this.addModifiedTypeLevelArguments(new ThereExistsQuantifiedInstanceRestrictedVariable(c2));
    } else if (rmp.equals(relationExistsAll)){
      this.addModifiedTypeLevelArguments(new ThereExistsQuantifiedInstanceRestrictedVariable(c1));
      this.addModifiedTypeLevelArguments(new ForAllQuantifiedInstanceRestrictedVariable(c2));      
    }
  }
  
  private static PredAndArgs identifyRelationForQuantifiers(Object... argList) throws KBApiException {

    List<Object> argInputList = new ArrayList<Object>();
    argInputList.addAll(Arrays.asList(argList));
    
    String helpMessage = "The TypeFact constructor can handle the following formats:\n"
              + "1. <RULE_MACRO_PRED BIN_PRED OBJ1 OBJ2>\n"
              + "2. <BIN_PRED OBJ1 OBJ2> one of the two objects needs a quantifier.";
    if (argInputList.size() > 4){
      throw new KBApiException("Got more than four arguments.\n "
              + helpMessage);
    } else if (argInputList.size() == 4) {
      Object isItRMP = argInputList.get(0);
      KBPredicateImpl rmp = null;
      if (isItRMP instanceof KBPredicate){
        rmp = KBPredicateImpl.class.cast(isItRMP);
        if (!rmp.isInstanceOf(KBCollectionImpl.get("RuleMacroPredicate"))) {
          throw new KBApiException("When four arguments are supplied, first argument "
                  + "has to be a #$RuleMacroPredicate. Got: " + rmp + " instead."
                  + helpMessage);
        }
      }
      
      // We got a rule macro GAF, no transformation required.
      return new PredAndArgs(argInputList);
    } else if (argInputList.size() == 3) {
      if (!(argInputList.get(1) instanceof QuantifiedInstanceRestrictedVariable
            || argInputList.get(2) instanceof QuantifiedInstanceRestrictedVariable)){
        throw new KBApiException("Can not make a TypeFact when a "
                + "QuantifiedRestrictedVariable is not present with three arguments.\n"
                + helpMessage);
      }
    }
    
    Object isItP = argInputList.get(0);
    KBPredicateImpl p = null;
    if (isItP instanceof KBPredicate){
       p = KBPredicateImpl.class.cast(argInputList.get(0));
       if (p.getArity() != 2){
         throw new KBApiException("Currently TypeFact expects a #$BinaryPredicate");
       }
    } else {
      throw new KBApiException("First argument of " + argList + ", " + isItP + ", is not a #$Predicate");
    }
    Object subject = argInputList.get(1);
    Object object = argInputList.get(2);
    
    int count = 0;
    for (Object o : argInputList) {
      if (o instanceof QuantifiedInstanceRestrictedVariable) {
        System.out.println("Arg " + count + ": " + ((QuantifiedInstanceRestrictedVariable) o).getCollection());
      } else if (o instanceof KBObject) {
        System.out.println("Arg " + count + ": " + ((KBObject) o).getCore());
      } else {
        System.out.println("Arg " + count + ": " + o);
      }
      count++;
    }
    
    List<Object> argOutputList = new ArrayList<Object>();

    Quantifier forAll = new QuantifierImpl("forAll");
    Quantifier thereExists = new QuantifierImpl("thereExists");

    if (subject instanceof QuantifiedInstanceRestrictedVariable
            && object instanceof QuantifiedInstanceRestrictedVariable) {

      QuantifiedInstanceRestrictedVariable subject1 = QuantifiedInstanceRestrictedVariable.class.cast(subject);
      QuantifiedInstanceRestrictedVariable object1 = QuantifiedInstanceRestrictedVariable.class.cast(object);

      if (subject1.getQuantifier().equals(forAll)) {
        if (object1.getQuantifier().equals(forAll)) {
          argOutputList.add(0, KBPredicateImpl.get("relationAllAll"));
        } else if (object1.getQuantifier().equals(thereExists)) {
          argOutputList.add(0, KBPredicateImpl.get("relationAllExists"));
        }
      } else if (subject1.getQuantifier().equals(thereExists)) {
        if (object1.getQuantifier().equals(forAll)) {
          argOutputList.add(0, KBPredicateImpl.get("relationExistsAll"));
        } else if (object1.getQuantifier().equals(thereExists)) {
          argOutputList.add(0, KBPredicateImpl.get("relationExistsExists"));
        }
      }

      argOutputList.add((Object) p);
      argOutputList.add(subject1.getCollection());
      argOutputList.add(object1.getCollection());

    } else if (subject instanceof QuantifiedInstanceRestrictedVariable) {

      QuantifiedInstanceRestrictedVariable subject1 = QuantifiedInstanceRestrictedVariable.class.cast(subject);

      if (subject1.getQuantifier().equals(forAll)) {
        argOutputList.add(0, KBPredicateImpl.get("relationAllInstance"));
      } else if (subject1.getQuantifier().equals(thereExists)) {
        argOutputList.add(0, KBPredicateImpl.get("relationExistsInstance"));
      }

      argOutputList.add((Object) p);
      argOutputList.add(subject1.getCollection());
      argOutputList.add(object);
    } else if (object instanceof QuantifiedInstanceRestrictedVariable) {

      QuantifiedInstanceRestrictedVariable object1 = QuantifiedInstanceRestrictedVariable.class.cast(object);

      if (object1.getQuantifier().equals(forAll)) {
        argOutputList.add(0, KBPredicateImpl.get("relationInstanceAll"));
      } else if (object1.getQuantifier().equals(thereExists)) {
        argOutputList.add(0, KBPredicateImpl.get("relationInstanceExists"));
      }

      argOutputList.add((Object) p);
      argOutputList.add(subject);
      argOutputList.add(object1.getCollection());
    } 

    return new PredAndArgs(argOutputList);
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.TypeFact#getTypeArgument(int, java.lang.Class)
   */
  @Override
  public <O> O getTypeArgument (int getPos, Class<O> retType) throws KBApiException {
    Object o = this.getModifiedTypeLevelArguments().get(getPos);

    if (QuantifiedInstanceRestrictedVariable.class.isAssignableFrom(retType)
            && o instanceof QuantifiedInstanceRestrictedVariable) {
      return retType.cast(o);
    } else if (KBObjectImpl.class.isAssignableFrom(retType)) {
      return retType.cast(o); // KBObjectFactory.getKBObject((CycObject) o, retType);
    } 
    return null;
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.TypeFact#getModifiedTypeLevelArguments()
   */
  @Override
  public List<Object> getModifiedTypeLevelArguments() {
    return modifiedTypeLevelArguments;
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.TypeFact#setModifiedTypeLevelArguments(java.util.List)
   */
  @Override
  public void setModifiedTypeLevelArguments(List<Object> modifiedTypeLevelArguments) {
    this.modifiedTypeLevelArguments = modifiedTypeLevelArguments;
  }
  
  /* (non-Javadoc)
   * @see com.cyc.kb.TypeFact#addModifiedTypeLevelArguments(java.lang.Object)
   */
  @Override
  public void addModifiedTypeLevelArguments(Object modifiedTypeLevelArgument) {
    this.modifiedTypeLevelArguments.add(modifiedTypeLevelArgument);
  }
  
  private static class PredAndArgs {
    private final KBPredicate p;
    private final Object[] args;
    
    private PredAndArgs(List<Object> args) {
      this.p = (KBPredicate)(args.get(0));
      this.args = args.subList(1, args.size()).toArray();
    }

    /**
     * @return the p
     */
    public KBPredicate getPredicate() {
      return p;
    }

    /**
     * @return the args
     */
    public Object[] getArgs() {
      return args;
    }
    
  }
  
}
