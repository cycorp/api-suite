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
import com.cyc.kb.exception.DeleteException;
import com.cyc.kb.exception.InvalidNameException;
import com.cyc.kb.exception.KbException;
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
 */
public interface KbObject {
  
  /**
   * Gets the asserted Facts visible from <code>ctx</code>, using the predicate
   * <code>predicate</code> with <code>this</code> at position <code>thisArgPos</code>
   * of the Fact
   *
   * @param   predicate   the Predicate of the returned fact
   * @param   thisArgPos  the position where <code>this</code> is found in the fact
   * @param   ctx         the Context. If null, returns facts from the default context
   *                      {@link com.cyc.kb.DefaultContext#forQuery()}
   * @return  a collection of facts, empty if none are found
   */
  Collection<Fact> getFacts(KbPredicate predicate, int thisArgPos, Context ctx);

  /**
   * This method gets asserted facts visible from <code>ctx</code> with
   * predicate represented by <code>pred</code> and <code>this</code> at the
   * <code>thisArgPos</code> arg position of the fact, and returns as
   * <code>O</code> objects based on the arguments in the <code>getArgPos</code>
   * argument position of the facts.
   * 
   * @param   <O>         the type of the objects returned
   * @param   predStr     the string representation of the predicate
   * @param   thisArgPos  the argument position of this object in the candidate facts
   * @param   getArgPos   the argument position of the returned objects in the candidate facts
   * @param   ctxStr      the string representation of the context. If set to "", returns facts from
   *                      the default context {@link com.cyc.kb.DefaultContext#forQuery()}
   * @return  a collection of objects of type O
   * @see     #getFacts(KbPredicate, int, Context) for a method that returns the facts, rather than
   *          just the objects at a specific argument position of the facts
   */
  <O> Collection<O> getValues(String predStr, int thisArgPos, int getArgPos, String ctxStr);

  /**
   * This method gets all facts with predicate <code>pred</code> and
   * <code>this</code> at the <code>thisArgPos</code> arg position of the fact,
   * as visible from <code>ctx</code>, and returns as <code>O</code> objects
   * based on the arguments in the <code>getArgPos</code> argument position of
   * the facts.
   * 
   * @param   <O>         the type of the objects returned
   * @param   pred        the predicate of the facts
   * @param   thisArgPos  the argument position of this object in the candidate facts
   * @param   getArgPos   the argument position of the returned objects in the candidate facts
   * @param   ctx         the context where the facts are found. If null, returns facts from the
   *                      default context {@link com.cyc.kb.DefaultContext#forQuery()}
   * @return  a collection of objects of type O
   * @see     #getFacts(KbPredicate, int, Context) for a method that returns the facts, rather than
   *          just the objects at a specific argument position of the facts
   */
  <O> Collection<O> getValues(KbPredicate pred, int thisArgPos, int getArgPos, Context ctx);

  /**
   * This method gets all facts visible from <code>ctx</code> that use predicate
   * <code>pred</code>, have <code>this</code> in the <code>thisArgPos</code>
   * arg position, and also have <code>matchArg</code> in the
   * <code>matchArgPos</code> arg position; it returns a list of <code>O</code>
   * objects from the <code>getArgPos</code> argument position of the fact.
   *
   * @param   <O>          the type of the objects returned
   * @param   pred         the predicate of the facts
   * @param   thisArgPos   the argument position of <code>this</code> in the candidate facts
   * @param   getArgPos    the argument position of the returned objects in the candidate facts
   * @param   matchArg     the object in the argument position matchArgPos
   * @param   matchArgPos  the argument position that must be filled with matchArg
   * @param   ctx          the context. If null, returns facts from the default context
   *                       {@link com.cyc.kb.DefaultContext#forQuery()}
   * @return  a collection of objects of type O
   */
  <O> Collection<O> getValues(
          KbPredicate pred, int thisArgPos, int getArgPos, 
          Object matchArg, int matchArgPos, Context ctx);

