package com.cyc.baseclient.justification;

/*
 * #%L
 * File: JustificationWalker.java
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

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

import com.cyc.base.justification.Justification;
import com.cyc.base.justification.Justification.Node;
import com.cyc.session.exception.OpenCycUnsupportedFeatureException;

/**
 * A depth-first iterator over the nodes of a justification.
 * @author baxter
 */
public class JustificationWalker implements Iterator<Node> {
  final Deque<Justification.Node> queue = new ArrayDeque<Justification.Node>();

  public JustificationWalker(final Justification justification) throws OpenCycUnsupportedFeatureException {
    queue.add(justification.getRoot());
  }

  @Override
  public boolean hasNext() {
    return !queue.isEmpty();
  }

  @Override
  public Node next() {
    final Justification.Node node = queue.remove();
    final List<? extends Node> children = node.getChildren();
    //Add children to front of queue for depth-first traversal:
    for (final Iterator<Justification.Node> it = new ArrayDeque<Justification.Node>(children).descendingIterator(); it.hasNext();) {
      queue.addFirst(it.next());
    }
    return node;
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException("Not supported.");
  }
  
}
