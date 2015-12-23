package com.cyc.baseclient;

/*
 * #%L
 * File: SimpleKbTransactionTest.java
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

import com.cyc.baseclient.testing.TestUtils;
import com.cyc.base.CycAccess;
import com.cyc.base.exception.CycConnectionException;
import com.cyc.base.cycobject.CycConstant;
import com.cyc.base.cycobject.Fort;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.cyc.baseclient.cycobject.CycFormulaSentence;
import com.cyc.base.cycobject.ElMt;
import com.cyc.base.exception.CycApiException;
import com.cyc.session.exception.SessionException;
import com.cyc.session.exception.SessionConfigurationException;

/**
 *
 * @author daves
 */
public class SimpleKbTransactionTest {

    final static String isa = CommonConstants.ISA.stringApiValue();
    final static String predicate = CommonConstants.PREDICATE.stringApiValue();
    final static String binaryPredicate = CommonConstants.BINARY_PREDICATE.stringApiValue();
    final static String collection = CommonConstants.COLLECTION.stringApiValue();
    final static String thing = CommonConstants.THING.stringApiValue();
    final static String universalVocabularyMt = CommonConstants.UNIVERSAL_VOCABULARY_MT.stringApiValue();
    final static String baseKB = CommonConstants.BASE_KB.stringApiValue();
    
    static CycAccess cyc = null;

    public SimpleKbTransactionTest() {
    }

    @BeforeClass
    public static void setUpClass() throws CycConnectionException, SessionException {
      TestUtils.ensureTestEnvironmentInitialized();
      cyc = TestUtils.getCyc();
    }

    @AfterClass
    public static void tearDownClass() throws SessionException {
        CycClientManager.getCurrentClient().close();
    }

    @Before
    public void setUp() throws CycConnectionException {
      cleanupTestConstants();
    }

    @After
    public void tearDown() {
      CycClient.setCurrentTransaction(null);
    }

    /**
     * Test of begin method, of class SimpleKBTransaction.
     */
    @Test
    public void testBegin() {
        System.out.println("begin");
        SimpleKbTransaction instance = new SimpleKbTransaction();
        instance.begin();
        assertTrue(CycClient.getCurrentTransaction().equals(instance));
        instance.commit();
        assertTrue(CycClient.getCurrentTransaction() == null);
    }

    /**
     * Test of begin method, of class SimpleKBTransaction. Make sure we throw an
     * Exception if you try to nest transactions.
     */
    @Test(expected = IllegalStateException.class)
    public void testBegin2() {
        System.out.println("begin2");
        SimpleKbTransaction instance = new SimpleKbTransaction();
        SimpleKbTransaction instance2 = new SimpleKbTransaction();
        instance.begin();
        instance2.begin();
    }

