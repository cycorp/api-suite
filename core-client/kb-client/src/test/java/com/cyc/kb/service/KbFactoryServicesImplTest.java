/*
 * Copyright 2015 Cycorp, Inc.
 *
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
 */
package com.cyc.kb.service;

/*
 * #%L
 * File: KbFactoryServicesImplTest.java
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

import com.cyc.kb.spi.KbObjectService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author nwinant
 */
public class KbFactoryServicesImplTest {
  
  public KbFactoryServicesImplTest() {
  }
  
  @BeforeClass
  public static void setUpClass() {
  }
  
  @AfterClass
  public static void tearDownClass() {
  }
  
  @Before
  public void setUp() {
    instance = new KbFactoryServicesImpl();
  }
  
  @After
  public void tearDown() {
  }
  
  
  // Fields
  
  private KbFactoryServicesImpl instance = new KbFactoryServicesImpl();
  
  
  // Tests
  
  @Test
  public void testGetAssertionService() {
    System.out.println("getAssertionService");
    AssertionServiceImpl result = instance.getAssertionService();
    assertNotNull(result);
    assertNotNull(result.toString());
  }
  
  @Test
  public void testGetBinaryPredicateService() {
    System.out.println("getBinaryPredicateService");
    BinaryPredicateServiceImpl result = instance.getBinaryPredicateService();
    assertNotNull(result);
    assertNotNull(result.toString());
  }
  
  @Test
  public void testGetCollectionService() {
    System.out.println("getCollectionService");
    KbCollectionServiceImpl result = instance.getCollectionService();
    assertNotNull(result);
    assertNotNull(result.toString());
  }
  
  @Test
  public void testGetContextService() {
    System.out.println("getContextService");
    ContextServiceImpl result = instance.getContextService();
    assertNotNull(result);
    assertNotNull(result.toString());
  }
  
  @Test
  public void testGetConvenienceService() {
    System.out.println("getConvenienceService");
    ConvenienceServiceImpl result = instance.getConvenienceService();
    assertNotNull(result);
    assertNotNull(result.toString());
  }
  
  @Test
  public void testGetFactService() {
    System.out.println("getFactService");
    FactServiceImpl result = instance.getFactService();
    assertNotNull(result);
    assertNotNull(result.toString());
  }
  
  @Test
  public void testGetFirstOrderCollectionService() {
    System.out.println("getFirstOrderCollectionService");
    FirstOrderCollectionServiceImpl result = instance.getFirstOrderCollectionService();
    assertNotNull(result);
    assertNotNull(result.toString());
  }
  
  @Test
  public void testGetFunctionService() {
    System.out.println("getFunctionService");
    KbFunctionServiceImpl result = instance.getFunctionService();
    assertNotNull(result);
    assertNotNull(result.toString());
  }
  
  @Test
  public void testGetIndividualService() {
    System.out.println("getIndividualService");
    KbIndividualServiceImpl result = instance.getIndividualService();
    assertNotNull(result);
    assertNotNull(result.toString());
  }

  @Test
  public void testGetKbObjectService() {
    System.out.println("getKbObjectService");
    KbObjectServiceImpl result = instance.getKbObjectService();
    assertNotNull(result);
    assertNotNull(result.toString());
  }
  
  @Test
  public void testGetPredicateService() {
    System.out.println("getPredicateService");
    KbPredicateServiceImpl result = instance.getPredicateService();
    assertNotNull(result);
    assertNotNull(result.toString());
  }
  
  @Test
  public void testGetRelationService() {
    System.out.println("getRelationService");
    RelationServiceImpl result = instance.getRelationService();
    assertNotNull(result);
    assertNotNull(result.toString());
  }
  
  @Test
  public void testGetRuleService() {
    System.out.println("getRuleService");
    RuleServiceImpl result = instance.getRuleService();
    assertNotNull(result);
    assertNotNull(result.toString());
  }

  @Test
  public void testGetSecondOrderCollectionService() {
    System.out.println("getSecondOrderCollectionService");
    SecondOrderCollectionServiceImpl result = instance.getSecondOrderCollectionService();
    assertNotNull(result);
    assertNotNull(result.toString());
  }
  
  @Test
  public void testGetSentenceService() {
    System.out.println("getSentenceService");
    SentenceServiceImpl result = instance.getSentenceService();
    assertNotNull(result);
    assertNotNull(result.toString());
  }
  
  @Test
  public void testGetSymbolService() {
    System.out.println("getSymbolService");
    SymbolServiceImpl result = instance.getSymbolService();
    assertNotNull(result);
    assertNotNull(result.toString());
  }
  
  @Test
  public void testGetTermService() {
    System.out.println("getTermService");
    KbTermServiceImpl result = instance.getTermService();
    assertNotNull(result);
    assertNotNull(result.toString());
  }
  
  @Test
  public void testGetVariableService() {
    System.out.println("getVariableService");
    VariableServiceImpl result = instance.getVariableService();
    assertNotNull(result);
    assertNotNull(result.toString());
  }
  
  @Test
  public void testServiceEquality() {
    System.out.println("testServiceEquality");
    final List<KbObjectService> services = Arrays.asList(
            instance.getAssertionService(),
            instance.getBinaryPredicateService(),
            instance.getCollectionService(),
            instance.getContextService(),
            instance.getConvenienceService(),
            instance.getFactService(),
            instance.getFirstOrderCollectionService(),
            instance.getFunctionService(),
            instance.getIndividualService(),
            instance.getKbObjectService(),
            instance.getPredicateService(),
            instance.getRelationService(),
            instance.getRuleService(),
            instance.getSecondOrderCollectionService(),
            instance.getSentenceService(),
            instance.getSymbolService(),
            instance.getTermService(),
            instance.getVariableService());
    final int total = services.size();
    for (KbObjectService service : services) {
      System.out.println("Service: " + service);
      final List<KbObjectService> otherServices = new ArrayList(services);
      otherServices.remove(service);
      assertEquals(total, otherServices.size() + 1);
      for (KbObjectService otherService : otherServices) {
        assertNotEquals(otherService, service);
      }
    }
  }
  
}
