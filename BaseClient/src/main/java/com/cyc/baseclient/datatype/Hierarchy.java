package com.cyc.baseclient.datatype;

/*
 * #%L
 * File: Hierarchy.java
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

import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import com.cyc.base.cycobject.DenotationalTerm;
import com.cyc.baseclient.nl.Paraphrase;
import com.cyc.baseclient.datatype.Hierarchy.Walker.Mode;

/**
 * A class for representing, generating and accessing hierarchies.
 *
 * @param <>> the type of object constituting the hierarchy.
 *
 * @author baxter
 */
public class Hierarchy<T> {

  public Hierarchy(T rootContent) {
    this.root = new Node<T>(rootContent);
  }

  /**
   * Returns the root node of this hierarchy,
   *
   * @return the root node of this hierarchy,
   */
  public Node<T> getRoot() {
    return root;
  }

  /**
   * Insert a new node containing the specified content above the specified node.
   *
   * @param content
   * @param child
   */
  public void insertAbove(T content, Node<T> child) {
    final Node<T> newNode = new Node<T>(content);
    newNode.add(child);
    synchronized (this) {
      Node<T> oldParent = child.setParent(newNode);
      if (oldParent == null) {
        this.root = newNode;
      } else {
        newNode.setParent(oldParent);
      }
    }
  }

  /**
   * Insert a new node containing the specified content below the specified node.
   *
   * @param content
   * @param parent
   */
  public void insertBelow(T content, Node<T> parent) {
    final Node<T> newNode = new Node<T>(content);
    synchronized (this) {
      newNode.setParent(parent);
      parent.add(newNode);
    }
  }

  /**
   * Output this hierarchy with indenting to show structure.
   *
   * @param stream the destination stream.
   */
  public void printIndented(PrintStream stream) {
    String indentString = "";
    for (final SortedWalker walker = new SortedWalker(this,
            Walker.Mode.DEPTHFIRST); walker.hasNext();) {
      final Node<Paraphrase<DenotationalTerm>> node = walker.next();
      final int depth = node.getDepth();
      if (indentString.length() > depth) {
        indentString = indentString.substring(0, depth);
      } else {
        while (indentString.length() < depth) {
          indentString += " ";
        }
      }
      stream.println(indentString + node.getContent());
    }
  }

  /**
   * A class representing nodes in a hierarchy.
   *
   * @param <>> the type of object constituting the hierarchy.
   */
  public static class Node<T> {

    /**
     * Returns the parent of this node, null iff root node.
     *
     * @return the parent of this node.
     */
    public Node<T> getParent() {
      return parent;
    }

    /**
     * Set the parent of this node.
     *
     * @param newParent
     * @return the previous parent node.
     */
    private Node<T> setParent(Node<T> newParent) {
      final Node<T> oldParent = getParent();
      this.parent = newParent;
      return oldParent;
    }

    /**
     * Returns the set containing all and only this node's children.
     *
     * @return the child nodes.
     */
    public Set<Node<T>> getChildren() {
      return Collections.unmodifiableSet(children);
    }

    @Override
    public String toString() {
      return "[N: " + content + "]";
    }

    /**
     * Get the depth of this node in its tree. The root node has depth 0.
     *
     * @return the depth.
     */
    public int getDepth() {
      int depth = 0;
      Node<T> parent = this.getParent();
      while (parent != null) {
        depth++;
        parent = parent.getParent();
      }
      return depth;
    }

    /**
     * Add a new child to this node.
     *
     * @param child
     * @return true if add is successful
     */
    private boolean add(Node<T> child) {
      return children.add(child);
    }

    /**
     * Returns the object at this node.
     *
     * @return the object.
     */
    public T getContent() {
      return content;
    }

    private Node(T content) {
      this.content = content;
    }
    private Node<T> parent;
    private T content;
    final private Set<Node<T>> children = new HashSet<Node<T>>();
  }

  public static class Walker<T> implements Iterator<Node<T>> {

    public enum Mode {

      BREADTHFIRST, DEPTHFIRST
    };
    private final Mode mode;

    public Walker(final Hierarchy<T> hierarchy) {
      this(hierarchy, Mode.BREADTHFIRST);
    }

    public Walker(final Hierarchy<T> hierarchy, final Mode mode) {
      queue.add(hierarchy.getRoot());
      this.mode = mode;
    }

    @Override
    public boolean hasNext() {
      return (!queue.isEmpty());
    }

    @Override
    synchronized public Node<T> next() {
      final Node<T> next = queue.remove();
      Collection<Node<T>> children = orderSiblingNodes(next.getChildren());
      switch (mode) {
        case BREADTHFIRST: {
          queue.addAll(children);
          break;
        }
        case DEPTHFIRST: {
          children = new ArrayList<Node<T>>(children);
          Collections.reverse((List) children);
          for (final Node<T> child : children) {
            queue.addFirst(child);
          }
        }
      }
      return next;
    }
    final protected Deque<Node<T>> queue = new ArrayDeque<Node<T>>();

    @Override
    public void remove() {
      throw new UnsupportedOperationException("Not supported.");
    }

    /**
     * Exercise control over the order in which sibling nodes are visited.
     *
     * @param nodes
     * @return a possibly ordered collection of the input nodes.
     */
    protected Collection<Node<T>> orderSiblingNodes(Collection<Node<T>> nodes) {
      return nodes;
    }
  }

