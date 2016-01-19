package com.cyc.baseclient;

/*
 * #%L
 * File: GeneralUnitTest.java
 * Project: Base Client
 * %%
 * Copyright (C) 2013 - 2016 Cycorp, Inc.
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
// FIXME: TestSentences - nwinant
import com.cyc.baseclient.testing.TestUtils;
import com.cyc.base.CycAccess;
import com.cyc.base.CycAccessManager;
import com.cyc.base.exception.CycConnectionException;
import com.cyc.base.conn.CycConnection;
import com.cyc.base.conn.LeaseManager;
import com.cyc.base.conn.LeaseManager.LeaseEventObject;
import com.cyc.base.cycobject.CycConstant;
import com.cyc.base.cycobject.Fort;
import com.cyc.base.cycobject.CycList;
import com.cyc.base.cycobject.Naut;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import static org.junit.Assert.*;
import org.junit.Test;
import static com.cyc.baseclient.CommonConstants.*;
import static com.cyc.baseclient.CycObjectFactory.*;
import static com.cyc.baseclient.testing.TestConstants.*;
import static com.cyc.baseclient.testing.TestGuids.*;
import static com.cyc.baseclient.testing.TestSentences.*;
import com.cyc.baseclient.cycobject.ByteArray;
import com.cyc.base.cycobject.CycAssertion;
import com.cyc.baseclient.cycobject.CycConstantImpl;
import com.cyc.baseclient.cycobject.CycArrayList;
import com.cyc.baseclient.cycobject.CycListParser;
import com.cyc.baseclient.cycobject.NartImpl;
import com.cyc.baseclient.cycobject.NautImpl;
import com.cyc.base.cycobject.CycObject;
import com.cyc.baseclient.cycobject.CycSymbolImpl;
import com.cyc.baseclient.cycobject.DefaultCycObject;
import com.cyc.base.cycobject.ElMt;
import com.cyc.query.InferenceParameters;
import com.cyc.base.inference.InferenceResultSet;
import com.cyc.baseclient.connection.CfaslInputStream;
import com.cyc.baseclient.connection.CfaslOutputStream;
import com.cyc.baseclient.connection.CompactHlidConverter;
import com.cyc.baseclient.exception.CycApiClosedConnectionException;
import com.cyc.base.exception.CycApiException;
import com.cyc.base.cycobject.CycVariable;
import com.cyc.base.cycobject.FormulaSentence;
import com.cyc.base.cycobject.Nart;
import com.cyc.base.kbtool.InferenceTool;
import com.cyc.baseclient.connection.CycLeaseManager;
import com.cyc.baseclient.connection.DefaultSublWorkerSynch;
import com.cyc.baseclient.connection.SublWorkerSynch;
import com.cyc.baseclient.cycobject.CycFormulaSentence;
import com.cyc.baseclient.cycobject.GuidImpl;
import com.cyc.baseclient.datatype.NonAsciiStrings;
import com.cyc.baseclient.inference.params.DefaultInferenceParameters;
import com.cyc.baseclient.parser.CyclParserUtil;
import com.cyc.baseclient.datatype.StringUtils;
import com.cyc.baseclient.kbtool.CycObjectTool;
import com.cyc.baseclient.nl.ParaphraserFactory;
import com.cyc.baseclient.testing.TestConstants;
import com.cyc.baseclient.testing.TestSentences;
import static com.cyc.baseclient.testing.TestUtils.assumeNotOpenCyc;
import static com.cyc.baseclient.testing.TestUtils.isEnterpriseCyc;
import static com.cyc.baseclient.testing.TestUtils.skipTest;
import com.cyc.nl.Paraphraser;
import com.cyc.session.exception.OpenCycUnsupportedFeatureException;
import org.junit.AfterClass;
import org.junit.Before;

/**
 * Provides a unit test suite for the <tt>com.cyc.baseclient.api</tt> package
 *
 * @version $Id: UnitTest.java 135027 2011-07-15 21:35:08Z daves $
 * @author Stefano Bertolo
 * @author Stephen L. Reed
 */
public class GeneralUnitTest implements CycLeaseManager.CycLeaseManagerListener {

  /**
   * Indicates the use of a local CycConnection object to connect with a Cyc server.
   */
  public static final int LOCAL_CYC_CONNECTION = 1;
  /*
   * Indicates the use of Comm object as input to Cyc Access 
   */
  public static final int SOCKET_COMM_CYC_CONNECTION = 4;
  public static final int ROUND_ROBIN_SOCKET_COMM_CYC_CONNECTION = 5;
  /**
   * the connection mode
   */
  public static int connectionMode = LOCAL_CYC_CONNECTION;

  /**
   * Indicates whether unit tests should be performed only in binary api mode.
   */
  public static boolean performOnlyBinaryApiModeTests = false;
  //public static boolean performOnlyBinaryApiModeTests = true;
  /**
   * indicates whether to enforce strict cyc-api function filtering
   */
  public static boolean enforceApiFunctions = false;
  private static CycAccess cycAccess;

  @Before
  public void setup() throws CycConnectionException, CycApiException, IOException {
    if (cycAccess == null || cycAccess.isClosed()) {
      cycAccess = TestUtils.getCyc();
    }
  }

  private static CycAccess getCycAccess() throws CycConnectionException, CycApiException, IOException {
    return TestUtils.getCyc();
  }

  @AfterClass
  public static void tearDownClass() {
    if (cycAccess != null) {
      cycAccess.close();
    }
  }

  /**
   * Compares expected object to the test object without causing a unit test failure, reporting if
   * the parameters are not equal.
   *
   * @param expectedObject the expected object
   * @param testObject the test object
   */
  public static void nofailAssertEquals(Object expectedObject,
          Object testObject) {
    if (!expectedObject.equals(testObject)) {
      System.out.println(
              "Expected <" + expectedObject + "> \nfound <" + testObject);
    }
  }

  /**
   * Reports if the given boolean expression is false, without causing a unit test failure.
   *
   * @param testExpression the test expression
   * @param message the message to display when the test fails
   */
  public static void nofailAssertTrue(boolean testExpression,
          String message) {
    if (!testExpression) {
      System.out.println("Test expression not true\n" + message);
    }
  }

  /**
   * Tests the makeValidConstantName method.
   */
  @Test
  public void testMakeValidConstantName() {
    System.out.println("\n**** testMakeValidConstantName ****");

    String candidateName = "abc";
    assertEquals(candidateName, CycConstantImpl.makeValidConstantName(candidateName));
    candidateName = "()[]//abc";

    String expectedValidName = "______abc";
    assertEquals(expectedValidName, CycConstantImpl.makeValidConstantName(
            candidateName));
    System.out.println("**** testMakeValidConstantName OK ****");
  }

  @Test
  public void testMerge() throws Exception {
    System.out.println("\n**** testMerge ****");

    String candidateName = "Abc1";
    String candidateName2 = "Abc2";
    CycAccess cyc = cycAccess;
    CycConstant c = cyc.getLookupTool().findOrCreate(candidateName);
    CycConstant c2 = cyc.getLookupTool().findOrCreate(candidateName2);
    try {

      cyc.getAssertTool().assertIsa(c, PERSON);
      cyc.getAssertTool().assertIsa(c2, FEMALE);

      cyc.getAssertTool().merge(c2, c);
      System.out.println("Constant: " + c2);
      System.out.println("**** testMerge OK ****");
    } finally {
      cyc.getUnassertTool().killWithoutTranscript(c);
      cyc.getUnassertTool().killWithoutTranscript(c2);
    }
  }

  /**
   * Tests CycAccess initialization.
   */
  @Test
  public void testCycAccessInitialization() {
    System.out.println("\n**** testCycAccessInitialization ****");

    CycAccess cycAccess = null;

    System.out.println("CycAccess 1 closed, creating CycAccess 2");
    try {
      cycAccess = getCycAccess();
      assertNotNull(cycAccess.getServerInfo().getCycKbVersionString());
    } catch (ConnectException e) {
      e.printStackTrace();
      fail(e.toString());
    } catch (Throwable e) {
      e.printStackTrace();
      fail(e.toString());
    } finally {
      if (cycAccess != null) {
        cycAccess.close();
        cycAccess = null;
      }
    }

    System.out.println("CycAccess 2 closed, creating CycAccess 3");
    try {
      cycAccess = getCycAccess();
      assertNotNull(cycAccess.getServerInfo().getCycKbVersionString());
    } catch (ConnectException e) {
      e.printStackTrace();
      fail(e.toString());
    } catch (Throwable e) {
      e.printStackTrace();
      fail(e.toString());
    } finally {
      if (cycAccess != null) {
        cycAccess.close();
        cycAccess = null;
      }
    }

    System.out.println("CycAccess 3 closed, creating CycAccess 4");
    try {
      cycAccess = getCycAccess();
      assertNotNull(cycAccess.getServerInfo().getCycKbVersionString());
    } catch (ConnectException e) {
      e.printStackTrace();
      fail(e.toString());
    } catch (Throwable e) {
      e.printStackTrace();
      fail(e.toString());
    } finally {
      if (cycAccess != null) {
        cycAccess.close();
        cycAccess = null;
      }
    }

    System.out.println("CycAccess 4 closed, creating CycAccess 5");
    try {
      cycAccess = getCycAccess();
      assertNotNull(cycAccess.getServerInfo().getCycKbVersionString());
    } catch (ConnectException e) {
      e.printStackTrace();
      fail(e.toString());
    } catch (Throwable e) {
      e.printStackTrace();
      fail(e.toString());
    } finally {
      if (cycAccess != null) {
        cycAccess.close();
        cycAccess = null;
      }
    }

    System.out.println("CycAccess 5 closed, creating CycAccess 6");
    try {
      cycAccess = getCycAccess();
      assertNotNull(cycAccess.getServerInfo().getCycKbVersionString());
    } catch (ConnectException e) {
      e.printStackTrace();
      fail(e.toString());
    } catch (Throwable e) {
      e.printStackTrace();
      fail(e.toString());
    } finally {
      if (cycAccess != null) {
        cycAccess.close();
        cycAccess = null;
      }
    }

    System.out.println("CycAccess 6 closed, creating CycAccess 7");
    try {
      cycAccess = getCycAccess();
      assertNotNull(cycAccess.getServerInfo().getCycKbVersionString());
    } catch (ConnectException e) {
      e.printStackTrace();
      fail(e.toString());
    } catch (Throwable e) {
      e.printStackTrace();
      fail(e.toString());
    } finally {
      if (cycAccess != null) {
        cycAccess.close();
      }
    }
    System.out.println("CycAccess 7 closed");

    System.out.println("**** testCycAccessInitialization OK ****");
  }

  /**
   * Tests the fundamental aspects of the binary (cfasl) api connection to the OpenCyc server.
   */
  @Test
  public void testBinaryCycConnection1() {
    System.out.println("\n**** testBinaryCycConnection1 ****");

    CycArrayList command = new CycArrayList();
    command.add(makeCycSymbol("+"));
    command.add(2);
    command.add(3);

    Object[] response = {0, ""};
    final CycConnection cycConnection = cycAccess.getCycConnection();
    try {
      response = cycConnection.converse(command);
    } catch (Throwable e) {
      fail(e.toString());
    }

    assertEquals(true, response[0]);
    assertEquals(5, response[1]);

    // Test return of string.
    command = new CycArrayList();
    command.add(quote);
    command.add("abc");

    try {
      response = cycConnection.converse(command);
    } catch (Throwable e) {
      fail(e.toString());
    }

    assertEquals(true, response[0]);
    assertEquals("abc", response[1]);

    // Test return of symbolic expression.
    command = new CycArrayList();
    command.add(quote);

    CycList cycList2 = CycArrayList.makeCycList(makeCycSymbol("a"), makeCycSymbol(
            "b"));
    command.add(cycList2);

    CycArrayList cycList3 = new CycArrayList();
    cycList2.add(cycList3);
    cycList3.add(makeCycSymbol("c"));
    cycList3.add(makeCycSymbol("d"));

    CycArrayList cycList4 = new CycArrayList();
    cycList3.add(cycList4);
    cycList4.add(makeCycSymbol("e"));
    cycList3.add(makeCycSymbol("f"));
    cycList3.add(makeCycSymbol("?my-var"));

    try {
      response = cycConnection.converse(command);
    } catch (Throwable e) {
      fail(e.toString());
    }

    assertEquals(true, response[0]);
    assertEquals("(A B (C D (E) F ?MY-VAR))", response[1].toString());

    // Test return of improper list.
    command = new CycArrayList();
    command.add(quote);
    cycList2 = new CycArrayList();
    command.add(cycList2);
    cycList2.add(makeCycSymbol("A"));
    cycList2.setDottedElement(makeCycSymbol("B"));

    try {
//      cycAccess.traceOn();
      response = cycConnection.converse(command);
    } catch (Throwable e) {
      fail(e.toString());
    }

    assertEquals(true, response[0]);
    assertEquals("(A . B)", response[1].toString());

    // Test error return
    command = new CycArrayList();
    command.add(nil);

    try {
      response = cycConnection.converse(command);
    } catch (Throwable e) {
      fail(e.toString());
    }
    assertTrue(response[1].toString().contains("NIL"));

    System.out.println("**** testBinaryCycConnection1 OK ****");
  }

  /**
   * Tests the fundamental aspects of the binary (cfasl) api connection to the OpenCyc server.
   * CycAccess is set to null;
   */
  @Test
  public void testBinaryCycConnection2() {
    System.out.println("\n**** testBinaryCycConnection2 ****");

    CycConnection cycConnection = cycAccess.getCycConnection();

    // Test return of atom.
    CycArrayList command = new CycArrayList();
    command.add(makeCycSymbol("+"));
    command.add(2);
    command.add(3);

    Object[] response = {0, ""};

    try {
      response = cycConnection.converse(command);
    } catch (Throwable e) {
      fail(e.toString());
    }

    assertEquals(true, response[0]);
    assertEquals(5, response[1]);

    // Test return of string.
    command = new CycArrayList();
    command.add(quote);
    command.add("abc");

    try {
      response = cycConnection.converse(command);
    } catch (Throwable e) {
      fail(e.toString());
    }

    assertEquals(true, response[0]);
    assertEquals("abc", response[1]);

    // Test return of symbolic expression.
    command = new CycArrayList();
    command.add(quote);

    CycArrayList cycList2 = new CycArrayList();
    command.add(cycList2);
    cycList2.add(makeCycSymbol("a"));
    cycList2.add(makeCycSymbol("b"));

    CycArrayList cycList3 = new CycArrayList();
    cycList2.add(cycList3);
    cycList3.add(makeCycSymbol("c"));
    cycList3.add(makeCycSymbol("d"));

    CycArrayList cycList4 = new CycArrayList();
    cycList3.add(cycList4);
    cycList4.add(makeCycSymbol("e"));
    cycList3.add(makeCycSymbol("f"));

    try {
      response = cycConnection.converse(command);
    } catch (Throwable e) {
      fail(e.toString());
    }

    assertEquals(true, response[0]);
    assertEquals("(A B (C D (E) F))", response[1].toString());

    // Test return of improper list.
    command = new CycArrayList();
    command.add(quote);
    cycList2 = new CycArrayList();
    command.add(cycList2);
    cycList2.add(makeCycSymbol("A"));
    cycList2.setDottedElement(makeCycSymbol("B"));

    try {
      //cycConnection.trace = true;
      response = cycConnection.converse(command);

      //cycConnection.trace = false;
    } catch (Throwable e) {
      fail(e.toString());
    }

    assertEquals(true, response[0]);
    assertEquals("(A . B)", response[1].toString());

    // Test error return
    command = new CycArrayList();
    command.add(nil);

    try {
      response = cycConnection.converse(command);
    } catch (Throwable e) {
      fail(e.toString());
    }

    if (!response[1].toString().contains("NIL")) {
      System.out.println(response[1]);
    }

    // various error messages to effect that NIL is not defined in the API.
    assertTrue(response[1].toString().contains("NIL"));

    System.out.println("**** testBinaryCycConnection2 OK ****");
  }

  @Test
  public void testCycSymbolLocaleIndependence() {
    System.out.println("\n**** testCycSymbolLocaleIndependence ****");
    final Locale defaultLocale = Locale.getDefault();
    final String lowercaseName = "abcdefghijklmnopqrstuvwxyz1234567890-_";
    Locale.setDefault(Locale.ENGLISH);
    final CycSymbolImpl englishSymbol = makeCycSymbol(lowercaseName);
    Locale.setDefault(new Locale("tr"));
    final CycSymbolImpl turkishSymbol = makeCycSymbol(lowercaseName);
    Locale.setDefault(defaultLocale);
    assertEquals(englishSymbol.toString(), turkishSymbol.toString());
    System.out.println("**** testCycSymbolLocaleIndependence OK ****");
  }

  /**
   * Tests a portion of the CycAccess methods using the binary api connection.
   *
   * @param cycAccess the server connection handler
   */
  @Test
  public void testBinaryCycAccess1() {
    long startMilliseconds = System.currentTimeMillis();

    resetCycConstantCaches();

    // getConstantByName.
    CycConstant cycConstant = null;

    try {
      //cycAccess.traceOnDetailed();
      cycConstant = cycAccess.getLookupTool().getConstantByName(DOG.stringApiValue());
    } catch (Throwable e) {
      e.printStackTrace();
      fail(e.toString());
    }

    assertNotNull(cycConstant);
    assertEquals(DOG_GUID_STRING, cycConstant.getGuid().toString());

    // getConstantByGuid.
    try {
      cycConstant = cycAccess.getLookupTool().getConstantByGuid(makeGuid(DOG_GUID_STRING));
    } catch (Throwable e) {
      fail(e.toString());
    }

    assertNotNull(cycConstant);
    assertEquals("#$Dog", cycConstant.cyclify());
    assertEquals("Dog", cycConstant.getName());

    // getComment.
    String comment = null;

    try {
      CycConstant raindrop = cycAccess.getLookupTool().getKnownConstantByGuid(
              RAINDROP_GUID_STRING);
      comment = cycAccess.getLookupTool().getComment(raindrop);
    } catch (Throwable e) {
      e.printStackTrace();
      fail(e.toString());
    }

    assertNotNull(comment);
    assertEquals(
            "The collection of drops of liquid water emitted by clouds in instances of #$RainProcess.",
            comment);

    // getIsas.
    List isas = null;

    try {
      CycConstant dog = cycAccess.getLookupTool().getKnownConstantByGuid(DOG_GUID_STRING);
      isas = cycAccess.getLookupTool().getIsas(dog);
    } catch (Throwable e) {
      e.printStackTrace();
      fail(e.toString());
    }

    assertNotNull(isas);
    assertTrue(isas instanceof CycArrayList);
    isas = ((CycArrayList) isas).sort();

    try {
      CycConstant biologicalSpecies = cycAccess.getLookupTool().getKnownConstantByGuid(
              BIOLIGICAL_SPECIES_GUID_STRING);
      assertTrue(isas.contains(biologicalSpecies));
    } catch (Throwable e) {
      fail(e.toString());
    }

    isas = null;

    try {
      CycConstant dog = cycAccess.getLookupTool().getKnownConstantByGuid(DOG_GUID_STRING);
      isas = cycAccess.getLookupTool().getIsas(dog);
    } catch (Throwable e) {
      e.printStackTrace();
      fail(e.toString());
    }

    assertNotNull(isas);
    assertTrue(isas instanceof CycArrayList);
    isas = ((CycArrayList) isas).sort();

    try {
      CycConstant biologicalSpecies = cycAccess.getLookupTool().getKnownConstantByGuid(
              BIOLIGICAL_SPECIES_GUID_STRING);
      assertTrue(isas.contains(biologicalSpecies));
    } catch (Throwable e) {
      fail(e.toString());
    }

    long endMilliseconds = System.currentTimeMillis();
    System.out.println(
            "  " + (endMilliseconds - startMilliseconds) + " milliseconds");
  }

