package com.cyc.kb;

/*
 * #%L
 * File: KbObject.java
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
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbTypeException;
import com.cyc.nl.Paraphraser;
import com.cyc.session.exception.SessionCommunicationException;
import com.cyc.session.exception.SessionException;
import java.util.Collection;
import java.util.Map;

/**
 * The top-level interface for representing objects in a Cyc KB.
 * 
 * @author vijay
 * @author nwinant
 */
public interface KbObject {
  
  /**
   * Returns a stable, globally unique ID for <code>this</code>. In terms of
   * Cyc-specific nomenclature, this Id is the compact, hl external ID for the
   * underlying Cyc object.
   *
   * Generally the HL IDs of indexed terms <code>#$CycLIndexedTerm</code>s are
   * useful. Cyc can generate HL IDs for Sentences, Variables and Symbols.
   *
   * @return  the globally unique ID
   */
  String getId();
  
  /**
   * Return the KbCollection, as a KbObject, of the Cyc term that underlies this class. For example,
   * calling this on a <tt>KbCollection</tt> object will return the KbCollection for #$Collection.
   *
   * @return the KbCollection of the underlying Cyc term of the class
   */
  KbObject getType();
    
  /**
   * Provides direct access to the encapsulated object.
   * <p>
   * This method should generally not be needed for application code, though it
   * may be necessary is some rare occasions.
   *
   * @return  the implementation-specific wrapped object
   */
  Object getCore();
  
  /**
   * Is <tt>this</tt> a non-decomposable KbObject? This will return true for <tt>KbObject</tt>s that
   * represent Cyc constants, Cyc variables, or Cyc symbols. It returns false for anything else. For
   * atomic assertions, or ground atomic formula, use instanceof Fact.
   *
   * @return  true if and only if this KbObject is not decomposable into other constituent KbObjects
   */
  Boolean isAtomic();
  
  /**
   * Is this object any kind of {@link Assertion}?
   *
   * @return true if and only if this object represents a <tt>#$CycLAssertion</tt>
   */ 
  Boolean isAssertion();
  
  /**
   * Is this object any kind of {@link KbCollection}?
   *
   * @return true if and only if this object represents a <tt>#$Collection</tt>
   */
  Boolean isCollection();
  
  /**
   * Is this object a {@link Context}?
   *
   * @return true if and only if this object represents a <tt>#$Microtheory</tt>
   */
  Boolean isContext();
  
  /**
   * Is this object a {@link KbFunction}?
   *
   * @return true if and only if this object represents a <tt>#$Function-Denotational</tt>
   */
  Boolean isFunction();
  
  /**
   * Is this object any kind of {@link KbIndividual}?
   *
   * @return true if and only if this object represents a <tt>#$Individual</tt>
   */
  Boolean isIndividual();
  
  /**
   * Is this object any kind of {@link KbPredicate}?
   *
   * @return true if and only if this object represents a <tt>#$Predicate</tt>
   */
  Boolean isPredicate();
  
  /**
   * Is this object a {@link Sentence}?
   *
   * @return true if and only if this object represents a <tt>#$CycLSentence</tt>
   */
  Boolean isSentence();
  
  /**
   * Is this object a {@link Symbol}?
   *
   * @return true if and only if this object represents a <tt>#$CycLSubLSymbol</tt>
   */
  Boolean isSymbol();
  
  /**
   * Is this object any kind of {@link KbTerm}?
   *
   * @return true if and only if this object represents a <tt>#$CycLDenotationalTerm</tt>
   */
  Boolean isTerm();
  
  /**
   * Is this object a {@link Variable}?
   *
   * @return true if and only if this object represents a <tt>#$CycLVariable</tt>
   */
  Boolean isVariable();
  
