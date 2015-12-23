/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cyc.xml.query;

/*
 * #%L
 * File: Utils.java
 * Project: Query Client
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

import com.cyc.base.exception.CycConnectionException;
import com.cyc.base.cycobject.CycConstant;
import com.cyc.base.cycobject.ElMt;
import com.cyc.baseclient.cycobject.ElMtConstant;
import com.cyc.kb.client.Constants;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbException;
import com.cyc.kb.exception.KbTypeException;
import com.cyc.query.QueryImpl;
import com.cyc.query.QueryAnswer;
import com.cyc.query.QueryTestConstants;
import com.cyc.query.exception.QueryConstructionException;
import com.cyc.session.exception.SessionException;

/**
 *
 * @author baxter
 */
public class Utils {

  public static void setup() throws CycConnectionException, QueryConstructionException, KbTypeException, CreateException,
          KbException, SessionException {
    QueryImpl query = new QueryImpl(QueryTestConstants.getInstance().genlsEmuBird, Constants.inferencePSCMt());
    query.retainInference();
    answer = query.getAnswer(0);
    System.out.println("Performed inference. Got answer: " + answer);
  }
  public static QueryAnswer answer = null;
  public static ElMt domainMt
          = ElMtConstant.makeELMtConstant((CycConstant) Constants.baseKbMt().getCore());
  public static ElMt languageMt = ElMtConstant.makeELMtConstant((CycConstant) QueryTestConstants.getInstance().englishParaphraseMt.getCore());

  public static void teardown() {
    try {
      answer.getId().getInferenceIdentifier().close();
    } catch (Exception e) {
    }
  }
}