  /**
   * Tests a portion of the CycAccess methods using the binary api connection.
   */
  @Test
  public void testBinaryCycAccess2() throws CycConnectionException {
    long startMilliseconds = System.currentTimeMillis();
    System.out.println(cycAccess.getCycConnection().connectionInfo());
    resetCycConstantCaches();

    // getGenls.
    CycConstant dog = cycAccess.getLookupTool().getKnownConstantByGuid(DOG_GUID_STRING);
    List genls = cycAccess.getLookupTool().getGenls(dog);
    assertNotNull(genls);
    assertTrue(genls instanceof CycArrayList);
    genls = ((CycArrayList) genls).sort();
    assertTrue(genls.toString().contains("CanisGenus"));

    // getGenlPreds.
    CycConstant target = cycAccess.getLookupTool().getKnownConstantByGuid(TARGET_GUID_STRING);
    List genlPreds = cycAccess.getLookupTool().getGenlPreds(target);
    assertNotNull(genlPreds);
    assertTrue((genlPreds.toString().equals("(preActors)")) || (genlPreds.toString().equals(
            "(actors)")));

    // getAllGenlPreds.
    List allGenlPreds = cycAccess.getLookupTool().getAllGenlPreds(target);
    assertNotNull(allGenlPreds);
    assertTrue(allGenlPreds.size() > 2);

    // getArg1Formats.
    List arg1Formats = cycAccess.getLookupTool().getArg1Formats(target);
    assertNotNull(arg1Formats);
    assertEquals("(SetTheFormat)", arg1Formats.toString());

    // getArg1Formats.
    CycConstant constantName = cycAccess.getLookupTool().getKnownConstantByGuid(
            CONSTANT_NAME_GUID_STRING);
    arg1Formats = cycAccess.getLookupTool().getArg1Formats(constantName);
    assertNotNull(arg1Formats);
    assertEquals("(singleEntryFormatInArgs)", arg1Formats.toString());

    // getArg2Formats.
    CycConstant internalParts = cycAccess.getLookupTool().getKnownConstantByGuid(
            INTERNAL_PARTS_GUID_STRING);
    List arg2Formats = cycAccess.getLookupTool().getArg2Formats(internalParts);
    assertNotNull(arg2Formats);
    assertEquals("(SetTheFormat)", arg2Formats.toString());

    // getDisjointWiths.
    CycConstant vegetableMatter = cycAccess.getLookupTool().getKnownConstantByGuid(
              VEGETABLE_MATTER_GUID_STRING);
    List disjointWiths = cycAccess.getLookupTool().getDisjointWiths(vegetableMatter);
    assertNotNull(disjointWiths);
    assertTrue(disjointWiths.toString().indexOf("AnimalBLO") > 0);

    // getArg1Isas.
    CycConstant doneBy = cycAccess.getLookupTool().getKnownConstantByGuid(DONE_BY_GUID_STRING);
    List arg1Isas = cycAccess.getLookupTool().getArg1Isas(doneBy);
    assertNotNull(arg1Isas);
    assertEquals("(Event)", arg1Isas.toString());

    // getArg2Isas.
    List arg2Isas = cycAccess.getLookupTool().getArg2Isas(doneBy);
    assertNotNull(arg2Isas);
    assertTrue(arg2Isas.contains(SOMETHING_EXISTING));

    // getArgNIsas.
    List argNIsas = cycAccess.getLookupTool().getArgNIsas(doneBy, 1);
    assertNotNull(argNIsas);
    assertEquals("(Event)", argNIsas.toString());

    // getArgNGenls.
    CycConstant superTaxons = cycAccess.getLookupTool().getKnownConstantByGuid(
              SUPERTAXONS_GUID_STRING);
    List argGenls = cycAccess.getLookupTool().getArgNGenls(superTaxons, 2);
    assertNotNull(argGenls);
    assertEquals("(Organism-Whole)", argGenls.toString());

    // isCollection.
    assertTrue(cycAccess.getInspectorTool().isCollection(dog));

    // isCollection.
    assertFalse(cycAccess.getInspectorTool().isCollection(doneBy));

    // isCollection on a NAUT
    CycConstant fruitFn = cycAccess.getLookupTool().getKnownConstantByGuid(
            FRUIT_FN_GUID_STRING);
    CycConstant appleTree = cycAccess.getLookupTool().getKnownConstantByGuid(
            APPLE_TREE_GUID_STRING);
    Naut fruitFnAppleTreeNaut = new NautImpl(fruitFn, appleTree);
    assertTrue(cycAccess.getInspectorTool().isCollection(fruitFnAppleTreeNaut));

    // isCollection on a NAUT
    CycConstant cityNamedFn = cycAccess.getLookupTool().getKnownConstantByGuid(
            CITY_NAMED_FN_GUID_STRING);
    CycConstant swaziland = cycAccess.getLookupTool().getKnownConstantByGuid(
            SWAZILAND_GUID_STRING);
    Naut cityNamedFnNaut = new NautImpl(cityNamedFn, "swaziville", swaziland);
    assertFalse(cycAccess.getInspectorTool().isCollection(cityNamedFnNaut));

    // isCollection on a non-CycObject
    assertFalse(cycAccess.getInspectorTool().isCollection(7));

    // isBinaryPredicate doneBy.
    assertTrue(cycAccess.getInspectorTool().isBinaryPredicate(doneBy));

    // isBinaryPredicate dog.
    assertFalse(cycAccess.getInspectorTool().isBinaryPredicate(dog));
    
    // denots-of-string
    String brazilDenotationString = "Brazil";
    CycList brazilDenotations = cycAccess.getLookupTool().getDenotsOfString(brazilDenotationString);
    System.out.println(brazilDenotations.cyclify());
    assertTrue(brazilDenotations.contains(cycAccess.getLookupTool().getKnownConstantByGuid(
            TestConstants.BRAZIL.getGuid().getGuidString())));

    String sDakotaDenotationString = "South Dakota";
    CycList sDakotaDenotations = cycAccess.getLookupTool().getDenotsOfString(sDakotaDenotationString);
    System.out.println(sDakotaDenotations.cyclify());
    assertTrue(sDakotaDenotations.contains(SOUTH_DAKOTA));
    
    long endMilliseconds = System.currentTimeMillis();
    System.out.println(
            "  " + (endMilliseconds - startMilliseconds) + " milliseconds");
  }

  /**
   * Notifies the listener of the given Cyc API services lease event.
   *
   * @param evt the the given Cyc API services lease event
   */
  @Override
  public void notifyCycLeaseEvent(
          LeaseEventObject evt) {
    System.out.println("Notified of: " + evt.toString());
  }

  /**
   * Tests a portion of the CycAccess methods using the binary api connection.
   */
  @Test
  public void testBinaryCycAccess3_1() throws CycConnectionException {
    System.out.println("\n**** testBinaryCycAccess 3.1 ****");

    long startMilliseconds = System.currentTimeMillis();
    resetCycConstantCaches();

    // getComment.
    CycConstant brazil = cycAccess.getLookupTool().getKnownConstantByGuid(BRAZIL_GUID_STRING);
    assertEquals("#$Brazil", brazil.cyclify());
    String comment = cycAccess.getLookupTool().getComment(brazil);
    assertNotNull(comment);
    assertEquals("An instance of #$IndependentCountry.  #$Brazil is the "
            + "largest country in South America, and is bounded on the "
            + "northwest by #$Colombia; on the north by #$Venezuela, "
            + "#$Guyana, #$Suriname, and #$FrenchGuiana; on the east by "
            + "the #$AtlanticOcean; on the south by #$Uruguay; on the "
            + "southwest by #$Argentina and #$Paraguay; and on the west "
            + "by #$Bolivia and #$Peru.",
            comment);
    
    // getIsas.
    List isas = cycAccess.getLookupTool().getIsas(brazil);

    assertNotNull(isas);
    assertTrue(isas instanceof CycArrayList);
    assertTrue(isas.toString().indexOf("IndependentCountry") > 0);
    isas = ((CycArrayList) isas).sort();
    assertTrue(isas.toString().indexOf("IndependentCountry") > 0);
    
    System.out.println("**** testBinaryCycAccess 3.1 OK ****");
  }
  
  @Test
  public void testBinaryCycAccess3_2() throws CycConnectionException {
    System.out.println("\n**** testBinaryCycAccess 3.2 ****");
    long startMilliseconds = System.currentTimeMillis();
    resetCycConstantCaches();

    // getGenls.
    CycConstant dog = cycAccess.getLookupTool().getKnownConstantByGuid(DOG_GUID_STRING);
    List genls = cycAccess.getLookupTool().getGenls(dog);

    assertNotNull(genls);
    assertTrue(genls instanceof CycArrayList);
    genls = ((CycArrayList) genls).sort();
    assertTrue(genls.toString().contains("CanisGenus"));

    // getMinGenls.
    CycConstant lion = cycAccess.getLookupTool().getKnownConstantByGuid(LION_GUID_STRING);
    List minGenls = cycAccess.getLookupTool().getMinGenls(lion);

    assertNotNull(minGenls);
    assertTrue(minGenls instanceof CycArrayList);
    minGenls = ((CycArrayList) minGenls).sort();
    assertEquals("(FelidaeFamily)", minGenls.toString());

    // getMinGenls mt.
    minGenls = null;

    // #$BiologyVocabularyMt
    minGenls = cycAccess.getLookupTool().getMinGenls(lion,
            cycAccess.getLookupTool().getKnownConstantByGuid(BIOLOGY_VOCABULARY_MT_GUID_STRING));

    assertNotNull(minGenls);
    assertTrue(minGenls instanceof CycArrayList);
    minGenls = ((CycArrayList) minGenls).sort();
    assertEquals("(FelidaeFamily)", minGenls.toString());

    // getSpecs.
    CycConstant canineAnimal = cycAccess.getLookupTool().getKnownConstantByGuid(
            CANINE_ANIMAL_GUID_STRING);
    List specs = cycAccess.getLookupTool().getSpecs(canineAnimal);

    assertNotNull(specs);
    assertTrue(specs instanceof CycArrayList);
    final String specsString = specs.toString();
    for (final String name : Arrays.asList("CanisGenus", "Coyote-Animal", "Dog",
            "Fox", "Jackal")) {
      assertTrue(specsString.indexOf(name) > 0);
    }

    // getMaxSpecs.
    List maxSpecs = cycAccess.getLookupTool().getMaxSpecs(canineAnimal);

    assertNotNull(maxSpecs);
    assertTrue(maxSpecs instanceof CycArrayList);
    maxSpecs = ((CycArrayList) maxSpecs).sort();
    assertTrue(maxSpecs.toString().indexOf("CanisGenus") > 0);
    assertTrue(maxSpecs.toString().indexOf("Fox") > 0);

    // getGenlSiblings.
    List genlSiblings = cycAccess.getLookupTool().getGenlSiblings(dog);
    assertNotNull(genlSiblings);
    assertTrue(genlSiblings instanceof CycArrayList);
    genlSiblings = ((CycArrayList) genlSiblings).sort();
    assertTrue(genlSiblings.toString().contains("JuvenileAnimal"));


    /* long running.
     // getSiblings.
     List siblings = null;
     try {
     //  CycConstant dog = cycAccess.getLookupTool().getKnownConstantByGuid("bd58daa0-9c29-11b1-9dad-c379636f7270");
     siblings = cycAccess.getLookupTool().getSiblings(dog);
     assertNotNull(siblings);
     assertTrue(siblings instanceof CycArrayList);
     //  CycConstant gooseDomestic = cycAccess.getLookupTool().getKnownConstantByGuid("bd5ca864-9c29-11b1-9dad-c379636f7270");
     assertTrue(siblings.contains(gooseDomestic));
     //  CycConstant goatDomestic = cycAccess.getLookupTool().getKnownConstantByGuid("bd58e278-9c29-11b1-9dad-c379636f7270");
     assertTrue(siblings.contains(goatDomestic));
     }
     catch (Throwable e) {
     e.printStackTrace();
     fail(e.toString());
     }
     // getSpecSiblings.
     List specSiblings = null;
     try {
     //  CycConstant dog = cycAccess.getLookupTool().getKnownConstantByGuid("bd58daa0-9c29-11b1-9dad-c379636f7270");
     specSiblings = cycAccess.getLookupTool().getSpecSiblings(dog);
     assertNotNull(specSiblings);
     assertTrue(specSiblings instanceof CycArrayList);
     //  CycConstant gooseDomestic = cycAccess.getLookupTool().getKnownConstantByGuid("bd5ca864-9c29-11b1-9dad-c379636f7270");
     assertTrue(specSiblings.contains(gooseDomestic));
     //  CycConstant goatDomestic = cycAccess.getLookupTool().getKnownConstantByGuid("bd58e278-9c29-11b1-9dad-c379636f7270");
     assertTrue(specSiblings.contains(goatDomestic));
     }
     catch (Throwable e) {
     fail(e.toString());
     }
     */
    // getAllGenls.
    CycConstant existingObjectType = cycAccess.getLookupTool().getKnownConstantByGuid(
            EXISTING_OBJECT_TYPE_GUID_STRING);
    List allGenls = cycAccess.getLookupTool().getAllGenls(existingObjectType);
    assertNotNull(allGenls);
    assertTrue(allGenls instanceof CycArrayList);

    CycConstant objectType = cycAccess.getLookupTool().getKnownConstantByGuid(
            OBJECT_TYPE_GUID_STRING);

    assertTrue(allGenls.contains(objectType));
    assertTrue(allGenls.contains(THING));

    // getAllSpecs.
    List allSpecs = cycAccess.getLookupTool().getAllSpecs(canineAnimal);
    assertNotNull(allSpecs);
    assertTrue(allSpecs instanceof CycArrayList);

    CycConstant jackal = cycAccess.getLookupTool().getKnownConstantByGuid(JACKAL_GUID_STRING);
    assertTrue(allSpecs.contains(jackal));

    CycConstant retrieverDog = cycAccess.getLookupTool().getKnownConstantByGuid(
            RETRIEVER_DOG_GUID_STRING);
    assertTrue(allSpecs.contains(retrieverDog));

    // getAllGenlsWrt.
    CycConstant animal = cycAccess.getLookupTool().getKnownConstantByGuid(ANIMAL_GUID_STRING);
    List allGenlsWrt = cycAccess.getLookupTool().getAllGenlsWrt(dog, animal);
    assertNotNull(allGenlsWrt);
    assertTrue(allGenlsWrt instanceof CycArrayList);

    CycConstant carnivore = cycAccess.getLookupTool().getKnownConstantByGuid(
            CARNIVORE_ORDER_GUID_STRING);
    assertTrue(allGenlsWrt.contains(carnivore));

    CycConstant airBreathingVertebrate = cycAccess.getLookupTool().getKnownConstantByGuid(
            AIR_BREATHING_VERTEBRATE_GUID_STRING);
    assertTrue(allGenlsWrt.contains(airBreathingVertebrate));
    System.out.println("**** testBinaryCycAccess 3.2 OK ****");
  }
  
  @Test
  public void testBinaryCycAccess3_3() throws CycConnectionException {
    System.out.println("\n**** testBinaryCycAccess 3.3 ****");
    if (isEnterpriseCyc()) {
      // TODO: rewrite test to include EnterpriseCyc KB.
      skipTest(this, "testBinaryCycAccess3_3", "#$BushDog not present in EnterpriseCyc KB.");
    }

    CycConstant canineAnimal = cycAccess.getLookupTool().getKnownConstantByGuid(
            CANINE_ANIMAL_GUID_STRING);
    
    // getAllDependentSpecs.
    List allCanineAnimalDependentSpecs = cycAccess.getLookupTool().getAllDependentSpecs(canineAnimal);
    assertNotNull(allCanineAnimalDependentSpecs);
    assertTrue(allCanineAnimalDependentSpecs instanceof CycArrayList);
    CycConstant bushDog = cycAccess.getLookupTool().getKnownConstantByGuid(BUSH_DOG_GUID_STRING);
    assertTrue(allCanineAnimalDependentSpecs.contains(bushDog));
    System.out.println("**** testBinaryCycAccess 3.3 OK ****");
  }
  
  @Test
  public void testBinaryCycAccess3_4() throws CycConnectionException {
    System.out.println("\n**** testBinaryCycAccess 3.4 ****");
    CycConstant canineAnimal = cycAccess.getLookupTool().getKnownConstantByGuid(
            CANINE_ANIMAL_GUID_STRING);
    CycConstant animal = cycAccess.getLookupTool().getKnownConstantByGuid(ANIMAL_GUID_STRING);
    CycConstant dog = cycAccess.getLookupTool().getKnownConstantByGuid(DOG_GUID_STRING);

    // getSampleLeafSpecs.
    List sampleLeafSpecs = cycAccess.getLookupTool().getSampleLeafSpecs(canineAnimal, 3);

    assertNotNull(sampleLeafSpecs);
    assertTrue(sampleLeafSpecs instanceof CycArrayList);

    //System.out.println("sampleLeafSpecs: " + sampleLeafSpecsArrayList);
    assertTrue(sampleLeafSpecs.size() > 0);

    // isSpecOf.
    assertTrue(cycAccess.getInspectorTool().isSpecOf(dog, animal));

    // isGenlOf.
    CycConstant wolf = cycAccess.getLookupTool().getKnownConstantByGuid(WOLF_GUID_STRING);
    assertTrue(cycAccess.getInspectorTool().isGenlOf(canineAnimal, wolf));

    //cycAccess.traceOn();
    CycConstant domesticatedAnimal = cycAccess.getLookupTool().getKnownConstantByGuid(
            DOMESTICATED_ANIMAL_GUID_STRING);
    CycConstant tameAnimal = cycAccess.getLookupTool().getKnownConstantByGuid(
            CARNIVORE_GUID_STRING);
    assertTrue(cycAccess.getComparisonTool().areIntersecting(domesticatedAnimal, tameAnimal));

    // cycAccess.getLookupTool().getCycLeaseManager().removeListener(this);
    for (Map.Entry<InputStream, LeaseManager> kv : cycAccess.getCycConnection().getCycLeaseManagerCommMap().entrySet()) {
      kv.getValue().removeListener(this);
    }

    System.out.println("**** testBinaryCycAccess 3.4 OK ****");
  }

  /**
   * Tests a portion of the CycAccess methods using a CycAccess connection.
   */
  @Test
  public void testBinaryCycAccess4_1() throws CycConnectionException {
    System.out.println("\n**** testBinaryCycAccess 4.1 ****");
    long startMilliseconds = System.currentTimeMillis();
    resetCycConstantCaches();

    // getCollectionLeaves.
    //cycAccess.traceOnDetailed();
    CycConstant animal = cycAccess.getLookupTool().getKnownConstantByGuid(ANIMAL_GUID_STRING);
    List collectionLeaves = cycAccess.getLookupTool().getCollectionLeaves(animal);
    assertNotNull(collectionLeaves);
    assertTrue(collectionLeaves instanceof CycArrayList);
      //cycAccess.traceOff();

    // getWhyGenl.
    CycList whyGenl = null;

    CycConstant dog = cycAccess.getLookupTool().getKnownConstantByGuid(DOG_GUID_STRING);
    // CycConstant animal = cycAccess.getLookupTool().getKnownConstantByGuid(ANIMAL_GUID_STRING);
    whyGenl = cycAccess.getLookupTool().getWhyGenl(dog, animal);

    assertNotNull(whyGenl);
    System.out.println("whyGenl " + whyGenl);

    /*
     CycSymbolImpl whyGenlFirst = (CycSymbolImpl) ((CycArrayList) ((CycArrayList) whyGenl.first()).first()).second();
     CycSymbolImpl whyGenlLast = (CycSymbolImpl) ((CycArrayList) ((CycArrayList) whyGenl.last()).first()).third();
     try {
     //  CycConstant dog = cycAccess.getLookupTool().getKnownConstantByGuid("bd58daa0-9c29-11b1-9dad-c379636f7270");
     assertEquals(dog, whyGenlFirst);
     //  CycConstant animal = cycAccess.getLookupTool().getKnownConstantByGuid("bd58b031-9c29-11b1-9dad-c379636f7270");
     assertEquals(animal, whyGenlLast);
     }
     catch (Throwable e) {
     fail(e.toString());
     }
     */
    // getWhyCollectionsIntersect.
    CycConstant domesticatedAnimal = cycAccess.getLookupTool().getKnownConstantByGuid(
            DOMESTICATED_ANIMAL_GUID_STRING);
    CycConstant nonPersonAnimal = cycAccess.getLookupTool().getKnownConstantByGuid(
            NON_PERSON_ANIMAL_GUID_STRING);
    List whyCollectionsIntersect = cycAccess.getLookupTool().getWhyCollectionsIntersect(
            domesticatedAnimal, nonPersonAnimal);
    assertNotNull(whyCollectionsIntersect);
    assertTrue(whyCollectionsIntersect instanceof CycArrayList);
    System.out.println("whyCollectionsIntersect " + whyCollectionsIntersect);

    CycList expectedWhyCollectionsIntersect = cycAccess.getObjectTool().makeCycList(
            "(((" + GENLS_STRING + " " + DOMESTICATED_ANIMAL_STRING + " " + TAME_ANIMAL_STRING + ") :TRUE) "
            + "((" + GENLS_STRING + " " + TAME_ANIMAL_STRING + " " + NON_PERSON_ANIMAL_STRING + ") :TRUE))");

    /**
     * assertEquals(expectedWhyCollectionsIntersect.toString(), whyCollectionsIntersect.toString());
     * assertEquals(expectedWhyCollectionsIntersect, whyCollectionsIntersect);
     */
    // getLocalDisjointWith.
    CycConstant vegetableMatter = cycAccess.getLookupTool().getKnownConstantByGuid(
            VEGETABLE_MATTER_GUID_STRING);
    List localDisjointWiths = cycAccess.getLookupTool().getDisjointWiths(vegetableMatter);
    assertNotNull(localDisjointWiths);
    assertTrue(localDisjointWiths.toString().indexOf("AnimalBLO") > 0);

    // areDisjoint.
    CycConstant plant = cycAccess.getLookupTool().getKnownConstantByGuid(PLANT_GUID_STRING);
    assertTrue(cycAccess.getComparisonTool().areDisjoint(animal, plant));

    // getMinIsas.
    CycConstant wolf = cycAccess.getLookupTool().getKnownConstantByGuid(WOLF_GUID_STRING);
    List minIsas = cycAccess.getLookupTool().getMinIsas(wolf);

    CycConstant organismClassificationType = cycAccess.getLookupTool().getKnownConstantByGuid(
            ORGANISM_CLASSIFICATION_TYPE_GUID_STRING);
    assertTrue(minIsas.contains(organismClassificationType));

    // getInstances.
    CycConstant maleHuman = cycAccess.getLookupTool().getKnownConstantByGuid(
            MALE_HUMAN_GUID_STRING);
    List instances = cycAccess.getLookupTool().getInstances(maleHuman);
    assertTrue(instances instanceof CycArrayList);

    CycConstant plato = cycAccess.getLookupTool().getKnownConstantByGuid(PLATO_GUID_STRING);
    assertTrue(((CycArrayList) instances).contains(plato));

    // getAllIsa.
    //cycAccess.traceOn();
    // CycConstant animal = cycAccess.getLookupTool().getKnownConstantByGuid(ANIMAL_GUID_STRING);
    List allIsas = cycAccess.getLookupTool().getAllIsa(animal);

    //System.out.println(allIsas);
    //CycConstant organismClassificationType = cycAccess.getLookupTool().getKnownConstantByGuid(
    //        ORGANISM_CLASSIFICATION_TYPE_GUID_STRING);
    assertTrue(allIsas.contains(organismClassificationType));

    // getAllInstances.
    long endMilliseconds = System.currentTimeMillis();
    System.out.println(
            "  " + (endMilliseconds - startMilliseconds) + " milliseconds");
    System.out.println("**** testBinaryCycAccess 4.1 OK ****");
  }

