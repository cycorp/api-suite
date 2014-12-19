package com.cyc.baseclient.cycobject;

/*
 * #%L
 * File: ELMtCycNaut.java
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

import com.cyc.base.cycobject.ELMt;
import java.util.List;

/**
 * Provides the container for the ELMt NAUT (Epistemlogical Level Microtheory
 * Non Atomic Un-reified Term).<p>
 *
 * @version $Id: ELMtCycNaut.java 155483 2014-12-10 21:56:51Z nwinant $
 * @author Tony Brusseau
 */

public class ELMtCycNaut extends NautImpl implements ELMt {
  
  /** Creates a new instance of ELMtCycNaut */
  private ELMtCycNaut(List terms) {
    super(terms);
  }
  
  /**
   * Returns a new ELMtCycNaut.  Note, use the
   * factory method in the CycAccess to create these.
   */
  public static ELMtCycNaut makeELMtCycNaut(List terms) {
    return new ELMtCycNaut(terms);
  }
  /**
   * Returns this object in a form suitable for use as an <tt>String</tt> api expression value.
   *
   * @return this object in a form suitable for use as an <tt>String</tt> api expression value
   */
  //@ToDo uncomment this when (list* issues has been resolved in cyclify() of CycList
  /*public String stringApiValue() {
    return "'" + super.stringApiValue();
  }*/

}
