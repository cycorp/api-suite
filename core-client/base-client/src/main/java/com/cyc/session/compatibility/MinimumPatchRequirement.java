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
 * File: MinimumPatchRequirement.java
 * Project: Base Client
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

import com.cyc.base.exception.CycApiException;
import com.cyc.base.exception.CycConnectionException;
import com.cyc.baseclient.CycClient;
import com.cyc.baseclient.CycClientManager;
import com.cyc.session.CycServerReleaseType;
import com.cyc.session.CycSession;
import com.cyc.session.exception.SessionCommandException;
import com.cyc.session.exception.SessionCommunicationException;

/**
 *
 * @author nwinant
 */
public class MinimumPatchRequirement extends AbstractCycSessionRequirement implements CycSessionRequirement, CycClientRequirement {
  final private CycServerReleaseType serverType;
  final private int minimumPatchNumber;
  
  public MinimumPatchRequirement(String msg, CycServerReleaseType serverType, int minimumPatchNumber) {
    super(msg);
    this.serverType = serverType;
    this.minimumPatchNumber = minimumPatchNumber;
  }
  
  public MinimumPatchRequirement(CycServerReleaseType serverType, int minimumPatchNumber) {
    this(null, serverType, minimumPatchNumber);
  }
  
  @Override
  public CompatibilityResults checkCompatibility(CycClient client) throws CycApiException, CycConnectionException {
    try {
      final CycServerReleaseType actualServerType = client.getServerInfo().getSystemReleaseType();
      if (actualServerType == null) {
        return new CompatibilityResultsImpl(false, "Server returned null for CycServerReleaseType, which is invalid");
      }
      if (!serverType.equals(actualServerType)) {
        return new CompatibilityResultsImpl(true);
      }
      final boolean compatible = client.getServerInfo().getCycMinorRevisionNumber() >= minimumPatchNumber;
      if (compatible) {
        return new CompatibilityResultsImpl(compatible);
      } else {
        return new CompatibilityResultsImpl(compatible, 
                serverType + " must be at patch level " + minimumPatchNumber + " or higher, but is at " 
                        + client.getServerInfo().getCycMinorRevisionNumber());
      }
    } catch (SessionCommunicationException ex) {
      throw new CycConnectionException(ex);
    } catch (SessionCommandException ex) {
      throw new CycApiException(ex);
    }
  }
  
  @Override
  public CompatibilityResults checkCompatibility(CycSession session) throws SessionCommunicationException, SessionCommandException {
    try {
      return checkCompatibility(CycClientManager.getClientManager().fromSession(session));
    } catch (CycApiException ex) {
      throw new SessionCommandException(ex);
    } catch (CycConnectionException ex) {
      throw new SessionCommunicationException(ex);
    }
  }
}
