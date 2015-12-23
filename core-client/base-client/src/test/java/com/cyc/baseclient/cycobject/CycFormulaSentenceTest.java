package com.cyc.baseclient.cycobject;

/*
 * #%L
 * File: CycFormulaSentenceTest.java
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
import com.cyc.base.cycobject.CycConstant;
import com.cyc.base.cycobject.FormulaSentence;
import com.cyc.base.cycobject.Fort;
import com.cyc.base.cycobject.ElMt;
import com.cyc.base.cycobject.CycSentence;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.Nart;
import com.cyc.base.cycobject.NonAtomicTerm;
import com.cyc.base.kbtool.LookupTool;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.junit.*;
import static org.junit.Assert.*;
import static com.cyc.baseclient.CommonConstants.*;
import static com.cyc.baseclient.testing.TestConstants.*;
import static com.cyc.baseclient.testing.TestSentences.*;
import com.cyc.baseclient.testing.TestUtils;
import static com.cyc.baseclient.testing.TestUtils.getCyc;
import static com.cyc.baseclient.cycobject.CycFormulaSentence.*;
import com.cyc.baseclient.inference.params.DefaultInferenceParameters;
import com.cyc.baseclient.testing.TestConstants;
import com.cyc.baseclient.testing.TestSentences;
import static com.cyc.baseclient.testing.TestUtils.assumeNotOpenCyc;
import com.cyc.session.exception.SessionException;
import com.cyc.session.exception.OpenCycUnsupportedFeatureException;

// FIXME: TestSentences - nwinant

/**
 *
 * @author daves
 */
public class CycFormulaSentenceTest {
  
  public CycFormulaSentenceTest() {
  }
  
  @BeforeClass
  public static void setUpClass() throws Exception {
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }

  @Before
  public void setUp() throws CycConnectionException, SessionException {
    TestUtils.ensureTestEnvironmentInitialized();
  }

  @After
  public void tearDown() {
  }

  /**
   * Test of makeCycFormulaSentence method, of class CycFormulaSentence.
   */
  @Test
  public void testMakeCycFormulaSentence() {
    System.out.println("makeCycFormulaSentence");
    makeCycFormulaSentence(ISA, THING, THING);
  }

  /**
   * Test of makeConjunction method, of class CycFormulaSentence.
   */
  @Test
  public void testMakeConjunction_CycFormulaSentenceArr() {
    System.out.println("makeConjunction");
    assertTrue(makeConjunction(isaThingThing, isaThingThing).isConjunction());
  }

  /**
   * Test of makeConjunction method, of class CycFormulaSentence.
   */
  @Test
  public void testMakeConjunction_Iterable() {
    System.out.println("makeConjunction");
    assertTrue(
            makeConjunction(Arrays.asList(isaThingThing, isaThingThing)).isConjunction());
  }

  /**
   * Test of makeDisjunction method, of class CycFormulaSentence.
   */
  @Test
  public void testMakeDisjunction() {
    System.out.println("makeDisjunction");
    makeDisjunction(Arrays.asList(isaThingThing, isaThingThing));
  }

  /**
   * Test of makeNegation method, of class CycFormulaSentence.
   */
  @Test
  public void testMakeNegation() {
    System.out.println("makeNegation");
    assertTrue(makeNegation(isaThingThing).isNegated());
  }

  /**
   * Test of convertIfPromising method, of class CycFormulaSentence.
   */
  @Test
  public void testConvertIfPromising() {
    System.out.println("convertIfPromising");
    assertTrue(
            convertIfPromising(CycArrayList.makeCycList(ISA, THING, THING)) instanceof CycFormulaSentence);
  }

  /**
   * Test of isConditionalSentence method, of class CycFormulaSentence.
   */
  @Test
  public void testIsConditionalSentence() {
    System.out.println("isConditionalSentence");
    assertTrue(makeConditional(isaThingThing,
            isaThingThing).isConditionalSentence());
  }

