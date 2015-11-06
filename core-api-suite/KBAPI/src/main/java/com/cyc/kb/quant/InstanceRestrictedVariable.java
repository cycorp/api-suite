/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cyc.kb.quant;

/*
 * #%L
 * File: InstanceRestrictedVariable.java
 * Project: KB API Implementation
 * %%
 * Copyright (C) 2013 - 2015 Cycorp, Inc
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
import com.cyc.kb.KBCollection;
import com.cyc.kb.Variable;
import com.cyc.kb.client.KBPredicateImpl;
import com.cyc.kb.exception.KBApiException;

/**
 * THIS CLASS IS NOT SUPPORTED IN KB API 1.0.
 * @author vijay
 */
public class InstanceRestrictedVariable extends RestrictedVariable {

  private KBCollection collection;

  public InstanceRestrictedVariable(Context ctx, KBCollection c) throws KBApiException {
    super(ctx, c.getVariable(), new Object[]{KBPredicateImpl.get("isa"), c.getVariable(), c});
    this.setCollection(c);
  }
  
  public InstanceRestrictedVariable(Context ctx, KBCollection c, Variable v) throws KBApiException {
    super(ctx, v, new Object[]{KBPredicateImpl.get("isa"), v, c});
    this.setCollection(c);
  }

  public KBCollection getCollection() {
    return collection;
  }

  public void setCollection(KBCollection collection) {
    this.collection = collection;
  }
}
