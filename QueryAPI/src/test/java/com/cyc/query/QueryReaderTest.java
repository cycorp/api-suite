package com.cyc.query;

/*
 * #%L
 * File: QueryReaderTest.java
 * Project: Query API
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
import com.cyc.kb.KBIndividual;
import com.cyc.kb.client.BinaryPredicateImpl;
import com.cyc.kb.client.KBIndividualImpl;
import com.cyc.kb.client.SentenceImpl;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author baxter
 */
public class QueryReaderTest {

  public QueryReaderTest() {
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
    TestUtils.ensureConstantsInitialized();
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  //@Test
  public void testLoadIstQuery() throws Exception {
    System.out.println("testLoadIstQuery");
    final Query q1 = new Query(
            new SentenceImpl(BinaryPredicateImpl.get("ist"), TestUtils.inferencePSC,
                    TestUtils.xIsaBird()), TestUtils.inferencePSC);
    try {
      q1.saveAs("QueryForTestLoadIstQuery");
      final KBIndividual queryObj = KBIndividualImpl.get("QueryForTestLoadIstQuery");
      final Query q = Query.load(queryObj); // TODO: this test is throwing QueryConstructionExceptions
      System.out.println(q.getQuerySentence());
      q.setMaxNumber(1);
      assertTrue("Failed to get any answers.", q.getAnswerCount() > 0);
    } finally {
      q1.getId().delete();
    }
  }

  /**
   * Test of queryFromXML method, of class QueryReader.
   * @throws java.lang.Exception
   */
  @Test
  public void testQueryFromXML() throws Exception {
    System.out.println("queryFromXML");
    InputStream stream = new ByteArrayInputStream(xml.getBytes());
    QueryReader instance = new ValidatingQueryReader();
    Query query = instance.queryFromXML(stream);
    final QueryApiTestConstants testConstants = QueryApiTestConstants.getInstance();
    assertEquals("Wrong context.", testConstants.generalCycKECollector, query.getContext());
    assertEquals("Wrong formula.", testConstants.academyAwardWinners, query.getQuerySentence());
    assertEquals("Wrong max time.", 60, (Object) query.getMaxTime());
  }
  private static final String xml = "<?xml version=\"1.0\" encoding=\"US-ASCII\" standalone=\"no\"?>"
          + "<cyclQuery xmlns=\"http://www.opencyc.org/xml/cyclQuery/\"><queryID>"
          + "<function reified=\"true\" xmlns=\"http://www.opencyc.org/xml/cycML/\">"
          + " <constant>"
          + "  <guid>8a8a8d13-4760-11db-8fd2-0002b3a85161</guid>"
          + "  <name>QueryTemplateFromSentenceAndIDFn</name>"
          + " </constant>"
          + " <function reified=\"false\">"
          + "  <constant>"
          + "   <guid>80605b12-436e-11d6-8000-00a0c9da2002</guid>"
          + "   <name>Quote</name>"
          + "  </constant>"
          + "  <sentence>"
          + "   <predicate>"
          + "    <constant>"
          + "     <guid>c090f65d-9c29-11b1-9dad-c379636f7270</guid>"
          + "     <name>academyAwardWinner</name>"
          + "    </constant>"
          + "   </predicate>"
          + "   <variable>?X</variable>"
          + "   <variable>?Y</variable>"
          + "   <variable>?Z</variable>"
          + "  </sentence>"
          + " </function>"
          + " <string>e0d0803c-430e-11e2-9de9-00219b4436b2</string>"
          + "</function>"
          + "  </queryID><queryFormula>"
          + "   <sentence xmlns=\"http://www.opencyc.org/xml/cycML/\">"
          + "    <predicate>"
          + "     <constant>"
          + "      <guid>c090f65d-9c29-11b1-9dad-c379636f7270</guid>"
          + "      <name>academyAwardWinner</name>"
          + "     </constant>"
          + "    </predicate>"
          + "    <variable>?X</variable>"
          + "    <variable>?Y</variable>"
          + "    <variable>?Z</variable>"
          + "   </sentence>"
          + "  </queryFormula><queryMt>"
          + "   <function reified=\"true\" xmlns=\"http://www.opencyc.org/xml/cycML/\">"
          + "    <constant>"
          + "     <guid>d5d71b27-24c5-4b0d-bcb5-072449b3e77e</guid>"
          + "     <name>AssistedReaderSourceSpindleCollectorForTaskFn</name>"
          + "    </constant>"
          + "    <constant>"
          + "     <guid>18ea376c-b788-11db-8000-000ea663fab7</guid>"
          + "     <name>GeneralCycKETask-Allotment</name>"
          + "    </constant>"
          + "   </function>"
          + "  </queryMt><queryComment>"
          + "   <string xmlns=\"http://www.opencyc.org/xml/cycML/\">Z is X made of Y.</string>"
          + "  </queryComment><queryInferenceProperties><queryInferenceProperty><propertySymbol>MAX-TRANSFORMATION-DEPTH"
          + "    </propertySymbol><propertyValue>"
          + "     <number xmlns=\"http://www.opencyc.org/xml/cycML/\">0</number>"
          + "    </propertyValue>"
          + "   </queryInferenceProperty><queryInferenceProperty><propertySymbol>ALLOW-INDETERMINATE-RESULTS?"
          + "    </propertySymbol><propertyValue>"
          + "     <symbol xmlns=\"http://www.opencyc.org/xml/cycML/\">"
          + "      <package>COMMON-LISP</package>"
          + "      <name>T</name>"
          + "     </symbol>"
          + "    </propertyValue>"
          + "   </queryInferenceProperty><queryInferenceProperty><propertySymbol>NEW-TERMS-ALLOWED?"
          + "    </propertySymbol><propertyValue>"
          + "     <symbol xmlns=\"http://www.opencyc.org/xml/cycML/\">"
          + "      <package>COMMON-LISP</package>"
          + "      <name>T</name>"
          + "     </symbol>"
          + "    </propertyValue>"
          + "   </queryInferenceProperty><queryInferenceProperty><propertySymbol>MAX-TIME"
          + "    </propertySymbol><propertyValue>"
          + "     <number xmlns=\"http://www.opencyc.org/xml/cycML/\">60</number>"
          + "    </propertyValue>"
          + "   </queryInferenceProperty><queryInferenceProperty><propertySymbol>DISJUNCTION-FREE-EL-VARS-POLICY"
          + "    </propertySymbol><propertyValue>"
          + "     <symbol xmlns=\"http://www.opencyc.org/xml/cycML/\">"
          + "      <package>KEYWORD</package>"
          + "      <name>COMPUTE-INTERSECTION</name>"
          + "     </symbol>"
          + "    </propertyValue>"
          + "   </queryInferenceProperty><queryInferenceProperty><propertySymbol>PRODUCTIVITY-LIMIT"
          + "    </propertySymbol><propertyValue>"
          + "     <number xmlns=\"http://www.opencyc.org/xml/cycML/\">2000000</number>"
          + "    </propertyValue>"
          + "   </queryInferenceProperty><queryInferenceProperty><propertySymbol>COMPUTE-ANSWER-JUSTIFICATIONS?"
          + "    </propertySymbol><propertyValue>"
          + "     <symbol xmlns=\"http://www.opencyc.org/xml/cycML/\">"
          + "      <package>COMMON-LISP</package>"
          + "      <name>T</name>"
          + "     </symbol>"
          + "    </propertyValue>"
          + "   </queryInferenceProperty>"
          + "  </queryInferenceProperties>"
          + " </cyclQuery>";
}
