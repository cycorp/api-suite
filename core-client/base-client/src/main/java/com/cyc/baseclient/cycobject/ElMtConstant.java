package com.cyc.baseclient.cycobject;

/*
 * #%L
 * File: ElMtConstant.java
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

import com.cyc.base.cycobject.CycConstant;
import com.cyc.base.cycobject.ElMt;
import com.cyc.base.exception.BaseClientRuntimeException;
import com.cyc.baseclient.CycObjectFactory;
import com.cyc.kb.Context;

/**
 * Provides the container for the ElMt CycConstantImpl (Epistemlogical Level Microtheory
 Constant).<p>
 *
 * @version $Id: ElMtConstant.java 163503 2016-01-11 23:42:19Z nwinant $
 * @author Tony Brusseau
 */
public class ElMtConstant extends CycConstantImpl implements ElMt {
  
  static final long serialVersionUID = -2405506745680227189L;
  
  /** Privately creates a new instance of ElMtConstant 
   * deprecated
   */
  private ElMtConstant(CycConstant cycConstant) {
    super(cycConstant.getName(), cycConstant.getGuid());
  }
    
  /**
   * Returns a new ElMtConstant given a CycConstantImpl.  Note, use the
 factory method in the CycClient to create these.
   */
  public static ElMtConstant makeElMtConstant(CycConstant cycConstant) {
    CycObjectFactory.removeCaches(cycConstant);
    ElMtConstant elmtConstant = new ElMtConstant(cycConstant);
    CycObjectFactory.addCycConstantCache(cycConstant);
    return elmtConstant;
  }
  
  public static boolean isCompatible(Context context) {
    final Object core = context.getCore();
    return (core instanceof ElMtConstant) || (core instanceof CycConstant);
  }
  
  public static ElMtConstant fromContext(Context context) {
    final Object core = context.getCore();
    if (core instanceof ElMtConstant) {
      return (ElMtConstant) core;
    } else if (core instanceof CycConstant) {
     return makeElMtConstant((CycConstant) core);
    }
    throw new BaseClientRuntimeException("Could not create " + ElMtConstant.class.getSimpleName() 
            + " from " + core);
  }
  
}
