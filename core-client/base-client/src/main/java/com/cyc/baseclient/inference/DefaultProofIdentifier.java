package com.cyc.baseclient.inference;

/*
 * #%L
 * File: DefaultProofIdentifier.java
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

import com.cyc.base.CycAccess;
import com.cyc.query.InferenceIdentifier;
import com.cyc.query.ProofIdentifier;
import com.cyc.session.CycSession;
import org.slf4j.LoggerFactory;

/**
 * An object that identifies an inference object in a Cyc image.
 *
 * @author baxter
 */
public class DefaultProofIdentifier implements ProofIdentifier {

  private int problemStoreId;
  private Integer inferenceId; //technically optional, so use nullable Integer instead of int.
  private int proofId;
  private InferenceIdentifier inferenceIdentifier;
  private CycAccess cyc;
  private CycSession session;
  
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final DefaultProofIdentifier other = (DefaultProofIdentifier) obj;
    if (this.problemStoreId != other.problemStoreId) {
      return false;
    }
    if (!this.inferenceId.equals(inferenceId)) {
      return false;
    }
    if (this.proofId != other.proofId) {
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
    hash = 71 * hash + this.problemStoreId;
    hash = 71 * hash + this.inferenceId;
    hash = 71 * hash + this.proofId;
    hash = 71 * hash + (this.cyc != null ? this.cyc.hashCode() : 0);
    return hash;
  }

  public CycAccess getCycAccess() {
    return cyc;
  }

  @Override
  public int getInferenceId() {
    return inferenceId;
  }

  @Override
  public int getProblemStoreId() {
    return problemStoreId;
  }

  @Override
  public int getProofId() {
    return proofId;
  }

  @Override
  public InferenceIdentifier getInferenceIdentifier() {
    if (inferenceIdentifier == null) {
      inferenceIdentifier = new DefaultInferenceIdentifier(problemStoreId, inferenceId, session);
    }
    return inferenceIdentifier;
  }
  
  private static void logSevereException(Exception ex) {
    LoggerFactory.getLogger(DefaultProofIdentifier.class.getName()).warn(null, ex);
  }

  @Override
  public String toString() {
    return "Proof " + proofId + " in Problem Store " + problemStoreId;
  }

  @Override
  public String stringApiValue() {
    return "(find-proof-by-ids " + Integer.toString(problemStoreId) + " " + Integer.toString(proofId) + ")";
  }

  public DefaultProofIdentifier(int problemStoreId, Integer inferenceId, int proofId, CycSession session) {
    this.problemStoreId = problemStoreId;
    this.inferenceId = inferenceId;
    this.proofId = proofId;
    this.session = session;
  }

  public DefaultProofIdentifier(int problemStoreID, int inferenceID, int proofId) {
    this(problemStoreID, inferenceID, proofId, null);
  }

  public DefaultProofIdentifier(int problemStoreID, int proofId) {
    this(problemStoreID, null, proofId, null);
  }

  public void close() {
    throw new UnsupportedOperationException();
//    try {
//      ((CycAccessSession)session).getAccess().converse().converseVoid(
//              "(destroy-inference-and-problem-store " + stringApiValue() + ")");
//    } catch (CycConnectionException ex) {
//      logSevereException(ex);
//    } catch (CycApiException ex) {
//      logSevereException(ex);
//    }
  }


  public String toXML() {
    return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<proofIdentifier>"
            + "<problemStore id=\"" + problemStoreId + "\"/>"
            + "<proof id=\"" + proofId + "\"/>"
            + "</inferenceIdentifier>";
  }

  @Override
  public CycSession getSession() {
    return session;
  }
}
