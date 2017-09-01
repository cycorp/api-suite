package com.cyc.kb;

import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbException;
import com.cyc.kb.exception.KbTypeException;
import com.cyc.query.Query;
import com.cyc.session.exception.SessionCommunicationException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
 * #%L
 * File: Sentence.java
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
 * The interface for Cyc sentences. Sentences encode propositions, which can be used as questions
 * (in a {@link Query}) or claims (in an {@link Assertion}).
 *
 * @author   vijay
 * @version  $Id: Sentence.java 173072 2017-07-27 01:21:15Z nwinant $
 * @since    1.0
 */
public interface Sentence extends KbObject {

  /**
   * Assert <code>this</code> sentence in the context <code>ctx</code>.
   *
   * In many cases, it will be useful to call
   * {@link #isAssertible(com.cyc.kb.Context)} before asserting a sentence.
   *
   * @param   ctx  context where <tt>this</tt> sentence will be asserted
   * @return  the {@link Assertion} object, either a {@link Fact} or a {@link Rule}
   * @throws  KbException  if the assertion fails for any reason, include being syntactically
   *                       invalid
   */
  Assertion assertIn(Context ctx) throws KbException;

  /**
   * Checks if a given sentence meets the constraints necessary to be assertible in
   * <code>ctx</code>. In particular, returns true if the arguments of the sentence are compatible 
   * with the argument constraints of the operator (i.e. could be made to actually meet the 
   * constraints without removing any existing assertions). For sentences with predicate operators, 
   * the arguments must also be compatible with all relevant interArg* constraints. In some cases,
   * additional requirements are imposed (e.g. the constraints must actually be known to be met, not
   * just not be known to be incompatible). For an explanation of why a particular sentence is not
   * assertible, check {@link #notAssertibleExplanation(com.cyc.kb.Context)}
   *
   * @param   ctx  the context where semantic constraints are checked
   * @return  whether the sentence is compatible with all relevant syntactic and semantic 
   *          constraints, and is therefore assertible
   * @throws  CycApiException  if unable to determine whether constraints are met
   */
  boolean isAssertible(Context ctx);

  /**
   * Return an explanation of why the sentence is not assertible. Note that this reason may not be
   * exhaustive; once the problems mentioned in the reason are fixed, additional calls to this
   * method may turn up additional problems.
   *
   * @param   ctx  context where semantic constraints are checked
   * @return  a detailed explanation of why a sentence is not assertible. Returns null if it 
   *          encounters an internal error.
   */
  String notAssertibleExplanation(Context ctx);
  
  /**
   * Apply each of the substitutions specified in 
   * @param   updates
   * @return  
   */
  Sentence performUpdates(List<ArgUpdate> updates);
  
  /**
   * Returns a list of the Variables in the sentence. Note that this is a copy of the variables, and
   * modification of the returned value will not result in modifications of the Sentence.
   *
   * @param   includeQueryable  should this return the variables for which a query would return
   *                            bindings, or all free variables?
   * @return  a list of the variables in this sentence.
   * @throws  KbException
   * @see     com.cyc.query.QuerySpecification#getQueryVariables() 
   */
  List<Variable> getVariables(boolean includeQueryable) throws KbException;
  
  /**
   * Returns any <tt>#$IndexicalConcept</tt>s within the sentence. This method takes a boolean 
   * argument (<tt>includeAllIndexicals</tt>) which specifies whether it should return all of
   * the indexical terms within the Sentence, or only those terms which must be resolved by the
   * application. In other words, automatically-resolvable indexicals such as 
   * <tt>#$Now-Indexical</tt> will only be returned by this method if 
   * <tt>includeAllIndexicals</code> is <tt>true</code>.
   * 
   * <p>For a brief overview of indexicals, see {@link com.cyc.kb.KbObject#isIndexical() }.
   * 
   * @param   includeAllIndexicals  whether to include all indexicals, or only those which must be
   *                                resolved by the application
   * @return  the indexicals within the sentence
   * @throws  KbException
   * @throws  SessionCommunicationException 
   * @see     com.cyc.kb.KbObject#isIndexical() 
   * @see     com.cyc.kb.Sentence#getIndexicals() 
   * @see     com.cyc.kb.Sentence#replaceTerms(java.util.Map) 
   * @see     com.cyc.query.QuerySpecification#getUnresolvedIndexicals() 
   * @see     com.cyc.query.QuerySpecification#setSubstitutions(java.util.Map) 
   * @see     com.cyc.query.QuerySpecification#addSubstitutions(java.util.Map) 
   * @see     com.cyc.query.QuerySpecification#getSubstitutions() 
   */
  List<KbObject> getIndexicals(boolean includeAllIndexicals) 
          throws KbException, SessionCommunicationException;
  
