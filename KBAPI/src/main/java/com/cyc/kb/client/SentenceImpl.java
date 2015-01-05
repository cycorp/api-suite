package com.cyc.kb.client;

/*
 * #%L
 * File: SentenceImpl.java
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
import com.cyc.base.CycApiException;
import com.cyc.base.CycConnectionException;
import com.cyc.base.cycobject.CycConstant;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.FormulaSentence;
import com.cyc.base.cycobject.Guid;
import com.cyc.base.cycobject.Naut;
import com.cyc.baseclient.CommonConstants;
import com.cyc.baseclient.cycobject.CycConstantImpl;
import com.cyc.baseclient.cycobject.CycFormulaSentence;
import com.cyc.baseclient.cycobject.NautImpl;
import com.cyc.baseclient.datatype.DateConverter;
import com.cyc.kb.*;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KBApiException;
import com.cyc.kb.exception.KBApiRuntimeException;
import com.cyc.kb.exception.KBTypeException;
import com.cyc.kb.quant.QuantifiedRestrictedVariable;
import com.cyc.kb.quant.RestrictedVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A <code>Sentence</code> object is a Java representation of a syntactically well-formed
 * <code>#$CycLSentence</code>.
 *
 * {@link com.cyc.kb.KBTerm Denotational terms} (including terms formed by
 * {@link com.cyc.kb.KBFunction functions}) are not sentences, but can be included as elements of
 * sentences. A syntactically well-formed Sentence consists of a {@link Relation} followed by any
 * number of additional terms, which could be denotational terms or additional sentences. A sentence
 * need not obey the arity restrictions of the top-level Relation in order to be syntactically
 * well-formed. In other words, the only restriction that must be obeyed to construct a sentence is
 * that there must be a relation in the 0th position.
 *
 * Unlike most of the objects in the KB API which correspond directly to an object on the Cyc
 * server, sentences are merely combinations of objects that exist on the server, but the
 * combination itself need not correspond to a server-side object. They can, however, still be used
 * and understood by the server, and are used extensively to perform queries or make assertions.
 *
 * @author Vijay Raj
 * @version $Id: SentenceImpl.java 155325 2014-12-03 19:44:38Z daves $
 * @since 1.0
 */
public class SentenceImpl extends StandardKBObject implements Sentence {

  private static Logger log = LoggerFactory.getLogger(SentenceImpl.class.getCanonicalName());

  // The list of KBObject or Primitive datatypes
  // This is to preserve all the KBObjects passed in to construct the sentence.
  // This is expected to be useful when Sentence has to be reconstructed when
  // handling RestrictedVariable, since the RVs have a restriction within, that
  // gets added to the sentence. 
  /**
   * NOT PART OF KB API 1.0
   */
  // 2014-10-28: This is not populated from FormulaSentence and can be assumed to be
  // non null at any time.
  private List<Object> arguments;

  /**
   * Return a new <code>Sentence</code> based on the existing CycFormulaSentence
   * <code>cycObject</code>.
   *
   * @param cycObject	the source CycObject for the Sentence. The constructor verifies that the
   * CycObject is a #$CycLSentence
   *
   * @throws KBTypeException is thrown in cycObject is not an instance of CycFormulaSentence
   */
  public SentenceImpl(CycObject cycObject) throws KBTypeException, CreateException {
    super(cycObject);
    arguments = formulaSentenceToArgList((FormulaSentence)cycObject);
  }
  
  // @TODO: This does not support typed 
  private List<Object> formulaSentenceToArgList(FormulaSentence formula) throws CreateException {
    List<Object> tempArgList = new ArrayList<Object>();
    for (Object o : formula.getArgs()) {
      // NOTE: There is a recursion here. 
      // checkAndCastObject calls SentenceImpl(CycObject) which inturn calls
      // this method
      tempArgList.add(KBObjectImpl.checkAndCastObject(o));
    }
    return tempArgList;
  }

  /**
   * Builds a sentence based on <code>pred</code> and other <code>args</code>. Note that
   * <code>args</code> should be KBObjects,
   * {@link java.lang.String Strings}, {@link java.lang.Number Numbers}, or
   * {@link java.util.Date Dates}. This constructor also handles {@link java.util.List Lists} and
   * {@link java.util.Set Sets} (and Lists of Lits or Sets of Lists, etc.) of those supported
   * objects.
   *
   * @param pred the first argument of the formula
   * @param args the other arguments of the formula in the order they appear in the list
   *
   * @throws KBTypeException is thrown if the built cycObject is not a instance of
   * CycFormulaSentence. This should never happen.
   */
  public SentenceImpl(Relation pred, Object... args) throws KBTypeException, CreateException {
    this(combineParams(pred, args));
  }