  /**
   * Finds or creates a new Fact in the underlying KB
   * 
   * <p>The method finds a Fact in the KB with predicate <code>pred</code>, and
   * <code>this</code> at the argument position <code>thisArgPos</code>, in the
   * context <code>ctx</code>. The otherArgs specify all the arguments of the
   * Fact, completely. If the fact specified by the input arguments is not
   * found, creates and persists a new fact in the underlying KB.
   *
   * <p>Note: Not all subclasses of the KB objects are directly assertible. For
   * example, Sentence, Variable and Symbol need to be quoted, using Quote
   * method, before they can participate in closed assertions.
   *
   * <p>Note: The context is the first argument in methods that have variable
   * number of arguments. In all other methods, context is the last argument.
   *
   * @param   ctx         the context where the fact is found or created
   * @param   pred        the predicate of the fact
   * @param   thisArgPos  the argument position of this object in the fact
   * @param   otherArgs   the arguments in positions other than p (0th argument) and argPos
   * @return  the fact found or created
   * @throws  KbTypeException
   * @throws  CreateException
   */
  Fact addFact(Context ctx, KbPredicate pred, int thisArgPos, Object... otherArgs) 
          throws KbTypeException, CreateException;

  /**
   * Get all collections that the <code>this</code> object is a quoted instance
   * of. The collections are instance of <code>SubLExpressionType</code>.
   * 
   * <p>All subclasses of KbObject can be quoted. Refer to <code>#$NoteAboutQuotingInCycL</code> for
   * a more detailed discussion of quoting.
   *
   * @return  the collection of KbCollections that <code>this</code> is quoted instance of
   */
  Collection<KbCollection> getQuotedIsa();

  /**
   * A <code>quotedIsa</code> assertion relates CycL expression to <code>SubLExpressionType</code>.
   * 
   * <p>All subclasses of KbObject can be quoted. Refer to <code>#$NoteAboutQuotingInCycL</code> for
   * a more detailed discussion of quoting.
   *
   * @param   collection  the instance of SubLExpressionType, the collection <code>this</code> is a 
   *                      quoted instance of
   * @param   context     the context where the fact is asserted.
   * @throws  CreateException
   * @throws  KbTypeException
   */
  void addQuotedIsa(KbCollection collection, Context context) 
          throws KbTypeException, CreateException;

  /**
   * Creates a new <code>Fact</code> stating that <code>this</code> <code>KbIndividual</code>
   * instantiates the <code>#$Collection</code> represented by <code>collection</code> in the 
   * context represented by <code>context</code>. Effectively, this asserts 
   * <code>(#$isa this collection)</code>.
   *
   * @param   collectionStr  string representing the KbCollection this individual is an instance of
   * @param   contextStr     string representing the context where the fact is to be asserted
   * @return  this KbObject, for method chaining
   * @throws  CreateException
   * @throws  KbTypeException
   */
  KbObject instantiates(String collectionStr, String contextStr) 
          throws KbTypeException, CreateException;

  /**
   * Creates a new <code>Fact</code> stating that this <code>KbIndividual</code> instantiates the
   * <code>collection</code> in <code>context</code>. Effectively, this asserts 
   * <code>(#$isa this collection)</code>.
   *
   * @param   collection  the collection of which this KbIndividual is an instance
   * @param   context     the context where the fact is to be asserted
   * @return  this KbObject, for method chaining
   * @throws  CreateException
   * @throws  KbTypeException
   */
  KbObject instantiates(KbCollection collection, Context context) 
          throws KbTypeException, CreateException;
  
  /**
   * Creates a new <code>Fact</code> stating that this <code>KbIndividual</code> instantiates the
   * <code>collection</code> in the default assertion context.
   * 
   * See {@link #instantiates(com.cyc.kb.KbCollection, com.cyc.kb.Context) } for more details.
   * 
   * @param   collection  the collection of which this KbIndividual is an instance
   * @return  this KbObject, for method chaining
   * @throws  KbTypeException
   * @throws  CreateException 
   */
  KbObject instantiates(KbCollection collection) throws KbTypeException, CreateException;

  /**
   * This method returns the Sentence <code>(#$isa this collection)</code>. The key difference 
   * between this and {@link #instantiates(com.cyc.kb.KbCollection) } is that, this method does not 
   * make any assertion in the KB. The sentence form of the assertion is generally useful when
   * seeking user feedback before asserting into the KB. Use 
   * {@link Sentence#assertIn(com.cyc.kb.Context) } to assert the sentence in a Context.
   * 
   * @param   collection  the collection of which <code>this</code> KbIndividual is an instance
   * @return  the #$isa sentence between this and col
   * @throws  KbTypeException 
   */
  Sentence instantiatesSentence(KbCollection collection) throws KbTypeException, CreateException;
  
