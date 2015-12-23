package com.cyc.baseclient.kbtool;

/*
 * #%L
 * File: CycInspectorTool.java
 * Project: Base Client
 * %%
 * Copyright (C) 2013 - 2015 Cycorp, Inc.
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
import com.cyc.base.exception.CycApiException;
import com.cyc.base.exception.CycConnectionException;
import com.cyc.base.exception.CycTimeOutException;
import com.cyc.base.cycobject.CycConstant;
import com.cyc.base.cycobject.Formula;
import com.cyc.base.cycobject.FormulaSentence;
import com.cyc.base.cycobject.Fort;
import com.cyc.base.cycobject.CycList;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.CycSymbol;
import com.cyc.base.cycobject.ElMt;
import com.cyc.base.kbtool.InspectorTool;
import java.util.Map;
import com.cyc.baseclient.AbstractKbTool;
import com.cyc.baseclient.CommonConstants;
import com.cyc.baseclient.CycObjectFactory;
import static com.cyc.baseclient.CycObjectFactory.makeCycSymbol;
import static com.cyc.baseclient.connection.SublApiHelper.makeNestedSubLStmt;
import static com.cyc.baseclient.connection.SublApiHelper.makeSubLStmt;
import com.cyc.baseclient.cycobject.CycFormulaSentence;
import com.cyc.baseclient.cycobject.CycSymbolImpl;
import com.cyc.baseclient.cycobject.DefaultCycObject;
import com.cyc.baseclient.cycobject.ElMtConstant;
import com.cyc.baseclient.datatype.Pair;
import static com.cyc.baseclient.subl.functions.SublFunctions.CATEGORIZE_TERM_WRT_API;
import com.cyc.baseclient.util.LruCache;

/**
 * Tools for examining individual CycObjects. To examine the relationship between
 different CycObjects, use the {@link com.cyc.baseclient.kbtool.CycComparisonTool}.
 * 
 * @see com.cyc.baseclient.kbtool.CycComparisonTool
 * @author nwinant
 */
public class CycInspectorTool extends AbstractKbTool implements InspectorTool {
  
  public CycInspectorTool(CycAccess client) {
    super(client);
  }
  
  
  // Public
  
