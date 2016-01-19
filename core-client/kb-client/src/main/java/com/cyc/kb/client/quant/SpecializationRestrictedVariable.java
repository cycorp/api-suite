/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cyc.kb.client.quant;

/*
 * #%L
 * File: SpecializationRestrictedVariable.java
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

import com.cyc.kb.Context;
import com.cyc.kb.KbCollection;
import com.cyc.kb.Variable;
import com.cyc.kb.client.KbCollectionImpl;
import com.cyc.kb.client.KbPredicateImpl;
import com.cyc.kb.exception.KbException;

/**
 * THIS CLASS IS NOT SUPPORTED IN KB API 1.0.
 * @author vijay
 */
public class SpecializationRestrictedVariable extends RestrictedVariable {

  private KbCollection collection;

  public SpecializationRestrictedVariable(Context ctx, KbCollection c) throws KbException {
    super(ctx, ((KbCollectionImpl) c).getVariable(), new Object[]{KbPredicateImpl.get("genls"), ((KbCollectionImpl) c).getVariable(), c});
    this.setCollection(c);
  }
  
  public SpecializationRestrictedVariable(Context ctx, KbCollection c, Variable v) throws KbException {
    super(ctx, v, new Object[]{KbPredicateImpl.get("genls"), v, c});
    this.setCollection(c);
  }

  public KbCollection getCollection() {
    return collection;
  }

  public void setCollection(KbCollection collection) {
    this.collection = collection;
  }
}
