package com.cyc.baseclient.cycobject;

/*
 * #%L
 * File: ELMtConstant.java
 * Project: Base Client
 * %%
 * Copyright (C) 2013 - 2014 Cycorp, Inc.
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
import com.cyc.base.cycobject.ELMt;
import com.cyc.baseclient.CycObjectFactory;

/**
 * Provides the container for the ELMt CycConstantImpl (Epistemlogical Level Microtheory
 Constant).<p>
 *
 * @version $Id: ELMtConstant.java 155483 2014-12-10 21:56:51Z nwinant $
 * @author Tony Brusseau
 */
public class ELMtConstant extends CycConstantImpl implements ELMt {
  
  static final long serialVersionUID = -2405506745680227189L;
  
  /** Privately creates a new instance of ELMtConstant 
   * deprecated
   */
  private ELMtConstant(CycConstant cycConstant) {
    super(cycConstant.getName(), cycConstant.getGuid());
  }
    
  /**
   * Returns a new ELMtConstant given a CycConstantImpl.  Note, use the
 factory method in the CycClient to create these.
   */
  public static ELMtConstant makeELMtConstant(CycConstant cycConstant) {
    CycObjectFactory.removeCaches(cycConstant);
    ELMtConstant elmtConstant = new ELMtConstant(cycConstant);
    CycObjectFactory.addCycConstantCache(cycConstant);
    return elmtConstant;
  }
}
