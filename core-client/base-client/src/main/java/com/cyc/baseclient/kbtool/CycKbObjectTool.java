/*
 * Copyright 2016 Cycorp, Inc..
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
package com.cyc.baseclient.kbtool;

/*
 * #%L
 * File: CycKbObjectTool.java
 * Project: Base Client
 * %%
 * Copyright (C) 2013 - 2016 Cycorp, Inc.
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

import com.cyc.base.CycAccess;
import com.cyc.base.cycobject.CycConstant;
import com.cyc.base.cycobject.CycList;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.DenotationalTerm;
import com.cyc.base.cycobject.ElMt;
import com.cyc.base.cycobject.Fort;
import com.cyc.base.cycobject.Nart;
import com.cyc.base.cycobject.Naut;
import com.cyc.base.exception.BaseClientRuntimeException;
import com.cyc.baseclient.AbstractKbTool;
import com.cyc.baseclient.cycobject.ElMtConstant;
import com.cyc.baseclient.cycobject.ElMtCycNaut;
import com.cyc.baseclient.cycobject.ElMtNart;
import com.cyc.kb.Context;
import com.cyc.base.kbtool.KbObjectTool;
import com.cyc.kb.KbObject;
import com.cyc.kb.KbTerm;

/**
 * Provides tools for converting KbObjects to Base Client objects.
 * 
 * @author nwinant
 */
public class CycKbObjectTool extends AbstractKbTool implements KbObjectTool {
  
  public CycKbObjectTool(CycAccess client) {
    super(client);
  }
  
  
  // Public
  
  @Override
  public CycConstant convertToConstant(KbTerm term) {
    final Object core = term.getCore();
    if (core instanceof CycConstant) {
      return (CycConstant) core;
    }
    // TODO: flesh out more cases - nwinant, 2016-01-11
    throw new BaseClientRuntimeException("Could not create " + CycConstant.class.getSimpleName() 
            + " from " + term);
  }
  
  @Override
  public CycList convertToCycList(KbObject kbObject) {
    final Object core = kbObject.getCore();
    if (core instanceof CycList) {
      return (CycList) core;
    }
    // TODO: flesh out more cases - nwinant, 2016-01-11
    throw new BaseClientRuntimeException("Could not create " + CycList.class.getSimpleName() 
            + " from " + kbObject);
  }
  
  @Override
  public CycObject convertToCycObject(KbObject kbObject) {
    final Object core = kbObject.getCore();
    if (core instanceof CycObject) {
      return (CycObject) core;
    }
    // TODO: flesh out more cases - nwinant, 2016-01-11
    throw new BaseClientRuntimeException("Could not create " + CycObject.class.getSimpleName() 
            + " from " + kbObject);
  }

  @Override
  public DenotationalTerm convertToDenotationalTerm(KbTerm term) {
    final Object core = term.getCore();
    if (core instanceof DenotationalTerm) {
      return (DenotationalTerm) core;
    }
    // TODO: flesh out more cases - nwinant, 2016-01-11
    throw new BaseClientRuntimeException("Could not create "
            + DenotationalTerm.class.getSimpleName() + " from " + term);
  }
  
  @Override
  public ElMt convertToElMt(Context context) {
    final Object core = context.getCore();
    if (core instanceof ElMt) {
      return (ElMt) core;
    }
    if (ElMtConstant.isCompatible(context)) {
      return ElMtConstant.fromContext(context);
    } else if (ElMtNart.isCompatible(context)) {
      return ElMtNart.fromContext(context);
    } else if (ElMtCycNaut.isCompatible(context)) {
      return ElMtCycNaut.fromContext(context);
    }
    throw new BaseClientRuntimeException("Could not create " + ElMt.class.getSimpleName() 
            + " from " + context);
  }
  
  @Override
  public Fort convertToFort(KbTerm term) {
    final Object core = term.getCore();
    if (core instanceof Fort) {
      return (Fort) core;
    }
    // TODO: flesh out more cases - nwinant, 2016-01-11
    throw new BaseClientRuntimeException("Could not create " + Fort.class.getSimpleName() 
            + " from " + term);
  }
  
  @Override
  public Nart convertToNart(KbTerm term) {
    final Object core = term.getCore();
    if (core instanceof Nart) {
      return (Nart) core;
    }
    // TODO: flesh out more cases - nwinant, 2016-01-11
    throw new BaseClientRuntimeException("Could not create " + Nart.class.getSimpleName() 
            + " from " + term);
  }

  @Override
  public Naut convertToNaut(KbTerm term) {
    final Object core = term.getCore();
    if (core instanceof Naut) {
      return (Naut) core;
    }
    // TODO: flesh out more cases - nwinant, 2016-01-11
    throw new BaseClientRuntimeException("Could not create " + Naut.class.getSimpleName() 
            + " from " + term);
  }
  
}
