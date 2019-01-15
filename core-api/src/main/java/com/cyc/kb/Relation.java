package com.cyc.kb;

/*
 * #%L
 * File: Relation.java
 * Project: Core API
 * %%
 * Copyright (C) 2015 - 2019 Cycorp, Inc
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
import com.cyc.Cyc;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbTypeException;
import com.cyc.kb.exception.VariableArityException;
import com.cyc.kb.spi.RelationService;
import java.util.Collection;
import java.util.List;

/**
 * The common interface for {@link KbPredicate} and {@link KbFunction}. All
 * <code>Relations</code> apply to one or more arguments to produce a new
 * {@link KbObject}. Most of the methods in this interface describe restrictions
 * on the number or types of arguments a <code>Relation</code> can apply to.
 *
 * @author baxter
 */
public interface Relation extends KbIndividual {

  //====|    Factory methods    |=================================================================//
  
  /**
   * Get the <code>Relation</code> with the name <code>nameOrId</code>. This static method wraps a
   * call to {@link RelationService#get(java.lang.String) }; see that method's documentation for
   * more details.
   *
   * @param nameOrId the string representation or the HLID of the #$Relation
   *
   * @return a new Relation
   *
   * @throws KbTypeException
   * @throws CreateException
   */
  public static Relation get(String nameOrId) throws KbTypeException, CreateException {
    return Cyc.getRelationService().get(nameOrId);
  }
  
  public static Relation findOrCreate(String nameOrId) throws CreateException, KbTypeException {
    return Cyc.getRelationService().findOrCreate(nameOrId);
  }
  
  public static Relation findOrCreate(String nameOrId, String constraintColStr)
          throws CreateException, KbTypeException {
    return Cyc.getRelationService().findOrCreate(nameOrId, constraintColStr);
  }
  
  public static Relation findOrCreate(String nameOrId, String constraintColStr, String ctxStr)
          throws CreateException, KbTypeException {
    return Cyc.getRelationService().findOrCreate(nameOrId, constraintColStr, ctxStr);
  }
  
  public static Relation findOrCreate(String nameOrId, KbCollection constraintCol)
          throws CreateException, KbTypeException {
    return Cyc.getRelationService().findOrCreate(nameOrId, constraintCol);
  }

  public static Relation findOrCreate(String nameOrId, KbCollection constraintCol, Context ctx)
          throws CreateException, KbTypeException {
    return Cyc.getRelationService().findOrCreate(nameOrId, constraintCol, ctx);
  }
  
  public static boolean existsAsType(String nameOrId) {
    return Cyc.getRelationService().existsAsType(nameOrId);
  }
  
  public static KbStatus getStatus(String nameOrId) {
    return Cyc.getRelationService().getStatus(nameOrId);
  }
  
  //====|    Interface methods    |===============================================================//
  
  /**
   * Returns an ordered list of all argIsa constraints for <code>this</code>
   * Relation. This method iterates over all the argument positions, that is,
   * from 1 to {@link #getArity()} and builds a list of argIsa constraints, 
   * as viewed from the default context {@link com.cyc.kb.DefaultContext#forQuery()} set in
   * {@link com.cyc.session.SessionOptions#getDefaultContext()}.
   * 
   * @see #getArgGenl(int) 
   * 
   * @return a list of argIsa constraints
   */
  public List<Collection<KbCollection>> getArgIsaList();
  
  /**
   * Returns an ordered list of all argIsa constraints for <code>this</code>
   * Relation. This method iterates over all the argument positions, that is,
   * from 1 to {@link #getArity()} and builds a list of argIsa constraints, 
   * as viewed from Context <code>ctx</code>.
   * 
   * @param ctx the context of query
   * 
   * @return a list of argIsa constraints
   */
  public List<Collection<KbCollection>> getArgIsaList(Context ctx);
  
  /**
   * gets the <code>KbCollection</code>s that all arguments in position
   * <code>argPos</code> of the relation most be instances of, as viewed from
   * the default context {@link com.cyc.kb.DefaultContext#forQuery()} set in
   * {@link com.cyc.session.SessionOptions#getDefaultContext()}.
   *
   * The returned collection could be empty.
   * <p>
   *
   * @param argPos the relevant argument position of the relation
   *
   * @return the constraining getArgIsa <code>KbCollection</code>s
   */
  public Collection<KbCollection> getArgIsa(int argPos);

  /* *
   * gets the <code>KbCollection</code>s that all arguments in position
   * <code>argPos</code> of the relation most be instances of, as viewed from
   * the context <code>ctx</code>.
   * <p>
   *
   * @param argPos the relevant argument position of the relation
   * @param ctxStr the string representing the context of the query
   *
   * @return the constraining getArgIsa <code>KbCollection</code>s
   * /
  public Collection<KbCollection> getArgIsa(int argPos, String ctxStr);
  */
  
