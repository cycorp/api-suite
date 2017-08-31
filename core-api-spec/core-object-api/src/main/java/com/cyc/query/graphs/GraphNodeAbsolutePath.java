/*
 * Copyright 2017 Cycorp, Inc..
 *
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
 */
package com.cyc.query.graphs;

/*
 * #%L
 * File: GraphNodeAbsolutePath.java
 * Project: Core API Object Specification
 * %%
 * Copyright (C) 2013 - 2017 Cycorp, Inc
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

/**
 * An object which represents the absolute path to a node within a graph. It facilitates the 
 * flattening of graphs into Collections or Maps (useful for mapping a graph into a spreadsheet or 
 * database table) and provides a String representation of a node's absolute path for use in, e.g.,
 * logging statements.
 * 
 * @param <V> The type of the node's ID
 * @param <T> The implementation's most specific sub-interface of GraphNodeAbsolutePath
 * 
 * @author nwinant
 */
public interface GraphNodeAbsolutePath<V, T extends GraphNodeAbsolutePath<V, T>> extends Comparable<T> {

  /**
   * Returns the ID of current node. The type of the ID object is left entirely to the specific 
   * <code>GraphNodeAbsolutePath</code> implementation, but should equal the most atomic ID for the
   * node as used within  its graph.
   * 
   * <p>Because a <code>GraphNodeAbsolutePath</code> represents an absolute path to a node, the
   * node's ID does not need to be unique within the graph so long as all nodes have IDs that are
   * unique among their siblings. E.g., in a graph where each node's position in its parent's list
   * of children serves as its ID, many nodes might have an ID of 0, but a given node could be
   * uniquely identified by a path of 0.0.0.0.0.
   * 
   * @return the ID of the current node.
   */
  public V getNodeId();
  
  /**
   * Indicates whether the node has a parent or is the root node.
   * 
   * @return <code>true </code> if this node has a parent; <code>false </code> if it does not.
   */
  public boolean hasParent();

  /**
   * Returns the <code>GraphNodeAbsolutePath</code> of the current node's parent.
   * 
   * @return the GraphNodeAbsolutePath of the current node's parent.
   */
  public T getParentPath();
  
  /**
   * Returns an ordered, unmodifiable list of IDs representing the path to this node via its
   * ancestors. The list is ordered with each node immediately followed by its child, if it has one;
   * thus, the list's first element is the ID of the root node, and its last element is the ID of
   * the current node instance.
   * 
   * @return a list of IDs representing the path to this node via its ancestors.
   */
  public List<V> toList();

  /**
   * Indicates whether a <code>GraphNodeAbsolutePath</code> instance is of the same type and has the
   * same path as this instance. Note that this methods only indicates whether the paths and classes
   * are equal, and says nothing about the actual nodes to which they refer; thus, it is possible 
   * for node paths from different graphs to be found to be equal to one another.
   * 
   * <p>Different implementations of <code>GraphNodeAbsolutePath</code> should be found to be 
   * unequal, even if the values of their paths are identical. The paths of different
   * <code>GraphNodeAbsolutePath</code> implementations may be compared via their {@link #toList()}
   * methods.
   * 
   * @param obj the reference object with which to compare.
   * @return 
   */
  @Override
  public boolean equals(Object obj);
  
  /**
   * Returns a string representation of the node's absolute path. Because formatting rules for the
   * path (including node IDs) may be highly dependent on the particular type of graph, all 
   * formatting decisions are left entirely to the particular <code>GraphNodeAbsolutePath</code> implementation.
   * 
   * @return a string representation of the node's absolute path.
   */
  @Override
  public String toString();
  
  /**
   * Returns a string representation of the node's absolute path, with each node ID padded out to a
   * minimum length (optional operation). As with {@link #toString()}, the formatting rules for the 
   * path, including how nodes should be padded, are left entirely to the specific <code>GraphNodeAbsolutePath</code>
   * implementations.
   * 
   * <p>A <code>GraphNodeAbsolutePath</code> implementation is not required to provide any special behavior for this 
   * method, but if it does not, this method should simply return the normal {@link #toString()} 
   * value.
   * 
   * @param minIdLength the minimum length for each node ID.
   * @return a string representation of the node's absolute path, with each node ID padded out to a
   * minimum length.
   */
  public String toPaddedString(int minIdLength);
  
}
