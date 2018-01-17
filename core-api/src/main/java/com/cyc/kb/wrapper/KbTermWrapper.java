package com.cyc.kb.wrapper;

/*
 * #%L
 * File: KbTermWrapper.java
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
import com.cyc.kb.KbCollection;
import com.cyc.kb.KbIndividual;
import com.cyc.kb.KbTerm;
import com.cyc.kb.Sentence;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.DeleteException;
import com.cyc.kb.exception.InvalidNameException;
import com.cyc.kb.exception.KbTypeException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * An abstract base class for implementing KbTerms per the decorator pattern. To use, extend this
 * class and implement the {@link #wrapped() } method to return the wrapped object.
 *
 * @author nwinant
 */
public abstract class KbTermWrapper extends KbObjectWrapper implements KbTerm {

  //====|    Abstract methods    |================================================================//
  
  @Override
  protected abstract KbTerm wrapped();

  //====|    Public methods    |==================================================================//
  
  @Override
  public boolean provablyNotInstanceOf(KbCollection col, Context ctx) {
    return wrapped().provablyNotInstanceOf(col, ctx);
  }

  @Override
  public boolean provablyNotInstanceOf(String colStr, String ctxStr) {
    return wrapped().provablyNotInstanceOf(colStr, ctxStr);
  }

  @Override
  public <O> O replaceTerms(Map substitutions) throws KbTypeException, CreateException {
    return wrapped().replaceTerms(substitutions);
  }

  @Override
  public KbIndividual getCreator() {
    return wrapped().getCreator();
  }

  @Override
  public Date getCreationDate() {
    return wrapped().getCreationDate();
  }

  @Override
  public KbTerm rename(String name) throws InvalidNameException {
    return wrapped().rename(name);
  }

  @Override
  public void delete() throws DeleteException {
    wrapped().delete();
  }

  @Override
  public Boolean isValid() {
    return wrapped().isValid();
  }

  @Override
  public Integer getArity() {
    return wrapped().getArity();
  }

  @Override
  public <O> O getArgument(int argPosition)
          throws KbTypeException, CreateException, UnsupportedOperationException {
    return wrapped().getArgument(argPosition);
  }

  @Override
  public Collection<String> getComments() {
    return wrapped().getComments();
  }

  @Override
  public Collection<String> getComments(Context ctx) {
    return wrapped().getComments(ctx);
  }

  @Override
  public Collection<String> getComments(String ctxStr) {
    return wrapped().getComments(ctxStr);
  }

  @Override
  public Fact addComment(String comment, Context ctx)
          throws KbTypeException, CreateException {
    return wrapped().addComment(comment, ctx);
  }

  @Override
  public Fact addComment(String comment, String ctx)
          throws KbTypeException, CreateException {
    return wrapped().addComment(comment, ctx);
  }

  @Override
  public KbTerm addQuotedIsa(KbCollection collection, Context context)
          throws KbTypeException, CreateException {
    return wrapped().addQuotedIsa(collection, context);
  }

  @Override
  public KbTerm instantiates(KbCollection collection, Context context)
          throws KbTypeException, CreateException {
    return wrapped().instantiates(collection, context);
  }

  @Override
  public KbTerm instantiates(String collectionStr, String contextStr)
          throws KbTypeException, CreateException {
    return wrapped().instantiates(collectionStr, contextStr);
  }

  @Override
  public KbTerm instantiates(KbCollection collection) throws KbTypeException, CreateException {
    return wrapped().instantiates(collection);
  }

  @Override
  public Sentence instantiatesSentence(KbCollection collection)
          throws KbTypeException, CreateException {
    return wrapped().instantiatesSentence(collection);
  }

  @Override
  public boolean isInstanceOf(KbCollection collection) {
    return wrapped().isInstanceOf(collection);
  }

  @Override
  public boolean isInstanceOf(String collectionStr) {
    return wrapped().isInstanceOf(collectionStr);
  }

  @Override
  public boolean isInstanceOf(KbCollection collection, Context context) {
    return wrapped().isInstanceOf(collection, context);
  }

  @Override
  public boolean isInstanceOf(String collectionStr, String contextStr) {
    return wrapped().isInstanceOf(collectionStr, contextStr);
  }

}
