/*
 */
package com.cyc.query;

/*
 * #%L
 * File: TestUtils.java
 * Project: Query API
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
import com.cyc.query.exception.QueryConstructionException;
import com.cyc.base.CycAccess;
import com.cyc.base.CycAccessManager;
import com.cyc.kb.Context;
import com.cyc.kb.Sentence;
import com.cyc.kb.Variable;
import com.cyc.kb.client.Constants;
import com.cyc.kb.client.SentenceImpl;
import com.cyc.kb.client.VariableImpl;
import com.cyc.kb.config.KBAPIConfiguration;
import com.cyc.kb.exception.KBApiException;
import com.cyc.kb.exception.KBApiRuntimeException;


/**
 *
 * @author baxter
 */
public class TestUtils {

  public static Query q;

  public static Sentence xIsaBird() throws KBApiException {
    return new SentenceImpl(Constants.isa(), X, QueryApiTestConstants.bird());
  }

  private static QueryApiTestConstants testConstants() throws KBApiRuntimeException {
    return QueryApiTestConstants.getInstance();
  }
  public static CycAccess cyc = null;
  public static Variable X;
  public static Sentence xIsaEmu;
  public static Context inferencePSC;

  private static boolean initialized = false;

  public static synchronized void ensureConstantsInitialized() {
    if (initialized == false) {
      try {
        KBAPIConfiguration.setShouldTranscriptOperations(false);
        cyc = CycAccessManager.getCurrentAccess();
        System.out.println("current cyc: " + cyc);
        X = new VariableImpl("?X");
        xIsaEmu = new SentenceImpl(Constants.isa(), X, testConstants().emu());
        inferencePSC = Constants.inferencePSCMt();
        initialized = true;
      } catch (Exception e) {
        throw new RuntimeException("Failed to initialize test constants.", e);
      }
    }
  }

  static Query constructXIsaBirdQuery() throws QueryConstructionException {
    try {
      return new Query(xIsaBird(), inferencePSC);
    } catch (KBApiException ex) {
      throw new QueryConstructionException(ex);
    }
  }

  static protected void closeTestQuery() {
    if (q != null) {
      q.close();
    }
  }
}
