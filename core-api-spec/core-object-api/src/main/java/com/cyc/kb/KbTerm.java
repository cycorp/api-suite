package com.cyc.kb;

import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbTypeException;
import java.util.Date;
import java.util.Map;

/*
 * #%L
 * File: KbTerm.java
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
 * The top-level interface for most kinds of {@link KbObject}s that are not
 * {@link Sentence}s.
 *
 * @author vijay
 */
public interface KbTerm extends KbObject {

  /**
   * Determine whether <code>this</code> is provably not an instance of
   * <code>col</code>. This means that there is no way to convert
   * <code>this</code> into an instance of <code>col</code> without editing or
   * removing current KB assertions.
   *
   * @param col the collection which <code>this</code> is to be tested against
   * @param ctx the context in which the semantic test should be performed
   *
   * @return whether <code>this</code> is provably not an instance of
   * <code>col</code>
   */
  boolean provablyNotInstanceOf(KbCollection col, Context ctx);

  /**
   * Determine whether <code>this</code> is provably not an instance of
   * <code>col</code>. This means that there is no way to convert
   * <code>this</code> into an instance of the collection without editing or
   * removing current KB assertions.
   *
   * @param colStr the string representing the collection which
   * <code>this</code> is to be tested against
   * @param ctxStr the context in which the semantic test should be performed
   *
   * @return whether <code>this</code> is provably not an instance
   * <code>col</code>
   */
  boolean provablyNotInstanceOf(String colStr, String ctxStr);
  
  /**
   * Non-destructively replace a set of objects within a non-atomic term. This does not modify the 
   * original term, but instead returns a new one. Any KBObject or Java 
   * 
   * <p>Note: a common use of this method is to replace indexical terms, so you may want to see
   * {@link com.cyc.kb.KbObject#isIndexical() } for more details about them. However, this method
   * can be used to replace <em>any</em> element within a term, not just indexicals. 
   * 
   * @param   <O>            the referent's expected type
   * @param   substitutions  the replacement mapping
   * @return  a new term with the replaced terms
   * @throws  KbTypeException
   * @throws  CreateException
   */
  <O> O replaceTerms(Map substitutions) throws KbTypeException, CreateException;
  
  /**
   * Return the #$Cyclist (not necessarily #$HumanCyclist) who created this term. 
   * If not found or if there is an exception, return null.
   * 
   * @return the cyclist who created the term
   */
  KbIndividual getCreator();
  
  /**
   * Return the date and time at which this term was created. If not found or if
   * there is an exception, return null.
   * 
   * @return the date and time at which the term was created
   */
  Date getCreationDate();
}
