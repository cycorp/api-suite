package com.cyc.baseclient.kbtool;

/*
 * #%L
 * File: CycComparisonTool.java
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
import com.cyc.baseclient.AbstractKBTool;
import com.cyc.base.CycApiException;
import com.cyc.base.CycConnectionException;
import com.cyc.base.cycobject.CycConstant;
import com.cyc.base.cycobject.Fort;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.kbtool.ComparisonTool;
import com.cyc.baseclient.CommonConstants;
import com.cyc.baseclient.CycObjectFactory;
import static com.cyc.baseclient.CycObjectFactory.makeCycSymbol;
import static com.cyc.baseclient.api.SubLAPIHelper.makeSubLStmt;
import com.cyc.baseclient.cycobject.CycSymbolImpl;


/**
 * Tools for comparing different CycObjects.
 * 
 * @author nwinant
 */
public class CycComparisonTool extends AbstractKBTool implements ComparisonTool {
  
  public CycComparisonTool(CycAccess client) {
    super(client);
  }
  
  
  // Public

  /**
   * Returns true if CycFort COLLECION1 and CycFort COLLECTION2 are tacitly coextensional via
   * mutual genls of each other.
   *
   * @param collection1 the first given collection
   * @param collection2 the second given collection
   *
   * @return true if CycFort COLLECION1 and CycFort COLLECTION2 are tacitly coextensional via
   * mutual genls of each other, otherwise false
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  public boolean areTacitCoextensional(Fort collection1,
          Fort collection2)
          throws CycConnectionException, com.cyc.base.CycApiException {
    return getConverse().converseBoolean("(with-all-mts (tacit-coextensional? " + collection1.stringApiValue()
            + " " + collection2.stringApiValue() + "))");
  }

  /**
   * Returns true if CycFort COLLECION1 and CycFort COLLECTION2 are tacitly coextensional via
   * mutual genls of each other.
   *
   * @param collection1 the first given collection
   * @param collection2 the second given collection
   * @param mt the relevant mt
   *
   * @return true if CycFort COLLECION1 and CycFort COLLECTION2 are tacitly coextensional via
   * mutual genls of each other, otherwise false
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  public boolean areTacitCoextensional(Fort collection1,
          Fort collection2,
          CycObject mt)
          throws CycConnectionException, com.cyc.base.CycApiException {
    return getConverse().converseBoolean("(tacit-coextensional? " + collection1.stringApiValue() + " "
            + collection2.stringApiValue() + " " + makeELMt_inner(
            mt).stringApiValue() + ")");
  }

  /**
   * Returns true if CycFort COLLECION1 and CycFort COLLECTION2 are asserted coextensional.
   *
   * @param collection1 the first collection
   * @param collection2 the second collection
   *
   * @return true if CycFort COLLECION1 and CycFort COLLECTION2 are asserted coextensional
   * otherwise false
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  public boolean areAssertedCoextensional(Fort collection1,
          Fort collection2)
          throws CycConnectionException, com.cyc.base.CycApiException {
    //CycConstantImpl coExtensional = getKnownConstantByGuid_inner(
    //        "bd59083a-9c29-11b1-9dad-c379636f7270");

    if (predicateRelates(CommonConstants.CO_EXTENSIONAL,
            collection1,
            collection2)) {
      return true;
    } else if (predicateRelates(CommonConstants.CO_EXTENSIONAL,
            collection2,
            collection1)) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Returns true if CycFort COLLECION1 and CycFort COLLECTION2 are asserted coextensional.
   *
   * @param collection1 the first collection
   * @param collection2 the second collection
   * @param mt the relevant mt
   *
   * @return true if CycFort COLLECION1 and CycFort COLLECTION2 are asserted coextensional
   * otherwise false
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  public boolean areAssertedCoextensional(Fort collection1,
          Fort collection2,
          CycObject mt)
          throws CycConnectionException, com.cyc.base.CycApiException {
    //CycConstantImpl coExtensional = getKnownConstantByGuid_inner(
    //        "bd59083a-9c29-11b1-9dad-c379636f7270");

    if (predicateRelates(CommonConstants.CO_EXTENSIONAL,
            collection1,
            collection2,
            mt)) {
      return true;
    } else if (predicateRelates(CommonConstants.CO_EXTENSIONAL,
            collection2,
            collection1,
            mt)) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Returns true if CycFort COLLECION1 and CycFort COLLECTION2 intersect with regard to all-specs.
   *
   * @param collection1 the first collection
   * @param collection2 the second collection
   *
   * @return true if CycFort COLLECION1 and CycFort COLLECTION2 intersect with regard to all-specs
   * otherwise false
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean areIntersecting(Fort collection1,
          Fort collection2)
          throws CycConnectionException, com.cyc.base.CycApiException {
    return getConverse().converseBoolean("(with-all-mts (collections-intersect? "
            + collection1.stringApiValue() + " " + collection2.stringApiValue()
            + "))");
  }

  /**
   * Returns true if CycFort COLLECION1 and CycFort COLLECTION2 intersect with regard to all-specs.
   *
   * @param collection1 the first collection
   * @param collection2 the second collection
   * @param mt the relevant mt
   *
   * @return true if CycFort COLLECION1 and CycFort COLLECTION2 intersect with regard to all-specs
   * otherwise false
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean areIntersecting(Fort collection1,
          Fort collection2,
          CycObject mt)
          throws CycConnectionException, com.cyc.base.CycApiException {
    return getConverse().converseBoolean("(collections-intersect? " + collection1.stringApiValue() + " "
            + collection2.stringApiValue() + " " + makeELMt_inner(
            mt).stringApiValue() + ")");
  }
  
    /**
   * Returns true if CycFort COLLECION1 and CycFort COLLECTION2 are disjoint.
   *
   * @param collection1 the first collection
   * @param collection2 the second collection
   *
   * @return true if CycFort COLLECION1 and CycFort COLLECTION2 are disjoint, otherwise false
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean areDisjoint(CycObject collection1,
          CycObject collection2)
          throws CycConnectionException, com.cyc.base.CycApiException {
    return getConverse().converseBoolean("(with-all-mts (disjoint-with? " + collection1.stringApiValue() + " "
            + collection2.stringApiValue() + "))");
  }

  /**
   * Returns true if CycFort COLLECION1 and CycFort COLLECTION2 are disjoint.
   *
   * @param collection1 the first collection
   * @param collection2 the second collection
   * @param mt the relevant mt
   *
   * @return true if CycFort COLLECION1 and CycFort COLLECTION2 are disjoint, otherwise false
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean areDisjoint(CycObject collection1,
          CycObject collection2,
          CycObject mt)
          throws CycConnectionException, com.cyc.base.CycApiException {
    return getConverse().converseBoolean("(with-all-mts (disjoint-with? " + collection1.stringApiValue() + " "
            + collection2.stringApiValue() + " " + makeELMt_inner(
            mt).stringApiValue() + ")");
  }
  
  /** 
   * Determines whether the two input objects are equal EL expressions.
   */
  @Override
  public boolean equalsEL(Object obj1, Object obj2) throws CycConnectionException {
    if (obj1 != null && obj1.equals(obj2)) {
      return true;
    } else if (obj2 != null && obj2.equals(obj1)) {
      return true;
    } else {
      String command = makeSubLStmt(EQUALS_EL, obj1, obj2);
      // execute the SubL function-call and access the response
      Object response = getConverse().converseObject(command);
      return !response.equals(CycObjectFactory.nil);
    }
  }
  
