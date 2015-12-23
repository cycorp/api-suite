package com.cyc.baseclient.kbtool;

/*
 * #%L
 * File: CycAssertTool.java
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
import com.cyc.base.exception.CycConnectionException;
import com.cyc.base.cycobject.CycConstant;
import com.cyc.base.cycobject.DenotationalTerm;
import com.cyc.base.cycobject.FormulaSentence;
import com.cyc.base.cycobject.Fort;
import com.cyc.base.cycobject.CycList;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.ElMt;
import com.cyc.base.kbtool.AssertTool;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.cyc.baseclient.AbstractKbTool;
import com.cyc.baseclient.CommonConstants;
import com.cyc.base.exception.CycApiException;
import com.cyc.baseclient.CycObjectFactory;
import com.cyc.baseclient.cycobject.CycArrayList;
import com.cyc.baseclient.cycobject.CycFormulaSentence;

/**
 * Tools for asserting facts to the Cyc KB. To unassert facts from the Cyc KB, 
 * use the {@link com.cyc.baseclient.kbtool.CycUnassertTool}. To perform simple tasks,
 like constant creation or renaming, use the {@link com.cyc.baseclient.kbtool.CycObjectTool}.
 * 
 * @see com.cyc.baseclient.kbtool.CycUnassertTool
 * @see com.cyc.baseclient.kbtool.CycObjectTool
 * @author nwinant
 */
public class CycAssertTool extends AbstractKbTool implements AssertTool {
  
  public CycAssertTool(CycAccess client) {
    super(client);
  }
  
  
  // Public
  
  /**
   * Assert an argument isa contraint for the given relation and argument position. The operation
 will be added to the KB transcript for replication and archive.
   *
   * @param relation the given relation
   * @param argPosition the given argument position
   * @param argNIsa the argument constraint
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertArgIsa(Fort relation,
          int argPosition,
          Fort argNIsa)
          throws CycConnectionException, CycApiException {
    // (#$argIsa relation argPosition argNIsa)
    CycArrayList sentence = new CycArrayList();
//    sentence.add(getKnownConstantByGuid_inner("bee22d3d-9c29-11b1-9dad-c379636f7270"));
    sentence.add(CommonConstants.ARG_ISA);
    sentence.add(relation);
    sentence.add(argPosition);
    sentence.add(argNIsa);
    assertGaf(sentence, CommonConstants.UNIVERSAL_VOCABULARY_MT);
  }

  /**
   * Assert an argument one genls contraint for the given relation. The operation will be added to
 the KB transcript for replication and archive.
   *
   * @param relation the given relation
   * @param argGenl the argument constraint
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertArg1Genl(Fort relation,
          Fort argGenl)
          throws CycConnectionException, CycApiException {
    // (#$arg1Genl relation argGenl)
    CycArrayList sentence = new CycArrayList();
    //sentence.add(getKnownConstantByGuid_inner("bd588b1d-9c29-11b1-9dad-c379636f7270"));
    sentence.add(CommonConstants.ARG_1_GENL);
    sentence.add(relation);
    sentence.add(argGenl);
    assertGaf(sentence,
            CommonConstants.UNIVERSAL_VOCABULARY_MT);
  }

  /**
   * Assert an argument two genls contraint for the given relation. The operation will be added to
 the KB transcript for replication and archive.
   *
   * @param relation the given relation
   * @param argGenl the argument constraint
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertArg2Genl(Fort relation,
          Fort argGenl)
          throws CycConnectionException, CycApiException {
    // (#$arg2Genl relation argGenl)
    CycArrayList sentence = new CycArrayList();
    //sentence.add(getKnownConstantByGuid_inner("bd58dcda-9c29-11b1-9dad-c379636f7270"));
    sentence.add(CommonConstants.ARG_2_GENL);
    sentence.add(relation);
    sentence.add(argGenl);
    assertGaf(sentence,
            CommonConstants.UNIVERSAL_VOCABULARY_MT);
  }

  /**
   * Assert an argument three genls contraint for the given relation. The operation will be added
 to the KB transcript for replication and archive.
   *
   * @param relation the given relation
   * @param argGenl the argument constraint
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertArg3Genl(Fort relation,
          Fort argGenl)
          throws CycConnectionException, CycApiException {
    // (#$arg3Genl relation argGenl)
    CycArrayList sentence = new CycArrayList();
    //sentence.add(getKnownConstantByGuid_inner("bd58b8c3-9c29-11b1-9dad-c379636f7270"));
    sentence.add(CommonConstants.ARG_3_GENL);
    sentence.add(relation);
    sentence.add(argGenl);
    assertGaf(sentence,
            CommonConstants.UNIVERSAL_VOCABULARY_MT);
  }

  /**
   * Assert the isa result contraint for the given denotational function. The operation will be
 added to the KB transcript for replication and archive.
   *
   * @param denotationalFunction the given denotational function
   * @param resultIsa the function's isa result constraint
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertResultIsa(Fort denotationalFunction,
          Fort resultIsa)
          throws CycConnectionException, CycApiException {
    // (#$resultIsa denotationalFunction resultIsa)
    assertGaf(CommonConstants.UNIVERSAL_VOCABULARY_MT,
    //        getKnownConstantByGuid_inner("bd5880f1-9c29-11b1-9dad-c379636f7270"),
            CommonConstants.RESULT_ISA,
            denotationalFunction,
            resultIsa);
  }

  /**
   * Assert the genls result contraint for the given denotational function. The operation will be
 added to the KB transcript for replication and archive.
   *
   * @param denotationalFunction the given denotational function
   * @param resultGenl the function's genls result constraint
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertResultGenl(Fort denotationalFunction,
          Fort resultGenl)
          throws CycConnectionException, CycApiException {
    // (#$resultGenl denotationalFunction resultGenls)
    assertGaf(CommonConstants.UNIVERSAL_VOCABULARY_MT,
//            getKnownConstantByGuid_inner("bd58d6ab-9c29-11b1-9dad-c379636f7270"),
            CommonConstants.RESULT_GENL,
            denotationalFunction,
            resultGenl);
  }
  
  /**
   * Asserts the given sentence, and then places it on the transcript queue.
   *
   * @param sentence the given sentence for assertion
   * @param mt the microtheory in which the assertion is placed
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertWithTranscript(CycList sentence,
          CycObject mt)
          throws CycConnectionException, CycApiException {
    assertWithTranscript(sentence.stringApiValue(), mt);
  }

  /**
   * Asserts the given sentence, and then places it on the transcript queue.
   *
   * @param sentence the given sentence for assertion
   * @param mt the microtheory in which the assertion is placed
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertWithTranscript(String sentence,
          CycObject mt)
          throws CycConnectionException, CycApiException {
    assertSentence(sentence, makeELMt_inner(mt), null, null, false, true);
  }

  /**
   * Asserts the given sentence with bookkeeping, and then places it on the transcript queue.
   *
   * @param sentence the given sentence for assertion
   * @param mt the microtheory in which the assertion is placed
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertWithTranscriptAndBookkeeping(String sentence, CycObject mt)
          throws CycConnectionException, CycApiException {
    assertWithTranscriptAndBookkeeping(getCyc().getObjectTool().makeCycSentence(sentence), mt);
  }

  /**
   * Asserts the given sentence with bookkeeping, and then places it on the transcript queue.
   *
   * @param sentence the given sentence for assertion
   * @param mt the microtheory in which the assertion is placed
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertWithTranscriptAndBookkeeping(FormulaSentence sentence,
          CycObject mt)
          throws CycConnectionException, CycApiException {
    assertWithTranscriptAndBookkeepingInternal(sentence, mt);
  }

  /**
   * Asserts the given sentence with bookkeeping, and then places it on the transcript queue.
   *
   * @param sentence the given sentence for assertion
   * @param mt the microtheory in which the assertion is placed
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertWithTranscriptAndBookkeeping(CycList sentence, CycObject mt)
          throws CycConnectionException, CycApiException {
    assertWithTranscriptAndBookkeepingInternal(sentence, mt);
  }

  /**
   * Asserts the given sentence with bookkeeping and without placing it on the transcript queue.
   *
   * @param sentence the given sentence for assertion
   * @param mt the microtheory in which the assertion is placed
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertWithBookkeepingAndWithoutTranscript(CycList sentence,
          CycObject mt)
          throws CycConnectionException, CycApiException {
    assertWithBookkeepingAndWithoutTranscript(sentence.stringApiValue(), mt);
  }

  /**
   * Asserts the given sentence with bookkeeping and without placing it on the transcript queue.
   *
   * @param sentence the given sentence for assertion
   * @param mt the microtheory in which the assertion is placed
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  public void assertWithBookkeepingAndWithoutTranscript(String sentence,
          CycObject mt)
          throws CycConnectionException,
          CycApiException {
    assertSentence(sentence, makeELMt_inner(mt), null, null, true, false);
  }

  @Override
  public void assertSentence(String sentence, ElMt mt, boolean bookkeeping,
          boolean transcript)
          throws CycConnectionException, CycApiException {
    assertSentence(sentence, mt, null, null, bookkeeping, transcript, (Fort) null);
  }
  
  @Override
  public void assertSentence(String sentence, ElMt mt, String strength, String direction, boolean bookkeeping,
          boolean transcript)
          throws CycConnectionException, CycApiException {
    assertSentence(sentence, mt, strength, direction, bookkeeping, transcript, (Fort) null);
  }
  
  @Override
  public void assertSentence(String sentence, ElMt mt, String strength, String direction, boolean bookkeeping,
          boolean transcript,
          Fort template)
          throws CycConnectionException, CycApiException {
    List<Fort> templates = new ArrayList<Fort>();
    if (template != null) {
      templates.add(template);
    }
    assertSentence(sentence, mt, strength, direction, bookkeeping, transcript, false, templates);
  }

  @Override
  public void assertSentence(String sentence, ElMt mt, boolean bookkeeping,
          boolean transcript,
          List<Fort> templates)
          throws CycConnectionException, CycApiException {
    assertSentence(sentence, mt, null, null, bookkeeping, transcript, false, templates);
  }
  
  @Override
  public void assertSentence(String sentence, ElMt mt, String strength, String direction, boolean bookkeeping,
          boolean transcript, boolean disableWFFChecking,
          List<Fort> templates)
          throws CycConnectionException, CycApiException {
    if (getCurrentTransaction() != null) {
      getCurrentTransaction().noteForAssertion(sentence, mt, bookkeeping,
              transcript, disableWFFChecking, templates);
      return;
    }
    if (strength == null){
    	strength = ":default";
    }
    
    String command = ""; 
    if (transcript) {
    command = "(multiple-value-list (ke-assert-now\n"
            + sentence + "\n" + mt.stringApiValue() + " " + strength + " " + (direction != null ? direction : "") + "))";
    } else {
      command = "(multiple-value-list (cyc-assert\n"
            + sentence + "\n" + mt.stringApiValue() + " '(:strength " + strength + " " + (direction != null ? ":direction " + direction : "") + ")))";
    }
    if (bookkeeping) {
      command = getConverse().wrapBookkeeping(command);
    } else {
      command = getConverse().wrapCyclistAndPurpose(command);
    }
    command = getConverse().wrapForwardInferenceRulesTemplates(command, templates);
    if (disableWFFChecking) {
      command = getConverse().wrapDisableWffChecking(command);
    }
    CycList<Object> results = getConverse().converseList(command);
    boolean statusOk = !results.get(0).equals(CycObjectFactory.nil);
    if (!statusOk) {
      String message = "Assertion sentence: " + sentence + " failed in mt: " + mt.cyclify();
      if (results.size() > 1) {
        message += "\n" + sentence + "\nbecause: \n" + results.get(1);
      }
      throw new CycApiException(message);
    }
  }
  
  /** Asserts that the given term is dependent upon the given independent term. If the latter is
   * killed, then the truth maintenance kills the dependent term.
   *
   * @param dependentTerm the dependent term
   * @param independentTerm the independent term
   * @param mt the assertion microtheory
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   */
  @Override
  public void assertTermDependsOn(final Fort dependentTerm,
          final Fort independentTerm, final Fort mt) throws CycConnectionException, CycApiException {
    // assert (#$termDependsOn <dependentTerm> <independentTerm>) in #$UniversalVocabularyMt
    assertGaf(mt, 
            //getKnownConstantByGuid_inner("bdf02d74-9c29-11b1-9dad-c379636f7270"),
            CommonConstants.TERM_DEPENDS_ON,
            dependentTerm, 
            independentTerm);
  }

