package com.cyc.kb.quant;

/*
 * #%L
 * File: QuantifiedInstanceRestrictedVariable.java
 * Project: KB API
 * %%
 * Copyright (C) 2013 - 2014 Cycorp, Inc
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

import com.cyc.base.cycobject.CycObject;
import com.cyc.kb.KBCollection;
import com.cyc.kb.client.ContextImpl;
import com.cyc.kb.client.QuantifierImpl;
import com.cyc.kb.exception.KBApiException;


/**
 * THIS CLASS IS NOT SUPPORTED IN KB API 1.0.
 * @author vijay
 */
public class QuantifiedInstanceRestrictedVariable extends QuantifiedRestrictedVariable {

  private KBCollection collection;
  
  protected QuantifiedInstanceRestrictedVariable(CycObject core) throws KBApiException {
    super(core);
    // Should we attempt to set quantifier and collection from
    // the CycFormulaSentence.
  }
  
  /**
   *
   * @param ctx
   * @param col
   */
  public QuantifiedInstanceRestrictedVariable (QuantifierImpl q, KBCollection col) throws KBApiException {
    super(q, col.toInstanceRestrictedVariable());
    this.setCollection(col);
  }

  public KBCollection getCollection() {
    return collection;
  }

  private void setCollection(KBCollection collection) {
    this.collection = collection;
  }
  
  @Override
  public InstanceRestrictedVariable getRestrictedVariable() {
    return InstanceRestrictedVariable.class.cast(super.getRestrictedVariable());
  }
}