  /**
   * Returns a new list of objects based on <code>pred</code> and other <code>args</code>. Note that
   * <code>args</code> should be KBObjects,
   * {@link java.lang.String Strings}, {@link java.lang.Number Numbers}, or
   * {@link java.util.Date Dates}. This constructor also handles {@link java.util.List Lists} and
   * {@link java.util.Set Sets} (and Lists of Lits or Sets of Lists, etc.) of those supported
   * objects.
   *
   * @param pred the first argument of the formula
   * @param args the other arguments of the formula in order
   *
   * @return a new list with the supplied arguments
   */
  private static Object[] combineParams(Relation pred, Object... args) {
    List<Object> l = new ArrayList<Object>();
    l.addAll(Arrays.asList(args));
    l.add(0, pred);
    return l.toArray();
  }

  /**
   * Builds an arbitrary sentence based on the <code>args</code> provided. Note that
   * <code>args</code> should either be KBObjects or Java classes, String, Number or Date. This
   * constructor also handles java.util.List and java.util.Set of other supported KB API or Java
   * objects. It even supports, List of List etc.
   *
   * @param args the arguments of the formula in order
   *
   * @throws KBTypeException never thrown
   */
  public SentenceImpl(Object... args) throws KBTypeException, CreateException {
    this(convertKBObjectArrayToCycFormulaSentence(args));
    arguments = Arrays.asList(args);
    log.info("Create sentence with args: {}", Arrays.asList(args));
  }

  /**
   * Used in the method {@link #convertKBObjectArrayToCycFormulaSentence(java.lang.Object...)} 
   */
  static private final CycConstant THE_EMPTY_LIST = new CycConstantImpl("TheEmptyList", new Guid("bd79c885-9c29-11b1-9dad-c379636f7270"));

  /**
   * Build a CycFormulaSentence from the given KBObjects arguments <code>args</code>. Note that
   * <code>args</code> should either be KBObjects or Java classes, String, Number or Date. This
   * method also handles java.util.List and java.util.Set of other supported KB API or Java objects.
   * It even supports, List of List etc.
   *
   * @param args
   *
   * @return a CycFormulaSentence corresponding to the arguments <code>args</code>.
   */
  private static FormulaSentence convertKBObjectArrayToCycFormulaSentence(Object... args) {
    List<Object> outargs = new ArrayList<Object>();
    List<Object> tempoutargs = new ArrayList<Object>();
    try {
      for (Object arg : args) {
        if (arg instanceof RestrictedVariable) {
          tempoutargs.add(((RestrictedVariable) arg).getVariable().getCore());
          if (outargs.isEmpty()) {
            outargs.add(CommonConstants.AND);
          }
          outargs.add(convertKBObjectArrayToCycFormulaSentence(((RestrictedVariable) arg).getSentenceArguments().toArray()));
        } else if (arg instanceof QuantifiedRestrictedVariable) {

        } else if (arg instanceof KBObject) {
          tempoutargs.add(((KBObject) arg).getCore());
        } else if (arg instanceof List) {
          if (((List) arg).isEmpty()) {
            tempoutargs.add(THE_EMPTY_LIST);
          } else {
            FormulaSentence cfs = convertKBObjectArrayToCycFormulaSentence(((List<?>) arg).toArray());
            Naut cn = new NautImpl(getStaticAccess().getLookupTool().getKnownFortByName(
                    "TheList"), cfs.toCycList().toArray());
            tempoutargs.add(cn);
          }
        } else if (arg instanceof Set) {
          FormulaSentence cfs = convertKBObjectArrayToCycFormulaSentence(((Set<?>) arg).toArray());
          Naut cn = new NautImpl(getStaticAccess().getLookupTool().getKnownFortByName(
                  "TheSet"), cfs.toCycList().toArray());
          tempoutargs.add(cn);
        } else if (arg instanceof Date) {
          DateConverter.getInstance();
          CycObject co = DateConverter.toCycDate((Date) arg);
          tempoutargs.add(co);
        } else {
          tempoutargs.add(arg);
        }
      }
    } catch (CycConnectionException e) {
      // Low level connection exception
      throw new KBApiRuntimeException(e.getMessage(), e);
    }

    if (outargs.isEmpty()) {
      outargs.addAll(tempoutargs);
    } else {
      outargs.add(CycFormulaSentence.makeCycFormulaSentence(tempoutargs.toArray()));
    }

    return CycFormulaSentence.makeCycFormulaSentence(outargs.toArray());
  }