  /**
   * Tests a portion of the CycAccess methods using a CycAccess connection.
   */
  @Test
  public void testBinaryCycAccess4_2() throws CycConnectionException {
    System.out.println("\n**** testBinaryCycAccess 4.2 ****");
    long startMilliseconds = System.currentTimeMillis();
    resetCycConstantCaches();
    CycConstant plant = cycAccess.getLookupTool().getKnownConstantByGuid(PLANT_GUID_STRING);
    CycConstant animal = cycAccess.getLookupTool().getKnownConstantByGuid(ANIMAL_GUID_STRING);
    boolean answer = cycAccess.getComparisonTool().areDisjoint(animal, plant);

    // CycConstant animal = cycAccess.getLookupTool().getKnownConstantByGuid(ANIMAL_GUID_STRING);
    //CycConstant plant = cycAccess.getLookupTool().getKnownConstantByGuid(PLANT_GUID_STRING);
    List allPlants = cycAccess.getLookupTool().getAllInstances(plant);

    CycConstant treatyOak = cycAccess.getLookupTool().getKnownConstantByGuid(
            TREATY_OAK_GUID_STRING);
    assertTrue(allPlants.contains(treatyOak));

    CycConstant burningBushOldTestament = cycAccess.getLookupTool().getKnownConstantByGuid(
            BURNING_BUSH_GUID_STRING);
    assertTrue(allPlants.contains(burningBushOldTestament));

    // isa.
    //CycConstant plant = cycAccess.getLookupTool().getKnownConstantByGuid(PLANT_GUID_STRING);
    answer = cycAccess.getInspectorTool().isa(treatyOak, plant);
    assertTrue(answer);

    final CycConstant term1 = cycAccess.getLookupTool().getKnownConstantByName(
            NTH_SUB_SITUATION_TYPE_OF_TYPE_FN_STRING);
    final CycConstant term2 = cycAccess.getLookupTool().getKnownConstantByName(
            PREPARING_FOOD_ITEM_FN_STRING);
    final CycConstant term3 = cycAccess.getLookupTool().getKnownConstantByName(
            SPAGHETTI_MARINARA_STRING);
    final CycConstant term4 = cycAccess.getLookupTool().getKnownConstantByName(
            FLUID_FLOW_COMPLETE_STRING);
    final CycConstant collection = cycAccess.getLookupTool().getKnownConstantByName(COLLECTION_STRING);
    final CycConstant mt = cycAccess.getLookupTool().getKnownConstantByName(
            HUMAN_ACTIVITIES_MT_STRING);
    final Nart nart1 = new NartImpl(term2, term3);
    final CycArrayList nartList = new CycArrayList();
    nartList.add(term1);
    nartList.add(nart1);
    nartList.add(term4);
    nartList.add(2);
    final Nart nart2 = new NartImpl(nartList);

    //(isa? (QUOTE (NthSubSituationTypeOfTypeFn (PreparingFoodItemFn SpaghettiMarinara) FluidFlow-Complete 2)) Collection HumanActivitiesMt)
    answer = cycAccess.getInspectorTool().isa(nart2, collection, mt);
    assertTrue(answer);

    // getWhyCollectionsIntersectParaphrase.
    ArrayList whyCollectionsIntersectParaphrase = null;

    //cycAccess.traceOn();
    //  CycConstant domesticatedAnimal = cycAccess.getLookupTool().getKnownConstantByGuid(              DOMESTICATED_ANIMAL_GUID_STRING);
    //  CycConstant nonPersonAnimal = cycAccess.getLookupTool().getKnownConstantByGuid(              NON_PERSON_ANIMAL_GUID_STRING);
    System.out.println("bypassing getWhyCollectionsIntersectParaphrase");

    /*
     whyCollectionsIntersectParaphrase =
     cycAccess.getLookupTool().getWhyCollectionsIntersectParaphrase(domesticatedAnimal, nonPersonAnimal);
     */
    /*
     assertNotNull(whyCollectionsIntersectParaphrase);
     String oneExpectedCollectionsIntersectParaphrase =
     "every domesticated animal (tame animal) is a tame animal";
     //System.out.println(whyCollectionsIntersectParaphrase);
     assertTrue(whyCollectionsIntersectParaphrase.contains(oneExpectedCollectionsIntersectParaphrase));
     */
    // getWhyGenlParaphrase.
    ArrayList whyGenlParaphrase = null;

    //cycAccess.traceOn();
    //  CycConstant dog = cycAccess.getLookupTool().getKnownConstantByGuid(DOG_GUID_STRING);
    //  CycConstant animal = cycAccess.getLookupTool().getKnownConstantByGuid(ANIMAL_GUID_STRING);
    System.out.println("bypassing getWhyGenlParaphrase");

    /*
     whyGenlParaphrase = cycAccess.getLookupTool().getWhyGenlParaphrase(dog, animal);
     */
    /*
     assertNotNull(whyGenlParaphrase);
     String oneExpectedGenlParaphrase =
     "every tame animal is a non-human animal";
    
     //for (int i = 0; i < whyGenlParaphrase.size(); i++) {
     //    System.out.println(whyGenlParaphrase.get(i));
     //}
    
     assertTrue(whyGenlParaphrase.contains(oneExpectedGenlParaphrase));
     */
    long endMilliseconds = System.currentTimeMillis();
    System.out.println(
            "  " + (endMilliseconds - startMilliseconds) + " milliseconds");
    System.out.println("**** testBinaryCycAccess 4.2 OK ****");
  }

  @Test
  public void testExecuteQueryWithClosedCycAccess() {
    try {
      final CycAccess cyc = cycAccess;
      final FormulaSentence query = cyc.getObjectTool().makeCycSentence(WHAT_IS_IN_AUSTIN_STRING);
      final CycVariable what = CycObjectFactory.makeCycVariable("WHAT");
      cyc.getCycConnection().close();
      cyc.getInferenceTool().queryVariable(what, query, EVERYTHING_PSC, null, 15000);
    } catch (CycApiClosedConnectionException e) {
      fail("Failed to recover from closed Cyc connection.");
    } catch (Throwable e) {
      fail(e.toString());
    }
  }

  /**
   * Tests a portion of the CycAccess methods using the given api connection.
   *
   * @throws com.cyc.base.exception.CycConnectionException
   */
  @Test
  public void testBinaryCycAccess5_1() throws CycConnectionException {
    System.out.println("\n**** testBinaryCycAccess 5.1 ****");
    long startMilliseconds = System.currentTimeMillis();
    resetCycConstantCaches();

    //cycAccess.traceOn();
    // findOrCreateNewPermanent.
    //cycAccess.traceNamesOn();
    cycAccess.getOptions().setCyclist(CYC_ADMINISTRATOR);
    cycAccess.getOptions().setKePurpose(GENERAL_CYC_KE);
    CycConstant cycConstant = cycAccess.getAssertTool().findOrCreateNewPermanent("CycAccessTestConstant");
    assertNotNull(cycConstant);
    assertEquals("CycAccessTestConstant", cycConstant.getName());

    // kill.
    cycAccess.getUnassertTool().kill(cycConstant);

    // assertComment.
    cycConstant = cycAccess.getAssertTool().findOrCreateNewPermanent("CycAccessTestConstant");
    assertNotNull(cycConstant);
    assertEquals("CycAccessTestConstant", cycConstant.getName());

    CycConstant baseKb = cycAccess.getLookupTool().getConstantByName(CommonConstants.BASE_KB.stringApiValue());
    assertNotNull(baseKb);
    assertEquals("BaseKB", baseKb.getName());

    String assertedComment = "A test comment";
    cycAccess.getAssertTool().assertComment(cycConstant, assertedComment, baseKb);
    String comment = cycAccess.getLookupTool().getComment(cycConstant);
    assertEquals(assertedComment, comment);

    cycAccess.getUnassertTool().kill(cycConstant);
    assertNull(cycAccess.getLookupTool().getConstantByName("CycAccessTestConstant"));

    // isValidConstantName.
    assertTrue(cycAccess.getInspectorTool().isValidConstantName("abc"));
    assertFalse(cycAccess.getInspectorTool().isValidConstantName(" abc"));
    assertFalse(cycAccess.getInspectorTool().isValidConstantName("[abc]"));

    // isConstantNameAvailable
    assertFalse(cycAccess.getInspectorTool().isConstantNameAvailable(AGENT_PARTIALLY_TANGIBLE.getName()));
    assertTrue(cycAccess.getInspectorTool().isConstantNameAvailable("myAgent"));

    // describeMicrotheory.
    ArrayList genlMts = new ArrayList();
    CycConstant modernMilitaryMt = cycAccess.getLookupTool().getKnownConstantByGuid(
            MODERN_MILITARY_MT_GUID_STRING);
    CycConstant microtheory = cycAccess.getLookupTool().getKnownConstantByGuid(
            MICROTHEORY_GUID_STRING);
    genlMts.add(modernMilitaryMt);
    CycConstant mt = cycAccess.getAssertTool().createMicrotheory("CycAccessTestMt",
            "a unit test comment for the CycAccessTestMt microtheory.",
            microtheory, genlMts);
    assertNotNull(mt);
    cycAccess.getUnassertTool().kill(mt);
    assertNull(cycAccess.getLookupTool().getConstantByName("CycAccessTestMt"));
    
    // countAllInstances
    CycConstant country = cycAccess.getLookupTool().getKnownConstantByGuid(COUNTRY_GUID_STRING);
    CycConstant worldGeographyMt = cycAccess.getLookupTool().getKnownConstantByGuid(
            WORLD_GEOGRAPHY_MT_GUID_STRING);
    assertTrue(cycAccess.getInspectorTool().countAllInstances(country, worldGeographyMt) > 0);
    System.out.println("**** testBinaryCycAccess 5.1 OK ****");
  }

  /**
   * Tests a portion of the CycAccess methods using the binary api connection.
   */
  @Test
  public void testBinaryCycAccess5_2() throws CycConnectionException {
    System.out.println("\n**** testBinaryCycAccess 5.2 ****");
    long startMilliseconds = System.currentTimeMillis();
    resetCycConstantCaches();
    // executeQuery
    FormulaSentence query1 = cycAccess.getObjectTool().makeCycSentence(WHAT_IS_IN_AUSTIN_STRING);
    CycConstant mt = (CycConstantImpl) EVERYTHING_PSC;

    InferenceParameters queryProperties1 = new DefaultInferenceParameters(
            cycAccess);
    InferenceResultSet response1 = cycAccess.getInferenceTool().executeQuery(query1,
            cycAccess.getObjectTool().makeElMt(mt), queryProperties1, 20000);
    assertNotNull(response1);

    //System.out.println("query: " + query + "\n  response: " + response);
    //queryProperties.setMaxNumber(4);
    queryProperties1.put(":max-time", 30);
    response1 = cycAccess.getInferenceTool().executeQuery(query1, cycAccess.getObjectTool().makeElMt(mt),
            queryProperties1, 20000);
    //System.out.println("query: " + query + "\n  response: " + response);  
    assertTrue(response1.getCurrentRowCount() > 0);

    CycConstant cycorp = cycAccess.getLookupTool().getKnownConstantByName("#$Cycorp");

    boolean findCycorp = false;
    while (response1.next()) {
      try {
        CycConstant res = response1.getConstant("?WHAT");
        if (res.equals(cycorp)) {
          findCycorp = true;
        }
      } catch (Exception ex) {

      }
    }

    assertTrue("Could not find Cycorp in Austin.", findCycorp);

    // executeQuery with query pragma
    FormulaSentence query2 = cycAccess.getObjectTool().makeCycSentence(WHAT_IS_IN_AUSTIN_STRING);
    mt = (CycConstantImpl) EVERYTHING_PSC;

    InferenceParameters queryProperties2 = new DefaultInferenceParameters(
            cycAccess);
    queryProperties2.put(":non-explanatory-sentence",
            cycAccess.getObjectTool().makeCycSentence(TestSentences.UNKNOWN_SENTENCE_FOOD_ORG.cyclify()));
    InferenceResultSet response2 = cycAccess.getInferenceTool().executeQuery(query2,
            cycAccess.getObjectTool().makeElMt(mt), queryProperties2, 20000);
    assertNotNull(response2);

    // askWithVariable
    FormulaSentence query3 = cycAccess.getObjectTool().makeCycSentence(
            WHAT_IS_IN_AUSTIN_STRING);
    CycVariable variable = makeCycVariable("?WHAT");
    mt = (CycConstantImpl) CommonConstants.EVERYTHING_PSC;
    InferenceParameters queryProperties3 = new DefaultInferenceParameters(
            cycAccess);
    CycList response3 = cycAccess.getInferenceTool().queryVariable(variable, query3, mt,
            queryProperties3);
    assertNotNull(response3);
    assertTrue(response3.contains(cycAccess.getLookupTool().getConstantByName(
            UT_AUSTIN.cyclify())));

    // askWithVariables
    FormulaSentence query4 = cycAccess.getObjectTool().makeCycSentence(
            TestSentences.OBJECT_FOUND_WHAT_WHERE.cyclify());
    CycArrayList variables = new CycArrayList();
    variables.add(makeCycVariable("?WHAT"));
    variables.add(makeCycVariable("?WHERE"));
    InferenceParameters queryProperties4 = new DefaultInferenceParameters(
            cycAccess);
    CycConstant universeDataMt = cycAccess.getLookupTool().getKnownConstantByGuid(
            UNIVERSE_DATA_MT_GUID_STRING);
    CycList response4 = cycAccess.getInferenceTool().queryVariables(variables, query4,
            universeDataMt, queryProperties4);
    assertNotNull(response4);

    // isQueryTrue
    //cycAccess.traceOn();
    FormulaSentence query5 = cycAccess.getObjectTool().makeCycSentence(
            UT_AUSTIN_IN_AUSTIN.cyclify()
    );
    mt = (CycConstantImpl) EVERYTHING_PSC;
    InferenceParameters queryProperties5 = new DefaultInferenceParameters(
            cycAccess);
    assertTrue(cycAccess.getInferenceTool().isQueryTrue(query5, mt, queryProperties5));
    query5 = cycAccess.getObjectTool().makeCycSentence(
            UT_AUSTIN_IN_HOUSTON.cyclify());
    assertTrue(!cycAccess.getInferenceTool().isQueryTrue(query5, mt, queryProperties5));

    long endMilliseconds = System.currentTimeMillis();
    System.out.println(
            "  " + (endMilliseconds - startMilliseconds) + " milliseconds");
  }

  /**
   * Tests a portion of the CycAccess methods using the binary api connection.
   *
   * @param cycAccess the server connection handler
   */
  @Test
  public void testBinaryCycAccess6() throws CycConnectionException {
    long startMilliseconds = System.currentTimeMillis();

    // Test sending a constant to Cyc.
    CycArrayList command = new CycArrayList();
    command.add(makeCycSymbol("identity"));
    command.add(COLLECTION);

    Object obj = cycAccess.converse().converseObject(command);
    assertNotNull(obj);
    assertTrue(obj instanceof CycConstantImpl);
    assertEquals(obj, COLLECTION);

    // Test isBackchainRequired, isBackchainEncouraged, isBackchainDiscouraged, isBackchainForbidden
    CycConstant keRequirement = cycAccess.getLookupTool().getKnownConstantByGuid(
            KE_REQUIREMENT_GUID_STRING);
    assertTrue(cycAccess.getInspectorTool().isBackchainRequired(keRequirement, BASE_KB));
    assertTrue(!cycAccess.getInspectorTool().isBackchainEncouraged(keRequirement, BASE_KB));
    assertTrue(!cycAccess.getInspectorTool().isBackchainDiscouraged(keRequirement, BASE_KB));
    assertTrue(!cycAccess.getInspectorTool().isBackchainForbidden(keRequirement, BASE_KB));

    CycConstant nearestIsa = cycAccess.getLookupTool().getKnownConstantByGuid(
            NEAREST_ISA_GUID_STRING);
    assertTrue(!cycAccess.getInspectorTool().isBackchainRequired(nearestIsa, BASE_KB));
    assertTrue(!cycAccess.getInspectorTool().isBackchainEncouraged(nearestIsa, BASE_KB));
    assertTrue(!cycAccess.getInspectorTool().isBackchainDiscouraged(nearestIsa, BASE_KB));
    assertTrue(cycAccess.getInspectorTool().isBackchainForbidden(nearestIsa, BASE_KB));

    // isWellFormedFormula
    assertTrue(cycAccess.getInspectorTool().isWellFormedFormula(cycAccess.getObjectTool().makeCycSentence(
            "(" + GENLS_STRING + " " + DOG_STRING + " " + ANIMAL_STRING + ")")));

    // Not true, but still well formed.
    assertTrue(cycAccess.getInspectorTool().isWellFormedFormula(cycAccess.getObjectTool().makeCycSentence(
            "(" + GENLS_STRING + " " + DOG_STRING + " " + PLANT_STRING + " )")));
    assertTrue(cycAccess.getInspectorTool().isWellFormedFormula(cycAccess.getObjectTool().makeCycSentence(
            "(" + GENLS_STRING + " ?X " + ANIMAL_STRING + ")")));
    assertTrue(!cycAccess.getInspectorTool().isWellFormedFormula(
            cycAccess.getObjectTool().makeCycSentence("(" + GENLS_STRING + " " + DOG_STRING + " " + BRAZIL_STRING + ")")));
    assertTrue(!cycAccess.getInspectorTool().isWellFormedFormula(
            cycAccess.getObjectTool().makeCycSentence("(" + GENLS_STRING + " ?X " + BRAZIL_STRING + ")")));

    // isEvaluatablePredicate
    assertTrue(cycAccess.getInspectorTool().isEvaluatablePredicate(DIFFERENT));

    CycConstant doneBy = cycAccess.getLookupTool().getKnownConstantByGuid(DONE_BY_GUID_STRING);
    assertTrue(!cycAccess.getInspectorTool().isEvaluatablePredicate(
            doneBy));

    // hasSomePredicateUsingTerm
    if (!cycAccess.isOpenCyc()) {
      // TODO: rewrite this without #$CIAWorldFactbook1995Mt
      CycConstant algeria = cycAccess.getLookupTool().getKnownConstantByGuid(
              ALGERIA_GUID_STRING);
      CycConstant percentOfRegionIs = cycAccess.getLookupTool().getKnownConstantByGuid(
              PERCENT_OF_REGION_IS_GUID_STRING);

      assertTrue(cycAccess.getLookupTool().hasSomePredicateUsingTerm(
              percentOfRegionIs,
              algeria,
              1,
              CIA_WORLD_FACTBOOK_1995_MT));

      assertTrue(cycAccess.getLookupTool().hasSomePredicateUsingTerm(
              percentOfRegionIs,
              algeria,
              1,
              INFERENCE_PSC));
      assertTrue(!cycAccess.getLookupTool().hasSomePredicateUsingTerm(
              percentOfRegionIs,
              algeria,
              2,
              CIA_WORLD_FACTBOOK_1995_MT));
    }

    // Test common constants.
    assertEquals(cycAccess.getLookupTool().getConstantByName("and"), AND);
    assertEquals(cycAccess.getLookupTool().getConstantByName("BaseKB"), BASE_KB);
    assertEquals(cycAccess.getLookupTool().getConstantByName("BinaryPredicate"),
            BINARY_PREDICATE);
    assertEquals(cycAccess.getLookupTool().getConstantByName("comment"), COMMENT);
    assertEquals(cycAccess.getLookupTool().getConstantByName("different"), DIFFERENT);
    assertEquals(cycAccess.getLookupTool().getConstantByName("elementOf"), ELEMENT_OF);
    assertEquals(cycAccess.getLookupTool().getConstantByName("genlMt"), GENL_MT);
    assertEquals(cycAccess.getLookupTool().getConstantByName("genls"), GENLS);
    assertEquals(cycAccess.getLookupTool().getConstantByName("isa"), ISA);
    assertEquals(cycAccess.getLookupTool().getConstantByName("numericallyEquals"),
            NUMERICALLY_EQUALS);
    assertEquals(cycAccess.getLookupTool().getConstantByName("or"), OR);
    assertEquals(cycAccess.getLookupTool().getConstantByName("PlusFn"), PLUS_FN);

    resetCycConstantCaches();

    assertEquals(cycAccess.getLookupTool().getConstantByName("and"), AND);
    assertEquals(cycAccess.getLookupTool().getConstantByName("BaseKB"), BASE_KB);
    assertEquals(cycAccess.getLookupTool().getConstantByName("BinaryPredicate"),
            BINARY_PREDICATE);
    assertEquals(cycAccess.getLookupTool().getConstantByName("comment"), COMMENT);
    assertEquals(cycAccess.getLookupTool().getConstantByName("different"), DIFFERENT);
    assertEquals(cycAccess.getLookupTool().getConstantByName("elementOf"), ELEMENT_OF);
    assertEquals(cycAccess.getLookupTool().getConstantByName("genlMt"), GENL_MT);
    assertEquals(cycAccess.getLookupTool().getConstantByName("genls"), GENLS);
    assertEquals(cycAccess.getLookupTool().getConstantByName("isa"), ISA);
    assertEquals(cycAccess.getLookupTool().getConstantByName("numericallyEquals"),
            NUMERICALLY_EQUALS);
    assertEquals(cycAccess.getLookupTool().getConstantByName("or"), OR);
    assertEquals(cycAccess.getLookupTool().getConstantByName("PlusFn"), PLUS_FN);

    long endMilliseconds = System.currentTimeMillis();
    System.out.println(
            "  " + (endMilliseconds - startMilliseconds) + " milliseconds");
  }

