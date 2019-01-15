package com.cyc.kb.wrapper;

/*
 * #%L
 * File: KbFunctionWrapper.java
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
import com.cyc.kb.Context;
import com.cyc.kb.KbCollection;
import com.cyc.kb.KbFunction;
import com.cyc.kb.KbObject;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbException;
import com.cyc.kb.exception.KbTypeException;
import java.util.Collection;

/**
 * An abstract base class for implementing KbFunctions per the decorator pattern. To use, extend this
 * class and implement the {@link #wrapped() } method to return the wrapped object.
 *
 * @author nwinant
 */
public abstract class KbFunctionWrapper extends RelationWrapper implements KbFunction {

  //====|    Abstract methods    |================================================================//
  
  @Override
  protected abstract KbFunction wrapped();

  //====|    Public methods    |==================================================================//

  @Override
  public <O extends KbObject> O findOrCreateFunctionalTerm(Class<O> retType, Object... args) 
          throws KbTypeException, CreateException {
    return wrapped().findOrCreateFunctionalTerm(retType, args);
  }

  @Override
  public Collection<KbCollection> getResultIsa() {
    return wrapped().getResultIsa();
  }

  @Override
  public Collection<KbCollection> getResultIsa(Context ctx) {
    return wrapped().getResultIsa(ctx);
  }

  @Override
  public KbFunction addResultIsa(KbCollection col, Context ctx) 
          throws KbTypeException, CreateException {
    return wrapped().addResultIsa(col, ctx);
  }

  @Override
  public Collection<KbCollection> getResultGenl() throws KbException {
    return wrapped().getResultGenl();
  }

  @Override
  public Collection<KbCollection> getResultGenl(Context ctx) {
    return wrapped().getResultGenl(ctx);
  }

  @Override
  public KbFunction addResultGenl(KbCollection col, Context ctx)
          throws KbTypeException, CreateException {
    return wrapped().addResultGenl(col, ctx);
  }

  @Override
  public boolean isUnreifiable() {
    return wrapped().isUnreifiable();
  }

}
