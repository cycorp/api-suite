package com.cyc;

/*
 * #%L
 * File: Cyc.java
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
import com.cyc.kb.Assertion;
import com.cyc.kb.BinaryPredicate;
import com.cyc.kb.Context;
import com.cyc.kb.Fact;
import com.cyc.kb.FirstOrderCollection;
import com.cyc.kb.KbCollection;
import com.cyc.kb.KbFunction;
import com.cyc.kb.KbIndividual;
import com.cyc.kb.KbObject;
import com.cyc.kb.KbPredicate;
import com.cyc.kb.KbTerm;
import com.cyc.kb.Relation;
import com.cyc.kb.Rule;
import com.cyc.kb.SecondOrderCollection;
import com.cyc.kb.Sentence;
import com.cyc.kb.Symbol;
import com.cyc.kb.Variable;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KbException;
import com.cyc.kb.exception.KbTypeException;
import com.cyc.kb.spi.AssertionService;
import com.cyc.kb.spi.BinaryPredicateService;
import com.cyc.kb.spi.ContextService;
import com.cyc.kb.spi.FactService;
import com.cyc.kb.spi.FirstOrderCollectionService;
import com.cyc.kb.spi.KbCollectionService;
import com.cyc.kb.spi.KbFunctionService;
import com.cyc.kb.spi.KbIndividualService;
import com.cyc.kb.spi.KbPredicateService;
import com.cyc.kb.spi.KbService;
import com.cyc.kb.spi.KbTermService;
import com.cyc.kb.spi.RelationService;
import com.cyc.kb.spi.RuleService;
import com.cyc.kb.spi.SecondOrderCollectionService;
import com.cyc.kb.spi.SentenceService;
import com.cyc.kb.spi.SymbolService;
import com.cyc.kb.spi.VariableService;
import com.cyc.nl.spi.ParaphraserFactory;
import com.cyc.query.ProofView;
import com.cyc.query.Query;
import com.cyc.query.QueryAnswer;
import com.cyc.query.QueryAnswerExplanation;
import com.cyc.query.QueryAnswerExplanationSpecification;
import com.cyc.query.spi.ProofViewService;
import com.cyc.query.spi.QueryAnswerExplanationService;
import com.cyc.query.spi.QueryService;
import com.cyc.session.SessionManager;
import com.cyc.session.spi.SessionApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 * @author nwinant
 */
public class Cyc {

  //====|    Construction & internal methods    |=================================================//
  
  private Cyc() {
  }

  private static CoreServicesLoader getLoader() {
    return CoreServicesLoader.getInstance();
  }

  //====|    Service accessors    |===============================================================//
  
  /**
   * Returns the {@link SessionApiService}.
   * 
   * @return the SessionApiService
   */
  public static SessionApiService getSessionManagerService() {
    return getLoader().getSessionApiService();
  }
  
  /**
   * Returns the current Cyc {@link SessionManager}.
   *
   * @return the SessionManager
   */
  public static SessionManager getSessionManager() {
    return getLoader().getSessionApiService().getSessionManager();
  }
  
  /**
   * Returns a {@link Assertion} factory service.
   *
   * @return an AssertionService
   */
  public static AssertionService getAssertionService() {
    return getLoader().getKbApiServices().getAssertionService();
  }

  /**
   * Returns a {@link BinaryPredicate} factory service.
   *
   * @return a BinaryPredicateService
   */
  public static BinaryPredicateService getBinaryPredicateService() {
    return getLoader().getKbApiServices().getBinaryPredicateService();
  }

  /**
   * Returns a {@link Context} factory service.
   *
   * @return a ContextService
   */
  public static ContextService getContextService() {
    return getLoader().getKbApiServices().getContextService();
  }

  /**
   * Returns a {@link Fact} factory service.
   *
   * @return a FactService
   */
  public static FactService getFactService() {
    return getLoader().getKbApiServices().getFactService();
  }

  /**
   * Returns a {@link FirstOrderCollection} factory service.
   *
   * @return a FirstOrderCollectionService
   */
  public static FirstOrderCollectionService getFirstOrderCollectionService() {
    return getLoader().getKbApiServices().getFirstOrderCollectionService();
  }

  /**
   * Returns a {@link KbCollection} factory service.
   *
   * @return a KbCollectionService
   */
  public static KbCollectionService getKbCollectionService() {
    return getLoader().getKbApiServices().getKbCollectionService();
  }

  /**
   * Returns a {@link KbFunction} factory service.
   *
   * @return a KbFunctionService
   */
  public static KbFunctionService getKbFunctionService() {
    return getLoader().getKbApiServices().getKbFunctionService();
  }

