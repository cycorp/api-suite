package com.cyc.baseclient.cycobject;

/*
 * #%L
 * File: CycObjectUnitTest.java
 * Project: Base Client
 * %%
 * Copyright (C) 2013 - 2014 Cycorp, Inc.
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

//// External Imports
import com.cyc.base.CycConnectionException;
import com.cyc.base.cycobject.ArgPosition;
import com.cyc.base.cycobject.CycConstant;
import com.cyc.base.cycobject.CycObject;
import com.cyc.base.cycobject.NonAtomicTerm;
import com.cyc.base.cycobject.DenotationalTerm;
import com.cyc.base.cycobject.Naut;
import com.cyc.base.cycobject.CycVariable;
import java.math.BigInteger;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.*;
import org.junit.Test;
import com.cyc.baseclient.CommonConstants;
import com.cyc.base.CycApiException;
import com.cyc.base.cycobject.FormulaSentence;
import com.cyc.base.cycobject.CycList;
import com.cyc.base.cycobject.Fort;
import com.cyc.base.cycobject.Nart;
import com.cyc.baseclient.CycObjectFactory;
import static com.cyc.baseclient.CycObjectFactory.makeCycVariable;
import com.cyc.baseclient.api.SubLAPIHelper;
import com.cyc.baseclient.testing.TestUtils;
import static com.cyc.baseclient.testing.TestUtils.getCyc;
import static com.cyc.baseclient.cycobject.ArgPositionImpl.*;
import static com.cyc.baseclient.cycobject.CycArrayList.makeCycList;
import static com.cyc.baseclient.CommonConstants.*;
import com.cyc.baseclient.datatype.DateConverter;
import static com.cyc.baseclient.testing.TestConstants.*;
import com.cyc.baseclient.util.CycUtils;
import com.cyc.baseclient.util.MyStreamTokenizer;
import com.cyc.baseclient.datatype.Span;
import com.cyc.baseclient.testing.TestConstants;
import com.cyc.baseclient.testing.TestGuids;
import com.cyc.baseclient.testing.TestSentences;
import com.cyc.baseclient.xml.Marshaller;
import com.cyc.baseclient.xml.XMLStringWriter;
import com.cyc.session.SessionApiException;
import org.junit.Before;

// FIXME: TestSentences - nwinant

/**
 * Provides a suite of JUnit test cases for the <tt>com.cyc.baseclient.cycobject</tt> package.<p>
 *
 * @version $Id: UnitTest.java 135652 2011-08-30 10:24:52Z baxter $
 * @author Stephen L. Reed
 */
public class CycObjectUnitTest {

  private static final boolean SHOULD_SET_CONNECTION_PARAMS_INTERACTIVELY = false;
  private static boolean areConnectionParamsSet = !SHOULD_SET_CONNECTION_PARAMS_INTERACTIVELY;
  
  private static final String and = CommonConstants.AND.stringApiValue();
  private static final String personString = TestConstants.PERSON.stringApiValue();
  private static final String isa = CommonConstants.ISA.stringApiValue();
  private static final String modernMilitaryOrganization = TestConstants.MODERN_MILITARY_ORGANIZATION.cyclify();
  private static final String objectFoundInLocation = TestConstants.OBJECT_FOUND_IN_LOCATION.cyclify();
  private static final String illinoisState = TestConstants.ILLINOIS_STATE.cyclify();
  private static final String behaviorCapable = TestConstants.BEHAVIOR_CAPABLE.cyclify();
  private static final String reactionToSituationTypeFn = TestConstants.REACTION_TO_SITUATION_TYPE_FN.cyclify();
  private static final String chemicalAttack = TestConstants.CHEMICAL_ATTACK.cyclify();
  private static final String performedBy = TestConstants.PERFORMED_BY.cyclify();
  private static final String brazil = TestConstants.BRAZIL.cyclify();
  private static final String likesAsFriend = TestConstants.LIKES_AS_FRIEND.cyclify();
  private static final String juvenileFn = TestConstants.JUVENILE_FN.cyclify();
  private static final String dog = TestConstants.DOG.cyclify();
  private static final String implies = CommonConstants.IMPLIES.cyclify();
  private static final String usDollarFn = TestConstants.DOLLAR.cyclify();
  private static final String unicodeStringFn = CommonConstants.UNICODE_STRING_FN.cyclify();
  private static final String istAsserted = TestConstants.IST_ASSERTED.cyclify();
  private static final String totalInvestmentEarningsForStockTypeBoughtDuring = TestConstants.TOTAL_INVESTMENT_EARNINGS_FOR_STOCK_TYPE_BOUGHT_DURING.cyclify();
  private static final String techStock = TestConstants.TECH_STOCK.cyclify();
  private static final String minusFn = TestConstants.MINUS_FN.cyclify();
  private static final String poundGreatBritain = TestConstants.POUND_GREAT_BRITAIN.cyclify();
  private static final String earlyPartFn = TestConstants.EARLY_PART_FN.cyclify();
  private static final String theMotleyFoolUKCorpusMt = TestConstants.THE_MOTLEY_FOOL_UK_CORPUS_MT.cyclify();
  private static final String yearFn = DateConverter.YEAR_FN.cyclify();
  private static final String instanceNamedFn = TestConstants.INSTANCE_NAMED_FN.cyclify();
    
  
  @Before
  public void setUp() throws CycConnectionException, SessionApiException {
    TestUtils.ensureTestEnvironmentInitialized();
  }

  @Test
  public void testCycList50() throws NumberFormatException, CycApiException, CycConnectionException {  
    // toHTMLPrettyString
    CycArrayList cycList50 = (CycArrayList) getCyc().getObjectTool().makeCycList(
            "(QUOTE (" + and + "(" + isa + " ?UNIT " + modernMilitaryOrganization +
                    ") (" + objectFoundInLocation + " ?UNIT " + illinoisState + 
                    ") (" + behaviorCapable + " ?UNIT (" + reactionToSituationTypeFn + " " + chemicalAttack
                    + ") " + performedBy + ")))");
    assertEquals(
            "<html><body>(QUOTE<br>&nbsp&nbsp(and<br>&nbsp&nbsp&nbsp&nbsp(isa ?UNIT ModernMilitaryOrganization)<br>&nbsp&nbsp&nbsp&nbsp(objectFoundInLocation ?UNIT Illinois-State)<br>&nbsp&nbsp&nbsp&nbsp(behaviorCapable ?UNIT<br>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp(ReactionToSituationTypeFn ChemicalAttack) performedBy)))</body></html>",
            cycList50.toHTMLPrettyString(""));
    // treeContains
    cycList50 = (CycArrayList) getCyc().getObjectTool().makeCycList(
            "(DEFMACRO-IN-API MY-MACRO (A B C) (RET ` (LIST , A , B , C)))");
    assertTrue(cycList50.treeContains(CycObjectFactory.backquote));

    // getValueForKeyword
    cycList50 = (CycArrayList) getCyc().getObjectTool().makeCycList(
            "(fipa-transport-message\n" + "  (envelope\n" + "    :to my-remote-agent\n" + "    :from my-cyc-agent\n" + "    :date 3215361678\n" + "    :X-agent-community :coabs\n" + "    :X-cyc-image-id \"balrog-200111112091457-939\"\n" + "    :X-base-tcp-port 3600)\n" + "  (payload\n" + "    (inform\n" + "      :sender my-cyc-agent\n" + "      :receiver my-remote-agent\n" + "      :reply-to message1\n" + "      :content \"Hello from my-cyc-agent\"\n" + "      :language :cycl\n" + "      :reply-with \"my cookie\"\n" + "      :ontology cyc-api\n" + "      :protocol :fipa-request)))");
    assertEquals(cycList50.size(), 3);
    assertEquals(cycList50.first(), CycObjectFactory.makeCycSymbol(
            "fipa-transport-message"));
    assertTrue(cycList50.second() instanceof CycArrayList);
    CycList envelope = (CycArrayList) cycList50.second();
    assertEquals(CycObjectFactory.makeCycSymbol("my-remote-agent"),
            envelope.getValueForKeyword(CycObjectFactory.makeCycSymbol(":to")));
    assertEquals(CycObjectFactory.makeCycSymbol("my-cyc-agent"),
            envelope.getValueForKeyword(CycObjectFactory.makeCycSymbol(":from")));
    assertEquals(new Long("3215361678"),
            envelope.getValueForKeyword(CycObjectFactory.makeCycSymbol(":date")));
    assertEquals(CycObjectFactory.makeCycSymbol(":coabs"),
            envelope.getValueForKeyword(CycObjectFactory.makeCycSymbol(
            ":X-agent-community")));
    assertEquals("balrog-200111112091457-939",
            envelope.getValueForKeyword(CycObjectFactory.makeCycSymbol(
            ":X-cyc-image-id")));
    assertEquals(Integer.valueOf(3600),
            envelope.getValueForKeyword(CycObjectFactory.makeCycSymbol(
            ":X-base-tcp-port")));
    assertNull(envelope.getValueForKeyword(CycObjectFactory.makeCycSymbol(
            ":not-there")));
    assertTrue(cycList50.third() instanceof CycArrayList);
    assertTrue(cycList50.third() instanceof CycArrayList);
    CycList payload = (CycArrayList) cycList50.third();
    assertTrue(payload.second() instanceof CycArrayList);
    CycList aclList = (CycArrayList) payload.second();
    assertEquals(CycObjectFactory.makeCycSymbol("my-cyc-agent"),
            aclList.getValueForKeyword(CycObjectFactory.makeCycSymbol(":sender")));
    assertEquals(CycObjectFactory.makeCycSymbol("my-remote-agent"),
            aclList.getValueForKeyword(CycObjectFactory.makeCycSymbol(
            ":receiver")));
    assertEquals(CycObjectFactory.makeCycSymbol("message1"),
            aclList.getValueForKeyword(CycObjectFactory.makeCycSymbol(
            ":reply-to")));
    assertEquals("Hello from my-cyc-agent",
            aclList.getValueForKeyword(
            CycObjectFactory.makeCycSymbol(":content")));
    assertEquals(CycObjectFactory.makeCycSymbol(":cycl"),
            aclList.getValueForKeyword(CycObjectFactory.makeCycSymbol(
            ":language")));
    assertEquals("my cookie",
            aclList.getValueForKeyword(CycObjectFactory.makeCycSymbol(
            ":reply-with")));
    assertEquals(CycObjectFactory.makeCycSymbol("cyc-api"),
            aclList.getValueForKeyword(CycObjectFactory.makeCycSymbol(
            ":ontology")));
    assertEquals(CycObjectFactory.makeCycSymbol(":fipa-request"),
            aclList.getValueForKeyword(CycObjectFactory.makeCycSymbol(
            ":protocol")));
    assertNull(aclList.getValueForKeyword(CycObjectFactory.makeCycSymbol(
            ":not-there")));
  }

  /**
   * Tests the test harness itself.
   */
  @Test
  public void testTestHarness() {
    System.out.println("\n*** testTestHarness ***");
    assertTrue(true);
    System.out.println("*** testTestHarness OK ***");
  }

