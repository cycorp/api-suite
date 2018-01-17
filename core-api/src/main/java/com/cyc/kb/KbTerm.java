package com.cyc.kb;

import com.cyc.Cyc;
import com.cyc.kb.KbObject.KbObjectWithArity;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.DeleteException;
import com.cyc.kb.exception.InvalidNameException;
import com.cyc.kb.exception.KbTypeException;
import com.cyc.kb.spi.KbTermService;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/*
 * #%L
 * File: KbTerm.java
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
/**
 * The top-level interface for most kinds of {@link KbObject}s that are not
 * {@link Sentence}s.
 *
 * @author vijay
 */
public interface KbTerm extends KbObjectWithArity {
  
  //====|    Factory methods    |=================================================================//
  
  /**
   * Get the <code>KbTerm</code> with the name <code>nameOrId</code>. This static method wraps a
   * call to {@link KbTermService#get(java.lang.String) }; see that method's documentation for more
   * details.
   *
   * @param nameOrId the string representation or the HLID of the term
   *
   * @return a new KbTerm
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  public static KbTerm get(String nameOrId) throws KbTypeException, CreateException {
    return Cyc.getKbTermService().get(nameOrId);
  }
  
  public static KbTerm findOrCreate(String nameOrId) throws CreateException, KbTypeException {
    return Cyc.getKbTermService().findOrCreate(nameOrId);
  }
  
  public static KbTerm findOrCreate(String nameOrId, String constraintColStr)
          throws CreateException, KbTypeException {
    return Cyc.getKbTermService().findOrCreate(nameOrId, constraintColStr);
  }
  
  public static KbTerm findOrCreate(String nameOrId, String constraintColStr, String ctxStr)
          throws CreateException, KbTypeException {
    return Cyc.getKbTermService().findOrCreate(nameOrId, constraintColStr, ctxStr);
  }
  
  public static KbTerm findOrCreate(String nameOrId, KbCollection constraintCol)
          throws CreateException, KbTypeException {
    return Cyc.getKbTermService().findOrCreate(nameOrId, constraintCol);
  }

  public static KbTerm findOrCreate(String nameOrId, KbCollection constraintCol, Context ctx)
          throws CreateException, KbTypeException {
    return Cyc.getKbTermService().findOrCreate(nameOrId, constraintCol, ctx);
  }
  
  public static boolean existsAsType(String nameOrId) {
    return Cyc.getKbTermService().existsAsType(nameOrId);
  }
  
  public static KbStatus getStatus(String nameOrId) {
    return Cyc.getKbTermService().getStatus(nameOrId);
  }

  //====|    Interface methods    |===============================================================//
  
  /**
   * Determine whether <code>this</code> is provably not an instance of
   * <code>col</code>. This means that there is no way to convert
   * <code>this</code> into an instance of <code>col</code> without editing or
   * removing current KB assertions.
   *
   * @param col the collection which <code>this</code> is to be tested against
   * @param ctx the getContextService in which the semantic test should be performed
   *
   * @return whether <code>this</code> is provably not an instance of
   * <code>col</code>
   */
  boolean provablyNotInstanceOf(KbCollection col, Context ctx);

  /* *
   * Determine whether <code>this</code> is provably not an instance of
   * <code>col</code>. This means that there is no way to convert
   * <code>this</code> into an instance of the collection without editing or
   * removing current KB assertions.
   *
   * @param colStr the string representing the collection which
   * <code>this</code> is to be tested against
   * @param ctxStr the getContextService in which the semantic test should be performed
   *
   * @return whether <code>this</code> is provably not an instance
   * <code>col</code>
   * /
  boolean provablyNotInstanceOf(String colStr, String ctxStr);
  */
  
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
  