  /**
   * Returns a {@link KbIndividual} factory service.
   *
   * @return a KbIndividualService
   */
  public static KbIndividualService getKbIndividualService() {
    return getLoader().getKbApiServices().getKbIndividualService();
  }

  /**
   * Returns a {@link KbPredicate} factory service.
   * 
   * @return a KbPredicateService
   */
  public static KbPredicateService getKbPredicateService() {
    return getLoader().getKbApiServices().getKbPredicateService();
  }
  
  /**
   * Returns a {@link KbService} factory service. 
   * 
   * @return a KbService
   */
  public static KbService getKbService() {
    return getLoader().getKbApiServices().getKbService();
  }

  /**
   * Returns a {@link KbTerm} factory service.
   *
   * @return a KbTermService
   */
  public static KbTermService getKbTermService() {
    return getLoader().getKbApiServices().getKbTermService();
  }

  /**
   * Returns a {@link Relation} factory service.
   *
   * @return a RelationService
   */
  public static RelationService getRelationService() {
    return getLoader().getKbApiServices().getRelationService();
  }

  /**
   * Returns a {@link Rule} factory service.
   *
   * @return a RuleService
   */
  public static RuleService getRuleService() {
    return getLoader().getKbApiServices().getRuleService();
  }

  /**
   * Returns a {@link SecondOrderCollection} factory service.
   *
   * @return a SecondOrderCollectionService
   */
  public static SecondOrderCollectionService getSecondOrderCollectionService() {
    return getLoader().getKbApiServices().getSecondOrderCollectionService();
  }

  /**
   * Returns a {@link Sentence} factory service.
   *
   * @return a SentenceService
   */
  public static SentenceService getSentenceService() {
    return getLoader().getKbApiServices().getSentenceService();
  }

  /**
   * Returns a {@link Symbol} factory service.
   *
   * @return a SymbolService
   */
  public static SymbolService getSymbolService() {
    return getLoader().getKbApiServices().getSymbolService();
  }

  /**
   * Returns a {@link Variable} factory service.
   *
   * @return a VariableService
   */
  public static VariableService getVariableService() {
    return getLoader().getKbApiServices().getVariableService();
  }

  /**
   * Returns a {@link Query} factory service.
   *
   * @return a QueryService
   */
  public static QueryService getQueryService() {
    return getLoader().getQueryApiService().getQueryService();
  }

  /**
   * Returns a {@link ProofView} factory service.
   *
   * @return a ProofViewService
   */
  public static ProofViewService getProofViewService() {
    return getLoader().getQueryApiService().getProofViewService();
  }
  
  /**
   * Returns the {@link ParaphraserFactory}.
   * 
   * @return the ParaphraserFactory
   */
  public static ParaphraserFactory getParaphraserFactory() {
    return getLoader().getNlApiServices().getParaphraserFactory();
  }
  
  //====|    KbService-specific accessors    |====================================================//
  
  /**
   * Checks whether a given ID or Cycl term has in the KB. This static method wraps a call to
   * {@link KbService#existsInKb(java.lang.String)}; see that method's documentation for more
   * details.
   *
   * @param nameOrId
   *
   * @return whether a given ID or Cycl term has in the KB
   *
   * @see KbService#existsInKb(java.lang.String)
   */
  public static boolean existsInKb(String nameOrId) {
    return getLoader().getKbApiServices().getKbService().existsInKb(nameOrId);
  }

  /**
   * Returns a KbObject which is the best representation for a given String, so long as it doesn't
   * need to be created in the KB. This static method wraps a call to
   * {@link KbService#getKbObject(java.lang.String)}; see that method's documentation for more
   * details.
   *
   * @param cycLOrId
   *
   * @return a KbObject which is the best representation for the given string
   *
   * @throws KbTypeException
   * @throws CreateException
   *
   * @see KbService#getKbObject(java.lang.String)
   */
  public static KbObject getKbObject(String cycLOrId) throws KbTypeException, CreateException {
    return (KbObject) getLoader().getKbApiServices().getKbService().getKbObject(cycLOrId);
  }

  /**
   * Returns a Object which is the best representation for a given Object (generally a String), so
   * long as it doesn't need to be created in the KB. This static method wraps a call to
   * {@link KbService#getApiObject(java.lang.Object)}; see that method's documentation for more
   * details.
   *
   * @param cycLOrId
   *
   * @return an Object (possibly a KbObject) which is the best representation for the given input
   *
   * @throws KbTypeException
   * @throws CreateException
   *
   * @see KbService#getApiObject(java.lang.Object)
   */
  public static Object getApiObject(Object cycLOrId) throws KbTypeException, CreateException {
    return getLoader().getKbApiServices().getKbService().getApiObject(cycLOrId);
  }

