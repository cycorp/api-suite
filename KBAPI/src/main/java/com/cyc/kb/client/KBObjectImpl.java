package com.cyc.kb.client;

/*
 * #%L
 * File: KBObjectImpl.java
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
import com.cyc.base.CycAccess;
import com.cyc.base.CycAccessManager;
import com.cyc.base.CycApiException;
import com.cyc.base.CycConnectionException;
import com.cyc.base.cycobject.CycAssertion;
import com.cyc.base.cycobject.CycConstant;
import com.cyc.base.cycobject.CycList;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.CycSymbol;
import com.cyc.base.cycobject.CycVariable;
import com.cyc.base.cycobject.DenotationalTerm;
import com.cyc.base.cycobject.FormulaSentence;
import com.cyc.base.cycobject.Fort;
import com.cyc.base.cycobject.Guid;
import com.cyc.base.cycobject.Nart;
import com.cyc.base.cycobject.Naut;
import com.cyc.baseclient.CycObjectFactory;
import com.cyc.baseclient.cycobject.CycArrayList;
import com.cyc.baseclient.cycobject.CycConstantImpl;
import com.cyc.baseclient.cycobject.DefaultCycObject;
import com.cyc.baseclient.cycobject.NautImpl;
import com.cyc.baseclient.datatype.DateConverter;
import com.cyc.baseclient.nl.Paraphraser;
import com.cyc.kb.Assertion;
import com.cyc.kb.BinaryPredicate;
import com.cyc.kb.Context;
import com.cyc.kb.Fact;
import com.cyc.kb.KBCollection;
import com.cyc.kb.KBFunction;
import com.cyc.kb.KBIndividual;
import com.cyc.kb.KBObject;
import com.cyc.kb.KBPredicate;
import com.cyc.kb.KBTerm;
import com.cyc.kb.Sentence;
import com.cyc.kb.config.KBAPIConfiguration;
import com.cyc.kb.config.KBAPIDefaultContext;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.DeleteException;
import com.cyc.kb.exception.InvalidNameException;
import com.cyc.kb.exception.KBApiException;
import com.cyc.kb.exception.KBApiRuntimeException;
import com.cyc.kb.exception.KBApiServerSideException;
import com.cyc.kb.exception.KBTypeException;
import com.cyc.kb.exception.StaleKBObjectException;
import com.cyc.kb.quant.QuantifiedInstanceRestrictedVariable;
import com.cyc.session.CycSession;
import com.cyc.session.CycSessionManager;
import com.cyc.session.SessionApiException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The base class for all the other classes in this package. Each KBObject is
 * basically a facade for an object in the Cyc KB, and as such it provides
 * common methods to make, retrieve, and remove CycL Assertions.
 * <p>
 *
 * @author Vijay Raj
 * @version "$Id: KBObjectImpl.java 155043 2014-11-18 18:40:24Z baxter $"
 */
public class KBObjectImpl implements KBObject {

  /**
   * The CORE object wrapped by all KBObjects. The type of object wrapped by
   * each subclass of KBObject will be a subclass of CycObject class.
   *
   * INTERNAL DEVELOPERS: There is a strong assumption in the KB API that the
   * CycObjects are immutable. No defensive copy is made when KB API objects are
   * constructed or when returned by getCore()!!
   */
  CycObject core = null;

  /**
   * !!!EXPERIMENTAL!! Can change any time without notice.
   */
  List<Object> quantification = new ArrayList<Object>();
  private static final Logger log = LoggerFactory.getLogger(KBObjectImpl.class.getCanonicalName());
  private final CycSession session;
  private final CycAccess access;
  
  /**
   * Set the flag to false after API provide delete operation.
   */
  //should change to true when the KBObject is deleted from the KB, or otherwise invalidated.
  private boolean isValid = true;

  /**
   * Not part of the KB API. This default constructor only has the effect of
   * ensuring that there is access to a Cyc server.
   * <p>
   *
   * @throws KBApiRuntimeException if there is a problem connecting to Cyc
   */
  KBObjectImpl() {
    try {
      this.session = CycSessionManager.getCurrentSession();
      this.access = CycAccessManager.getAccessManager().fromSession(session);
    } catch (Exception ex) {
      throw new KBApiRuntimeException("Encountered a problem with the current CycSession.", ex);
    }
  }

  /**
   * Not part of the KB API. Base class constructor currently used only for unit
   * testing.
   * <p>
   *
   * @param co The <code>CycObject</code> being wrapped.
   *
   * @throws KBApiRuntimeException if there is a problem connecting to Cyc.
   */
  // We will not document the run time exceptions all the way up the stack.
  @Deprecated
  KBObjectImpl(CycObject co) {
    this();
    core = co;
  }
  
  protected CycSession getSession() {
    return this.session;
  }
  
  protected CycAccess getAccess() {
    return this.access;
  }
  
  static protected CycAccess getStaticAccess() {
    try {
      return CycAccessManager.getCurrentAccess();
    } catch (SessionApiException ex) {
      throw new KBApiRuntimeException(ex.getMessage(), ex);
    }
  }