    /**
     * Test of commit method, of class SimpleKBTransaction.
     */
    @Test
    public void testCommit() throws CycConnectionException {
        System.out.println("commit");
        CycConstant commitTestConstant = null;
        CycConstant commitTestConstant2 = null;
        assertTrue("commitTestConstant already exists.", cyc.getLookupTool().getConstantByName("commitTestConstant") == null);
        assertTrue("commitTestConstant2 already exists.", cyc.getLookupTool().getConstantByName("commitTestConstant2") == null);
        try {
            commitTestConstant2 = cyc.getAssertTool().findOrCreateNewPermanent("commitTestConstant2");
            cyc.getAssertTool().assertGaf(CycFormulaSentence.makeCycSentence(cyc, "(" + isa + " #$commitTestConstant2 " + predicate + ")"), CommonConstants.BASE_KB);
            assert (cyc.getInspectorTool().isPredicate(commitTestConstant2));

            SimpleKbTransaction instance = new SimpleKbTransaction();
            CycClient.setCurrentTransaction(instance);
            assertTrue("Before commit, there's no active KBTransaction", CycClient.getCurrentTransaction() != null);
            cyc.getUnassertTool().unassertGaf(CycFormulaSentence.makeCycSentence(cyc, "(" + isa + " #$commitTestConstant2 " + predicate + ")"), CommonConstants.BASE_KB);
            assertTrue("Before commit, commitTestConstant2 is not known to be a predicate.", cyc.getInspectorTool().isPredicate(commitTestConstant2));
            commitTestConstant = cyc.getAssertTool().findOrCreateNewPermanent("commitTestConstant");
            assertTrue("Unable to find commitTestConstant before commit.", cyc.getLookupTool().getConstantByName("commitTestConstant") != null);

            cyc.getAssertTool().assertGaf(CycFormulaSentence.makeCycSentence(cyc, "(" + isa + " #$commitTestConstant " + predicate + ")"), CommonConstants.BASE_KB);
            assertFalse("commitTestConstant is known to be a predicate before the transaction has been committed.", cyc.getInspectorTool().isPredicate(commitTestConstant));

            instance.commit();

            assertTrue("Unable to find commitTestConstant after commit.", cyc.getLookupTool().getConstantByName("commitTestConstant") != null);
            assertTrue("commitTestConstant is not known to be a predicate after the transaction has been committed.", cyc.getInspectorTool().isPredicate(commitTestConstant));
            assertFalse("After commit, commitTestConstant2 is known to be a predicate.", cyc.getInspectorTool().isPredicate(commitTestConstant2));

            assertTrue("After commit, there's still an active KBTransaction", CycClient.getCurrentTransaction() == null);

        } finally {
            cleanupTestConstants();
        }
    }

    /**
     * Test that commit fails when there's a bad assertion, and that it is
 correctly rolled back.
     */
    @Test
    public void testCommitFailure() throws CycConnectionException {
        System.out.println("commit");
        CycConstant commitTestConstant = null;
        assertTrue("commitTestConstant already exists.", cyc.getLookupTool().getConstantByName("commitTestConstant") == null);

        CycConstant commitTestConstant2 = null;
        assertTrue("commitTestConstant2 already exists.", cyc.getLookupTool().getConstantByName("commitTestConstant2") == null);

        try {
            commitTestConstant2 = cyc.getAssertTool().findOrCreateNewPermanent("commitTestConstant2");
            cyc.getAssertTool().assertGaf(CycFormulaSentence.makeCycSentence(cyc, "(" + isa + " #$commitTestConstant2 " + predicate + ")"), CommonConstants.BASE_KB);
            assert (cyc.getInspectorTool().isPredicate(commitTestConstant2));

            SimpleKbTransaction instance = new SimpleKbTransaction();
            CycClient.setCurrentTransaction(instance);

            cyc.getUnassertTool().unassertGaf(CycFormulaSentence.makeCycSentence(cyc, "(" + isa + " #$commitTestConstant2 " + predicate + ")"), CommonConstants.BASE_KB);

            cyc.getAssertTool().assertGaf(CycFormulaSentence.makeCycSentence(cyc, "(" + isa + " #$commitTestConstant2 " + binaryPredicate + ")"), CommonConstants.BASE_KB);

            commitTestConstant = cyc.getAssertTool().findOrCreateNewPermanent("commitTestConstant");
            cyc.getAssertTool().assertGaf(CycFormulaSentence.makeCycSentence(cyc, "(" + isa + " #$commitTestConstant " + predicate + ")"), CommonConstants.BASE_KB);
            cyc.getAssertTool().assertGaf(CycFormulaSentence.makeCycSentence(cyc, "(" + isa + " #$commitTestConstant " + isa + ")"), CommonConstants.BASE_KB);
            assertFalse("commitTestConstant is known to be a predicate before the transaction has been committed.", cyc.getInspectorTool().isPredicate(commitTestConstant));
            Exception ex = null;
            try {
                instance.commit();
            } catch (Exception e) {
                ex = e;
            }
            assertTrue("Commit with nonsense assertion didn't trigger an exception", ex instanceof RuntimeException);


            assertTrue("After failed commit, there's still an active KBTransaction", CycClient.getCurrentTransaction() == null);

            instance.rollback();
            assertTrue("commitTestConstant was not deleted after rollback.", cyc.getLookupTool().getConstantByName("commitTestConstant") == null);
            assertFalse("commitTestConstant2 is known to be a binary predicate after the transaction rollback.", cyc.getInspectorTool().isBinaryPredicate(commitTestConstant2));
            assertTrue("commitTestConstant2 is not known to be a predicate after the transaction rollback.", cyc.getInspectorTool().isPredicate(commitTestConstant2));


        } finally {
            cleanupTestConstants();
        }
    }

