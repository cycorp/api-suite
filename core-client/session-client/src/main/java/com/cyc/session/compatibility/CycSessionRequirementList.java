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
 * Project: Session Client
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

import com.cyc.session.CycSession;
import com.cyc.session.CycSessionManager;
import com.cyc.session.exception.SessionException;
import com.cyc.session.exception.SessionCommandException;
import com.cyc.session.exception.SessionCommunicationException;
import com.cyc.session.exception.SessionConfigurationException;
import com.cyc.session.exception.SessionInitializationException;
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
  public CompatibilityResults checkCompatibility(CycSession session) throws SessionCommunicationException, SessionCommandException {
    final List<String> errorMessages = new ArrayList<String>();
    boolean compatible = true;
    for (CycSessionRequirement requirement : this) {
      LOGGER.info("Checking {}", requirement.getClass());
      final CompatibilityResults reqResult = requirement.checkCompatibility(session);
      if (!reqResult.isCompatible()) {
        compatible = false;
        errorMessages.addAll(reqResult.getCompatibilityErrorMessages());
        for (String errMsg : reqResult.getCompatibilityErrorMessages()) {
          LOGGER.error("{}: {}", requirement.getClass().getSimpleName(), errMsg);
        }
        //LOGGER.warn("{}: {}", requirement.getClass().getSimpleName(), reqResult.checkCompatibility());
      } else {
        LOGGER.info("{}: {}", requirement.getClass().getSimpleName(), reqResult.isCompatible());
      }
    }
    if (compatible) {
      return new CompatibilityResultsImpl(compatible);
    } else {
      return new CompatibilityResultsImpl(compatible, errorMessages);
    }
  }
  
  public CompatibilityResults checkCompatibility() throws SessionCommunicationException, SessionCommandException, SessionConfigurationException, SessionInitializationException {
    return checkCompatibility(getSession());
  }
  
  @Override
  public void throwExceptionIfIncompatible(CycSession session) throws T, SessionCommunicationException, SessionCommandException {
    for (CycSessionRequirement<T> check : this) {
      check.throwExceptionIfIncompatible(session);
    }
  }
  
  public void throwExceptionIfIncompatible() throws T, SessionCommunicationException, SessionCommandException, SessionConfigurationException, SessionInitializationException {
    throwExceptionIfIncompatible(getSession());
  }
  
  public void throwRuntimeExceptionIfIncompatible(CycSession session) throws T {
    try {
      throwExceptionIfIncompatible(session);
    } catch (UnsupportedCycOperationException ex) {
      throw (T) ex;
    } catch (SessionException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  public void throwRuntimeExceptionIfIncompatible() throws T {
    try {
      throwExceptionIfIncompatible();
    } catch (UnsupportedCycOperationException ex) {
      throw (T) ex;
    } catch (SessionException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  // Private
  
  private CycSession getSession() throws SessionConfigurationException, SessionCommunicationException, SessionInitializationException {
    return CycSessionManager.getCurrentSession();
  }

}
