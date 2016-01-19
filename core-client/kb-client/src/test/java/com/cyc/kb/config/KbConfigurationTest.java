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
package com.cyc.kb.config;

/*
 * #%L
 * File: KbConfigurationTest.java
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

import com.cyc.kb.client.config.KbDefaultContext;
import com.cyc.kb.client.config.KbConfiguration;
import com.cyc.kb.DefaultContext;
import com.cyc.kb.client.Constants;
import com.cyc.session.exception.SessionRuntimeException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author nwinant
 */
public class KbConfigurationTest {

  @Test(expected = SessionRuntimeException.class)
  public void testGetDefaultContext_Unset() {
    System.out.println("testGetDefaultContext_Unset");
    //final DefaultContext expected = new KBAPIDefaultContext(null, null);
    KbConfiguration.getOptions().reset();
    final DefaultContext result = KbConfiguration.getDefaultContext();
    //System.out.println(result);
    //assertEquals(expected, result);
  }
  
  @Test
  public void testGetDefaultContext_Set() {
    System.out.println("testGetDefaultContext_Set");
    final DefaultContext emptyDefaults = new KbDefaultContext(null, null);
    final DefaultContext expected = new KbDefaultContext(Constants.uvMt(), Constants.inferencePSCMt());
    KbConfiguration.getOptions().setDefaultContext(expected);
    final DefaultContext result = KbConfiguration.getDefaultContext();
    System.out.println(result);
    assertEquals(expected, result);
    assertNotEquals(expected, emptyDefaults);
  }
  
}