  /**
   * Returns any <code>#$IndexicalConcept</code>s within the sentence which <em>cannot</em> be 
   * automatically resolved by Cyc. For example, <code>#$Now-Indexical</code> would not be included
   * in the results (because Cyc can automatically resolve it to the current time) but a term like
   * <code>(TheNamedFn InformationStore "data source")</code> <em>would</em> be included, because 
   * Cyc has no rules to resolve such a term. In other words, this method is the equivalent of
   * passing <code>false</code> to {@link #getIndexicals(boolean) }.
   * 
   * <p>For a brief overview of indexicals, see {@link com.cyc.kb.KbObject#isIndexical() }.
   *
   * @return  a list of all indexicals which will not be automatically resolved by Cyc
   * @throws  KbException
   * @throws  SessionCommunicationException
   */
  List<KbObject> getIndexicals() throws KbException, SessionCommunicationException;
  
  /**
   * Non-destructively replace a set of objects within a Sentence. This does not modify the original
   * Sentence, but instead returns a new one. To replace terms in a Query sentence, use 
   * {@link com.cyc.query.QuerySpecification#setSubstitutions(java.util.Map)} instead.
   * 
   * <p>Note: a common use of this method is to replace indexical terms, particularly in Query 
   * sentences, so you may want to see {@link com.cyc.kb.KbObject#isIndexical() } for more details 
   * about them. However, this method can be used to replace <em>any</em> KbObjects within a 
   * Sentence, not just indexicals. 
   * 
   * @param   substitutions  the replacement mapping
   * @return  a new Sentence with the replaced terms
   * @throws  KbTypeException
   * @throws  CreateException
   * @see     Sentence#replaceTerms(java.util.List, java.util.List) 
   */
  Sentence replaceTerms(Map substitutions) throws KbTypeException, CreateException;
  
  /**
   * For each element of <code>from</code> replace it with the element of <code>to</code> that's at
   * the same index. This does not modify the original Sentence, but instead returns a new one.
   * 
   * @param   from  a list of objects that will be replaced if found
   * @param   to    a list of objects that will be in the new modified sentence
   * @return  a new Sentence with the replaced terms
   * @throws  KbTypeException
   * @throws  CreateException
   * 
   * @see Sentence#replaceTerms(java.util.Map) 
   */
  Sentence replaceTerms(List<Object> from, List<Object> to) throws KbTypeException, CreateException;
  
  /**
   * Set the value with sentence at pos to be value.  Returns a new Sentence.
   * 
   * @param   pos    
   * @param   value  
   * @return  
   * @throws  KbTypeException 
   * @throws  CreateException 
   */
  Sentence setArgPosition(ArgPosition pos, Object value) throws KbTypeException, CreateException;
  
  Sentence quantify(KbObject variable) throws KbTypeException, CreateException;
  
  /**
   * Returns a set of arg positions that describe all the locations where the
   * given term can be found in this formula.
   *
   * @param   term  The term to search for
   * @return  the set of all arg positions where term can be found
   */ 
  Set<ArgPosition> getArgPositionsForTerm(final Object term);
  
  /*
  // A ENUM interface that returns modified sentences for commonly used sentence operators,
  // such as #$not and #$assertedSentence.
  public interface SentenceOperator {
    // Return a new sentence with <code>sent</code> sentence wrapped with <code>this</code>
    // enumerator.
    public Sentence wrap(Sentence sent) throws KbTypeException, CreateException;
  }
  */
  
  /**
   * Returns the syntactic arity of this object. If it has a relation applied to
   * some arguments (i.e. it's a sentence, an assertion, or a functional term),
   * the arity is the number of arguments. By convention, Cyc constants have a
   * formula arity of 0.
   *
   * @return  the arity of this object, <tt>null</tt> if not a Cyc constant, functional term, 
   *          sentence, or assertion
   */
  Integer getArity();
  
  /**
   * gets the object in <code>argPosition</code> argument position of this KbObject
   * as an object of type <code>O</code>. This method works for Sentences and
   * Assertions, as well as non-atomic KbTerms. However, because a constant has
   * no "arguments", calling this method on a KbObject representing a Cyc
   * constant will result in a KbException.
   * 
   * @param   <O>          the object type
   * @param   argPosition  the argument position of the object returned
   * @return  the object at <code>getPos</code> as a <code>O</code>
   * @throws  CreateException
   * @throws  KbTypeException
   */
  <O> O getArgument(int argPosition) throws KbTypeException, CreateException;

  /**
   * Returns <tt>true</tt> if all of terms in the Sentence are valid per {@link KbTerm#isValid() }.
   *
   * @return  false if the KB object behind this object has been deleted or otherwise rendered
   *          invalid on the Cyc server. Returns true otherwise
   */
  Boolean isValid();
  
}