  /** Asserts that the given term is defined in the given mt. If the mt is
   * subsequently killed, then the truth maintenance kills the dependent term.
   *
   * @param dependentTerm the dependent term
   * @param mt the defining microtheory
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   */
  @Override
  public void assertDefiningMt(final Fort dependentTerm, final Fort mt) throws CycConnectionException, CycApiException {
    // assert (#$definingMt <dependentTerm> <mt>) in #$BaseKB
    assertGaf(CommonConstants.BASE_KB, 
            //getKnownConstantByGuid_inner("bde5ec9c-9c29-11b1-9dad-c379636f7270"), 
            CommonConstants.DEFINING_MT,
            dependentTerm, mt);
  }
  
    /**
   * Assert an argument contraint for the given relation and argument position. The operation will
 be added to the KB transcript for replication and archive.
   *
   * @param relation the given relation
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertArg1FormatSingleEntry(Fort relation)
          throws CycConnectionException, CycApiException {
    // (#$arg1Format relation SingleEntry)
    assertArgFormat(relation, 1, 
            //getKnownConstantByGuid_inner("bd5880eb-9c29-11b1-9dad-c379636f7270")
            CommonConstants.SINGLE_ENTRY
    );
  }

  /**
   * Assert an argument format contraint for the given relation and argument position. The
 operation will be added to the KB transcript for replication and archive.
   *
   * @param relation the given relation
   * @param argPosition the given argument position
   * @param argNFormat the argument format constraint
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertArgFormat(Fort relation, int argPosition,
          Fort argNFormat)
          throws CycConnectionException, CycApiException {
    // (#$argFormat relation argPosition argNFormat)
    FormulaSentence sentence = CycFormulaSentence.makeCycFormulaSentence(
            //getKnownConstantByGuid_inner("bd8a36e1-9c29-11b1-9dad-c379636f7270"),
            CommonConstants.ARG_FORMAT,
            relation,
            argPosition, argNFormat);
    assertGaf(sentence, CommonConstants.BASE_KB);
  }

  /**
   * Asserts that the given DAML imported term is mapped to the given Cyc term.
   *
   * @param cycTerm the mapped Cyc term
   * @param informationSource the external indexed information source
   * @param externalConcept the external concept within the information source
   * @param mt the assertion microtheory
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the Cyc server returns an error
   */
  @Override
  public void assertSynonymousExternalConcept(String cycTerm,
          String informationSource,
          String externalConcept,
          String mt)
          throws CycConnectionException, CycApiException {
    assertSynonymousExternalConcept(getKnownConstantByName_inner(cycTerm),
            getKnownConstantByName_inner(informationSource),
            externalConcept,
            getKnownConstantByName_inner(mt));
  }

  /**
   * Asserts that the given DAML imported term is mapped to the given Cyc term.
   *
   * @param cycTerm the mapped Cyc term
   * @param informationSource the external indexed information source
   * @param externalConcept the external concept within the information source
   * @param mt the assertion microtheory
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the Cyc server returns an error
   */
  @Override
  public void assertSynonymousExternalConcept(Fort cycTerm,
          Fort informationSource,
          String externalConcept,
          CycObject mt)
          throws CycConnectionException, CycApiException {
    FormulaSentence gaf = CycFormulaSentence.makeCycFormulaSentence(
            //getKnownConstantByGuid_inner("c0e2af4e-9c29-11b1-9dad-c379636f7270"),
            CommonConstants.SYNONYMOUS_EXTERNAL_CONCEPT,
            cycTerm, informationSource, externalConcept);
    assertGaf(gaf, makeELMt_inner(mt));
  }
  