  public static void testCycNumber() {
    System.out.println("\n*** testCycNumber ***");
    final CycNumberImpl one = new CycNumberImpl(1);
    assertTrue(one instanceof DenotationalTerm);
    assertTrue(one.equalsAtEL(1));
    final CycNumberImpl oneTwoThree = CycObjectFactory.makeCycNumber(123);
    assertTrue(oneTwoThree instanceof Comparable);
    final CycNumberImpl three = CycObjectFactory.makeCycNumber(3);
    final CycNumberImpl minusZero = CycObjectFactory.makeCycNumber(-0.0);
    // Get a couple BigIntegers larger than the largest Double:
    BigInteger bigInteger = new BigInteger("212");
    BigInteger biggerInteger = new BigInteger("213");
    while (bigInteger.doubleValue() <= Double.MAX_VALUE) {
      bigInteger = biggerInteger;
      biggerInteger = biggerInteger.multiply(biggerInteger);
    }
    final CycNumberImpl big = CycObjectFactory.makeCycNumber(bigInteger);
    final CycNumberImpl bigger = CycObjectFactory.makeCycNumber(biggerInteger);
    assertNotSame(big, bigger);
    assertTrue(bigger.isGreaterThan(big));
    final List<CycNumberImpl> numbers = Arrays.asList(three, big, minusZero, bigger);
    Collections.sort(numbers);
    assertEquals(minusZero, numbers.get(0));
    assertEquals(big, numbers.get(2));
    final CycNumberImpl doubleOne = CycObjectFactory.makeCycNumber(1.0);
    assertNotSame(one, doubleOne);
    assertFalse(one.isGreaterThan(doubleOne));
    assertFalse(doubleOne.isGreaterThan(one));
    final CycNumberImpl floatOne = CycObjectFactory.makeCycNumber(1.0F);
    assertNotSame(floatOne, doubleOne);
    assertNotSame(floatOne, one);
    assertEquals(doubleOne, doubleOne);
    final CycNumberImpl plusZero = CycObjectFactory.makeCycNumber(0.0);
    assertNotSame(minusZero, plusZero);
    assertTrue(plusZero.isGreaterThan(minusZero));
    System.out.println("*** testCycNumber OK ***");
  }

  /**
   * Tests FormulaImpl functionality.
   */
  @Test
  public void testCycFormula() throws CycConnectionException {
    System.out.println("\n*** testCycFormula ***");
    final FormulaSentence isaXThing = CycFormulaSentence.makeCycFormulaSentence(
            ISA, VAR_X, THING);
    assertEquals(isaXThing.getFirstArgPositionForTerm(isaXThing),
            ArgPositionImpl.TOP);
    final Set<CycConstantImpl> gatheredConstants = isaXThing.treeGather(
            CycConstantImpl.class);
    assertEquals(2, gatheredConstants.size());
    assertTrue(isaXThing.treeContains(isaXThing));
    assertTrue(isaXThing.treeContains(ISA));
    assertTrue(isaXThing.treeContains(VAR_X));
    assertTrue(isaXThing.treeContains(THING));
    assertTrue(isaXThing.contains(ISA));
    assertTrue(isaXThing.contains(VAR_X));
    assertTrue(isaXThing.contains(THING));
    final FormulaSentence someXIsaThing = CycFormulaSentence.makeCycFormulaSentence(
            THERE_EXISTS, VAR_X, isaXThing);
    assertTrue(someXIsaThing.contains(isaXThing));
    assertTrue(someXIsaThing.treeContains(isaXThing));
    assertTrue(someXIsaThing.treeContains(ISA));
    assertTrue(someXIsaThing.treeContains(VAR_X));
    assertTrue(someXIsaThing.treeContains(THING));
    assertEquals(ARG1, isaXThing.getFirstArgPositionForTerm(VAR_X));
    assertEquals(new ArgPositionImpl(2, 2),
            someXIsaThing.getFirstArgPositionForTerm(THING));
    {
      assertEquals(new ArrayList(Arrays.asList(VAR_X)),
              new ArrayList(isaXThing.findFreeVariables()));
      assertTrue(someXIsaThing.findFreeVariables().isEmpty());
      final FormulaSentence everyXIsaThing = CycFormulaSentence.makeCycFormulaSentence(
              FOR_ALL, VAR_X, isaXThing);
      assertTrue(everyXIsaThing.findFreeVariables().isEmpty());
      final FormulaSentence isaXY = CycFormulaSentence.makeCycFormulaSentence(
              ISA, VAR_X, VAR_Y);
      assertEquals(new ArrayList(Arrays.asList(VAR_X, VAR_Y)),
              new ArrayList(isaXY.findFreeVariables()));
      final FormulaSentence conj = CycFormulaSentence.makeConjunction(
              isaXThing, isaXY);
      assertEquals(new ArrayList(Arrays.asList(VAR_X, VAR_Y)),
              new ArrayList(conj.findFreeVariables()));
    }
    { //getSpecifiedObject()
      FormulaImpl formula2 = new FormulaImpl(getCyc().getObjectTool().makeCycList(
              "(1 (2 3 (4)) 5)"));
      ArgPositionImpl pathSpecification = ARG0;
      Object obj = formula2.getSpecifiedObject(pathSpecification);
      Object expectedObj = Integer.valueOf(1);
      assertEquals(expectedObj, obj);

      pathSpecification = ARG1;
      obj = formula2.getSpecifiedObject(pathSpecification);
      expectedObj = formula2.getArg1();
      assertEquals(expectedObj, obj);

      pathSpecification = ARG2;
      obj = formula2.getSpecifiedObject(pathSpecification);
      expectedObj = formula2.getArg2();
      assertEquals(expectedObj, obj);

      pathSpecification = new ArgPositionImpl(1, 2, 0);
      obj = formula2.getSpecifiedObject(pathSpecification);
      expectedObj = 4;
      assertEquals(expectedObj, obj);

      // setSpecifedObject
      pathSpecification = ARG0;
      formula2.setSpecifiedObject(pathSpecification, "a");
      expectedObj = new FormulaImpl(getCyc().getObjectTool().makeCycList("(\"a\" (2 3 (4)) 5)"));
      assertEquals(expectedObj, formula2);

      pathSpecification = ARG2;
      formula2.setSpecifiedObject(pathSpecification, "b");
      expectedObj = new FormulaImpl(getCyc().getObjectTool().makeCycList(
              "(\"a\" (2 3 (4)) \"b\")"));
      assertEquals(expectedObj, formula2);

      pathSpecification = new ArgPositionImpl(1, 2, 0);
      formula2.setSpecifiedObject(pathSpecification, "c");
      expectedObj = new FormulaImpl(getCyc().getObjectTool().makeCycList(
              "(\"a\" (2 3 (\"c\")) \"b\")"));
      assertEquals(expectedObj, formula2);

      try {
        Naut cycNat = new NautImpl(DOLLAR, 1);
        formula2.addArg(cycNat);
        expectedObj = new FormulaImpl(getCyc().getObjectTool().canonicalizeList(getCyc().getObjectTool().makeCycList(
                "(\"a\" (2 3 (\"c\")) \"b\" (" + usDollarFn + " 2))")));
        pathSpecification = new ArgPositionImpl(3, 1);
        formula2.setSpecifiedObject(pathSpecification, 2);
        assertEquals(expectedObj, formula2);
        formula2.setSpecifiedObject(TOP, cycNat.getFormula());
        assertEquals(cycNat.getFormula().getArgs(), formula2.getArgs());
      } catch (CycConnectionException ex) {
        Logger.getLogger(CycObjectUnitTest.class.getName()).log(Level.SEVERE, null, ex);
      } catch (CycApiException ex) {
        Logger.getLogger(CycObjectUnitTest.class.getName()).log(Level.SEVERE, null, ex);
      }

      // test getArgPositionsForTerm

      FormulaImpl list = new FormulaImpl(
              makeCycList(makeCycList("c", "1", "2"), "a", "b", "c",
              makeCycList("a", makeCycList("c", "10", "11"), "c", "2")));
      assertEquals(
              "((\"c\" \"1\" \"2\") \"a\" \"b\" \"c\" (\"a\" (\"c\" \"10\" \"11\") \"c\" \"2\"))",
              list.toString());
      Set<ArgPosition> result = list.getArgPositionsForTerm("a");
      assertEquals(new HashSet(Arrays.asList(ARG1,
              new ArgPositionImpl(4, 0))), result);
      Set<ArgPosition> result1 = list.getArgPositionsForTerm("c");
      assertEquals(new HashSet(Arrays.asList(new ArgPositionImpl(0, 0),
              ARG3,
              new ArgPositionImpl(4, 1, 0), new ArgPositionImpl(4, 2))), result1);
      ArgPosition result2 = list.getFirstArgPositionForTerm("d");
      assertEquals(null, result2);
    }
    System.out.println("*** testCycFormula OK ***");
  }

  @Test
  public void testCompactExternalIds() {
    System.out.println("\n*** testCompactExternalIds ***");
    try {
      final Object obj = CommonConstants.BASE_KB;
      final String id = "Mx4rvViBEZwpEbGdrcN5Y29ycA";
      assertEquals(id, DefaultCycObject.toCompactExternalId(obj, getCyc()));
      assertEquals(obj, DefaultCycObject.fromCompactExternalId(id, getCyc()));
    } catch (Exception e) {
      failWithException(e);
    }
    System.out.println("*** testCompactExternalIds OK ***");
  }

  /**
   * Tests <tt>GuidImpl</tt> object behavior.
   */
  @Test
  public void testGuid() {
    System.out.println("\n*** testGuid ***");
    CycObjectFactory.resetGuidCache();
    assertEquals(0, CycObjectFactory.getGuidCacheSize());
    String guidString = TestGuids.APPLE_TREE_GUID_STRING;
    GuidImpl guid = CycObjectFactory.makeGuid(guidString);
    assertEquals(1, CycObjectFactory.getGuidCacheSize());
    assertEquals(guidString, guid.toString());
    GuidImpl guid2 = CycObjectFactory.getGuidCache(guidString);
    assertEquals(guid, guid2);
    GuidImpl guid3 = CycObjectFactory.makeGuid(guidString);
    assertEquals(guid, guid3);
    assertEquals(1, CycObjectFactory.getGuidCacheSize());

    // toXML, toXMLString, unmarshal
    XMLStringWriter xmlStringWriter = new XMLStringWriter();
    try {
      guid.toXML(xmlStringWriter, 0, false);
      assertEquals("<guid>bd58c19d-9c29-11b1-9dad-c379636f7270</guid>\n",
              xmlStringWriter.toString());
      assertEquals("<guid>bd58c19d-9c29-11b1-9dad-c379636f7270</guid>\n",
              guid.toXMLString());
      String guidXMLString = guid.toXMLString();
      CycObjectFactory.resetGuidCache();
      Object object = CycObjectFactory.unmarshal(guidXMLString);
      assertTrue(object instanceof GuidImpl);
      assertEquals(guid, (GuidImpl) object);
      assertTrue(CycObjectFactory.unmarshal(guidXMLString)
              == CycObjectFactory.unmarshal(guidXMLString));
    } catch (Exception e) {
      failWithException(e);
    }

    System.out.println("*** testGuid OK ***");
  }

