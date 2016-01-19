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
import com.cyc.kb.spi.RelationService;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/*
 * #%L
 * File: RelationFactoryTest.java
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

/**
 *
 * @author nwinant
 */
public class RelationFactoryTest {
  
  // Tests
  
  @Test
  public void testGetInstance() throws Exception {
    RelationFactory instance = RelationFactory.getInstance();
    assertNotNull(instance);
    assertNotNull(instance.toString());
  }
  
  @Test
  public void testGetService() throws Exception {
    RelationService instance = RelationFactory.getInstance().getService();
    assertNotNull(instance);
    assertNotNull(instance.toString());
    assertEquals(CoreServicesLoader.getKbFactoryServices().getRelationService(), instance);
  }
  
}
