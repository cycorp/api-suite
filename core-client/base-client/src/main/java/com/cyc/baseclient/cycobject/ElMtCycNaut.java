package com.cyc.baseclient.cycobject;

/*
 * #%L
 * File: ElMtCycNaut.java
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

import com.cyc.base.cycobject.ElMt;
import com.cyc.base.exception.BaseClientRuntimeException;
import com.cyc.kb.Context;
import java.util.List;

/**
 * Provides the container for the ElMt NAUT (Epistemlogical Level Microtheory
 * Non Atomic Un-reified Term).<p>
 *
 * @version $Id: ElMtCycNaut.java 163503 2016-01-11 23:42:19Z nwinant $
 * @author Tony Brusseau
 */

public class ElMtCycNaut extends NautImpl implements ElMt {
  
  /** Creates a new instance of ElMtCycNaut */
  private ElMtCycNaut(List terms) {
    super(terms);
  }
  
  /**
   * Returns a new ElMtCycNaut.  Note, use the
   * factory method in the CycAccess to create these.
   */
  public static ElMtCycNaut makeElMtCycNaut(List terms) {
    return new ElMtCycNaut(terms);
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
  
  public static boolean isCompatible(Context context) {
    final Object core = context.getCore();
    return (core instanceof ElMtCycNaut) || (core instanceof List);
  }

  public static ElMtCycNaut fromContext(Context context) {
    final Object core = context.getCore();
    if (core instanceof ElMtCycNaut) {
      return (ElMtCycNaut) core;
    } else if (core instanceof List) {
     return makeElMtCycNaut((List) core);
    }
    throw new BaseClientRuntimeException("Could not create " + ElMtCycNaut.class.getSimpleName() 
            + " from " + core);
  }
}
