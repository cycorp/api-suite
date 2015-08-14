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
 * File: AbstractCycSessionRequirement.java
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
import com.cyc.session.SessionCommandException;
import com.cyc.session.SessionCommunicationException;
import com.cyc.session.exception.UnsupportedCycOperationException;

/**
 *
 * @author nwinant
 * @param <T> The type of UnsupportedCycOperationException which should be thrown if incompatible.
 */
abstract public class AbstractCycSessionRequirement<T extends UnsupportedCycOperationException> implements CycSessionRequirement<T> {
  
  final private String msg;
  
  protected AbstractCycSessionRequirement(String msg) {
    this.msg = msg;
  }
  
  protected AbstractCycSessionRequirement() {
    this(null);
  }
  
  
  // Public
  
  @Override
  public void testCompatibility(CycSession session) throws UnsupportedCycOperationException, SessionCommunicationException, SessionCommandException {
    final String errMsg = getErrorMessage(session);
    if (!isCompatible(session)) {
      if (errMsg != null) {
        throw new UnsupportedCycOperationException(errMsg);
      } else {
        throw new UnsupportedCycOperationException();
      }
    }
  }
  
  public String getErrorMessage(CycSession session) throws UnsupportedCycOperationException, SessionCommunicationException, SessionCommandException {
    return this.msg;
  }
}