  /**
   * gets the <code>KbCollection</code>s that all arguments in position
   * <code>argPos</code> of the relation most be instances of, as viewed from
   * the context <code>ctx</code>.
   * <p>
   *
   * @param argPos the relevant argument position of the relation
   * @param ctx the context of the query
   *
   * @return the constraining getArgIsa <code>KbCollection</code>s
   */
  public Collection<KbCollection> getArgIsa(int argPos, Context ctx);

  /* *
   * creates a new Fact stating that any object in the <code>argPos</code>
   * position of <code>this</code> relation should be an instance of
   * the Collection <code>col</code>, in the context <code>ctx</code>.
   * <p>
   * A formula satisfying these rules will be semantically well formed.
   *
   * @param argPos the argument position for the argIsa constraint
   * @param colStr the string representing the collection for the new argIsa
   * constraint
   * @param ctxStr the string representing the context where the constraint is
   * to be stated
   *
   * @return this
   *
   * @throws CreateException
   * @throws KbTypeException
   * /
  public Relation addArgIsa(int argPos, String colStr, String ctxStr)
          throws KbTypeException, CreateException;
  */
  
  /**
   * creates a new Fact stating that any object in the <code>argPos</code>
   * position of <code>this</code> relation should be an instance of
   * the Collection <code>col</code>, in the context <code>ctx</code>.
   * <p>
   * A formula satisfying these rules will be semantically well formed.
   *
   * @param argPos the argument position for the new argIsa constraint
   * @param col the collection for the new argIsa constraint
   * @param ctx the context where the constraint is to be stated
   *
   * @return this
   *
   * @throws CreateException
   * @throws KbTypeException
   */
  public Relation addArgIsa(int argPos, KbCollection col, Context ctx)
          throws KbTypeException, CreateException;

  /**
   * Return a sentence to add argIsa for a relation.
   * @see #addArgIsa(int, com.cyc.kb.KbCollection, com.cyc.kb.Context) for more details.
   * 
   * @param argPos  the argument position for the new argIsa constraint
   * @param col     the collection for the new argIsa constraint
   * 
   * @return a sentence to add argIsa for a relation
   * 
   * @throws KbTypeException 
   */
  Sentence addArgIsaSentence(int argPos, KbCollection col) throws KbTypeException, CreateException;
  
  /**
   * Returns an ordered list of all argGenl constraints for <code>this</code>
   * Relation. This method iterates over all the argument positions, that is,
   * from 1 to {@link #getArity()} and builds a list of argGenl constraints as 
   * viewed from {@link com.cyc.kb.DefaultContext#forQuery()} set in
   * {@link com.cyc.session.SessionOptions#getDefaultContext()}.
   * 
   * @see #getArgGenl(int) 
   * 
   * @return a list of argGenl constraints
   */
  public List<Collection<KbCollection>> getArgGenlList();
          
  /**
   * Returns an ordered list of all argGenl constraints for <code>this</code>
   * Relation. This method iterates over all the argument positions, that is,
   * from 1 to {@link #getArity()} and builds a list of argGenl constraints, 
   * as viewed from Context <code>ctx</code>.
   * 
   * @param ctx the context of query
   * 
   * @return a list of argGenl constraints
   */
  public List<Collection<KbCollection>> getArgGenlList(Context ctx);
  
  /**
   * gets the <code>KbCollection</code>s that all arguments in position
   * <code>argPos</code> of the relation must be specializations of, as viewed
   * from the default context specified by {@link com.cyc.kb.DefaultContext#forQuery()} set in
   * {@link com.cyc.session.SessionOptions#getDefaultContext()}.
   * <p>
   *
   * @param argPos the argument position of the relation
   *
   * @return the relevant argGenl constraints on <code>this</code> at position
   * <code>argPos</code>
   */
  public Collection<KbCollection> getArgGenl(int argPos);

  /* *
   * gets the <code>KbCollection</code>s that all arguments in position
   * <code>argPos</code> of the relation must be specializations of, as viewed
   * from the context <code>ctx</code>.
   * <p>
   *
   * @param argPos the argument position of the relation
   * @param ctxStr the string representing the context of query
   *
   * @return the relevant argGenl constraints on <code>this</code> at position
   * <code>argPos</code>
   * /
  public Collection<KbCollection> getArgGenl(int argPos, String ctxStr);
  */
  
  /**
   * gets the <code>KbCollection</code>s that all arguments in position
   * <code>argPos</code> of the relation must be specializations of, as viewed
   * from the context <code>ctx</code>
   * <p>
   *
   * @param argPos the argument position of the relation
   * @param ctx the context of query
   *
   * @return the relevant argGenl constraints on <code>this</code> at position
   * <code>argPos</code>
   */
  public Collection<KbCollection> getArgGenl(int argPos, Context ctx);

