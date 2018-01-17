package com.cyc.nl;

/*
 * #%L
 * File: SubParaphrase.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2017 Cycorp, Inc
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

import com.cyc.kb.ArgPosition;

/**
 *
 * @author daves
 */
public interface SubParaphrase extends Paraphrase {

  /**
   * Returns the arg position of the term paraphrased in its parent formula.
   * @return the arg position of the term paraphrased in its parent formula.
   */
  ArgPosition getArgPosition();

  /**
   * Returns the parent paraphrase of this sub-paraphrase.
   * @return the parent paraphrase of this sub-paraphrase.
   */
  Paraphrase getParentParaphrase();

  /**
   * Returns the start index of this paraphrase within its parent.
   * @return the start index of this paraphrase within its parent.
   */
  int getStartIndex();
  
}
