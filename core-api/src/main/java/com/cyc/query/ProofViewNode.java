package com.cyc.query;

/*
 * #%L
 * File: ProofViewNode.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2018 Cycorp, Inc
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
import com.cyc.query.graph.GraphNodeAbsolutePath;
import java.util.List;

/**
 *
 * @author daves
 */
public interface ProofViewNode {

  public interface ProofViewNodePath extends GraphNodeAbsolutePath<Integer, ProofViewNodePath> {
  }

  /**
   * The ID for this node. ProofViewNodes are unique within a ProofView.
   *
   * @return
   */
  int getEntryId();

  ProofViewNodePath getEntryPath();

  List<? extends ProofViewNode> getChildren();

  //  Object getCycl();
  String getCyclString();

  int getDepth();

  String getHTML();

  /**
   * Get the label for this node.
   *
   * @return the label, or null if none.
   */
  String getLabel();

  /**
   * Get the parent node of this node.
   *
   * @return the parent node, or null if it has no parent.
   */
  ProofViewNode getParent();

  /**
   * Is it advised that this node be expanded when first displayed to expose its children?
   *
   * @return true iff it is so advised.
   */
  boolean isExpandInitially();

}