  /**
   * Is <code>this</code> an instance of <code>collection</code> in any context? This does not 
   * require that <code>(#$isa this collection)</code> be asserted, merely that it be trivially 
   * inferable.
   *
   * @param   collection  the collection which <code>this</code> may or may not be an instance of
   * @return  whether <tt>this</tt> is trivially provable to be an instance of <tt>collection</tt>
   */
  boolean isInstanceOf(KbCollection collection);

  /**
   * Is <code>this</code> an instance of <code>collection</code> in any context? This does not 
   * require that <code>(#$isa this collection)</code> be asserted, merely that it be trivially 
   * inferable.
   *
   * @param   collectionStr  the string representing the collection which <code>this</code> may or
   *                         may not be an instance of
   * @return  whether <tt>this</tt> is trivially provable to be an instance of <tt>collection</tt>
   */
  boolean isInstanceOf(String collectionStr);

  /**
   * Is <code>this</code> an instance of <code>collection</code> in <code>context</code>? This does 
   * not require that <code>(#$isa this collection)</code> be asserted, merely that it be trivially 
   * inferable.
   *
   * @param   collection  the collection which <code>this</code> may or may not be an instance of
   * @param   context     the context where the instance relation holds.
   * @return  whether <tt>this</tt> is trivially provable to be an instance of <tt>collection</tt>
   */
  boolean isInstanceOf(KbCollection collection, Context context);

  /**
   * Is <code>this</code> an instance of <code>collection</code> in <code>context</code>? This does
   * not require that <code>(#$isa this collection)</code> be asserted, merely that it be trivially
   * inferable.
   *
   * @param   collectionStr  string representation of the collection which <code>this</code> may or 
   *                         may not be an instance of
   * @param   contextStr     string representation of the context where the instance relation holds
   * @return  whether <tt>this</tt> is trivially provable to be an instance of <tt>collection</tt>
   */
  boolean isInstanceOf(String collectionStr, String contextStr);
  
  /**
   * Is this object an indexical? I.e., it is an instance of <code>#$IndexicalConcept</code>?
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
   * Is there an assertion in <code>ctx</code> with a sentence using
   * <code>pred</code>, with <code>this</code> in the <code>thisArgPos</code>
   * argument position, and with the arguments in <code>otherArgs</code> as the
   * rest of its arguments? This method will not throw exceptions. Effectively,
   * this is a wrapper around 
   * {@link #getFact(com.cyc.kb.Context, com.cyc.kb.KbPredicate, int, java.lang.Object...) }
   * that returns false if there are any exceptions.
   *
   * <p>Note: The context is the first argument in methods that have variable
   * number of arguments. In all other methods, context is the last argument.
   *
   * @param   ctx         the context in which the sentence is asserted
   * @param   pred        the predicate of the candidate assertion
   * @param   thisArgPos  the argument position of <code>this</code> object in assertion
   * @param   otherArgs   the arguments other than <code>pred</code> and <code>this</code>
   * @return  true if and only if the fact is determined to be true
   */
  Boolean isAsserted(Context ctx, KbPredicate pred, int thisArgPos, Object... otherArgs);

  /**
   * find the specific existing Fact, in <code>ctx</code>, that has
   * <code>pred</code> as its predicate, <code>this</code> in the
   * <code>thisArgPos</code> argument position, and <code>otherArgs</code> as
   * its other arguments. Effectively, <code>this</code> is inserted into
   * position <code>thisArgPos</code> of <code>otherArgs</code> to form the
   * arguments for a Fact that is searched for in the KB. If an assertion using
   * that sentence is found in the context, the Fact is returned. If no such
   * assertion is found, appropriate exception is thrown.
   *
   * Note: The context is the first argument in methods that have variable
   * number of arguments. In all other methods, context is the last argument.
   *
   * @param   ctx         the context of the fact
   * @param   pred        the Predicate of the returned fact
   * @param   thisArgPos  the position where <code>this</code> is found in the fact
   * @param   otherArgs   the arguments in positions other than 0 and thisArgPos. They must be
   *                      KbObjects or Java objects such as Date, int, float, String.
   * @return  the fact represented by the parameters in context <code>ctx</code>
   * @throws  CreateException
   * @throws  KbTypeException
   */
  Fact getFact(Context ctx, KbPredicate pred, int thisArgPos, Object... otherArgs) 
          throws KbTypeException, CreateException;

