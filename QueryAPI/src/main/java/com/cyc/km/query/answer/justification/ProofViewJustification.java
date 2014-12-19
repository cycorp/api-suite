package com.cyc.km.query.answer.justification;

/*
 * #%L
 * File: ProofViewJustification.java
 * Project: Query API
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
import static com.cyc.baseclient.CycObjectFactory.makeCycSymbol;
import static com.cyc.baseclient.api.SubLAPIHelper.makeSubLStmt;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBException;

import com.cyc.base.CycAccess;
import com.cyc.base.CycApiException;
import com.cyc.base.CycConnectionException;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.CycSymbol;
import com.cyc.base.cycobject.DenotationalTerm;
import com.cyc.base.cycobject.ELMt;
import com.cyc.base.inference.InferenceAnswer;
import com.cyc.base.inference.InferenceAnswerIdentifier;
import com.cyc.base.inference.InferenceIdentifier;
import com.cyc.base.justification.Justification;
import com.cyc.baseclient.CycObjectFactory;
import com.cyc.baseclient.api.SubLAPIHelper;
import com.cyc.baseclient.xml.cycml.CycMLDecoder;
import com.cyc.baseclient.xml.cycml.Paraphrase;
import com.cyc.query.QueryAnswer;
import com.cyc.query.exception.QueryApiRuntimeException;
import com.cyc.xml.query.Content;
import com.cyc.xml.query.ProofView;
import com.cyc.xml.query.ProofViewEntry;
import com.cyc.xml.query.ProofViewMarshaller;
import com.cyc.xml.query.ProofViewUnmarshaller;
import com.cyc.xml.query.SubEntries;

/**
 * A {@link Justification} backed by a {@link ProofView}.
 *
 * Generally, a new ProofViewJustification object is constructed, parameters are
 * set as desired, then {@link #populate()} is called to flesh it out. Then you
 * can get its root note (via {@link #getRoot()}), and display it as an
 * interactive tree structure.
 *
 * @author baxter
 */
public class ProofViewJustification implements Justification {