  public static class HierarchyComparator<T> implements Comparator<Hierarchy<T>> {

    public HierarchyComparator() {
    }

    @Override
    public int compare(Hierarchy<T> h1, Hierarchy<T> h2) {
      return ((Comparable) h1.getRoot().getContent()).compareTo(
              h2.getRoot().getContent());
    }
  }

  public static class SortedWalker<T> extends Walker<T> {

    public SortedWalker(Hierarchy<T> hierarchy) {
      super(hierarchy);
    }

    public SortedWalker(Hierarchy<T> hierarchy, Walker.Mode mode) {
      super(hierarchy, mode);
    }

    @Override
    protected Collection<Node<T>> orderSiblingNodes(
            Collection<Node<T>> nodes) {
      final Comparator<Node<T>> comparator = new Comparator<Node<T>>() {
        @Override
        public int compare(Node<T> node1, Node<T> node2) {
          return ((Comparable) node1.getContent()).compareTo(node2.getContent());
        }
      };
      final TreeSet<Node<T>> ordered = new TreeSet<Node<T>>(comparator);
      ordered.addAll(nodes);
      return ordered;
    }
  }

  /**
   * A class for building hierarchies of objects of a specified type.
   *
   * @param <T>
   */
  public static abstract class Builder<T> {

    private boolean debug;

    /**
     * Construct a new builder object.
     */
    public Builder() {
    }

    /**
     * Organize objects into a collection of hierarchies.
     *
     * @param objects the objects to organize
     * @return a collection of hierarchies containing objects.
     */
    public Collection<Hierarchy<T>> organize(Collection<T> objects) {
      final Set<Hierarchy<T>> hierarchies = new HashSet<Hierarchy<T>>();
      for (final T obj : objects) {
        addItem(obj, hierarchies);
      }
      return hierarchies;
    }

    private void addItem(T item, Set<Hierarchy<T>> hierarchies) {
      debug = false;
      if (debug) {
        System.out.println("Adding " + item);
      }
      final Set<Hierarchy> toMerge = new HashSet<Hierarchy>();
      if (!placeInExistingHierarchies(hierarchies, item, toMerge)) {
        hierarchies.add(new Hierarchy<T>(item));
      } else {
        mergeHierarchies(toMerge, hierarchies);
      }
    }

    private boolean tryToAddItem(T item, Hierarchy<T> hierarchy) {
      final Set<Node<T>> superiors = new HashSet<Node<T>>();
      for (final Walker<T> walker = new Walker<T>(hierarchy); walker.hasNext();) {
        final Node<T> node = walker.next();
        final T other = node.getContent();
        if (debug) {
          System.out.println("Comparing " + item + " to " + other);
        }
        if (isSuperior(item, other)) {
          if (debug) {
            System.out.println("Inserting " + item + " above " + other);
          }
          hierarchy.insertAbove(item, node);
          return true;
        } else if (isSuperior(other, item)) {
          superiors.add(node);
        }
      }
      if (superiors.isEmpty()) {
        return false;
      } else {
        for (final Node<T> superior : superiors) {
          if (debug) {
            System.out.println(
                    "Inserting " + item + " below " + superior.getContent());
          }
          hierarchy.insertBelow(item, superior);
        }
        return true;
      }
    }

    /**
     * Place item in hierarchies, noting hierarchies that should be removed.
     *
     * @param hierarchies
     * @param item
     * @param toRemove Hierarchies to be removed are added to this set.
     * @return true iff item was successfully placed in hierarchies.
     */
    private boolean placeInExistingHierarchies(Set<Hierarchy<T>> hierarchies,
            T item,
            final Set<Hierarchy> toRemove) {
      boolean placed = false;
      Hierarchy hierarchyRootedByItem = null;
      for (final Iterator<Hierarchy<T>> it = hierarchies.iterator(); it.hasNext();) {
        final Hierarchy<T> hierarchy = it.next();
        if (tryToAddItem(item, hierarchy)) {
          placed = true;
          if (hierarchyRootedByItem != null) {
            toRemove.add(hierarchyRootedByItem);
          }
          final T rootItem = hierarchy.getRoot().getContent();
          if (item.equals(rootItem)) {
            hierarchyRootedByItem = hierarchy;
          }
        }
      }
      return placed;
    }

    /**
     * Merge toMerge into hierarchies.
     *
     * @param toMerge
     * @param hierarchies
     */
    private void mergeHierarchies(final Set<Hierarchy> toMerge,
            Set<Hierarchy<T>> hierarchies) {
      final Set<T> relocated = new HashSet<T>();
      hierarchies.removeAll(toMerge);
      for (final Hierarchy hierarchy : toMerge) {
        for (final Walker<T> walker = new Walker<T>(hierarchy); walker.hasNext();) {
          final Node<T> node = walker.next();
          if (!node.equals(hierarchy.getRoot())) {
            final T childItem = node.getContent();
            if (relocated.add(childItem)) {
              addItem(childItem, hierarchies);
            }
          }
        }
      }
    }
    /**
     * Determine relative positioning of two objects.
     *
     * @param obj1
     * @param obj2
     * @return true iff obj1 belongs somewhere above obj2.
     */
    protected abstract boolean isSuperior(T obj1, T obj2);
  }
  protected Node<T> root;
}
