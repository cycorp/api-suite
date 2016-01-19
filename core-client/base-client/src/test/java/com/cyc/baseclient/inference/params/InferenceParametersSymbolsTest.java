/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cyc.baseclient.inference.params;

/*
 * #%L
 * File: InferenceParametersSymbolsTest.java
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

import com.cyc.query.InferenceParameter;
import com.cyc.base.CycAccess;
import com.cyc.base.exception.CycConnectionException;
import com.cyc.base.cycobject.CycSymbol;
import com.cyc.baseclient.testing.TestUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author baxter
 */
public class InferenceParametersSymbolsTest {

  public InferenceParametersSymbolsTest() {
  }

  @BeforeClass
  public static void setUpClass() {
  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testGettingDefaultValuesForAllSymbols() {
    System.out.println("Default inference parameter values.");
    try {
      final CycAccess cyc = TestUtils.getCyc();
      final InferenceParameterDescriptions descriptions = DefaultInferenceParameterDescriptions.getDefaultInferenceParameterDescriptions(cyc);
      for (final Field field : InferenceParametersSymbols.class.getFields()) {
        if (Modifier.isStatic(field.getModifiers()) && field.getType().equals(CycSymbol.class)) {
          try {
            final CycSymbol symbol = (CycSymbol) field.get(null);
            if (symbol.isKeyword()) {
              final InferenceParameter parameter = descriptions.get(symbol);
              assertNotNull("Couldn't find inference parameter for " + symbol, parameter);
              System.out.println(symbol + ": " + parameter.getDefaultValue());
            }
          } catch (IllegalArgumentException ex) {
            fail("Exception while testing " + field.getName());
          } catch (IllegalAccessException ex) {
            fail("Exception while testing " + field.getName());
          }
        }
      }
    } catch (CycConnectionException ex) {
      fail("Couldn't connect to Cyc.");
    }
  }

}
