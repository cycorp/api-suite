package com.cyc.kb.wrapper;

/*
 * #%L
 * File: FirstOrderCollectionWrapper.java
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
import com.cyc.kb.FirstOrderCollection;
import com.cyc.kb.KbCollection;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbTypeException;
import java.util.Collection;

/**
 * An abstract base class for implementing FirstOrderCollections per the decorator pattern. To use,
 * extend this class and implement the {@link #wrapped() } method to return the wrapped object.
 *
 * @author nwinant
 */
public abstract class FirstOrderCollectionWrapper
        extends KbCollectionWrapper
        implements FirstOrderCollection {

  //====|    Abstract methods    |================================================================//
  
  @Override
  protected abstract FirstOrderCollection wrapped();

  //====|    Public methods    |==================================================================//

  @Override
  public Collection<FirstOrderCollection> getGeneralizations() {
    return wrapped().getGeneralizations();
  }

  @Override
  public Collection<FirstOrderCollection> getGeneralizations(Context ctx) {
    return wrapped().getGeneralizations(ctx);
  }

  @Override
  public FirstOrderCollection addGeneralization(KbCollection moreGeneral, Context ctx) 
          throws KbTypeException, CreateException {
    return wrapped().addGeneralization(moreGeneral, ctx);
  }

}
