package com.cyc.kb.wrapper;

/*
 * #%L
 * File: ContextWrapper.java
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
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbTypeException;
import java.util.Collection;

/**
 * An abstract base class for implementing Contexts per the decorator pattern. To use, extend this
 * class and implement the {@link #wrapped() } method to return the wrapped object.
 *
 * @author nwinant
 */
public abstract class ContextWrapper extends KbIndividualWrapper implements Context {

  //====|    Abstract methods    |================================================================//
  
  @Override
  protected abstract Context wrapped();

  //====|    Public methods    |==================================================================//
  
  @Override
  public Collection<Context> getExtensions() {
    return wrapped().getExtensions();
  }
/*
  @Override
  public Context addExtension(String moreSpecificStr) throws KbTypeException, CreateException {
    return wrapped().addExtension(moreSpecificStr);
  }
*/
  @Override
  public Context addExtension(Context moreSpecific) throws KbTypeException, CreateException {
    return wrapped().addExtension(moreSpecific);
  }

  @Override
  public Collection<Context> getInheritsFrom() {
    return wrapped().getInheritsFrom();
  }
/*
  @Override
  public Context addInheritsFrom(String moreGeneralStr) throws KbTypeException, CreateException {
    return wrapped().addInheritsFrom(moreGeneralStr);
  }
*/
  @Override
  public Context addInheritsFrom(Context moreGeneral) throws KbTypeException, CreateException {
    return wrapped().addInheritsFrom(moreGeneral);
  }

  @Override
  public Context getMonad() {
    return wrapped().getMonad();
  }

}