  /**
   * Attempts to convert a CycL string into a CycFormulaSentence and thus into 
   * a KBObject, Sentence. 
   * <p>
   * 
   * @param sentStr	the string representing a Sentence in the KB, a CycL sentence
   *
   * @throws KBApiException	if the Sentence represented by sentStr could not be 
   * parsed.
   */
  public SentenceImpl(String sentStr) throws KBApiException {
    super(parseCycLString(sentStr));
  }
  
  /**
   * Introducing a static method to change the Exception thrown to KBApiException instead
   * of CycApiException.
   * 
   * @param cycLString  the string to parse into a CycFormulaSentence
   * 
   * @return  a FormulaSentence represented by CycL string, <code>cycLString</code>
   * 
   * @throws CreateException if cycLString can not be parsed
   */
  private static FormulaSentence parseCycLString (String cycLString) throws CreateException {
    try {
      return CycFormulaSentence.makeCycSentence(getStaticAccess(), cycLString);
    } catch (CycApiException ex) {
      throw new CreateException (ex.getMessage(), ex);
    } catch (CycConnectionException ex) {
      throw new CreateException (ex.getMessage(), ex);
    }
  }

  /*
   * Creates a
   * <code>Sentence</code> based on the pre-existing term in the Cyc KB
   * with HL ID sentHlid and the name sentHlid.
   * <p>
   * See {@link Sentence#Sentence(String)} for a way to make a new
   * predicate.
   *
   * @param sentStr	the string representing a Sentence in the KB
   * @param	sentHlid	the HLID of the implementation-dependent object
   *
   * @throws KBApiException	if the Sentence represented by
   * sentHlid and having an HLID sentHlid is not found
   * @todo what happens if the hlid and the predStr don't match? Is that also an
   * Exception, or is one or the other preferred?
   */
  //  We will either use HLID or cycObjString. There is no point using both.
  // @Deprecated //deprecated in the sense of broken, not in the sense that it shouldn't be here
  // DO WE WANT THIS AT ALL???
  /*
   public Sentence(String sentStr, String sentHlid) throws KBApiException {
   super(sentStr, sentHlid);
   this.arguments = new ArrayList<Object> ();
   throw new UnsupportedOperationException("Stub method. Not tested.");
   }
   */

    

  /*********************
   * Static methods
   *********************/ 
    
  /**
   * This not part of the public, supported KB API. Check that the candidate core object is valid
   * CycFormulaSentence. In the CycKB the object would be valid #$CycLSentence
   *
   * Refer to {@link StandardKBObject#isValidCore(com.cyc.base.cycobject.CycObject) } for more comments
   */
  // NOTE: We might want to do a lenient WFF check here. But it could be very expensive
  // and unnecessary. 
  @Override
  //@todo Should this be static?  Also, why is the javadoc for this not showing up.  I'm seeing javadoc from somewhere else...
  protected boolean isValidCore(CycObject cycObject) {
    if (cycObject instanceof CycFormulaSentence) {
      return true;
      // return !((CycFormulaSentence) tempCore).hasWffConstraintViolations(CycAccess.getCurrent(), CycAccess.universalVocabularyMt);
    } else {
      return false;
    }
  }

  @Override
  public boolean isAssertible(Context ctx) {
    return !((CycFormulaSentence) core).hasWffConstraintViolations(getAccess(), ctx.asELMt());
  }

  @Override
  public String notAssertibleExplanation(Context ctx) {
    try {
      return ((CycFormulaSentence) core).getNonWffAssertExplanation(getAccess(), ctx.asELMt());
    } catch (Exception e) {
      log.error(e.getMessage());
      log.error(Arrays.toString(e.getStackTrace()));
      return null;
    }
  }

  /**

   * Conjoin sentences. Creates a list and calls {@link #and(java.lang.Iterable)}
   *
   * @param sentences list of sentences to be conjoined
   *
   * @return a new conjoined sentence
   * @throws KBTypeException
   */
  public static Sentence and(Sentence... sentences) throws KBTypeException, CreateException {
    return and(Arrays.asList(sentences));
  }

