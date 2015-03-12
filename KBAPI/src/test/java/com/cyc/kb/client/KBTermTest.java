package com.cyc.kb.client;

/*
 * #%L
 * File: KBTermTest.java
 * Project: KB API Implementation
 * %%
 * Copyright (C) 2013 - 2015 Cycorp, Inc
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

import com.cyc.base.cycobject.Guid;
import com.cyc.baseclient.cycobject.CycConstantImpl;
import com.cyc.kb.KBCollection;
import com.cyc.kb.KBIndividual;
import com.cyc.kb.KBStatus;
import com.cyc.kb.KBTerm;
import com.cyc.kb.client.ContextImpl;
import com.cyc.kb.client.KBCollectionImpl;
import com.cyc.kb.client.KBTermImpl;
import com.cyc.kb.config.KBAPIConfiguration;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.InvalidNameException;
import com.cyc.kb.exception.KBApiException;
import com.cyc.kb.exception.KBTypeException;

import java.util.logging.Logger;

import org.junit.AfterClass;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class KBTermTest {

    private static Logger log = null;

    @BeforeClass
    public static void setUp() throws Exception {
        log = Logger.getLogger(KBTermTest.class.getName());
        TestConstants.ensureInitialized();
    }

    @AfterClass
    public static void tearDown() throws Exception {
    }

    
    @Test
    public void testIsInstanceOf() throws KBApiException {      
            KBTermImpl hc = KBTermImpl.get("HumanCyclist");
            assertTrue(hc.isInstanceOf("Collection", "UniversalVocabularyMt"));
            KBCollectionImpl thing = KBCollectionImpl.get("Thing");
            assertTrue(hc.isInstanceOf(thing, ContextImpl.get("UniversalVocabularyMt")));
            assertTrue(thing.isInstanceOf(thing, ContextImpl.get("UniversalVocabularyMt")));
            assertTrue(thing.isInstanceOf(KBCollectionImpl.get("Collection"), ContextImpl.get("UniversalVocabularyMt")));
    }
    
    
    /**
     * KBTermFactory get tests
     */
    @Test
    public void testKBTermFactoryGet() throws KBApiException {
        System.out.println("collectionFactoryGet");
        KBTerm c1 = new KBTermImpl("Emu");
        KBTermImpl c2 = new KBTermImpl("Emu");
        assertFalse("Two different Emu are the same object!", c1 == c2);
        assertTrue("Two different Emu are not equals()!", c2.equals(c1));


        c1 = KBCollectionImpl.get("Emu");
        c2 = KBCollectionImpl.get(new CycConstantImpl("Emu", new Guid("c01a4ba0-9c29-11b1-9dad-c379636f7270")));
        assertTrue("CycObject-based BaseKB and string-based version differ!", c1 == c2);

        c2 = KBCollectionImpl.get("Mx4rwBpLoJwpEbGdrcN5Y29ycA");
        assertTrue("HLID-based BaseKB and string-based version differ!", c1 == c2);

    }

    @Test
    public void testNoGetTypeConflictException() throws KBApiException {
        //make sure that all of these different kinds of things can be KBTerms.
        KBTerm c3 = KBTermImpl.get("isa");
        KBTerm c4 = KBTermImpl.get("Dog");
        KBTerm c5 = KBTermImpl.get("BillClinton"); 
        assertTrue(true);
    }

    @Test
    public void testCollectionFactoryFindOrCreateWorksLikeGet() throws KBApiException {

        KBCollection c1 = KBCollectionImpl.findOrCreate("Emu");
        KBCollectionImpl c2 = KBCollectionImpl.findOrCreate("Emu");
        assertTrue("Two different BaseKBs are actually different objects!", c2 == c1);
        assertTrue("Two different BaseKBs are not equals()!", c2.equals(c1));

        c2 = KBCollectionImpl.findOrCreate(new CycConstantImpl("Emu", new Guid("c01a4ba0-9c29-11b1-9dad-c379636f7270")));
        assertTrue("CycObject-based BaseKB and string-based version differ!", c1 == c2);

        c2 = KBCollectionImpl.findOrCreate("Mx4rwBpLoJwpEbGdrcN5Y29ycA");
        assertTrue("HLID-based BaseKB and string-based version differ!", c1 == c2);

    }

    @Test
    public void testFactoryFindOrCreateOnlyCreatesOneObject() throws KBApiException {

        KBTermImpl c1 = KBTermImpl.findOrCreate("Emu234");
        KBTermImpl c2 = KBTermImpl.findOrCreate("Emu234");
        assertTrue("Two different Emu234s are actually different objects!", c2 == c1);
        assertTrue("Two different Emu234s are not equals()!", c2.equals(c1));

        c2 = KBTermImpl.findOrCreate(c1.getCore());
        assertTrue("CycObject-based Emu234 and string-based version differ!", c1 == c2);

        c2 = KBTermImpl.findOrCreate(c1.getId());
        assertTrue("HLID-based BaseKB234 and string-based version differ!", c1 == c2);
    }


    @Test(expected = InvalidNameException.class)
    public void testFindOrCreateInvalidNameException() throws KBApiException {
        KBTerm c3 = KBTermImpl.findOrCreate("Emu@#$SVA!@#R");
    }


    @Test
    public void testCollectionFactoryGetStatus() throws KBApiException {
        KBIndividual i1 = null;
        String testIndName = "TestIndividualForTypeConflict34";
        try {
            assertTrue("Emu isn't a KBTerm!", KBTermImpl.getStatus("Emu").equals(KBStatus.EXISTS_AS_TYPE));
            assertTrue("isa isn't in a KBTerm!", KBTermImpl.getStatus("isa").equals(KBStatus.EXISTS_AS_TYPE));
            assertTrue(testIndName + " shouldn't exist at all, but has status " + KBTermImpl.getStatus(testIndName) + ".",
                    KBTermImpl.getStatus(testIndName).equals(KBStatus.DOES_NOT_EXIST));
            System.out.println("Status = " + KBTermImpl.getStatus(testIndName));
        } finally {
            if (KBTermImpl.getStatus(testIndName) != KBStatus.DOES_NOT_EXIST) {
                KBTermImpl.get(testIndName).delete();
            }
        }
    }

    @Test
    public void testCollectionFactoryExistsAsType() throws KBApiException {
        KBTermImpl i1 = null;
        try {
            String testIndName = "TestIndividualForTypeConflict";

            i1 = KBTermImpl.findOrCreate(testIndName);
            assertFalse(testIndName + " is a collection!", KBCollectionImpl.existsAsType(testIndName));
            assertTrue(testIndName + " is not a KBTerm!", KBTermImpl.existsAsType(testIndName));

        } finally {
            if (i1 != null) {
                i1.delete();
            }
        }
    }
    
    @Test
    public void testGetCreationStory () throws KBTypeException, CreateException {
      KBAPIConfiguration.setShouldTranscriptOperations(true);
      KBAPIConfiguration.setCurrentCyclist("TheUser");
      KBIndividual term = KBIndividualImpl.findOrCreate("TemporaryTestTerm");
      
      KBIndividual creator = term.getCreator();
      assertEquals(KBIndividualImpl.get("TheUser"), creator);
      System.out.println("Creator: " + creator);
      
      
      System.out.println("Creator: " + term.getCreationDate());
      KBAPIConfiguration.setShouldTranscriptOperations(false);
    }
    
}
