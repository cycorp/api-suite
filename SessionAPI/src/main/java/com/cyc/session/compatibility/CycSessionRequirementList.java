/*
 * Copyright 2015 Cycorp, Inc..
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

package com.cyc.session.compatibility;

/*
 * #%L
 * File: CycSessionRequirementList.java
 * Project: Session API Implementation
 * %%
 * Copyright (C) 2013 - 2015 Cycorp, Inc.
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

import com.cyc.session.CycSession;
import com.cyc.session.CycSessionManager;
import com.cyc.session.SessionApiException;
import com.cyc.session.SessionCommandException;
import com.cyc.session.SessionCommunicationException;
import com.cyc.session.SessionConfigurationException;
import com.cyc.session.SessionInitializationException;
import com.cyc.session.exception.UnsupportedCycOperationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nwinant
 * @param <T> The type of UnsupportedCycOperationException which should be thrown if incompatible.
 */
public class CycSessionRequirementList<T extends UnsupportedCycOperationException> extends ArrayList<CycSessionRequirement<T>> implements List<CycSessionRequirement<T>>, CycSessionRequirement {
  
  // Fields
  
  final private static Logger LOGGER = LoggerFactory.getLogger(CycSessionRequirementList.class);
  
  
  // Static
  
  public static CycSessionRequirementList fromList(CycSessionRequirement... compatibilityTests) {
    CycSessionRequirementList suite = new CycSessionRequirementList();
    suite.addAll(Arrays.asList(compatibilityTests));
    return suite;
  }
  
  
  // Basic test methods
  
  @Override
  public boolean isCompatible(CycSession session) throws SessionCommunicationException, SessionCommandException {
    boolean compatible = true;
    for (CycSessionRequirement check : this) {
      LOGGER.warn("Checking {}", check.getClass());
      if (!check.isCompatible(session)) {
        compatible = false;
      }
    }
    return compatible;
  }
  
  public boolean isCompatible() throws SessionCommunicationException, SessionCommandException, SessionConfigurationException, SessionInitializationException {
    return isCompatible(getSession());
  }
  
  @Override
  public void testCompatibility(CycSession session) throws T, SessionCommunicationException, SessionCommandException {
    for (CycSessionRequirement<T> check : this) {
      check.testCompatibility(session);
    }
  }
  
  public void testCompatibility() throws T, SessionCommunicationException, SessionCommandException, SessionConfigurationException, SessionInitializationException {
    testCompatibility(getSession());
  }
  
  public void testCompatibilityWithRuntimeException(CycSession session) throws T {
    try {
      testCompatibility(session);
    } catch (UnsupportedCycOperationException ex) {
      throw (T) ex;
    } catch (SessionApiException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  public void testCompatibilityWithRuntimeException() throws T {
    try {
      testCompatibility();
    } catch (UnsupportedCycOperationException ex) {
      throw (T) ex;
    } catch (SessionApiException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  // Private
  
  private CycSession getSession() throws SessionConfigurationException, SessionCommunicationException, SessionInitializationException {
    return CycSessionManager.getCurrentSession();
  }

}
