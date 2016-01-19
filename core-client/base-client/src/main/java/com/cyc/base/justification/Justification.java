package com.cyc.base.justification;

/*
 * #%L
 * File: Justification.java
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

import java.util.List;

import com.cyc.base.inference.InferenceAnswer;
import com.cyc.session.exception.OpenCycUnsupportedFeatureException;

/**
 * An interface for representing and generating justifications of Cyc inference
 * answers.
 * <p/>
 * The structure of a <code>Justification</code> is a tree of {@link Node}s,
 * with the content varying widely for different types of
 * <code>Justification</code>. In general they are intended to be rendered in an
 * interactive display, with CycL and/or NL (encoded in HTML) for nodes and
 * their children displayed or hidden according to user actions or preferences.
 *
 *
 * @author baxter
 */
public interface Justification {

  /**
   * Get the root of the tree structure of this justification. A suggested
   * rendering algorithm would display this node, and recurse on its child nodes
   * iff it is to be expanded initially.
   *
   * @throws com.cyc.session.exception.OpenCycUnsupportedFeatureException when run against an OpenCyc server.
   * @see com.cyc.base.justification.Justification.Node#isExpandInitially()
   * @return the root node
   */
  Node getRoot() throws OpenCycUnsupportedFeatureException;

  /**
   * Flesh out this justification, setting its root node and tree structure
   * underneath the root.
   * 
   * @throws com.cyc.session.exception.OpenCycUnsupportedFeatureException when run against an OpenCyc server.
   */
  void populate() throws OpenCycUnsupportedFeatureException;

  /**
   * Returns the inference answer justified by this object
   *
   * @return the inference answer
   */
  InferenceAnswer getAnswer();

  /**
   * Marshal this justification into a DOM tree.
   *
   * @param destination
   */
  void marshal(org.w3c.dom.Node destination);

  /**
   * An interface representing one node in a Justification tree.
   */
  public interface Node {

    /**
     * Returns the HTML string for this node.
     *
     * @return the HTML string for this node.
     */
    String getHTML();

    /**
     * Returns a string rendering of the CycL content of this node.
     *
     * @return a string rendering the CycL content of this node.
     */
    String getCycLString();

    /**
     * Returns the CycL content of this node.
     *
     * @return the CycL content of this node.
     */
    Object getCycL();

    /**
     * Returns the children of this node.
     *
     * @return the children of this node.
     */
    List<? extends Node> getChildren();

    /**
     * Is it advised that this node be expanded when first displayed to expose
     * its children?
     *
     * @return true iff it is so advised.
     */
    boolean isExpandInitially();

    /**
     * Marshal this node into a DOM tree.
     *
     * @param destination
     */
    void marshal(org.w3c.dom.Node destination);

    /**
     * Get the number of levels between this node and the root.
     *
     * @return the depth
     */
    public int getDepth();

    /**
     * Get the parent node of this node.
     *
     * @return the parent node, or null if it has no parent.
     */
    Node getParent();

    /**
     * Get the label for this node.
     *
     * @return the label, or null if none.
     */
    String getLabel();
  }
}