  /**
   * Is this object an indexical? I.e., it is an instance of <tt>#$IndexicalConcept</tt>?
   * 
   * <p>Indexicals are those terms which depend upon the occasion of use or the user. For example,
   * consider words such as "I", "Now", or "Here". When I use the word "I," I am referring to 
   * myself, but when you use the word "I", you are referring to yourself. Similarly, "Now" denotes 
   * the moment in which the word is used, and "Here" refers to the location at which the word is 
   * used.
   * 
   * <p>An entity which is referenced by an indexical is known as a <em>referent</em>. For example,
   * the referent of "Here" could be France, the City of Austin, the room that we're currently in,
   * or whatever else the geographical context might be when the word is used. (See 
   * <code>#$indexicalReferent</code> in the Cyc KB.) The process of determining the referent for an
   * indexical is known as <em>resolving</em> the indexical.
   * 
   * <p>Some indexicals can be resolved automatically by Cyc; for example, Cyc can automatically 
   * resolve <code>#$Now-Indexical</code> to the current time, <code>#$TheUser</code> to the current
   * Cyclist (per {@link com.cyc.session.SessionOptions#getCyclistName() }, or 
   * <code>#$TheCurrentHostName</code> to the hostname of the Cyc server. Other indexicals must be
   * ultimately resolved somehow by the external application; for example, Cyc doesn't have any
   * rules for resolving an indexical like <code>(TheNamedFn InformationStore "data source")</code>.
   * 
   * <p>In the case of queries, <em>all</em> of the indexicals in a query's sentence and context 
   * must be resolved in order for the query to be run. Cyc will handle any automatically resolvable
   * indexicals (such as <code>#$Now-Indexical</code>), but all other indexicals must be resolved by
   * the application; for example, <code>(TheNamedFn InformationStore "data source")</code> would
   * need to be replaced with a specific instance of <code>#$InformationStore</code>. This can be 
   * accomplished by adding substitutions to a Query; for more information, see
   * {@link com.cyc.query.QuerySpecification#setSubstitutions(java.util.Map) }.
   * 
   * @return  whether the object is an indexical
   * @throws  SessionCommunicationException  if there is a problem communicating with Cyc
   * @see     com.cyc.kb.KbObject#resolveIndexical() 
   * @see     com.cyc.kb.KbTerm#replaceTerms(java.util.Map) 
   * @see     com.cyc.kb.Sentence#getIndexicals(boolean)
   * @see     com.cyc.kb.Sentence#replaceTerms(java.util.Map) 
   * @see     com.cyc.query.QuerySpecification#setSubstitutions(java.util.Map) 
   * @see     com.cyc.query.QuerySpecification#addSubstitutions(java.util.Map) 
   * @see     com.cyc.query.QuerySpecification#getSubstitutions() 
   */
  boolean isIndexical() throws SessionCommunicationException;
  
  /**
   * Get all collections that the <code>this</code> object is a quoted instance of. The collections
   * are instance of <code>SubLExpressionType</code>.
   * 
   * <p>All subclasses of KbObject can be quoted. Refer to <code>#$NoteAboutQuotingInCycL</code> for
   * a more detailed discussion of quoting.
   *
   * @return  the collection of KbCollections that <code>this</code> is quoted instance of
   */
  Collection<KbCollection> getQuotedIsa();
  
  /**
   * Is <code>this</code> a quoted instance of <code>col</code> in any context? Essentially this
   * verifies that <code>(#$quotedIsa this col)</code> is true in some context.
   *
   * <p>Refer to <code>#$NoteAboutQuotingInCycL</code> for a more detailed discussion of quoting.
   *
   * @param   col  the collection which <code>this</code> may or may not be a quoted instance of
   * @return  whether <code>this</code> is provable to be a quoted instance of <code>col</code>
   */
  boolean isQuotedInstanceOf(KbCollection col);

  /**
   * Is <code>this</code> a quoted instance of <code>col</code> in any context? Essentially this 
   * verifies that <code>(#$quotedIsa this col)</code> is true in some context.
   *
   * <p>Refer to <code>#$NoteAboutQuotingInCycL</code> for a more detailed discussion of quoting.
   *
   * @param   colStr  the string representation of the collection which <code>this</code> may or may
   *                  not be a quoted instance of
   * @return  whether <code>this</code> is provable to be a quoted instance of <code>col</code>
   */
  boolean isQuotedInstanceOf(String colStr);

  /**
   * Is <code>this</code> a quoted instance of <code>col</code>? Essentially this verifies that
   * <code>(#$quotedIsa this col)</code> is true in the context <code>ctx</code>.
   *
   * @param   col  the collection which <code>this</code> may or may not be a quoted instance of
   * @param   ctx  the context
   * @return  whether <code>this</code> is provable to be a quoted instance of <code>col</code>
   */
  boolean isQuotedInstanceOf(KbCollection col, Context ctx);

  /**
   * Is <code>this</code> a quoted instance of <code>col</code>? Essentially this verifies that 
   * <code>(#$quotedIsa this col)</code> is true in the context <code>ctx</code>.
   *
   * @param   colStr  the string representation of the collection which <code>this</code> may or may
   *                  not be a quoted instance of
   * @param   ctxStr  the string representation of the context
   * @return  whether <code>this</code> is provable to be a quoted instance of <code>col</code>
   */
  boolean isQuotedInstanceOf(String colStr, String ctxStr);