  /**
   * Create a new, unpopulated justification for the specified answer.
   *
   * @param answer
   * @throws CycConnectionException if there is a problem talking to Cyc.
   * @see ProofViewJustification#populate()
   */
  public ProofViewJustification(QueryAnswer answer) throws CycConnectionException {
    this.answer = answer;
    final InferenceAnswerIdentifier answerID = answer.getId();
    final InferenceIdentifier inferenceID = answerID.getInferenceID();
    this.cyc = inferenceID.getCycAccess();
    this.proofViewId = cyc.converse().converseInt(makeSubLStmt(
            "get-new-empty-proof-view-id",
            inferenceID.getProblemStoreID(), inferenceID.getInferenceID(),
            answerID.getAnswerID()));
    try {
      this.proofViewUnmarshaller = new ProofViewUnmarshaller();
    } catch (JAXBException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public InferenceAnswer getAnswer() {
    throw new UnsupportedOperationException("Use getQueryAnswer() instead.");
  }

  public QueryAnswer getQueryAnswer() {
    return answer;
  }

  /**
   * Flesh out this justification, setting its root node and tree structure
   * underneath the root.
   */
  @Override
  public void populate() {
    synchronized (lock) {
      requireNotPopulated();
      try {
        converseVoid(makeSubLStmt("proof-view-id-populate", proofViewId));
      } catch (Exception e) {
        throw new RuntimeException("Failed to populate proof view.", e);
      }
      isPopulated = true;
    }
  }

  @Override
  public String toString() {
    return "Proof View for " + answer;
  }

  /**
   * Get the microtheory from which semantic checks are performed to construct
   * this justification.
   *
   * @return the domain microtheory
   * @throws CycConnectionException if there is a problem talking to Cyc.
   * @see #setDomainMt(ELMt)
   */
  public ELMt getDomainMt() throws CycConnectionException {
    synchronized (lock) {
      if (domainMt == null) {
        final CycObject mtObject = converseCycObject(makeSubLStmt(
                "get-proof-view-domain-mt", proofViewId));
        domainMt = mtFromObject(mtObject);
      }
      return domainMt;
    }
  }

  private ELMt mtFromObject(final CycObject mtObject) throws CycConnectionException {
    return cyc.getObjectTool().makeELMt(mtObject);
  }

  /**
   * Set the microtheory from which semantic checks are performed to construct
   * this justification.
   *
   * @param domainMt the domain microtheory
   * @throws CycConnectionException if there is a problem talking to Cyc.
   * @see #getDomainMt()
   */
  public void setDomainMt(ELMt domainMt) throws CycConnectionException {
    synchronized (lock) {
      requireNotPopulated();
      converseVoid(makeSubLStmt("set-proof-view-domain-mt", proofViewId,
              domainMt));
      this.domainMt = domainMt;
    }
  }

  /**
   * Should we include a detailed, drill-down section in this justification?
   *
   * @return true iff such a tree is or should be included
   * @throws CycConnectionException
   * @see #setIncludeDetails(boolean)
   */
  public boolean isIncludeDetails() throws CycConnectionException {
    synchronized (lock) {
      if (includeDetails == null) {
        includeDetails = converseBoolean(makeSubLStmt(
                "get-proof-view-include-details", proofViewId));
      }
      return includeDetails;
    }
  }

  /**
   * Indicate that this justification should or should not include a details
   * section.
   *
   * @param includeDetails true iff it should
   * @throws CycConnectionException
   * @see #isIncludeDetails()
   */
  public void setIncludeDetails(boolean includeDetails) throws CycConnectionException {
    synchronized (lock) {
      requireNotPopulated();
      converseVoid(makeSubLStmt("set-proof-view-include-details",
              proofViewId,
              includeDetails));
      this.includeDetails = includeDetails;
    }
  }

  /**
   * Should we include a linear, syllogistic section in this justification?
   *
   * @return true iff such a section is or should be included
   * @throws CycConnectionException
   * @see #setIncludeLinear(boolean)
   */
  public boolean isIncludeLinear() throws CycConnectionException {
    synchronized (lock) {
      if (includeLinear == null) {
        includeLinear = converseBoolean(makeSubLStmt(
                "get-proof-view-include-linear", proofViewId));
      }
      return includeLinear;
    }
  }

  /**
   * Indicate that this justification should or should not include a linear
   * section.
   *
   * @param includeLinear true iff it should
   * @throws CycConnectionException
   * @see #isIncludeLinear()
   */
  public void setIncludeLinear(boolean includeLinear) throws CycConnectionException {
    synchronized (lock) {
      requireNotPopulated();
      converseVoid(makeSubLStmt("set-proof-view-include-linear", proofViewId,
              includeLinear));
      this.includeLinear = includeLinear;
    }
  }

  /**
   * Should we include a short, executive-summary section in this justification?
   *
   * @return true iff such a section is or should be included
   * @throws CycConnectionException
   * @see #setIncludeSummary(boolean)
   */
  public boolean isIncludeSummary() throws CycConnectionException {
    synchronized (lock) {
      if (includeSummary == null) {
        final String command = makeSubLStmt(
                "get-proof-view-include-summary", proofViewId);
        includeSummary = converseBoolean(command);
      }
      return includeSummary;
    }
  }

  /**
   * Indicate that this justification should or should not include a summary
   * section.
   *
   * @param includeSummary true iff it should
   * @throws CycConnectionException
   * @see #isIncludeSummary()
   */
  public void setIncludeSummary(boolean includeSummary) throws CycConnectionException {
    synchronized (lock) {
      requireNotPopulated();
      converseVoid(makeSubLStmt("set-proof-view-include-summary", proofViewId, includeSummary));
      this.includeSummary = includeSummary;
    }
  }

  /**
   * Returns the microtheory used for natural-language generation in this
   * justification
   *
   * @return the language microtheory
   * @throws CycConnectionException
   * @see #setLanguageMt(com.cyc.base.cycobject.ELMt)
   */
  public ELMt getLanguageMt() throws CycConnectionException {
    synchronized (lock) {
      if (languageMt == null) {
        final CycObject mtObject = converseCycObject(makeSubLStmt(
                "get-proof-view-language-mt", proofViewId));
        languageMt = mtFromObject(mtObject);
      }
      return languageMt;
    }
  }

  /**
   * Set the microtheory used for natural-language generation in this
   * justification
   *
   * @param languageMt the language microtheory
   * @throws CycConnectionException
   * @see #getLanguageMt()
   */
  public void setLanguageMt(ELMt languageMt) throws CycConnectionException {
    synchronized (lock) {
      requireNotPopulated();
      converseVoid(makeSubLStmt("set-proof-view-language-mt", proofViewId, languageMt));
      this.languageMt = languageMt;
    }
  }

  /**
   * Toggles whether to include rich CycL objects in this justification
   *
   * @param bool Whether to include rich CycL
   * @throws com.cyc.base.CycConnectionException
   * @see com.cyc.base.justification.Justification.Node#getCycL()
   */
  public void setRichCycLContent(boolean bool) throws CycConnectionException {
    synchronized (lock) {
      requireNotPopulated();
      converseVoid(makeSubLStmt("set-proof-view-include-cycml", proofViewId, bool));
      this.richCycl = bool;
    }
  }

  /**
   * Get the proof view object backing this justification.
   *
   * @return the proof view.
   */
  public ProofView getProofView() {
    ensureProofViewInitialized();
    return proofView;
  }

  /**
   * Get the root of the tree structure of this justification. A suggested
   * rendering algorithm would display this node, and recurse on its child nodes
   * iff it is to be expanded initially.
   *
   * @see com.cyc.base.justification.Justification.Node#isExpandInitially()
   * @return the root node
   */
  @Override
  public Node getRoot() {
    ensureProofViewInitialized();
    return root;
  }

  private String requireNamespace(String command) {
    return SubLAPIHelper.wrapVariableBinding(
            command, makeCycSymbol("*proof-view-include-namespace?*"),
            makeCycSymbol("T"));
  }

  private void setRoot(Node root) {
    synchronized (lock) {
      this.root = root;
    }
  }

  /**
   * Should bookkeeping data for assertions be included? . Bookkeeping data
   * includes who made the assertion and when, etc.
   *
   * @return true iff it should
   * @throws CycConnectionException
   * @see #setSuppressAssertionBookkeeping(boolean)
   */
  public boolean isSuppressAssertionBookkeeping() throws CycConnectionException {
    synchronized (lock) {
      if (suppressAssertionBookkeeping == null) {
        suppressAssertionBookkeeping = converseBoolean(makeSubLStmt(
                "get-proof-view-suppress-assertion-bookkeeping", proofViewId));
      }
      return suppressAssertionBookkeeping;
    }
  }

  /**
   * Indicate that this justification should or should not include bookkeeping
   * nodes for assertions.
   *
   * @param suppressAssertionBookkeeping
   * @throws CycConnectionException
   * @see #isSuppressAssertionBookkeeping()
   */
  public void setSuppressAssertionBookkeeping(
          boolean suppressAssertionBookkeeping) throws CycConnectionException {
    synchronized (lock) {
      requireNotPopulated();
      converseVoid(makeSubLStmt(
              "set-proof-view-suppress-assertion-bookkeeping", proofViewId,
              suppressAssertionBookkeeping));
      this.suppressAssertionBookkeeping = suppressAssertionBookkeeping;
    }
  }

  /**
   * Should the cyclist who made a given assertion be cited?
   *
   * @return true iff the cyclist should be cited
   * @throws CycConnectionException
   * @see #setSuppressAssertionCyclists(boolean)
   */
  public boolean isSuppressAssertionCyclists() throws CycConnectionException {
    synchronized (lock) {
      if (suppressAssertionCyclists == null) {
        suppressAssertionCyclists = converseBoolean(makeSubLStmt(
                "get-proof-view-suppress-assertion-cyclists", proofViewId));
      }
      return suppressAssertionCyclists;
    }
  }

  /**
   * Indicate that this justification should or should not cite cyclists
   * responsible for assertions.
   *
   * @param suppressAssertionCyclists
   * @throws CycConnectionException
   * @see #isSuppressAssertionCyclists()
   */
  public void setSuppressAssertionCyclists(boolean suppressAssertionCyclists)
          throws CycConnectionException {
    synchronized (lock) {
      requireNotPopulated();
      converseVoid(makeSubLStmt("set-proof-view-suppress-assertion-cyclists",
              proofViewId,
              suppressAssertionCyclists));
      this.suppressAssertionCyclists = suppressAssertionCyclists;
    }
  }

  public SummaryAlgorithm getSummaryAlgorithm() {
    return summaryAlgorithm;
  }

  public void setSummaryAlgorithm(final SummaryAlgorithm algorithm) throws CycConnectionException {
    synchronized (lock) {
      requireNotPopulated();
      converseVoid(makeSubLStmt("set-proof-view-summary-algorithm",
              proofViewId, algorithm.getCycName()));
      this.summaryAlgorithm = algorithm;
    }
  }

  public DenotationalTerm getAddressee() {
    return addressee;
  }

  public void setAddressee(final DenotationalTerm addressee) throws CycConnectionException {
    synchronized (lock) {
      requireNotPopulated();
      converseVoid(makeSubLStmt("set-proof-view-addressee",
              proofViewId, addressee));
      this.addressee = addressee;
    }
  }

  public enum SummaryAlgorithm {

    DEFAULT(":default"), WHITELIST(":whitelist");
    private final CycSymbol cycName;

    private SummaryAlgorithm(final String cycName) {
      this.cycName = CycObjectFactory.makeCycSymbol(cycName);
    }

    private CycSymbol getCycName() {
      return cycName;
    }
  }

  @Override
  public void marshal(org.w3c.dom.Node destination) {
    try {
      marshal(destination, new ProofViewMarshaller());
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Marshal this justification to the specified DOM node using the specified
   * marshaller.
   *
   * @param destination
   * @param marshaller
   */
  public void marshal(org.w3c.dom.Node destination,
          final ProofViewMarshaller marshaller) {
    try {
      marshaller.marshal(proofView, destination);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private void requireNotPopulated() throws UnsupportedOperationException {
    if (isPopulated) {
      throw new UnsupportedOperationException(
              "Justification already populated.");
    }
  }
  private final QueryAnswer answer;
  final private Object lock = new Object();
  private boolean isPopulated = false;
  private final CycAccess cyc;
  private final int proofViewId;
  private Boolean richCycl = null;
  private Boolean includeSummary = null;
  private SummaryAlgorithm summaryAlgorithm = SummaryAlgorithm.DEFAULT;
  private DenotationalTerm addressee = null;
  private Boolean includeDetails = null;
  private Boolean includeLinear = null;
  private Boolean suppressAssertionBookkeeping = null;
  private Boolean suppressAssertionCyclists = null;
  private ELMt languageMt = null;
  private ELMt domainMt = null;
  private ProofView proofView = null;
  private final ProofViewUnmarshaller proofViewUnmarshaller;
  private Node root = null;
  //"get-new-empty-proof-view-id"

  private void converseVoid(final String command) throws CycConnectionException {
    try {
      cyc.converse().converseVoid(command);
    } catch (CycApiException e) {
      throw new QueryApiRuntimeException(e);
    }
  }

  private boolean converseBoolean(final String command) throws CycConnectionException {
    try {
      return cyc.converse().converseBoolean(command);
    } catch (CycApiException e) {
      throw new QueryApiRuntimeException(e);
    }
  }

  private CycObject converseCycObject(final String command) throws CycConnectionException {
    try {
      return cyc.converse().converseCycObject(command);
    } catch (CycApiException e) {
      throw new QueryApiRuntimeException(e);
    }
  }

  private void ensureProofViewInitialized() throws RuntimeException {
    synchronized (lock) {
      if (!isPopulated) {
        populate();
      }
      try {
        final String xml = cyc.converse().converseString(requireNamespace(makeSubLStmt(
                "proof-view-xml", proofViewId)));
        proofView = (ProofView) proofViewUnmarshaller.unmarshalProofview(new ByteArrayInputStream(
                xml.getBytes()));
        final ProofViewEntry rootEntry = proofView.getProofViewEntry();
        setRoot(new ProofViewEntryNode(rootEntry));
      } catch (Exception e) {
        e.printStackTrace(System.err);
        throw new RuntimeException("Failed to get root of proof view.", e);
      }
    }
  }

  /**
   * Implementation of Node backed by a proof-view entry.
   *
   * @see org.opencyc.xml.proofview.ProofViewEntry.
   */
  private class ProofViewEntryNode implements Node {

    private Node parent;

    private ProofViewEntryNode(ProofViewEntry entry) {
      this.entryId = entry.getId().intValue();
      this.entry = entry;
      populateChildren();
    }

    @Override
    public List<? extends Node> getChildren() {
      return Collections.unmodifiableList(children);
    }

    @Override
    public String getCycLString() {
      maybeFetchDetails();
      return entry.getCycl();
    }

    @Override
    public Object getCycL() {
      if (!richCycl) {
        return null;
      }
      maybeFetchDetails();
      final Content content = entry.getContent();
      if (content.getConstant() != null) {
        return CycMLDecoder.translateObject(content.getConstant());
      } else if (content.getFunction() != null) {
        return CycMLDecoder.translateObject(content.getFunction());
      } else if (content.getSentence() != null) {
        return CycMLDecoder.translateObject(content.getSentence());
      } else if (content.getString() != null) {
        return CycMLDecoder.translateObject(content.getString());
      } else if (content.getNumber() != null) {
        return CycMLDecoder.translateObject(content.getNumber());
      } else {
        return null;
      }
    }

    /**
     * Get the label for this node.
     *
     * @return the label, or null if none.
     */
    @Override
    public String getLabel() {
      return entry.getLabel();
    }

    @Override
    public String getHTML() {
      maybeFetchDetails();
      final Paraphrase paraphrase = entry.getParaphrase();
      final StringBuilder sb = new StringBuilder();
      if (paraphrase != null) {
        for (Object obj : paraphrase.getContent()) {
          if (obj instanceof String) {
            sb.append(obj);
          }
        }
      }
      return sb.toString();
    }

    @Override
    public boolean isExpandInitially() {
      return entry.isExpandInitially();
    }

    @Override
    public void marshal(org.w3c.dom.Node destination) {
      try {
        marshal(destination, new ProofViewMarshaller());
      } catch (JAXBException ex) {
        throw new RuntimeException(ex);
      }
    }

    /**
     * Marshal this justification node to the specified DOM node using the
     * specified marshaller.
     *
     * @param destination
     * @param marshaller
     */
    public void marshal(org.w3c.dom.Node destination,
            final ProofViewMarshaller marshaller) {
      try {
        marshaller.marshal(entry, destination);
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    }

    private synchronized void maybeFetchDetails() {
      if (needToFetchDetails()) {
        try {
          final String xml = cyc.converse().converseString(requireNamespace(makeSubLStmt(
                  "proof-view-entry-xml", proofViewId, entryId)));
          this.entry = proofViewUnmarshaller.unmarshalEntry(new ByteArrayInputStream(
                  xml.getBytes()));
          populateChildren();
        } catch (Exception e) {
          throw new RuntimeException(
                  "Failed to fetch proof-view details for " + ProofViewJustification.this.proofViewId + " " + entryId,
                  e);
        }
      }
    }

    private boolean needToFetchDetails() {
      return entry.getParaphrase() == null;
    }

    private void populateChildren() {
      children.clear();
      final SubEntries subEntries = entry.getSubEntries();
      if (subEntries != null) {
        for (final ProofViewEntry subEntry : subEntries.getProofViewEntry()) {
          final ProofViewEntryNode child = new ProofViewEntryNode(subEntry);
          children.add(child);
          child.parent = this;
        }
      }
    }
    private final int entryId;
    private ProofViewEntry entry;
    private final List<ProofViewEntryNode> children = new ArrayList<ProofViewEntryNode>();

    @Override
    public int getDepth() {
      if (getParent() == null) {
        return 0;
      } else {
        return getParent().getDepth() + 1;
      }
    }

    /**
     * Get the parent node of this node.
     *
     * @return the parent node, or null if it has no parent.
     */
    @Override
    public Node getParent() {
      return parent;
    }
  }
}
