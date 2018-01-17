package com.cyc.kb;

import com.cyc.Cyc;
import com.cyc.kb.KbObject.KbObjectWithArity;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbException;
import com.cyc.kb.exception.KbTypeException;
import com.cyc.kb.spi.SentenceService;
import com.cyc.query.Query;
import com.cyc.query.exception.QueryConstructionException;
import com.cyc.query.parameters.InferenceParameters;
import com.cyc.session.exception.SessionCommunicationException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
 * #%L
 * File: Sentence.java
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
/**
 * The interface for Cyc sentences. Sentences encode propositions, which can be used as questions
 * (in a {@link Query}) or claims (in an {@link Assertion}).
 *
 * @author   vijay
 * @version  $Id: Sentence.java 176591 2018-01-09 17:27:27Z nwinant $
 * @since    1.0
 */
public interface Sentence extends KbObjectWithArity {

  //====|    Factory methods    |=================================================================//
  
  /**
   * Attempts to convert a CycL string into a CycFormulaSentence and thus into a KbObject, Sentence.
   * This static method wraps a call to {@link SentenceService#get(java.lang.String) }; see that
   * method's documentation for more details.
   *
   * @param sentStr the string representing a Sentence in the KB, a CycL sentence
   *
   * @return a Sentence object
   *
   * @throws com.cyc.kb.exception.KbTypeException
   *
   * @throws CreateException                      if the Sentence represented by sentStr could not
   *                                              be parsed.
   */
  public static Sentence get(String sentStr) throws KbTypeException, CreateException {
    return Cyc.getSentenceService().get(sentStr);
  }

  /**
   * Builds a sentence based on <code>pred</code> and other <code>args</code>. This static
   * method wraps a call to {@link SentenceService#get(com.cyc.kb.Relation, java.lang.Object...) };
   * see that method's documentation for more details.
   *
   * @param pred the first argument of the formula
   * @param args the other arguments of the formula in the order they appear in the list
   *
   * @return a Sentence object
   *
   * @throws KbTypeException                      is thrown if the built cycObject is not a instance
   *                                              of CycFormulaSentence. This should never happen.
   * @throws com.cyc.kb.exception.CreateException
   */
  public static Sentence get(Relation pred, Object... args) 
          throws KbTypeException, CreateException {
    return Cyc.getSentenceService().get(pred, args);
  }

  /**
   * Builds an arbitrary sentence based on the <code>args</code> provided. This static
   * method wraps a call to {@link SentenceService#get(java.lang.Object...) }; see that method's
   * documentation for more details.
   *
   * @param args the arguments of the formula in order
   *
   * @return a Sentence object
   *
   * @throws KbTypeException                      never thrown
   * @throws com.cyc.kb.exception.CreateException
   */
  public static Sentence get(Object... args) throws KbTypeException, CreateException {
    return Cyc.getSentenceService().get(args);
  }
  
  /**
   * Conjoin sentences. This static method wraps a call to 
   * {@link SentenceService#and(com.cyc.kb.Sentence...)}; see that method's documentation for more
   * details.
   *
   * @param sentences list of sentences to be conjoined
   *
   * @return a new conjoined sentence
   *
   * @throws KbTypeException
   * @throws com.cyc.kb.exception.CreateException
   */
  public static Sentence and(Sentence... sentences) throws KbTypeException, CreateException {
    return Cyc.getSentenceService().and(sentences);
  }

  /**
   * Conjoin sentences. This static method wraps a call to 
   * {@link SentenceService#and(java.lang.Iterable)}; see that method's documentation for more 
   * details.
   *
   * @param sentences list of sentences to be conjoined
   *
   * @return a new conjunction sentence
   *
   * @throws com.cyc.kb.exception.KbTypeException
   * @throws com.cyc.kb.exception.CreateException
   */
  public static Sentence and(Iterable<Sentence> sentences) throws KbTypeException, CreateException {
    return Cyc.getSentenceService().and(sentences);
  }

  /**
   * Make a new conditional sentence with the specified antecedent and consequent. This static
   * method wraps a call to 
   * {@link SentenceService#implies(java.util.Collection, com.cyc.kb.Sentence)}; see that method's 
   * documentation for more details.
   *
   * @param antecedent
   * @param consequent
   *
   * @return a new conditional Sentence
   *
   * @throws com.cyc.kb.exception.KbTypeException
   * @throws com.cyc.kb.exception.CreateException
   */
  public static Sentence implies(Collection<Sentence> antecedent, Sentence consequent)
          throws KbTypeException, CreateException {
    return Cyc.getSentenceService().implies(antecedent, consequent);
  }

