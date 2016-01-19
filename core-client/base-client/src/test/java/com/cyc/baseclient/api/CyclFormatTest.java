package com.cyc.baseclient.api;

/*
 * #%L
 * File: CyclFormatTest.java
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

import com.cyc.baseclient.connection.CyclFormat;
import com.cyc.baseclient.testing.TestUtils;
import com.cyc.base.CycAccess;
import com.cyc.base.exception.CycConnectionException;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import org.junit.*;
import static org.junit.Assert.*;
import com.cyc.baseclient.cycobject.CycArrayList;
import com.cyc.baseclient.testing.TestSentences;

// FIXME: TestSentences - nwinant

/**
 *
 * @author daves
 */
public class CyclFormatTest {
    private CyclFormat instance;
    private String newLine;
    
    public CyclFormatTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
      TestUtils.ensureTestEnvironmentInitialized();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() throws CycConnectionException {
        CycAccess cyc = TestUtils.getCyc();
        instance = CyclFormat.getInstance(cyc);
        // (princ-to-string #\\Newline) does not work, because CycArrayList parser cannot handle #\ ... :P
        newLine = cyc.converse().converseString("(clet (result) (cwith-output-to-string (s result) (terpri s)) (identity result))");
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getInstance method, of class CycLFormat.
     */
    //@Test
    public void testGetInstance() {
        System.out.println("getInstance");
        CycAccess cyc = null;
        CyclFormat expResult = null;
        CyclFormat result = CyclFormat.getInstance(cyc);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setWrapLines method, of class CycLFormat.
     */
    //@Test
    public void testSetWrapLines() {
        System.out.println("setWrapLines");
        boolean newlines = false;
        CyclFormat instance = null;
        instance.setWrapLines(newlines);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setShowHashDollar method, of class CycLFormat.
     */
    //@Test
    public void testSetShowHashDollar() {
        System.out.println("setShowHashDollar");
        boolean showHashDollar = false;
        CyclFormat instance = null;
        instance.setShowHashDollar(showHashDollar);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }



    /**
     * Test of parseObject method, of class CycLFormat.
     */
    @Test
    public void testParseObject() {
        System.out.println("testParseObject");
        String source = TestSentences.CATS_LIKE_CYC_ADMIN_STRING;
        ParsePosition pos = new ParsePosition(0);
        Object expResult = null;
        Object result = instance.parseObject(source, pos);
        assertTrue(result instanceof CycArrayList);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
        /**
     * Test of format method, of class CycLFormat.
     */
    @Test
    public void testFormat() throws ParseException {
        System.out.println("format");
        Object obj = instance.parseObject(TestSentences.CATS_LIKE_CYC_ADMIN_STRING, new ParsePosition(0));
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = null;
        String expResult = TestSentences.CATS_LIKE_CYC_ADMIN_STRING;
        instance.setShowHashDollar(true);
        instance.setWrapLines(false);
        StringBuffer result = instance.format(obj, toAppendTo, pos);
        assertEquals(expResult, result.toString());
        // TODO review the generated test code and remove the default call to fail.
        
    }
    
    @Test
    public void testFormatNoHashDollar() throws ParseException {
        System.out.println("formatNoHashDollar");
        Object obj = instance.parseObject(TestSentences.CATS_LIKE_CYC_ADMIN_STRING, new ParsePosition(0));
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = null;
        String expResult = "(implies (isa ?X Cat) (likesAsFriend ?X CycAdministrator))";
        instance.setShowHashDollar(false);
        instance.setWrapLines(false);
        StringBuffer result = instance.format(obj, toAppendTo, pos);
        assertEquals(expResult, result.toString());
        // TODO review the generated test code and remove the default call to fail.
        
    }
    
    @Test
    public void testFormatWrap() throws ParseException {
        System.out.println("formatWrap");
        Object obj = instance.parseObject(TestSentences.CATS_LIKE_CYC_ADMIN_STRING, new ParsePosition(0));
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = null;
        String expResult = "(#$implies " + newLine +"  (#$isa ?X #$Cat) " + newLine + "  (#$likesAsFriend ?X #$CycAdministrator))";
        instance.setShowHashDollar(true);
        instance.setWrapLines(true);
        StringBuffer result = instance.format(obj, toAppendTo, pos);
        assertEquals(expResult, result.toString());
        // TODO review the generated test code and remove the default call to fail.
    }
    
    @Test
    public void testFormatWrapNoHashDollar() throws ParseException {
        System.out.println("formatWrapNoHashDollar");
        Object obj = instance.parseObject(TestSentences.CATS_LIKE_CYC_ADMIN_STRING, new ParsePosition(0));
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = null;
        String expResult = "(implies " + newLine + "  (isa ?X Cat) " + newLine + "  (likesAsFriend ?X CycAdministrator))";
        instance.setShowHashDollar(false);
        instance.setWrapLines(true);
        StringBuffer result = instance.format(obj, toAppendTo, pos);
        assertEquals(expResult, result.toString());
        // TODO review the generated test code and remove the default call to fail.
        
    }
}