  /**
   * Conjoin sentences. Creates a new sentence with #$and as the relation and all other sentences as
   * the arguments.
   *
   * @param sentences list of sentences to be conjoined
   *
   * @return a new conjunction sentence
   * @throws com.cyc.kb.exception.KBTypeException
   */
  public static Sentence and(Iterable<Sentence> sentences) throws KBTypeException, CreateException {
    List<FormulaSentence> cfsList = new ArrayList<FormulaSentence>();
    for (Sentence s : sentences) {
      cfsList.add(((CycFormulaSentence) s.getCore()));
    }
    final FormulaSentence cfs = CycFormulaSentence.makeConjunction(cfsList);
    // TODO: Can we catch KBTypeException. We know all components are Sentences.
    // combination should be a Sentence
    return new SentenceImpl(cfs);
  }
  /**
   * Disjoin sentences. Creates a list and calls {@link #or(java.lang.Iterable)}
   * @param sentences list of sentences to be disjoined
   *
   * @return a new disjunction sentence
   * @throws KBTypeException
   */
  public static Sentence or(Sentence... sentences) throws KBTypeException, CreateException {
    return or(Arrays.asList(sentences));
  }

  /**
   * Disjoin sentences. Creates a new sentence with #$or as the relation and all other sentences as
   * the arguments.
   *
   * @param sentences list of sentences to be disjoined
   *
   * @return a new disjunction sentence
   * @throws com.cyc.kb.exception.KBTypeException
   */
  public static Sentence or(Iterable<Sentence> sentences) throws KBTypeException, CreateException {
    List<FormulaSentence> cfsList = new ArrayList<FormulaSentence>();
    for (Sentence s : sentences) {
      cfsList.add(((CycFormulaSentence) s.getCore()));
    }
    final FormulaSentence cfs = CycFormulaSentence.makeDisjunction(cfsList);
    // TODO: Can we catch KBTypeException. We know all components are Sentences.
    // combination should be a Sentence    
    return new SentenceImpl(cfs);
  }

  public static Sentence implies(Collection<Sentence> posLiterals, Sentence negLiteral) throws KBTypeException, CreateException {
    return implies(and(posLiterals), negLiteral);
  }

  public static Sentence implies(Sentence posLiteral, Sentence negLiteral) throws KBTypeException, CreateException {
    final FormulaSentence conditional = CycFormulaSentence.makeConditional((FormulaSentence) posLiteral.getCore(), (FormulaSentence) negLiteral.getCore());
    return new SentenceImpl(conditional);
  }

  @Override
  public Set<ArgPosition> getArgPositionsForTerm(Object term) {
    Set<ArgPosition> returnResult = new HashSet<ArgPosition>();
    if (term instanceof KBObject) {
      term = ((KBObject)term).getCore();
    }
    if (getCore() instanceof CycFormulaSentence) {
      Set<com.cyc.base.cycobject.ArgPosition> result =  ((CycFormulaSentence)getCore()).getArgPositionsForTerm(term);
      for (com.cyc.base.cycobject.ArgPosition pos : result) {
        returnResult.add(new ArgPositionImpl(pos));
      }
    }
    return returnResult;
  }
  
  public enum SentenceOperatorImpl implements SentenceOperator {
    /**
     * Return a new sentence with <code>sent</code> sentence wrapped with #$not.
     * #$not in queries requires that a sentence be provably false. Therefore in
     * queries it is generally more useful to specify #$unknownSentence for the sub-sentence that is
     * required to be not-provable. Use {@link #UNKNOWN} for that.
     */
    NOT       (Constants.getInstance().NOT_LC),
    /**
     * Return a new sentence with <code>sent</code> sentence wrapped with #$unknownSentence. If this
     * type of sentence is part of a query sentence, the query will only be provable if
     * <code>this</code> sentence is not provable in the context.
     */
    UNKNOWN   (Constants.getInstance().UNKNOWN_SENT_PRED),
    /**
     * Return a new sentence with <code>sent</code> sentence wrapped with #$assertedSentence. If this
     * type of sentence is part of a query sentence, Cyc will return true only if <code>this</code>
     * sentence is explicitly asserted in the KB, not just inferrible.
     */
    ASSERTED  (Constants.getInstance().ASSERTED_SENT_PRED),
    /**
     * Return a new sentence with <code>sent</code> sentence wrapped with #$checkSentence. If this
     * type of sentence is part of a query, Cyc will ensure that <code>this</code> sentence is
     * considered for solving only when all the open variables are bound, by solving other clauses of
     * the query first.
     */
    CHECK     (Constants.getInstance().CHECK_SENT_PRED);

    private final Relation unaryRel;
    /**
     * Enum constructor.
     * 
     * @param unaryRel the relation use to wrap the sentence in {@link #wrap(com.cyc.kb.Sentence) } argument.
     */
    SentenceOperatorImpl(Relation unaryRel) {
      this.unaryRel = unaryRel;
    }
    
