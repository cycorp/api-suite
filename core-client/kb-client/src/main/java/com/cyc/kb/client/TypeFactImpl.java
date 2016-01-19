/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cyc.kb.client;

/*
 * #%L
 * File: TypeFactImpl.java
 * Project: KB Client
 * %%
 * Copyright (C) 2013 - 2016 Cycorp, Inc
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
import com.cyc.kb.KbCollection;
import com.cyc.kb.KbObject;
import com.cyc.kb.KbPredicate;
import com.cyc.kb.Quantifier;
import com.cyc.kb.TypeFact;
import com.cyc.kb.exception.KbException;
import com.cyc.kb.client.quant.ForAllQuantifiedInstanceRestrictedVariable;
import com.cyc.kb.client.quant.QuantifiedInstanceRestrictedVariable;
import com.cyc.kb.client.quant.ThereExistsQuantifiedInstanceRestrictedVariable;

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

  private static KbPredicate relationAllExists = null;
  private static KbPredicate relationExistsAll = null;
  static {
    try {
      relationAllExists = KbPredicateImpl.get("relationAllExists");
      relationExistsAll = KbPredicateImpl.get("relationExistsAll");
    } catch (Exception e){
    }
  }

  @Deprecated
  public TypeFactImpl(CycObject cycAssert) throws KbException {
    super(cycAssert);
    try {
      KbPredicate p = this.<KbPredicate>getArgument(0);
      if (!p.isInstanceOf(KbCollectionImpl.get("RuleMacroPredicate"))) {
        String msg = "The predicate \"" + ((CycAssertion)cycAssert).getGaf().getOperator().toString() 
                + "\" is not an instance of #$RuleMacroPredicate.";
        log.trace(msg);
        throw new KbException(msg);
      }
    } catch (Exception e) {
      throw new KbException(e.getMessage(), e);
    }
    identifyTypeObjectsFromAssertion((CycAssertion)core);
  }

  public TypeFactImpl(Context ctx, Object... argList) throws KbException {
    this(ctx, identifyRelationForQuantifiers(argList));
  }
  
  private TypeFactImpl(Context ctx, PredAndArgs content) throws KbException {
    super(false, ctx, content.getPredicate(), content.getArgs());
    identifyTypeObjectsFromAssertion((CycAssertion)core);
  }
  
  private void identifyTypeObjectsFromAssertion(CycAssertion ca) throws KbException {
    KbPredicate rmp = this.<KbPredicate>getArgument(0);
    KbPredicate p = this.<KbPredicate>getArgument(1);
    KbCollection c1 = this.<KbCollection>getArgument(2);
    KbCollection c2 = this.<KbCollection>getArgument(3);
    this.addModifiedTypeLevelArguments(p);
    if (rmp.equals(relationAllExists)){
      this.addModifiedTypeLevelArguments(new ForAllQuantifiedInstanceRestrictedVariable(c1));
      this.addModifiedTypeLevelArguments(new ThereExistsQuantifiedInstanceRestrictedVariable(c2));
    } else if (rmp.equals(relationExistsAll)){
      this.addModifiedTypeLevelArguments(new ThereExistsQuantifiedInstanceRestrictedVariable(c1));
      this.addModifiedTypeLevelArguments(new ForAllQuantifiedInstanceRestrictedVariable(c2));      
    }
  }
  
  private static PredAndArgs identifyRelationForQuantifiers(Object... argList) throws KbException {

    List<Object> argInputList = new ArrayList<Object>();
    argInputList.addAll(Arrays.asList(argList));
    
    String helpMessage = "The TypeFact constructor can handle the following formats:\n"
              + "1. <RULE_MACRO_PRED BIN_PRED OBJ1 OBJ2>\n"
              + "2. <BIN_PRED OBJ1 OBJ2> one of the two objects needs a quantifier.";
    if (argInputList.size() > 4){
      throw new KbException("Got more than four arguments.\n "
              + helpMessage);
    } else if (argInputList.size() == 4) {
      Object isItRMP = argInputList.get(0);
      KbPredicateImpl rmp = null;
      if (isItRMP instanceof KbPredicate){
        rmp = KbPredicateImpl.class.cast(isItRMP);
        if (!rmp.isInstanceOf(KbCollectionImpl.get("RuleMacroPredicate"))) {
          throw new KbException("When four arguments are supplied, first argument "
                  + "has to be a #$RuleMacroPredicate. Got: " + rmp + " instead."
                  + helpMessage);
        }
      }
      
      // We got a rule macro GAF, no transformation required.
      return new PredAndArgs(argInputList);
    } else if (argInputList.size() == 3) {
      if (!(argInputList.get(1) instanceof QuantifiedInstanceRestrictedVariable
            || argInputList.get(2) instanceof QuantifiedInstanceRestrictedVariable)){
        throw new KbException("Can not make a TypeFact when a "
                + "QuantifiedRestrictedVariable is not present with three arguments.\n"
                + helpMessage);
      }
    }
    
    Object isItP = argInputList.get(0);
    KbPredicateImpl p = null;
    if (isItP instanceof KbPredicate){
       p = KbPredicateImpl.class.cast(argInputList.get(0));
       if (p.getArity() != 2){
         throw new KbException("Currently TypeFact expects a #$BinaryPredicate");
       }
    } else {
      throw new KbException("First argument of " + argList + ", " + isItP + ", is not a #$Predicate");
    }
    Object subject = argInputList.get(1);
    Object object = argInputList.get(2);
    
    int count = 0;
    for (Object o : argInputList) {
      if (o instanceof QuantifiedInstanceRestrictedVariable) {
        System.out.println("Arg " + count + ": " + ((QuantifiedInstanceRestrictedVariable) o).getCollection());
      } else if (o instanceof KbObject) {
        System.out.println("Arg " + count + ": " + ((KbObject) o).getCore());
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
          argOutputList.add(0, KbPredicateImpl.get("relationAllAll"));
        } else if (object1.getQuantifier().equals(thereExists)) {
          argOutputList.add(0, KbPredicateImpl.get("relationAllExists"));
        }
      } else if (subject1.getQuantifier().equals(thereExists)) {
        if (object1.getQuantifier().equals(forAll)) {
          argOutputList.add(0, KbPredicateImpl.get("relationExistsAll"));
        } else if (object1.getQuantifier().equals(thereExists)) {
          argOutputList.add(0, KbPredicateImpl.get("relationExistsExists"));
        }
      }

      argOutputList.add((Object) p);
      argOutputList.add(subject1.getCollection());
      argOutputList.add(object1.getCollection());

    } else if (subject instanceof QuantifiedInstanceRestrictedVariable) {

      QuantifiedInstanceRestrictedVariable subject1 = QuantifiedInstanceRestrictedVariable.class.cast(subject);

      if (subject1.getQuantifier().equals(forAll)) {
        argOutputList.add(0, KbPredicateImpl.get("relationAllInstance"));
      } else if (subject1.getQuantifier().equals(thereExists)) {
        argOutputList.add(0, KbPredicateImpl.get("relationExistsInstance"));
      }

      argOutputList.add((Object) p);
      argOutputList.add(subject1.getCollection());
      argOutputList.add(object);
    } else if (object instanceof QuantifiedInstanceRestrictedVariable) {

      QuantifiedInstanceRestrictedVariable object1 = QuantifiedInstanceRestrictedVariable.class.cast(object);

      if (object1.getQuantifier().equals(forAll)) {
        argOutputList.add(0, KbPredicateImpl.get("relationInstanceAll"));
      } else if (object1.getQuantifier().equals(thereExists)) {
        argOutputList.add(0, KbPredicateImpl.get("relationInstanceExists"));
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
  public <O> O getTypeArgument (int getPos, Class<O> retType) throws KbException {
    Object o = this.getModifiedTypeLevelArguments().get(getPos);

    if (QuantifiedInstanceRestrictedVariable.class.isAssignableFrom(retType)
            && o instanceof QuantifiedInstanceRestrictedVariable) {
      return retType.cast(o);
    } else if (KbObjectImpl.class.isAssignableFrom(retType)) {
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
    private final KbPredicate p;
    private final Object[] args;
    
    private PredAndArgs(List<Object> args) {
      this.p = (KbPredicate)(args.get(0));
      this.args = args.subList(1, args.size()).toArray();
    }

    /**
     * @return the p
     */
    public KbPredicate getPredicate() {
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