  /**
   * Returns the count of the instances of the given COLLECTION.
   *
   * @param collection the COLLECTION whose instances are counted
   * @param mt microtheory (including its genlMts) in which the count is determined
   *
   * @return the count of the instances of the given COLLECTION
   *
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public int countAllInstances(Fort collection,
          CycObject mt)
          throws CycConnectionException, CycApiException {
    return getConverse().converseInt("(count-all-instances " + collection.stringApiValue() + " "
            + makeELMt_inner(mt).stringApiValue() + ")");
  }

  /**
   * Returns the count of the instances of the given COLLECTION, implements a cache to avoid asking
 the same question twice from the KB.
   *
   * @param collection the COLLECTION whose instances are counted
   * @param mt microtheory (including its genlMts) in which the count is determined
   *
   * @return the count of the instances of the given COLLECTION
   *
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public int countAllInstances_Cached(Fort collection, CycObject mt)
          throws CycConnectionException, CycApiException {
    Integer countAllInstances = countAllInstancesCache.get(collection);

    if (countAllInstances != null) {
      return countAllInstances;
    }

    final int answer = countAllInstances(collection, makeELMt_inner(mt));
    countAllInstancesCache.put(collection, answer);

    return answer;
  }
  
  @Override
  public boolean isConstantInKB(CycConstant obj)
          throws CycConnectionException, CycApiException {
    if (obj != null) {
      final CycConstant c = this.getCyc().getLookupTool().getConstantByGuid(obj.getGuid());
      return obj.equals(c) && obj.getName().equals(c.getName());
    }
    return false;
  }
  
  @Override
  public boolean isELMtInKB(ElMt obj)
          throws CycConnectionException, CycApiException {
    if (obj != null) {
      if (ElMtConstant.class.isInstance(obj)) {
        return isConstantInKB((ElMtConstant) obj);
      }
    }
    return false;
  }
  
  /**
   * Returns true if CycFort TERM is a instance of CycFort COLLECTION, defaulting to all
 microtheories.
   *
   * @param term the term
   * @param collectionName the name of the COLLECTION
   *
   * @return <tt>true</tt> if CycFort TERM is a instance of the CycFort named by COLLECTION
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isa(CycObject term,
          String collectionName)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    return isa(term,
            getKnownConstantByName_inner(collectionName));
  }

  /**
   * Returns true if CycFort TERM is a instance of CycFort COLLECTION, defaulting to all
 microtheories.
   *
   * @param term the term
   * @param collection the COLLECTION
   *
   * @return <tt>true</tt> if CycFort TERM is a instance of CycFort COLLECTION
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isa(CycObject term,
          Fort collection)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    return getConverse().converseBoolean("(isa-in-any-mt? " + term.stringApiValue() + " "
            + collection.stringApiValue() + ")");
  }

  /**
   * Returns true if CycFort TERM is a instance of CycFort COLLECTION, using the given microtheory.
   * Method implementation optimised for the binary api.
   *
   * @param term the term
   * @param collection the COLLECTION
   * @param mt the microtheory in which the ask is performed
   *
   * @return <tt>true</tt> if CycFort TERM is a instance of CycFort COLLECTION
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isa(CycObject term,
          CycObject collection,
          CycObject mt)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    final String command = "(isa? " + term.stringApiValue() + " "
            + collection.stringApiValue() + " " + makeELMt_inner(mt).stringApiValue() + ")";

    return getConverse().converseBoolean(command);
  }
  
  @Override
  public CycObject categorizeTermWRTApi (CycObject term) throws CycConnectionException {
    return CATEGORIZE_TERM_WRT_API.eval(getCyc(), term);
  }
  
  /**
   * Returns true if the quoted CycFort TERM is a instance of CycFort COLLECTION, in any microtheory.
   * Method implementation optimised for the binary api.
   *
   * @param term the term
   * @param collection the COLLECTION
   *
   * @return <tt>true</tt> if the quoted CycFort TERM is a instance of CycFort COLLECTION
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isQuotedIsa(final CycObject term, final CycObject collection)
          throws CycConnectionException, com.cyc.base.exception.CycApiException, CycTimeOutException {
    return isQuotedIsa(term, collection, 0);
  }

  /**
   * Returns true if the quoted CycFort TERM is a instance of CycFort COLLECTION, in any microtheory.
   * Method implementation optimised for the binary api.
   *
   * @param term the term
   * @param collection the COLLECTION
   * @param timeoutMsecs the time in milliseconds to wait before giving up, set to
 zero to wait forever.
   *
   * @return <tt>true</tt> if the quoted CycFort TERM is a instance of CycFort COLLECTION
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   * @throws CycTimeOutException if the calculation times out
   */
  public boolean isQuotedIsa(final CycObject term, final CycObject collection,
          long timeoutMsecs)
          throws CycConnectionException, com.cyc.base.exception.CycApiException, CycTimeOutException {
    //// Preconditions
    if (term == null) {
      throw new NullPointerException("term must not be null");
    }
    if (collection == null) {
      throw new NullPointerException("collection must not be null");
    }
    final FormulaSentence query = CycFormulaSentence.makeCycFormulaSentence(
            CommonConstants.QUOTED_ISA, term, collection);
    return getCyc().getInferenceTool().isQueryTrue(query, CommonConstants.INFERENCE_PSC, null, timeoutMsecs);
  }

  /**
   * Returns true if the quoted CycFort TERM is a instance of CycFort COLLECTION,
 in the given inference microtheory.
   *
   * @param term the term
   * @param collection the COLLECTION
   * @param mt the inference microtheory
 set to zero in order to wait forever
   *
   * @return <tt>true</tt> if the quoted CycFort TERM is a instance of CycFort COLLECTION
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isQuotedIsa(final CycObject term, final CycObject collection,
          final CycObject mt)
          throws CycConnectionException, com.cyc.base.exception.CycApiException, CycTimeOutException {
    return isQuotedIsa(term, collection, mt, 0);
  }

  /**
   * Returns true if the quoted CycFort TERM is a instance of CycFort COLLECTION,
 in the given inference microtheory.
   *
   * @param term the term
   * @param collection the COLLECTION
   * @param mt the inference microtheory
   * @param timeoutMsecs the time in milliseconds to wait before giving up,
 set to zero in order to wait forever
   *
   * @return <tt>true</tt> if the quoted CycFort TERM is a instance of CycFort COLLECTION
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   * @throws CycTimeOutException if the calculation times out
   */
  public boolean isQuotedIsa(final CycObject term, final CycObject collection,
          final CycObject mt, long timeoutMsecs)
          throws CycConnectionException, com.cyc.base.exception.CycApiException, CycTimeOutException {
    if (term == null) {
      throw new NullPointerException("Term must not be null.");
    }
    if (collection == null) {
      throw new NullPointerException("Collection must not be null.");
    }
    if (mt == null) {
      throw new NullPointerException("Microtheory must not be null.");
    }
    final FormulaSentence query = CycFormulaSentence.makeCycFormulaSentence(
            CommonConstants.QUOTED_ISA, term, collection);
    return getCyc().getInferenceTool().isQueryTrue(query, makeELMt_inner(mt), null, timeoutMsecs);
  }
  