  /**
   * Tests <tt>CycSymbolImpl</tt> object behavior.
   */
  @Test
  public void testCycSymbol() {
    System.out.println("\n*** testCycSymbol ***");
    CycObjectFactory.resetCycSymbolCache();
    assertEquals(CycObjectFactory.RESET_SYMBOL_CACHE_SIZE,
            CycObjectFactory.getCycSymbolCacheSize());
    String symbolName = "WHY-ISA?";
    CycSymbolImpl cycSymbol = CycObjectFactory.makeCycSymbol(symbolName);
    assertEquals(CycObjectFactory.RESET_SYMBOL_CACHE_SIZE + 1,
            CycObjectFactory.getCycSymbolCacheSize());
    assertEquals(symbolName, cycSymbol.toString());
    assertNotNull(CycObjectFactory.getCycSymbolCache(symbolName));
    CycSymbolImpl cycSymbol2 = CycObjectFactory.getCycSymbolCache(symbolName);
    assertEquals(cycSymbol, cycSymbol2);
    CycSymbolImpl cycSymbol3 = CycObjectFactory.makeCycSymbol(symbolName);
    assertEquals(cycSymbol, cycSymbol3);
    assertEquals(CycObjectFactory.RESET_SYMBOL_CACHE_SIZE + 1,
            CycObjectFactory.getCycSymbolCacheSize());
    String symbolName4 = "WHY-ISA?";
    CycSymbolImpl cycSymbol4 = CycObjectFactory.makeCycSymbol(symbolName4);
    assertEquals(cycSymbol.toString(), cycSymbol4.toString());
    assertEquals(cycSymbol, cycSymbol4);
    try {
      testCycObjectRetrievable(cycSymbol);
    } catch (Exception ex) {
      failWithException(ex);
    }

    // compareTo
    ArrayList symbols = new ArrayList();
    symbols.add(CycObjectFactory.makeCycSymbol("isa?"));
    symbols.add(CycObjectFactory.makeCycSymbol("define-private"));
    symbols.add(CycObjectFactory.makeCycSymbol("nil"));
    Collections.sort(symbols);
    assertEquals("[DEFINE-PRIVATE, ISA?, NIL]", symbols.toString());

    // isKeyword
    CycSymbolImpl cycSymbol5 = CycObjectFactory.makeCycSymbol("nil");
    assertFalse(cycSymbol5.isKeyword());
    CycSymbolImpl cycSymbol6 = CycObjectFactory.makeCycSymbol(":pos");
    assertTrue(cycSymbol6.isKeyword());

    // isValidSymbolName
    assertTrue(CycSymbolImpl.isValidSymbolName("t"));
    assertTrue(CycSymbolImpl.isValidSymbolName("nil"));
    assertTrue(CycSymbolImpl.isValidSymbolName("a_"));
    assertTrue(CycSymbolImpl.isValidSymbolName("a-b"));
    assertTrue(CycSymbolImpl.isValidSymbolName("a-b"));
    assertTrue(CycSymbolImpl.isValidSymbolName("a-9b"));
    assertTrue(CycSymbolImpl.isValidSymbolName("*MY-SYMBOL*"));
    assertFalse(CycSymbolImpl.isValidSymbolName(" "));
    assertFalse(CycSymbolImpl.isValidSymbolName(brazil));
    assertFalse(CycSymbolImpl.isValidSymbolName("\"a-string\""));

    //packages
    CycSymbolImpl symbol7 = new CycSymbolImpl("CYC", "BLAH");
    CycSymbolImpl symbol8 = new CycSymbolImpl("|CYC|", "BLAH");
    CycSymbolImpl symbol9 = new CycSymbolImpl("CYC", "|BLAH|");
    CycSymbolImpl symbol10 = new CycSymbolImpl("|CYC|", "|BLAH|");
    assertEquals("CYC", symbol7.getPackageName());
    assertEquals("CYC", symbol8.getPackageName());
    assertEquals("CYC", symbol9.getPackageName());
    assertEquals("CYC", symbol10.getPackageName());
    assertEquals("CYC", symbol7.getPackageNamePrecise());
    assertEquals("CYC", symbol8.getPackageNamePrecise());
    assertEquals("CYC", symbol9.getPackageNamePrecise());
    assertEquals("CYC", symbol10.getPackageNamePrecise());
    assertEquals("BLAH", symbol7.getSymbolName());
    assertEquals("BLAH", symbol8.getSymbolName());
    assertEquals("BLAH", symbol9.getSymbolName());
    assertEquals("BLAH", symbol10.getSymbolName());
    assertEquals("BLAH", symbol7.getSymbolNamePrecise());
    assertEquals("BLAH", symbol8.getSymbolNamePrecise());
    assertEquals("BLAH", symbol9.getSymbolNamePrecise());
    assertEquals("BLAH", symbol10.getSymbolNamePrecise());
    assertEquals(symbol7, symbol8);
    assertEquals(symbol7, symbol9);
    assertEquals(symbol7, symbol10);
    assertEquals("BLAH", symbol7.toString());
    assertEquals("BLAH", symbol8.toString());
    assertEquals("BLAH", symbol9.toString());
    assertEquals("BLAH", symbol10.toString());
    assertEquals("CYC:BLAH", symbol7.toFullStringForced());
    assertEquals("CYC:BLAH", symbol8.toFullStringForced());
    assertEquals("CYC:BLAH", symbol9.toFullStringForced());
    assertEquals("CYC:BLAH", symbol10.toFullStringForced());
    assertEquals("CYC:BLAH", symbol7.toFullString("SL"));
    assertEquals("CYC:BLAH", symbol8.toFullString("SL"));
    assertEquals("CYC:BLAH", symbol9.toFullString("SL"));
    assertEquals("CYC:BLAH", symbol10.toFullString("SL"));
    assertEquals("BLAH", symbol10.toFullString("CYC"));
    assertFalse(symbol7.isKeyword());
    assertFalse(symbol8.isKeyword());
    assertFalse(symbol9.isKeyword());
    assertFalse(symbol10.isKeyword());

    CycSymbolImpl symbol11 = new CycSymbolImpl("|CYC RuLeS|", "|BLAH BiTeS|");
    CycSymbolImpl symbol12 = new CycSymbolImpl("CYC RuLeS", "BLAH BiTeS");
    assertEquals("CYC RuLeS", symbol11.getPackageName());
    assertEquals("CYC RuLeS", symbol12.getPackageName());
    assertEquals("|CYC RuLeS|", symbol11.getPackageNamePrecise());
    assertEquals("|CYC RuLeS|", symbol12.getPackageNamePrecise());
    assertEquals("BLAH BiTeS", symbol11.getSymbolName());
    assertEquals("BLAH BiTeS", symbol12.getSymbolName());
    assertEquals("|BLAH BiTeS|", symbol11.getSymbolNamePrecise());
    assertEquals("|BLAH BiTeS|", symbol12.getSymbolNamePrecise());
    assertEquals(symbol11, symbol12);
    assertEquals("|BLAH BiTeS|", symbol11.toString());
    assertEquals("|BLAH BiTeS|", symbol12.toString());
    assertEquals("|CYC RuLeS|:|BLAH BiTeS|", symbol11.toFullStringForced());
    assertEquals("|CYC RuLeS|:|BLAH BiTeS|", symbol12.toFullStringForced());
    assertEquals("|CYC RuLeS|:|BLAH BiTeS|", symbol11.toFullString("SL"));
    assertEquals("|CYC RuLeS|:|BLAH BiTeS|", symbol12.toFullString("SL"));
    assertEquals("|BLAH BiTeS|", symbol12.toFullString("CYC RuLeS"));
    assertFalse(symbol11.isKeyword());
    assertFalse(symbol12.isKeyword());

    CycSymbolImpl symbol13 = new CycSymbolImpl("KEYWORD", "BLAH");
    CycSymbolImpl symbol14 = new CycSymbolImpl("|KEYWORD|", "BLAH");
    CycSymbolImpl symbol15 = new CycSymbolImpl("", ":BLAH");
    CycSymbolImpl symbol16 = new CycSymbolImpl(null, ":BLAH");
    assertEquals("KEYWORD", symbol13.getPackageName());
    assertEquals("KEYWORD", symbol14.getPackageName());
    assertEquals("KEYWORD", symbol15.getPackageName());
    assertEquals("KEYWORD", symbol16.getPackageName());
    assertEquals("KEYWORD", symbol13.getPackageNamePrecise());
    assertEquals("KEYWORD", symbol14.getPackageNamePrecise());
    assertEquals("KEYWORD", symbol15.getPackageNamePrecise());
    assertEquals("KEYWORD", symbol16.getPackageNamePrecise());
    assertEquals("BLAH", symbol13.getSymbolName());
    assertEquals("BLAH", symbol14.getSymbolName());
    assertEquals("BLAH", symbol15.getSymbolName());
    assertEquals("BLAH", symbol16.getSymbolName());
    assertEquals("BLAH", symbol13.getSymbolNamePrecise());
    assertEquals("BLAH", symbol14.getSymbolNamePrecise());
    assertEquals("BLAH", symbol15.getSymbolNamePrecise());
    assertEquals("BLAH", symbol16.getSymbolNamePrecise());
    assertEquals(symbol13, symbol14);
    assertEquals(symbol13, symbol15);
    assertEquals(symbol13, symbol16);
    assertEquals(":BLAH", symbol13.toString());
    assertEquals(":BLAH", symbol14.toString());
    assertEquals(":BLAH", symbol15.toString());
    assertEquals(":BLAH", symbol16.toString());
    assertEquals("KEYWORD:BLAH", symbol13.toFullStringForced());
    assertEquals("KEYWORD:BLAH", symbol14.toFullStringForced());
    assertEquals("KEYWORD:BLAH", symbol15.toFullStringForced());
    assertEquals("KEYWORD:BLAH", symbol16.toFullStringForced());
    assertEquals(":BLAH", symbol13.toFullString("SL"));
    assertEquals(":BLAH", symbol14.toFullString("SL"));
    assertEquals(":BLAH", symbol15.toFullString("SL"));
    assertEquals(":BLAH", symbol16.toFullString("SL"));
    assertEquals(":BLAH", symbol16.toFullString("KEYWORD"));
    assertTrue(symbol13.isKeyword());
    assertTrue(symbol14.isKeyword());
    assertTrue(symbol15.isKeyword());
    assertTrue(symbol16.isKeyword());

    // toXML, toXMLString, unmarshal
    XMLStringWriter xmlStringWriter = new XMLStringWriter();
    try {
      cycSymbol6.toXML(xmlStringWriter, 0, false);
      assertEquals("<symbol>:POS</symbol>\n", xmlStringWriter.toString());
      assertEquals("<symbol>:POS</symbol>\n", cycSymbol6.toXMLString());
      String cycSymbolXMLString = cycSymbol6.toXMLString();
      Object object = CycObjectFactory.unmarshal(cycSymbolXMLString);
      assertTrue(object instanceof CycSymbolImpl);
      assertEquals(cycSymbol6, (CycSymbolImpl) object);
    } catch (Exception e) {
      failWithException(e);
    }

    System.out.println("*** testCycSymbol OK ***");
  }

