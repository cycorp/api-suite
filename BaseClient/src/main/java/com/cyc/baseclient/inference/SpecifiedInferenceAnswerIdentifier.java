package com.cyc.baseclient.inference;

/*
 * #%L
 * File: SpecifiedInferenceAnswerIdentifier.java
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

import com.cyc.base.inference.InferenceAnswerIdentifier;
import com.cyc.base.inference.InferenceIdentifier;
import com.cyc.base.cycobject.CycList;
import com.cyc.baseclient.cycobject.CycArrayList;

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
    return cycListApiValue().stringApiValue();
  }

  @Override
  public CycList<Integer> cycListApiValue() {
    return CycArrayList.makeCycList(inferenceID.getProblemStoreID(),
            inferenceID.getInferenceID(), answerID);
  }

  @Override
  public int getAnswerID() {
    return answerID;
  }

  @Override
  public InferenceIdentifier getInferenceID() {
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
  private InferenceIdentifier inferenceID;
  private int answerID;
}