    /**
   * Assert that the specified CycConstantImpl is a COLLECTION in the UniversalVocabularyMt. The
 operation will be added to the KB transcript for replication and archive.
   *
   * @param cycFort the given COLLECTION term
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertIsaCollection(Fort cycFort)
          throws CycConnectionException, CycApiException {
    assertGaf(CommonConstants.UNIVERSAL_VOCABULARY_MT,
            CommonConstants.ISA,
            cycFort,
            CommonConstants.COLLECTION);
  }

  /**
   * Assert that the specified CycConstantImpl is a COLLECTION in the specified defining microtheory
 MT. The operation will be added to the KB transcript for replication and archive.
   *
   * @param cycFort the given COLLECTION term
   * @param mt the assertion microtheory
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertIsaCollection(Fort cycFort,
          CycObject mt)
          throws CycConnectionException, CycApiException {
    ElMt elmt = makeELMt_inner(mt);
    assertGaf(elmt,
            CommonConstants.ISA,
            cycFort,
            CommonConstants.COLLECTION);
  }

  /**
   * Assert that the genlsCollection is a genls of specCollection, in the specified defining
 microtheory MT. The operation will be added to the KB transcript for replication and archive.
   *
   * @param specCollectionName the name of the more specialized COLLECTION
   * @param genlsCollectionName the name of the more generalized COLLECTION
   * @param mtName the assertion microtheory name
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertGenls(String specCollectionName,
          String genlsCollectionName,
          String mtName)
          throws CycConnectionException, CycApiException {
    assertGaf(makeELMt_inner(getKnownConstantByName_inner(mtName)),
            CommonConstants.GENLS,
            getKnownConstantByName_inner(specCollectionName),
            getKnownConstantByName_inner(genlsCollectionName));
  }

  /**
   * Assert that the genlsCollection is a genls of specCollection, in the UniversalVocabularyMt The
 operation will be added to the KB transcript for replication and archive.
   *
   * @param specCollectionName the name of the more specialized COLLECTION
   * @param genlsCollectionName the name of the more generalized COLLECTION
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertGenls(String specCollectionName,
          String genlsCollectionName)
          throws CycConnectionException, CycApiException {
    assertGaf(CommonConstants.UNIVERSAL_VOCABULARY_MT,
            CommonConstants.GENLS,
            getKnownConstantByName_inner(specCollectionName),
            getKnownConstantByName_inner(genlsCollectionName));
  }

  /**
   * Assert that the genlsCollection is a genls of specCollection, in the UniveralVocabularyMt. The
 operation will be added to the KB transcript for replication and archive.
   *
   * @param specCollection the more specialized COLLECTION
   * @param genlsCollection the more generalized COLLECTION
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertGenls(Fort specCollection,
          Fort genlsCollection)
          throws CycConnectionException, CycApiException {
    assertGaf(CommonConstants.UNIVERSAL_VOCABULARY_MT,
            CommonConstants.GENLS,
            specCollection,
            genlsCollection);
  }

  /**
   * Assert that the genlsCollection is a genls of specCollection, in the specified defining
 microtheory MT. The operation will be added to the KB transcript for replication and archive.
   *
   * @param specCollection the more specialized COLLECTION
   * @param genlsCollection the more generalized COLLECTION
   * @param mt the assertion microtheory
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertGenls(Fort specCollection,
          Fort genlsCollection,
          CycObject mt)
          throws CycConnectionException, CycApiException {
    ElMt elmt = makeELMt_inner(mt);
    assertGaf(elmt,
            CommonConstants.GENLS,
            specCollection,
            genlsCollection);
  }

  /**
   * Assert that the more general PREDICATE is a genlPreds of the more specialized PREDICATE,
 asserted in the UniversalVocabularyMt The operation will be added to the KB transcript for
 replication and archive.
   *
   * @param specPredName the name of the more specialized PREDICATE
   * @param genlPredName the name of the more generalized PREDICATE
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertGenlPreds(String specPredName,
          String genlPredName)
          throws CycConnectionException, CycApiException {
    //CycConstant genlPreds = getKnownConstantByGuid_inner(
    //        "bd5b4951-9c29-11b1-9dad-c379636f7270");
    assertGaf(CommonConstants.UNIVERSAL_VOCABULARY_MT,
            CommonConstants.GENL_PREDS,
            getKnownConstantByName_inner(specPredName),
            getKnownConstantByName_inner(genlPredName));
  }

  /**
   * Assert that the more general PREDICATE is a genlPreds of the more specialized PREDICATE,
 asserted in the UniversalVocabularyMt The operation will be added to the KB transcript for
 replication and archive.
   *
   * @param specPred the more specialized PREDICATE
   * @param genlPred the more generalized PREDICATE
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertGenlPreds(Fort specPred,
          Fort genlPred)
          throws CycConnectionException, CycApiException {
    //CycConstant genlPreds = getKnownConstantByGuid_inner(
    //        "bd5b4951-9c29-11b1-9dad-c379636f7270");
    assertGaf(CommonConstants.UNIVERSAL_VOCABULARY_MT,
            CommonConstants.GENL_PREDS,
            specPred,
            genlPred);
  }


  /**
   * Assert that the more general micortheory is a genlMt of the more specialized microtheory,
 asserted in the UniversalVocabularyMt The operation will be added to the KB transcript for
 replication and archive.
   *
   * @param specMtName the name of the more specialized microtheory
   * @param genlsMtName the name of the more generalized microtheory
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertGenlMt(String specMtName,
          String genlsMtName)
          throws CycConnectionException, CycApiException {
    assertGaf(CommonConstants.UNIVERSAL_VOCABULARY_MT,
            CommonConstants.GENL_MT,
            getKnownConstantByName_inner(specMtName),
            getKnownConstantByName_inner(genlsMtName));
  }

  /**
   * Assert that the more general micortheory is a genlMt of the more specialized microtheory,
 asserted in the UniversalVocabularyMt The operation will be added to the KB transcript for
 replication and archive.
   *
   * @param specMt the more specialized microtheory
   * @param genlsMt the more generalized microtheory
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertGenlMt(Fort specMt,
          Fort genlsMt)
          throws CycConnectionException, CycApiException {
    assertGaf(CommonConstants.UNIVERSAL_VOCABULARY_MT, CommonConstants.GENL_MT, specMt, genlsMt);
  }

  /**
   * Assert that the cycFort is a COLLECTION in the UniversalVocabularyMt. The operation will be
 added to the KB transcript for replication and archive.
   *
   * @param cycFortName the COLLECTION element name
   * @param collectionName the COLLECTION name
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertIsa(String cycFortName,
          String collectionName)
          throws CycConnectionException, CycApiException {
    assertGaf(CommonConstants.UNIVERSAL_VOCABULARY_MT,
            CommonConstants.ISA,
            getKnownConstantByName_inner(cycFortName),
            getKnownConstantByName_inner(collectionName));
  }

  /**
   * Assert that the cycFort is a COLLECTION, in the specified defining microtheory MT. The
 operation will be added to the KB transcript for replication and archive.
   *
   * @param cycFortName the COLLECTION element name
   * @param collectionName the COLLECTION name
   * @param mtName the assertion microtheory name
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertIsa(String cycFortName,
          String collectionName,
          String mtName)
          throws CycConnectionException, CycApiException {
    assertGaf(makeELMt_inner(getKnownConstantByName_inner(mtName)),
            CommonConstants.ISA,
            getKnownConstantByName_inner(cycFortName),
            getKnownConstantByName_inner(collectionName));
  }

  /**
   * Assert that the cycFort is a COLLECTION, in the specified defining microtheory MT. The
 operation will be added to the KB transcript for replication and archive.
   *
   * @param cycFort the COLLECTION element
   * @param aCollection the COLLECTION
   * @param mt the assertion microtheory
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertIsa(Fort cycFort,
          Fort aCollection,
          CycObject mt)
          throws CycConnectionException, CycApiException {
    assertGaf(makeELMt_inner(mt),
            CommonConstants.ISA,
            cycFort,
            aCollection);
  }

  /**
   * Assert that the cycFort term itself is a COLLECTION, in the given mt. The operation will be
 added to the KB transcript for replication and archive.
   *
   * @param cycFort the COLLECTION element
   * @param aCollection the COLLECTION
   * @param mt the assertion microtheory
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertQuotedIsa(Fort cycFort, Fort aCollection, CycObject mt)
          throws CycConnectionException, CycApiException {
    assertGaf(makeELMt_inner(mt),
            //getKnownConstantByGuid_inner("055544a2-4371-11d6-8000-00a0c9da2002"),
            CommonConstants.QUOTED_ISA,
            cycFort,
            aCollection);
  }

  /**
   * Assert that the cycFort is a COLLECTION, in the UniversalVocabularyMt. The operation will be
 added to the KB transcript for replication and archive.
   *
   * @param instance the COLLECTION element
   * @param aCollection the COLLECTION
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertIsa(DenotationalTerm instance,
          DenotationalTerm aCollection)
          throws CycConnectionException, CycApiException {
    assertGaf(CommonConstants.UNIVERSAL_VOCABULARY_MT,
            CommonConstants.ISA,
            instance,
            aCollection);
  }

  /**
   * Assert that the specified CycConstantImpl is a #$BinaryPredicate in the specified defining
 microtheory. The operation will be added to the KB transcript for replication and archive.
   *
   * @param cycFort the given term
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertIsaBinaryPredicate(Fort cycFort)
          throws CycConnectionException, CycApiException {
    assertIsa(cycFort,
            CommonConstants.BINARY_PREDICATE,
            CommonConstants.UNIVERSAL_VOCABULARY_MT);
  }

  /**
   * Assert that the specified CycConstantImpl is a #$BinaryPredicate in the specified defining
 microtheory. The operation will be added to the KB transcript for replication and archive.
   *
   * @param cycFort the given term
   * @param mt the defining microtheory
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertIsaBinaryPredicate(Fort cycFort,
          CycObject mt)
          throws CycConnectionException, CycApiException {
    assertIsa(cycFort,
            CommonConstants.BINARY_PREDICATE,
            makeELMt_inner(mt));
  }
  
  /**
   * Assert a nameString for the specified CycConstantImpl in the specified lexical microtheory. The
 operation will be added to the KB transcript for replication and archive.
   *
   * @param cycConstantName the name of the given term
   * @param nameString the given name string for the term
   * @param mtName the name of the microtheory in which the name string is asserted
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertNameString(String cycConstantName,
          String nameString,
          String mtName)
          throws CycConnectionException, CycApiException {
    assertGaf(makeELMt_inner(getKnownConstantByName_inner(mtName)),
            //getKnownConstantByGuid_inner("c0fdf7e8-9c29-11b1-9dad-c379636f7270"),
            CommonConstants.NAME_STRING,
            getKnownConstantByName_inner(cycConstantName),
            nameString);
  }

  /**
   * Assert a comment for the specified CycConstantImpl in the specified microtheory MT. The operation
 will be added to the KB transcript for replication and archive.
   *
   * @param cycConstantName the name of the given term
   * @param comment the comment string
   * @param mtName the name of the microtheory in which the comment is asserted
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertComment(String cycConstantName,
          String comment,
          String mtName)
          throws CycConnectionException, CycApiException {
    assertGaf(makeELMt_inner(getKnownConstantByName_inner(mtName)),
            CommonConstants.COMMENT,
            getKnownConstantByName_inner(cycConstantName),
            comment);
  }

  /**
   * Assert a comment for the specified Fort in the specified microtheory. The operation will be
 added to the KB transcript for replication and archive.
   *
   * @param cycFort the given term
   * @param comment the comment string
   * @param mt the comment assertion microtheory
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertComment(Fort cycFort,
          String comment,
          CycObject mt)
          throws CycConnectionException, CycApiException {
    ElMt elmt = makeELMt_inner(mt);
    assertGaf(elmt,
            CommonConstants.COMMENT,
            cycFort,
            comment);
  }

  /**
   * Assert a name string for the specified Fort in the specified microtheory. The operation
 will be added to the KB transcript for replication and archive.
   *
   * @param cycFort the given term
   * @param nameString the name string
   * @param mt the name string assertion microtheory
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertNameString(Fort cycFort,
          String nameString,
          CycObject mt)
          throws CycConnectionException, CycApiException {
    ElMt elmt = makeELMt_inner(mt);
    assertGaf(elmt,
            //getKnownConstantByGuid_inner("c0fdf7e8-9c29-11b1-9dad-c379636f7270"),
            CommonConstants.NAME_STRING,
            cycFort,
            nameString);
  }

  /**
   * Assert a paraphrase format for the specified Fort in the #$EnglishParaphraseMt. The
 operation will be added to the KB transcript for replication and archive.
   *
   * @param relation the given term
   * @param genFormatString the genFormat string
   * @param genFormatList the genFormat argument substitution sequence
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Deprecated
  public void assertGenFormat(Fort relation,
          String genFormatString,
          CycList genFormatList)
          throws CycConnectionException, CycApiException {
    // (#$genFormat <relation> <genFormatString> <genFormatList>)
    CycArrayList sentence = new CycArrayList();
    //sentence.add(getKnownConstantByGuid_inner("beed06de-9c29-11b1-9dad-c379636f7270"));
    sentence.add(CommonConstants.GEN_FORMAT);
    sentence.add(relation);
    sentence.add(genFormatString);

    if (genFormatList.size() == 0) {
      sentence.add(CycObjectFactory.nil);
    } else {
      sentence.add(genFormatList);
    }

    assertGaf(sentence,
            // #$EnglishParaphraseMt
//            makeELMt_inner(getKnownConstantByGuid_inner(
//            "bda16220-9c29-11b1-9dad-c379636f7270")));
            CommonConstants.ENGLISH_PARAPHRASE_MT);
  }
  
  /**
   * Asserts each of the given list of forts to be instances of the given COLLECTION in the
 UniversalVocabularyMt
   *
   * @param fortNames the list of forts
   * @param collectionName
   *
   * @throws CycConnectionException if a communications error occurs
   * @throws CycApiException if the Cyc server returns an error
   */
  @Override
  public void assertIsas(List fortNames,
          String collectionName)
          throws CycConnectionException, CycApiException {
    List forts = new ArrayList();

    for (int i = 0; i < forts.size(); i++) {
      Object fort = forts.get(i);

      if (fort instanceof String) {
        forts.add(getKnownConstantByName_inner((String) fort));
      } else if (fort instanceof Fort) {
        forts.add(fort);
      } else {
        throw new CycApiException(fort + " is neither String nor CycFort");
      }

      assertIsas(forts,
              getKnownConstantByName_inner(collectionName));
    }
  }

