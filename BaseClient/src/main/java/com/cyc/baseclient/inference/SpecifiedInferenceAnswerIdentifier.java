package com.cyc.baseclient.inference;

/*
 * #%L
 * File: SpecifiedInferenceAnswerIdentifier.java
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

import com.cyc.query.InferenceAnswerIdentifier;
import com.cyc.query.InferenceIdentifier;
import com.cyc.base.cycobject.CycList;
import com.cyc.baseclient.cycobject.CycArrayList;
import com.cyc.session.CycSession;

/**
 * An identifier for an individual answer within a specific Cyc inference.
 *
 * @author baxter
 */
public class SpecifiedInferenceAnswerIdentifier implements InferenceAnswerIdentifier {

  public SpecifiedInferenceAnswerIdentifier(InferenceIdentifier inferenceID, int answerID) {
    this.inferenceID = inferenceID;
    this.answerID = answerID;
  }

  @Override
  public String toString() {
    return "Answer " + answerID + " for " + inferenceID;
  }

  @Override
  public String stringApiValue() {
    return ((CycList<Integer>)cycListApiValue()).stringApiValue();
  }

  @Override
  public Object cycListApiValue() {
    return CycArrayList.makeCycList(inferenceID.getProblemStoreID(),
            inferenceID.getInferenceID(), answerID);
  }

  @Override
  public int getAnswerID() {
    return answerID;
  }

  @Override
  public InferenceIdentifier getInferenceIdentifier() {
    return inferenceID;
  }

  public static boolean possiblyInferenceAnswerSignature(Object obj) {
    if (obj instanceof CycArrayList) {
      final CycArrayList cycList = (CycArrayList) obj;
      if (cycList.size() == 3) {
        try {
          return (Integer.valueOf(cycList.get(0).toString()) >= 0
                  && Integer.valueOf(cycList.get(1).toString()) >= 0
                  && Integer.valueOf(cycList.get(2).toString()) >= 0);
        } catch (NumberFormatException e) {
          return false;
        }
      }
    }
    return false;
  }
  private final InferenceIdentifier inferenceID;
  private final int answerID;

  @Override
  public CycSession getSession() {
    return inferenceID.getSession();
  }
}
