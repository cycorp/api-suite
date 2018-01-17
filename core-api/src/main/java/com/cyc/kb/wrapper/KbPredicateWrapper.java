package com.cyc.kb.wrapper;

/*
 * #%L
 * File: KbPredicateWrapper.java
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
import com.cyc.kb.Context;
import com.cyc.kb.Fact;
import com.cyc.kb.KbPredicate;
import com.cyc.kb.Sentence;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbException;
import com.cyc.kb.exception.KbTypeException;
import java.util.Collection;
import java.util.List;

/**
 * An abstract base class for implementing KbPredicates per the decorator pattern. To use, extend this
 * class and implement the {@link #wrapped() } method to return the wrapped object.
 *
 * @author nwinant
 */
public abstract class KbPredicateWrapper extends RelationWrapper implements KbPredicate {

  //====|    Abstract methods    |================================================================//
  
  @Override
  protected abstract KbPredicate wrapped();

  //====|    Public methods    |==================================================================//

  @Override
  public Collection<KbPredicate> getSpecializations() {
    return wrapped().getSpecializations();
  }

  @Override
  public Collection<KbPredicate> getSpecializations(Context ctx) {
    return wrapped().getSpecializations(ctx);
  }

  @Override
  public KbPredicate addSpecialization(KbPredicate moreSpecific, Context ctx)
          throws KbTypeException, CreateException {
    return wrapped().addSpecialization(moreSpecific, ctx);
  }

  @Override
  public Collection<KbPredicate> getGeneralizations() throws KbException {
    return wrapped().getGeneralizations();
  }

  @Override
  public Collection<KbPredicate> getGeneralizations(Context ctx) {
    return wrapped().getGeneralizations(ctx);
  }

  @Override
  public Sentence getGeneralizationSentence(KbPredicate moreGeneral)
          throws KbTypeException, CreateException {
    return wrapped().getGeneralizationSentence(moreGeneral);
  }

  @Override
  public Sentence getInverseGeneralizationSentence(KbPredicate moreGeneral)
          throws KbTypeException, CreateException {
    return wrapped().getInverseGeneralizationSentence(moreGeneral);
  }

  @Override
  public KbPredicate addGeneralization(KbPredicate moreGeneral, Context ctx)
          throws KbTypeException, CreateException {
    return wrapped().addGeneralization(moreGeneral, ctx);
  }

  @Override
  public boolean isGeneralizationOf(KbPredicate moreSpecific, Context ctx) {
    return wrapped().isGeneralizationOf(moreSpecific, ctx);
  }

  @Override
  public List<Fact> getExtent() {
    return wrapped().getExtent();
  }

  @Override
  public List<Fact> getExtent(Context ctx) {
    return wrapped().getExtent(ctx);
  }

  @Override
  public Fact addFact(Context ctx, Object... args) throws KbTypeException, CreateException {
    return wrapped().addFact(ctx, args);
  }

  @Override
  public Fact getFact(Context ctx, Object... args) throws KbTypeException, CreateException {
    return wrapped().getFact(ctx, args);
  }

  @Override
  public Collection<Fact> getFacts(Object arg, int argPosition, Context ctx) {
    return wrapped().getFacts(arg, argPosition, ctx);
  }

  @Override
  public Boolean isAsserted(Context ctx, Object... args) {
    return wrapped().isAsserted(ctx, args);
  }

  @Override
  public Sentence getSentence(Object... args) throws KbTypeException, CreateException {
    return wrapped().getSentence(args);
  }

  @Override
  public <O> Collection<O> getValuesForArgPosition(
          Object arg, int argPosition, int valuePosition, Context ctx) {
    return wrapped().getValuesForArgPosition(arg, argPosition, valuePosition, ctx);
  }

  @Override
  public <O> Collection<O> getValuesForArgPositionWithMatchArg(Object arg, 
                                                               int argPosition,
                                                               int valuePosition, 
                                                               Object matchArg,
                                                               int matchArgPos, 
                                                               Context ctx) {
    return wrapped().getValuesForArgPositionWithMatchArg(
            arg, argPosition, valuePosition, matchArg, matchArgPos, ctx);
  }

}
