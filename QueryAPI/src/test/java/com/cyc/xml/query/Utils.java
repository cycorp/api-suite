/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cyc.xml.query;

/*
 * #%L
 * File: Utils.java
 * Project: Query API Implementation
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

import com.cyc.base.CycAccess;
import com.cyc.base.CycAccessManager;
import com.cyc.base.CycConnectionException;
import com.cyc.base.cycobject.CycConstant;
import com.cyc.base.cycobject.ELMt;
import com.cyc.baseclient.cycobject.ELMtConstant;
import com.cyc.kb.client.Constants;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KBApiException;
import com.cyc.kb.exception.KBTypeException;
import com.cyc.query.QueryImpl;
import com.cyc.query.QueryAnswer;
import com.cyc.query.QueryApiTestConstants;
import com.cyc.query.exception.QueryConstructionException;
import com.cyc.session.SessionApiException;

/**
 *
 * @author baxter
 */
public class Utils {

  static CycAccess cyc = null;

  public static void setup() throws CycConnectionException, QueryConstructionException, KBTypeException, CreateException,
          KBApiException, SessionApiException {
    if (cyc == null) {
      cyc = CycAccessManager.getAccess();
    }
    QueryImpl query = new QueryImpl(QueryApiTestConstants.getInstance().genlsEmuBird, Constants.inferencePSCMt());
    query.retainInference();
    answer = query.getAnswer(0);
    System.out.println("Performed inference. Got answer: " + answer);
  }
  public static QueryAnswer answer = null;
  public static ELMt domainMt
          = ELMtConstant.makeELMtConstant((CycConstant) Constants.baseKbMt().getCore());
  public static ELMt languageMt = ELMtConstant.makeELMtConstant(
          (CycConstant) QueryApiTestConstants.getInstance().englishParaphraseMt.getCore());

  public static void teardown() {
    try {
      answer.getId().getInferenceIdentifier().close();
    } catch (Exception e) {
    }
  }
}
