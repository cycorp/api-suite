package com.cyc.kb;

/*
 * #%L
 * File: KBObject.java
 * Project: KB API
 * %%
 * Copyright (C) 2013 - 2015 Cycorp, Inc
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
import com.cyc.base.CycConnectionException;
import java.util.Collection;
import java.util.List;

import com.cyc.base.cycobject.CycObject;
import com.cyc.baseclient.nl.Paraphraser;
import com.cyc.kb.config.KBAPIDefaultContext;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.DeleteException;
import com.cyc.kb.exception.InvalidNameException;
import com.cyc.kb.exception.KBApiException;
import com.cyc.kb.exception.KBTypeException;

/**
 * The top-level interface for representing objects in a Cyc KB.
 *
 * @author vijay
 */
public interface KBObject {

  /**
   * gets the asserted Facts visible from <code>ctx</code>, using the predicate
   * <code>p</code> with <code>this</code> at position <code>thisArgPos</code>
   * of the Fact
   * <p>
   *
   * @param p	the Predicate of the returned fact
   * @param thisArgPos	the position where <code>this</code> is found in the fact
   * @param ctx the Context. If null, returns facts from the default context
   * {@link KBAPIDefaultContext#forQuery()}
   *
   * @return a collection of facts, empty if none are found
   */
  public Collection<Fact> getFacts(KBPredicate p, int thisArgPos,
          Context ctx);

  /**
   * This method gets asserted facts visible from <code>ctx</code> with
   * predicate represented by <code>pred</code> and <code>this</code> at the
   * <code>thisArgPos</code> arg position of the fact, and returns as
   * <code>O</code> objects based on the arguments in the <code>getArgPos</code>
   * argument position of the facts.
   *
   * @param predStr the string representation of the predicate
   * @param thisArgPos the argument position of this object in the candidate
   * facts
   * @param getArgPos the argument position of the returned objects in the
   * candidate facts
   * @param <O> the type of the objects returned
   * @param ctxStr the string representation of the context. If set to "",
   * returns facts from the default context
   * {@link KBAPIDefaultContext#forQuery()}
   *
   * @return a collection of objects of type O
   *
   * @see #getFacts(KBPredicate, int, Context) for a method that returns the
   * facts, rather than just the objects at a specific argument position of the
   * facts.
   */
  public <O> Collection<O> getValues(String predStr, int thisArgPos,
          int getArgPos, String ctxStr);

  /**
   * This method gets all facts with predicate <code>pred</code> and
   * <code>this</code> at the <code>thisArgPos</code> arg position of the fact,
   * as visible from <code>ctx</code>, and returns as <code>O</code> objects
   * based on the arguments in the <code>getArgPos</code> argument position of
   * the facts.
   *
   * @param pred the predicate of the facts
   * @param thisArgPos the argument position of this object in the candidate
   * facts
   * @param getArgPos the argument position of the returned objects in the
   * candidate facts
   * @param <O> the type of the objects returned
   * @param ctx the context where the facts are found. If null, returns facts
   * from the default context {@link KBAPIDefaultContext#forQuery()}
   *
   * @return a collection of objects of type O
   *
   * @see #getFacts(KBPredicate, int, Context) for a method that returns the
   * facts, rather than just the objects at a specific argument position of the
   * facts.
   */
  public <O> Collection<O> getValues(KBPredicate pred, int thisArgPos,
          int getArgPos, Context ctx);

  /**
   * This method gets all facts visible from <code>ctx</code> that use predicate
   * <code>pred</code>, have <code>this</code> in the <code>thisArgPos</code>
   * arg position, and also have <code>matchArg</code> in the
   * <code>matchArgPos</code> arg position; it returns a list of <code>O</code>
   * objects from the <code>getArgPos</code> argument position of the fact.
   *
   * @param pred	the predicate of the facts
   * @param thisArgPos	the argument position of <code>this</code> in the
   * candidate facts
   * @param getArgPos	the argument position of the returned objects in the
   * candidate facts
   * @param matchArg	the object in the argument position matchArgPos
   * @param matchArgPos	the argument position that must be filled with matchArg
   * @param <O>	the type of the objects returned
   * @param ctx	the context. If null, returns facts from the default context
   * {@link KBAPIDefaultContext#forQuery()}
   *
   * @return a collection of objects of type O
   */
  public <O> Collection<O> getValues(KBPredicate pred, int thisArgPos,
          int getArgPos, Object matchArg, int matchArgPos, Context ctx);

