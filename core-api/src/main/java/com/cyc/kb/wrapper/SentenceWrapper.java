package com.cyc.kb.wrapper;

/*
 * #%L
 * File: SentenceWrapper.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2019 Cycorp, Inc
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
import com.cyc.kb.ArgPosition;
import com.cyc.kb.ArgUpdate;
import com.cyc.kb.Assertion;
import com.cyc.kb.Context;
import com.cyc.kb.KbObject;
import com.cyc.kb.Sentence;
import com.cyc.kb.Variable;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbException;
import com.cyc.kb.exception.KbTypeException;
import com.cyc.session.exception.SessionCommunicationException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An abstract base class for implementing Sentences per the decorator pattern. To use, extend this
 * class and implement the {@link #wrapped() } method to return the wrapped object.
 *
 * @author nwinant
 */
public abstract class SentenceWrapper extends KbObjectWrapper implements Sentence {

  //====|    Abstract methods    |================================================================//
  
  @Override
  protected abstract Sentence wrapped();

  //====|    Public methods    |==================================================================//
  
  @Override
  public Assertion assertIn(Context ctx) throws KbException {
    return wrapped().assertIn(ctx);
  }

  @Override
  public boolean isAssertible(Context ctx) {
    return wrapped().isAssertible(ctx);
  }

  @Override
  public String notAssertibleExplanation(Context ctx) {
    return wrapped().notAssertibleExplanation(ctx);
  }

  @Override
  public Sentence performUpdates(List<ArgUpdate> updates) {
    return wrapped().performUpdates(updates);
  }

  @Override
  public List<Variable> getVariables(boolean includeQueryable) throws KbException {
    return wrapped().getVariables(includeQueryable);
  }

  @Override
  public List<KbObject> getIndexicals(boolean includeAllIndexicals)
          throws KbException, SessionCommunicationException {
    return wrapped().getIndexicals(includeAllIndexicals);
  }

  @Override
  public List<KbObject> getIndexicals() throws KbException, SessionCommunicationException {
    return wrapped().getIndexicals();
  }

  @Override
  public Sentence replaceTerms(Map substitutions) throws KbTypeException, CreateException {
    return wrapped().replaceTerms(substitutions);
  }

  @Override
  public Sentence replaceTerms(List<Object> from, List<Object> to)
          throws KbTypeException, CreateException {
    return wrapped().replaceTerms(from, to);
  }

  @Override
  public Sentence setArgPosition(ArgPosition pos, Object value)
          throws KbTypeException, CreateException {
    return wrapped().setArgPosition(pos, value);
  }

  @Override
  public Sentence quantify(KbObject variable) throws KbTypeException, CreateException {
    return wrapped().quantify(variable);
  }

  @Override
  public Set<ArgPosition> getArgPositionsForTerm(Object term) {
    return wrapped().getArgPositionsForTerm(term);
  }

  @Override
  public Integer getArity() {
    return wrapped().getArity();
  }

  @Override
  public <O> O getArgument(int argPosition) throws KbTypeException, CreateException {
    return wrapped().getArgument(argPosition);
  }

  @Override
  public Boolean isValid() {
    return wrapped().isValid();
  }

}