  /**
   * Asserts each of the given list of forts to be instances of the given COLLECTION in the
 UniversalVocabularyMt
   *
   * @param forts the list of forts
   * @param collection
   *
   * @throws CycConnectionException if a communications error occurs
   * @throws CycApiException if the Cyc server returns an error
   */
  @Override
  public void assertIsas(List forts,
          Fort collection)
          throws CycConnectionException, CycApiException {
    for (int i = 0; i < forts.size(); i++) {
      assertIsa((Fort) forts.get(i),
              collection);
    }
  }
  
  /**
   * Asserts a ground atomic formula (gaf) in the specified microtheory MT. The operation will be
 added to the KB transcript for replication and archive.
   *
   * @param mt the microtheory in which the assertion is made
   * @param predicate the binary PREDICATE of the assertion
   * @param arg1 the first argument of the PREDICATE
   * @param arg2 the second argument of the PREDICATE
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertGaf(CycObject mt,
          DenotationalTerm predicate,
          DenotationalTerm arg1,
          DenotationalTerm arg2)
          throws CycConnectionException, CycApiException {
    // (PREDICATE <CycFort> <CycFort>)
    CycArrayList sentence = new CycArrayList();
    sentence.add(predicate);
    sentence.add(arg1);
    sentence.add(arg2);
    assertWithTranscriptAndBookkeeping(sentence, mt);
  }

  /**
   * Asserts a ground atomic formula (gaf) in the specified microtheory MT. The operation will be
 added to the KB transcript for replication and archive.
   *
   * @param mt the microtheory in which the assertion is made
   * @param predicate the binary PREDICATE of the assertion
   * @param arg1 the first argument of the PREDICATE
   * @param arg2 the second argument of the PREDICATE, which is a string
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertGaf(CycObject mt,
          Fort predicate,
          Fort arg1,
          String arg2)
          throws CycConnectionException, CycApiException {
    // (PREDICATE <CycFort> <String>)
    CycArrayList sentence = new CycArrayList();
    sentence.add(predicate);
    sentence.add(arg1);
    sentence.add(arg2);
    assertWithTranscriptAndBookkeeping(sentence, mt);
  }

  /**
   * Asserts a ground atomic formula (gaf) in the specified microtheory MT. The operation will be
 added to the KB transcript for replication and archive.
   *
   * @param mt the microtheory in which the assertion is made
   * @param predicate the binary PREDICATE of the assertion
   * @param arg1 the first argument of the PREDICATE
   * @param arg2 the second argument of the PREDICATE, which is a CycArrayList
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertGaf(CycObject mt,
          CycConstant predicate,
          Fort arg1,
          CycList arg2)
          throws CycConnectionException, CycApiException {
    // (PREDICATE <CycFort> <List>)
    CycArrayList sentence = new CycArrayList();
    sentence.add(predicate);
    sentence.add(arg1);
    sentence.add(arg2);
    assertWithTranscriptAndBookkeeping(sentence, mt);
  }

  /**
   * Asserts a ground atomic formula (gaf) in the specified microtheory MT. The operation will be
 added to the KB transcript for replication and archive.
   *
   * @param mt the microtheory in which the assertion is made
   * @param predicate the binary PREDICATE of the assertion
   * @param arg1 the first argument of the PREDICATE
   * @param arg2 the second argument of the PREDICATE, which is an int
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertGaf(CycObject mt,
          CycConstant predicate,
          Fort arg1,
          int arg2)
          throws CycConnectionException, CycApiException {
    // (PREDICATE <CycFort> <int>)
    assertGaf(mt, predicate, arg1, arg2);
  }

  /**
   * Asserts a ground atomic formula (gaf) in the specified microtheory. The operation will be
 added to the KB transcript for replication and archive.
   *
   * @param mt the microtheory in which the assertion is made
   * @param predicate the binary PREDICATE of the assertion
   * @param arg1 the first argument of the PREDICATE
   * @param arg2 the second argument of the PREDICATE, which is an Integer
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertGaf(CycObject mt,
          Fort predicate,
          Fort arg1,
          Integer arg2)
          throws CycConnectionException, CycApiException {
    // (PREDICATE <CycFort> <int>)
    CycArrayList sentence = new CycArrayList();
    sentence.add(predicate);
    sentence.add(arg1);
    sentence.add(arg2);
    assertWithTranscriptAndBookkeeping(sentence, mt);
  }

  /**
   * Asserts a ground atomic formula (gaf) in the specified microtheory. The operation will be
 added to the KB transcript for replication and archive.
   *
   * @param mt the microtheory in which the assertion is made
   * @param predicate the binary PREDICATE of the assertion
   * @param arg1 the first argument of the PREDICATE
   * @param arg2 the second argument of the PREDICATE, which is a Double
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertGaf(CycObject mt,
          Fort predicate,
          Fort arg1,
          Double arg2)
          throws CycConnectionException, CycApiException {
    // (PREDICATE <CycFort> <int>)
    CycArrayList sentence = new CycArrayList();
    sentence.add(predicate);
    sentence.add(arg1);
    sentence.add(arg2);
    assertWithTranscriptAndBookkeeping(sentence, mt);
  }

  /**
   * Asserts a ground atomic formula (gaf) in the specified microtheory MT. The operation and its
 bookkeeping info will be added to the KB transcript for replication and archive.
   *
   * @param mt the microtheory in which the assertion is made
   * @param predicate the ternary PREDICATE of the assertion
   * @param arg1 the first argument of the PREDICATE
   * @param arg2 the second argument of the PREDICATE
   * @param arg3 the third argument of the PREDICATE
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertGaf(CycObject mt,
          CycConstant predicate,
          Fort arg1,
          Fort arg2,
          Fort arg3)
          throws CycConnectionException, CycApiException {
    // (PREDICATE <CycFort> <CycFort> <CycFort>)
    CycArrayList sentence = new CycArrayList();
    sentence.add(predicate);
    sentence.add(arg1);
    sentence.add(arg2);
    sentence.add(arg3);
    assertWithTranscriptAndBookkeeping(sentence, mt);
  }

  /**
   * Asserts a ground atomic formula (gaf) in the specified microtheory MT. The operation will be
 added to the KB transcript for replication and archive.
   *
   * @param gaf the gaf in the form of a CycArrayList
   * @param mt the microtheory in which the assertion is made
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertGaf(CycList gaf,
          CycObject mt)
          throws CycConnectionException, CycApiException {
    assertWithTranscriptAndBookkeeping(gaf, mt);
  }

  /**
   * Asserts a ground atomic formula (gaf) in the specified microtheory MT. The operation will be
 added to the KB transcript for replication and archive.
   *
   * @param gaf the gaf in the form of a CycFormulaSentence
   * @param mt the microtheory in which the assertion is made
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void assertGaf(FormulaSentence gaf, CycObject mt)
          throws CycConnectionException, CycApiException {
    assertWithTranscriptAndBookkeepingInternal(gaf, mt);
  }
    
  /**
   * Edit the sentence contained in unassertSentence to instead be
   * assertSentence. If the Mt is null, unassertSentence and assertSentence
   * must be contextualized sentences (i.e. the Mt must be specified using
   * #$ist). If unassertSentence is an empty conjunction, this amounts to "assert", while
   * if assertSentence is an empty conjunction, it amounts to unassert.
   *
   * @param unassertSentence
   * @param assertSentence
   * @param mt
   * @param bookkeeping
   * @param transcript
   * @param disableWFFChecking
   * @param templates
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   */
  @Override
  public void edit(String unassertSentence, String assertSentence, ElMt mt,
          boolean bookkeeping, boolean transcript, boolean disableWFFChecking,
          List<Fort> templates) throws CycApiException, CycConnectionException {
    String command = "(multiple-value-list (" + (transcript ? "ke-edit-now" : "cyc-edit") + "\n"
            + unassertSentence + "\n" + assertSentence + "\n" + mt.stringApiValue() + "))";
    if (bookkeeping) {
      command = getConverse().wrapBookkeeping(command);
    } else {
      command = getConverse().wrapCyclistAndPurpose(command);
    }
    command = getConverse().wrapForwardInferenceRulesTemplates(command, templates);
    if (disableWFFChecking) {
      command = getConverse().wrapDisableWffChecking(command);
    }
    CycList<Object> results = getConverse().converseList(command);
    boolean statusOk = !results.get(0).equals(CycObjectFactory.nil);
    if (!statusOk) {
      throw new CycApiException("Edit failure of " + unassertSentence + " to " + assertSentence + " in mt: " + mt.cyclify()
              + "\nbecause: \n" + results.get(1));
    }
  }
  