  /**
   * Make a new conditional sentence with the specified antecedent and consequent. This static
   * method wraps a call to
   * {@link SentenceService#implies(com.cyc.kb.Sentence, com.cyc.kb.Sentence)}; see that method's 
   * documentation for more details.
   *
   * @param antecedent
   * @param consequent
   *
   * @return a new conditional Sentence
   *
   * @throws com.cyc.kb.exception.KbTypeException
   * @throws com.cyc.kb.exception.CreateException
   */
  public static Sentence implies(Sentence antecedent, Sentence consequent) 
          throws KbTypeException, CreateException {
    return Cyc.getSentenceService().implies(antecedent, consequent);
  }

  /**
   * Disjoin sentences. This static method wraps a call to
   * {@link SentenceService#or(com.cyc.kb.Sentence...)}; see that method's documentation for more 
   * details.
   *
   * @param sentences list of sentences to be disjoined
   *
   * @return a new disjunction sentence
   *
   * @throws KbTypeException
   * @throws com.cyc.kb.exception.CreateException
   */
  public static Sentence or(Sentence... sentences) throws KbTypeException, CreateException {
    return Cyc.getSentenceService().or(sentences);
  }

  /**
   * Disjoin sentences. This static method wraps a call to 
   * {@link SentenceService#or(java.lang.Iterable)}; see that method's documentation for more 
   * details.
   *
   * @param sentences list of sentences to be disjoined
   *
   * @return a new disjunction sentence
   *
   * @throws com.cyc.kb.exception.KbTypeException
   * @throws com.cyc.kb.exception.CreateException
   */
  public static Sentence or(Iterable<Sentence> sentences) throws KbTypeException, CreateException {
    return Cyc.getSentenceService().or(sentences);
  }
  
  //====|    Interface methods    |===============================================================//
  
  /**
   * Assert <code>this</code> sentence in the context <code>ctx</code>.
   *
   * In many cases, it will be useful to call
   * {@link #isAssertible(com.cyc.kb.Context)} before asserting a sentence.
   *
   * @param   ctx  context where <tt>this</tt> sentence will be asserted
   * @return  the {@link Assertion} object, either a {@link Fact} or a {@link Rule}
   * @throws  KbException  if the assertion fails for any reason, include being syntactically
                       invalid
   */
  Assertion assertIn(Context ctx) throws KbException;

  /**
   * Checks if a given sentence meets the constraints necessary to be assertible in
 <code>ctx</code>. In particular, returns true if the arguments of the sentence are compatible 
 with the argument constraints of the operator (i.e. could be made to actually meet the 
 constraints without removing any existing assertions). For sentences with predicate operators, 
 the arguments must also be compatible with all relevant interArg* constraints. In some cases,
 additional requirements are imposed (e.g. the constraints must actually be known to be met, not
 just not be known to be incompatible). For an explanation of why a particular sentence is not
 assertible, check {@link #notAssertibleExplanation(com.cyc.kb.Context)}
   *
   * @param   ctx  the context where semantic constraints are checked
   * @return  whether the sentence is compatible with all relevant syntactic and semantic 
          constraints, and is therefore assertible
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
          encounters an internal error.
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
   * @param   includeQueryable  should this return the variables for which aquery would return
                            bindings, or all free variables?
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
 Sentence, but instead returns a new one. To replace terms in a Query sentence, use 
 {@link com.cyc.query.QuerySpecification#setSubstitutions(java.util.Map)} instead.
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
   * Returns <tt>true</tt> if all of terms in the Sentence are valid per {@link KbTerm#isValid() }.
   *
   * @return  false if the KB object behind this object has been deleted or otherwise rendered
   *          invalid on the Cyc server. Returns true otherwise
   */
  Boolean isValid();
  
  default Query toQuery(Context ctx) throws QueryConstructionException {
    return Query.get(this, ctx);
  }
  
  default Query toQuery(Context ctx, InferenceParameters params) throws QueryConstructionException {
    return Query.get(this, ctx, params);
  }
  
}
