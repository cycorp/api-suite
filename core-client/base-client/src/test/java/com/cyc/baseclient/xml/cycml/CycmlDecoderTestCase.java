package com.cyc.baseclient.xml.cycml;

/*
 * #%L
 * File: CycmlDecoderTestCase.java
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

/**
 *
 * @author baxter
 */
public enum CycmlDecoderTestCase {

  NAT1("<?xml version=\"1.0\" encoding=\"US-ASCII\" standalone=\"no\"?>"
  + "  <function reified=\"false\" xmlns=\"http://www.opencyc.org/xml/cycML/\">"
  + "   <constant>"
  + "    <guid>8a8a8d13-4760-11db-8fd2-0002b3a85161</guid>"
  + "    <name>QueryTemplateFromSentenceAndIDFn</name>"
  + "   </constant>"
  + "   <sentence>"
  + "    <predicate>"
  + "     <constant>"
  + "      <guid>bd58a962-9c29-11b1-9dad-c379636f7270</guid>"
  + "      <name>performedBy</name>"
  + "     </constant>"
  + "    </predicate>"
  + "    <variable>?X</variable>"
  + "    <variable>?Y</variable>"
  + "   </sentence>"
  + "   <string>e0d08023-430e-11e2-9de9-00219b4436b2</string>"
  + "  </function>",
  "(QueryTemplateFromSentenceAndIDFn (performedBy ?X ?Y) \"e0d08023-430e-11e2-9de9-00219b4436b2\")"),
  SENTENCE1("<?xml version=\"1.0\" encoding=\"US-ASCII\" standalone=\"no\"?>"
  + "   <sentence xmlns=\"http://www.opencyc.org/xml/cycML/\">"
  + "    <predicate>"
  + "     <constant>"
  + "      <guid>bd58a962-9c29-11b1-9dad-c379636f7270</guid>"
  + "      <name>performedBy</name>"
  + "     </constant>"
  + "    </predicate>"
  + "    <variable>?X</variable>"
  + "    <variable>?Y</variable>"
  + "   </sentence>", "(performedBy ?X ?Y)"),
  NAT2("<?xml version=\"1.0\" encoding=\"US-ASCII\" standalone=\"no\"?>"
  + "   <function reified=\"true\" xmlns=\"http://www.opencyc.org/xml/cycML/\">"
  + "    <constant>"
  + "     <guid>d5d71b27-24c5-4b0d-bcb5-072449b3e77e</guid>"
  + "     <name>AssistedReaderSourceSpindleCollectorForTaskFn</name>"
  + "    </constant>"
  + "    <constant>"
  + "     <guid>18ea376c-b788-11db-8000-000ea663fab7</guid>"
  + "     <name>GeneralCycKETask-Allotment</name>"
  + "    </constant>"
  + "   </function>",
  "(AssistedReaderSourceSpindleCollectorForTaskFn GeneralCycKETask-Allotment)"),
  STRING1(
  "<?xml version=\"1.0\" encoding=\"US-ASCII\" standalone=\"no\"?>"
  + "   <string xmlns=\"http://www.opencyc.org/xml/cycML/\">Y deliberately performed X.</string>",
  "Y deliberately performed X."),
  INT1("<?xml version=\"1.0\" encoding=\"US-ASCII\" standalone=\"no\"?>"
  + "     <number xmlns=\"http://www.opencyc.org/xml/cycML/\">0</number>", "0"),
  SYMBOL1("<?xml version=\"1.0\" encoding=\"US-ASCII\" standalone=\"no\"?>"
  + "     <symbol xmlns=\"http://www.opencyc.org/xml/cycML/\">"
  + "      <package>COMMON-LISP</package>"
  + "      <name>T</name>"
  + "     </symbol>", "T"),
  NAT3("<?xml version=\"1.0\" encoding=\"US-ASCII\" standalone=\"no\"?>"
  + "  <function reified=\"true\" xmlns=\"http://www.opencyc.org/xml/cycML/\">"
  + "   <constant>"
  + "    <guid>d5d71b27-24c5-4b0d-bcb5-072449b3e77e</guid>"
  + "    <name>AssistedReaderSourceSpindleCollectorForTaskFn</name>"
  + "   </constant>"
  + "   <constant>"
  + "    <guid>18ea376c-b788-11db-8000-000ea663fab7</guid>"
  + "    <name>GeneralCycKETask-Allotment</name>"
  + "   </constant>"
  + "  </function>",
  "(AssistedReaderSourceSpindleCollectorForTaskFn GeneralCycKETask-Allotment)"),
  SENTENCE2("<?xml version=\"1.0\" encoding=\"US-ASCII\" standalone=\"no\"?>"
  + "   <sentence xmlns=\"http://www.opencyc.org/xml/cycML/\">"
  + "    <predicate>"
  + "     <constant>"
  + "      <guid>36ca436c-4932-4847-a70f-9d733d29b82d</guid>"
  + "      <name>artifactTypeMadeMainlyOfType</name>"
  + "     </constant>"
  + "    </predicate>"
  + "    <variable>?X</variable>"
  + "    <variable>?Y</variable>"
  + "    <variable>?Z</variable>"
  + "   </sentence>",
  "(artifactTypeMadeMainlyOfType ?X ?Y ?Z)"),
  SYMBOL2("<?xml version=\"1.0\" encoding=\"US-ASCII\" standalone=\"no\"?>"
  + "     <symbol xmlns=\"http://www.opencyc.org/xml/cycML/\">"
  + "      <package>COMMON-LISP</package>"
  + "      <name>NIL</name>"
  + "     </symbol>", "NIL"),
  DATE1("<?xml version=\"1.0\" encoding=\"US-ASCII\" standalone=\"no\"?>"
  + "     <function reified=\"false\" xmlns=\"http://www.opencyc.org/xml/cycML/\">"
  + " <constant>"
  + "  <guid>bd58f29a-9c29-11b1-9dad-c379636f7270</guid>"
  + "  <name>YearFn</name>"
  + " </constant>"
  + " <number>2010</number>"
  + " </function>", "(YearFn 2010)");
  final String xml;
  final String cycl;

  private CycmlDecoderTestCase(String xml, String cycl) {
    this.xml = xml;
    this.cycl = cycl;
  }
}
