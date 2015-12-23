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
package com.cyc.kb;

import com.cyc.core.service.CoreServicesLoader;
import com.cyc.kb.spi.AssertionService;
import com.cyc.kb.spi.BinaryPredicateService;
import com.cyc.kb.spi.ContextService;
import com.cyc.kb.spi.FactService;
import com.cyc.kb.spi.FirstOrderCollectionService;
import com.cyc.kb.spi.KbCollectionService;
import com.cyc.kb.spi.KbFactoryServices;
import com.cyc.kb.spi.KbFunctionService;
import com.cyc.kb.spi.KbIndividualService;
import com.cyc.kb.spi.KbObjectService;
import com.cyc.kb.spi.KbPredicateService;
import com.cyc.kb.spi.KbTermService;
import com.cyc.kb.spi.RelationService;
import com.cyc.kb.spi.RuleService;
import com.cyc.kb.spi.SecondOrderCollectionService;
import com.cyc.kb.spi.SentenceService;
import com.cyc.kb.spi.SymbolService;
import com.cyc.kb.spi.VariableService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;

/*
 * #%L
 * File: KbFactoryTest.java
 * Project: KB Client
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

/**
 *
 * @author nwinant
 */
public class KbFactoryTest {
  
  // Tests
  
  @Test
  public void testGetKbFactoryServices() {
    KbFactoryServices result = KbFactory.getInstance();
    assertNotNull(result);
    assertNotNull(result.toString());
    assertEquals(CoreServicesLoader.getKbFactoryServices(), result);
  }
  
}