  /**
   * Change the name of an atomic term to <code>nameString</code>. This method only applies to
   * <em>atomic</em> terms (i.e., CycL constants); attempts to rename non-atomic terms will result
   * in an exception, as will attempts to rename constants that are mentioned in server code.
   *
   * @param nameString the new name
   *
   * @return the renamed KbTerm
   *
   * @throws InvalidNameException          in cases where the rename cannot be completed; e.g. the
   *                                       name does not conform to Cyc constant-naming conventions
   * @throws UnsupportedOperationException if the object being renamed is not {@link #isAtomic()}
   *
   * @see KbObject#isAtomic()
   */
  KbTerm rename(String nameString) throws InvalidNameException;
  
  /**
   * Delete <code>this</code> term and all the facts using it in the KB.
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
   * Gets all the comments for <code>this</code> visible from the default Context 
   * {@link com.cyc.kb.DefaultContext#forQuery()}.
   *
   * @return  comment strings
   */
  Collection<String> getComments();
  
  /**
   * Gets all the comments for <code>this</code> visible from the getContextService
   * <p>
   *
   * @param   ctx  the getContextService of getQueryService
   * @return  comment strings
   */
  Collection<String> getComments(Context ctx);
  
  /* *
   * Gets all the comments for <code>this</code> visible from the getContextService
   * <p>
   *
   * @param   ctxStr  the getContextService of getQueryService
   * @return  comment strings
   * /
  Collection<String> getComments(String ctxStr);
  */
  
  /**
   * Add a new comment for <code>this</code> in the getContextService specified
   * <p>
   *
   * In the CycKB comments can be added only on <code>#$CycLIndexedTerm</code>s,
   * which include <code>CycLReifiableDenotationalTerm</code> and
   * <code>CycLAssertion</code>. An exception will be thrown if attempted to add
   * a comment on Quoted terms, Sentence, Variable and Symbol. This means that
   * only subclasses of KbTerm and Assertion can have comments.
   *
   * @param   ctx      the getContextService where the comment is created. Cannot be null.
   * @param   comment  the comment string
   * @return  the getFactService created
   * @throws  CreateException
   * @throws  KbTypeException
   */
  Fact addComment(String comment, Context ctx) throws KbTypeException, CreateException;
  
  /* *
   * Add a new comment for <code>this</code> in the getContextService specified
   * <p>
   *
   * In the CycKB comments can be added only on <code>#$CycLIndexedTerm</code>s,
   * which include <code>CycLReifiableDenotationalTerm</code> and
   * <code>CycLAssertion</code>. An exception will be thrown if attempted to add
   * a comment on Quoted terms, Sentence, Variable and Symbol. This means that
   * only subclasses of KbTerm and Assertion can have comments.
   *
   * @param   ctx      the getContextService where the comment is created. Cannot be null.
   * @param   comment  the comment string
   * @return  the getFactService created
   * @throws  CreateException
   * @throws  KbTypeException
   * /
  Fact addComment(String comment, String ctx) throws KbTypeException, CreateException;
  */
  
  /**
   * A <code>quotedIsa</code> getAssertionService relates CycL expression to <code>SubLExpressionType</code>.
   * 
   * <p>All subclasses of KbObject can be quoted. Refer to <code>#$NoteAboutQuotingInCycL</code> for
   * a more detailed discussion of quoting.
   *
   * @param   collection  the instance of SubLExpressionType, the collection <code>this</code> is a 
   *                      quoted instance of
   * @param   context     the getContextService where the getFactService is asserted.
   * @return  this object, for method chaining
   * @throws  CreateException
   * @throws  KbTypeException
   */
  KbTerm addQuotedIsa(KbCollection collection, Context context) 
         throws KbTypeException, CreateException;
  
  /**
   * Creates a new <code>Fact</code> stating that this <code>KbIndividual</code> instantiates the
   * <code>collection</code> in <code>getContextService</code>. Effectively, this asserts 
   * <code>(#$isa this collection)</code>.
   *
   * @param   collection  the collection of which this KbIndividual is an instance
   * @param   context     the getContextService where the getFactService is to be asserted
   * @return  this object, for method chaining
   * @throws  CreateException
   * @throws  KbTypeException
   */
  KbTerm instantiates(KbCollection collection, Context context) 
          throws KbTypeException, CreateException;
  