  /**
   * Test of isConjunction method, of class CycFormulaSentence.
   */
  @Test
  public void testIsConjunction() {
    System.out.println("isConjunction");
    assertTrue(makeConjunction(isaThingThing).isConjunction());
  }

  /**
   * Test of isLogicalConnectorSentence method, of class CycFormulaSentence.
   */
  @Test
  public void testIsLogicalConnectorSentence() {
    System.out.println("isLogicalConnectorSentence");
    assertTrue(makeConjunction(isaThingThing).isLogicalConnectorSentence());
  }

  /**
   * Test of isExistential method, of class CycFormulaSentence.
   */
  @Test
  public void testIsExistential() {
    System.out.println("isExistential");
    final FormulaSentence sentence = makeCycFormulaSentence(ISA, X, THING);
    assertFalse(sentence.isExistential());
    sentence.existentiallyBind(X);
    assertTrue(sentence.isExistential());
  }

  /**
   * Test of isUniversal method, of class CycFormulaSentence.
   */
  @Test
  public void testIsUniversal() {
    System.out.println("isUniversal");
    assertFalse(isaThingThing.isUniversal());
  }

  /**
   * Test of getOptimizedVarNames method, of class CycFormulaSentence.
   * @throws com.cyc.base.exception.CycConnectionException
   */
  @Test
  public void testGetOptimizedVarNames() throws CycConnectionException {
    // TODO: could this be re-enabled for OpenCyc? - nwinant, 2015-06-08
    System.out.println("getOptimizedVarNames");
    assumeNotOpenCyc();
    final FormulaSentence sentence = makeCycFormulaSentence(ISA, X,
            COLLECTION);
    Map result = sentence.getOptimizedVarNames(getCyc());
    assertTrue(result.containsKey(X));
  }

  /**
   * Test of getSimplifiedSentence method, of class CycFormulaSentence.
   * @throws com.cyc.base.exception.CycConnectionException
   * @throws com.cyc.session.exception.OpenCycUnsupportedFeatureException
   */
  @Test
  public void testGetSimplifiedSentence_CycAccess() throws CycConnectionException, OpenCycUnsupportedFeatureException {
    System.out.println("getSimplifiedSentence");
    assumeNotOpenCyc();
    final FormulaSentence sentence = makeCycFormulaSentence(ISA, X,
            COLLECTION);
    assertEquals(sentence, makeConjunction(sentence).getSimplifiedSentence(
            getCyc()));
  }

  /**
   * Test of getSimplifiedSentence method, of class CycFormulaSentence.
   * @throws com.cyc.base.exception.CycConnectionException
   * @throws com.cyc.session.exception.OpenCycUnsupportedFeatureException
   */
  @Test
  public void testGetSimplifiedSentence_CycAccess_ELMt() throws CycConnectionException, OpenCycUnsupportedFeatureException {
    System.out.println("getSimplifiedSentence");
    assumeNotOpenCyc();
    final FormulaSentence sentence = makeCycFormulaSentence(ISA, X,
            COLLECTION);
    assertEquals(sentence, makeConjunction(sentence).getSimplifiedSentence(
            getCyc(), BASE_KB));
  }

  /**
   * Test of getNonWffAssertExplanation method, of class CycFormulaSentence.
   * @throws com.cyc.base.exception.CycConnectionException
   */
  @Test
  public void testGetNonWffAssertExplanation_CycAccess() throws CycConnectionException {
    System.out.println("getNonWffAssertExplanation");
    makeCycSentence(getCyc(), genlsWilliamHenryHarrisonBLO_STRING).getNonWffAssertExplanation(
            getCyc());
  }

  /**
   * Test of getNonWffAssertExplanation method, of class CycFormulaSentence.
   */
  @Test
  public void testGetNonWffAssertExplanation_CycAccess_ELMt() throws CycConnectionException {
    System.out.println("getNonWffAssertExplanation");
    makeCycSentence(getCyc(), genlsWilliamHenryHarrisonBLO_STRING).getNonWffAssertExplanation(
            getCyc(), BASE_KB);
  }

