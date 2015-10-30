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
 * File: NotCycServerReleaseTypeRequirement.java
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

import com.cyc.session.CycServerReleaseType;
import com.cyc.session.CycSession;
import com.cyc.session.SessionCommandException;
import com.cyc.session.SessionCommunicationException;
import com.cyc.session.exception.UnsupportedCycOperationException;

/**
 *
 * @author nwinant
 * @param <T> The type of UnsupportedCycOperationException which should be thrown if incompatible.
 */
public class NotCycServerReleaseTypeRequirement<T extends UnsupportedCycOperationException> extends AbstractCycSessionRequirement<T> implements CycSessionRequirement<T> {
    
  final private CycServerReleaseType unacceptableReleaseType;
  
  public NotCycServerReleaseTypeRequirement(CycServerReleaseType unacceptableReleaseType, String msg) {
    this.unacceptableReleaseType = unacceptableReleaseType;
  }
  
  public NotCycServerReleaseTypeRequirement(CycServerReleaseType unacceptableReleaseType) {
    this(unacceptableReleaseType, "This feature is not supported in EnterpriseCyc.");
  }
  
  @Override
  public CompatibilityResults checkCompatibility(CycSession session) throws SessionCommunicationException, SessionCommandException {
    final CycServerReleaseType actualReleaseType = session.getServerInfo().getSystemReleaseType();
    final boolean compatible = !unacceptableReleaseType.equals(actualReleaseType);
    if (!compatible) {
      return new CompatibilityResultsImpl(compatible, getDefaultErrorMessage());
    }
    return new CompatibilityResultsImpl(compatible);
  }

}