  /**
   * finds or creates a new Fact in the underlying KB
   * <p>
   * The method finds a Fact in the KB with predicate <code>pred</code>, and
   * <code>this</code> at the argument position <code>thisArgPos</code>, in the
   * context <code>ctx</code>. The otherArgs specify all the arguments of the
   * Fact, completely. If the fact specified by the input arguments is not
   * found, creates and persists a new fact in the underlying KB.
   *
   * Note: Not all subclasses of the KB objects are directly assertible. For
   * example, Sentence, Variable and Symbol need to be quoted, using Quote
   * method, before they can participate in closed assertions.
   *
   * Note: The context is the first argument in methods that have variable
   * number of arguments. In all other methods, context is the last argument.
   *
   * @param ctx	the context where the fact is found or created
   * @param pred	the predicate of the fact
   * @param thisArgPos	the argument position of this object in the fact
   * @param otherArgs	the arguments in positions other than p (0th argument) and
   * argPos
   *
   * @return the fact found or created
   *
   * @throws KBTypeException
   * @throws CreateException
   */
  public Fact addFact(Context ctx, KBPredicate pred,
          int thisArgPos, Object... otherArgs) throws KBTypeException,
          CreateException;

  /**
   * get all collections that the <code>this</code> object is a quoted instance
   * of. The collections are instance of <code>SubLExpressionType</code>.
   * <p>
   * All subclasses of KBObject can be quoted. Refer to
   * <code>#$NoteAboutQuotingInCycL</code> for a more detailed discussion of
   * quoting.
   *
   * @return the collection of KBCollections that <code>this</code> is quoted
   * instance of
   */
  public Collection<KBCollection> getQuotedIsa();

  /**
   * A <code>quotedIsa</code> assertion relates CycL expression to
   * <code>SubLExpressionType</code>.
   * <p>
   * All subclasses of KBObject can be quoted. Refer to
   * <code>#$NoteAboutQuotingInCycL</code> for a more detailed discussion of
   * quoting.
   *
   * @param col the instance of SubLExpressionType, the collection <code>this</code>
   * is a quoted instance of.
   *
   * @param ctx the context where the fact is asserted
   *
   * @throws CreateException
   * @throws KBTypeException
   */
  public void addQuotedIsa(KBCollection col, Context ctx)
          throws KBTypeException, CreateException;

  /**
   * creates a new <code>Fact</code> stating that <code>this</code>
   * <code>KBIndividual</code> instantiates the <code>#$Collection</code>
   * represented by <code>col</code> in the context represented by
   * <code>ctx</code>. Effectively, this asserts <code>(#$isa this col)</code>.
   * <p>
   *
   * @param colStr the string representing the KBCollection this individual is
   * an instance of
   * @param ctxStr the string representing the context where the fact is to be
   * asserted
   *
   * @return this
   *
   * @throws CreateException
   * @throws KBTypeException
   */
  public KBObject instantiates(String colStr, String ctxStr)
          throws KBTypeException, CreateException;

  /**
   * creates a new <code>Fact</code> stating that this <code>KBIndividual</code>
   * instantiates the <code>col</code> in <code>ctx</code>. Effectively, this
   * asserts <code>(#$isa this col)</code>.
   * <p>
   *
   * @param col	the collection of which this KBIndividual is an instance
   * @param ctx	the context where the fact is to be asserted
   *
   * @return	this
   *
   * @throws CreateException
   * @throws KBTypeException
   */
  public KBObject instantiates(KBCollection col, Context ctx)
          throws KBTypeException, CreateException;

  public KBObject instantiates(KBCollection col)
          throws KBTypeException, CreateException;

  /**
   * This method returns the Sentence <code>(#$isa this col)</code>. The key
   * difference between this and {@link #instantiates(com.cyc.kb.KBCollection) } is
   * that, this method does not make any assertion in the KB. The sentence form of the
   * assertion is generally useful when seeking user feedback before asserting into the
   * KB. Use {@link Sentence#assertIn(com.cyc.kb.Context) } to assert the sentence in
   * a Context.
   * 
   * @param col the collection of which <code>this</code> KBIndividual is an instance
   * 
   * @return the #$isa sentence between this and col
   * 
   * @throws KBTypeException 
   */
  public Sentence instantiatesSentence(KBCollection col) 
          throws KBTypeException, CreateException;
  
