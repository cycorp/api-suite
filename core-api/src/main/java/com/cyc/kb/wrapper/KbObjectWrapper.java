package com.cyc.kb.wrapper;

/*
 * #%L
 * File: KbObjectWrapper.java
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
import com.cyc.kb.KbIndividual;
import com.cyc.kb.KbObject;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbTypeException;
import com.cyc.session.exception.SessionCommunicationException;
import com.cyc.session.exception.SessionException;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * An abstract base class for implementing KbObjects per the decorator pattern. To use, extend this
 * class and implement the {@link #wrapped() } method to return the wrapped object.
 *
 * @author nwinant
 */
public abstract class KbObjectWrapper implements KbObject {

  //====|    Abstract methods    |================================================================//
  
  protected abstract KbObject wrapped();

  //====|    Public methods    |==================================================================//
  
  @Override
  public String getId() {
    return wrapped().getId();
  }

  @Override
  public KbObject getType() {
    return wrapped().getType();
  }

  @Override
  public Object getCore() {
    return wrapped().getCore();
  }

  @Override
  public Boolean isAtomic() {
    return wrapped().isAtomic();
  }

  /*
  @Override
  public Boolean isAssertion() {
    return wrapped().isAssertion();
  }

  @Override
  public Boolean isCollection() {
    return wrapped().isCollection();
  }

  @Override
  public Boolean isContext() {
    return wrapped().isContext();
  }

  @Override
  public Boolean isFunction() {
    return wrapped().isFunction();
  }

  @Override
  public Boolean isIndividual() {
    return wrapped().isIndividual();
  }

  @Override
  public Boolean isPredicate() {
    return wrapped().isPredicate();
  }

  @Override
  public Boolean isSentence() {
    return wrapped().isSentence();
  }

  @Override
  public Boolean isSymbol() {
    return wrapped().isSymbol();
  }

  @Override
  public Boolean isTerm() {
    return wrapped().isTerm();
  }

  @Override
  public Boolean isVariable() {
    return wrapped().isVariable();
  }
  */

  @Override
  public boolean isIndexical() throws SessionCommunicationException {
    return wrapped().isIndexical();
  }

  @Override
  public Collection<KbCollection> getQuotedIsa() {
    return wrapped().getQuotedIsa();
  }

  @Override
  public boolean isQuotedInstanceOf(KbCollection col) {
    return wrapped().isQuotedInstanceOf(col);
  }

  /*
  @Override
  public boolean isQuotedInstanceOf(String colStr) {
    return wrapped().isQuotedInstanceOf(colStr);
  }
*/
  
  @Override
  public boolean isQuotedInstanceOf(KbCollection col, Context ctx) {
    return wrapped().isQuotedInstanceOf(col, ctx);
  }
/*
  @Override
  public boolean isQuotedInstanceOf(String colStr, String ctxStr) {
    return wrapped().isQuotedInstanceOf(colStr, ctxStr);
  }
*/
  @Override
  public boolean isQuoted() throws KbTypeException, CreateException {
    return wrapped().isQuoted();
  }

  @Override
  public KbIndividual quote() throws KbTypeException, CreateException {
    return wrapped().quote();
  }

  @Override
  public <O> O unquote() throws KbTypeException, CreateException {
    return wrapped().unquote();
  }

  @Override
  public KbIndividual toQuoted() throws KbTypeException, CreateException {
    return wrapped().toQuoted();
  }

  @Override
  public <O> O toUnquoted() throws KbTypeException, CreateException {
    return wrapped().toUnquoted();
  }

  @Override
  public <O> O possiblyResolveIndexical(Map<KbObject, Object> substitutions)
          throws SessionCommunicationException, KbTypeException {
    return wrapped().possiblyResolveIndexical(substitutions);
  }

  @Override
  public <O> O resolveIndexical()
          throws SessionCommunicationException, KbTypeException, CreateException {
    return wrapped().resolveIndexical();
  }

  @Override
  public String stringApiValue() {
    return wrapped().stringApiValue();
  }

  @Override
  public String toNlString() throws SessionException {
    return wrapped().toNlString();
  }

  @Override
  public String toString() {
    return wrapped().toString();
  }

  @Override
  public boolean equalsSemantically(Object object) {
    return wrapped().equalsSemantically(object);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    return (obj != null)
                   && (getClass() == obj.getClass())
                   && Objects.equals(this.wrapped(), ((KbObjectWrapper) obj).wrapped());
  }

  @Override
  public int hashCode() {
    return wrapped().hashCode();
  }

}