    /**
   * Returns <tt>true</tt> iff backchain inference on the given PREDICATE is required.
   *
   * @param predicate the <tt>CycConstantImpl</tt> PREDICATE for which backchaining required status is
 sought
   * @param mt microtheory (including its genlMts) in which the backchaining required status is
 sought
   *
   * @return <tt>true</tt> iff backchain inference on the given PREDICATE is required
   *
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isBackchainRequired(CycConstant predicate,
          CycObject mt)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
//    CycConstantImpl backchainRequired = getKnownConstantByGuid_inner(
//            "beaa3d29-9c29-11b1-9dad-c379636f7270");

    return getCyc().getLookupTool().hasSomePredicateUsingTerm(CommonConstants.BACKCHAIN_REQUIRED,
            predicate, 1, makeELMt_inner(mt));
  }

  /**
   * Returns <tt>true</tt> iff backchain inference on the given PREDICATE is encouraged.
   *
   * @param predicate the <tt>CycConstantImpl</tt> PREDICATE for which backchaining encouraged status
 is sought
   * @param mt microtheory (including its genlMts) in which the backchaining encouraged status is
 sought
   *
   * @return <tt>true</tt> iff backchain inference on the given PREDICATE is encouraged
   *
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isBackchainEncouraged(CycConstant predicate,
          CycObject mt)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
   // CycConstantImpl backchainEncouraged = getKnownConstantByGuid_inner(
   //         "c09d1cea-9c29-11b1-9dad-c379636f7270");

    return getCyc().getLookupTool().hasSomePredicateUsingTerm(CommonConstants.BACKCHAIN_ENCOURAGED,
            predicate, 1, makeELMt_inner(mt));
  }

  /**
   * Returns <tt>true</tt> iff backchain inference on the given PREDICATE is discouraged.
   *
   * @param predicate the <tt>CycConstantImpl</tt> PREDICATE for which backchaining discouraged status
 is sought
   * @param mt microtheory (including its genlMts) in which the backchaining discouraged status is
 sought
   *
   * @return <tt>true</tt> iff backchain inference on the given PREDICATE is discouraged
   *
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isBackchainDiscouraged(CycConstant predicate,
          CycObject mt)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    //CycConstantImpl backchainDiscouraged = getKnownConstantByGuid_inner(
    //        "bfcbce14-9c29-11b1-9dad-c379636f7270");

    return getCyc().getLookupTool().hasSomePredicateUsingTerm(CommonConstants.BACKCHAIN_DISCOURAGED,
            predicate, 1, makeELMt_inner(mt));
  }

  /**
   * Returns <tt>true</tt> iff backchain inference on the given PREDICATE is forbidden.
   *
   * @param predicate the <tt>CycConstantImpl</tt> PREDICATE for which backchaining forbidden status is
 sought
   * @param mt microtheory (including its genlMts) in which the backchaining forbidden status is
 sought
   *
   * @return <tt>true</tt> iff backchain inference on the given PREDICATE is forbidden
   *
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isBackchainForbidden(CycConstant predicate,
          CycObject mt)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    //CycConstantImpl backchainForbidden = getKnownConstantByGuid_inner(
    //        "bfa4e9d2-9c29-11b1-9dad-c379636f7270");

    return getCyc().getLookupTool().hasSomePredicateUsingTerm(CommonConstants.BACKCHAIN_FORBIDDEN,
            predicate, 1, makeELMt_inner(mt));
  }

  /**
   * Returns <tt>true</tt> iff the PREDICATE has the irreflexive property: (#$isa ?PRED
   * #$IrreflexsiveBinaryPredicate).
   *
   * @param predicate the <tt>CycConstantImpl</tt> PREDICATE for which irreflexive status is sought
   * @param mt microtheory (including its genlMts) in which the irreflexive status is sought
   *
   * @return <tt>true</tt> iff the PREDICATE has the irreflexive property: (#$isa ?PRED
   * #$IrreflexsiveBinaryPredicate)
   *
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isIrreflexivePredicate(CycConstant predicate,
          CycObject mt)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    //CycConstantImpl irreflexiveBinaryPredicate = getKnownConstantByGuid_inner(
    //        "bd654be7-9c29-11b1-9dad-c379636f7270");

    return this.isa(predicate,
            CommonConstants.IRREFLEXIVE_BINARY_PREDICATE,
            makeELMt_inner(mt));
  }
  
    /**
   * Returns <tt>true</tt> iff <tt>CycArrayList</tt> represents a well formed formula.
   *
   * @param cycList the candidate well-formed-formula
   *
   * @return true iff cycList represents a well formed formula
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isWellFormedFormula(CycList cycList)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    return isWellFormedFormulaInternal(cycList);
  }

  /**
   * Returns <tt>true</tt> iff <tt>CycArrayList</tt> represents a well formed formula.
   *
   * @param formula the candidate well-formed-formula
   *
   * @return true iff cycList represents a well formed formula
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isWellFormedFormula(Formula formula)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    return isWellFormedFormulaInternal(formula);
  }

  /**
   * Returns true if the given HL formula and microtheory correspond to a valid
 assertion in that microtheory.
   *
   * @param gaf the given assertion formula
   * @param mt the candidate assertion microtheory
   * @return if <code>gaf</code> is asserted in <code>mt</code>
   */
  @Override
  public boolean isGafValidAssertion(CycList gaf, ElMt mt)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    String command = makeSubLStmt("find-gaf", gaf, mt);
    Object response = getConverse().converseObject(command);
    return !response.equals(CycObjectFactory.nil);
  }

  /**
   * Returns true if the given HL formula and microtheory correspond to a valid
 assertion in that microtheory.
   *
   * @param gaf the given assertion formula
   * @param mt the candidate assertion microtheory
   */
  @Override
  public boolean isGafValidAssertion(FormulaSentence gaf, ElMt mt)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    return isGafValidAssertion(gaf.getArgs(), mt);
  }

  /**
   * Returns true if the given HL formula and microtheory correspond to a valid
 assertion in that microtheory.
   *
   * @param hlFormula the given HL formula
   * @param mt the candidate assertion microtheory
   */
  @Override
  public boolean isAssertionValid(CycList hlFormula, Fort mt)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    String command = makeSubLStmt("find-assertion", hlFormula, mt);
    Object response = getConverse().converseObject(command);
    return !response.equals(CycObjectFactory.nil);
  }  
  
   /**
   * Returns true if the cyc object is a BinaryPredicate.
   *
   * @param cycObject the given cyc object
   *
   * @return true if cycObject is a BinaryPredicate, otherwise false
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isBinaryPredicate(final CycObject cycObject)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    //// Preconditions
    if (cycObject == null) {
      throw new NullPointerException("cycObject must not be null");
    }
    verifyPossibleDenotationalTerm_inner(cycObject);
    return getConverse().converseBoolean(
            "(binary-predicate? " + cycObject.stringApiValue() + ")");
  }

  /**
   * Returns true if the candidate name uses valid CycConstantImpl characters.
   *
   * @param candidateName the candidate name
   *
   * @return true if the candidate name uses valid CycConstantImpl characters
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isValidConstantName(String candidateName)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    return getConverse().converseBoolean(
            "(new-constant-name-spec-p " + DefaultCycObject.stringApiValue(candidateName) + ")");
  }

  /**
   * Returns true if the candidate name is an available CycConstantImpl name, case insensitive.
   *
   * @param candidateName the candidate name
   *
   * @return true if the candidate name uses valid CycConstantImpl characters
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isConstantNameAvailable(String candidateName)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    return getConverse().converseBoolean("(constant-name-available \"" + candidateName + "\")");
  }

  /**
   * Returns true if term is a quotedCollection, in any microtheory
   *
   * @param cycFort the given CycFort term
   *
   * @return true if term is a quotedCollection
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   * @deprecated
   */
  @Override
  public boolean isQuotedCollection(Fort cycFort)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    throw new com.cyc.base.exception.CycApiException(
            "quotedCollection is no longer supported, see Quote");
  }

  /**
   * Returns true if term is a quotedCollection is a quotedCollection.
   *
   * @param cycFort the given CycFort term
   * @param mt the microtheory in which the query is made
   *
   * @return true if term is a quotedCollection
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   * @deprecated
   */
  @Override
  public boolean isQuotedCollection(Fort cycFort,
          CycObject mt)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    throw new com.cyc.base.exception.CycApiException(
            "quotedCollection is no longer supported, see Quote");
  }

  /** @return true iff expression is free of all variables.
   * @throws IOException if a data communication error occurs
   */
  @Override
  public boolean isGround(CycObject expression) throws CycConnectionException {
    return getConverse().converseBoolean("(ground? " + DefaultCycObject.stringApiValue(
            expression) + ")");
  }

  /* *
   * Returns true if cycConstant is a PublicConstant.
   *
   * @param cycConstant the given constant
   *
   * @return true if cycConstant is a PublicConstant
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  // Commented out regarding BASEAPI-63 - nwinant, 2014-08-18
  /*
  @Override
  public boolean isPublicConstant(CycConstant cycConstant)
          throws CycConnectionException, com.cyc.base.CycApiException {
    return getConverse().converseBoolean("(isa-in-any-mt? " + cycConstant.stringApiValue()
            + " " + PUBLIC_CONSTANT + ")");
  }
  */
  
  /**
   * Returns true if the given term is a microtheory.
   *
   * @param term the constant for determination as a microtheory
   *
   * @return <tt>true</tt> iff cycConstant is a microtheory
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isMicrotheory(CycObject term)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    return getConverse().converseBoolean(
            "(isa-in-any-mt? " + term.stringApiValue() + " " + MICROTHEORY + ")");
  }
  
  /**
   * Returns true if the given term is a Collection.
   *
   * @param cycObject the given term
   *
   * @return true if the given term is a Collection
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isCollection(final CycObject cycObject)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    //// Preconditions
    if (cycObject == null) {
      throw new NullPointerException("cycObject must not be null");
    }
    verifyPossibleDenotationalTerm_inner(cycObject);
    return getConverse().converseBoolean(
            "(isa-in-any-mt? " + cycObject.stringApiValue() + " " + COLLECTION + ")");
  }

  /**
   * Returns true if the given object is a Collection.
   *
   * @param obj the given term
   *
   * @return true if the given term is a Collection
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isCollection(final Object obj)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    //// Preconditions
    if (obj == null) {
      throw new NullPointerException("term must not be null");
    }
    if (obj instanceof CycObject) {
      return isCollection((CycObject) obj);
    } else {
      return false;
    }
  }

  /**
   * Returns true if the given term is a COLLECTION, implemented by a cache to avoid asking the same
 question twice from the KB.
   *
   * @param cycObject the given term
   *
   * @return true if the given term is a COLLECTION
   *
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isCollection_Cached(CycObject cycObject)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    //// Preconditions
    if (cycObject == null) {
      throw new NullPointerException("cycObject must not be null");
    }
    verifyPossibleDenotationalTerm_inner(cycObject);
    Boolean isCollection = isCollectionCache.get(cycObject);

    if (isCollection != null) {
      return isCollection.booleanValue();
    }

    final boolean answer = isCollection(cycObject);
    isCollectionCache.put(cycObject, answer);

    return answer;
  }

  @Override
  public boolean isCollection_Cached(Object term)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    if (term instanceof CycObject) {
      return isCollection_Cached((CycObject) term);
    } else {
      return false;
    }
  }

  /**
   * Returns true if the given term is an Individual.
   *
   * @param cycObject the given term
   *
   * @return true if the given term is an Individual
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isIndividual(final CycObject cycObject)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    //// Preconditions
    if (cycObject == null) {
      throw new NullPointerException("cycObject must not be null");
    }
    verifyPossibleDenotationalTerm_inner(cycObject);
    return getConverse().converseBoolean(
            "(isa-in-any-mt? " + cycObject.stringApiValue() + " " + INDIVIDUAL + ")");
  }

  /**
   * Returns true if the given is a Function.
   *
   * @param cycObj the given term
   *
   * @return true if the given is a Function
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isFunction(CycObject cycObj)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    return getConverse().converseBoolean("(isa-in-any-mt? " + cycObj.stringApiValue()
            + " " + FUNCTION_DENOTATIONAL + ")");
  }
  
  /**
   * Returns true if the given is a Function.
   *
   * @param cycObj the given term
   *
   * @return true if the given is a ReifiableFunction
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */ 
  @Override
  public boolean isReifiableFunction(CycObject cycObj)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    return getConverse().converseBoolean("(isa-in-any-mt? " + cycObj.stringApiValue()
            + " " + REIFIABLE_FUNCTION + ")");
  }
  
  /**
   * Returns true if the given term is an evaluatable PREDICATE.
   *
   * @param predicate the given term
   *
   * @return true if true if the given term is an evaluatable PREDICATE, otherwise false
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isEvaluatablePredicate(Fort predicate)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    final String command = makeSubLStmt("with-all-mts", makeNestedSubLStmt(
            "evaluatable-predicate?", predicate));
    return getConverse().converseBoolean(command);
  }

  /**
   * Returns true if cycObject is a Predicate.
   *
   * @param cycObject the term for determination as a PREDICATE
   *
   * @return true if cycObject is a Predicate
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isPredicate(CycObject cycObject)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    //// Preconditions
    if (cycObject == null) {
      throw new NullPointerException("cycObject must not be null");
    }
    verifyPossibleDenotationalTerm_inner(cycObject);
    return getConverse().converseBoolean(
            "(isa-in-any-mt? " + cycObject.stringApiValue() + " " + PREDICATE + ")");
  }

  @Override
  public boolean isPredicate(final Object object) throws CycConnectionException, com.cyc.base.exception.CycApiException {
    if (object == null) {
      throw new NullPointerException("cycObject must not be null");
    }
    if (object instanceof CycObject) {
      return isPredicate((CycObject) object);
    } else {
      return false;
    }
  }

  /**
   * Returns true if the given term is a UnaryPredicate.
   *
   * @param cycObject the given term
   *
   * @return true if true if the given term is a UnaryPredicate, otherwise false
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isUnaryPredicate(CycObject cycObject)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    //// Preconditions
    if (cycObject == null) {
      throw new NullPointerException("cycObject must not be null");
    }
    verifyPossibleDenotationalTerm_inner(cycObject);
    return getConverse().converseBoolean("(isa-in-any-mt? " + cycObject.stringApiValue()
            + " " + UNARY_PREDICATE + ")");
  }
          
  /**
   * Returns true if formula is well-formed in the relevant mt.
   *
   * @param formula the given EL formula
   * @param mt the relevant mt
   *
   * @return true if formula is well-formed in the relevant mt, otherwise false
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isFormulaWellFormed(CycList formula,
          CycObject mt)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    return isFormulaWellFormedInternal(formula, mt);
  }

  /**
   * Returns true if formula is well-formed in the relevant mt.
   *
   * @param formula the given EL formula
   * @param mt the relevant mt
   *
   * @return true if formula is well-formed in the relevant mt, otherwise false
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isFormulaWellFormed(Formula formula,
          CycObject mt)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    return isFormulaWellFormedInternal(formula, mt);
  }



  /**
   * Returns true if formula is well-formed Non Atomic Reifable Term.
   *
   * @param formula the given EL formula
   *
   * @return true if formula is well-formed Non Atomic Reifable Term, otherwise false
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isCycLNonAtomicReifableTerm(CycList formula)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    return isCycLNonAtomicReifableTerm(formula);
  }

  /**
   * Returns true if formula is well-formed Non Atomic Reifable Term.
   *
   * @param formula the given EL formula
   *
   * @return true if formula is well-formed Non Atomic Reifable Term, otherwise false
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isCycLNonAtomicReifableTerm(CycObject formula)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    return getConverse().converseBoolean("(cycl-nart-p " + formula.stringApiValue() + ")");
  }

  /**
   * Returns true if formula is well-formed Non Atomic Un-reifable Term.
   *
   * @param formula the given EL formula
   *
   * @return true if formula is well-formed Non Atomic Un-reifable Term, otherwise false
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isCycLNonAtomicUnreifableTerm(CycObject formula)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    return getConverse().converseBoolean("(cycl-naut-p " + formula.stringApiValue() + ")");
  }
  
    /**
   * Returns true if the given symbol is defined as an api function.
   *
   * @param symbolName the candidate api function symbol name
   *
   * @return true if the given symbol is defined as an api function
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isFunctionBound(String symbolName)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    CycSymbolImpl cycSymbol = makeCycSymbol(
            symbolName);

    return isFunctionBound(cycSymbol);
  }

  /**
   * Returns true if the given symbol is defined as an api function.
   *
   * @param cycSymbol the candidate api function symbol
   *
   * @return rue if the given symbol is defined as an api function
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isFunctionBound(CycSymbol cycSymbol)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    String command = makeSubLStmt("boolean", makeNestedSubLStmt("fboundp",
            cycSymbol));
    return getConverse().converseBoolean(command);
  }
  
    /**
   * Returns true if CycFort SPEC is a spec of CycFort GENL.
   *
   * @param spec the considered spec COLLECTION
   * @param genl the considered genl COLLECTION
   *
   * @return true if CycFort SPEC is a spec of CycFort GENL, otherwise false
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isSpecOf(CycObject spec,
          CycObject genl)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    return isGenlOf(genl,
            spec);
  }

  /**
   * Returns true if CycFort SPEC is a spec of CycFort GENL.
   *
   * @param spec the considered spec COLLECTION
   * @param genl the considered genl COLLECTION
   * @param mt the relevant mt
   *
   * @return true if CycFort SPEC is a spec of CycFort GENL, otherwise false
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isSpecOf(CycObject spec,
          CycObject genl,
          CycObject mt)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    return isGenlOf(genl,
            spec,
            mt);
  }

  /**
   * Returns true if CycFort GENL is a genl of CycFort SPEC.
   *
   * @param genl the COLLECTION for genl determination
   * @param spec the COLLECTION for spec determination
   *
   * @return <tt>true</tt> if CycFort GENL is a genl of CycFort SPEC
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isGenlOf(CycObject genl,
          CycObject spec)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    return getConverse().converseBoolean("(genl-in-any-mt? " + spec.stringApiValue() + " "
            + genl.stringApiValue() + ")");
  }

  /**
   * Returns true if CycFort GENL is a genl of CycFort SPEC, implements a cache to avoid asking the
 same question twice from the KB.
   *
   * @param genl the COLLECTION for genl determination
   * @param spec the COLLECTION for spec determination
   *
   * @return <tt>true</tt> if CycFort GENL is a genl of CycFort SPEC
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isGenlOf_Cached(CycObject genl,
          CycObject spec)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    final Pair args = new Pair(genl, spec);
    Boolean isGenlOf = isGenlOfCache.get(args);
    if (isGenlOf != null) {
      return isGenlOf.booleanValue();
    }
    final boolean answer = isGenlOf(genl, spec);
    isGenlOfCache.put(args, answer);
    return answer;
  }

  /**
   * Returns true if CycFort GENL is a genl of CycFort SPEC in MT.
   *
   * @param genl the COLLECTION for genl determination
   * @param spec the COLLECTION for spec determination
   * @param mt the microtheory for spec determination
   *
   * @return <tt>true</tt> if CycFort GENL is a genl of CycFort SPEC in MT
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isGenlOf(CycObject genl,
          CycObject spec,
          CycObject mt)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    return getConverse().converseBoolean("(genl? " + spec.stringApiValue() + " " + genl.stringApiValue() + " "
            + makeELMt_inner(mt).stringApiValue() + ")");
  }

  /**
   * Returns true if CycFort GENLPRED is a genl-pred of CycFort SPECPRED in MT.
   *
   * @param genlPred the PREDICATE for genl-pred determination
   * @param specPred the PREDICATE for spec-pred determination
   * @param mt the microtheory for subsumption determination
   *
   * @return <tt>true</tt> if CycFort GENLPRED is a genl-pred of CycFort SPECPRED in MT
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isGenlPredOf(Fort genlPred,
          Fort specPred,
          CycObject mt)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    return getConverse().converseBoolean("(genl-predicate? " + specPred.stringApiValue() + " "
            + genlPred.stringApiValue() + " " + makeELMt_inner(
            mt).stringApiValue() + ")");
  }

  /**
   * Returns true if CycFort GENLPRED is a genl-pred of CycFort SPECPRED in any MT.
   *
   * @param genlPred the PREDICATE for genl-pred determination
   * @param specPred the PREDICATE for spec-pred determination
   *
   * @return <tt>true</tt> if CycFort GENLPRED is a genl-pred of CycFort SPECPRED in any MT
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isGenlPredOf(Fort genlPred,
          Fort specPred)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    return getConverse().converseBoolean("(with-all-mts (genl-predicate? " + specPred.stringApiValue() + " "
            + genlPred.stringApiValue() + "))");
  }

  /**
   * Returns true if CycFort GENLPRED is a genl-inverse of CycFort SPECPRED in MT.
   *
   * @param genlPred the PREDICATE for genl-inverse determination
   * @param specPred the PREDICATE for spec-inverse determination
   * @param mt the microtheory for inverse subsumption determination
   *
   * @return <tt>true</tt> if CycFort GENLPRED is a genl-inverse of CycFort SPECPRED in MT
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isGenlInverseOf(Fort genlPred,
          Fort specPred,
          CycObject mt)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    return getConverse().converseBoolean("(genl-inverse? " + specPred.stringApiValue() + " "
            + genlPred.stringApiValue() + " " + makeELMt_inner(
            mt).stringApiValue() + ")");
  }

  /**
   * Returns true if CycFort GENLPRED is a genl-inverse of CycFort SPECPRED in any MT.
   *
   * @param genlPred the PREDICATE for genl-inverse determination
   * @param specPred the PREDICATE for spec-inverse determination
   *
   * @return <tt>true</tt> if CycFort GENLPRED is a genl-inverse of CycFort SPECPRED in any MT
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isGenlInverseOf(Fort genlPred,
          Fort specPred)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    return getConverse().converseBoolean("(with-all-mts (genl-inverse? " + specPred.stringApiValue() + " "
            + genlPred.stringApiValue() + "))");
  }

  /**
   * Returns true if CycFort genlMt is a genl-mt of CycFort SPECPRED in mt-mt (currently
   * #$UniversalVocabularyMt).
   *
   * @param genlMt the microtheory for genl-mt determination
   * @param specMt the microtheory for spec-mt determination
   *
   * @return <tt>true</tt> if CycFort genlMt is a genl-mt of CycFort SPECPRED in mt-mt (currently
   * #$UniversalVocabularyMt)
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean isGenlMtOf(CycObject genlMt,
          CycObject specMt)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    return getConverse().converseBoolean("(genl-mt? " + makeELMt_inner(specMt).stringApiValue() + " "
            + makeELMt_inner(genlMt).stringApiValue() + ")");
  }
  
  
  // Private
  
  private boolean isWellFormedFormulaInternal(CycObject cycList)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    return getConverse().converseBoolean(makeSubLStmt(WITH_ALL_MTS, makeNestedSubLStmt(EL_WFF,
            cycList)));
  }
  
  private boolean isFormulaWellFormedInternal(CycObject formula,
          CycObject mt)
          throws CycConnectionException, com.cyc.base.exception.CycApiException {
    return getConverse().converseBoolean("(el-formula-ok? " + formula.stringApiValue() + " "
            + makeELMt_inner(mt).stringApiValue() + ")");
  }
  
  
  // Internal
  
  private static final CycSymbolImpl EL_WFF = makeCycSymbol("el-wff?");

  /** 
   * Least Recently Used Cache of isCollection results.
   */
  private final Map<CycObject, Boolean> isCollectionCache = new LruCache<CycObject, Boolean>(
          500, 5000, true);
  
  /** 
   * Least Recently Used Cache of isGenlOf results.
   */
  private final Map<Pair, Boolean> isGenlOfCache = new LruCache<Pair, Boolean>(
          500, 5000, true);
  
  /** 
   * Least Recently Used Cache of countAllInstances results. 
   */
  private final Map<Fort, Integer> countAllInstancesCache = new LruCache<Fort, Integer>(
          500, 5000, true);
  
  private static final String MICROTHEORY = CommonConstants.MICROTHEORY.cyclify();
  // Commented out regarding BASEAPI-63 - nwinant, 2014-08-18
  //  private static final String PUBLIC_CONSTANT = CommonConstants.PUBLIC_CONSTANT.cyclify();
  private static final String COLLECTION = CommonConstants.COLLECTION.cyclify();
  private static final String INDIVIDUAL = CommonConstants.INDIVIDUAL.cyclify();
  private static final String FUNCTION_DENOTATIONAL = CommonConstants.FUNCTION_DENOTATIONAL.cyclify();
  private static final String REIFIABLE_FUNCTION = CommonConstants.REIFIABLE_FUNCTION.cyclify();
  private static final String PREDICATE = CommonConstants.PREDICATE.cyclify();
  private static final String UNARY_PREDICATE = CommonConstants.UNARY_PREDICATE.cyclify();
}