  /**
   * Returns a Object which is the best representation for a given String, so long as it doesn't
   * need to be created in the KB. This static method wraps a call to
   * {@link KbService#getApiObject(java.lang.String)}; see that method's documentation for more
   * details.
   *
   * @param cycLOrId
   *
   * @return an Object (possibly a KbObject) which is the best representation for the given String
   *
   * @throws KbTypeException
   * @throws CreateException
   *
   * @see KbService#getApiObject(java.lang.String)
   */
  public static Object getApiObject(String cycLOrId) throws KbTypeException, CreateException {
    return getLoader().getKbApiServices().getKbService().getApiObject(cycLOrId);
  }

  /**
   * Returns a Object which is the best representation for a given String, so long as it doesn't
   * need to be created in the KB. This static method wraps a call to
   * {@link KbService#getApiObjectDwim(java.lang.String)}; see that method's documentation for more
   * details.
   *
   * @param cycLOrId
   *
   * @return an Object (possibly a KbObject) which is the best representation for the given input
   *
   * @throws KbTypeException
   * @throws CreateException
   *
   * @see KbService#getApiObjectDwim(java.lang.String)
   */
  public static Object getApiObjectDwim(String cycLOrId) throws KbTypeException, CreateException {
    /*
    if (cycLOrId.startsWith("{") && cycLOrId.endsWith("}")) {
      cycLOrId = "(TheSet " + cycLOrId.substring(1, cycLOrId.length() - 1) + ")";
    }
    if (cycLOrId.startsWith("[") && cycLOrId.endsWith("]")) {
      cycLOrId = "(TheList " + cycLOrId.substring(1, cycLOrId.length() - 1) + ")";
    }
    return KB_SVC.getApiObject(cycLOrId);
     */
    return getLoader().getKbApiServices().getKbService().getApiObjectDwim(cycLOrId);
  }

  //====|    Getters: Queries, Explanations, ProofViews    |======================================//
  
  public static <T extends QueryAnswerExplanation>
          QueryAnswerExplanationService<T> findExplanationService(
                  QueryAnswer answer,
                  QueryAnswerExplanationSpecification<T> spec) {
    //return getLoader().findExplanationService(answer, spec);
    return getLoader().getQueryApiService().findExplanationService(getLoader(), answer, spec);
  }
  
  //====|    Constants    |=======================================================================//
          
  public static class Constants {

    private static final Logger LOG = LoggerFactory.getLogger(Cyc.class);

    private static <O extends KbObject> O handleException(Class<O> clazz,
                                                          String nameOrId,
                                                          KbException ex) {
      LOG.error("Error loading " + clazz.getSimpleName() + " '" + nameOrId + "';"
                        + " attempting to continue...", ex);
      return null;
    }

    private static BinaryPredicate initBinaryPredicate(String nameOrId) {
      try {
        return Cyc.getBinaryPredicateService().get(nameOrId);
      } catch (KbException ex) {
        return handleException(BinaryPredicate.class, nameOrId, ex);
      }
    }

    private static Context initContext(String nameOrId) {
      try {
        return Cyc.getContextService().get(nameOrId);
      } catch (KbException ex) {
        return handleException(Context.class, nameOrId, ex);
      }
    }

    private static FirstOrderCollection initFirstOrderCollection(String nameOrId) {
      try {
        return Cyc.getFirstOrderCollectionService().get(nameOrId);
      } catch (KbException ex) {
        return handleException(FirstOrderCollection.class, nameOrId, ex);
      }
    }

    private static KbCollection initKbCollection(String nameOrId) {
      try {
        return Cyc.getKbCollectionService().get(nameOrId);
      } catch (KbException ex) {
        return handleException(KbCollection.class, nameOrId, ex);
      }
    }

    private static KbFunction initKbFunction(String nameOrId) {
      try {
        return Cyc.getKbFunctionService().get(nameOrId);
      } catch (KbException ex) {
        return handleException(KbFunction.class, nameOrId, ex);
      }
    }

    private static KbIndividual initKbIndividual(String nameOrId) {
      try {
        return Cyc.getKbIndividualService().get(nameOrId);
      } catch (KbException ex) {
        return handleException(KbIndividual.class, nameOrId, ex);
      }
    }

