package com.cyc.baseclient.examples;

/*
 * #%L
 * File: ApiDemo.java
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

import com.cyc.base.CycAccess;
import com.cyc.base.CycAccessManager;
import com.cyc.base.exception.CycConnectionException;
import com.cyc.base.cycobject.FormulaSentence;
import com.cyc.base.cycobject.Fort;
import com.cyc.base.cycobject.CycList;
import com.cyc.base.cycobject.Guid;
import com.cyc.query.InferenceParameters;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.cyc.baseclient.CycClientManager;
import com.cyc.base.exception.CycApiException;
import com.cyc.base.cycobject.Nart;
import com.cyc.baseclient.CycClient;
import com.cyc.baseclient.cycobject.NartImpl;
import com.cyc.baseclient.inference.params.DefaultInferenceParameters;
import com.cyc.baseclient.nl.ParaphraserFactory;
import com.cyc.baseclient.util.Log;
import com.cyc.nl.Paraphraser;
import com.cyc.session.exception.OpenCycUnsupportedFeatureException;

// FIXME: TestSentences - nwinant ? ? ? ? ? 

/**
 * Provides a simple demo of the Base Client.<p>
 *
 * @version $Id: ApiDemo.java 163356 2016-01-04 20:55:47Z nwinant $
 * @author Stephen L. Reed
 */

public class ApiDemo {
  
  /**
   * the CycAccess object
   */
  protected CycAccess cycAccess;
  
  public ApiDemo() {
    Log.makeLog();
    Log.current.println("Initializing Cyc server connection, and caching frequently used terms.");
    try {
      cycAccess = CycAccessManager.getCurrentAccess();
    }
    catch (Exception e) {
      Log.current.errorPrintln(e.getMessage());
      Log.current.printStackTrace(e);
    }
    cycAccess.traceOn();
    Log.current.println("Now tracing Cyc server messages");
  }
  
  /**
   * Interacts with the user to perform specified demos.
   */
  protected void demoInteraction() {
    Log.current.println("Ready.  Enter demo number 1 ... 17, or exit");
    BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
    try {
      while (true) {
        System.out.print("> ");
        String userDemoCommand = stdin.readLine();
        if (userDemoCommand.equals("exit"))
          return;
        int demoNbr = 0;
        try {
          demoNbr = Integer.parseInt(userDemoCommand);
        }
        catch (NumberFormatException e) {
          Log.current.println("Not a valid demo number");
          continue;
        }
        switch (demoNbr) {
          case 1:
            demo1();
            break;
          case 2:
            demo2();
            break;
          case 3:
            demo3();
            break;
          case 4:
            demo4();
            break;
          case 5:
            demo5();
            break;
          case 6:
            demo6();
            break;
          case 7:
            demo7();
            break;
          case 8:
            demo8();
            break;
          case 9:
            demo9();
            break;
          case 10:
            demo10();
            break;
          case 11:
            System.out.println("Demo 11 has been removed.");
            break;
          case 12:
            demo12();
            break;
          case 13:
            demo13();
            break;
          case 14:
            demo14();
            break;
          case 15:
            demo15();
            break;
          case 16:
            demo16();
            break;
          case 17:
            demo17();
            break;
          default:
            Log.current.println("Not a valid demo number");
        }
      }
    }
    catch (Exception e) {
      Log.current.errorPrintln(e.getMessage());
      Log.current.printStackTrace(e);
    }
  }
  
  /**
   * Provides the main method for the api demo application
   */
  public static void main(String[] args) {
    ApiDemo apiDemo = new ApiDemo();
    apiDemo.demoInteraction();
    // kill all threads
    System.exit(0);
  }
  
  /**
   * Demonstrates getKnownConstantByName api function.
   */
  protected void demo1() throws CycConnectionException, CycApiException {
    Log.current.println("Demonstrating getKnownConstantByName api function.\n");
    Fort snowSkiing = cycAccess.getLookupTool().getKnownConstantByName("SnowSkiing");
    Log.current.println("\nThe obtained constant is " + snowSkiing.cyclify());
  }
  
  /**
   * Demonstrates getConstantCycGuid api function.
   */
  protected void demo2() throws CycConnectionException, CycApiException {
    Log.current.println("Demonstrating getConstantGuid api function.\n");
    Guid unitedStatesOfAmericaGuid = cycAccess.getLookupTool().getConstantGuid("UnitedStatesOfAmerica");
    Log.current.println("\nThe obtained guid is " + unitedStatesOfAmericaGuid);
  }
  