  /**
   * Get the <code>KBObject</code> that corresponds to <code>cycObject</code>.
   * Throws exceptions if the object isn't in the KB.
   *
   * This is a very general factory method to build a KBObject from any
   * arbitrary CycObject without any kind of semantic check on the CycObject.
   * This is used as a "catch all" in the API, but otherwise it should never be
   * used.
   *
   * @param cycObject the underlying CycObject to be wrapped
   *
   * @return the KBObject wrapping the cycObject.
   *
   * @throws CreateException
   * @todo update documentation to state what this really does
   */
  @SuppressWarnings("deprecation")
  public static KBObject get(CycObject cycObject) throws CreateException, KBTypeException {
    try {
      return KBObjectFactory.get(cycObject, KBObjectImpl.class);
    } catch (KBTypeException te) {
      // This type is not possible, since we are not checking for a specific Cyc collection
      // Fix API if this ever happens
      throw new KBApiRuntimeException(te.getMessage(), te);
    }
  }

  public static KBObject get(String nameOrId) throws CreateException, KBTypeException {
    try {
      return KBObjectFactory.get(nameOrId, KBObjectImpl.class);
    } catch (KBTypeException te){
      // This type is not possible, since we are not checking for a specific Cyc collection
      // Fix API if this ever happens
      throw new KBApiRuntimeException(te.getMessage(), te);
    }
  }

  /**
   * For a given class <code>c</code> that extends <code>KBObject</code>, return
   * the <code>KBCollection</code> that the class corresponds to.
   *
   * For Example, getBaseCycType(Context.class) will return
   * KBCollection.get("Context");
   *
   * @param c the subclass of KBObject whose underlying #$Collection is desired
   *
   * @return the KBCollection representation of underlying #$Collection backing
   * the class
   */
  static KBCollection getBaseCycType(Class<? extends KBObjectImpl> c) {
    Method getTypeString = null;
    try {
      getTypeString = c.getDeclaredMethod("getClassTypeString");
    } catch (NoSuchMethodException e) {
      throw new IllegalArgumentException(c
              + " does not have a known base Cyc type.");
    }
    try {
      return KBCollectionImpl.get((String) getTypeString.invoke((Object[]) null, (Object[]) null));
    } catch (KBApiException te) {
      // We expect the getTypeString return string to be in the KB since it is
      // a fundamental concept. CreateException and KBTypeException are possible
      // but can't recover from such an exception anyways.
      throw new KBApiRuntimeException(te.getMessage(), te);
    } catch (Exception e) {
      throw new KBApiRuntimeException(e.getMessage(), e);
    }
  }

  /**
   * For a given class <code>c</code> that extends <code>KBObject</code>, return
   * the <code>CycObject</code>, a CycDenotationalTerm that the class
   * corresponds to.
   *
   * For Example, getBaseCycTypeCore(Context.class) will return new
   * CycConstantImpl("Individual", new
   * Guid("bd58da02-9c29-11b1-9dad-c379636f7270"))
   *
   * @param c the subclass of KBObject whose underlying #$Collection is desired
   *
   * @return the CycObject representation of underlying #$Collection backing the
   * class
   */
  static CycObject getBaseCycTypeCore(Class<? extends KBObjectImpl> c) {
    try {
      final Method getType = c.getDeclaredMethod("getClassTypeCore");
      return (CycObject) getType.invoke((Object[]) null, (Object[]) null);
    } catch (Exception e) {
      // We expect NoSuchMethodException and IllegalArgumentException both of which are 
      // internal API errors. So just throw RuntimeException
      throw new KBApiRuntimeException(e.getMessage(), e);
    }
  }

  /**
   * @see KBObject#getFacts(KBPredicate, int, Context)
   */
  @Override
  public Collection<Fact> getFacts(KBPredicate p, int thisArgPos, Context ctx) {
    return getFacts(p, this, thisArgPos, ctx);
  }