    private static KbPredicate initKbPredicate(String nameOrId) {
      try {
        return Cyc.getKbPredicateService().get(nameOrId);
      } catch (KbException ex) {
        return handleException(KbPredicate.class, nameOrId, ex);
      }
    }

    /*
    private static Symbol initSymbol(String name) {
      try {
        return Cyc.getSymbolService().get(name);
      } catch (KbException ex) {
        return handleException(Symbol.class, name, ex);
      }
    }
     */
    private static Variable initVariable(String name) {
      try {
        return Cyc.getVariableService().get(name);
      } catch (KbException ex) {
        return handleException(Variable.class, name, ex);
      }
    }

    /**
     * Context for #$BaseKB
     */
    public static final Context BASE_KB = initContext("BaseKB");

    /**
     * Context for #$EverythingPSC
     */
    public static final Context EVERYTHING_PSC = initContext("EverythingPSC");

    /**
     * Context for #$InferencePSC
     */
    public static final Context INFERENCE_PSC = initContext("InferencePSC");

    /**
     * Context for #$UniversalVocabularyMt
     */
    public static final Context UV_MT = initContext("UniversalVocabularyMt");

    /**
     * <tt>#$ContentForAPITest</tt>, the collection of all regression tests that test the
     * presence/production of KB content that is leveraged by a Java API unit test.
     */
    public static final KbCollection CONTENT_FOR_API_TEST = initKbCollection("ContentForAPITest");

    /**
     * <tt>#$CycLQuerySpecification</tt>, the collection of all KBQs. Each instance of
     * CycLQuerySpecification gives a description of how a particular getQueryService should be run
     * by the CycInferenceEngine. At minimum, this description includes the CycL formula that
     * expresses the getQueryService, but can also include a number of other parameters that control
     * inference, such as what microtheory to do the getQueryService in, how much time to allow,
     * etc.
     */
    public static final KbCollection CYCL_QUERY_SPECIFICATION
            = initKbCollection("CycLQuerySpecification");

    public static final FirstOrderCollection DATA_MT = initFirstOrderCollection("DataMicrotheory");

    public static final KbCollection REIFIABLE_FUNCTION = initKbCollection("ReifiableFunction");

    public static final KbCollection UNREIFIABLE_FUNCTION = initKbCollection("UnreifiableFunction");

    public static final KbCollection VARIABLE_ARITY_FUNCTION
            = initKbCollection("VariableArityFunction");

    public static final KbCollection VARIABLE_ARITY_PREDICATE
            = initKbCollection("VariableArityPredicate");

    // There is no way to get to these variables
    // Preferred way is to get them from their respective classes using getType or getClassType
    /*
    private final KBCollection THING_COL = new KBCollectionImpl("#$Thing");
    private final KBCollection INDIVIDUAL_COL = new KBCollectionImpl("#$Individual");
    private final KBCollection RELATION_COL = new KBCollectionImpl("#$Relation");
    private final KBCollection FUNCTION_COL = new KBCollectionImpl("#$Function-Denotational");
    private final KBCollection PREDICATE_COL = new KBCollectionImpl("#$Predicate");
    private final KBCollection BPRED_COL = new KBCollectionImpl("#$BinaryPredicate");
    private final KBCollection SCOPE_REL_COL = new KBCollectionImpl("#$ScopingRelation");
    private final KBCollection QUANTIFIER_COL = new KBCollectionImpl("#$Quantifier");
    private final KBCollection LOG_CON_COL = new KBCollectionImpl("#$LogicalConnective");
    private final KBCollection COLLECTION_COL = new KBCollectionImpl("#$Collection");
    private final KBCollection FIRST_ORD_COL = new KBCollectionImpl("#$FirstOrderCollection");
    private final KBCollection SECOND_ORD_COL = new KBCollectionImpl("#$SecondOrderCollection");
    private final KBCollection ASSERTION_COL = new KBCollectionImpl("#$CycLAssertion");
    private final KBCollection GAF_COL = new KBCollectionImpl("#$CycLGAFAssertion");
    private final KBCollection VARIABLE_COL = new KBCollectionImpl("#$CycLVariable");
    private final KBCollection SYMBOL_COL = new KBCollectionImpl("#$CycLSubLSymbol");
     */
    public static final KbFunction QUOTE = initKbFunction("Quote");

    public static final KbFunction THE_LIST = initKbFunction("TheList");

    public static final KbFunction THE_SET = initKbFunction("TheSet");

    public static final KbIndividual TRUE_CYCL = initKbIndividual("True");

    public static final KbIndividual FALSE_CYCL = initKbIndividual("False");

    public static final BinaryPredicate ISA = initBinaryPredicate("isa");

