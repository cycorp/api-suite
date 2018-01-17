package com.cyc.kb.wrapper;

/*
 * #%L
 * File: AssertionWrapper.java
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
import com.cyc.kb.Assertion;
import com.cyc.kb.Context;
import com.cyc.kb.Fact;
import com.cyc.kb.KbCollection;
import com.cyc.kb.Sentence;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.DeleteException;
import com.cyc.kb.exception.KbException;
import com.cyc.kb.exception.KbTypeException;
import java.util.Collection;

/**
 * An abstract base class for implementing Assertions per the decorator pattern. To use, extend this
 * class and implement the {@link #wrapped() } method to return the wrapped object.
 *
 * @author nwinant
 */
public abstract class AssertionWrapper extends KbObjectWrapper implements Assertion {

  //====|    Abstract methods    |================================================================//
  
  @Override
  protected abstract Assertion wrapped();

  //====|    Public methods    |==================================================================//
  
  @Override
  public Sentence getFormula() {
    return wrapped().getFormula();
  }

  @Override
  public Context getContext() {
    return wrapped().getContext();
  }

  @Override
  public Collection<Assertion> getSupportingAssertions() throws KbTypeException, CreateException {
    return wrapped().getSupportingAssertions();
  }

  @Override
  public Boolean isDeducedAssertion() {
    return wrapped().isDeducedAssertion();
  }

  @Override
  public Boolean isGroundAtomicFormula() {
    return wrapped().isGroundAtomicFormula();
  }

  @Override
  public Boolean isAssertedAssertion() {
    return wrapped().isAssertedAssertion();
  }

  @Override
  public Direction getDirection() {
    return wrapped().getDirection();
  }

  @Override
  public Assertion changeDirection(Direction direction)
          throws KbTypeException, CreateException, KbException {
    return wrapped().changeDirection(direction);
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
  public <O> O getArgument(int argPosition) throws KbTypeException, CreateException {
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
  public Fact addComment(String comment, Context ctx) throws KbTypeException, CreateException {
    return wrapped().addComment(comment, ctx);
  }

  @Override
  public Fact addComment(String comment, String ctx) throws KbTypeException, CreateException {
    return wrapped().addComment(comment, ctx);
  }

  @Override
  public Assertion addQuotedIsa(KbCollection collection, Context context)
          throws KbTypeException, CreateException {
    return wrapped().addQuotedIsa(collection, context);
  }

}