  /**
   * Tests <tt>CycConstantImpl</tt> object behavior.
   */
  @Test
  public void testCycConstant() {
    System.out.println("\n*** testCycConstantI ***");
    try {
      CycObjectFactory.resetCycConstantCaches();
      assertEquals(0, CycObjectFactory.getCycConstantCacheByNameSize());
      String guidString = TAME_ANIMAL.getGuid().getGuidString();
      String constantName = TAME_ANIMAL.getName();
      CycObjectFactory.addCycConstantCache(TAME_ANIMAL);
      assertNotNull(TAME_ANIMAL);
      assertEquals(1, CycObjectFactory.getCycConstantCacheByNameSize());

      // Attempt to create a duplicate returns the cached existing object.
      CycConstant cycConstant2 = new CycConstantImpl(constantName,
              CycObjectFactory.makeGuid(guidString));
      CycObjectFactory.addCycConstantCache(cycConstant2);
      assertEquals(1, CycObjectFactory.getCycConstantCacheByNameSize());
      assertEquals(TAME_ANIMAL, cycConstant2);

      CycConstant cycConstant3 = new CycConstantImpl(constantName,
              CycObjectFactory.makeGuid(guidString));
      CycObjectFactory.addCycConstantCache(cycConstant3);
      assertEquals(TAME_ANIMAL.toString(), cycConstant3.toString());
      assertEquals(TAME_ANIMAL.cyclify(), cycConstant3.cyclify());
      assertEquals(TAME_ANIMAL, cycConstant3);
      testCycObjectRetrievable(TAME_ANIMAL);


      // compareTo
      ArrayList constants = new ArrayList();

      constants.add(DOG);
      constants.add(CAT);
      constants.add(BRAZIL);
      constants.add(COLLECTION);
      Collections.sort(constants);
      assertEquals("[Brazil, Cat, Collection, Dog]", constants.toString());

      CycConstantImpl cycConstant4 =
              TRANSPORTATION_DEVICE_VEHICLE;

      XMLStringWriter xmlStringWriter = new XMLStringWriter();
      cycConstant4.toXML(xmlStringWriter, 0, false);
      String expectedXML =
              "<constant>\n"
              + "  <guid>c0bce169-9c29-11b1-9dad-c379636f7270</guid>\n"
              + "  <name>TransportationDevice-Vehicle</name>\n"
              + "</constant>\n";
      assertEquals(expectedXML, xmlStringWriter.toString());
      assertEquals(expectedXML, cycConstant4.toXMLString());
      String cycConstantXMLString = cycConstant4.toXMLString();
      CycObjectFactory.resetCycConstantCaches();
      Object object = CycObjectFactory.unmarshal(cycConstantXMLString);
      assertTrue(object instanceof CycConstantImpl);
      assertEquals(cycConstant4, (CycConstantImpl) object);
    } catch (Exception e) {
      failWithException(e);
    }

    System.out.println("*** testCycConstantI OK ***");
  }

  /**
   * Tests <tt>Nart</tt> object behavior.
   */
  @Test
  public void testCycNart() {
    System.out.println("\n*** testCycNart ***");
    try {
      Nart cycNart = new NartImpl(DOLLAR, 1);
      //testCycObjectRetrievable(cycNart);
      Nart dollar1 = cycNart;
      assertNotNull(cycNart);
      assertEquals("(USDollarFn 1)", cycNart.toString());
      assertEquals("(" + usDollarFn + " 1)", cycNart.cyclify());

      Nart cycNart2 = new NartImpl(DOLLAR, 1);
      assertEquals(cycNart.toString(), cycNart2.toString());
      assertEquals(cycNart, cycNart2);

      // compareTo
      {
        ArrayList narts = new ArrayList();
        CycArrayList<CycObject> nartCycList = new CycArrayList<CycObject>();
        nartCycList.add(DateConverter.YEAR_FN);
        nartCycList.add(Integer.valueOf(2000));
        Nart year2K = new NartImpl(nartCycList);
        narts.add(year2K);
        assertEquals("[(YearFn 2000)]", narts.toString());
        CycConstant person =
                getCyc().getLookupTool().getKnownConstantByGuid(
                CycObjectFactory.makeGuid(TestConstants.PERSON.getGuid().getGuidString()));
        CycList nartCycList2 = new CycArrayList<CycObject>();
        nartCycList2.add(CONVEY_FN);
        nartCycList2.add(person);
        narts.add(new NartImpl(nartCycList2));
        CycList nartCycList3 = new CycArrayList<CycObject>();
        nartCycList3.add(DOLLAR);
        nartCycList3.add(Integer.valueOf(1));
        narts.add(new NartImpl(nartCycList3));
        Collections.sort(narts);
        assertEquals("[(ConveyFn Person), (USDollarFn 1), (YearFn 2000)]",
                narts.toString());
      }

      // hasFunctorAndArgs
      assertTrue(dollar1.hasFunctorAndArgs());

      // toCycList()
      {
        CycList cycList = new CycArrayList<CycObject>();
        cycList.add(DOLLAR);
        cycList.add(Integer.valueOf(1));
        assertEquals(cycList, dollar1.toCycList());
      }

      // check cfasl representation of narts in a list
      {
        CycList myNarts = new CycArrayList<CycObject>();
        final Object randomNart1 = getCyc().converse().converseObject("(random-nart)");
        myNarts.add(randomNart1);
        final Object randomNart2 = getCyc().converse().converseObject("(random-nart)");
        myNarts.add(randomNart2);

        for (int i = 0; i < myNarts.size(); i++) {
          assertTrue(myNarts.get(i) instanceof Nart);
        }
        String command = SubLAPIHelper.makeSubLStmt("csetq",
                CycObjectFactory.makeCycSymbol("my-narts", false), myNarts);
        CycList myNartsBackFromCyc1 = getCyc().converse().converseList(command);
        CycList commandList = new CycArrayList();
        commandList.add(CycObjectFactory.makeCycSymbol("csetq"));
        commandList.add(CycObjectFactory.makeCycSymbol("my-narts"));
        commandList.addQuoted(myNarts);
        CycList myNartsBackFromCyc = getCyc().converse().converseList(commandList);
        for (int i = 0; i < myNartsBackFromCyc.size(); i++) {
          assertTrue(myNartsBackFromCyc.get(i) instanceof Nart);
          Nart myNartBackFromCyc = (Nart) myNartsBackFromCyc.get(i);
          assertTrue(myNartBackFromCyc.getFunctor() instanceof Fort);
          assertTrue(myNartBackFromCyc.getArguments() instanceof ArrayList);
          ArrayList args = (ArrayList) myNartBackFromCyc.getArguments();
          final Naut myNartFormula = ((Nart) myNarts.get(i)).getFormula();
          assertEquals(myNartFormula,
                  ((Nart) myNartsBackFromCyc.get(i)).getFormula());
          assertEquals(myNartFormula,
                  ((Nart) myNartsBackFromCyc1.get(i)).getFormula());

        }
      }

      // coerceToCycNart
      {
        NartImpl cycNart4 = new NartImpl(DOLLAR, Integer.valueOf(1));
        assertEquals(cycNart4, NartImpl.coerceToCycNart(cycNart4));
        CycList cycList4 = new CycArrayList<CycObject>();
        cycList4.add(DOLLAR);
        cycList4.add(Integer.valueOf(1));
        assertEquals(cycNart2, NartImpl.coerceToCycNart(cycList4));

        // toXML, toXMLString
        XMLStringWriter xmlStringWriter = new XMLStringWriter();
        cycNart4.toXML(xmlStringWriter, 0, false);
        System.out.println(xmlStringWriter.toString());

        String cycNartXMLString = cycNart4.toXMLString();
        System.out.println("cycNartXMLString\n" + cycNartXMLString);
        Object object = CycObjectFactory.unmarshal(cycNartXMLString);
        assertTrue(object instanceof Nart);
        assertEquals(cycNart4, (Nart) object);
        NartImpl cycNart5 = new NartImpl(THE_LIST, Integer.valueOf(1), "a string");
        cycNartXMLString = cycNart5.toXMLString();
        System.out.println("cycNartXMLString\n" + cycNartXMLString);
        object = CycObjectFactory.unmarshal(cycNartXMLString);
        assertTrue(object instanceof Nart);
        assertEquals(cycNart5, (Nart) object);
      }

      // Check whether stringApiValue() behaves properly on a NART with a string argument
      {
        Nart attawapiskat = new NartImpl(CITY_NAMED_FN, "Attawapiskat",
                ONTARIO_CANADIAN_PROVINCE);

        Object result = CycUtils.evalSubLWithWorker(getCyc(),
                attawapiskat.stringApiValue());
        assertTrue(result instanceof Nart);
        assertEquals(attawapiskat, (Nart) result);
      }

      // Check whether stringApiValue() behaves properly on a NART
      // with a string that contains a character that needs to be escaped in SubL
      {
        Nart hklmSam = new NartImpl(REGISTRY_KEY_FN, "HKLM\\SAM");

        Object result0 = NautImpl.convertIfPromising(CycUtils.evalSubLWithWorker(getCyc(),
                hklmSam.stringApiValue()));
        assertTrue(result0 instanceof NonAtomicTerm);
        assertEquals(hklmSam.getFormula(), ((NonAtomicTerm)result0).getFormula());
      }

      /*
       CycAssertion cycAssertion = getCyc().getAssertionById(Integer.valueOf(968857));
       Nart complexNart = (Nart) cycAssertion.getFormula().second();
       System.out.println(complexNart.toString());
       System.out.println(complexNart.cyclify());
       */
    } catch (Exception e) {
      failWithException(e);
    }

    System.out.println("*** testCycNart OK ***");
  }

