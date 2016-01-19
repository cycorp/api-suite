/*
 * Copyright 2015 Cycorp, Inc.
 *
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
 */
package com.cyc.kb.service;

/*
 * #%L
 * File: ContextServiceImpl.java
 * Project: KB Client
 * %%
 * Copyright (C) 2013 - 2016 Cycorp, Inc
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

import com.cyc.kb.spi.ContextService;
import com.cyc.kb.Context;
import com.cyc.kb.client.ContextImpl;
import com.cyc.kb.DefaultContext;
import com.cyc.kb.client.config.KbDefaultContext;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbTypeException;

/**
 *
 * @author nwinant
 */
public class ContextServiceImpl<T extends ContextImpl> extends KbIndividualServiceImpl<T> implements ContextService<T> {
  
  // Protected
  
  @Override
  protected Class<T> getObjectType() {
    return (Class<T>) ContextImpl.class;
  }
  
  @Override
  public DefaultContext getDefaultContext(Context assertionContext, Context queryContext) {
    return new KbDefaultContext(assertionContext, queryContext);
  }
  
  @Override
  public DefaultContext getDefaultContext(String assertionCtxStr, String queryCtxStr) throws KbTypeException, CreateException {
    return getDefaultContext(get(assertionCtxStr), get(queryCtxStr));
  }
  
}