  /**
   * Tests a portion of the CycAccess methods using the binary api connection.
   *
   * TODO associated the Cyc user state with the java client uuid, then put these tests back.
   *
   * N O T E be sure that the test system is clean of the special symbols introduced in the test.
   * E.G. MY-MACRO, A, B, C
   */
  @Test
  public void testBinaryCycAccess7_1() throws CycConnectionException {
    long startMilliseconds = System.currentTimeMillis();
    resetCycConstantCaches();

    //cycAccess.traceOn();
    // SubL scripts
    //cycAccess.traceNamesOn();
    String script = null;
    // Java ByteArray  and SubL byte-vector are used only in the binary api.
    script = "(csetq my-byte-vector (vector 0 1 2 3 4 255))";

    Object responseObject = cycAccess.converse().converseObject(script);
    assertNotNull(responseObject);
    assertTrue(responseObject instanceof ByteArray);

    byte[] myBytes = {0, 1, 2, 3, 4, -1};
    ByteArray myByteArray = new ByteArray(myBytes);
    assertEquals(myByteArray, responseObject);

    CycArrayList command = new CycArrayList();
    command.add(makeCycSymbol("equalp"));
    command.add(makeCycSymbol("my-byte-vector"));

    CycArrayList command1 = new CycArrayList();
    command.add(command1);
    command1.add(quote);
    command1.add(myByteArray);
    assertTrue(cycAccess.converse().converseBoolean(command));

    CycList responseList;
    String responseString;
    boolean responseBoolean;

    // definition
    script = "(define my-copy-tree (tree) \n" + "  (ret \n" + "    (fif (atom tree) \n"
            + "         tree \n" + "         ;; else \n"
            + "         (cons (my-copy-tree (first tree)) \n"
            + "               (my-copy-tree (rest tree))))))";
    testObjectScript(cycAccess, script, makeCycSymbol("my-copy-tree"));
    script = "(define my-floor (x y) \n" + "  (clet (results) \n"
            + "    (csetq results (multiple-value-list (floor x y))) \n"
            + "    (ret results)))";
    testObjectScript(cycAccess, script, makeCycSymbol("my-floor"));
    script = "(my-floor 5 3)";
    testListScript(cycAccess, script, cycAccess.getObjectTool().makeCycList("(1 2)"));

    script = "(defmacro my-macro (a b c) \n" + "  (ret `(list ,a ,b ,c)))";
    testObjectScript(cycAccess, script, makeCycSymbol("my-macro"));
    script = "(my-macro " + DOG_STRING + " " + PLANT_STRING + "  " + BRAZIL_STRING + ")";
    testListScript(cycAccess, script, cycAccess.getObjectTool().makeCycList(
            "(" + DOG_STRING + " " + PLANT_STRING + "  " + BRAZIL_STRING + ")"));

    script = "(defmacro my-floor-macro (x y) \n" + "  (ret `(floor ,x ,y)))";
    testObjectScript(cycAccess, script, makeCycSymbol("my-floor-macro"));
    script = "(define my-floor-macro-test (x y) \n" + "    (ret (multiple-value-list (my-floor-macro x y))))";
    testObjectScript(cycAccess, script, makeCycSymbol("my-floor-macro-test"));
    script = "(my-floor-macro-test 5 3)";
    testListScript(cycAccess, script, cycAccess.getObjectTool().makeCycList("(1 2)"));

    script = "(defmacro my-floor-macro (x y) \n" + "  (ret `(floor ,x ,y)))";
    testObjectScript(cycAccess, script, makeCycSymbol("my-floor-macro"));
    script = "(my-floor-macro-test 5 3)";
    testListScript(cycAccess, script, cycAccess.getObjectTool().makeCycList("(1 2)"));

    /**
     * TODO: Use of the task processor means that CSETQ statements appear inside of a CLET wrapper.
     * Need a way to set global variables. Current method removes the effect of CSETQ if setting a
     * new variable.
     *
     */
    script = "(csetq a '(1 " + DOG_STRING + " " + PLANT_STRING + " ))";
    cycAccess.converse().converseVoid(script);
    script = "(symbol-value 'a)";
    testListScript(cycAccess, script, cycAccess.getObjectTool().makeCycList(
            "(1 " + DOG_STRING + " " + PLANT_STRING + " )"));

    script = "(csetq a -1)";
    cycAccess.converse().converseVoid(script);
    script = "(symbol-value 'a)";
    testObjectScript(cycAccess, script, -1);

    script = "(csetq a '(1 " + DOG_STRING + " " + PLANT_STRING + " ) \n" + "       b '(2 " + DOG_STRING + " " + PLANT_STRING + " ) \n" + "       c 3)";
    cycAccess.converse().converseVoid(script);
    script = "(symbol-value 'a)";
    testListScript(cycAccess, script, cycAccess.getObjectTool().makeCycList(
            "(1 " + DOG_STRING + " " + PLANT_STRING + " )"));
    script = "(symbol-value 'b)";
    testListScript(cycAccess, script, cycAccess.getObjectTool().makeCycList(
            "(2 " + DOG_STRING + " " + PLANT_STRING + " )"));
    script = "(symbol-value 'c)";
    testObjectScript(cycAccess, script, 3);

    script = "(clet ((a 0)) (cinc a) a)";
    assertEquals(1, cycAccess.converse().converseObject(script));

    script = "(clet ((a 0)) (cinc a 10) a)";
    assertEquals(10, cycAccess.converse().converseObject(script));

    script = "(clet ((a 0)) (cdec a) a)";
    assertEquals(-1, cycAccess.converse().converseObject(script));

    script = "(clet ((a 0)) (cdec a 10) a)";
    assertEquals(-10, cycAccess.converse().converseObject(script));

    script = "(cpush 4 a)";
    cycAccess.converse().converseVoid(script);
    script = "(symbol-value 'a)";
    testListScript(cycAccess, script, cycAccess.getObjectTool().makeCycList(
            "(4 1 " + DOG_STRING + " " + PLANT_STRING + " )"));

    script = "(cpop a)";
    cycAccess.converse().converseVoid(script);
    script = "(symbol-value 'a)";
    testListScript(cycAccess, script, cycAccess.getObjectTool().makeCycList(
            "(1 " + DOG_STRING + " " + PLANT_STRING + " )"));

    script = "(fi-set-parameter 'my-parm '(1 " + DOG_STRING + " " + PLANT_STRING + " ))";
    cycAccess.converse().converseVoid(script);
    script = "(symbol-value 'my-parm)";
    testListScript(cycAccess, script, cycAccess.getObjectTool().makeCycList(
            "(1 " + DOG_STRING + " " + PLANT_STRING + " )"));

    script = "(clet (a b) \n" + "  (csetq a '(1 2 3)) \n" + "  (csetq b (cpop a)) \n" + "  (list a b))";
    testListScript(cycAccess, script, cycAccess.getObjectTool().makeCycList("((2 3) (2 3))"));

    // boundp
    final Random random = new Random();
    CycSymbolImpl symbol = makeCycSymbol(
            "test-symbol-for-value-binding" + random.nextInt());
    assertTrue(!cycAccess.converse().converseBoolean("(boundp '" + symbol + ")"));
    cycAccess.converse().converseVoid("(csetq " + symbol + " nil)");
    assertTrue(cycAccess.converse().converseBoolean("(boundp '" + symbol + ")"));

    // fi-get-parameter
    script = "(csetq my-parm '(2 " + DOG_STRING + " " + PLANT_STRING + " ))";
    cycAccess.converse().converseVoid(script);
    script = "(fi-get-parameter 'my-parm)";
    testListScript(cycAccess, script, cycAccess.getObjectTool().makeCycList(
            "(2 " + DOG_STRING + " " + PLANT_STRING + " )"));

    // eval
    script = "(eval '(csetq a 4))";
    testObjectScript(cycAccess, script, 4);
    script = "(eval 'a)";
    testObjectScript(cycAccess, script, 4);

    script = "(eval (list 'csetq 'a 5))";
    testObjectScript(cycAccess, script, 5);
    script = "(eval 'a)";
    testObjectScript(cycAccess, script, 5);

    // apply
    script = "(apply #'+ '(1 2 3))";
    testObjectScript(cycAccess, script, 6);

    script = "(apply #'+ 1 2 '(3 4 5))";
    testObjectScript(cycAccess, script, 15);

    script = "(apply (function +) '(1 2 3))";
    testObjectScript(cycAccess, script, 6);

    script = "(apply (function +) 1 2 '(3 4 5))";
    testObjectScript(cycAccess, script, 15);

    script = "(apply #'my-copy-tree '((1 (2 (3)))))";
    testListScript(cycAccess, script, cycAccess.getObjectTool().makeCycList("(1 (2 (3)))"));

    // funcall
    script = "(funcall #'+ 1 2 3)";
    testObjectScript(cycAccess, script, 6);

    script = "(funcall (function +) 1 2 3)";
    final int expected = 6;
    testObjectScript(cycAccess, script, expected);

    script = "(funcall #'first '(1 (2 (3))))";
    responseObject = cycAccess.converse().converseObject(script);
    assertEquals("1", responseObject.toString());

    script = "(funcall #'my-copy-tree '(1 (2 (3))))";
    testListScript(cycAccess, script, cycAccess.getObjectTool().makeCycList("(1 (2 (3)))"));

    // multiple values
    script = "(multiple-value-list (floor 5 3))";
    testListScript(cycAccess, script, cycAccess.getObjectTool().makeCycList("(1 2)"));

    script = "(csetq answer nil)";
    testObjectScript(cycAccess, script, nil);

    script = "(cmultiple-value-bind (a b) \n" + "    (floor 5 3) \n" + "  (csetq answer (list a b)))";
    testListScript(cycAccess, script, cycAccess.getObjectTool().makeCycList("(1 2)"));
    script = "(symbol-value 'answer)";
    testListScript(cycAccess, script, cycAccess.getObjectTool().makeCycList("(1 2)"));

    script = "(define my-multiple-value-fn (arg1 arg2) \n"
            + "  (ret (values arg1 arg2 (list arg1 arg2) 0)))";
    testObjectScript(cycAccess, script, makeCycSymbol(
            "my-multiple-value-fn"));

    script = "(my-multiple-value-fn " + BRAZIL_STRING + " " + DOG_STRING + ")";
    testObjectScript(cycAccess, script, cycAccess.getLookupTool().getKnownConstantByGuid(
            BRAZIL_GUID_STRING));

    script = "(cmultiple-value-bind (a b c d) \n" + "    (my-multiple-value-fn " + BRAZIL_STRING + " " + DOG_STRING + ") \n"
            + "  (csetq answer (list a b c d)))";
    testListScript(
            cycAccess, script, cycAccess.getObjectTool().makeCycList(
                    "(" + BRAZIL_STRING + " " + DOG_STRING + " (" + BRAZIL_STRING + " " + DOG_STRING + ") 0)"));
    script = "(symbol-value 'answer)";
    testListScript(
            cycAccess, script, cycAccess.getObjectTool().makeCycList(
                    "(" + BRAZIL_STRING + " " + DOG_STRING + " (" + BRAZIL_STRING + " " + DOG_STRING + ") 0)"));

    // arithmetic
    script = "(add1 2)";
    testObjectScript(cycAccess, script, 3);

    script = "(eq (add1 2) 3)";
    assertTrue(cycAccess.converse().converseBoolean(script));

    script = "(sub1 10)";
    testObjectScript(cycAccess, script, 9);

    script = "(eq (sub1 10) 9)";
    assertTrue(cycAccess.converse().converseBoolean(script));

    // sequence
    script = "(csetq a nil)";
    testObjectScript(cycAccess, script, nil);

    script = "(progn (csetq a nil) (csetq a (list a)) (csetq a (list a)))";
    cycAccess.converse().converseVoid(script);
    script = "(symbol-value 'a)";
    testListScript(cycAccess, script, cycAccess.getObjectTool().makeCycList("((nil))"));

    // sequence with variable bindings
    script = "(clet (a b) " + "  (csetq a 1) " + "  (csetq b (+ a 3)) " + "  b)";
    testObjectScript(cycAccess, script, 4);

    script = "(clet ((a nil)) " + "  (cpush 1 a) " + "  a)";
    testListScript(cycAccess, script, cycAccess.getObjectTool().makeCycList("(1)"));

    script = "(clet (a b) " + "  (csetq a '(1 2 3)) " + "  (csetq b (cpop a)) " + "  (list a b))";
    testListScript(cycAccess, script, cycAccess.getObjectTool().makeCycList("((2 3) (2 3))"));

    script = "(clet ((a 1) " + "       (b (add1 a)) " + "       (c (sub1 b))) " + "  c)";
    testObjectScript(cycAccess, script, 1);

    // boolean expressions
    script = "(cand t nil t)";
    responseBoolean = cycAccess.converse().converseBoolean(script);
    assertTrue(!responseBoolean);

    script = "(cand t t t)";
    responseBoolean = cycAccess.converse().converseBoolean(script);
    assertTrue(responseBoolean);

    script = "(cand t)";
    responseBoolean = cycAccess.converse().converseBoolean(script);
    assertTrue(responseBoolean);

    script = "(cand nil)";
    responseBoolean = cycAccess.converse().converseBoolean(script);
    assertTrue(!responseBoolean);

    script = "(cand t " + DOG_STRING + ")";
    testObjectScript(cycAccess, script, t);

    script = "(cor t nil t)";
    responseBoolean = cycAccess.converse().converseBoolean(script);
    assertTrue(responseBoolean);

    script = "(cor nil nil nil)";
    responseBoolean = cycAccess.converse().converseBoolean(script);
    assertTrue(!responseBoolean);

    script = "(cor t)";
    responseBoolean = cycAccess.converse().converseBoolean(script);
    assertTrue(responseBoolean);

    script = "(cor nil)";
    responseBoolean = cycAccess.converse().converseBoolean(script);
    assertTrue(!responseBoolean);

    script = "(cor nil " + PLANT_STRING + " )";
    testObjectScript(cycAccess, script, t);

    script = "(cnot nil)";
    responseBoolean = cycAccess.converse().converseBoolean(script);
    assertTrue(responseBoolean);

    script = "(cnot t)";
    responseBoolean = cycAccess.converse().converseBoolean(script);
    assertTrue(!responseBoolean);

    script = "(cnot (cand t nil))";
    responseBoolean = cycAccess.converse().converseBoolean(script);
    assertTrue(responseBoolean);

    script = "(cand (cnot nil) (cor t nil))";
    responseBoolean = cycAccess.converse().converseBoolean(script);
    assertTrue(responseBoolean);

    // conditional sequencing
    script = "(csetq answer nil)";
    testObjectScript(cycAccess, script, nil);
    script = "(pcond ((eq 0 0) \n" + "        (csetq answer \"clause 1 true\")) \n"
            + "       ((> 1 4) \n" + "        (csetq answer \"clause 2 true\")) \n"
            + "       (t \n" + "        (csetq answer \"clause 3 true\")))";
    responseString = cycAccess.converse().converseString(script);
    assertEquals("clause 1 true",
            responseString);
    script = "(symbol-value 'answer)";
    responseString = cycAccess.converse().converseString(script);
    assertEquals("clause 1 true",
            responseString);

    script = "(pcond ((eq 1 0) \n" + "        (csetq answer \"clause 1 true\")) \n"
            + "       ((> 5 4) \n" + "        (csetq answer \"clause 2 true\")) \n"
            + "       (t \n" + "        (csetq answer \"clause 3 true\")))";
    responseString = cycAccess.converse().converseString(script);
    assertEquals("clause 2 true",
            responseString);
    script = "(symbol-value 'answer)";
    responseString = cycAccess.converse().converseString(script);
    assertEquals("clause 2 true",
            responseString);

    script = "(pcond ((eq 1 0) \n" + "        (csetq answer \"clause 1 true\")) \n"
            + "       ((> 1 4) \n" + "        (csetq answer \"clause 2 true\")) \n"
            + "       (t \n" + "        (csetq answer \"clause 3 true\")))";
    responseString = cycAccess.converse().converseString(script);
    assertEquals("clause 3 true",
            responseString);
    script = "(symbol-value 'answer)";
    responseString = cycAccess.converse().converseString(script);
    assertEquals("clause 3 true",
            responseString);

    script = "(pif (string= \"abc\" \"abc\") \n" + "     (csetq answer \"clause 1 true\") \n"
            + "     ;; else \n" + "     (csetq answer \"clause 2 true\"))";
    responseString = cycAccess.converse().converseString(script);
    assertEquals("clause 1 true",
            responseString);
    script = "(symbol-value 'answer)";
    responseString = cycAccess.converse().converseString(script);
    assertEquals("clause 1 true",
            responseString);

    script = "(pif (string> \"abc\" \"abc\") \n" + "     (csetq answer \"clause 1 true\") \n"
            + "     ;; else \n" + "     (csetq answer \"clause 2 true\"))";
    responseString = cycAccess.converse().converseString(script);
    assertEquals("clause 2 true",
            responseString);
    script = "(symbol-value 'answer)";
    responseString = cycAccess.converse().converseString(script);
    assertEquals("clause 2 true",
            responseString);

    script = "(csetq answer \n" + "       (fif (string= \"abc\" \"abc\") \n"
            + "            \"clause 1 true\" \n" + "            ;; else \n"
            + "            \"clause 2 true\"))";
    responseString = cycAccess.converse().converseString(script);
    assertEquals("clause 1 true",
            responseString);
    script = "(symbol-value 'answer)";
    responseString = cycAccess.converse().converseString(script);
    assertEquals("clause 1 true",
            responseString);

    script = "(csetq answer \n" + "       (fif (string> \"abc\" \"abc\") \n"
            + "            \"clause 1 true\" \n" + "            ;; else \n" + "            \"clause 2 true\"))";
    responseString = cycAccess.converse().converseString(script);
    assertEquals("clause 2 true",
            responseString);
    script = "(symbol-value 'answer)";
    responseString = cycAccess.converse().converseString(script);
    assertEquals("clause 2 true",
            responseString);

    script = "(progn \n" + "  (csetq answer \"clause 1 true\") \n"
            + "  (pwhen (string= \"abc\" \"abc\") \n"
            + "         (csetq answer \"clause 2 true\")))";
    responseString = cycAccess.converse().converseString(script);
    assertEquals("clause 2 true",
            responseString);
    script = "(symbol-value 'answer)";
    responseString = cycAccess.converse().converseString(script);
    assertEquals("clause 2 true",
            responseString);

    script = "(progn \n" + "  (csetq answer \"clause 1 true\") \n"
            + "  (pwhen (string> \"abc\" \"abc\") \n"
            + "         (csetq answer \"clause 2 true\")))";
    testObjectScript(cycAccess, script, nil);
    script = "(symbol-value 'answer)";
    responseString = cycAccess.converse().converseString(script);
    assertEquals("clause 1 true",
            responseString);

    script = "(progn \n" + "  (csetq answer \"clause 1 true\") \n"
            + "  (punless (string> \"abc\" \"abc\") \n"
            + "           (csetq answer \"clause 2 true\")))";
    responseString = cycAccess.converse().converseString(script);
    assertEquals("clause 2 true",
            responseString);
    script = "(symbol-value 'answer)";
    responseString = cycAccess.converse().converseString(script);
    assertEquals("clause 2 true",
            responseString);

    script = "(progn \n" + "  (csetq answer \"clause 1 true\") \n"
            + "  (punless (string= \"abc\" \"abc\") \n"
            + "           (csetq answer \"clause 2 true\")))";
    testObjectScript(cycAccess, script, nil);
    script = "(symbol-value 'answer)";
    responseString = cycAccess.converse().converseString(script);
    assertEquals("clause 1 true",
            responseString);

    // iteration
    script = "(csetq answer nil)";
    testObjectScript(cycAccess, script, nil);

    script = "(clet ((i 11)) \n" + "  (csetq answer -10) \n"
            + "  ;;(break \"environment\") \n" + "  (while (> i 0) \n"
            + "    (cdec i) \n" + "    (cinc answer)))";
    cycAccess.converse().converseVoid(script);
    script = "(symbol-value 'answer)";
    testObjectScript(cycAccess, script, 1);

    script = "(csetq answer nil)";
    testObjectScript(cycAccess, script, nil);
    script = "(progn \n" + "  (cdo ((x 0 (add1 x)) \n" + "        (y (+ 0 1) (+ y 2)) \n"
            + "        (z -10 (- z 1))) \n" + "       ((> x 3)) \n"
            + "    (cpush (list 'x x 'y y 'z z) answer)) \n" + "  (csetq answer (nreverse answer)))";
    testListScript(cycAccess, script,
            cycAccess.getObjectTool().makeCycList("((x 0 y 1 z -10) " + " (x 1 y 3 z -11) "
                    + " (x 2 y 5 z -12) " + " (x 3 y 7 z -13))"));

    script = "(csetq answer nil)";
    testObjectScript(cycAccess, script, nil);
    script = "(progn \n" + "  (clet ((x '(1 2 3))) \n" + "    (cdo nil ((null x) (csetq x 'y)) \n"
            + "      (cpush x answer) \n" + "      (cpop x)) \n" + "    x) \n"
            + "  (csetq answer (reverse answer)))";
    testListScript(cycAccess, script,
            cycAccess.getObjectTool().makeCycList("((1 2 3) " + " (2 3) " + " (3))"));

    script = "(csetq answer nil)";
    testObjectScript(cycAccess, script, nil);
    script = "(cdolist (x '(1 2 3 4)) \n" + "  (cpush x answer))";
    assertEquals(nil, cycAccess.converse().converseObject(script));
    script = "(symbol-value 'answer)";
    testListScript(cycAccess, script, cycAccess.getObjectTool().makeCycList("(4 3 2 1)"));

    // mapping
    script = "(mapcar #'list '(a b c))";
    testListScript(cycAccess, script, cycAccess.getObjectTool().makeCycList("((a) (b) (c))"));

    script = "(mapcar #'list '(a b c) '(d e f))";
    testListScript(cycAccess, script, cycAccess.getObjectTool().makeCycList(
            "((a d) (b e) (c f))"));

    script = "(mapcar #'eq '(a b c) '(d b f))";
    testListScript(cycAccess, script, cycAccess.getObjectTool().makeCycList("(nil t nil)"));

    script = "(csetq answer nil)";
    testObjectScript(cycAccess, script, nil);

    long endMilliseconds = System.currentTimeMillis();
    System.out.println(
            "  " + (endMilliseconds - startMilliseconds) + " milliseconds");
  }