    public static final KbPredicate DIFFERENT = initKbPredicate("different");

    public static final BinaryPredicate GENLS = initBinaryPredicate("genls");

    public static final BinaryPredicate GENL_MT = initBinaryPredicate("genlMt");

    public static final BinaryPredicate GENL_PREDS = initBinaryPredicate("genlPreds");

    public static final KbPredicate GENL_INVERSE = initKbPredicate("genlInverse");

    public static final BinaryPredicate QUOTED_ISA = initBinaryPredicate("quotedIsa");

    public static final KbPredicate ARG_ISA = initKbPredicate("argIsa");

    public static final KbPredicate ARG_GENL = initKbPredicate("argGenl");

    public static final BinaryPredicate ARITY = initBinaryPredicate("arity");

    public static final BinaryPredicate RESULT_ISA = initBinaryPredicate("resultIsa");

    public static final BinaryPredicate RESULT_GENL = initBinaryPredicate("resultGenl");

    public static final KbPredicate MT_MONAD = initKbPredicate("mtMonad");

    public static final KbPredicate MT_TIME_INDEX = initKbPredicate("mtTimeIndex");

    public static final BinaryPredicate COMMENT = initBinaryPredicate("comment");

    public static final KbPredicate INTER_ARG_DIFFERENT = initKbPredicate("interArgDifferent");

    public static final KbPredicate ASSERTED_SENTENCE = initKbPredicate("assertedSentence");

    public static final KbPredicate CHECK_SENTENCE = initKbPredicate("checkSentence");

    public static final KbPredicate UNKNOWN_SENTENCE = initKbPredicate("unknownSentence");

    /**
     * CycL variable <tt>?ARG</tt>
     */
    public static final Variable ARG = initVariable("?ARG");

    /**
     * CycL variable <tt>?ARG0</tt>
     */
    public static final Variable ARG0 = initVariable("?ARG0");

    /**
     * CycL variable <tt>?ARG1</tt>
     */
    public static final Variable ARG1 = initVariable("?ARG1");

    /**
     * CycL variable <tt>?ARG2</tt>
     */
    public static final Variable ARG2 = initVariable("?ARG2");

    /**
     * CycL variable <tt>?ARG3</tt>
     */
    public static final Variable ARG3 = initVariable("?ARG3");

    /**
     * CycL variable <tt>?ARG4</tt>
     */
    public static final Variable ARG4 = initVariable("?ARG4");

    /**
     * CycL variable <tt>?ARG5</tt>
     */
    public static final Variable ARG5 = initVariable("?ARG5");

    /**
     * CycL variable <tt>?ARG6</tt>
     */
    public static final Variable ARG6 = initVariable("?ARG6");

    /**
     * CycL variable <tt>?ARG7</tt>
     */
    public static final Variable ARG7 = initVariable("?ARG7");

    /**
     * CycL variable <tt>?ARG8</tt>
     */
    public static final Variable ARG8 = initVariable("?ARG8");

    /**
     * CycL variable <tt>?ARG9</tt>
     */
    public static final Variable ARG9 = initVariable("?ARG9");

    /**
     * CycL variable <tt>?ARGN</tt>
     */
    public static final Variable ARGN = initVariable("?ARGN");

    /**
     * CycL variable <tt>?VAR</tt>
     */
    public static final Variable VAR = initVariable("?VAR");

    /**
     * CycL variable <tt>?VAR1</tt>
     */
    public static final Variable VAR1 = initVariable("?VAR1");

    /**
     * CycL variable <tt>?VAR2</tt>
     */
    public static final Variable VAR2 = initVariable("?VAR2");

    /**
     * CycL variable <tt>?VAR3</tt>
     */
    public static final Variable VAR3 = initVariable("?VAR3");

    /**
     * CycL variable <tt>?VAR4</tt>
     */
    public static final Variable VAR4 = initVariable("?VAR4");

    /**
     * CycL variable <tt>?VAR5</tt>
     */
    public static final Variable VAR5 = initVariable("?VAR5");

    /**
     * CycL variable <tt>?VAR6</tt>
     */
    public static final Variable VAR6 = initVariable("?VAR6");

    /**
     * CycL variable <tt>?VAR7</tt>
     */
    public static final Variable VAR7 = initVariable("?VAR7");

    /**
     * CycL variable <tt>?VAR8</tt>
     */
    public static final Variable VAR8 = initVariable("?VAR8");

    /**
     * CycL variable <tt>?VAR9</tt>
     */
    public static final Variable VAR9 = initVariable("?VAR8");

  }

}