  /**
   * Test of getNonWffExplanation method, of class CycFormulaSentence.
   */
  @Test
  public void testGetNonWffExplanation_CycAccess() throws CycConnectionException {
    System.out.println("getNonWffExplanation");
    makeCycSentence(getCyc(), genlsWilliamHenryHarrisonBLO_STRING).getNonWffExplanation(
            getCyc());
  }

  /**
   * Test of getNonWffExplanation method, of class CycFormulaSentence.
   */
  @Test
  public void testGetNonWffExplanation_CycAccess_ELMt() throws CycApiException, CycConnectionException {
    System.out.println("getNonWffExplanation");
    makeCycSentence(getCyc(), genlsWilliamHenryHarrisonBLO_STRING).getNonWffExplanation(
            getCyc(), BASE_KB);
  }

  /**
   * Test of deepCopy method, of class CycFormulaSentence.
   */
  @Test
  public void testDeepCopy() {
    System.out.println("deepCopy");
    final FormulaSentence sentence = makeCycFormulaSentence(ISA, X,
            COLLECTION);
    assertEquals(sentence.deepCopy(), sentence);
  }

  /**
   * Test of substituteNonDestructive method, of class CycFormulaSentence.
   */
  @Test
  public void testSubstituteNonDestructive() {
    System.out.println("substituteNonDestructive");
    final FormulaSentence sentence = makeCycFormulaSentence(ISA, X,
            COLLECTION);
    assertEquals(makeCycFormulaSentence(ISA, THING,
            COLLECTION), sentence.substituteNonDestructive(X,
                    THING));
    assertEquals(makeCycFormulaSentence(ISA, X,
            COLLECTION), sentence);
  }

  /**
   * Test of substituteDestructive method, of class CycFormulaSentence.
   */
  @Test
  public void testSubstituteDestructive() {
    System.out.println("substituteDestructive");
    final FormulaSentence sentence = makeCycFormulaSentence(ISA, X,
            COLLECTION);
    sentence.substituteDestructive(X, THING);
    assertEquals(makeCycFormulaSentence(ISA, THING,
            COLLECTION), sentence);
  }

  /**
   * Test of treeSubstitute method, of class CycFormulaSentence.
   */
  @Test
  public void testTreeSubstitute() throws CycConnectionException {
    System.out.println("treeSubstitute");
    final CycAccess cyc = getCyc();
    final LookupTool lookupTool = cyc.getLookupTool();
    Fort theDog = lookupTool.getKnownFortByName(TestSentences.THE_DOG_STRING);
    Map<CycObject, Object> substitutions = new HashMap<CycObject, Object>();

    substitutions.put(theDog, X);

    FormulaSentence sentence = cyc.getObjectTool().makeCyclifiedSentence(
            TestSentences.ISA_THE_DOG_DOG_STRING);

    FormulaSentence result = sentence.treeSubstitute(cyc, substitutions);
    assertNotEquals(result, sentence);

    System.out.println("...Verifying substitution into a random NART.");
    Nart nart = lookupTool.getRandomNart();
    while (nart.getArity() < 1 || !(nart.getArgument(1) instanceof CycObject)) {
      nart = lookupTool.getRandomNart();
    }
    sentence.setSpecifiedObject(ArgPositionImpl.ARG1, nart);
    assertTrue(sentence.getArg1() instanceof Nart);
    substitutions.clear();
    CycObject natArg1 = (CycObject) nart.getArgument(1);
    CycConstant cat = lookupTool.getKnownConstantByName(TestConstants.CAT.cyclify());
    substitutions.put(natArg1, cat);
    System.out.println("...Substituting " + cat + " for " + natArg1 + " in " + sentence);
    result = sentence.treeSubstitute(cyc, substitutions);
    System.out.println("...Result: " + result);
    assertTrue(cat.equalsAtEL(((NonAtomicTerm)result.getArg1()).getArgument(1)));
  }

