package com.cyc.baseclient.cycobject;

/*
 * #%L
 * File: ElMtNart.java
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

import com.cyc.base.cycobject.Nart;
import com.cyc.base.cycobject.ElMt;
import com.cyc.base.exception.BaseClientRuntimeException;
import com.cyc.kb.Context;

/**
 * Provides the container for the ElMt NART (Epistemlogical Level Microtheory
 * Non Atomic Reified Term).<p>
 *
 * @version $Id: ElMtNart.java 163503 2016-01-11 23:42:19Z nwinant $
 * @author Tony Brusseau
 */
public class ElMtNart extends NartImpl implements ElMt {
  
  /** Creates a new instance of ElMtNart */
  private ElMtNart(Nart nart) {
    super(nart.toCycList());
  }
  
  /**
   * Returns a new ElMtNart given a Nart.  Note, use the
   * factory method in the CycAccess to create these.
   */
  public static ElMtNart makeElMtNart(Nart nart) {
    return new ElMtNart(nart);
  }
  
  public static boolean isCompatible(Context context) {
    final Object core = context.getCore();
    return (core instanceof ElMtNart) || (core instanceof Nart);
  }
  
  public static ElMtNart fromContext(Context context) {
    final Object core = context.getCore();
    if (core instanceof ElMtNart) {
      return (ElMtNart) core;
    } else if (core instanceof Nart) {
     return makeElMtNart((Nart) core);
    }
    throw new BaseClientRuntimeException("Could not create " + ElMtNart.class.getSimpleName() 
            + " from " + core);
  }
}