  /**
   * Constructs a Sentence, that has <code>pred</code> as its predicate,
   * <code>this</code> in the <code>thisArgPos</code> argument position, and
   * <code>otherArgs</code> as its other arguments. Effectively,
   * <code>this</code> is inserted into position <code>thisArgPos</code> of
   * <code>otherArgs</code> to form the sentence.
   *
   * @param   pred        the Predicate of the returned fact
   * @param   thisArgPos  the position where <code>this</code> is found in the fact
   * @param   otherArgs   the arguments in positions other than 0 and thisArgPos. They must be 
   *                      KbObjects or Java objects such as Date, int, float, String.
   * @return  the sentence constructed by the parameters
   * @throws  KbTypeException
   */
  // KB API does not do any introspection.. so if we want to use Query API, we should construct
  // a fully qualified sentence for the given predicate. That can only be possible when all
  // other variables are passed in.
  // 
  Sentence getSentence(KbPredicate pred, int thisArgPos, Object... otherArgs) 
          throws KbTypeException, CreateException;

  /**
   * Adds a new Fact in <code>ctx</code> using <code>binPred</code>, with
   * <code>arg1</code> as the first argument, and <code>this</code> as the
   * second argument.
   *
   * @param   binPred  the predicate of the fact
   * @param   arg1     the arg1 of the new binary predicate fact
   * @param   ctx      the context where the fact is added
   * @return  the newly added Fact
   * @throws  CreateException
   * @throws  KbTypeException
   */
  // @todo add versions of this that take Strings as well as KbObjects.
  Fact addArg1(BinaryPredicate binPred, Object arg1, Context ctx)
          throws KbTypeException, CreateException;

  /**
   * Adds a new Fact in <code>ctx</code> using <code>binPred</code>, with
   * <code>this</code> as the first argument, and <code>arg2</code> as the
   * second argument.
   *
   * @param   binPred  the predicate of the fact
   * @param   arg2     the arg2 of the new binary predicate fact
   * @param   ctx      the context where the fact is added
   * @return  the newly added Fact
   * @throws  KbTypeException
   * @throws  CreateException
   */
  // @todo add versions of this that take Strings as well as KbObjects.
  Fact addArg2(BinaryPredicate binPred, Object arg2, Context ctx) 
          throws KbTypeException, CreateException;

  /**
   * Gets all the comments for <code>this</code> visible from the default
   * context {@link com.cyc.kb.DefaultContext#forQuery()}
   * <p>
   *
   * @return  comment strings
   */
  Collection<String> getComments();

  /**
   * Gets all the comments for <code>this</code> visible from the context
   * <p>
   *
   * @param   ctxStr  the context of query
   * @return  comment strings
   */
  Collection<String> getComments(String ctxStr);

  /**
   * Gets all the comments for <code>this</code> visible from the context
   * <p>
   *
   * @param   ctx  the context of query
   * @return  comment strings
   */
  Collection<String> getComments(Context ctx);

  /**
   * Add a new comment for <code>this</code> in the context specified
   * <p>
   *
   * In the CycKB comments can be added only on <code>#$CycLIndexedTerm</code>s,
   * which include <code>CycLReifiableDenotationalTerm</code> and
   * <code>CycLAssertion</code>. An exception will be thrown if attempted to add
   * a comment on Quoted terms, Sentence, Variable and Symbol. This means that
   * only subclasses of KbTerm and Assertion can have comments.
   *
   * @param   ctx      the context where the comment is created. Cannot be null.
   * @param   comment  the comment string
   * @return  the fact created
   * @throws  CreateException
   * @throws  KbTypeException
   */
  Fact addComment(String comment, String ctx) throws KbTypeException, CreateException;

  /**
   * Add a new comment for <code>this</code> in the context specified
   * <p>
   *
   * In the CycKB comments can be added only on <code>#$CycLIndexedTerm</code>s,
   * which include <code>CycLReifiableDenotationalTerm</code> and
   * <code>CycLAssertion</code>. An exception will be thrown if attempted to add
   * a comment on Quoted terms, Sentence, Variable and Symbol. This means that
   * only subclasses of KbTerm and Assertion can have comments.
   *
   * @param   ctx      the context where the comment is created. Cannot be null.
   * @param   comment  the comment string
   * @return  the fact created
   * @throws  CreateException
   * @throws  KbTypeException
   */
  Fact addComment(String comment, Context ctx) throws KbTypeException, CreateException;