  /**
   * Returns true if CycConstantImpl BINARYPREDICATE relates CycFort ARG1 and CycFort ARG2.
   *
   * @param binaryPredicate the predicate
   * @param arg1 the first argument related by the predicate
   * @param arg2 the second argument related by the predicate
   *
   * @return true if CycConstantImpl BINARYPREDICATE relates CycFort ARG1 and CycFort ARG2 otherwise
 false
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean predicateRelates(CycConstant binaryPredicate,
          Fort arg1,
          Fort arg2)
          throws CycConnectionException, com.cyc.base.CycApiException {
    Object[] response = {null, null};
    final String command =
            makeSubLStmt("pred-u-v-holds-in-any-mt", binaryPredicate, arg1, arg2);
    response = converse_inner(command);

    if (response[0].equals(Boolean.TRUE)) {
      if (response[1] == null) {
        return false;
      } else if (response[1].toString().equals("T")) {
        return true;
      } else {
        return false;
      }
    } else {
      throw new com.cyc.base.CycApiException(response[1].toString());
    }
  }

  /**
   * Returns true if CycConstantImpl BINARYPREDICATE relates CycFort ARG1 and CycFort ARG2.
   *
   * @param binaryPredicate the predicate
   * @param arg1 the first argument related by the predicate
   * @param arg2 the second argument related by the predicate
   * @param mt the relevant mt
   *
   * @return true if CycConstantImpl BINARYPREDICATE relates CycFort ARG1 and CycFort ARG2 otherwise
 false
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean predicateRelates(CycConstant binaryPredicate,
          Fort arg1,
          Fort arg2,
          CycObject mt)
          throws CycConnectionException, com.cyc.base.CycApiException {
    Object[] response = {null, null};
    final String command =
            makeSubLStmt("pred-u-v-holds", binaryPredicate, arg1, arg2,
            makeELMt_inner(mt));
    response = converse_inner(command);

    if (response[0].equals(Boolean.TRUE)) {
      if (response[1] == null) {
        return false;
      } else if (response[1].toString().equals("T")) {
        return true;
      } else {
        return false;
      }
    } else {
      throw new com.cyc.base.CycApiException(response[1].toString());
    }
  }

  /**
   * Returns true if CycFort TERM is provably not a instance of CycFort COLLECTION, 
   * using the given microtheory.
   * Method implementation optimised for the binary api.
   *
   * @param term the term
   * @param collection the collection
   * @param mt the microtheory in which the ask is performed
   *
   * @return <tt>true</tt> if CycFort TERM is provably not a instance of CycFort COLLECTION
   *
   * @throws UnknownHostException if cyc server host not found on the network
   * @throws IOException if a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public boolean provablyNotIsa(CycObject term,
          CycObject collection,
          CycObject mt)
          throws CycConnectionException, com.cyc.base.CycApiException {
    final String command = "(not-isa? " + term.stringApiValue() + " "
            + collection.stringApiValue() + " " + makeELMt_inner(mt).stringApiValue() + ")";
    return getConverse().converseBoolean(command);
  }
  
  
  // Internal
  
  private static final CycSymbolImpl EQUALS_EL = makeCycSymbol("equals-el?");
}