  /**
   * Demonstrates getComment api function.
   */
  protected void demo3() throws CycConnectionException, CycApiException {
    Log.current.println("Demonstrating getComment api function.\n");
    String comment = cycAccess.getLookupTool().getComment(cycAccess.getLookupTool().getKnownConstantByName("bordersOn"));
    Log.current.println("\nThe obtained comment is:\n" + comment);
  }
  
  /**
   * Demonstrates getIsas api function.
   */
  protected void demo4() throws CycConnectionException, CycApiException {
    Log.current.println("Demonstrating getIsas api function.\n");
    CycList isas = cycAccess.getLookupTool().getIsas(cycAccess.getLookupTool().getKnownConstantByName("BillClinton"));
    Log.current.println("\nThe obtained isas are:\n" + isas.cyclify());
  }
  
  /**
   * Demonstrates getGenls api function.
   */
  protected void demo5() throws CycConnectionException, CycApiException {
    Log.current.println("Demonstrating getGenls api function.\n");
    CycList genls = cycAccess.getLookupTool().getGenls(cycAccess.getLookupTool().getKnownConstantByName("Dog"));
    Log.current.println("\nThe obtained direct genls are:\n" + genls.cyclify());
  }
  
  /**
   * Demonstrates getArity api function.
   */
  protected void demo6() throws CycConnectionException, CycApiException {
    Log.current.println("Demonstrating getArity api function.\n");
    int arity = cycAccess.getLookupTool().getArity(cycAccess.getLookupTool().getKnownConstantByName("likesAsFriend"));
    Log.current.println("\nThe obtained arity is " + arity);
  }
  
  /**
   * Demonstrates arg1Isas api function.
   */
  protected void demo7() throws CycConnectionException, CycApiException {
    Log.current.println("Demonstrating arg1Isas api function.\n");
    CycList arg1Isas = cycAccess.getLookupTool().getArg1Isas(cycAccess.getLookupTool().getKnownConstantByName("performedBy"));
    Log.current.println("\nThe obtained arg1Isas are:\n" + arg1Isas.cyclify());
  }
  
  /**
   * Demonstrates getArgNGenls api function.
   */
  protected void demo8() throws CycConnectionException, CycApiException {
    Log.current.println("Demonstrating getArgNGenls api function.\n");
    CycList argNGenls = cycAccess.getLookupTool().getArgNGenls(cycAccess.getLookupTool().getKnownConstantByName("skillCapableOf"), 2);
    Log.current.println("\nThe obtained getArgNGenls are:\n" + argNGenls.cyclify());
  }
  
  /**
   * Demonstrates getParaphrase (with quantified formula) api function.
   */
  protected void demo9() throws CycConnectionException, CycApiException {
    Log.current.println("Demonstrating getParaphrase api function.\n");
    FormulaSentence formula = cycAccess.getObjectTool().makeCycSentence("(#$forAll ?THING (#$isa ?Thing #$Thing))");
    Paraphraser p = ParaphraserFactory.getInstance(ParaphraserFactory.ParaphrasableType.FORMULA);
    String paraphrase = p.paraphrase(formula).getString();
    Log.current.println("\nThe obtained paraphrase for\n" + formula + "\nis:\n" + paraphrase);
  }
  
  /**
   * Demonstrates getParaphrase (with quantified formula) api function.
   * @todo this (and several others near here) should move into the NLAPI.
   */
  protected void demo10() throws CycConnectionException, CycApiException {
    if (cycAccess.isOpenCyc()) {
      Log.current.println("\nThis demo is not available in OpenCyc");
    }
    else {
    Log.current.println("Demonstrating getParaphrase api function.\n");
    FormulaSentence formula = cycAccess.getObjectTool().makeCycSentence(
      "(#$thereExists ?PLANET\n" +
      "  (#$and\n" +
      "    (#$isa ?PLANET #$Planet)\n" +
      "    (#$orbits ?PLANET #$Sun)))");
    Paraphraser p = ParaphraserFactory.getInstance(ParaphraserFactory.ParaphrasableType.FORMULA);
    String paraphrase = p.paraphrase(formula).getString();
    Log.current.println("\nThe obtained paraphrase for\n" + formula + "\nis:\n" + paraphrase);
  }
  }
  
  /**
   * Demonstrates usage of Nart and getInstanceSiblings api function.
   */
  protected void demo12() throws CycConnectionException, CycApiException {
    Log.current.println("Demonstrating CycNart and getInstanceSiblings api function.\n");
    Nart usGovernment = new NartImpl(cycAccess.getLookupTool().getKnownConstantByName("GovernmentFn"),
    cycAccess.getLookupTool().getKnownConstantByName("UnitedStatesOfAmerica"));
    CycList siblings = cycAccess.getLookupTool().getInstanceSiblings(usGovernment);
    Log.current.println("\nThe obtained instance sibling terms of " + usGovernment + "\nare:\n" + siblings.cyclify());
  }
  