  /**
   * Is <code>this</code> an instance of <code>col</code> in any context? This
   * does not require that <code>(#$isa this col)</code> be asserted, merely
   * that it be trivially inferable.
   *
   * @param col the collection which <code>this</code> may or may not be an
   * instance of
   *
   * @return whether <code>this</code> is trivially provable to be an instance
   * of <code>col</code>
   */
  public boolean isInstanceOf(KBCollection col);

  /**
   * Is <code>this</code> an instance of <code>col</code> in any context? This
   * does not require that <code>(#$isa this col)</code> be asserted, merely
   * that it be trivially inferable.
   *
   * @param colStr the string representing the collection which
   * <code>this</code> may or may not be an instance of
   *
   * @return whether <code>this</code> is trivially provable to be an instance
   * of <code>col</code>
   */
  public boolean isInstanceOf(String colStr);

  /**
   * Is <code>this</code> an instance of <code>col</code> in context
   * <code>ctx</code>? This does not require that <code>(#$isa this col)</code>
   * be asserted, merely that it be trivially inferable.
   *
   * @param col the collection which <code>this</code> may or may not be an
   * instance of
   * @param ctx the context where the instance relation holds
   *
   * @return whether <code>this</code> is trivially provable to be an instance
   * of <code>col</code>
   */
  public boolean isInstanceOf(KBCollection col, Context ctx);

  /**
   * Is <code>this</code> an instance of <code>col</code> in context
   * <code>ctx</code>? This does not require that <code>(#$isa this col)</code>
   * be asserted, merely that it be trivially inferable.
   *
   * @param colStr the string representation of the collection which
   * <code>this</code> may or may not be an instance of
   * @param ctxStr the string representation of the context where the instance
   * relation holds
   *
   * @return whether <code>this</code> is trivially provable to be an instance
   * of <code>col</code>
   */
  public boolean isInstanceOf(String colStr, String ctxStr);

  /**
   * Is <code>this</code> a quoted instance of <code>col</code> in any context?
   * Essentially this verifies that <code>(#$quotedIsa this col)</code> is true
   * in some context.
   *
   * Refer to <code>#$NoteAboutQuotingInCycL</code> for a more detailed
   * discussion of quoting.
   *
   * @param col the collection which <code>this</code> may or may not be a
   * quoted instance of
   *
   * @return whether <code>this</code> is provable to be a quoted instance of
   * <code>col</code>
   */
  public boolean isQuotedInstanceOf(KBCollection col);

  /**
   * Is <code>this</code> a quoted instance of <code>col</code> in any context?
   * Essentially this verifies that <code>(#$quotedIsa this col)</code> is true
   * in some context.
   *
   * Refer to <code>#$NoteAboutQuotingInCycL</code> for a more detailed
   * discussion of quoting.
   *
   * @param colStr the string representation of the collection which
   * <code>this</code> may or may not be a quoted instance of
   *
   * @return whether <code>this</code> is provable to be a quoted instance of
   * <code>col</code>
   */
  public boolean isQuotedInstanceOf(String colStr);

  /**
   * Is <code>this</code> a quoted instance of <code>col</code>? Essentially
   * this verifies that <code>(#$quotedIsa this col)</code> is true in the
   * context <code>ctx</code>.
   *
   * @param col the collection which <code>this</code> may or may not be a
   * quoted instance of
   * @param ctx the context
   *
   * @return whether <code>this</code> is provable to be a quoted instance of
   * <code>col</code>
   */
  public boolean isQuotedInstanceOf(KBCollection col, Context ctx);

  /**
   * Is <code>this</code> a quoted instance of <code>col</code>? Essentially
   * this verifies that <code>(#$quotedIsa this col)</code> is true in the
   * context <code>ctx</code>.
   *
   * @param colStr the string representation of the collection which
   * <code>this</code> may or may not be a quoted instance of
   * @param ctxStr the string representation of the context
   *
   * @return whether <code>this</code> is provable to be a quoted instance of
   * <code>col</code>
   */
  public boolean isQuotedInstanceOf(String colStr, String ctxStr);

