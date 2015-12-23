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
 * File: NotOpenCycRequirement.java
 * Project: Session Client
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
import com.cyc.session.exception.SessionCommandException;
import com.cyc.session.exception.SessionCommunicationException;
import com.cyc.session.exception.OpenCycUnsupportedFeatureException;

/**
 *
 * @author nwinant
 */
public class NotOpenCycRequirement extends NotCycServerReleaseTypeRequirement<OpenCycUnsupportedFeatureException> implements CycSessionRequirement<OpenCycUnsupportedFeatureException> {
  
  final static public NotOpenCycRequirement NOT_OPENCYC = new NotOpenCycRequirement();
  final static private String MSG = "This feature is not supported in OpenCyc.";
  
  public NotOpenCycRequirement(String msg) {
    super(CycServerReleaseType.OPENCYC, msg);
  }
  
  public NotOpenCycRequirement() {
    this(MSG);
  }
  
  @Override
  public void throwExceptionIfIncompatible(CycSession session) throws OpenCycUnsupportedFeatureException, SessionCommunicationException, SessionCommandException {
    CompatibilityResults results = checkCompatibility(session);
    if (!results.isCompatible()) {
      throw new OpenCycUnsupportedFeatureException(results.getExceptionMessage());
    }
  }
  
}