  /* *
   * creates a new Fact stating that <code>col</code> must be a generalization
   * of any object in the <code>argPos</code> position of <code>this</code> relation, in the context
   * <code>ctx</code>.
   * <p>
   * A formula satisfying these rules will be semantically well formed.
   *
   * @param argPos the position where instance of c fits
   * @param colStr the string representing the collection which is a
   * generalization of objects that fit in argPos
   * @param ctxStr the string representing the context where the fact is
   * asserted
   *
   * @return this
   *
   * @throws CreateException
   * @throws KbTypeException
   * /
  public Relation addArgGenl(int argPos, String colStr, String ctxStr)
          throws KbTypeException, CreateException;
  */
  
  /**
   * creates a new Fact stating that <code>col</code> must be a generalization
   * of any object in the <code>argPos</code> position of <code>this</code> relation, in the 
   * context.
   * <p>
   * A formula satisfying these rules will be semantically well formed.
   *
   * @param argPos the relevant argument position
   * @param col the collection which is a generalization of objects that fit in
   * argPos
   * @param ctx the context where the fact is asserted
   *
   * @return this
   *
   * @throws CreateException
   * @throws KbTypeException
   */
  public Relation addArgGenl(int argPos, KbCollection col, Context ctx)
          throws KbTypeException, CreateException;
  
  /**
   * Returns a list of argument positions that should have explicitly different
   * concepts for the truth/denotation condition to be valid.
   * 
   * Some relations have interArgDifferent assertions defined on them. Return
   * a single, arbitrary, interArgDifferent condition for the relation. If none
   * is found, return null. Refer to comments on #$interArgDifferent in the KB
   * for a more detailed discussion.
   * 
   * Example: 
   * <code>
   * RelationFactory.get("largerThan").getInterArgDifferent(Constants.uvMt()); // == [1, 2]
   * </code>
   * 
   * @param ctx the context of query for interArgDifferent assertion
   * 
   * @return the list of arguments that need to be different
   */
  public List<Integer> getInterArgDifferent(Context ctx);
  
  /**
   * Add a new interArgDifferent condition of <code>this</code> relation.
   * 
   * The list of two argument positions that should have explicitly different
   * concepts for the truth/denotation condition to be valid. Refer to comments 
   * on #$interArgDifferent in the KB for a more detailed discussion.
   * 
   * @param argPosM one of the argument positions that needs to be different from the second
   * @param argPosN one of the argument positions that needs to be different from the first
   * @param ctx the context of assertion
   * 
   * @return this object for method chaining
   * 
   * @throws KbTypeException
   * @throws CreateException 
   */
  public Relation addInterArgDifferent(Integer argPosM, Integer argPosN, Context ctx)
          throws KbTypeException, CreateException;
  
  /**
   * Return the number of arguments required for <code>this</code> relation. See
   * {@link #getArityMin()} and {@link #getArityMax()} for arity information of
   * variable arity relations.
   *
   * @return the number of arguments required for <code>this</code> relation
   *
   * @throws VariableArityException for relations that have more than one
   * possible arity value (i.e. for instances of #$VariableArityRelation).
   */
  public Integer getArity() throws VariableArityException;

  /**
   * Returns true if <code>this</code> is a variable-arity relation. 
   *
   * @return whether or not <code>this</code> is a variable-arity relation. 
   *
   */
  public boolean isVariableArity();

  /**
   * find the minimum number of arguments required for the relation
   * <code>this</code>. Note that except for the case of variable-arity
   * relations, this method will return the same number as both
   * {@link #getArity()} and {@link #getArityMax()}.
   *
   * @return the minimum number of arguments allowed for <code>this</code>
   * relation
   */
  public Integer getArityMin();

  /**
   * find the maximum number of arguments required for the relation
   * <code>this</code>. Note that except for the case of variable-arity
   * relations, this method will return the same number as both
   * {@link #getArity()} and {@link #getArityMin()}.
   *
   * @return the maximum number of arguments allowed for <code>this</code>
   * relation
   */
  public Integer getArityMax();

  /**
   * Set the arity for a Cyc relation. If the relation has an arity value
   * already that is different from <code>arityValue</code>, an exception will
   * be thrown.
   *
   * @param arityValue the number of arguments <code>this</code> relation takes
   *
   * @return this
   *
   * @throws CreateException
   * @throws KbTypeException
   */
  public Relation setArity(int arityValue) throws KbTypeException,
          CreateException;
  
  /**
   * Return a sentence to set the arity of a Cyc relation.
   * @see #setArity(int) for more details.
   * 
   * @param arityValue the number of arguments <code>this</code> relation takes
   * 
   * @return a Sentence to set the arity.
   * 
   * @throws KbTypeException
   * @throws CreateException
   */
  public Sentence setAritySentence(int arityValue) throws KbTypeException, CreateException;

}