  /**
   * gets the object in <code>getPos</code> argument position of this KbObject
   * as an object of type <code>O</code>. This method works for Sentences and
   * Assertions, as well as non-atomic KbTerms. However, because a constant has
   * no "arguments", calling this method on a KbObject representing a Cyc
   * constant will result in a KbException.
   *
   * @param   <O>     the object type
   * @param   getPos  the argument position of the object returned
   * @return  the object at <code>getPos</code> as a <code>O</code>
   * @throws  CreateException
   * @throws  KbTypeException
   * @throws  UnsupportedOperationException  if getArgument is called on Atomic terms. Do not use
   *                                         this to test for term atomicity, use
   *                                         {@link #isAtomic()} instead.
   * @throws  IllegalArgumentException       for a class of object types which do not support
   *                                         getArgument. Example include: Variable, Symbol.
   */
  <O> O getArgument(int getPos) throws KbTypeException, CreateException;

  /**
   * Is <tt>this</tt> a non-decomposable KbObject? This will return true for <tt>KbObject</tt>s that
   * represent Cyc constants, Cyc variables, or Cyc symbols. It returns false for anything else. For
   * atomic assertions, or ground atomic formula, use instanceof Fact.
   *
   * @return  true if and only if this KbObject is not decomposable into other constituent KbObjects
   */
  Boolean isAtomic();

  /**
   * Does <code>this</code> correspond to any kind of Cyc assertion?
   *
   * @return  true if and only if this object is an assertion
   */
  Boolean isAssertion();

  /**
   * This method returns a sentence with the type restriction of the KbObject or
   * its subclasses, even the ones extended outside of KB API. For example,
   * restriction for KbPredicate object would be (isa <THIS PRED> #$Predicate).
   *
   * This is most useful for building sentences of KB Object typed-variables,
   * for use in rules and queries.
   *
   * @return  the restriction sentence for the class
   */
  Sentence getRestriction() throws KbException;

  /**
   * Returns the syntactic arity of this object. If it has a relation applied to
   * some arguments (i.e. it's a sentence, an assertion, or a functional term),
   * the arity is the number of arguments. By convention, Cyc constants have a
   * formula arity of 0.
   *
   * @return  the arity of this object, <tt>null</tt> if not a Cyc constant, functional term, 
   *          sentence, or assertion
   */
  Integer formulaArity();

  /**
   * provides direct access to the encapsulated object.
   * <p>
   * This method should generally not be needed for application code, though it
   * may be necessary is some rare occasions.
   *
   * @return  the implementation-specific wrapped object
   */
  Object getCore();
  
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
   * delete <code>this</code> term and all the facts using it in the KB.
   * <p>
   * This will irreversible modify the KB.
   *
   * @throws  DeleteException if the operation cannot be completed
   */
  void delete() throws DeleteException;

  /**
   * Returns false if the KB object behind this object has been deleted or
   * otherwise rendered invalid on the Cyc server.
   *
   * @return  false if the KB object behind this object has been deleted or otherwise rendered
   *          invalid on the Cyc server. Returns true otherwise
   */
  Boolean isValid();

  /**
   * Change the name of this object to <code>name</code>. Throws
   * {@link InvalidNameException} in cases where the rename cannot be completed,
   * for any reason, one of the reasons could be that the name does not conform
   * to Cyc constant-naming conventions. Among objects that cannot be renamed
   * are non-atomic terms, assertions, and constant that are mentioned in server
   * code. An {@link UnsupportedOperationException} will be thrown if attempted
   * to rename Assertion or Non-atomic terms.
   *
   * @param   name   the new name
   * @return  the renamed KbObject
   * @throws  InvalidNameException           if Cyc Server errors out for any reason, including 
   *                                         invalid name
   * @throws  UnsupportedOperationException  if the object being renamed is not {@link #isAtomic()}
   */
  // @todo how can this actually throw the exception? And it should throw a more informative 
  // Exception.
  KbObject rename(String name) throws InvalidNameException;

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
   * Get the default natural-language string for this object.
   *
   * @return  a natural-language string denoting this object
   * @throws  SessionException
   * @throws  UnsupportedOperationException  if unable to build a {@link Paraphraser}
   */
  String toNlString() throws SessionException;

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
   * Return the KbCollection, as a KbObject, of the Cyc term that underlies this class. For example,
   * calling this on a <tt>KbCollection</tt> object will return the KbCollection for #$Collection.
   *
   * @return the KbCollection of the underlying Cyc term of the class
   */
  KbObject getType();
}