  /**
   * Finds or creates a new permanent Cyc constant in the KB with the specified name. The operation will be
 added to the KB transcript for replication and archive.
   *
   * @param constantName the name of the constant
   *
   * @return the new constant term
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public CycConstant findOrCreateNewPermanent(String constantName)
          throws CycConnectionException, CycApiException {
    return getCyc().getObjectTool().makeCycConstant(constantName);
  }
  
    /**
   * Finds or creates a new binary PREDICATE term.
   *
   * @param predicateName the name of the binary PREDICATE
   * @param predicateTypeName the type of binary PREDICATE, for example
   * #$TransitiveBinaryPredicate, which when null defaults to #$BinaryPredicate
   * @param comment the comment for the new binary PREDICATE, or null
   * @param arg1IsaName the argument position one type constraint, or null
   * @param arg2IsaName the argument position two type constraint, or null
   * @param arg1FormatName the argument position one format constraint, or null
   * @param arg2FormatName the argument position two format constraint, or null
   * @param genlPredsName the more general binary PREDICATE of which this new PREDICATE is a
   * specialization, that when null defaults to #$conceptuallyRelated
   *
   * @return the new binary PREDICATE term
   *
   * @throws CycConnectionException if a communications error occurs
   * @throws CycApiException if the Cyc server returns an error
   * @deprecated use of genFormat is deprecated.
   */
  @Deprecated
  public CycConstant findOrCreateBinaryPredicate(String predicateName,
          String predicateTypeName,
          String comment,
          String arg1IsaName,
          String arg2IsaName,
          String arg1FormatName,
          String arg2FormatName,
          String genlPredsName)
          throws CycConnectionException, CycApiException {
    return findOrCreateBinaryPredicate(predicateName,
            find_inner(predicateTypeName),
            comment,
            find_inner(arg1IsaName),
            find_inner(arg2IsaName),
            find_inner(arg1FormatName),
            find_inner(arg2FormatName),
            find_inner(genlPredsName));
  }

  /**
   * Finds or creates a new binary PREDICATE term.
   *
   * @param predicateName the name of the binary PREDICATE
   * @param predicateType the type of binary PREDICATE, for example #$TransitiveBinaryPredicate,
   * which when null defaults to #$BinaryPredicate
   * @param comment the comment for the new binary PREDICATE, or null
   * @param arg1Isa the argument position one type constraint, or null
   * @param arg2Isa the argument position two type constraint, or null
   * @param arg1Format the argument position one format constraint, or null
   * @param arg2Format the argument position two format constraint, or null
   * @param genlPreds the more general binary PREDICATE of which this new PREDICATE is a
   * specialization, that when null defaults to #$conceptuallyRelated
   *
   * @return the new binary PREDICATE term
   *
   * @throws CycConnectionException if a communications error occurs
   * @throws CycApiException if the Cyc server returns an error
   */
  public CycConstant findOrCreateBinaryPredicate(String predicateName,
          Fort predicateType,
          String comment,
          Fort arg1Isa,
          Fort arg2Isa,
          Fort arg1Format,
          Fort arg2Format,
          Fort genlPreds)
          throws CycConnectionException, CycApiException {
    CycConstant predicate = findOrCreate_inner(predicateName);

    if (predicateType == null) {
      assertIsa(predicate,
              CommonConstants.BINARY_PREDICATE);
    } else {
      assertIsa(predicate,
              predicateType);
    }

    if (comment != null) {
      assertComment(predicate,
              comment,
              CommonConstants.BASE_KB);
    }

    if (arg1Isa != null) {
      assertArgIsa(predicate,
              1,
              arg1Isa);
    }

    if (arg2Isa != null) {
      assertArgIsa(predicate,
              2,
              arg2Isa);
    }

    if (arg1Format != null) {
      assertArgFormat(predicate,
              1,
              arg1Format);
    }

    if (arg2Format != null) {
      assertArgFormat(predicate,
              2,
              arg2Format);
    }

    if (genlPreds == null) {
      assertGenlPreds(predicate,
              // #$conceptuallyRelated
              //getKnownConstantByGuid_inner("bd58803e-9c29-11b1-9dad-c379636f7270"));
              CommonConstants.CONCEPTUALLY_RELATED);
    } else {
      assertGenlPreds(predicate,
              genlPreds);
    }


    return predicate;
  }

  /* *
   * Creates a new KB subset COLLECTION term.
   *
   * @param constantName the name of the new KB subset COLLECTION
   * @param comment the comment for the new KB subset COLLECTION
   *
   * @return the new KB subset COLLECTION term
   *
   * @throws CycConnectionException if a communications error occurs
   * @throws CycApiException if the Cyc server returns an error
   */
  // Commented out regarding BASEAPI-63 - nwinant, 2014-08-18
  /*
  public CycConstant createKbSubsetCollection(String constantName,
          String comment)
          throws CycConnectionException, CycApiException {
    //CycConstant kbSubsetCollection = getKnownConstantByName_inner(
    //        "KBSubsetCollection");
    CycConstant cycConstant = getConstantByName_inner(constantName);

    if (cycConstant == null) {
      cycConstant = findOrCreateNewPermanent(constantName);
    }

    assertIsa(cycConstant,
            CommonConstants.CYC_KB_SUBSET_COLLECTION);
    assertComment(cycConstant,
            comment,
            CommonConstants.BASE_KB);
    assertGenls(cycConstant,
            CommonConstants.THING);

    //Fort variableOrderCollection = getKnownConstantByGuid_inner(
    //        "36cf85d0-20a1-11d6-8000-0050dab92c2f");
    assertIsa(cycConstant,
//            variableOrderCollection,
            CommonConstants.VARIED_ORDER_COLLECTION,
            CommonConstants.BASE_KB);

    return cycConstant;
  }
  */
  
