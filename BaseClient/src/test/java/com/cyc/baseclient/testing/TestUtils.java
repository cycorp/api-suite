package com.cyc.baseclient.testing;

/*
 * #%L
 * File: TestUtils.java
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

import com.cyc.base.CycAccess;
import com.cyc.base.CycAccessManager;
import com.cyc.base.BaseClientRuntimeException;
import com.cyc.base.CycConnectionException;
import com.cyc.session.SessionApiException;

/**
 *
 * @author baxter
 */
public class TestUtils {

  private static final KBPopulator populator = new KBPopulator();
  
  synchronized public static void ensureKBPopulated(CycAccess access) throws CycConnectionException {
    if (!populator.isAlreadyCalledForKB(access)){
      populator.populate(access);
    }
  }
  
  public static boolean isCurrentKBAlreadyPopulated() throws CycConnectionException {
    return populator.isAlreadyCalledForKB(getCyc());
  }
  
  public static void ensureTestEnvironmentInitialized() throws SessionApiException, CycConnectionException {
      CycAccess access = CycAccessManager.getCurrentAccess();
      ensureKBPopulated(access);
  }
  
  public static CycAccess getCyc() throws CycConnectionException {
    CycAccess cyc = null;
    try {
      cyc = CycAccessManager.getCurrentAccess();
      ensureKBPopulated(cyc);
    } catch (SessionApiException ex) {
      throw new BaseClientRuntimeException(ex);
    } 
//    throw new UnsupportedOperationException("FIXME NATHAN SERIOUSLY.");0
    return cyc;
  }
}
