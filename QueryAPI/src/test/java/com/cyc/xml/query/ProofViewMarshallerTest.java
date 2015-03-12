/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cyc.xml.query;

/*
 * #%L
 * File: ProofViewMarshallerTest.java
 * Project: Query API Implementation
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

import com.cyc.km.query.answer.justification.ProofViewJustification;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import javax.xml.bind.JAXBException;

import org.junit.After;
import org.junit.AfterClass;

import static com.cyc.xml.query.Utils.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.cyc.base.CycApiException;
import com.cyc.base.CycConnectionException;
import com.cyc.base.CycTimeOutException;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.KBApiException;
import com.cyc.query.exception.QueryConstructionException;
import com.cyc.session.SessionApiException;

/**
 *
 * @author baxter
 */
public class ProofViewMarshallerTest {

  public ProofViewMarshallerTest() {
  }

  @BeforeClass
  public static void setUpClass() throws IOException, UnknownHostException, JAXBException, 
          CycApiException, CycConnectionException, CycTimeOutException, QueryConstructionException, 
          SessionApiException, CreateException, KBApiException {
    setup();
    proofViewJustification = new ProofViewJustification(answer);
  }

  @AfterClass
  public static void tearDownClass() {
    teardown();
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }
  private static ProofViewJustification proofViewJustification;

  /**
   * Test of marshal method, of class ProofViewMarshaller.
   */
  @Test
  public void testMarshal_ProofView_Writer() throws Exception {
    System.out.println("\nmarshal to writer");
    final File destination = File.createTempFile("proofViewTest", ".xml");
    System.out.println("Marshaling proof view to " + destination);
    final ProofView proofView = proofViewJustification.getProofView();
    assertNotNull("Failed to get proof view.", proofView);
    new ProofViewMarshaller().marshal(proofView, new FileWriter(destination));
  }

  /**
   * Test of marshal method, of class ProofViewMarshaller.
   */
  @Test
  public void testMarshal_ProofView_OutputStream() throws Exception {
    System.out.println("\nmarshal to stream");
    final ProofView proofView = proofViewJustification.getProofView();
    assertNotNull("Failed to get proof view.", proofView);
    new ProofViewMarshaller().marshal(proofView, System.out);
  }

  /**
   * Test round-trip marshalling and unmarshalling.
   */
  @Test
  public void testMarshalUnmarshalRoundTrip() throws Exception {
    System.out.println("\nTest marshal-unmarshal round trip.");
    final File file1 = File.createTempFile("proofViewTest", ".xml");
    final ProofView proofView = proofViewJustification.getProofView();
    final ProofViewMarshaller marshaller = new ProofViewMarshaller();
    marshaller.marshal(proofView, new FileWriter(file1));
    final ProofView proofView2 = new ProofViewUnmarshaller().unmarshalProofview(new FileInputStream(
            file1));
    final File file2 = File.createTempFile("proofViewTest", ".xml");
    marshaller.marshal(proofView2, new FileWriter(file2));
    System.out.println("Marshalled proof views to " + file1 + "\n and " + file2);
    assertEquals("Proof view did not survive marshalling/unmarshalling.",
            readFile(file1), readFile(file2));
  }

  private static String readFile(File file) throws IOException {
    FileInputStream stream = new FileInputStream(file);
    try {
      FileChannel fc = stream.getChannel();
      MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
      /* Instead of using default, pass in a decoder. */
      return Charset.defaultCharset().decode(bb).toString();
    } finally {
      stream.close();
    }
  }
}