    @Override
    public Sentence wrap(Sentence sent) throws KBTypeException, CreateException {
      return new SentenceImpl(unaryRel, sent);
    }
  }
  
  /* (non-Javadoc)
   * @see com.cyc.kb.Sentence#assertIn(com.cyc.kb.ContextImpl)
   */
  @Override
  public Assertion assertIn(Context ctx) throws KBApiException {
    if (this.getArgument(0).equals(LogicalConnectiveImpl.get("implies"))) {
      log.debug("Attempting to assert the Sentence " + this + " in Context: " + ctx + " as a rule.");
      return RuleImpl.findOrCreate(this, ctx);
    } else {
      log.debug("Attempting to assert the Sentence " + this + " in Context: " + ctx + " as a fact.");
      return FactImpl.findOrCreate(this, ctx);
    }
  }

  /**
   * Not part of the public, supported KB API.
   */
  @Override
  public Sentence expandSentence() throws KBApiException {
    List<Sentence> literals = new ArrayList<Sentence>();
    literals.add(this);
    for (Object arg : arguments) {
      if (arg instanceof KBTerm) {
        if (((KBTerm) arg).isVariable()) {
          literals.add(((KBTerm) arg).getRestriction());
        }
      }
    }
    if (literals.isEmpty()) {
      return this;
    } else {
      return new SentenceImpl(LogicalConnectiveImpl.get("and"), literals.toArray());
    }
  }
  
  // @Override
  /**
   * Not part of the public, supported KB API.
   */
  public Collection<KBTerm> getListOfTypedVariables() {
    Set<KBTerm> terms = new HashSet<KBTerm>();
    for (Object arg : arguments) {
      if (arg instanceof KBTerm && ((KBTerm) arg).isVariable()) {
        terms.add((KBTerm) arg);
      }
    }
    return terms;
  }

  @Override
  public Sentence replaceTerms(List<Object> from, List<Object> to) throws KBTypeException, CreateException {
    List<Object> modifiedArgument = new ArrayList<Object>();
    for (Object arg : arguments) {
      // If a user wants to replace an entire sentence, it is allowed.
      if (from.contains(arg)) {
        int fromIdx = from.indexOf(arg);
        modifiedArgument.add(to.get(fromIdx));
      } else {
        if (arg instanceof Sentence) {
          Sentence modSent = ((SentenceImpl) arg).replaceTerms(from, to);
          modifiedArgument.add(modSent);
        } else {
          modifiedArgument.add(arg);
        }
      }
    }
    return new SentenceImpl(modifiedArgument.toArray());
  }

    @Override
    public Sentence quantify(KBObject variable) throws KBTypeException, CreateException {
        if (((KBObjectImpl)variable).getKboData().containsKey("quantifier")) {
            Quantifier q = (Quantifier) ((KBObjectImpl)variable).getKboData().get("quantifier");
            return new SentenceImpl(q, variable, this);
        } else {
            return this;
        }
    }

    /**
   * Return the KBCollection as a KBObject of the Cyc term that underlies this class (<code>CycLSentence</code>).
   *
   * @return KBCollectionImpl.get("#$CycLSentence");
   */
  @Override
  public KBObject getType() {
    return getClassType();
  }

  /**
   * Return the KBCollection as a KBObject of the Cyc term that underlies this class (<code>CycLSentence</code>).
   *
   * @return KBCollectionImpl.get("#$CycLSentence");
   */
  public static KBObject getClassType() {
    try {
      return KBCollectionImpl.get(getClassTypeString());
    } catch (KBApiException kae) {
      throw new KBApiRuntimeException(kae.getMessage(), kae);
    }
  }

  @Override
  String getTypeString() {
    return getClassTypeString();
  }

  static String getClassTypeString() {
    return "#$CycLSentence";
  }

  //@todo shouldn't all of these be in the Sentence Interface, with appropriate comments, include mention that they destructively modify things?
  /* (non-Javadoc)
   * @see com.cyc.kb.Sentence#getArguments()
   */
  /**
   * This is not part of KB API 1.0
   * @return the arguments as list of KBObjects
   */
  public List<Object> getArguments() {
    // Make a new list to preserve immutability
    List<Object> copiedList = new ArrayList<Object>();
    // The objects themselves (KB Objects) are immutable, so it is safe to just copy
    // them. Java.util.date is mutable though.
    for (Object arg: arguments) {
      copiedList.add(arg);
    }
    return arguments;
  }

  /* (non-Javadoc)
   * @see com.cyc.kb.Sentence#setArguments(java.util.List)
   */
  public void setArguments(List<Object> arguments) {
    this.arguments = arguments;
  }
}
