package com.cyc.kb.client;

/*
 * #%L
 * File: TestApp.java
 * Project: KB API
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


import static org.junit.Assert.*;

import com.cyc.kb.Context;
import com.cyc.kb.KBCollection;
import com.cyc.kb.KBIndividual;
import com.cyc.kb.client.ContextImpl;
import com.cyc.kb.client.KBCollectionImpl;
import com.cyc.kb.client.KBIndividualImpl;
import com.cyc.kb.exception.KBApiException;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestApp {

  private static Logger log = null;

  @BeforeClass
  public static void setUp() throws Exception {
    log = Logger.getLogger(TestApp.class.getName());
    TestConstants.ensureInitialized();
  }

  @AfterClass
  public static void tearDown() throws Exception {
  }

  @Test
  public void testConnection() throws Exception {

    KBCollection d = KBCollectionImpl.get("Dog");
    KBCollection f = KBCollectionImpl.get("(FruitFn AppleTree)");
    Context m = ContextImpl.get("BiologyMt");

    
    KBCollectionImpl w = KBCollectionImpl.get("Flying-Move");
    KBIndividual expected = KBIndividualImpl.get("FlightXYZ-APITest");
    assertTrue(w.<KBIndividual>getValues("isa", 2, 1, "SomeAirlineLogMt").contains(expected));
    assertTrue(w.<KBIndividual>getInstances("SomeAirlineLogMt").contains(expected));

    // LOG.info("values of d: " + d.getValues("ownerOfType"));
    // LOG.info("values of m: " +
    // m.getValues("genlMt").get(2).getValues("genlMt"));


  }

  @Test
  public void testHelloWorlds() throws Exception {
      KBCollectionImpl p = KBCollectionImpl.get("Planet");

      Collection<KBIndividual> ps1 = p.<KBIndividual>getInstances("SimpleAstronomyDataVocabularyMt");
      System.out.println("Instance of planets are: " + ps1.toString());

      Collection<KBIndividual> ps2 = p.<KBIndividual>getValues(
              "isa", 2, 1, "SimpleAstronomyDataVocabularyMt");
      System.out.println("Instance of planets are: " + ps2.toString());

      KBIndividualImpl aPlanet = (KBIndividualImpl)p.<KBIndividual>getValues("isa", 2, 1, 
              "SimpleAstronomyDataVocabularyMt").toArray()[0]; 
      Collection<KBCollection> d = aPlanet.<KBCollection>getValues("isa",
              1, 2, "SimpleAstronomyDataVocabularyMt");
      System.out.println("Instance of planets are: " + d.toString());

      Collection<String> str = p.<String>getValues("comment",
              1, 2, "UniversalVocabularyMt");
      System.out.println("Comment of planets are: " + str.toArray());
      System.out.println("Comment of planets are (2): " + p.getComments(
              "UniversalVocabularyMt"));

      KBIndividualImpl pluto = KBIndividualImpl.get("Mx4rvVjS-pwpEbGdrcN5Y29ycA");
      Collection<Double> flt = pluto.<Double>getValues("orbitalEccentricity", 1, 2, 
              "UniverseDataMt");
      System.out.println("Orbital eccentricity of planets are: " + flt);

      
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd HH:mm");
      Date date = sdf.parse("2014 03 15 10:20");
      
      KBIndividualImpl i = KBIndividualImpl.findOrCreate("FlightXYZ-APITest");
      Collection<Date> dates = i.<Date>getValues("endingDate", 1,
              2, "SomeAirlineLogMt");
      assertTrue(dates.contains(date));
  }
}
