/*
 * Copyright 2017 Cycorp, Inc.
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
package com.cyc.query;

/*
 * #%L
 * File: ProofView.java
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

/**
 * An explanation for a specific answer to a query, expressed as a tree of {@link ProofViewNode}s.
 * In general they are intended to be rendered in an interactive display, with CycL and/or NL 
 * (encoded in HTML) for nodes, and with their children displayed or hidden according to user 
 * actions or preferences.
 * 
 * <p>A <code>ProofView</code> instance is also a <code>ProofViewNode</code>, and serves as the root
 * of the tree. A suggested rendering algorithm would be to display this node, and recurse on all
 * child nodes for whom {@link ProofViewNode#isExpandInitially()} returns <code>true</code>.
 * 
 * @author daves
 */
public interface ProofView extends ProofViewNode, QueryAnswerExplanation {
  
}