  /**
   * Tests <tt>CycVariable</tt> object behavior.
   */
  @Test
  public void testCycVariable() {
    System.out.println("\n*** testCycVariable ***");
    CycVariable cycVariable1 = VAR_X;
    assertNotNull(cycVariable1);
    assertEquals("?X", cycVariable1.toString());
    assertEquals("?X", cycVariable1.cyclify());
    assertEquals("'?X", cycVariable1.stringApiValue());
    CycVariable cycVariable2 = VAR_VARIABLE;
    assertNotNull(cycVariable2);
    assertEquals("?variable", cycVariable2.toString().toLowerCase());
    assertEquals("?variable", cycVariable2.cyclify().toLowerCase());
    assertEquals("'?variable", cycVariable2.stringApiValue().toLowerCase());
    CycVariable cycVariable3 = new CycVariableImpl("?X");
    assertEquals(cycVariable1.toString(), cycVariable3.toString());
    assertEquals(cycVariable1.cyclify(), cycVariable3.cyclify());
    assertEquals(cycVariable1.stringApiValue(), cycVariable3.stringApiValue());
    assertEquals(cycVariable1, cycVariable3);
    assertTrue(makeCycVariable("??X").isDontCareVariable());

    // compareTo
    ArrayList variables = new ArrayList();
    variables.add(VAR_Y);
    variables.add(VAR_Z);
    variables.add(VAR_Y);
    variables.add(VAR_X);
    variables.add(VAR_Z);
    variables.add(VAR_X);
    Collections.sort(variables);
    assertEquals("[?X, ?X, ?Y, ?Y, ?Z, ?Z]", variables.toString().toUpperCase());
    CycVariable cycVariable1000 = new CycVariableImpl(":X");
    assertNotSame(cycVariable1, cycVariable1000);

    try {
      testCycObjectRetrievable(VAR_0);
      testCycObjectRetrievable(VAR_X);
    } catch (Exception e) {
      failWithException(e);
    }

    // makeUniqueCycVariable
    CycVariableImpl x = (CycVariableImpl) VAR_X;
    CycVariable x1 = CycObjectFactory.makeUniqueCycVariable(x);
    CycVariable x2 = CycObjectFactory.makeUniqueCycVariable(x);
    CycVariable x3 = CycObjectFactory.makeUniqueCycVariable(x);
    assertFalse((x.equals(x1)));
    assertFalse((x.equals(x2)));
    assertFalse((x.equals(x3)));
    assertFalse((x1.equals(x2)));
    assertTrue(x.cyclify().equals("?X"));
    assertTrue(x1.cyclify().startsWith("?X-"));
    assertTrue(x3.cyclify().startsWith("?X-"));

    // toXML, toXMLString, unmarshal
    XMLStringWriter xmlStringWriter = new XMLStringWriter();
    try {
      x.toXML(xmlStringWriter, 0, false);
      assertEquals("<variable>X</variable>\n", xmlStringWriter.toString());
      assertEquals("<variable>X</variable>\n", x.toXMLString());
      String cycVariableXMLString = x.toXMLString();
      CycObjectFactory.resetCycVariableCache();
      Object object = CycObjectFactory.unmarshal(cycVariableXMLString);
      assertTrue(object instanceof CycVariable);
      assertEquals(x, (CycVariable) object);
      assertTrue(CycObjectFactory.unmarshal(cycVariableXMLString)
              == CycObjectFactory.unmarshal(cycVariableXMLString));
    } catch (Exception e) {
      fail(e.getMessage());
    }

    System.out.println("*** testCycVariable OK ***");
  }

  static void testCycObjectRetrievable(final CycObject obj) throws CycConnectionException {
    final String command = "(IDENTITY " + obj.stringApiValue() + ")";
    final CycObject retrievedVersion = getCyc().converse().converseCycObject(command);
    assertEquals(
            "Retrieved version of " + obj + " is not 'equals' to the original.",
            obj, retrievedVersion);
  }

  /**
   * Tests StreamTokenizer CycList parsing behavior.
   */
  @Test
  public void testStreamTokenizer() {
    System.out.println("\n*** testStreamTokenizer ***");
    try {
      String string = "()";
      MyStreamTokenizer st = CycListParser.makeStreamTokenizer(string);
      assertEquals(40, st.nextToken());
      assertEquals(41, st.nextToken());
      assertEquals(st.TT_EOF, st.nextToken());

      string = "(1)";
      st = CycListParser.makeStreamTokenizer(string);
      assertEquals(40, st.nextToken());

      int token = st.nextToken();
      assertEquals(st.TT_WORD, token);
      assertEquals("1", st.sval);

      assertEquals(41, st.nextToken());
      assertEquals(st.TT_EOF, st.nextToken());
      string = "(-10 -2 -1.0 -5.2E05)";
      st = CycListParser.makeStreamTokenizer(string);
      assertEquals(40, st.nextToken());

      token = st.nextToken();
      assertEquals(st.TT_WORD, token);
      assertEquals("-10", st.sval);

      token = st.nextToken();
      assertEquals(st.TT_WORD, token);
      assertEquals("-2", st.sval);

      token = st.nextToken();
      assertEquals(st.TT_WORD, token);
      assertEquals("-1.0", st.sval);

      token = st.nextToken();
      assertEquals(st.TT_WORD, token);
      assertEquals("-5.2E05", st.sval);

      assertEquals(41, st.nextToken());
      assertEquals(st.TT_EOF, st.nextToken());

    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }

    System.out.println("*** testStreamTokenizer OK ***");
  }