  /**
   * Returns whether this is a quoted object; i.e., if it refers to the CycL object <em>itself</em>
   * and not its meaning. For example, <tt>(#$Quote #$Plato)</tt> refers to the term 
   * <tt>#$Plato</tt>, and not to the philosopher.
   *
   * <p>Refer to <tt>#$NoteAboutQuotingInCycL</tt> for a more detailed discussion of quoting.
   * 
   * @return  whether this is a quoted object
   * @throws  KbTypeException
   * @throws  CreateException 
   * @see     #quote() 
   * @see     #unquote() 
   * @see     #toQuoted() 
   * @see     #toUnquoted() 
   */
  boolean isQuoted() throws KbTypeException, CreateException;
  
  /**
   * Returns a quoted version of this object. E.g., this method would return 
   * <tt>(#$Quote #$Plato)</tt> from <tt>#$Plato</tt>. This method does not modify the original
   * KbObject, but instead returns a new KbObject. Calling this method on a quoted object will 
   * return a quoted object which is nested one level deeper; e.g., this method would return 
   * <tt>(#$Quote (#$Quote #$Plato))</tt> from <tt>(#$Quote #$Plato)</tt>. For a version of this
   * method which won't further nest quoted objects, see {@link #toQuoted() }.
   * 
   * <p>See {@link #isQuoted() } for more details about quoted objects.
   * 
   * @return  the quoted version of <tt>this</tt> object
   * @throws  CreateException
   * @throws  KbTypeException
   * @see     #isQuoted()
   * @see     #unquote() 
   * @see     #toQuoted() 
   * @see     #toUnquoted() 
   * 
   */
  KbIndividual quote() throws KbTypeException, CreateException;
  
  /**
   * Returns the object referred to by a quoted object. E.g., this method would return 
   * <tt>#$Plato</tt> from <tt>(#$Quote #$Plato)</tt>. This method does not modify the original 
   * KbObject, but instead returns a new object. This method will only remove one level of quoting;
   * e.g., it would return <tt>(#$Quote #$Plato)</tt> from <tt>(#$Quote (#$Quote #$Plato))</tt>. If
   * the object is <em>not</em> quoted, a KbTypeException will be thrown. For a version of this 
   * method which won't throw an exception for an unquoted object, and which will fully unquote 
   * nested quotes, see {@link #toUnquoted() }.
   * 
   * <p>See {@link #isQuoted() } for more details about quoted objects.
   * 
   * @param   <O>  expected return type of the unquoted object
   * @return  the unquoted version of <tt>this</tt> object
   * @throws  KbTypeException
   * @throws  CreateException 
   * @see     #isQuoted() 
   * @see     #quote() 
   * @see     #toQuoted() 
   * @see     #toUnquoted()
   */
  <O> O unquote() throws KbTypeException, CreateException;
  
   /**
   * Returns a quoted version of this object. This method is similar to {@link #quote() }, but will 
   * return a reference to itself if the object is already quoted (as opposed to returning a nested
   * version of the quoted object). E.g., this method would return <tt>(#$Quote #$Plato)</tt> from
   * either <tt>#$Plato</tt> or <tt>(#$Quote #$Plato)</tt>. This method does not modify the original
   * KbObject, but instead returns a new KbObject (or a reference to itself, if it's already 
   * quoted.)
   *
   * <p>See {@link #isQuoted() } for more details about quoted objects.
   * 
   * @return  the quoted version of <tt>this</tt> object, or this object if it is already quoted
   * @throws  CreateException
   * @throws  KbTypeException
   * @see     #isQuoted() 
   * @see     #quote() 
   * @see     #unquote() 
   * @see     #toUnquoted() 
   */
  KbIndividual toQuoted() throws KbTypeException, CreateException;
  
  /**
   * Returns a fully-unquoted version of this object. This method is similar to {@link #unquote() },
   * but will remove multiple levels of quoting if necessary, and will return a reference to itself 
   * if the object is <em>not</em> quoted. E.g., this method would return <tt>#$Plato</tt> from
   * <tt>#$Plato</tt>, <tt>(#$Quote #$Plato)</tt>, or <tt>(#$Quote (#$Quote #$Plato))</tt>. This 
   * method does not modify the original KbObject, but instead returns a new object (or a reference
   * to itself, if it's already fully unquoted.)
   * 
   * <p>See {@link #isQuoted() } for more details about quoted objects.
   * 
   * @param   <O>  expected return type of the unquoted object
   * @return  the unquoted version of <tt>this</tt> object, or this object if it is not quoted
   * @throws  KbTypeException
   * @throws  CreateException 
   * @see     #isQuoted() 
   * @see     #quote() 
   * @see     #unquote() 
   * @see     #toQuoted() 
   */
  <O> O toUnquoted() throws KbTypeException, CreateException;
  