  /**
   * Is there an assertion in <code>ctx</code> with a sentence using
   * <code>pred</code>, with <code>this</code> in the <code>thisArgPos</code>
   * argument position, and with the arguments in <code>otherArgs</code> as the
   * rest of its arguments? This method will not throw exceptions. Effectively,
   * this is a wrapper around 
   * {@link #getFact(com.cyc.kb.Context, com.cyc.kb.KBPredicate, int, java.lang.Object...) }
   * that returns false if there are any exceptions.
   *
   * Note: The context is the first argument in methods that have variable
   * number of arguments. In all other methods, context is the last argument.
   *
   * @param ctx the context in which the sentence is asserted
   * @param pred the predicate of the candidate assertion
   * @param thisArgPos the argument position of <code>this</code> object in
   * assertion
   * @param otherArgs the arguments other than <code>pred</code> and
   * <code>this</code>
   *
   * @return true if and only if the fact is determined to be true.
   */
  public boolean isAsserted(Context ctx, KBPredicate pred,
          int thisArgPos, Object... otherArgs);

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
   * @param ctx the context of the fact
   * @param pred the Predicate of the returned fact
   * @param thisArgPos the position where <code>this</code> is found in the fact
   * @param otherArgs the arguments in positions other than 0 and thisArgPos.
   * They have to be KBObjects or Java objects such as Date, int, float, String.
   *
   * @return the fact represented by the parameters in context <code>ctx</code>
   *
   * @throws CreateException
   * @throws KBTypeException
   */
  public Fact getFact(Context ctx, KBPredicate pred,
          int thisArgPos, Object... otherArgs) throws KBTypeException,
          CreateException;

  /**
   * construct a sentence, that has <code>pred</code> as its predicate,
   * <code>this</code> in the <code>thisArgPos</code> argument position, and
   * <code>otherArgs</code> as its other arguments. Effectively,
   * <code>this</code> is inserted into position <code>thisArgPos</code> of
   * <code>otherArgs</code> to form the sentence.
   *
   * @param pred the Predicate of the returned fact
   * @param thisArgPos the position where <code>this</code> is found in the fact
   * @param otherArgs the arguments in positions other than 0 and thisArgPos.
   * They have to be KBObjects or Java objects such as Date, int, float, String.
   *
   * @return the sentence constructed by the parameters
   *
   * @throws KBTypeException
   */
  // KB API does not do any introspection.. so if we want to use Query API, we should construct
  // a fully qualified sentence for the given predicate. That can only be possible when all
  // other variables are passed in.
  // 
  public Sentence getSentence(KBPredicate pred, int thisArgPos,
          Object... otherArgs) throws KBTypeException, CreateException;

  /**
   * Adds a new Fact in <code>ctx</code> using <code>binPred</code>, with
   * <code>arg1</code> as the first argument, and <code>this</code> as the
   * second argument.
   *
   * @param binPred the predicate of the fact
   * @param arg1 the arg1 of the new binary predicate fact
   * @param ctx the context where the fact is added
   *
   * @return the newly added Fact
   *
   * @throws CreateException
   * @throws KBTypeException
   */
  // @todo add versions of this that take Strings as well as KBObjects.
  public Fact addArg1(BinaryPredicate binPred, Object arg1,
          Context ctx) throws KBTypeException, CreateException;

  /**
   * Adds a new Fact in <code>ctx</code> using <code>binPred</code>, with
   * <code>this</code> as the first argument, and <code>arg2</code> as the
   * second argument.
   *
   * @param binPred the predicate of the fact
   * @param arg2 the arg2 of the new binary predicate fact
   * @param ctx the context where the fact is added
   *
   * @return the newly added Fact
   *
   * @throws KBTypeException
   * @throws CreateException
   */
  // @todo add versions of this that take Strings as well as KBObjects.
  public Fact addArg2(BinaryPredicate binPred, Object arg2,
          Context ctx) throws KBTypeException, CreateException;

  /**
   * gets all the comments for <code>this</code> visible from the default
   * context {@link KBAPIDefaultContext#forQuery()}
   * <p>
   *
   * @return comment strings
   */
  public Collection<String> getComments();

  /**
   * gets all the comments for <code>this</code> visible from the context
   * <p>
   *
   * @param ctxStr	the context of query
   *
   * @return comment strings
   */
  public Collection<String> getComments(String ctxStr);

  /**
   * gets all the comments for <code>this</code> visible from the context
   * <p>
   *
   * @param ctx	the context of query
   *
   * @return comment strings
   */
  public Collection<String> getComments(Context ctx);