  /* *
   * Creates a new <code>Fact</code> stating that <code>this</code> <code>KbIndividual</code>
   * instantiates the <code>#$Collection</code> represented by <code>collection</code> in the 
 getContextService represented by <code>getContextService</code>. Effectively, this asserts 
   * <code>(#$isa this collection)</code>.
   *
   * @param   collectionStr  string representing the KbCollection this individual is an instance of
   * @param   contextStr     string representing the getContextService where the getFactService is to be asserted
   * @return  this object, for method chaining
   * @throws  CreateException
   * @throws  KbTypeException
   * /
  KbTerm instantiates(String collectionStr, String contextStr) 
          throws KbTypeException, CreateException;
  */
  
  /**
   * Creates a new <code>Fact</code> stating that this <code>KbIndividual</code> instantiates the
   * <code>collection</code> in the default getAssertionService getContextService.
   * 
   * See {@link #instantiates(com.cyc.kb.KbCollection, com.cyc.kb.Context) } for more details.
   * 
   * @param   collection  the collection of which this KbIndividual is an instance
   * @return  this object, for method chaining
   * @throws  KbTypeException
   * @throws  CreateException 
   */
  KbTerm instantiates(KbCollection collection) throws KbTypeException, CreateException;
  
  /**
   * This method returns the Sentence <code>(#$isa this collection)</code>. The key difference 
   * between this and {@link #instantiates(com.cyc.kb.KbCollection) } is that, this method does not 
 make any getAssertionService in the KB. The getSentenceService form of the getAssertionService is generally useful when
 seeking user feedback before asserting into the KB. Use 
 {@link Sentence#assertIn(com.cyc.kb.Context) } to assert the getSentenceService in a Context.
   * 
   * @param   collection  the collection of which <code>this</code> KbIndividual is an instance
   * @return  the #$isa getSentenceService between this and col
   * @throws  KbTypeException 
   */
  Sentence instantiatesSentence(KbCollection collection) throws KbTypeException, CreateException;
  
  /**
   * Is <code>this</code> an instance of <code>collection</code> in any getContextService? This does not 
 require that <code>(#$isa this collection)</code> be asserted, merely that it be trivially 
   * inferable.
   *
   * @param   collection  the collection which <code>this</code> may or may not be an instance of
   * @return  whether <tt>this</tt> is trivially provable to be an instance of <tt>collection</tt>
   */
  boolean isInstanceOf(KbCollection collection);

  /* *
   * Is <code>this</code> an instance of <code>collection</code> in any getContextService? This does
   * not require that <code>(#$isa this collection)</code> be asserted, merely that it be trivially 
   * inferable.
   *
   * @param   collectionStr  the string representing the collection which <code>this</code> may or
   *                         may not be an instance of
   * @return  whether <tt>this</tt> is trivially provable to be an instance of <tt>collection</tt>
   * /
  boolean isInstanceOf(String collectionStr);
  */
  
  /**
   * Is <code>this</code> an instance of <code>collection</code> in <code>getContextService</code>? This does 
   * not require that <code>(#$isa this collection)</code> be asserted, merely that it be trivially 
   * inferable.
   *
   * @param   collection  the collection which <code>this</code> may or may not be an instance of
   * @param   context     the getContextService where the instance getRelation holds.
   * @return  whether <tt>this</tt> is trivially provable to be an instance of <tt>collection</tt>
   */
  boolean isInstanceOf(KbCollection collection, Context context);

  /* *
   * Is <code>this</code> an instance of <code>collection</code> in <code>getContextService</code>? This does
   * not require that <code>(#$isa this collection)</code> be asserted, merely that it be trivially
   * inferable.
   *
   * @param   collectionStr  string representation of the collection which <code>this</code> may or 
   *                         may not be an instance of
   * @param   contextStr     string representation of the getContextService where the instance getRelation holds
   * @return  whether <tt>this</tt> is trivially provable to be an instance of <tt>collection</tt>
   * /
  boolean isInstanceOf(String collectionStr, String contextStr);
  */
  
}