  /**
   * Demonstrates usage of isQueryTrue api function.
   */
  protected void demo13() throws CycConnectionException, CycApiException {
    Log.current.println("Demonstrating isQueryTrue api function.\n");
    FormulaSentence gaf = cycAccess.getObjectTool().makeCycSentence("(#$likesAsFriend #$BillClinton #$JimmyCarter)");
    Fort mt = cycAccess.getLookupTool().getKnownConstantByName("PeopleDataMt");
    InferenceParameters queryProperties = new DefaultInferenceParameters(cycAccess);
    boolean isQueryTrue = cycAccess.getInferenceTool().isQueryTrue(gaf, mt, queryProperties);
    if (isQueryTrue)
      Log.current.println("\nThe assertion\n" + gaf + "\nis true in the " + mt.cyclify());
    else
      Log.current.println("\nThe assertion\n" + gaf + "\nis not known to be true in the " + mt.cyclify());
  }
  
  /**
   * Demonstrates usage of the assertGaf api function.
   */
  protected void demo14() throws CycConnectionException, CycApiException {
    Log.current.println("Demonstrating usage of the assertGaf api function.\n");
    Fort mt = cycAccess.getLookupTool().getKnownConstantByName("PeopleDataMt");
    FormulaSentence gaf = cycAccess.getObjectTool().makeCycSentence("(#$likesAsFriend #$BillClinton #$JimmyCarter)");
    cycAccess.getAssertTool().assertGaf(gaf, mt);
  }
  
  /**
   * Demonstrates usage of the unassertGaf api function.
   */
  protected void demo15() throws CycConnectionException, CycApiException {
    Log.current.println("Demonstrating usage of the unassertGaf api function.\n");
    Fort mt = cycAccess.getLookupTool().getKnownConstantByName("PeopleDataMt");
    FormulaSentence gaf = cycAccess.getObjectTool().makeCycSentence("(#$likesAsFriend #$BillClinton #$JimmyCarter)");
    cycAccess.getUnassertTool().unassertGaf(gaf, mt);
  }
  
  /**
   * Demonstrates usage of the rkfPhraseReader api function.
   * @throws com.cyc.base.exception.CycConnectionException
   * @throws com.cyc.session.exception.OpenCycUnsupportedFeatureException
   */
  protected void demo16() throws CycConnectionException, CycApiException, OpenCycUnsupportedFeatureException {
    if (cycAccess.isOpenCyc()) {
      Log.current.println("\nThis demo is not available in OpenCyc");
    }
    else {
    Log.current.println("Demonstrating usage of the rkfPhraseReader api function.\n");
    String phrase = "penguins";
      Fort inferencePsc =
      cycAccess.getLookupTool().getKnownConstantByGuid("bd58915a-9c29-11b1-9dad-c379636f7270");
      Fort rkfEnglishLexicalMicrotheoryPsc =
      cycAccess.getLookupTool().getKnownConstantByGuid("bf6df6e3-9c29-11b1-9dad-c379636f7270");
    final CycClient cycClient = CycClientManager.getClientManager().fromCycAccess(cycAccess);
    CycList parsingExpression = cycClient.getRKFTool().rkfPhraseReader(phrase,
            rkfEnglishLexicalMicrotheoryPsc,
            inferencePsc);
    Log.current.println("the result of parsing the phrase \"" + phrase + "\" is\n" + parsingExpression);
  }
  }
  
  /**
   * Demonstrates usage of the generateDisambiguationPhraseAndTypes api function.
   */
  protected void demo17() throws CycConnectionException, CycApiException {
    if (cycAccess.isOpenCyc()) {
      Log.current.println("\nThis demo is not available in OpenCyc");
    }
    else {
    Log.current.println("Demonstrating usage of the generateDisambiguationPhraseAndTypes api function.\n");
    Fort mt = cycAccess.getLookupTool().getKnownConstantByName("PeopleDataMt");
    CycList objects = cycAccess.getObjectTool().makeCycList("(#$Penguin #$PittsburghPenguins)");
    CycList disambiguationExpression = cycAccess.getObjectTool().generateDisambiguationPhraseAndTypes(objects);
      Log.current.println("the result of disambiguating the objects \"" + objects.cyclify() + "\" is\n" +
      disambiguationExpression);
  }
  }
  
}