    /**
     * Test of rollback method, of class SimpleKBTransaction.
     */
    @Test
    public void testRollback() throws Exception {
        System.out.println("rollback");

        System.out.println("commit");
        CycConstant commitTestConstant = null;
        CycConstant commitTestConstant2 = null;
        assertTrue("commitTestConstant already exists.", cyc.getLookupTool().getConstantByName("commitTestConstant") == null);
        assertTrue("commitTestConstant2 already exists.", cyc.getLookupTool().getConstantByName("commitTestConstant2") == null);
        try {
            commitTestConstant2 = cyc.getAssertTool().findOrCreateNewPermanent("commitTestConstant2");
            cyc.getAssertTool().assertGaf(CycFormulaSentence.makeCycSentence(cyc, "(" + isa + " #$commitTestConstant2 " + predicate + ")"), CommonConstants.BASE_KB);
            assert (cyc.getInspectorTool().isPredicate(commitTestConstant2));

            SimpleKbTransaction instance = new SimpleKbTransaction();
            CycClient.setCurrentTransaction(instance);
            assertTrue("Before commit, there's no active KBTransaction", CycClient.getCurrentTransaction() != null);
            cyc.getUnassertTool().unassertGaf(CycFormulaSentence.makeCycSentence(cyc, "(" + isa + " #$commitTestConstant2 " + predicate + ")"), CommonConstants.BASE_KB);
            assertTrue("Before commit, commitTestConstant2 is not known to be a predicate.", cyc.getInspectorTool().isPredicate(commitTestConstant2));
            cyc.getAssertTool().assertGaf(CycFormulaSentence.makeCycSentence(cyc, "(" + isa + " #$commitTestConstant2 " + binaryPredicate + ")"), CommonConstants.BASE_KB);



            commitTestConstant = cyc.getAssertTool().findOrCreateNewPermanent("commitTestConstant");
            assertTrue("Unable to find commitTestConstant before commit.", cyc.getLookupTool().getConstantByName("commitTestConstant") != null);

            cyc.getAssertTool().assertGaf(CycFormulaSentence.makeCycSentence(cyc, "(" + isa + " #$commitTestConstant " + predicate + ")"), CommonConstants.BASE_KB);
            assertFalse("commitTestConstant is known to be a predicate before the transaction has been committed.", cyc.getInspectorTool().isPredicate(commitTestConstant));

            instance.rollback();

            assertFalse("Able to find commitTestConstant after rollback.", cyc.getLookupTool().getConstantByName("commitTestConstant") != null);
            assertTrue("After rollback, commitTestConstant2 is not known to be a predicate.", cyc.getInspectorTool().isPredicate(commitTestConstant2));
            assertFalse("After rollback, commitTestConstant2 is known to be a binary predicate.", cyc.getInspectorTool().isBinaryPredicate(commitTestConstant2));

        } finally {
            cleanupTestConstants();
            CycClient.setCurrentTransaction(null);

        }
    }