  /**
   * Attempts to resolve the object from a substitutions map. If the object is an indexical and has
   * a referent specified in the substitutions map, this method will return the referent object from
   * the substitutions map; if the original object is not an indexical, it will simply return
   * itself. Note that this method will not attempt to do any automatic resolution; see 
   * {@link KbObject#resolveIndexical() } if you need that.
   * 
   * <p>This method is specifically intended to be called in large batch operations where some 
   * KbObjects may be indexicals and others may not, and to be chained with other methods like 
   * {@link KbTerm#replaceTerms(java.util.Map) }, so it attempts to be a little more forgiving
   * than #resolveIndexical(). For this reason, it will not throw an exception simply because the 
   * original object is not an indexical. However, it will throw an exception under any of the 
   * following conditions: 
   * 
   * <ul>
   *   <li>The object is not an indexical, but a referent is found in the substitutions map.</li>
   *   <li>The object is an indexical which cannot be automatically resolved, and no referent is 
   *       found in the substitutions map.</li>
   *   <li>The referent provided by the substitutions map is the same as the original object.</li>
   *   <li>The referent is not of the expected type.</li>
   * </ul>
   * 
   * <p>For a brief overview of indexicals, see {@link com.cyc.kb.KbObject#isIndexical() }.
   * 
   * @param   <O>            the referent's expected type
   * @param   substitutions  the substitutions map
   * @return  the object's referent, or the object itself (if not an indexical)
   * @throws  SessionCommunicationException  if there is a problem communicating with Cyc
   * @throws  KbTypeException                if the referent is not of the expected type
   * @see     #resolveIndexical() 
   */
  <O> O possiblyResolveIndexical(Map<KbObject, Object> substitutions) 
          throws SessionCommunicationException, KbTypeException;
  
  /**
   * If the object is an indexical, attempt to resolve it and return the appropriate referent. For 
   * example, if called on <code>#$Now-Indexical</code>, this method would return the current date
   * and time. This method does not modify the original KbObject, but instead returns a new
   * KbObject. If the indexical cannot be automatically resolved, or if the KbObject is not an
   * indexical, an exception will be thrown. 
   * 
   * <p>For a brief overview of indexicals, see {@link com.cyc.kb.KbObject#isIndexical() }.
   * 
   * @param   <O>  the referent's expected type
   * @return  the indexical's referent for the current context
   * @throws  SessionCommunicationException  if there is a problem communicating with Cyc
   * @throws  KbTypeException                if the object is not an indexical
   * @throws  CreateException                
   * @see     KbObject#possiblyResolveIndexical(java.util.Map) 
   */
  <O> O resolveIndexical() throws SessionCommunicationException, KbTypeException, CreateException;
  
  /**
   * Provides a version of the <code>toString</code> that is suitable for use
   * when talking directly to a Cyc server.
   * <p>
   * The CycL output of each CycObject is required to interact with the
   * underlying KB, for example, to find or create facts.
   *
   * @return  the CycL string representation of the wrapped CycObject
   */
  String stringApiValue();
  
  /**
   * Get the default natural-language string for this object.
   *
   * @return  a natural-language string denoting this object
   * @throws  SessionException
   * @throws  UnsupportedOperationException  if unable to build a {@link Paraphraser}
   */
  String toNlString() throws SessionException;
  
  /**
   * Get the CycL representation of <code>this</code> as a String. This
   * representation should be suitable for use with the KbObject factory
   * methods, but may not be suitable for use with lower-level APIs. It should
   * not be expected to contain "#$" constant-prefixes.
   *
   * @return  the string representation of the underlying CycObject
   */
  @Override
  String toString();
  
  /**
   * Does <tt>this</tt> represent the same object in the KB as <tt>object</tt>. This can return true
   * even if the two objects being compared are of different classes, as long as they represent the 
   * same object in the KB.
   *
   * Ideally if factory methods are used to create KB API objects, it will ensure that the object
   * returned will be of the tightest subclass possible, so object comparisons will work even when
   * cast to super.
   *
   * @param   object the object to compare against <code>this</code>
   * @return  true if the underlying CycObject is the same for two different KbObjects
   */
  boolean equalsSemantically(Object object);
  
}