  /**
   * Finds or creates a new COLLECTION term.
   *
   * @param collectionName the name of the COLLECTION
   * @param comment the comment for the COLLECTION
   * @param commentMtName the name of the microtheory in which the comment is asserted
   * @param isaName the name of the COLLECTION of which the new COLLECTION is an instance
   * @param genlsName the name of the COLLECTION of which the new COLLECTION is a subset
   *
   * @return the new COLLECTION term
   *
   * @throws CycConnectionException if a communications error occurs
   * @throws CycApiException if the Cyc server returns an error
   */
  @Override
  public CycConstant findOrCreateCollection(String collectionName,
          String comment,
          String commentMtName,
          String isaName,
          String genlsName)
          throws CycConnectionException, CycApiException {
    CycConstant cycCollection = findOrCreate_inner(collectionName);
    assertComment(cycCollection,
            comment,
            getKnownConstantByName_inner(commentMtName));
    assertIsa(cycCollection,
            getKnownConstantByName_inner(isaName));
    assertGenls(cycCollection,
            getKnownConstantByName_inner(genlsName));

    return cycCollection;
  }

  /**
   * Finds or creates a new COLLECTION term.
   *
   * @param collectionName the name of the COLLECTION
   * @param comment the comment for the COLLECTION
   * @param commentMt the microtheory in which the comment is asserted
   * @param isa the COLLECTION of which the new COLLECTION is an instance
   * @param genls the COLLECTION of which the new COLLECTION is a subset
   *
   * @return the new COLLECTION term
   *
   * @throws CycConnectionException if a communications error occurs
   * @throws CycApiException if the Cyc server returns an error
   */
  @Override
  public Fort findOrCreateCollection(String collectionName,
          String comment,
          Fort commentMt,
          Fort isa,
          Fort genls)
          throws CycConnectionException, CycApiException {
    return describeCollection(findOrCreate_inner(collectionName),
            comment,
            commentMt,
            isa,
            genls);
  }

  /**
   * Describes a COLLECTION term.
   *
   * @param collection the COLLECTION
   * @param comment the comment for the COLLECTION
   * @param commentMt the microtheory in which the comment is asserted
   * @param isa the COLLECTION of which the new COLLECTION is an instance
   * @param genls the COLLECTION of which the new COLLECTION is a subset
   *
   * @return the new COLLECTION term
   *
   * @throws CycConnectionException if a communications error occurs
   * @throws CycApiException if the Cyc server returns an error
   */
  @Override
  public Fort describeCollection(Fort collection,
          String comment,
          Fort commentMt,
          Fort isa,
          Fort genls)
          throws CycConnectionException, CycApiException {
    assertComment(collection,
            comment,
            commentMt);
    assertIsa(collection,
            isa);
    assertGenls(collection,
            genls);

    return collection;
  }
  
  /**
   * Create a microtheory MT, with a comment, isa MT-TYPE and Fort genlMts. An existing
 microtheory with the same name is killed first, if it exists.
   *
   * @param mtName the name of the microtheory term
   * @param comment the comment for the new microtheory
   * @param isaMt the type of the new microtheory
   * @param genlMts the list of more general microtheories
   *
   * @return the new microtheory term
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public CycConstant createMicrotheory(String mtName,
          String comment,
          Fort isaMt,
          List genlMts)
          throws CycConnectionException, CycApiException {
    CycConstant mt = getConstantByName_inner(mtName);

    if (mt != null) {
      getCyc().getUnassertTool().kill(mt);
    }

    mt = findOrCreateNewPermanent(mtName);
    assertComment(mt,
            comment,
            CommonConstants.BASE_KB);
    assertGaf(CommonConstants.UNIVERSAL_VOCABULARY_MT,
            CommonConstants.ISA,
            mt,
            isaMt);

    Iterator iterator = genlMts.iterator();

    while (true) {
      if (!iterator.hasNext()) {
        break;
      }

      Fort aGenlMt = (Fort) iterator.next();
      assertGaf(CommonConstants.UNIVERSAL_VOCABULARY_MT,
              CommonConstants.GENL_MT,
              mt,
              aGenlMt);
    }

    return mt;
  }
  
  /**
   * Describe a microtheory MT, with a comment, isa MT-TYPE and Fort genlMts.
   *
   * @param mt the microtheory term
   * @param comment the comment for the new microtheory
   * @param isaMt the type of the new microtheory
   * @param genlMts the list of more general microtheories
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public void describeMicrotheory(Fort mt,
          String comment,
          Fort isaMt,
          List genlMts)
          throws CycConnectionException, CycApiException {
    assertComment(mt,
            comment,
            CommonConstants.BASE_KB);
    assertGaf(CommonConstants.UNIVERSAL_VOCABULARY_MT,
            CommonConstants.ISA,
            mt,
            isaMt);
    Iterator iterator = genlMts.iterator();
    while (true) {
      if (!iterator.hasNext()) {
        break;
      }
      final CycArrayList gaf = new CycArrayList(3);
      gaf.add(CommonConstants.GENL_MT);
      gaf.add(mt);
      gaf.add(iterator.next());
      assertGaf(gaf, CommonConstants.UNIVERSAL_VOCABULARY_MT);
    }
  }
  
  /**
   * Create a microtheory MT, with a comment, isa MT-TYPE and Fort genlMts. An existing
 microtheory with the same name is killed first, if it exists.
   *
   * @param mtName the name of the microtheory term
   * @param comment the comment for the new microtheory
   * @param isaMtName the type (as a string) of the new microtheory
   * @param genlMts the list of more general microtheories (as strings)
   *
   * @return the new microtheory term
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  @Override
  public CycConstant createMicrotheory(String mtName,
          String comment,
          String isaMtName,
          List genlMts)
          throws CycConnectionException, CycApiException {
    CycConstant mt = getConstantByName_inner(mtName);

    if (mt != null) {
      getCyc().getUnassertTool().kill(mt);
    }

    mt = findOrCreateNewPermanent(mtName);
    assertComment(mt,
            comment,
            CommonConstants.BASE_KB);
    assertIsa(mtName,
            isaMtName);

    Iterator iterator = genlMts.iterator();

    while (true) {
      if (!iterator.hasNext()) {
        break;
      }

      String genlMtName = (String) iterator.next();
      assertGenlMt(mtName,
              genlMtName);
    }

    return mt;
  }
  
  /**
   * Creates a new Collector microtheory and links it more general mts.
   *
   * @param mtName the name of the new collector microtheory
   * @param comment the comment for the new collector microtheory
   * @param genlMts the list of more general microtheories
   *
   * @return the new microtheory
   *
   * @throws CycConnectionException if a communications error occurs
   * @throws CycApiException if the Cyc server returns an error
   */
  @Override
  public CycConstant createCollectorMt(String mtName,
          String comment,
          List genlMts)
          throws CycConnectionException, CycApiException {
    //CycConstant collectorMt = getKnownConstantByName_inner("CollectorMicrotheory");

    return createMicrotheory(mtName,
            comment,
            CommonConstants.COLLECTOR_MICROTHEORY,
            genlMts);
  }

  /**
   * Creates a new spindle microtheory in the given spindle system.
   *
   * @param spindleMtName the name of the new spindle microtheory
   * @param comment the comment for the new spindle microtheory
   * @param spindleHeadMtName the name of the spindle head microtheory
   * @param spindleCollectorMtName the name of the spindle head microtheory
   *
   * @return the new spindle microtheory in the given spindle system
   *
   * @throws CycConnectionException if a communications error occurs
   * @throws CycApiException if the Cyc server returns an error
   */
  @Override
  public CycConstant createSpindleMt(String spindleMtName,
          String comment,
          String spindleHeadMtName,
          String spindleCollectorMtName)
          throws CycConnectionException, CycApiException {
    return createSpindleMt(spindleMtName,
            comment,
            getKnownConstantByName_inner(spindleHeadMtName),
            getKnownConstantByName_inner(spindleCollectorMtName));
  }

  /**
   * Creates a new spindle microtheory in the given spindle system.
   *
   * @param spindleMtName the name of the new spindle microtheory
   * @param comment the comment for the new spindle microtheory
   * @param spindleHeadMt the spindle head microtheory
   * @param spindleCollectorMt the spindle head microtheory
   *
   * @return the new spindle microtheory in the given spindle system
   *
   * @throws CycConnectionException if a communications error occurs
   * @throws CycApiException if the Cyc server returns an error
   */
  @Override
  public CycConstant createSpindleMt(String spindleMtName,
          String comment,
          Fort spindleHeadMt,
          Fort spindleCollectorMt)
          throws CycConnectionException, CycApiException {
    //CycConstant spindleMt = getKnownConstantByName_inner("SpindleMicrotheory");
    List genlMts = new ArrayList();
    genlMts.add(spindleHeadMt);

    CycConstant mt = this.createMicrotheory(spindleMtName,
            comment,
            CommonConstants.SPINDLE_MICROTHEORY,
            genlMts);
    assertGaf(CommonConstants.UNIVERSAL_VOCABULARY_MT,
            CommonConstants.GENL_MT,
            spindleCollectorMt,
            mt);

    return mt;
  }
  
