package com.cyc.kb.client;

/*
 * #%L
 * File: TestApp.java
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


import static org.junit.Assert.*;

import com.cyc.kb.Context;
import com.cyc.kb.KbCollection;
import com.cyc.kb.KbIndividual;
import com.cyc.kb.client.ContextImpl;
import com.cyc.kb.client.KbCollectionImpl;
import com.cyc.kb.client.KbIndividualImpl;
import com.cyc.kb.exception.KbException;

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

    KbCollection d = KbCollectionImpl.get("Dog");
    KbCollection f = KbCollectionImpl.get("(FruitFn AppleTree)");
    Context m = ContextImpl.get("BiologyMt");

    
    KbCollectionImpl w = KbCollectionImpl.get("Flying-Move");
    KbIndividual expected = KbIndividualImpl.get("FlightXYZ-APITest");
    assertTrue(w.<KbIndividual>getValues("isa", 2, 1, "SomeAirlineLogMt").contains(expected));
    assertTrue(w.<KbIndividual>getInstances("SomeAirlineLogMt").contains(expected));

    // LOG.info("values of d: " + d.getValues("ownerOfType"));
    // LOG.info("values of m: " +
    // m.getValues("genlMt").get(2).getValues("genlMt"));


  }

  @Test
  public void testHelloWorlds() throws Exception {
      KbCollectionImpl p = KbCollectionImpl.get("Planet");

      Collection<KbIndividual> ps1 = p.<KbIndividual>getInstances("SimpleAstronomyDataVocabularyMt");
      System.out.println("Instance of planets are: " + ps1.toString());

      Collection<KbIndividual> ps2 = p.<KbIndividual>getValues(
              "isa", 2, 1, "SimpleAstronomyDataVocabularyMt");
      System.out.println("Instance of planets are: " + ps2.toString());

      KbIndividualImpl aPlanet = (KbIndividualImpl)p.<KbIndividual>getValues("isa", 2, 1, 
              "SimpleAstronomyDataVocabularyMt").toArray()[0]; 
      Collection<KbCollection> d = aPlanet.<KbCollection>getValues("isa",
              1, 2, "SimpleAstronomyDataVocabularyMt");
      System.out.println("Instance of planets are: " + d.toString());

      Collection<String> str = p.<String>getValues("comment",
              1, 2, "UniversalVocabularyMt");
      System.out.println("Comment of planets are: " + str.toArray());
      System.out.println("Comment of planets are (2): " + p.getComments(
              "UniversalVocabularyMt"));

      KbIndividualImpl pluto = KbIndividualImpl.get("Mx4rvVjS-pwpEbGdrcN5Y29ycA");
      Collection<Double> flt = pluto.<Double>getValues("orbitalEccentricity", 1, 2, 
              "UniverseDataMt");
      System.out.println("Orbital eccentricity of planets are: " + flt);

      
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd HH:mm");
      Date date = sdf.parse("2014 03 15 10:20");
      
      KbIndividualImpl i = KbIndividualImpl.findOrCreate("FlightXYZ-APITest");
      Collection<Date> dates = i.<Date>getValues("endingDate", 1,
              2, "SomeAirlineLogMt");
      assertTrue(dates.contains(date));
  }
}