  @Test
  public void testBinaryCycAccess7_2() throws CycConnectionException {
    TestUtils.assumeNotObfuscated();
    long startMilliseconds = System.currentTimeMillis();
    resetCycConstantCaches();

    String script;
    Object responseObject;
    CycList responseList;

    script = "(csetq my-small-dictionary nil)";
    testObjectScript(cycAccess, script, nil);

    // Wrap the dictionary assignment in a progn that returns nil, to avoid sending the
    // dictionary itself back to the client, where it is not supported.
    cycAccess.converse().converseVoid(
            "(progn (csetq my-small-dictionary (new-dictionary #'eq 3)) nil)");
    script = "(progn \n" + "  (dictionary-enter my-small-dictionary 'a 1) \n"
            + "  (dictionary-enter my-small-dictionary 'b 2) \n"
            + "  (dictionary-enter my-small-dictionary 'c 3))";
    testObjectScript(cycAccess, script, makeCycSymbol("c"));

    script = "(define my-mapdictionary-fn (key value) \n"
            + "  (cpush (list key value) answer) \n"
            + "  (ret nil))";
    testObjectScript(cycAccess, script, makeCycSymbol("my-mapdictionary-fn"));

    script = "(mapdictionary my-small-dictionary #'my-mapdictionary-fn)";
    testObjectScript(cycAccess, script, nil);
    script = "(symbol-value 'answer)";
    responseList = cycAccess.converse().converseList(script);
    assertTrue(responseList.contains(cycAccess.getObjectTool().makeCycList("(a 1)")));
    assertTrue(responseList.contains(cycAccess.getObjectTool().makeCycList("(b 2)")));
    assertTrue(responseList.contains(cycAccess.getObjectTool().makeCycList("(c 3)")));

    script = "(csetq my-large-dictionary nil)";
    testObjectScript(cycAccess, script, nil);
    script = "(progn (csetq my-large-dictionary (new-dictionary #'eq 200)) nil)";
    responseObject = cycAccess.converse().converseObject(script);
    script = "(clet ((cities (remove-duplicates \n" + "                 (with-all-mts \n"
            + "                   (instances " + INDEPENDENT_COUNTRY_STRING + ")))) \n"
            + "        capital-city) \n" + "  (cdolist (city cities) \n"
            + "    (csetq capital-city (pred-values-in-any-mt city " + CAPITAL_CITY_STRING + ")) \n"
            + "    (dictionary-enter my-large-dictionary \n" + "                      city \n"
            + "                      (fif (consp capital-city) \n"
            + "                           (first capital-city) \n"
            + "                           ;; else \n" + "                           nil))))";
    responseObject = cycAccess.converse().converseObject(script);

    script = "(mapdictionary my-large-dictionary #'my-mapdictionary-fn)";
    testObjectScript(cycAccess, script, nil);
    script = "(symbol-value 'answer)";
    responseList = cycAccess.converse().converseList(script);
    assertTrue(responseList.contains(cycAccess.getObjectTool().makeCycList(
            "(" + FRANCE_STRING + " " + CITY_OF_PARIS_FRANCE_STRING + ")")));

    script = "(define my-parameterized-mapdictionary-fn (key value args) \n"
            + "  (cpush (list key value args) answer) \n" + "  (ret nil))";
    testObjectScript(cycAccess, script, makeCycSymbol(
            "my-parameterized-mapdictionary-fn"));

    script = "(mapdictionary-parameterized my-small-dictionary #'my-parameterized-mapdictionary-fn '(\"x\"))";
    testObjectScript(cycAccess, script, nil);
    script = "(symbol-value 'answer)";
    responseList = cycAccess.converse().converseList(script);
    assertTrue(responseList.contains(cycAccess.getObjectTool().makeCycList("(a 1 (\"x\"))")));
    assertTrue(responseList.contains(cycAccess.getObjectTool().makeCycList("(b 2 (\"x\"))")));
    assertTrue(responseList.contains(cycAccess.getObjectTool().makeCycList("(c 3 (\"x\"))")));

    script = "(mapdictionary-parameterized my-large-dictionary #'my-parameterized-mapdictionary-fn '(1 2))";
    testObjectScript(cycAccess, script, nil);
    script = "(symbol-value 'answer)";
    responseList = cycAccess.converse().converseList(script);
    assertTrue(responseList.contains(cycAccess.getObjectTool().makeCycList(
            "(" + FRANCE_STRING + " " + CITY_OF_PARIS_FRANCE_STRING + " (1 2))")));

    // ccatch and throw
    script = "(define my-super () \n" + "  (clet (result) \n" + "    (ccatch :abort \n"
            + "      result \n" + "      (my-sub) \n" + "      (csetq result 0)) \n"
            + "  (ret result)))";
    testObjectScript(cycAccess, script, makeCycSymbol("my-super"));

    script = "(define my-sub () \n" + "  (clet ((a 1) (b 2)) \n" + "  (ignore a b) \n"
            + "  (ret (throw :abort 99))))";
    testObjectScript(cycAccess, script, makeCycSymbol("my-sub"));
    script = "(my-super)";
    testObjectScript(cycAccess, script, 99);

    // ignore-errors, cunwind-protect
    //cycAccess.traceOn();
    script = "(clet (result) \n" + "  (ignore-errors \n" + "    (cunwind-protect \n"
            + "	(/ 1 0) \n" + "      (csetq result \"protected\"))) \n"
            + "  result)";
    testObjectScript(cycAccess, script, "protected");

    // get-environment
    script = "(csetq a nil)";
    testObjectScript(cycAccess, script, nil);
    script = "(csetq b -1)";
    testObjectScript(cycAccess, script, -1);

    // cdestructuring-bind
    script = "(cdestructuring-bind () '() (print 'foo))";
    testObjectScript(cycAccess, script, makeCycSymbol("foo"));

    script = "(cdestructuring-bind (&whole a) () (print 'foo))";
    testObjectScript(cycAccess, script, makeCycSymbol("foo"));

    script = "(cdestructuring-bind (&whole a b c) '(1 2) (print (list a b c)))";
    responseList = cycAccess.converse().converseList(script);
    assertEquals(responseList, cycAccess.getObjectTool().makeCycList("((1 2) 1 2)"));

    script = "(cdestructuring-bind (a b . c) '(1 2 3 4) (print (list a b c)))";
    responseList = cycAccess.converse().converseList(script);
    assertEquals(responseList, cycAccess.getObjectTool().makeCycList("(1 2 (3 4))"));

    script = "(cdestructuring-bind (&optional a) '(1) (print (list a)))";
    responseList = cycAccess.converse().converseList(script);
    assertEquals(responseList, cycAccess.getObjectTool().makeCycList("(1)"));

    script = "(cdestructuring-bind (a &optional b) '(1 2) (print (list a b)))";
    responseList = cycAccess.converse().converseList(script);
    assertEquals(responseList, cycAccess.getObjectTool().makeCycList("(1 2)"));

    script = "(cdestructuring-bind (&whole a &optional b) '(1) (print (list a b)))";
    responseList = cycAccess.converse().converseList(script);
    assertEquals(responseList, cycAccess.getObjectTool().makeCycList("((1) 1)"));

    script = "(cdestructuring-bind (&rest a) '(1 2) (print (list a)))";
    responseList = cycAccess.converse().converseList(script);
    assertEquals(responseList, cycAccess.getObjectTool().makeCycList("((1 2))"));

    script = "(cdestructuring-bind (&whole a b &rest c) '(1 2 3) (print (list a b c)))";
    responseList = cycAccess.converse().converseList(script);
    assertEquals(responseList, cycAccess.getObjectTool().makeCycList("((1 2 3) 1 (2 3))"));

    script = "(cdestructuring-bind (&key a b) '(:b 2 :a 1) (print (list a b)))";
    responseList = cycAccess.converse().converseList(script);
    assertEquals(responseList, cycAccess.getObjectTool().makeCycList("(1 2)"));

    script = "(cdestructuring-bind (&key a b) '(:b 2 :allow-other-keys t :a 1 :c 3) (print (list a b)))";
    responseList = cycAccess.converse().converseList(script);
    assertEquals(responseList, cycAccess.getObjectTool().makeCycList("(1 2)"));

    script = "(cdestructuring-bind (&key ((key a) 23 b)) '(key 1) (print (list a b)))";
    responseList = cycAccess.converse().converseList(script);
    assertEquals(responseList, cycAccess.getObjectTool().makeCycList("(1 T)"));

    script = "(cdestructuring-bind (a &optional b &key c) '(1 2 :c 3) (print (list a b c)))";
    responseList = cycAccess.converse().converseList(script);
    assertEquals(responseList, cycAccess.getObjectTool().makeCycList("(1 2 3)"));

    script = "(cdestructuring-bind (&whole a b &optional c &rest d &key e &allow-other-keys &aux f) '(1 2 :d 4 :e 3) (print (list a b c d e f)))";
    responseList = cycAccess.converse().converseList(script);
    assertEquals(responseList, cycAccess.getObjectTool().makeCycList(
            "((1 2 :D 4 :E 3) 1 2 (:D 4 :E 3) 3 NIL)"));

    long endMilliseconds = System.currentTimeMillis();
    System.out.println(
            "  " + (endMilliseconds - startMilliseconds) + " milliseconds");
  }

  @Test
  public void testBinaryCycAccess7_3() throws CycConnectionException {
    //TestUtils.assumeNotObfuscated();
    long startMilliseconds = System.currentTimeMillis();
    resetCycConstantCaches();

    String script;
    Object responseObject;
    CycArrayList command;

    // type testing
    script = "(csetq a 1)";
    testObjectScript(cycAccess, script, 1);
    script = "(numberp a)";
    assertTrue(cycAccess.converse().converseBoolean(script));
    script = "(integerp a)";
    assertTrue(cycAccess.converse().converseBoolean(script));
    script = "(stringp a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(atom a)";
    assertTrue(cycAccess.converse().converseBoolean(script));
    script = "(floatp a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(symbolp a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(consp a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(listp a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(null a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));

    script = "(csetq a \"abc\")";
    testObjectScript(cycAccess, script, "abc");
    script = "(numberp a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(integerp a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(stringp a)";
    assertTrue(cycAccess.converse().converseBoolean(script));
    script = "(atom a)";
    assertTrue(cycAccess.converse().converseBoolean(script));
    script = "(floatp a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(symbolp a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(consp a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(listp a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(null a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));

    script = "(csetq a 2.14)";
    responseObject = cycAccess.converse().converseObject(script);
    assertTrue(responseObject instanceof Double);
    assertTrue(((Double) responseObject).doubleValue() > 2.13999);
    assertTrue(((Double) responseObject).doubleValue() < 2.14001);
    script = "(numberp a)";
    assertTrue(cycAccess.converse().converseBoolean(script));
    script = "(integerp a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(stringp a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(atom a)";
    assertTrue(cycAccess.converse().converseBoolean(script));
    script = "(floatp a)";
    assertTrue(cycAccess.converse().converseBoolean(script));
    script = "(symbolp a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(consp a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(listp a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(null a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));

    script = "(csetq a 'my-symbol)";
    testObjectScript(cycAccess, script, makeCycSymbol(
            "my-symbol"));
    script = "(numberp a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(integerp a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(stringp a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(atom a)";
    assertTrue(cycAccess.converse().converseBoolean(script));
    script = "(floatp a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(symbolp a)";
    assertTrue(cycAccess.converse().converseBoolean(script));
    script = "(consp a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(listp a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(null a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));

    script = "(csetq a '(1 . 2))";
    testObjectScript(cycAccess, script, cycAccess.getObjectTool().makeCycList("(1 . 2)"));
    script = "(numberp a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(integerp a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(stringp a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(atom a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(floatp a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(symbolp a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(consp a)";
    assertTrue(cycAccess.converse().converseBoolean(script));
    script = "(listp a)";
    assertTrue(cycAccess.converse().converseBoolean(script));
    script = "(null a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));

    script = "(csetq a '(1 2))";
    testObjectScript(cycAccess, script, cycAccess.getObjectTool().makeCycList("(1 2)"));
    script = "(numberp a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(integerp a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(stringp a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(atom a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(floatp a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(symbolp a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(consp a)";
    assertTrue(cycAccess.converse().converseBoolean(script));
    script = "(listp a)";
    assertTrue(cycAccess.converse().converseBoolean(script));
    script = "(null a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));

    script = "(csetq a nil)";
    testObjectScript(cycAccess, script, nil);
    script = "(numberp a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(integerp a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(stringp a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(atom a)";
    assertTrue(cycAccess.converse().converseBoolean(script));
    script = "(floatp a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(symbolp a)";
    assertTrue(cycAccess.converse().converseBoolean(script));
    script = "(consp a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(listp a)";
    assertTrue(cycAccess.converse().converseBoolean(script));
    script = "(null a)";
    assertTrue(cycAccess.converse().converseBoolean(script));

    // empty list is treated the same as nil.
    command = new CycArrayList();
    command.add(makeCycSymbol("csetq"));
    command.add(makeCycSymbol("a"));
    command.add(new CycArrayList());
    testObjectScript(cycAccess, command, nil);
    script = "(numberp a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(integerp a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(stringp a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(atom a)";
    assertTrue(cycAccess.converse().converseBoolean(script));
    script = "(floatp a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(symbolp a)";
    assertTrue(cycAccess.converse().converseBoolean(script));
    script = "(consp a)";
    assertTrue(!cycAccess.converse().converseBoolean(script));
    script = "(listp a)";
    assertTrue(cycAccess.converse().converseBoolean(script));
    script = "(null a)";
    assertTrue(cycAccess.converse().converseBoolean(script));


    /*
     // constant name with embedded slash
     //cycAccess.traceOn();
     script =
     "(rtp-parse-exp-w/vpp \"Symptoms of EEE begin 4-10 days after infection\" \n" +
     "(fort-for-string \"STemplate\") \n" +
     "(fort-for-string \"AllEnglishTemplateMt\") \n" +
     "(fort-for-string \"RKFParsingMt\"))";
     responseList = cycAccess.converse().converseList(script);
     */
    // check-type
    script
            = "(clet (result) \n" + "  (ignore-errors \n" + "    (/ 1 1) \n"
            + "    (csetq result t)) \n" + "  result)";
    assertEquals((Object) t, cycAccess.converse().converseObject(script));
    script
            = "(clet (result) \n" + "  (ignore-errors \n" + "    (/ 1 0) \n"
            + "    (csetq result t)) \n" + "  result)";
    assertEquals((Object) nil, cycAccess.converse().converseObject(script));

    //cycAccess.traceOn();
    long endMilliseconds = System.currentTimeMillis();
    System.out.println(
            "  " + (endMilliseconds - startMilliseconds) + " milliseconds");
  }

  /**
   * Tests a portion of the CycAccess methods using the binary api connection.
   */
  @Test
  public void testBinaryCycAccess8_1() throws CycConnectionException {
    System.out.println("\n**** testBinaryCycAccess 8.1 ****");

    long startMilliseconds = System.currentTimeMillis();

    // List containing null is coerced to list containing NIL.
    String script = "(put-api-user-variable 'a '(nil 1))";
    Object responseObject = cycAccess.converse().converseObject(script);
    assertEquals(nil, responseObject);

    script = "(get-api-user-variable 'a)";

    CycList responseList = cycAccess.converse().converseList(script);
    assertEquals(cycAccess.getObjectTool().makeCycList("(nil 1)"), responseList);

    long endMilliseconds = System.currentTimeMillis();
    System.out.println(
            "  " + (endMilliseconds - startMilliseconds) + " milliseconds");
  }

  @Test
  public void testBinaryCycAccess8_2() throws CycConnectionException, CycApiException, OpenCycUnsupportedFeatureException {
    System.out.println("\n**** testBinaryCycAccess 8.2 ****");
    assumeNotOpenCyc();
    long startMilliseconds = System.currentTimeMillis();

    // rkfPhraseReader
    Fort rkfEnglishLexicalMicrotheoryPsc = cycAccess.getLookupTool().getKnownConstantByGuid(ENGLISHMT_GUID_STRING);
    String text = "penguins";
    final CycClient cycClient = CycClientManager.getClientManager().fromCycAccess(cycAccess);
    CycList parseExpressions = cycClient.getRKFTool().rkfPhraseReader(
            text,
            rkfEnglishLexicalMicrotheoryPsc,
            (Fort) INFERENCE_PSC);
    CycArrayList parseExpression = (CycArrayList) parseExpressions.first();
    CycArrayList spanExpression = (CycArrayList) parseExpression.first();
    CycArrayList terms = (CycArrayList) parseExpression.second();

    // #$Penguin
    Fort penguin = cycAccess.getLookupTool().getKnownConstantByGuid(PENGUIN_GUID_STRING);
    assertTrue(terms.contains(penguin));

    // #$PittsburghPenguins
    Fort pittsburghPenguins = cycAccess.getLookupTool().getKnownConstantByGuid(
            PITTSBURGH_PENGUINS_GUID_STRING);
    assertTrue(terms.contains(pittsburghPenguins));

    // generateDisambiguationPhraseAndTypes
    CycArrayList objects = new CycArrayList();
    objects.add(penguin);
    objects.add(pittsburghPenguins);

    CycList disambiguationExpression = cycAccess.getObjectTool().generateDisambiguationPhraseAndTypes(
            objects);
    System.out.println(
            "disambiguationExpression\n" + disambiguationExpression);
    assertEquals(2, disambiguationExpression.size());

    CycArrayList penguinDisambiguationExpression = (CycArrayList) disambiguationExpression.first();
    System.out.println(
            "penguinDisambiguationExpression\n" + penguinDisambiguationExpression);
    assertTrue(penguinDisambiguationExpression.contains("penguin"));

    CycArrayList pittsburghPenguinDisambiguationExpression
            = (CycArrayList) disambiguationExpression.second();
    System.out.println(
            "pittsburghPenguinDisambiguationExpression\n" + pittsburghPenguinDisambiguationExpression);
    assertTrue(pittsburghPenguinDisambiguationExpression.contains(
            "the Pittsburgh Penguins"));
    boolean hasTeam = false;
    for (Object o : pittsburghPenguinDisambiguationExpression) {
      // Can contain "ice hockey team" or "NHL team"
      hasTeam = hasTeam || o.toString().toLowerCase().contains("team");
    }
    assertTrue(hasTeam);

    long endMilliseconds = System.currentTimeMillis();
    System.out.println(
            "  " + (endMilliseconds - startMilliseconds) + " milliseconds");
  }