  /**
   * Tests <tt>CycArrayList</tt> object behavior.
   */
  @Test
  public void testCycList() {
    System.out.println("\n*** testCycList ***");

    try {

      // Simple empty list constructor.
      ArrayList arrayList = new ArrayList();
      CycList cycList = new CycArrayList<CycObject>(arrayList);
      assertNotNull(cycList);
      assertEquals("()", cycList.toString());

      // Construct list of one element.
      ArrayList arrayList2 = new ArrayList();
      CycObjectFactory.addCycConstantCache(BRAZIL);
      arrayList2.add(BRAZIL);
      CycList cycList2 = new CycArrayList<CycObject>(arrayList2);
      assertEquals("(Brazil)", cycList2.toString());
      assertEquals("(" + brazil + ")", cycList2.cyclify());

      // Construct list with embedded sublist.
      ArrayList arrayList3 = new ArrayList();
      arrayList3.add(BRAZIL);
      arrayList3.add(cycList);
      arrayList3.add(cycList2);
      CycList cycList3 = new CycArrayList<CycObject>(arrayList3);
      assertEquals("(Brazil () (Brazil))", cycList3.toString());
      assertEquals("(" + brazil + " () (" + brazil + "))", cycList3.cyclify());

      // isValid()
      assertTrue(cycList.isValid());
      assertTrue(cycList2.isValid());
      assertTrue(cycList3.isValid());
      CycList cycList4 = new CycArrayList(new Hashtable());
      assertFalse(cycList4.isValid());

      // first(), rest()
      ArrayList arrayList5 = new ArrayList();
      arrayList5.add(BRAZIL);
      CycList cycList5 = new CycArrayList<CycObject>(arrayList5);
      assertEquals("(Brazil)", cycList5.toString());
      assertEquals("(" + brazil + ")", cycList5.cyclify());
      assertEquals(cycList5.first(), BRAZIL);
      assertTrue(((CycArrayList) (cycList5.rest())).size() == 0);
      CycList cycList5a = new CycArrayList<CycObject>();
      cycList5a.add("a");
      cycList5a.setDottedElement("b");
      assertEquals("b", cycList5a.rest());

      // reverse()
      assertEquals(cycList5.toString(), cycList5.reverse().toString());
      assertEquals("((" + brazil + ") () " + brazil + ")", cycList3.reverse().cyclify());

      // reverse of strings.
      ArrayList arrayList6 = new ArrayList();
      arrayList6.add("z");
      arrayList6.add("y");
      arrayList6.add("x");
      CycList cycList6 = new CycArrayList(arrayList6);
      assertEquals("(\"z\" \"y\" \"x\")", cycList6.toString());
      assertEquals("(\"x\" \"y\" \"z\")", cycList6.reverse().toString());

      // Improper lists.
      ArrayList arrayList7 = new ArrayList();
      arrayList7.add(Integer.valueOf(10));
      CycList cycList7 = new CycArrayList(arrayList7);
      cycList7.setDottedElement(BRAZIL);
      assertTrue(cycList7.size() == 2);
      assertEquals("(10 . Brazil)", cycList7.toString());
      //CycListParser.verbosity = 10;

      CycListParser cycListParser = new CycListParser(null);
      CycList cycList7_1 = cycListParser.read("(a b c)");
      assertEquals("(A B C)", cycList7_1.toString());

      CycList cycList7a = getCyc().getObjectTool().makeCycList("(a . (b . (c . (d))))");
      assertEquals("(A B C D)", cycList7a.toString());
      CycList cycList7b = getCyc().getObjectTool().makeCycList("((a . b) . (c . d))");
      assertEquals("((A . B) C . D)", cycList7b.toString());
      CycList cycList7c = getCyc().getObjectTool().makeCycList("((a . (b)) . (c . (d)))");
      assertEquals("((A B) C D)", cycList7c.toString());
      CycList cycList7d = getCyc().getObjectTool().makeCycList("(a b . c)");
      assertEquals("(A B . C)", cycList7d.toString());
      CycList cycList7e = getCyc().getObjectTool().makeCycList("(a b c . d)");
      assertEquals("(A B C . D)", cycList7e.toString());
      // construct
      Object object1 = CycArrayList.construct(BRAZIL, CycObjectFactory.nil);
      assertNotNull(object1);
      assertTrue(object1 instanceof CycArrayList);
      assertEquals("(Brazil)", object1.toString());
      // makeDottedPair
      CycList cycList8 = CycArrayList.makeDottedPair(BRAZIL, "Atlantic");
      assertEquals("(Brazil . \"Atlantic\")", cycList8.toString());

      CycList cycList9 = CycArrayList.makeDottedPair(BRAZIL, Integer.valueOf(1));
      assertEquals("(Brazil . 1)", cycList9.toString());

      CycList cycList10 = CycArrayList.makeDottedPair(BRAZIL,
              CycObjectFactory.makeCycSymbol("foo"));
      assertEquals("(Brazil . FOO)", cycList10.toString());

      // Parse strings to make CycLists.
      String listAsString = "()";
      CycList cycList11 = getCyc().getObjectTool().makeCycList(listAsString);
      assertEquals(listAsString, cycList11.toString());
      listAsString = "(1)";
      CycList cycList12 = getCyc().getObjectTool().makeCycList(listAsString);
      assertEquals(listAsString, cycList12.toString());
      listAsString = "(1 2 3 4 5)";
      CycList cycList13 = getCyc().getObjectTool().makeCycList(listAsString);
      assertEquals(listAsString, cycList13.toString());
      listAsString = "(\"1\" \"bar\" A " + brazil + " Z 4.25 :KEYWORD ?COLLECTION NIL)";
      CycList cycList14 = getCyc().getObjectTool().makeCycList(listAsString);
      assertEquals(listAsString, cycList14.cyclify());
      listAsString = "((A))";
      CycList cycList15 = getCyc().getObjectTool().makeCycList(listAsString);
      assertEquals(listAsString, cycList15.toString());
      listAsString = "((A) (B C) (((D))))";
      CycList cycList16 = getCyc().getObjectTool().makeCycList(listAsString);
      assertEquals(listAsString, cycList16.toString());
      CycList cycList17 = getCyc().getObjectTool().makeCycList(listAsString);
      assertEquals(cycList17.toString(), cycList16.toString());
      assertEquals(cycList17.toString(), cycList16.toString());
      assertEquals(getCyc().getObjectTool().makeCycList("(A)"), cycList17.first());
      assertEquals(getCyc().getObjectTool().makeCycList("(B C)"), cycList17.second());
      assertEquals(getCyc().getObjectTool().makeCycList("(((D)))"), cycList17.third());
      listAsString = "(apply #'+ '(1 2 3))";
      CycList cycList18 = getCyc().getObjectTool().makeCycList(listAsString);
      assertEquals("(APPLY (FUNCTION +) (QUOTE (1 2 3)))",
              cycList18.toString());
      listAsString = "(1 2 \n"
              + " ;; a comment \n"
              + " 3 4 5)";
      CycList cycList19 = getCyc().getObjectTool().makeCycList(listAsString);
      assertEquals(cycList13, cycList19);
      listAsString = "(" + Double.toString(1.0E-05) + ")";
      CycList cycList19a = getCyc().getObjectTool().makeCycList(listAsString);
      assertEquals(listAsString, cycList19a.cyclify());
      cycListParser = new CycListParser(getCyc());
      listAsString = "(1 2 3) 4 \"5 6\" 7 (8 9 10) 11 test";
      CycList cycList19b = cycListParser.read(listAsString);
      assertEquals("(1 2 3)", cycList19b.toString());
      assertEquals(" 4 \"5 6\" 7 (8 9 10) 11 test",
              cycListParser.remainingString());
      listAsString =
              "(" + istAsserted + " \n"
              + "  (" + totalInvestmentEarningsForStockTypeBoughtDuring + "  \n"
              + "    " + techStock + "  \n"
              + "    (" + minusFn + " (" + poundGreatBritain + " 330000000000))  \n"
              + "    (" + earlyPartFn + " (" + yearFn + " 2000)))  \n"
              + "  " + theMotleyFoolUKCorpusMt + "))";
      final CycList cycList19c = cycListParser.read(listAsString);
      assertTrue(cycList19c.cyclify().indexOf("330000000000") > -1);
      assertTrue(
              DefaultCycObject.cyclify(cycList19c).indexOf("330000000000") > -1);
      testCycListAdd();

      // subst
      cycList18 = getCyc().getObjectTool().makeCycList("(b)");
      cycList19 = cycList18.subst(CycObjectFactory.makeCycSymbol("x"),
              CycObjectFactory.makeCycSymbol("a"));
      assertEquals(getCyc().getObjectTool().makeCycList("(b)"), cycList19);
      CycList cycList20 = getCyc().getObjectTool().makeCycList("(a)");
      CycList cycList21 = cycList20.subst(CycObjectFactory.makeCycSymbol("x"),
              CycObjectFactory.makeCycSymbol("a"));
      assertEquals(getCyc().getObjectTool().makeCycList("(x)"), cycList21);
      CycList cycList22 = getCyc().getObjectTool().makeCycList("((a))");
      CycList cycList23 = cycList22.subst(CycObjectFactory.makeCycSymbol("x"),
              CycObjectFactory.makeCycSymbol("a"));
      assertEquals(getCyc().getObjectTool().makeCycList("((x))"), cycList23);
      CycList cycList24 = getCyc().getObjectTool().makeCycList("((a) (b c) (((d))))");
      CycList cycList25 = cycList24.subst(CycObjectFactory.makeCycSymbol("x"),
              CycObjectFactory.makeCycSymbol("a"));
      assertEquals(getCyc().getObjectTool().makeCycList("((x) (b c) (((d))))"), cycList25);

      // containsDuplicates
      CycList cycList26 = getCyc().getObjectTool().makeCycList("(a b c d)");
      assertFalse(cycList26.containsDuplicates());
      CycList cycList27 = getCyc().getObjectTool().makeCycList("(a a c d)");
      assertTrue(cycList27.containsDuplicates());
      CycList cycList28 = getCyc().getObjectTool().makeCycList("(a b c c)");
      assertTrue(cycList28.containsDuplicates());
      CycList cycList29 = getCyc().getObjectTool().makeCycList("(a (b) (b) c)");
      assertTrue(cycList29.containsDuplicates());

      // list
      CycList cycList30 = CycArrayList.list(CycObjectFactory.makeCycSymbol("a"));
      assertEquals("(A)", cycList30.toString());
      CycList cycList31 = CycArrayList.list(CycObjectFactory.makeCycSymbol("a"),
              CycObjectFactory.makeCycSymbol("b"));
      assertEquals("(A B)", cycList31.toString());
      CycList cycList32 = CycArrayList.list(CycObjectFactory.makeCycSymbol("a"),
              CycObjectFactory.makeCycSymbol("b"),
              CycObjectFactory.makeCycSymbol("c"));
      assertEquals("(A B C)", cycList32.toString());

      // combinationsOf
      CycList cycList33 = getCyc().getObjectTool().makeCycList("(1 2 3 4)");
      assertEquals("((1) (2) (3) (4))", cycList33.combinationsOf(1).toString());
      assertEquals("((1 2) (1 3) (1 4) (2 3) (2 4) (3 4))",
              cycList33.combinationsOf(2).toString());
      assertEquals("((1 2 3 4))",
              cycList33.combinationsOf(4).toString());
      assertEquals("()",
              cycList33.combinationsOf(0).toString());
      assertEquals("()",
              (new CycArrayList()).combinationsOf(4).toString());

      // randomPermutation
      CycList cycList34 = getCyc().getObjectTool().makeCycList("(1 2 3 4 5 6 7 8 9 10)");
      CycList permutedCycList = cycList34.randomPermutation();
      assertEquals(10, permutedCycList.size());
      assertTrue(permutedCycList.contains(Integer.valueOf(2)));
      assertFalse(permutedCycList.containsDuplicates());

      // doesElementPrecedeOthers
      CycList cycList35 = getCyc().getObjectTool().makeCycList("(1 2 3 4 5 6 7 8 9 10)");
      assertTrue(cycList35.doesElementPrecedeOthers(Integer.valueOf(1),
              getCyc().getObjectTool().makeCycList("(8 7 6)")));
      assertTrue(cycList35.doesElementPrecedeOthers(Integer.valueOf(9),
              getCyc().getObjectTool().makeCycList("(10)")));
      assertTrue(cycList35.doesElementPrecedeOthers(Integer.valueOf(10),
              getCyc().getObjectTool().makeCycList("(18 17 16)")));
      assertFalse(cycList35.doesElementPrecedeOthers(Integer.valueOf(12),
              getCyc().getObjectTool().makeCycList("(1 2 10)")));
      assertFalse(cycList35.doesElementPrecedeOthers(Integer.valueOf(9),
              getCyc().getObjectTool().makeCycList("(8 7 6)")));

      // clone
      CycList cycList36 = getCyc().getObjectTool().makeCycList("(1 2 3 4 5)");
      CycList cycList37 = (CycArrayList) cycList36.clone();
      assertEquals(cycList36, cycList37);
      assertTrue(cycList36 != cycList37);
      CycList cycList38 = getCyc().getObjectTool().makeCycList("(1 2 3 4 5 . 6)");
      CycList cycList39 = (CycArrayList) cycList38.clone();

      assertEquals(cycList38, cycList39);
      assertTrue(cycList38 != cycList39);

      // deepCopy
      CycList cycList40 = getCyc().getObjectTool().makeCycList("(1 2 3 4 5)");
      CycList cycList41 = (CycArrayList) cycList40.deepCopy();
      assertEquals(cycList40, cycList41);
      assertTrue(cycList40 != cycList41);
      CycList cycList42 = getCyc().getObjectTool().makeCycList("(1 2 3 4 5 . 6)");
      CycList cycList43 = (CycArrayList) cycList42.deepCopy();
      assertEquals(cycList42, cycList43);
      assertTrue(cycList42 != cycList43);
      CycList cycList44 = getCyc().getObjectTool().makeCycList("(1 (2 3) (4 5) ((6)))");
      CycList cycList45 = (CycArrayList) cycList44.deepCopy();
      assertEquals(cycList44, cycList45);
      assertTrue(cycList44 != cycList45);
      assertEquals(cycList44.first(), cycList45.first());
      assertTrue(cycList44.first() == cycList45.first());
      assertEquals(cycList44.second(), cycList45.second());
      assertTrue(cycList44.second() != cycList45.second());
      assertEquals(cycList44.fourth(), cycList45.fourth());
      assertTrue(cycList44.fourth() != cycList45.fourth());
      assertEquals(((CycArrayList) cycList44.fourth()).first(),
              ((CycArrayList) cycList45.fourth()).first());
      assertTrue(((CycArrayList) cycList44.fourth()).first()
              != ((CycArrayList) cycList45.fourth()).first());

      // addNew
      CycList cycList46 = getCyc().getObjectTool().makeCycList("(1 2 3 4 5)");
      assertEquals(5, cycList46.size());
      cycList46.addNew(Integer.valueOf(6));
      assertEquals(6, cycList46.size());
      cycList46.addNew(Integer.valueOf(2));
      assertEquals(6, cycList46.size());
      // addAllNew
      CycArrayList cycList47 = (CycArrayList) getCyc().getObjectTool().makeCycList("(1 2 3 4 5)");
      assertEquals(5, cycList47.size());
      CycList cycList48 = getCyc().getObjectTool().makeCycList("(6 7 8 9 10)");
      assertEquals(5, cycList48.size());
      cycList47.addAllNew(cycList48);
      assertEquals(10, cycList47.size());
      CycList cycList49 = getCyc().getObjectTool().makeCycList("(2 5 8 9 11)");
      assertEquals(5, cycList49.size());
      cycList47.addAllNew(cycList49);
      assertEquals(11, cycList47.size());

      // last
      cycList46 = getCyc().getObjectTool().makeCycList("(8 7 6)");
      assertEquals(Integer.valueOf(6), cycList46.last());
      // toXML, toXMLString
      listAsString = "(\"1\" A (" + brazil + " . Z) 4.25 :KEYWORD ?collection NIL . " + dog + ")";
      cycList47 = (CycArrayList) getCyc().getObjectTool().makeCycList(listAsString);
      XMLStringWriter xmlStringWriter = new XMLStringWriter();
      String cycListXMLString = cycList47.toXMLString();
      Object object = CycObjectFactory.unmarshal(cycListXMLString);
      assertTrue(object instanceof CycArrayList);
      assertEquals(cycList47, (CycArrayList) object);
      cycList48 =
              getCyc().getObjectTool().makeCycList("(T " + TestSentences.BIOLOGICAL_TAXON_ETC.cyclify() + ")");
      cycListXMLString = Marshaller.marshall(cycList48);
//      System.out.println(cycListXMLString);
      object = CycObjectFactory.unmarshal(cycListXMLString);
      assertTrue(object instanceof CycArrayList);
      assertEquals(cycList48, (CycArrayList) object);
      cycListXMLString =
              "\n<list>\n"
              + "  <symbol>QUOTE</symbol>\n"
              + "  <list>\n"
              + "    <symbol>A</symbol>\n"
              + "    <dotted-element>\n"
              + "      <symbol>B</symbol>\n"
              + "    </dotted-element>\n"
              + "  </list>\n"
              + "</list>\n";
      object = CycObjectFactory.unmarshal(cycListXMLString);
      assertTrue(object instanceof CycArrayList);
      cycList49 = getCyc().getObjectTool().makeCycList("(QUOTE (A . B))");
      assertEquals(cycList49, object);
      testCycList50();

      // addQuoted
      CycList cycList51 = new CycArrayList();
      cycList51.add(Integer.valueOf(1));
      cycList51.addQuoted(CycObjectFactory.makeCycSymbol("quote-me"));
      assertEquals("(1 (QUOTE QUOTE-ME))", cycList51.toString());

      // toString (with null element)
      CycList cycList52 = new CycArrayList();
      cycList52.add(null);
      assertNull(cycList52.first());
      assertEquals("(null)", cycList52.toString());

      // treeConstants
      CycList cycList54 =
              getCyc().getObjectTool().makeCycList("(T " + TestSentences.BIOLOGICAL_TAXON_ETC.cyclify() + ")");
      cycList54.add(new NartImpl(getCyc().getLookupTool().getKnownConstantByName("FruitFn"),
              getCyc().getLookupTool().getKnownConstantByName("PumpkinPlant")));
      CycList cycList55 = cycList54.treeConstants();
      assertEquals(7, cycList55.size());

      // stringApiValue()
      CycConstant ontario = null;
      ontario =
              getCyc().getLookupTool().getKnownConstantByGuid(
              CycObjectFactory.makeGuid(TestGuids.ONTARIO_CANADIAN_PROVINCE_GUID_STRING));
      CycList cycList56 = new CycArrayList(ontario);
      Object result56 = CycUtils.evalSubLWithWorker(getCyc(),
              cycList56.stringApiValue());
      assertTrue(result56 instanceof CycArrayList);
      assertEquals(cycList56, (CycArrayList) result56);
      // Check whether stringApiValue works properly on a CycList with a NartImpl element
      CycConstant cityNamedFn =
              getCyc().getLookupTool().getKnownConstantByGuid(
              CycObjectFactory.makeGuid(TestGuids.CITY_NAMED_FN_GUID_STRING));
      Nart attawapiskat = new NartImpl(cityNamedFn, "Attawapiskat", ontario);
      CycList cycListWithNart = new CycArrayList(ontario, attawapiskat);
      Object resultObj = CycUtils.evalSubLWithWorker(getCyc(),
              cycListWithNart.stringApiValue());
      assertTrue(resultObj instanceof CycArrayList);
      assertEquals(cycListWithNart.cyclify(), ((CycArrayList) resultObj).cyclify());
      // stringApiValue() on a CycList containing a String containing a double-quote
      CycList cycListWithString = new CycArrayList(new String(
              "How much \"wood\" would a \"woodchuck\" \"chuck\"?"));
      resultObj = CycUtils.evalSubLWithWorker(getCyc(),
              cycListWithString.stringApiValue());
      assertTrue(resultObj instanceof CycArrayList);
      assertEquals(cycListWithString, (CycArrayList) resultObj);

      // stringApiValue() on a dotted CycArrayList
      CycList dottedCycList = new CycArrayList("first element", "second element");
      dottedCycList.setDottedElement("dotted element");
      resultObj = CycUtils.evalSubLWithWorker(getCyc(),
              dottedCycList.stringApiValue());
      assertTrue(resultObj instanceof CycArrayList);
      assertEquals(dottedCycList, (CycArrayList) resultObj);
      // Parse a list containing a string with a backslash
      String script = "(identity \"abc\")";
      resultObj = CycUtils.evalSubLWithWorker(getCyc(), script);
      assertTrue(resultObj instanceof String);
      script = "(identity \"abc\\\\\")";
      resultObj = CycUtils.evalSubLWithWorker(getCyc(), script);
      assertTrue(resultObj instanceof String);
      CycList command = new CycArrayList();
      command.add(CycObjectFactory.makeCycSymbol("identity"));
      command.add("abc\\");
      script = command.cyclifyWithEscapeChars();
      resultObj = CycUtils.evalSubLWithWorker(getCyc(), script);
      assertTrue(resultObj instanceof String);

      final String xml = ((CycArrayList) CycArrayList.makeCycList(2, 3, "foo")).toXMLString();
      assertNotNull(xml);

    } catch (Exception e) {
      failWithException(e);
    }

    testUnmodifiableCycList();

    System.out.println("*** testCycList OK ***");
  }