  /**
   * Add a new comment for <code>this</code> in the context specified
   * <p>
   *
   * In the CycKB comments can be added only on <code>#$CycLIndexedTerm</code>s,
   * which include <code>CycLReifiableDenotationalTerm</code> and
   * <code>CycLAssertion</code>. An exception will be thrown if attempted to add
   * a comment on Quoted terms, Sentence, Variable and Symbol. This means that
   * only subclasses of KBTerm and Assertion can have comments.
   *
   * @param ctx	the context where the comment is created. Cannot be null.
   * @param comment	the comment string
   *
   * @return the fact created
   *
   * @throws CreateException
   * @throws KBTypeException
   */
  public Fact addComment(String comment, String ctx)
          throws KBTypeException, CreateException;

  /**
   * Add a new comment for <code>this</code> in the context specified
   * <p>
   *
   * In the CycKB comments can be added only on <code>#$CycLIndexedTerm</code>s,
   * which include <code>CycLReifiableDenotationalTerm</code> and
   * <code>CycLAssertion</code>. An exception will be thrown if attempted to add
   * a comment on Quoted terms, Sentence, Variable and Symbol. This means that
   * only subclasses of KBTerm and Assertion can have comments.
   *
   * @param ctx	the context where the comment is created. Cannot be null.
   * @param comment	the comment string
   *
   * @return the fact created
   *
   * @throws CreateException
   * @throws KBTypeException
   */
  public Fact addComment(String comment, Context ctx)
          throws KBTypeException, CreateException;

  /**
   * gets the object in <code>getPos</code> argument position of this KBObject
   * as an object of type <code>O</code>. This method works for Sentences and
   * Assertions, as well as non-atomic KBTerms. However, because a constant has
   * no "arguments", calling this method on a KBObject representing a Cyc
   * constant will result in a KBApiException.
   *
   * @param <O> the object type
   * @param getPos	the argument position of the object returned
   *
   * @return	the object at <code>getPos</code> as a <code>O</code>
   *
   * @throws CreateException
   * @throws KBTypeException
   *
   * @throws UnsupportedOperationException if getArgument is called on Atomic
   * terms. Do not use this to test for term atomicity, use {@link #isAtomic()}
   * instead.
   * @throws IllegalArgumentException for a class of object types which do not
   * support getArgument. Example include: Variable, Symbol
   */
  public <O> O getArgument(int getPos) throws KBTypeException, CreateException;

  /**
   * Is <code>this</code> a non-decomposable KBObject? This will return true for
   * <code>KBObject</code>s that represent Cyc constants, Cyc variables, or Cyc
   * symbols. It returns false for anything else.
   *
   * @return true if and only if this KBObject is not decomposable into other constituent
   * KBObjects.
   *
   * For atomic assertions, or ground atomic formula, use instanceof Fact
   */
  public boolean isAtomic();

  /**
   * Does <code>this</code> correspond to any kind of Cyc assertion?
   *
   * @return true if and only if this object is an assertion
   */
  public boolean isAssertion();

  /**
   * NOT PART OF THE KB API 1.0
   *
   * @return the variable
   */
  public boolean isVariable();

  /**
   * This method returns a sentence with the type restriction of the KBObject or
   * its subclasses, even the ones extended outside of KB API. For example,
   * restriction for KBPredicate object would be (isa <THIS PRED> #$Predicate).
   *
   * This is most useful for building sentences of KB Object typed-variables,
   * for use in rules and queries.
   *
   * @return the restriction sentence for the class
   *
   */
  public Sentence getRestriction() throws KBApiException;

  // (Quantifier OTHER_OPTIONAL_VARS Sentence)
  /**
   * NOT PART OF THE KB API 1.0
   *
   * @return the quantification
   * @throws KBApiException
   */
  public List<Object> getQuantification() throws KBApiException;

  // In the interest of making immutable objects we will not 
  // implement setQuantification()
  /**
   * NOT PART OF THE KB API 1.0
   */
  public void setQuantification();

  /**
   * Returns the syntactic arity of this object. If it has a relation applied to
   * some arguments (i.e. it's a sentence, an assertion, or a functional term),
   * the arity is the number of arguments. By convention, Cyc constants have a
   * formula arity of 0.
   *
   * @return the arity of this object, <tt>null</tt> if not a Cyc constant,
   * functional term, sentence, or assertion.
   *
   * @see com.cyc.base.cycobject.CycConstant
   * @see com.cyc.base.cycobject.Formula#getArity()
   */
  public Integer formulaArity();

