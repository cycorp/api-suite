package com.cyc.kb.client;

/*
 * #%L
 * File: BinaryPredicateTest.java
 * Project: KB API
 * %%
 * Copyright (C) 2013 - 2014 Cycorp, Inc
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
import com.cyc.base.cycobject.CycConstant;
import com.cyc.kb.BinaryPredicate;
import com.cyc.kb.exception.CreateException;
import com.cyc.kb.exception.DeleteException;
import com.cyc.kb.exception.KBApiRuntimeException;
import com.cyc.kb.exception.KBTypeException;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;


import org.junit.*;

import static org.junit.Assert.*;

public class BinaryPredicateTest {

	private static KBIndividualImpl i;

	@BeforeClass
	public static void setUp() throws Exception {
            
      org.apache.log4j.Logger rootLogger = org.apache.log4j.Logger.getLogger("com.cyc");
      rootLogger.setLevel(Level.TRACE);
      PatternLayout layout = new PatternLayout("%d{HH:mm:ss} %-5p %c{1}:%L - %m%n");
      rootLogger.removeAllAppenders();
      rootLogger.addAppender(new FileAppender(layout, "/tmp/kbapi.log", true));

      TestConstants.ensureInitialized();
      i = KBIndividualImpl.findOrCreate("TestIndividual001");
      i.instantiates("Person", "UniversalVocabularyMt");
	}

	@AfterClass
	public static void tearDown() throws Exception {
	}

	@Test
	public void testBinaryPredicateString() throws KBTypeException, CreateException, DeleteException {
		try {
          KBPredicateImpl.get("isa");
			BinaryPredicate bp = Constants.getInstance().COMMENT_PRED;
			i.addArg2(bp, "TestIndividual001 is a person", Constants.uvMt());
			i.delete();
			java.util.Collection<String> comments = i.getComments();
			assertEquals(comments.size(), 0);
		} catch (KBApiRuntimeException ex) {
			assertEquals(ex.getMessage(),
					"The reference to TestIndividual001 object is stale. Possibly because it was delete using x.delete() method.");
		} 
        

	}
	
	@Test
	public void testStaticMethods() throws Exception {
		System.out.println("existsAsType; getStatus");
                CycAccess cyc = CycAccessManager.getCurrentAccess();
		assertTrue(BinaryPredicateImpl.existsAsType("flyingDoneBySomeone-Travel"));
		
		CycConstant cc1 = cyc.getLookupTool().getKnownConstantByName("flyingDoneBySomeone-Travel");
		assertTrue(BinaryPredicateImpl.existsAsType(cc1));
		
		assertFalse(BinaryPredicateImpl.existsAsType("FlyingAnObject-Operate"));
		
		CycConstant cc2 = cyc.getLookupTool().getKnownConstantByName("FlyingAnObject-Operate");
		assertFalse(BinaryPredicateImpl.existsAsType(cc2));
		
        KBPredicateImpl.get("isa");
	}
	

}