  private static void testCycListAdd() {

    // add
    CycArrayList<Long> longList = new CycArrayList<Long>();
    long n = 16;
    longList.add(n);
    assertEquals(new CycArrayList<Long>(n), longList);

    CycArrayList<Boolean> booleanList = new CycArrayList<Boolean>();
    booleanList.add(false);
    assertEquals(new CycArrayList<Boolean>(false), booleanList);

    CycArrayList<Float> floatList = new CycArrayList<Float>();
    float f = 16.0f;
    floatList.add(f);
    assertEquals(new CycArrayList<Float>(f), floatList);
  }

  /**
   * Tests <tt>CycArrayListVisitor</tt> object behavior.
   */
  @Test
  public void testCycListVisitor() {
    System.out.println("\n*** testCycListVisitor ***");

    CycListParser.verbosity = 0;

    try {
      CycList cycList2000 = getCyc().getObjectTool().makeCycList("(1 . 24)");
      CycList cycList2001 = getCyc().getObjectTool().makeCycList("(1 . 23)");
      assertFalse(cycList2001.equals(cycList2000));

      CycList cycList1 = getCyc().getObjectTool().makeCycList("()");
      Enumeration e1 = cycList1.cycListVisitor();
      assertFalse(e1.hasMoreElements());

      CycList cycList2 = getCyc().getObjectTool().makeCycList("(1 \"a\" :foo " + brazil + ")");
      Enumeration e2 = cycList2.cycListVisitor();
      assertTrue(e2.hasMoreElements());
      Integer integer1 = Integer.valueOf(1);
      Object nextObject = e2.nextElement();
      assertTrue(nextObject instanceof Integer);
      assertTrue(((Integer) nextObject).intValue() == integer1.intValue());
      assertTrue(((Integer) nextObject).intValue() == 1);
      assertTrue(e2.hasMoreElements());
      assertEquals("a", e2.nextElement());
      assertTrue(e2.hasMoreElements());
      assertEquals(CycObjectFactory.makeCycSymbol(":foo"), e2.nextElement());
      assertTrue(e2.hasMoreElements());
      assertEquals(getCyc().getObjectTool().makeCycConstant(brazil),
              e2.nextElement());
      assertFalse(e1.hasMoreElements());

      CycList cycList3 = getCyc().getObjectTool().makeCycList("((()))");
      Enumeration e3 = cycList3.cycListVisitor();
      assertFalse(e3.hasMoreElements());

      CycList cycList4 = getCyc().getObjectTool().makeCycList("(()())");
      Enumeration e4 = cycList4.cycListVisitor();
      assertFalse(e4.hasMoreElements());

      CycList cycList5 = getCyc().getObjectTool().makeCycList(
              "(\"a\" (\"b\") (\"c\") \"d\" \"e\")");
      Enumeration e5 = cycList5.cycListVisitor();
      assertTrue(e5.hasMoreElements());
      assertEquals("a", e5.nextElement());
      assertTrue(e5.hasMoreElements());
      assertEquals("b", e5.nextElement());
      assertTrue(e5.hasMoreElements());
      assertEquals("c", e5.nextElement());
      assertTrue(e5.hasMoreElements());
      assertEquals("d", e5.nextElement());
      assertTrue(e5.hasMoreElements());
      assertEquals("e", e5.nextElement());
      assertFalse(e5.hasMoreElements());

      CycList cycList6 = getCyc().getObjectTool().makeCycList(
              "(\"a\" (\"b\" \"c\") (\"d\" \"e\"))");
      Enumeration e6 = cycList6.cycListVisitor();
      assertTrue(e6.hasMoreElements());
      assertEquals("a", e6.nextElement());
      assertTrue(e6.hasMoreElements());
      assertEquals("b", e6.nextElement());
      assertTrue(e6.hasMoreElements());
      assertEquals("c", e6.nextElement());
      assertTrue(e6.hasMoreElements());
      assertEquals("d", e6.nextElement());
      assertTrue(e6.hasMoreElements());
      assertEquals("e", e6.nextElement());
      assertFalse(e6.hasMoreElements());
    } catch (Exception e) {
      failWithException(e);
    }

    System.out.println("*** testCycListVisitor OK ***");
  }


  /**
   * Tests the ByteArray class.
   */
  @Test
  public void testByteArray() {
    System.out.println("\n*** testByteArray ***");
    byte[] bytes = {0, 1, 2, 3, 4, -128};
    ByteArray byteArray1 = new ByteArray(bytes);
    assertNotNull(byteArray1);
    assertEquals(6, byteArray1.byteArrayValue().length);
    assertEquals(0, byteArray1.byteArrayValue()[0]);
    assertEquals(1, byteArray1.byteArrayValue()[1]);
    assertEquals(2, byteArray1.byteArrayValue()[2]);
    assertEquals(3, byteArray1.byteArrayValue()[3]);
    assertEquals(4, byteArray1.byteArrayValue()[4]);
    assertEquals(-128, byteArray1.byteArrayValue()[5]);
    byte[] bytes2 = {0, 1, 2, 3, 4, -128};
    ByteArray byteArray2 = new ByteArray(bytes2);
    assertEquals(byteArray1, byteArray1);
    assertEquals(byteArray1, byteArray2);
    byte[] bytes3 = {0, -1, 2, 3, 4, -128};
    ByteArray byteArray3 = new ByteArray(bytes3);
    assertFalse(byteArray1.equals(byteArray3));
    assertEquals("[ByteArray len:6 0,1,2,3,4,-128]", byteArray1.toString());

    // toXML, toXMLString, unmarshal
    XMLStringWriter xmlStringWriter = new XMLStringWriter();
    try {
      byteArray1.toXML(xmlStringWriter, 0, false);
      String expectedXmString =
              "<byte-vector>\n"
              + "  <length>6</length>\n"
              + "  <byte>0</byte>\n"
              + "  <byte>1</byte>\n"
              + "  <byte>2</byte>\n"
              + "  <byte>3</byte>\n"
              + "  <byte>4</byte>\n"
              + "  <byte>-128</byte>\n"
              + "</byte-vector>\n";

      assertEquals(expectedXmString, xmlStringWriter.toString());
      assertEquals(expectedXmString, byteArray1.toXMLString());
      assertEquals(byteArray1,
              CycObjectFactory.unmarshal(byteArray1.toXMLString()));
    } catch (Exception e) {
      failWithException(e);
    }
    System.out.println("*** testByteArray OK ***");
  }

  /**
   * Tests the ELMTCycList class.
   */
  @Test
  public void testELMTCycList() {
    System.out.println("\n*** testELMTCycList ***");

    try {
      if (!getCyc().isOpenCyc()) {
        final CycObject mt = new NautImpl(
                getCyc().getObjectTool().makeCycList(
                        TestSentences.MT_SPACE_CYCLISTS_MT_TIME_POINT.cyclify()
                ));
        assertNotNull(getCyc().getLookupTool().getComment(CommonConstants.ISA, mt));
      }
    } catch (Exception e) {
      failWithException(e);
    }
    System.out.println("*** testELMTCycList OK ***");
  }