  /**
   * provides direct access to the encapsulated object.
   * <p>
   * This method should generally not be needed for application code, though it
   * may be necessary is some rare occasions.
   *
   * @return the implementation-specific wrapped object
   */
  // TODO: Make sure that CycObject and all its subclasses are immutable. 
  // JIRA: BASEAPI-17
  public CycObject getCore();

  /**
   * provides a version of the <code>toString</code> that is suitable for use
   * when talking directly to a Cyc server via methods in
   * {@link com.cyc.base.CycAccess}.
   * <p>
   * The CycL output of each CycObject is required to interact with the
   * underlying KB, for example, to find or create facts.
   *
   * @return the CycL string representation of the wrapped CycObject
   */
  public String stringApiValue();

  /**
   * delete <code>this</code> term and all the facts using it in the KB.
   * <p>
   * This will irreversible modify the KB.
   *
   * @throws DeleteException if the operation cannot be completed
   */
  public void delete() throws DeleteException;

  /**
   * Returns false if the KB object behind this object has been deleted or
   * otherwise rendered invalid on the Cyc server.
   *
   * @return false if the KB object behind this object has been deleted or
   * otherwise rendered invalid on the Cyc server. Returns true otherwise.
   */
  public boolean isValid();

  /**
   * Change the name of this object to <code>name</code>. Throws
   * {@link InvalidNameException} in cases where the rename cannot be completed,
   * for any reason, one of the reasons could be that the name does not conform
   * to Cyc constant-naming conventions. Among objects that cannot be renamed
   * are non-atomic terms, assertions, and constant that are mentioned in server
   * code. An {@link UnsupportedOperationException} will be thrown if attempted
   * to rename Assertion or Non-atomic terms.
   *
   * @param name the new name
   * @return the renamed KBObject
   *
   * @throws InvalidNameException if Cyc Server errors out for any reason,
   * including invalid name
   * @throws UnsupportedOperationException if the object being renamed is not
   * {@link #isAtomic()}
   */
  // @todo how can this actually throw the exception? And it should throw a more informative Exception.
  public KBObject rename(String name) throws InvalidNameException;

  /**
   * get the CycL representation of <code>this</code> as a String. This
   * representation should be suitable for use with the KBObject factory
   * methods, but may not be suitable for use with lower-level APIs. It should
   * not be expected to contain "#$" constant-prefixes.
   *
   * @return the string representation of the underlying CycObject
   */
  @Override
  public String toString();

  /**
   * get the default natural-language string for this object.
   *
   * @return a natural-language string denoting this object
   *
   * @throws UnsupportedOperationException if unable to build a
   * {@link Paraphraser}
   */
  public String toNLString() throws CycConnectionException;

  /**
   * Returns a stable, globally unique ID for <code>this</code>. In terms of
   * Cyc-specific nomenclature, this Id is the compact, hl external ID for the
   * underlying Cyc object.
   *
   * Generally the HL IDs of indexed terms <code>#$CycLIndexedTerm</code>s are
   * useful. Cyc can generate HL IDs for Sentences, Variables and Symbols.
   *
   * @return the globally unique ID
   */
  public String getId();

  @Override
  public int hashCode();

  @Override
  public boolean equals(Object obj);

  /**
   * Does <code>this</code> represent the same object in the KB as
   * <code>object</code>. This can return true even if the two objects being
   * compared are of different classes, as long as they represent the same
   * object in the KB.
   *
   * Ideally if factory methods are used to create KB API objects, it will
   * ensure that the object returned will be of the tightest subclass possible.
   * So object comparisons will work even when cast to super.
   *
   * @param object the object to compare against <code>this</code>
   *
   * @return true if the underlying CycObject is the same for two different
   * KBObjects
   */
  public boolean equalsSemantically(Object object);

  /**
   * A quoted object refers to the object itself and not its meaning. For
   * example, (#$Quote #$Plato) refers to the term #$Plato, and not to the
   * philosopher.
   *
   * Refer to <code>#$NoteAboutQuotingInCycL</code> for a more detailed
   * discussion of quoting.
   *
   * @return the quoted version of <code>this</code> object
   *
   * @throws CreateException
   * @throws KBTypeException
   */
  public KBIndividual quote() throws KBTypeException, CreateException;

  /**
   * Return the KBCollection, as a KBObject, of the Cyc term that underlies this
   * class. For example, calling this on a <code>KBCollection</code> object will
   * return KBCollectionImpl.get("#$Collection").
   *
   * @return the KBCollection of the underlying Cyc term of the class.
   */
  KBObject getType();
}