  /**
   * Tests a portion of the CycAccess methods using the binary api connection.
   */
  @Test
  public void testBinaryCycAccess9() {
    System.out.println("\n**** testBinaryCycAccess 9 ****");

    long startMilliseconds = System.currentTimeMillis();

    try {
      CycConstant brazil = cycAccess.getLookupTool().getKnownConstantByGuid(BRAZIL_GUID_STRING);
      CycConstant country = cycAccess.getLookupTool().getKnownConstantByGuid(COUNTRY_GUID_STRING);
      CycConstant worldGeographyMt = cycAccess.getLookupTool().getKnownConstantByGuid(
              WORLD_GEOGRAPHY_MT_GUID_STRING);
      CycConstant dog = cycAccess.getLookupTool().getKnownConstantByGuid(DOG_GUID_STRING);
      HashSet hashSet = new HashSet();
      hashSet.add(dog);
      assertTrue(hashSet.contains(dog));

      CycConstant animal = cycAccess.getLookupTool().getKnownConstantByGuid(ANIMAL_GUID_STRING);
      CycConstant biologyVocabularyMt = cycAccess.getLookupTool().getKnownConstantByGuid(
              BIOLOGY_VOCABULARY_MT_GUID_STRING);
      CycConstant performedBy = cycAccess.getLookupTool().getKnownConstantByGuid(
              PERFORMED_BY_GUID_STRING);
      CycConstant doneBy = cycAccess.getLookupTool().getKnownConstantByGuid(DONE_BY_GUID_STRING);
      CycConstant siblings = cycAccess.getLookupTool().getKnownConstantByGuid(
              SIBLINGS_GUID_STRING);
      CycConstant generalLexiconMt = cycAccess.getLookupTool().getKnownConstantByGuid(
              GENERAL_LEXICON_MT_GUID_STRING);
      CycConstant paraphraseMt = cycAccess.getLookupTool().getKnownConstantByGuid(
              PARAPHRASE_MT_GUID_STRING);
      CycConstant mtTimeWithGranularityDimFn = cycAccess.getLookupTool().getKnownConstantByGuid(
              MT_TIME_WITH_GRANULARITY_DIM_FN_GUID_STRING);
      CycConstant mtSpace = cycAccess.getLookupTool().getKnownConstantByGuid(
              MT_SPACE_GUID_STRING);
      CycConstant now = cycAccess.getLookupTool().getKnownConstantByGuid(NOW_GUID_STRING);
      CycConstant timePoint = cycAccess.getLookupTool().getKnownConstantByGuid(
              TIMEPOINT_GUID_STRING);
      //(#$MtSpace (#$MtTimeWithGranularityDimFn #$Now #$TimePoint) #$WorldGeographyMt)
      ElMt worldGeographyMtNow
              = cycAccess.getObjectTool().makeElMt(
                      new NautImpl(
                              mtSpace, new NautImpl(mtTimeWithGranularityDimFn, now, timePoint),
                              worldGeographyMt));

      // isa
      assertTrue(cycAccess.getInspectorTool().isa(brazil, country, worldGeographyMt));
      assertTrue(cycAccess.getInspectorTool().isa(brazil, country, worldGeographyMtNow));
      assertTrue(cycAccess.getInspectorTool().isa(brazil, country));

      // isGenlOf
      assertTrue(cycAccess.getInspectorTool().isGenlOf(animal, dog, biologyVocabularyMt));
      assertTrue(cycAccess.getInspectorTool().isGenlOf(animal, dog));

      // isGenlPredOf
      assertTrue(cycAccess.getInspectorTool().isGenlPredOf(doneBy, performedBy, BASE_KB));
      assertTrue(cycAccess.getInspectorTool().isGenlPredOf(doneBy, performedBy));

      // isGenlInverseOf
      assertTrue(cycAccess.getInspectorTool().isGenlInverseOf(siblings, siblings,
              biologyVocabularyMt));
      assertTrue(cycAccess.getInspectorTool().isGenlInverseOf(siblings, siblings));

      // isGenlMtOf
      assertTrue(cycAccess.getInspectorTool().isGenlMtOf(BASE_KB, biologyVocabularyMt));

      /*
       // tests proper receipt of narts from the server.
       String script = "(csetq all-narts nil)";
       cycAccess.converse().converseVoid(script);
       script = "(progn \n" +
       "  (do-narts (nart) \n" +
       "    (cpush nart all-narts)) \n" +
       "  nil)";
       cycAccess.converse().converseVoid(script);
       script = "(clet (nart) \n" +
       "  (csetq nart (first all-narts)) \n" +
       "  (csetq all-narts (rest all-narts)) \n" +
       "  nart)";
       long numberGood = 0;
       long numberNil = 0;
       while (true) {
       Object obj = cycAccess.converse().converseObject(script);
       if (obj.equals(nil))
       break;
       assertTrue(obj instanceof NartImpl);
       NartImpl cycNart = (NartImpl) obj;
       assertTrue(cycNart.cyclify() instanceof String);
       String script2 = "(find-nart " + cycNart.stringApiValue() + ")";
       Object obj2 = cycAccess.converse().converseObject(script2);
       if (cycNart.equals(obj))
       numberGood++;
       else
       numberNil++;
       }
       assertTrue(numberGood > 20 * numberNil);
       script = "(csetq all-narts nil)";
       cycAccess.converse().converseVoid(script);
       */
    } catch (Throwable e) {
      e.printStackTrace();
      fail(e.toString());
    }

    long endMilliseconds = System.currentTimeMillis();
    System.out.println(
            "  " + (endMilliseconds - startMilliseconds) + " milliseconds");
  }

  /**
   * Tests a portion of the CycAccess methods using the binary api connection.
   */
  @Test
  public void testBinaryCycAccess10() {
    System.out.println("\n**** testBinaryCycAccess 10 ****");

    long startMilliseconds = System.currentTimeMillis();

    try {
      // demonstrate quoted strings
      //cycAccess.traceOn();
      StringBuffer stringBuffer = new StringBuffer();
      stringBuffer.append("a");
      stringBuffer.append('"');
      stringBuffer.append("b");
      stringBuffer.append('"');
      stringBuffer.append("c");

      String expectedString = stringBuffer.toString();
      CycArrayList command = new CycArrayList();
      command.add(makeCycSymbol("identity"));
      command.add(expectedString);

      String resultString = cycAccess.converse().converseString(command);
      assertEquals(expectedString, resultString);

      CycList cycList53 = cycAccess.getObjectTool().makeCycList("(\"abc\")");
      assertEquals(1,
              cycAccess.converse().converseInt(
                      "(length '" + cycList53.cycListApiValue() + ")"));
      assertEquals(3,
              cycAccess.converse().converseInt(
                      "(length (first '" + cycList53.cycListApiValue() + "))"));

      String string = "abc";
      CycArrayList cycList54 = new CycArrayList();
      cycList54.add(makeCycSymbol("length"));
      cycList54.add(string);
      assertEquals(3, cycAccess.converse().converseInt(cycList54));

      String quotedString = "\"abc\" def";
      CycArrayList cycList55 = new CycArrayList();
      cycList55.add(makeCycSymbol("length"));
      cycList55.add(quotedString);

      // Note that in binary mode, that Cyc's cfasl input will insert the required escape
      // chars for embedded quotes.
      // And in ascii mode note that CycConnection will insert the required escape
      // chars for embedded quotes.  While in binary mode, CfaslOutputStream will insert
      // the required escapes.
      //
      // Cyc should see (length "\"abc\" def") and return 9
      assertEquals(9, cycAccess.converse().converseInt(cycList55));

      // demonstrate quoted strings with the CycListParser
      CycList cycList56 = cycAccess.getObjectTool().makeCycList("(\"" + string + "\")");
      assertEquals(1,
              cycAccess.converse().converseInt(
                      "(length " + cycList56.stringApiValue() + ")"));
      assertEquals(3,
              cycAccess.converse().converseInt(
                      "(length (first " + cycList56.stringApiValue() + "))"));

      String embeddedQuotesString = "(" + "\"\\\"abc\\\" def\"" + ")";
      CycList cycList57 = cycAccess.getObjectTool().makeCycList(embeddedQuotesString);
      String script = "(length " + cycList57.stringApiValue() + ")";
      int actualLen = cycAccess.converse().converseInt(script);
      assertEquals(1, actualLen);
      assertEquals(9,
              cycAccess.converse().converseInt(
                      "(length (first " + cycList57.stringApiValue() + "))"));

      script = "(identity (quote (" + GIVEN_NAMES_STRING + " " + GUEST_STRING + " \"\\\"The\\\" Guest\")))";

      String script1 = "(IDENTITY (QUOTE (" + GIVEN_NAMES_STRING + " " + GUEST_STRING + " \"\"The\" Guest\")))";

      //CycListParser.verbosity = 3;
      CycList scriptCycList = cycAccess.getObjectTool().makeCycList(script);

      // Java strings do not escape embedded quote chars
      assertEquals(script1, scriptCycList.cyclify());

      CycList answer = cycAccess.converse().converseList(script);
      Object third = answer.third();
      assertTrue(third instanceof String);
      assertEquals(11, ((String) third).length());

      answer = cycAccess.converse().converseList(scriptCycList);
      third = answer.third();
      assertTrue(third instanceof String);
      assertEquals(11, ((String) third).length());

      // isFormulaWellFormed
      FormulaSentence formula1 = cycAccess.getObjectTool().makeCycSentence(
              "(" + ISA_STRING + " " + BRAZIL_STRING + " " + INDEPENDENT_COUNTRY_STRING + ")");
      CycConstant mt = cycAccess.getLookupTool().getKnownConstantByName(
              WORLD_POLITICAL_GEO_DATA_VOCAB_MT.cyclify());
      assertTrue(cycAccess.getInspectorTool().isFormulaWellFormed(formula1, mt));
      FormulaSentence formula2 = cycAccess.getObjectTool().makeCycSentence(
              "(" + GENLS_STRING + " " + BRAZIL_STRING + " " + COLLECTION_STRING + ")");
      assertTrue(!cycAccess.getInspectorTool().isFormulaWellFormed(formula2, mt));

      // isCycLNonAtomicReifableTerm
      Naut formula3 = cycAccess.getObjectTool().makeCycNaut(
              WATERCRAFT_COVERING.cyclify());
      assertTrue(cycAccess.getInspectorTool().isCycLNonAtomicReifableTerm(formula3));

      FormulaSentence formula4 = cycAccess.getObjectTool().makeCycSentence(
              "(" + ISA_STRING + " " + PLANT_STRING + "  " + ANIMAL_STRING + ")");
      assertTrue(!cycAccess.getInspectorTool().isCycLNonAtomicReifableTerm(formula4));

      Naut formula5 = cycAccess.getObjectTool().makeCycNaut("(" + PLUS_FN_STRING + " 1)");
      assertTrue(!cycAccess.getInspectorTool().isCycLNonAtomicReifableTerm(formula5));

      // isCycLNonAtomicUnreifableTerm
      Naut formula6 = cycAccess.getObjectTool().makeCycNaut(
              WATERCRAFT_COVERING.cyclify());
      assertTrue(!cycAccess.getInspectorTool().isCycLNonAtomicUnreifableTerm(formula6));

      FormulaSentence formula7 = cycAccess.getObjectTool().makeCycSentence(
              "(" + ISA_STRING + " " + PLANT_STRING + " " + ANIMAL_STRING + ")");
      assertTrue(!cycAccess.getInspectorTool().isCycLNonAtomicUnreifableTerm(formula7));

      Naut formula8 = cycAccess.getObjectTool().makeCycNaut("(" + PLUS_FN_STRING + " 1)");
      assertTrue(cycAccess.getInspectorTool().isCycLNonAtomicUnreifableTerm(formula8));
    } catch (Throwable e) {
      e.printStackTrace();
      fail(e.toString());
    }

    long endMilliseconds = System.currentTimeMillis();
    System.out.println(
            "  " + (endMilliseconds - startMilliseconds) + " milliseconds");
  }

  /**
   * Tests a portion of the CycAccess methods using the binary api connection.
   */
  @Test
  public void testBinaryCycAccess11() {
    System.out.println("\n**** testBinaryCycAccess 11 ****");
    resetCaches();
    try {

      long startMilliseconds = System.currentTimeMillis();

      try {
        String script = "(+ 1 2)";
        int answer = cycAccess.converse().converseInt(script);
        assertEquals(3, answer);
      } catch (Throwable e) {
        e.printStackTrace();
        fail(e.toString());
      }
//    cycAccess.getLookupTool().getCycLeaseManager().setLeaseDurationMilliseconds(100000);
//    cycAccess.getLookupTool().getCycLeaseManager().immediatelyRenewLease();

      System.out.println("Concurrent API requests.");

      ArrayList apiRequestors = new ArrayList();

      apiRequestors.add(new LongApiRequestor(cycAccess));
      for (int i = 1; i < 8; i++) {
        apiRequestors.add(new ShortApiRequestor(cycAccess));
      }

      int iterationsUntilCancel = 10;
      boolean isCancelled = false;
      while (true) {
        boolean apiRequestorTheadRunning = false;

        try {
          Thread.sleep(3000);
        } catch (InterruptedException e) {
          break;
        }

        System.out.println("-----------------------");
        for (int i = 0; i < apiRequestors.size(); i++) {
          final ApiRequestor apiRequestor = (ApiRequestor) apiRequestors.get(i);

          if (!apiRequestor.done) {
            apiRequestorTheadRunning = true;

            if ((iterationsUntilCancel-- < 0) && apiRequestor instanceof LongApiRequestor && !isCancelled) {
              System.out.println("Cancelling " + apiRequestor.name);
              isCancelled = true;
              try {
                apiRequestor.cancel();
              } catch (Throwable e) {
                e.printStackTrace();
                fail(e.getMessage());
              }
            }
          }
        }

        if (!apiRequestorTheadRunning) {
          break;
        }
      }

      long endMilliseconds = System.currentTimeMillis();
      System.out.println(
              "  " + (endMilliseconds - startMilliseconds) + " milliseconds");

    } catch (Throwable e) {
      e.printStackTrace();
      fail(e.toString());
    }

    // cycAccess.getLookupTool().getCycLeaseManager().removeAllListeners();
    for (Map.Entry<InputStream, LeaseManager> kv : cycAccess.getCycConnection().getCycLeaseManagerCommMap().entrySet()) {
      kv.getValue().removeAllListeners();
    }
    System.out.println("**** testBinaryCycAccess 11 OK ****");
  }

  private void testListScript(CycAccess cycAccess, String script,
          final CycList<Object> expected) throws CycConnectionException, CycApiException {
    assertEquals(expected, cycAccess.converse().converseList(script));
  }

  private void testObjectScript(CycAccess cycAccess, String script,
          final Object expected) throws CycConnectionException, CycApiException {
    assertEquals(expected, cycAccess.converse().converseObject(script));
  }

  private void testObjectScript(CycAccess cycAccess, CycArrayList script,
          Object expected) throws CycConnectionException {
    assertEquals(expected, cycAccess.converse().converseObject(script));

  }

  /**
   * Class ApiRequestor.
   */
  static protected class ApiRequestor extends Thread {

    /**
     * the connection to Cyc
     */
    final private CycAccess cycAccess;
    /**
     * the name of the api requestor process
     */
    final public String name;
    /**
     * the api request repeat count
     */
    final private int repeatCount;
    /**
     * the api request duration factor
     */
    final private String durationFactor;
    /**
     * the process completion indicator
     */
    public boolean done = false;
    public SublWorkerSynch worker;

    /**
     * Constructs and starts a ApiRequestor object.
     *
     * @param name the name of the api requestor process
     * @param repeatCount the api request repeat count
     * @param durationFactor the api request duration factor
     * @param cycAccess the connection to Cyc
     */
    public ApiRequestor(final String name,
            final int repeatCount,
            final String durationFactor,
            final CycAccess cycAccess) {
      this.name = name;
      this.repeatCount = repeatCount;
      this.durationFactor = durationFactor;
      this.cycAccess = cycAccess;
      start();
    }

    /**
     * Makes some API requests.
     *
     * @throws RuntimeException when wrong answer detected
     */
    public void run() {

      try {
        for (int i = 0; i < repeatCount; i++) {
          final String testPhrase = name + "-" + Integer.toString(i + 1);
          final String script = (name.equals("Long"))
                  ? "(catch-task-processor-termination-quietly (progn (do-assertions (assertion))\n"
                  + " \"" + testPhrase + "\"))"
                  : "(catch-task-processor-termination-quietly (progn (cdotimes (x "
                  + durationFactor + "))\n" + " \"" + testPhrase + "\"))";
          worker = new DefaultSublWorkerSynch(script, cycAccess);
          final Object answer = worker.getWork();
          if (answer.toString().equals(":CANCEL")) {
            System.out.println(name + " returned :CANCEL");
            done = true;
            return;
          } else {
            if (!answer.equals(testPhrase)) {
              throw new RuntimeException(testPhrase + " not equal to " + answer);
            }
          }
        }
      } catch (Throwable e) {
        System.out.println(
                "ApiRequestor " + name + " exception: " + e.toString());
        e.printStackTrace();
        done = true;
        return;
      }

      done = true;
    }

    /**
     * Cancels this thread at the Cyc Server.
     *
     * @throws CycApiException when an api error occurs
     * @throws IOException when a communication error occurs
     */
    public void cancel() throws CycApiException, CycConnectionException {
      cycAccess.getCycConnection().cancelCommunication(worker);
    }
  }

  static private class LongApiRequestor extends ApiRequestor {

    public LongApiRequestor(CycAccess cycAccess) {
      super("Long", 1, "1", cycAccess);
    }
  }

  static private class ShortApiRequestor extends ApiRequestor {

    static private int i = 0;

    public ShortApiRequestor(CycAccess cycAccess) {
      super("Short" + ++i, 4, "150000", cycAccess);
    }
  }

  /**
   * Tests a portion of the CycAccess methods using the binary api connection.
   */
  @Test
  public void testBinaryCycAccess12() {
    System.out.println("\n**** testBinaryCycAccess 12 ****");

    long startMilliseconds = System.currentTimeMillis();

    try {
      //cycAccess.traceOn();

      String utf8String = "ABCdef";
      assertEquals(utf8String,
              cycAccess.converse().converseString("(identity \"" + utf8String + "\")"));

      InputStreamReader inputStreamReader = null;

      try {
        inputStreamReader = new InputStreamReader(new FileInputStream(
                new File("utf8-sample.html")),
                "UTF-8");
      } catch (IOException e) {
        return;
      }

      StringBuffer utf8StringBuffer = new StringBuffer();

      while (true) {
        int ch = inputStreamReader.read();

        if (ch == -1) {
          break;
        }

        if ((ch == '\n') || (ch == '\r')) {
          utf8StringBuffer.append(' ');
        } else {
          utf8StringBuffer.append((char) ch);
        }
      }

      utf8String = utf8StringBuffer.toString();

      PrintWriter utf8Output = new PrintWriter(new OutputStreamWriter(
              new FileOutputStream("utf8-sample-without-newlines.html"), "UTF8"));
      utf8Output.print(utf8String);
      utf8Output.close();

      CycArrayList command = new CycArrayList();
      command.add(makeCycSymbol("identity"));
      command.add(utf8String);

      String echoUtf8Sting = cycAccess.converse().converseString(command);

      utf8Output = new PrintWriter(new OutputStreamWriter(
              new FileOutputStream("utf8-sample-from-cyc.html"), "UTF8"));
      utf8Output.print(utf8String);
      utf8Output.close();

      System.out.println("utf8String\n" + utf8String);
      System.out.println("echoUtf8Sting\n" + echoUtf8Sting);
      assertEquals(utf8String,
              echoUtf8Sting);

      Fort myTerm = cycAccess.getLookupTool().getConstantByName("my-term");

      if (myTerm != null) {
        cycAccess.getUnassertTool().kill(myTerm);
      }

      myTerm = cycAccess.getLookupTool().findOrCreate("my-term");
      cycAccess.getAssertTool().assertComment(myTerm, utf8String, BASE_KB);
    } catch (Throwable e) {
      e.printStackTrace();
      fail(e.toString());
    }
  }

