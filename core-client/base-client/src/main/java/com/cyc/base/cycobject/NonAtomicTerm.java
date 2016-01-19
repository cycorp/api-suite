package com.cyc.base.cycobject;

/*
 * #%L
 * File: NonAtomicTerm.java
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

/** 
 * <P>NonAtomicTerm is designed to...
 * 
 * @author baxter, Jul 6, 2009, 10:05:43 AM
 * @version $Id: NonAtomicTerm.java 163356 2016-01-04 20:55:47Z nwinant $
 */
public interface NonAtomicTerm extends DenotationalTerm {
  
  /**
   *
   * @return
   */
  DenotationalTerm getFunctor();
  /**
   * Get the arguments of this non-atomic term, not including the functor,
   * or arg0.
   * @return the list of arguments.
   */
  List getArguments();
  Naut getFormula();
  Object getArgument(final int argnum);

  /**
   * Returns a list representation of the Cyc NART.
   *
   * @return a <tt>CycList</tt> representation of the Cyc NART.
   */
  CycList toCycList();

  /**
   * Returns a list representation of the Cyc NART and expands any embedded NARTs as well.
   *
   * @return a <tt>CycList</tt> representation of the Cyc NART.
   */
  CycList toDeepCycList();

  public int getArity();
}
