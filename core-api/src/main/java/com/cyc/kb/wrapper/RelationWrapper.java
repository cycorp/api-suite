package com.cyc.kb.wrapper;

/*
 * #%L
 * File: RelationWrapper.java
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
import com.cyc.kb.Relation;
import com.cyc.kb.Sentence;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbTypeException;
import java.util.Collection;
import java.util.List;

/**
 * An abstract base class for implementing Relations per the decorator pattern. To use, extend this
 * class and implement the {@link #wrapped() } method to return the wrapped object.
 *
 * @author nwinant
 */
public abstract class RelationWrapper extends KbIndividualWrapper implements Relation {

  //====|    Abstract methods    |================================================================//
  
  @Override
  protected abstract Relation wrapped();

  //====|    Public methods    |==================================================================//

  @Override
  public List<Collection<KbCollection>> getArgIsaList() {
    return wrapped().getArgIsaList();
  }

  @Override
  public List<Collection<KbCollection>> getArgIsaList(Context ctx) {
    return wrapped().getArgIsaList(ctx);
  }

  @Override
  public Collection<KbCollection> getArgIsa(int argPos) {
    return wrapped().getArgIsa(argPos);
  }

  @Override
  public Collection<KbCollection> getArgIsa(int argPos, Context ctx) {
    return wrapped().getArgIsa(argPos, ctx);
  }

  @Override
  public Relation addArgIsa(int argPos, KbCollection col, Context ctx) 
          throws KbTypeException, CreateException {
    return wrapped().addArgIsa(argPos, col, ctx);
  }

  @Override
  public Sentence addArgIsaSentence(int argPos, KbCollection col)
          throws KbTypeException, CreateException {
    return wrapped().addArgIsaSentence(argPos, col);
  }

  @Override
  public List<Collection<KbCollection>> getArgGenlList() {
    return wrapped().getArgGenlList();
  }

  @Override
  public List<Collection<KbCollection>> getArgGenlList(Context ctx) {
    return wrapped().getArgGenlList(ctx);
  }

  @Override
  public Collection<KbCollection> getArgGenl(int argPos) {
    return wrapped().getArgGenl(argPos);
  }

  @Override
  public Collection<KbCollection> getArgGenl(int argPos, Context ctx) {
    return wrapped().getArgGenl(argPos, ctx);
  }

  @Override
  public Relation addArgGenl(int argPos, KbCollection col, Context ctx)
          throws KbTypeException, CreateException {
    return wrapped().addArgGenl(argPos, col, ctx);
  }

  @Override
  public List<Integer> getInterArgDifferent(Context ctx) {
    return wrapped().getInterArgDifferent(ctx);
  }

  @Override
  public Relation addInterArgDifferent(Integer argPosM, Integer argPosN, Context ctx)
          throws KbTypeException, CreateException {
    return wrapped().addInterArgDifferent(argPosM, argPosN, ctx);
  }

  @Override
  public boolean isVariableArity() {
    return wrapped().isVariableArity();
  }

  @Override
  public Integer getArityMin() {
    return wrapped().getArityMin();
  }

  @Override
  public Integer getArityMax() {
    return wrapped().getArityMax();
  }

  @Override
  public Relation setArity(int arityValue) throws KbTypeException, CreateException {
    return wrapped().setArity(arityValue);
  }

  @Override
  public Sentence setAritySentence(int arityValue) throws KbTypeException, CreateException {
    return wrapped().setAritySentence(arityValue);
  }

}