  /**
   * Tests a portion of the CycAccess methods using the binary api connection.
   */
  @Test
  public void testBinaryCycAccess13_1() throws CycConnectionException {
    System.out.println("\n**** testBinaryCycAccess 13_1 ****");
    // NART case
    {
      //cycAccess.traceNamesOn();
      final String elmtString = AZERI_LANGUAGE_LEXICAL_MT.cyclify();
      final Naut mt = cycAccess.getObjectTool().makeCycNaut(elmtString);
      final ElMt hlmtObject = cycAccess.getObjectTool().canonicalizeHLMT(mt);
      assertNotNull(hlmtObject);
      assertTrue(hlmtObject instanceof NartImpl);
      assertEquals(elmtString, hlmtObject.cyclify());
    }

    // NAUT case
    {
      final String elmtString = MT_SPACE_TIME_NOW.cyclify();
      final Naut mt = cycAccess.getObjectTool().makeCycNaut(elmtString);
      final ElMt hlmtObject = cycAccess.getObjectTool().canonicalizeHLMT(mt);
      assertNotNull(hlmtObject);
      assertTrue(hlmtObject instanceof Naut);
      assertEquals(elmtString, hlmtObject.cyclify());
    }

    // Constant case
    {
      final String elmtString = MT_SPACE_TIME_ALWAYS.cyclify();
      final Naut mt = cycAccess.getObjectTool().makeCycNaut(elmtString);
      final ElMt hlmtObject = cycAccess.getObjectTool().canonicalizeHLMT(mt);
      assertNotNull(hlmtObject);
      assertTrue(hlmtObject instanceof CycConstantImpl);
      assertEquals("#$BaseKB", hlmtObject.cyclify());
    }

    // Nonsense case
    {
      final String elmtString = "(" + PLUS_FN_STRING + " 1 1)";
      final Naut mt = cycAccess.getObjectTool().makeCycNaut(elmtString);
      final CycObject hlmtObject = cycAccess.getObjectTool().canonicalizeHLMT(mt);
      assertNotNull(hlmtObject);
      assertTrue(hlmtObject instanceof Naut);
      assertEquals(elmtString,
              hlmtObject.cyclify());
    }

    // makeElMt
    // NART case
    String elmtString = AZERI_LANGUAGE_LEXICAL_MT.cyclify();
    Naut naut = cycAccess.getObjectTool().makeCycNaut(elmtString);
    ElMt elmt = cycAccess.getObjectTool().makeElMt(naut);
    assertNotNull(elmt);
    assertTrue(elmt instanceof Nart);
    assertEquals(elmtString, elmt.cyclify());

    // Nonsense case
    elmtString = "(" + PLUS_FN_STRING + " 1 1)";
    naut = cycAccess.getObjectTool().makeCycNaut(elmtString);
    elmt = cycAccess.getObjectTool().makeElMt(naut);
    assertNotNull(elmt);
    assertTrue(elmt instanceof Naut);
    assertEquals(elmtString, elmt.cyclify());

    // Constant case
    elmtString = BASE_KB.toString();
    Fort baseKB = cycAccess.getLookupTool().getKnownConstantByName(elmtString);
    elmt = cycAccess.getObjectTool().makeElMt(baseKB);
    assertNotNull(elmt);
    assertTrue(elmt instanceof CycConstantImpl);
    assertEquals(elmtString, elmt.toString());
    System.out.println("**** testBinaryCycAccess 13_1 OK ****");
  }

  @Test
  public void testBinaryCycAccess13_2() throws CycConnectionException {
    System.out.println("\n**** testBinaryCycAccess 13_2 ****");

    // getHLCycTerm
    Object obj = ((CycObjectTool) (cycAccess.getObjectTool())).getHLCycTerm("1");
    assertTrue(obj instanceof Integer);
    obj = ((CycObjectTool) (cycAccess.getObjectTool())).getHLCycTerm("\"abc\"");
    assertTrue(obj instanceof String);
    {
      CycConstant randomConstant = cycAccess.getLookupTool().getRandomConstant();
      obj = ((CycObjectTool) (cycAccess.getObjectTool())).getHLCycTerm(randomConstant.cyclify());
      assertEquals(randomConstant, obj);
    }
    {
      boolean ok = true;
      Nart randomNart = null;
      for (int count = 0; (count < 1000) && (ok == true); count++) {
        while (randomNart == null || !(cycAccess.getInspectorTool().isGround(randomNart))) { //Non-ground NARTs can have canonicalization issues.
          randomNart = cycAccess.getLookupTool().getRandomNart();
        }
        obj = ((CycObjectTool) (cycAccess.getObjectTool())).getHLCycTerm(randomNart.cyclify());
        if (!randomNart.equalsAtEL(obj)) {
          ((CycObjectTool) (cycAccess.getObjectTool())).getHLCycTerm(randomNart.cyclify());
          randomNart.equalsAtEL(obj);
          ok = false;
        }
      }
      assertTrue(randomNart.cyclify() + " is not equal (at EL) to " + obj,
              ok);
    }

    // getELCycTerm
    obj = cycAccess.getObjectTool().getELCycTerm("1");
    assertTrue(obj instanceof Integer);
    obj = cycAccess.getObjectTool().getELCycTerm("\"abc\"");
    assertTrue(obj instanceof String);

    CycConstant randomConstant = cycAccess.getLookupTool().getRandomConstant();
    obj = ((CycObjectTool) (cycAccess.getObjectTool())).getHLCycTerm(randomConstant.cyclify());
    assertEquals(randomConstant, obj);

    Nart randomNart = cycAccess.getLookupTool().getRandomNart();
    obj = ((CycObjectTool) (cycAccess.getObjectTool())).getHLCycTerm(randomNart.cyclify());
    assertEquals(randomNart, obj);

    // canonicalizeList
    String query = DAY_MARCH_1_2004_EVENT.cyclify();
    CycList queryList = cycAccess.getObjectTool().makeCycList(query);
    //System.out.println(queryList.cyclify());
    CycList canonicalizedList = cycAccess.getObjectTool().canonicalizeList(queryList);
    //System.out.println(canonicalizedList.cyclify());
    assertTrue(canonicalizedList.second() instanceof CycArrayList);
    InferenceParameters queryProperties = new DefaultInferenceParameters(
            cycAccess);
    assertTrue(!cycAccess.getInferenceTool().isQueryTrue(canonicalizedList,
            UNIVERSAL_VOCABULARY_MT, queryProperties));

    query = DAY_MARCH_1_2004_CALENDAR_DAY.cyclify();
    queryList = cycAccess.getObjectTool().makeCycList(query);
    //System.out.println(queryList.cyclify());
    canonicalizedList = cycAccess.getObjectTool().canonicalizeList(queryList);
    //System.out.println(canonicalizedList.cyclify());
    assertTrue(canonicalizedList.second() instanceof CycArrayList);
    assertTrue(cycAccess.getInferenceTool().isQueryTrue(canonicalizedList,
            UNIVERSAL_VOCABULARY_MT, queryProperties));

    query = DAY_MARCH_1_2004_CALENDAR_DAY.cyclify();
    final FormulaSentence sentence = cycAccess.getObjectTool().makeCycSentence(query);
    //System.out.println(queryList.cyclify());
    assertTrue(cycAccess.getInferenceTool().isQueryTrue(sentence, UNIVERSAL_VOCABULARY_MT,
            queryProperties));
    System.out.println("**** testBinaryCycAccess 13_2 OK ****");
  }

  @Test
  public void testBinaryCycAccess13_3() throws CycConnectionException {
    System.out.println("**** testBinaryCycAccess 13_3 ****");
    // assertions containing hl variables
    FormulaSentence query2 = cycAccess.getObjectTool().makeCycSentence(
            "(" + SALIENT_ASSERTIONS_STRING + " " + PERFORMED_BY_STRING + " ?ASSERTION)");
    InferenceParameters queryProperties2 = cycAccess.getInferenceTool().getHLQueryProperties();
    queryProperties2.put(":answer-language", makeCycSymbol(
            ":hl"));
    InferenceResultSet resultSet = cycAccess.getInferenceTool().executeQuery(query2, BASE_KB,
            queryProperties2);
    resultSet.next();
    CycAssertion cycAssertion = (CycAssertion) resultSet.getCycObject(1);
    System.out.println("cycAssertion= " + cycAssertion.cyclify());
    assertTrue(cycAssertion.cyclify().contains("?VAR0"));
    CycArrayList command = new CycArrayList();
    command.add(makeCycSymbol("identity"));
    command.add(cycAssertion);
    //cycAccess.traceOnDetailed();
    Object result = cycAccess.converse().converseObject(command);
    assertTrue(result instanceof CycAssertion);
    assertTrue(((CycAssertion) result).cyclify().contains("?VAR0"));
    //System.out.println("cycAssertion= " + ((CycAssertion) result).cyclify());
    System.out.println("**** testBinaryCycAccess 13_3 OK ****");
  }

  /**
   * Tests a portion of the CycAccess methods using the binary api connection.
   */
  @Test
  public void testBinaryCycAccess14() {
    System.out.println("\n**** testBinaryCycAccess 14 ****");

    long startMilliseconds = System.currentTimeMillis();

    if (cycAccess.getCycConnection() != null) {
      System.out.println(cycAccess.getCycConnection().connectionInfo());
    } else {
      System.out.println("CycConnection info is null.");
    }

    resetCycConstantCaches();

    try {
      // second call should access the cache by GUID
      cycAccess.traceOn();
      System.out.println("------------");

      GuidImpl organizationGuid = new GuidImpl(ORGANIZATION_GUID_STRING);
      CycConstant organization = cycAccess.getLookupTool().getConstantByGuid(organizationGuid);
      System.out.println("------------");
      organization = cycAccess.getLookupTool().getConstantByGuid(organizationGuid);
      System.out.println("------------");
      cycAccess.traceOff();
    } catch (Throwable e) {
      e.printStackTrace();
      fail(e.getMessage());
    }

    List localDisjointWiths = null;


    /*
     // complete received objects immediately
     cycAccess.deferObjectCompletion = false;
     System.out.println("deferObjectCompletion = false");
    
     // trace should show the use of the CycConstantCache to avoid redundant server
     // accesses for the term name.
     // getLocalDisjointWith.
     try {
     CycConstant vegetableMatter =
     // cycAccess.getLookupTool().getKnownConstantByGuid("bd58c455-9c29-11b1-9dad-c379636f7270");
     localDisjointWiths = cycAccess.getLookupTool().getDisjointWiths(vegetableMatter);
     assertNotNull(localDisjointWiths);
     assertTrue(localDisjointWiths.toString().indexOf("AnimalBLO") > 0);
     localDisjointWiths = cycAccess.getLookupTool().getDisjointWiths(vegetableMatter);
     localDisjointWiths = cycAccess.getLookupTool().getDisjointWiths(vegetableMatter);
     localDisjointWiths = cycAccess.getLookupTool().getDisjointWiths(vegetableMatter);
     }
     catch (Throwable e) {
     fail(e.toString());
     }
     */
    // getLocalDisjointWith.
    localDisjointWiths = null;

    try {
      CycConstant vegetableMatter = cycAccess.getLookupTool().getKnownConstantByGuid(
              VEGETABLE_MATTER_GUID_STRING);
      localDisjointWiths = cycAccess.getLookupTool().getDisjointWiths(vegetableMatter);
      assertNotNull(localDisjointWiths);

      //System.out.println("localDisjointWiths.toString()");
      //assertTrue(localDisjointWiths.toString().indexOf("AnimalBLO") > 0);
      //System.out.println("localDisjointWiths.toString()");
      //assertTrue(localDisjointWiths.toString().indexOf("AnimalBLO") > 0);
      localDisjointWiths = cycAccess.getLookupTool().getDisjointWiths(vegetableMatter);
      localDisjointWiths = cycAccess.getLookupTool().getDisjointWiths(vegetableMatter);
      localDisjointWiths = cycAccess.getLookupTool().getDisjointWiths(vegetableMatter);
    } catch (Throwable e) {
      fail(e.toString());
    }

    // makeUniqueCycConstant
    try {
      final String constantName = "MyConstant";
      CycConstant cycConstant1 = cycAccess.getObjectTool().makeUniqueCycConstant(constantName);
      System.out.println(cycConstant1.cyclify());
      assertTrue(cycConstant1.getName().startsWith(constantName));
      CycConstant cycConstant2 = cycAccess.getObjectTool().makeUniqueCycConstant(constantName);
      System.out.println(cycConstant2.cyclify());
      assertTrue(cycConstant2.getName().startsWith(constantName));
      assertTrue(!cycConstant1.getName().equals(cycConstant2.getName()));
      CycConstant cycConstant3 = cycAccess.getObjectTool().makeUniqueCycConstant(constantName);
      System.out.println(cycConstant3.cyclify());
      assertTrue(cycConstant3.getName().startsWith(constantName));
      assertTrue(!cycConstant3.getName().equals(cycConstant1.getName()));
      assertTrue(!cycConstant3.getName().equals(cycConstant2.getName()));
    } catch (Throwable e) {
      fail(e.toString());
    }

    long endMilliseconds = System.currentTimeMillis();
    System.out.println(
            "  " + (endMilliseconds - startMilliseconds) + " milliseconds");
  }

  /**
   * Tests a portion of the CycAccess methods using the binary api connection. This test case
   * specifically is used to test cfasl id versus guid constant encoding, and the eager obtaining of
   * constant names.
   */
  @Test
  public void testBinaryCycAccess15() {
    System.out.println("\n**** testBinaryCycAccess 15 ****");

    try {

      // backquote
      String command
              = "(identity " + "`(,(canonicalize-term \'(" + COLLECTION_UNION_FN_STRING + " (" + THE_SET_STRING + " " + TOURIST_STRING
              + " (" + GROUP_FN_STRING + " " + TOURIST_STRING + ")))) ," + COMPUTATIONAL_SYSTEM_STRING + "))";
      Object result;
      try {
        //cycAccess.traceOn();
        result = cycAccess.converse().converseObject(command);
        assertNotNull(result);
        assertTrue(result instanceof CycArrayList);
        System.out.println("backquoted nart: " + ((CycArrayList) result).cyclify());
        System.out.println(
                "embedded obj class: " + ((CycArrayList) result).first().getClass().toString());
        assertTrue(((CycArrayList) result).first() instanceof Nart);
      } catch (Throwable e) {
        e.printStackTrace();
        fail(e.toString());
      }

      // getComment with NartImpl
      Nart nart = null;
      String comment = null;
      try {
        nart = (Nart) cycAccess.converse().converseObject(
                "(find-nart '(" + JUVENILE_FN_STRING + " " + DOG_STRING + "))");
        comment = cycAccess.getLookupTool().getComment(nart);
        assertNotNull(comment);
        //assertEquals("", comment);
      } catch (Throwable e) {
        e.printStackTrace();
        fail(e.toString());
      }

      // newlines in strings
      try {
        command = "(nart-substitute \"first text line\nsecond text line\")";
        System.out.println("string with newlines: " + command);
        System.out.println(cycAccess.converse().converseObject(command));
      } catch (Throwable e) {
        e.printStackTrace();
        fail(e.toString());
      }

      //cycAccess.traceOnDetailed();
      // getLocalDisjointWith.
      List localDisjointWiths = null;

      try {
        CycConstant vegetableMatter = cycAccess.getLookupTool().getKnownConstantByGuid(
                VEGETABLE_MATTER_GUID_STRING);
        localDisjointWiths = cycAccess.getLookupTool().getDisjointWiths(vegetableMatter);
        assertNotNull(localDisjointWiths);

        //assertTrue(localDisjointWiths.toString().indexOf("AnimalBLO") > 0);
      } catch (Throwable e) {
        fail(e.toString());
      }

      // ensure that constants have names
      try {
        String physicalDeviceGuidString = PHYSICAL_DEVICE_GUID_STRING;
        CycConstant physicalDevice = cycAccess.getLookupTool().getKnownConstantByGuid(
                physicalDeviceGuidString);
        final CycList constants = cycAccess.getLookupTool().getAllInstances(physicalDevice);
        if (constants.size() > 0 && constants.first() instanceof CycConstantImpl) {
          assertNotNull(((CycConstantImpl) constants.first()).name);
        }
        if (constants.size() > 1 && constants.second() instanceof CycConstantImpl) {
          assertNotNull(((CycConstantImpl) constants.second()).name);
        }
      } catch (Throwable e) {
        e.printStackTrace();
        fail(e.toString());
      }

      try {
        Nart nart1 = cycAccess.getLookupTool().getRandomNart();
        assertNotNull(nart1);
        assertNotNull(nart1.getFunctor());
        assertTrue(nart1.getFunctor() instanceof Fort);
        assertNotNull(nart1.getArguments());
        assertTrue(nart1.getArguments() instanceof CycArrayList);

        //System.out.println(nart1.cyclify());
      } catch (Throwable e) {
        e.printStackTrace();
        fail(e.toString());
      }

      // Narts in a list.
      try {
        //cycAccess.traceOn();
        Nart nart1 = cycAccess.getLookupTool().getRandomNart();
        Nart nart2 = new NartImpl(nart1.toCycList());
        assertEquals(nart1, nart2);

        CycArrayList nartList = new CycArrayList();
        nartList.add(nart1);
        nartList.add(nart2);

        Object object = null;
        CycSymbolImpl a = makeCycSymbol("a");
        cycAccess.getObjectTool().setSymbolValue(a, nartList);

        object = cycAccess.getObjectTool().getSymbolValue(a);
        assertNotNull(object);
        assertTrue(object instanceof CycArrayList);

        CycArrayList nartList1 = (CycArrayList) object;
        Object element1 = nartList1.first();
        assertTrue(
                (element1 instanceof Nart) || (element1 instanceof CycArrayList));

        if (element1 instanceof CycArrayList) {
          element1 = NartImpl.coerceToCycNart(element1);
        }

        Nart nart3 = (NartImpl) element1;
        assertNotNull(nart3.getFunctor());
        assertTrue(nart3.getFunctor() instanceof Fort);
        assertNotNull(nart3.getArguments());
        assertTrue(nart3.getArguments() instanceof CycArrayList);

        Object element2 = nartList1.second();
        assertTrue(
                (element2 instanceof Nart) || (element2 instanceof CycArrayList));

        if (element2 instanceof CycArrayList) {
          element2 = NartImpl.coerceToCycNart(element2);
        }

        Nart nart4 = (Nart) element2;
        assertNotNull(nart4.getFunctor());
        assertTrue(nart4.getFunctor() instanceof Fort);
        assertNotNull(nart4.getArguments());
        assertTrue(nart4.getArguments() instanceof CycArrayList);

        assertEquals(nart1.cyclify(), nart3.cyclify());
        assertEquals(nart1.cyclify(), nart4.cyclify());
      } catch (Throwable e) {
        e.printStackTrace();
        fail(e.toString());
      }
    } finally {

    }
    System.out.println("**** testBinaryCycAccess 15 OK ****");
  }

  /**
   * Tests a portion of the CycAccess methods using the binary api connection. This test case
   * specifically is used to test soap service handling of an xml response from Cyc.
   */
  @Test
  public void testBinaryCycAccess16() throws CycConnectionException {
    System.out.println("\n**** testBinaryCycAccess 16 ****");
    List genls = null;
    CycConstant carAccident = null;
    carAccident = cycAccess.getLookupTool().getKnownConstantByGuid(
            CAR_ACCIDENT_GUID_STRING);
    genls = cycAccess.getLookupTool().getGenls(carAccident);
    assertNotNull(genls);
    assertTrue(genls instanceof CycArrayList);

    Iterator iter = genls.iterator();

    while (iter.hasNext()) {
      Object obj = iter.next();
      assertTrue(obj instanceof Fort);
    }
    System.out.println("**** testBinaryCycAccess 16 OK ****");
  }

  /**
   * Tests the api getting of gafs (Ground Atomic Formula).
   */
  @Test
  public void testGetGafs() {
    System.out.println("\n**** testGetGafs ****");

    try {
      CycListParser parser = new CycListParser(cycAccess);
      CycArrayList nart = parser.read(
              "(" + REMOTELY_EXPLOITABLE_FN_STRING + " " + VULNERABLE_TO_DTML_METHOD_EXECUTION_STRING + ")");
      System.out.println("Nart: " + nart);

      CycList gafs = cycAccess.getLookupTool().getGafs(NartImpl.coerceToCycNart(nart), ISA);
      assertTrue(gafs.size() > 0);
    } catch (Throwable e) {
      fail(e.toString());
    }

    System.out.println("**** testGetGafs OK ****");
  }

  /**
   * Tests the getCycImageID() api method.
   */
  @Test
  public void testGetCycImage() throws CycConnectionException {
    System.out.println("\n**** testGetCycImage ****");
    String id = cycAccess.getCycImageID();
    assertTrue(id != null);
    System.out.println("**** testGetCycImage OK ****");
  }

  /**
   * Tests the ggetELCycTerm method.
   */
  @Test
  public void testGetELCycTerm() throws CycConnectionException {
    System.out.println("\n**** testGetELCycTerm ****");
    Object obj = cycAccess.getObjectTool().getELCycTerm("(" + JUVENILE_FN_STRING + " " + DOG_STRING + ")");
    assertTrue(obj != null);
    System.out.println("**** testGetELCycTerm OK ****");
  }

  /**
   * Tests the assertWithTranscriptAndBookkeeping method.
   */
  @Test
  public void testAssertWithTranscriptAndBookkeeping() {
    System.out.println("\n**** testAssertWithTranscriptAndBookkeeping ****");

    try {
      CycConstant cycAdministrator = cycAccess.getLookupTool().getKnownConstantByName(
              "CycAdministrator");
      cycAccess.getOptions().setCyclist(cycAdministrator);
      String assertionString = ISA_CYC_ADMIN_PERSON.cyclify();
      ElMt mt = UNIVERSAL_VOCABULARY_MT;
      cycAccess.getAssertTool().assertWithTranscriptAndBookkeeping(assertionString, mt);
    } catch (Throwable e) {
      fail(e.toString());
    }
    System.out.println("**** testAssertWithTranscriptAndBookkeeping OK ****");
  }

