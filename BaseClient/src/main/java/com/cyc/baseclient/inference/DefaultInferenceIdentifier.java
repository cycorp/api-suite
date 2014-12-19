package com.cyc.baseclient.inference;

/*
 * #%L
 * File: DefaultInferenceIdentifier.java
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
import com.cyc.base.CycConnectionException;
import com.cyc.base.inference.InferenceIdentifier;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.cyc.base.CycApiException;
import com.cyc.baseclient.CycObjectFactory;
import static com.cyc.baseclient.api.SubLAPIHelper.makeNestedSubLStmt;
import static com.cyc.baseclient.api.SubLAPIHelper.makeSubLStmt;

/**
 * An object that identifies an inference object in a Cyc image.
 *
 * @author baxter
 */
public class DefaultInferenceIdentifier implements InferenceIdentifier {

  private int problemStoreID;

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final DefaultInferenceIdentifier other = (DefaultInferenceIdentifier) obj;
    if (this.problemStoreID != other.problemStoreID) {
      return false;
    }
    if (this.inferenceID != other.inferenceID) {
      return false;
    }
    if (this.cyc != other.cyc && (this.cyc == null || !this.cyc.equals(other.cyc))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 71 * hash + this.problemStoreID;
    hash = 71 * hash + this.inferenceID;
    hash = 71 * hash + (this.cyc != null ? this.cyc.hashCode() : 0);
    return hash;
  }
  private int inferenceID;
  private CycAccess cyc;

  public CycAccess getCycAccess() {
    return cyc;
  }

  public int getInferenceID() {
    return inferenceID;
  }

  public int getProblemStoreID() {
    return problemStoreID;
  }

  public Integer getFirstProofId(Integer answerId) {
    Integer proofId = null;
    try {
      proofId = cyc.converse().converseInt(makeSubLStmt("proof-suid", makeNestedSubLStmt(
              "inference-answer-justification-first-proof",
              makeNestedSubLStmt("inference-answer-first-justification",
              makeNestedSubLStmt("find-inference-answer-by-ids",
              getProblemStoreID(), getInferenceID(), answerId)))));
    } catch (CycConnectionException ex) {
      logSevereException(ex);
    } catch (CycApiException ex) {
      logSevereException(ex);
    }
    return proofId;
  }

  private static void logSevereException(Exception ex) {
    Logger.getLogger(DefaultInferenceIdentifier.class.getName()).log(Level.SEVERE, null,
            ex);
  }

  @Override
  public String toString() {
    return "Inference " + inferenceID + " in Problem Store " + problemStoreID;
  }

  public String stringApiValue() {
    return "(find-inference-by-ids " + Integer.toString(problemStoreID) + " " + Integer.toString(
            inferenceID) + ")";
  }

  public DefaultInferenceIdentifier(int problemStoreID, int inferenceID, CycAccess cyc) {
    this.problemStoreID = problemStoreID;
    this.inferenceID = inferenceID;
    this.cyc = cyc;
  }

  public DefaultInferenceIdentifier(int problemStoreID, int inferenceID) {
    this(problemStoreID, inferenceID, null);
  }

  public void close() {
    try {
      getCycAccess().converse().converseVoid(
              "(destroy-inference-and-problem-store " + stringApiValue() + ")");
    } catch (CycConnectionException ex) {
      logSevereException(ex);
    } catch (CycApiException ex) {
      logSevereException(ex);
    }
  }

  /**
   * Interrupt this inference.
   *
   * @param patience Give inference process this many seconds to halt gracefully,
   * after which terminate it with prejudice. A null value indicates infinite patience.
   */
  public void interrupt(final Integer patience) {
    try {
      getCycAccess().converse().converseVoid(
              "(inference-interrupt-external " + stringApiValue() + " "
              + ((patience == null) ? CycObjectFactory.nil : patience)
              + ")");
    } catch (CycConnectionException ex) {
      logSevereException(ex);
    } catch (CycApiException ex) {
      logSevereException(ex);
    }
  }

  public String toXML() {
    return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<inferenceIdentifier>"
            + "<problemStore id=\"" + problemStoreID + "\"/>"
            + "<inference id=\"" + inferenceID + "\"/>"
            + "</inferenceIdentifier>";
  }
}
