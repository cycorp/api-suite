package com.cyc.kb.wrapper;

/*
 * #%L
 * File: BinaryPredicateWrapper.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2018 Cycorp, Inc
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
import com.cyc.kb.BinaryPredicate;
import com.cyc.kb.Context;
import com.cyc.kb.Fact;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbTypeException;
import java.util.Collection;

/**
 * An abstract base class for implementing BinaryPredicates per the decorator pattern. To use,
 * extend this class and implement the {@link #wrapped() } method to return the wrapped object.
 *
 * @author nwinant
 */
public abstract class BinaryPredicateWrapper extends KbPredicateWrapper implements BinaryPredicate {

  //====|    Abstract methods    |================================================================//
  
  @Override
  protected abstract BinaryPredicate wrapped();

  //====|    Public methods    |==================================================================//

  @Override
  public Fact addFact(Context ctx, Object arg1, Object arg2) 
          throws KbTypeException, CreateException {
    return wrapped().addFact(ctx, arg1, arg2);
  }

  @Override
  public <O> Collection<O> getValuesForArg(Object arg1, Object arg2, Context ctx) {
    return wrapped().getValuesForArg(arg1, arg2, ctx);
  }

  @Override
  public <O> Collection<O> getValuesForArg(Object arg1, Object arg2) {
    return wrapped().getValuesForArg(arg1, arg2);
  }

}