  /**
   * Tests the getArg2 method.
   */
  @Test
  public void testGetArg2() throws CycConnectionException {
    System.out.println("\n**** testGetArg2 ****");
    Fort cycAdministrator = cycAccess.getLookupTool().getKnownConstantByName(
            "CycAdministrator");
    Object obj = cycAccess.getLookupTool().getArg2(ISA, cycAdministrator);
    assertNotNull(obj);
    assertTrue(obj instanceof Fort || obj instanceof CycArrayList);
    assumeNotOpenCyc();
    final String predName = "scientificName";
    final String termName = "Emu";
    final String mtName = "AllEnglishValidatedLexicalMicrotheoryPSC";
    obj = cycAccess.getLookupTool().getArg2(predName, termName, mtName);
    System.out.println(obj);
    assertNotNull(obj);
    Fort predicate = cycAccess.getLookupTool().getKnownConstantByName(predName);
    Fort arg1 = cycAccess.getLookupTool().getKnownConstantByName(termName);
    Fort mt = cycAccess.getLookupTool().getKnownConstantByName(mtName);
    obj = cycAccess.getLookupTool().getArg2(predicate, arg1, mt);
    System.out.println(obj);
    assertNotNull(obj);
    System.out.println("**** testGetArg2 OK ****");
  }

  private boolean testManyQuickQueries_internal() throws Exception {
    final String varName = "?X";
    final CycVariable var = CycObjectFactory.makeCycVariable(varName);
    final CycObject mt = INFERENCE_PSC;
    final CycAccess cyc = CycAccessManager.getCurrentAccess();
    final InferenceParameters params = getOneShotQueryParams(cyc);
    final FormulaSentence sentence = CycFormulaSentence.makeCycFormulaSentence(
            CommonConstants.ISA, TEXAS_STATE, var);
    final CycList answers = cyc.getInferenceTool().queryVariable(var, sentence, mt, params);
    assertFalse("Couldn't find any answers! Query: " + sentence, answers.isEmpty());

    final FormulaSentence qSentenceTemplate = CycFormulaSentence.makeCycFormulaSentence(
            CommonConstants.ISA, makeCycSymbol(":THING"), var);
    final int maxThreads = Runtime.getRuntime().availableProcessors();
    /*
     * Test and compare two different ways to do a bunch of query-variables:
     * 1) Using CycAccess#queryVariable()
     * 2) Using CycAccess.#converseList(...QUERY-VARIABLE...)
     */
    final InferenceTool inferenceTool = cyc.getInferenceTool();
    final MultiThreadTester multiTester1 = new MultiThreadTester() {
      @Override
      List getValues(Object region) throws Exception {
        final FormulaSentence querySentence = qSentenceTemplate.deepCopy();
        querySentence.getArgs().set(1, region);
        final CycList<Object> answers = inferenceTool.queryVariable(var, querySentence, mt, params);
        return answers;
      }
    }.run(answers, maxThreads);
    final MultiThreadTester multiTester2 = new MultiThreadTester() {
      final String mtStringApiValue = mt.stringApiValue();
      final String paramsStringApiValue = params.stringApiValue();

      @Override
      List getValues(Object region) throws Exception {
        final FormulaSentence querySentence = qSentenceTemplate.deepCopy();
        querySentence.getArgs().set(1, region);
        final CycList<Object> names = cyc.converse().converseList(
                "(query-variable '" + var + " "
                + querySentence.stringApiValue() + " "
                + mtStringApiValue + " "
                + paramsStringApiValue + ")");
        return names;
      }
    }.run(answers, maxThreads);
    assertEquals(multiTester1.lastResultCount, multiTester2.lastResultCount);
    final float ratio = (1f * multiTester1.lastElapsedMillis) / (1f * multiTester2.lastElapsedMillis);
    //assertTrue("queryVariable took " + ratio + " times as long as converseList", ratio < 2);
    System.out.println("Ratio: " + ratio);
    System.out.println("queryVariable took " + ratio + " times as long as converseList");
    return ratio < 2;
  }

  @Test
  public void testManyQuickQueries() throws Exception {
    System.out.println("manyQuickQueries, attempt 1...");
    boolean test1Passed = testManyQuickQueries_internal();
    if (test1Passed) {
      assertTrue(true);
    } else {
      System.out.println("manyQuickQueries, attempt 2...");
      boolean test2Passed = testManyQuickQueries_internal();
      assertTrue("Ratio is acceptable.", test2Passed);
    }
  }

  private InferenceParameters getOneShotQueryParams(final CycAccess cyc) {
    final DefaultInferenceParameters params = new DefaultInferenceParameters(cyc);
    params.setBrowsable(false);
    params.setContinuable(false);
    params.setAllowIndeterminateResults(false);
    params.put(":ALLOW-ABNORMALITY-CHECKING?", CycObjectFactory.nil);
    params.put(":NEW-TERMS-ALLOWED?", CycObjectFactory.nil);
    params.put(":COMPUTE-ANSWER-JUSTIFICATIONS?", CycObjectFactory.nil);
    params.put(":MAX-PROBLEM-COUNT", CycObjectFactory.makeCycSymbol(":POSITIVE-INFINITY"));
    params.put(":PRODUCTIVITY-LIMIT", CycObjectFactory.makeCycSymbol(":POSITIVE-INFINITY"));
    params.put(":ANSWER-LANGUAGE", CycObjectFactory.makeCycSymbol(":HL"));
    params.setMaxTime(600);
    params.setMaxTransformationDepth(0);
    return params;

  }

  /**
   * See how long it takes to run a number of tasks using a particular method.
   */
  private abstract class MultiThreadTester {

    private long lastElapsedMillis;
    private int lastResultCount;

    public MultiThreadTester() throws Exception {
    }

    /**
     * Get values for the specified input.
     *
     * @param input
     * @return
     * @throws Exception
     */
    abstract List getValues(final Object input) throws Exception;

    /**
     * Test the specified list of inputs with the specified max number of threads.
     *
     * @param inputs
     * @param maxThreads
     * @throws Exception
     */
    MultiThreadTester run(List inputs, int maxThreads) throws Exception {

      final long startTime = System.currentTimeMillis();
      lastResultCount = 0;
      final ExecutorService executorService = Executors.newFixedThreadPool(maxThreads);
      final Deque<Future<List>> results = new LinkedList<Future<List>>();
      for (final Object thing : inputs) {
        final Callable<List> task = new Callable<List>() {
          @Override
          public List call() throws Exception {
            return getValues(thing);
          }
        };
        final Future<List> future = executorService.submit(task);
        results.offer(future);
      }
      while (!results.isEmpty()) {
        lastResultCount += results.poll().get().size();
      }
      lastElapsedMillis = System.currentTimeMillis() - startTime;
      System.out.println("Found " + lastResultCount + " values for " + inputs.size()
              + " inputs using max of " + maxThreads + " threads in "
              + lastElapsedMillis + "ms.");
      return this;
    }
  }

  @Test
  public void testBigCycList() {
    System.out.println("\n**** testBigCycList ****");

    CycArrayList cycList = new CycListParser(cycAccess).read(
            "(0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
            + "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0)");

    System.out.println(
            "**** testBigCycList OK (length = " + cycList.size() + ") ****");
  }

  @Test
  public void testUnicodeCFASL() {
    System.out.println("\n**** testUnicodeCFASL ****");
    CFASLStringTest("abc", 15);
    CFASLStringTest("", 15);
    StringBuffer sb = new StringBuffer();
    sb.append("a");
    sb.append((char) 0x401);
    CFASLStringTest(sb.toString(), 53);
    System.out.println("**** testUnicodeCFASL OK ****");
  }

  private boolean CFASLStringTest(String str, int opcode) {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
      CfaslOutputStream cos = new CfaslOutputStream(baos);
      cos.writeObject(str);
      cos.flush();
      byte[] ba = baos.toByteArray();
      if (ba == null || ba.length == 0) {
        fail("Null Byte Array Return");
      }
      //System.out.println("BA test: "+ba.length);
      //for(int i=0;i<ba.length;i++)
      //  System.out.println("ba check "+i+" "+Integer.toHexString(0xff & (int)ba[i]));
      assertEquals((int) ba[0], opcode);  // make sure opcode is correct
      ByteArrayInputStream bais = new ByteArrayInputStream(ba);

      CfaslInputStream cis = new CfaslInputStream(bais);
      Object obj = cis.readObject();
      assertTrue(obj instanceof String);
      String result = (String) obj;
      assertTrue(result.equals(str));
    } catch (IOException e) {
      fail("IOException CFASLStringTest for: " + str);
    }
    return true;

  }

  @Test
  public void testUnicodeCycSentence() throws Exception {
    System.out.println("UnicodeCycSentence");
    final CycAccess cyc = CycAccessManager.getCurrentAccess();
    final String namestring = NAMESTRING.cyclify();
    final String georgeWashington = GEORGE_WASHINGTON.cyclify();
    final String georgzine = NonAsciiStrings.get("georgzhine");
    final String sentence = "(" + namestring + " " + georgeWashington + " \"" + georgzine + "\")";
    checkUnicodeName(cyc.getObjectTool().makeCycSentence(sentence), georgzine);
    checkUnicodeName(CyclParserUtil.parseCycLSentence(sentence, true, cyc), georgzine);
  }

  private void checkUnicodeName(final FormulaSentence cycSentence,
          final String name) throws Exception {
    final int arity = cycSentence.getArity();
    assertEquals("Sentence has " + arity + " args.", 2, arity);
    assertEquals(name, cycSentence.getArg(2));
    final CycAccess cyc = CycAccessManager.getCurrentAccess();
    final ElMt mt = BASE_KB;
    try {
      cyc.getAssertTool().assertGaf(cycSentence, mt);
      final CycVariable var = CycObjectFactory.makeCycVariable("?X");
      final FormulaSentence qSentence = cycSentence.deepCopy();
      qSentence.getArgs().set(2, var);
      final CycList<Object> knownNames = cyc.getInferenceTool().queryVariable(var, qSentence, mt);
      assertTrue("Couldn't find " + name + " in " + knownNames, knownNames.contains(name));
    } finally {
      cyc.getUnassertTool().unassertGaf(cycSentence, mt);
    }
  }

  @Test
  public void testHLIDGeneration() {
    System.out.println("\n**** testHLIDGeneration ****");

    HLIDGenerationTest(1, cycAccess);
    HLIDGenerationTest(0, cycAccess);
    HLIDGenerationTest(-1, cycAccess);
    //the following two tests are broken because of an apparent problem
    //in sending 1.0 over to Cyc via the API (it seems to come out on the
    //Cyc side as 1 instead of 1.0 or 1.0d0
//    HLIDGenerationTest(-1.0, cycAccess);
//    HLIDGenerationTest(1.0, cycAccess);
    HLIDGenerationTest(-1.1231231, cycAccess);
    HLIDGenerationTest(-12385341.1231231, cycAccess);

    HLIDGenerationTest("", cycAccess);
    HLIDGenerationTest("The quick brown fox jumps over the lazy dog",
            cycAccess);

    System.out.println("**** testHLIDGeneration OK ****");

  }

  private boolean HLIDGenerationTest(Object obj, CycAccess cyc) {
    try {
      String cmd = "(compact-hl-external-id-string "
              + DefaultCycObject.stringApiValue(obj) + ")";
      String cycId = cyc.converse().converseString(cmd);
      String apiId = null;
      if (obj instanceof String) {
        apiId = CompactHlidConverter.converter().toCompactHLId((String) obj);
        assertTrue(CompactHlidConverter.converter().isStringCompactHLId(apiId));
      } else if (obj instanceof Number) {
        apiId = CompactHlidConverter.converter().toCompactHLId((Number) obj);
        assertTrue(CompactHlidConverter.converter().isNumberCompactHLId(apiId));
      }
      assertTrue(apiId.equals(cycId));
    } catch (Throwable e) {
      fail(e.toString());
    }
    return true;
  }

  @Test
  public void testHLIDRoundTripConversion() {
    System.out.println("\n**** testHLIDRoundTripConversion ****");
    HLIDRoundTripConversionTest(1);
    HLIDRoundTripConversionTest(0);
    HLIDRoundTripConversionTest(-1);
    HLIDRoundTripConversionTest(-1.0);
    HLIDRoundTripConversionTest(1.0);
    HLIDRoundTripConversionTest(-1.1231231);
    HLIDRoundTripConversionTest(-12385341.1231231);

    HLIDRoundTripConversionTest("");
    HLIDRoundTripConversionTest("This is a string");

    System.out.println("**** testHLIDRoundTripConversion OK ****");
  }

  private boolean HLIDRoundTripConversionTest(Object obj) {
    try {
      String apiId = null;
      if (obj instanceof String) {
        apiId = CompactHlidConverter.converter().toCompactHLId((String) obj);
      } else if (obj instanceof Number) {
        apiId = CompactHlidConverter.converter().toCompactHLId((Number) obj);
      }
      Object roundTripObj = CompactHlidConverter.converter().fromCompactHLId(
              apiId);
      assertTrue(obj.equals(roundTripObj));
    } catch (Throwable e) {
      fail(e.toString());
    }
    return true;
  }

  /**
   * Tests the CycLeaseManager.
   */
  @Test
  public void testCycLeaseManager() {
    System.out.println("\n**** testCycLeaseManager ****");

    try {
      Thread.sleep(6000);
    } catch (Throwable e) {
      e.printStackTrace();
      fail(e.toString());
    }

    /*
     if (cycAccess.getLookupTool().getCycLeaseManager() != null) {
     cycAccess.getLookupTool().getCycLeaseManager().removeAllListeners();
     }
     */
    for (Map.Entry<InputStream, LeaseManager> kv : cycAccess.getCycConnection().getCycLeaseManagerCommMap().entrySet()) {
      if (kv.getValue() != null) {
        kv.getValue().removeAllListeners();
      }
    }

    System.out.println("**** testCycLeaseManager OK ****");
  }

  /**
   * Tests the CycLeaseManager.
   */
  @Test
  public void testCycLeaseManager2() throws Exception {
    System.out.println("\n**** testCycLeaseManager2 ****");

    if (cycAccess.getCycLeaseManager() != null) {
      cycAccess.getCycLeaseManager().removeAllListeners();
    }

    System.out.println("**** testCycLeaseManager2 OK ****");
  }

  /**
   * Tests inference problem store reuse.
   */
  @Test
  public void testInferenceProblemStoreReuse() throws CycConnectionException {
    System.out.println("\n**** testInferenceProblemStoreReuse ****");
    final String inferenceProblemStoreName = "my-problem-store";
    cycAccess.getInferenceTool().initializeNamedInferenceProblemStore(
            inferenceProblemStoreName, null);
    FormulaSentence query = cycAccess.getObjectTool().makeCycSentence(
            OBJECT_FOUND_WHAT_WHERE.cyclify());
    CycArrayList variables = new CycArrayList();
    variables.add(makeCycVariable("?WHAT"));
    variables.add(makeCycVariable("?WHERE"));
    InferenceParameters queryProperties = new DefaultInferenceParameters(
            cycAccess);
    CycConstant universeDataMt = cycAccess.getLookupTool().getKnownConstantByGuid(
            UNIVERSE_DATA_MT_GUID_STRING);
    CycList response = cycAccess.getInferenceTool().queryVariables(variables, query,
            universeDataMt, queryProperties, inferenceProblemStoreName);
    assertNotNull(response);
    response = cycAccess.getInferenceTool().queryVariables(variables, query, universeDataMt,
            queryProperties, inferenceProblemStoreName);
    assertNotNull(response);
    response = cycAccess.getInferenceTool().queryVariables(variables, query, universeDataMt,
            queryProperties, inferenceProblemStoreName);
    assertNotNull(response);
    cycAccess.getInferenceTool().destroyInferenceProblemStoreByName(inferenceProblemStoreName);

    System.out.println("**** testInferenceProblemStoreReuse OK ****");
  }

  /**
   * Tests inference problem store reuse.
   */
  @Test
  public void testInvalidTerms() {
    System.out.println("\n**** testInvalidTerms ****");
    // invalid constant
    {
      final boolean invalidConstantCfaslWorks = false; // Until system 1.131015.
      if (invalidConstantCfaslWorks) {
        try {
          final String command = "(list \"a\" 1 " + BRAZIL_STRING + " (cfasl-invalid-constant) \"z\")";
          cycAccess.converse().converseList(command);
          fail("Expected CycApiException not thrown.");
        } catch (CycApiException e) {
        } catch (CycConnectionException e) {
          fail(StringUtils.getStringForException(e));
        } catch (Throwable t) {
          fail(t.getLocalizedMessage());
        }
        try {
          final String command = "(list \"a\" 1 " + BRAZIL_STRING + " \"z\")";
          final CycList result = cycAccess.converse().converseList(command);
          assertEquals(result.toString(), "(\"a\" 1 Brazil \"z\")");
        } catch (Throwable e) {
          fail(StringUtils.getStringForException(e));
        }
      }
    }

    // invalid nart
    try {
      final String command = "(list \"a\" 1 " + BRAZIL_STRING + " (cfasl-invalid-nart) \"z\")";
      final CycList result = cycAccess.converse().converseList(command);
      fail("Expected CycApiException not thrown.");
    } catch (CycApiException e) {
    } catch (CycConnectionException e) {
      fail(StringUtils.getStringForException(e));
    }
    try {
      final String command = "(list \"a\" 1 " + BRAZIL_STRING + " \"z\")";
      final CycList result = cycAccess.converse().converseList(command);
      assertEquals(result.toString(), "(\"a\" 1 Brazil \"z\")");
    } catch (Throwable e) {
      fail(StringUtils.getStringForException(e));
    }

    // invalid assertion
    try {
      final String command = "(list \"a\" 1 " + BRAZIL_STRING + " (create-sample-invalid-assertion) \"z\")";
      final CycList result = cycAccess.converse().converseList(command);
      fail("Expected CycApiException not thrown.");
    } catch (CycApiException e) {
      //System.out.println(e.getMessage());
    } catch (CycConnectionException e) {
      fail(StringUtils.getStringForException(e));
    }
    try {
      final String command = "(list \"a\" 1 " + BRAZIL_STRING + " \"z\")";
      final CycList result = cycAccess.converse().converseList(command);
      assertEquals(result.toString(), "(\"a\" 1 Brazil \"z\")");
    } catch (Throwable e) {
      fail(StringUtils.getStringForException(e));
    }
    System.out.println("**** testInvalidTerms OK ****");
  }

  private static final String ISA_STRING = ISA.cyclify();
  private static final String PLUS_FN_STRING = PLUS_FN.cyclify();
  private static final String PLANT_STRING = TestConstants.PLANT.cyclify();
  private static final String DOG_STRING = TestConstants.DOG.cyclify();
  private static final String BRAZIL_STRING = TestConstants.BRAZIL.cyclify();
  private static final String GENLS_STRING = CommonConstants.GENLS.cyclify();
  private static final String COLLECTION_STRING = CommonConstants.COLLECTION.cyclify();
  private static final String ANIMAL_STRING = TestConstants.ANIMAL.cyclify();
  private static final String DOMESTICATED_ANIMAL_STRING = TestConstants.DOMESTICATED_ANIMAL.cyclify();
  private static final String TAME_ANIMAL_STRING = TestConstants.TAME_ANIMAL.cyclify();
  private static final String NON_PERSON_ANIMAL_STRING = TestConstants.NON_PERSON_ANIMAL.cyclify();
  private static final String FLUID_FLOW_COMPLETE_STRING = TestConstants.FLUID_FLOW_COMPLETE.cyclify();
  private static final String NTH_SUB_SITUATION_TYPE_OF_TYPE_FN_STRING = TestConstants.NTH_SUB_SITUATION_TYPE_OF_TYPE_FN.cyclify();
  private static final String PREPARING_FOOD_ITEM_FN_STRING = TestConstants.PREPARING_FOOD_ITEM_FN.cyclify();
  private static final String SPAGHETTI_MARINARA_STRING = TestConstants.SPAGHETTI_MARINARA.cyclify();
  private static final String HUMAN_ACTIVITIES_MT_STRING = TestConstants.HUMAN_ACTIVITIES_MT.cyclify();
  private static final String REMOTELY_EXPLOITABLE_FN_STRING = TestConstants.REMOTELY_EXPLOITABLE_FN.cyclify();
  private static final String VULNERABLE_TO_DTML_METHOD_EXECUTION_STRING = TestConstants.VULNERABLE_TO_DTML_METHOD_EXECUTION.cyclify();
  private static final String JUVENILE_FN_STRING = TestConstants.JUVENILE_FN.cyclify();
  private static final String INDEPENDENT_COUNTRY_STRING = TestConstants.INDEPENDENT_COUNTRY.cyclify();
  private static final String CAPITAL_CITY_STRING = TestConstants.CAPITAL_CITY.cyclify();
  private static final String FRANCE_STRING = TestConstants.FRANCE.cyclify();
  private static final String CITY_OF_PARIS_FRANCE_STRING = TestConstants.CITY_OF_PARIS_FRANCE.cyclify();
  private static final String GIVEN_NAMES_STRING = GIVEN_NAMES.cyclify();
  private static final String GUEST_STRING = GUEST.cyclify();
  private static final String PERFORMED_BY_STRING = PERFORMED_BY.cyclify();
  private static final String SALIENT_ASSERTIONS_STRING = SALIENT_ASSERTIONS.cyclify();
  private static final String COLLECTION_UNION_FN_STRING = COLLECTION_UNION_FN.cyclify();
  private static final String THE_SET_STRING = THE_SET.cyclify();
  private static final String TOURIST_STRING = TOURIST.cyclify();
  private static final String GROUP_FN_STRING = GROUP_FN.cyclify();
  private static final String COMPUTATIONAL_SYSTEM_STRING = COMPUTATIONAL_SYSTEM.cyclify();

}
