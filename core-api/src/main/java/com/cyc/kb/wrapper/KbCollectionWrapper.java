package com.cyc.kb.wrapper;

/*
 * #%L
 * File: KbCollectionWrapper.java
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
import com.cyc.kb.KbCollection;
import com.cyc.kb.Sentence;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbTypeException;
import java.util.Collection;

/**
 * An abstract base class for implementing KbCollections per the decorator pattern. To use, extend
 * this class and implement the {@link #wrapped() } method to return the wrapped object.
 *
 * @author nwinant
 */
public abstract class KbCollectionWrapper extends KbTermWrapper implements KbCollection {

  //====|    Abstract methods    |================================================================//
  
  @Override
  protected abstract KbCollection wrapped();

  //====|    Public methods    |==================================================================//
  
  @Override
  public Collection<KbCollection> allSpecializations() {
    return wrapped().allSpecializations();
  }

  @Override
  public Collection<KbCollection> allSpecializations(String ctxStr) {
    return wrapped().allSpecializations(ctxStr);
  }

  @Override
  public Collection<KbCollection> allSpecializations(Context ctx) {
    return wrapped().allSpecializations(ctx);
  }

  @Override
  public Collection<KbCollection> getSpecializations() {
    return wrapped().getSpecializations();
  }

  @Override
  public Collection<KbCollection> getSpecializations(String ctxStr) {
    return wrapped().getSpecializations(ctxStr);
  }

  @Override
  public Collection<KbCollection> getSpecializations(Context ctx) {
    return wrapped().getSpecializations(ctx);
  }

  @Override
  public KbCollection addSpecialization(String moreSpecificStr)
          throws KbTypeException, CreateException {
    return wrapped().addSpecialization(moreSpecificStr);
  }

  @Override
  public KbCollection addSpecialization(String moreSpecificStr, String ctxStr)
          throws KbTypeException, CreateException {
    return wrapped().addSpecialization(moreSpecificStr, ctxStr);
  }

  @Override
  public KbCollection addSpecialization(KbCollection moreSpecific)
          throws KbTypeException, CreateException {
    return wrapped().addSpecialization(moreSpecific);
  }

  @Override
  public KbCollection addSpecialization(KbCollection moreSpecific, Context ctx)
          throws KbTypeException, CreateException {
    return wrapped().addSpecialization(moreSpecific, ctx);
  }

  @Override
  public Collection<KbCollection> allGeneralizations() {
    return wrapped().allGeneralizations();
  }

  @Override
  public Collection<KbCollection> allGeneralizations(String ctxStr) {
    return wrapped().allGeneralizations(ctxStr);
  }

  @Override
  public Collection<KbCollection> allGeneralizations(Context ctx) {
    return wrapped().allGeneralizations(ctx);
  }

  @Override
  public Collection<? extends KbCollection> getGeneralizations() {
    return wrapped().getGeneralizations();
  }

  @Override
  public Collection<? extends KbCollection> getGeneralizations(String ctxStr) {
    return wrapped().getGeneralizations(ctxStr);
  }

  @Override
  public Collection<? extends KbCollection> getGeneralizations(Context ctx) {
    return wrapped().getGeneralizations(ctx);
  }

  @Override
  public KbCollection addGeneralization(String moreGeneralStr)
          throws KbTypeException, CreateException {
    return wrapped().addGeneralization(moreGeneralStr);
  }

  @Override
  public KbCollection addGeneralization(String moreGeneralStr, String ctxStr)
          throws KbTypeException, CreateException {
    return wrapped().addGeneralization(moreGeneralStr, ctxStr);
  }

  @Override
  public KbCollection addGeneralization(KbCollection moreGeneral)
          throws KbTypeException, CreateException {
    return wrapped().addGeneralization(moreGeneral);
  }

  @Override
  public KbCollection addGeneralization(KbCollection moreGeneral, Context ctx)
          throws KbTypeException, CreateException {
    return wrapped().addGeneralization(moreGeneral, ctx);
  }

  @Override
  public Sentence addGeneralizationSentence(KbCollection moreGeneral)
          throws KbTypeException, CreateException {
    return wrapped().addGeneralizationSentence(moreGeneral);
  }

  @Override
  public <O> Collection<? extends O> getInstances() {
    return wrapped().getInstances();
  }

  @Override
  public <O> Collection<O> getInstances(String ctxStr) {
    return wrapped().getInstances(ctxStr);
  }

  @Override
  public <O> Collection<O> getInstances(Context ctx) {
    return wrapped().getInstances(ctx);
  }

  @Override
  public Collection<KbCollection> instancesOf() {
    return wrapped().instancesOf();
  }

  @Override
  public Collection<KbCollection> instancesOf(String ctxStr) {
    return wrapped().instancesOf(ctxStr);
  }

  @Override
  public Collection<KbCollection> instancesOf(Context ctx) {
    return wrapped().instancesOf(ctx);
  }

  @Override
  public KbCollection instantiates(String colStr, String ctxStr)
          throws KbTypeException, CreateException {
    return wrapped().instantiates(colStr, ctxStr);
  }

  @Override
  public KbCollection instantiates(KbCollection col, Context ctx)
          throws KbTypeException, CreateException {
    return wrapped().instantiates(col, ctx);
  }

  @Override
  public boolean isGeneralizationOf(KbCollection moreSpecific, Context ctx) {
    return wrapped().isGeneralizationOf(moreSpecific, ctx);
  }

  @Override
  public boolean isGeneralizationOf(String moreSpecificStr) {
    return wrapped().isGeneralizationOf(moreSpecificStr);
  }

  @Override
  public boolean isGeneralizationOf(KbCollection moreSpecific) {
    return wrapped().isGeneralizationOf(moreSpecific);
  }

}