    /**
     * Test of noteForAssertion method, of class SimpleKBTransaction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNoteForAssertion() {
        System.out.println("noteForAssertion");
        String sentence = "(" + isa + " " + collection + " " + thing + ")";
        ElMt mt = null;
        boolean bookkeeping = false;
        boolean transcripting = false;
        boolean wffDisabled = false;
        List<Fort> templates = new ArrayList<Fort>();
        SimpleKbTransaction instance = new SimpleKbTransaction();
        instance.noteForAssertion(sentence, mt, bookkeeping, transcripting, wffDisabled, templates);
        bookkeeping = true;
        instance.noteForAssertion(sentence, mt, bookkeeping, transcripting, wffDisabled, templates);
        assertTrue(instance.getAssertInfo().size() == 2);
        // TODO review the generated test code and remove the default call to fail.
        assertTrue(true);
    }

    /**
     * Test of noteForUnassertion method, of class SimpleKBTransaction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNoteForAssertion2() {
        System.out.println("noteForAssertion2");
        String sentence = "(" + isa + " " + collection + " " + thing + ")";
        ElMt mt = null;
        boolean bookkeeping = false;
        boolean transcripting = false;
        boolean wffDisabled = false;
        List<Fort> templates = new ArrayList<Fort>();
        SimpleKbTransaction instance = new SimpleKbTransaction();
        instance.noteForAssertion(sentence, mt, bookkeeping, transcripting, wffDisabled, templates);
        transcripting = true;
        instance.noteForAssertion(sentence, mt, bookkeeping, transcripting, wffDisabled, templates);
        // TODO review the generated test code and remove the default call to fail.
        assertTrue(true);
    }

    /**
     * Test of noteForUnassertion method, of class SimpleKBTransaction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNoteForAssertion2a() {
        System.out.println("noteForAssertion2a");
        String sentence = "(" + isa + " " + collection + " " + thing + ")";
        ElMt mt = null;
        boolean bookkeeping = false;
        boolean transcripting = false;
        boolean wffDisabled = false;
        List<Fort> templates = new ArrayList<Fort>();
        SimpleKbTransaction instance = new SimpleKbTransaction();
        instance.noteForAssertion(sentence, mt, bookkeeping, transcripting, wffDisabled, templates);
        wffDisabled = true;
        instance.noteForAssertion(sentence, mt, bookkeeping, transcripting, wffDisabled, templates);
        // TODO review the generated test code and remove the default call to fail.
        assertTrue(true);
    }

    /**
     * Test of noteForUnassertion method, of class SimpleKBTransaction. Ensure
 that an exception is thrown if the forward-inference templates are
 changed in mid-transaction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNoteForAssertion2b() throws CycConnectionException {
        System.out.println("noteForAssertion2b");
        String sentence = "(" + isa + " " + collection + " " + thing + ")";
        ElMt mt = null;
        boolean bookkeeping = false;
        boolean transcripting = false;
        boolean wffDisabled = false;
        List<Fort> templates = new ArrayList<Fort>();
        SimpleKbTransaction instance = new SimpleKbTransaction();
        instance.noteForAssertion(sentence, mt, bookkeeping, transcripting, wffDisabled, templates);
        templates.add((Fort) cyc.getObjectTool().makeELMt("" + thing + ""));
        instance.noteForAssertion(sentence, mt, bookkeeping, transcripting, wffDisabled, templates);
        // TODO review the generated test code and remove the default call to fail.
        assertTrue(true);
    }

    /**
     * Test of noteForUnassertion method, of class SimpleKBTransaction. Make
     * sure we can change the Mt without complication.
     */
    @Test
    public void testNoteForAssertion3() {
        try {
            System.out.println("noteForAssertion3");
            String sentence = "(" + isa + " " + collection + " " + thing + ")";
            ElMt mt = cyc.getObjectTool().makeELMt(universalVocabularyMt);
            boolean bookkeeping = false;
            boolean transcripting = false;
            boolean wffDisabled = false;
            List<Fort> templates = new ArrayList<Fort>();
            SimpleKbTransaction instance = new SimpleKbTransaction();
            instance.noteForAssertion(sentence, mt, bookkeeping, transcripting, wffDisabled, templates);
            mt = cyc.getObjectTool().makeELMt(baseKB);
            instance.noteForAssertion(sentence, mt, bookkeeping, transcripting, wffDisabled, templates);
            assertTrue(true);
        } catch (CycConnectionException ex) {
            Logger.getLogger(SimpleKbTransactionTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Threw unexpected IOException");
        } catch (CycApiException ex) {
            Logger.getLogger(SimpleKbTransactionTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Threw unexpected CycApiException");
        }
    }

    /**
     * Test of noteForUnassertion method, of class SimpleKBTransaction. make
 sure we can actually unassert lots of sentences, as long as we don't
 change anything that's supposed to be constant.
     */
    @Test
    public void testNoteForAssertion4() {
        try {
            System.out.println("noteForAssertion4");
            String sentence = "(" + isa + " " + collection + " " + thing + ")";
            ElMt mt = null;
            boolean bookkeeping = false;
            boolean transcripting = false;
            boolean wffDisabled = false;
            List<Fort> templates = new ArrayList<Fort>();
            SimpleKbTransaction instance = new SimpleKbTransaction();
            mt = cyc.getObjectTool().makeELMt(baseKB);
            instance.noteForAssertion(sentence, mt, bookkeeping, transcripting, wffDisabled, templates);
            instance.noteForAssertion(sentence, mt, bookkeeping, transcripting, wffDisabled, templates);
            sentence = "(" + isa + " " + thing + " " + collection + ")";
            instance.noteForAssertion(sentence, mt, bookkeeping, transcripting, wffDisabled, templates);
            assertTrue(true);
        } catch (CycConnectionException ex) {
            Logger.getLogger(SimpleKbTransactionTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Threw unexpected IOException" + ex.getMessage());
        } catch (CycApiException ex) {
            Logger.getLogger(SimpleKbTransactionTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Threw unexpected CycApiException" + ex.getMessage());
        }
    }

    /**
     * Test of noteForUnassertion method, of class SimpleKBTransaction. Make
     * sure that an exception is thrown if you try to assert a sentence after
     * unasserting the very same sentence.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testNoteForAssertion5() {
        try {
            System.out.println("noteForAssertion4");
            String sentence = "(" + isa + " " + collection + " " + thing + ")";
            ElMt mt = null;
            boolean bookkeeping = false;
            boolean transcripting = false;
            boolean wffDisabled = false;
            List<Fort> templates = new ArrayList<Fort>();
            SimpleKbTransaction instance = new SimpleKbTransaction();
            mt = cyc.getObjectTool().makeELMt(baseKB);
            instance.noteForUnassertion(sentence, mt, bookkeeping, transcripting);
            instance.noteForAssertion(sentence, mt, bookkeeping, transcripting, wffDisabled, templates);
        } catch (CycConnectionException ex) {
            Logger.getLogger(SimpleKbTransactionTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Threw unexpected IOException" + ex.getMessage());
        } catch (CycApiException ex) {
            Logger.getLogger(SimpleKbTransactionTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Threw unexpected CycApiException" + ex.getMessage());
        }
    }

    //@todo add tests to make sure the transaction info has the right stuff (i.e. right number of asserts/unasserts, correct creates, etc.)
    /**
     * Test of noteForUnassertion method, of class SimpleKBTransaction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNoteForUnassertion() {
        System.out.println("noteForUnassertion");
        String sentence = "(" + isa + " " + collection + " " + thing + ")";
        ElMt mt = null;
        boolean bookkeeping = false;
        boolean transcripting = false;
        SimpleKbTransaction instance = new SimpleKbTransaction();
        instance.noteForUnassertion(sentence, mt, bookkeeping, transcripting);
        bookkeeping = true;
        instance.noteForUnassertion(sentence, mt, bookkeeping, transcripting);
    }

    /**
     * Test of noteForUnassertion method, of class SimpleKBTransaction.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNoteForUnassertion2() {
        System.out.println("noteForUnassertion2");
        String sentence = "(" + isa + " " + collection + " " + thing + ")";
        ElMt mt = null;
        boolean bookkeeping = false;
        boolean transcripting = false;
        SimpleKbTransaction instance = new SimpleKbTransaction();
        instance.noteForUnassertion(sentence, mt, bookkeeping, transcripting);
        transcripting = true;
        instance.noteForUnassertion(sentence, mt, bookkeeping, transcripting);
        // TODO review the generated test code and remove the default call to fail.
        assertTrue(true);
    }

    /**
     * Test of noteForUnassertion method, of class SimpleKBTransaction. Make
     * sure we can change the Mt without complication.
     */
    @Test
    public void testNoteForUnassertion3() {
        try {
            System.out.println("noteForUnassertion3");
            String sentence = "(" + isa + " " + collection + " " + thing + ")";
            ElMt mt = cyc.getObjectTool().makeELMt("" + universalVocabularyMt + "");
            boolean bookkeeping = false;
            boolean transcripting = false;
            SimpleKbTransaction instance = new SimpleKbTransaction();
            instance.noteForUnassertion(sentence, mt, bookkeeping, transcripting);
            mt = cyc.getObjectTool().makeELMt(baseKB);
            instance.noteForUnassertion(sentence, mt, bookkeeping, transcripting);
            // TODO review the generated test code and remove the default call to fail.
            assertTrue(true);
        } catch (CycConnectionException ex) {
            Logger.getLogger(SimpleKbTransactionTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Threw unexpected IOException");
        } catch (CycApiException ex) {
            Logger.getLogger(SimpleKbTransactionTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Threw unexpected CycApiException");
        }
    }

    /**
     * Test of noteForUnassertion method, of class SimpleKBTransaction. make
 sure we can actually unassert lots of sentences, as long as we don't
 change anything that's supposed to be constant.
     */
    @Test
    public void testNoteForUnassertion4() {
        try {
            System.out.println("noteForUnassertion4");
            String sentence = "(" + isa + " " + collection + " " + thing + ")";
            ElMt mt = null;
            boolean bookkeeping = false;
            boolean transcripting = false;
            SimpleKbTransaction instance = new SimpleKbTransaction();
            mt = cyc.getObjectTool().makeELMt(baseKB);
            instance.noteForUnassertion(sentence, mt, bookkeeping, transcripting);
            instance.noteForUnassertion(sentence, mt, bookkeeping, transcripting);
            sentence = "(" + isa + " " + thing + " " + collection + ")";
            instance.noteForUnassertion(sentence, mt, bookkeeping, transcripting);
            assertTrue(true);
        } catch (CycConnectionException ex) {
            Logger.getLogger(SimpleKbTransactionTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Threw unexpected IOException" + ex.getMessage());
        } catch (CycApiException ex) {
            Logger.getLogger(SimpleKbTransactionTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Threw unexpected CycApiException" + ex.getMessage());
        }
    }

    /**
     * Test of noteCreation method, of class SimpleKBTransaction.
     */
    @Test
    public void testNoteCreation() {
        System.out.println("noteCreation");
        Fort fort = null;
        SimpleKbTransaction instance = new SimpleKbTransaction();
        instance.noteCreation(fort);
        instance.noteCreation(fort);
        instance.noteCreation(fort);
        assertEquals(instance.getCreatedTerms().size(), 3);
    }

    /**
     * Test that actually creating constants results in the correct number of
 constants being added to the transaction.
     */
    @Test
    public void testNoteCreation2() throws CycConnectionException {
        System.out.println("noteCreation");
        Fort fort = null;
        SimpleKbTransaction instance = new SimpleKbTransaction();
        instance.begin();
        cyc.getObjectTool().makeCycConstant("BaseKB");
        assertEquals(instance.getCreatedTerms().size(), 0);
        cyc.getObjectTool().makeCycConstant("BaseKB-" + System.currentTimeMillis());
        assertEquals(instance.getCreatedTerms().size(), 1);
        cyc.getObjectTool().makeCycConstant("BaseKB-" + System.currentTimeMillis());
        assertEquals(instance.getCreatedTerms().size(), 2);
    }
  
  private void cleanupTestConstants() throws CycConnectionException {
    if (cyc.getLookupTool().getConstantByName("commitTestConstant") != null) {
      cyc.getUnassertTool().kill(cyc.getLookupTool().getConstantByName("commitTestConstant"));
    }
    if (cyc.getLookupTool().getConstantByName("commitTestConstant2") != null) {
      cyc.getUnassertTool().kill(cyc.getLookupTool().getConstantByName("commitTestConstant2"));
    }
  }
}
