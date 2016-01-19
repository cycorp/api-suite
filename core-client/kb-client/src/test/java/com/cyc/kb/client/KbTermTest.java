package com.cyc.kb.client;

/*
 * #%L
 * File: KbTermTest.java
 * Project: KB Client
 * %%
 * Copyright (C) 2013 - 2016 Cycorp, Inc
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
import com.cyc.kb.KbCollection;
import com.cyc.kb.KbIndividual;
import com.cyc.kb.KbStatus;
import com.cyc.kb.KbTerm;
import com.cyc.kb.client.ContextImpl;
import com.cyc.kb.client.KbCollectionImpl;
import com.cyc.kb.client.KbTermImpl;
import com.cyc.kb.client.config.KbConfiguration;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.InvalidNameException;
import com.cyc.kb.exception.KbException;
import com.cyc.kb.exception.KbTypeException;
import com.cyc.session.exception.SessionCommunicationException;
import com.cyc.session.exception.SessionConfigurationException;

import java.util.logging.Logger;

import org.junit.AfterClass;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class KbTermTest {

    private static Logger log = null;

    @BeforeClass
    public static void setUp() throws Exception {
        log = Logger.getLogger(KbTermTest.class.getName());
        TestConstants.ensureInitialized();
    }

    @AfterClass
    public static void tearDown() throws Exception {
    }

    
    @Test
    public void testIsInstanceOf() throws KbException {      
            KbTermImpl hc = KbTermImpl.get("HumanCyclist");
            assertTrue(hc.isInstanceOf("Collection", "UniversalVocabularyMt"));
            KbCollectionImpl thing = KbCollectionImpl.get("Thing");
            assertTrue(hc.isInstanceOf(thing, ContextImpl.get("UniversalVocabularyMt")));
            assertTrue(thing.isInstanceOf(thing, ContextImpl.get("UniversalVocabularyMt")));
            assertTrue(thing.isInstanceOf(KbCollectionImpl.get("Collection"), ContextImpl.get("UniversalVocabularyMt")));
    }
    
    
    /**
     * KBTermFactory get tests
     */
    @Test
    public void testKBTermFactoryGet() throws KbException {
        System.out.println("collectionFactoryGet");
        KbTerm c1 = new KbTermImpl("Emu");
        KbTermImpl c2 = new KbTermImpl("Emu");
        assertFalse("Two different Emu are the same object!", c1 == c2);
        assertTrue("Two different Emu are not equals()!", c2.equals(c1));


        c1 = KbCollectionImpl.get("Emu");
        c2 = KbCollectionImpl.get(new CycConstantImpl("Emu", new Guid("c01a4ba0-9c29-11b1-9dad-c379636f7270")));
        assertTrue("CycObject-based BaseKB and string-based version differ!", c1 == c2);

        c2 = KbCollectionImpl.get("Mx4rwBpLoJwpEbGdrcN5Y29ycA");
        assertTrue("HLID-based BaseKB and string-based version differ!", c1 == c2);

    }

    @Test
    public void testNoGetTypeConflictException() throws KbException {
        //make sure that all of these different kinds of things can be KBTerms.
        KbTerm c3 = KbTermImpl.get("isa");
        KbTerm c4 = KbTermImpl.get("Dog");
        KbTerm c5 = KbTermImpl.get("BillClinton"); 
        assertTrue(true);
    }

    @Test
    public void testCollectionFactoryFindOrCreateWorksLikeGet() throws KbException {

        KbCollection c1 = KbCollectionImpl.findOrCreate("Emu");
        KbCollectionImpl c2 = KbCollectionImpl.findOrCreate("Emu");
        assertTrue("Two different BaseKBs are actually different objects!", c2 == c1);
        assertTrue("Two different BaseKBs are not equals()!", c2.equals(c1));

        c2 = KbCollectionImpl.findOrCreate(new CycConstantImpl("Emu", new Guid("c01a4ba0-9c29-11b1-9dad-c379636f7270")));
        assertTrue("CycObject-based BaseKB and string-based version differ!", c1 == c2);

        c2 = KbCollectionImpl.findOrCreate("Mx4rwBpLoJwpEbGdrcN5Y29ycA");
        assertTrue("HLID-based BaseKB and string-based version differ!", c1 == c2);

    }

    @Test
    public void testFactoryFindOrCreateOnlyCreatesOneObject() throws KbException {

        KbTermImpl c1 = KbTermImpl.findOrCreate("EmuTheTermThing");
        KbTermImpl c2 = KbTermImpl.findOrCreate("EmuTheTermThing");
        assertTrue("Two different EmuTheTermThings are actually different objects!", c2 == c1);
        assertTrue("Two different EmuTheTermThings are not equals()!", c2.equals(c1));

        c2 = KbTermImpl.findOrCreate(c1.getCore());
        assertTrue("CycObject-based EmuTheTermThing and string-based version differ!", c1 == c2);

        c2 = KbTermImpl.findOrCreate(c1.getId());
        assertTrue("HLID-based BaseKB234 and string-based version differ!", c1 == c2);
        
        c1.delete();
    }


    @Test(expected = InvalidNameException.class)
    public void testFindOrCreateInvalidNameException() throws KbException {
        KbTerm c3 = KbTermImpl.findOrCreate("Emu@#$SVA!@#R");
    }


    @Test
    public void testCollectionFactoryGetStatus() throws KbException {
        KbIndividual i1 = null;
        String testIndName = "TestIndividualForTypeConflict34";
        try {
            assertTrue("Emu isn't a KBTerm!", KbTermImpl.getStatus("Emu").equals(KbStatus.EXISTS_AS_TYPE));
            assertTrue("isa isn't in a KBTerm!", KbTermImpl.getStatus("isa").equals(KbStatus.EXISTS_AS_TYPE));
            assertTrue(testIndName + " shouldn't exist at all, but has status " + KbTermImpl.getStatus(testIndName) + ".",
                    KbTermImpl.getStatus(testIndName).equals(KbStatus.DOES_NOT_EXIST));
            System.out.println("Status = " + KbTermImpl.getStatus(testIndName));
        } finally {
            if (KbTermImpl.getStatus(testIndName) != KbStatus.DOES_NOT_EXIST) {
                KbTermImpl.get(testIndName).delete();
            }
        }
    }

    @Test
    public void testCollectionFactoryExistsAsType() throws KbException {
        KbTermImpl i1 = null;
        try {
            String testIndName = "TestIndividualForTypeConflict";

            i1 = KbTermImpl.findOrCreate(testIndName);
            assertFalse(testIndName + " is a collection!", KbCollectionImpl.existsAsType(testIndName));
            assertTrue(testIndName + " is not a KBTerm!", KbTermImpl.existsAsType(testIndName));

        } finally {
            if (i1 != null) {
                i1.delete();
            }
        }
    }
    
    @Test
    public void testGetCreationStory () throws KbTypeException, CreateException, SessionCommunicationException, SessionConfigurationException {
      KbConfiguration.getOptions().setShouldTranscriptOperations(true);
      KbConfiguration.getOptions().setCyclistName("TheUser");
      KbIndividual term = KbIndividualImpl.findOrCreate("TemporaryTestTerm");
      
      KbIndividual creator = term.getCreator();
      assertEquals(KbIndividualImpl.get("TheUser"), creator);
      System.out.println("Creator: " + creator);
      
      
      System.out.println("Creator: " + term.getCreationDate());
      KbConfiguration.getOptions().setShouldTranscriptOperations(false);
    }
    
}