  /**
   * Finds or creates a new individual term.
   *
   * @param IndividualName the name of the individual term
   * @param comment the comment for the individual
   * @param commentMt the microtheory in which the comment is asserted
   * @param isa the COLLECTION of which the new individual is an instance
   *
   * @return the new individual term
   *
   * @throws CycConnectionException if a communications error occurs
   * @throws CycApiException if the Cyc server returns an error
   */
  @Override
  public Fort findOrCreateIndividual(String IndividualName,
          String comment,
          String commentMt,
          String isa)
          throws CycConnectionException, CycApiException {
    return findOrCreateIndividual(IndividualName,
            comment,
            getKnownConstantByName_inner(commentMt),
            getKnownConstantByName_inner(isa));
  }

  /**
   * Finds or creates a new individual term.
   *
   * @param IndividualName the name of the individual term
   * @param comment the comment for the individual
   * @param commentMt the microtheory in which the comment is asserted
   * @param isa the COLLECTION of which the new individual is an instance
   *
   * @return the new individual term
   *
   * @throws CycConnectionException if a communications error occurs
   * @throws CycApiException if the Cyc server returns an error
   */
  @Override
  public Fort findOrCreateIndividual(String IndividualName,
          String comment,
          DenotationalTerm commentMt,
          DenotationalTerm isa)
          throws CycConnectionException, CycApiException {
    Fort individual = findOrCreate_inner(IndividualName);
    assertComment(individual,
            comment,
            commentMt);
    assertIsa(individual,
            isa);

    return individual;
  }

  /**
   * Finds or creates a new individual-denoting reifiable unary function term.
   *
   * @param unaryFunction the unary function
   * @param comment the comment for the unary function
   * @param commentMt the microtheory in which the comment is asserted
   * @param arg1Isa the kind of objects this unary function takes as its argument
   * @param resultIsa the kind of object represented by this reified term
   *
   * @return the new individual-denoting reifiable unary function term
   *
   * @throws CycConnectionException if a communications error occurs
   * @throws CycApiException if the Cyc server returns an error
   */
  @Override
  public Fort findOrCreateIndivDenotingUnaryFunction(String unaryFunction,
          String comment,
          String commentMt,
          String arg1Isa,
          String resultIsa)
          throws CycConnectionException, CycApiException {
    return describeIndivDenotingUnaryFunction(findOrCreate_inner(
            unaryFunction),
            comment,
            getKnownConstantByName_inner(
            commentMt),
            getKnownConstantByName_inner(
            arg1Isa),
            getKnownConstantByName_inner(
            resultIsa));
  }

  /**
   * Describes an individual-denoting reifiable unary function term.
   *
   * @param unaryFunction the unary function
   * @param comment the comment for the unary function
   * @param commentMt the microtheory in which the comment is asserted
   * @param arg1Isa the kind of objects this unary function takes as its argument
   * @param resultIsa the kind of object represented by this reified term
   *
   * @return the new individual-denoting reifiable unary function term
   *
   * @throws CycConnectionException if a communications error occurs
   * @throws CycApiException if the Cyc server returns an error
   */
  @Override
  public Fort describeIndivDenotingUnaryFunction(Fort unaryFunction,
          String comment,
          Fort commentMt,
          Fort arg1Isa,
          Fort resultIsa)
          throws CycConnectionException, CycApiException {
    assertComment(unaryFunction,
            comment,
            commentMt);


    // (#$isa unaryFunction #$UnaryFunction)
    assertIsa(unaryFunction,
            CommonConstants.UNARY_FUNCTION);
//            getKnownConstantByGuid_inner("bd58af89-9c29-11b1-9dad-c379636f7270"));


    // (#$isa unaryFunction #$ReifiableFunction)
    assertIsa(unaryFunction,
            CommonConstants.REIFIABLE_FUNCTION);
            //getKnownConstantByGuid_inner("bd588002-9c29-11b1-9dad-c379636f7270"));


    // (#$isa unaryFunction #$IndividualDenotingFunction)
    assertIsa(unaryFunction,
            CommonConstants.INDIVIDUAL_DENOTING_FUNCTION);
            //getKnownConstantByGuid_inner("bd58fad9-9c29-11b1-9dad-c379636f7270"));


    // (#$isa unaryFunction #$Function-Denotational)
    assertIsa(unaryFunction,
            CommonConstants.FUNCTION_DENOTATIONAL);
            //getKnownConstantByGuid_inner("bd5c40b0-9c29-11b1-9dad-c379636f7270"));
    assertArgIsa(unaryFunction,
            1,
            arg1Isa);
    assertResultIsa(unaryFunction,
            resultIsa);

    return unaryFunction;
  }

  /**
   * Finds or creates a new COLLECTION-denoting reifiable unary function term.
   *
   * @param unaryFunction the unary function
   * @param comment the comment for the unary function
   * @param commentMt the microtheory in which the comment is asserted
   * @param arg1Isa the isa type constraint for the argument
   * @param arg1GenlName the genls type constraint for the argument if it is a COLLECTION
   * @param resultIsa the isa object represented by this reified term
   * @param resultGenlName the genls object represented by this reified term
   *
   * @return the new COLLECTION-denoting reifiable unary function term
   *
   * @throws CycConnectionException if a communications error occurs
   * @throws CycApiException if the Cyc server returns an error
   */
  @Override
  public Fort findOrCreateCollectionDenotingUnaryFunction(String unaryFunction,
          String comment,
          String commentMt,
          String arg1Isa,
          String arg1GenlName,
          String resultIsa,
          String resultGenlName)
          throws CycConnectionException, CycApiException {
    Fort arg1Genl;

    if (arg1GenlName != null) {
      arg1Genl = getKnownConstantByName_inner(arg1GenlName);
    } else {
      arg1Genl = null;
    }

    Fort resultGenl;

    if (resultGenlName != null) {
      resultGenl = getKnownConstantByName_inner(resultGenlName);
    } else {
      resultGenl = null;
    }

    return describeCollectionDenotingUnaryFunction(findOrCreate_inner(
            unaryFunction),
            comment,
            getKnownConstantByName_inner(
            commentMt),
            getKnownConstantByName_inner(
            arg1Isa),
            arg1Genl,
            getKnownConstantByName_inner(
            resultIsa),
            resultGenl);
  }

  /**
   * Describes a new COLLECTION-denoting reifiable unary function term.
   *
   * @param unaryFunction the unary function
   * @param comment the comment for the unary function
   * @param commentMt the microtheory in which the comment is asserted
   * @param arg1Isa the isa type constraint for the argument
   * @param arg1Genl the genls type constraint for the argument if it is a COLLECTION
   * @param resultIsa the isa object represented by this reified term
   * @param resultGenl the genls object represented by this reified term
   *
   * @return the new COLLECTION-denoting reifiable unary function term
   *
   * @throws CycConnectionException if a communications error occurs
   * @throws CycApiException if the Cyc server returns an error
   */
  @Override
  public Fort describeCollectionDenotingUnaryFunction(Fort unaryFunction,
          String comment,
          Fort commentMt,
          Fort arg1Isa,
          Fort arg1Genl,
          Fort resultIsa,
          Fort resultGenl)
          throws CycConnectionException, CycApiException {
    assertComment(unaryFunction,
            comment,
            commentMt);


    // (#$isa unaryFunction #$UnaryFunction)
    assertIsa(unaryFunction,
            CommonConstants.UNARY_FUNCTION);
            //getKnownConstantByGuid_inner("bd58af89-9c29-11b1-9dad-c379636f7270"));


    // (#$isa unaryFunction #$ReifiableFunction)
    assertIsa(unaryFunction,
            CommonConstants.REIFIABLE_FUNCTION);
            //getKnownConstantByGuid_inner("bd588002-9c29-11b1-9dad-c379636f7270"));


    // (#$isa unaryFunction #$CollectionDenotingFunction)
    assertIsa(unaryFunction,
            CommonConstants.COLLECTION_DENOTING_FUNCTION);
            //getKnownConstantByGuid_inner("bd58806a-9c29-11b1-9dad-c379636f7270"));


    // (#$isa unaryFunction #$Function-Denotational)
    assertIsa(unaryFunction,
            CommonConstants.FUNCTION_DENOTATIONAL);
            //getKnownConstantByGuid_inner("bd5c40b0-9c29-11b1-9dad-c379636f7270"));
    assertArgIsa(unaryFunction,
            1,
            arg1Isa);

    if (arg1Genl != null) {
      assertArg1Genl(unaryFunction,
              arg1Genl);
    }

    assertResultIsa(unaryFunction,
            resultIsa);

    if (resultGenl != null) {
      assertResultGenl(unaryFunction,
              resultGenl);
    }

    return unaryFunction;
  }

