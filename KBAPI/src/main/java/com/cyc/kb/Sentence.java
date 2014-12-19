package com.cyc.kb;

import com.cyc.base.CycApiException;
import com.cyc.baseclient.util.query.Query;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KBApiException;
import com.cyc.kb.exception.KBTypeException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/*
 * #%L
 * File: Sentence.java
 * Project: KB API
 * %%
 * Copyright (C) 2013 - 2014 Cycorp, Inc
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
 * The interface for Cyc sentences. Sentences encode propositions, which can be used as questions
 * (in a {@link Query}) or claims (in an {@link Assertion}).
 *
 * @author vijay
 * @version $Id: Sentence.java 155325 2014-12-03 19:44:38Z daves $
 * @since 1.0
 */
public interface Sentence extends KBObject {

  /**
   * Assert <code>this</code> sentence in the context <code>ctx</code>.
   *
   * In many cases, it will be useful to call
   * {@link #isAssertible(com.cyc.kb.Context)} before asserting a sentence.
   *
   * @param ctx the context where <code>this</code> sentence will be asserted.
   *
   * @return the {@link Assertion} object, either a {@link Fact} or a {@link Rule}.
   *
   * @throws KBApiException if the assertion fails for any reason, include being syntactically
   * invalid.
   */
  public Assertion assertIn(Context ctx) throws KBApiException;

  /**
   * Checks if a given sentence meets the constraints necessary to be assertible in
   * <code>ctx</code>. In particular, returns true if the arguments of the sentence are compatible with
   * the argument constraints of the operator (i.e. could be made to actually meet the constraints
   * without removing any existing assertions). For sentences with predicate operators, the
   * arguments must also be compatible with all relevant interArg* constraints. In some cases,
   * additional requirements are imposed (e.g. the constraints must actually be known to be met, not
   * just not be known to be incompatible). For an explanation of why a particular sentence is not
   * assertible, check {@link #notAssertibleExplanation(com.cyc.kb.Context)}
   *
   * @param ctx the context where semantic constraints are checked
   *
   * @return if the sentence is compatible with all relevant syntactic and semantic constraints, and
   * is therefore assertible.
   *
   * @throws CycApiException if unable to determine whether constraints are met.
   */
  public boolean isAssertible(Context ctx);

  /**
   * Return an explanation of why the sentence is not assertible. Note that this reason may not be
   * exhaustive; once the problems mentioned in the reason are fixed, additional calls to this
   * method may turn up additional problems.
   *
   * @param ctx the context where semantic constraints are checked
   *
   * @return a detailed explanation of why a sentence is not assertible. Returns null if it
   * encounters an internal error.
   *
   */
  public String notAssertibleExplanation(Context ctx);

  /**
   * For each element of <code>from</code> replace it with the element of <code>to</code> that's at
   * the same index.  This does not modify the original SentenceImpl, but instead returns a new sentence.
   * 
   * @param from  a list of objects that will be replaced if found
   * @param to    a list of objects that will be in the new modified sentence
   * 
   * @return a new Sentence with the replaced terms
   * 
   * @throws KBTypeException
   */
  public Sentence replaceTerms(List<Object> from, List<Object> to) throws KBTypeException, CreateException;

  public Sentence quantify(KBObject variable) throws KBTypeException, CreateException;

    // A ENUM interface that returns modified sentences for commonly used sentence operators,
  // such as #$not and #$assertedSentence.
  public interface SentenceOperator {
    // Return a new sentence with <code>sent</code> sentence wrapped with <code>this</code>
    // enumerator.
    public Sentence wrap(Sentence sent) throws KBTypeException, CreateException;
  }

  /**
   * This is not part of the public, supported KB API. 
   *
   * @return the expanded sentence
   *
   * @throws KBApiException
   */
  // Tentatively it will convert an atomic, open
  // sentence into a non-atomic, open sentence by adding isa constraints on the variables.
   
  public Sentence expandSentence() throws KBApiException;

  /**
   * This is not part of the public, supported KB API.
   *
   * @return a list of typed variables in a sentence.
   */
  public Collection<KBTerm> getListOfTypedVariables();
  
   /**
   * Returns a set of arg positions that describe all the locations where the
   * given term can be found in this formula.
   *
   * @param term The term to search for
   * @return The set of all arg positions where term can be found
   */ 
  public Set<ArgPosition> getArgPositionsForTerm(final Object term);
}