  /**
   * Test of clone method, of class CycFormulaSentence.
   */
  //@Test
  public void testClone() {
    System.out.println("clone");
    CycFormulaSentence instance = null;
    Object expResult = null;
    Object result = instance.clone();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of compareTo method, of class CycFormulaSentence.
   */
  //@Test
  public void testCompareTo() {
    System.out.println("compareTo");
    Object o = null;
    CycFormulaSentence instance = null;
    int expResult = 0;
    int result = instance.compareTo(o);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of makeCycSentence method, of class CycFormulaSentence.
   */
  @Test
  public void testMakeCycSentence() throws CycConnectionException {
    System.out.println("makeCycSentence");
    final String isaThingThing = TestSentences.ISA_THING_THING_STRING;
    assertEquals(getCyc().getObjectTool().makeCycSentence(isaThingThing),
            makeCycSentence(getCyc(), isaThingThing));
  }

  /**
   * Test of isNegated method, of class CycFormulaSentence.
   */
  //@Test
  public void testIsNegated() {
    System.out.println("isNegated");
    CycFormulaSentence instance = null;
    boolean expResult = false;
    boolean result = instance.isNegated();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of existentiallyBind method, of class CycFormulaSentence.
   */
  //@Test
  public void testExistentiallyBind() {
    System.out.println("existentiallyBind");
    CycVariableImpl var = null;
    CycFormulaSentence instance = null;
    instance.existentiallyBind(var);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of existentiallyUnbind method, of class CycFormulaSentence.
   */
  //@Test
  public void testExistentiallyUnbind() {
    System.out.println("existentiallyUnbind");
    CycVariableImpl var = null;
    CycFormulaSentence instance = null;
    instance.existentiallyUnbind(var);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of splice method, of class CycFormulaSentence.
   */
  @Test
  public void testSplice() throws CycConnectionException, OpenCycUnsupportedFeatureException {
    System.out.println("splice");
    assumeNotOpenCyc();
    final FormulaSentence conjunction = CycFormulaSentence.makeConjunction(
            isaThingThing);
    final FormulaSentence toInsert = getCyc().getObjectTool().makeCycSentence(
            isaWilliamHenryHarrisonBLO_STRING);
    final FormulaSentence result = conjunction.splice(
            toInsert, ArgPositionImpl.ARG1, getCyc());
    assertTrue(result.treeContains(isaThingThing));
    assertTrue(result.treeContains(toInsert));
  }

  /**
   * Test of getCandidateReplacements method, of class CycFormulaSentence.
   * @throws com.cyc.base.exception.CycConnectionException
   * @throws com.cyc.session.exception.OpenCycUnsupportedFeatureException
   */
  @Test
  public void testGetCandidateReplacements() throws CycConnectionException, OpenCycUnsupportedFeatureException {
    System.out.println("getCandidateReplacements");
    assumeNotOpenCyc();
    final CycConstant universeDataMt = getCyc().getLookupTool().getKnownConstantByName(
            UNIVERSE_DATA_MT);
    final FormulaSentence suggestionSentence
            = getCyc().getObjectTool().makeCycSentence(
                    "(" + SUGGESTION_FOR_PRED_RELATIVE_TO_ISA_IN_ARG + " " + WEIGHT_ON_PLANET
                    + " " + CELESTIAL_BODY + " 2 " + CELESTIAL_BODY + " 2)");
    final boolean suggestionKnown = getCyc().getInferenceTool().isQueryTrue(
            suggestionSentence, universeDataMt,
            new DefaultInferenceParameters(
                    getCyc()));
    if (!suggestionKnown) {
      getCyc().getAssertTool().assertGaf(suggestionSentence, universeDataMt);
    }
    try {
      Collection result = getCyc().getObjectTool().makeCycSentence(
              "(" + WEIGHT_ON_PLANET + " ?ME " + PLANET_MARS + ")").getCandidateReplacements(
                      ArgPositionImpl.ARG2,
                      ElMtConstant.makeELMtConstant(universeDataMt), getCyc());
      assertFalse(result.isEmpty());
      assertTrue(
              result.contains(getCyc().getLookupTool().getKnownConstantByName(PLANET_VENUS)));
    } finally {
      if (!suggestionKnown) {
        getCyc().getUnassertTool().unassertGaf(suggestionSentence, universeDataMt);
      }
    }
  }
  
  /**
   * Test of isValidReplacement method, of class CycFormulaSentence.
   */
  @Test
  public void testIsValidReplacement() throws CycConnectionException {
    System.out.println("isValidReplacement");
    Object isa = isaThingThing.getArg0();
    assertFalse(isaThingThing.isValidReplacement(ArgPositionImpl.ARG2, isa,
            BASE_KB, getCyc()));
    assertTrue(isaThingThing.isValidReplacement(ArgPositionImpl.ARG1, isa,
            BASE_KB, getCyc()));
  }

  /**
   * Test of getEqualsFoldedSentence method, of class CycFormulaSentence.
   */
  //@Test
  public void testGetEqualsFoldedSentence_CycAccess() throws CycConnectionException {
    System.out.println("getEqualsFoldedSentence");
    CycAccess access = null;
    CycFormulaSentence instance = null;
    CycSentence expResult = null;
    CycSentence result = instance.getEqualsFoldedSentence(access);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getEqualsFoldedSentence method, of class CycFormulaSentence.
   */
  //@Test
  public void testGetEqualsFoldedSentence_CycAccess_ELMt() throws CycConnectionException {
    System.out.println("getEqualsFoldedSentence");
    CycAccess access = null;
    ElMt mt = null;
    CycFormulaSentence instance = null;
    CycSentence expResult = null;
    CycSentence result = instance.getEqualsFoldedSentence(access, mt);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getExpandedSentence method, of class CycFormulaSentence.
   */
  //@Test
  public void testGetExpandedSentence_CycAccess() throws CycConnectionException {
    System.out.println("getExpandedSentence");
    CycAccess access = null;
    CycFormulaSentence instance = null;
    CycFormulaSentence expResult = null;
    CycFormulaSentence result = instance.getExpandedSentence(access);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getExpandedSentence method, of class CycFormulaSentence.
   */
  //@Test
  public void testGetExpandedSentence_CycAccess_ELMt() throws CycConnectionException {
    System.out.println("getExpandedSentence");
    CycAccess access = null;
    ElMt mt = null;
    CycFormulaSentence instance = null;
    CycFormulaSentence expResult = null;
    CycFormulaSentence result = instance.getExpandedSentence(access, mt);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getCanonicalElSentence method, of class CycFormulaSentence.
   */
  //@Test
  public void testGetCanonicalElSentence_CycAccess() throws CycConnectionException {
    System.out.println("getCanonicalElSentence");
    CycAccess access = null;
    CycFormulaSentence instance = null;
    CycFormulaSentence expResult = null;
    CycFormulaSentence result = instance.getCanonicalElSentence(access);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getCanonicalElSentence method, of class CycFormulaSentence.
   */
  //@Test
  public void testGetCanonicalElSentence_CycAccess_Boolean() throws CycConnectionException {
    System.out.println("getCanonicalElSentence");
    CycAccess access = null;
    Boolean canonicalizeVars = null;
    CycFormulaSentence instance = null;
    CycFormulaSentence expResult = null;
    CycFormulaSentence result = instance.getCanonicalElSentence(access,
            canonicalizeVars);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getCanonicalElSentence method, of class CycFormulaSentence.
   */
  //@Test
  public void testGetCanonicalElSentence_3args() throws CycConnectionException {
    System.out.println("getCanonicalElSentence");
    CycAccess access = null;
    ElMt mt = null;
    Boolean canonicalizeVars = null;
    CycFormulaSentence instance = null;
    CycFormulaSentence expResult = null;
    CycFormulaSentence result = instance.getCanonicalElSentence(access, mt,
            canonicalizeVars);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of hasWffConstraintViolations method, of class CycFormulaSentence.
   */
  //@Test
  public void testHasWffConstraintViolations() {
    System.out.println("hasWffConstraintViolations");
    CycAccess access = null;
    ElMt mt = null;
    CycFormulaSentence instance = null;
    boolean expResult = false;
    boolean result = instance.hasWffConstraintViolations(access, mt);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
  
  @Test
  public void testHashCode() throws CycConnectionException {
    CycAccess access = TestUtils.getCyc();
    String str1 = "(moleculeStateToState-OneWay (TheList (ProteinMoleculeTypeStateFn RasProtein (BindingSiteWithNameAndPStateFn \"S1S2\" UnspecifiedWRTPhosphorylation-Site 1)) (ProteinMoleculeTypeStateFn RafKinase (BindingSiteWithNameAndPStateFn \"x\" PhosphorylatedSite 1))) (TheList (ProteinMoleculeTypeStateFn RasProtein (BindingSiteWithNameAndPStateFn \"S1S2\" UnspecifiedWRTPhosphorylation-Site 1)) (ProteinMoleculeTypeStateFn RafKinase (BindingSiteWithNameAndPStateFn \"x\" UnphosphorylatedSite 1))))";
    String str2 = "(moleculeStateToState-OneWay (TheList (ProteinMoleculeTypeStateFn RasProtein (BindingSiteWithNameAndPStateFn \"S1S2\" UnspecifiedWRTPhosphorylation-Site 1)) (ProteinMoleculeTypeStateFn RafKinase (BindingSiteWithNameAndPStateFn \"x\" UnphosphorylatedSite 1))) (TheList (ProteinMoleculeTypeStateFn RasProtein (BindingSiteWithNameAndPStateFn \"S1S2\" UnspecifiedWRTPhosphorylation-Site 1)) (ProteinMoleculeTypeStateFn RafKinase (BindingSiteWithNameAndPStateFn \"x\" PhosphorylatedSite 1))))";
    FormulaSentence f1 = CycFormulaSentence.makeCycSentence(access, str1);
    FormulaSentence f2 = CycFormulaSentence.makeCycSentence(access, str2);
    /*
    System.out.println("f1 arg0: " + f1.getArg0().getClass().getName() + " ... " + f1.getArg0() + ": " + f1.getArg0().hashCode());
    System.out.println("f1 arg1: " + f1.getArg1().getClass().getName() + " ... " + f1.getArg1() + ": " + f1.getArg1().hashCode());
    System.out.println("f1 arg2: " + f1.getArg2().getClass().getName() + " ... " + f1.getArg2() + ": " + f1.getArg2().hashCode());
    System.out.println("f2 arg0: " + f2.getArg0().getClass().getName() + " ... " + f2.getArg0() + ": " + f2.getArg0().hashCode());
    System.out.println("f2 arg1: " + f2.getArg1().getClass().getName() + " ... " + f2.getArg1() + ": " + f2.getArg1().hashCode());
    System.out.println("f2 arg2: " + f2.getArg2().getClass().getName() + " ... " + f2.getArg2() + ": " + f2.getArg2().hashCode());
    System.out.println("f1 hash: " + f1.hashCode() + " ! " + f1.getClass().getName());
    System.out.println("f2 hash: " + f2.hashCode() + " ! " + f2.getClass().getName());
        */
    assertNotEquals(f1.hashCode(), f2.hashCode());
  }
  
  
  // Internal
  
  private static final String UNIVERSE_DATA_MT = TestConstants.UNIVERSE_DATA_MT.cyclify();
  private static final String SUGGESTION_FOR_PRED_RELATIVE_TO_ISA_IN_ARG =
          TestConstants.SUGGESTION_FOR_PRED_RELATIVE_TO_ISA_IN_ARG.cyclify();
  private static final String CELESTIAL_BODY = TestConstants.CELESTIAL_BODY.cyclify();
  private static final String WEIGHT_ON_PLANET = TestConstants.WEIGHT_ON_PLANET.cyclify();
  private static final String PLANET_MARS = TestConstants.PLANET_MARS.cyclify();
  private static final String PLANET_VENUS = TestConstants.PLANET_VENUS.cyclify();
}