  /**
   * Gets the asserted facts visible from <code>ctx</code>, using the predicate
   * <code>pred</code>, with <code>matchArg</code> at the position
   * <code>matchArgPos</code> of the fact. Ignores <code>this</code> object.
   * <p>
   *
   * @param pred	the Predicate of the returned fact
   * @param matchArg	the Object in the matchArgPos
   * @param matchArgPos
   * @param ctx the Context. If null, returns facts from the default context
   * {@link KBAPIDefaultContext#forQuery()}
   *
   * @return a collection of facts, empty if none are found
   */
  @SuppressWarnings("deprecation")
  protected Collection<Fact> getFacts(KBPredicate pred, KBObject matchArg, int matchArgPos, Context ctx) {
    try {
      final String ctxStr = (ctx == null) ? KBAPIConfiguration.getDefaultContext().forQuery().stringApiValue() //"#$BaseKB"
              : ctx.stringApiValue();
      String command = "(with-inference-mt-relevance " + ctxStr
              + " (GATHER-GAF-ARG-INDEX "
              + matchArg.stringApiValue() + " " + matchArgPos + " "
              + pred.stringApiValue() + "))";

      log.trace("getfacts: {}", command);
      Object res = getAccess().converse().converseObject(command);
      log.trace("getfacts response: {}", res);
      Set<Fact> facts = new HashSet<Fact>();
      if (!CycObjectFactory.nil.equals(res)) {
        CycList<CycAssertion> assertList = (CycList<CycAssertion>) res;
        for (Object o : assertList) {
          if (o instanceof CycAssertion) {
            try {
              facts.add(FactImpl.get((CycAssertion) o));
            } catch (KBApiException kbe) {
              // Nothing to do. We did get the facts we are building
              // but something went wrong. Just don't add it to the list.
            }
          }
        }
      }
      return facts;
    } catch (CycConnectionException ex) {
      throw new KBApiRuntimeException(ex);
    } catch (CycApiException ex) {
      throw new KBApiRuntimeException(ex.getMessage(), ex);
    }
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#getValues(java.lang.String, int, int, java.lang.String)
   */
  @Override
  public <O> Collection<O> getValues(String predStr, int thisArgPos, int getArgPos, String ctxStr) {
    KBPredicateImpl pred;
    ContextImpl ctx = null;
    try {
      pred = KBPredicateImpl.get(predStr);
      if (!(ctxStr == null || ctxStr.equals(""))) {
        ctx = ContextImpl.get(ctxStr);
      }
    } catch (KBApiException kae) {
      throw new IllegalArgumentException(kae.getMessage(), kae);
    }
    return this.<O>getValues(pred, this, thisArgPos, getArgPos, ctx);
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#getValues(com.cyc.kb.KBPredicateImpl, int, int, com.cyc.kb.ContextImpl)
   */
  @Override
  public <O> Collection<O> getValues(KBPredicate pred, int thisArgPos, int getArgPos, Context ctx) {
    return this.<O>getValues(pred, this, thisArgPos, getArgPos, ctx);
  }

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
   * @param O the type of the objects returned
   * @param ctx the context where the facts are found. If null, returns facts
   * from the default context {@link KBAPIDefaultContext#forQuery()}
   *
   * @return a collection of objects of type O
   *
   * @see KBObject#getFacts(KBPredicate, int, Context) for a method that returns
   * the facts, rather than just the objects at a specific argument position of
   * the facts.
   */
  private <O> Collection<O> getValues(KBPredicate pred, KBObject matchArg, int matchArgPos, int getArgPos, Context ctx) {
    Set<O> myvalues = new HashSet<O>();
    java.util.Collection<Fact> facts = getFacts(pred, matchArg, matchArgPos, ctx);

    for (Fact a : facts) {
      CycAssertion ca = (CycAssertion) a.getCore();
      CycList<Object> g = ca.getGaf().getArgs();
      Object o = g.get(getArgPos);
      try {
        myvalues.add(KBObjectImpl.<O>checkAndCastObject(o));
      } catch (KBApiException kbe) {
        // Don't do anything. 
      }

      // TODO: Need to unify casting and typing of KBObject. And individual
      // types.
      // TODO: Need to decide what exception to throw if an KBObject can't
      // be typed into a subclass here.
      // TODO: Need to decide if instanceof check should be present
    }
    log.debug("Results from getValues: {}", myvalues);
    return myvalues;
  }

  /**
   * @see com.cyc.kb.KBObject#getValues(com.cyc.kb.KBPredicate, int, int,
   * java.lang.Object, int, com.cyc.kb.Context)
   */
  @Override
  public <O> Collection<O> getValues(KBPredicate pred, int thisArgPos, int getArgPos, Object matchArg, int matchArgPos, Context ctx) {
    return this.<O>getValues(pred, this, thisArgPos, getArgPos, matchArg,
            matchArgPos, ctx);
  }

  /**
   * gets the objects in <code>getPos</code> of relevant facts as <code>O</code>
   * objects, with the condition that the fact has the predicate
   * <code>pred</code>, the object at match1ArgPos is <code>match1Arg</code> and
   * the object at the <code>match2ArgPos</code> arg position is
   * <code>match2Arg</code>.
   * <p>
   *
   * @param pred	the predicate of the facts
   * @param match1Arg	the object in argument position match1ArgPos
   * @param match1ArgPos	the argument position of match1Arg in the candidate
   * facts
   * @param getPos	the argument position of the returned objects in the
   * candidate facts
   * @param match2Arg	the object in the argument position match2ArgPos
   * @param match2ArgPos	the argument position that must contain the match2Arg
   * @param O	the type of the objects returned
   * @param ctx	the context. If null, returns facts from the default context
   * {@link KBAPIDefaultContext#forQuery()}
   *
   * @return a collection of objects of type O
   * @throws KBApiException
   *
   * @see #getFacts(com.cyc.kb.KBPredicate, int, com.cyc.kb.Context)
   */
  private <O> Collection<O> getValues(KBPredicate pred, KBObject match1Arg, int match1ArgPos, int getPos, Object match2Arg, int match2ArgPos, Context ctx) {
    Set<O> myvalues = new HashSet<O>();
    Collection<Fact> facts = getFacts(pred, match1Arg, match1ArgPos, ctx);

    Object cycAccessFilter;
    if (match2Arg instanceof KBObjectImpl) {
      cycAccessFilter = ((KBObject) match2Arg).getCore();
    } else {
      cycAccessFilter = match2Arg;
    }

    for (Fact a : facts) {
      CycAssertion ca = (CycAssertion) a.getCore();
      CycList<Object> g = ca.getGaf().getArgs();
      Object o = g.get(getPos);
      if (g.get(match2ArgPos).equals(cycAccessFilter)) {
        try {
          myvalues.add(KBObjectImpl.<O>checkAndCastObject(o));
        } catch (KBApiException kbe) {
          // Don't do anything. 
        }
      }

      // TODO: Need to unify casting and typing of KBObject. And individual
      // types.
      // TODO: Need to decide what exception to throw if an KBObject can't
      // be typed into a subclass here.
      // TODO: Need to decide if instanceof check should be present
    }
    return myvalues;
  }

  /**
   * @see com.cyc.kb.KBObject#addFact(com.cyc.kb.Context,
   * com.cyc.kb.KBPredicate, int, java.lang.Object...)
   */
  @Override
  public Fact addFact(Context ctx, KBPredicate pred, int thisArgPos,
          Object... otherArgs) throws KBTypeException, CreateException {
    List<Object> argList = new ArrayList<Object>(Arrays.asList(otherArgs));
    argList.add(thisArgPos - 1, (Object) this);

    SentenceImpl s = new SentenceImpl(pred, argList.toArray());
    return FactImpl.findOrCreate(s, ctx);
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#getQuotedIsa()
   */
  @Override
  public Collection<KBCollection> getQuotedIsa() {
    return this.<KBCollection>getValues(Constants.quotedIsa(), 1, 2, Constants.inferencePSCMt());
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#addQuotedIsa(com.cyc.kb.KBCollectionImpl, com.cyc.kb.ContextImpl)
   */
  @Override
  public void addQuotedIsa(KBCollection c, Context ctx) throws KBTypeException, CreateException {
    this.addArg2(Constants.quotedIsa(), c, ctx);
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#instantiates(java.lang.String, java.lang.String)
   */
  @Override
  public KBObject instantiates(String colStr, String ctxStr) throws KBTypeException, CreateException {
    return instantiates(KBCollectionImpl.get(colStr), ContextImpl.get(ctxStr));
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#instantiates(com.cyc.kb.KBCollectionImpl, com.cyc.kb.ContextImpl)
   */
  @Override
  public KBObject instantiates(KBCollection col, Context ctx) throws KBTypeException, CreateException {
    addFact(ctx, Constants.isa(), 1, (Object) col);
    return this;
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#instantiates(com.cyc.kb.KBCollectionImpl)
   */
  @Override
  public KBObject instantiates(KBCollection col) throws KBTypeException, CreateException {
    addFact(KBAPIConfiguration.getDefaultContext().forAssertion(), Constants.isa(), 1, (Object) col);
    return this;
  }

  @Override
  public Sentence instantiatesSentence(KBCollection col) throws KBTypeException, CreateException {
    return new SentenceImpl(Constants.isa(), this, (Object) col);
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#isInstanceOf(com.cyc.kb.KBCollectionImpl)
   */
  @Override
  public boolean isInstanceOf(KBCollection col) {
    try {
      return getAccess().getInspectorTool().isa(this.getCore(), (Fort) col.getCore());
    } catch (CycConnectionException e) {
      throw new KBApiRuntimeException(e.getMessage(), e);
    }
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#isInstanceOf(java.lang.String)
   */
  @Override
  public boolean isInstanceOf(String colStr) {
    return isInstanceOf(KBUtils.getKBObjectForArgument(colStr, KBCollectionImpl.class));
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#isInstanceOf(com.cyc.kb.KBCollectionImpl, com.cyc.kb.ContextImpl)
   */
  @Override
  public boolean isInstanceOf(KBCollection col, Context ctx) {
    try {
      return getAccess().getInspectorTool().isa(this.getCore(), col.getCore(), ctx.getCore());
    } catch (CycConnectionException e) {
      throw new KBApiRuntimeException(e.getMessage(), e);
    }
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#isInstanceOf(java.lang.String, java.lang.String)
   */
  @Override
  public boolean isInstanceOf(String colStr, String ctxStr) {
    return isInstanceOf(KBUtils.getKBObjectForArgument(colStr, KBCollectionImpl.class), KBUtils.getKBObjectForArgument(ctxStr, ContextImpl.class));
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#isQuotedInstanceOf(com.cyc.kb.KBCollectionImpl)
   */
  @Override
  public boolean isQuotedInstanceOf(KBCollection col) {
    try {
      return getAccess().getInspectorTool().isQuotedIsa(this.getCore(), (Fort) col.getCore());
    } catch (CycConnectionException ioe) {
      throw new KBApiRuntimeException(ioe.getMessage(), ioe);
    }
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#isQuotedInstanceOf(java.lang.String)
   */
  @Override
  public boolean isQuotedInstanceOf(String colStr) {
    return isQuotedInstanceOf(KBUtils.getKBObjectForArgument(colStr, KBCollectionImpl.class));
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#isQuotedInstanceOf(com.cyc.kb.KBCollectionImpl, com.cyc.kb.ContextImpl)
   */
  @Override
  public boolean isQuotedInstanceOf(KBCollection col, Context ctx) {
    try {
      return getAccess().getInspectorTool().isQuotedIsa(this.getCore(), col.getCore(), ctx.getCore());
    } catch (CycConnectionException ioe) {
      throw new KBApiRuntimeException(ioe.getMessage(), ioe);
    }
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#isQuotedInstanceOf(java.lang.String, java.lang.String)
   */
  @Override
  public boolean isQuotedInstanceOf(String colStr, String ctxStr) {
    return isQuotedInstanceOf(KBUtils.getKBObjectForArgument(colStr, KBCollectionImpl.class), KBUtils.getKBObjectForArgument(ctxStr, ContextImpl.class));
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#isAsserted(com.cyc.kb.ContextImpl, com.cyc.kb.KBPredicateImpl, int, java.lang.Object)
   */
  @Override
  public boolean isAsserted(Context ctx, KBPredicate pred, int thisArgPos,
          Object... otherArgs) {
    try {
      getFact(ctx, pred, thisArgPos, otherArgs);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#getFact(com.cyc.kb.ContextImpl, com.cyc.kb.KBPredicateImpl, int, java.lang.Object)
   */
  @Override
  public Fact getFact(Context ctx, KBPredicate pred, int thisArgPos,
          Object... otherArgs) throws KBTypeException, CreateException {
    List<Object> argList = new ArrayList<Object>(Arrays.asList(otherArgs));
    argList.add(thisArgPos, (Object) this);

    SentenceImpl s = new SentenceImpl(pred, argList.toArray());
    return FactImpl.findOrCreate(s, ctx);
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#getSentence(com.cyc.kb.KBPredicateImpl, int, java.lang.Object)
   */
  // KB API does not do any introspection.. so if we want to use Query API, we should construct
  // a fully qualified sentence for the given predicate. That can only be possible when all
  // other variables are passed in.
  // 
  @Override
  public Sentence getSentence(KBPredicate pred, int thisArgPos, Object... otherArgs) throws KBTypeException, CreateException {

    List<Object> argList = new ArrayList<Object>(Arrays.asList(otherArgs));
    argList.add(0, pred);
    argList.add(thisArgPos, (Object) this);

    return new SentenceImpl(argList.toArray());
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#addArg1(com.cyc.kb.BinaryPredicateImpl, java.lang.Object, com.cyc.kb.ContextImpl)
   */
  // @todo add versions of this that take Strings as well as KBObjects.
  @Override
  public Fact addArg1(BinaryPredicate binPred, Object arg1, Context ctx) throws KBTypeException, CreateException {
    List<Object> argList = new ArrayList<Object>();
    argList.add(arg1);
    argList.add(this);
    SentenceImpl s = new SentenceImpl(binPred, argList.toArray());
    return FactImpl.findOrCreate(s, ctx);
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#addArg2(com.cyc.kb.BinaryPredicateImpl, java.lang.Object, com.cyc.kb.ContextImpl)
   */
  // @todo add versions of this that take Strings as well as KBObjects.
  @Override
  public Fact addArg2(BinaryPredicate binPred, Object arg2, Context ctx) throws KBTypeException, CreateException {
    List<Object> argList = new ArrayList<Object>();
    argList.add(this);
    argList.add(arg2);

    if (this instanceof QuantifiedInstanceRestrictedVariable
            || arg2 instanceof QuantifiedInstanceRestrictedVariable) {
      List<Object> argsWithPredicate = new ArrayList<Object>(argList);
      argsWithPredicate.add(0, binPred);
      try {
        return new TypeFactImpl(ctx, argsWithPredicate.toArray());
      } catch (KBApiException kbe) {
        throw new CreateException(kbe.getMessage(), kbe);
      }
    } else {
      SentenceImpl s = new SentenceImpl(binPred, argList.toArray());
      return FactImpl.findOrCreate(s, ctx);
    }
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#getComments()
   */
  @Override
  public Collection<String> getComments() {
    return getComments(KBAPIConfiguration.getDefaultContext().forQuery());
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#getComments(java.lang.String)
   */
  @Override
  public Collection<String> getComments(String ctxStr) {
    return getComments(KBUtils.getKBObjectForArgument(ctxStr, ContextImpl.class));
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#getComments(com.cyc.kb.ContextImpl)
   */
  @Override
  public Collection<String> getComments(Context ctx) {
    return this.<String>getValues(Constants.getInstance().COMMENT_PRED, 1, 2, ctx);
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#addComment(java.lang.String, java.lang.String)
   */
  @Override
  public Fact addComment(String comment, String ctx) throws KBTypeException, CreateException {
    return addComment(comment, ContextImpl.get(ctx));
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#addComment(java.lang.String, com.cyc.kb.ContextImpl)
   */
  @Override
  public Fact addComment(String comment, Context ctx) throws KBTypeException, CreateException {
    List<Object> argList = new ArrayList<Object>();
    argList.add((Object) this);
    argList.add((Object) comment);

    SentenceImpl s = new SentenceImpl(Constants.getInstance().COMMENT_PRED, argList.toArray());
    return FactImpl.findOrCreate(s, ctx);
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#getArgument(int)
   */
  @Override
  public <O> O getArgument(int getPos) throws KBTypeException, CreateException {

    Object o;
    if (this.getCore() instanceof Nart) {
      Nart cn = (Nart) this.getCore();
      o = cn.getArgument(getPos);// .getReferencedConstants().get(getPos);
    } else if (this.getCore() instanceof Naut) {
      Naut cn = (Naut) this.getCore();
      o = cn.getArgument(getPos);// .getReferencedConstants().get(getPos);
    } else if (this instanceof Assertion) {
      try {
        o = ((CycAssertion) this.getCore()).getArg(getPos, getAccess());
      } catch (CycApiException ex) {
        throw new KBApiServerSideException(ex.getMessage(), ex);
      } catch (CycConnectionException ex) {
        throw new KBApiRuntimeException(ex.getMessage(), ex);
      }
    } else if (this.getCore() instanceof CycList) {
      CycList<CycObject> cl = (CycList<CycObject>) this.getCore();
      o = cl.get(getPos);
    } else if (this.getCore() instanceof FormulaSentence) {
      FormulaSentence cfs = (FormulaSentence) this.getCore();
      o = cfs.getArg(getPos);
    } else if (this.getCore() instanceof CycConstant) {
      throw new UnsupportedOperationException(
              "The object: " + this.toString() + " is an Atomic term. It does not have arguments.");
    } else {
      throw new IllegalArgumentException("Unable to determine the arg " + getPos + " of " + this.toString());
    }

    return (O) KBObjectImpl.checkAndCastObject(o);
  }

  /**
   * Attempts to return an Object (expected to be of type T) for the input
   * Object <code>o</code>.
   *
   * For basic Java objects like String, Number and Date, the object is returned
   * without any modification. CycObjects are converted to KBObjects, of the
   * most specific type possible.
   *
   * @param o object to be mapped to KBObject
   *
   * @return the KBObject constructed.
   * @throws CreateException
   */
  static public <T> T checkAndCastObject(Object o) throws CreateException {
    if (o instanceof CycObject) {
      return (T) KBObjectImpl.convertCycObject((CycObject) o);
    } else if (o instanceof String || o instanceof Number || o instanceof Date) {
      return (T) o;
    } else {
      throw new IllegalArgumentException("Unable to coerce " + o + "(" + o.getClass() + ").");
      // return null;
    }
  }
  static private final CycConstant THE_LIST = new CycConstantImpl("TheList", new Guid("bdcc9f7c-9c29-11b1-9dad-c379636f7270"));
  static private final CycConstant THE_EMPTY_LIST = new CycConstantImpl("TheEmptyList", new Guid("bd79c885-9c29-11b1-9dad-c379636f7270"));
  static private final CycConstant THE_SET = new CycConstantImpl("TheSet", new Guid("bd58e476-9c29-11b1-9dad-c379636f7270"));

  static private Object convertCycObject(CycObject cyco) throws CreateException {
    // First try converting to a Set, List, or Date:
    if (cyco instanceof CycArrayList) {
      CycList cl = (CycArrayList) cyco;
      if (cl.get(0) instanceof CycConstantImpl) {
        try {
          KBTerm kbt = KBTermImpl.get((CycConstant) cl.get(0));
          if (kbt instanceof KBFunction) {
            KBFunction kbf = (KBFunction) kbt;
            // Do not check arity if its a VariableArityFunction, since that check throws an exception
            if ((kbf.isInstanceOf(Constants.getInstance().VAR_ARITY_COL) || kbf.getArity() == cl.size() - 1)
                    && kbf.isInstanceOf(Constants.getInstance().UNREIFIABLE_FUNC_COL)) {
              cyco = new NautImpl(cl);
            }
          }
        } catch (Exception e) {
          // ignore and move on
        }
      }
    }

    // handle an empty list
    if (cyco instanceof CycConstant) {
      final CycConstant c = (CycConstant) cyco;
      if (c.equals(THE_EMPTY_LIST)) {
        return new ArrayList<Object>();
      }
    }

    if (cyco instanceof Naut) {
      final Naut cn = (Naut) cyco;
      final DenotationalTerm functor = (cn).getFunctor();
      if (functor.equals(THE_SET) || functor.equals(THE_LIST)) {
        final Collection<Object> c = functor.equals(THE_SET) ? new HashSet<Object>()
                : new ArrayList<Object>();
        for (Object item : cn.getArguments()) {
          // TODO: Build a KBObject out of the Object item
          c.add(KBObjectImpl.checkAndCastObject(item));
        }
        try {
          return c;
        } catch (ClassCastException ex) {
          //Guess we weren't looking for a Set/List.
        }
      } else if (DateConverter.isCycDate(cn)) {
        try {
          return DateConverter.parseCycDate(cn);
        } catch (ClassCastException ex) {
          System.out.println("Class Cast exception on a date.");
          //Guess we weren't looking for a Date.
        }
      }
    }
    return convertToKBObject(cyco);
  }

  private static KBObject convertToKBObject(CycObject cyco) throws CreateException {
    try {
      if (cyco instanceof CycVariable) {
        return new VariableImpl(cyco);
      } else if (cyco instanceof CycSymbol) {
        return new SymbolImpl(cyco);
      } else if (cyco instanceof CycAssertion) {
        return AssertionImpl.get(cyco);
      } else if (cyco instanceof FormulaSentence) {
        return new SentenceImpl(cyco);
      }

      // Find most specific type, convert it to that and cast to T:
      if (KBTermImpl.existsAsType(cyco)) {
        if (KBCollectionImpl.existsAsType(cyco)) {
          if (FirstOrderCollectionImpl.existsAsType(cyco)) {
            return FirstOrderCollectionImpl.get(cyco);
          } else if (SecondOrderCollectionImpl.existsAsType(cyco)) {
            return SecondOrderCollectionImpl.get(cyco);
          } else {
            return KBCollectionImpl.get(cyco);
          }
        } else if (KBIndividualImpl.existsAsType(cyco)) {
          if (ContextImpl.existsAsType(cyco)) {
            return ContextImpl.get(cyco);
          } else if (RelationImpl.existsAsType(cyco)) {
            if (KBFunctionImpl.existsAsType(cyco)) {
              return KBFunctionImpl.get(cyco);
            } else if (KBPredicateImpl.existsAsType(cyco)) {
              if (BinaryPredicateImpl.existsAsType(cyco)) {
                return BinaryPredicateImpl.get(cyco);
              } else {
                return KBPredicateImpl.get(cyco);
              }
            } else if (QuantifierImpl.existsAsType(cyco)) {
              return QuantifierImpl.get(cyco);
            } else {
              return RelationImpl.get(cyco);
            }
          } else {
            return KBIndividualImpl.get(cyco);
          }
        } else {
          return KBTermImpl.get(cyco);
        }
      } else {
        return KBObjectImpl.get(cyco);
      }
    } catch (KBTypeException ex) {
      throw new CreateException(ex);
    }
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#isAtomic()
   */
  @Override
  public boolean isAtomic() {
    if (this.getCore() instanceof CycConstant
            || this.getCore() instanceof CycVariable
            || this.getCore() instanceof CycSymbol) {
      return true;
    } else {
      return false;
    }
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#isAssertion()
   */
  @Override
  public boolean isAssertion() {
    if (this.getCore() instanceof CycAssertion) {
      return true;
    } else {
      return false;
    }
  }
  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#isVariable()
   */

  @Override
  public boolean isVariable() {
    if (this.getCore() instanceof CycVariable) {
      return true;
    } else {
      return false;
    }
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#getRestriction()
   */
  @Override
  public Sentence getRestriction() {
    if (this.isVariable()) {
      List<Object> l = new ArrayList<Object>();
      l.add(Constants.isa());
      l.add(this);
      l.add(this.getType());
      try {
        return new SentenceImpl(l.toArray());
      } catch (KBTypeException kte) {
        throw new KBApiRuntimeException(kte.getMessage(), kte);
      } catch (CreateException ex) {
        throw new KBApiRuntimeException(ex.getMessage(), ex);
      }
    } else {
      return null; //new ArrayList<Object>();
    }
  }

  // (Quantifier OTHER_OPTIONAL_VARS Sentence)
  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#getQuantification()
   */
  @Override
  public List<Object> getQuantification() throws KBApiException {
    if (this.isVariable()) {
      // By default return "some" (#$thereExits) quantifer for object
      if (this.quantification.isEmpty()) {
        return new ArrayList<Object>();
        /*
         List<Object> nl = new ArrayList<Object>();
         nl.add(Quantifier.get("thereExists"));
         nl.add(this);
         return nl;
         */
      } else {
        List<Object> nl = new ArrayList<Object>();
        nl.addAll(this.quantification);
        nl.add(this);
        return nl;
      }
      /*
       List<Object> l = new ArrayList<Object>();
       l.add(Predicate.get("thereExists"));
       l.add(this);
       return l;
       */

    } else {
      return new ArrayList<Object>();
    }
  }

  // In the interest of making immutable objects we will not 
  // implement setQuantification()
  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#setQuantification()
   */
  @Override
  public void setQuantification() {
    throw new UnsupportedOperationException();
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#formulaArity()
   */
  @Override
  public Integer formulaArity() {
    if (this.getCore() instanceof CycConstantImpl) {
      return 0;
    } else if (this.getCore() instanceof Nart) {
      Nart cn = (Nart) this.getCore();
      return cn.getArity();// .getReferencedConstants().get(getPos);
    } else if (this.getCore() instanceof NautImpl) {
      NautImpl cn = (NautImpl) this.getCore();
      return cn.getArity();// .getReferencedConstants().get(getPos);
    } else if (this.getCore() instanceof CycAssertion) {
      CycAssertion ca = (CycAssertion) this.getCore();
      return ((CycList<Object>) ca.getFormula().get(1)).size();
      //TODO: Careful!! No error checking what so ever!!
    } else if (this.getCore() instanceof FormulaSentence) {
      FormulaSentence cfs = (FormulaSentence) this.getCore();
      return cfs.getArity();
    } else {
      return (Integer) null;
    }
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#getCore()
   */
  // TODO: Make sure that CycObject and all its subclasses are immutable. 
  // JIRA: BASEAPI-17
  @Override
  public CycObject getCore() {
    return core;
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#stringApiValue()
   */
  @Override
  public String stringApiValue() {
    if (!isValid()) {
      throw new StaleKBObjectException("The reference to " + this + " object is stale. "
              + "Possibly because it was delete using x.delete() method.");
    }
    return core.stringApiValue();
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#delete()
   */
  @Override
  public void delete() throws DeleteException {

    try {
      if (core instanceof Fort) {
        getAccess().getUnassertTool().kill((Fort) core, true, KBAPIConfiguration.getShouldTranscriptOperations());
        isValid = false;
      } /*
       * else if (core instanceof CycAssertion) { CycAssertion ca =
       * (CycAssertion) core; if (ca.isGaf()){
       * cyc.unassertGaf(ca.getGaf(), ca.getMt()); } else { throw new
       * Exception ("Couldn't delete the fact: " + core.toString()); } }
       */ else {
        throw new DeleteException("Couldn't kill: "
                + core.toString()
                + ". It was not a Fort.");
      }
    } catch (CycConnectionException e) {
      throw new KBApiRuntimeException(
              "Couldn't kill the constant " + core.toString(), e);
    } catch (CycApiException cae) {
      throw new KBApiRuntimeException("Could not kill the constant: " + core
              + " very likely because it is not in the KB. " + cae.getMessage(), cae);
    }
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#isValid()
   */
  @Override
  public boolean isValid() {
    return isValid;
  }

  /**
   * Package private method to set the validity of object from subclasses, for
   * example Assertion class.
   *
   * @param valid
   */
  void setIsValid(boolean valid) {
    isValid = valid;
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#rename(java.lang.String)
   */
  @Override
  public KBObject rename(final String name) throws InvalidNameException {
    if (core instanceof CycConstantImpl) {
      try {

        getAccess().getObjectTool().rename(((CycConstantImpl) core), name, true,
                KBAPIConfiguration.getShouldTranscriptOperations());
      } catch (CycConnectionException e) {
        throw new KBApiRuntimeException("Unable to rename " + this + " to " + name, e);
      } catch (CycApiException cae) {
        throw new InvalidNameException(cae.getMessage(), cae);
      }
      return this;
    } else {
      throw new UnsupportedOperationException("Couldn't rename " + core
              + ". Not an atomic term (i.e. a CycConstant.) Check if the object isAtomic() before rename operation.");
    }
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#toString()
   */
  @Override
  public String toString() {
    return core.toString();
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#toNLString()
   */
  @Override
  public String toNLString() throws CycConnectionException {
    Paraphraser p = Paraphraser
            .getInstance(Paraphraser.ParaphrasableType.KBOBJECT);
    return p.paraphrase(core).getString();
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#getId()
   */
  @Override
  public String getId() {
    try {
      return DefaultCycObject.toCompactExternalId(core, getAccess());
    } catch (CycConnectionException e) {
      // DefaultCycObject.toCompactExternalId throws exception if core is
      // null
      // or not a CycObject.
      // This should never happen in our case.
      // TODO: Check for null core in the constructor. Happens in Facts
      e.printStackTrace();
    }
    return "";
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((core == null) ? 0 : core.hashCode());
    return result;
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    KBObjectImpl other = (KBObjectImpl) obj;
    if (core == null) {
      if (other.core != null) {
        return false;
      }
    } else if (!core.equals(other.core)) {
      return false;
    }
    return true;
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#equalsSemantically(java.lang.Object)
   */
  @Override
  public boolean equalsSemantically(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || !(object instanceof KBObjectImpl)) {
      return false;
    }
    KBObjectImpl other = (KBObjectImpl) object;
    if (core == null) {
      if (other.core != null) {
        return false;
      }
    } else if (!core.equals(other.core)) {
      return false;
    }
    return true;
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.KBObject#quote()
   */
  @Override
  public KBIndividual quote() throws KBTypeException, CreateException {
    return Constants.getInstance().QUOTE_FUNC.findOrCreateFunctionalTerm(KBIndividualImpl.class, this);
  }

  /**
   * Return the KBCollection as a KBObject of the Cyc term that underlies this
   * class. For example, calling this on a <code>KBCollection</code> object will
   * return KBCollectionImpl.get("#$Collection").
   *
   * @return the KBCollection of the underlying Cyc term of the class.
   */
  public KBObject getType() {
    return getClassType();
  }

  /**
   * Return the KBCollection as a KBObject of the Cyc term that underlies this
   * class. For example, calling this on a <code>KBCollection</code> object will
   * return KBCollectionImpl.get("#$Collection").
   *
   * @return the KBCollection of the underlying Cyc term of the class.
   */
  public static KBObject getClassType() {
    try {
      return KBCollectionImpl.get(getClassTypeString());
    } catch (KBApiException kae) {
      throw new KBApiRuntimeException(kae.getMessage(), kae);
    }
  }

  String getTypeString() {
    return getClassTypeString();
  }

  static String getClassTypeString() {
    return "#$Thing";
  }
  
  private KBCollection typeCore = null;
  
  private Map<String, Object> kboData = new HashMap<String, Object>();

  public KBCollection getTypeCore() {
    return typeCore;
  }

  public void setTypeCore(KBCollection typeCore) {
    this.typeCore = typeCore;
  }

  public Map<String, Object> getKboData() {
    return kboData;
  }

  public void setKboData(Map<String, Object> kboData) {
    this.kboData = kboData;
  }
  
  
}