  /**
   * Tests the character support in the DefaultCycObject class.
   */
  @Test
  public void testCharacter() {
    System.out.println("\n*** testCharacter ***");
    final Character[] testChars = {'a', 'A', '\t', ' '};
    for (final Character character : testChars) {
      final boolean testResult = DefaultCycObject.isCycLObject(character);
      assertTrue("char test " + character, testResult);
    }
    for (final Character character : testChars) {
      final String cyclified = DefaultCycObject.cyclify(character);
      final boolean testResult = cyclified.startsWith("#\\");
      assertTrue("char cyclify test " + character, testResult);
    }
    for (final Character character : testChars) {
      final String cyclified = DefaultCycObject.cyclifyWithEscapeChars(character,
              false);
      final boolean testResult = cyclified.startsWith("#\\\\");
      assertTrue("char escaped cyclify test " + character, testResult);
    }
    System.out.println("*** testCharacter OK ***");
  }

  /**
   * Tests the Unicode support in the DefaultCycObject class.
   */
  @Test
  public void testUnicodeString() {
    System.out.println("\n*** testUnicodeString ***");
    String result = DefaultCycObject.cyclifyWithEscapeChars("abc", false);
    //System.out.println("abc test |"+result+"|");
    assertTrue("abc test", "\"abc\"".equals(result));



    result = DefaultCycObject.cyclifyWithEscapeChars("a\\b", false);
    //System.out.println("a\\b test |"+result+"|");
    assertTrue("a\\\\b test", "\"a\\\\b\"".equals(result));

    result = DefaultCycObject.cyclifyWithEscapeChars("a\"b", false);
    //System.out.println("a\"b test |"+result+"|");
    assertTrue("a\"c test", "\"a\\\"b\"".equals(result));

    StringBuffer sb = new StringBuffer();
    sb.append('a');
    sb.append((char) (0x140));
    result = DefaultCycObject.cyclifyWithEscapeChars(sb.toString(), false);
    //System.out.println("a&u140 test |"+result+"|");
    assertEquals("(" + unicodeStringFn + " \"a&u140;\")", result);

    result = DefaultCycObject.cyclifyWithEscapeChars(sb.toString(), true);
    //System.out.println("a&u140 test |"+result+"|");
    assertEquals("(list " + unicodeStringFn + " \"a&u140;\")", result);

    CycList list = new CycArrayList();
    list.add(sb.toString());
    result = list.stringApiValue();
    //System.out.println("a&u140 test |"+result+"|");
    assertEquals("(list (list " + unicodeStringFn + " \"a&u140;\"))", result);

    CycList list2 = new CycArrayList();
    list2.add(list);
    result = list2.stringApiValue();
    //System.out.println("a&u140 test |"+result+"|");
    assertEquals("(list (list (list " + unicodeStringFn + " \"a&u140;\")))", result);


    System.out.println("*** testUnicodeString OK ***");
  }

  private void checkPrettyStringDetail(Map<ArgPositionImpl, Span> map,
          ArgPositionImpl curPos,
          int expectedBegin, int expectedEnd) {
    Span span = map.get(curPos);
    assertNotNull(span);
    assertEquals(expectedBegin, span.getStart());
    assertEquals(expectedEnd, span.getEnd());
  }

  /**
   * Test the CycList pretty printer
   */
  @Test
  public void testCycListPrettyStringDetails() {
    System.out.println("\n*** testCycListPrettyStringDetails ***");
    try {
      CycArrayList example = null;
      Map<ArgPositionImpl, Span> map = null;
      ArgPositionImpl curPos = null;
      example = (CycArrayList) com.cyc.baseclient.parser.CycLParserUtil.parseCycLTerm(
              TestSentences.ISA_MUFFET_DOG_STRING, true, getCyc());
      map = example.getPrettyStringDetails();
      checkPrettyStringDetail(map, ArgPositionImpl.TOP, 0, 16);
      checkPrettyStringDetail(map, ARG0, 1, 4);
      checkPrettyStringDetail(map, ARG1, 5, 11);
      checkPrettyStringDetail(map, ARG2, 12, 15);

      example = (CycArrayList) com.cyc.baseclient.parser.CycLParserUtil.parseCycLTerm(
              "(" + isa + " (" + instanceNamedFn + " \"Muffet\" (" + juvenileFn + " " + dog + "))"
                      + " (" + juvenileFn + " " + dog + "))",
              true, getCyc());
      map = example.getPrettyStringDetails();
      checkPrettyStringDetail(map, ArgPositionImpl.TOP, 0, 74);
      checkPrettyStringDetail(map, ARG0, 1, 4);
      checkPrettyStringDetail(map, new ArgPositionImpl(1, 0), 8, 23);
      checkPrettyStringDetail(map, new ArgPositionImpl(1, 1), 24, 32);
      curPos = new ArgPositionImpl(1, 2);
      curPos.extend(0);
      checkPrettyStringDetail(map, curPos, 38, 48);
      curPos = new ArgPositionImpl(1, 2);
      curPos.extend(1);
      checkPrettyStringDetail(map, curPos, 49, 52);
      checkPrettyStringDetail(map, new ArgPositionImpl(1, 2), 37, 53);
      checkPrettyStringDetail(map, new ArgPositionImpl(1), 7, 54);
      checkPrettyStringDetail(map, new ArgPositionImpl(2, 0), 58, 68);
      checkPrettyStringDetail(map, new ArgPositionImpl(2, 1), 69, 72);
      checkPrettyStringDetail(map, ARG2, 57, 73);

      final CycArrayList<String> testList = new CycArrayList<String>();
      final StringBuffer stringBuffer = new StringBuffer();
      stringBuffer.append('"');
      stringBuffer.append("abc");
      testList.add(stringBuffer.toString());
      final String testEscapedCyclifiedString = testList.toPrettyEscapedCyclifiedString(
              "");
      assertEquals("(\"\\\"abc\")", testEscapedCyclifiedString);
    } catch (Exception e) {
      failWithException(e);
    }


    System.out.println("*** testCycListPrettyStringDetails OK ***");
  }

  private void testUnmodifiableCycList() {
    testEmptyCycListAdd();
    final CycArrayList<Integer> frozenList = new CycArrayList.UnmodifiableCycList<Integer>(CycArrayList.makeCycList(
            1, 3, 2));
    testUnmodifiableCycListAdd(frozenList);
    testUnmodifiableCycListSort(frozenList);
  }

  private void testEmptyCycListAdd() {
    UnsupportedOperationException x = null;
    try {
      CycArrayList.EMPTY_CYC_LIST.add(4);
    } catch (UnsupportedOperationException e) {
      x = e;
    }
    assertNotNull(x);
  }

  private void testUnmodifiableCycListAdd(final CycList frozenList) {
    UnsupportedOperationException x = null;
    try {
      frozenList.add(4);
    } catch (UnsupportedOperationException e) {
      x = e;
    }
    assertNotNull(x);
  }

  private void testUnmodifiableCycListSort(final CycList frozenList) {
    UnsupportedOperationException x = null;
    try {
      Collections.sort(frozenList);
    } catch (UnsupportedOperationException e) {
      x = e;
    }
    assertNotNull(x);
  }

  @Test
  public void testVariableNameOptimization() throws CycConnectionException {
    System.out.println("\n*** testVariableNameOptimization ***");
    if (!getCyc().isOpenCyc()) {
      FormulaSentence sentence = getCyc().getObjectTool().makeCycSentence(
              "(" + likesAsFriend + " ?X ?Y)");
      Map<CycVariable, String> varMap = sentence.getOptimizedVarNames(getCyc());
      assertEquals(varMap.size(), 2);
    }
    System.out.println("*** testVariableNameOptimization OK ***");
  }

  @Test
  public void testIsConditionalSentence() {
    System.out.println("\n*** testIsConditionalSentence ***");
    try {
      FormulaSentence sentence = getCyc().getObjectTool().makeCycSentence(
              "(" + likesAsFriend + " ?X ?Y)");
      boolean conditional = sentence.isConditionalSentence();
      assertEquals(conditional, false);
      sentence = getCyc().getObjectTool().makeCycSentence(
              "(" + implies +  " (" + likesAsFriend + " ?X ?Y) (" + isa + " ?X " + dog + "))");
      conditional = sentence.isConditionalSentence();
      assertEquals(conditional, true);

    } catch (Exception e) {
      failWithException(e);
    }
    System.out.println("*** testVariableNameOptimization OK ***");
  }

  @Test
  public void testSubstituteNonDestructive() {
    System.out.println("\n*** testSubstituteNonDestructive ***");
    try {
      FormulaSentence sentence = getCyc().getObjectTool().makeCycSentence(
              "(" + likesAsFriend + " ?X ?Y)");
      FormulaSentence newSentence =
              sentence.substituteNonDestructive(CycObjectFactory.makeCycVariable(
              "X"), CycObjectFactory.makeCycVariable("Z"));
      assertFalse(sentence.equals(newSentence));
      assertFalse(sentence.equals(getCyc().getObjectTool().makeCycSentence(
              "(" + likesAsFriend + " ?Z ?Y)")));
      assertTrue(newSentence.equals(getCyc().getObjectTool().makeCycSentence(
              "(" + likesAsFriend + " ?Z ?Y)")));
    } catch (Exception e) {
      failWithException(e);
    }
    System.out.println("*** testSubstituteNonDestructive OK ***");
  }

  @Test
  public void testSubstituteDestructive() {
    System.out.println("\n*** testSubstituteDestructive ***");
    try {
      FormulaSentence sentence = getCyc().getObjectTool().makeCycSentence(
              "(" + likesAsFriend + " ?X ?Y)");
      sentence.substituteDestructive(CycObjectFactory.makeCycVariable(
              "X"), CycObjectFactory.makeCycVariable("Z"));
      assertTrue(sentence.equalsAtEL(getCyc().getObjectTool().makeCycSentence(
              "(" + likesAsFriend + " ?Z ?Y)")));
    } catch (Exception e) {
      failWithException(e);
    }
    System.out.println("*** testSubstituteDestructive OK ***");
  }

  @Test
  public void testEqualsAtEL() {
    System.out.println("\n*** testEqualsAtEL ***");
    try {
      FormulaSentence sentence = getCyc().getObjectTool().makeCycSentence(
              "(" + likesAsFriend + " ?X ?Y)");
      assertTrue(sentence.equalsAtEL(getCyc().getObjectTool().makeCycSentence(
              "(" + likesAsFriend + " ?Z ?Y)")));
      FormulaSentence sentence2 = getCyc().getObjectTool().makeCycSentence(
              "(" + and + "(" + isa + " ?X " + personString + ") (" + likesAsFriend + " ?X ?Y))");
      assertTrue(sentence2.equalsAtEL(getCyc().getObjectTool().makeCycSentence(
              "(" + and + "(" + isa + " ?Z " + personString + ") (" + likesAsFriend + " ?Z ?Y))")));
      FormulaSentence sentence3 = getCyc().getObjectTool().makeCycSentence(
              "(" + and + "(" + isa + " ?Y " + personString + ") (" + likesAsFriend + " ?X ?Y))");
      assertFalse(sentence3.equalsAtEL(getCyc().getObjectTool().makeCycSentence(
              "(" + and + "(" + isa + " ?X " + personString + ") (" + likesAsFriend + " ?X ?Y))")));
    } catch (Exception e) {
      failWithException(e);
    }
    System.out.println("*** testEqualsAtEL OK ***");
  }
  
  static void failWithException(Exception e) {
    e.printStackTrace();
    fail(e.getMessage());
  }
}
