package com.cyc.baseclient.cycobject;

/*
 * #%L
 * File: ElMtConstant.java
 * Project: Base Client
 * %%
 * Copyright (C) 2013 - 2015 Cycorp, Inc.
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
import com.cyc.baseclient.CycObjectFactory;

/**
 * Provides the container for the ELMt CycConstantImpl (Epistemlogical Level Microtheory
 Constant).<p>
 *
 * @version $Id: ElMtConstant.java 162904 2015-12-02 18:35:34Z nwinant $
 * @author Tony Brusseau
 */
public class ElMtConstant extends CycConstantImpl implements ElMt {
  
  static final long serialVersionUID = -2405506745680227189L;
  
  /** Privately creates a new instance of ELMtConstant 
   * deprecated
   */
  private ElMtConstant(CycConstant cycConstant) {
    super(cycConstant.getName(), cycConstant.getGuid());
  }
    
  /**
   * Returns a new ELMtConstant given a CycConstantImpl.  Note, use the
 factory method in the CycClient to create these.
   */
  public static ElMtConstant makeELMtConstant(CycConstant cycConstant) {
    CycObjectFactory.removeCaches(cycConstant);
    ElMtConstant elmtConstant = new ElMtConstant(cycConstant);
    CycObjectFactory.addCycConstantCache(cycConstant);
    return elmtConstant;
  }
}