  /**
   * Finds or creates a new COLLECTION-denoting reifiable binary function term.
   *
   * @param binaryFunction the binary function
   * @param comment the comment for the binary function
   * @param commentMt the microtheory in which the comment is asserted
   * @param arg1IsaName the COLLECTION of which the new binary function is an instance
   * @param arg2GenlsName the kind of objects this binary function takes as its first argument, or
 null
   * @param arg2IsaName the kind of objects this binary function takes as its second argument, or
 null
   * @param arg1GenlsName the general COLLECTIONs this binary function takes as its first argument,
 or null
   * @param resultIsa the kind of object represented by this reified term
   *
   * @return the new COLLECTION-denoting reifiable binary function term
   *
   * @throws CycConnectionException if a communications error occurs
   * @throws CycApiException if the Cyc server returns an error
   */
  @Override
  public Fort findOrCreateCollectionDenotingBinaryFunction(String binaryFunction,
          String comment,
          String commentMt,
          String arg1IsaName,
          String arg2IsaName,
          String arg1GenlsName,
          String arg2GenlsName,
          String resultIsa)
          throws CycConnectionException, CycApiException {
    Fort arg1Isa = null;
    Fort arg2Isa = null;
    Fort arg1Genls = null;
    Fort arg2Genls = null;

    if (arg1IsaName != null) {
      arg1Isa = getKnownConstantByName_inner(arg1IsaName);
    }

    if (arg2IsaName != null) {
      arg1Isa = getKnownConstantByName_inner(arg2IsaName);
    }

    if (arg1GenlsName != null) {
      arg1Genls = getKnownConstantByName_inner(arg1GenlsName);
    }

    if (arg2GenlsName != null) {
      arg2Genls = getKnownConstantByName_inner(arg2GenlsName);
    }

    return describeCollectionDenotingBinaryFunction(findOrCreate_inner(
            binaryFunction),
            comment,
            getKnownConstantByName_inner(
            commentMt),
            arg1Isa,
            arg2Isa,
            arg1Genls,
            arg2Genls,
            getKnownConstantByName_inner(
            resultIsa));
  }

  /**
   * Describes a COLLECTION-denoting reifiable binary function term.
   *
   * @param binaryFunction the binary function
   * @param comment the comment for the binary function
   * @param commentMt the microtheory in which the comment is asserted
   * @param arg1Isa the kind of objects this binary function takes as its first argument, or null
   * @param arg2Isa the kind of objects this binary function takes as its first argument, or null
   * @param arg1Genls the general COLLECTIONs this binary function takes as its first argument, or
 null
   * @param arg2Genls the general COLLECTIONs this binary function takes as its second argument, or
 null
   * @param resultIsa the kind of object represented by this reified term
   *
   * @return the new COLLECTION-denoting reifiable binary function term
   *
   * @throws CycConnectionException if a communications error occurs
   * @throws CycApiException if the Cyc server returns an error
   */
  @Override
  public Fort describeCollectionDenotingBinaryFunction(Fort binaryFunction,
          String comment,
          Fort commentMt,
          Fort arg1Isa,
          Fort arg2Isa,
          Fort arg1Genls,
          Fort arg2Genls,
          Fort resultIsa)
          throws CycConnectionException, CycApiException {
    assertComment(binaryFunction,
            comment,
            commentMt);


    // (#$isa binaryFunction #$BinaryFunction)
    assertIsa(binaryFunction,
            CommonConstants.BINARY_FUNCTION);
            //getKnownConstantByGuid_inner("c0e7247c-9c29-11b1-9dad-c379636f7270"));


    // (#$isa binaryFunction #$ReifiableFunction)
    assertIsa(binaryFunction,
            CommonConstants.REIFIABLE_FUNCTION);
            //getKnownConstantByGuid_inner("bd588002-9c29-11b1-9dad-c379636f7270"));


    // (#$isa binaryFunction #$CollectionDenotingFunction)
    assertIsa(binaryFunction,
            CommonConstants.COLLECTION_DENOTING_FUNCTION);
            //getKnownConstantByGuid_inner("bd58806a-9c29-11b1-9dad-c379636f7270"));


    // (#$isa binaryFunction #$Function-Denotational)
    assertIsa(binaryFunction,
            CommonConstants.FUNCTION_DENOTATIONAL);
            //getKnownConstantByGuid_inner("bd5c40b0-9c29-11b1-9dad-c379636f7270"));

    if (arg1Isa != null) {
      assertArgIsa(binaryFunction,
              1,
              arg1Isa);
    }

    if (arg2Isa != null) {
      assertArgIsa(binaryFunction,
              2,
              arg2Isa);
    }

    if (arg1Genls != null) {
      assertArg1Genl(binaryFunction,
              arg1Genls);
    }

    if (arg2Genls != null) {
      assertArg2Genl(binaryFunction,
              arg2Genls);
    }

    assertResultIsa(binaryFunction,
            resultIsa);

    return binaryFunction;
  }
  
    /**
   * Ensures that the given term meets the given isa and genl wff constraints in the
 UniversalVocabularyMt.
   *
   * @param cycFort the given term
   * @param isaConstraintName the given isa type constraint, or null
   * @param genlsConstraintName the given genls type constraint, or null
   *
   * @throws IOException if a communications error occurs
   * @throws UnknownHostException if the Cyc server cannot be found
   * @throws CycApiException if the Cyc server returns an error
   */
  @Override
  public void ensureWffConstraints(String cycFort,
          String isaConstraintName,
          String genlsConstraintName)
          throws CycConnectionException, CycApiException {
    CycConstant cycConstant = getCyc().getLookupTool().find(cycFort);
    CycConstant isaConstraint = null;
    CycConstant genlsConstraint = null;

    if (isaConstraintName != null) {
      isaConstraint = getCyc().getLookupTool().find(isaConstraintName);
    }

    if (genlsConstraintName != null) {
      genlsConstraint = getCyc().getLookupTool().find(genlsConstraintName);
    }

    ensureWffConstraints(cycConstant, isaConstraint, genlsConstraint);
  }

  /**
   * Ensures that the given term meets the given isa and genl wff constraints in the
 UniversalVocabularyMt.
   *
   * @param cycFort the given term
   * @param isaConstraint the given isa type constraint, or null
   * @param genlsConstraint the given genls type constraint, or null
   *
   * @throws IOException if a communications error occurs
   * @throws UnknownHostException if the Cyc server cannot be found
   * @throws CycApiException if the Cyc server returns an error
   */
  @Override
  public void ensureWffConstraints(Fort cycFort, Fort isaConstraint,
          Fort genlsConstraint)
          throws CycConnectionException, CycApiException {
    if ((isaConstraint != null)
            && (!getCyc().getInspectorTool().isa(cycFort, isaConstraint, CommonConstants.UNIVERSAL_VOCABULARY_MT))) {
      assertIsa(cycFort, isaConstraint);
    }

    if ((genlsConstraint != null)
            && (!getCyc().getInspectorTool().isSpecOf(cycFort, genlsConstraint, CommonConstants.UNIVERSAL_VOCABULARY_MT))) {
      assertGenls(cycFort, genlsConstraint);
    }
  }
  
  /**
   * Merge assertions of KILL-ForT onto KEEP-ForT and kill KILL-ForT.
   * 
   * @return 0 boolean ;; t if success, o/w nil
   * @return 1 list ;; error list of form (ERRor-TYPE ERRor-STRING) otherwise.
   * @param killFort the fort to kill
   * @param keepFort the fort to keep
   * @note Assumes cyclist is ok.
   * @note The salient property of this function is that it never throws an error.
   * @owner jantos
   * @privacy done
   */
  @Override
  public synchronized boolean merge(Fort killFort, Fort keepFort)
          throws CycConnectionException, CycApiException {
    String command = "(ke-merge-now " + killFort.stringApiValue() + " " + keepFort.stringApiValue() + ")";
    Object[] response = {null, null};
    response = converse_inner(command);
    if (response[0].equals(Boolean.TRUE)) {
      if (response[1].equals(CycObjectFactory.nil)) {
        return false;
      } else {
        return true;
      }
    } else {
      throw new CycApiException("Failed to evaluate " + command + "\n  " + response);
    }
  }
  
  
  // Private
  
  /**
   * Asserts the given sentence with bookkeeping, and then places it on the transcript queue.
   *
   * @param sentence the given sentence for assertion
   * @param mt the microtheory in which the assertion is placed
   *
   * @throws CycConnectionException if cyc server host not found on the network or a data communication error occurs
   * @throws CycApiException if the api request results in a cyc server error
   */
  private void assertWithTranscriptAndBookkeepingInternal(CycObject sentence,
          CycObject mt)
          throws CycConnectionException, CycApiException {
    assertSentence(sentence.stringApiValue(), makeELMt_inner(mt), null, null, true, true);
  }
}
